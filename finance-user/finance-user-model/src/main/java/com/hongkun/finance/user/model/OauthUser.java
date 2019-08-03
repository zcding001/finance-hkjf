package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.OauthUser.java
 * @Class Name    : OauthUser.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class OauthUser extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: guid
	 * 字段: guid  VARCHAR(255)
	 */
	private java.lang.String guid;
	
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
	 * 描述: archived
	 * 字段: archived  BIT(0)
	 * 默认值: 0
	 */
	private java.lang.Boolean archived;
	
	/**
	 * 描述: email
	 * 字段: email  VARCHAR(255)
	 */
	private java.lang.String email;
	
	/**
	 * 描述: password
	 * 字段: password  VARCHAR(255)
	 */
	private java.lang.String password;
	
	/**
	 * 描述: phone
	 * 字段: phone  VARCHAR(255)
	 */
	private java.lang.String phone;
	
	/**
	 * 描述: username
	 * 字段: username  VARCHAR(255)
	 */
	private java.lang.String username;
	
	/**
	 * 描述: defaultUser
	 * 字段: default_user  BIT(0)
	 * 默认值: 0
	 */
	private java.lang.Boolean defaultUser;
	
	/**
	 * 描述: lastLoginTime
	 * 字段: last_login_time  DATETIME(19)
	 */
	private java.util.Date lastLoginTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lastLoginTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date lastLoginTimeEnd;
 
	public OauthUser(){
	}

	public OauthUser(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setGuid(java.lang.String guid) {
		this.guid = guid;
	}
	
	public java.lang.String getGuid() {
		return this.guid;
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
	public void setArchived(java.lang.Boolean archived) {
		this.archived = archived;
	}
	
	public java.lang.Boolean getArchived() {
		return this.archived;
	}
	
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	
	public java.lang.String getEmail() {
		return this.email;
	}
	
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	
	public java.lang.String getPassword() {
		return this.password;
	}
	
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}
	
	public java.lang.String getPhone() {
		return this.phone;
	}
	
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	
	public java.lang.String getUsername() {
		return this.username;
	}
	
	public void setDefaultUser(java.lang.Boolean defaultUser) {
		this.defaultUser = defaultUser;
	}
	
	public java.lang.Boolean getDefaultUser() {
		return this.defaultUser;
	}
	
	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public java.util.Date getLastLoginTime() {
		return this.lastLoginTime;
	}
	
	public void setLastLoginTimeBegin(java.util.Date lastLoginTimeBegin) {
		this.lastLoginTimeBegin = lastLoginTimeBegin;
	}
	
	public java.util.Date getLastLoginTimeBegin() {
		return this.lastLoginTimeBegin;
	}
	
	public void setLastLoginTimeEnd(java.util.Date lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}
	
	public java.util.Date getLastLoginTimeEnd() {
		return this.lastLoginTimeEnd;
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

