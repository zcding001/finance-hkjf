package com.hongkun.finance.invest.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yirun.framework.core.model.BaseModel;

/**
 * 
 * @Description   : 投资记录vo（app-查询好友投资记录列表展示）
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.BidInvestVoForApp.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */
public class BidInvestVoForApp extends BaseModel {
	    
	private static final long serialVersionUID = 1L;
	//投资金额
	private BigDecimal money;
	//期限单位
	private Integer termUnit ;
	//期限值
	private Integer termValue;
	//投资时间
	private Date investTime;
	//投资年化率
	private BigDecimal rate;
	private Integer actionScope;
	
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }
}
