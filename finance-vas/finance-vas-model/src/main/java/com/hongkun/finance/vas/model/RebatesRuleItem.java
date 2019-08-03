package com.hongkun.finance.vas.model;

import java.io.Serializable;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.RebatesRuleItem.java
 * @Class Name : RebatesRuleItem.java
 * @Description : 好友推荐规则条目
 * @Author : yanbinghuang
 */
public class RebatesRuleItem implements Serializable {
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

}
