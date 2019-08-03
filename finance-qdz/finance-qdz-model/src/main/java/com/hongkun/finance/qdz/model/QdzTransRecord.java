package com.hongkun.finance.qdz.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.qdz.model.QdzTransRecord.java
 * @Class Name    : QdzTransRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class QdzTransRecord extends BaseModel {
	
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
	 * 描述: 交易类型1、转入2、转出
	 * 字段: type  TINYINT(3)
	 */
	private Integer type;
	
	/**
	 * 描述: 交易金额
	 * 字段: trans_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;
	
	/**
	 * 描述: 交易之前余额
	 * 字段: pre_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal preMoney;
	
	/**
	 * 描述: 交易之后余额
	 * 字段: after_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal afterMoney;
	
	/**
	 * 描述: 流水状态1.成功，2.失败
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 转入转出来源 0-PC 1-WAP 2-Android 3-IOS
	 * 字段: source  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer source;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
 
	public QdzTransRecord(){
	}

	public QdzTransRecord(java.lang.Integer id){
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
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setTransMoney(java.math.BigDecimal transMoney) {
		this.transMoney = transMoney;
	}
	
	public java.math.BigDecimal getTransMoney() {
		return this.transMoney;
	}
	
	public void setPreMoney(java.math.BigDecimal preMoney) {
		this.preMoney = preMoney;
	}
	
	public java.math.BigDecimal getPreMoney() {
		return this.preMoney;
	}
	
	public void setAfterMoney(java.math.BigDecimal afterMoney) {
		this.afterMoney = afterMoney;
	}
	
	public java.math.BigDecimal getAfterMoney() {
		return this.afterMoney;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setSource(java.lang.Integer source) {
		this.source = source;
	}
	
	public java.lang.Integer getSource() {
		return this.source;
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

