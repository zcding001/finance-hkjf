package com.hongkun.finance.sms.consumer;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsAppMsgPush;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsAppMsgPushService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.List;

/**
 * @Description : 消息推送handler
 * @Project : framework
 * @Program Name  : com.hongkun.finance.sms.consumer.MsgAppPushJmsMessageHandler
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Component("msgAppPushJmsMessageHandler")
public class MsgAppPushJmsMessageHandler extends AbstractJmsMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MsgAppPushJmsMessageHandler.class);
    @Autowired
    private SmsAppMsgPushService smsAppMsgPushService;

    @Override
    public void setDestNameAndType() {
        super.setDestinations(SmsConstants.MQ_QUEUE_MSG_PUSH);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info("从[" + SmsConstants.MQ_QUEUE_MSG_PUSH + "]中接收的消息：" + objectMessage);
        if(objectMessage.getObject() instanceof SmsWebMsg) {
            SmsAppMsgPush model = (SmsAppMsgPush)objectMessage.getObject();
            this.smsAppMsgPushService.addAppMsgPush(model);
        }else if(objectMessage.getObject() instanceof List<?>){
            for (SmsAppMsgPush model:(List<SmsAppMsgPush>)objectMessage.getObject()){
                this.smsAppMsgPushService.addAppMsgPush(model);
            }
        }
    }
}
