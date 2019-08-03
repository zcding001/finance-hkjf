package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.util.List;

public class RecommendRuleVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 一级 好友受益笔数
	private List<String> invNumOne;
	// 一级好友受益笔数对应的利率
	private List<String> rebatesRateOne;
	// 一级好友，默认从第几笔开始
	private String fromInvNumOne;
	// 一级好友，默认到几笔结束
	private String toInvNumOne;
	// 一级好友，默认利率
	private String rebatesRateLevelOne;

	// 二级好友受益笔数
	private List<String> invNumTwo;
	// 二级好友受益笔数对应的利率
	private List<String> rebatesRateTwo;
	// 一级好友，默认从第几笔开始
	private String fromInvNumTwo;
	// 一级好友，默认到几笔结束
	private String toInvNumTwo;
	// 一级好友，默认利率
	private String rebatesRateLevelTwo;

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
	// 转化周期
	private String switchCycle;
	// 用户类型
	private String userType;

	public List<String> getInvNumOne() {
		return invNumOne;
	}

	public void setInvNumOne(List<String> invNumOne) {
		this.invNumOne = invNumOne;
	}

	public List<String> getRebatesRateOne() {
		return rebatesRateOne;
	}

	public void setRebatesRateOne(List<String> rebatesRateOne) {
		this.rebatesRateOne = rebatesRateOne;
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

	public List<String> getInvNumTwo() {
		return invNumTwo;
	}

	public void setInvNumTwo(List<String> invNumTwo) {
		this.invNumTwo = invNumTwo;
	}

	public List<String> getRebatesRateTwo() {
		return rebatesRateTwo;
	}

	public void setRebatesRateTwo(List<String> rebatesRateTwo) {
		this.rebatesRateTwo = rebatesRateTwo;
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

	public String getSwitchCycle() {
		return switchCycle;
	}

	public void setSwitchCycle(String switchCycle) {
		this.switchCycle = switchCycle;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "RecommendRuleVo [invNumOne=" + invNumOne + ", rebatesRateOne=" + rebatesRateOne + ", fromInvNumOne="
				+ fromInvNumOne + ", toInvNumOne=" + toInvNumOne + ", rebatesRateLevelOne=" + rebatesRateLevelOne
				+ ", invNumTwo=" + invNumTwo + ", rebatesRateTwo=" + rebatesRateTwo + ", fromInvNumTwo=" + fromInvNumTwo
				+ ", toInvNumTwo=" + toInvNumTwo + ", rebatesRateLevelTwo=" + rebatesRateLevelTwo + ", beginTime="
				+ beginTime + ", endTime=" + endTime + ", vasRebatesRuleId=" + vasRebatesRuleId + ", state=" + state
				+ ", type=" + type + ", switchCycle=" + switchCycle + ", userType=" + userType + "]";
	}

}
