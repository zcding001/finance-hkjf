package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.model.RegCompanyInfo.java
 * @Class Name    : RegCompanyInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegCompanyInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 企业id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 企业code
	 * 字段: code  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String code;
	
	/**
	 * 描述: 企业名称
	 * 字段: login_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String enterpriseName;
	
	/**
	 * 描述: 法人代表
	 * 字段: corporate  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String corporate;
	
	/**
	 * 描述: 代理人
	 * 字段: agent  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String agent;
	
	/**
	 * 描述: 营业执照号码
	 * 字段: license_no  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String licenseNo;
	
	/**
	 * 描述: 联系电话
	 * 字段: tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String tel;

	/**
	 * 描述: 法人手机号
	 * 字段: tel  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String legalTel;
	
	/**
	 * 描述: 开户银行许可证
	 * 字段: bank_license_url  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String bankLicenseUrl;
	
	/**
	 * 描述: 组织机构代码
	 * 字段: org_no  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String orgNo;
	
	/**
	 * 描述: 法人身份证号
	 * 字段: legalld_no  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String legalldNo;
	
	/**
	 * 描述: 税务登记号
	 * 字段: tax_no  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String taxNo;
	
	/**
	 * 描述: 联系人邮箱
	 * 字段: email  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String email;
	
	/**
	 * 描述: 注册地址所在省
	 * 字段: register_province  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String registerProvince;
	
	/**
	 * 描述: 注册地址所在市
	 * 字段: register_city  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String registerCity;
	
	/**
	 * 描述: 注册地址所在县
	 * 字段: register_country  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String registerCountry;
	
	/**
	 * 描述: 注册详细地址
	 * 字段: register_address  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String registerAddress;
	
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
 
	public RegCompanyInfo(){
	}

	public RegCompanyInfo(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setCode(java.lang.String code) {
		this.code = code;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	
	public void setEnterpriseName(java.lang.String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public java.lang.String getEnterpriseName() {
		return this.enterpriseName;
	}
	
	public void setCorporate(java.lang.String corporate) {
		this.corporate = corporate;
	}
	
	public java.lang.String getCorporate() {
		return this.corporate;
	}
	
	public void setAgent(java.lang.String agent) {
		this.agent = agent;
	}
	
	public java.lang.String getAgent() {
		return this.agent;
	}
	
	public void setLicenseNo(java.lang.String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public java.lang.String getLicenseNo() {
		return this.licenseNo;
	}
	
	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}
	
	public java.lang.String getTel() {
		return this.tel;
	}
	
	public void setBankLicenseUrl(java.lang.String bankLicenseUrl) {
		this.bankLicenseUrl = bankLicenseUrl;
	}
	
	public java.lang.String getBankLicenseUrl() {
		return this.bankLicenseUrl;
	}
	
	public void setOrgNo(java.lang.String orgNo) {
		this.orgNo = orgNo;
	}
	
	public java.lang.String getOrgNo() {
		return this.orgNo;
	}
	
	public void setLegalldNo(java.lang.String legalldNo) {
		this.legalldNo = legalldNo;
	}
	
	public java.lang.String getLegalldNo() {
		return this.legalldNo;
	}
	
	public void setTaxNo(java.lang.String taxNo) {
		this.taxNo = taxNo;
	}
	
	public java.lang.String getTaxNo() {
		return this.taxNo;
	}
	
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	
	public java.lang.String getEmail() {
		return this.email;
	}
	
	public void setRegisterProvince(java.lang.String registerProvince) {
		this.registerProvince = registerProvince;
	}
	
	public java.lang.String getRegisterProvince() {
		return this.registerProvince;
	}
	
	public void setRegisterCity(java.lang.String registerCity) {
		this.registerCity = registerCity;
	}
	
	public java.lang.String getRegisterCity() {
		return this.registerCity;
	}
	
	public void setRegisterCountry(java.lang.String registerCountry) {
		this.registerCountry = registerCountry;
	}
	
	public java.lang.String getRegisterCountry() {
		return this.registerCountry;
	}
	
	public void setRegisterAddress(java.lang.String registerAddress) {
		this.registerAddress = registerAddress;
	}
	
	public java.lang.String getRegisterAddress() {
		return this.registerAddress;
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

	public java.lang.String getLegalTel() {
		return this.legalTel;
	}

	public void setLegalTel(java.lang.String legalTel) {
		this.legalTel = legalTel;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

