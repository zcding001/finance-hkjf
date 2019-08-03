package com.hongkun.finance.sms.consumer;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.service.SmsEmailMsgService;
import com.hongkun.finance.sms.utils.MailSenderUtil;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.List;
import java.util.Optional;

/**
 * @Described	: 邮件消息存储handler 
 * @project		: com.yirun.framework.sms.consumer.MsgEmailJmsMessageHandler
 * @author 		: zc.ding
 * @date 		: 2017年3月13日
 */
@Component("msgEmailJmsMessageHandler")
public class MsgEmailJmsMessageHandler extends AbstractJmsMessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(MsgEmailJmsMessageHandler.class);
	
	@Autowired
	private SmsEmailMsgService smsEmailMsgService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(SmsConstants.MQ_QUEUE_MSG_EMAIL);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 处理邮件消息通知
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[{}]中接收的消息：{}", SmsConstants.MQ_QUEUE_MSG_EMAIL, objectMessage);
		if(objectMessage.getObject() instanceof SmsEmailMsg) {
			SmsEmailMsg model = (SmsEmailMsg)objectMessage.getObject();
			if(StringUtils.isNotEmpty(model.getEmail())){
//				保存数据到数据库
				this.smsEmailMsgService.insertSmsEmailMsg(model);
			}
		}else if(objectMessage.getObject() instanceof List<?>){
            Optional.ofNullable((List<SmsEmailMsg>) objectMessage.getObject()).ifPresent(l -> this.smsEmailMsgService
                    .insertSmsEmailMsgBatch(l, l.size()));
		}
	}
	
	/**
	 *  @Description    : 执行发送邮件的操作
	 *  @Method_Name    : getSmsEmailMsg
	 *  @param smsEmailMsg
	 *  @return
	 *  @return         : SmsEmailMsg
	 *  @Creation Date  : 2017年6月9日 下午4:07:02 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private SmsEmailMsg getSmsEmailMsg(SmsEmailMsg smsEmailMsg){
		logger.info("要发送的邮件消息：{}", smsEmailMsg.getMsg());
		boolean success = MailSenderUtil.send(
				smsEmailMsg.getEmail(), 
				smsEmailMsg.getTitle(),
				smsEmailMsg.getMsg()
				);
//		初始化用于持久化的对象
		smsEmailMsg.setState(success ? 1 :0);
		return smsEmailMsg;
	}
}
