package com.hongkun.finance.loan.consumer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.MakeLoanFacade;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * 
 * @Description   : 生成还款计划和回款计划--消息消费
 * @Project       : finance-loan-service
 * @Program Name  : com.hongkun.finance.loan.consumer.BidRepayPlanAndReceiptPlanHandler.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Component("bidRepayPlanAndReceiptPlanHandler")
public class BidRepayPlanAndReceiptPlanHandler extends AbstractJmsMessageHandler {

	private Logger logger = LoggerFactory.getLogger(BidRepayPlanAndReceiptPlanHandler.class);
	
	@Autowired
	private MakeLoanFacade makeLoanFacade;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(RepayConstants.MQ_QUEUE_REPAYANDRECEIPTPLAN);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[{}]中接收的消息, bidInfoId: {}",RepayConstants.MQ_QUEUE_REPAYANDRECEIPTPLAN,objectMessage.getObject());
		Integer bidInfoId = (Integer) objectMessage.getObject();
		if(bidInfoId!=null&&bidInfoId>0){
			makeLoanFacade.initRepayPlans(bidInfoId);
		}
	}
}
