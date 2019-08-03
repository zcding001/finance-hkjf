package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinCityRefer.java
 * @Class Name    : FinCityRefer.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinCityRefer extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 渠道名称
	 * 字段: third_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String thirdName;
	
	/**
	 * 描述: 渠道编码
	 * 字段: third_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String thirdCode;
	
	/**
	 * 描述: 省
	 * 字段: province  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String province;
	
	/**
	 * 描述: 省编码
	 * 字段: province_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String provinceCode;
	
	/**
	 * 描述: 第三方省编码
	 * 字段: province_third_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String provinceThirdCode;
	
	/**
	 * 描述: 市
	 * 字段: city  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String city;
	
	/**
	 * 描述: 市编码
	 * 字段: city_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String cityCode;
	
	/**
	 * 描述: 第三方市编码
	 * 字段: city_third_code  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String cityThirdCode;
	
	/**
	 * 描述: 状态:0-删除,1-正常
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
 
	public FinCityRefer(){
	}

	public FinCityRefer(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setThirdName(java.lang.String thirdName) {
		this.thirdName = thirdName;
	}
	
	public java.lang.String getThirdName() {
		return this.thirdName;
	}
	
	public void setThirdCode(java.lang.String thirdCode) {
		this.thirdCode = thirdCode;
	}
	
	public java.lang.String getThirdCode() {
		return this.thirdCode;
	}
	
	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	
	public java.lang.String getProvince() {
		return this.province;
	}
	
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	public java.lang.String getProvinceCode() {
		return this.provinceCode;
	}
	
	public void setProvinceThirdCode(java.lang.String provinceThirdCode) {
		this.provinceThirdCode = provinceThirdCode;
	}
	
	public java.lang.String getProvinceThirdCode() {
		return this.provinceThirdCode;
	}
	
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	public java.lang.String getCity() {
		return this.city;
	}
	
	public void setCityCode(java.lang.String cityCode) {
		this.cityCode = cityCode;
	}
	
	public java.lang.String getCityCode() {
		return this.cityCode;
	}
	
	public void setCityThirdCode(java.lang.String cityThirdCode) {
		this.cityThirdCode = cityThirdCode;
	}
	
	public java.lang.String getCityThirdCode() {
		return this.cityThirdCode;
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

