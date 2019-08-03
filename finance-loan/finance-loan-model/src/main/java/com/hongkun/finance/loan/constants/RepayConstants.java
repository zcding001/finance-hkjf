
/**
 * 
 */
package com.hongkun.finance.loan.constants;

/**
 * @Description : 还款状态常量类
 * @Project : finance-loan-model
 * @Program Name : com.hongkun.finance.loan.constants.RepayConstants.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class RepayConstants {

	public static final String REPAY_PLAN = "REPAY_PLAN";// 还款
	public static final String RECEIPT_PLAN = "RECEIPT_PLAN";// 回款

	/** 还款状态，无效的 **/
	public static final int REPAY_STATE_INVALID = 0;
	/** 还款状态，未还款的 **/
	public static final int REPAY_STATE_NONE = 1;
	/** 还款状态，已还款 **/
	public static final int REPAY_STATE_FINISH = 2;
	/** 还款状态，风险储备金还款 **/
	public static final int REPAY_STATE_RISK_RESERVE = 3;
	/** 还款状态，逾期还款 **/
	public static final int REPAY_STATE_OVERDUE = 4;
	/** 还款状态， 提前还本 **/
	public static final int REPAY_STATE_ADVANCE_CAPITAL = 8;
	/** 还款状态，未还款的 用于app显示还款按钮标识**/
	public static final int REPAY_STATE_NONE_BTN = -1;

	/** 还款方式：等额本息 */
	public static final int REPAYTYPE_PRINCIPAL_INTEREST_EQ = 1;
	/** 还款方式：按月付息，到期还本 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END = 2;
	/** 还款方式：到期还本付息 */
	public static final int REPAYTYPE_ONECE_REPAYMENT = 3;
	/** 还款方式：到期付息，本金回收 */
	public static final int REPAYTYPE_INTEREST_END_PRINCIPAL_RECOVERY = 4;
	/** 还款方式：每月付息，到期本金回收 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_RECOVERY = 5;
	/** 还款方式：按月付息，本金划归企业 */
	public static final int REPAYTYPE_INTEREST_MONTH_PRINCIPAL_ENTERPRISE = 6;
	/** 还款方式：按月付息，按日计息 */
	public static final int REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH = 7 ;

	/**
	 * 期限单位 ：天
	 **/
	public static final int BID_TERM_UNIT_DAY = 3;
	/**
	 * 期限单位：月
	 **/
	public static final int BID_TERM_UNIT_MONTH = 2;
	/**
	 * 期限单位：年
	 **/
	public static final int BID_TERM_UNIT_YEAR = 1;
	/** 回款状态， 无效 **/
	public static final int RECEIPT_STATE_INVALID = 0;
	/** 回款状态， 未回款 **/
	public static final int RECEIPT_STATE_NONE = 1;
	/** 回款状态， 已回款 **/
	public static final int RECEIPT_STATE_FINISH = 2;
	
	/**初始化还款计划回款计划Vo:标的信息vo */
	public static final String REPAY_BIDCOMMONPLAN_VO = "BidCommonPlanVo";
	/**初始化还款计划回款计划Vo:投资信息vo */
	public static final String REPAY_BIDINVEST_VO = "BidInvestVo";
	/**还款、回款计划消息队列 */
	public static final String MQ_QUEUE_REPAYANDRECEIPTPLAN = "repayAndReceiptPlan";
	
	/** 待收收益*/
	public static final String NOT_RECEIVED_INTEREST = "notReceivedInterest";
	/** 已收收益*/
	public static final String RECEIVED_INTEREST = "receivedInterest";
	/**下期回款计划 */
	public static final String RECEIPT_NEXT_PLAN = "receiptNextPlan";
	/**最后一期回款计划 */
	public static final String RECEIPT_LAST_PLAN = "receiptLastPlan";
	
	public static final String REPAY_END_DATE = "repay_end_date";
	/**还款返回前台状态码 */
	public static final int REPAY_RETURN_STATE = 2008;

	public static final int INIT_PLAN_REPAY_AND_RECEIPT = 1;

	public static final int INIT_PLAN_RECEIPT_ONLY = 2;
}





