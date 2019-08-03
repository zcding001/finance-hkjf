package com.hongkun.finance.payment.model.vo;

import com.yirun.framework.core.annotation.Union;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description : 支付模块通用VO
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.vo.PaymentVO.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public class PaymentVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 流水ID **/
	@Union(bind = { "FinTradeFlow" }, reNameTo = "id")
	private Integer tfId;
	/** 流水flow_id **/
	@Union(bind = { "FinTradeFlow" }, reNameTo = "flowId")
	private String tfFlowId;
	/** 流水p_flow_id **/
	@Union(bind = { "FinTradeFlow" }, reNameTo = "pflowId")
	private String tfPFlowId;
	/** 交易金额 **/
	@Union(bind = { "FinTradeFlow" }, reNameTo = "transMoney")
	private BigDecimal tfMoney;
	/** 流水交易类型 **/
	private Integer tradeType;

	@Union(bind = { "FinTradeFlow" }, reNameTo = "payChannel")
	private Integer payChannel;
	/** 资金划转ID **/
	@Union(bind = { "FinFundtransfer" }, reNameTo = "id")
	private Integer ftId;
	@Union(bind = { "FinFundtransfer" }, reNameTo = "flowId")
	/** 资金划转flow_id **/
	private String ftFlowId;
	/** 资金划转p_flow_id **/
	@Union(bind = { "FinFundtransfer" }, reNameTo = "pflowId")
	private String ftPFlowId;
	/** 资金划转交易类型 **/
	private Integer subCode;
	/** 交易金额 **/
	@Union(bind = { "FinFundtransfer" }, reNameTo = "transMoney")
	private BigDecimal ftMoney;
	/** 交易时间 **/
	@Union(bind = { "FinFundtransfer" }, reNameTo = "createTime")
	private Date createTime;

	/** 用户id **/
	@Union(bind = { "UserVO" })
	private Integer userId;

	/** 用户ID集合 **/
	private List<Integer> userIds;
	/** 用户真实姓名 **/
	@Union(bind = { "UserVO" })
	private String realName;
	/** 用户手机号 **/
	@Union(bind = { "UserVO" })
	private java.lang.Long login;
	/** 收款方Id **/
	private Integer recUserId;
	/** 收款人名称 **/
	private String recRealName;

	private String createTimeBegin;
	private String createTimeEnd;
	
	//支付次数
	private Integer payTimes;
	//支付人数
	private Integer payUserSum;
	//支付总金额
	private BigDecimal transMoneySum;

	public Integer getPayTimes() {
        return payTimes;
    }

    public void setPayTimes(Integer payTimes) {
        this.payTimes = payTimes;
    }

    public Integer getPayUserSum() {
        return payUserSum;
    }

    public void setPayUserSum(Integer payUserSum) {
        this.payUserSum = payUserSum;
    }

    public BigDecimal getTransMoneySum() {
        return transMoneySum;
    }

    public void setTransMoneySum(BigDecimal transMoneySum) {
        this.transMoneySum = transMoneySum;
    }

    public Integer getTfId() {
		return tfId;
	}

	public void setTfId(Integer tfId) {
		this.tfId = tfId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public Integer getSubCode() {
		return subCode;
	}

	public void setSubCode(Integer subCode) {
		this.subCode = subCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getTfFlowId() {
		return tfFlowId;
	}

	public void setTfFlowId(String tfFlowId) {
		this.tfFlowId = tfFlowId;
	}

	public String getTfPFlowId() {
		return tfPFlowId;
	}

	public void setTfPFlowId(String tfPFlowId) {
		this.tfPFlowId = tfPFlowId;
	}

	public String getFtFlowId() {
		return ftFlowId;
	}

	public void setFtFlowId(String ftFlowId) {
		this.ftFlowId = ftFlowId;
	}

	public String getFtPFlowId() {
		return ftPFlowId;
	}

	public void setFtPFlowId(String ftPFlowId) {
		this.ftPFlowId = ftPFlowId;
	}

	public BigDecimal getTfMoney() {
		return tfMoney;
	}

	public void setTfMoney(BigDecimal tfMoney) {
		this.tfMoney = tfMoney;
	}

	public BigDecimal getFtMoney() {
		return ftMoney;
	}

	public void setFtMoney(BigDecimal ftMoney) {
		this.ftMoney = ftMoney;
	}

	public Integer getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(Integer recUserId) {
		this.recUserId = recUserId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRecRealName() {
		return recRealName;
	}

	public void setRecRealName(String recRealName) {
		this.recRealName = recRealName;
	}

	public Integer getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public Long getLogin() {
		return login;
	}

	public void setLogin(Long login) {
		this.login = login;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
}
