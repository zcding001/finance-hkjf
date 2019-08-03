package com.hongkun.finance.qdz.enums;

public enum TransTypeEnum {

	PAYIN("钱袋子转入", 1), PAYOUT("钱袋子转出", 2), CAPITAL_REPAY("钱袋子本金还款", 3);

	private Integer value;
	private String name;

	private TransTypeEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	public static String getNameByValue(Integer value) {
		for (TransTypeEnum typeEnum : TransTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum.name;
			}
		}
		return null;
	}
}
