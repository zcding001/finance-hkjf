package com.hongkun.finance.point.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.model.PointProductImg.java
 * @Class Name    : PointProductImg.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class PointProductImg extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 商品id
	 * 字段: product_id  INT(10)
	 */
	private Integer productId;
	
	/**
	 * 描述: 图片名称
	 * 字段: title  VARCHAR(50)
	 * 默认值: ''
	 */
	private String title;
	
	/**
	 * 描述: 图片路径大图
	 * 字段: big_img_url  VARCHAR(200)
	 * 默认值: ''
	 */
	private String bigImgUrl;
	
	/**
	 * 描述: 图片路径小图
	 * 字段: small_img_url  VARCHAR(200)
	 */
	private String smallImgUrl;
	
	/**
	 * 描述: 是否是商品展示页图片：0-不是 1-是
	 * 字段: head_img  INT(10)
	 * 默认值: 0
	 */
	private Integer headImg;
	
	/**
	 * 描述: 状态 0无效 1有效
	 * 字段: state  INT(10)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 图片排序
	 * 字段: sort  INT(10)
	 * 默认值: 0
	 */
	private Integer sort;
	
 
	public PointProductImg(){
	}

	public PointProductImg(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public Integer getProductId() {
		return this.productId;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setBigImgUrl(String bigImgUrl) {
		this.bigImgUrl = bigImgUrl;
	}
	
	public String getBigImgUrl() {
		return this.bigImgUrl;
	}
	
	public void setSmallImgUrl(String smallImgUrl) {
		this.smallImgUrl = smallImgUrl;
	}
	
	public String getSmallImgUrl() {
		return this.smallImgUrl;
	}
	
	public void setHeadImg(Integer headImg) {
		this.headImg = headImg;
	}
	
	public Integer getHeadImg() {
		return this.headImg;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getSort() {
		return this.sort;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

