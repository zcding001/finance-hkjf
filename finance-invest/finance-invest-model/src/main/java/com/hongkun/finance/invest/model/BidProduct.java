package com.hongkun.finance.invest.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.model.BidProduct.java
 * @Class Name    : BidProduct.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class BidProduct extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 描述: 编号
     * 字段: id  INT(10)
     */
    @NotNull(message = "请指定标的产品",groups = {UPDATE.class})
    private java.lang.Integer id;

    /**
     * 描述: 产品名称
     * 字段: name  VARCHAR(50)
     * 默认值: ''
     */
    @NotEmpty(message = "产品名称不能为空",groups = {SAVE.class, UPDATE.class})
    private java.lang.String name;

    /**
     * 描述: 适应人群
     * 字段: adaptcrowd  VARCHAR(100)
     * 默认值: ''
     */
    @NotEmpty(message = "适应人群不能为空",groups = {SAVE.class, UPDATE.class})
    private java.lang.String adaptcrowd;

    /**
     * 描述: 产品描述
     * 字段: description  VARCHAR(1000)
     * 默认值: ''
     */
    @NotNull(message = "产品描述不能为空",groups = {SAVE.class, UPDATE.class})
    private java.lang.String description;

    /**
     * 描述: 额度范围起始（万）
     * 字段: amount_min  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请输入额度范围起始",groups = {SAVE.class, UPDATE.class})
    private java.lang.Integer amountMin;

    /**
     * 描述: 额度范围结束（万）
     * 字段: amount_max  INT(10)
     * 默认值: 0
     */
    @NotNull(message = "请输入额度范围结束",groups = {SAVE.class, UPDATE.class})
    private java.lang.Integer amountMax;

    /**
     * 描述: 利率范围起始（%)
     * 字段: rate_min  DECIMAL(4)
     * 默认值: 0.00
     */
    @NotNull(message = "请输入利率范围起始",groups = {SAVE.class, UPDATE.class})
    private java.math.BigDecimal rateMin;

    /**
     * 描述: 利率范围结束（%）
     * 字段: rate_max  DECIMAL(4)
     * 默认值: 0.00
     */
    @NotNull(message = "请输入利率范围结束",groups = {SAVE.class, UPDATE.class})
    private java.math.BigDecimal rateMax;

    /**
     * 描述: 期限单位:1-年,2-月,3-天
     * 字段: term_unit  TINYINT(3)
     * 默认值: 0
     */
    private Integer termUnit;

    /**
     * 描述: 借款期限最小值
     * 字段: term_value_min  SMALLINT(5)
     * 默认值: 0
     */
    @NotNull(message = "请输入借款期限最小值",groups = {SAVE.class, UPDATE.class})
    private Integer termValueMin;

    /**
     * 描述: 借款期限最大值
     * 字段: term_value_max  SMALLINT(5)
     * 默认值: 0
     */
    @NotNull(message = "请输入借款期限最大值",groups = {SAVE.class, UPDATE.class})
    private Integer termValueMax;

    /**
     * 描述: 招标方案值
     * 字段: bid_scheme_value  SMALLINT(5)
     * 默认值: 0
     */
    @NotNull(message = "请输入招标方案值",groups = {SAVE.class, UPDATE.class})
    private Integer bidSchemeValue;

    /**
     * 描述: 招标方案:0-平均金额招标,1-最低金额招标
     * 字段: bid_scheme  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请选择招标方案",groups = {SAVE.class, UPDATE.class})
    private Integer bidScheme;

    /**
     * 描述: 投标期限最大（天）
     * 字段: bid_deadline  SMALLINT(5)
     * 默认值: 0
     */
//    @NotNull(message = "请输入最大投标期限")
    private Integer bidDeadline;

    /**
     * 描述: 还款方式:1-等额本息,2-按月付息，到期还本,3-到期还本付息,4-到期付息，本金回收,5-每月付息，到期本金回收,6-按月付息，本金划归企业
     * 字段: repaymentway  TINYINT(3)
     * 默认值: 1
     */
    @NotNull(message = "请输入还款方式",groups = {SAVE.class, UPDATE.class})
    private String repaymentway;

    /**
     * 描述: 合同
     * 字段: contract  VARCHAR(50)
     * 默认值: ''
     */
    @NotNull(message = "请输入合同",groups = {SAVE.class, UPDATE.class})
    private java.lang.String contract;

    /**
     * 描述: 产品类型 0-普通产品,1-优选产品,2-月月盈,3-季季盈,4-年年盈,5-体验金产品,6-购房宝,7-活期产品,8-活动产品,9-物业宝'
     * 字段: type  TINYINT(3)
     * 默认值: 0
     */
    @NotNull(message = "请输入产品类型",groups = {SAVE.class, UPDATE.class})
    private Integer type;

    /**
     * 描述: 借款人必审资料
     * 字段: audit_data  VARCHAR(100)
     * 默认值: ''
     */
    private java.lang.String auditData;

    /**
     * 描述: 创建人员ID
     * 字段: create_user_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer createUserId;

    /**
     * 描述: 状态0-已删除,1-正常状态
     * 字段: state  TINYINT(3)
     * 默认值: 1
     */
    private Integer state;

    /**
     * 描述: 创建时间
     * 字段: create_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date createTime;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date createTimeEnd;
    /**
     * 描述: 修改时间
     * 字段: modify_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeEnd;

    //作用域：1-hkjf, 2-cxj
    private Integer actionScope;

    public BidProduct() {
    }

    public BidProduct(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setAdaptcrowd(java.lang.String adaptcrowd) {
        this.adaptcrowd = adaptcrowd;
    }

    public java.lang.String getAdaptcrowd() {
        return this.adaptcrowd;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setAmountMin(java.lang.Integer amountMin) {
        this.amountMin = amountMin;
    }

    public java.lang.Integer getAmountMin() {
        return this.amountMin;
    }

    public void setAmountMax(java.lang.Integer amountMax) {
        this.amountMax = amountMax;
    }

    public java.lang.Integer getAmountMax() {
        return this.amountMax;
    }

    public void setRateMin(java.math.BigDecimal rateMin) {
        this.rateMin = rateMin;
    }

    public java.math.BigDecimal getRateMin() {
        return this.rateMin;
    }

    public void setRateMax(java.math.BigDecimal rateMax) {
        this.rateMax = rateMax;
    }

    public java.math.BigDecimal getRateMax() {
        return this.rateMax;
    }

    public void setTermUnit(Integer termUnit) {
        this.termUnit = termUnit;
    }

    public Integer getTermUnit() {
        return this.termUnit;
    }

    public void setTermValueMin(Integer termValueMin) {
        this.termValueMin = termValueMin;
    }

    public Integer getTermValueMin() {
        return this.termValueMin;
    }

    public void setTermValueMax(Integer termValueMax) {
        this.termValueMax = termValueMax;
    }

    public Integer getTermValueMax() {
        return this.termValueMax;
    }

    public void setBidSchemeValue(Integer bidSchemeValue) {
        this.bidSchemeValue = bidSchemeValue;
    }

    public Integer getBidSchemeValue() {
        return this.bidSchemeValue;
    }

    public void setBidScheme(Integer bidScheme) {
        this.bidScheme = bidScheme;
    }

    public Integer getBidScheme() {
        return this.bidScheme;
    }

    public void setBidDeadline(Integer bidDeadline) {
        this.bidDeadline = bidDeadline;
    }

    public Integer getBidDeadline() {
        return this.bidDeadline;
    }

    public void setRepaymentway(String repaymentway) {
        this.repaymentway = repaymentway;
    }

    public String getRepaymentway() {
        return this.repaymentway;
    }

    public void setContract(java.lang.String contract) {
        this.contract = contract;
    }

    public java.lang.String getContract() {
        return this.contract;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setAuditData(java.lang.String auditData) {
        this.auditData = auditData;
    }

    public java.lang.String getAuditData() {
        return this.auditData;
    }

    public void setCreateUserId(java.lang.Integer createUserId) {
        this.createUserId = createUserId;
    }

    public java.lang.Integer getCreateUserId() {
        return this.createUserId;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
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

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

