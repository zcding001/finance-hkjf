package com.hongkun.finance.loan.model.vo;

import java.math.BigDecimal;

/**
 * @Description   : 投资记录vo
 * （为支撑在还款服务中生成回款计划调用投资相关信息）
 * @Project       : finance-loan-model
 * @Program Name  : com.hongkun.finance.loan.model.vo.BidInvestVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInvestVo {
	
	private Integer id;
	private Integer regUserId;
	
	private BigDecimal investAmount;
	
	private BigDecimal couponWorthJ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getCouponWorthJ() {
		return couponWorthJ;
	}
	public void setCouponWorthJ(BigDecimal couponWorthJ) {
		this.couponWorthJ = couponWorthJ;
	}
	
}
