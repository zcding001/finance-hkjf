package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 标的统计查询
 *
 * @author zc.ding
 * @create 2018/9/19
 */
public class StaFunBidVO extends BaseModel {

    /**
     * id
     */
    private Integer id;
    /**
     * 描述: 标的名称
     */
    private java.lang.String name;
    /**
     * 预期年化收益率
     */
    private java.math.BigDecimal interestRate;
    /**
     * 投资期限
     */
    private Integer termValue;
    /**
     * 投资单位
     */
    private Integer termUnit;
    /**
     * 描述: 标的总金额
     */
    private java.math.BigDecimal totalAmount;
    /***
     * 标的状态
     */
    private Integer state;
    
    /**
     * 标的类型
     */
    private Integer type;

    /**
     * 投标开始时间
     */
    private Date startTime;
    /**
    * 投标结束时间
    */
    private Date endTime;

    /**
    * 项目显示位置
    */
    private String showPosition;
    /**
     * 项目投资位置
     */
    private String investPosition;
    /**
     * app标签内容
     */
    private String labelText;
    /**
     * 借款用途
     */
    private String loanUse;
    /**
     * 还款方式
     */
    private Integer biddRepaymentWay;
    /**
     * 放款时间
     */
    private Date lendingTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 借款手续费率
     */
    private Integer commissionRate;
    /**
     * 服务费率
     */
    private Integer serviceRate;
    /**
     * 预留利息
     */
    private Integer reserveInterest;
    /**
     * 提前还本
     */
    private Integer advanceRepayState;
    /**
     * 赠送积分
     */
    private Integer givingPointState;
    /**
     * 好友推荐
     */
    private Integer recommendState;
    /**
     * 借款人
     */
    private Integer borrowerId;
    /**
     * 借款人手机号
     */
    private long login;
    /**
     * 借款真实姓名
     */
    private String realName;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
    * 产品名称
    */
    private String bidProductName;
    /**
     * 产品类型
     */
    private Integer productType;
    
    /**
    * 总利息
    */
    private BigDecimal interestAmount;
    /**
     * 已还本金
     */
    private BigDecimal repayedcaptailAmount;
    /**
     * 已还利息
     */
    private BigDecimal repayedInterestAmount;
    /**
     * 最近已还款时间
     */
    private Date latestRepayedTime;
    
    private String createTimeBegin;
    private String createTimeEnd;
    //作用域：1-hkjf, 2-cxj
    private Integer actionScope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getLoanUse() {
        return loanUse;
    }

    public void setLoanUse(String loanUse) {
        this.loanUse = loanUse;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public Date getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(Date lendingTime) {
        this.lendingTime = lendingTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Integer commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Integer getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Integer serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Integer getReserveInterest() {
        return reserveInterest;
    }

    public void setReserveInterest(Integer reserveInterest) {
        this.reserveInterest = reserveInterest;
    }

    public Integer getAdvanceRepayState() {
        return advanceRepayState;
    }

    public void setAdvanceRepayState(Integer advanceRepayState) {
        this.advanceRepayState = advanceRepayState;
    }

    public Integer getGivingPointState() {
        return givingPointState;
    }

    public void setGivingPointState(Integer givingPointState) {
        this.givingPointState = givingPointState;
    }

    public Integer getRecommendState() {
        return recommendState;
    }

    public void setRecommendState(Integer recommendState) {
        this.recommendState = recommendState;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public long getLogin() {
        return login;
    }

    public void setLogin(long login) {
        this.login = login;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getBidProductName() {
        return bidProductName;
    }

    public void setBidProductName(String bidProductName) {
        this.bidProductName = bidProductName;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
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

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getRepayedcaptailAmount() {
        return repayedcaptailAmount;
    }

    public void setRepayedcaptailAmount(BigDecimal repayedcaptailAmount) {
        this.repayedcaptailAmount = repayedcaptailAmount;
    }

    public BigDecimal getRepayedInterestAmount() {
        return repayedInterestAmount;
    }

    public void setRepayedInterestAmount(BigDecimal repayedInterestAmount) {
        this.repayedInterestAmount = repayedInterestAmount;
    }

    public Date getLatestRepayedTime() {
        return latestRepayedTime;
    }

    public void setLatestRepayedTime(Date latestRepayedTime) {
        this.latestRepayedTime = latestRepayedTime;
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
