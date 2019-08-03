package com.hongkun.finance.user.consumer;

import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.AppLoginLog;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.AppLoginLogService;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Description : 处理App端登录之后的log操作
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.consumer.RegUserAppLoginLogJmsMessageHandler
 * @Author : zhongpingtang@hongkun.com.cn
 */
@Component("RegUserVipJmsMessageHandler")
public class RegUserVipJmsMessageHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(RegUserVipJmsMessageHandler.class);

	@Autowired
	private RegUserService regUserService;

	@Override
	public void setDestNameAndType() {
		super.setDestinations(UserConstants.MQ_QUEUE_USER_VIP);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 用户升级为VIP
	 */
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("用户升级VIP, 从[{}]中接收的消息：{}", UserConstants.MQ_QUEUE_APP_LOGIN_LOG, objectMessage);
		if(objectMessage.getObject() instanceof RegUser) {
			RegUser regUser = (RegUser)objectMessage.getObject();
			this.regUserService.updateRegUser(regUser);
		}else if(objectMessage.getObject() instanceof List<?>){
            Optional.ofNullable((List<RegUser>) objectMessage.getObject())
                    .ifPresent(l -> l.forEach(o -> {
                        this.regUserService.updateRegUser(o);
                    }));
        }
	}
}
