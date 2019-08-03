package com.hongkun.finance.roster.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance-roster
 * @Program Name  : com.hongkun.finance.roster.model.SysFunctionCfg.java
 * @Class Name    : SysFunctionCfg.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysFunctionCfg extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 功能名称
	 * 字段: title  VARCHAR(50)
	 */
	private java.lang.String title;
	
	/**
	 * 描述: 功能编码
	 * 字段: func_code  VARCHAR(20)
	 */
	private java.lang.String funcCode;
	
	/**
	 * 描述: 是否启用 1启用;0不启用
	 * 字段: isEnable  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer isEnable;
	
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String note;
	
	/**
	 * 描述: 创建时间
	 * 字段: created_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createdTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modified_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date modifiedTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeEnd;
 
	public SysFunctionCfg(){
	}

	public SysFunctionCfg(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	public java.lang.String getTitle() {
		return this.title;
	}
	
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
	public Integer getIsEnable() {
		return this.isEnable;
	}
	
	public void setNote(java.lang.String note) {
		this.note = note;
	}
	
	public java.lang.String getNote() {
		return this.note;
	}
	
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	
	public void setCreatedTimeBegin(java.util.Date createdTimeBegin) {
		this.createdTimeBegin = createdTimeBegin;
	}
	
	public java.util.Date getCreatedTimeBegin() {
		return this.createdTimeBegin;
	}
	
	public void setCreatedTimeEnd(java.util.Date createdTimeEnd) {
		this.createdTimeEnd = createdTimeEnd;
	}
	
	public java.util.Date getCreatedTimeEnd() {
		return this.createdTimeEnd;
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

