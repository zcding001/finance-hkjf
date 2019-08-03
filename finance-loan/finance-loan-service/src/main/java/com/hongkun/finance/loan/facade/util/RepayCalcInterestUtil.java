package com.hongkun.finance.loan.facade.util;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.yirun.framework.core.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

import static com.hongkun.finance.invest.constants.InvestConstants.*;

/**
 * 
 * @Description : 提前还款，重新计算利息工具类
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.util.RepayCalcInterestUtil.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class RepayCalcInterestUtil {

    public static BigDecimal calForReturnCap(BidInfo bidInfo, BidInfoDetail bidInfoDetail, BigDecimal money,
                                                     BidRepayPlan bidRepayPlan, BigDecimal rate) {
        final int repaymentWay = bidInfo.getBiddRepaymentWay();
        final int returnCapWay = bidInfoDetail.getReturnCapWay();
        // 每月付息，到期还本
        if (repaymentWay == REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END) {
            return calcEveryMonth(returnCapWay, bidRepayPlan, money, rate);
        }
        // 到期还本付息（一次本息）
        if (repaymentWay == REPAYTYPE_ONECE_REPAYMENT) {
            return calcOnce(bidInfo, bidRepayPlan, money, rate);
        }
        //按月付息，按日计息
        if(repaymentWay == REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH){
            // 按天计息
            if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {
                return calcByDay(bidRepayPlan, money, rate);
            }else{//按月计息
                return calcByMonth(bidRepayPlan, money, rate);
            }
        }
        // 等额本息
        if (repaymentWay == REPAYTYPE_PRINCIPAL_INTEREST_EQ) {
            // TODO 暂不支持等额本息方式提前还本
        }
        return BigDecimal.ZERO;
    }
    
 	/**
	 * 
	 * @Description :提前还本，重新计算利息
	 * @Method_Name : calInterestForReturnCap
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @param money:提前还本金额
	 * @param bidRepayPlan
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月3日 下午2:00:25
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
 	@Deprecated
	public static BigDecimal calInterestForReturnCap(BidInfo bidInfo, BidInfoDetail bidInfoDetail, BigDecimal money,
			BidRepayPlan bidRepayPlan) {
		final int repaymentWay = bidInfo.getBiddRepaymentWay();
		final int returnCapWay = bidInfoDetail.getReturnCapWay();
		final BigDecimal rate = bidInfo.getInterestRate();
        // 判断提前还本日期是否是还款日
        int day = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), new Date());
        // 是还款日
        if (bidRepayPlan.getPlanTime().before(new Date()) && day >= 0) {
            return CalcInterestUtil.calcOverdue(money, day, bidInfo.getAdvanceServiceRate(),
                    bidInfo.getLiquidatedDamagesRate());
        }
		// 每月付息，到期还本
		if (repaymentWay == REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END) {
            return calcEveryMonth(returnCapWay, bidRepayPlan, money, rate);
		}
		// 到期还本付息（一次本息）
		if (repaymentWay == REPAYTYPE_ONECE_REPAYMENT) {
            return calcOnce(bidInfo, bidRepayPlan, money, rate);
		}
        //按月付息，按日计息
        if(repaymentWay == REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH){
            // 按天计息
            if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {
                return calcByDay(bidRepayPlan, money, rate);
            }else{//按月计息
                return calcByMonth(bidRepayPlan, money, rate);
            }
        }
		// 等额本息
		if (repaymentWay == REPAYTYPE_PRINCIPAL_INTEREST_EQ) {
			// TODO 暂不支持等额本息方式提前还本
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 
	 * @Description : 提前还本，服务费计算方式
	 * @Method_Name : calServiceInterestForReturnCap
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @param money
	 * @param bidRepayPlan
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月3日 下午6:06:26
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Deprecated
	public static BigDecimal calServiceInterestForReturnCap(BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			BigDecimal money, BidRepayPlan bidRepayPlan) {
		final int repaymentWay = bidInfo.getBiddRepaymentWay();
		final int returnCapWay = bidInfoDetail.getReturnCapWay();
		final BigDecimal rate = bidInfo.getServiceRate();
		// 每月付息，到期还本
		if (repaymentWay == REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END) {
            return calcEveryMonth(returnCapWay, bidRepayPlan, money, rate);
		}
		// 到期还本付息（一次本息）
		if (repaymentWay == REPAYTYPE_ONECE_REPAYMENT) {
            return calcOnce(bidInfo, bidRepayPlan, money, rate);
		}
		//按月付息，按日计息
		if(repaymentWay == REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH){
            // 按天计息
            if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {
                return calcByDay(bidRepayPlan, money, rate);
            }else{
                return calcByMonth(bidRepayPlan, money, rate);
            }
		}
		// 等额本息
		if (repaymentWay == REPAYTYPE_PRINCIPAL_INTEREST_EQ) {
			// TODO 暂不支持等额本息方式提前还本
		}
		return BigDecimal.ZERO;
	}
	
	/**
	*  按月付息
	*  @Method_Name             ：calcEveryMonth
	*  @param returnCapWay
	*  @param bidRepayPlan
	*  @param money
	*  @param rate
	*  @return java.math.BigDecimal
	*  @Creation Date           ：2018/5/22
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	private static BigDecimal calcEveryMonth(int returnCapWay, BidRepayPlan bidRepayPlan, BigDecimal money, BigDecimal rate){
        // 按天计息
        if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {
            return calcByDay(bidRepayPlan, money, rate);
        } else {// 按月计息
            return rate.multiply(money).divide(new BigDecimal(1200), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    /**
    *  一次本息
    *  @Method_Name             ：calcOnce
    *  @param bidInfo
    *  @param bidRepayPlan
    *  @param money
    *  @param rate
    *  @return java.math.BigDecimal
    *  @Creation Date           ：2018/5/22
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    private static BigDecimal calcOnce(BidInfo bidInfo, BidRepayPlan bidRepayPlan, BigDecimal money, BigDecimal rate){
        int n =  DateUtils.getDaysBetween(new Date(), bidInfo.getLendingTime());
//        // 计算总利息
//        BigDecimal repayInterestAtmSum = rate.multiply(new BigDecimal(100))
//                .multiply(new BigDecimal(bidInfo.getTermUnit() == 1 ? bidInfo.getTermValue() * 12 : bidInfo.getTermValue()))
//                .divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_EVEN);
//        // 总天数
//        int sumDay = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), bidInfo.getLendingTime());
//        BigDecimal dayInterest = repayInterestAtmSum.divide(new BigDecimal(sumDay), 10, BigDecimal.ROUND_HALF_EVEN);
//        return money.multiply(new BigDecimal(n)).multiply(dayInterest).divide(new BigDecimal(100), 2,
//                BigDecimal.ROUND_HALF_EVEN);
        return money.multiply(rate).multiply(new BigDecimal(n)).divide(new BigDecimal(36000), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
    *  按天计算利息
    *  @Method_Name             ：calcByDay
    *  @param bidRepayPlan
    *  @param money
    *  @param rate
    *  @return java.math.BigDecimal
    *  @Creation Date           ：2018/5/22
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    private static  BigDecimal calcByDay(BidRepayPlan bidRepayPlan, BigDecimal money, BigDecimal rate){
        int n = DateUtils.getDaysBetween(new Date(), DateUtils.addMonth(bidRepayPlan.getPlanTime(), -1));
        // 根据还款时间，判断提前还本的钱是否需要还利息,问题场景：还款时间8月6日，9月6日，还款时间8月4日，还完8月6的还款计划后，接着提前还本9月6的钱，那么此时不需要计算本金对应的利息
        if (n <= 0) {
            return BigDecimal.ZERO;
        }
        // 公式 = 日利率*金额*天数 >>> 注：日利率 = 年利率/（100*30）
        return rate.multiply(money).multiply(new BigDecimal(n)).divide(new BigDecimal(36000),
                2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    /**
    *  按月计息： 计算本次还款时间与上次还款时间之间天数，作为计算时间，例如本月有30天，用了一天就还款了，那么计算的时间还是按照30天计算
    *  @Method_Name             ：calcByMonth
    *  @param bidRepayPlan
    *  @param money
    *  @param rate
    *  @return java.math.BigDecimal
    *  @Creation Date           ：2018/5/22
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    private static BigDecimal calcByMonth(BidRepayPlan bidRepayPlan, BigDecimal money, BigDecimal rate){
        //按月计息
        int n = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), DateUtils.addMonth(bidRepayPlan.getPlanTime(), -1));
        // 根据还款时间，判断提前还本的钱是否需要还利息,问题场景：还款时间8月6日，9月6日，还款时间8月4日，还完8月6的还款计划后，接着提前还本9月6的钱，那么此时不需要计算本金对应的利息
        if (n <= 0) {
            return BigDecimal.ZERO;
        }
        return money.multiply(rate).multiply(new BigDecimal(n)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP);
    }
    
    /**
    *  计算罚息天数
    *  @Method_Name             ：calcPunishDays
    * 
    *  @return int
    *  @Creation Date           ：2018/5/31
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    public static int calcPunishDays(Date planTime, Integer currentPeriods, Integer termUnit, Integer termValue){
        int lateDay = DateUtils.getDays(new Date(), planTime);
        if(lateDay > 0){
            int totalPeriods = 0;
            if (termUnit == BID_TERM_UNIT_MONTH) {
                totalPeriods = termValue;
            } else if (termUnit == BID_TERM_UNIT_YEAR) {
                totalPeriods = termValue * 12;
            } else {
                totalPeriods = 1;
            }
            //如果不是最后一期
            if(totalPeriods > currentPeriods){
                Date nextPlanTime = DateUtils.addMonth(planTime, 1);
                //逾期时间超过两期，逾期天数设置为30
                if(DateUtils.getDays(new Date(), nextPlanTime) > 0){
                    lateDay = 30;
                }
            }
        }
        return lateDay;
    }
}
