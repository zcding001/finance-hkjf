package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description : 标的表和详情表vo
 * @Project : finance-invest-model
 * @Program Name : com.hongkun.finance.invest.model.vo.BidInfoVO.java
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidInfoVO extends BaseModel {

	private static final long serialVersionUID = 1L;

	// ################bid_info信息
	private Integer id;
	private Integer bidId;
	private String bidCode; // 项目编码
	private BigDecimal totalAmount;
	private Integer loanUse;// 借款用途

	private BigDecimal residueAmount;
	private BigDecimal interestRate;// 年化率
    //基础利率=年化率-已加息率
    private BigDecimal baseRate;
    private BigDecimal raiseRate;
	private Integer termValue;
	private Integer termUnit;
	private Date lendingTime;// 放款时间
	private String lendTime;
	private Date createTime;
	private Integer state;
	private Integer borrowerId;// 借款人ID
	private Integer receiptUserId;// 本金接收人员ID
	private Integer bidScheme;// 招标方案
	private Integer bidSchemeValue;// 招标方案值
	private List<Integer> borrowerIds;
	private String showPosition;// 显示平台
	private String investPosition;// 投资平台
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;// 投标开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;// 投标结束时间
	private BigDecimal commissionRate;// 借款手续费率
	private BigDecimal serviceRate;// 借款服务费率
	private Integer biddRepaymentWay;// 还款方式
	private String printImgurl;// 印章图片
	private String labelText;//标签内容
	private String labelUrl;//标签链接
	private String imgUrl;// 项目图片
	private Integer allowCoupon;// 是否允许使用卡券:0：不支持 1-不限制；2:仅加息券；3：仅红包',
	private BigDecimal advanceServiceRate; // 垫付服务费率
	private BigDecimal liquidatedDamagesRate; // 垫付违约金费率
	private Integer stepValue;

	// ################bid_info_detail信息
	private Integer matchType;
	private BigDecimal returnCapAmount;
	private Date nextMatchTime;
	private Date nextMatchDate;
	private Integer punishState;// 是否逾期罚款 0-否,1-是
	private Integer reserveInterest;// 是否预留借款人利息 0-不预留,1-预留
	private String billNo;// 票据号
	private String repaymentSource;// 还款来源
	private String assureDescription;// 风险控制措施
	private String projectDescription;// 项目信息
	private String fundUse;// 资金用途
	private Integer withholdState;// 代扣状态
	private Integer recommendState;// 好友推荐状态 0-不参与,1-参与
	private Integer givingPointState;// 赠送积分状态 0-不赠送,1-赠送
	private Integer bourseFlg;// 是否为交易所产品
	private String assureType;
	private String creditLevel;
	private java.lang.String legalFiles;
	private Integer type;
	private Integer creditorDays;// 转让前持有债权天数
	private Integer dealRepayDays;// 转让交易日期距下次还款日期天数
	private BigDecimal convertRateStart;// 折价率范围起始
	private BigDecimal convertRateEnd;// 折价率范围结束
	private BigDecimal overflowRateStart;// 溢价率范围开始
	private BigDecimal overflowRateEnd;// 溢价率范围结束
	private Integer mostTransferCount;// 债权最多转让次数
	private Integer maturityRemind;// 到期是否提醒0-不提醒,1-提醒
	private String autidNote;// 审核意见
	private Integer creditorState;
	private BigDecimal transferRate;
	private Integer transferDays;
	private Integer advanceRepayState; // 提前归还本金状态:0-不允许,1-允许
	private Integer returnCapDays; // 借款满n天后才允许提前还本金
	private Integer returnCapWay; // 归还本金计息方式:1-按日计息,2-按月计息
	// ################bid_product信息
	private Integer bidProductId;
	private Integer productType;
	private List<Integer> productTypes = new ArrayList<>();
	private String productName;
	private Integer productState;//产品状态
	private Integer amountMin;
	private Integer amountMax;
	private Integer termValueMin;
	private Integer termValueMax;
	private Integer bidDeadline;
	private BigDecimal rateMin;
	private BigDecimal rateMax;
	private String description;
	private String contract;

	// ################其他
	private java.lang.Integer modifyUserId;
	private java.util.Date modifyTime;
	private java.lang.Integer createUserId;
	private List<Integer> states;// 查询条件：标的状态集合
	// 是否允许匹配操作
	private Integer isMatch;
	private Integer startTerm;
	private Integer endTerm;
	// 0 散标 1优选
	private Integer bidType;
	// 待匹配金额
	private BigDecimal matchingMoney;
	// 借款人姓名
	private String borrowerName;
	// 本金接收人员
	private String ReceiptUserName;
	// 利息总金额
	private BigDecimal interestAmount;
	private String createTimeBegin;
	private String createTimeEnd;
	// 是否存在匹配记录，0-不存在，1-存在
	private Integer hasMatch;

	// 产品类型限制ids
	private List<Integer> productTypeLimitIds;
	// 标的类型限制ids
	private List<Integer> bidTypeLimitIds;
	// 还款方式限制ids
	private List<Integer> repayWayLimitIds;
	// 标的限制id
	private List<Integer> bidLimitIds;
	/**
	* 标的名称
	*/
	@Union(reNameTo = "name")
	private String title;
	/**
	 * 是否加息
	 */
	private Integer raiseFlag;

	/**
	 * 当前标的是否可以修改
	 */
	private Boolean modifyAble = false;

	/**
	 * 距离下次匹配时间天数
	 */
	private int resiDays;
	/**
	 * 规则ID
	 */
	private Integer ruleId;

	/**
	 * 标的本息
	 */
	private BigDecimal sumRepay;
	//导出excel所需字段开始
	//
	//期限值
	private String termValueStr;
	//待匹配天数
	private String matchingDaysStr;
	//待匹配情况
	private String matchingDetailsStr;
	//创建时间
	private String createTimeStr;
	//放款时间
	private  String lendingTimeStr;
	//下次匹配时间
	private String nextMatchDateStr;
	//
	//导出excel所需字段结束
    //企业账户ID
	private Integer companyId;
	//企业账户名称
	private String companyName;
	//服务器系统时间，查询匹配列表传递该参数，方便修改系统时间进行匹配测试
	private Date serverSysDate;
	//作用域：1-hkjf, 2-cxj
	private Integer actionScope;
	public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProductState() {
		return productState;
	}

	public void setProductState(Integer productState) {
		this.productState = productState;
	}

	public int getResiDays() {
		return resiDays;
	}

	public void setResiDays(int resiDays) {
		this.resiDays = resiDays;
	}

	public void setBidLimitIds(List<Integer> bidLimitIds) {
		this.bidLimitIds = bidLimitIds;
	}

	public List<Integer> getProductTypeLimitIds() {
		return productTypeLimitIds;
	}

	public void setProductTypeLimitIds(List<Integer> productTypeLimitIds) {
		this.productTypeLimitIds = productTypeLimitIds;
	}

	public BigDecimal getSumRepay() {
		return sumRepay;
	}

	public void setSumRepay(BigDecimal sumRepay) {
		this.sumRepay = sumRepay;
	}

	public Integer getRaiseFlag() {
		return raiseFlag;
	}

	public void setRaiseFlag(Integer raiseFlag) {
		this.raiseFlag = raiseFlag;
	}

	public List<Integer> getBidTypeLimitIds() {
		return bidTypeLimitIds;
	}

	public void setBidTypeLimitIds(List<Integer> bidTypeLimitIds) {
		this.bidTypeLimitIds = bidTypeLimitIds;
	}

	public List<Integer> getRepayWayLimitIds() {
		return repayWayLimitIds;
	}

	public void setRepayWayLimitIds(List<Integer> repayWayLimitIds) {
		this.repayWayLimitIds = repayWayLimitIds;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public String getBidCode() {
		return bidCode;
	}

	public void setBidCode(String bidCode) {
		this.bidCode = bidCode;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getLoanUse() {
		return loanUse;
	}

	public void setLoanUse(Integer loanUse) {
		this.loanUse = loanUse;
	}

	public BigDecimal getResidueAmount() {
		return residueAmount;
	}

	public void setResidueAmount(BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTermValue() {
		return termValue;
	}

	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}

	public Integer getTermUnit() {
		return termUnit;
	}

	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}

	public Date getLendingTime() {
		return lendingTime;
	}

	public void setLendingTime(Date lendingTime) {
		this.lendingTime = lendingTime;
	}

	public String getLendTime() {
		return lendTime;
	}

	public void setLendTime(String lendTime) {
		this.lendTime = lendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}

	public Integer getReceiptUserId() {
		return receiptUserId;
	}

	public void setReceiptUserId(Integer receiptUserId) {
		this.receiptUserId = receiptUserId;
	}

	public Integer getBidScheme() {
		return bidScheme;
	}

	public void setBidScheme(Integer bidScheme) {
		this.bidScheme = bidScheme;
	}

	public Integer getBidSchemeValue() {
		return bidSchemeValue;
	}

	public void setBidSchemeValue(Integer bidSchemeValue) {
		this.bidSchemeValue = bidSchemeValue;
	}

	public List<Integer> getBorrowerIds() {
		return borrowerIds;
	}

	public void setBorrowerIds(List<Integer> borrowerIds) {
		this.borrowerIds = borrowerIds;
	}

	public String getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(String showPosition) {
		this.showPosition = showPosition;
	}

	public String getInvestPosition() {
		return investPosition;
	}

	public void setInvestPosition(String investPosition) {
		this.investPosition = investPosition;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	public BigDecimal getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(BigDecimal serviceRate) {
		this.serviceRate = serviceRate;
	}

	public Integer getBiddRepaymentWay() {
		return biddRepaymentWay;
	}

	public void setBiddRepaymentWay(Integer biddRepaymentWay) {
		this.biddRepaymentWay = biddRepaymentWay;
	}

	public String getPrintImgurl() {
		return printImgurl;
	}

	public void setPrintImgurl(String printImgurl) {
		this.printImgurl = printImgurl;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getLabelUrl() {
		return labelUrl;
	}

	public void setLabelUrl(String labelUrl) {
		this.labelUrl = labelUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getAllowCoupon() {
		return allowCoupon;
	}

	public void setAllowCoupon(Integer allowCoupon) {
		this.allowCoupon = allowCoupon;
	}

	public Integer getMatchType() {
		return matchType;
	}

	public void setMatchType(Integer matchType) {
		this.matchType = matchType;
	}

	public BigDecimal getReturnCapAmount() {
		return returnCapAmount;
	}

	public void setReturnCapAmount(BigDecimal returnCapAmount) {
		this.returnCapAmount = returnCapAmount;
	}

	public Date getNextMatchTime() {
		return nextMatchTime;
	}

	public void setNextMatchTime(Date nextMatchTime) {
		this.nextMatchTime = nextMatchTime;
	}

	public Date getNextMatchDate() {
		return nextMatchDate;
	}

	public void setNextMatchDate(Date nextMatchDate) {
		this.nextMatchDate = nextMatchDate;
	}

	public Integer getPunishState() {
		return punishState;
	}

	public void setPunishState(Integer punishState) {
		this.punishState = punishState;
	}

	public Integer getReserveInterest() {
		return reserveInterest;
	}

	public void setReserveInterest(Integer reserveInterest) {
		this.reserveInterest = reserveInterest;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRepaymentSource() {
		return repaymentSource;
	}

	public void setRepaymentSource(String repaymentSource) {
		this.repaymentSource = repaymentSource;
	}

	public String getAssureDescription() {
		return assureDescription;
	}

	public void setAssureDescription(String assureDescription) {
		this.assureDescription = assureDescription;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getFundUse() {
		return fundUse;
	}

	public void setFundUse(String fundUse) {
		this.fundUse = fundUse;
	}

	public Integer getWithholdState() {
		return withholdState;
	}

	public void setWithholdState(Integer withholdState) {
		this.withholdState = withholdState;
	}

	public Integer getRecommendState() {
		return recommendState;
	}

	public void setRecommendState(Integer recommendState) {
		this.recommendState = recommendState;
	}

	public Integer getGivingPointState() {
		return givingPointState;
	}

	public void setGivingPointState(Integer givingPointState) {
		this.givingPointState = givingPointState;
	}

	public Integer getBourseFlg() {
		return bourseFlg;
	}

	public void setBourseFlg(Integer bourseFlg) {
		this.bourseFlg = bourseFlg;
	}

	public String getAssureType() {
		return assureType;
	}

	public void setAssureType(String assureType) {
		this.assureType = assureType;
	}

	public String getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public String getLegalFiles() {
		return legalFiles;
	}

	public void setLegalFiles(String legalFiles) {
		this.legalFiles = legalFiles;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCreditorDays() {
		return creditorDays;
	}

	public void setCreditorDays(Integer creditorDays) {
		this.creditorDays = creditorDays;
	}

	public Integer getDealRepayDays() {
		return dealRepayDays;
	}

	public void setDealRepayDays(Integer dealRepayDays) {
		this.dealRepayDays = dealRepayDays;
	}

	public BigDecimal getConvertRateStart() {
		return convertRateStart;
	}

	public void setConvertRateStart(BigDecimal convertRateStart) {
		this.convertRateStart = convertRateStart;
	}

	public BigDecimal getConvertRateEnd() {
		return convertRateEnd;
	}

	public void setConvertRateEnd(BigDecimal convertRateEnd) {
		this.convertRateEnd = convertRateEnd;
	}

	public BigDecimal getOverflowRateStart() {
		return overflowRateStart;
	}

	public void setOverflowRateStart(BigDecimal overflowRateStart) {
		this.overflowRateStart = overflowRateStart;
	}

	public BigDecimal getOverflowRateEnd() {
		return overflowRateEnd;
	}

	public void setOverflowRateEnd(BigDecimal overflowRateEnd) {
		this.overflowRateEnd = overflowRateEnd;
	}

	public Integer getMostTransferCount() {
		return mostTransferCount;
	}

	public void setMostTransferCount(Integer mostTransferCount) {
		this.mostTransferCount = mostTransferCount;
	}

	public Integer getMaturityRemind() {
		return maturityRemind;
	}

	public void setMaturityRemind(Integer maturityRemind) {
		this.maturityRemind = maturityRemind;
	}

	public String getAutidNote() {
		return autidNote;
	}

	public void setAutidNote(String autidNote) {
		this.autidNote = autidNote;
	}

	public Integer getCreditorState() {
		return creditorState;
	}

	public void setCreditorState(Integer creditorState) {
		this.creditorState = creditorState;
	}

	public BigDecimal getTransferRate() {
		return transferRate;
	}

	public void setTransferRate(BigDecimal transferRate) {
		this.transferRate = transferRate;
	}

	public Integer getBidProductId() {
		return bidProductId;
	}

	public void setBidProductId(Integer bidProductId) {
		this.bidProductId = bidProductId;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public List<Integer> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<Integer> productTypes) {
		this.productTypes = productTypes;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getAmountMin() {
		return amountMin;
	}

	public void setAmountMin(Integer amountMin) {
		this.amountMin = amountMin;
	}

	public Integer getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(Integer amountMax) {
		this.amountMax = amountMax;
	}

	public Integer getTermValueMin() {
		return termValueMin;
	}

	public void setTermValueMin(Integer termValueMin) {
		this.termValueMin = termValueMin;
	}

	public Integer getTermValueMax() {
		return termValueMax;
	}

	public void setTermValueMax(Integer termValueMax) {
		this.termValueMax = termValueMax;
	}

	public Integer getBidDeadline() {
		return bidDeadline;
	}

	public void setBidDeadline(Integer bidDeadline) {
		this.bidDeadline = bidDeadline;
	}

	public BigDecimal getRateMin() {
		return rateMin;
	}

	public void setRateMin(BigDecimal rateMin) {
		this.rateMin = rateMin;
	}

	public BigDecimal getRateMax() {
		return rateMax;
	}

	public void setRateMax(BigDecimal rateMax) {
		this.rateMax = rateMax;
	}

	public BigDecimal getRaiseRate() {
		return raiseRate;
	}

	public void setRaiseRate(BigDecimal raiseRate) {
		this.raiseRate = raiseRate;
	}

	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public List<Integer> getStates() {
		return states;
	}

	public void setStates(List<Integer> states) {
		this.states = states;
	}

	public Integer getIsMatch() {
		return isMatch;
	}

	public void setIsMatch(Integer isMatch) {
		this.isMatch = isMatch;
	}

	public Integer getStartTerm() {
		return startTerm;
	}

	public void setStartTerm(Integer startTerm) {
		this.startTerm = startTerm;
	}

	public Integer getEndTerm() {
		return endTerm;
	}

	public void setEndTerm(Integer endTerm) {
		this.endTerm = endTerm;
	}

	public Integer getBidType() {
		return bidType;
	}

	public void setBidType(Integer bidType) {
		this.bidType = bidType;
	}

	public BigDecimal getMatchingMoney() {
		return matchingMoney;
	}

	public void setMatchingMoney(BigDecimal matchingMoney) {
		this.matchingMoney = matchingMoney;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getReceiptUserName() {
		return ReceiptUserName;
	}

	public void setReceiptUserName(String receiptUserName) {
		ReceiptUserName = receiptUserName;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
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

	public Integer getHasMatch() {
		return hasMatch;
	}

	public void setHasMatch(Integer hasMatch) {
		this.hasMatch = hasMatch;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAdvanceRepayState() {
		return advanceRepayState;
	}

	public void setAdvanceRepayState(Integer advanceRepayState) {
		this.advanceRepayState = advanceRepayState;
	}

	public Integer getReturnCapDays() {
		return returnCapDays;
	}

	public void setReturnCapDays(Integer returnCapDays) {
		this.returnCapDays = returnCapDays;
	}

	public Integer getTransferDays() {
		return transferDays;
	}

	public void setTransferDays(Integer transferDays) {
		this.transferDays = transferDays;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public BigDecimal getAdvanceServiceRate() {
		return advanceServiceRate;
	}

	public void setAdvanceServiceRate(BigDecimal advanceServiceRate) {
		this.advanceServiceRate = advanceServiceRate;
	}

	public BigDecimal getLiquidatedDamagesRate() {
		return liquidatedDamagesRate;
	}

	public void setLiquidatedDamagesRate(BigDecimal liquidatedDamagesRate) {
		this.liquidatedDamagesRate = liquidatedDamagesRate;
	}

	public Boolean getModifyAble() {
		return modifyAble;
	}

	public void setModifyAble(Boolean modifyAble) {
		this.modifyAble = modifyAble;
	}

	public Integer getReturnCapWay() {
		return returnCapWay;
	}

	public void setReturnCapWay(Integer returnCapWay) {
		this.returnCapWay = returnCapWay;
	}

	public Integer getStepValue() {
		return stepValue;
	}

	public void setStepValue(Integer stepValue) {
		this.stepValue = stepValue;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}


	public String getTermValueStr() {
		return termValueStr;
	}

	public void setTermValueStr(String termValueStr) {
		this.termValueStr = termValueStr;
	}

	public String getMatchingDaysStr() {
		return matchingDaysStr;
	}

	public void setMatchingDaysStr(String matchingDaysStr) {
		this.matchingDaysStr = matchingDaysStr;
	}

	public String getMatchingDetailsStr() {
		return matchingDetailsStr;
	}

	public void setMatchingDetailsStr(String matchingDetailsStr) {
		this.matchingDetailsStr = matchingDetailsStr;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getLendingTimeStr() {
		return lendingTimeStr;
	}

	public void setLendingTimeStr(String lendingTimeStr) {
		this.lendingTimeStr = lendingTimeStr;
	}

	public String getNextMatchDateStr() {
		return nextMatchDateStr;
	}

	public void setNextMatchDateStr(String nextMatchDateStr) {
		this.nextMatchDateStr = nextMatchDateStr;
	}

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

	public Date getServerSysDate() {
		return serverSysDate;
	}

	public void setServerSysDate(Date serverSysDate) {
		this.serverSysDate = serverSysDate;
	}

	public Integer getActionScope() {
		return actionScope;
	}

	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
