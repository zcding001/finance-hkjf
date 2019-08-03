package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaReceipt.java
 * @Class Name    : StaReceipt.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaReceipt extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 自增ID
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 日期
	 * 字段: da  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date da;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daEnd;
	/**
	 * 描述: 产品类型
	 * 字段: pro_type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer proType;
	
	/**
	 * 描述: 计划回款金额
	 * 字段: plan_receipt_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planReceiptAmount;
	
	/**
	 * 描述: 计划回款本金
	 * 字段: plan_receipt_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planReceiptcaptail;
	
	/**
	 * 描述: 计划回款利息
	 * 字段: plan_receipt_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planReceiptInterest;
	
	/**
	 * 描述: 计划回款加息
	 * 字段: plan_receipt_increase  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planReceiptIncrease;
	
	/**
	 * 描述: 计划回款人数
	 * 字段: plan_receipt_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer planReceiptUserCou;
	
	/**
	 * 描述: 计划回款笔数
	 * 字段: plan_receipt_times  INT(10)
	 * 默认值: 0
	 */
	private Integer planReceiptTimes;
	
	/**
	 * 描述: 实际已收金额
	 * 字段: receipted_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedAmount;
	
	/**
	 * 描述: 已收本金
	 * 字段: receipted_capital  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedCapital;
	
	/**
	 * 描述: 已收利息
	 * 字段: receipted_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedInterest;
	
	/**
	 * 描述: 已收加息
	 * 字段: receipted_increase  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedIncrease;
	
	/**
	 * 描述: 已收罚息
	 * 字段: receipted_punish  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptedPunish;
	
	/**
	 * 描述: 已收人数
	 * 字段: receipted_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptedUserCou;
	
	/**
	 * 描述: 已回款笔数
	 * 字段: receipted_times  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptedTimes;
	
	/**
	 * 描述: 计划回款日已收金额
	 * 字段: curr_receipted_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currReceiptedAmount;
	
	/**
	 * 描述: 计划回款日已收本金
	 * 字段: curr_receipted_capital  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currReceiptedCapital;
	
	/**
	 * 描述: 计划回款日已收利息
	 * 字段: curr_receipted_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currReceiptedInterest;
	
	/**
	 * 描述: 计划回款日已收加息
	 * 字段: curr_receipted_increase  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currReceiptedIncrease;
	
	/**
	 * 描述: 计划回款日已收款人数
	 * 字段: curr_receipted_user_cou  INT(10)
	 */
	private Integer currReceiptedUserCou;
	
	/**
	 * 描述: 回款日回款笔数
	 * 字段: curr_receipted_times  INT(10)
	 * 默认值: 0
	 */
	private Integer currReceiptedTimes;
	
	/**
	 * 描述: 提前回款金额
	 * 字段: before_receipted_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeReceiptedAmount;
	
	/**
	 * 描述: 提前回款本金
	 * 字段: before_receipted_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeReceiptedcaptail;
	
	/**
	 * 描述: 提前回款利息
	 * 字段: before_receipted_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeReceiptedInterest;
	
	/**
	 * 描述: 提前回款加息
	 * 字段: before_receipted_increase  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeReceiptedIncrease;
	
	/**
	 * 描述: 提前回款人数
	 * 字段: before_receipted_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer beforeReceiptedUserCou;
	
	/**
	 * 描述: 回款日钱回款笔数
	 * 字段: before_receipted_times  INT(10)
	 * 默认值: 0
	 */
	private Integer beforeReceiptedTimes;
	
	/**
	 * 描述: 待收金额
	 * 字段: receipting_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingAmount;
	
	/**
	 * 描述: 待收本金
	 * 字段: receipting_capital  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingCapital;
	
	/**
	 * 描述: 待收利息
	 * 字段: receipting_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingInterest;
	
	/**
	 * 描述: 待收加息
	 * 字段: receipting_increase  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingIncrease;
	
	/**
	 * 描述: 待收人数
	 * 字段: receipting_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptingUserCou;
	
	/**
	 * 描述: 回款中笔数
	 * 字段: receipting_times  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptingTimes;
	
	/**
	 * 描述: 未来30日待收金额
	 * 字段: receipting_money_30  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingMoney30;
	
	/**
	 * 描述: 未来30天回款笔数
	 * 字段: receipting_times_30  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptingTimes30;
	
	/**
	 * 描述: 未来60日待收金额
	 * 字段: receipting_money_60  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal receiptingMoney60;
	
	/**
	 * 描述: 未来60天还款笔数
	 * 字段: receipting_times_60  INT(10)
	 * 默认值: 0
	 */
	private Integer receiptingTimes60;
	
 
	public StaReceipt(){
	}

	public StaReceipt(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
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
	public void setProType(Integer proType) {
		this.proType = proType;
	}
	
	public Integer getProType() {
		return this.proType;
	}
	
	public void setPlanReceiptAmount(java.math.BigDecimal planReceiptAmount) {
		this.planReceiptAmount = planReceiptAmount;
	}
	
	public java.math.BigDecimal getPlanReceiptAmount() {
		return this.planReceiptAmount;
	}
	
	public void setPlanReceiptcaptail(java.math.BigDecimal planReceiptcaptail) {
		this.planReceiptcaptail = planReceiptcaptail;
	}
	
	public java.math.BigDecimal getPlanReceiptcaptail() {
		return this.planReceiptcaptail;
	}
	
	public void setPlanReceiptInterest(java.math.BigDecimal planReceiptInterest) {
		this.planReceiptInterest = planReceiptInterest;
	}
	
	public java.math.BigDecimal getPlanReceiptInterest() {
		return this.planReceiptInterest;
	}
	
	public void setPlanReceiptIncrease(java.math.BigDecimal planReceiptIncrease) {
		this.planReceiptIncrease = planReceiptIncrease;
	}
	
	public java.math.BigDecimal getPlanReceiptIncrease() {
		return this.planReceiptIncrease;
	}
	
	public void setPlanReceiptUserCou(Integer planReceiptUserCou) {
		this.planReceiptUserCou = planReceiptUserCou;
	}
	
	public Integer getPlanReceiptUserCou() {
		return this.planReceiptUserCou;
	}
	
	public void setPlanReceiptTimes(Integer planReceiptTimes) {
		this.planReceiptTimes = planReceiptTimes;
	}
	
	public Integer getPlanReceiptTimes() {
		return this.planReceiptTimes;
	}
	
	public void setReceiptedAmount(java.math.BigDecimal receiptedAmount) {
		this.receiptedAmount = receiptedAmount;
	}
	
	public java.math.BigDecimal getReceiptedAmount() {
		return this.receiptedAmount;
	}
	
	public void setReceiptedCapital(java.math.BigDecimal receiptedCapital) {
		this.receiptedCapital = receiptedCapital;
	}
	
	public java.math.BigDecimal getReceiptedCapital() {
		return this.receiptedCapital;
	}
	
	public void setReceiptedInterest(java.math.BigDecimal receiptedInterest) {
		this.receiptedInterest = receiptedInterest;
	}
	
	public java.math.BigDecimal getReceiptedInterest() {
		return this.receiptedInterest;
	}
	
	public void setReceiptedIncrease(java.math.BigDecimal receiptedIncrease) {
		this.receiptedIncrease = receiptedIncrease;
	}
	
	public java.math.BigDecimal getReceiptedIncrease() {
		return this.receiptedIncrease;
	}
	
	public void setReceiptedPunish(java.math.BigDecimal receiptedPunish) {
		this.receiptedPunish = receiptedPunish;
	}
	
	public java.math.BigDecimal getReceiptedPunish() {
		return this.receiptedPunish;
	}
	
	public void setReceiptedUserCou(Integer receiptedUserCou) {
		this.receiptedUserCou = receiptedUserCou;
	}
	
	public Integer getReceiptedUserCou() {
		return this.receiptedUserCou;
	}
	
	public void setReceiptedTimes(Integer receiptedTimes) {
		this.receiptedTimes = receiptedTimes;
	}
	
	public Integer getReceiptedTimes() {
		return this.receiptedTimes;
	}
	
	public void setCurrReceiptedAmount(java.math.BigDecimal currReceiptedAmount) {
		this.currReceiptedAmount = currReceiptedAmount;
	}
	
	public java.math.BigDecimal getCurrReceiptedAmount() {
		return this.currReceiptedAmount;
	}
	
	public void setCurrReceiptedCapital(java.math.BigDecimal currReceiptedCapital) {
		this.currReceiptedCapital = currReceiptedCapital;
	}
	
	public java.math.BigDecimal getCurrReceiptedCapital() {
		return this.currReceiptedCapital;
	}
	
	public void setCurrReceiptedInterest(java.math.BigDecimal currReceiptedInterest) {
		this.currReceiptedInterest = currReceiptedInterest;
	}
	
	public java.math.BigDecimal getCurrReceiptedInterest() {
		return this.currReceiptedInterest;
	}
	
	public void setCurrReceiptedIncrease(java.math.BigDecimal currReceiptedIncrease) {
		this.currReceiptedIncrease = currReceiptedIncrease;
	}
	
	public java.math.BigDecimal getCurrReceiptedIncrease() {
		return this.currReceiptedIncrease;
	}
	
	public void setCurrReceiptedUserCou(Integer currReceiptedUserCou) {
		this.currReceiptedUserCou = currReceiptedUserCou;
	}
	
	public Integer getCurrReceiptedUserCou() {
		return this.currReceiptedUserCou;
	}
	
	public void setCurrReceiptedTimes(Integer currReceiptedTimes) {
		this.currReceiptedTimes = currReceiptedTimes;
	}
	
	public Integer getCurrReceiptedTimes() {
		return this.currReceiptedTimes;
	}
	
	public void setBeforeReceiptedAmount(java.math.BigDecimal beforeReceiptedAmount) {
		this.beforeReceiptedAmount = beforeReceiptedAmount;
	}
	
	public java.math.BigDecimal getBeforeReceiptedAmount() {
		return this.beforeReceiptedAmount;
	}
	
	public void setBeforeReceiptedcaptail(java.math.BigDecimal beforeReceiptedcaptail) {
		this.beforeReceiptedcaptail = beforeReceiptedcaptail;
	}
	
	public java.math.BigDecimal getBeforeReceiptedcaptail() {
		return this.beforeReceiptedcaptail;
	}
	
	public void setBeforeReceiptedInterest(java.math.BigDecimal beforeReceiptedInterest) {
		this.beforeReceiptedInterest = beforeReceiptedInterest;
	}
	
	public java.math.BigDecimal getBeforeReceiptedInterest() {
		return this.beforeReceiptedInterest;
	}
	
	public void setBeforeReceiptedIncrease(java.math.BigDecimal beforeReceiptedIncrease) {
		this.beforeReceiptedIncrease = beforeReceiptedIncrease;
	}
	
	public java.math.BigDecimal getBeforeReceiptedIncrease() {
		return this.beforeReceiptedIncrease;
	}
	
	public void setBeforeReceiptedUserCou(Integer beforeReceiptedUserCou) {
		this.beforeReceiptedUserCou = beforeReceiptedUserCou;
	}
	
	public Integer getBeforeReceiptedUserCou() {
		return this.beforeReceiptedUserCou;
	}
	
	public void setBeforeReceiptedTimes(Integer beforeReceiptedTimes) {
		this.beforeReceiptedTimes = beforeReceiptedTimes;
	}
	
	public Integer getBeforeReceiptedTimes() {
		return this.beforeReceiptedTimes;
	}
	
	public void setReceiptingAmount(java.math.BigDecimal receiptingAmount) {
		this.receiptingAmount = receiptingAmount;
	}
	
	public java.math.BigDecimal getReceiptingAmount() {
		return this.receiptingAmount;
	}
	
	public void setReceiptingCapital(java.math.BigDecimal receiptingCapital) {
		this.receiptingCapital = receiptingCapital;
	}
	
	public java.math.BigDecimal getReceiptingCapital() {
		return this.receiptingCapital;
	}
	
	public void setReceiptingInterest(java.math.BigDecimal receiptingInterest) {
		this.receiptingInterest = receiptingInterest;
	}
	
	public java.math.BigDecimal getReceiptingInterest() {
		return this.receiptingInterest;
	}
	
	public void setReceiptingIncrease(java.math.BigDecimal receiptingIncrease) {
		this.receiptingIncrease = receiptingIncrease;
	}
	
	public java.math.BigDecimal getReceiptingIncrease() {
		return this.receiptingIncrease;
	}
	
	public void setReceiptingUserCou(Integer receiptingUserCou) {
		this.receiptingUserCou = receiptingUserCou;
	}
	
	public Integer getReceiptingUserCou() {
		return this.receiptingUserCou;
	}
	
	public void setReceiptingTimes(Integer receiptingTimes) {
		this.receiptingTimes = receiptingTimes;
	}
	
	public Integer getReceiptingTimes() {
		return this.receiptingTimes;
	}
	
	public void setReceiptingMoney30(java.math.BigDecimal receiptingMoney30) {
		this.receiptingMoney30 = receiptingMoney30;
	}
	
	public java.math.BigDecimal getReceiptingMoney30() {
		return this.receiptingMoney30;
	}
	
	public void setReceiptingTimes30(Integer receiptingTimes30) {
		this.receiptingTimes30 = receiptingTimes30;
	}
	
	public Integer getReceiptingTimes30() {
		return this.receiptingTimes30;
	}
	
	public void setReceiptingMoney60(java.math.BigDecimal receiptingMoney60) {
		this.receiptingMoney60 = receiptingMoney60;
	}
	
	public java.math.BigDecimal getReceiptingMoney60() {
		return this.receiptingMoney60;
	}
	
	public void setReceiptingTimes60(Integer receiptingTimes60) {
		this.receiptingTimes60 = receiptingTimes60;
	}
	
	public Integer getReceiptingTimes60() {
		return this.receiptingTimes60;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

