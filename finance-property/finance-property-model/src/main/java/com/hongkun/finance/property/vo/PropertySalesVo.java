package com.hongkun.finance.property.vo;

import java.math.BigDecimal;

/**
 * @Description   : 物业销售查询vo
 * @Project       : finance-property-model
 * @Program Name  : com.hongkun.finance.property.vo.PropertySalesVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class PropertySalesVo {
	private String tel;
	private String realName;
	//员工id
	private Integer userId;
	//推荐累计投资金额
	private BigDecimal investAmount;
	//折标金额
	private BigDecimal niggerAmount;
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getNiggerAmount() {
		return niggerAmount;
	}
	public void setNiggerAmount(BigDecimal niggerAmount) {
		this.niggerAmount = niggerAmount;
	}
	
}
