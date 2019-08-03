package com.hongkun.finance.sms.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.sms.utils.SingletonClient;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.List;
import java.util.Optional;
/**
 * @Described	: 短信消息存储接口
 * @project		: com.yirun.framework.sms.consumer.MsgTelJmsMessageHandler
 * @author 		: zc.ding
 * @date 		: 2017年3月13日
 */
@Component("MsgTelJmsMessageHandler")
public class MsgTelJmsMessageHandler extends AbstractJmsMessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(MsgTelJmsMessageHandler.class);
	
	@Autowired
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private RosInfoService rosInfoService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(SmsConstants.MQ_QUEUE_MSG_TEL);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 处理短信消息
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[" + SmsConstants.MQ_QUEUE_MSG_TEL + "]中接收的消息：" + objectMessage);
		if(objectMessage.getObject() instanceof SmsTelMsg) {
			SmsTelMsg model = (SmsTelMsg)objectMessage.getObject();
            SmsTelMsg stl = getSmsTelMsg(model);
            if(stl != null){
                this.smsTelMsgService.insertSmsTelMsgOnly(stl);
            }
		}else if(objectMessage.getObject() instanceof List<?>){
			Optional.ofNullable((List<SmsTelMsg>) objectMessage.getObject())
					.ifPresent(l -> l.forEach(o -> {
                        SmsTelMsg stl = getSmsTelMsg(o);
                        if(stl != null){
                            this.smsTelMsgService.insertSmsTelMsgOnly(stl);
                        }
                    }));
		}
	}
	
	/**
	 *  @Description    : 执行发送短信的消息并维护属性
	 *  @Method_Name    : getSmsTelMsg
	 *  @param smsTelMsg
	 *  @return         : SmsTelMsg
	 *  @Creation Date  : 2017年6月9日 下午3:04:11 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private SmsTelMsg getSmsTelMsg(SmsTelMsg smsTelMsg){
		RosterType rosterType = RosterType.SMS_CTL;
		//判断是不是还款提醒短信
		if(smsTelMsg.getBusType() != null) {
			if(smsTelMsg.getBusType().intValue() == RosterType.SMS_REAPY_NOTICE.getValue()) {
				rosterType = RosterType.SMS_REAPY_NOTICE;
			}
		}
		if(smsTelMsg.getRegUserId() != null && this.rosInfoService.validateRoster(smsTelMsg.getRegUserId(), rosterType, RosterFlag.BLACK)) {
			logger.info("用户：{}，没有{}类型的短信通知权限", smsTelMsg.getRegUserId(), rosterType);
			return null;
		}
		int result = SingletonClient.getClient().sendSms(String.valueOf(smsTelMsg.getTel()), smsTelMsg.getMsg());
		smsTelMsg.setState(result > 0 ? 1 : 0);
		smsTelMsg.setInfo(String.valueOf(result));
		return smsTelMsg;
	}
	
}
