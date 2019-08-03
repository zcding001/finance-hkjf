package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description : 债权转让详情VO类（产品详情）
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualDetailAppVo extends BaseModel {
    private Integer investUserId;
    private Integer investId;
    private Integer bidId;
    private String bidName;
    private BigDecimal creditorAmount;//转让金额
    private BigDecimal transferPrice;//转让价格
    private BigDecimal interestRate; //标的年化率
    private BigDecimal buyerRate;// 购买人预期年化率
    private Integer transferDays;//筹集天数
    private BigDecimal transferedRate;//转让比率（默认100）
    private Date transferEndTime;//筹集期限结束日期
    private Date currentTime;//当前系统时间
    private String investUserName;//原债权人姓名
    private BigDecimal holdMoney;//持有金额
    private Integer biddRepaymentWay;//还款方式
    private String biddRepaymentWayShow; //还款方式展示名称
    private BigDecimal expectAmount;//预期收益
    private String lendingTime; //放款时间（计息日期）
    private String endDate;//标的结束日期
    private Integer buyerHoldDays;//购买人持有天数（投资期限）
    private Integer state;
    private Integer termUnit;
    private Integer termValue;

    private List<Integer> conInfoTypes;//合同类型

    private Integer showConInfoFlag;//是否支持查看原项目

    private Integer newInvestId;

    private Date createTime;

    private long expireTime;

    private Integer resuTransferDays;

    private Integer firstInvestId;

    public Integer getFirstInvestId() {
        return firstInvestId;
    }

    public void setFirstInvestId(Integer firstInvestId) {
        this.firstInvestId = firstInvestId;
    }

    public Integer getInvestUserId() {
        return investUserId;
    }

    public void setInvestUserId(Integer investUserId) {
        this.investUserId = investUserId;
    }

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

    public BigDecimal getCreditorAmount() {
        return creditorAmount;
    }

    public void setCreditorAmount(BigDecimal creditorAmount) {
        this.creditorAmount = creditorAmount;
    }

    public BigDecimal getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(BigDecimal buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Integer getTransferDays() {
        return transferDays;
    }

    public void setTransferDays(Integer transferDays) {
        this.transferDays = transferDays;
    }

    public BigDecimal getTransferedRate() {
        return transferedRate;
    }

    public void setTransferedRate(BigDecimal transferedRate) {
        this.transferedRate = transferedRate;
    }

    public Date getTransferEndTime() {
        return transferEndTime;
    }

    public void setTransferEndTime(Date transferEndTime) {
        this.transferEndTime = transferEndTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getInvestUserName() {
        return investUserName;
    }

    public void setInvestUserName(String investUserName) {
        this.investUserName = investUserName;
    }

    public BigDecimal getHoldMoney() {
        return holdMoney;
    }

    public void setHoldMoney(BigDecimal holdMoney) {
        this.holdMoney = holdMoney;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public String getBiddRepaymentWayShow() {
        return biddRepaymentWayShow;
    }

    public void setBiddRepaymentWayShow(String biddRepaymentWayShow) {
        this.biddRepaymentWayShow = biddRepaymentWayShow;
    }

    public BigDecimal getExpectAmount() {
        return expectAmount;
    }

    public void setExpectAmount(BigDecimal expectAmount) {
        this.expectAmount = expectAmount;
    }

    public String getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(String lendingTime) {
        this.lendingTime = lendingTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getBuyerHoldDays() {
        return buyerHoldDays;
    }

    public void setBuyerHoldDays(Integer buyerHoldDays) {
        this.buyerHoldDays = buyerHoldDays;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public List<Integer> getConInfoTypes() {
        return conInfoTypes;
    }

    public void setConInfoTypes(List<Integer> conInfoTypes) {
        this.conInfoTypes = conInfoTypes;
    }

    public Integer getShowConInfoFlag() {
        return showConInfoFlag;
    }

    public void setShowConInfoFlag(Integer showConInfoFlag) {
        this.showConInfoFlag = showConInfoFlag;
    }

    public Integer getNewInvestId() {
        return newInvestId;
    }

    public void setNewInvestId(Integer newInvestId) {
        this.newInvestId = newInvestId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getResuTransferDays() {
        return resuTransferDays;
    }

    public void setResuTransferDays(Integer resuTransferDays) {
        this.resuTransferDays = resuTransferDays;
    }
}
