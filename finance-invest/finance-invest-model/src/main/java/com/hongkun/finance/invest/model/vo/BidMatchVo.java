package com.hongkun.finance.invest.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description   : 匹配详情vo
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.BidMatchVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidMatchVo implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer goodBidId;
	private String goodBidName;
	private Integer goodTermUnit;
	private Integer goodTermValue;
	private BigDecimal goodTotalAmount;
	private Integer commonBidId;
	private String commonBidName;
	private Integer commonTermUnit;
	private Integer commonTermValue;
	private BigDecimal commonTotalAmount;
	private String goodTerm;
	private String commonTerm;
	private BigDecimal matchMoney;
	private Date modifyTime;
	private Integer state;
	private Date createTime;
	private String createTimeBegin;
	private String createTimeEnd;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGoodBidId() {
		return goodBidId;
	}
	public void setGoodBidId(Integer goodBidId) {
		this.goodBidId = goodBidId;
	}
	public Integer getGoodTermUnit() {
		return goodTermUnit;
	}
	public void setGoodTermUnit(Integer goodTermUnit) {
		this.goodTermUnit = goodTermUnit;
	}
	public Integer getGoodTermValue() {
		return goodTermValue;
	}
	public void setGoodTermValue(Integer goodTermValue) {
		this.goodTermValue = goodTermValue;
	}
	public BigDecimal getGoodTotalAmount() {
		return goodTotalAmount;
	}
	public void setGoodTotalAmount(BigDecimal goodTotalAmount) {
		this.goodTotalAmount = goodTotalAmount;
	}
	public Integer getCommonBidId() {
		return commonBidId;
	}
	public void setCommonBidId(Integer commonBidId) {
		this.commonBidId = commonBidId;
	}
	public Integer getCommonTermUnit() {
		return commonTermUnit;
	}
	public void setCommonTermUnit(Integer commonTermUnit) {
		this.commonTermUnit = commonTermUnit;
	}
	public Integer getCommonTermValue() {
		return commonTermValue;
	}
	public void setCommonTermValue(Integer commonTermValue) {
		this.commonTermValue = commonTermValue;
	}
	public BigDecimal getCommonTotalAmount() {
		return commonTotalAmount;
	}
	public void setCommonTotalAmount(BigDecimal commonTotalAmount) {
		this.commonTotalAmount = commonTotalAmount;
	}
	public String getGoodTerm() {
		return goodTerm;
	}
	public void setGoodTerm(String goodTerm) {
		this.goodTerm = goodTerm;
	}
	public String getCommonTerm() {
		return commonTerm;
	}
	public void setCommonTerm(String commonTerm) {
		this.commonTerm = commonTerm;
	}
	public BigDecimal getMatchMoney() {
		return matchMoney;
	}
	public void setMatchMoney(BigDecimal matchMoney) {
		this.matchMoney = matchMoney;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getGoodBidName() {
		return goodBidName;
	}
	public void setGoodBidName(String goodBidName) {
		this.goodBidName = goodBidName;
	}
	public String getCommonBidName() {
		return commonBidName;
	}
	public void setCommonBidName(String commonBidName) {
		this.commonBidName = commonBidName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeBegin() {
		return createTimeBegin;
	}
	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	
}