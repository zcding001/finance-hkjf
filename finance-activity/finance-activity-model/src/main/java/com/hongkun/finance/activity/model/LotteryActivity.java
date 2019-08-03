package com.hongkun.finance.activity.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : LotteryActivity.java
 * @Class Name    : LotteryActivity.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class LotteryActivity extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: ID
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 活动名称
	 * 字段: name  VARCHAR(255)
	 * 默认值: ''
	 */
	private java.lang.String name;
	
	/**
	 * 描述: 状态: 0-禁用，1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 活动开始时间
	 * 字段: start_time  DATETIME(19)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTimeEnd;
	/**
	 * 描述: 活动结束时间
	 * 字段: end_time  DATETIME(19)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 转盘图片url
	 * 字段: items_url  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String itemsUrl;
	
	/**
	 * 描述: 背景图片url
	 * 字段: background_url  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String backgroundUrl;
	
	/**
	 * 描述: 转盘指针图片url
	 * 字段: pointer_url  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String pointerUrl;
	
	/**
	 * 描述: 每人限制抽奖次数
	 * 字段: limit_user_times  BIGINT(19)
	 * 默认值: 1
	 */
	private java.lang.Long limitUserTimes;
	
	/**
	 * 描述: 每日限制抽奖次数
	 * 字段: limit_day_times  BIGINT(19)
	 * 默认值: 1
	 */
	private java.lang.Long limitDayTimes;
	
	/**
	 * 描述: 活动规则
	 * 字段: activity_rule  VARCHAR(800)
	 * 默认值: ''
	 */
	private java.lang.String activityRule;
	
	/**
	 * 描述: 兑奖说明
	 * 字段: introduction  VARCHAR(800)
	 * 默认值: ''
	 */
	private java.lang.String introduction;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP(3)
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 创建人ID
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String note;

	public LotteryActivity(){
	}

	public LotteryActivity(java.lang.Integer id){
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
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTimeBegin(java.util.Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}
	
	public java.util.Date getStartTimeBegin() {
		return this.startTimeBegin;
	}
	
	public void setStartTimeEnd(java.util.Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}
	
	public java.util.Date getStartTimeEnd() {
		return this.startTimeEnd;
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
	public void setItemsUrl(java.lang.String itemsUrl) {
		this.itemsUrl = itemsUrl;
	}
	
	public java.lang.String getItemsUrl() {
		return this.itemsUrl;
	}
	
	public void setBackgroundUrl(java.lang.String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	
	public java.lang.String getBackgroundUrl() {
		return this.backgroundUrl;
	}
	
	public void setPointerUrl(java.lang.String pointerUrl) {
		this.pointerUrl = pointerUrl;
	}
	
	public java.lang.String getPointerUrl() {
		return this.pointerUrl;
	}
	
	public void setLimitUserTimes(java.lang.Long limitUserTimes) {
		this.limitUserTimes = limitUserTimes;
	}
	
	public java.lang.Long getLimitUserTimes() {
		return this.limitUserTimes;
	}
	
	public void setLimitDayTimes(java.lang.Long limitDayTimes) {
		this.limitDayTimes = limitDayTimes;
	}
	
	public java.lang.Long getLimitDayTimes() {
		return this.limitDayTimes;
	}
	
	public void setActivityRule(java.lang.String activityRule) {
		this.activityRule = activityRule;
	}
	
	public java.lang.String getActivityRule() {
		return this.activityRule;
	}
	
	public void setIntroduction(java.lang.String introduction) {
		this.introduction = introduction;
	}
	
	public java.lang.String getIntroduction() {
		return this.introduction;
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
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setNote(java.lang.String note) {
		this.note = note;
	}
	
	public java.lang.String getNote() {
		return this.note;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

