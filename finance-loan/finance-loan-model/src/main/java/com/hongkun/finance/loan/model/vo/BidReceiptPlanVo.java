package com.hongkun.finance.loan.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidReceiptPlan.java
 * @Class Name    : BidReceiptPlan.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidReceiptPlanVo extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;

	/**
	 * 描述: 投资id
	 * 字段: invest_id  INT(10)
	 * 默认值: 0
	 */
	private Integer investId;

	/**
	 * 描述: 标的id
	 * 字段: bid_id  INT(10)
	 * 默认值: 0
	 */
	private Integer bidId;

	/**
	 * 描述: 期数
	 * 字段: periods  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer periods;

	/**
	 * 描述: 回款金额=回款本金+回款利息+加息收益+逾期收益
	 * 字段: amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal amount;

	/**
	 * 描述: 回款本金
	 * 字段: capital_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal capitalAmount;

	/**
	 * 描述: 回款利息
	 * 字段: interest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal interestAmount;

	/**
	 * 描述: 加息收益
	 * 字段: increase_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal increaseAmount;

	/**
	 * 描述: 利差
	 * 字段: yearrate_difference_money  DECIMAL(10)
	 * 默认值: 0.0000
	 */
	private java.math.BigDecimal yearrateDifferenceMoney;

	/**
	 * 描述: 逾期收益
	 * 字段: punish_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal punishAmount;

	/**
	 * 描述: 计划回款时间
	 * 字段: plan_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date planTime;

	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date planTimeBegin;

	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date planTimeEnd;
	/**
	 * 描述: 实际回款时间
	 * 字段: actual_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date actualTime;

	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeBegin;

	//【非数据库字段，查询时使用】
	private java.util.Date actualTimeEnd;
	/**
	 * 描述: 收益用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;

	/**
	 * 描述: 状态：0-无效，1-正常
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

	/**借款人ID集合*/
	private List<Integer> regUserIds = new ArrayList<>();

	/**标的名称*/
	private String bidName;

	/**借款人姓名*/
	private String realName;
	/**借款人手机号*/
	private String login;

	/**待回款数目*/
	private Integer remainCount;
	/**项目总额*/
	private java.math.BigDecimal bidAmount;
	/**借款人姓名*/
	private String borrowerName;

	public BidReceiptPlanVo(){
	}

	public BidReceiptPlanVo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setInvestId(Integer investId) {
		this.investId = investId;
	}

	public Integer getInvestId() {
		return this.investId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public Integer getBidId() {
		return this.bidId;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public Integer getPeriods() {
		return this.periods;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.math.BigDecimal getAmount() {
		return this.amount;
	}

	public void setCapitalAmount(java.math.BigDecimal capitalAmount) {
		this.capitalAmount = capitalAmount;
	}

	public java.math.BigDecimal getCapitalAmount() {
		return this.capitalAmount;
	}

	public void setInterestAmount(java.math.BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public java.math.BigDecimal getInterestAmount() {
		return this.interestAmount;
	}

	public void setIncreaseAmount(java.math.BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public java.math.BigDecimal getIncreaseAmount() {
		return this.increaseAmount;
	}

	public void setYearrateDifferenceMoney(java.math.BigDecimal yearrateDifferenceMoney) {
		this.yearrateDifferenceMoney = yearrateDifferenceMoney;
	}

	public java.math.BigDecimal getYearrateDifferenceMoney() {
		return this.yearrateDifferenceMoney;
	}

	public void setPunishAmount(java.math.BigDecimal punishAmount) {
		this.punishAmount = punishAmount;
	}

	public java.math.BigDecimal getPunishAmount() {
		return this.punishAmount;
	}

	public void setPlanTime(java.util.Date planTime) {
		this.planTime = planTime;
	}

	public java.util.Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTimeBegin(java.util.Date planTimeBegin) {
		this.planTimeBegin = planTimeBegin;
	}

	public java.util.Date getPlanTimeBegin() {
		return this.planTimeBegin;
	}

	public void setPlanTimeEnd(java.util.Date planTimeEnd) {
		this.planTimeEnd = planTimeEnd;
	}

	public java.util.Date getPlanTimeEnd() {
		return this.planTimeEnd;
	}
	public void setActualTime(java.util.Date actualTime) {
		this.actualTime = actualTime;
	}

	public java.util.Date getActualTime() {
		return this.actualTime;
	}

	public void setActualTimeBegin(java.util.Date actualTimeBegin) {
		this.actualTimeBegin = actualTimeBegin;
	}

	public java.util.Date getActualTimeBegin() {
		return this.actualTimeBegin;
	}

	public void setActualTimeEnd(java.util.Date actualTimeEnd) {
		this.actualTimeEnd = actualTimeEnd;
	}

	public java.util.Date getActualTimeEnd() {
		return this.actualTimeEnd;
	}
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getRegUserId() {
		return this.regUserId;
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

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealName() {
		return realName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	public String getBidName() {
		return bidName;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setRegUserIds(List<Integer> regUserIds) {
		this.regUserIds = regUserIds;
	}

	public List<Integer> getRegUserIds() {
		return regUserIds;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setBidAmount(BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}

	public BigDecimal getBidAmount() {
		return bidAmount;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

