package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.HouseProPic.java
 * @Class Name    : HouseProPic.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class HouseProPic extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 图片类型：1-户型图 2-楼盘相册3-封面图片
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 图片地址
	 * 字段: url  VARCHAR(255)
	 * 默认值: ''
	 */
	private String url;
	
	/**
	 * 描述: 名称
	 * 字段: name  VARCHAR(255)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 排序
	 * 字段: sort  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer sort;
	
	/**
	 * 描述: 状态：1-正常0-删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: infoId
	 * 字段: info_id  INT(10)
	 * 默认值: 0
	 */
	private Integer infoId;
	
 
	public HouseProPic(){
	}

	public HouseProPic(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getSort() {
		return this.sort;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
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

