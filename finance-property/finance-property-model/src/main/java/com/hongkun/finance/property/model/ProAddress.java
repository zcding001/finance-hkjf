package com.hongkun.finance.property.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.model.ProAddress.java
 * @Class Name    : ProAddress.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class ProAddress extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用id(缴物业费人的)
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 用户name(缴物业费人的)
	 * 字段: user_name  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String userName;
	
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
	 * 描述: 楼号
	 * 字段: floor  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String floor;
	
	/**
	 * 描述: 单元
	 * 字段: unit  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String unit;
	
	/**
	 * 描述: 户号
	 * 字段: door  VARCHAR(10)
	 * 默认值: ''
	 */
	private java.lang.String door;
	
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
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String note;
	
	/**
	 * 描述: 地址状态：0-删除  1-有效
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 具体地址
	 * 字段: address  VARCHAR(255)
	 */
	private java.lang.String address;
	
	/***
	 * 检查是否是有效地址（比如该地址因外力因素不存在了） ，0 无效  1有效
	 */
	private Integer isValid;
	
	public ProAddress(){
	}

	public ProAddress(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	
	public java.lang.String getUserName() {
		return this.userName;
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
	
	public void setFloor(java.lang.String floor) {
		this.floor = floor;
	}
	
	public java.lang.String getFloor() {
		return this.floor;
	}
	
	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}
	
	public java.lang.String getUnit() {
		return this.unit;
	}
	
	public void setDoor(java.lang.String door) {
		this.door = door;
	}
	
	public java.lang.String getDoor() {
		return this.door;
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
	public void setNote(java.lang.String note) {
		this.note = note;
	}
	
	public java.lang.String getNote() {
		return this.note;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	
	public java.lang.String getAddress() {
		return this.address;
	}
	public Integer getIsValid() {
		return isValid;
	}
	
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

