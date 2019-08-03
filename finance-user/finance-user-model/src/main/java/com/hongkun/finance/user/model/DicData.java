package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.DicData.java
 * @Class Name    : DicData.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class DicData extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: ID  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 业务类型
	 * 字段: business_name  VARCHAR(30)
	 */
	private java.lang.String businessName;
	
	/**
	 * 描述: 实体类常量值
	 * 字段: subject_name  VARCHAR(30)
	 */
	private java.lang.String subjectName;
	
	/**
	 * 描述: 数据库字典值
	 * 字段: value  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer value;
	
	/**
	 * 描述: 数据字典名称
	 * 字段: name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String name;
	
 
	public DicData(){
	}

	public DicData(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setBusinessName(java.lang.String businessName) {
		this.businessName = businessName;
	}
	
	public java.lang.String getBusinessName() {
		return this.businessName;
	}
	
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	
	public java.lang.String getSubjectName() {
		return this.subjectName;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

