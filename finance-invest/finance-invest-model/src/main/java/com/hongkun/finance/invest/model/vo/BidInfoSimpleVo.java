package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

public class BidInfoSimpleVo extends BaseModel{
	    
	private static final long serialVersionUID = 1L;
	
	//********************标的基础信息--bid_info 
	private java.lang.Integer id;
	private java.lang.String name;
	private java.lang.String bidCode;
	private java.lang.Integer bidProductId;
	private java.math.BigDecimal totalAmount;
	private java.math.BigDecimal residueAmount;
	private java.math.BigDecimal interestRate;
	private Integer termValue;
	private Integer termUnit;
	private Integer biddRepaymentWay;
	private Integer type;
	private java.lang.String showPosition;
	private java.lang.String investPosition;
	private Integer bidSchemeValue;
	private Integer bidScheme;
	private Integer sort;
	private java.lang.String assureType;
	private java.lang.String creditLevel;
	private java.lang.Integer borrowerId;
	private java.lang.Integer repayUserId;
	private java.lang.Integer receiptUserId;
	private java.lang.Integer companyId;
	private java.math.BigDecimal commissionRate;
	private java.math.BigDecimal serviceRate;
	private java.math.BigDecimal advanceServiceRate;
	private java.math.BigDecimal liquidatedDamagesRate;
	private java.lang.String imgUrl;
	private java.lang.String printImgurl;
	private Integer loanUse;
	private java.lang.Integer createUserId;
	private java.lang.Integer modifyUserId;
	private Integer state;
    private Integer allowCoupon;
	private java.util.Date createTime;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.util.Date lendingTime;
	
	//*********************标的产品信息 bid_product
	//产品类型
	private Integer productType;
	
	//*********************特殊字段
	//标的截止日期
	private java.util.Date endDate;
	private Integer actionScope;//作用域：1-hkjf, 2-cxj

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getBidCode() {
		return bidCode;
	}

	public void setBidCode(java.lang.String bidCode) {
		this.bidCode = bidCode;
	}

	public java.lang.Integer getBidProductId() {
		return bidProductId;
	}

	public void setBidProductId(java.lang.Integer bidProductId) {
		this.bidProductId = bidProductId;
	}

	public java.math.BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(java.math.BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public java.math.BigDecimal getResidueAmount() {
		return residueAmount;
	}

	public void setResidueAmount(java.math.BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
	}

	public java.math.BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(java.math.BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTermValue() {
		return termValue;
	}

	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}

	public Integer getTermUnit() {
		return termUnit;
	}

	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}

	public Integer getBiddRepaymentWay() {
		return biddRepaymentWay;
	}

	public void setBiddRepaymentWay(Integer biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.util.Date getLendingTime() {
		return lendingTime;
	}

	public void setLendingTime(java.util.Date lendingTime) {
		this.lendingTime = lendingTime;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public java.lang.String getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(java.lang.String showPosition) {
		this.showPosition = showPosition;
	}

	public java.lang.String getInvestPosition() {
		return investPosition;
	}

	public void setInvestPosition(java.lang.String investPosition) {
		this.investPosition = investPosition;
	}

	public Integer getBidSchemeValue() {
		return bidSchemeValue;
	}

	public void setBidSchemeValue(Integer bidSchemeValue) {
		this.bidSchemeValue = bidSchemeValue;
	}

	public Integer getBidScheme() {
		return bidScheme;
	}

	public void setBidScheme(Integer bidScheme) {
		this.bidScheme = bidScheme;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public java.lang.String getAssureType() {
		return assureType;
	}

	public void setAssureType(java.lang.String assureType) {
		this.assureType = assureType;
	}

	public java.lang.String getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(java.lang.String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public java.lang.Integer getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(java.lang.Integer borrowerId) {
		this.borrowerId = borrowerId;
	}

	public java.lang.Integer getRepayUserId() {
		return repayUserId;
	}

	public void setRepayUserId(java.lang.Integer repayUserId) {
		this.repayUserId = repayUserId;
	}

	public java.lang.Integer getReceiptUserId() {
		return receiptUserId;
	}

	public void setReceiptUserId(java.lang.Integer receiptUserId) {
		this.receiptUserId = receiptUserId;
	}

	public java.lang.Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(java.lang.Integer companyId) {
		this.companyId = companyId;
	}

	public java.math.BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(java.math.BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	public java.math.BigDecimal getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(java.math.BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}

	public java.math.BigDecimal getAdvanceServiceRate() {
		return advanceServiceRate;
	}

	public void setAdvanceServiceRate(java.math.BigDecimal advanceServiceRate) {
		this.advanceServiceRate = advanceServiceRate;
	}

	public java.math.BigDecimal getLiquidatedDamagesRate() {
		return liquidatedDamagesRate;
	}

	public void setLiquidatedDamagesRate(java.math.BigDecimal liquidatedDamagesRate) {
		this.liquidatedDamagesRate = liquidatedDamagesRate;
	}

	public java.lang.String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(java.lang.String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public java.lang.String getPrintImgurl() {
		return printImgurl;
	}

	public void setPrintImgurl(java.lang.String printImgurl) {
		this.printImgurl = printImgurl;
	}

	public Integer getLoanUse() {
		return loanUse;
	}

	public void setLoanUse(Integer loanUse) {
		this.loanUse = loanUse;
	}

	public java.lang.Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(java.lang.Integer createUserId) {
		this.createUserId = createUserId;
	}

	public java.lang.Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(java.lang.Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAllowCoupon() {
		return allowCoupon;
	}

	public void setAllowCoupon(Integer allowCoupon) {
		this.allowCoupon = allowCoupon;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
