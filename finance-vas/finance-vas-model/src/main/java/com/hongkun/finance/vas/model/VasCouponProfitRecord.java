package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.vas.model.VasCouponProfitRecord.java
 * @Class Name    : VasCouponProfitRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasCouponProfitRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户ID
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 用户名称
	 * 字段: reg_user_detail_real_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String regUserDetailRealName;
	
	/**
	 * 描述: 用户电话
	 * 字段: reg_user_login  BIGINT(19)
	 * 默认值: 0
	 */
	private Long regUserLogin;
	
	/**
	 * 描述: 标的ID
	 * 字段: bid_info_id  INT(10)
	 * 默认值: 0
	 */
	private Integer bidInfoId;
	
	/**
	 * 描述: 标的名称
	 * 字段: bid_info_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String bidInfoName;
	
	/**
	 * 描述: 期限值
	 * 字段: bid_info_term_value  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer bidInfoTermValue;
	
	/**
	 * 描述: 期限单位:1-年,2-月,3-天
	 * 字段: bid_info_term_unit  TINYINT(3)
	 * 默认值: 2
	 */
	private Integer bidInfoTermUnit;
	
	/**
	 * 描述: 投资金额
	 * 字段: bid_invest_invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal bidInvestInvestAmount;
	
	/**
	 * 描述: 回款计划ID
	 * 字段: bid_receipt_plan_id  INT(10)
	 * 默认值: 0
	 */
	private Integer bidReceiptPlanId;
	
	/**
	 * 描述: 计划回款时间
	 * 字段: bid_receipt_plan_plan_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date bidReceiptPlanPlanTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date bidReceiptPlanPlanTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date bidReceiptPlanPlanTimeEnd;
	/**
	 * 描述: 实际回款时间
	 * 字段: bid_receipt_plan_actual_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date bidReceiptPlanActualTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date bidReceiptPlanActualTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date bidReceiptPlanActualTimeEnd;
	/**
	 * 描述: 期数
	 * 字段: bid_receipt_plan_periods  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer bidReceiptPlanPeriods;
	
	/**
	 * 描述: 回款金额=回款本金+回款利息+加息收益+逾期收益
	 * 字段: bid_receipt_plan_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal bidReceiptPlanAmount;
	
	/**
	 * 描述: 卡券ID
	 * 字段: vas_coupon_detail_id  INT(10)
	 * 默认值: 0
	 */
	private Integer vasCouponDetailId;
	
	/**
	 * 描述: 产品类型:0-加息券，1-投资红包，2-免费提现券，3-好友券
	 * 字段: vas_coupon_product_type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer vasCouponProductType;
	
	/**
	 * 描述: 价值
	 * 字段: vas_coupon_detail_worth  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal vasCouponDetailWorth;
	
	/**
	 * 描述: 使用时间
	 * 字段: vas_coupon_detail_used_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date vasCouponDetailUsedTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date vasCouponDetailUsedTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date vasCouponDetailUsedTimeEnd;
	/**
	 * 描述: 福利的收益值
	 * 字段: profit  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal profit;
	
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
 
	public VasCouponProfitRecord(){
	}

	public VasCouponProfitRecord(Integer id){
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
	
	public void setRegUserDetailRealName(String regUserDetailRealName) {
		this.regUserDetailRealName = regUserDetailRealName;
	}
	
	public String getRegUserDetailRealName() {
		return this.regUserDetailRealName;
	}
	
	public void setRegUserLogin(Long regUserLogin) {
		this.regUserLogin = regUserLogin;
	}
	
	public Long getRegUserLogin() {
		return this.regUserLogin;
	}
	
	public void setBidInfoId(Integer bidInfoId) {
		this.bidInfoId = bidInfoId;
	}
	
	public Integer getBidInfoId() {
		return this.bidInfoId;
	}
	
	public void setBidInfoName(String bidInfoName) {
		this.bidInfoName = bidInfoName;
	}
	
	public String getBidInfoName() {
		return this.bidInfoName;
	}
	
	public void setBidInfoTermValue(Integer bidInfoTermValue) {
		this.bidInfoTermValue = bidInfoTermValue;
	}
	
	public Integer getBidInfoTermValue() {
		return this.bidInfoTermValue;
	}
	
	public void setBidInfoTermUnit(Integer bidInfoTermUnit) {
		this.bidInfoTermUnit = bidInfoTermUnit;
	}
	
	public Integer getBidInfoTermUnit() {
		return this.bidInfoTermUnit;
	}
	
	public void setBidInvestInvestAmount(java.math.BigDecimal bidInvestInvestAmount) {
		this.bidInvestInvestAmount = bidInvestInvestAmount;
	}
	
	public java.math.BigDecimal getBidInvestInvestAmount() {
		return this.bidInvestInvestAmount;
	}
	
	public void setBidReceiptPlanId(Integer bidReceiptPlanId) {
		this.bidReceiptPlanId = bidReceiptPlanId;
	}
	
	public Integer getBidReceiptPlanId() {
		return this.bidReceiptPlanId;
	}
	
	public void setBidReceiptPlanPlanTime(java.util.Date bidReceiptPlanPlanTime) {
		this.bidReceiptPlanPlanTime = bidReceiptPlanPlanTime;
	}
	
	public java.util.Date getBidReceiptPlanPlanTime() {
		return this.bidReceiptPlanPlanTime;
	}
	
	public void setBidReceiptPlanPlanTimeBegin(java.util.Date bidReceiptPlanPlanTimeBegin) {
		this.bidReceiptPlanPlanTimeBegin = bidReceiptPlanPlanTimeBegin;
	}
	
	public java.util.Date getBidReceiptPlanPlanTimeBegin() {
		return this.bidReceiptPlanPlanTimeBegin;
	}
	
	public void setBidReceiptPlanPlanTimeEnd(java.util.Date bidReceiptPlanPlanTimeEnd) {
		this.bidReceiptPlanPlanTimeEnd = bidReceiptPlanPlanTimeEnd;
	}
	
	public java.util.Date getBidReceiptPlanPlanTimeEnd() {
		return this.bidReceiptPlanPlanTimeEnd;
	}	
	public void setBidReceiptPlanActualTime(java.util.Date bidReceiptPlanActualTime) {
		this.bidReceiptPlanActualTime = bidReceiptPlanActualTime;
	}
	
	public java.util.Date getBidReceiptPlanActualTime() {
		return this.bidReceiptPlanActualTime;
	}
	
	public void setBidReceiptPlanActualTimeBegin(java.util.Date bidReceiptPlanActualTimeBegin) {
		this.bidReceiptPlanActualTimeBegin = bidReceiptPlanActualTimeBegin;
	}
	
	public java.util.Date getBidReceiptPlanActualTimeBegin() {
		return this.bidReceiptPlanActualTimeBegin;
	}
	
	public void setBidReceiptPlanActualTimeEnd(java.util.Date bidReceiptPlanActualTimeEnd) {
		this.bidReceiptPlanActualTimeEnd = bidReceiptPlanActualTimeEnd;
	}
	
	public java.util.Date getBidReceiptPlanActualTimeEnd() {
		return this.bidReceiptPlanActualTimeEnd;
	}	
	public void setBidReceiptPlanPeriods(Integer bidReceiptPlanPeriods) {
		this.bidReceiptPlanPeriods = bidReceiptPlanPeriods;
	}
	
	public Integer getBidReceiptPlanPeriods() {
		return this.bidReceiptPlanPeriods;
	}
	
	public void setBidReceiptPlanAmount(java.math.BigDecimal bidReceiptPlanAmount) {
		this.bidReceiptPlanAmount = bidReceiptPlanAmount;
	}
	
	public java.math.BigDecimal getBidReceiptPlanAmount() {
		return this.bidReceiptPlanAmount;
	}
	
	public void setVasCouponDetailId(Integer vasCouponDetailId) {
		this.vasCouponDetailId = vasCouponDetailId;
	}
	
	public Integer getVasCouponDetailId() {
		return this.vasCouponDetailId;
	}
	
	public void setVasCouponProductType(Integer vasCouponProductType) {
		this.vasCouponProductType = vasCouponProductType;
	}
	
	public Integer getVasCouponProductType() {
		return this.vasCouponProductType;
	}
	
	public void setVasCouponDetailWorth(java.math.BigDecimal vasCouponDetailWorth) {
		this.vasCouponDetailWorth = vasCouponDetailWorth;
	}
	
	public java.math.BigDecimal getVasCouponDetailWorth() {
		return this.vasCouponDetailWorth;
	}
	
	public void setVasCouponDetailUsedTime(java.util.Date vasCouponDetailUsedTime) {
		this.vasCouponDetailUsedTime = vasCouponDetailUsedTime;
	}
	
	public java.util.Date getVasCouponDetailUsedTime() {
		return this.vasCouponDetailUsedTime;
	}
	
	public void setVasCouponDetailUsedTimeBegin(java.util.Date vasCouponDetailUsedTimeBegin) {
		this.vasCouponDetailUsedTimeBegin = vasCouponDetailUsedTimeBegin;
	}
	
	public java.util.Date getVasCouponDetailUsedTimeBegin() {
		return this.vasCouponDetailUsedTimeBegin;
	}
	
	public void setVasCouponDetailUsedTimeEnd(java.util.Date vasCouponDetailUsedTimeEnd) {
		this.vasCouponDetailUsedTimeEnd = vasCouponDetailUsedTimeEnd;
	}
	
	public java.util.Date getVasCouponDetailUsedTimeEnd() {
		return this.vasCouponDetailUsedTimeEnd;
	}	
	public void setProfit(java.math.BigDecimal profit) {
		this.profit = profit;
	}
	
	public java.math.BigDecimal getProfit() {
		return this.profit;
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

