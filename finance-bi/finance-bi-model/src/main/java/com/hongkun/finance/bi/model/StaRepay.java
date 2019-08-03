package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaRepay.java
 * @Class Name    : StaRepay.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaRepay extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 自增ID
	 * 字段: id  INT UNSIGNED(10)
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
	 * 字段: pro_type  INT(10)
	 * 默认值: 1
	 */
	private Integer proType;
	
	/**
	 * 描述: 计划还款金额
	 * 字段: plan_repay_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planRepayAmount;
	
	/**
	 * 描述: 计划还款本金
	 * 字段: plan_repay_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planRepaycaptail;
	
	/**
	 * 描述: 计划还款利息
	 * 字段: plan_repay_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planRepayInterest;
	
	/**
	 * 描述: 计划还款服务费
	 * 字段: plan_repay_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal planRepayServiceCharge;
	
	/**
	 * 描述: 计划还款人数
	 * 字段: plan_repay_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer planRepayUserCou;
	
	/**
	 * 描述: 计划还款笔数
	 * 字段: plan_repay_times  INT(10)
	 * 默认值: 0
	 */
	private Integer planRepayTimes;
	
	/**
	 * 描述: 已还金额
	 * 字段: repayed_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedAmount;
	
	/**
	 * 描述: 已还本金
	 * 字段: repayed_capital  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedCapital;
	
	/**
	 * 描述: 已还利息
	 * 字段: repayed_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedInterest;
	
	/**
	 * 描述: 已还服务费
	 * 字段: repayed_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedServiceCharge;
	
	/**
	 * 描述: 已还罚息
	 * 字段: repayed_punish  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayedPunish;
	
	/**
	 * 描述: 已还人数
	 * 字段: repayed_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer repayedUserCou;
	
	/**
	 * 描述: 已还笔数
	 * 字段: repayed_times  INT(10)
	 * 默认值: 0
	 */
	private Integer repayedTimes;
	
	/**
	 * 描述: 计划还款日已还金额
	 * 字段: curr_repayed_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currRepayedAmount;
	
	/**
	 * 描述: 计划还款日已还本金
	 * 字段: curr_repayed_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currRepayedcaptail;
	
	/**
	 * 描述: 计划还款日已还利息
	 * 字段: curr_repayed_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currRepayedInterest;
	
	/**
	 * 描述: 计划还款日已还服务费
	 * 字段: curr_repayed_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal currRepayedServiceCharge;
	
	/**
	 * 描述: 计划还款日已还人数
	 * 字段: curr_repayed_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer currRepayedUserCou;
	
	/**
	 * 描述: 还款日还款笔数
	 * 字段: curr_repayed_times  INT(10)
	 * 默认值: 0
	 */
	private Integer currRepayedTimes;
	
	/**
	 * 描述: 提前还款金额
	 * 字段: before_repayed_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeRepayedAmount;
	
	/**
	 * 描述: 提前还本金额
	 * 字段: before_repayed_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeRepayedcaptail;
	
	/**
	 * 描述: 提前还利息金额
	 * 字段: before_repayed_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeRepayedInterest;
	
	/**
	 * 描述: 提前还服务费金额
	 * 字段: before_repayed_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal beforeRepayedServiceCharge;
	
	/**
	 * 描述: 提前还款人数
	 * 字段: before_repayed_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer beforeRepayedUserCou;
	
	/**
	 * 描述: 还款日钱还款笔数
	 * 字段: before_repayed_times  INT(10)
	 * 默认值: 0
	 */
	private Integer beforeRepayedTimes;
	
	/**
	 * 描述: 提前还本已还金额
	 * 字段: advance_repayed_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal advanceRepayedAmount;
	
	/**
	 * 描述: 提前还本已还本金
	 * 字段: advance_repayed_captail  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal advanceRepayedcaptail;
	
	/**
	 * 描述: 提前还本已还利息
	 * 字段: advance_repayed_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal advanceRepayedInterest;
	
	/**
	 * 描述: 提前还本已还服务费
	 * 字段: advance_repayed_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal advanceRepayedServiceCharge;
	
	/**
	 * 描述: 提前还本人数
	 * 字段: advance_repayed_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer advanceRepayedUserCou;
	
	/**
	 * 描述: 提前还款笔数
	 * 字段: advance_repayed_times  INT(10)
	 * 默认值: 0
	 */
	private Integer advanceRepayedTimes;
	
	/**
	 * 描述: 待还金额
	 * 字段: repaying_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingAmount;
	
	/**
	 * 描述: 待还本金
	 * 字段: repaying_capital  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingCapital;
	
	/**
	 * 描述: 待还利息
	 * 字段: repaying_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingInterest;
	
	/**
	 * 描述: 待还款服务费金额
	 * 字段: repaying_service_charge  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingServiceCharge;
	
	/**
	 * 描述: 待还人数
	 * 字段: repaying_user_cou  INT(10)
	 * 默认值: 0
	 */
	private Integer repayingUserCou;
	
	/**
	 * 描述: 还款中笔数
	 * 字段: repaying_times  INT(10)
	 * 默认值: 0
	 */
	private Integer repayingTimes;
	
	/**
	 * 描述: 未来30日待还金额
	 * 字段: repaying_money_30  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingMoney30;
	
	/**
	 * 描述: 未来30天待还笔数
	 * 字段: repaying_times_30  INT(10)
	 * 默认值: 0
	 */
	private Integer repayingTimes30;
	
	/**
	 * 描述: 未来60日待还金额
	 * 字段: repaying_money_60  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayingMoney60;
	
	/**
	 * 描述: 围栏60天待还笔数
	 * 字段: repaying_times_60  INT(10)
	 * 默认值: 0
	 */
	private Integer repayingTimes60;
	
 
	public StaRepay(){
	}

	public StaRepay(Integer id){
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
	
	public void setPlanRepayAmount(java.math.BigDecimal planRepayAmount) {
		this.planRepayAmount = planRepayAmount;
	}
	
	public java.math.BigDecimal getPlanRepayAmount() {
		return this.planRepayAmount;
	}
	
	public void setPlanRepaycaptail(java.math.BigDecimal planRepaycaptail) {
		this.planRepaycaptail = planRepaycaptail;
	}
	
	public java.math.BigDecimal getPlanRepaycaptail() {
		return this.planRepaycaptail;
	}
	
	public void setPlanRepayInterest(java.math.BigDecimal planRepayInterest) {
		this.planRepayInterest = planRepayInterest;
	}
	
	public java.math.BigDecimal getPlanRepayInterest() {
		return this.planRepayInterest;
	}
	
	public void setPlanRepayServiceCharge(java.math.BigDecimal planRepayServiceCharge) {
		this.planRepayServiceCharge = planRepayServiceCharge;
	}
	
	public java.math.BigDecimal getPlanRepayServiceCharge() {
		return this.planRepayServiceCharge;
	}
	
	public void setPlanRepayUserCou(Integer planRepayUserCou) {
		this.planRepayUserCou = planRepayUserCou;
	}
	
	public Integer getPlanRepayUserCou() {
		return this.planRepayUserCou;
	}
	
	public void setPlanRepayTimes(Integer planRepayTimes) {
		this.planRepayTimes = planRepayTimes;
	}
	
	public Integer getPlanRepayTimes() {
		return this.planRepayTimes;
	}
	
	public void setRepayedAmount(java.math.BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	
	public java.math.BigDecimal getRepayedAmount() {
		return this.repayedAmount;
	}
	
	public void setRepayedCapital(java.math.BigDecimal repayedCapital) {
		this.repayedCapital = repayedCapital;
	}
	
	public java.math.BigDecimal getRepayedCapital() {
		return this.repayedCapital;
	}
	
	public void setRepayedInterest(java.math.BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	
	public java.math.BigDecimal getRepayedInterest() {
		return this.repayedInterest;
	}
	
	public void setRepayedServiceCharge(java.math.BigDecimal repayedServiceCharge) {
		this.repayedServiceCharge = repayedServiceCharge;
	}
	
	public java.math.BigDecimal getRepayedServiceCharge() {
		return this.repayedServiceCharge;
	}
	
	public void setRepayedPunish(java.math.BigDecimal repayedPunish) {
		this.repayedPunish = repayedPunish;
	}
	
	public java.math.BigDecimal getRepayedPunish() {
		return this.repayedPunish;
	}
	
	public void setRepayedUserCou(Integer repayedUserCou) {
		this.repayedUserCou = repayedUserCou;
	}
	
	public Integer getRepayedUserCou() {
		return this.repayedUserCou;
	}
	
	public void setRepayedTimes(Integer repayedTimes) {
		this.repayedTimes = repayedTimes;
	}
	
	public Integer getRepayedTimes() {
		return this.repayedTimes;
	}
	
	public void setCurrRepayedAmount(java.math.BigDecimal currRepayedAmount) {
		this.currRepayedAmount = currRepayedAmount;
	}
	
	public java.math.BigDecimal getCurrRepayedAmount() {
		return this.currRepayedAmount;
	}
	
	public void setCurrRepayedcaptail(java.math.BigDecimal currRepayedcaptail) {
		this.currRepayedcaptail = currRepayedcaptail;
	}
	
	public java.math.BigDecimal getCurrRepayedcaptail() {
		return this.currRepayedcaptail;
	}
	
	public void setCurrRepayedInterest(java.math.BigDecimal currRepayedInterest) {
		this.currRepayedInterest = currRepayedInterest;
	}
	
	public java.math.BigDecimal getCurrRepayedInterest() {
		return this.currRepayedInterest;
	}
	
	public void setCurrRepayedServiceCharge(java.math.BigDecimal currRepayedServiceCharge) {
		this.currRepayedServiceCharge = currRepayedServiceCharge;
	}
	
	public java.math.BigDecimal getCurrRepayedServiceCharge() {
		return this.currRepayedServiceCharge;
	}
	
	public void setCurrRepayedUserCou(Integer currRepayedUserCou) {
		this.currRepayedUserCou = currRepayedUserCou;
	}
	
	public Integer getCurrRepayedUserCou() {
		return this.currRepayedUserCou;
	}
	
	public void setCurrRepayedTimes(Integer currRepayedTimes) {
		this.currRepayedTimes = currRepayedTimes;
	}
	
	public Integer getCurrRepayedTimes() {
		return this.currRepayedTimes;
	}
	
	public void setBeforeRepayedAmount(java.math.BigDecimal beforeRepayedAmount) {
		this.beforeRepayedAmount = beforeRepayedAmount;
	}
	
	public java.math.BigDecimal getBeforeRepayedAmount() {
		return this.beforeRepayedAmount;
	}
	
	public void setBeforeRepayedcaptail(java.math.BigDecimal beforeRepayedcaptail) {
		this.beforeRepayedcaptail = beforeRepayedcaptail;
	}
	
	public java.math.BigDecimal getBeforeRepayedcaptail() {
		return this.beforeRepayedcaptail;
	}
	
	public void setBeforeRepayedInterest(java.math.BigDecimal beforeRepayedInterest) {
		this.beforeRepayedInterest = beforeRepayedInterest;
	}
	
	public java.math.BigDecimal getBeforeRepayedInterest() {
		return this.beforeRepayedInterest;
	}
	
	public void setBeforeRepayedServiceCharge(java.math.BigDecimal beforeRepayedServiceCharge) {
		this.beforeRepayedServiceCharge = beforeRepayedServiceCharge;
	}
	
	public java.math.BigDecimal getBeforeRepayedServiceCharge() {
		return this.beforeRepayedServiceCharge;
	}
	
	public void setBeforeRepayedUserCou(Integer beforeRepayedUserCou) {
		this.beforeRepayedUserCou = beforeRepayedUserCou;
	}
	
	public Integer getBeforeRepayedUserCou() {
		return this.beforeRepayedUserCou;
	}
	
	public void setBeforeRepayedTimes(Integer beforeRepayedTimes) {
		this.beforeRepayedTimes = beforeRepayedTimes;
	}
	
	public Integer getBeforeRepayedTimes() {
		return this.beforeRepayedTimes;
	}
	
	public void setAdvanceRepayedAmount(java.math.BigDecimal advanceRepayedAmount) {
		this.advanceRepayedAmount = advanceRepayedAmount;
	}
	
	public java.math.BigDecimal getAdvanceRepayedAmount() {
		return this.advanceRepayedAmount;
	}
	
	public void setAdvanceRepayedcaptail(java.math.BigDecimal advanceRepayedcaptail) {
		this.advanceRepayedcaptail = advanceRepayedcaptail;
	}
	
	public java.math.BigDecimal getAdvanceRepayedcaptail() {
		return this.advanceRepayedcaptail;
	}
	
	public void setAdvanceRepayedInterest(java.math.BigDecimal advanceRepayedInterest) {
		this.advanceRepayedInterest = advanceRepayedInterest;
	}
	
	public java.math.BigDecimal getAdvanceRepayedInterest() {
		return this.advanceRepayedInterest;
	}
	
	public void setAdvanceRepayedServiceCharge(java.math.BigDecimal advanceRepayedServiceCharge) {
		this.advanceRepayedServiceCharge = advanceRepayedServiceCharge;
	}
	
	public java.math.BigDecimal getAdvanceRepayedServiceCharge() {
		return this.advanceRepayedServiceCharge;
	}
	
	public void setAdvanceRepayedUserCou(Integer advanceRepayedUserCou) {
		this.advanceRepayedUserCou = advanceRepayedUserCou;
	}
	
	public Integer getAdvanceRepayedUserCou() {
		return this.advanceRepayedUserCou;
	}
	
	public void setAdvanceRepayedTimes(Integer advanceRepayedTimes) {
		this.advanceRepayedTimes = advanceRepayedTimes;
	}
	
	public Integer getAdvanceRepayedTimes() {
		return this.advanceRepayedTimes;
	}
	
	public void setRepayingAmount(java.math.BigDecimal repayingAmount) {
		this.repayingAmount = repayingAmount;
	}
	
	public java.math.BigDecimal getRepayingAmount() {
		return this.repayingAmount;
	}
	
	public void setRepayingCapital(java.math.BigDecimal repayingCapital) {
		this.repayingCapital = repayingCapital;
	}
	
	public java.math.BigDecimal getRepayingCapital() {
		return this.repayingCapital;
	}
	
	public void setRepayingInterest(java.math.BigDecimal repayingInterest) {
		this.repayingInterest = repayingInterest;
	}
	
	public java.math.BigDecimal getRepayingInterest() {
		return this.repayingInterest;
	}
	
	public void setRepayingServiceCharge(java.math.BigDecimal repayingServiceCharge) {
		this.repayingServiceCharge = repayingServiceCharge;
	}
	
	public java.math.BigDecimal getRepayingServiceCharge() {
		return this.repayingServiceCharge;
	}
	
	public void setRepayingUserCou(Integer repayingUserCou) {
		this.repayingUserCou = repayingUserCou;
	}
	
	public Integer getRepayingUserCou() {
		return this.repayingUserCou;
	}
	
	public void setRepayingTimes(Integer repayingTimes) {
		this.repayingTimes = repayingTimes;
	}
	
	public Integer getRepayingTimes() {
		return this.repayingTimes;
	}
	
	public void setRepayingMoney30(java.math.BigDecimal repayingMoney30) {
		this.repayingMoney30 = repayingMoney30;
	}
	
	public java.math.BigDecimal getRepayingMoney30() {
		return this.repayingMoney30;
	}
	
	public void setRepayingTimes30(Integer repayingTimes30) {
		this.repayingTimes30 = repayingTimes30;
	}
	
	public Integer getRepayingTimes30() {
		return this.repayingTimes30;
	}
	
	public void setRepayingMoney60(java.math.BigDecimal repayingMoney60) {
		this.repayingMoney60 = repayingMoney60;
	}
	
	public java.math.BigDecimal getRepayingMoney60() {
		return this.repayingMoney60;
	}
	
	public void setRepayingTimes60(Integer repayingTimes60) {
		this.repayingTimes60 = repayingTimes60;
	}
	
	public Integer getRepayingTimes60() {
		return this.repayingTimes60;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

