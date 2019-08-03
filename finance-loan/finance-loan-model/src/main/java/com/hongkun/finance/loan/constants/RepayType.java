package com.hongkun.finance.loan.constants;

/**
 * @Description : 还款方式
 * @Project : finance-loan-model
 * @Program Name : com.hongkun.finance.loan.model.RepayType.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public enum RepayType {

	/**
	 * 正常还款
	 */
	REPAY_NORMAL(1, "正常还款"),
	/**
	 * 提前还款
	 */
	REPAY_ADVANCE(2, "提前还款"),
	/**
	 * 风险储备金还款
	 */
	REPAY_RISK_RESERVE(3, "风险储备金还款"),
	/**
	 * 还风险储备金
	 */
	REPAY_RETURN_RISK_RESERVE(4, "还风险储备金");

	private int value;
	private String desc;

	private RepayType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return this.value;
	}

	public String getDesc() {
		return this.desc;
	}
}
