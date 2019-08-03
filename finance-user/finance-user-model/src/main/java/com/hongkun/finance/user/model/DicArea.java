package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.DicArea.java
 * @Class Name    : DicArea.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class DicArea extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 地区编码
	 * 字段: area_code  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String areaCode;

	/**
	 * 描述: 地区名称
	 * 字段: area_name  VARCHAR(120)
	 * 默认值: ''
	 */
	private java.lang.String areaName;

	/**
	 * 描述: 地区父级编码
	 * 字段: parent_code  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String parentCode;

	/**
	 * 描述: 地区等级，例如1级：省、市；2级县、区
	 * 字段: grade  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer grade;

	/**
	 * 描述: 0-不能选择，1-能进行选择
	 * 字段: status  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer status;

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

	public DicArea(){
	}

	public DicArea(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}

	public java.lang.String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaName(java.lang.String areaName) {
		this.areaName = areaName;
	}

	public java.lang.String getAreaName() {
		return this.areaName;
	}

	public void setParentCode(java.lang.String parentCode) {
		this.parentCode = parentCode;
	}

	public java.lang.String getParentCode() {
		return this.parentCode;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getGrade() {
		return this.grade;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
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

