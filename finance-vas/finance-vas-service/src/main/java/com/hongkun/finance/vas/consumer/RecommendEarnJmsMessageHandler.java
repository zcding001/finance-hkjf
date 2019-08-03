package com.hongkun.finance.vas.consumer;

import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.RecommendEarnBuild;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.yirun.framework.core.model.ResponseEntity;
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
 * @Description : 推荐奖记录消费者handler
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.consumer.
 *          RecommendEarnJmsMessageHandler
 * @Author : yanbinghuang
 */
@Component("recommendEarnJmsMessageHandler")
public class RecommendEarnJmsMessageHandler extends AbstractJmsMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(RecommendEarnJmsMessageHandler.class);

	@Autowired
	VasBidRecommendEarnService vasBidRecommendEarnService;
	@Autowired
	RecommendEarnFacade recommendEarnFacade;

	@Override
	public void setDestNameAndType() {
		super.setDestinations(VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD);
		super.setDestinationType(DestinationType.QUEUE.getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info("从[" + VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD + "]中接收的消息：" + objectMessage);
		if (objectMessage.getObject() instanceof RcommendEarnInfo) {
			RcommendEarnInfo rcommendEarnInfo = (RcommendEarnInfo) objectMessage.getObject();
			try{
    			ResponseEntity<?> responseEntity = recommendEarnFacade.buildQdzRecommendEarn(rcommendEarnInfo);
    			logger.info("[" + VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD + "]" + "被推荐人ID："
    					+ rcommendEarnInfo.getRegUserId() + "推荐奖处理状态:" + responseEntity.getResStatus() + "处理结果信息:"
    					+ responseEntity.getResMsg());
			}catch(Exception e){
			    logger.error("[" + VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD + "]" + "被推荐人ID："
                        + rcommendEarnInfo.getRegUserId() +"钱袋子推荐奖生成失败:",e);
			}
		} else if (objectMessage.getObject() instanceof List<?>) {
			List<RcommendEarnInfo> list = (List<RcommendEarnInfo>) objectMessage.getObject();
			for (RcommendEarnInfo rcommendEarnInfo : list) {
			    try{
    				ResponseEntity<?> responseEntity =  recommendEarnFacade.buildQdzRecommendEarn(rcommendEarnInfo);
    				logger.info("[" + VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD + "]" + "被推荐人ID:"
    						+ rcommendEarnInfo.getRegUserId() + "推荐奖处理状态:" + responseEntity.getResStatus() + "处理结果信息:"
    						+ responseEntity.getResMsg());
			    }catch(Exception e){
			        logger.error("[" + VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD + "]" + "被推荐人ID："
	                        + rcommendEarnInfo.getRegUserId() +"钱袋子推荐奖生成失败:",e);
			    }
			}
		} else if (objectMessage.getObject() instanceof RecommendEarnBuild) {
			RecommendEarnBuild recommendEarnBuild = (RecommendEarnBuild) objectMessage.getObject();
			recommendEarnFacade.createRecommendRecordInfo(recommendEarnBuild);
		}
	}
}
