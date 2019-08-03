package com.hongkun.finance.vas.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.model.VasCouponProduct.java
 * @Class Name    : VasCouponProduct.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class VasCouponProduct extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 描述: id
     * 字段: id  INT(10)
     */
    private Integer id;

    /**
     * 描述: 名称
     * 字段: name  VARCHAR(50)
     * 默认值: ''
     */

    private String name;

    /**
     * 描述: 产品类型:0-代表加息券，1-代表充值卡，2-免费提现券，3-好友券
     * 字段: type  TINYINT(3)
     * 默认值: 0
     */
    private Integer type;

    /**
     * 描述: 截止日期类型，0为过期天数，1为截至日期
     * 字段: deadline_type  TINYINT(3)
     * 默认值: 1
     */
    private Integer deadlineType;

    /**
     * 描述: 价值
     * 字段: worth  DOUBLE(12)
     * 默认值: 0.0000
     */
    private BigDecimal worth;

    /**
     * 描述: 可使用标的产品类型
     * 字段: bid_product_type_range  VARCHAR(11)
     * 默认值: ''
     */
    private String bidProductTypeRange;

    /**
     * 描述: 最小投资金额
     * 字段: amount_min  DOUBLE(20)
     * 默认值: 0.00
     */
    private BigDecimal amountMin;

    /**
     * 描述: 最大投资金额
     * 字段: amount_max  DOUBLE(20)
     * 默认值: 0.00
     */
    private BigDecimal amountMax;

    /**
     * 描述: 是否创建条目详细:0-未创建，1-创建
     * 字段: has_item  TINYINT(3)
     * 默认值: 0
     */
    private Integer hasItem;

    /**
     * 描述: 有效天数
     * 字段: valid_day  SMALLINT(5)
     * 默认值: 0
     */
    private Integer validDay;

    /**
     * 描述: 有效开始时间
     * 字段: begin_time  DATETIME(19)
     * 默认值: 0000-00-00 00:00:00
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date beginTime;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date beginTimeBegin;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date beginTimeEnd;
    /**
     * 描述: 有效结束时间
     * 字段: end_time  DATETIME(19)
     * 默认值: 0000-00-00 00:00:00
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTimeBegin;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTimeEnd;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTimeEndForGive;

    /**
     * 描述: 使用场景:0-无，1-注册赠送加息券，2-会员赠送加息券，3-会员待遇
     * 字段: coupon_scenes  TINYINT(3)
     * 默认值: 0
     */
    private Integer couponScenes;

    /**
     * 描述: 状态:0-未使用，1-使用中
     * 字段: state  TINYINT(3)
     * 默认值: 1
     */
    private Integer state;

    /**
     * 描述: 作用域:1-hkjf，2-cxj
     * 字段: state  TINYINT(4)
     * 默认值: 1
     */
    private Integer actionScope;
    /**
     * 描述: 创建时间
     * 字段: create_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date createTime;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTimeEnd;
    /**
     * 描述: 修改时间
     * 字段: modify_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTimeEnd;

    public VasCouponProduct() {
    }

    public VasCouponProduct(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setDeadlineType(Integer deadlineType) {
        this.deadlineType = deadlineType;
    }

    public Integer getDeadlineType() {
        return this.deadlineType;
    }

    public void setBidProductTypeRange(String bidProductTypeRange) {
        this.bidProductTypeRange = bidProductTypeRange;
    }

    public String getBidProductTypeRange() {
        return this.bidProductTypeRange;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public BigDecimal getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(BigDecimal amountMin) {
        this.amountMin = amountMin;
    }

    public BigDecimal getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(BigDecimal amountMax) {
        this.amountMax = amountMax;
    }

    public void setHasItem(Integer hasItem) {
        this.hasItem = hasItem;
    }

    public Integer getHasItem() {
        return this.hasItem;
    }

    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
    }

    public Integer getValidDay() {
        return this.validDay;
    }

    public void setBeginTime(java.util.Date beginTime) {
        this.beginTime = beginTime;
    }

    public java.util.Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTimeBegin(java.util.Date beginTimeBegin) {
        this.beginTimeBegin = beginTimeBegin;
    }

    public java.util.Date getBeginTimeBegin() {
        return this.beginTimeBegin;
    }

    public void setBeginTimeEnd(java.util.Date beginTimeEnd) {
        this.beginTimeEnd = beginTimeEnd;
    }

    public java.util.Date getBeginTimeEnd() {
        return this.beginTimeEnd;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public java.util.Date getEndTime() {
        return this.endTime;
    }

    public void setEndTimeBegin(java.util.Date endTimeBegin) {
        this.endTimeBegin = endTimeBegin;
    }

    public java.util.Date getEndTimeBegin() {
        return this.endTimeBegin;
    }

    public void setEndTimeEnd(java.util.Date endTimeEnd) {
        this.endTimeEnd = endTimeEnd;
    }

    public java.util.Date getEndTimeEnd() {
        return this.endTimeEnd;
    }

    public void setCouponScenes(Integer couponScenes) {
        this.couponScenes = couponScenes;
    }

    public Integer getCouponScenes() {
        return this.couponScenes;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTimeBegin(java.util.Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public java.util.Date getCreateTimeBegin() {
        return this.createTimeBegin;
    }

    public void setCreateTimeEnd(java.util.Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public java.util.Date getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public java.util.Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
        this.modifyTimeBegin = modifyTimeBegin;
    }

    public java.util.Date getModifyTimeBegin() {
        return this.modifyTimeBegin;
    }

    public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
        this.modifyTimeEnd = modifyTimeEnd;
    }

    public java.util.Date getModifyTimeEnd() {
        return this.modifyTimeEnd;
    }

    public Date getEndTimeEndForGive() {
        return endTimeEndForGive;
    }

    public void setEndTimeEndForGive(Date endTimeEndForGive) {
        this.endTimeEndForGive = endTimeEndForGive;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

