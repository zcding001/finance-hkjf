package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinTradeFlow.java
 * @Class Name : FinTradeFlow.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinTradeFlow extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 交易流水:交易类型+交易来源+YYYYMMDDH24mmssSSS+五位流水 字段: flow_id VARCHAR(40) 默认值:
	 * ''
	 */
	private java.lang.String flowId;

	/**
	 * 描述: 业务交易流水 字段: p_flow_id VARCHAR(40) 默认值: ''
	 */
	private java.lang.String pflowId;

	/**
	 * 描述: 交易发起方id 字段: reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 交易金额 字段: trans_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;

	/**
	 * 描述: 交易来源 10-PC 11-IOS 12-ANDRIOD 13-WAP 字段: trade_source TINYINT(3) 默认值:
	 * 10
	 */
	private Integer tradeSource;

	/**
	 * 描述: 交易类型:(业务模块+业务操作)
	 * 充值-1001,投资-1101,放款-1201,还款-1301,提现-1401,红包-1501,推荐奖-1601,钱袋子转入-1701,钱袋子转出
	 * -1702,积分支付-1801,手动债权转让-1901,自动债权转让-1902,物业缴费-2001 字段: trade_type
	 * SMALLINT(5) 默认值: 0
	 */
	private Integer tradeType;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	// 资金划转SUBCODE
	private Integer transferSubCode;

	public FinTradeFlow() {
	}

	public FinTradeFlow(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setFlowId(java.lang.String flowId) {
		this.flowId = flowId;
	}

	public java.lang.String getFlowId() {
		return this.flowId;
	}

	public void setPflowId(java.lang.String pflowId) {
		this.pflowId = pflowId;
	}

	public java.lang.String getPflowId() {
		return this.pflowId;
	}

	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}

	public void setTransMoney(java.math.BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public java.math.BigDecimal getTransMoney() {
		return this.transMoney;
	}

	public void setTradeSource(Integer tradeSource) {
		this.tradeSource = tradeSource;
	}

	public Integer getTradeSource() {
		return this.tradeSource;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getTradeType() {
		return this.tradeType;
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

	public Integer getTransferSubCode() {
		return transferSubCode;
	}

	public void setTransferSubCode(Integer transferSubCode) {
		this.transferSubCode = transferSubCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
