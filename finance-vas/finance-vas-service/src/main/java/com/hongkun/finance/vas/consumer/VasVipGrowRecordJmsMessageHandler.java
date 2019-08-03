package com.hongkun.finance.vas.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.service.VasVipGrowRecordService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.handler.AbstractJmsMessageHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.List;
import java.util.Objects;

/**
 * @Description : 用户成长值记录handler
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.service.consumer.VasVipGrowRecordJmsMessageHandler
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@Component("vasVipGrowRecordJmsMessageHandler")
public class VasVipGrowRecordJmsMessageHandler extends AbstractJmsMessageHandler {
    private final Logger logger = LoggerFactory.getLogger(VasVipGrowRecordJmsMessageHandler.class);
    @Autowired
    private VasVipGrowRecordService vasVipGrowRecordService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private FinPaymentRecordService finPaymentRecordService;

    @Override
    public void setDestNameAndType() {
        super.setDestinations(VasVipConstants.MQ_QUEUE_VAS_VIP_GROW_RECORD);
        super.setDestinationType(DestinationType.QUEUE.getValue());
    }

    @Override
    public void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
        logger.info( "从[{}]中接收的消息： {}", VasVipConstants.MQ_QUEUE_VAS_VIP_GROW_RECORD, objectMessage);
        if (objectMessage.getObject() instanceof VasVipGrowRecordMqVO) {
            VasVipGrowRecordMqVO vasVipGrowRecordMqVO = (VasVipGrowRecordMqVO) objectMessage.getObject();
            //查询用户信息
            RegUser regUser = regUserService.findRegUserById(vasVipGrowRecordMqVO.getUserId());
            if (whetherFirstRecharge(vasVipGrowRecordMqVO)){
                vasVipGrowRecordService.insertVasVipGrowRecord(regUser.getCreateTime(),vasVipGrowRecordMqVO);
            }
        } else if (objectMessage.getObject() instanceof List<?>) {
            List<VasVipGrowRecordMqVO> list = (List<VasVipGrowRecordMqVO>) objectMessage.getObject();
            for (VasVipGrowRecordMqVO vasVipGrowRecordMqVO:list){
                //查询用户信息
                RegUser regUser = regUserService.findRegUserById(vasVipGrowRecordMqVO.getUserId());
                if (whetherFirstRecharge(vasVipGrowRecordMqVO)){
                    vasVipGrowRecordService.insertVasVipGrowRecord(regUser.getCreateTime(),vasVipGrowRecordMqVO);
                }
            }
        }
    }

    /**
     *  @Description    : 判断成长值记录用户是否为首次充值
     *  @Method_Name    : whetherFirstRecharge
     *  @param vasVipGrowRecordMqVO
     *  @Creation Date  : 2018年03月30日 下午17:55:55
     *  @Author         : pengwu@hongkun.com.cn
     */
    private boolean whetherFirstRecharge(VasVipGrowRecordMqVO vasVipGrowRecordMqVO){
        //若为首次充值赠送成长值，判断是否为首次充值
        if (Objects.equals(vasVipGrowRecordMqVO.getGrowType(),VasVipConstants.VAS_VIP_GROW_TYPE_FIRST_RECHARGE)){
            FinPaymentRecord condition = new FinPaymentRecord();
            condition.setRegUserId(vasVipGrowRecordMqVO.getUserId());
            condition.setState(TradeStateConstants.ALREADY_PAYMENT);
            condition.setTradeType(10);
            //查询用户充值记录是否为0
            if (finPaymentRecordService.findFinPaymentRecordCount(condition) > 0){
                return false;
            }
        }
        return true;
    }
}
