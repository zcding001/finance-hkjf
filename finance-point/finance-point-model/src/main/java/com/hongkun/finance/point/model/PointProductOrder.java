package com.hongkun.finance.point.model;

import com.yirun.framework.core.model.BaseModel;
import com.yirun.framework.core.support.validate.SAVE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointProductOrder.java
 * @Class Name    : PointProductOrder.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointProductOrder extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: ID  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 商品id
	 * 字段: product_id  INT(10)
	 * 默认值: 0
	 */
	@NotNull(groups = SAVE.class)
	private Integer productId;
	
	/**
	 * 描述: 兑换消耗积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private Integer point;
	
	/**
	 * 描述: 兑换数量
	 * 字段: number  INT(10)
	 * 默认值: 0
	 */
	@NotNull(groups = SAVE.class)
	private Integer number;
	
	/**
	 * 描述: 总价值
	 * 字段: worth  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal worth;
	
	/**
	 * 描述: 地址信息
	 * 字段: address  VARCHAR(100)
	 * 默认值: ''
	 */
	private String address;
	
	/**
	 * 描述: 快递单号
	 * 字段: courier_no  VARCHAR(50)
	 * 默认值: ''
	 */
	private String courierNo;
	
	/**
	 * 描述: 状态：0-待审核，1-商家处理中，2-商家处理完成，3-订单已取消',
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 创建用户id
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer createUserId;
	
	/**
	 * 描述: 修改用户id
	 * 字段: modify_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer modifyUserId;

	/**
	 * 商品名称（说明：冗余字段，数据表没有）
	 */
	private String name;

	/**
	 * 配送方式（说明：冗余字段，数据表没有）
	 */
	private Integer sendWay;


	
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTimeEnd;
	//错误描述
	private String errorMsg;
 
	public PointProductOrder(){
	}

	public PointProductOrder(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}

	public Integer getSendWay() {
		return sendWay;
	}

	public void setSendWay(Integer sendWay) {
		this.sendWay = sendWay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public Integer getProductId() {
		return this.productId;
	}
	
	public void setPoint(Integer point) {
		this.point = point;
	}
	
	public Integer getPoint() {
		return this.point;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setWorth(java.math.BigDecimal worth) {
		this.worth = worth;
	}
	
	public java.math.BigDecimal getWorth() {
		return this.worth;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setCourierNo(String courierNo) {
		this.courierNo = courierNo;
	}
	
	public String getCourierNo() {
		return this.courierNo;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public Integer getModifyUserId() {
		return this.modifyUserId;
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
	
	public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

