package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.VasCouponDonationRecord.java
 * @Class Name    : VasCouponDonationRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasCouponDonationRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 卡券发送人
	 * 字段: send_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sendUserId;
	
	/**
	 * 描述: 卡券接受人
	 * 字段: receive_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer receiveUserId;
	
	/**
	 * 描述: 卡券详情id
	 * 字段: coupon_detail_id  INT(10)
	 * 默认值: 0
	 */
	private Integer couponDetailId;
	
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
 
	public VasCouponDonationRecord(){
	}

	public VasCouponDonationRecord(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setSendUserId(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	public Integer getSendUserId() {
		return this.sendUserId;
	}
	
	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	
	public Integer getReceiveUserId() {
		return this.receiveUserId;
	}
	
	public void setCouponDetailId(Integer couponDetailId) {
		this.couponDetailId = couponDetailId;
	}
	
	public Integer getCouponDetailId() {
		return this.couponDetailId;
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

