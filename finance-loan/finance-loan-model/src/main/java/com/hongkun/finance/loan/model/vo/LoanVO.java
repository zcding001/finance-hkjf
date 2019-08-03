package com.hongkun.finance.loan.model.vo;

import com.yirun.framework.core.annotation.Union;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description   : loan服务通用VO
 * @Project       : finance-loan-model
 * @Program Name  : com.hongkun.finance.loan.model.vo.LoanVO.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class LoanVO implements Serializable {
	    
	private static final long serialVersionUID = 1L;
	
	/**计划类别 1：还款计划 	2：回款计划*/
	private Integer planType = 1;
	private Integer id;
	/**用户id*/
	@Union(bind = {"BidRepayPlan", "BidReceiptPlan"}, reNameTo = "regUserId")
	private Integer userId;
	private List<Integer> userIds = new ArrayList<>();
	/**用户姓名*/
	private String realName;
	/**总金额**/
	private BigDecimal amount;
	/**总本金**/
	private BigDecimal capitalAmount;
	/**总利息**/
	private BigDecimal interestAmount;
	/**总加息收益**/
	private BigDecimal increaseAmount;
	/**总逾期金额*/
	private BigDecimal punishAmount;
	/**逾期天数**/
	private Integer punishDays;
	/**总违约金*/
	private BigDecimal deditAmount;
	/**总服务费*/
	private BigDecimal serviceCharge;
	/**剩余代还本金*/
	private BigDecimal residueAmount;
	/**未完成期数*/
	private Integer unFinishPeriods;
	/**完成期数*/
	private Integer finishPeriods;
	/**期数**/
	@Union(reNameTo = "periods")
	private Integer periods;
	/**计划状态**/
	private Integer state;
	/**当前还款标识*/
	private Integer currRepayFlag = 0;
	/**是否允许代扣，弹出代扣询问框*/
	private Integer allowWithholdFlag = 0;
	/**是否显示提前还款按钮*/
	private Integer advanceRepayFlag = 0;
	/**状态集合**/
	private List<Integer> states = new ArrayList<>();
	/**标的ID*/
	private Integer bidId;
	/**标的产品类型*/
	private Integer productType;
	/**标的ID集合*/
	private List<Integer> bidIds = new ArrayList<>();
	/**项目名称*/
	@Union(bind = {"BidInfo"}, reNameTo = "name")
	private String bidName;
	/**项目总金额*/
	@Union(bind = {"BidInfo"}, reNameTo = "totalAmount")
	private BigDecimal bidAmount;
	private Integer investId;
	/**使用加息券加息率*/
	private BigDecimal couponWorthJ;
	
	private Date planTime;
	/**
	 * 描述: 实际还款时间
	 * 字段: actual_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date actualTime;
	private Date createTime;
	private String createTimeBegin;
	private String createTimeEnd;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlanType() {
		return planType;
	}
	public void setPlanType(Integer planType) {
		this.planType = planType;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getCapitalAmount() {
		return capitalAmount;
	}
	public void setCapitalAmount(BigDecimal capitalAmount) {
		this.capitalAmount = capitalAmount;
	}
	public BigDecimal getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	public BigDecimal getIncreaseAmount() {
		return increaseAmount;
	}
	public void setIncreaseAmount(BigDecimal increaseAmount) {
		this.increaseAmount = increaseAmount;
	}
	public BigDecimal getPunishAmount() {
		return punishAmount;
	}
	public void setPunishAmount(BigDecimal punishAmount) {
		this.punishAmount = punishAmount;
	}
	public BigDecimal getDeditAmount() {
		return deditAmount;
	}
	public void setDeditAmount(BigDecimal deditAmount) {
		this.deditAmount = deditAmount;
	}
	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public Integer getUnFinishPeriods() {
		return unFinishPeriods;
	}
	public void setUnFinishPeriods(Integer unFinishPeriods) {
		this.unFinishPeriods = unFinishPeriods;
	}
	public Integer getFinishPeriods() {
		return finishPeriods;
	}
	public void setFinishPeriods(Integer finishPeriods) {
		this.finishPeriods = finishPeriods;
	}
	public Integer getPeriods() {
		return periods;
	}
	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public List<Integer> getStates() {
		return states;
	}
	public void setStates(List<Integer> states) {
		this.states = states;
	}
	public Integer getBidId() {
		return bidId;
	}
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	public List<Integer> getBidIds() {
		return bidIds;
	}
	public void setBidIds(List<Integer> bidIds) {
		this.bidIds = bidIds;
	}
	public String getBidName() {
		return bidName;
	}
	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	public BigDecimal getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeBegin() {
		return createTimeBegin;
	}
	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public java.util.Date getActualTime() {
		return actualTime;
	}
	public void setActualTime(java.util.Date actualTime) {
		this.actualTime = actualTime;
	}
	public BigDecimal getResidueAmount() {
		return residueAmount;
	}
	public void setResidueAmount(BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
	}
	public Integer getCurrRepayFlag() {
		return currRepayFlag;
	}
	public void setCurrRepayFlag(Integer currRepayFlag) {
		this.currRepayFlag = currRepayFlag;
	}
	public Integer getAllowWithholdFlag() {
		return allowWithholdFlag;
	}
	public void setAllowWithholdFlag(Integer allowWithholdFlag) {
		this.allowWithholdFlag = allowWithholdFlag;
	}
	public Integer getAdvanceRepayFlag() {
		return advanceRepayFlag;
	}
	public void setAdvanceRepayFlag(Integer advanceRepayFlag) {
		this.advanceRepayFlag = advanceRepayFlag;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}

    public Integer getPunishDays() {
        return punishDays;
    }

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    public void setPunishDays(Integer punishDays) {
        this.punishDays = punishDays;
    }

	public BigDecimal getCouponWorthJ() {
		return couponWorthJ;
	}

	public void setCouponWorthJ(BigDecimal couponWorthJ) {
		this.couponWorthJ = couponWorthJ;
	}
}
