package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 债权转让VO类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualAppVo extends BaseModel {
    private Integer investId; //投资记录id
    private Integer transferId; //转让记录id
    private BigDecimal investAmount; //投资记录金额
    private BigDecimal creditorAmount; //转让金额
    private BigDecimal creditorPrice; //转让价格
    private BigDecimal buyerRate; //购买人利率
    private String bidName; //标的名称
    private String endDate; //筹集结束日期
    private Integer transferDays;//筹集天数
    private Integer buyerHoldDays;//购买人持有总天数
    private Integer bidId;//标的Id
    private Date createTime;//发布时间
    private Integer state;

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
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

    public void setCreditorAmount(BigDecimal creditorAmount) {
        this.creditorAmount = creditorAmount;
    }

    public BigDecimal getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(BigDecimal buyerRate) {
        this.buyerRate = buyerRate;
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

    public BigDecimal getCreditorPrice() {
        return creditorPrice;
    }

    public void setCreditorPrice(BigDecimal creditorPrice) {
        this.creditorPrice = creditorPrice;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public Integer getTransferDays() {
        return transferDays;
    }

    public void setTransferDays(Integer transferDays) {
        this.transferDays = transferDays;
    }

    public Integer getBuyerHoldDays() {
        return buyerHoldDays;
    }

    public void setBuyerHoldDays(Integer buyerHoldDays) {
        this.buyerHoldDays = buyerHoldDays;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
