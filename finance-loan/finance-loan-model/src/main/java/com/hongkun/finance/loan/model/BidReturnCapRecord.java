package com.hongkun.finance.loan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidReturnCapRecord.java
 * @Class Name    : BidReturnCapRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidReturnCapRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: bidId
	 * 字段: bid_id  INT(10)
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 还款本金
	 * 字段: return_cap_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal returnCapAmount;
	
	/**
	 * 描述: 原来本金
	 * 字段: origin_cap_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal originCapAmount;
	
	/**
	 * 描述: 状态：0-未匹配，1-已匹配
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
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
 
	public BidReturnCapRecord(){
	}

	public BidReturnCapRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}
	
	public java.lang.Integer getBidId() {
		return this.bidId;
	}
	
	public void setReturnCapAmount(java.math.BigDecimal returnCapAmount) {
		this.returnCapAmount = returnCapAmount;
	}
	
	public java.math.BigDecimal getReturnCapAmount() {
		return this.returnCapAmount;
	}
	
	public void setOriginCapAmount(java.math.BigDecimal originCapAmount) {
		this.originCapAmount = originCapAmount;
	}
	
	public java.math.BigDecimal getOriginCapAmount() {
		return this.originCapAmount;
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

