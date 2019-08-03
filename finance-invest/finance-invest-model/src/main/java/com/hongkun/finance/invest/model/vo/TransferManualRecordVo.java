package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualRecordVo extends BaseModel {
    private Integer transferId;
    private String buyerName;
    private Date buyerTime;
    private BigDecimal creditorAmount;

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Date getBuyerTime() {
        return buyerTime;
    }

    public void setBuyerTime(Date buyerTime) {
        this.buyerTime = buyerTime;
    }

    public BigDecimal getCreditorAmount() {
        return creditorAmount;
    }

    public void setCreditorAmount(BigDecimal creditorAmount) {
        this.creditorAmount = creditorAmount;
    }
}
