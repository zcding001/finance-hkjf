package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.model.VasRebatesRuleChild.java
 * @Class Name    : VasRebatesRuleChild.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasRebatesRuleChild extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 规则id
	 * 字段: vas_rebates_rule_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer vasRebatesRuleId;
	
	/**
	 * 描述: 规则内容
	 * 字段: content  VARCHAR(2000)
	 */
	private java.lang.String content;
	
	/**
	 * 描述: 转换周期
	 * 字段: switch_cycle  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer switchCycle;
	
	/**
	 * 描述: 类型:0-普通用户，1-物业员工,2-销售员工，3-内部员工
	 * 字段: user_type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer userType;
	
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
 
	public VasRebatesRuleChild(){
	}

	public VasRebatesRuleChild(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setVasRebatesRuleId(java.lang.Integer vasRebatesRuleId) {
		this.vasRebatesRuleId = vasRebatesRuleId;
	}
	
	public java.lang.Integer getVasRebatesRuleId() {
		return this.vasRebatesRuleId;
	}
	
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	
	public java.lang.String getContent() {
		return this.content;
	}
	
	public void setSwitchCycle(java.lang.Integer switchCycle) {
		this.switchCycle = switchCycle;
	}
	
	public java.lang.Integer getSwitchCycle() {
		return this.switchCycle;
	}
	
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	public Integer getUserType() {
		return this.userType;
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

