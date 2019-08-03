package com.hongkun.finance.loan.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description   : 生成还款计划或者回款计划的通用实体类
 * @Project       : finance-loan-model
 * @Program Name  : com.hongkun.finance.loan.model.vo.BidCommonPlanVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 刘旭辉
 */
public class BidCommonPlanVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//还款类型
	private Integer repayType;
	
	private Integer bidId;
	//期限类型
	private Integer termUnit;
	//借款期限值
	private Integer termValue;
	//总金额
	private BigDecimal totalAmount;
	//年化率
	private BigDecimal interestRate;
	//服务费率
	private BigDecimal serviceRate;
	private Integer regUserId;
	//本金接收人id
	private Integer receiptUserId;
	//偿还本金人员id
	private Integer repayUserId;
	
	//投资记录id
	private Integer investId;
	
	//放款时间
	private Date lendingTime;
	
	//企业账户id   回款计划中接受本金的用户
	private Integer companyId;
	
	public Integer getRepayType() {
		return repayType;
	}
	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}
	
	public Integer getBidId() {
		return bidId;
	}
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	public Integer getTermUnit() {
		return termUnit;
	}
	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}
	public Integer getTermValue() {
		return termValue;
	}
	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public BigDecimal getServiceRate() {
		return serviceRate;
	}
	public void setServiceRate(BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}
	public Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	public Integer getReceiptUserId() {
		return receiptUserId;
	}
	public void setReceiptUserId(Integer receiptUserId) {
		this.receiptUserId = receiptUserId;
	}
	public Integer getRepayUserId() {
		return repayUserId;
	}
	public void setRepayUserId(Integer repayUserId) {
		this.repayUserId = repayUserId;
	}
	public Integer getInvestId() {
		return investId;
	}
	public void setInvestId(Integer investId) {
		this.investId = investId;
	}
	public Date getLendingTime() {
		return lendingTime;
	}
	public void setLendingTime(Date lendingTime) {
		this.lendingTime = lendingTime;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}
