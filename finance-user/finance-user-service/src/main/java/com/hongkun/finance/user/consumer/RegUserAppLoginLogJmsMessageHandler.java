package com.hongkun.finance.user.consumer;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.AppLoginLog;
import com.hongkun.finance.user.service.AppLoginLogService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.Date;

/**
 * @Description : 处理App端登录之后的log操作
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.consumer.RegUserAppLoginLogJmsMessageHandler
 * @Author : zhongpingtang@hongkun.com.cn
 */
@Component("RegUserAppLoginLogJmsMessageHandler")
public class RegUserAppLoginLogJmsMessageHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(RegUserAppLoginLogJmsMessageHandler.class);

	@Autowired
	private AppLoginLogService appLoginLogService;

	@Override
	public void setDestNameAndType() {
		super.setDestinations(UserConstants.MQ_QUEUE_APP_LOGIN_LOG);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	/**
	 * 处理邮件消息通知
	 */
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[{}]中接收的消息：{}", UserConstants.MQ_QUEUE_APP_LOGIN_LOG, objectMessage);
		if(objectMessage.getObject() instanceof AppLoginLog) {
			AppLoginLog unhandLog = (AppLoginLog) objectMessage.getObject();
			AppLoginLog queryLog = new AppLoginLog();
			queryLog.setRegUserId(unhandLog.getRegUserId());
            logger.info("用户从App端登录，时间：{},用户Id {},详细信息", new Date(), unhandLog.getRegUserId(), unhandLog);
            try{
                if (appLoginLogService.findAppLoginLogCount(queryLog)>0) {
                    appLoginLogService.updateAppLoginLogByUserId(unhandLog);
                    return;
                }
                appLoginLogService.insertAppLoginLog(unhandLog);
            }catch(Exception e){
                logger.error("用户从App端登录，时间：{},用户Id {},详细信息", new Date(), unhandLog.getRegUserId(), unhandLog);
                throw new JMSException("");
            }
		}
	}
}
