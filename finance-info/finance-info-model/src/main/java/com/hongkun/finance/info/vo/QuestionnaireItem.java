package com.hongkun.finance.info.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QuestionnaireItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 选项名称
	 */
	private String optionTitle;
	/**
	 * 选项内容
	 */
	private String optionContent;

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
