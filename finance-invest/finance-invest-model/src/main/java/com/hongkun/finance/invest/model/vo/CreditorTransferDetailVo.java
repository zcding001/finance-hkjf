package com.hongkun.finance.invest.model.vo;
import java.math.BigDecimal;
import java.util.Date;

import com.yirun.framework.core.model.BaseModel;
public class CreditorTransferDetailVo extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	//##########################标的基础信息开始
	//标的
	private Integer bidId;
	//标的名称
	private String bidName;
	//期限单位
	private Integer termUnit;
	//期限值
	private Integer termValue;
	//起息日
	private Date lendingTime;
	//标的年化率
	private BigDecimal rate;
	//折价、溢价范围
	private Double convertRateStart;
	private Double convertRateEnd;
	private Double overflowRateStart;
	private Double overflowRateEnd; 
	//到期日
	private Date endDate;
	//最大筹集天数
	private Integer transferDaysMax;
	//还款方式
	private Integer biddRepaymentWay;
	//剩余到期天数
	private Integer endDateNum;
	//##########################标的基础信息结束
	
	//##########################投资记录信息开始
	//投资记录id
	private Integer investId;
	//投资记录金额
	private BigDecimal investAmount;
	//已转让债权（投资记录中已转让的金额）
	private BigDecimal transferedAmount;
	//##########################投资记录信息结束
	
	
	//##########################债权信息
	//持有金额
	private BigDecimal holdMoney;
	//可转本金
	private BigDecimal useAbleCapital;
	//已收收益
	private BigDecimal receivedIncome;
	//未收收益
	private BigDecimal notReceivedIncome;
	
	
	//##########################转让信息
	//转让记录id
	private Integer transferId;
	//状态 1-转让中 2-已转让
	private Integer state ;
	private BigDecimal creditorAmount;
	//转让价格（实际购买债权金额）
	private BigDecimal transferPrice;
	//筹集天数
	private int transferDays;
	//转让比率(控制平价、折价、溢价)
	private BigDecimal transferRate;
	//转让时间
	private Date transferTime;
	//转让人转让后年化率
	private BigDecimal transferedRate;
	//购买人年化率
	private BigDecimal buyerRate;
	//过期时间
	private long expireTime;
	//发布日期
	private Date createTime;
	//债权人id
	private Integer investUserId;
	//债权人姓名
	private String investUserName;
	//购买人预期总收益
	private BigDecimal buyerSumInterest;
	//购买人总回款
	private BigDecimal buyerExpectAmount;
	//新投资id
	private Integer newInvestId;
	public Integer getBidId() {
		return bidId;
	}
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	public String getBidName() {
		return bidName;
	}
	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	public Integer getTermUnit() {
		return termUnit;
	}
	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}
	public Integer getTermValue() {
		return termValue;
	}
	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}
	public Date getLendingTime() {
		return lendingTime;
	}
	public void setLendingTime(Date lendingTime) {
		this.lendingTime = lendingTime;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Double getConvertRateStart() {
		return convertRateStart;
	}
	public void setConvertRateStart(Double convertRateStart) {
		this.convertRateStart = convertRateStart;
	}
	public Double getConvertRateEnd() {
		return convertRateEnd;
	}
	public void setConvertRateEnd(Double convertRateEnd) {
		this.convertRateEnd = convertRateEnd;
	}
	public Double getOverflowRateStart() {
		return overflowRateStart;
	}
	public void setOverflowRateStart(Double overflowRateStart) {
		this.overflowRateStart = overflowRateStart;
	}
	public Double getOverflowRateEnd() {
		return overflowRateEnd;
	}
	public void setOverflowRateEnd(Double overflowRateEnd) {
		this.overflowRateEnd = overflowRateEnd;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getInvestId() {
		return investId;
	}
	public void setInvestId(Integer investId) {
		this.investId = investId;
	}
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getTransferedAmount() {
		return transferedAmount;
	}
	public void setTransferedAmount(BigDecimal transferedAmount) {
		this.transferedAmount = transferedAmount;
	}
	public BigDecimal getHoldMoney() {
		return holdMoney;
	}
	public void setHoldMoney(BigDecimal holdMoney) {
		this.holdMoney = holdMoney;
	}
	public BigDecimal getUseAbleCapital() {
		return useAbleCapital;
	}
	public void setUseAbleCapital(BigDecimal useAbleCapital) {
		this.useAbleCapital = useAbleCapital;
	}
	public BigDecimal getReceivedIncome() {
		return receivedIncome;
	}
	public void setReceivedIncome(BigDecimal receivedIncome) {
		this.receivedIncome = receivedIncome;
	}
	public BigDecimal getNotReceivedIncome() {
		return notReceivedIncome;
	}
	public void setNotReceivedIncome(BigDecimal notReceivedIncome) {
		this.notReceivedIncome = notReceivedIncome;
	}
	public Integer getTransferId() {
		return transferId;
	}
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public BigDecimal getTransferPrice() {
		return transferPrice;
	}
	public void setTransferPrice(BigDecimal transferPrice) {
		this.transferPrice = transferPrice;
	}
	public int getTransferDays() {
		return transferDays;
	}
	public void setTransferDays(int transferDays) {
		this.transferDays = transferDays;
	}
	public BigDecimal getTransferRate() {
		return transferRate;
	}
	public void setTransferRate(BigDecimal transferRate) {
		this.transferRate = transferRate;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	public BigDecimal getTransferedRate() {
		return transferedRate;
	}
	public void setTransferedRate(BigDecimal transferedRate) {
		this.transferedRate = transferedRate;
	}
	public Integer getTransferDaysMax() {
		return transferDaysMax;
	}
	public void setTransferDaysMax(Integer transferDaysMax) {
		this.transferDaysMax = transferDaysMax;
	}
	public BigDecimal getCreditorAmount() {
		return creditorAmount;
	}
	public void setCreditorAmount(BigDecimal creditorAmount) {
		this.creditorAmount = creditorAmount;
	}
	public BigDecimal getBuyerRate() {
		return buyerRate;
	}
	public void setBuyerRate(BigDecimal buyerRate) {
		this.buyerRate = buyerRate;
	}
	public Integer getBiddRepaymentWay() {
		return biddRepaymentWay;
	}
	public void setBiddRepaymentWay(Integer biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getEndDateNum() {
		return endDateNum;
	}
	public void setEndDateNum(Integer endDateNum) {
		this.endDateNum = endDateNum;
	}
	public Integer getInvestUserId() {
		return investUserId;
	}
	public void setInvestUserId(Integer investUserId) {
		this.investUserId = investUserId;
	}
	public String getInvestUserName() {
		return investUserName;
	}
	public void setInvestUserName(String investUserName) {
		this.investUserName = investUserName;
	}
	public BigDecimal getBuyerSumInterest() {
		return buyerSumInterest;
	}
	public void setBuyerSumInterest(BigDecimal buyerSumInterest) {
		this.buyerSumInterest = buyerSumInterest;
	}
	public BigDecimal getBuyerExpectAmount() {
		return buyerExpectAmount;
	}
	public void setBuyerExpectAmount(BigDecimal buyerExpectAmount) {
		this.buyerExpectAmount = buyerExpectAmount;
	}
	public Integer getNewInvestId() {
		return newInvestId;
	}
	public void setNewInvestId(Integer newInvestId) {
		this.newInvestId = newInvestId;
	}
}
