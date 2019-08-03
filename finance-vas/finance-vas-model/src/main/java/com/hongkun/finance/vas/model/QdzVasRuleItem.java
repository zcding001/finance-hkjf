package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.QdzVasRuleItem.java
 * @Class Name : QdzVasRuleItem.java
 * @Description : 钱袋子规则条目
 * @Author : yanbinghuang
 */
public class QdzVasRuleItem implements Serializable {
	private static final long serialVersionUID = 1L;
	// 每天免费转入次数
	private Integer inOPPPerMonth;
	// 每人每天最多转入金额
	private BigDecimal inMaxMoneyPPPD;
	// 超出转入次数后收费比率
	private Double inPayRate;
	// 每月免费转出次数
	private Integer outOPPPerMonth;
	// 每人每天最多转出金额
	private BigDecimal outMaxMoneyPPPD;
	// 超出转出次数后收费比率
	private Double outPayRate;
	// 当前获取收益率=基准利率+活动利率
	private Double currInterestRate;
	// 当前获取收益率
	private Double baseRate;
	// 活动加息利率
	private Double activityRate;
	// 最低起投金额
	private BigDecimal investLowest;
	// 客户最大持有金额
	private BigDecimal holdInvestMax;
	// 禁止转入/转出开始时间
	private String noInOutStartTimes;
	// 禁止转入/转出结束时间
	private String noInOutEndTimes;

	// 仅用前台展示数据处理//
	// 剩余可投金额
	private BigDecimal residueBuyAmount;
	// 当前钱袋子状态
	private Integer state;
	// 下次购买时间
	private Long nextBuyTime;
	//系统时间
	private Long systemTime;

	public Long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Long systemTime) {
        this.systemTime = systemTime;
    }

    public BigDecimal getResidueBuyAmount() {
		return residueBuyAmount;
	}

	public void setResidueBuyAmount(BigDecimal residueBuyAmount) {
		this.residueBuyAmount = residueBuyAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getNextBuyTime() {
		return nextBuyTime;
	}

	public void setNextBuyTime(Long nextBuyTime) {
		this.nextBuyTime = nextBuyTime;
	}

	public Double getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(Double baseRate) {
		this.baseRate = baseRate;
	}

	public Double getActivityRate() {
		return activityRate;
	}

	public void setActivityRate(Double activityRate) {
		this.activityRate = activityRate;
	}

	public Integer getInOPPPerMonth() {
		return inOPPPerMonth;
	}

	public void setInOPPPerMonth(Integer inOPPPerMonth) {
		this.inOPPPerMonth = inOPPPerMonth;
	}

	public BigDecimal getInMaxMoneyPPPD() {
		return inMaxMoneyPPPD;
	}

	public void setInMaxMoneyPPPD(BigDecimal inMaxMoneyPPPD) {
		this.inMaxMoneyPPPD = inMaxMoneyPPPD;
	}

	public Double getInPayRate() {
		return inPayRate;
	}

	public void setInPayRate(Double inPayRate) {
		this.inPayRate = inPayRate;
	}

	public Integer getOutOPPPerMonth() {
		return outOPPPerMonth;
	}

	public void setOutOPPPerMonth(Integer outOPPPerMonth) {
		this.outOPPPerMonth = outOPPPerMonth;
	}

	public BigDecimal getOutMaxMoneyPPPD() {
		return outMaxMoneyPPPD;
	}

	public void setOutMaxMoneyPPPD(BigDecimal outMaxMoneyPPPD) {
		this.outMaxMoneyPPPD = outMaxMoneyPPPD;
	}

	public Double getOutPayRate() {
		return outPayRate;
	}

	public void setOutPayRate(Double outPayRate) {
		this.outPayRate = outPayRate;
	}

	public Double getCurrInterestRate() {
		return currInterestRate;
	}

	public void setCurrInterestRate(Double currInterestRate) {
		this.currInterestRate = currInterestRate;
	}

	public BigDecimal getInvestLowest() {
		return investLowest;
	}

	public void setInvestLowest(BigDecimal investLowest) {
		this.investLowest = investLowest;
	}

	public BigDecimal getHoldInvestMax() {
		return holdInvestMax;
	}

	public void setHoldInvestMax(BigDecimal holdInvestMax) {
		this.holdInvestMax = holdInvestMax;
	}

	public String getNoInOutStartTimes() {
		return noInOutStartTimes;
	}

	public void setNoInOutStartTimes(String noInOutStartTimes) {
		this.noInOutStartTimes = noInOutStartTimes;
	}

	public String getNoInOutEndTimes() {
		return noInOutEndTimes;
	}

	public void setNoInOutEndTimes(String noInOutEndTimes) {
		this.noInOutEndTimes = noInOutEndTimes;
	}
}
