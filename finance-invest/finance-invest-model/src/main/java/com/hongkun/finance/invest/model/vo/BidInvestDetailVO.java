package com.hongkun.finance.invest.model.vo;

import com.yirun.framework.core.annotation.Union;
import com.yirun.framework.core.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BidInvestDetailVO extends BaseModel{

	/**
	 * serialVersionUID:序列化
	 */
	    
	private static final long serialVersionUID = 1L;

	/********************* 投资记录信息  ************************/
	/**id*/
	private java.lang.Integer bidInvestId;
	//债权转让Id
	private Integer transferId;
	//持有天数
	private Integer buyerHoldDays;
	/**投资用户ID*/
	private java.lang.Integer regUserId;
	/**投资用户名称*/
	private java.lang.String realName;
	/**投资金额*/
	private java.math.BigDecimal investAmount;
	/**投资范围*/
	private BigDecimal investMin;
	/**投资范围*/
	private BigDecimal investMax;
	/**投资来源:0-为pc,1-为wap,2-ios,3-android*/
	private Integer investSource;
	/**状态1-投资成功,2-用户手动债券转让,3-系统自动债券转让,9-删除 */
	private Integer bidInvestState;
	/** 标的投资状态集合 **/
	private List<Integer> bidInvestStates;
	/**投资类型： 1 手动投资 2：自动投资*/
	private Integer investChannel;
	/** 预期收益 **/
	private BigDecimal exceptAmount;
	/** 转让金额 **/
	private BigDecimal transAmount;
	/** 加息券ID**/
	private Integer couponIncreaseId;
	
	/**投资方式 1：手动投资 2：自动投资**/
	private Integer investType;
	/**创建时间*/
	private java.util.Date createTime;
	
	private Integer goodBidId;
	private String goodBidName;
	private String investTerm;
	private String goodInvestTerm;
	
	private String createTimeBegin;
	private String createTimeEnd;
	@Union(bind = {"UserVO"}, reNameTo = "login")
	private String investUserTel;
	/********************* 投资记录信息  ************************/	
	
	/********************* 标的信息  ************************/
	/**标的ID*/
	private java.lang.Integer bidInfoId;
	/**标的id集合*/
	private List<Integer> bidInfoIds = new ArrayList<>();
	/**标的名称*/
	private java.lang.String bidName;
	/**标的总金额*/
	private BigDecimal totalAmount;
	/**标的剩余可投金额*/
	private BigDecimal residueAmount;
	/**期限值*/
	private Integer termValue;
	/**期限单位:1-年,2-月,3-天**/
	private Integer termUnit;
	/**年化率*/
	private java.math.BigDecimal interestRate;
	/**状态:0-初始化,1-待上架,2-投标中,3-满标待审核,4-待放款,5-放款中,6-已完成,7-上架审核拒绝,8-满标审核拒绝,9-删除*/
	private Integer bidState;
	/** 转让状态:0-不允许债权转让,1-允许债权转让 */
	private Integer transState;
	/** 标的状态集合*/
	private List<Integer> bidStates;
	/** 放款时间**/
	private Date lendingTime;
	/** 完成时间**/
	private Date finishTime;
	/** 还款方式 **/
	private Integer biddRepaymentWay;
	/** 还款方式中文描述**/
	private String biddRepaymentWayView;
	/** 作用域 **/
	private Integer actionScope;
	
	/********************* 标的信息  ************************/
	
	/********************* 产品信息  ************************/
	/**标的ID*/
	private java.lang.Integer bidProductId;
	/**产品类型 0-普通产品,1-优选产品,2-月月盈,3-季季盈,4-年年盈,5-体验金产品,6-购房宝,7-活期产品,8-活动产品,9-物业宝'*/
    private Integer bidProductType;
	/********************* 产品信息  ************************/
    /**产品类型集合*/
    private List<Integer> bidProductTypes = new ArrayList<>();
    //用户集合
    private List<Integer> regUserIdList;
	public List<Integer> getRegUserIdList() {
        return regUserIdList;
    }
    public void setRegUserIdList(List<Integer> regUserIdList) {
        this.regUserIdList = regUserIdList;
    }
    public java.lang.Integer getBidInfoId() {
		return bidInfoId;
	}
	public void setBidInfoId(java.lang.Integer bidInfoId) {
		this.bidInfoId = bidInfoId;
	}
	public java.lang.String getBidName() {
		return bidName;
	}
	public void setBidName(java.lang.String bidName) {
		this.bidName = bidName;
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
	public java.math.BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(java.math.BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getBidState() {
		return bidState;
	}

	public void setBidState(Integer bidState) {
		this.bidState = bidState;
	}

	public java.lang.Integer getBidProductId() {
		return bidProductId;
	}

	public void setBidProductId(java.lang.Integer bidProductId) {
		this.bidProductId = bidProductId;
	}

	public Integer getBidProductType() {
		return bidProductType;
	}

	public void setBidProductType(Integer bidProductType) {
		this.bidProductType = bidProductType;
	}

	public java.lang.Integer getBidInvestId() {
		return bidInvestId;
	}

	public void setBidInvestId(java.lang.Integer bidInvestId) {
		this.bidInvestId = bidInvestId;
	}

	public java.math.BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Integer getInvestSource() {
		return investSource;
	}

	public void setInvestSource(Integer investSource) {
		this.investSource = investSource;
	}

	public Integer getBidInvestState() {
		return bidInvestState;
	}

	public void setBidInvestState(Integer bidInvestState) {
		this.bidInvestState = bidInvestState;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.lang.Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(java.lang.Integer investUserId) {
		this.regUserId = investUserId;
	}

	public java.lang.String getInvestUserName() {
		return getRealName();
	}
	public java.lang.String getRealName() {
		return realName;
	}

	public void setInvestUserName(java.lang.String investUserName) {
		setRealName(investUserName);
	}
	public void setRealName(java.lang.String investUserName) {
		this.realName = investUserName;
	}

	public String getInvestTerm() {
		return investTerm;
	}

	public void setInvestTerm(String investTerm) {
		this.investTerm = investTerm;
	}

	public String getGoodInvestTerm() {
		return goodInvestTerm;
	}

	public void setGoodInvestTerm(String goodInvestTerm) {
		this.goodInvestTerm = goodInvestTerm;
	}

	public Integer getGoodBidId() {
		return goodBidId;
	}

	public void setGoodBidId(Integer goodBidId) {
		this.goodBidId = goodBidId;
	}
	public String getGoodBidName() {
		return goodBidName;
	}
	public void setGoodBidName(String goodBidName) {
		this.goodBidName = goodBidName;
	}

	public String getInvestUserTel() {
		return investUserTel;
	}

	public void setInvestUserTel(String investUserTel) {
		this.investUserTel = investUserTel;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getResidueAmount() {
		return residueAmount;
	}
	public void setResidueAmount(BigDecimal residueAmount) {
		this.residueAmount = residueAmount;
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
	public List<Integer> getBidInfoIds() {
		return bidInfoIds;
	}
	public void setBidInfoIds(List<Integer> bidInfoIds) {
		this.bidInfoIds = bidInfoIds;
	}
	public Integer getTransState() {
		return transState;
	}
	public void setTransState(Integer transState) {
		this.transState = transState;
	}
	public List<Integer> getBidProductTypes() {
		return bidProductTypes;
	}
	public void setBidProductTypes(List<Integer> bidProductTypes) {
		this.bidProductTypes = bidProductTypes;
	}
	public List<Integer> getBidStates() {
		return bidStates;
	}
	public void setBidStates(List<Integer> bidStates) {
		this.bidStates = bidStates;
	}
	public Integer getInvestType() {
		return investType;
	}
	public void setInvestType(Integer investType) {
		this.investType = investType;
	}
	public BigDecimal getInvestMin() {
		return investMin;
	}
	public void setInvestMin(BigDecimal investMin) {
		this.investMin = investMin;
	}
	public BigDecimal getInvestMax() {
		return investMax;
	}
	public void setInvestMax(BigDecimal investMax) {
		this.investMax = investMax;
	}

    public Integer getInvestChannel() {
        return investChannel;
    }

    public void setInvestChannel(Integer investChannel) {
        this.investChannel = investChannel;
    }

    public BigDecimal getExceptAmount() {
        return exceptAmount;
    }

    public void setExceptAmount(BigDecimal exceptAmount) {
        this.exceptAmount = exceptAmount;
    }

    public Integer getCouponIncreaseId() {
        return couponIncreaseId;
    }

    public void setCouponIncreaseId(Integer couponIncreaseId) {
        this.couponIncreaseId = couponIncreaseId;
    }

    public Date getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(Date lendingTime) {
        this.lendingTime = lendingTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getBiddRepaymentWay() {
        return biddRepaymentWay;
    }

    public void setBiddRepaymentWay(Integer biddRepaymentWay) {
        this.biddRepaymentWay = biddRepaymentWay;
    }

    public String getBiddRepaymentWayView() {
        return biddRepaymentWayView;
    }

    public void setBiddRepaymentWayView(String biddRepaymentWayView) {
        this.biddRepaymentWayView = biddRepaymentWayView;
    }

    public List<Integer> getBidInvestStates() {
        return bidInvestStates;
    }

    public void setBidInvestStates(List<Integer> bidInvestStates) {
        this.bidInvestStates = bidInvestStates;
    }

	public Integer getBuyerHoldDays() {
		return buyerHoldDays;
	}

	public void setBuyerHoldDays(Integer buyerHoldDays) {
		this.buyerHoldDays = buyerHoldDays;
	}

	public Integer getTransferId() {
		return transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

    public Integer getActionScope() {
        return actionScope;
    }

    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }

    public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
}
