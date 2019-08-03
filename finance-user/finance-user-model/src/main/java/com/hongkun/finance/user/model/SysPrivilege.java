package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.model.SysPrivilege.java
 * @Class Name    : SysPrivilege.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysPrivilege extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 权限名称
	 * 字段: priv_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String privName;
	
	/**
	 * 描述: 权限描述
	 * 字段: priv_desc  VARCHAR(50)
	 * 默认值: ''
	 */
	private String privDesc;
	
	/**
	 * 描述: 控制url
	 * 字段: priv_url  VARCHAR(50)
	 * 默认值: ''
	 */
	private String privUrl;
	
	/**
	 * 描述: 创建用户id
	 * 字段: create_user_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private Integer createUserId;
	
	/**
	 * 描述: 修改用户id
	 * 字段: modify_user_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private Integer modifyUserId;
	
	/**
	 * 描述: 状态:0-删除,1-正常,2-禁用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public SysPrivilege(){
	}

	public SysPrivilege(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setPrivName(String privName) {
		this.privName = privName;
	}
	
	public String getPrivName() {
		return this.privName;
	}
	
	public void setPrivDesc(String privDesc) {
		this.privDesc = privDesc;
	}
	
	public String getPrivDesc() {
		return this.privDesc;
	}
	
	public void setPrivUrl(String privUrl) {
		this.privUrl = privUrl;
	}
	
	public String getPrivUrl() {
		return this.privUrl;
	}
	
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public Integer getModifyUserId() {
		return this.modifyUserId;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
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
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}
	
	public java.util.Date getModifyTimeBegin() {
		return this.modifyTimeBegin;
	}
	
	public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}
	
	public java.util.Date getModifyTimeEnd() {
		return this.modifyTimeEnd;
	}	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

