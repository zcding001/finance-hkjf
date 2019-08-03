package com.hongkun.finance.loan.vo;

import java.io.Serializable;

/**
 * 
 * @Description : 还款代扣vo
 * @Project : finance-loan-model
 * @Program Name : com.hongkun.finance.loan.vo.RepayPlanPayVo.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class RepayPlanPayVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String date; // 计划时间
	private String amount;// 计划还款金额

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
