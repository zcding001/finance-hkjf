package com.hongkun.finance.user.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.model.RegUserFriends.java
 * @Class Name : RegUserFriends.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class RegUserFriends extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 分组id 字段: group_id INT(10)
	 */
	private java.lang.Integer groupId;

	/**
	 * 描述: 好友id 字段: reg_user_id INT(10)
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 好友姓名 字段: real_name VARCHAR(50)
	 */
	private java.lang.String realName;

	/**
	 * 描述: 好友手机号 字段: tel VARCHAR(11)
	 */
	private java.lang.String tel;

	/**
	 * 描述: 好友备注名称 字段: memo_name VARCHAR(50)
	 */
	private java.lang.String memoName;

	/**
	 * 描述: 备注 字段: remarks VARCHAR(500)
	 */
	private java.lang.String remarks;

	/**
	 * 描述: 好友等级 1-一级好友,2-二级好友 字段: grade TINYINT(3)
	 */
	private Integer grade;

	/**
	 * 描述: 是否已投资0-未投资，1-已投资 字段: invest_state TINYINT(3) 默认值: 0
	 */
	private Integer investState;

	/**
	 * 描述: 推荐人id 字段: recommend_id INT(10)
	 */
	private java.lang.Integer recommendId;
	
	/**
	 * 描述: 状态 0：删除 1：正常 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: createTime 字段: create_time DATETIME(19)
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: modifiedTime 字段: modified_time DATETIME(19)
	 */
	private java.util.Date modifiedTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeEnd;
	// 好友ID集合
	private List<Integer> regUserIdList;
	
	//【非数据库字段，查询时使用】 分组名称
	private String groupName;
	
	private Integer groupType;
	
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public RegUserFriends() {
	}

	public RegUserFriends(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setGroupId(java.lang.Integer groupId) {
		this.groupId = groupId;
	}

	public java.lang.Integer getGroupId() {
		return this.groupId;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}

	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}

	public java.lang.String getRealName() {
		return this.realName;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}

	public java.lang.String getTel() {
		return this.tel;
	}

	public void setMemoName(java.lang.String memoName) {
		this.memoName = memoName;
	}

	public java.lang.String getMemoName() {
		return this.memoName;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getGrade() {
		return this.grade;
	}

	public void setInvestState(Integer investState) {
		this.investState = investState;
	}

	public Integer getInvestState() {
		return this.investState;
	}

	public void setRecommendId(java.lang.Integer recommendId) {
		this.recommendId = recommendId;
	}

	public java.lang.Integer getRecommendId() {
		return this.recommendId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public void setModifiedTime(java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public java.util.Date getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTimeBegin(java.util.Date modifiedTimeBegin) {
		this.modifiedTimeBegin = modifiedTimeBegin;
	}

	public java.util.Date getModifiedTimeBegin() {
		return this.modifiedTimeBegin;
	}

	public void setModifiedTimeEnd(java.util.Date modifiedTimeEnd) {
		this.modifiedTimeEnd = modifiedTimeEnd;
	}

	public java.util.Date getModifiedTimeEnd() {
		return this.modifiedTimeEnd;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
