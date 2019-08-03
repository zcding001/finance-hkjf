package com.hongkun.finance.contract.consumer;

import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.facade.InvestPreserveFacade;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.contract.service.InvestPreserveService;
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
 * @Description   : 用户投资数据进行保全
 * @Project       : finance-contract-service
 * @Program Name  : com.hongkun.finance.contract.consumer.InvestPreserveHandler.java
 * @Author        : binliang@hongkun.com 梁彬
 */
@Component("investPreserveHandler")
public class InvestPreserveHandler extends AbstractJmsMessageHandler{

	private static final Logger logger = LoggerFactory.getLogger(InvestPreserveHandler.class);
	
	@Autowired
	private InvestPreserveFacade investPreserveFacade;
	
	@Override
	public void setDestNameAndType() {
		super.setDestinations(ContractConstants.MQ_QUEUE_INVEST_PRESERVE);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info("investPreserveHandler 从[" + ContractConstants.MQ_QUEUE_INVEST_PRESERVE + "]中接收的消息：" + objectMessage);
        if(objectMessage.getObject() instanceof InvestPreServeTemplate) {
        	InvestPreServeTemplate model = (InvestPreServeTemplate)objectMessage.getObject();
        	investPreserveFacade.sendToAncunInvestData(model);
        }else if(objectMessage.getObject() instanceof List<?>){
           
        }
	}

}
