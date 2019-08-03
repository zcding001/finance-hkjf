package com.hongkun.finance.sms.consumer;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * @Described	: 站内信消息handler
 * @project		: com.yirun.framework.sms.consumer.MsgWebJmsMessageHandler
 * @author 		: zc.ding
 * @date 		: 2017年3月13日
 */
@Component("msgWebJmsMessageHandler")
public class MsgWebJmsMessageHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(MsgWebJmsMessageHandler.class);
	@Autowired
	private SmsWebMsgService SmsWebMsgService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(SmsConstants.MQ_QUEUE_MSG_WEB);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[" + SmsConstants.MQ_QUEUE_MSG_WEB + "]中接收的消息：" + objectMessage);
		if(objectMessage.getObject() instanceof SmsWebMsg) {
			SmsWebMsg model = (SmsWebMsg)objectMessage.getObject();
			this.SmsWebMsgService.insertSmsWebMsg(model);
		}else if(objectMessage.getObject() instanceof List<?>){
			this.SmsWebMsgService.insertSmsWebMsgBatch((List<SmsWebMsg>)objectMessage.getObject());
		}
	}
}
