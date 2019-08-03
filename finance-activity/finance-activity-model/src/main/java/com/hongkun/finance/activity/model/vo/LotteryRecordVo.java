package com.hongkun.finance.activity.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 活动抽奖记录Vo
 * @Program: com.hongkun.finance.activity.model.vo.LotteryRecordVo
 * @Author: yunlongliu@hongkun.com.cn
 * @Date: 2018-10-15 11:42
 **/
public class LotteryRecordVo extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 描述: id
     * 字段: id  INT(10)
     */
    private java.lang.Integer id;

    /**
     * 描述: 活动id
     * 字段: lottery_activity_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer lotteryActivityId;

    /**
     * 描述: 奖品id
     * 字段: lottery_item_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer lotteryItemId;

    /**
     * 描述: 状态: 0-初始化，1-已兑换
     * 字段: state  TINYINT(3)
     * 默认值: 0
     */
    private Integer state;

    /**
     * 描述: 抽奖手机号
     * 字段: tel  BIGINT UNSIGNED(20)
     * 默认值: 0
     */
    private java.lang.Long tel;

    /**
     * 描述: 中奖用户ID
     * 字段: reg_user_id  INT(10)
     * 默认值: 0
     */
    private java.lang.Integer regUserId;

    /**
     * 描述: 用户姓名
     * 字段: user_name  VARCHAR(50)
     * 默认值: ''
     */
    private java.lang.String userName;

    /**
     * 描述: 兑换码
     * 字段: verfication  VARCHAR(20)
     * 默认值: ''
     */
    private java.lang.String verfication;

    /**
     * 描述: 创建时间
     * 字段: create_time  DATETIME(19)
     * 默认值: CURRENT_TIMESTAMP(3)
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
    /**
     * 描述: 来源
     * 字段: source  VARCHAR(50)
     * 默认值: ''
     */
    private java.lang.String source;


    // 活动及奖品相关字段
    private String name;
    private Date startTime;
    private Date endTime;
    private String activityRule;
    private String introduction;
    private String itemName;
    private Integer itemType;
    private Double itemRate;


    public LotteryRecordVo(){
    }

    public LotteryRecordVo(java.lang.Integer id){
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Double getItemRate() {
        return itemRate;
    }

    public void setItemRate(Double itemRate) {
        this.itemRate = itemRate;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setLotteryActivityId(java.lang.Integer lotteryActivityId) {
        this.lotteryActivityId = lotteryActivityId;
    }

    public java.lang.Integer getLotteryActivityId() {
        return this.lotteryActivityId;
    }

    public void setLotteryItemId(java.lang.Integer lotteryItemId) {
        this.lotteryItemId = lotteryItemId;
    }

    public java.lang.Integer getLotteryItemId() {
        return this.lotteryItemId;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public void setTel(java.lang.Long tel) {
        this.tel = tel;
    }

    public java.lang.Long getTel() {
        return this.tel;
    }

    public void setRegUserId(java.lang.Integer regUserId) {
        this.regUserId = regUserId;
    }

    public java.lang.Integer getRegUserId() {
        return this.regUserId;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public java.lang.String getUserName() {
        return this.userName;
    }

    public void setVerfication(java.lang.String verfication) {
        this.verfication = verfication;
    }

    public java.lang.String getVerfication() {
        return this.verfication;
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
    public void setSource(java.lang.String source) {
        this.source = source;
    }

    public java.lang.String getSource() {
        return this.source;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
