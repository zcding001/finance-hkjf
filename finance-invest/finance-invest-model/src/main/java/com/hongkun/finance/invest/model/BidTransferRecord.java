package com.hongkun.finance.invest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.BidTransferRecord.java
 * @Class Name    : BidTransferRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class BidTransferRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 债券转让标的id（优选）
	 * 字段: transferbidid  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer transferbidid;
	
	/**
	 * 描述: 接收债券标的id（优选）
	 * 字段: receivebidid  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer receivebidid;
	
	/**
	 * 描述: 对应的散标id
	 * 字段: commonbidid  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer commonbidid;
	
	/**
	 * 描述: 转让金额
	 * 字段: transfer_amount  DECIMAL(10)
	 * 默认值: 0
	 */
	private java.math.BigDecimal transferAmount;
	
	/**
	 * 描述: 转让时间
	 * 字段: transfer_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date transferTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date transferTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date transferTimeEnd;
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
 
	public BidTransferRecord(){
	}

	public BidTransferRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setTransferbidid(java.lang.Integer transferbidid) {
		this.transferbidid = transferbidid;
	}
	
	public java.lang.Integer getTransferbidid() {
		return this.transferbidid;
	}
	
	public void setReceivebidid(java.lang.Integer receivebidid) {
		this.receivebidid = receivebidid;
	}
	
	public java.lang.Integer getReceivebidid() {
		return this.receivebidid;
	}
	
	public void setCommonbidid(java.lang.Integer commonbidid) {
		this.commonbidid = commonbidid;
	}
	
	public java.lang.Integer getCommonbidid() {
		return this.commonbidid;
	}
	
	public void setTransferAmount(java.math.BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	public java.math.BigDecimal getTransferAmount() {
		return this.transferAmount;
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

