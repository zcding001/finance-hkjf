package com.hongkun.finance.qdz.consumer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.vo.QdzAutoCreditorVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

/**
 * @Description : 推荐奖记录消费者handler
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.consumer.
 *          RecommendEarnJmsMessageHandler
 * @Author : yanbinghuang
 */
@Component("qdzAutoCreditorJmsMessageHandler")
public class QdzAutoCreditorJmsMessageHandler extends AbstractJmsMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(QdzAutoCreditorJmsMessageHandler.class);
	@Autowired
	QdzTaskJobFacade qdzTaskJobFacade;

	@Override
	public void setDestNameAndType() {
		super.setDestinations(QdzConstants.MQ_QUEUE_AUTO_CREDITOR_TRANSFER);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}

	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[" + QdzConstants.MQ_QUEUE_AUTO_CREDITOR_TRANSFER + "]中接收的消息：" + objectMessage);
		if (objectMessage.getObject() instanceof QdzAutoCreditorVo) {
			QdzAutoCreditorVo qdzAutoCreditorVo = (QdzAutoCreditorVo) objectMessage.getObject();
			ResponseEntity<?> responseEntity = qdzTaskJobFacade.sellAutoCreditorByMQ(qdzAutoCreditorVo);
			logger.info("[" + QdzConstants.MQ_QUEUE_AUTO_CREDITOR_TRANSFER + "]" + "用户标识："
					+ qdzAutoCreditorVo.getRegUserId() + "自动债权转让处理状态：" + responseEntity.getResStatus() + "处理结果信息："
					+ responseEntity.getResMsg());
		}
	}
}
