package com.hongkun.finance.invest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.model.BidMatch.java
 * @Class Name    : BidMatch.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidMatch extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 散标标的ID
	 * 字段: comn_bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer comnBidId;
	
	/**
	 * 描述: 优选标的ID
	 * 字段: good_bid_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer goodBidId;
	
	/**
	 * 描述: 匹配金额
	 * 字段: mid_money  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal midMoney;
	
	/**
	 * 描述: 优选匹配期数
	 * 字段: good_term_value  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String goodTermValue;
	
	/**
	 * 描述: 散标匹配期数
	 * 字段: comm_term_value  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String commTermValue;
	
	/**
	 * 描述: 提前还本产生的关系:0-不是,1-是
	 * 字段: return_cap_state  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer returnCapState;
	
	/**
	 * 描述: 创建人员ID
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer createUserId;
	
	/**
	 * 描述: 最后修改人员ID
	 * 字段: modify_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer modifyUserId;
	
	/**
	 * 描述: 状态:0-初始化,1-成功,2-失败,3-已完成,9-删除,
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
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

	private List<Integer> goodIds;
	private List<Integer> commIds;
 
	public BidMatch(){
	}

	public BidMatch(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setComnBidId(java.lang.Integer comnBidId) {
		this.comnBidId = comnBidId;
	}
	
	public java.lang.Integer getComnBidId() {
		return this.comnBidId;
	}
	
	public void setGoodBidId(java.lang.Integer goodBidId) {
		this.goodBidId = goodBidId;
	}
	
	public java.lang.Integer getGoodBidId() {
		return this.goodBidId;
	}
	
	public void setMidMoney(java.math.BigDecimal midMoney) {
		this.midMoney = midMoney;
	}
	
	public java.math.BigDecimal getMidMoney() {
		return this.midMoney;
	}
	
	public void setGoodTermValue(java.lang.String goodTermValue) {
		this.goodTermValue = goodTermValue;
	}
	
	public java.lang.String getGoodTermValue() {
		return this.goodTermValue;
	}
	
	public void setCommTermValue(java.lang.String commTermValue) {
		this.commTermValue = commTermValue;
	}
	
	public java.lang.String getCommTermValue() {
		return this.commTermValue;
	}
	
	public void setReturnCapState(Integer returnCapState) {
		this.returnCapState = returnCapState;
	}
	
	public Integer getReturnCapState() {
		return this.returnCapState;
	}
	
	public void setCreateUserId(java.lang.Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public java.lang.Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifyUserId(java.lang.Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public java.lang.Integer getModifyUserId() {
		return this.modifyUserId;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
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

	public List<Integer> getGoodIds() {
		return goodIds;
	}

	public void setGoodIds(List<Integer> goodIds) {
		this.goodIds = goodIds;
	}

	public List<Integer> getCommIds() {
		return commIds;
	}

	public void setCommIds(List<Integer> commIds) {
		this.commIds = commIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

