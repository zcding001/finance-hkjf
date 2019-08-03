package com.hongkun.finance.payment.enums;

/**
 * @Description : 支付渠道资金划转大类型枚举类
 * @Project : finance-payment-model
 * @Program Name :
 *          com.hongkun.finance.payment.enum.FundtransferBigTypeStateEnum.java
 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
 */
public enum FundtransferBigTypeStateEnum {
	/** 收入 **/
	INCOME(10, "收入"),
	/** 支出 **/
	PAY(20, "支出"),
	/** 冻结 **/
	FREEZE(30, "冻结"),
	/** 解冻 **/
	THAW(40, "解冻"),
	/** 钱袋子转入 **/
	TURNS_IN(50, "钱袋子转入"),
	/** 钱袋子转出 **/
	TURNS_OUT(60, "钱袋子转出"),
	/** 钱袋子债转收入 **/
	CREDITOR_TRANSFER_INCOME(70, "钱袋子债转收入"),
	/** 钱袋子债转支出 **/
	CREDITOR_TRANSFER_PAY(80, "钱袋子债转支出");

	// 资金划转大类型划分
	private int bigTransferType;
	// 资金划转大类型描述
	private String typeDesc;

	public int getBigTransferType() {
		return bigTransferType;
	}

	public void setBigTransferType(int bigTransferType) {
		this.bigTransferType = bigTransferType;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	private FundtransferBigTypeStateEnum(int bigTransferType, String typeDesc) {
		this.bigTransferType = bigTransferType;
		this.typeDesc = typeDesc;
	}

	/**
	 * @Description :根据资金划转大类型获取对应的类型描述
	 * @Method_Name : getTypeDescByBigType;
	 * @param type
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月14日 下午2:54:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String getTypeDescByBigType(int type) {
		for (FundtransferBigTypeStateEnum s : FundtransferBigTypeStateEnum.values()) {
			if (s.getBigTransferType() == type) {
				return s.getTypeDesc();
			}
		}
		return "";
	}
}
