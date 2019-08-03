package com.hongkun.finance.payment.service;

import java.util.Date;

import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface FinPayCheckAccountService {
	/***
	 * @Description : 连连执行对账
	 * @Method_Name : excuteReconciliation;
	 * @param reconciliationDate
	 *            清算日期 格式为"yyyy-MM-dd"
	 * @return : void;
	 * @Creation Date : 2018年1月25日 上午10:12:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	void excuteLianLianReconciliation(String reconciliationDate);

	/**
	 * @Description : 宝付执行对账
	 * @Method_Name : excuteBaoFuReconciliation;
	 * @param reconciliationDate 
	 * 			清算日期
	 * @return : void;
	 * @Creation Date : 2018年1月25日 上午10:13:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	void excuteBaoFuReconciliation(Date settleDate);

	/**
	 * @Description : 查询用户和第三方的支付对账信息
	 * @Method_Name : findUserPayCheckAccount;
	 * @param tradeType
	 *            交易类型 10 充值 14提现
	 * @param systemTypeEnums
	 *            系统类型
	 * @param regUserId
	 *            用户ID
	 * @param pager
	 *            分页对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月18日 上午11:47:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findUserPayCheckAccount(Integer tradeType, SystemTypeEnums systemTypeEnums, Integer regUserId,
			Pager pager);
	/**
	 * @Description : 易宝执行对账
	 * @Method_Name : excuteYeepayReconciliation;
	 * @param reconciliationDate 
	 * 			清算日期
	 * @return : void;
	 * @Creation Date : 2018年10月25日 下午15:48:36;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public void excuteYeepayReconciliation(Date settleDate);
}
