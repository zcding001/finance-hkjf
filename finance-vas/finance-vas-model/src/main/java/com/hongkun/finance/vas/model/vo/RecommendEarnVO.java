package com.hongkun.finance.vas.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.annotation.Union;

/**
 * @Description :
 * @Project : finance-vas-model
 * @Program Name : com.hongkun.finance.vas.model.vo.RecommendEarnVO.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public class RecommendEarnVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 推荐记录ID */
	private Integer id;
	/** 推荐人ID */
	private Integer recommendRegUserId;
	/** 推荐人手机号 */
	@Union(bind = { "UserVO" }, reNameTo = "login")
	private Long recommedTel;
	/** 推荐人姓名 */
	@Union(bind = { "UserVO" }, reNameTo = "realName")
	private String recommendName;
	/** 推荐码 */
	private String commendNo;
	/** 投资人id */
	private Integer regUserId;
	/** 被推荐人手机号 */
	@Union(bind = { "UserVO" }, reNameTo = "login")
	private Long referraTel;
	/** 机构码 **/
	@Union(bind = { "UserVO" }, reNameTo = "groupCode")
	private String groupCode;
	/** 被推荐人姓名 */
	@Union(bind = { "UserVO" }, reNameTo = "realName")
	private String referraName;
	/** 好友级别 */
	private Integer friendLevel;
	/** 标的名称 */
	private String biddName;
	/** 放款时间 */
	private Date lendingTime;
	/** 投资金额 */
	private BigDecimal investAmount;
	/** 奖金金额 */
	private BigDecimal earnAmount;
	/** 推荐状态 0未审核 3审核成功 4审核失败 */
	private Integer state;
	/** 创建时间 */
	private Date createTime;
	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date grantTimeBegin;

	// 【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date grantTimeEnd;
	/** 备注 */
	private String note;
	// 0:佣金审核查询 1：佣金发放
	private Integer recommendState;
	// 一级好友人数
	private Integer sumFriendLevelOne;
	// 二级好友人数
	private Integer sumFriendLevelTwo;
	// 奖金总额
	private BigDecimal totalEarnAmount;
	// 推荐奖ID集合
	private List<Integer> recommendEarnIds;
	/**
	 * 期限值
	 */
	private Integer termValue;

	/**
	 * 描述: 期限单位:1-年,2-月,3-天 字段: term_unit TINYINT(3) 默认值: 2
	 */
	private Integer termUnit;
	/**
	 * 推荐类型:0-普通用户，1-物业员工,2-销售员工，3-内部员工，4-理财家
	 */
	private Integer recommendType;
	/**
	 * 投资笔数
	 */
	private Integer investCount;
	/**
	 * 推荐奖执行利率
	 */
	private BigDecimal rate;
	/**
	 * 投资时间
	 */
	private Date investTime;
	/**
	 * 折标系数
	 */
	private BigDecimal backStepRatio;
	/**
	 * 折标金额
	 */
	private BigDecimal backStepMoney;
	/**
	 * 标的利率
	 */
	private BigDecimal bidRate;
	/**
	 * 物业公司名称
	 */
	private String enterpriseName;

	public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public BigDecimal getBackStepRatio() {
		return backStepRatio;
	}

	public void setBackStepRatio(BigDecimal backStepRatio) {
		this.backStepRatio = backStepRatio;
	}

	public BigDecimal getBackStepMoney() {
		return backStepMoney;
	}

	public void setBackStepMoney(BigDecimal backStepMoney) {
		this.backStepMoney = backStepMoney;
	}

	public BigDecimal getBidRate() {
		return bidRate;
	}

	public void setBidRate(BigDecimal bidRate) {
		this.bidRate = bidRate;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

	public Integer getInvestCount() {
		return investCount;
	}

	public void setInvestCount(Integer investCount) {
		this.investCount = investCount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public List<Integer> getRecommendEarnIds() {
		return recommendEarnIds;
	}

	public void setRecommendEarnIds(List<Integer> recommendEarnIds) {
		this.recommendEarnIds = recommendEarnIds;
	}

	public Integer getRecommendState() {
		return recommendState;
	}

	public void setRecommendState(Integer recommendState) {
		this.recommendState = recommendState;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getRecommedTel() {
		return recommedTel;
	}

	public void setRecommedTel(Long recommedTel) {
		this.recommedTel = recommedTel;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public String getCommendNo() {
		return commendNo;
	}

	public void setCommendNo(String commendNo) {
		this.commendNo = commendNo;
	}

	public Long getReferraTel() {
		return referraTel;
	}

	public void setReferraTel(Long referraTel) {
		this.referraTel = referraTel;
	}

	public String getReferraName() {
		return referraName;
	}

	public void setReferraName(String referraName) {
		this.referraName = referraName;
	}

	public Integer getFriendLevel() {
		return friendLevel;
	}

	public void setFriendLevel(Integer friendLevel) {
		this.friendLevel = friendLevel;
	}

	public String getBiddName() {
		return biddName;
	}

	public void setBiddName(String biddName) {
		this.biddName = biddName;
	}

	public Date getLendingTime() {
		return lendingTime;
	}

	public void setLendingTime(Date lendingTime) {
		this.lendingTime = lendingTime;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getEarnAmount() {
		return earnAmount;
	}

	public void setEarnAmount(BigDecimal earnAmount) {
		this.earnAmount = earnAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getRecommendRegUserId() {
		return recommendRegUserId;
	}

	public void setRecommendRegUserId(Integer recommendRegUserId) {
		this.recommendRegUserId = recommendRegUserId;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public java.util.Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public java.util.Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public java.util.Date getGrantTimeBegin() {
		return grantTimeBegin;
	}

	public void setGrantTimeBegin(java.util.Date grantTimeBegin) {
		this.grantTimeBegin = grantTimeBegin;
	}

	public java.util.Date getGrantTimeEnd() {
		return grantTimeEnd;
	}

	public void setGrantTimeEnd(java.util.Date grantTimeEnd) {
		this.grantTimeEnd = grantTimeEnd;
	}

	public Integer getSumFriendLevelOne() {
		return sumFriendLevelOne;
	}

	public void setSumFriendLevelOne(Integer sumFriendLevelOne) {
		this.sumFriendLevelOne = sumFriendLevelOne;
	}

	public Integer getSumFriendLevelTwo() {
		return sumFriendLevelTwo;
	}

	public void setSumFriendLevelTwo(Integer sumFriendLevelTwo) {
		this.sumFriendLevelTwo = sumFriendLevelTwo;
	}

	public BigDecimal getTotalEarnAmount() {
		return totalEarnAmount;
	}

	public void setTotalEarnAmount(BigDecimal totalEarnAmount) {
		this.totalEarnAmount = totalEarnAmount;
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

	public Integer getRecommendType() {
		return recommendType;
	}

	public void setRecommendType(Integer recommendType) {
		this.recommendType = recommendType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
