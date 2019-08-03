package com.hongkun.finance.invest.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description   : 匹配统计vo
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.BidMatchStatisticsVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidMatchStatisticsVo implements Serializable{
	    
	private static final long serialVersionUID = 1L;
	//当前时间
	private Date currentDate;
	//散标待匹配总金额
	private BigDecimal commonMatchMoney;
	//优选待匹配总金额
	private BigDecimal goodMatchMoney;
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public BigDecimal getCommonMatchMoney() {
		return commonMatchMoney;
	}
	public void setCommonMatchMoney(BigDecimal commonMatchMoney) {
		this.commonMatchMoney = commonMatchMoney;
	}
	public BigDecimal getGoodMatchMoney() {
		return goodMatchMoney;
	}
	public void setGoodMatchMoney(BigDecimal goodMatchMoney) {
		this.goodMatchMoney = goodMatchMoney;
	}
	
}
