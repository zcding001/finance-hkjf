package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaQdz.java
 * @Class Name    : StaQdz.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaQdz extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
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
	private java.util.Date daBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daEnd;
	/**
	 * 描述: 活期总金额
	 * 字段: amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal amount;
	
	/**
	 * 描述: 待匹配金额
	 * 字段: matching_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal matchingAmount;
	
	/**
	 * 描述: 转入总金额
	 * 字段: in_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal inAmount;
	
	/**
	 * 描述: 转出总金额
	 * 字段: out_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal outAmount;
	
	/**
	 * 描述: 总利息
	 * 字段: interest_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interestAmount;
	
	/**
	 * 描述: 加息利息总金额
	 * 字段: increase_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal increaseAmount;
	
	/**
	 * 描述: 转入用户数量
	 * 字段: in_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer inUserCou;
	
	/**
	 * 描述: 转出用户数量
	 * 字段: out_user_cou  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer outUserCou;
	
	/**
	 * 描述: 转入次数
	 * 字段: int_times  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer intTimes;
	
	/**
	 * 描述: 转出次数
	 * 字段: out_times  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer outTimes;
	
 
	public StaQdz(){
	}

	public StaQdz(java.lang.Integer id){
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
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
	
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}
	
	public void setMatchingAmount(java.math.BigDecimal matchingAmount) {
		this.matchingAmount = matchingAmount;
	}
	
	public java.math.BigDecimal getMatchingAmount() {
		return this.matchingAmount;
	}
	
	public void setInAmount(java.math.BigDecimal inAmount) {
		this.inAmount = inAmount;
	}
	
	public java.math.BigDecimal getInAmount() {
		return this.inAmount;
	}
	
	public void setOutAmount(java.math.BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	
	public java.math.BigDecimal getOutAmount() {
		return this.outAmount;
	}
	
	public void setInterestAmount(java.math.BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	
	public java.math.BigDecimal getInterestAmount() {
		return this.interestAmount;
	}
	
	public void setIncreaseAmount(java.math.BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}
	
	public java.math.BigDecimal getIncreaseAmount() {
		return this.increaseAmount;
	}
	
	public void setInUserCou(java.lang.Integer inUserCou) {
		this.inUserCou = inUserCou;
	}
	
	public java.lang.Integer getInUserCou() {
		return this.inUserCou;
	}
	
	public void setOutUserCou(java.lang.Integer outUserCou) {
		this.outUserCou = outUserCou;
	}
	
	public java.lang.Integer getOutUserCou() {
		return this.outUserCou;
	}
	
	public void setIntTimes(java.lang.Integer intTimes) {
		this.intTimes = intTimes;
	}
	
	public java.lang.Integer getIntTimes() {
		return this.intTimes;
	}
	
	public void setOutTimes(java.lang.Integer outTimes) {
		this.outTimes = outTimes;
	}
	
	public java.lang.Integer getOutTimes() {
		return this.outTimes;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

