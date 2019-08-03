package com.hongkun.finance.activity.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : LotteryItem.java
 * @Class Name    : LotteryItem.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class LotteryItem extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: ID
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
	 * 描述: 奖品名称
	 * 字段: item_name  VARCHAR(255)
	 * 默认值: ''
	 */
	private java.lang.String itemName;
	
	/**
	 * 描述: 奖品类型: 1-红包,2-积分,3-投资红包,4-加息券,5-兑换码物品,6-其他奖品,7-谢谢参与
	 * 字段: item_type  TINYINT(3)
	 * 默认值: 5
	 */
	private Integer itemType;
	
	/**
	 * 描述: 抽奖概率
	 * 字段: item_rate  DOUBLE(20)
	 * 默认值: 0.000
	 */
	private java.lang.Double itemRate;
	
	/**
	 * 描述: 状态: 0-删除，1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 奖品面值
	 * 字段: amount_atm  DOUBLE(20)
	 * 默认值: 0.000
	 */
	private java.lang.Double amountAtm;
	
	/**
	 * 描述: 奖品数量
	 * 字段: item_count  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer itemCount;
	
	/**
	 * 描述: 奖品排序号
	 * 字段: sequence_num  INT(10)
	 * 默认值: 1
	 */
	private java.lang.Integer sequenceNum;
	
	/**
	 * 描述: 是否为京籍：0-全部，1-是，2-否
	 * 字段: location_flag  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer locationFlag;
	
	/**
	 * 描述: 用户群组：1-新用户, 2-老用户, 3-男用户, 4-女用户, 5-已投资用户, 6-未投资用户
	 * 字段: item_group  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer itemGroup;
	
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
	
 
	public LotteryItem(){
	}

	public LotteryItem(java.lang.Integer id){
		this.id = id;
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
	
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}
	
	public java.lang.String getItemName() {
		return this.itemName;
	}
	
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public Integer getItemType() {
		return this.itemType;
	}
	
	public void setItemRate(java.lang.Double itemRate) {
		this.itemRate = itemRate;
	}
	
	public java.lang.Double getItemRate() {
		return this.itemRate;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setAmountAtm(java.lang.Double amountAtm) {
		this.amountAtm = amountAtm;
	}
	
	public java.lang.Double getAmountAtm() {
		return this.amountAtm;
	}
	
	public void setItemCount(java.lang.Integer itemCount) {
		this.itemCount = itemCount;
	}
	
	public java.lang.Integer getItemCount() {
		return this.itemCount;
	}
	
	public void setSequenceNum(java.lang.Integer sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
	
	public java.lang.Integer getSequenceNum() {
		return this.sequenceNum;
	}
	
	public void setLocationFlag(java.lang.Integer locationFlag) {
		this.locationFlag = locationFlag;
	}
	
	public java.lang.Integer getLocationFlag() {
		return this.locationFlag;
	}
	
	public void setItemGroup(Integer itemGroup) {
		this.itemGroup = itemGroup;
	}
	
	public Integer getItemGroup() {
		return this.itemGroup;
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

