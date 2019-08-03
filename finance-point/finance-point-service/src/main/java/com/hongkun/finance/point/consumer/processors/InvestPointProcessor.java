package com.hongkun.finance.point.consumer.processors;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.dto.InvestPointDTO;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.point.support.CalcPointUtils;
import com.yirun.framework.jms.JmsProcessor;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description : 处理积分模块的积分处理器
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.support.processors.InvestPointProcessor
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Component
public class InvestPointProcessor implements JmsProcessor{
    private static final Logger logger = LoggerFactory.getLogger(InvestPointProcessor.class);
    @Autowired
    private PointAccountService pointAccountService;

    @Autowired
    private PointRecordService pointRecordService;

    @Autowired
    private PointRuleService pointRuleService;


    @Override
    public Object processKeyedMessage(Message message) {
        InvestPointDTO investPointDTO;
        try {
            investPointDTO = (InvestPointDTO)((ActiveMQObjectMessage) message).getObject();
        } catch (JMSException e) {
            logger.error("执行投资送积分出错,从队列中接收的Message信息为:{}",message);
            return null;
        }
        //step0：获取计算基数
        PointRule currentRule=pointRuleService.getCurrentOnUseRule();
        BigDecimal pointInvestBase = currentRule.getPointInvestBase();
        //step1:获取标的信息
        BidInfo bidInfo = investPointDTO.getBidInfo();
        Integer termValue = bidInfo.getTermValue();
        Integer termUnit = bidInfo.getTermUnit();

        //step2:获取投资记录
        List<BidInvest> bidInvests = investPointDTO.getBidInvests();
        //step3:给每个投资人赠送积分
        bidInvests.stream().forEach((invest)->{
            int points = CalcPointUtils.calculateInvestPoint(invest.getInvestAmount().doubleValue(), termValue, termUnit, pointInvestBase.intValue());
            //初始化积分账户
            PointAccount pointAccount = new PointAccount();
            pointAccount.setRegUserId(invest.getRegUserId());
            pointAccount.setPoint(points);
            //根据原来的积分更新现有的积分
            pointAccountService.updateByRegUserId(pointAccount);
            //插入一条积分记录
            PointRecord record = new PointRecord();
            record.setComments("投资获取积分，用户姓名："+invest.getRealName());
            Date currentDate = new Date();
            record.setRegUserId(invest.getRegUserId());
            record.setCreateTime(currentDate);
            record.setModifyTime(currentDate);
            record.setPoint(points);
            record.setBusinessId(invest.getId());
            //状态：已确认
            record.setState(PointConstants.POINT_STATE_CONFIRMED);
            //类型：投资送积分
            record.setType(PointConstants.POINT_TYPE_INVEST);
            //插入积分
            pointRecordService.insertPointRecord(record);
        });

        return null;
    }
}

