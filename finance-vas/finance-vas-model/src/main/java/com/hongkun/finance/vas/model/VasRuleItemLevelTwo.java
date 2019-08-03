package com.hongkun.finance.vas.model;

import java.io.Serializable;

public class VasRuleItemLevelTwo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 投资笔数
	private String invNumTwo;
	// 利率
	private String rebatesRateTwo;

	public String getInvNumTwo() {
		return invNumTwo;
	}

	public void setInvNumTwo(String invNumTwo) {
		this.invNumTwo = invNumTwo;
	}

	public String getRebatesRateTwo() {
		return rebatesRateTwo;
	}

	public void setRebatesRateTwo(String rebatesRateTwo) {
		this.rebatesRateTwo = rebatesRateTwo;
	}

}
