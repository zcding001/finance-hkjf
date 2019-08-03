package com.hongkun.finance.monitor.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.monitor.model.MonitorOperateRecord.java
 * @Class Name    : MonitorOperateRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class MonitorOperateRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 操作类型（1有盾实名、2宝付实名、3充值异步通知）
	 * 字段: operate_type  INT(10)
	 */
	private java.lang.Integer operateType;
	
	/**
	 * 描述: 操作描述
	 * 字段: operate_desc  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String operateDesc;
	
	/**
	 * 描述: 结果状态（0成功，1不一致，2不存在，9异常）
	 * 字段: operate_state  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer operateState;
	
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
 
	public MonitorOperateRecord(){
	}

	public MonitorOperateRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setOperateType(java.lang.Integer operateType) {
		this.operateType = operateType;
	}
	
	public java.lang.Integer getOperateType() {
		return this.operateType;
	}
	
	public void setOperateDesc(java.lang.String operateDesc) {
		this.operateDesc = operateDesc;
	}
	
	public java.lang.String getOperateDesc() {
		return this.operateDesc;
	}
	
	public void setOperateState(java.lang.Integer operateState) {
		this.operateState = operateState;
	}
	
	public java.lang.Integer getOperateState() {
		return this.operateState;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

