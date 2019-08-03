package com.hongkun.finance.invest.model.vo;
import java.math.BigDecimal;
import java.util.Date;

import com.yirun.framework.core.model.BaseModel;
public class CreditorTransferVo extends BaseModel {
	private static final long serialVersionUID = 1L;
	//转让记录id
	private Integer transferId;
	private Integer investId;
	
	private Integer firstInvestId;
	private Integer investUserId;
	private Integer newInvestId;
	
	private Integer bidId;
	
	//标的名称
	private String bidName;
	//年化率
	private BigDecimal rate;
	//投资记录金额
	private BigDecimal investAmount;
	//已转让债权（投资记录中已转让的金额）
	private BigDecimal transAmount;
	
	
	//转让价格（实际购买债权金额）
	private BigDecimal transferAmount;
	//转让中债权
	private BigDecimal creditorAmount;
	//可转本金
	private BigDecimal transferAbleAmount;
	//到期日
	private Date endDate;
	private int  endDateNum;
	//下次回款日
	private  Date nextRepayTime;
	//状态
	private Integer state ;
	
	private Integer investState;
	//已收收益
	private BigDecimal receivedIncome;
	//未收收益
	private BigDecimal notReceivedIncome;
	//购买人预期收益
	private BigDecimal buyerExpectAmount;
	//期限单位
	private Integer termUnit;
	//期限值
	private Integer termValue;
	//起息日
	private Date lendingTime;
	//认购方年化率
	private BigDecimal buyRate;
	//转让比率(控制平价、折价、溢价)
	private BigDecimal transferRate;
	
	//还款方式
	private int biddRepaymentWay;
	//筹集天数
	private int transferDays;
	//付款金额
	private BigDecimal payAmount;
	//认购时间
	private Date buyTime;
	//过期剩余时间
	private Long expireTime;
	private String userName;
	
	private Integer bidInfoId;
	
	private Double convertRateStart;
	private Double convertRateEnd;
	private Double overflowRateStart;
	private Double overflowRateEnd; 
	
	
	
	private String oldRealName;
	private String newRealName;
	private Date createTime;
	private Date transferTime;
	public Integer getTransferId() {
		return transferId;
	}
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}
	public Integer getInvestId() {
		return investId;
	}
	public void setInvestId(Integer investId) {
		this.investId = investId;
	}
	public Integer getFirstInvestId() {
		return firstInvestId;
	}
	public void setFirstInvestId(Integer firstInvestId) {
		this.firstInvestId = firstInvestId;
	}
	public Integer getInvestUserId() {
		return investUserId;
	}
	public void setInvestUserId(Integer investUserId) {
		this.investUserId = investUserId;
	}
	public Integer getNewInvestId() {
		return newInvestId;
	}
	public void setNewInvestId(Integer newInvestId) {
		this.newInvestId = newInvestId;
	}
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getInvestAmount() {
		return investAmount;
	}
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public BigDecimal getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}
	public BigDecimal getCreditorAmount() {
		return creditorAmount;
	}
	public void setCreditorAmount(BigDecimal creditorAmount) {
		this.creditorAmount = creditorAmount;
	}
	public BigDecimal getTransferAbleAmount() {
		return transferAbleAmount;
	}
	public void setTransferAbleAmount(BigDecimal transferAbleAmount) {
		this.transferAbleAmount = transferAbleAmount;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getEndDateNum() {
		return endDateNum;
	}
	public void setEndDateNum(int endDateNum) {
		this.endDateNum = endDateNum;
	}
	public Date getNextRepayTime() {
		return nextRepayTime;
	}
	public void setNextRepayTime(Date nextRepayTime) {
		this.nextRepayTime = nextRepayTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getInvestState() {
		return investState;
	}
	public void setInvestState(Integer investState) {
		this.investState = investState;
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
	public BigDecimal getBuyerExpectAmount() {
		return buyerExpectAmount;
	}
	public void setBuyerExpectAmount(BigDecimal buyerExpectAmount) {
		this.buyerExpectAmount = buyerExpectAmount;
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
	public BigDecimal getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(BigDecimal buyRate) {
		this.buyRate = buyRate;
	}
	public BigDecimal getTransferRate() {
		return transferRate;
	}
	public void setTransferRate(BigDecimal transferRate) {
		this.transferRate = transferRate;
	}
	public int getBiddRepaymentWay() {
		return biddRepaymentWay;
	}
	public void setBiddRepaymentWay(int biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}
	public int getTransferDays() {
		return transferDays;
	}
	public void setTransferDays(int transferDays) {
		this.transferDays = transferDays;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public Long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getBidInfoId() {
		return bidInfoId;
	}
	public void setBidInfoId(Integer bidInfoId) {
		this.bidInfoId = bidInfoId;
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
	public String getOldRealName() {
		return oldRealName;
	}
	public void setOldRealName(String oldRealName) {
		this.oldRealName = oldRealName;
	}
	public String getNewRealName() {
		return newRealName;
	}
	public void setNewRealName(String newRealName) {
		this.newRealName = newRealName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	
	
	
}
