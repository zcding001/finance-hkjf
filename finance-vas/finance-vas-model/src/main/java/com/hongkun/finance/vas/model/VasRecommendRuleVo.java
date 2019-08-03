package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class VasRecommendRuleVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 一级 好友受益笔数
	private List<VasRuleItemLevelOne> friendLvelOneList;
	// 一级好友，默认从第几笔开始
	private String fromInvNumOne;
	// 一级好友，默认到几笔结束
	private String toInvNumOne;
	// 一级好友，默认利率
	private String rebatesRateLevelOne;

	// 二级好友受益笔数
	private List<VasRuleItemLevelTwo> friendLvelTwoList;
	// 一级好友，默认从第几笔开始
	private String fromInvNumTwo;
	// 一级好友，默认到几笔结束
	private String toInvNumTwo;
	// 一级好友，默认利率
	private String rebatesRateLevelTwo;

	// 开始时间
	private Date beginTime;
	// 结束时间
	private Date endTime;
	// 规则ID
	private Integer vasRebatesRuleId;
	// 状态
	private Integer state;
	// 推荐类型
	private Integer type;
	// 转化周期
	private Integer switchCycle;
	// 用户类型
	private String userType;

	public List<VasRuleItemLevelOne> getFriendLvelOneList() {
		return friendLvelOneList;
	}

	public void setFriendLvelOneList(List<VasRuleItemLevelOne> friendLvelOneList) {
		this.friendLvelOneList = friendLvelOneList;
	}

	public String getFromInvNumOne() {
		return fromInvNumOne;
	}

	public void setFromInvNumOne(String fromInvNumOne) {
		this.fromInvNumOne = fromInvNumOne;
	}

	public String getToInvNumOne() {
		return toInvNumOne;
	}

	public void setToInvNumOne(String toInvNumOne) {
		this.toInvNumOne = toInvNumOne;
	}

	public String getRebatesRateLevelOne() {
		return rebatesRateLevelOne;
	}

	public void setRebatesRateLevelOne(String rebatesRateLevelOne) {
		this.rebatesRateLevelOne = rebatesRateLevelOne;
	}

	public List<VasRuleItemLevelTwo> getFriendLvelTwoList() {
		return friendLvelTwoList;
	}

	public void setFriendLvelTwoList(List<VasRuleItemLevelTwo> friendLvelTwoList) {
		this.friendLvelTwoList = friendLvelTwoList;
	}

	public String getFromInvNumTwo() {
		return fromInvNumTwo;
	}

	public void setFromInvNumTwo(String fromInvNumTwo) {
		this.fromInvNumTwo = fromInvNumTwo;
	}

	public String getToInvNumTwo() {
		return toInvNumTwo;
	}

	public void setToInvNumTwo(String toInvNumTwo) {
		this.toInvNumTwo = toInvNumTwo;
	}

	public String getRebatesRateLevelTwo() {
		return rebatesRateLevelTwo;
	}

	public void setRebatesRateLevelTwo(String rebatesRateLevelTwo) {
		this.rebatesRateLevelTwo = rebatesRateLevelTwo;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSwitchCycle() {
		return switchCycle;
	}

	public void setSwitchCycle(Integer switchCycle) {
		this.switchCycle = switchCycle;
	}

}
