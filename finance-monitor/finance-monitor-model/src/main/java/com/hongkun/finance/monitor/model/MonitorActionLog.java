package com.hongkun.finance.monitor.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.monitor.model.MonitorActionLog.java
 * @Class Name    : MonitorActionLog.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class MonitorActionLog extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户登录手机号
	 * 字段: login  BIGINT(19)
	 */
	private java.lang.Long login;
	
	/**
	 * 描述: 用户操作行为
	 * 字段: action  VARCHAR(255)
	 * 默认值: ''
	 */
	private java.lang.String action;
	
	/**
	 * 描述: 用户操作行为
	 * 字段: id  INT(3)
	 * 默认值: 1 注册类型:1-一般用户,2-企业,3-物业,4-第三方账户,5-后台账户，6-后台root用户
	 */
	private java.lang.Integer type;
	
	/**
	 * 描述: 操作时间
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
 
	public MonitorActionLog(){
	}

	public MonitorActionLog(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setLogin(java.lang.Long login) {
		this.login = login;
	}
	
	public java.lang.Long getLogin() {
		return this.login;
	}
	
	public void setAction(java.lang.String action) {
		this.action = action;
	}
	
	public java.lang.String getAction() {
		return this.action;
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
	
	public java.lang.Integer getType() {
		return type;
	}

	public void setType(java.lang.Integer type) {
		this.type = type;
	}

	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

