package com.hongkun.finance.payment.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinPaymentRecord.java
 * @Class Name : FinPaymentRecord.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinPaymentRecord extends BaseModel {

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
	 * 描述: 交易发起方id 字段: reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 交易金额 字段: trans_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;

	/**
	 * 描述: 交易来源 10-PC 11-IOS 12-ANDRIOD 13-WAP 字段: trade_source TINYINT(3) 默认值:
	 * 1
	 */
	private Integer tradeSource;

	/**
	 * 描述: 交易类型:10-充值 14-提现 字段: trade_type SMALLINT(5) 默认值: 0
	 */
	private Integer tradeType;

	/**
	 * 描述: 交易渠道:1-汇付宝,2-连连 字段: pay_channel TINYINT(3) 默认值: 0
	 */
	private Integer payChannel;

	/**
	 * 描述: 充值方式:20-快捷、21-认证、22-网银 字段: recharge_source TINYINT(3) 默认值: 0
	 */
	private Integer rechargeSource;

	/**
	 * 描述: 银行卡ID 字段: bank_card_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer bankCardId;

	/**
	 * 描述: 状态:0-删除,1-待审核状态,2-待放款,3-运营审核拒绝,4-财务审核拒绝,5-划转中,6-已划转,7-划转失败
	 * ,8-已冲正,9失败,10等待定时提现 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 驳回信息 字段: reject_info VARCHAR(50) 默认值: ''
	 */
	private java.lang.String rejectInfo;

	/**
	 * 描述: 对账状态:1-未对账,2-对账成功,3-对账失败 字段: reconciliation_state TINYINT(3) 默认值: 0
	 */
	private Integer reconciliationState;

	/**
	 * 描述: 对账结果说明 字段: reconciliation_desc VARCHAR(100) 默认值: ''
	 */
	private java.lang.String reconciliationDesc;

	/**
	 * 描述: 对账时间 字段: reconciliation_time DATETIME(19) 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date reconciliationTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date reconciliationTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date reconciliationTimeEnd;
	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	// 提现券ID
	private java.lang.Integer couponDetailId;
	// 手续费
	private BigDecimal commission;
	// 状态集合
	private List<Integer> states;

	// 查询时用 最大金额
	private java.math.BigDecimal maxTransMoney;
	// 查询时用 最小金额
	private java.math.BigDecimal minTransMoney;

	/**
	 * 状态类型 1:划转中, 2:成功, 3:失败
	 */
	private Integer stateType;
	/**
	 * 用户ID集合
	 */
	private List<Integer> regUserIdList;

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public FinPaymentRecord() {
	}

	public FinPaymentRecord(java.lang.Integer id) {
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

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public Integer getPayChannel() {
		return this.payChannel;
	}

	public void setRechargeSource(Integer rechargeSource) {
		this.rechargeSource = rechargeSource;
	}

	public Integer getRechargeSource() {
		return this.rechargeSource;
	}

	public void setBankCardId(java.lang.Integer bankCardId) {
		this.bankCardId = bankCardId;
	}

	public java.lang.Integer getBankCardId() {
		return this.bankCardId;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return this.state;
	}

	public void setRejectInfo(java.lang.String rejectInfo) {
		this.rejectInfo = rejectInfo;
	}

	public java.lang.String getRejectInfo() {
		return this.rejectInfo;
	}

	public void setReconciliationState(Integer reconciliationState) {
		this.reconciliationState = reconciliationState;
	}

	public Integer getReconciliationState() {
		return this.reconciliationState;
	}

	public void setReconciliationDesc(java.lang.String reconciliationDesc) {
		this.reconciliationDesc = reconciliationDesc;
	}

	public java.lang.String getReconciliationDesc() {
		return this.reconciliationDesc;
	}

	public void setReconciliationTime(java.util.Date reconciliationTime) {
		this.reconciliationTime = reconciliationTime;
	}

	public java.util.Date getReconciliationTime() {
		return this.reconciliationTime;
	}

	public void setReconciliationTimeBegin(java.util.Date reconciliationTimeBegin) {
		this.reconciliationTimeBegin = reconciliationTimeBegin;
	}

	public java.util.Date getReconciliationTimeBegin() {
		return this.reconciliationTimeBegin;
	}

	public void setReconciliationTimeEnd(java.util.Date reconciliationTimeEnd) {
		this.reconciliationTimeEnd = reconciliationTimeEnd;
	}

	public java.util.Date getReconciliationTimeEnd() {
		return this.reconciliationTimeEnd;
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

	public Integer getCouponDetailId() {
		return couponDetailId;
	}

	public void setCouponDetailId(Integer couponDetailId) {
		this.couponDetailId = couponDetailId;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public List<Integer> getStates() {
		return states;
	}

	public void setStates(List<Integer> states) {
		this.states = states;
	}

	public BigDecimal getMaxTransMoney() {
		return maxTransMoney;
	}

	public void setMaxTransMoney(BigDecimal maxTransMoney) {
		this.maxTransMoney = maxTransMoney;
	}

	public BigDecimal getMinTransMoney() {
		return minTransMoney;
	}

	public void setMinTransMoney(BigDecimal minTransMoney) {
		this.minTransMoney = minTransMoney;
	}

	public void setStateType(Integer stateType) {
		this.stateType = stateType;
	}

	public Integer getStateType() {
		return stateType;
	}
}
