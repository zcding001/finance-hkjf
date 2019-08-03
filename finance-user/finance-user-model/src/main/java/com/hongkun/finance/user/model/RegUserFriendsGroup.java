package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserFriendsGroup.java
 * @Class Name    : RegUserFriendsGroup.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserFriendsGroup extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 分组名称
	 * 字段: name  VARCHAR(50)
	 */
	private java.lang.String name;
	
	/**
	 * 描述: regUserId
	 * 字段: reg_user_id  VARCHAR(50)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 分组类型，0-自定义分组，1-未投资分组2-已投资分组
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 排序
	 * 字段: sort  TINYINT(3)
	 */
	private Integer sort;
	
	//分组内好友人数
	private Integer userNum;
	
	/**
	 * 描述: createTime
	 * 字段: create_time  DATETIME(19)
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: modifiedTime
	 * 字段: modified_time  DATETIME(19)
	 */
	private java.util.Date modifiedTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeEnd;
 
	public RegUserFriendsGroup(){
	}

	public RegUserFriendsGroup(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public java.lang.Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getSort() {
		return this.sort;
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
	public Integer getUserNum() {
		return userNum;
	}
	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

