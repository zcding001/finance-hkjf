package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;
import java.util.Date;

import com.hongkun.finance.payment.model.FinPayConfig;

/**
 * @Description : 连连签约绑卡bean
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.BankCardSignInfo.java
 * @Author : yanbinghuang
 */
public class BankCardSignInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 注册时间
	 */
	private Date registerDate;
	/**
	 * 商户用户唯一标示
	 */
	private String userId;
	/**
	 * 商户用户登录名
	 */
	private String loginName;
	/**
	 * 用户邮箱email
	 */
	private String email;
	/**
	 * 用户手机号
	 */
	private String tel;
	/**
	 * 用户身份证号
	 */
	private String idCard;
	/**
	 * 用户真实姓名
	 */
	private String userName;
	/**
	 * 银行卡号
	 */
	private String cardNo;
	/**
	 * 连连签约异步返回地址
	 */
	private String urlReturn;
	/**
	 * 连连签约同步返回地址
	 */
	private String backUrl;
	/**
	 * 支付配置信息
	 */
	private FinPayConfig finPayConfig;

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUrlReturn() {
		return urlReturn;
	}

	public void setUrlReturn(String urlReturn) {
		this.urlReturn = urlReturn;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public FinPayConfig getFinPayConfig() {
		return finPayConfig;
	}

	public void setFinPayConfig(FinPayConfig finPayConfig) {
		this.finPayConfig = finPayConfig;
	}
}
