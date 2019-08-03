package com.hongkun.finance.point.consumer.processors;

import com.hongkun.finance.invest.model.dto.RealnameAuthPointDTO;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointRecordService;
import com.yirun.framework.jms.JmsProcessor;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @Description : 处理实名认证赠送积分
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.support.processors.InvestPointProcessor
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Component
public class RealnameAuthPointProcessor implements JmsProcessor{
    private static final Logger logger = LoggerFactory.getLogger(RealnameAuthPointProcessor.class);
    @Autowired
    private PointAccountService pointAccountService;

    @Autowired
    private PointRecordService pointRecordService;

    @Override
    public Object processKeyedMessage(Message message) {

        RealnameAuthPointDTO realnameAuthPointDTO;
        try {
            realnameAuthPointDTO = (RealnameAuthPointDTO)((ActiveMQObjectMessage) message).getObject();
        } catch (JMSException e) {
            logger.error("执行实名注册赠送积分出错,从队列中接收的Message信息为:{}",message);
            return null;
        }
        /**
          step 1:更新积分记账户
         */
        PointAccount pointAccount = new PointAccount();
        pointAccount.setRegUserId(realnameAuthPointDTO.getUserId());
        pointAccount.setPoint(realnameAuthPointDTO.getPoint());
        //根据原来的积分更新现有的积分
        pointAccountService.updateByRegUserId(pointAccount);
        /**
          step 2:插入一条积分记录
         */

        PointRecord record = new PointRecord();
        record.setComments("实名认证赠送积分");

        record.setRegUserId(realnameAuthPointDTO.getUserId());

        record.setPoint(realnameAuthPointDTO.getPoint());
        //设置业务id为用户id
        record.setBusinessId(realnameAuthPointDTO.getUserId());

        //状态：已确认
        record.setState(PointConstants.POINT_STATE_CONFIRMED);
        //类型：实名认证送积分
        record.setType(PointConstants.POINT_TYPE_REAL_NAME_AUTH);
        //插入积分记录
        pointRecordService.insertPointRecord(record);

        return null;
    }
}

