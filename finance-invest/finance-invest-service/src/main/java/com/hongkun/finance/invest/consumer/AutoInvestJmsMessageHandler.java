package com.hongkun.finance.invest.consumer;

import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * 自动投资消息
 *
 * @author zc.ding
 * @create 2018/8/28
 */
@Component("autoInvestJmsMessageHandler")
public class AutoInvestJmsMessageHandler extends AbstractJmsMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AutoInvestJmsMessageHandler.class);
    @Autowired
    private BidInvestFacade bidInvestFacade;
            
    @Override
    public void setDestNameAndType() {
        super.setDestinations(InvestConstants.MQ_QUEUE_AUTO_INVEST);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    @Override
    public void handlerTextMessage(TextMessage textMessage) throws JMSException {
        logger.info("自动投投资标识：{}", textMessage);
        String autoInvestFlag = PropertiesHolder.getProperty("auto_invest_flag");
        logger.info("自动投资, 自动投资标识: {}",  autoInvestFlag);
        if (StringUtils.isNoneBlank(autoInvestFlag) && !"1".equals(autoInvestFlag)) {
            return;
        }
        if(StringUtils.isNotBlank(textMessage.getText())){
            this.bidInvestFacade.autoInvest();
        }
    }
}
