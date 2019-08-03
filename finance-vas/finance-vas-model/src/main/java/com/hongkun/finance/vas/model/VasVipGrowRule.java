package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.model.VasVipGrowRule.java
 * @Class Name    : VasVipGrowRule.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasVipGrowRule extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 获取成长值渠道类型:1-投资，2-邀请好友注册，3-签到，4-首次充值，5-积分兑换，6-邀请好友投资，7-积分支付，8-积分转赠
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;

	/**
	 * 描述: 成长值，为具体值或计算成长值公式
	 * 字段: grow_value  VARCHAR(100)
	 * 默认值: '0'
	 */
	private java.lang.String growValue;

	/**
	 * 描述: 0-不启用公式，1-启用公式
	 * 字段: formula_enable  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer formulaEnable;

	/**
	 * 描述: 启用状态：0-不启用，1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 适用用户注册开始时间
	 * 字段: regist_begin_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date registBeginTime;

	//【非数据库字段，查询时使用】
	private java.util.Date registBeginTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date registBeginTimeEnd;
	/**
	 * 描述: 适用用户注册结束时间
	 * 字段: regist_end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date registEndTime;

	//【非数据库字段，查询时使用】
	private java.util.Date registEndTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date registEndTimeEnd;
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

	public VasVipGrowRule(){
	}

	public VasVipGrowRule(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}

	public void setGrowValue(java.lang.String growValue) {
		this.growValue = growValue;
	}

	public java.lang.String getGrowValue() {
		return this.growValue;
	}

	public void setFormulaEnable(Integer formulaEnable) {
		this.formulaEnable = formulaEnable;
	}

	public Integer getFormulaEnable() {
		return this.formulaEnable;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return this.state;
	}

	public void setRegistBeginTime(java.util.Date registBeginTime) {
		this.registBeginTime = registBeginTime;
	}

	public java.util.Date getRegistBeginTime() {
		return this.registBeginTime;
	}

	public void setRegistBeginTimeBegin(java.util.Date registBeginTimeBegin) {
		this.registBeginTimeBegin = registBeginTimeBegin;
	}

	public java.util.Date getRegistBeginTimeBegin() {
		return this.registBeginTimeBegin;
	}

	public void setRegistBeginTimeEnd(java.util.Date registBeginTimeEnd) {
		this.registBeginTimeEnd = registBeginTimeEnd;
	}

	public java.util.Date getRegistBeginTimeEnd() {
		return this.registBeginTimeEnd;
	}
	public void setRegistEndTime(java.util.Date registEndTime) {
		this.registEndTime = registEndTime;
	}

	public java.util.Date getRegistEndTime() {
		return this.registEndTime;
	}

	public void setRegistEndTimeBegin(java.util.Date registEndTimeBegin) {
		this.registEndTimeBegin = registEndTimeBegin;
	}

	public java.util.Date getRegistEndTimeBegin() {
		return this.registEndTimeBegin;
	}

	public void setRegistEndTimeEnd(java.util.Date registEndTimeEnd) {
		this.registEndTimeEnd = registEndTimeEnd;
	}

	public java.util.Date getRegistEndTimeEnd() {
		return this.registEndTimeEnd;
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