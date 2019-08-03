package com.hongkun.finance.loan.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidRepayPlan.java
 * @Class Name    : BidRepayPlan.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidRepayPlan extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 标的id
	 * 字段: bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 还款期数
	 * 字段: periods  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer periods;
	
	/**
	 * 描述: 还款金额=还款本金+还款利息+逾期金额+违约金额+服务费
	 * 字段: amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal amount;
	
	/**
	 * 描述: 还款本金
	 * 字段: capital_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal capitalAmount;
	
	/**
	 * 描述: 还款利息
	 * 字段: interest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interestAmount;
	
	/**
	 * 描述: 剩余本金
	 * 字段: residue_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal residueAmount;
	
	/**
	 * 描述: 逾期金额
	 * 字段: punish_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal punishAmount;
	
	/**
	 * 描述: 违约金额
	 * 字段: dedit_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal deditAmount;
	
	/**
	 * 描述: 借款人每月应还给平台的服务费
	 * 字段: service_charge  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal serviceCharge;
	
	/**
	 * 描述: 预期还款时间
	 * 字段: plan_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date planTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date planTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date planTimeEnd;
	/**
	 * 描述: 实际还款时间
	 * 字段: actual_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date actualTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeEnd;
	/**
	 * 描述: 借款人ID
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 启用风险准备金时间
	 * 字段: riskaccount_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date riskaccountTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date riskaccountTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date riskaccountTimeEnd;
	/**
	 * 描述: 状态：0-无效，1-未还款，2-已还款,3-启用风险储备金，4-逾期，8-提前结清
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;

	private Integer actionScope;
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
	/**标的ID集合*/
	private List<Integer> bidIds = new ArrayList<>();

	private List<Integer> states = new ArrayList<>();

	/**借款人ID集合*/
	private List<Integer> regUserIds = new ArrayList<>();

    public BidRepayPlan(){
	}

	public BidRepayPlan(java.lang.Integer id){
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
	
	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	
	public Integer getPeriods() {
		return this.periods;
	}
	
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
	
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}
	
	public void setCapitalAmount(java.math.BigDecimal capitalAmount) {
		this.capitalAmount = capitalAmount;
	}
	
	public java.math.BigDecimal getCapitalAmount() {
		return this.capitalAmount;
	}
	
	public void setInterestAmount(java.math.BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	
	public java.math.BigDecimal getInterestAmount() {
		return this.interestAmount;
	}
	
	public void setResidueAmount(java.math.BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
	}
	
	public java.math.BigDecimal getResidueAmount() {
		return this.residueAmount;
	}
	
	public void setPunishAmount(java.math.BigDecimal punishAmount) {
		this.punishAmount = punishAmount;
	}
	
	public java.math.BigDecimal getPunishAmount() {
		return this.punishAmount;
	}
	
	public void setDeditAmount(java.math.BigDecimal deditAmount) {
		this.deditAmount = deditAmount;
	}
	
	public java.math.BigDecimal getDeditAmount() {
		return this.deditAmount;
	}
	
	public void setServiceCharge(java.math.BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	
	public java.math.BigDecimal getServiceCharge() {
		return this.serviceCharge;
	}
	
	public void setPlanTime(java.util.Date planTime) {
		this.planTime = planTime;
	}
	
	public java.util.Date getPlanTime() {
		return this.planTime;
	}
	
	public void setPlanTimeBegin(java.util.Date planTimeBegin) {
		this.planTimeBegin = planTimeBegin;
	}
	
	public java.util.Date getPlanTimeBegin() {
		return this.planTimeBegin;
	}
	
	public void setPlanTimeEnd(java.util.Date planTimeEnd) {
		this.planTimeEnd = planTimeEnd;
	}
	
	public java.util.Date getPlanTimeEnd() {
		return this.planTimeEnd;
	}	
	public void setActualTime(java.util.Date actualTime) {
		this.actualTime = actualTime;
	}
	
	public java.util.Date getActualTime() {
		return this.actualTime;
	}
	
	public void setActualTimeBegin(java.util.Date actualTimeBegin) {
		this.actualTimeBegin = actualTimeBegin;
	}
	
	public java.util.Date getActualTimeBegin() {
		return this.actualTimeBegin;
	}
	
	public void setActualTimeEnd(java.util.Date actualTimeEnd) {
		this.actualTimeEnd = actualTimeEnd;
	}
	
	public java.util.Date getActualTimeEnd() {
		return this.actualTimeEnd;
	}	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRiskaccountTime(java.util.Date riskaccountTime) {
		this.riskaccountTime = riskaccountTime;
	}
	
	public java.util.Date getRiskaccountTime() {
		return this.riskaccountTime;
	}
	
	public void setRiskaccountTimeBegin(java.util.Date riskaccountTimeBegin) {
		this.riskaccountTimeBegin = riskaccountTimeBegin;
	}
	
	public java.util.Date getRiskaccountTimeBegin() {
		return this.riskaccountTimeBegin;
	}
	
	public void setRiskaccountTimeEnd(java.util.Date riskaccountTimeEnd) {
		this.riskaccountTimeEnd = riskaccountTimeEnd;
	}
	
	public java.util.Date getRiskaccountTimeEnd() {
		return this.riskaccountTimeEnd;
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
	
	public List<Integer> getBidIds() {
		return bidIds;
	}

	public void setBidIds(List<Integer> bidIds) {
		this.bidIds = bidIds;
	}

    public List<Integer> getStates() {
        return states;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }

	public void setRegUserIds(List<Integer> regUserIds) {
		this.regUserIds = regUserIds;
	}

	public List<Integer> getRegUserIds() {
		return regUserIds;
	}

	public Integer getActionScope() {
		return actionScope;
	}

	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

