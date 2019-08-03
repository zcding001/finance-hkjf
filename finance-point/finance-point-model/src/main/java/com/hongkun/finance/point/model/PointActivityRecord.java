package com.hongkun.finance.point.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointActivityRecord.java
 * @Class Name    : PointActivityRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointActivityRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: ID  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 商品id
	 * 字段: product_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer productId;
	
	/**
	 * 描述: 活动积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer point;
	
	/**
	 * 描述: 活动开始时间
	 * 字段: begin_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date beginTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date beginTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date beginTimeEnd;
	/**
	 * 描述: 活动结束时间
	 * 字段: end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 活动销量
	 * 字段: sales  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer sales;
	
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
	//商品名称
	private String productName;
	//商品分类
	private String productCategoryName; 
 
	public PointActivityRecord(){
	}

	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public PointActivityRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setProductId(java.lang.Integer productId) {
		this.productId = productId;
	}
	
	public java.lang.Integer getProductId() {
		return this.productId;
	}
	
	public void setPoint(java.lang.Integer point) {
		this.point = point;
	}
	
	public java.lang.Integer getPoint() {
		return this.point;
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
	public void setSales(java.lang.Integer sales) {
		this.sales = sales;
	}
	
	public java.lang.Integer getSales() {
		return this.sales;
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

