package com.hongkun.finance.info.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.info.model.InfoQuestionnaireAnswer.java
 * @Class Name    : InfoQuestionnaireAnswer.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class InfoQuestionnaireAnswer extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键ID
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 资讯问卷调查id
	 * 字段: info_questionnaire_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer infoQuestionnaireId;
	
	/**
	 * 描述: 答案
	 * 字段: answer  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String answer;
	
	/**
	 * 描述: 来源 1-PC 2-IOS 3-ANDRIOD 4-WAP
	 * 字段: source  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer source;
	
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
 
	public InfoQuestionnaireAnswer(){
	}

	public InfoQuestionnaireAnswer(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setInfoQuestionnaireId(java.lang.Integer infoQuestionnaireId) {
		this.infoQuestionnaireId = infoQuestionnaireId;
	}
	
	public java.lang.Integer getInfoQuestionnaireId() {
		return this.infoQuestionnaireId;
	}
	
	public void setAnswer(java.lang.String answer) {
		this.answer = answer;
	}
	
	public java.lang.String getAnswer() {
		return this.answer;
	}
	
	public void setSource(Integer source) {
		this.source = source;
	}
	
	public Integer getSource() {
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

