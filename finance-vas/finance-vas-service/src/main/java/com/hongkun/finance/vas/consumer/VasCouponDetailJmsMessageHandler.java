package com.hongkun.finance.vas.consumer;

import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.facade.CouponFacade;
import com.hongkun.finance.vas.model.dto.CouponDetailMqDTO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
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
 * @Description : 系统补发卡券handler
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.consumer.VasCouponDetailJmsMessageHandler
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Component("vasCouponDetailJmsMessageHandler")
public class VasCouponDetailJmsMessageHandler extends AbstractJmsMessageHandler{

    private final Logger logger = LoggerFactory.getLogger(VasCouponDetailJmsMessageHandler.class);

    @Autowired
    private CouponFacade couponFacade;

    @Override
    public void setDestNameAndType() {
        super.setDestinations(VasCouponConstants.MQ_QUEUE_VAS_COUPON_DETAIL);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info( "从[{}]中接收的消息： {}", VasCouponConstants.MQ_QUEUE_VAS_COUPON_DETAIL, objectMessage.getObject());
        if (objectMessage.getObject() instanceof CouponDetailMqDTO){
            CouponDetailMqDTO couponDetailMqDTO = (CouponDetailMqDTO) objectMessage.getObject();
            logger.info("VasCouponDetailJmsMessageHandler, 补发卡券, 接收的参数: {}",couponDetailMqDTO.toString());
            couponFacade.reissueUserCouponDetail(couponDetailMqDTO.getRegUserId(),couponDetailMqDTO
                    .getCouponProductId(),couponDetailMqDTO.getReason());
        }else if (objectMessage.getObject() instanceof List<?>){
            List<CouponDetailMqDTO> list = (List<CouponDetailMqDTO>) objectMessage.getObject();
            for (CouponDetailMqDTO couponDetailMqDTO:list){
                couponFacade.reissueUserCouponDetail(couponDetailMqDTO.getRegUserId(),couponDetailMqDTO
                        .getCouponProductId(),couponDetailMqDTO.getReason());
            }
        }

    }
}
