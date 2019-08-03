package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.SimGoldVo.java
 * @Class Name : BiddRecommendEarnVo.java
 * @Description : 用于推荐奖展示的VO类
 * @Author : yanbinghuang
 */
public class SimGoldVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 体验金记录ID
	 */
	private Integer id;
	/**
	 * 用户手机号
	 */
	private Long login;
	/**
	 * 用户姓名
	 */
	private String realName;
	/**
	 * 体验金金额
	 */
	private BigDecimal money;
	/**
	 * 获取时间
	 */
	private Date createTime;
	/**
	 * 获取时间开始
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTimeBegin;
	/**
	 * 获取时间结束
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTimeEnd;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 使用状态
	 */
	private Integer state;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户ID集合
	 */
	private List<Integer> userIds;
	/**
	 * 事件类型
	 */
	private int ruleType;

	public int getRuleType() {
		return ruleType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
