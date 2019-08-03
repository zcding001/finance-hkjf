package com.hongkun.finance.user.consumer;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;

import com.hongkun.finance.user.service.RegUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * @Description   : 好友关系维护
 * @Project       : finance-user-service
 * @Program Name  : com.hongkun.finance.user.consumer.RegUserFriendJmsMessageHandler.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Component("RegUserFriendForRegistJmsMessageHandler")
public class RegUserFriendForRegistJmsMessageHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(RegUserFriendForRegistJmsMessageHandler.class);
	
	@Autowired
	private RegUserFriendsService regUserFriendsService;
	@Autowired
	private RegUserService regUserService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(UserConstants.MQ_QUEUE_USER_FRIEND_REGIST);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 处理好友推荐消息通知
	 */
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[{}]中接收的消息：{}", UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, objectMessage);
		Integer regUserId = (Integer) objectMessage.getObject();
		//给用户插入默认分组、创建一级、二级好友关系
		regUserFriendsService.initUserFriends(regUserId);
	}

	/**
	*  处理鸿坤金服推荐人关系，并维护推荐关系
	*  @Method_Name             ：handlerMapMessage
	*  @param mapMessage
	*  @return void
	*  @Creation Date           ：2018/7/13
	*  @Author                  ：zc.ding@foxmail.com
	*/
    @Override
    public void handlerMapMessage(MapMessage mapMessage) throws JMSException {
        logger.info("从[{}]中接收的消息：{}", UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, mapMessage);
        int registRegUserId = mapMessage.getInt("registRegUserId");
        int recommendRegUserId = mapMessage.getInt("recommendRegUserId");
        //给用户插入默认分组、创建一级、二级好友关系
        regUserFriendsService.initUserFriends(recommendRegUserId);
//        this.regUserService.updateCommendNo(registRegUserId + "", mapMessage.getString("investNo"));
    }
}
