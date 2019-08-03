package com.hongkun.finance.test.monitor;

import com.hongkun.finance.monitor.constants.MonitorConstants;
import com.hongkun.finance.monitor.model.MonitorOperateRecord;
import com.hongkun.finance.test.BaseTest;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/4/26
 */
public class TestMonitor extends BaseTest {
    
    @Autowired
    private JmsService jmsService;
    
    @Test
    public void testSendMonitorOperateLog(){
        MonitorOperateRecord mor = new MonitorOperateRecord();
        mor.setId(1);
        mor.setRegUserId(1);
        mor.setOperateDesc("测试");
        mor.setOperateState(1);
        mor.setOperateType(1);
        jmsService.sendMsg(MonitorConstants.MQ_QUEUE_MONITOR_OPERATE_RECORD, DestinationType.QUEUE, mor, JmsMessageType.OBJECT);
    }
}
