package com.hongkun.finance.vas.model;

import java.io.Serializable;

public class RuleItemVo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 投资笔数
	private String investStrokeCount;
	// 利率
	private String rate;

	public String getInvestStrokeCount() {
		return investStrokeCount;
	}

	public void setInvestStrokeCount(String investStrokeCount) {
		this.investStrokeCount = investStrokeCount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

}
