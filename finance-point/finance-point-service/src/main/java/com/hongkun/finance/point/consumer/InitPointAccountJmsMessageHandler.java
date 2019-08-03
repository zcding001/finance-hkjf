package com.hongkun.finance.point.consumer;

import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.service.PointAccountService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * 初始化积分账户
 * @author zc.ding
 * @create 2018/7/13
 */
@Component
public class InitPointAccountJmsMessageHandler extends AbstractJmsMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(InitPointAccountJmsMessageHandler.class);
    
    @Autowired
    private PointAccountService pointAccountService;
    
    @Override
    public void setDestNameAndType() {
        super.setDestinations(PointConstants.MQ_QUEUE_INIT_POINT_ACCOUNT);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }
    
    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info("从[{}]中接收的消息：{}", PointConstants.MQ_QUEUE_INIT_POINT_ACCOUNT, objectMessage);
        Integer regUserId = (Integer) objectMessage.getObject();
        if(regUserId != null){
            PointAccount pointAccount = new PointAccount();
            pointAccount.setRegUserId(regUserId);
            pointAccountService.insertPointAccount(pointAccount);
        }
    }
}
