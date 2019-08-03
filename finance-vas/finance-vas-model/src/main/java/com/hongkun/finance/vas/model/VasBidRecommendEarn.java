package com.hongkun.finance.vas.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.VasBidRecommendEarn.java
 * @Class Name : VasBidRecommendEarn.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class VasBidRecommendEarn extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 投资人id 字段: reg_user_id INT(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 推荐人id 字段: recommend_reg_user_id INT(10) 默认值: 0
	 */
	private java.lang.Integer recommendRegUserId;

	/**
	 * 描述: 标的id 字段: bid_id INT(10) 默认值: 0
	 */
	private java.lang.Integer bidId;

	/**
	 * 描述: 来源id(投资即为投资记录ID) 字段: resource_id INT(10) 默认值: 0
	 */
	private java.lang.Integer resourceId;

	/**
	 * 描述: 类型:0-投资推荐奖 字段: type TINYINT(3) 默认值: 0
	 */
	private Integer type;

	/**
	 * 描述: 奖励规则id 字段: rule_id INT(10) 默认值: 0
	 */
	private java.lang.Integer ruleId;

	/**
	 * 描述: 投资金额 字段: invest_amount DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;

	/**
	 * 描述: 奖励金额 字段: earn_amount DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal earnAmount;

	/**
	 * 描述: 好友等级 字段: friend_level TINYINT(3) 默认值: 1
	 */
	private Integer friendLevel;

	/**
	 * 描述: 发放时间 字段: grant_time DATETIME(19) 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date grantTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date grantTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date grantTimeEnd;
	/**
	 * 描述: 奖励备注 字段: note VARCHAR(20) 默认值: ''
	 */
	private java.lang.String note;

	/**
	 * 描述: 状态:0-初始化，1-无需审核已发放，2-无需审核待发放(线下处理)，3-审核成功，4-审核失败，5-已发放，6-发放失败，7-失效
	 * 字段: state TINYINT(3) 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 推荐状态
	 */
	private List<Integer> stateList;
	/**
	 * 投资人ID集合
	 */
	private List<Integer> regUserIdList;

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}

	public VasBidRecommendEarn() {
	}

	public VasBidRecommendEarn(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}

	public void setRecommendRegUserId(java.lang.Integer recommendRegUserId) {
		this.recommendRegUserId = recommendRegUserId;
	}

	public java.lang.Integer getRecommendRegUserId() {
		return this.recommendRegUserId;
	}

	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}

	public java.lang.Integer getBidId() {
		return this.bidId;
	}

	public void setResourceId(java.lang.Integer resourceId) {
		this.resourceId = resourceId;
	}

	public java.lang.Integer getResourceId() {
		return this.resourceId;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}

	public void setRuleId(java.lang.Integer ruleId) {
		this.ruleId = ruleId;
	}

	public java.lang.Integer getRuleId() {
		return this.ruleId;
	}

	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}

	public void setEarnAmount(java.math.BigDecimal earnAmount) {
		this.earnAmount = earnAmount;
	}

	public java.math.BigDecimal getEarnAmount() {
		return this.earnAmount;
	}

	public void setFriendLevel(Integer friendLevel) {
		this.friendLevel = friendLevel;
	}

	public Integer getFriendLevel() {
		return this.friendLevel;
	}

	public void setGrantTime(java.util.Date grantTime) {
		this.grantTime = grantTime;
	}

	public java.util.Date getGrantTime() {
		return this.grantTime;
	}

	public void setGrantTimeBegin(java.util.Date grantTimeBegin) {
		this.grantTimeBegin = grantTimeBegin;
	}

	public java.util.Date getGrantTimeBegin() {
		return this.grantTimeBegin;
	}

	public void setGrantTimeEnd(java.util.Date grantTimeEnd) {
		this.grantTimeEnd = grantTimeEnd;
	}

	public java.util.Date getGrantTimeEnd() {
		return this.grantTimeEnd;
	}

	public void setNote(java.lang.String note) {
		this.note = note;
	}

	public java.lang.String getNote() {
		return this.note;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
