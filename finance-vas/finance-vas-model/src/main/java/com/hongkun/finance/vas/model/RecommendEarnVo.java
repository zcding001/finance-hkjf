package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.BiddRecommendEarnVo.java
 * @Class Name : BiddRecommendEarnVo.java
 * @Description : 用于推荐奖展示的VO类
 * @Author : yanbinghuang
 */
public class RecommendEarnVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 推荐记录ID
	 */
	private Integer id;
	/**
	 * 推荐人手机号
	 */
	private Long recommedTel;
	/**
	 * 推荐人姓名
	 */
	private String recommendName;
	/**
	 * 推荐码
	 */
	private String commendNo;

	/**
	 * 被推荐人手机号
	 */
	private Long referraTel;
	/**
	 * 被推荐人姓名
	 */
	private String referraName;
	/**
	 * 好友级别
	 */
	private Integer friendLevel;
	/**
	 * 标的名称
	 */
	private String biddName;
	/**
	 * 放款时间
	 */
	private Date lendingTime;
	/**
	 * 投资金额
	 */
	private BigDecimal investAmount;
	/**
	 * 奖金金额
	 */
	private BigDecimal earnAmount;

	/**
	 * 推荐状态 0未审核 3审核成功 4审核失败
	 */
	private Integer state;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备注
	 */
	private String note;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
