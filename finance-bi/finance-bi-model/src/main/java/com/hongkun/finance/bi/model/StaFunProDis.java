package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaFunProDis.java
 * @Class Name    : StaFunProDis.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaFunProDis extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 日期
	 * 字段: da  DATE(10)
	 * 默认值: 0000-00-00
	 */
	private java.util.Date da;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date daEnd;
	/**
	 * 描述: 产品类型:1-新手标,2-月月盈,3-季季盈,4-年年盈,5-体验金产品,6-购房宝,7-活期产品,8-活动产品,9-物业宝,10-散标直投(普通产品), 11-散标匹配(普通产品)
	 * 字段: bid_pro_type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer bidProType;
	
	/**
	 * 描述: 投资金额
	 * 字段: invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 还款金额
	 * 字段: repay_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal repayAmount;
	
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
 
	public StaFunProDis(){
	}

	public StaFunProDis(Integer id){
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
	public void setBidProType(Integer bidProType) {
		this.bidProType = bidProType;
	}
	
	public Integer getBidProType() {
		return this.bidProType;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setRepayAmount(java.math.BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}
	
	public java.math.BigDecimal getRepayAmount() {
		return this.repayAmount;
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

