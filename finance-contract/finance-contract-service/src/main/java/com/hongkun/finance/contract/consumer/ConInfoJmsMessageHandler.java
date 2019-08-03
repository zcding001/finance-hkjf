package com.hongkun.finance.contract.consumer;

import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description   : 根据投资记录id生成合同MQ消费
 * @Project       : finance-contract-service
 * @Program Name  : com.hongkun.finance.contract.service.consumer.ConInfoJmsMessageHandler.java
 * @Author        : xuhuiliu@hongkun.com.cn 刘旭辉
 */
@Component("conInfoJmsMessageHandler")
public class ConInfoJmsMessageHandler extends AbstractJmsMessageHandler {
	
	private Logger logger = LoggerFactory.getLogger(ConInfoJmsMessageHandler.class);

	@Autowired
	private ConInfoFacade conInfoFacade;

	@Override
	public void setDestNameAndType() {
		super.setDestinations(ContractConstants.MQ_QUEUE_CONINFO);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}
	
	
	/**
	 * 处理mq消息，执行插入合同操作
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[" + ContractConstants.MQ_QUEUE_CONINFO + "]中接收的消息：" + objectMessage);
		List<Integer> list = new ArrayList<>();
		if(objectMessage.getObject() instanceof Integer) {
			Integer model = (Integer)objectMessage.getObject();
			logger.info("ConInfoJmsMessageHandler, 消费单个投资记录, 投资记录id: {}", model);
			list.add(model);
		}else if(objectMessage.getObject() instanceof List<?>){
			List<Integer> result = (List<Integer>)objectMessage.getObject();
			logger.info("ConInfoJmsMessageHandler, 消费批量投资记录, 投资记录id集合: {}", JSON.toJSON(result));
			if(result != null && result.size() > 0){
				list.addAll(result);
			}
		}
		//集合有记录才进行处理
		if (list.size() > 0){
			conInfoFacade.generateContract(list);
		}
	}
}
