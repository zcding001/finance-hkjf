package com.hongkun.finance.info.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.model.InfoQuestionnaire.java
 * @Class Name    : InfoQuestionnaire.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class InfoQuestionnaire extends BaseModel {
	
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
	 * 描述: 答案类型 1:radio2:checkbox3:text
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer type;
	
	/**
	 * 描述: 题目内容
	 * 字段: content  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String content;
	
	/**
	 * 描述: 状态 1:显示2:隐藏3:删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 问题排序号
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
	//调查问题的选项内容
	private List<InfoQuestionnaireItem> questionnaireItemList;
	//调查问题的答案
	private InfoQuestionnaireAnswer questionnaireAnswer;
 
	public InfoQuestionnaire(){
	}

	public InfoQuestionnaireAnswer getQuestionnaireAnswer() {
        return questionnaireAnswer;
    }

    public void setQuestionnaireAnswer(InfoQuestionnaireAnswer questionnaireAnswer) {
        this.questionnaireAnswer = questionnaireAnswer;
    }

    public InfoQuestionnaire(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public List<InfoQuestionnaireItem> getQuestionnaireItemList() {
        return questionnaireItemList;
    }

    public void setQuestionnaireItemList(List<InfoQuestionnaireItem> questionnaireItemList) {
        this.questionnaireItemList = questionnaireItemList;
    }

    public void setInfoInformationNewsId(java.lang.Integer infoInformationNewsId) {
		this.infoInformationNewsId = infoInformationNewsId;
	}
	
	public java.lang.Integer getInfoInformationNewsId() {
		return this.infoInformationNewsId;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	
	public java.lang.String getContent() {
		return this.content;
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

