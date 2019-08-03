package com.hongkun.finance.monitor.consumer;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.monitor.model.MonitorActionLog;
import com.hongkun.finance.monitor.service.MonitorActionLogService;
import com.hongkun.finance.user.constants.UserConstants;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * @Description   : 消费管理员操作日志
 * @Project       : finance-monitor-service
 * @Program Name  : com.hongkun.finance.monitor.consumer.MonitorActionLogHandler.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Component("monitorActionLogHandler")
public class MonitorActionLogHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(MonitorActionLogHandler.class);
	
	@Autowired
	private MonitorActionLogService monitorActionLogService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(UserConstants.MQ_QUEUE_ACTION_LOG);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	@Override
	public void handlerMapMessage(MapMessage mapMessage) throws JMSException {
		logger.info("消息体：{}", mapMessage);
		Long login = (Long) mapMessage.getObject("login");
		String action = String.valueOf(mapMessage.getObject("action"));
		Integer type = (Integer) mapMessage.getObject("type");
		MonitorActionLog monitorActionLog = new MonitorActionLog();
		monitorActionLog.setLogin(login);
		monitorActionLog.setAction(action);
		logger.info(action);
		monitorActionLog.setType(type);
		try{
            monitorActionLogService.insertMonitorActionLog(monitorActionLog);
		}catch(Exception e){
		    logger.error("操作日志解析失败, 用户: {}, 操作: {}", monitorActionLog.getLogin(), monitorActionLog.getAction(), e);
		    throw new JMSException("操作日志解析失败[" + mapMessage.getObject("className") + "#" + mapMessage.getObject("methodName") + "]");
		}
		
	}

}
