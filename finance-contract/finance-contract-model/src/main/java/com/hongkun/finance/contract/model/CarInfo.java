package com.hongkun.finance.contract.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.model.CarInfo.java
 * @Class Name    : CarInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class CarInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id主键
	 * 字段: id  TINYINT(3)
	 */
	private Integer id;
	
	/**
	 * 描述: 机动车品牌
	 * 字段: car_brand  VARCHAR(30)
	 */
	private String carBrand;
	
	/**
	 * 描述: 车型号
	 * 字段: car_type  VARCHAR(20)
	 */
	private String carType;
	
	/**
	 * 描述: 车架号
	 * 字段: frame_num  VARCHAR(30)
	 */
	private String frameNum;
	
	/**
	 * 描述: 颜色
	 * 字段: car_color  VARCHAR(10)
	 */
	private String carColor;
	
	/**
	 * 描述: 钥匙数量
	 * 字段: key_num  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer keyNum;
	
	/**
	 * 描述: 备注
	 * 字段: remark  VARCHAR(30)
	 * 默认值: ''
	 */
	private String remark;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 最后修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public CarInfo(){
	}

	public CarInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	
	public String getCarBrand() {
		return this.carBrand;
	}
	
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	public String getCarType() {
		return this.carType;
	}
	
	public void setFrameNum(String frameNum) {
		this.frameNum = frameNum;
	}
	
	public String getFrameNum() {
		return this.frameNum;
	}
	
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	
	public String getCarColor() {
		return this.carColor;
	}
	
	public void setKeyNum(Integer keyNum) {
		this.keyNum = keyNum;
	}
	
	public Integer getKeyNum() {
		return this.keyNum;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
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

