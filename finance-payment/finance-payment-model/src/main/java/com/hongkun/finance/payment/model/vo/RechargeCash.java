
/**
 * @Title: RechargeCash.java
 * @Package com.hongkun.finance.payment.model
 * @Description: Copyright: Copyright (c) 2011 Company:北京亿润财富
 * 
 * @author Comsys-cxb
 * @date 2017年6月1日 下午3:41:35
 * @version V1.0
 */
package com.hongkun.finance.payment.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @Description : 值对象vo
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.vo.RechargeCash.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
public class RechargeCash implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 银行卡号
	 */
	private String bankCard;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 第三方协议号
	 */
	private String thirdAccount;
	/**
	 * 省
	 */
	private String bankProvince;
	/**
	 * 市
	 */
	private String bankCity;
	/**
	 * 支行名称
	 */
	private String branchName;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 交易金额（元）
	 */
	private BigDecimal transMoney;
	/**
	 * 银行卡ID
	 */
	private Integer bankCardId;
	/**
	 * 身份证号
	 */
	private String idCard;
	/***
	 * 平台类型 PlatformSourceEnums10-PC 11-IOS 12-ANDRIOD 13-WAP
	 */
	private String platformSourceName;
	/**
	 * 系统类型 SystemTypeEnums QKD("QKD", 1), QSH("QSH", 2), HKJF("HKJF", 3),
	 * CXVIP("CXVIP", 4);
	 */
	private String systemTypeName;
	/**
	 * 支付渠道 PayChannelEnum
	 * 
	 * LianLian("lianlian", 1), LianDong("liandong", 2), BaoFu("baofu", 3);
	 */
	private String payChannel;
	/**
	 * 支付方式 PayStyleEnum 快捷支付KJ("KJ", 20) 认证支付RZ("RZ", 21) 网银支付WY("WY", 22)
	 * 实时付款SSFK("SSFK", 23) 分期付款FQFK("FQFK", 24) 批次付款 PCFK("PCFK", 25)
	 * 代扣DK("DK", 26)
	 */
	private String payStyle;
	/**
	 * 用户银行预留手机号
	 */
	private String tel;
	/**
	 * 用户登录手机号
	 */
	private String loginTel;
	/**
	 * 协议号和卡号同时存在时，优先将协议号送给连连，不要将协议号和卡号都送给连连
	 */
	private String noAgree;
	/**
	 * 用户类型
	 */
	private Integer uType;
	/**
	 * 支付业务表流水ID
	 */
	private String paymentFlowId;
	/**
	 * 加密类型 RAS /MD5
	 */
	private String sign_type;
	/**
	 * 签名 加密签名
	 */
	private String sign;
	/**
	 * 充值标识 0:正常充值 1：投资充值
	 */
	private Integer rechargeFlag = 0;
	/**
	 * 验证码
	 */
	private String verificationCode;
	/**
	 * 操作类型 1：预绑卡，2：确认绑卡，3：直接支付（无需短信验证码），4：预支付，5：确认支付（需要预支付的唯一码） 6：易宝收银台短信重发
	 */
	private Integer operateType;
	/**
	 * 唯一码（如果上一步是预操作，唯一码是必填项）
	 */
	private String payUnionCode;
	/**
	 * 交易流水号
	 */
	private String flowId;
	/**
	 * 用户注册时间
	 */
	public Date registerTime;
	
	
	

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getPaymentFlowId() {
		return paymentFlowId;
	}

	public void setPaymentFlowId(String paymentFlowId) {
		this.paymentFlowId = paymentFlowId;
	}

	public Integer getRechargeFlag() {
		return rechargeFlag;
	}

	public void setRechargeFlag(Integer rechargeFlag) {
		this.rechargeFlag = rechargeFlag;
	}

	public Integer getuType() {
		return uType;
	}

	public void setuType(Integer uType) {
		this.uType = uType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNoAgree() {
		return noAgree;
	}

	public void setNoAgree(String noAgree) {
		this.noAgree = noAgree;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLoginTel() {
		return loginTel;
	}

	public void setLoginTel(String loginTel) {
		this.loginTel = loginTel;
	}

	public String getPlatformSourceName() {
		return platformSourceName;
	}

	public void setPlatformSourceName(String platformSourceName) {
		this.platformSourceName = platformSourceName;
	}

	public String getSystemTypeName() {
		return systemTypeName;
	}

	public void setSystemTypeName(String systemTypeName) {
		this.systemTypeName = systemTypeName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Integer getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(Integer bankCardId) {
		this.bankCardId = bankCardId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(String payStyle) {
		this.payStyle = payStyle;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getPayUnionCode() {
		return payUnionCode;
	}

	public void setPayUnionCode(String payUnionCode) {
		this.payUnionCode = payUnionCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
