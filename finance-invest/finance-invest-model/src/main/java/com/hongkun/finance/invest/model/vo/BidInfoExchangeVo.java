package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 交易所匹配标的vo
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInfoExchangeVo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String bidName;
    private String borrowerName;
    private Integer borrowerId;
    private String productName;
    private BigDecimal totalAmount;//标的总金额
    private BigDecimal interestRate;
    private Integer termUnit;
    private Integer termValue;
    private Integer state;
    private BigDecimal repayAmount;//本息合计
    private Date createTime;
    private Integer actionScope;//作用域：1-hkjf, 2-cxj

    private Integer biddRepaymentWay;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }
}
