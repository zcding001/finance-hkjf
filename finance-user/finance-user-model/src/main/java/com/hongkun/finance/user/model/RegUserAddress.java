package com.hongkun.finance.user.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.SAVE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserAddress.java
 * @Class Name    : RegUserAddress.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserAddress extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 省的编码
	 * 字段: province  VARCHAR(30)
	 * 默认值: ''
	 */
	@NotNull(groups = {SAVE.class})
	private java.lang.String province;

	/**
	 * 描述: 市的编码
	 * 字段: city  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String city;

	/**
	 * 描述: 县的编码
	 * 字段: county  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String county;

	/**
	 * 描述: 收货人姓名
	 * 字段: consignee  VARCHAR(30)
	 * 默认值: ''
	 */
	@NotNull(groups = {SAVE.class})
	private java.lang.String consignee;

	/**
	 * 描述: 用户ID
	 * 字段: reg_user_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 详细地址
	 * 字段: district  VARCHAR(100)
	 * 默认值: ''
	 */
	@NotNull(groups = {SAVE.class})
	private java.lang.String district;

	/**
	 * 描述: 手机电话
	 * 字段: mobile_phone  BIGINT UNSIGNED(20)
	 * 默认值: 0
	 */
	@NotNull(groups = {SAVE.class})
	private java.lang.Long mobilePhone;

	/**
	 * 描述: 固定电话
	 * 字段: telephone  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String telephone;

	/**
	 * 描述: 地址状态：0-已删除  1-启用  2-禁用  3-默认地址
	 * 字段: state  TINYINT UNSIGNED(3)
	 * 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 地址别名
	 * 字段: alias  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String alias;

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

	public RegUserAddress(){
	}

	public RegUserAddress(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setProvince(java.lang.String province) {
		this.province = province;
	}

	public java.lang.String getProvince() {
		return this.province;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getCity() {
		return this.city;
	}

	public void setCounty(java.lang.String county) {
		this.county = county;
	}

	public java.lang.String getCounty() {
		return this.county;
	}

	public void setConsignee(java.lang.String consignee) {
		this.consignee = consignee;
	}

	public java.lang.String getConsignee() {
		return this.consignee;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}

	public void setDistrict(java.lang.String district) {
		this.district = district;
	}

	public java.lang.String getDistrict() {
		return this.district;
	}

	public void setMobilePhone(java.lang.Long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public java.lang.Long getMobilePhone() {
		return this.mobilePhone;
	}

	public void setTelephone(java.lang.String telephone) {
		this.telephone = telephone;
	}

	public java.lang.String getTelephone() {
		return this.telephone;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return this.state;
	}

	public void setAlias(java.lang.String alias) {
		this.alias = alias;
	}

	public java.lang.String getAlias() {
		return this.alias;
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