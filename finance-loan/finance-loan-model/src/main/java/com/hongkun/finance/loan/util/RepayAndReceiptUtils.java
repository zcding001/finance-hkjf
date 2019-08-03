package com.hongkun.finance.loan.util;

import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.BidCommonPlanVo;
import com.hongkun.finance.loan.model.vo.BidInvestVo;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : 还款计划&回款计划工具类
 * @Project : finance-loan-model
 * @Program Name : com.hongkun.finance.loan.util.RepayAndReceiptUtils.java
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class RepayAndReceiptUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(RepayAndReceiptUtils.class);

	/**
	 * @Description : 生成还款计划&回款计划
	 * @Method_Name : initRepayPlan
	 * @param planVo
	 *            标的vo对象， 包含标的一些信息
	 * @param invests
	 *            投资vo对象，包含投资用户id&投资金额
	 * @param  initFlag
	 *         1--生成还款计划&回款计划
	 *         2-只生成回款计划
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月5日 上午9:09:01
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static ResponseEntity<?> initRepayPlan(BidCommonPlanVo planVo, List<BidInvestVo> invests,int initFlag) {
		logger.info("RepayAndReceiptUtils.initRepayPlan,  生成还款计划&回款计划,  planVo: {}", planVo.toString());
		try {
			ResponseEntity<?> result = new ResponseEntity<>();
			List<BidReceiptPlan> receiptList = new ArrayList<BidReceiptPlan>();
			List<BidRepayPlan> repayList = null;
			switch (planVo.getRepayType()) {
			case RepayConstants.REPAYTYPE_PRINCIPAL_INTEREST_EQ:
				if (planVo.getTermUnit() != RepayConstants.BID_TERM_UNIT_MONTH) {
					result = new ResponseEntity<String>(Constants.ERROR, "标的期限类型和标的还款方式不相符");
					break;
				}
				if(initFlag == RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT){
					// 还款计划
					repayList = getRepayPlanForPrincipalInterestEq(planVo);
				}
				// 回款计划
				for (BidInvestVo bidInvest : invests) {
					planVo.setRegUserId(bidInvest.getRegUserId());
					planVo.setTotalAmount(bidInvest.getInvestAmount());
					planVo.setInvestId(bidInvest.getId());
					List<BidReceiptPlan> list = getReceiptPlanForPrincipalInterestEq(planVo, bidInvest.getCouponWorthJ());
					receiptList.addAll(list);
				}
				result.getParams().put("repayPlanList", repayList);
				result.getParams().put("recieptPlanList", receiptList);
				result.setResStatus(Constants.SUCCESS);
				break;
			case RepayConstants.REPAYTYPE_ONECE_REPAYMENT:
			case RepayConstants.REPAYTYPE_INTEREST_END_PRINCIPAL_RECOVERY:
				if(initFlag == RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT){
					repayList = getRepayPlanForOneceRepayment(planVo);
				}

				// 回款计划
				for (BidInvestVo bidInvest : invests) {
					planVo.setInvestId(bidInvest.getId());
					planVo.setRegUserId(bidInvest.getRegUserId());
					planVo.setTotalAmount(bidInvest.getInvestAmount());
					BidReceiptPlan receiptPlan = getReceiptPlanForOneceRepayment(planVo, bidInvest.getCouponWorthJ());
					receiptList.add(receiptPlan);
				}

				result.getParams().put("repayPlanList", repayList);
				result.getParams().put("recieptPlanList", receiptList);
				result.setResStatus(Constants.SUCCESS);
				break;
			case RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END:
			case RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_RECOVERY:
			case RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE:

				if (planVo.getTermUnit() != RepayConstants.BID_TERM_UNIT_MONTH) {
					result = new ResponseEntity<String>(Constants.ERROR, "标的期限类型和标的还款方式不相符");
					break;
				}
				if(initFlag == RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT){
					repayList = getRepayPlanForInterestMonth(planVo);
				}

				// 回款计划
				for (BidInvestVo bidInvest : invests) {
					planVo.setInvestId(bidInvest.getId());
					planVo.setRegUserId(bidInvest.getRegUserId());
					planVo.setTotalAmount(bidInvest.getInvestAmount());
					List<BidReceiptPlan> list = getReceiptPlanForInterestMonth(planVo, bidInvest.getCouponWorthJ());
					receiptList.addAll(list);
				}
				if (RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE == planVo.getRepayType()) {
					// 生成最后一条企业账户的回款计划
					BidReceiptPlan receiptPlan = new BidReceiptPlan();
					receiptPlan.setBidId(planVo.getBidId());
					receiptPlan.setPeriods(planVo.getTermValue());
					receiptPlan.setCapitalAmount(planVo.getTotalAmount());
					receiptPlan.setAmount(planVo.getTotalAmount());
					receiptPlan.setRegUserId(planVo.getCompanyId());
					receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
					receiptPlan.setPlanTime(DateUtils.addMonth(
							planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), planVo.getTermValue()));
					receiptList.add(receiptPlan);
				}
				result.getParams().put("repayPlanList", repayList);
				result.getParams().put("recieptPlanList", receiptList);
				result.setResStatus(Constants.SUCCESS);
				break;
			case RepayConstants.REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH:
				if (planVo.getTermUnit() != RepayConstants.BID_TERM_UNIT_MONTH) {
					result = new ResponseEntity<String>(Constants.ERROR, "标的期限类型和标的还款方式不相符");
					break;
				}
				if(initFlag == RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT){
					repayList = getRepayPlanForInterestMonthAndCalDay(planVo);
				}
				// 回款计划
				for (BidInvestVo bidInvest : invests) {
					planVo.setInvestId(bidInvest.getId());
					planVo.setRegUserId(bidInvest.getRegUserId());
					planVo.setTotalAmount(bidInvest.getInvestAmount());
					List<BidReceiptPlan> list = getReceiptPlanForInterestMonthAndCalDay(planVo, bidInvest.getCouponWorthJ());
					receiptList.addAll(list);
				}
				result.getParams().put("repayPlanList", repayList);
				result.getParams().put("recieptPlanList", receiptList);
				result.setResStatus(Constants.SUCCESS);
				break;
			default:
				break;
			}
			return result;
		} catch (Exception e) {
			logger.error("RepayAndReceiptUtils.initRepayPlan,  生成还款计划&回款计划异常,  planVo: {}, 异常信息: ", planVo.toString(), e);
			throw new GeneralException("生成还款计划&回款计划异常");
		}
		
	}

	/**
	 * @Description : 生成回款计划：等额本息
	 * @Method_Name : getReceiptPlanForPrincipalInterestEq
	 * @param planVo
	 * @param increaseRate
	 * @return
	 * @return : List<BidReceiptPlan>
	 * @Creation Date : 2017年7月5日 上午8:52:03
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static List<BidReceiptPlan> getReceiptPlanForPrincipalInterestEq(BidCommonPlanVo planVo,
			BigDecimal increaseRate) {
		logger.info("RepayAndReceiptUtils.getReceiptPlanForPrincipalInterestEq, 生成回款计划：等额本息, planVo: {}, increaseRate: {}",planVo.toString(),increaseRate);
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		int regUserId = planVo.getRegUserId();
		int investId = planVo.getInvestId();

		List<BidReceiptPlan> list = new ArrayList<BidReceiptPlan>();
		// 每个月还款本金
		BigDecimal capitalAmount = totalAmount.divide(new BigDecimal(termValue), 2,BigDecimal.ROUND_HALF_UP);
		// 每个月还款利息
		BigDecimal interestAmount = totalAmount.multiply(interestRate).divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		increaseRate = CompareUtil.gtZero(increaseRate)?increaseRate:BigDecimal.ZERO;
		BigDecimal increaseAmount = totalAmount.multiply(increaseRate).divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		for (int i = 1; i <= termValue; i++) {
			BidReceiptPlan receiptPlan = new BidReceiptPlan();
			if (i == termValue && i > 1) {
				// 最后一期 本金=总本金 - 总本金*（期数-1） 利息和服务费同理
				capitalAmount = totalAmount.subtract(capitalAmount.multiply(new BigDecimal(i - 1)));
				interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP).subtract(interestAmount.multiply(new BigDecimal(i - 1)));
				increaseAmount = totalAmount.multiply(increaseRate).multiply(new BigDecimal(termValue))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP).subtract(increaseAmount.multiply(new BigDecimal(i - 1)));
			}
			receiptPlan.setInvestId(investId);
			receiptPlan.setBidId(bidId);
			receiptPlan.setPeriods(i);
			receiptPlan.setCapitalAmount(capitalAmount);
			receiptPlan.setInterestAmount(interestAmount);
			receiptPlan.setAmount(capitalAmount.add(interestAmount).add(increaseAmount));
			receiptPlan.setIncreaseAmount(increaseAmount);
			receiptPlan.setRegUserId(regUserId);
			receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			receiptPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(receiptPlan);
		}
		return list;
	}

	/**
	 * @Description : 生成回款计划：一次性本息
	 * @Method_Name : getReceiptPlanForOneceRepayment
	 * @param planVo
	 * @param increaseRate
	 * @return
	 * @return : BidReceiptPlan
	 * @Creation Date : 2017年7月5日 上午8:52:23
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static BidReceiptPlan getReceiptPlanForOneceRepayment(BidCommonPlanVo planVo, BigDecimal increaseRate) {
		logger.info("RepayAndReceiptUtils.getReceiptPlanForPrincipalInterestEq, 生成回款计划: 一次性本息, planVo: {}, increaseRate: {}",planVo.toString(),increaseRate);
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		Integer regUserId = planVo.getRegUserId();
		Integer investId = planVo.getInvestId();

		BidReceiptPlan receiptPlan = new BidReceiptPlan();
		int allTerm = planVo.getTermUnit() == 2?12:360;
		BigDecimal interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
				.divide(new BigDecimal(allTerm*100), 2,BigDecimal.ROUND_HALF_UP);
		increaseRate = CompareUtil.gtZero(increaseRate)?increaseRate:BigDecimal.ZERO;
		BigDecimal increaseAmount = totalAmount.multiply(increaseRate).multiply(new BigDecimal(termValue))
				.divide(new BigDecimal(allTerm*100), 2,BigDecimal.ROUND_HALF_UP);
		receiptPlan.setInvestId(investId);
		receiptPlan.setBidId(bidId);
		receiptPlan.setPeriods(1);
		if (RepayConstants.REPAYTYPE_INTEREST_END_PRINCIPAL_RECOVERY == planVo.getRepayType()) {
			totalAmount = new BigDecimal(0);
		}
		receiptPlan.setCapitalAmount(totalAmount);
		receiptPlan.setInterestAmount(interestAmount);
		receiptPlan.setAmount(totalAmount.add(interestAmount).add(increaseAmount));
		receiptPlan.setIncreaseAmount(increaseAmount);
		receiptPlan.setRegUserId(regUserId);
		receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
		Date planTime = null;
			if (planVo.getTermUnit() == 2) {
			planTime = DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()),
					termValue);
		} else if (planVo.getTermUnit() == 3) {
            planTime = DateUtils.addDays(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), termValue);
		}
		receiptPlan.setPlanTime(planTime);
		return receiptPlan;
	}

	/**
	 * @Description : 生成回款计划：按月付息
	 * @Method_Name : getReceiptPlanForInterestMonth
	 * @param planVo
	 * @param increaseRate
	 * @return
	 * @return : List<BidReceiptPlan>
	 * @Creation Date : 2017年7月5日 上午8:52:42
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static List<BidReceiptPlan> getReceiptPlanForInterestMonth(BidCommonPlanVo planVo,
			BigDecimal increaseRate) {
		logger.info("RepayAndReceiptUtils.getReceiptPlanForInterestMonth, 生成回款计划: 按月付息, planVo: {}, increaseRate: {}",planVo.toString(),increaseRate);
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		Integer regUserId = planVo.getRegUserId();
		Integer investId = planVo.getInvestId();

		List<BidReceiptPlan> list = new ArrayList<BidReceiptPlan>();
		BigDecimal capitalAmount = new BigDecimal(0);
		// 每个月还款利息
		BigDecimal interestAmount = totalAmount.multiply(interestRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		// 每个月还款加息
		increaseRate = CompareUtil.gtZero(increaseRate)?increaseRate:BigDecimal.ZERO;
		BigDecimal increaseAmount = totalAmount.multiply(increaseRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		for (int i = 1; i <= termValue; i++) {
			BidReceiptPlan receiptPlan = new BidReceiptPlan();
			if (i == termValue) {
				capitalAmount = totalAmount;
				interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
						.subtract(interestAmount.multiply(new BigDecimal(i - 1)));
				increaseAmount = totalAmount.multiply(increaseRate).multiply(new BigDecimal(termValue))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
						.subtract(increaseAmount.multiply(new BigDecimal(i - 1)));
			}
			receiptPlan.setInvestId(investId);
			receiptPlan.setBidId(bidId);
			receiptPlan.setPeriods(i);
			if (RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE == planVo.getRepayType()
					|| RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_RECOVERY == planVo.getRepayType()) {
				capitalAmount = new BigDecimal(0);
			}
			receiptPlan.setCapitalAmount(capitalAmount);
			receiptPlan.setInterestAmount(interestAmount);
			receiptPlan.setIncreaseAmount(increaseAmount);
			receiptPlan.setAmount(capitalAmount.add(interestAmount).add(increaseAmount));
			receiptPlan.setRegUserId(regUserId);
			receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			receiptPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(receiptPlan);
		}
		return list;
	}


	/**
	*  @Description    ：生成回款计划--按日计息，按月还款，到期还本
	*  @Method_Name    ：getReceiptPlanForInterestMonthAndCalDay
	*  @param planVo
	*  @param increaseRate
	*  @return java.util.List<com.hongkun.finance.loan.model.BidReceiptPlan>
	*  @Creation Date  ：2018/4/24
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private static List<BidReceiptPlan> getReceiptPlanForInterestMonthAndCalDay(BidCommonPlanVo planVo,BigDecimal increaseRate) {
		logger.info("RepayAndReceiptUtils.getReceiptPlanForInterestMonth, 生成回款计划: 按月付息, planVo: {}, increaseRate: {}",planVo.toString(),increaseRate);
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		int regUserId = planVo.getRegUserId();
		int investId = planVo.getInvestId();

		List<BidReceiptPlan> list = new ArrayList<BidReceiptPlan>();
		BigDecimal capitalAmount = new BigDecimal(0);
		// 每个月还款加息
		increaseRate = CompareUtil.gtZero(increaseRate)?increaseRate:BigDecimal.ZERO;
		int calDay = 0;
		for (int i = 1; i <= termValue; i++) {
			BidReceiptPlan receiptPlan = new BidReceiptPlan();
			//本月天数 = 本期结束日期 - 放款日期 - 已处理过的天数
			Date endDate = DateUtils.addMonth(planVo.getLendingTime(),i);
			int thisMonthDay = DateUtils.getDaysBetween(planVo.getLendingTime(),endDate) - calDay;
			BigDecimal interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(thisMonthDay)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP);
			BigDecimal increaseAmount = totalAmount.multiply(increaseRate).multiply(new BigDecimal(thisMonthDay)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP);
			if (i == termValue) {
				capitalAmount = totalAmount;
			}
			receiptPlan.setInvestId(investId);
			receiptPlan.setBidId(bidId);
			receiptPlan.setPeriods(i);
			receiptPlan.setCapitalAmount(capitalAmount);
			receiptPlan.setInterestAmount(interestAmount);
			receiptPlan.setIncreaseAmount(increaseAmount);
			receiptPlan.setAmount(capitalAmount.add(interestAmount).add(increaseAmount));
			receiptPlan.setRegUserId(regUserId);
			receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			receiptPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(receiptPlan);
			calDay = calDay + thisMonthDay;
		}
		return list;
	}

	/**
	 * 
	 * @Description : 根据剩余回款计划&新的投资金额，生成新的回款计划
	 * @Method_Name : getReceiptPlanForInterestMonth
	 * @param oldBidReceiptPlan
	 * @param newInvestAmount
	 * @param termValue
	 * @param increaseRate
	 * @param yearrateDifferenceRate
	 * @return
	 * @return : List<BidReceiptPlan>
	 * @Creation Date : 2017年7月26日 上午9:43:41
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static List<BidReceiptPlan> getReceiptPlanForInterestMonth(List<BidReceiptPlan> oldBidReceiptPlan,
			BigDecimal newInvestAmount, Integer termValue, BigDecimal interestRate, 
			BigDecimal increaseRate,BigDecimal yearrateDifferenceRate) {
		//剩余期数 = 回款期数
		int bidResidueTermValue = oldBidReceiptPlan.size();
		List<BidReceiptPlan> newBidReceiptPlan = new ArrayList<BidReceiptPlan>();
		// 每个月还款利息
		BigDecimal interestAmount = newInvestAmount.multiply(interestRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		// 加息收益利息
		BigDecimal increaseAmount = BigDecimal.ZERO;
		if (CompareUtil.gtZero(increaseRate)) {
			increaseAmount = newInvestAmount.multiply(increaseRate)
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		}
		// 利息差=购买金额*（年利率差）/12 即每月的利息差
		BigDecimal interestDiffAmount = BigDecimal.ZERO;
		if (CompareUtil.gtZero(yearrateDifferenceRate)) {
			interestDiffAmount = newInvestAmount.multiply(yearrateDifferenceRate)
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		}
		for (BidReceiptPlan bidReceiptPlan : oldBidReceiptPlan) {
			bidReceiptPlan.setModifyTime(new Date());
			if (termValue == bidReceiptPlan.getPeriods()) {
				interestAmount = newInvestAmount.multiply(interestRate).multiply(new BigDecimal(bidResidueTermValue))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
						.subtract(interestAmount.multiply(new BigDecimal(bidResidueTermValue - 1)));
				if (CompareUtil.gtZero(increaseAmount)) {
					increaseAmount = newInvestAmount.multiply(increaseRate).multiply(new BigDecimal(bidResidueTermValue))
							.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
							.subtract(increaseAmount.multiply(new BigDecimal(bidResidueTermValue - 1)));
				}
				if (CompareUtil.gtZero(increaseAmount)) {
					interestDiffAmount = newInvestAmount.multiply(yearrateDifferenceRate)
							.multiply(new BigDecimal(termValue))
							.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
							.subtract(interestAmount.multiply(new BigDecimal(oldBidReceiptPlan.size() - 1)));
				}
				bidReceiptPlan.setCapitalAmount(newInvestAmount);
			}
			
			bidReceiptPlan.setInterestAmount(interestAmount);
			bidReceiptPlan.setAmount(interestAmount.add(increaseAmount).add(increaseAmount).add(bidReceiptPlan.getCapitalAmount()));
			bidReceiptPlan.setYearrateDifferenceMoney(interestDiffAmount);
			if(CompareUtil.lteZero(bidReceiptPlan.getAmount()) && CompareUtil.gtZero(yearrateDifferenceRate)) {
				bidReceiptPlan.setState(RepayConstants.RECEIPT_STATE_FINISH);	
			}else {
				bidReceiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			}
			newBidReceiptPlan.add(bidReceiptPlan);
		}

		return newBidReceiptPlan;
	}

	/**
	 * 
	 * @Description : 生产回款计划 每月付息到期还本
	 * @Method_Name : getReceiptPlanForInterestMonth
	 * @param planVo
	 * @param increaseRate：加息利率
	 * @param yearrateDifferenceRate：利息差利率
	 * @return
	 * @return : List<BidReceiptPlan>
	 * @Creation Date : 2017年7月25日 下午5:23:05
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static List<BidReceiptPlan> getReceiptPlanForInterestMonth(BidCommonPlanVo planVo, BigDecimal increaseRate,
			BigDecimal yearrateDifferenceRate, int startBuildPeriod) {
		logger.info("RepayAndReceiptUtils.getReceiptPlanForInterestMonth  planVo:{},increaseRate:{},yearrateDifferenceRate:{},"
				+ "startBuildPeriod:{}",planVo.toString(),increaseRate,yearrateDifferenceRate,startBuildPeriod);
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		int regUserId = planVo.getRegUserId();
		int investId = planVo.getInvestId();

		List<BidReceiptPlan> list = new ArrayList<BidReceiptPlan>();
		BigDecimal capitalAmount = new BigDecimal(0);
		// 每个月还款利息
		BigDecimal interestAmount = totalAmount.multiply(interestRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		// 加息收益利息
		BigDecimal increaseAmount = BigDecimal.ZERO;
		if (CompareUtil.gtZero(increaseRate)) {
			increaseAmount = totalAmount.multiply(increaseRate)
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		}
		// 利息差=购买金额*（年利率差）/12 即每月的利息差
		BigDecimal interestDiffAmount = totalAmount.multiply(yearrateDifferenceRate)
				.divide(new BigDecimal(100 * 12),2,BigDecimal.ROUND_HALF_UP);
		int i = termValue - startBuildPeriod + 1;
		for (; i <= termValue; i++) {
			BidReceiptPlan receiptPlan = new BidReceiptPlan();
			if (i == termValue) {
				capitalAmount = totalAmount;
				interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(startBuildPeriod))
						.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
						.subtract(interestAmount.multiply(new BigDecimal(i - 1)));
			}
			receiptPlan.setInvestId(investId);
			receiptPlan.setBidId(bidId);
			receiptPlan.setPeriods(i);
			if (RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE == planVo.getRepayType()
					|| RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_RECOVERY == planVo.getRepayType()) {
				capitalAmount = new BigDecimal(0);
			}
			receiptPlan.setCapitalAmount(capitalAmount);
			receiptPlan.setInterestAmount(interestAmount);
			receiptPlan.setAmount(capitalAmount.add(interestAmount).add(increaseAmount));
			receiptPlan.setIncreaseAmount(increaseAmount);
			receiptPlan.setRegUserId(regUserId);
			receiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			receiptPlan.setYearrateDifferenceMoney(interestDiffAmount);
			receiptPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(receiptPlan);
		}
		return list;
	}

	/**
	 * @Description : 生成还款计划：等额本息
	 * @Method_Name : getRepayPlanForPrincipalInterestEq
	 * @param planVo
	 * @return
	 * @return : List<BidRepayPlan>
	 * @Creation Date : 2017年7月5日 上午9:05:58
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static List<BidRepayPlan> getRepayPlanForPrincipalInterestEq(BidCommonPlanVo planVo) {
		logger.info("RepayAndReceiptUtils.getRepayPlanForPrincipalInterestEq, 生成还款计划, planVo: {}",planVo.toString());
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		BigDecimal serviceRate = planVo.getServiceRate();
		int regUserId = planVo.getRegUserId();
		List<BidRepayPlan> list = new ArrayList<BidRepayPlan>();
		// 每个月还款本金
		BigDecimal capitalAmount = totalAmount.divide(new BigDecimal(termValue), 2,BigDecimal.ROUND_HALF_UP);
		// 总利息 = 本金*年化率/100/12*期数
		BigDecimal allInterest = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		// 每个月还款利息 = 本金*年化率/100/12
		BigDecimal interestAmount = totalAmount.multiply(interestRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		BigDecimal serviceCharge = new BigDecimal(0);
		// 每个月服务费
		if (serviceRate != null && CompareUtil.gteZero(serviceRate)) {
			serviceCharge = totalAmount.multiply(serviceRate)
					.divide(new BigDecimal(1200), 2,BigDecimal.ROUND_DOWN);
		}
		for (int i = 1; i <= termValue; i++) {
			BidRepayPlan repayPlan = new BidRepayPlan();
			if (i == termValue && i > 1) {
				// 最后一期 本金=总本金 - 总本金*（期数-1） 利息和服务费同理
				capitalAmount = totalAmount.subtract(capitalAmount.multiply(new BigDecimal(i - 1)));
				interestAmount = allInterest.subtract(interestAmount.multiply(new BigDecimal(i - 1)));
				if (CompareUtil.gtZero(serviceCharge)) {
					serviceCharge = totalAmount.multiply(serviceRate).multiply(new BigDecimal(termValue))
							.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
							.subtract(serviceCharge.multiply(new BigDecimal(i - 1)));
				}

			}
			repayPlan.setBidId(bidId);
			repayPlan.setPeriods(i);
			repayPlan.setCapitalAmount(capitalAmount);
			repayPlan.setInterestAmount(interestAmount);
			repayPlan.setServiceCharge(serviceCharge);
			repayPlan.setAmount(capitalAmount.add(interestAmount).add(serviceCharge));
			// 剩余回款本金 = 总本金 - 已还本金 （已还本金=每月本金*i-1）
			repayPlan.setResidueAmount(totalAmount.subtract(capitalAmount.multiply(new BigDecimal(i - 1))));
			repayPlan.setRegUserId(regUserId);
			repayPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
			repayPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(repayPlan);
		}
		return list;
	}

	/**
	 * @Description : 生成还款计划：一次性付息
	 * @Method_Name : getOneceRepayment
	 * @param planVo
	 * @return
	 * @return : List<BidRepayPlan>
	 * @Creation Date : 2017年7月5日 上午9:06:16
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static List<BidRepayPlan> getRepayPlanForOneceRepayment(BidCommonPlanVo planVo) {
		logger.info("RepayAndReceiptUtils.getRepayPlanForOneceRepayment, 生成还款计划：一次性付息, planVo:{}",planVo.toString());
		List<BidRepayPlan> result = new ArrayList<BidRepayPlan>();
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		BigDecimal serviceRate = planVo.getServiceRate();
		int regUserId = planVo.getRegUserId();
		BidRepayPlan repayPlan = new BidRepayPlan();
		BigDecimal interestAmount = null;
		if (planVo.getTermUnit() == 2) {
			interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		} else if (planVo.getTermUnit() == 3) {
			interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
					.divide(new BigDecimal(100 * 360), 2,BigDecimal.ROUND_HALF_UP);
		}
		BigDecimal serviceCharge = BigDecimal.ZERO;
		if (CompareUtil.gtZero(serviceRate)){
			serviceCharge = totalAmount.multiply(serviceRate).multiply(new BigDecimal(termValue))
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		}
		repayPlan.setBidId(bidId);
		repayPlan.setPeriods(1);
		repayPlan.setCapitalAmount(totalAmount);
		repayPlan.setInterestAmount(interestAmount);
		repayPlan.setServiceCharge(serviceCharge);
		repayPlan.setAmount(totalAmount.add(interestAmount).add(serviceCharge));
		repayPlan.setRegUserId(regUserId);
		repayPlan.setState(RepayConstants.REPAY_STATE_NONE);
		Date planTime = null;
		if (planVo.getTermUnit() == 2) {
			planTime = DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()),
					termValue);
		} else if (planVo.getTermUnit() == 3) {
			planTime = DateUtils.addDays(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()),
					termValue);
		}
		repayPlan.setPlanTime(planTime);
		result.add(repayPlan);
		return result;
	}

	/**
	 * @Description : 生成还款计划：按月付息
	 * @Method_Name : getInterestMonth
	 * @param planVo
	 * @return
	 * @return : List<BidRepayPlan>
	 * @Creation Date : 2017年7月5日 上午9:06:48
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static List<BidRepayPlan> getRepayPlanForInterestMonth(BidCommonPlanVo planVo) {
		logger.info("RepayAndReceiptUtils.getRepayPlanForInterestMonth, 生成还款计划：按月付息, planVo: {}",planVo.toString());
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		BigDecimal serviceRate = planVo.getServiceRate();
		int regUserId = planVo.getRegUserId();

		List<BidRepayPlan> list = new ArrayList<BidRepayPlan>();
		BigDecimal capitalAmount = new BigDecimal(0);

		// 总利息 = 本金*年化率/100/12*期数
		BigDecimal allInterest = totalAmount.multiply(interestRate).multiply(new BigDecimal(termValue))
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP);
		// 每个月还款利息
		BigDecimal interestAmount = totalAmount.multiply(interestRate)
				.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		BigDecimal serviceCharge = new BigDecimal(0);
		// 每个月服务费
		if (serviceRate != null && CompareUtil.gteZero(serviceRate)) {
			serviceCharge = totalAmount.multiply(serviceRate)
					.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_DOWN);
		}
		for (int i = 1; i <= termValue; i++) {
			BidRepayPlan repayPlan = new BidRepayPlan();
			repayPlan.setResidueAmount(totalAmount);
			if (i == termValue) {
				repayPlan.setResidueAmount(new BigDecimal(0));
				capitalAmount = totalAmount;
				interestAmount = allInterest.subtract(interestAmount.multiply(new BigDecimal(i - 1)));
				if (CompareUtil.gtZero(serviceCharge)) {
					serviceCharge = totalAmount.multiply(serviceRate).multiply(new BigDecimal(termValue))
							.divide(new BigDecimal(100 * 12), 2,BigDecimal.ROUND_HALF_UP)
							.subtract(serviceCharge.multiply(new BigDecimal(i - 1)));
				}
			}
			repayPlan.setBidId(bidId);
			repayPlan.setPeriods(i);
			repayPlan.setCapitalAmount(capitalAmount);
			repayPlan.setInterestAmount(interestAmount);
			repayPlan.setServiceCharge(serviceCharge);
			repayPlan.setActualTime(null);
			repayPlan.setAmount(capitalAmount.add(interestAmount).add(serviceCharge));
			repayPlan.setRegUserId(regUserId);
			repayPlan.setState(RepayConstants.REPAY_STATE_NONE);
			repayPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(repayPlan);
		}
		return list;
	}

	/**
	*  @Description    ：生成还款计划--按日计息，按月还款，到期还本
	*  @Method_Name    ：getRepayPlanForInterestMonthAndCalDay
	*  @param planVo
	*  @return java.util.List<com.hongkun.finance.loan.model.BidRepayPlan>
	*  @Creation Date  ：2018/4/24
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private static List<BidRepayPlan> getRepayPlanForInterestMonthAndCalDay(BidCommonPlanVo planVo) {
		logger.info("RepayAndReceiptUtils.getRepayPlanForInterestMonthAndCalDay, 生成还款计划: 按月付息, planVo: {}",planVo.toString());
		int bidId = planVo.getBidId();
		int termValue = planVo.getTermValue();
		BigDecimal totalAmount = planVo.getTotalAmount();
		BigDecimal interestRate = planVo.getInterestRate();
		BigDecimal serviceRate = planVo.getServiceRate();
		int regUserId = planVo.getRegUserId();

		List<BidRepayPlan> list = new ArrayList<BidRepayPlan>();
		BigDecimal capitalAmount = new BigDecimal(0);
		int calDay = 0 ;
		for (int i = 1; i <= termValue; i++) {
			BidRepayPlan repayPlan = new BidRepayPlan();
			//本月天数 = 本期结束日期 - 放款日期 - 已处理过的天数
			Date endDate = DateUtils.addMonth(planVo.getLendingTime(),i);
			int thisMonthDay = DateUtils.getDaysBetween(planVo.getLendingTime(),endDate) - calDay;
			BigDecimal interestAmount = totalAmount.multiply(interestRate).multiply(new BigDecimal(thisMonthDay)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP);
			BigDecimal serviceCharge = BigDecimal.ZERO;
			if (CompareUtil.gtZero(serviceRate)){
				serviceCharge = totalAmount.multiply(serviceRate).multiply(new BigDecimal(thisMonthDay)).divide(new BigDecimal(36000), 2,BigDecimal.ROUND_HALF_UP);
			}
			repayPlan.setResidueAmount(totalAmount);
			repayPlan.setBidId(bidId);
			repayPlan.setPeriods(i);
			if(i == termValue){
				capitalAmount = totalAmount;
			}
			repayPlan.setCapitalAmount(capitalAmount);
			repayPlan.setInterestAmount(interestAmount);
			repayPlan.setServiceCharge(serviceCharge);
			repayPlan.setActualTime(null);
			repayPlan.setAmount(capitalAmount.add(interestAmount).add(serviceCharge));
			repayPlan.setRegUserId(regUserId);
			repayPlan.setState(RepayConstants.REPAY_STATE_NONE);
			repayPlan.setPlanTime(
					DateUtils.addMonth(planVo.getLendingTime() == null ? new Date() : DateUtils.getLastTimeOfDay(planVo.getLendingTime()), i));
			list.add(repayPlan);
			calDay = calDay + thisMonthDay;
		}
		return list;
	}
}
