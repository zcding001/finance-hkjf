package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.HouseProInfo.java
 * @Class Name    : HouseProInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class HouseProInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 项目名称
	 * 字段: name  VARCHAR(255)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 价格
	 * 字段: price  VARCHAR(20)
	 * 默认值: '0.00'
	 */
	private String price;
	
	/**
	 * 描述: 物业类别：0-普通住宅
	 * 字段: pro_type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer proType;
	
	/**
	 * 描述: 重要特色1-品牌地产 2-小户型 3-花园洋房
	 * 字段: feature  VARCHAR(20)
	 * 默认值: '0'
	 */
	private String feature;
	
	/**
	 * 描述: 建筑类别：1-板楼2-低层3-多层4-小高层5-高层
	 * 字段: build_type  VARCHAR(20)
	 * 默认值: '0'
	 */
	private String buildType;
	
	/**
	 * 描述: 装修状况：1-毛胚2-精装修3-普通装修
	 * 字段: redecorate  VARCHAR(20)
	 * 默认值: '0'
	 */
	private String redecorate;
	
	/**
	 * 描述: 产权年限
	 * 字段: pro_years  VARCHAR(50)
	 * 默认值: '70'
	 */
	private String proYears;
	
	/**
	 * 描述: 开发商
	 * 字段: developers  VARCHAR(255)
	 * 默认值: '北京鸿坤地产集团'
	 */
	private String developers;
	
	/**
	 * 描述: 楼盘地址
	 * 字段: address  VARCHAR(255)
	 * 默认值: ''
	 */
	private String address;
	
	/**
	 * 描述: 状态：0-售罄1-在售2-停盘
	 * 字段: sale_state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer saleState;
	
	/**
	 * 描述: 楼盘优惠：0-暂无 1-金服增值卡
	 * 字段: prefer  VARCHAR(255)
	 * 默认值: '0'
	 */
	private String prefer;
	
	/**
	 * 描述: 开盘时间
	 * 字段: start_sale_date  VARCHAR(255)
	 * 默认值: ''
	 */
	private String startSaleDate;
	
	/**
	 * 描述: 交房时间
	 * 字段: make_house_date  VARCHAR(255)
	 * 默认值: ''
	 */
	private String makeHouseDate;
	
	/**
	 * 描述: 售楼地址
	 * 字段: sale_address  VARCHAR(255)
	 * 默认值: ''
	 */
	private String saleAddress;
	
	/**
	 * 描述: 咨询电话
	 * 字段: sal_tel  VARCHAR(50)
	 * 默认值: ''
	 */
	private String salTel;
	
	/**
	 * 描述: 主力户型：2-二居3-三居4-四居5-五居6-六居
	 * 字段: room_type  VARCHAR(255)
	 * 默认值: ''
	 */
	private String roomType;
	
	/**
	 * 描述: 状态：1-上架 0-下架
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 房屋面积
	 * 字段: home_area  VARCHAR(255)
	 * 默认值: ''
	 */
	private String homeArea;
	
	/**
	 * 描述: 操作人
	 * 字段: modified_user  VARCHAR(255)
	 * 默认值: ''
	 */
	private String modifiedUser;
	
	/**
	 * 描述: 0000-00-00 00:00:00
	 * 字段: create_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeEnd;
 
	public HouseProInfo(){
	}

	public HouseProInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getPrice() {
		return this.price;
	}
	
	public void setProType(Integer proType) {
		this.proType = proType;
	}
	
	public Integer getProType() {
		return this.proType;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getFeature() {
		return this.feature;
	}
	
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	
	public String getBuildType() {
		return this.buildType;
	}
	
	public void setRedecorate(String redecorate) {
		this.redecorate = redecorate;
	}
	
	public String getRedecorate() {
		return this.redecorate;
	}
	
	public void setProYears(String proYears) {
		this.proYears = proYears;
	}
	
	public String getProYears() {
		return this.proYears;
	}
	
	public void setDevelopers(String developers) {
		this.developers = developers;
	}
	
	public String getDevelopers() {
		return this.developers;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setSaleState(Integer saleState) {
		this.saleState = saleState;
	}
	
	public Integer getSaleState() {
		return this.saleState;
	}
	
	public void setPrefer(String prefer) {
		this.prefer = prefer;
	}
	
	public String getPrefer() {
		return this.prefer;
	}
	
	public void setStartSaleDate(String startSaleDate) {
		this.startSaleDate = startSaleDate;
	}
	
	public String getStartSaleDate() {
		return this.startSaleDate;
	}
	
	public void setMakeHouseDate(String makeHouseDate) {
		this.makeHouseDate = makeHouseDate;
	}
	
	public String getMakeHouseDate() {
		return this.makeHouseDate;
	}
	
	public void setSaleAddress(String saleAddress) {
		this.saleAddress = saleAddress;
	}
	
	public String getSaleAddress() {
		return this.saleAddress;
	}
	
	public void setSalTel(String salTel) {
		this.salTel = salTel;
	}
	
	public String getSalTel() {
		return this.salTel;
	}
	
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
	public String getRoomType() {
		return this.roomType;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setHomeArea(String homeArea) {
		this.homeArea = homeArea;
	}
	
	public String getHomeArea() {
		return this.homeArea;
	}
	
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	
	public String getModifiedUser() {
		return this.modifiedUser;
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

