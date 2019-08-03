package com.hongkun.finance.qdz.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.qdz.model.QdzInterestDay.java
 * @Class Name    : QdzInterestDay.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class QdzInterestDay extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 当日利息
	 * 字段: day_interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal dayInterest;
	
	/**
	 * 描述: 利息日期
	 * 字段: day  DATETIME(19)
	 */
	private java.util.Date day;
	
	//【非数据库字段，查询时使用】
	private java.util.Date dayBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date dayEnd;
	/**
	 * 描述: 利率
	 * 字段: rate  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal rate;
	
	/**
	 * 描述: 利息对应本金金额
	 * 字段: money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 描述: 划转状态:1成功，2失败
	 * 字段: state  TINYINT(3)
	 */
	private Integer state;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public QdzInterestDay(){
	}

	public QdzInterestDay(java.lang.Integer id){
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
	
	public void setDayInterest(java.math.BigDecimal dayInterest) {
		this.dayInterest = dayInterest;
	}
	
	public java.math.BigDecimal getDayInterest() {
		return this.dayInterest;
	}
	
	public void setDay(java.util.Date day) {
		this.day = day;
	}
	
	public java.util.Date getDay() {
		return this.day;
	}
	
	public void setDayBegin(java.util.Date dayBegin) {
		this.dayBegin = dayBegin;
	}
	
	public java.util.Date getDayBegin() {
		return this.dayBegin;
	}
	
	public void setDayEnd(java.util.Date dayEnd) {
		this.dayEnd = dayEnd;
	}
	
	public java.util.Date getDayEnd() {
		return this.dayEnd;
	}	
	public void setRate(java.math.BigDecimal rate) {
		this.rate = rate;
	}
	
	public java.math.BigDecimal getRate() {
		return this.rate;
	}
	
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	public java.math.BigDecimal getMoney() {
		return this.money;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

