package com.hongkun.finance.user.consumer;

import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * 注册用户推荐人信息维护。从鸿坤金服系统获取当前注册用户的推荐人信息
 * 并持久化到鸿坤金服的系统中
 * @author zc.ding
 * @create 2018/7/13
 */
@Component("regUserRecommendRegistJmsMessageHandler")
public class RegUserRecommendRegistJmsMessageHandler extends AbstractJmsMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegUserRecommendRegistJmsMessageHandler.class);

    @Autowired
    private RegUserFriendsService regUserFriendsService;
    @Autowired
    private RegUserService regUserService;
    @Autowired
    private UserFacade userFacade;
    
    @Override
    public void setDestNameAndType() {
        super.setDestinations(UserConstants.MQ_QUEUE_USER_RECOMMEND_REGIST);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    /**
     * 处理鸿坤金服推荐人注册信息
     */
    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info("从[{}]中接收的消息：{}", UserConstants.MQ_QUEUE_USER_RECOMMEND_REGIST, objectMessage);
        Integer regUserId = (Integer) objectMessage.getObject();
        RegUser regUser = BaseUtil.getRegUser(regUserId, () -> regUserService.findRegUserById(regUserId));
        String msg = JedisClusterUtils.get(UserConstants.KEY_PREFIX_HKJF_USER_STATE + regUser.getLogin());
        logger.info("用户: {}, 鸿坤金服中推荐人信息: {}", regUserId, msg);
        if(StringUtils.isNotBlank(msg)){
            JSONObject json = JSONObject.parseObject(msg);
            //鸿坤金服推荐人信息存在
            if(json.getLong("login") != null && json.getInteger("type") != null && StringUtils.isNotBlank(json.getString("pwd"))){
                if(!PaymentUtil.checkSignByMd5(json.toJSONString())){
                    logger.error("用户: {}, 验证签名失败.", regUserId);
                    return;
                }
                RegUser recommendUser = new RegUser();
                recommendUser.setLogin(json.getLong("login"));
                recommendUser.setType(json.getInteger("type"));
                recommendUser.setPasswd(json.getString("pwd"));
                ResponseEntity<?> result = userFacade.insertRecommendUserForRegist(regUserId, recommendUser);
                if(result.validSuc()){
                    JedisClusterUtils.delete(UserConstants.KEY_PREFIX_HKJF_USER_STATE + regUser.getLogin());
                }
            }
        }
    }
}
