package com.hongkun.finance.point.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointRecord.java
 * @Class Name    : PointRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 来源：投资对应的投资记录的id，积分兑换对应的兑换记录id，转赠收入对应发送人id，转赠支出对应接收人id，积分支付对应商户id，物业抵扣对应物业账户id
	 * 字段: business_id  INT(10)
	 * 默认值: 0
	 */
	private Integer businessId;
	
	/**
	 * 描述: 积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private Integer point;
	
	/**
	 * 描述: 类型：1-投资，2-平台赠送，3-积分兑换，4-转赠收入，5-转赠支出，6-积分支付，7-签到，8-活动赠送，9-物业抵扣
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 手续费（积分）
	 * 字段: fee  INT(10)
	 * 默认值: 0
	 */
	private Integer fee;
	
	/**
	 * 描述: 手续费价值（人民币）
	 * 字段: fee_worth  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal feeWorth;
	
	/**
	 * 描述: 积分价值（人民币）
	 * 字段: worth  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal worth;
	
	/**
	 * 描述: 积分实际价值（人民币）
	 * 字段: real_worth  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal realWorth;
	
	/**
	 * 描述: 交易信息描述
	 * 字段: comments  VARCHAR(200)
	 * 默认值: ''
	 */
	private String comments;
	
	/**
	 * 描述: 状态：0-已确认，1-待审核，2-审核失败
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 拒绝原因
	 * 字段: refuse_cause  VARCHAR(200)
	 * 默认值: ''
	 */
	private String refuseCause;
	
	/**
	 * 描述: 兑换平台：1-鸿坤金服，2-前生活
	 * 字段: platform  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer platform;


	private Integer createUserId;
	private Integer modifyUserId;

	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
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
	//状态集合
	private List<Integer> stateList;
	//积分支付人手机号【非数据库字段，收款统计 使用】
	private String buyersTel;
	//积分支付人姓名【非数据库字段，收款统计 使用】
	private String buyersName;
	//商户收银员手机号【非数据库字段，收款统计 使用】
	private String bussinessTel;
	//商户名字【非数据库字段，收款统计 使用】
	private String bussinessName;
	
	public PointRecord(){
	}
	
	public List<Integer> getStateList() {
        return stateList;
    }

    public void setStateList(List<Integer> stateList) {
        this.stateList = stateList;
    }

    public PointRecord(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	
	public Integer getBusinessId() {
		return this.businessId;
	}
	
	public void setPoint(Integer point) {
		this.point = point;
	}
	
	public Integer getPoint() {
		return this.point;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	
	public Integer getFee() {
		return this.fee;
	}
	
	public void setFeeWorth(java.math.BigDecimal feeWorth) {
		this.feeWorth = feeWorth;
	}
	
	public java.math.BigDecimal getFeeWorth() {
		return this.feeWorth;
	}
	
	public void setWorth(java.math.BigDecimal worth) {
		this.worth = worth;
	}
	
	public java.math.BigDecimal getWorth() {
		return this.worth;
	}
	
	public void setRealWorth(java.math.BigDecimal realWorth) {
		this.realWorth = realWorth;
	}
	
	public java.math.BigDecimal getRealWorth() {
		return this.realWorth;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}
	
	public String getRefuseCause() {
		return this.refuseCause;
	}
	
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	
	public Integer getPlatform() {
		return this.platform;
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

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getBuyersTel() {
		return buyersTel;
	}

	public void setBuyersTel(String buyersTel) {
		this.buyersTel = buyersTel;
	}

	public String getBuyersName() {
		return buyersName;
	}

	public void setBuyersName(String buyersName) {
		this.buyersName = buyersName;
	}

	public String getBussinessTel() {
		return bussinessTel;
	}

	public void setBussinessTel(String bussinessTel) {
		this.bussinessTel = bussinessTel;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}
	
	
	
}

