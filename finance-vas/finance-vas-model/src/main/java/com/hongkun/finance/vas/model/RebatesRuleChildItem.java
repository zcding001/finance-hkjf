package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.RebatesRuleItem.java
 * @Class Name : RebatesRuleItem.java
 * @Description : 好友推荐规则条目
 * @Author : yanbinghuang
 */
public class RebatesRuleChildItem implements Serializable {
	private static final long serialVersionUID = 1L;
	// 一级好友层级
	private List<RuleItemVo> friendLevelOne;
	// 二级好友层级
	private List<RuleItemVo> friendLevelTwo;
	// 一级好友，默认从第几笔开始
	private String fromInvNumOne;
	// 一级好友，默认到几笔结束
	private String toInvNumOne;
	// 一级好友，默认利率
	private String rebatesRateLevelOne;
	// 二级好友，默认从第几笔开始
	private String fromInvNumTwo;
	// 二级好友，默认到几笔结束
	private String toInvNumTwo;
	// 二级好友，默认利率
	private String rebatesRateLevelTwo;

	public List<RuleItemVo> getFriendLevelOne() {
		return friendLevelOne;
	}

	public void setFriendLevelOne(List<RuleItemVo> friendLevelOne) {
		this.friendLevelOne = friendLevelOne;
	}

	public List<RuleItemVo> getFriendLevelTwo() {
		return friendLevelTwo;
	}

	public void setFriendLevelTwo(List<RuleItemVo> friendLevelTwo) {
		this.friendLevelTwo = friendLevelTwo;
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

}
