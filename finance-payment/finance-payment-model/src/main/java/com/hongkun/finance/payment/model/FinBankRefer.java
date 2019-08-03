package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinBankRefer.java
 * @Class Name : FinBankRefer.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinBankRefer extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 平台银行CODE 字段: bank_code VARCHAR(20) 默认值: ''
	 */
	private java.lang.String bankCode;

	/**
	 * 描述: 渠道名称 字段: third_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String thirdName;

	/**
	 * 描述: 渠道编码 字段: third_code VARCHAR(20) 默认值: ''
	 */
	private java.lang.String thirdCode;

	/**
	 * 描述: 第三方银行名称 字段: bank_third_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String bankThirdName;

	/**
	 * 描述: 第三方银行编码 字段: bank_third_code VARCHAR(20) 默认值: ''
	 */
	private java.lang.String bankThirdCode;

	/**
	 * 描述: 支付方式编码(KJ、WY、RZ、SSFK) 字段: payway_codes VARCHAR(20) 默认值: ''
	 */
	private java.lang.String paywayCodes;

	/**
	 * 描述: 注册类型:1-一般用户,2-企业 字段: reg_user_type VARCHAR(20) 默认值: ''
	 */
	private java.lang.String regUserType;

	/**
	 * 描述: 单笔限额 字段: single_limit DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal singleLimit;

	/**
	 * 描述: 单日限额 字段: single_day_limit DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal singleDayLimit;

	/**
	 * 描述: 单月限额 字段: single_month_limit DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal singleMonthLimit;

	/**
	 * 描述: 银行图标地址 字段: bank_icon_address VARCHAR(50)
	 */
	private java.lang.String bankIconAddress;

	/**
	 * 描述: 状态:0-删除,1-正常 字段: state TINYINT(3) 默认值: 1
	 */
	private Integer state;

	/**
	 * 描述: 创建时间 字段: create_time DATETIME(19) 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;

	// 【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;

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
	/**
	 * 分组字段
	 */
	private String groupColumns;

	public FinBankRefer() {
	}

	public FinBankRefer(java.lang.Integer id) {
		this.id = id;
	}

	public String getGroupColumns() {
		return groupColumns;
	}

	public void setGroupColumns(String groupColumns) {
		this.groupColumns = groupColumns;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public java.lang.String getBankCode() {
		return this.bankCode;
	}

	public void setThirdName(java.lang.String thirdName) {
		this.thirdName = thirdName;
	}

	public java.lang.String getThirdName() {
		return this.thirdName;
	}

	public void setThirdCode(java.lang.String thirdCode) {
		this.thirdCode = thirdCode;
	}

	public java.lang.String getThirdCode() {
		return this.thirdCode;
	}

	public void setBankThirdName(java.lang.String bankThirdName) {
		this.bankThirdName = bankThirdName;
	}

	public java.lang.String getBankThirdName() {
		return this.bankThirdName;
	}

	public void setBankThirdCode(java.lang.String bankThirdCode) {
		this.bankThirdCode = bankThirdCode;
	}

	public java.lang.String getBankThirdCode() {
		return this.bankThirdCode;
	}

	public void setPaywayCodes(java.lang.String paywayCodes) {
		this.paywayCodes = paywayCodes;
	}

	public java.lang.String getPaywayCodes() {
		return this.paywayCodes;
	}

	public void setRegUserType(java.lang.String regUserType) {
		this.regUserType = regUserType;
	}

	public java.lang.String getRegUserType() {
		return this.regUserType;
	}

	public void setSingleLimit(java.math.BigDecimal singleLimit) {
		this.singleLimit = singleLimit;
	}

	public java.math.BigDecimal getSingleLimit() {
		return this.singleLimit;
	}

	public void setSingleDayLimit(java.math.BigDecimal singleDayLimit) {
		this.singleDayLimit = singleDayLimit;
	}

	public java.math.BigDecimal getSingleDayLimit() {
		return this.singleDayLimit;
	}

	public void setSingleMonthLimit(java.math.BigDecimal singleMonthLimit) {
		this.singleMonthLimit = singleMonthLimit;
	}

	public java.math.BigDecimal getSingleMonthLimit() {
		return this.singleMonthLimit;
	}

	public void setBankIconAddress(java.lang.String bankIconAddress) {
		this.bankIconAddress = bankIconAddress;
	}

	public java.lang.String getBankIconAddress() {
		return this.bankIconAddress;
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
