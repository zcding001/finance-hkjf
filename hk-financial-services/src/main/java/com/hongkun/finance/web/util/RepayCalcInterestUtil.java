package com.hongkun.finance.web.util;

import java.math.BigDecimal;
import java.util.Date;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.yirun.framework.core.utils.DateUtils;

/**
 * 
 * @Description : 提前还款，重新计算利息工具类
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.util.RepayCalcInterestUtil.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class RepayCalcInterestUtil {

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
	public static BigDecimal calInterestForReturnCap(BidInfo bidInfo, BidInfoDetail bidInfoDetail, BigDecimal money,
			BidRepayPlan bidRepayPlan) {
		final int repaymentWay = bidInfo.getBiddRepaymentWay();
		final int returnCapWay = bidInfoDetail.getReturnCapWay();
		// 判断提前还本日期是否是还款日
		int day = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), new Date());
		if (bidRepayPlan.getPlanTime().before(new Date()) && day >= 0) {// 是还款日
			return CalcInterestUtil.calcOverdue(money, day, bidInfo.getAdvanceServiceRate(),
					bidInfo.getLiquidatedDamagesRate());
		}
		// 每月付息，到期还本
		if (repaymentWay == REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END) {

			if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {// 按天计息
				int n = DateUtils.getDaysBetween(new Date(), DateUtils.addMonth(bidRepayPlan.getPlanTime(), 1));
				// 公式 = 日利率*金额*天数 >>> 注：日利率 = 年利率/（100*12*30）
				return bidInfo.getInterestRate().multiply(money).multiply(new BigDecimal(n))
						.divide(new BigDecimal(36000), 2, BigDecimal.ROUND_HALF_EVEN);
			} else {// 按月计息
				return bidInfo.getInterestRate().multiply(money).divide(new BigDecimal(1200), 2,
						BigDecimal.ROUND_HALF_UP);
			}
		}
		// 到期还本付息（一次本息）
		if (repaymentWay == REPAYTYPE_ONECE_REPAYMENT) {
			int n = DateUtils.getDaysBetween(new Date(), DateUtils.addDays(bidRepayPlan.getPlanTime(),
					bidInfo.getTermUnit() == 1 ? bidInfo.getTermUnit() : bidInfo.getTermUnit()));
			// 计算总利息
			BigDecimal repayInterestAtmSum = bidInfo.getInterestRate().multiply(new BigDecimal(100))
					.multiply(new BigDecimal(
							bidInfo.getTermUnit() == 1 ? bidInfo.getTermValue() * 12 : bidInfo.getTermValue()))
					.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_EVEN);
			// 总天数
			int sumDay = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), bidInfo.getLendingTime());
			BigDecimal dayInterest = repayInterestAtmSum.divide(new BigDecimal(sumDay), 10, BigDecimal.ROUND_HALF_EVEN);
			return money.multiply(new BigDecimal(n)).multiply(dayInterest).divide(new BigDecimal(100), 2,
					BigDecimal.ROUND_HALF_EVEN);
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
	public static BigDecimal calServiceInterestForReturnCap(BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			BigDecimal money, BidRepayPlan bidRepayPlan) {
		final int repaymentWay = bidInfo.getBiddRepaymentWay();
		final int returnCapWay = bidInfoDetail.getReturnCapWay();
		// 每月付息，到期还本
		if (repaymentWay == REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END) {
			if (returnCapWay == REPAYTYPE_CAT_WAY_DAY) {// 按天计息
				int n = DateUtils.getDaysBetween(new Date(), DateUtils.addMonth(bidRepayPlan.getPlanTime(), 1));
				// 公式 = 日利率*金额*天数 >>> 注：日利率 = 年利率/（100*30）
				return bidInfo.getServiceRate().multiply(money).multiply(new BigDecimal(n)).divide(new BigDecimal(3000),
						2, BigDecimal.ROUND_HALF_EVEN);
			} else {// 按月计息
				return bidInfo.getServiceRate().multiply(money).divide(new BigDecimal(100), 2,
						BigDecimal.ROUND_HALF_UP);
			}
		}
		// 到期还本付息（一次本息）
		if (repaymentWay == REPAYTYPE_ONECE_REPAYMENT) {
			int n = DateUtils.getDaysBetween(new Date(), DateUtils.addDays(bidRepayPlan.getPlanTime(),
					bidInfo.getTermUnit() == 1 ? bidInfo.getTermUnit() : bidInfo.getTermUnit()));
			// 计算总服务费
			BigDecimal serviceInterestAtmSum = bidInfo.getServiceRate().multiply(new BigDecimal(100))
					.multiply(new BigDecimal(
							bidInfo.getTermUnit() == 1 ? bidInfo.getTermValue() * 12 : bidInfo.getTermValue()))
					.divide(new BigDecimal(1200), 10, BigDecimal.ROUND_HALF_EVEN);
			// 总天数
			int sumDay = DateUtils.getDaysBetween(bidRepayPlan.getPlanTime(), bidInfo.getLendingTime());
			BigDecimal dayServiceInterest = serviceInterestAtmSum.divide(new BigDecimal(sumDay), 10,
					BigDecimal.ROUND_HALF_EVEN);
			return money.multiply(new BigDecimal(n)).multiply(dayServiceInterest).divide(new BigDecimal(100), 2,
					BigDecimal.ROUND_HALF_EVEN);
		}
		// 等额本息
		if (repaymentWay == REPAYTYPE_PRINCIPAL_INTEREST_EQ) {

		}
		return BigDecimal.ZERO;
	}
}
