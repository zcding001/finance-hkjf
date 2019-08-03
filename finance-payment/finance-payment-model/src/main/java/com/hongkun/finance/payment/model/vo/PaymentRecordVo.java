package com.hongkun.finance.payment.model.vo;

import com.yirun.framework.core.model.BaseModel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.List;

public class PaymentRecordVo extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: 交易流水:交易类型+交易来源+YYYYMMDDH24mmssSSS+五位流水 字段: flow_id VARCHAR(40) 默认值:
	 * ''
	 */
	private java.lang.String flowId;

	/**
	 * 描述: 交易金额 字段: trans_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal transMoney;
   /**
    * 手续费
    */
    private BigDecimal commission;

	/**
	 * 描述: 交易类型:10-充值 14-提现 字段: trade_type SMALLINT(5) 默认值: 0
	 */
	private Integer tradeType;

	/**
	 * 描述: 交易渠道:1-汇付宝,2-连连 字段: pay_channel TINYINT(3) 默认值: 0
	 */
	private Integer payChannel;
	/**
	 * 描述: 状态:0-删除,1-待审核状态,2-待放款,3-运营审核拒绝,4-财务审核拒绝,5-划转中,6-已划转,7-划转失败
	 * ,8-已冲正,9失败,10等待定时提现 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

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
	 * 用户ID集合
	 */
	private List<Integer> regUserIdList;
	/**
	 * 用户姓名
	 */
	private String realName;
	/**
	 * 手机号
	 */
	private Long login;
	/**
	 * 银行名称
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
    

	public PaymentRecordVo() {
	}


    public java.lang.String getFlowId() {
        return flowId;
    }


    public void setFlowId(java.lang.String flowId) {
        this.flowId = flowId;
    }


    public java.math.BigDecimal getTransMoney() {
        return transMoney;
    }


    public void setTransMoney(java.math.BigDecimal transMoney) {
        this.transMoney = transMoney;
    }

    public Integer getTradeType() {
        return tradeType;
    }


    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }


    public Integer getPayChannel() {
        return payChannel;
    }


    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }


    public Integer getState() {
        return state;
    }


    public void setState(Integer state) {
        this.state = state;
    }


    public java.util.Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }


    public java.util.Date getCreateTimeBegin() {
        return createTimeBegin;
    }


    public void setCreateTimeBegin(java.util.Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }


    public java.util.Date getCreateTimeEnd() {
        return createTimeEnd;
    }


    public void setCreateTimeEnd(java.util.Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }


    public List<Integer> getRegUserIdList() {
        return regUserIdList;
    }


    public void setRegUserIdList(List<Integer> regUserIdList) {
        this.regUserIdList = regUserIdList;
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


    public java.lang.String getBankName() {
        return bankName;
    }


    public void setBankName(java.lang.String bankName) {
        this.bankName = bankName;
    }


    public java.lang.String getBankCard() {
        return bankCard;
    }


    public void setBankCard(java.lang.String bankCard) {
        this.bankCard = bankCard;
    }


    public java.lang.String getBranchName() {
        return branchName;
    }


    public void setBranchName(java.lang.String branchName) {
        this.branchName = branchName;
    }
    
    public BigDecimal getCommission() {
        return commission;
    }


    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
