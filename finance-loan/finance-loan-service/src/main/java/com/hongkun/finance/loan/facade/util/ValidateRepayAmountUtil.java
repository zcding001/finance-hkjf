package com.hongkun.finance.loan.facade.util;

import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description : 根据不同还款方式，进行金额校验
 * @Project : finance-loan-model
 * @Program Name : com.hongkun.finance.loan.util.ValidateRepayAmountUtil.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class ValidateRepayAmountUtil {
	/**
	 * 
	 * @Description : 正常还款 包含逾期
	 * 
	 * @Method_Name : normalRepayMoney
	 * @param bidInfo
	 * @param repayPlan
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月28日 上午10:05:27
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static ResponseEntity<?> normalRepayMoney(BidInfoDetail bidInfoDetail, BigDecimal punishAmount,
			BigDecimal repayAmount, FinAccount account) {
		// 正常还款总金额
//		BigDecimal repayAmountSum = repayAmount;
//		BigDecimal useableMoney = account.getUseableMoney();
//		BigDecimal nowMoney = account.getNowMoney();
//		StringBuilder buffer = new StringBuilder();
//		if (CompareUtil.gtZero(punishAmount)) {
//			buffer.append("本期还款罚息金额：").append(punishAmount).append(";");
//		}
        // 不预留利息，还款金额直接从可用金额扣除
		if (bidInfoDetail.getReserveInterest() == InvestConstants.BID_DETAIL_RESERVE_INTEREST_NO && CompareUtil.gt(repayAmount, account.getUseableMoney())) {
//			buffer.append("借款人可用还款金额不足，剩余可用金额：").append(useableMoney);
//			return BaseUtil.error(buffer.toString());
            return new ResponseEntity<>(RepayConstants.REPAY_RETURN_STATE, "账户余额不足，请先充值");
		} 
		if (bidInfoDetail.getReserveInterest() == InvestConstants.BID_DETAIL_RESERVE_INTEREST_YES && CompareUtil.gt(repayAmount, account.getNowMoney())) {
//			buffer.append("借款人可用总金额不足！剩余总金额：").append(nowMoney);
//			return BaseUtil.error(buffer.toString());
            return new ResponseEntity<>(RepayConstants.REPAY_RETURN_STATE, "账户总金额不足，请先充值");
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	/**
	 * 
	 * @Description : 提前还款
	 * @Method_Name : advanceRepayMoney
	 * @param bidInfo
	 * @param repayPlan
	 * @param capital
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月28日 上午10:07:25
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static ResponseEntity<?> advanceRepayMoney(BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			BidRepayPlan repayPlan, FinAccount account, BigDecimal capital) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		if (!CompareUtil.residue(capital)) {
			return BaseUtil.error("还款金额必须是100的整数倍");
		}
		// 还款本金，非最后一期取还款计划中剩余还款本金，最后一期取还款本金
		BigDecimal repayCapital = repayPlan.getResidueAmount();
		if (CompareUtil.lteZero(repayCapital)) {
			repayCapital = repayPlan.getCapitalAmount();
		}
		if (CompareUtil.gt(capital, repayCapital)) {
			return BaseUtil.error("提前还本金额大于应换金额！应还总金额：" + repayCapital);
		}
		// 还款总金额 = 还款金额 + 利息 + 服务费
		BigDecimal repayAmount = capital;
		result.getParams().put("capital", repayAmount);
		// 利息
		BigDecimal calInterest = RepayCalcInterestUtil.calForReturnCap(bidInfo, bidInfoDetail, capital,
				repayPlan, bidInfo.getInterestRate());
		result.getParams().put("interest", calInterest);
		repayAmount = repayAmount.add(calInterest);
		result.getParams().put("serviceCharge", BigDecimal.ZERO);
		// 服务费
		if (CompareUtil.gtZero(bidInfo.getServiceRate())) {
			BigDecimal calService = RepayCalcInterestUtil.calForReturnCap(bidInfo, bidInfoDetail, capital,
					repayPlan, bidInfo.getServiceRate());
			repayAmount = repayAmount.add(calService);
			result.getParams().put("serviceCharge", calService);
		}
		result.getParams().put("repayAmount", repayAmount);
		return result; 

	}

	/**
	 * 
	 * @Description : 代扣逻辑校验
	 * @Method_Name : withHoldRepayMoney
	 * @param bidInfo
	 * @param repayPlan
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月28日 上午11:00:00
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static ResponseEntity<?> withHoldRepayMoney(BidInfoDetail bidInfoDetail, BidRepayPlan repayPlan,
			List<FinBankCard> cards) {
		// 操作人同意开通代扣并且是首次还款操作
		if (repayPlan.getPeriods() == 1) {
			// 判断操作人是否签约银行卡，如果没签约则返回信息
		}
		return null;

	}
}
