package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.model.FinAccount.java
 * @Class Name : FinAccount.java
 * @Description : GENERATOR VO类
 * @Author : generator
 */
public class FinAccount extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id 字段: id INT UNSIGNED(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: 用户id 字段: reg_user_id INT UNSIGNED(10) 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 用户姓名 字段: user_name VARCHAR(30) 默认值: ''
	 */
	private java.lang.String userName;

	/**
	 * 描述: 账户类型:1-客户账户,2-平台收益账户,3-平台中转账户,4-担保机构 字段: account_type TINYINT(3) 默认值:
	 * 0
	 */
	private Integer accountType;

	/**
	 * 描述: 支付密码 MD5 字段: passwd VARCHAR(32) 默认值: ''
	 */
	private java.lang.String passwd;

	/**
	 * 描述: 当前虚拟账户余额(元) 字段: now_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal nowMoney;

	/**
	 * 描述: 冻结金额(元) 字段: freeze_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal freezeMoney;

	/**
	 * 描述: 可用余额(元) 字段: useable_money DECIMAL(20) 默认值: 0.00
	 */
	private java.math.BigDecimal useableMoney;

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

	// 【非数据库字段，查询时使用】
	private java.math.BigDecimal useableMoneyStart;

	// 【非数据库字段，查询时使用】
	private java.math.BigDecimal useableMoneyEnd;
	// 更新之前的金额，CAS用
	private BigDecimal beforeNowMoney;
	//总资产
	private BigDecimal totalAmount;

	public FinAccount() {
	}

	public FinAccount(java.lang.Integer id) {
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

	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	public java.lang.String getUserName() {
		return this.userName;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getAccountType() {
		return this.accountType;
	}

	public void setPasswd(java.lang.String passwd) {
		this.passwd = passwd;
	}

	public java.lang.String getPasswd() {
		return this.passwd;
	}

	public void setNowMoney(java.math.BigDecimal nowMoney) {
		this.nowMoney = nowMoney;
	}

	public java.math.BigDecimal getNowMoney() {
		return this.nowMoney;
	}

	public void setFreezeMoney(java.math.BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public java.math.BigDecimal getFreezeMoney() {
		return this.freezeMoney;
	}

	public void setUseableMoney(java.math.BigDecimal useableMoney) {
		this.useableMoney = useableMoney;
	}

	public java.math.BigDecimal getUseableMoney() {
		return this.useableMoney;
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

	public BigDecimal getUseableMoneyStart() {
		return useableMoneyStart;
	}

	public void setUseableMoneyStart(BigDecimal useableMoneyStart) {
		this.useableMoneyStart = useableMoneyStart;
	}

	public BigDecimal getUseableMoneyEnd() {
		return useableMoneyEnd;
	}

	public void setUseableMoneyEnd(BigDecimal useableMoneyEnd) {
		this.useableMoneyEnd = useableMoneyEnd;
	}

	public BigDecimal getBeforeNowMoney() {
		return beforeNowMoney;
	}

	public void setBeforeNowMoney(BigDecimal beforeNowMoney) {
		this.beforeNowMoney = beforeNowMoney;
	}

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
