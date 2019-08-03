package com.hongkun.finance.invest.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础的标的信息类
 */
public class BaseBidInfoVO implements Serializable {


    /**
     * id
     */
    private Integer id;
    /**
     * 描述: 标的名称
     */
    private java.lang.String name;

    /**
     * 预期年化收益率
     */
    private java.math.BigDecimal interestRate;

    /*
    * 基础利率=interestRate - raiseRate
    */
    private BigDecimal baseRate;

    /**
     * 加息率
     */
    private BigDecimal raiseRate;

    /**
     * 投资期限
     */
    private Integer termValue;

    /**
     * 投资单位
     */
    private Integer termUnit;

    /**
     * 进度
     */
    private BigDecimal progress;

    /**
     * 描述: 标的总金额
     */
    private java.math.BigDecimal totalAmount;

    /**
     * 描述: 标的剩余金额
     */
    private java.math.BigDecimal residueAmount;

    /**
     * 万元收益
     */
    private java.math.BigDecimal tenThousandIncome;

    /**
     * 此标的是否允许使用卡券：0：不支持 1-不限制；2:仅加息券；3：仅红包
     */
    private Integer allowCoupon;


    /***
     * 标的状态,折合成前端状态:0-未开始,2-投标中,3-已售罄
     */
    private Integer state;

    /**
     * 产品类型
     */
    private Integer productType;
    /**
     * 标的类型
     */
    private Integer type;

    /**
     * 投标开始时间
     */
    private Date startTime;

    /**
     * 招标方案值
     */
    private Integer bidSchemeValue;

    /**
     * 描述: 还款方式:1-等额本息,2-按月付息，到期还本,3-到期还本付息,4-到期付息，本金回收,5-每月付息，到期本金回收,6-按月付息，本金划归企业
     * 字段: bidd_repayment_way  TINYINT(3)
     * 默认值: 2
     */
    private Integer biddRepaymentWay;

    /**
     * 本个标的是否已经开始
     */
    private Boolean hasStarted=Boolean.FALSE;

    private String labelText;//标签内容

    private String labelUrl;//标签链接

    private String printImgurl;// 印章图片

    private String imgUrl;// 项目图片

    private String biddRepaymentWayDesc; //还款方式描述

    /**
     * 单个标的最大投资金额  --目前只有新手标有此限制
     */
    private java.math.BigDecimal maxInvestMoney;

    /**
     * 单个标的最大投资金额字符串形式，如：限投56800元
     */
    private String maxInvestMoneyStr;

    /**
     * 用于app展示自定义的标的类型：10-新手标, 11-爆款标, 12-推荐标, 13-月月盈, 14-季季盈, 15-年年盈
     *                           16-体验标, 17-普通散标
     */
    private Integer appShowType;

    /**
     * 新手标建议起投金额
     */
    private BigDecimal recommendAmount;

    public BigDecimal getRecommendAmount() {
        return recommendAmount;
    }

    public void setRecommendAmount(BigDecimal recommendAmount) {
        this.recommendAmount = recommendAmount;
    }

    public Integer getAppShowType() {
        return appShowType;
    }

    public void setAppShowType(Integer appShowType) {
        this.appShowType = appShowType;
    }

    public String getBiddRepaymentWayDesc() {
        return biddRepaymentWayDesc;
    }

    public void setBiddRepaymentWayDesc(String biddRepaymentWayDesc) {
        this.biddRepaymentWayDesc = biddRepaymentWayDesc;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public BigDecimal getProgress() {
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getResidueAmount() {
        return residueAmount;
    }

    public void setResidueAmount(BigDecimal residueAmount) {
        this.residueAmount = residueAmount;
    }

    public BigDecimal getTenThousandIncome() {
        return tenThousandIncome;
    }

    public void setTenThousandIncome(BigDecimal tenThousandIncome) {
        this.tenThousandIncome = tenThousandIncome;
    }

    public Integer getAllowCoupon() {
        return allowCoupon;
    }

    public void setAllowCoupon(Integer allowCoupon) {
        this.allowCoupon = allowCoupon;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getRaiseRate() {
        return raiseRate;
    }

    public void setRaiseRate(BigDecimal raiseRate) {
        this.raiseRate = raiseRate;
    }

    public Integer getBidSchemeValue() {
        return bidSchemeValue;
    }

    public void setBidSchemeValue(Integer bidSchemeValue) {
        this.bidSchemeValue = bidSchemeValue;
    }

    public BigDecimal getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public String getMaxInvestMoneyStr() {
        return maxInvestMoneyStr;
    }

    public void setMaxInvestMoneyStr(String maxInvestMoneyStr) {
        this.maxInvestMoneyStr = maxInvestMoneyStr;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public String getPrintImgurl() {
        return printImgurl;
    }

    public void setPrintImgurl(String printImgurl) {
        this.printImgurl = printImgurl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}