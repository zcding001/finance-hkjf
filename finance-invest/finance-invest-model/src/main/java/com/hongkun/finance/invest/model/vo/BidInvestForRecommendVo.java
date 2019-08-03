package com.hongkun.finance.invest.model.vo;

import java.math.BigDecimal;

import com.yirun.framework.core.model.BaseModel;

/**
 * 
 * @Description   : 投资记录vo
 * 为支持查询被推荐人投资总金额和折标后总金额 
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.BidInvestForRecommendVo.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */
public class BidInvestForRecommendVo extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	private Integer regUserId;
	private String tel;
	private String realName;
	private String bidName;
	private String investTime;
	private Integer termUnit;
	private Integer termValue;
	private BigDecimal investAmount;//投资金额
	private BigDecimal investBackStepMoney;//折标后金额
	public Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	public Integer getTermUnit() {
		return termUnit;
	}
	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}
	public Integer getTermValue() {
		return termValue;
	}
	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getInvestBackStepMoney() {
		return investBackStepMoney;
	}
	public void setInvestBackStepMoney(BigDecimal investBackStepMoney) {
		this.investBackStepMoney = investBackStepMoney;
	}
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
	public String getBidName() {
		return bidName;
	}
	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	public String getInvestTime() {
		return investTime;
	}
	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}
	
}
