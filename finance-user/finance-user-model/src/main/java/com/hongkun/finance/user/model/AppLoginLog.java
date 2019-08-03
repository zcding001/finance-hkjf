package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.AppLoginLog.java
 * @Class Name    : AppLoginLog.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class AppLoginLog extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 手机操作系统名称
	 * 字段: operate_system  VARCHAR(10)
	 * 默认值: '0'
	 */
	private String operateSystem;
	
	/**
	 * 描述: app的版本号
	 * 字段: app_version  FLOAT(5)
	 * 默认值: 0.00
	 */
	private String appVersion;
	
	/**
	 * 描述: 设备号
	 * 字段: device_name  VARCHAR(10)
	 */
	private String deviceName;
	
	/**
	 * 描述: 设备id
	 * 字段: device_id  INT(10)
	 */
	private String deviceId;
	
	/**
	 * 描述: 上次登录时间
	 * 字段: last_login_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date lastLoginTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date lastLoginTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date lastLoginTimeEnd;
	/**
	 * 描述: 创建时间，首次登录时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	//用户手机号 列表显示用
	private java.lang.Long login;
 
	public AppLoginLog(){
	}

	public AppLoginLog(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setOperateSystem(String operateSystem) {
		this.operateSystem = operateSystem;
	}
	
	public String getOperateSystem() {
		return this.operateSystem;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public String getAppVersion() {
		return this.appVersion;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public String getDeviceName() {
		return this.deviceName;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceId() {
		return this.deviceId;
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

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

