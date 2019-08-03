package com.hongkun.finance.info.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.model.InfoBrowsingRecord.java
 * @Class Name    : InfoBrowsingRecord.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class InfoBrowsingRecord extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键ID
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 资讯信息id
	 * 字段: info_information_news_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer infoInformationNewsId;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 类型：1-浏览 2-点赞
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer type;
	
	/**
	 * 描述: 来源 0-PC 1-WAP 2-Android 3-IOS
	 * 字段: source  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer source;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
 
	public InfoBrowsingRecord(){
	}

	public InfoBrowsingRecord(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setInfoInformationNewsId(java.lang.Integer infoInformationNewsId) {
		this.infoInformationNewsId = infoInformationNewsId;
	}
	
	public java.lang.Integer getInfoInformationNewsId() {
		return this.infoInformationNewsId;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setSource(java.lang.Integer source) {
		this.source = source;
	}
	
	public java.lang.Integer getSource() {
		return this.source;
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

