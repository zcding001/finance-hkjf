package com.hongkun.finance.fund.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.model.HouseProIntroduce.java
 * @Class Name    : HouseProIntroduce.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class HouseProIntroduce extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 简介
	 * 字段: show_text  TEXT(65535)
	 */
	private String showText;
	
 
	public HouseProIntroduce(){
	}

	public HouseProIntroduce(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setShowText(String showText) {
		this.showText = showText;
	}
	
	public String getShowText() {
		return this.showText;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

