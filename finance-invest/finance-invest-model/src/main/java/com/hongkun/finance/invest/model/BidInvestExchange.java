package com.hongkun.finance.invest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance_hkjf
 * @Program Name  : com.hongkun.finance.invest.model.BidInvestExchange.java
 * @Class Name    : BidInvestExchange.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidInvestExchange extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: ID  INT(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述: investId
	 * 字段: INVEST_ID  INT(10)
	 */
	private java.lang.Integer investId;

	/**
	 * 描述: bidId
	 * 字段: BID_ID  INT(10)
	 */
	private java.lang.Integer bidId;

	/**
	 * 描述: investUserId
	 * 字段: INVEST_USER_ID  VARCHAR(11)
	 */
	private java.lang.Integer investUserId;

	/**
	 * 描述: investUserName
	 * 字段: INVEST_USER_NAME  VARCHAR(50)
	 */
	private java.lang.String investUserName;

	/**
	 * 描述: investShowName
	 * 字段: INVEST_SHOW_NAME  VARCHAR(50)
	 */
	private java.lang.String investShowName;

	/**
	 * 描述: 投资金额
	 * 字段: INVEST_ATM  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAtm;

	/**
	 * 描述: 字典【0初始化1成功】
	 * 字段: STATE  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: createdTime
	 * 字段: CREATED_TIME  DATETIME(19)
	 */
	private java.util.Date createdTime;

	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date createdTimeEnd;

	private String tel;

	private String realName;

	public BidInvestExchange(){
	}

	public BidInvestExchange(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setInvestId(java.lang.Integer investId) {
		this.investId = investId;
	}

	public java.lang.Integer getInvestId() {
		return this.investId;
	}

	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}

	public java.lang.Integer getBidId() {
		return this.bidId;
	}

	public Integer getInvestUserId() {
		return investUserId;
	}

	public void setInvestUserId(Integer investUserId) {
		this.investUserId = investUserId;
	}

	public void setInvestUserName(java.lang.String investUserName) {
		this.investUserName = investUserName;
	}

	public java.lang.String getInvestUserName() {
		return this.investUserName;
	}

	public void setInvestShowName(java.lang.String investShowName) {
		this.investShowName = investShowName;
	}

	public java.lang.String getInvestShowName() {
		return this.investShowName;
	}

	public void setInvestAtm(java.math.BigDecimal investAtm) {
		this.investAtm = investAtm;
	}

	public java.math.BigDecimal getInvestAtm() {
		return this.investAtm;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return this.state;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

