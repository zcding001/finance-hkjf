package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;

/**
 * @Description : 连连签约接口bean
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.AgreeNoAuthApply.java
 * @Author : yanbinghuang
 */

public class AgreeNoAuthApply implements Serializable {
	private static final long serialVersionUID = 1L;
	// 商户提交参数
	private String platform; // 平台来源
	private String user_id; // 用户唯一编号（商户用户唯一编号）
	private String oid_partner; // 商户编号
	private String sign_type; // 签名方式 RSA或者MD5
	private String sign; // RSA签名

	private String api_version; // 版本号
	private String repayment_plan; // 还款计划
	private String repayment_no; // 还款计划编号
	private String sms_param; // 短信参数

	private String pay_type; // 支付方式
	private String no_agree; // 签约协议号

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
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

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getRepayment_plan() {
		return repayment_plan;
	}

	public void setRepayment_plan(String repayment_plan) {
		this.repayment_plan = repayment_plan;
	}

	public String getRepayment_no() {
		return repayment_no;
	}

	public void setRepayment_no(String repayment_no) {
		this.repayment_no = repayment_no;
	}

	public String getSms_param() {
		return sms_param;
	}

	public void setSms_param(String sms_param) {
		this.sms_param = sms_param;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

}
