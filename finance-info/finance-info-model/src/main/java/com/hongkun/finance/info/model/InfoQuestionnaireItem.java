package com.hongkun.finance.info.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.model.InfoQuestionnaireItem.java
 * @Class Name    : InfoQuestionnaireItem.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class InfoQuestionnaireItem extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键ID
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 资讯问卷调查id
	 * 字段: info_questionnaire_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer infoQuestionnaireId;
	
	/**
	 * 描述: 选项名称
	 * 字段: option_title  VARCHAR(3)
	 * 默认值: ''
	 */
	private java.lang.String optionTitle;
	
	/**
	 * 描述: 选项内容
	 * 字段: option_content  VARCHAR(255)
	 * 默认值: ''
	 */
	private java.lang.String optionContent;
	
	/**
	 * 描述: 状态 1:显示2:隐藏3:删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 排序
	 * 字段: sort  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer sort;
	
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
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public InfoQuestionnaireItem(){
	}

	public InfoQuestionnaireItem(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setInfoQuestionnaireId(java.lang.Integer infoQuestionnaireId) {
		this.infoQuestionnaireId = infoQuestionnaireId;
	}
	
	public java.lang.Integer getInfoQuestionnaireId() {
		return this.infoQuestionnaireId;
	}
	
	public void setOptionTitle(java.lang.String optionTitle) {
		this.optionTitle = optionTitle;
	}
	
	public java.lang.String getOptionTitle() {
		return this.optionTitle;
	}
	
	public void setOptionContent(java.lang.String optionContent) {
		this.optionContent = optionContent;
	}
	
	public java.lang.String getOptionContent() {
		return this.optionContent;
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

