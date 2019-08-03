package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinBankCardBinding.java
 * @Class Name    : FinBankCardBinding.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinBankCardBinding extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 银行卡id
	 * 字段: fin_bank_card_id  INT UNSIGNED(10)
	 * 默认值: 0
	 */
	private java.lang.Integer finBankCardId;
	
	/**
	 * 描述: 第三方银行编码
	 * 字段: bank_third_code  VARCHAR(20)
	 */
	private java.lang.String bankThirdCode;
	
	/**
	 * 描述: 第三方协议号
	 * 字段: third_account  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String thirdAccount;
	
	/**
	 * 描述: 支付渠道
	 * 字段: pay_channel  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer payChannel;
	
	/**
	 * 描述: 状态:0-初始化,1-绑卡未认证,2-已认证,3-未认证禁用,4-认证禁用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
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
 
	public FinBankCardBinding(){
	}

	public FinBankCardBinding(java.lang.Integer id){
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
	
	public void setFinBankCardId(java.lang.Integer finBankCardId) {
		this.finBankCardId = finBankCardId;
	}
	
	public java.lang.Integer getFinBankCardId() {
		return this.finBankCardId;
	}
	
	public void setBankThirdCode(java.lang.String bankThirdCode) {
		this.bankThirdCode = bankThirdCode;
	}
	
	public java.lang.String getBankThirdCode() {
		return this.bankThirdCode;
	}
	
	public void setThirdAccount(java.lang.String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}
	
	public java.lang.String getThirdAccount() {
		return this.thirdAccount;
	}
	
	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}
	
	public Integer getPayChannel() {
		return this.payChannel;
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

