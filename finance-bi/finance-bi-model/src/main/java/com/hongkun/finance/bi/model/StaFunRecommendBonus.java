package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaFunRecommendBonus.java
 * @Class Name    : StaFunRecommendBonus.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaFunRecommendBonus extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 投资人(被推荐人)
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 投资人(被推荐人真实姓名)
	 * 字段: real_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String realName;
	
	/**
	 * 描述: 投资人手机号(被推荐人手机号)
	 * 字段: login  BIGINT(19)
	 * 默认值: 0
	 */
	private java.lang.Long login;
	
	/**
	 * 描述: 推荐人
	 * 字段: recommend_reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer recommendRegUserId;
	
	/**
	 * 描述: 推荐人真实姓名
	 * 字段: recommend_real_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String recommendRealName;
	
	/**
	 * 描述: 推荐人手机号
	 * 字段: recommend_login  BIGINT(19)
	 * 默认值: 0
	 */
	private java.lang.Long recommendLogin;
	
	/**
	 * 描述: 推荐码
	 * 字段: recommend_no  VARCHAR(8)
	 * 默认值: ''
	 */
	private java.lang.String recommendNo;
	
	/**
	 * 描述: 好友级别  1,2
	 * 字段: friend_level  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer friendLevel;
	
	/**
	 * 描述: 标的标识
	 * 字段: bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer bidId;
	
	/**
	 * 描述: 标的名称
	 * 字段: bid_name  VARCHAR(255)
	 * 默认值: ''
	 */
	private java.lang.String bidName;
	
	/**
	 * 描述: 期限单位:1-年,2-月,3-天
	 * 字段: term_unit  TINYINT(3)
	 * 默认值: 2
	 */
	private Integer termUnit;
	
	/**
	 * 描述: 投资期数
	 * 字段: term_value  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer termValue;
	
	/**
	 * 描述: 年化率
	 * 字段: rate  DECIMAL(4)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal rate;
	
	/**
	 * 描述: 投资时间
	 * 字段: invest_time  DATETIME(19)
	 */
	private java.util.Date investTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date investTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date investTimeEnd;
	/**
	 * 描述: 投资金额
	 * 字段: invest_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal investAmount;
	
	/**
	 * 描述: 投资笔数
	 * 字段: invest_num  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer investNum;
	
	/**
	 * 描述: 奖金
	 * 字段: bonus  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal bonus;
	
	/**
	 * 描述: 折标系数
	 * 字段: discount_num  DECIMAL(4)
	 * 默认值: 1.00
	 */
	private java.math.BigDecimal discountNum;
	
	/**
	 * 描述: discountAmount
	 * 字段: discount_amount  DECIMAL(10)
	 */
	private java.math.BigDecimal discountAmount;
	
	/**
	 * 描述: 推荐类型  1: 普通用户 2:内部员工 3:销售
	 * 字段: recommendType  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer recommendType;
	
	/**
	 * 描述: 执行利率
	 * 字段: exec_rate  DECIMAL(4)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal execRate;
	
	/**
	 * 描述: createTime
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")	
    private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTimeEnd;
 
	public StaFunRecommendBonus(){
	}

	public StaFunRecommendBonus(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}
	
	public java.lang.String getRealName() {
		return this.realName;
	}
	
	public void setLogin(java.lang.Long login) {
		this.login = login;
	}
	
	public java.lang.Long getLogin() {
		return this.login;
	}
	
	public void setRecommendRegUserId(java.lang.Integer recommendRegUserId) {
		this.recommendRegUserId = recommendRegUserId;
	}
	
	public java.lang.Integer getRecommendRegUserId() {
		return this.recommendRegUserId;
	}
	
	public void setRecommendRealName(java.lang.String recommendRealName) {
		this.recommendRealName = recommendRealName;
	}
	
	public java.lang.String getRecommendRealName() {
		return this.recommendRealName;
	}
	
	public void setRecommendLogin(java.lang.Long recommendLogin) {
		this.recommendLogin = recommendLogin;
	}
	
	public java.lang.Long getRecommendLogin() {
		return this.recommendLogin;
	}
	
	public void setRecommendNo(java.lang.String recommendNo) {
		this.recommendNo = recommendNo;
	}
	
	public java.lang.String getRecommendNo() {
		return this.recommendNo;
	}
	
	public void setFriendLevel(Integer friendLevel) {
		this.friendLevel = friendLevel;
	}
	
	public Integer getFriendLevel() {
		return this.friendLevel;
	}
	
	public void setBidId(java.lang.Integer bidId) {
		this.bidId = bidId;
	}
	
	public java.lang.Integer getBidId() {
		return this.bidId;
	}
	
	public void setBidName(java.lang.String bidName) {
		this.bidName = bidName;
	}
	
	public java.lang.String getBidName() {
		return this.bidName;
	}
	
	public void setTermUnit(Integer termUnit) {
		this.termUnit = termUnit;
	}
	
	public Integer getTermUnit() {
		return this.termUnit;
	}
	
	public void setTermValue(Integer termValue) {
		this.termValue = termValue;
	}
	
	public Integer getTermValue() {
		return this.termValue;
	}
	
	public void setRate(java.math.BigDecimal rate) {
		this.rate = rate;
	}
	
	public java.math.BigDecimal getRate() {
		return this.rate;
	}
	
	public void setInvestTime(java.util.Date investTime) {
		this.investTime = investTime;
	}
	
	public java.util.Date getInvestTime() {
		return this.investTime;
	}
	
	public void setInvestTimeBegin(java.util.Date investTimeBegin) {
		this.investTimeBegin = investTimeBegin;
	}
	
	public java.util.Date getInvestTimeBegin() {
		return this.investTimeBegin;
	}
	
	public void setInvestTimeEnd(java.util.Date investTimeEnd) {
		this.investTimeEnd = investTimeEnd;
	}
	
	public java.util.Date getInvestTimeEnd() {
		return this.investTimeEnd;
	}	
	public void setInvestAmount(java.math.BigDecimal investAmount) {
		this.investAmount = investAmount;
	}
	
	public java.math.BigDecimal getInvestAmount() {
		return this.investAmount;
	}
	
	public void setInvestNum(java.lang.Integer investNum) {
		this.investNum = investNum;
	}
	
	public java.lang.Integer getInvestNum() {
		return this.investNum;
	}
	
	public void setBonus(java.math.BigDecimal bonus) {
		this.bonus = bonus;
	}
	
	public java.math.BigDecimal getBonus() {
		return this.bonus;
	}
	
	public void setDiscountNum(java.math.BigDecimal discountNum) {
		this.discountNum = discountNum;
	}
	
	public java.math.BigDecimal getDiscountNum() {
		return this.discountNum;
	}
	
	public void setDiscountAmount(java.math.BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	public java.math.BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}
	
	public Integer getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Integer recommendType) {
        this.recommendType = recommendType;
    }

    public void setExecRate(java.math.BigDecimal execRate) {
		this.execRate = execRate;
	}
	
	public java.math.BigDecimal getExecRate() {
		return this.execRate;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

