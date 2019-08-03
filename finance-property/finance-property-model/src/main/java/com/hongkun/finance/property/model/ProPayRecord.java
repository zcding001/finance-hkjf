package com.hongkun.finance.property.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.model.ProPayRecord.java
 * @Class Name    : ProPayRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class ProPayRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 地址id
	 * 字段: address_id  INT(10)
	 */
	private java.lang.Integer addressId;
	
	/**
	 * 描述: 用户id(缴物业费人的)
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 物业费开始时间
	 * 字段: start_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date startTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date startTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date startTimeEnd;
	/**
	 * 描述: 物业费结束时间
	 * 字段: end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 缴费金额
	 * 字段: money  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 描述: 可用余额缴费金额
	 * 字段: useable_money  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal useableMoney;
	
	/**
	 * 描述: 充值金额
	 * 字段: recharge_money  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal rechargeMoney;
	
	/**
	 * 描述: 充值流水flow_id
	 * 字段: recharge_flow_id  VARCHAR(40)
	 * 默认值: ''
	 */
	private java.lang.String rechargeFlowId;
	
	/**
	 * 描述: 抵扣积分
	 * 字段: point  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer point;
	
	/**
	 * 描述: 积分抵扣金额
	 * 字段: pointMoney  DECIMAL(12)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal pointMoney;
	
	/**
	 * 描述: 抵扣积分记录id
	 * 字段: point_record_id  VARCHAR(36)
	 * 默认值: ''
	 */
	private java.lang.String pointRecordId;
	
	/**
	 * 描述: 0缴费中,1已冻结(未审核),2缴费失败,3审核成功,4审核拒绝,9逻辑删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 审核理由
	 * 字段: review_reason  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String reviewReason;
	
	/**
	 * 描述: 创建时间
	 * 字段: created_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createdTime;
	
	//【非数据库字段，查询时使用】
	private String createdTimeBegin;
	
	//【非数据库字段，查询时使用】
	private String createdTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private String modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private String modifyTimeEnd;
	/**
	 * 描述: 1物业费,2车位费
	 * 字段: pay_type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer payType;
	
	/**
	 * 描述: 客户留言
	 * 字段: note  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String note;
	
	/**
	 * 描述: 物业公司的id
	 * 字段: pro_id  INT(10)
	 */
	private java.lang.Integer proId;
	
	/**
	 * 描述: 物业公司的name
	 * 字段: pro_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String proName;
	
	/**
	 * 描述: 小区id
	 * 字段: community_id  INT(10)
	 */
	private java.lang.Integer communityId;
	
	/**
	 * 描述: 小区名字
	 * 字段: community_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String communityName;
	
	/**
	 * 描述: 具体地址
	 * 字段: address  VARCHAR(255)
	 */
	private java.lang.String address;
	
	/**
	 * 描述: 用户name(缴物业费人的)
	 * 字段: user_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String userName;
	
	private String tel;
	/**payType 描述说明 **/
	private String payTypeDesc;
	/**state 中文描述说明 **/
	private String stateDesc;
	private String createdTimeStr;
	
	public String getCreatedTimeStr() {
		return createdTimeStr;
	}

	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}

	private List<Integer> userIds;
	
	public ProPayRecord(){
	}
	
	public ProPayRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setAddressId(java.lang.Integer addressId) {
		this.addressId = addressId;
	}
	
	public java.lang.Integer getAddressId() {
		return this.addressId;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTimeBegin(java.util.Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}
	
	public java.util.Date getStartTimeBegin() {
		return this.startTimeBegin;
	}
	
	public void setStartTimeEnd(java.util.Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}
	
	public java.util.Date getStartTimeEnd() {
		return this.startTimeEnd;
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
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	public java.math.BigDecimal getMoney() {
		return this.money;
	}
	
	public void setUseableMoney(java.math.BigDecimal useableMoney) {
		this.useableMoney = useableMoney;
	}
	
	public java.math.BigDecimal getUseableMoney() {
		return this.useableMoney;
	}
	
	public void setRechargeMoney(java.math.BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}
	
	public java.math.BigDecimal getRechargeMoney() {
		return this.rechargeMoney;
	}
	
	public void setRechargeFlowId(java.lang.String rechargeFlowId) {
		this.rechargeFlowId = rechargeFlowId;
	}
	
	public java.lang.String getRechargeFlowId() {
		return this.rechargeFlowId;
	}
	
	public void setPoint(java.lang.Integer point) {
		this.point = point;
	}
	
	public java.lang.Integer getPoint() {
		return this.point;
	}
	
	public void setPointMoney(java.math.BigDecimal pointMoney) {
		this.pointMoney = pointMoney;
	}
	
	public java.math.BigDecimal getPointMoney() {
		return this.pointMoney;
	}
	
	public void setPointRecordId(java.lang.String pointRecordId) {
		this.pointRecordId = pointRecordId;
	}
	
	public java.lang.String getPointRecordId() {
		return this.pointRecordId;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setReviewReason(java.lang.String reviewReason) {
		this.reviewReason = reviewReason;
	}
	
	public java.lang.String getReviewReason() {
		return this.reviewReason;
	}
	
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	
	public void setCreatedTimeBegin(String createdTimeBegin) {
		this.createdTimeBegin = createdTimeBegin;
	}
	
	public String getCreatedTimeBegin() {
		return this.createdTimeBegin;
	}
	
	public void setCreatedTimeEnd(String createdTimeEnd) {
		this.createdTimeEnd = createdTimeEnd;
	}
	
	public String getCreatedTimeEnd() {
		return this.createdTimeEnd;
	}	
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTimeBegin(String modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}
	
	public String getModifyTimeBegin() {
		return this.modifyTimeBegin;
	}
	
	public void setModifyTimeEnd(String modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}
	
	public String getModifyTimeEnd() {
		return this.modifyTimeEnd;
	}	
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	public Integer getPayType() {
		return this.payType;
	}
	
	public void setNote(java.lang.String note) {
		this.note = note;
	}
	
	public java.lang.String getNote() {
		return this.note;
	}
	
	public void setProId(java.lang.Integer proId) {
		this.proId = proId;
	}
	
	public java.lang.Integer getProId() {
		return this.proId;
	}
	
	public void setProName(java.lang.String proName) {
		this.proName = proName;
	}
	
	public java.lang.String getProName() {
		return this.proName;
	}
	
	public void setCommunityId(java.lang.Integer communityId) {
		this.communityId = communityId;
	}
	
	public java.lang.Integer getCommunityId() {
		return this.communityId;
	}
	
	public void setCommunityName(java.lang.String communityName) {
		this.communityName = communityName;
	}
	
	public java.lang.String getCommunityName() {
		return this.communityName;
	}
	
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	public java.lang.String getAddress() {
		return this.address;
	}

	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPayTypeDesc() {
		return payTypeDesc;
	}
	
	public void setPayTypeDesc(String payTypeDesc) {
		this.payTypeDesc = payTypeDesc;
	}
	
	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	
	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

