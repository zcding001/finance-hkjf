package com.hongkun.finance.bi.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.bi.facade.StaIncomeFacade;
import com.hongkun.finance.bi.model.StaIncome;
import com.hongkun.finance.bi.service.StaIncomeService;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoDetailService;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.service.FinTradeFlowService;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.bi.constants.StaIncomeConstants.STA_INCOME_STATE_SUCCESS;
import static com.hongkun.finance.bi.constants.StaIncomeConstants.STA_INCOME_TRADE_TYPE_SERVICE_CHARGE;
import static com.hongkun.finance.invest.constants.InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE;
import static com.hongkun.finance.loan.constants.RepayConstants.RECEIPT_STATE_FINISH;

/**
 * @Description : 收入统计
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.facade.impl.StaIncomeFacadeImpl
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
@Service
public class StaIncomeFacadeImpl implements StaIncomeFacade {

    private static final Logger logger = LoggerFactory.getLogger(StaIncomeFacadeImpl.class);

    @Reference
    private BidInfoService bidInfoService;
    @Reference
    private BidInfoDetailService bidInfoDetailService;
    @Reference
    private BidInvestService bidInvestService;
    @Reference
    private FinFundtransferService finFundtransferService;
    @Reference
    private FinTradeFlowService finTradeFlowService;
    @Reference
    private BidReceiptPlanService bidReceiptPlanService;
    @Reference
    private RegUserService regUserService;
    @Reference
    private StaIncomeService staIncomeService;
    @Reference
    private BidRepayPlanService bidRepayPlanService;
    @Override
    public void initStaIncomes() {

        //当前业务备注： 优选没有服务费，所有存在服务费的流水都是散标
        //查询还款计划
        //查询上个月第一天和最后一天
        Date preMonth = DateUtils.addMonth(new Date(),-1);
        Date beginMonth = new  Date(DateUtils.getFirstDayOfMonth(preMonth).getTime());
        Date endMonth = new  Date(DateUtils.getLastDayOfMonth(preMonth).getTime());
        logger.info("initStaIncomes 收入统计定时跑批, beginMonth: {},endMonth: {}",beginMonth,endMonth );
        //还款计划两个条件：1已还款 2、计划还款时间是上个月（逾期跨月部分不能统计到）  并且存在服务费
        BidRepayPlan planCdt = new BidRepayPlan();
        planCdt.setState(RECEIPT_STATE_FINISH);
        planCdt.setPlanTimeBegin(beginMonth);
        planCdt.setPlanTimeEnd(endMonth);
        List<BidRepayPlan> bidRepayPlans =  bidRepayPlanService.findBidRepayPlanListForStaIncomes(planCdt);
        if(CommonUtils.isEmpty(bidRepayPlans)){
            return ;
        }
        //查询流水、资金划转、借款人信息
        Set<Integer> bidIds = new HashSet<Integer>();
        Set<Integer> bidRepayIds = new HashSet<Integer>();
        List<Integer> regUserIds = new ArrayList<Integer>();
        bidRepayPlans.forEach(bidRepayPlan -> {
            bidIds.add(bidRepayPlan.getBidId());
            bidRepayIds.add(bidRepayPlan.getId());
            regUserIds.add(bidRepayPlan.getRegUserId());
        });
        Map<Integer,BidInfoVO> bidInfoVOs = (Map<Integer,BidInfoVO>) bidInfoService.findBidInfoDetailVoByIdList(bidIds);
        Map<String,Object> tradeParams =  finFundtransferService.findParamsForStaIncome(bidRepayIds,beginMonth,endMonth);
        List<FinFundtransfer> serviceChargeFinFundtransferList = (List<FinFundtransfer>) tradeParams.get("serviceChargeFinFundtransferList");
        List<FinTradeFlow> serviceChargeTradeFlowList =  (List<FinTradeFlow>) tradeParams.get("serviceChargeTradeFlowList");
        List<FinFundtransfer> chargeAndPenFundtransferList =  (List<FinFundtransfer>) tradeParams.get("chargeAndPenFundtransferList");
        List<UserVO> userVOs = regUserService.findUserWithDetailByInfo(regUserIds);
        //计算每笔服务费对应的加息
        List<StaIncome> resultList = new ArrayList<StaIncome>();
        bidRepayPlans.forEach(bidRepayPlan -> {
            BidInfoVO bidInfoVO = bidInfoVOs.get(bidRepayPlan.getBidId());
            if(bidInfoVO != null){
                List<BidInvest> bidInvests =bidInvestService.findMatchBidInvestListByBidId(bidRepayPlan.getBidId());
                BigDecimal increaseAmountSum = BigDecimal.ZERO;
                if(CommonUtils.isNotEmpty(bidInvests)){
                    //4、判断标的是否是匹配标的
                    if(bidInfoVO.getMatchType() == BID_INVEST_CHANNEL_IMMEDIATE){
                        //如果是直投    查询投资记录是否含有加息   如果有，查询回款计划当前期中加息金额，返回结束。
                        increaseAmountSum =  getIncreaseSumMoneyForImmediate(bidInvests,beginMonth,endMonth);
                    }else{
                        //如果是匹配，根据计算公式  加息 = 优选加息*散标投资金额*提前还款本金（如果没有就是总金额）/(优选投资金额*散标标的总金额)
                        increaseAmountSum = getIncreaseSumMoneyForMatch(bidInvests,bidRepayPlan.getCapitalAmount(),bidInfoVO.getTotalAmount(),beginMonth,endMonth);
                    }
                }
                //查询流水信息
                StaIncome staIncome = findMatchFuntransfer(bidRepayPlan.getId(),serviceChargeTradeFlowList,serviceChargeFinFundtransferList,userVOs);
                if(StringUtils.isNotBlank(staIncome.getFlowId())){
                    staIncome.setPureMoney(staIncome.getTransMoney().subtract(increaseAmountSum));
                    resultList.add(staIncome);
                }
            }
        });
        //查询手续费流水和罚息流水
        List<StaIncome> chargeStas =  findMatchFuntransferForChare(chargeAndPenFundtransferList);
        if(CommonUtils.isNotEmpty(chargeStas)){
            resultList.addAll(chargeStas);
        }
        staIncomeService.insertStaIncomeBatch(resultList,50);
    }

    /**
    *  @Description    ：统计手续费和罚息
    *  @Method_Name    ：findMatchFuntransferForChare
    *  @param chargeAndPenFundtransferList
    *  @return java.util.List<com.hongkun.finance.bi.model.StaIncome>
    *  @Creation Date  ：2018/4/28
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private List<StaIncome> findMatchFuntransferForChare(List<FinFundtransfer> chargeAndPenFundtransferList) {
        List<StaIncome> resultList = new ArrayList<StaIncome>();
        if(CommonUtils.isNotEmpty(chargeAndPenFundtransferList)){
            List<Integer> regUserIds = new ArrayList<Integer>();
            chargeAndPenFundtransferList.forEach(finFundtransfer -> {
                regUserIds.add(finFundtransfer.getRegUserId());
            });
            List<UserVO> userVOs = regUserService.findUserWithDetailByInfo(regUserIds);
            for(FinFundtransfer finFundtransfer:chargeAndPenFundtransferList){
                StaIncome result = null;
                for (UserVO userVO:userVOs){
                    if (finFundtransfer.getRegUserId().equals(userVO.getUserId())){
                        result = new StaIncome();
                        result.setBorrowerId(finFundtransfer.getRegUserId());
                        result.setBorrowerName(userVO.getRealName());
                        result.setBorrowerTel(String.valueOf(userVO.getLogin()));
                        result.setFlowId(finFundtransfer.getFlowId());
                        result.setTransMoney(finFundtransfer.getTransMoney());
                        result.setPureMoney(finFundtransfer.getTransMoney());
                        result.setState(STA_INCOME_STATE_SUCCESS);
                        result.setTradeType(STA_INCOME_TRADE_TYPE_SERVICE_CHARGE);
                        result.setTransTime(finFundtransfer.getCreateTime());
                        result.setCreateTime(new Date());
                        resultList.add(result);
                        break;
                    }
                }
                if(result==null){
                    logger.error("findMatchFuntransferForChare, 收入统计处理手续费&罚息流水存在资金划转异常: 资金划转找不到用户: {}", finFundtransfer.toString());
                }
            }
        }
        return resultList;
    }

    /**
    *  @Description    ：查询匹配某笔还款计划的服务费资金划转
    *  @Method_Name    ：findMatchFuntransfer
    *  @param bidRepayId
    *  @param finTradeFlowList
    *  @param finFundtransferList
     * @param  userVOs
    *  @return com.hongkun.finance.payment.model.FinFundtransfer
    *  @Creation Date  ：2018/4/27
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private StaIncome findMatchFuntransfer(Integer bidRepayId, List<FinTradeFlow> finTradeFlowList, List<FinFundtransfer> finFundtransferList, List<UserVO> userVOs) {
        StaIncome result = new StaIncome();
        if (CommonUtils.isNotEmpty(finTradeFlowList) && CommonUtils.isNotEmpty(finFundtransferList)){
            for(FinFundtransfer finFundtransfer:finFundtransferList){
                String tradeFlowId =  "";
                for (FinTradeFlow tradeFlow:finTradeFlowList){
                    if (String.valueOf(bidRepayId).equals(tradeFlow.getPflowId())){
                        tradeFlowId = tradeFlow.getFlowId();
                        break;
                    }
                }
                if (tradeFlowId.equals(finFundtransfer.getTradeFlowId())){
                    for (UserVO userVO:userVOs){
                        if (finFundtransfer.getRegUserId().equals(userVO.getUserId())){
                            result.setBorrowerId(finFundtransfer.getRegUserId());
                            result.setBorrowerName(userVO.getRealName());
                            result.setBorrowerTel(String.valueOf(userVO.getLogin()));
                            result.setFlowId(finFundtransfer.getFlowId());
                            result.setTransMoney(finFundtransfer.getTransMoney());
                            result.setState(STA_INCOME_STATE_SUCCESS);
                            result.setTradeType(STA_INCOME_TRADE_TYPE_SERVICE_CHARGE);
                            result.setTransTime(finFundtransfer.getCreateTime());
                            result.setCreateTime(new Date());
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
    *  @Description    ：获取某笔还款的对应加息---针对直投标的
    *  @Method_Name    ：getIncreaseSumMoneyForImmediate
    *  @param bidInvests  当前还款对应的散标投资记录
    *  @param beginMonth  当前月份第一天
    *  @param endMonth    当前月份最后一天
    *  @return java.math.BigDecimal
    *  @Creation Date  ：2018/4/27
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private  BigDecimal  getIncreaseSumMoneyForImmediate(List<BidInvest> bidInvests,Date beginMonth,Date endMonth){
        BigDecimal increaseAmountSum = BigDecimal.ZERO;
        for(BidInvest bidInvest:bidInvests){
            if (CommonUtils.gtZero(bidInvest.getCouponIdJ())){
                //如果含有加息券
                BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
                bidReceiptPlan.setInvestId(bidInvest.getId());
                bidReceiptPlan.setPlanTimeBegin(beginMonth);
                bidReceiptPlan.setPlanTimeEnd(endMonth);
                List<BidReceiptPlan> bidReceiptPlans =   bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlan);
                if(CommonUtils.isNotEmpty(bidReceiptPlans)){
                    increaseAmountSum =  increaseAmountSum.add(bidReceiptPlan.getIncreaseAmount());
                }
            }
        }
        return increaseAmountSum;
    }

    /**
    *  @Description    ：获取某笔还款的对应加息---针对匹配标的
    *  @Method_Name    ：getIncreaseSumMoneyForMatch
    *  @param bidInvests 散标投资记录列表
    *  @param repayCapitalAmount  此笔还款本金
    *  @param totalAmount    标的总金额
    *  @param beginMonth   当前月第一天
    *  @param endMonth     当前月最后一天
    *  @return java.math.BigDecimal
    *  @Creation Date  ：2018/4/27
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private BigDecimal getIncreaseSumMoneyForMatch(List<BidInvest> bidInvests,BigDecimal repayCapitalAmount,
                                                   BigDecimal totalAmount,Date beginMonth,Date endMonth){
        BigDecimal goodIncreaseAmountSum = BigDecimal.ZERO;
        Set<Integer> investIds = new HashSet<Integer>();
        bidInvests.forEach(invest->{
            investIds.add(invest.getId());
        });
        //查询优选投资记录
        List<BidInvest> goodBidInvestList =  bidInvestService.findGoodBidInvestListByIdList(investIds);
        //如果存在加息， 按照时间范围查询回款计划
        for(BidInvest goodInvest : goodBidInvestList){
            //找到此笔优选对应的散标
            BidInvest comInvest = null;
            for (BidInvest commonInvest:bidInvests){
                if (goodInvest.getId().equals(commonInvest.getGoodInvestId())){
                    comInvest = commonInvest;
                    break;
                }
            }
            if (CommonUtils.gtZero(goodInvest.getCouponIdJ())){
                //如果含有加息券
                BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
                bidReceiptPlan.setInvestId(goodInvest.getId());
                bidReceiptPlan.setPlanTimeBegin(beginMonth);
                bidReceiptPlan.setPlanTimeEnd(endMonth);
                BidReceiptPlan thisBatchbidReceiptPlan =   bidReceiptPlanService.findGoodBidReceiptPlan(bidReceiptPlan);
                BigDecimal repayAmount = CompareUtil.gtZero(repayCapitalAmount)?repayCapitalAmount:totalAmount;
                if(thisBatchbidReceiptPlan!=null){
                    BigDecimal increaseAmount = thisBatchbidReceiptPlan.getIncreaseAmount().multiply(comInvest.getInvestAmount()).multiply(repayAmount)
                            .divide((goodInvest.getInvestAmount().multiply(totalAmount)),2,BigDecimal.ROUND_HALF_UP);
                    goodIncreaseAmountSum = goodIncreaseAmountSum.add(increaseAmount);
                }
            }
        }
        return  goodIncreaseAmountSum;
    }


}
