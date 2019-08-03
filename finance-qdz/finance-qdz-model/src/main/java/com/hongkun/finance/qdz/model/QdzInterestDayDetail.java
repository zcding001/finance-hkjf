package com.hongkun.finance.qdz.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.qdz.model.QdzInterestDayDetail.java
 * @Class Name    : QdzInterestDayDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class QdzInterestDayDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 钱袋子利息表id
	 * 字段: qdz_interest_day_id  INT(10)
	 */
	private java.lang.Integer qdzInterestDayId;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 标的id
	 * 字段: bid_id  INT(10)
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 第三方用户id
	 * 字段: third_reg_user_id  INT(10)
	 */
	private java.lang.Integer thirdRegUserId;
	
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
	 * 描述: 利息
	 * 字段: interest  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interest;
	
	/**
	 * 描述: 利率差
	 * 字段: interest_fee  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interestFee;
	
	/**
	 * 描述: 活期债权金额
	 * 字段: money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal money;
	
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
 
	public QdzInterestDayDetail(){
	}

	public QdzInterestDayDetail(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setQdzInterestDayId(java.lang.Integer qdzInterestDayId) {
		this.qdzInterestDayId = qdzInterestDayId;
	}
	
	public java.lang.Integer getQdzInterestDayId() {
		return this.qdzInterestDayId;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}
	
	public java.lang.Integer getBidId() {
		return this.bidId;
	}
	
	public void setThirdRegUserId(java.lang.Integer thirdRegUserId) {
		this.thirdRegUserId = thirdRegUserId;
	}
	
	public java.lang.Integer getThirdRegUserId() {
		return this.thirdRegUserId;
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
	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}
	
	public java.math.BigDecimal getInterest() {
		return this.interest;
	}
	
	public void setInterestFee(java.math.BigDecimal interestFee) {
		this.interestFee = interestFee;
	}
	
	public java.math.BigDecimal getInterestFee() {
		return this.interestFee;
	}
	
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	public java.math.BigDecimal getMoney() {
		return this.money;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

