package com.hongkun.finance.loan.facade.util;

import java.util.ArrayList;
import java.util.List;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.model.vo.BidCommonPlanVo;
import com.hongkun.finance.loan.model.vo.BidInvestVo;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;

/**
 * @Description   : 还款计划工具类
 * @Project       : finance-loan-service
 * @Program Name  : com.hongkun.finance.loan.facade.util.RepayPlanUtils.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class RepayPlanUtils {
	/**
	 *  @Description    : 初始化生成还款计划和回款计划的参数
	 *  @Method_Name    : initPlanVo
	 *  @param bidInfo
	 *  @param invests
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月14日 下午3:22:06 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static ResponseEntity<?> initPlanVo(BidInfo bidInfo,List<BidInvest> invests){
		BidCommonPlanVo planVo = new BidCommonPlanVo();
		planVo.setBidId(bidInfo.getId());
		planVo.setTotalAmount(bidInfo.getTotalAmount());
		planVo.setTermUnit(bidInfo.getTermUnit());
		planVo.setTermValue(bidInfo.getTermValue());
		planVo.setInterestRate(bidInfo.getInterestRate());
		planVo.setServiceRate(bidInfo.getServiceRate());
		planVo.setRegUserId(bidInfo.getBorrowerId());
		planVo.setRepayUserId(bidInfo.getRepayUserId());
		planVo.setRepayType(bidInfo.getBiddRepaymentWay());
		planVo.setCompanyId(bidInfo.getCompanyId());
		planVo.setLendingTime(bidInfo.getLendingTime());
		List<BidInvestVo> vos = new ArrayList<BidInvestVo>();
		for(BidInvest bidInvest:invests){
			BidInvestVo vo = new BidInvestVo();
			vo.setId(bidInvest.getId());
			vo.setRegUserId(bidInvest.getRegUserId());
			vo.setInvestAmount(bidInvest.getInvestAmount());
			vo.setCouponWorthJ(bidInvest.getCouponWorthJ());
			vos.add(vo);
		}
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		result.getParams().put(RepayConstants.REPAY_BIDCOMMONPLAN_VO, planVo);
		result.getParams().put(RepayConstants.REPAY_BIDINVEST_VO, vos);
		return result;
	}
}
