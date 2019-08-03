package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.HouseProDetail.java
 * @Class Name    : HouseProDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class HouseProDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 占地面积(平方米)
	 * 字段: land_area  VARCHAR(20)
	 * 默认值: '0.00'
	 */
	private String landArea;
	
	/**
	 * 描述: 建筑面积(平方米)
	 * 字段: build_area  VARCHAR(20)
	 * 默认值: '0.00'
	 */
	private String buildArea;
	
	/**
	 * 描述: 容积率
	 * 字段: cap_rate  VARCHAR(20)
	 * 默认值: '0.00'
	 */
	private String capRate;
	
	/**
	 * 描述: 绿化率
	 * 字段: green_rate  VARCHAR(20)
	 * 默认值: '0.00'
	 */
	private String greenRate;
	
	/**
	 * 描述: 停车位
	 * 字段: park_space  VARCHAR(50)
	 * 默认值: '0'
	 */
	private String parkSpace;
	
	/**
	 * 描述: 楼栋数量
	 * 字段: floor_num  VARCHAR(20)
	 * 默认值: '0'
	 */
	private String floorNum;
	
	/**
	 * 描述: 总户数
	 * 字段: door_num  VARCHAR(20)
	 * 默认值: '0'
	 */
	private String doorNum;
	
	/**
	 * 描述: 物业公司
	 * 字段: pro_company  VARCHAR(255)
	 */
	private String proCompany;
	
	/**
	 * 描述: 物业费
	 * 字段: pro_fee  VARCHAR(50)
	 * 默认值: '0.00'
	 */
	private String proFee;
	
 
	public HouseProDetail(){
	}

	public HouseProDetail(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setLandArea(String landArea) {
		this.landArea = landArea;
	}
	
	public String getLandArea() {
		return this.landArea;
	}
	
	public void setBuildArea(String buildArea) {
		this.buildArea = buildArea;
	}
	
	public String getBuildArea() {
		return this.buildArea;
	}
	
	public void setCapRate(String capRate) {
		this.capRate = capRate;
	}
	
	public String getCapRate() {
		return this.capRate;
	}
	
	public void setGreenRate(String greenRate) {
		this.greenRate = greenRate;
	}
	
	public String getGreenRate() {
		return this.greenRate;
	}
	
	public void setParkSpace(String parkSpace) {
		this.parkSpace = parkSpace;
	}
	
	public String getParkSpace() {
		return this.parkSpace;
	}
	
	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}
	
	public String getFloorNum() {
		return this.floorNum;
	}
	
	public void setDoorNum(String doorNum) {
		this.doorNum = doorNum;
	}
	
	public String getDoorNum() {
		return this.doorNum;
	}
	
	public void setProCompany(String proCompany) {
		this.proCompany = proCompany;
	}
	
	public String getProCompany() {
		return this.proCompany;
	}
	
	public void setProFee(String proFee) {
		this.proFee = proFee;
	}
	
	public String getProFee() {
		return this.proFee;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

