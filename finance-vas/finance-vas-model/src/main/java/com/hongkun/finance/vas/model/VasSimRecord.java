package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.VasSimRecord.java
 * @Class Name : VasSimRecord.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class VasSimRecord extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 用户id 字段: reg_user_id INT(10)
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 金额（元） 字段: money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal money;

	/**
	 * 描述: 过期时间 字段: expire_time DATETIME(19) 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date expireTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date expireTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date expireTimeEnd;
	/**
	 * 描述: 来源 字段: source TINYINT(3) 默认值: 0
	 */
	private Integer source;

	/**
	 * 描述: 状态:0-默认未使用，1-已使用，2-过期，3-失效 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间 字段: modify_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;

	// 【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;

	public VasSimRecord() {
	}

	public VasSimRecord(java.lang.Integer id) {
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

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public java.math.BigDecimal getMoney() {
		return this.money;
	}

	public void setExpireTime(java.util.Date expireTime) {
		this.expireTime = expireTime;
	}

	public java.util.Date getExpireTime() {
		return this.expireTime;
	}

	public void setExpireTimeBegin(java.util.Date expireTimeBegin) {
		this.expireTimeBegin = expireTimeBegin;
	}

	public java.util.Date getExpireTimeBegin() {
		return this.expireTimeBegin;
	}

	public void setExpireTimeEnd(java.util.Date expireTimeEnd) {
		this.expireTimeEnd = expireTimeEnd;
	}

	public java.util.Date getExpireTimeEnd() {
		return this.expireTimeEnd;
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
