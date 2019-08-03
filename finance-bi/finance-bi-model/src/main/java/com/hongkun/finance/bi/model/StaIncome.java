package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaIncome.java
 * @Class Name    : StaIncome.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaIncome extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;

	/**
	 * 描述: 流水id(对应资金划转id)
	 * 字段: flow_id  VARCHAR(255)
	 */
	private String flowId;

	/**
	 * 描述: 借款人姓名
	 * 字段: borrower_name  VARCHAR(255)
	 */
	private String borrowerName;

	/**
	 * 描述: 借款人id
	 * 字段: borrower_id  INT(10)
	 */
	private Integer borrowerId;

	/**
	 * 描述: 借款人手机号
	 * 字段: borrower_tel  VARCHAR(255)
	 */
	private String borrowerTel;

	/**
	 * 描述: 交易类型1-借款成功手续费 ;2-技术服务费;3-罚息
	 * 字段: trade_type  TINYINT(3)
	 */
	private Integer tradeType;

	/**
	 * 描述: 交易金额
	 * 字段: trans_money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;

	/**
	 * 描述: 扣息后交易金额
	 * 字段: pure_money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal pureMoney;

	/**
	 * 描述: 状态
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 */
	private java.util.Date createTime;

	//【非数据库字段，查询时使用】
	private java.lang.String createTimeBegin;

	//【非数据库字段，查询时使用】
	private java.lang.String createTimeEnd;
	/**
	 * 描述: 交易时间(资金划转时间)
	 * 字段: trans_time  DATETIME(19)
	 */
	private java.util.Date transTime;

	//【非数据库字段，查询时使用】
	private java.util.Date transTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date transTimeEnd;

	private String transTimeStr;
	public StaIncome(){
	}

	public String getTransTimeStr() {
		return transTimeStr;
	}

	public void setTransTimeStr(String transTimeStr) {
		this.transTimeStr = transTimeStr;
	}

	public StaIncome(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowId() {
		return this.flowId;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getBorrowerName() {
		return this.borrowerName;
	}

	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}

	public Integer getBorrowerId() {
		return this.borrowerId;
	}

	public void setBorrowerTel(String borrowerTel) {
		this.borrowerTel = borrowerTel;
	}

	public String getBorrowerTel() {
		return this.borrowerTel;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getTradeType() {
		return this.tradeType;
	}

	public void setTransMoney(java.math.BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public java.math.BigDecimal getTransMoney() {
		return this.transMoney;
	}

	public void setPureMoney(java.math.BigDecimal pureMoney) {
		this.pureMoney = pureMoney;
	}

	public java.math.BigDecimal getPureMoney() {
		return this.pureMoney;
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

	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}

	public java.util.Date getTransTime() {
		return this.transTime;
	}

	public void setTransTimeBegin(java.util.Date transTimeBegin) {
		this.transTimeBegin = transTimeBegin;
	}

	public java.util.Date getTransTimeBegin() {
		return this.transTimeBegin;
	}

	public void setTransTimeEnd(java.util.Date transTimeEnd) {
		this.transTimeEnd = transTimeEnd;
	}

	public java.util.Date getTransTimeEnd() {
		return this.transTimeEnd;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

