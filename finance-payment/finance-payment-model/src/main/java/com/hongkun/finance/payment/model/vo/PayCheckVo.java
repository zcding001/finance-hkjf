package com.hongkun.finance.payment.model.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description : 支付对账 ,查询条件VO
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.vo.PayCheckVo.java
 * @Author : yanbinghuang
 */
public class PayCheckVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// finPayRecord Id
	private Integer id;
	// 用户姓名
	private String realName;
	// 手机号
	private Long login;
	// 交易流水号
	private String flowId;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
	// 交易类型 10 充值， 14提现
	private Integer tradeType;
	// 交易金额
	private BigDecimal transMoney;
	// 描述: 交易渠道:1-汇付宝,2-连连
	private Integer payChannel;
	/**
	 * 描述: 对账状态:0-无需对账,1-未对账,2-对账成功,3-对账失败 字段: reconciliation_state TINYINT(3)
	 * 默认值: 0
	 */
	private Integer reconciliationState;

	/**
	 * 描述: 对账结果说明 字段: reconciliation_desc VARCHAR(100) 默认值: ''
	 */
	private java.lang.String reconciliationDesc;

	/** 用户ID集合 **/
	private List<Integer> userIds;
	/** 状态 */
	private Integer state;

	// 用户银行卡信息
	/**
	 * 描述: 银行编码 字段: bank_code VARCHAR(20) 默认值: ''
	 */
	private java.lang.String bankCode;

	/**
	 * 描述: 银行名称 字段: bank_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String bankName;

	/**
	 * 描述: 银行卡号 字段: bank_card VARCHAR(30) 默认值: ''
	 */
	private java.lang.String bankCard;

	/**
	 * 描述: 支行名称 字段: branch_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String branchName;

	/**
	 * 描述: 省 字段: bank_province VARCHAR(10) 默认值: ''
	 */
	private java.lang.String bankProvince;

	/**
	 * 描述: 市 字段: bank_city VARCHAR(10) 默认值: ''
	 */
	private java.lang.String bankCity;

	/**
	 * 描述: 可用余额(元) 字段: useable_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal useableMoney = BigDecimal.ZERO;

	/** 用户类型 **/
	private Integer type;
	/***
	 * 提现手续费
	 */
	private BigDecimal commission;
	/**
	 * 收入金额
	 */
	private BigDecimal incomeMoney = BigDecimal.ZERO;
	/**
	 * 支出金额
	 */
	private BigDecimal outMoney = BigDecimal.ZERO;
	/**
	 * 冻结金额
	 */
	private BigDecimal freezeMoney = BigDecimal.ZERO;
	/**
	 * 用户ID
	 */
	private Integer regUserId;

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public java.util.Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public java.util.Date getModifyTimeBegin() {
		return modifyTimeBegin;
	}

	public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}

	public java.util.Date getModifyTimeEnd() {
		return modifyTimeEnd;
	}

	public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public Integer getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public Integer getReconciliationState() {
		return reconciliationState;
	}

	public void setReconciliationState(Integer reconciliationState) {
		this.reconciliationState = reconciliationState;
	}

	public java.lang.String getReconciliationDesc() {
		return reconciliationDesc;
	}

	public void setReconciliationDesc(java.lang.String reconciliationDesc) {
		this.reconciliationDesc = reconciliationDesc;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public BigDecimal getUseableMoney() {
		return useableMoney;
	}

	public void setUseableMoney(BigDecimal useableMoney) {
		this.useableMoney = useableMoney;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getIncomeMoney() {
		return incomeMoney;
	}

	public void setIncomeMoney(BigDecimal incomeMoney) {
		this.incomeMoney = incomeMoney;
	}

	public BigDecimal getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(BigDecimal outMoney) {
		this.outMoney = outMoney;
	}

	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
