package com.hongkun.finance.invest.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : invest
 * @Program Name  : com.hongkun.finance.invest.model.BidAutoScheme.java
 * @Class Name    : BidAutoScheme.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidAutoScheme extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 标识
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户ID
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 用户姓名
	 * 字段: real_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String realName;
	
	/**
	 * 描述: 是否使用红包和加息券 0 否，1是
	 * 字段: use_coupon_flag  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(message = "请选择是否是否使用红包和加息劵")
	private Integer useCouponFlag;
	
	/**
	 * 描述: 最低预期年化收益率
	 * 字段: min_rate  DECIMAL(10)
	 * 默认值: 0.00
	 */
	@NotNull(message = "请选择是否是否使用红包和加息劵")
	@Min(value = 0, message = "最小利率不能低于0")
	@Max(value = 50, message = "最大利率不能超过50")
	private java.math.BigDecimal minRate;
	
	/**
	 * 描述: 投资期限范围上限
	 * 字段: invest_term_max  INT(10)
	 */
	@NotNull(message = "请输入最大投资期限")
	@Max(value = 50, message = "最大值不能超过36")
	private java.lang.Integer investTermMax;
	
	/**
	 * 描述: 投资期限范围下限
	 * 字段: invest_term_min  INT(10)
	 */
	@NotNull(message = "请输入最小投资期限")
	@Min(value = 0, message = "最小值不能小于1")
	private java.lang.Integer investTermMin;
	
	/**
	 * 描述: 还款方式(可多选) 0 不限 2 按月付息，到期还本 3 到期还本付息
	 * 字段: repay_type  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(message = "请选择还款方式")
	private Integer repayType;
	
	/**
	 * 描述: 投资类型优先级，第1至4位代表，年年盈、季季盈、月月盈、新手标，优先级0-4由不支持至最高, 例如:1210标识季季盈是优先级是中并且不支持新手标，其他等级是低
	 * 字段: bid_priority  INT(10)
	 * 默认值: 1111
	 */
	@NotNull(message = "请选择标的优先级")
	@Max(value=9994, message = "请选择优先级")
	@Min(value=1230, message = "请选择优先级")
	private java.lang.Integer bidPriority;
	
	/**
	 * 描述: 排序方式 1年化收益率 2 投资期限
	 * 字段: priority_type  TINYINT(3)
	 * 默认值: 1
	 */
	@NotNull(message = "请选择预期年化收益率和投资期限优先级")
	private Integer priorityType;
	
	/**
	 * 描述: 账户预留金额
	 * 字段: reserve_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	@NotNull(message = "请输入账户预留金额")
	private java.math.BigDecimal reserveAmount;
	
	/**
	 * 描述: 有效期类型 1:长期有效 2:自定义
	 * 字段: effective_type  TINYINT(3)
	 * 默认值: 1
	 */
	@NotNull(message = "请选择有效期类型")
	private Integer effectiveType;
	
	/**
	 * 描述: 投资有效开始时间
	 * 字段: effective_start_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date effectiveStartTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveStartTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveStartTimeEnd;
	/**
	 * 描述: 投资有效结束时间
	 * 字段: effective_end_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date effectiveEndTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveEndTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date effectiveEndTimeEnd;
	/**
	 * 描述: 状态 0:禁用,1:启用,2:失效,4:删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
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
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date modifyTimeBegin;

	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date modifyTimeEnd;
	
	/**当前排名*/
	private Integer currIndex;

	/** 用户手机号 */
	private Long tel;

	public Long getTel() {
		return tel;
	}

	public void setTel(Long tel) {
		this.tel = tel;
	}

	public BidAutoScheme(){
	}

	public BidAutoScheme(java.lang.Integer id){
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
	
	public void setUseCouponFlag(Integer useCouponFlag) {
		this.useCouponFlag = useCouponFlag;
	}
	
	public Integer getUseCouponFlag() {
		return this.useCouponFlag;
	}
	
	public void setMinRate(java.math.BigDecimal minRate) {
		this.minRate = minRate;
	}
	
	public java.math.BigDecimal getMinRate() {
		return this.minRate;
	}
	
	public void setInvestTermMax(java.lang.Integer investTermMax) {
		this.investTermMax = investTermMax;
	}
	
	public java.lang.Integer getInvestTermMax() {
		return this.investTermMax;
	}
	
	public void setInvestTermMin(java.lang.Integer investTermMin) {
		this.investTermMin = investTermMin;
	}
	
	public java.lang.Integer getInvestTermMin() {
		return this.investTermMin;
	}
	
	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}
	
	public Integer getRepayType() {
		return this.repayType;
	}
	
	public void setBidPriority(java.lang.Integer bidPriority) {
		this.bidPriority = bidPriority;
	}
	
	public java.lang.Integer getBidPriority() {
		return this.bidPriority;
	}
	
	public void setPriorityType(Integer priorityType) {
		this.priorityType = priorityType;
	}
	
	public Integer getPriorityType() {
		return this.priorityType;
	}
	
	public void setReserveAmount(java.math.BigDecimal reserveAmount) {
		this.reserveAmount = reserveAmount;
	}
	
	public java.math.BigDecimal getReserveAmount() {
		return this.reserveAmount;
	}
	
	public void setEffectiveType(Integer effectiveType) {
		this.effectiveType = effectiveType;
	}
	
	public Integer getEffectiveType() {
		return this.effectiveType;
	}
	
	public void setEffectiveStartTime(java.util.Date effectiveStartTime) {
		this.effectiveStartTime = effectiveStartTime;
	}
	
	public java.util.Date getEffectiveStartTime() {
		return this.effectiveStartTime;
	}
	
	public void setEffectiveStartTimeBegin(java.util.Date effectiveStartTimeBegin) {
		this.effectiveStartTimeBegin = effectiveStartTimeBegin;
	}
	
	public java.util.Date getEffectiveStartTimeBegin() {
		return this.effectiveStartTimeBegin;
	}
	
	public void setEffectiveStartTimeEnd(java.util.Date effectiveStartTimeEnd) {
		this.effectiveStartTimeEnd = effectiveStartTimeEnd;
	}
	
	public java.util.Date getEffectiveStartTimeEnd() {
		return this.effectiveStartTimeEnd;
	}	
	public void setEffectiveEndTime(java.util.Date effectiveEndTime) {
		this.effectiveEndTime = effectiveEndTime;
	}
	
	public java.util.Date getEffectiveEndTime() {
		return this.effectiveEndTime;
	}
	
	public void setEffectiveEndTimeBegin(java.util.Date effectiveEndTimeBegin) {
		this.effectiveEndTimeBegin = effectiveEndTimeBegin;
	}
	
	public java.util.Date getEffectiveEndTimeBegin() {
		return this.effectiveEndTimeBegin;
	}
	
	public void setEffectiveEndTimeEnd(java.util.Date effectiveEndTimeEnd) {
		this.effectiveEndTimeEnd = effectiveEndTimeEnd;
	}
	
	public java.util.Date getEffectiveEndTimeEnd() {
		return this.effectiveEndTimeEnd;
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
	
	public Integer getCurrIndex() {
		return currIndex;
	}

	public void setCurrIndex(Integer currIndex) {
		this.currIndex = currIndex;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

