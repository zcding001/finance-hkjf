package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinPayConfig.java
 * @Class Name    : FinPayConfig.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinPayConfig extends BaseModel {
	
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
	 * 描述: 平台名称PC、WAP、IOS、ANDROID
	 * 字段: platform_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String platformName;
	
	/**
	 * 描述: 第三方名称(连连、联动、新浪)
	 * 字段: third_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String thirdName;
	
	/**
	 * 描述: 版本名称编码(lianlian、baofu)
	 * 字段: third_name_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String thirdNameCode;
	
	/**
	 * 描述: 支付名称(快捷、网银、认证、实时付款)
	 * 字段: payway_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String paywayName;
	
	/**
	 * 描述: 支付编码(KJ、WY、RZ、SSFK)
	 * 字段: payway_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String paywayCode;
	
	/**
	 * 描述: 商戶编号
	 * 字段: merchant_no  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String merchantNo;
	
	/**
	 * 描述: 第三方公钥
	 * 字段: public_key  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String publicKey;
	
	/**
	 * 描述: 商户私钥
	 * 字段: private_key  VARCHAR(1500)
	 * 默认值: ''
	 */
	private java.lang.String privateKey;
	
	/**
	 * 描述: 接口版本号
	 * 字段: pay_version  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String payVersion;
	
	/**
	 * 描述: 业务类型
	 * 字段: business_type  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String businessType;
	
	/**
	 * 描述: 支付收银台支付服务地址
	 * 字段: pay_url  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String payUrl;
	
	/**
	 * 描述: 异步通知地址
	 * 字段: async_notice_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String asyncNoticeUrl;
	
	/**
	 * 描述: 同步通知地址
	 * 字段: sync_notice_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String syncNoticeUrl;
	
	/**
	 * 描述: 平台返回地址
	 * 字段: back_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String backUrl;
	
	/**
	 * 描述: 平台IP地址
	 * 字段: ip_address  VARCHAR(100)
	 * 默认值: ''
	 */
	private java.lang.String ipAddress;
	
	/**
	 * 描述: 签名方式（RSA或MD5）
	 * 字段: sign_style  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String signStyle;
	
	/**
	 * 描述: md5Key， 有时用作备用字段，当做 证书密码来用
	 * 字段: pay_md5_key  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String payMd5Key;
	
	/**
	 * 描述: 终端号
	 * 字段: terminal_id varchar(10)
	 * 默认值: ''
	 */
	private String terminalId;
	
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
	
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public FinPayConfig(){
	}

	public FinPayConfig(java.lang.Integer id){
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
	
	public void setMerchantNo(java.lang.String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	public java.lang.String getMerchantNo() {
		return this.merchantNo;
	}
	
	public void setPublicKey(java.lang.String publicKey) {
		this.publicKey = publicKey;
	}
	
	public java.lang.String getPublicKey() {
		return this.publicKey;
	}
	
	public void setPrivateKey(java.lang.String privateKey) {
		this.privateKey = privateKey;
	}
	
	public java.lang.String getPrivateKey() {
		return this.privateKey;
	}
	
	public void setPayVersion(java.lang.String payVersion) {
		this.payVersion = payVersion;
	}
	
	public java.lang.String getPayVersion() {
		return this.payVersion;
	}
	
	public void setBusinessType(java.lang.String businessType) {
		this.businessType = businessType;
	}
	
	public java.lang.String getBusinessType() {
		return this.businessType;
	}
	
	public void setPayUrl(java.lang.String payUrl) {
		this.payUrl = payUrl;
	}
	
	public java.lang.String getPayUrl() {
		return this.payUrl;
	}
	
	public void setAsyncNoticeUrl(java.lang.String asyncNoticeUrl) {
		this.asyncNoticeUrl = asyncNoticeUrl;
	}
	
	public java.lang.String getAsyncNoticeUrl() {
		return this.asyncNoticeUrl;
	}
	
	public void setSyncNoticeUrl(java.lang.String syncNoticeUrl) {
		this.syncNoticeUrl = syncNoticeUrl;
	}
	
	public java.lang.String getSyncNoticeUrl() {
		return this.syncNoticeUrl;
	}
	
	public void setBackUrl(java.lang.String backUrl) {
		this.backUrl = backUrl;
	}
	
	public java.lang.String getBackUrl() {
		return this.backUrl;
	}
	
	public void setIpAddress(java.lang.String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public java.lang.String getIpAddress() {
		return this.ipAddress;
	}
	
	public void setSignStyle(java.lang.String signStyle) {
		this.signStyle = signStyle;
	}
	
	public java.lang.String getSignStyle() {
		return this.signStyle;
	}
	
	public void setPayMd5Key(java.lang.String payMd5Key) {
		this.payMd5Key = payMd5Key;
	}
	
	public java.lang.String getPayMd5Key() {
		return this.payMd5Key;
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

