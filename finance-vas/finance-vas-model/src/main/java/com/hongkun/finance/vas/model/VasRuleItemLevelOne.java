package com.hongkun.finance.vas.model;

import java.io.Serializable;

public class VasRuleItemLevelOne implements Serializable {
	private static final long serialVersionUID = 1L;
	// 投资笔数
	private String invNumOne;
	// 利率
	private String rebatesRateOne;

	public String getInvNumOne() {
		return invNumOne;
	}

	public void setInvNumOne(String invNumOne) {
		this.invNumOne = invNumOne;
	}

	public String getRebatesRateOne() {
		return rebatesRateOne;
	}

	public void setRebatesRateOne(String rebatesRateOne) {
		this.rebatesRateOne = rebatesRateOne;
	}

}
