package com.hongkun.finance.payment.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description : 支付模块银行卡信息Vo
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.vo.BankCardVo.java
 * @Author : yanbinghuang
 */
public class BankCardVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;// bankcardbinding的ID
	/**
	 * 用户Id
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 银行名称 字段: bank_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String bankName;

	/**
	 * 描述: 银行卡号 字段: bank_card VARCHAR(30) 默认值: ''
	 */
	private java.lang.String bankCard;

	/**
	 * 描述: 支行名称 字段: branch_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String branchName;

	/**
	 * 描述: 省 字段: bank_province VARCHAR(10) 默认值: ''
	 */
	private java.lang.String bankProvince;

	/**
	 * 描述: 市 字段: bank_city VARCHAR(10) 默认值: ''
	 */
	private java.lang.String bankCity;
	/**
	 * 描述: 银行预留手机号 字段: bank_tel VARCHAR(10) 默认值: ''
	 */
	private java.lang.String bankTel;
	/**
	 * 平台银行编码
	 */
	private java.lang.String bankCode;

	/********************** FinBankCardBinding ****************************/
	/**
	 * 描述: 银行卡id 字段: fin_bank_card_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer finBankCardId;

	/**
	 * 描述: 第三方银行编码 字段: bank_third_code VARCHAR(20)
	 */
	private java.lang.String bankThirdCode;

	/**
	 * 描述: 第三方协议号 字段: third_account VARCHAR(50) 默认值: ''
	 */
	private java.lang.String thirdAccount;

	/**
	 * 描述: 支付渠道 字段: pay_channel TINYINT(3) 默认值: 0
	 */
	private Integer payChannel;

	/**
	 * 描述: 状态:0-初始化,1-绑卡未认证,2-已认证,3-未认证禁用,4-认证禁用 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;
	/**
	 * 单笔限额
	 */
	private BigDecimal singleLimit;
	/**
	 * 单日限额
	 */
	private BigDecimal singleDayLimit;
	/**
	 * 单月限额
	 */
	private BigDecimal singleMonthLimit;
	/**
	 * 银行图标地址
	 */
	private String bankIconAddress;

	public java.lang.String getBankCode() {
		return bankCode;
	}

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.lang.Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.String getBankName() {
		return bankName;
	}

	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}

	public java.lang.String getBankCard() {
		return bankCard;
	}

	public void setBankCard(java.lang.String bankCard) {
		this.bankCard = bankCard;
	}

	public java.lang.String getBranchName() {
		return branchName;
	}

	public void setBranchName(java.lang.String branchName) {
		this.branchName = branchName;
	}

	public java.lang.String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(java.lang.String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public java.lang.String getBankCity() {
		return bankCity;
	}

	public void setBankCity(java.lang.String bankCity) {
		this.bankCity = bankCity;
	}

	public java.lang.Integer getFinBankCardId() {
		return finBankCardId;
	}

	public void setFinBankCardId(java.lang.Integer finBankCardId) {
		this.finBankCardId = finBankCardId;
	}

	public java.lang.String getBankThirdCode() {
		return bankThirdCode;
	}

	public void setBankThirdCode(java.lang.String bankThirdCode) {
		this.bankThirdCode = bankThirdCode;
	}

	public java.lang.String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(java.lang.String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public Integer getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(BigDecimal singleLimit) {
		this.singleLimit = singleLimit;
	}

	public BigDecimal getSingleDayLimit() {
		return singleDayLimit;
	}

	public void setSingleDayLimit(BigDecimal singleDayLimit) {
		this.singleDayLimit = singleDayLimit;
	}

	public BigDecimal getSingleMonthLimit() {
		return singleMonthLimit;
	}

	public void setSingleMonthLimit(BigDecimal singleMonthLimit) {
		this.singleMonthLimit = singleMonthLimit;
	}

	public String getBankIconAddress() {
		return bankIconAddress;
	}

	public void setBankIconAddress(String bankIconAddress) {
		this.bankIconAddress = bankIconAddress;
	}

	public java.lang.String getBankTel() {
		return bankTel;
	}

	public void setBankTel(java.lang.String bankTel) {
		this.bankTel = bankTel;
	}
	
	
}
