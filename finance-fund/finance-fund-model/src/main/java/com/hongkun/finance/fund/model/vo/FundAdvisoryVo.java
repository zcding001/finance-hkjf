package com.hongkun.finance.fund.model.vo;


import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description : 咨询记录vo
 * @Project : finance-invest-model
 * @Program Name  : com.hongkun.finance.fund.model.vo.FundAdvisoryVo.java
 * @Author : yunlongliu@honghun.com.cn
 */
public class FundAdvisoryVo extends BaseModel {

    /**
     * 描述: id
     * 字段: id  INT(10)
     */
    private java.lang.Integer id;

    /**
     * 描述: 预约股权项目ID,一人可预约多个项目
     * 字段: info_ids  VARCHAR(30)
     * 默认值: ''
     */
    private java.lang.String infoIds;

    /**
     * 描述: 客户姓名
     * 字段: name  VARCHAR(50)
     * 默认值: ''
     */
    private java.lang.String name;

    /**
     * 描述: 客户电话
     * 字段: tel  VARCHAR(15)
     * 默认值: ''
     */
    private java.lang.String tel;

    /**
     * 描述: 客户性别:0-女,1-男
     * 字段: sex  TINYINT(3)
     * 默认值: 0
     */
    private Integer sex;

    /**
     * 描述: 是否是投(资)顾(问)客户: 0-否,1-是
     * 字段: advisor  TINYINT(3)
     * 默认值: 0
     */
    private Integer advisor;

    /**
     * 描述: 指派对象id
     * 字段: assign_user_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer assignUserId;

    /**
     * 描述: 指派对象姓名
     * 字段: assign_user_name  VARCHAR(50)
     */
    private java.lang.String assignUserName;

    /**
     * 描述: 客户来源: 0-平台,1-后台录入
     * 字段: source  TINYINT(3)
     * 默认值: 0
     */
    private Integer source;

    /**
     * 描述: 状态: 0-删除,1-已预约(待指派),2-已指派
     * 字段: state  TINYINT(3)
     * 默认值: 1
     */
    private Integer state;

    /**
     * 描述: 备注
     * 字段: remark  VARCHAR(255)
     * 默认值: ''
     */
    private java.lang.String remark;

    /**
     * 描述: 创建日期
     * 字段: create_time  TIMESTAMP(19)
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
     * 描述: 用户id
     * 字段: reg_user_id  INT UNSIGNED(10)
     * 默认值: 0
     */
    private java.lang.Integer regUserId;

    /**
     * 描述: 修改日期
     * 字段: modify_time  TIMESTAMP(19)
     * 默认值: CURRENT_TIMESTAMP
     */
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
    private java.util.Date modifyTimeEnd;
    /**
     * 描述: 修改用户ID
     * 字段: modify_user_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer modifyUserId;

    /**
     * 描述: 项目所属父类型
     * 字段: project_parent_type  TINYINT(3)
     * 默认值: 0
     */
    private Integer projectParentType;

    /**
     * 项目名称列表 “,” 分隔
     */
    private String projectNames;

    /**
     * 操作员姓名
     */
    private String modifyUserName;



    //【非数据库字段，查询时使用】
    /**
     * 审核状态
     */
    private Integer auditState;

    /**
     * 海外基金id
     */
    private Integer fundAgreementId;

    /**
     * 投资金额
     */
    private BigDecimal investAmount;


    /**
     * 年利率范围起始(%)
     */
    private Double minRate;

    /**
     * 年利率范围结束(%)
     */
    private Double maxRate;

    /**
     * 存息期限单位:1-年,2-月,3-日
     */
    private Integer termUnit;

    /**
     * 存息期限值
     */
    private Integer termValue;

    /**
     * 原因
     */
    private String reason;

    /**
     * 项目最低投资金额单位  1-元,2-美元
     */
    private Integer lowestAmountUnit;

    /**
     * 海外基金基金协议地址
     */
    private String contractUrl;


    public FundAdvisoryVo() {
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public Integer getLowestAmountUnit() {
        return lowestAmountUnit;
    }

    public void setLowestAmountUnit(Integer lowestAmountUnit) {
        this.lowestAmountUnit = lowestAmountUnit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public Integer getFundAgreementId() {
        return fundAgreementId;
    }

    public void setFundAgreementId(Integer fundAgreementId) {
        this.fundAgreementId = fundAgreementId;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Double getMinRate() {
        return minRate;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }

    public Double getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(Double maxRate) {
        this.maxRate = maxRate;
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

    public FundAdvisoryVo(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setInfoIds(java.lang.String infoIds) {
        this.infoIds = infoIds;
    }

    public java.lang.String getInfoIds() {
        return this.infoIds;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setTel(java.lang.String tel) {
        this.tel = tel;
    }

    public java.lang.String getTel() {
        return this.tel;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setAdvisor(Integer advisor) {
        this.advisor = advisor;
    }

    public Integer getAdvisor() {
        return this.advisor;
    }

    public void setAssignUserId(java.lang.Integer assignUserId) {
        this.assignUserId = assignUserId;
    }

    public java.lang.Integer getAssignUserId() {
        return this.assignUserId;
    }

    public void setAssignUserName(java.lang.String assignUserName) {
        this.assignUserName = assignUserName;
    }

    public java.lang.String getAssignUserName() {
        return this.assignUserName;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSource() {
        return this.source;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    public java.lang.String getRemark() {
        return this.remark;
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

    public void setRegUserId(java.lang.Integer regUserId) {
        this.regUserId = regUserId;
    }

    public java.lang.Integer getRegUserId() {
        return this.regUserId;
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

    public void setModifyUserId(java.lang.Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public java.lang.Integer getModifyUserId() {
        return this.modifyUserId;
    }

    public void setProjectParentType(Integer projectParentType) {
        this.projectParentType = projectParentType;
    }

    public Integer getProjectParentType() {
        return this.projectParentType;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    public void setProjectNames(String projectNames) {
        this.projectNames = projectNames;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public String getProjectNames() {
        return projectNames;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }
}
