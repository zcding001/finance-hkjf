package com.hongkun.finance.vas.model;

import java.io.Serializable;

public class QdzRecommendConditionVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 一级好友层级
	private String friendLevelOne;
	// 一级 好友受益笔数
	private String invNumOne;
	// 一级好友受益汇率
	private String rebatesRateOne;
	// 二级好友层级
	private String friendLevelTwo;
	// 二级好友受益笔数
	private String invNumTwo;
	// 二级好友受益汇率
	private String rebatesRateTwo;
	// 开始时间
	private String beginTime;
	// 结束时间
	private String endTime;
	// 规则ID
	private Integer vasRebatesRuleId;
	// 状态
	private Integer state;
	// 推荐类型
	private Integer type;

	public String getFriendLevelOne() {
		return friendLevelOne;
	}

	public void setFriendLevelOne(String friendLevelOne) {
		this.friendLevelOne = friendLevelOne;
	}

	public String getInvNumOne() {
		return invNumOne;
	}

	public void setInvNumOne(String invNumOne) {
		this.invNumOne = invNumOne;
	}

	public String getRebatesRateOne() {
		return rebatesRateOne;
	}

	public void setRebatesRateOne(String rebatesRateOne) {
		this.rebatesRateOne = rebatesRateOne;
	}

	public String getFriendLevelTwo() {
		return friendLevelTwo;
	}

	public void setFriendLevelTwo(String friendLevelTwo) {
		this.friendLevelTwo = friendLevelTwo;
	}

	public String getInvNumTwo() {
		return invNumTwo;
	}

	public void setInvNumTwo(String invNumTwo) {
		this.invNumTwo = invNumTwo;
	}

	public String getRebatesRateTwo() {
		return rebatesRateTwo;
	}

	public void setRebatesRateTwo(String rebatesRateTwo) {
		this.rebatesRateTwo = rebatesRateTwo;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getVasRebatesRuleId() {
		return vasRebatesRuleId;
	}

	public void setVasRebatesRuleId(Integer vasRebatesRuleId) {
		this.vasRebatesRuleId = vasRebatesRuleId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
