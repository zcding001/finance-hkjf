package com.hongkun.finance.payment.model;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinBankCard.java
 * @Class Name : FinBankCard.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinBankCard extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 用户id 字段: reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 银行编码 字段: bank_code VARCHAR(20) 默认值: ''
	 */
	private java.lang.String bankCode;

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
	 * 描述: 银行预留手机号 字段: bank_tel VARCHAR(15) 默认值: ''
	 */
	private java.lang.String bankTel;
	/**
	 * 描述: 状态:0-初始化,1-绑卡未认证,2-已认证,3-未认证禁用,4-认证禁用 字段: state TINYINT(3) 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	//查询用
	private List<Integer> stateList;

	/**
	 * 银行卡支付单笔限额
	 */
	private BigDecimal singleLimit;

	public FinBankCard() {
	}

	public FinBankCard(java.lang.Integer id) {
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

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public java.lang.String getBankCode() {
		return this.bankCode;
	}

	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}

	public java.lang.String getBankName() {
		return this.bankName;
	}

	public void setBankCard(java.lang.String bankCard) {
		this.bankCard = bankCard;
	}

	public java.lang.String getBankCard() {
		return this.bankCard;
	}

	public void setBranchName(java.lang.String branchName) {
		this.branchName = branchName;
	}

	public java.lang.String getBranchName() {
		return this.branchName;
	}

	public void setBankProvince(java.lang.String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public java.lang.String getBankProvince() {
		return this.bankProvince;
	}

	public void setBankCity(java.lang.String bankCity) {
		this.bankCity = bankCity;
	}

	public java.lang.String getBankCity() {
		return this.bankCity;
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

	public BigDecimal getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(BigDecimal singleLimit) {
		this.singleLimit = singleLimit;
	}

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}
	
	public java.lang.String getBankTel() {
		return bankTel;
	}
	
	public void setBankTel(java.lang.String bankTel) {
		this.bankTel = bankTel;
	}
	
	@Override
	public String toString() {
		return "FinBankCard{" +
				"id=" + id +
				", regUserId=" + regUserId +
				", bankCode='" + bankCode + '\'' +
				", bankName='" + bankName + '\'' +
				", bankCard='" + bankCard + '\'' +
				", branchName='" + branchName + '\'' +
				", bankProvince='" + bankProvince + '\'' +
				", bankCity='" + bankCity + '\'' +
				", bankTel='" + bankTel + '\'' +
				", state=" + state +
				", createTime=" + createTime +
				", createTimeBegin=" + createTimeBegin +
				", createTimeEnd=" + createTimeEnd +
				", modifyTime=" + modifyTime +
				", modifyTimeBegin=" + modifyTimeBegin +
				", modifyTimeEnd=" + modifyTimeEnd +
				", stateList=" + stateList +
				", singleLimit=" + singleLimit +
				'}';
	}
}
