package com.hongkun.finance.contract.model.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description : 散标投资记录合同信息VO
 * @Project : framework
 * @Program Name  : com.hongkun.finance.contract.model.vo.CommonInvestConInfoVO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class CommonInvestConInfoVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型
     */
    private Integer contractType;
    /**
     * 借款人名称
     */
    private String borrowName;
    /**
     * 投资人名称
     */
    private String investName;
    /**
     * 投资金额
     */
    private BigDecimal investAmount;
    /**
     * 投资记录id
     */
    private Integer bidInvestId;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Integer getBidInvestId() {
        return bidInvestId;
    }

    public void setBidInvestId(Integer bidInvestId) {
        this.bidInvestId = bidInvestId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
