package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 标的投资统计VO
 *
 * @author zc.ding
 * @create 2018/9/18
 */
public class StaFunInvestVO extends BaseModel {
    /**
    * 投资人数
    */
    private Integer investUserSum;
    /**
    * 投资金额
    */
    private BigDecimal investAmountSum;
    /**
    * 投资次数
    */
    private Integer investTimes;
    /**
    * 用户标识
    */
    private Integer regUserId;
    /**
    * 手机号
    */
    private Long login;
    /**
    * 真实姓名
    */
    private String realName;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 标的名称
     */
    private String bidName;
    /**
     * 产品类型
     */
    private Integer productType;
    /**
     * 投资金额
     */
    private BigDecimal investAmount;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
    * 单位期限
    */
    private Integer termUnit;
    /**
     * 单位期限值
     */
    private Integer termValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 投资次数
     */
    private Integer times;
    
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private List<Integer> userIds = new ArrayList<>();

   
    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getInvestUserSum() {
        return investUserSum;
    }

    public void setInvestUserSum(Integer investUserSum) {
        this.investUserSum = investUserSum;
    }

    public BigDecimal getInvestAmountSum() {
        return investAmountSum;
    }

    public void setInvestAmountSum(BigDecimal investAmountSum) {
        this.investAmountSum = investAmountSum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(Integer regUserId) {
        this.regUserId = regUserId;
    }

    public Integer getInvestTimes() {
        return investTimes;
    }

    public void setInvestTimes(Integer investTimes) {
        this.investTimes = investTimes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
