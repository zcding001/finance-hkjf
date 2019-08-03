package com.hongkun.finance.info.vo;

import java.io.Serializable;
import java.util.List;

public class InformationNewsItem implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 资讯信息id
	 */
	private Integer informationNewsId;
	/**
	 * 资讯内容
	 */
	private String content;
	/**
	 * 调查问卷列表
	 */
	private List<Questionnaire> problemsList;

	public Integer getInformationNewsId() {
		return informationNewsId;
	}

	public void setInformationNewsId(Integer informationNewsId) {
		this.informationNewsId = informationNewsId;
	}

	public List<Questionnaire> getProblemsList() {
		return problemsList;
	}

	public void setProblemsList(List<Questionnaire> problemsList) {
		this.problemsList = problemsList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
