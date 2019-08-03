package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinBankCardUpdate.java
 * @Class Name    : FinBankCardUpdate.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinBankCardUpdate extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 申请人ID
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 申请人姓名
	 * 字段: real_name  VARCHAR(100)
	 * 默认值: ''
	 */
	private String realName;
	
	/**
	 * 描述: 申请人手机号
	 * 字段: tel  BIGINT UNSIGNED(20)
	 * 默认值: 0
	 */
	private Long tel;
	
	/**
	 * 描述: 银行名称
	 * 字段: bank_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String bankName;
	
	/**
	 * 描述: 银行卡号
	 * 字段: bank_card  VARCHAR(30)
	 * 默认值: ''
	 */
	private String bankCard;
	
	/**
	 * 描述: 状态:0 待审核，1审核通过，2审核拒绝
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 修改人姓名
	 * 字段: modify_user_name  VARCHAR(100)
	 * 默认值: ''
	 */
	private String modifyUserName;
	
	/**
	 * 描述: 本人身份证正面
	 * 字段: card_up  VARCHAR(200)
	 * 默认值: ''
	 */
	private String cardUp;
	
	/**
	 * 描述: 本人身份证反面
	 * 字段: card_down  VARCHAR(200)
	 * 默认值: ''
	 */
	private String cardDown;
	
	/**
	 * 描述: 手持身份证半身照正面
	 * 字段: holding_card_up  VARCHAR(200)
	 * 默认值: ''
	 */
	private String holdingCardUp;
	
	/**
	 * 描述: 手持身份证半身照反面
	 * 字段: holding_card_down  VARCHAR(200)
	 * 默认值: ''
	 */
	private String holdingCardDown;
	
	/**
	 * 描述: 户口本照片（本人页）
	 * 字段: household_register  VARCHAR(200)
	 * 默认值: ''
	 */
	private String householdRegister;
	
	/**
	 * 描述: 修改用户id
	 * 字段: modify_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer modifyUserId;
	
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
	/**
	 * 描述: 解绑原因
	 * 字段: reason  VARCHAR(200)
	 * 默认值: ''
	 */
	private String reason;
	
	/**
	 * 描述: 备注
	 * 字段: remark  VARCHAR(200)
	 * 默认值: ''
	 */
	private String remark;
	
 
	public FinBankCardUpdate(){
	}

	public FinBankCardUpdate(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getRealName() {
		return this.realName;
	}
	
	public void setTel(Long tel) {
		this.tel = tel;
	}
	
	public Long getTel() {
		return this.tel;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	
	public String getBankCard() {
		return this.bankCard;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	
	public String getModifyUserName() {
		return this.modifyUserName;
	}
	
	public void setCardUp(String cardUp) {
		this.cardUp = cardUp;
	}
	
	public String getCardUp() {
		return this.cardUp;
	}
	
	public void setCardDown(String cardDown) {
		this.cardDown = cardDown;
	}
	
	public String getCardDown() {
		return this.cardDown;
	}
	
	public void setHoldingCardUp(String holdingCardUp) {
		this.holdingCardUp = holdingCardUp;
	}
	
	public String getHoldingCardUp() {
		return this.holdingCardUp;
	}
	
	public void setHoldingCardDown(String holdingCardDown) {
		this.holdingCardDown = holdingCardDown;
	}
	
	public String getHoldingCardDown() {
		return this.holdingCardDown;
	}
	
	public void setHouseholdRegister(String householdRegister) {
		this.householdRegister = householdRegister;
	}
	
	public String getHouseholdRegister() {
		return this.householdRegister;
	}
	
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public Integer getModifyUserId() {
		return this.modifyUserId;
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
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

