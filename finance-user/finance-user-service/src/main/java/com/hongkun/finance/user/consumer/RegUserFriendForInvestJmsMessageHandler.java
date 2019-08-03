package com.hongkun.finance.user.consumer;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * @Description   : 好友关系维护
 * @Project       : finance-user-service
 * @Program Name  : com.hongkun.finance.user.consumer.RegUserFriendJmsMessageHandler.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Component("RegUserFriendForInvestJmsMessageHandler")
public class RegUserFriendForInvestJmsMessageHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(RegUserFriendForInvestJmsMessageHandler.class);
	
	@Autowired
	private RegUserFriendsService regUserFriendsService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(UserConstants.MQ_QUEUE_USER_FRIEND_INVEST);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 处理邮件消息通知
	 */
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[{}]中接收的消息：{}", SmsConstants.MQ_QUEUE_MSG_EMAIL, objectMessage);
		if(objectMessage.getObject() instanceof RegUserFriends) {
			regUserFriendsService.updateRegUserFriends((RegUserFriends)objectMessage.getObject());
		}else if(objectMessage.getObject() instanceof List<?>){
			List<Integer> regUserIds = (List<Integer>) objectMessage.getObject();
			if(CommonUtils.isNotEmpty(regUserIds)){
				regUserFriendsService.dealRegUserFriendsForInvest(regUserIds);
			}
		}
	}
}
