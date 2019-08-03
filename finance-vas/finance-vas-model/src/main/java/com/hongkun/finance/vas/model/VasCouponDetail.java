package com.hongkun.finance.vas.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.VasCouponDetail.java
 * @Class Name    : VasCouponDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasCouponDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 接收人员ID
	 * 字段: acceptor_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer acceptorUserId;
	
	/**
	 * 描述: 激活码
	 * 字段: avt_key  VARCHAR(50)
	 */
	private String avtKey;
	
	/**
	 * 描述: 价值
	 * 字段: worth  DOUBLE(12)
	 * 默认值: 0.0000
	 */
	private BigDecimal worth;
	
	/**
	 * 描述: 券产品id
	 * 字段: coupon_product_id  INT(10)
	 * 默认值: 0
	 */
	private Integer couponProductId;
	
	/**
	 * 描述: 可使用标的产品类型
	 * 字段: bid_product_type_range  VARCHAR(11)
	 * 默认值: ''
	 */
	private String bidProductTypeRange;
	
	/**
	 * 描述: 来源:1-线下生成，2-手动派发，3-跑批生成
	 * 字段: source  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer source;
	
	/**
	 * 描述: 有效开始时间
	 * 字段: begin_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date beginTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date beginTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date beginTimeEnd;
	/**
	 * 描述: 有效结束时间
	 * 字段: end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 使用时间
	 * 字段: used_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date usedTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date usedTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date usedTimeEnd;
	

	/**
	 * 描述: 创建人员ID
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer createUserId;
	
	/**
	 * 描述: 修改人员ID
	 * 字段: modified_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer modifiedUserId;
	
	/**
	 * 描述: 赠送原因
	 * 字段: reason  VARCHAR(50)
	 * 默认值: ''
	 */
	private String reason;
	
	/**
	 * 描述: 状态:0-生成，1-已发放，2-已使用，3-已过期，4-已转赠
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 作用域:1-hkjf，2-cxj
	 * 字段: state  TINYINT(4)
	 * 默认值: 1
	 */
	private Integer actionScope;
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
	
	/** id集合 **/
	private List<Integer> ids = new ArrayList<>();
 
	public VasCouponDetail(){
	}

	public VasCouponDetail(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setAcceptorUserId(Integer acceptorUserId) {
		this.acceptorUserId = acceptorUserId;
	}
	
	public Integer getAcceptorUserId() {
		return this.acceptorUserId;
	}
	
	public void setAvtKey(String avtKey) {
		this.avtKey = avtKey;
	}
	
	public String getAvtKey() {
		return this.avtKey;
	}

	public BigDecimal getWorth() {
		return worth;
	}

	public void setWorth(BigDecimal worth) {
		this.worth = worth;
	}

	public void setCouponProductId(Integer couponProductId) {
		this.couponProductId = couponProductId;
	}
	
	public Integer getCouponProductId() {
		return this.couponProductId;
	}
	
	public void setBidProductTypeRange(String bidProductTypeRange) {
		this.bidProductTypeRange = bidProductTypeRange;
	}
	
	public String getBidProductTypeRange() {
		return this.bidProductTypeRange;
	}
	
	public void setSource(Integer source) {
		this.source = source;
	}
	
	public Integer getSource() {
		return this.source;
	}
	
	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}
	
	public java.util.Date getBeginTime() {
		return this.beginTime;
	}
	
	public void setBeginTimeBegin(java.util.Date beginTimeBegin) {
		this.beginTimeBegin = beginTimeBegin;
	}
	
	public java.util.Date getBeginTimeBegin() {
		return this.beginTimeBegin;
	}
	
	public void setBeginTimeEnd(java.util.Date beginTimeEnd) {
		this.beginTimeEnd = beginTimeEnd;
	}
	
	public java.util.Date getBeginTimeEnd() {
		return this.beginTimeEnd;
	}	
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTimeBegin(java.util.Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
	
	public java.util.Date getEndTimeBegin() {
		return this.endTimeBegin;
	}
	
	public void setEndTimeEnd(java.util.Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	
	public java.util.Date getEndTimeEnd() {
		return this.endTimeEnd;
	}	
	public void setUsedTime(java.util.Date usedTime) {
		this.usedTime = usedTime;
	}
	
	public java.util.Date getUsedTime() {
		return this.usedTime;
	}
	
	public void setUsedTimeBegin(java.util.Date usedTimeBegin) {
		this.usedTimeBegin = usedTimeBegin;
	}
	
	public java.util.Date getUsedTimeBegin() {
		return this.usedTimeBegin;
	}
	
	public void setUsedTimeEnd(java.util.Date usedTimeEnd) {
		this.usedTimeEnd = usedTimeEnd;
	}
	
	public java.util.Date getUsedTimeEnd() {
		return this.usedTimeEnd;
	}	


	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifiedUserId(Integer modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}
	
	public Integer getModifiedUserId() {
		return this.modifiedUserId;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}

	public Integer getActionScope() {
		return actionScope;
	}

	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
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

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

