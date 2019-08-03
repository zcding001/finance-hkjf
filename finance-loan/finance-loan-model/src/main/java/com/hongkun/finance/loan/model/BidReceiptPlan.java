package com.hongkun.finance.loan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidReceiptPlan.java
 * @Class Name    : BidReceiptPlan.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidReceiptPlan extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 投资id
	 * 字段: invest_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer investId;
	
	/**
	 * 描述: 标的id
	 * 字段: bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 期数
	 * 字段: periods  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer periods;
	
	/**
	 * 描述: 回款金额=回款本金+回款利息+加息收益+逾期收益
	 * 字段: amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal amount;
	
	/**
	 * 描述: 回款本金
	 * 字段: capital_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal capitalAmount;
	
	/**
	 * 描述: 回款利息
	 * 字段: interest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interestAmount;
	
	/**
	 * 描述: 加息收益
	 * 字段: increase_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal increaseAmount;
	
	/**
	 * 描述: 利差
	 * 字段: yearrate_difference_money  DECIMAL(10)
	 * 默认值: 0.0000
	 */
	private java.math.BigDecimal yearrateDifferenceMoney;
	
	/**
	 * 描述: 逾期收益
	 * 字段: punish_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal punishAmount;
	
	/**
	 * 描述: 计划回款时间
	 * 字段: plan_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date planTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date planTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date planTimeEnd;
	/**
	 * 描述: 实际回款时间
	 * 字段: actual_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date actualTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeEnd;
	/**
	 * 描述: 收益用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 状态：0-无效，1-正常
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;

	private Integer actionScope;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	//用户信息
	private List<Integer> regUserIds;

	public List<Integer> getRegUserIds() {
		return regUserIds;
	}

	public void setRegUserIds(List<Integer> regUserIds) {
		this.regUserIds = regUserIds;
	}

	public BidReceiptPlan(){
	}

	public BidReceiptPlan(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setInvestId(java.lang.Integer investId) {
		this.investId = investId;
	}
	
	public java.lang.Integer getInvestId() {
		return this.investId;
	}
	
	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}
	
	public java.lang.Integer getBidId() {
		return this.bidId;
	}
	
	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	
	public Integer getPeriods() {
		return this.periods;
	}
	
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
	
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}
	
	public void setCapitalAmount(java.math.BigDecimal capitalAmount) {
		this.capitalAmount = capitalAmount;
	}
	
	public java.math.BigDecimal getCapitalAmount() {
		return this.capitalAmount;
	}
	
	public void setInterestAmount(java.math.BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	
	public java.math.BigDecimal getInterestAmount() {
		return this.interestAmount;
	}
	
	public void setIncreaseAmount(java.math.BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}
	
	public java.math.BigDecimal getIncreaseAmount() {
		return this.increaseAmount;
	}
	
	public void setYearrateDifferenceMoney(java.math.BigDecimal yearrateDifferenceMoney) {
		this.yearrateDifferenceMoney = yearrateDifferenceMoney;
	}
	
	public java.math.BigDecimal getYearrateDifferenceMoney() {
		return this.yearrateDifferenceMoney;
	}
	
	public void setPunishAmount(java.math.BigDecimal punishAmount) {
		this.punishAmount = punishAmount;
	}
	
	public java.math.BigDecimal getPunishAmount() {
		return this.punishAmount;
	}
	
	public void setPlanTime(java.util.Date planTime) {
		this.planTime = planTime;
	}
	
	public java.util.Date getPlanTime() {
		return this.planTime;
	}
	
	public void setPlanTimeBegin(java.util.Date planTimeBegin) {
		this.planTimeBegin = planTimeBegin;
	}
	
	public java.util.Date getPlanTimeBegin() {
		return this.planTimeBegin;
	}
	
	public void setPlanTimeEnd(java.util.Date planTimeEnd) {
		this.planTimeEnd = planTimeEnd;
	}
	
	public java.util.Date getPlanTimeEnd() {
		return this.planTimeEnd;
	}	
	public void setActualTime(java.util.Date actualTime) {
		this.actualTime = actualTime;
	}
	
	public java.util.Date getActualTime() {
		return this.actualTime;
	}
	
	public void setActualTimeBegin(java.util.Date actualTimeBegin) {
		this.actualTimeBegin = actualTimeBegin;
	}
	
	public java.util.Date getActualTimeBegin() {
		return this.actualTimeBegin;
	}
	
	public void setActualTimeEnd(java.util.Date actualTimeEnd) {
		this.actualTimeEnd = actualTimeEnd;
	}
	
	public java.util.Date getActualTimeEnd() {
		return this.actualTimeEnd;
	}	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	
	public java.util.Date getCreateTimeBegin() {
		return this.createTimeBegin;
	}
	
	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}	
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}
	
	public java.util.Date getModifyTimeBegin() {
		return this.modifyTimeBegin;
	}
	
	public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}
	
	public java.util.Date getModifyTimeEnd() {
		return this.modifyTimeEnd;
	}

	public Integer getActionScope() {
		return actionScope;
	}

	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

