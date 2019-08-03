package com.hongkun.finance.contract.consumer;

import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.facade.ConInfoFacade;
import com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.List;

/**
 * @Description : 处理自动生成的合同消息队列
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.consumer.ConInfoSelfGenerateJmsMessageHandler
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Component("conInfoSelfGenerateJmsMessageHandler")
public class ConInfoSelfGenerateJmsMessageHandler extends AbstractJmsMessageHandler{

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ConInfoFacade conInfoFacade;

    @Override
    public void setDestNameAndType() {
        super.setDestinations(ContractConstants.MQ_QUEUE_SELF_CONINFO);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException{
        logger.info("从[" + ContractConstants.MQ_QUEUE_SELF_CONINFO + "]中接收的消息：" + objectMessage);
        if (objectMessage.getObject() instanceof ConInfoSelfGenerateDTO){
            ConInfoSelfGenerateDTO conInfoSelfGenerateDTO = (ConInfoSelfGenerateDTO) objectMessage.getObject();
            List<Integer> list = conInfoSelfGenerateDTO.getInvestIdList();
            Integer contractType = conInfoSelfGenerateDTO.getContractType();
            //判断参数的正确性
            if (list != null && list.size() >0 && contractType != null){
                conInfoFacade.generateContract(list,contractType);
            }
        }
    }
}
