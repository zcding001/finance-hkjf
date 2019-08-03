package com.hongkun.finance.invest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.BidInvest.java
 * @Class Name    : BidInvest.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidInvest extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 标的ID
	 * 字段: bid_info_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer bidInfoId;
	
	/**
	 * 描述: 投资用户ID
	 * 字段: invest_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 投资用户名称
	 * 字段: invest_user_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String realName;
	
	/**
	 * 描述: 投资金额
	 * 字段: invest_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 实际支付金额
	 * 字段: pay_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal payAmount;
	
	/**
	 * 描述: 转让金额
	 * 字段: trans_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transAmount;
	
	/**
	 * 描述: 充值卡ID
	 * 字段: coupon_id_k  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer couponIdK;
	
	/**
	 * 描述: 加息劵ID
	 * 字段: coupon_id_j  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer couponIdJ;
	
	/**
	 * 描述: 投资来源:0-为pc,1-为wap,2-ios,3-android
	 * 字段: invest_source  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer investSource;
	
	/**
	 * 描述: 转让状态:0-不允许债权转让,1-允许债权转让
	 * 字段: trans_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer transState;
	
	/**
	 * 描述: 已匹配金额(优选标的已匹配金额)
	 * 字段: match_amount  DOUBLE(20)
	 * 默认值: 0.00
	 */
	private java.lang.Double matchAmount;
	
	/**
	 * 描述: 匹配状态，0未匹配，1已匹配，2部分匹配
	 * 字段: match_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer matchState;
	
	/**
	 * 描述: 投资类型:1-直投,2-匹配投资
	 * 字段: invest_channel  INT(10)
	 * 默认值: 1
	 */
	private java.lang.Integer investChannel;
	
	/**
	 * 描述: 投资期数(包括起始期数和结束期数，中间用冒号分隔)
	 * 字段: invest_term  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String investTerm;
	
	/**
	 * 描述: 匹配的优选标ID
	 * 字段: good_bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer goodBidId;
	
	/**
	 * 描述: 优选标投资期数(包括起始期数和结束期数，中间用冒号分隔)
	 * 字段: good_invest_term  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String goodInvestTerm;
	
	/**
	 * 描述: 优选标投资ID
	 * 字段: good_invest_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer goodInvestId;
	
	/**
	 * 描述: 投资方式  1：手动投资 2：自动投资
	 * 字段: invest_type  TINYINT(2)
	 * 默认值: 1
	 */
	private java.lang.Integer investType;
	
	/**
	 * 描述: 保全号
	 * 字段: ancun_no  VARCHAR(40)
	 * 默认值: ''
	 */
	private java.lang.String ancunNo;
	
	/**
	 * 描述: 状态1-投资成功,2-用户手动债券转让,3-系统自动债券转让,9-删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;

    /**
     * 描述: 状态1-hkjf,2-cxj
     * 字段: state  TINYINT(4)
     * 默认值: 1
     */
    private Integer actionScope;

	//【非数据库字段，查询时使用】
	private List<Integer> stateList;
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
	//【非数据库字段，加息券利率】
	private java.math.BigDecimal couponWorthJ;

	private List<Integer> ids;
 
	public BidInvest(){
	}

	public BidInvest(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setBidInfoId(java.lang.Integer bidInfoId) {
		this.bidInfoId = bidInfoId;
	}
	
	public java.lang.Integer getBidInfoId() {
		return this.bidInfoId;
	}
	
	public void setRegUserId(java.lang.Integer investUserId) {
		this.regUserId = investUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setInvestUserName(java.lang.String investUserName) {
		setRealName(investUserName);
	}

	public void setRealName(java.lang.String investUserName) {
		this.realName = investUserName;
	}
	
	public java.lang.String getInvestUserName() {
		return getRealName();
	}

	public java.lang.String getRealName() {
		return this.realName;
	}
	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setPayAmount(java.math.BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	
	public java.math.BigDecimal getPayAmount() {
		return this.payAmount;
	}
	
	public void setTransAmount(java.math.BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	
	public java.math.BigDecimal getTransAmount() {
		return this.transAmount;
	}
	
	public void setCouponIdK(java.lang.Integer couponIdK) {
		this.couponIdK = couponIdK;
	}
	
	public java.lang.Integer getCouponIdK() {
		return this.couponIdK;
	}
	
	public void setCouponIdJ(java.lang.Integer couponIdJ) {
		this.couponIdJ = couponIdJ;
	}
	
	public java.lang.Integer getCouponIdJ() {
		return this.couponIdJ;
	}
	
	public void setInvestSource(Integer investSource) {
		this.investSource = investSource;
	}
	
	public Integer getInvestSource() {
		return this.investSource;
	}
	
	public void setTransState(Integer transState) {
		this.transState = transState;
	}
	
	public Integer getTransState() {
		return this.transState;
	}
	
	public void setMatchAmount(java.lang.Double matchAmount) {
		this.matchAmount = matchAmount;
	}
	
	public java.lang.Double getMatchAmount() {
		return this.matchAmount;
	}
	
	public void setMatchState(Integer matchState) {
		this.matchState = matchState;
	}
	
	public Integer getMatchState() {
		return this.matchState;
	}
	
	public void setInvestChannel(java.lang.Integer investChannel) {
		this.investChannel = investChannel;
	}
	
	public java.lang.Integer getInvestChannel() {
		return this.investChannel;
	}
	
	public void setInvestTerm(java.lang.String investTerm) {
		this.investTerm = investTerm;
	}
	
	public java.lang.String getInvestTerm() {
		return this.investTerm;
	}
	
	public void setGoodBidId(java.lang.Integer goodBidId) {
		this.goodBidId = goodBidId;
	}
	
	public java.lang.Integer getGoodBidId() {
		return this.goodBidId;
	}
	
	public void setGoodInvestTerm(java.lang.String goodInvestTerm) {
		this.goodInvestTerm = goodInvestTerm;
	}
	
	public java.lang.String getGoodInvestTerm() {
		return this.goodInvestTerm;
	}
	
	public void setGoodInvestId(java.lang.Integer goodInvestId) {
		this.goodInvestId = goodInvestId;
	}
	
	public java.lang.Integer getGoodInvestId() {
		return this.goodInvestId;
	}
	
	public void setAncunNo(java.lang.String ancunNo) {
		this.ancunNo = ancunNo;
	}
	
	public java.lang.String getAncunNo() {
		return this.ancunNo;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }

    public Integer getActionScope() {
        return this.actionScope;
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
	
	public java.lang.Integer getInvestType() {
		return investType;
	}

	public void setInvestType(java.lang.Integer investType) {
		this.investType = investType;
	}
	public java.math.BigDecimal getCouponWorthJ() {
		return couponWorthJ;
	}

	public void setCouponWorthJ(java.math.BigDecimal couponWorthJ) {
		this.couponWorthJ = couponWorthJ;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

