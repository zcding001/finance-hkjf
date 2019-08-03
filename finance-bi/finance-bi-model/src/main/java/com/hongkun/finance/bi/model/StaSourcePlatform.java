package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaSourcePlatform.java
 * @Class Name    : StaSourcePlatform.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaSourcePlatform extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 自增ID
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 日期
	 * 字段: da  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date da;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date daBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date daEnd;
	/**
	 * 描述: 注册人数
	 * 字段: reg_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserCou;
	
	/**
	 * 描述: 实名人数
	 * 字段: real_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer realUserCou;
	
	/**
	 * 描述: 充值人数
	 * 字段: rechange_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer rechangeUserCou;
	
	/**
	 * 描述: 充值金额
	 * 字段: rechange_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal rechangeAmount;
	
	/**
	 * 描述: 充值次数
	 * 字段: rechange_times  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer rechangeTimes;
	
	/**
	 * 描述: 提现人数
	 * 字段: withdraw_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer withdrawUserCou;
	
	/**
	 * 描述: 提现金额
	 * 字段: withdraw_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal withdrawAmount;
	
	/**
	 * 描述: 提现次数
	 * 字段: withdraw_times  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer withdrawTimes;
	
	/**
	 * 描述: 投资用户人数
	 * 字段: invest_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer investUserCou;
	
	/**
	 * 描述: 投资总金额
	 * 字段: invest_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 投资次数
	 * 字段: invest_times  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer investTimes;
	
	/**
	 * 描述: 借款人数
	 * 字段: borrower_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer borrowerCou;
	
	/**
	 * 描述: 待收人数
	 * 字段: receipting_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer receiptingUserCou;
	
	/**
	 * 描述: 待收金额
	 * 字段: receipting_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingAmount;
	
	/**
	 * 描述: 已收人数
	 * 字段: receipted_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer receiptedUserCou;
	
	/**
	 * 描述: 实际已收金额
	 * 字段: receipted_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedAmount;
	
	/**
	 * 描述: 待还人数
	 * 字段: repaying_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer repayingUserCou;
	
	/**
	 * 描述: 待还金额
	 * 字段: repaying_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingAmount;
	
	/**
	 * 描述: 已还人数
	 * 字段: repayed_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer repayedUserCou;
	
	/**
	 * 描述: 已还金额
	 * 字段: repayed_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedAmount;
	
 
	public StaSourcePlatform(){
	}

	public StaSourcePlatform(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setDa(java.util.Date da) {
		this.da = da;
	}
	
	public java.util.Date getDa() {
		return this.da;
	}
	
	public void setDaBegin(java.util.Date daBegin) {
		this.daBegin = daBegin;
	}
	
	public java.util.Date getDaBegin() {
		return this.daBegin;
	}
	
	public void setDaEnd(java.util.Date daEnd) {
		this.daEnd = daEnd;
	}
	
	public java.util.Date getDaEnd() {
		return this.daEnd;
	}	
	public void setRegUserCou(java.lang.Integer regUserCou) {
		this.regUserCou = regUserCou;
	}
	
	public java.lang.Integer getRegUserCou() {
		return this.regUserCou;
	}
	
	public void setRealUserCou(java.lang.Integer realUserCou) {
		this.realUserCou = realUserCou;
	}
	
	public java.lang.Integer getRealUserCou() {
		return this.realUserCou;
	}
	
	public void setRechangeUserCou(java.lang.Integer rechangeUserCou) {
		this.rechangeUserCou = rechangeUserCou;
	}
	
	public java.lang.Integer getRechangeUserCou() {
		return this.rechangeUserCou;
	}
	
	public void setRechangeAmount(java.math.BigDecimal rechangeAmount) {
		this.rechangeAmount = rechangeAmount;
	}
	
	public java.math.BigDecimal getRechangeAmount() {
		return this.rechangeAmount;
	}
	
	public void setRechangeTimes(Integer rechangeTimes) {
		this.rechangeTimes = rechangeTimes;
	}
	
	public Integer getRechangeTimes() {
		return this.rechangeTimes;
	}
	
	public void setWithdrawUserCou(java.lang.Integer withdrawUserCou) {
		this.withdrawUserCou = withdrawUserCou;
	}
	
	public java.lang.Integer getWithdrawUserCou() {
		return this.withdrawUserCou;
	}
	
	public void setWithdrawAmount(java.math.BigDecimal withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	
	public java.math.BigDecimal getWithdrawAmount() {
		return this.withdrawAmount;
	}
	
	public void setWithdrawTimes(java.lang.Integer withdrawTimes) {
		this.withdrawTimes = withdrawTimes;
	}
	
	public java.lang.Integer getWithdrawTimes() {
		return this.withdrawTimes;
	}
	
	public void setInvestUserCou(java.lang.Integer investUserCou) {
		this.investUserCou = investUserCou;
	}
	
	public java.lang.Integer getInvestUserCou() {
		return this.investUserCou;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setInvestTimes(java.lang.Integer investTimes) {
		this.investTimes = investTimes;
	}
	
	public java.lang.Integer getInvestTimes() {
		return this.investTimes;
	}
	
	public void setBorrowerCou(java.lang.Integer borrowerCou) {
		this.borrowerCou = borrowerCou;
	}
	
	public java.lang.Integer getBorrowerCou() {
		return this.borrowerCou;
	}
	
	public void setReceiptingUserCou(java.lang.Integer receiptingUserCou) {
		this.receiptingUserCou = receiptingUserCou;
	}
	
	public java.lang.Integer getReceiptingUserCou() {
		return this.receiptingUserCou;
	}
	
	public void setReceiptingAmount(java.math.BigDecimal receiptingAmount) {
		this.receiptingAmount = receiptingAmount;
	}
	
	public java.math.BigDecimal getReceiptingAmount() {
		return this.receiptingAmount;
	}
	
	public void setReceiptedUserCou(java.lang.Integer receiptedUserCou) {
		this.receiptedUserCou = receiptedUserCou;
	}
	
	public java.lang.Integer getReceiptedUserCou() {
		return this.receiptedUserCou;
	}
	
	public void setReceiptedAmount(java.math.BigDecimal receiptedAmount) {
		this.receiptedAmount = receiptedAmount;
	}
	
	public java.math.BigDecimal getReceiptedAmount() {
		return this.receiptedAmount;
	}
	
	public void setRepayingUserCou(java.lang.Integer repayingUserCou) {
		this.repayingUserCou = repayingUserCou;
	}
	
	public java.lang.Integer getRepayingUserCou() {
		return this.repayingUserCou;
	}
	
	public void setRepayingAmount(java.math.BigDecimal repayingAmount) {
		this.repayingAmount = repayingAmount;
	}
	
	public java.math.BigDecimal getRepayingAmount() {
		return this.repayingAmount;
	}
	
	public void setRepayedUserCou(java.lang.Integer repayedUserCou) {
		this.repayedUserCou = repayedUserCou;
	}
	
	public java.lang.Integer getRepayedUserCou() {
		return this.repayedUserCou;
	}
	
	public void setRepayedAmount(java.math.BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	
	public java.math.BigDecimal getRepayedAmount() {
		return this.repayedAmount;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

