package com.hongkun.finance.bi.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.bi.facade.StaFacade;

import com.hongkun.finance.bi.model.StaQdz;
import com.hongkun.finance.bi.model.StaReceipt;
import com.hongkun.finance.bi.model.StaRepay;

import com.hongkun.finance.bi.model.StaUserFirst;
import com.hongkun.finance.bi.model.vo.StaQdzInOutVo;
import com.hongkun.finance.bi.model.vo.StaUserFirstVO;

import com.hongkun.finance.bi.service.StaQdzService;
import com.hongkun.finance.bi.service.StaUserFirstService;

import com.hongkun.finance.bi.service.*;

import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.vo.StaFunBidVO;
import com.hongkun.finance.invest.model.vo.StaFunInvestVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;

import com.hongkun.finance.vas.model.VasRedpacketInfo;
import com.hongkun.finance.vas.model.vo.RedPacketVO;
import com.hongkun.finance.vas.service.VasRedpacketInfoService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PeriodEnum;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;

import java.math.BigDecimal;


/**
 * 统计门面类
 *
 * @author zc.ding
 * @create 2018/9/17
 */
@Service
public class StaFacadeImpl implements StaFacade {
    @Autowired
    private StaUserFirstService staUserFirstService;
    @Reference
    private BidInvestService bidInvestService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidRepayPlanService bidRepayPlanService;
    @Reference
    private StaQdzService  staQdzService;
    @Reference
    private FinPaymentRecordService finPaymentRecordService;
    @Reference
    private VasRedpacketInfoService vasRedpacketInfoService;
    @Reference
    private QdzTransRecordService qdzTransRecordService;
    @Autowired
    private StaRepayService staRepayService;
    @Autowired
    private StaReceiptService staReceiptService;
    
    @Override
    public StaUserFirstVO findInvestDis(Integer period, String startTime, String endTime) {
        Map<String, Date> map = DateUtils.getPeriod(PeriodEnum.getPeriodEnum(period), startTime, endTime);
        //查询新用户投子人数及金额
        StaUserFirst staUserFirst = new StaUserFirst();
        Date st = map.get(DateUtils.START);
        Date et = map.get(DateUtils.END);
        staUserFirst.setDaBegin(st);
        staUserFirst.setDaEnd(et);
        staUserFirst = this.staUserFirstService.findStaUserFirstCount(staUserFirst);
        //查询老用户投资人数及金额
        StaFunInvestVO staFunInvestVO = new StaFunInvestVO();
        staFunInvestVO.setStartTime(st);
        staFunInvestVO.setEndTime(et);
        Map<String, Object> countMap = this.bidInvestService.findStaFunInvestAddup(staFunInvestVO);
        StaUserFirstVO staUserFirstVO = new StaUserFirstVO();
        staUserFirstVO.setNewUserCount(staUserFirst.getUserCount());
        staUserFirstVO.setNewInvestAmountSum(staUserFirst.getInvestAmountSum());
        Object investUserCount = countMap.get("investUserCount");
        staUserFirstVO.setOldUserCount(investUserCount == null ? 0 : ((Long)investUserCount).intValue());
        Object investAmount = countMap.get("investAmount");
        staUserFirstVO.setOldInvestAmountSum((investAmount == null ? BigDecimal.ZERO : (BigDecimal)investAmount).subtract(staUserFirst.getInvestAmountSum()));
        return staUserFirstVO;
    }

    @Override
    public Pager findStaFunInvest(Pager pager, String realName, Long login, String startTime, String endTime) {
        List<Integer> userIds = null;
        if(StringUtils.isNotBlank(realName) || (login != null && login > 0)){
            UserVO userVO = new UserVO();
            userVO.setRealName(realName);
            userVO.setLogin(login);
            userIds = this.regUserService.findUserWithDetailByInfo(userVO).stream().map(UserVO::getUserId).collect(Collectors.toList());
        }
        StaFunInvestVO staFunInvestVO = new StaFunInvestVO();
        staFunInvestVO.setUserIds(userIds);
        if(StringUtils.isNotBlank(startTime)){
            staFunInvestVO.setStartTime(DateUtils.parse(startTime, DateUtils.DATE));
        }
        if(StringUtils.isNotBlank(endTime)){
            staFunInvestVO.setEndTime(DateUtils.addDays(DateUtils.parse(endTime, DateUtils.DATE), 1));
        }
        pager = this.bidInvestService.findStaFunInvestList(pager, staFunInvestVO);
        if(CommonUtils.isNotEmpty(pager.getData())){
            pager.getData().forEach(o -> {
                StaFunInvestVO tmp = (StaFunInvestVO)o;
                final Integer regUserId = tmp.getRegUserId();
                if(regUserId == 0){
                    tmp.setLogin(99999999999L);
                }else{
                    tmp.setLogin(BaseUtil.getRegUser(regUserId, () -> regUserService.findRegUserById(regUserId)).getLogin());
                    tmp.setIdCard(BaseUtil.getRegUserDetail(regUserId, () -> regUserDetailService.findRegUserDetailByRegUserId(regUserId)).getIdCard());
                }
            });
        }
        return pager;
    }

    @Override
    public Pager findStaFunBid(Pager pager, StaFunBidVO staFunBidVO) {
        //查询标的信息
        pager = this.bidInfoService.findStaFunBid(pager, staFunBidVO);
        if(CommonUtils.isNotEmpty(pager.getData())){
            List<StaFunBidVO> list = (List<StaFunBidVO>) pager.getData();
            List<Integer> bidIds = list.stream().filter(o -> o.getState().equals(InvestConstants.BID_STATE_WAIT_REPAY) || o.getState().equals(InvestConstants.BID_STATE_WAIT_FINISH)).map(StaFunBidVO::getId).collect(Collectors.toList());
            if(CommonUtils.isNotEmpty(bidIds)){
                //查询回款计划统计信息
                List<BidRepayPlan> repayedList = bidRepayPlanService.findRepayedSta(bidIds);
                list.forEach(o -> {
                    o.setInterestAmount(repayedList.stream().filter(t -> t.getBidId().equals(o.getId())).findAny().orElse(new BidRepayPlan()).getAmount());
                    o.setRepayedcaptailAmount(repayedList.stream().filter(t -> t.getBidId().equals(o.getId())).findAny().orElse(new BidRepayPlan()).getCapitalAmount());
                    o.setRepayedInterestAmount(repayedList.stream().filter(t -> t.getBidId().equals(o.getId())).findAny().orElse(new BidRepayPlan()).getInterestAmount());
                    o.setLatestRepayedTime(repayedList.stream().filter(t -> t.getBidId().equals(o.getId())).findAny().orElse(new BidRepayPlan()).getActualTime());
                });
            }
            //设置借款人信息
            list.forEach(o -> {
                final Integer regUserId = o.getBorrowerId();
                RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
                o.setLogin(regUser.getLogin());
                o.setUserType(regUser.getType());
                o.setRealName(BaseUtil.getRegUserDetail(regUserId, () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId)).getRealName());
            });
            pager.setData(list);
        }
        return pager;
    }

    @Override

    public List<Map<String, Object>> findTransferCountList(Integer period, String startTime,
            String endTime) {
        Map<String, Date> dateMap = DateUtils.getPeriod(PeriodEnum.getPeriodEnum(period), startTime, endTime); 
        //查询充值统计
        FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
        finPaymentRecord.setState(TradeStateConstants.ALREADY_PAYMENT);
        finPaymentRecord.setCreateTimeBegin(dateMap.get(DateUtils.START));
        finPaymentRecord.setCreateTimeEnd(dateMap.get(DateUtils.END));
        finPaymentRecord.setTradeType(10);//充值
        PaymentVO rechargePaymentVO = finPaymentRecordService.findPaymentSum(finPaymentRecord);
        finPaymentRecord.setTradeType(14);//提现
        PaymentVO withdrawPaymentVO = finPaymentRecordService.findPaymentSum(finPaymentRecord);

        //查询红包充值信息
        VasRedpacketInfo vasRedpacketInfo =new VasRedpacketInfo();
        vasRedpacketInfo.setType(2);//充值红包
        vasRedpacketInfo.setState(1);//已经领取
        vasRedpacketInfo.setModifyTimeBegin(dateMap.get(DateUtils.START));
        vasRedpacketInfo.setModifyTimeEnd(dateMap.get(DateUtils.END));
        RedPacketVO redPacketUserNum = vasRedpacketInfoService.findRedPacketUserSum(vasRedpacketInfo);
        //查询钱袋子转入转出统计总金额，总转入转出次数
        StaQdz staQdz=new StaQdz();
        staQdz.setDaBegin(dateMap.get(DateUtils.START));
        staQdz.setDaEnd(dateMap.get(DateUtils.END));
        StaQdzInOutVo staQdzVo = staQdzService.findStaQdzSum(staQdz);
        //查询钱袋了转入转出总人数
        QdzTransRecord qdzTransRecord = new QdzTransRecord();
        qdzTransRecord.setCreateTimeBegin(dateMap.get(DateUtils.START));
        qdzTransRecord.setCreateTimeEnd(dateMap.get(DateUtils.END));
        qdzTransRecord.setState(1);
        Integer inUserSum = qdzTransRecordService.findTransferUserSum(qdzTransRecord);
        qdzTransRecord.setState(2);
        Integer outUserSum = qdzTransRecordService.findTransferUserSum(qdzTransRecord);
        //查询投资总金额，总笔数，总人数
        StaFunInvestVO staFunInvestVO = new StaFunInvestVO();
        staFunInvestVO.setStartTime(dateMap.get(DateUtils.START));
        staFunInvestVO.setEndTime(dateMap.get(DateUtils.END));
        Map<String,Object>  investMap = this.bidInvestService.findStaFunInvestAddup(staFunInvestVO);
        
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> moneyMap = new HashMap<String, Object>();
        Map<String, Object> numMap = new HashMap<String, Object>();
        Map<String, Object> userNumMap = new HashMap<String, Object>();
        //组装统计的总金额
        moneyMap.put("type", "money");
        moneyMap.put("recharge", rechargePaymentVO.getTransMoneySum());
        moneyMap.put("redPacketRecharge", redPacketUserNum.getRedPacketMoneySum());
        
        moneyMap.put("invest", investMap.get("investAmount"));
        moneyMap.put("withdraw", withdrawPaymentVO.getTransMoneySum());
        moneyMap.put("turnIn", staQdzVo.getInAmountSum());
        moneyMap.put("turnOut", staQdzVo.getOutAmountSum());
        result.add(moneyMap);
        //组装统计的次数
        numMap.put("type", "nums");
        numMap.put("recharge", rechargePaymentVO.getPayTimes());
        numMap.put("redPacketRecharge", redPacketUserNum.getRedPacketTimes());
        numMap.put("invest",investMap.get("investTimes"));
        numMap.put("withdraw", withdrawPaymentVO.getPayTimes());
        numMap.put("turnIn", staQdzVo.getInCount());
        numMap.put("turnOut", staQdzVo.getOutCount());
        result.add(numMap);
        //组装统计的人数
        userNumMap.put("type", "userNum");
        userNumMap.put("recharge", rechargePaymentVO.getPayUserSum());
        userNumMap.put("redPacketRecharge", redPacketUserNum.getRedPacketUserNum());
        userNumMap.put("invest", investMap.get("investUserCount"));
        userNumMap.put("withdraw", withdrawPaymentVO.getPayUserSum());
        userNumMap.put("turnIn", inUserSum);
        userNumMap.put("turnOut", outUserSum);
        result.add(userNumMap);
        return result;
    }
    public ResponseEntity<?> findStaPlatformAddup(String startTime, String endTime) {
        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        Date daBegin = null;
        if(StringUtils.isNotBlank(startTime)){
            daBegin = DateUtils.parse(startTime, DateUtils.DATE);
        }
        Date daEnd = null;
        if(StringUtils.isNotBlank(endTime)){
            daEnd = DateUtils.addDays(DateUtils.parse(endTime, DateUtils.DATE), 1);
        }
        
        //查询还款
        StaRepay staRepay = new StaRepay();
        staRepay.setDaBegin(daBegin);
        staRepay.setDaEnd(daEnd);
        result.getParams().put("staRepayAddup", this.staRepayService.findStaRepayAddup(staRepay));
        result.getParams().put("staRepayFuture", this.staRepayService.findStaRepayFuture());
        
        //查询回款
        StaReceipt staReceipt = new StaReceipt();
        staReceipt.setDaBegin(daBegin);
        staReceipt.setDaEnd(daEnd);
        result.getParams().put("staReceiptAddup", this.staReceiptService.findStaReceiptAddup(staReceipt));
        result.getParams().put("staReceiptFuture", this.staReceiptService.findStaReceiptFuture());
        
        //查询投资人数&标的数量&标的总金额&借款人数
        StaFunBidVO staFunBidVO = new StaFunBidVO();
        staFunBidVO.setStartTime(daBegin);
        staFunBidVO.setEndTime(daEnd);
        result.getParams().putAll(this.bidInfoService.findStaBidUserAmountCount(staFunBidVO));
        return result;
    }
}
