package com.hongkun.finance.info.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 调查问卷ID
	 */
	private Integer questionnaireId;
	/**
	 * 题目内容
	 */
	private String content;
	/**
	 * 答案类型 1:radio2:checkbox3:text
	 */
	private Integer type;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 资讯调查问卷试题选项集合
	 */
	List<QuestionnaireItem> questionnaireItems;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<QuestionnaireItem> getQuestionnaireItems() {
		return questionnaireItems;
	}

	public void setQuestionnaireItems(List<QuestionnaireItem> questionnaireItems) {
		this.questionnaireItems = questionnaireItems;
	}

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
