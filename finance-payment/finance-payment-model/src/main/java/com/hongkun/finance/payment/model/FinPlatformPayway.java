package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinPlatformPayway.java
 * @Class Name    : FinPlatformPayway.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinPlatformPayway extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 系统名称
	 * 字段: sys_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String sysName;
	
	/**
	 * 描述: 系统名称编码
	 * 字段: sys_name_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String sysNameCode;
	
	/**
	 * 描述: 平台名称
	 * 字段: platform_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String platformName;
	
	/**
	 * 描述: 第三方名称
	 * 字段: third_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String thirdName;
	
	/**
	 * 描述: 第三方名称编码
	 * 字段: third_name_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String thirdNameCode;
	
	/**
	 * 描述: 支付名称
	 * 字段: payway_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String paywayName;
	
	/**
	 * 描述: 支付编码
	 * 字段: payway_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String paywayCode;
	
	/**
	 * 描述: 状态:0-删除,1-正常
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	/**
	 * 描述: 适用用户类型1普通用户2企业用户
	 * 字段: userType  VARCHAR(10)
	 * 默认值: 1
	 */
	private String userType;
	
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
 
	public FinPlatformPayway(){
	}

	public FinPlatformPayway(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setSysName(java.lang.String sysName) {
		this.sysName = sysName;
	}
	
	public java.lang.String getSysName() {
		return this.sysName;
	}
	
	public void setSysNameCode(java.lang.String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}
	
	public java.lang.String getSysNameCode() {
		return this.sysNameCode;
	}
	
	public void setPlatformName(java.lang.String platformName) {
		this.platformName = platformName;
	}
	
	public java.lang.String getPlatformName() {
		return this.platformName;
	}
	
	public void setThirdName(java.lang.String thirdName) {
		this.thirdName = thirdName;
	}
	
	public java.lang.String getThirdName() {
		return this.thirdName;
	}
	
	public void setThirdNameCode(java.lang.String thirdNameCode) {
		this.thirdNameCode = thirdNameCode;
	}
	
	public java.lang.String getThirdNameCode() {
		return this.thirdNameCode;
	}
	
	public void setPaywayName(java.lang.String paywayName) {
		this.paywayName = paywayName;
	}
	
	public java.lang.String getPaywayName() {
		return this.paywayName;
	}
	
	public void setPaywayCode(java.lang.String paywayCode) {
		this.paywayCode = paywayCode;
	}
	
	public java.lang.String getPaywayCode() {
		return this.paywayCode;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public String getUserType() {
		return userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
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

