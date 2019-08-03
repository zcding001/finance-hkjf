package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegAuditRule.java
 * @Class Name    : RegAuditRule.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegAuditRule extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 名称
	 * 字段: name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 启用状态 0：禁用 1:启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 规则类型 1：身份证
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer type;
	
	/**
	 * 描述: createTime
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: modifiedTime
	 * 字段: modified_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifiedTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeEnd;
	
	private List<String> picList = new ArrayList<>();
 
	public RegAuditRule(){
	}

	public RegAuditRule(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
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

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

