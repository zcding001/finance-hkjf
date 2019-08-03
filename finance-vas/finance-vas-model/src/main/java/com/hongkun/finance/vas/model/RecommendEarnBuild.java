package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.util.List;

import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;

public class RecommendEarnBuild implements Serializable {
	private static final long serialVersionUID = 1L;

	private SystemTypeEnums systemTypeEnums;
	/**
	 * 推荐规则类型
	 */
	private VasRuleTypeEnum vasRuleTypeEnum;
	/**
	 * 根据记录生成推荐奖
	 */
	private List<?> recommendRecordList;

	public SystemTypeEnums getSystemTypeEnums() {
		return systemTypeEnums;
	}

	public void setSystemTypeEnums(SystemTypeEnums systemTypeEnums) {
		this.systemTypeEnums = systemTypeEnums;
	}

	public VasRuleTypeEnum getVasRuleTypeEnum() {
		return vasRuleTypeEnum;
	}

	public void setVasRuleTypeEnum(VasRuleTypeEnum vasRuleTypeEnum) {
		this.vasRuleTypeEnum = vasRuleTypeEnum;
	}

	public List<?> getRecommendRecordList() {
		return recommendRecordList;
	}

	public void setRecommendRecordList(List<?> recommendRecordList) {
		this.recommendRecordList = recommendRecordList;
	}

}
