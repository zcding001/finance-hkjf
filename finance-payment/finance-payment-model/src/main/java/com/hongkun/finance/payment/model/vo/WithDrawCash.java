package com.hongkun.finance.payment.model.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;

public class WithDrawCash implements Serializable {

	private static final long serialVersionUID = 1L;
	private String flowId;// 支付记录表ID
	private Integer regUserId;// 用户ID
	private Integer userType;// 用户类型,对公对私，1对私 2对公
	private String userName;// 用户姓名
	private String loginTel;// 用户手机号
	private String idCard;// 身份证号
	/**
	 * 支付渠道 PayChannelEnum
	 * 
	 * LianLian("lianlian", "连连"), LianDong("liandong", "联动");
	 */
	private PayChannelEnum payChannelEnum;// 交易渠道
	private String orderDesc;// 订单描述
	/**
	 * 系统类型 QKD QSH HKJF SystemTypeEnums
	 */
	private String sysNameCode;
	/***
	 * 平台类型 PlatformSourceEnums PC("PC", 1), WAP("WAP", 2), ANDROID("ANDROID",
	 * 3), IOS("IOS", 4), WX("WX", 5);
	 */
	private String platformSourceName;
	/**
	 * 支付方式 PayStyleEnum
	 * 
	 * 20：快捷支付 21 认证支付 22：网银支付 23 实时付款 24 分期付款 25 批次付款
	 */
	private PayStyleEnum payStyleEnum;

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public PayStyleEnum getPayStyleEnum() {
		return payStyleEnum;
	}

	public void setPayStyleEnum(PayStyleEnum payStyleEnum) {
		this.payStyleEnum = payStyleEnum;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public PayChannelEnum getPayChannelEnum() {
		return payChannelEnum;
	}

	public void setPayChannelEnum(PayChannelEnum payChannelEnum) {
		this.payChannelEnum = payChannelEnum;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getSysNameCode() {
		return sysNameCode;
	}

	public void setSysNameCode(String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
