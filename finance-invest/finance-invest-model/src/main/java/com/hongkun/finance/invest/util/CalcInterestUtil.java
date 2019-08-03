package com.hongkun.finance.invest.util;

import com.yirun.framework.core.utils.CompareUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.hongkun.finance.invest.constants.InvestConstants.*;

/**
 * @Description : 利率计算工具类
 * @Project : finance-user-model
 * @Program Name : com.hongkun.finance.user.utils.CalcRateUtil.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public class CalcInterestUtil {

	private CalcInterestUtil() {
	}

	/**
	 * 
	 * @Description : 计算罚息<br/>
	 *              罚息算法一(保留2位小数): 罚息金额= （服务费率 + 逾期违约费率）* 借款金额 * 逾期天数
	 * @Method_Name : punishOne
	 * @param capital
	 *            借款金额
	 * @param day
	 *            逾期天数
	 * @param serviceRate
	 *            服务费率
	 * @param overdueRate
	 *            逾期违约费率
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月21日 上午8:59:33
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	public static BigDecimal calcOverdue(BigDecimal money, int day, BigDecimal serviceRate, BigDecimal overdueRate) {
		return serviceRate.add(overdueRate).multiply(money).multiply(BigDecimal.valueOf(day))
				.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 加息计算公式
	 * @Method_Name : calcInterest
	 * @param termUnit
	 *            期数单位（年月日）
	 * @param termValue
	 *            投资期数
	 * @param money
	 *            投资金额
	 * @param rate
	 *            标的利率
	 * @param raiseRate
	 *            加息券利率
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月15日 下午3:52:50
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	public static BigDecimal calcInterest(int termUnit, int termValue, BigDecimal money, BigDecimal rate,
			BigDecimal raiseRate) {
		BigDecimal increaseAtm = calcOperate(rate, money, getTermValue(termUnit, termValue), getDivisor(termUnit));
		if (raiseRate != null && CompareUtil.gtZero(raiseRate)) {
			return increaseAtm
					.add(calcOperate(raiseRate, money, getTermValue(termUnit, termValue), getDivisor(termUnit)));
		}
		return increaseAtm;
	}

	/**
	 * @Description : 利息计算公式
	 * @Method_Name : calcInterest
	 * @param termUnit
	 *            期数单位（年月日）
	 * @param termValue
	 *            投资期数
	 * @param money
	 *            投资金额
	 * @param rate
	 *            年化利率
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月15日 下午3:52:50
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	public static BigDecimal calcInterest(int termUnit, int termValue, BigDecimal money, BigDecimal rate) {
		return calcOperate(rate, money, getTermValue(termUnit, termValue), getDivisor(termUnit));
	}

	/**
	 * @Description : 计算投资天数
	 * @Method_Name : calInvestDays
	 * @param termUnit
	 * @param termValue
	 * @return
	 * @return : int
	 * @Creation Date : 2017年7月4日 上午11:57:07
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static int calInvestDays(int termUnit, int termValue) {
		int investDays;
		if (termUnit == BID_TERM_UNIT_MONTH) {
			investDays = 30 * termValue;
		} else if (termUnit == BID_TERM_UNIT_YEAR) {
			investDays = 360 * termValue;
		} else {
			investDays = termValue;
		}
		return investDays;
	}

	/**
	 * @Description : 利率计算 投资利率 x 投资时间 x 投资金额 / (12个月或 360 * 100), 保留两位小数
	 * @Method_Name : calcOperate
	 * @param rate
	 *            利率
	 * @param money
	 *            金额
	 * @param termValue
	 *            期数
	 * @param divisor
	 * @return : BigDecimal
	 * @Creation Date : 2017年6月14日 下午8:21:24
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private static BigDecimal calcOperate(BigDecimal rate, BigDecimal money, int termValue, int divisor) {
		return rate.multiply(BigDecimal.valueOf(termValue)).multiply(money).divide(BigDecimal.valueOf(divisor), 2,
				BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 获得期数单位
	 * @Method_Name : getDivisor
	 * @param termUnit
	 *            期数单位
	 * @return : int
	 * @Creation Date : 2017年6月15日 下午3:45:36
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private static int getDivisor(int termUnit) {
		int divisor;
		if (termUnit == BID_TERM_UNIT_DAY) {
			divisor = 36000;
		} else {
			divisor = 1200;
		}
		return divisor;
	}

	/**
	 * @Description : 获得期数
	 * @Method_Name : getTermValue
	 * @param termUnit
	 *            期数单位
	 * @param termValue
	 *            期数值
	 * @return : int
	 * @Creation Date : 2017年6月15日 下午3:45:25
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private static int getTermValue(int termUnit, int termValue) {
		if (termUnit != BID_TERM_UNIT_DAY) {
			return termUnit == BID_TERM_UNIT_YEAR ? termValue * 12 : termValue;
		}
		return termValue;
	}

	/**
	 * @Description : 计算年化率（保留两位小数，四舍五入） 计算公式： 【 每日收益】 = （预期收益）/（总金额*期限） 【年化收益】
	 *              = 每日收益 * 360
	 * @Method_Name : calAnnualizedRate
	 * @param receiveIncome
	 *            预期收益
	 * @param transferMoney
	 *            总金额
	 * @param limit
	 *            期限值（单位：天）
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月7日 下午2:17:51
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal calAnnualizedRate(BigDecimal receiveIncome, BigDecimal transferMoney, int limit) {
		return receiveIncome.multiply(new BigDecimal(360)).divide(transferMoney).divide(new BigDecimal(limit))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 计算每日利息（每年按照360天，保留两位小数，四舍五入） 计算公式：(总金额*年化率)/(360*100)
	 * @Method_Name : calDayInterest
	 * @param money
	 * @param rate
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月7日 下午4:02:22
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal calDayInterest(BigDecimal money, BigDecimal rate) {
		return money.multiply(rate).divide(new BigDecimal(36000)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 计算利息每年按照360天，保留两位小数，接近零的舍入模式。
	 *              在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1,即截短)。）
	 *              计算公式：(总金额*年化率)/(360*100)
	 * @Method_Name : calDayOfInterest;
	 * @param money
	 *            金额
	 * @param rate
	 *            利率
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年8月4日 下午3:45:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static BigDecimal calDayOfInterest(BigDecimal money, BigDecimal rate) {
		return money.multiply(rate).divide(new BigDecimal(36000), 2, RoundingMode.DOWN);
	}

	/**
	 * @Description : 计算预期收益（根据日利息 每年按照360天，保留两位小数，四舍五入）
	 *              计算公式：(总金额*年化率*持有天数)/(360*100)
	 * @Method_Name : calExpectInterest
	 * @param money
	 * @param rate
	 * @param days
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月7日 下午5:33:26
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal calExpectInterest(BigDecimal money, BigDecimal rate, int days) {
		return calDayInterest(money, rate).multiply(new BigDecimal(days));
	}

	/**
	 * 
	 * @Description : 计算利率差 r1>r2
	 * @Method_Name : calInterestDiffer
	 * @param money
	 * @param r1
	 * @param r2
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月19日 下午4:57:39
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static BigDecimal calInterestDiffer(BigDecimal money, BigDecimal r1, BigDecimal r2) {
		return r1.subtract(r2).multiply(money).divide(new BigDecimal(36000), 2, RoundingMode.DOWN);
	}

	/**
	 * 
	 * @Description : 计算折价率
	 * @Method_Name : calNiggerRate
	 * @param termUnit
	 * @param termValue
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月15日 下午4:23:44
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal calNiggerRate(int termUnit, int termValue) {
		BigDecimal result = new BigDecimal(1);
		if (BID_TERM_UNIT_DAY == termUnit) {
			result = new BigDecimal(termValue).divide(new BigDecimal(360), 2, BigDecimal.ROUND_HALF_UP);
		} else if (BID_TERM_UNIT_MONTH == termUnit) {
			result = new BigDecimal(termValue).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}

	/***
	 * 
	 * @Description : 计算折标金额
	 * @Method_Name : calFoldAmount
	 * @param money
	 * @param rate：折标系数
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2018年3月15日 下午4:39:38
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static BigDecimal calFoldAmount(BigDecimal money, BigDecimal rate) {
		return money.multiply(rate).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 折标系数
	 * @Method_Name : calFoldRatio;
	 * @param termUnit
	 * @param termValue
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2018年6月14日 下午6:18:46;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static BigDecimal calFoldRatio(int termUnit, int termValue) {
		return BigDecimal.valueOf(getTermValue(termUnit, termValue)).divide(new BigDecimal(12), 3,
				BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description : 推荐奖计算公式
	 * @Method_Name : calRecommendAmount;
	 * @param money
	 *            投资金额
	 * @param bidRate
	 *            标的利率
	 * @param termUnit
	 *            单位
	 * @param termValue
	 *            单位值
	 * @param recommendRate
	 *            执行利率
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2018年5月25日 下午2:28:36;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static BigDecimal calRecommendAmount(BigDecimal money, int termUnit, int termValue,
			BigDecimal recommendRate) {
		return money.multiply(BigDecimal.valueOf(getTermValue(termUnit, termValue))).multiply(recommendRate)
				.divide(BigDecimal.valueOf(1200), 2, BigDecimal.ROUND_HALF_UP);
	}

	public static void main(String[] args) {
	    System.out.println(calRecommendAmount(new BigDecimal(7000),2,3,new BigDecimal(0.2)));
		System.out.println(calDayInterest(BigDecimal.valueOf(500), BigDecimal.valueOf(4.5)));
	}
}
