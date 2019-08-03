package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.HouseProPermit.java
 * @Class Name    : HouseProPermit.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class HouseProPermit extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 名称
	 * 字段: name  VARCHAR(255)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 发证时间
	 * 字段: send_time  VARCHAR(50)
	 * 默认值: '0000-00-00 '
	 */
	private String sendTime;
	
	/**
	 * 描述: 绑定楼栋
	 * 字段: floor  VARCHAR(255)
	 * 默认值: ''
	 */
	private String floor;
	
	/**
	 * 描述: infoId
	 * 字段: info_id  INT(10)
	 * 默认值: 0
	 */
	private Integer infoId;
	
 
	public HouseProPermit(){
	}

	public HouseProPermit(Integer id){
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
	
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getSendTime() {
		return this.sendTime;
	}
	
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	public String getFloor() {
		return this.floor;
	}
	
	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}
	
	public Integer getInfoId() {
		return this.infoId;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

