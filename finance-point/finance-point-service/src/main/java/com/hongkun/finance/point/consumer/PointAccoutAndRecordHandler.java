package com.hongkun.finance.point.consumer;

import com.hongkun.finance.point.constants.PointConstants;
import com.yirun.framework.jms.KeyAssignedAware;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.KeyAssignedJmsMessageHandler;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * @Description : 处理从消息队列中的消息进行处理
 *              此类必须存在，负责积分消费者无法进到容器中
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.consumer.PointAccoutAndRecordHandler
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Component
public class PointAccoutAndRecordHandler extends KeyAssignedJmsMessageHandler {

    @Override
    public void setDestNameAndType() {
        super.setDestinations(PointConstants.MQ_QUEUE_POINT_RECORD);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }


    /**
     * 指定key的规则
     * @param message
     * @return
     */
    @Override
    protected Object tryToFindKey(Message message) {
        if (message==null||!(message instanceof ObjectMessage)) {
             return null;
        }
        ObjectMessage objectMessage = (ObjectMessage) message;
        //规定积分模块的信息必须实现KeyAssignedAware
        KeyAssignedAware assignedAware = null;

        try {
            assignedAware = (KeyAssignedAware) objectMessage.getObject();
        } catch (JMSException e) {
            //do nothing，it must invildate data.
        }
        String assignedKey = assignedAware.getAssignedKey();
        return assignedKey;
    }
}
