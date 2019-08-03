package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 债权转让VO类(发布转让之前-》展示债权详情)
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualAppPreVo extends BaseModel {
    private Integer investId;
    private Integer bidId;
    private String bidName;
    private String endDate; //标的截至日期
    private BigDecimal investAmount;//投资记录金额
    private BigDecimal holdAmount;//持有金额
    private BigDecimal creditorAmount;//可转本金
    private Integer termUnit;//期限单位
    private Integer termValue; //期限
    private BigDecimal interestRate; //标的年化率
    private BigDecimal receivedIncome; //已收收益
    private BigDecimal notReceivedIncome; //未收收益
    private BigDecimal transferRate; //转让比率
    private BigDecimal afterRate; //转让人转让后年化率
    private BigDecimal buyerRate;// 购买人预期年化率
    private Integer maxTransferDays;//最大筹集天数
    private  Integer creditorState;//是否允许全部转让  0-不允许转让 1-允许全部转让 2-允许部分转让
    private Date lendingTime;

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getCreditorAmount() {
        return creditorAmount;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public void setCreditorAmount(BigDecimal creditorAmount) {
        this.creditorAmount = creditorAmount;
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getReceivedIncome() {
        return receivedIncome;
    }

    public void setReceivedIncome(BigDecimal receivedIncome) {
        this.receivedIncome = receivedIncome;
    }

    public BigDecimal getNotReceivedIncome() {
        return notReceivedIncome;
    }

    public void setNotReceivedIncome(BigDecimal notReceivedIncome) {
        this.notReceivedIncome = notReceivedIncome;
    }

    public BigDecimal getTransferRate() {
        return transferRate;
    }

    public void setTransferRate(BigDecimal transferRate) {
        this.transferRate = transferRate;
    }

    public BigDecimal getAfterRate() {
        return afterRate;
    }

    public void setAfterRate(BigDecimal afterRate) {
        this.afterRate = afterRate;
    }

    public BigDecimal getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(BigDecimal buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Integer getMaxTransferDays() {
        return maxTransferDays;
    }

    public void setMaxTransferDays(Integer maxTransferDays) {
        this.maxTransferDays = maxTransferDays;
    }

    public Integer getCreditorState() {
        return creditorState;
    }

    public void setCreditorState(Integer creditorState) {
        this.creditorState = creditorState;
    }

    public Date getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(Date lendingTime) {
        this.lendingTime = lendingTime;
    }
}
