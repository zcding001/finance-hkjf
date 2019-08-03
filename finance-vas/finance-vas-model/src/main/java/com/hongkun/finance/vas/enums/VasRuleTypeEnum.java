package com.hongkun.finance.vas.enums;

/**
 * @Description : 增值服务常量类0-好友推荐 1-红包 2-钱袋子规则 3-钱袋子推荐奖规则 4-债权池限额 5-钱袋子转入转出总开关
 *              6-钱袋子利息加息开关
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.VasRuleTypeEnum
 * @Author : yanbinghuang
 */
public enum VasRuleTypeEnum {

	RECOMMEND(0), REDPACKAGE(1), QDZ(2), QDZRECOMMONEY(3), CREDITORMONEY(4), QDZINOUTONOFF(5), QDZINTERESTADDONOFF(6);

	private int value;

	VasRuleTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
