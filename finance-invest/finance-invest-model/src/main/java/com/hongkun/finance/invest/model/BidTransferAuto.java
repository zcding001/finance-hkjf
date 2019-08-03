package com.hongkun.finance.invest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.BidTransferAuto.java
 * @Class Name    : BidTransferAuto.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidTransferAuto extends BaseModel {
	
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
	 * 描述: 新投资id
	 * 字段: new_invest_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer newInvestId;
	
	/**
	 * 描述: 旧投资id
	 * 字段: old_invest_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer oldInvestId;
	
	/**
	 * 描述: 转让人ID
	 * 字段: invest_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer investUserId;
	
	/**
	 * 描述: 转让金额
	 * 字段: creditor_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal creditorAmount;
	
	/**
	 * 描述: 转让购买金额
	 * 字段: transfer_amount  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transferAmount;
	
	/**
	 * 描述: 筹集天数
	 * 字段: transfer_days  INT(10)
	 */
	private java.lang.Integer transferDays;
	
	/**
	 * 描述: 转让比率
	 * 字段: transfer_rate  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal transferRate;
	
	/**
	 * 描述: 转让日期
	 * 字段: transfer_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date transferTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date transferTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date transferTimeEnd;
	/**
	 * 描述: 认购方年化利率
	 * 字段: user_rate  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal userRate;
	
	/**
	 * 描述: 购买人编码
	 * 字段: buy_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer buyUserId;
	
	/**
	 * 描述: 付款金额
	 * 字段: pay_amount  DECIMAL(11)
	 */
	private java.math.BigDecimal payAmount;
	
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
	 * 描述: 状态:0-待认购,1-认购中,2-已认购9-已驳回,
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
 
	public BidTransferAuto(){
	}

	public BidTransferAuto(java.lang.Integer id){
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
	
	public void setNewInvestId(java.lang.Integer newInvestId) {
		this.newInvestId = newInvestId;
	}
	
	public java.lang.Integer getNewInvestId() {
		return this.newInvestId;
	}
	
	public void setOldInvestId(java.lang.Integer oldInvestId) {
		this.oldInvestId = oldInvestId;
	}
	
	public java.lang.Integer getOldInvestId() {
		return this.oldInvestId;
	}
	
	public void setInvestUserId(java.lang.Integer investUserId) {
		this.investUserId = investUserId;
	}
	
	public java.lang.Integer getInvestUserId() {
		return this.investUserId;
	}
	
	public void setCreditorAmount(java.math.BigDecimal creditorAmount) {
		this.creditorAmount = creditorAmount;
	}
	
	public java.math.BigDecimal getCreditorAmount() {
		return this.creditorAmount;
	}
	
	public void setTransferAmount(java.math.BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	public java.math.BigDecimal getTransferAmount() {
		return this.transferAmount;
	}
	
	public void setTransferDays(java.lang.Integer transferDays) {
		this.transferDays = transferDays;
	}
	
	public java.lang.Integer getTransferDays() {
		return this.transferDays;
	}
	
	public void setTransferRate(java.math.BigDecimal transferRate) {
		this.transferRate = transferRate;
	}
	
	public java.math.BigDecimal getTransferRate() {
		return this.transferRate;
	}
	
	public void setTransferTime(java.util.Date transferTime) {
		this.transferTime = transferTime;
	}
	
	public java.util.Date getTransferTime() {
		return this.transferTime;
	}
	
	public void setTransferTimeBegin(java.util.Date transferTimeBegin) {
		this.transferTimeBegin = transferTimeBegin;
	}
	
	public java.util.Date getTransferTimeBegin() {
		return this.transferTimeBegin;
	}
	
	public void setTransferTimeEnd(java.util.Date transferTimeEnd) {
		this.transferTimeEnd = transferTimeEnd;
	}
	
	public java.util.Date getTransferTimeEnd() {
		return this.transferTimeEnd;
	}	
	public void setUserRate(java.math.BigDecimal userRate) {
		this.userRate = userRate;
	}
	
	public java.math.BigDecimal getUserRate() {
		return this.userRate;
	}
	
	public void setBuyUserId(java.lang.Integer buyUserId) {
		this.buyUserId = buyUserId;
	}
	
	public java.lang.Integer getBuyUserId() {
		return this.buyUserId;
	}
	
	public void setPayAmount(java.math.BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	
	public java.math.BigDecimal getPayAmount() {
		return this.payAmount;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

