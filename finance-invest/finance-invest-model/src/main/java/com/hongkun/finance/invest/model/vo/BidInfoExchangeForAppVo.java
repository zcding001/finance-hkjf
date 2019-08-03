package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : TODO
 * @Project : finance-hkjf
 * @Program Name  : com.hongkun.finance.invest.model.vo
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInfoExchangeForAppVo extends BaseModel {
    private Integer id;
    private String name;//标的名称
    private BigDecimal totalAmount;//标的总金额
    private Integer allowCoupon; //是否允许使用卡券:0：不支持 1-不限制；2:仅加息券；3：仅红包',
    private Integer bidSchemeValue; //招标方案值
    private Integer biddRepaymentWay;//还款方式
    private String biddRepaymentWayDesc;//还款方式描述

    private boolean hasStarted;

    private BigDecimal interestRate; // 年化率

    private String labelText;//标签内容
    private String labelUrl;//标签链接

    private Integer productType;//产品类型：11-交易所匹配产品

    private Integer progress; //进度
    private BigDecimal raiseRate;//加息率
    private BigDecimal residueAmount;//剩余金额
    private Date startTime;//开始时间
    private Integer state;//标的状态0-未开始,2-投标中,3-已售罄

    private BigDecimal tenThousandIncome;//万元收益
    private Integer termUnit;//时间单位
    private Integer termValue;//时间值

    private Integer type;//标的类型：:0-正常,1-爆款标,2-推荐标'
    private Integer actionScope;//作用域：1-hkjf, 2-cxj

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAllowCoupon() {
        return allowCoupon;
    }

    public void setAllowCoupon(Integer allowCoupon) {
        this.allowCoupon = allowCoupon;
    }

    public Integer getBidSchemeValue() {
        return bidSchemeValue;
    }

    public void setBidSchemeValue(Integer bidSchemeValue) {
        this.bidSchemeValue = bidSchemeValue;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public String getBiddRepaymentWayDesc() {
        return biddRepaymentWayDesc;
    }

    public void setBiddRepaymentWayDesc(String biddRepaymentWayDesc) {
        this.biddRepaymentWayDesc = biddRepaymentWayDesc;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public BigDecimal getRaiseRate() {
        return raiseRate;
    }

    public void setRaiseRate(BigDecimal raiseRate) {
        this.raiseRate = raiseRate;
    }

    public BigDecimal getResidueAmount() {
        return residueAmount;
    }

    public void setResidueAmount(BigDecimal residueAmount) {
        this.residueAmount = residueAmount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getTenThousandIncome() {
        return tenThousandIncome;
    }

    public void setTenThousandIncome(BigDecimal tenThousandIncome) {
        this.tenThousandIncome = tenThousandIncome;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }
}
