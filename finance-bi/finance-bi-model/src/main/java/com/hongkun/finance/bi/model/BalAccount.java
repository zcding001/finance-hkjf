package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.BalAccount.java
 * @Class Name    : BalAccount.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BalAccount extends BaseModel {

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
	 * 描述: 用户姓名
	 * 字段: real_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String realName;

	/**
	 * 描述: 用户手机号
	 * 字段: tel  VARCHAR(11)
	 * 默认值: ''
	 */
	private java.lang.String tel;

	/**
	 * 描述: 活动收入(预留平账字段)
	 * 字段: acitve_fee  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal acitveFee;

	/**
	 * 描述: 入账总额
	 * 字段: in_money_sum  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal inMoneySum;

	/**
	 * 描述: 出账总额
	 * 字段: out_money_sum  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal outMoneySum;

	/**
	 * 描述: 入账积分总额（预留）
	 * 字段: in_point_sum  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer inPointSum;

	/**
	 * 描述: 出账积分总额（预留）
	 * 字段: out_point_sum  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer outPointSum;

	/**
	 * 描述: 账是否平:0不平、1平
	 * 字段: state  BIT(0)
	 */
	private java.lang.Integer state;

	/**
	 * 描述: 创建时间
	 * 字段: created_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createdTime;

	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modified_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifiedTime;

	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date modifiedTimeEnd;
	/**
	 * 描述: 冻结金额(元)
	 * 字段: freeze_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal freezeMoney;

	/**
	 * 描述: 可用金额(元)
	 * 字段: useable_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal useableMoney;

	/**
	 * 描述: 积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer point;


	public BalAccount(){
	}

	public BalAccount(java.lang.Integer id){
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

	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}

	public java.lang.String getRealName() {
		return this.realName;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}

	public java.lang.String getTel() {
		return this.tel;
	}

	public void setAcitveFee(java.math.BigDecimal acitveFee) {
		this.acitveFee = acitveFee;
	}

	public java.math.BigDecimal getAcitveFee() {
		return this.acitveFee;
	}

	public void setInMoneySum(java.math.BigDecimal inMoneySum) {
		this.inMoneySum = inMoneySum;
	}

	public java.math.BigDecimal getInMoneySum() {
		return this.inMoneySum;
	}

	public void setOutMoneySum(java.math.BigDecimal outMoneySum) {
		this.outMoneySum = outMoneySum;
	}

	public java.math.BigDecimal getOutMoneySum() {
		return this.outMoneySum;
	}

	public void setInPointSum(java.lang.Integer inPointSum) {
		this.inPointSum = inPointSum;
	}

	public java.lang.Integer getInPointSum() {
		return this.inPointSum;
	}

	public void setOutPointSum(java.lang.Integer outPointSum) {
		this.outPointSum = outPointSum;
	}

	public java.lang.Integer getOutPointSum() {
		return this.outPointSum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}

	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTimeBegin(java.util.Date createdTimeBegin) {
		this.createdTimeBegin = createdTimeBegin;
	}

	public java.util.Date getCreatedTimeBegin() {
		return this.createdTimeBegin;
	}

	public void setCreatedTimeEnd(java.util.Date createdTimeEnd) {
		this.createdTimeEnd = createdTimeEnd;
	}

	public java.util.Date getCreatedTimeEnd() {
		return this.createdTimeEnd;
	}
	public void setModifiedTime(java.util.Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public java.util.Date getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTimeBegin(java.util.Date modifiedTimeBegin) {
		this.modifiedTimeBegin = modifiedTimeBegin;
	}

	public java.util.Date getModifiedTimeBegin() {
		return this.modifiedTimeBegin;
	}

	public void setModifiedTimeEnd(java.util.Date modifiedTimeEnd) {
		this.modifiedTimeEnd = modifiedTimeEnd;
	}

	public java.util.Date getModifiedTimeEnd() {
		return this.modifiedTimeEnd;
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

	public void setPoint(java.lang.Integer point) {
		this.point = point;
	}

	public java.lang.Integer getPoint() {
		return this.point;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

