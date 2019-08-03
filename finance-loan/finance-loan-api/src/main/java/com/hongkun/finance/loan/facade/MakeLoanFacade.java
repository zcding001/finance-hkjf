package com.hongkun.finance.loan.facade;

import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description   : 放款接口
 * @Project       : finance-loan-api
 * @Program Name  : com.hongkun.finance.loan.facade.MakeLoanFacade.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface MakeLoanFacade {
	/**
	 *  @Description    : 放款
	 *  @Method_Name    : makeLoans
	 *  @param bidInfoId
	 *  @param regUserId  操作用户id
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年7月4日 上午11:07:28 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> makeLoans(Integer bidInfoId,Integer regUserId);
	/**
	 *  @Description    : 生成还款计划和回款计划
	 *  @Method_Name    : initRepayPlan
	 *  @param bidInfoId
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月24日 下午3:15:19 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	ResponseEntity<?> initRepayPlans(int bidInfoId);
	
}
