/**
 * 
 */
package com.hongkun.finance.loan.facade;

import java.math.BigDecimal;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description : 还款接口
 * @Project : finance-loan-api
 * @Program Name : com.hongkun.finance.loan.service.RepayPlanFacade.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public interface RepayFacade {

	/**
	 * 
	 * @Description : 跑批自动还款代扣
	 * @Method_Name : autoWithHoldRepay
	 * @param infoDetail
	 * @param bidInfo
	 * @param bidProduct
	 * @param repayPlan
	 * @param receiptPlanList
	 * @param regUser
	 * @param notifyUrl
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月4日 下午4:47:23
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> autoWithHoldRepay() throws Exception;

	/**
	 * 
	 * @Description : 跑批启用风险储备金还款
	 * @Method_Name : autoRiskReserveRepay
	 * @param infoDetail
	 * @param bidInfo
	 * @param bidProduct
	 * @param repayPlan
	 * @param receiptPlanList
	 * @param regUser
	 * @param notifyUrl
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月4日 下午4:47:23
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> autoRiskReserveRepay() throws Exception;

	/**
	 * 
	 * @Description : 还款接口
	 * @Method_Name : repay
	 * @param repayId 还款计划的id
	 * @param capital 还款本金
	 * @param withHoldflag 是否代扣
	 * @param regUser
	 * @param platformSourceEnums
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月12日 下午3:29:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> repay(int repayId, BigDecimal capital, int withHoldflag, RegUser regUser, PlatformSourceEnums platformSourceEnums);

	/**
	 * 
	 * @Description : 代扣异步通知
	 * @Method_Name : lianlianRepayNotice
	 * @param reqStr
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月13日 下午2:24:21
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> lianlianRepayNotice(String reqStr);
	
	/**
	 *  @Description    : 计算提前还本所需要的金额
	 *  @Method_Name    : calcAdvanceReapyAmount
	 *  @param regUserId: 还款用户
	 *  @param repayId  ： 还款计划id
	 *  @param capital	: 还款本金
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月3日 下午3:30:23 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> calcAdvanceReapyAmount(Integer regUserId, Integer repayId, BigDecimal capital);
}
