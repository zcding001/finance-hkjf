package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.BidRcommendEarnInfo.java
 * @Class Name : BidRcommendEarnInfo.java
 * @Description : 推荐奖信息VO，用于生成推荐奖记录
 * @Author : yanbinghuang
 */
public class RcommendEarnInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 被推荐人ID
	 */
	private Integer regUserId;
	/**
	 * 推荐人ID
	 */
	private Integer recommendRegUserId;
	/**
	 * 来源ID（投资即为投资记录ID）
	 */
	private Integer resourceId;
	/**
	 * 推荐奖类型 0-投资推荐奖
	 */
	private Integer type;
	/**
	 * 投资金额
	 */
	private BigDecimal investAmount;
	/**
	 * 好友级别
	 */
	private Integer friendLevel;
	/**
	 * 规则时间，这个用于判断是否在推荐规则时间之内，比如说：某天注册以后的用户才有推荐奖
	 */
	private Date ruleTime;
	/**
	 * 推荐记录状态
	 * 状态:0-初始化，1-无需审核已发放，2-无需审核待发放(线下处理)，3-审核成功，4-审核失败，5-已发放，6-发放失败，7-失效',
	 */
	private Integer state;
	/**
	 * 投资笔数
	 */
	private Integer InvestNum;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 标的ID不能为空
	 */
	private Integer biddId;
	/**
	 * 规则ID
	 */
	private Integer vasRuleId;
	/**
	 * 注册时间
	 */
	private Date registTime;
	/**
	 * 用户类型
	 */
	private Integer userType;
	/**
	 * 投资时间
	 */
	private Date investTime;
	/**
	 * 期限值
	 */
	private Integer termValue;
	/**
	 * 期限单位
	 */
	private Integer termUnit;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getVasRuleId() {
		return vasRuleId;
	}

	public void setVasRuleId(Integer vasRuleId) {
		this.vasRuleId = vasRuleId;
	}

	public Integer getBiddId() {
		return biddId;
	}

	public void setBiddId(Integer biddId) {
		this.biddId = biddId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getRecommendRegUserId() {
		return recommendRegUserId;
	}

	public void setRecommendRegUserId(Integer recommendRegUserId) {
		this.recommendRegUserId = recommendRegUserId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Integer getFriendLevel() {
		return friendLevel;
	}

	public void setFriendLevel(Integer friendLevel) {
		this.friendLevel = friendLevel;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getRuleTime() {
		return ruleTime;
	}

	public void setRuleTime(Date ruleTime) {
		this.ruleTime = ruleTime;
	}

	public Integer getInvestNum() {
		return InvestNum;
	}

	public void setInvestNum(Integer investNum) {
		InvestNum = investNum;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

	public Integer getTermValue() {
		return termValue;
	}

	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}

	public Integer getTermUnit() {
		return termUnit;
	}

	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
