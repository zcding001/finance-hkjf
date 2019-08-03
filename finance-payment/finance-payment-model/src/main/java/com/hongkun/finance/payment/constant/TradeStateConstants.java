package com.hongkun.finance.payment.constant;

/**
 * @Description : 支付表相关的状态常量类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.constant.TradeStateConstants.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class TradeStateConstants {

	/***************** 交易流水状态 ***********************/
	/**
	 * 支付成功
	 */
	public static Integer PAY_SUCCESS = 1;
	/**
	 * 支付失败
	 */
	public static Integer PAY_FAIL = 2;

	/***************** 支付记录表状态 ***********************/
	/**
	 * 逻缉删除
	 */
	public static Integer DEL_TRANSFER_FLOW = 0;
	/**
	 * 待审核状态 1
	 */
	public static Integer PENDING_PAYMENT = 1;
	/**
	 * 待放款状态 2
	 */
	public static Integer WAIT_PAY_MONEY = 2;
	/**
	 * 运营审核拒绝 状态 3
	 */
	public static Integer OPERATION_AUDIT_REJECT = 3;
	/***
	 * 财务审核拒绝状态 4
	 */
	public static Integer FINANCE_AUDIT_REJECT = 4;
	/**
	 * 划转中状态5
	 */
	public static Integer BANK_TRANSFER = 5;
	/***
	 * 已划转 状态 6
	 */
	public static Integer ALREADY_PAYMENT = 6;
	/**
	 * 划转失败状态 7
	 */
	public static Integer TRANSIT_FAIL = 7;
	/**
	 * 冲正状态8
	 */
	public static Integer CORRECT_MONEY = 8;
	/**
	 * 财务审核成功,等待定时提现
	 */
	public static Integer FINANCE_AUDIT_SUCCESS = 9;
	
	/****************** 资金划转标识 ***************************************/
	/**
	 * 划转标识 0：普通划转
	 */
	public static Integer COMMON_TRANSFER = 0;
	/**
	 * 划转标识 1：资金冻结
	 */
	public static Integer FREEZE_TRANSFER = 1;
	/**
	 * 划转标识 2：资金解冻
	 */
	public static Integer UNFREEZE_TRANSFER = 2;
	/**
	 * 划转标识 3：资金回退
	 */
	public static Integer BACK_TRANSFER = 3;
	/**
	 * 划转标识 4：资金增加
	 */
	public static Integer CORRECT_TRANSFER = 4;

	/************************* 银行卡状态 ************************************/
	/**
	 * 银行卡 初始化状态 0
	 */
	public static Integer BANK_CARD_STATE_INIT = 0;
	/**
	 * 绑卡未认证 1
	 */
	public static Integer BANK_CARD_STATE_UNAUTH = 1;
	/**
	 * 绑卡已认证 2
	 */
	public static Integer BANK_CARD_STATE_AUTH = 2;
	/**
	 * 未认证禁用 3
	 */
	public static Integer BANK_CARD_STATE_UNAUTH_FORBIDDEN = 3;
	/**
	 * 已认证禁用 4
	 */
	public static Integer BANK_CARD_STATE_AUTH_FORBIDDEN = 4;

	/************************* 支付记录表对账状态 ************************************/
	/**
	 * 未对账 1
	 */
	public static Integer PAY_CHECK_RECONCILIATION_WAIT = 1;
	/**
	 * 对账成功 2
	 */
	public static Integer PAY_CHECK_RECONCILIATION_SUCCESS = 2;
	/**
	 * 对账失败 3
	 */
	public static Integer PAY_CHECK_RECONCILIATION_FAIL = 3;

	/************************* 提现授权表状态 ************************************/
	/**
	 * 禁用
	 */
	public static Integer FORBIDDEN_STATE = 0;
	/**
	 * 启用
	 */
	public static Integer START_USING_STATE = 1;



	/************************* 充值提现记录交易状态 ************************************/
	/**
	 * 划转中
	 */
	public static Integer STATE_TYPE_TRANSFERING = 1;
	/**
	 * 成功
	 */
	public static Integer STATE_TYPE_SUCCESS = 2;
	/**
	 * 失败
	 */
	public static Integer STATE_TYPE_FAIL = 3;




}
