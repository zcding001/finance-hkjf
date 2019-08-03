package com.hongkun.finance.monitor.consumer;

import com.hongkun.finance.monitor.constants.MonitorConstants;
import com.hongkun.finance.monitor.model.MonitorOperateRecord;
import com.hongkun.finance.monitor.service.MonitorOperateRecordService;
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
 * @Description   : 用户第三方操作记录
 * @Project       : finance-monitor-service
 * @Program Name  : com.hongkun.finance.monitor.consumer.MonitorOperateRecordHandler.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Component("monitorOperateRecordHandler")
public class MonitorOperateRecordHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(MonitorOperateRecordHandler.class);
	
	@Autowired
	private MonitorOperateRecordService monitorOperateRecordService;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(MonitorConstants.MQ_QUEUE_MONITOR_OPERATE_RECORD);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info("从[" + MonitorConstants.MQ_QUEUE_MONITOR_OPERATE_RECORD + "]中接收的消息：" + objectMessage);
        if(objectMessage.getObject() instanceof MonitorOperateRecord) {
            MonitorOperateRecord model = (MonitorOperateRecord)objectMessage.getObject();
            this.monitorOperateRecordService.insertMonitorOperateRecord(model);
        }else if(objectMessage.getObject() instanceof List<?>){
            List<MonitorOperateRecord> list = (List<MonitorOperateRecord>) objectMessage.getObject();
            this.monitorOperateRecordService.insertMonitorOperateRecordBatch(list);
        }
	}

}
