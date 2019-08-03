package com.hongkun.finance.point.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointProduct.java
 * @Class Name    : PointProduct.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointProduct extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	public static final PointProduct EMPTY_PRODUCT = new PointProduct();
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 商品种类id
	 * 字段: product_category_id  INT(10)
	 * 默认值: 0
	 */
	private Integer productCategoryId;
	
	/**
	 * 描述: 商品名称
	 * 字段: name  VARCHAR(100)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 所需积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private Integer point;

	/**
	 * 描述: 折扣价格
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private Integer discountPoint;
	
	/**
	 * 描述: 商品价值
	 * 字段: worth  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal worth;
	
	/**
	 * 描述: 商品数量
	 * 字段: number  INT(10)
	 * 默认值: 0
	 */
	private Integer number;
	
	/**
	 * 描述: 排序号
	 * 字段: sort  INT(10)
	 * 默认值: 0
	 */
	private Integer sort;
	
	/**
	 * 描述: 商品发送方式：0-邮寄或自提，1-自提，2-邮寄，3-兑换码
	 * 字段: send_way  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer sendWay;
	
	/**
	 * 描述: 是否在ios端展示：0-不显示，1-显示
	 * 字段: ios_show  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer iosShow;
	
	/**
	 * 描述: 审核状态：0-待审核，1-待上架（审核通过），2-已上架，3-已拒绝
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 是否为推荐商品 :0-推荐商品 1 -非推荐商品
	 * 字段: recommend  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer recommend;

	/**
	 * 描述: 是否为限时抢购： 0- 非限时抢购 1-限时抢购
	 * 字段: recommend  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer flashSale;
	
	/**
	 * 描述: 描述
	 * 字段: comments  VARCHAR(500)
	 * 默认值: ''
	 */
	private String comments;
	
	/**
	 * 描述: 商品信息
	 * 字段: prduct_info  TEXT(65535)
	 */
	private String prductInfo;
	
	/**
	 * 描述: 商品详情
	 * 字段: goods_details  TEXT(65535)
	 */
	private String goodsDetails;
	
	/**
	 * 描述: 未通过原因
	 * 字段: refuse_cause  VARCHAR(200)
	 */
	private String refuseCause;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**
	 * 描述: 展示开始时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date showTimeStart;

	/**
	 * 描述: 展示结束时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date showTimeEnd;



	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 商品销售数量统计
	 * 字段: sales_count  INT(10)
	 * 默认值: 0
	 */
	private Integer salesCount;
	
 
	public PointProduct(){
	}

	public PointProduct(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	public Integer getProductCategoryId() {
		return this.productCategoryId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPoint(Integer point) {
		this.point = point;
	}
	
	public Integer getPoint() {
		return this.point;
	}
	
	public void setWorth(java.math.BigDecimal worth) {
		this.worth = worth;
	}
	
	public java.math.BigDecimal getWorth() {
		return this.worth;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getSort() {
		return this.sort;
	}
	
	public void setSendWay(Integer sendWay) {
		this.sendWay = sendWay;
	}
	
	public Integer getSendWay() {
		return this.sendWay;
	}
	
	public void setIosShow(Integer iosShow) {
		this.iosShow = iosShow;
	}
	
	public Integer getIosShow() {
		return this.iosShow;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setPrductInfo(String prductInfo) {
		this.prductInfo = prductInfo;
	}
	
	public String getPrductInfo() {
		return this.prductInfo;
	}
	
	public void setGoodsDetails(String goodsDetails) {
		this.goodsDetails = goodsDetails;
	}
	
	public String getGoodsDetails() {
		return this.goodsDetails;
	}
	
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}
	
	public String getRefuseCause() {
		return this.refuseCause;
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
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}
	
	public Integer getSalesCount() {
		return this.salesCount;
	}


	public Integer getDiscountPoint() {
		return discountPoint;
	}

	public void setDiscountPoint(Integer discountPoint) {
		this.discountPoint = discountPoint;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Integer getFlashSale() {
		return flashSale;
	}

	public void setFlashSale(Integer flashSale) {
		this.flashSale = flashSale;
	}

	public Date getShowTimeStart() {
		return showTimeStart;
	}

	public void setShowTimeStart(Date showTimeStart) {
		this.showTimeStart = showTimeStart;
	}

	public Date getShowTimeEnd() {
		return showTimeEnd;
	}

	public void setShowTimeEnd(Date showTimeEnd) {
		this.showTimeEnd = showTimeEnd;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

