package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.FundProject.java
 * @Class Name    : FundProject.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FundProject extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 项目类型名称
	 * 字段: name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String name;
	
	/**
	 * 描述: 1-明星独角兽系列,2- PRE-IPO系列,3-产业协同系列,4-地产基金,5-海外基金 6-信托产品
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer type;
	
	/**
	 * 描述: 1-私募基金,2-海外基金,3-信托产品
	 * 字段: parent_type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer parentType;
	
	/**
	 * 描述: 项目类型介绍
	 * 字段: introduction  TEXT(65535)
	 */
	private java.lang.String introduction;
	
	/**
	 * 描述: 备注
	 * 字段: remark  TEXT(65535)
	 */
	private java.lang.String remark;
	
 
	public FundProject(){
	}

	public FundProject(java.lang.Integer id){
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
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}
	
	public Integer getParentType() {
		return this.parentType;
	}
	
	public void setIntroduction(java.lang.String introduction) {
		this.introduction = introduction;
	}
	
	public java.lang.String getIntroduction() {
		return this.introduction;
	}
	
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

