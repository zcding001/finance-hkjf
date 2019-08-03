package com.hongkun.finance.point.model.vo;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : 积分模块通用VO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.point.model.vo.PointVO
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class PointVO extends BaseModel {

    /*****************字段来源于PointAccount****************************/
    /** 描述: 用户id 默认值: 0*/
    private Integer regUserId;
    /**描述: 积分值 默认值: 0*/
    @NotNull(message = "请填写积分数量",groups = {ValidateGivePoint.class})
    private Integer point;
    /** 描述:账户状态状态:0-删除,1-正常 默认值: 1*/
    private Integer accountState;

    /*****************字段来源于PointRecord****************************/

    /** 描述: 来源：投资对应的投资记录的id，积分兑换对应的兑换记录id，转赠收入对应发送人id，转赠支出对应接收人id，积分支付对应商户id，物业抵扣对应物业账户id 默认值: 0*/
    private Integer businessId;
    /** 描述: 手续费（积分）* 默认值: 0*/
    private Integer fee;
    /** 描述: 手续费价值（人民币）* 默认值: 0.00*/
    private java.math.BigDecimal feeWorth;
    /** 描述: 积分价值（人民币） 默认值: 0.00*/
    private java.math.BigDecimal worth;
    /** 描述: 积分实际价值（人民币） 默认值: 0.00*/
    private java.math.BigDecimal realWorth;
    /**描述: 交易信息描述 */
    @NotEmpty(message = "请填写积分原因",groups = {ValidateGivePoint.class})
    private String comments;
    /**描述:积分记录状态：0-已确认，1-待审核，2-审核失败* 默认值: 0*/
    @NotNull(message = "请选择要更新的记录状态",groups = {ValidateCheckPoint.class})
    private Integer recordState;
    /**描述: 拒绝原因* 默认值: ''*/
    private String refuseCause;
    /**描述: 兑换平台：1-鸿坤金服，2-前生活* 默认值: 0*/
    private Integer platform;
    /** 描述: 类型：1-投资，2-平台赠送，3-积分兑换，4-转赠收入，5-转赠支出，6-积分支付，7-签到，8-活动赠送，9-物业抵扣(积分来源) */
    private Integer type;

    /***************************其他字段*************************************/
    /**账户id*/
    private java.lang.Integer accountId;
    /**积分记录id*/
    private java.lang.Integer recordId;
    /**操作员姓名*/
    private String opratorName;
    /**用户名*/
    @Union(forLimitQuery = true)
    private java.lang.String realName;
    /** 手机号 */
    @Union(forLimitQuery = true)
    private java.lang.Long login;
    /** 描述: 积分值开始字段*/
    private Integer pointStart;
    /** 描述: 积分值结束字段*/
    private Integer pointEnd;
    /**账户查询限制ids*/
    private List<Integer> limitUserIds = new ArrayList<>();
    /**业务查询限制ids*/
    private List<Integer> limitBusinessIds = new ArrayList<>();
    /**用户其他用途id使用*/
    @NotNull(message = "请至少选择一个用户",groups = {ValidateGivePoint.class})
    private List<Integer> userIds;

    /**审核赠送积分记录id*/
    @NotEmpty(message = "请至少选择一个用户",groups = {ValidateCheckPoint.class})
    private List<Integer> recordIds;

    /** 描述: 创建时间*/
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    //【非数据库字段，查询时使用】
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeBegin;

    //【非数据库字段，查询时使用】
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTimeEnd;
    /** 描述: 修改时间* */
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    //【非数据库字段，查询时使用】
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTimeBegin;

    //【非数据库字段，查询时使用】
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTimeEnd;

    /**用于页面显示的积分来源*/
    private String pointSource;

    /**商户名*/
    @Union(forLimitQuery = true)
    private java.lang.String merchantName;
    /** 商户编号 */
    @Union(forLimitQuery = true)
    private java.lang.String merchantCode;
    //积分类型集合
    private List<Integer> typeList;
    //消费地点
    private String address;

    /**验证赠送积分分组标识*/
    public interface ValidateGivePoint {}
    /**验证审核积分分组标识*/
    public interface ValidateCheckPoint {}


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Integer> typeList) {
        this.typeList = typeList;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(Integer regUserId) {
        this.regUserId = regUserId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(Integer accountState) {
        this.accountState = accountState;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public BigDecimal getFeeWorth() {
        return feeWorth;
    }

    public void setFeeWorth(BigDecimal feeWorth) {
        this.feeWorth = feeWorth;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    public BigDecimal getRealWorth() {
        return realWorth;
    }

    public void setRealWorth(BigDecimal realWorth) {
        this.realWorth = realWorth;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getRecordState() {
        return recordState;
    }

    public void setRecordState(Integer recordState) {
        this.recordState = recordState;
    }

    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOpratorName() {
        return opratorName;
    }

    public void setOpratorName(String opratorName) {
        this.opratorName = opratorName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public Integer getPointStart() {
        return pointStart;
    }

    public void setPointStart(Integer pointStart) {
        this.pointStart = pointStart;
    }

    public Integer getPointEnd() {
        return pointEnd;
    }

    public void setPointEnd(Integer pointEnd) {
        this.pointEnd = pointEnd;
    }

    public List<Integer> getLimitUserIds() {
        return limitUserIds;
    }

    public void setLimitUserIds(List<Integer> limitUserIds) {
        this.limitUserIds = limitUserIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTimeBegin() {
        return modifyTimeBegin;
    }

    public void setModifyTimeBegin(Date modifyTimeBegin) {
        this.modifyTimeBegin = modifyTimeBegin;
    }

    public Date getModifyTimeEnd() {
        return modifyTimeEnd;
    }

    public void setModifyTimeEnd(Date modifyTimeEnd) {
        this.modifyTimeEnd = modifyTimeEnd;
    }

    public String getPointSource() {
        return pointSource;
    }

    public void setPointSource(String pointSource) {
        this.pointSource = pointSource;
    }

    public List<Integer> getRecordIds() {
        return recordIds;
    }

    public void setRecordIds(List<Integer> recordIds) {
        this.recordIds = recordIds;
    }

    public List<Integer> getLimitBusinessIds() {
        return limitBusinessIds;
    }

    public void setLimitBusinessIds(List<Integer> limitBusinessIds) {
        this.limitBusinessIds = limitBusinessIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}


