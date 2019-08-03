package com.hongkun.finance.invest.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Description   : 投资信息vo （针对销售app端展示）
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.BidInvestVoForSales.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */
public class BidInvestVoForSales extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	private Integer investId;
	private Integer regUserId;//投资人
	private String bidName;//标的名称
	private Date investTime; //投资时间
	private BigDecimal rate; //年化率
	private BigDecimal investAmount; //投资金额
	private BigDecimal expectAmount; //预期收益
	private Date expireDate; //到期日
	private Integer bidState; //标的状态
	private BigDecimal useAbleMoney; //可用余额
	private Integer termUnit;
	private Integer termValue;
	
	public Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public String getBidName() {
		return bidName;
	}
	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	public Date getInvestTime() {
		return investTime;
	}
	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}
	public Integer getBidState() {
		return bidState;
	}
	public void setBidState(Integer bidState) {
		this.bidState = bidState;
	}
	public BigDecimal getUseAbleMoney() {
		return useAbleMoney;
	}
	public void setUseAbleMoney(BigDecimal useAbleMoney) {
		this.useAbleMoney = useAbleMoney;
	}
	public Integer getInvestId() {
		return investId;
	}
	public void setInvestId(Integer investId) {
		this.investId = investId;
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

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
