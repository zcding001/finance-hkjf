package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.model.VasVipGrowRecord.java
 * @Class Name    : VasVipGrowRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasVipGrowRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 成长类型：1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠，9-平台赠送，10-会员降级
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 成长值
	 * 字段: grow_value  INT(10)
	 * 默认值: 0
	 */
	private Integer growValue;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 计算前成长值
	 * 字段: pre_grow_value  INT(10)
	 * 默认值: 0
	 */
	private Integer preGrowValue;
	
	/**
	 * 描述: 计算后成长值
	 * 字段: current_grow_value  INT(10)
	 * 默认值: 0
	 */
	private Integer currentGrowValue;
	
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(100)
	 * 默认值: ''
	 */
	private String note;
	
	/**
	 * 描述: 启用状态：0-无效，1-有效
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
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
 
	public VasVipGrowRecord(){
	}

	public VasVipGrowRecord(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setGrowValue(Integer growValue) {
		this.growValue = growValue;
	}
	
	public Integer getGrowValue() {
		return this.growValue;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setPreGrowValue(Integer preGrowValue) {
		this.preGrowValue = preGrowValue;
	}
	
	public Integer getPreGrowValue() {
		return this.preGrowValue;
	}
	
	public void setCurrentGrowValue(Integer currentGrowValue) {
		this.currentGrowValue = currentGrowValue;
	}
	
	public Integer getCurrentGrowValue() {
		return this.currentGrowValue;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNote() {
		return this.note;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

