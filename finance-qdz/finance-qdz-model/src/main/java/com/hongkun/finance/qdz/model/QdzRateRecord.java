package com.hongkun.finance.qdz.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.qdz.model.QdzRateRecord.java
 * @Class Name    : QdzRateRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class QdzRateRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 钱袋子规则id
	 * 字段: vas_rebates_rule_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer vasRebatesRuleId;
	
	/**
	 * 描述: 钱袋子规则利率
	 * 字段: rate  DECIMAL(10)
	 */
	private java.math.BigDecimal rate;
	
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
 
	public QdzRateRecord(){
	}

	public QdzRateRecord(java.lang.Integer id){
		this.id = id;
	} 

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setVasRebatesRuleId(java.lang.Integer vasRebatesRuleId) {
		this.vasRebatesRuleId = vasRebatesRuleId;
	}
	
	public java.lang.Integer getVasRebatesRuleId() {
		return this.vasRebatesRuleId;
	}
	
	public void setRate(java.math.BigDecimal rate) {
		this.rate = rate;
	}
	
	public java.math.BigDecimal getRate() {
		return this.rate;
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

