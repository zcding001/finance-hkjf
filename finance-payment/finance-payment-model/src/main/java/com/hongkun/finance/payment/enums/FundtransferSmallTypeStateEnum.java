package com.hongkun.finance.payment.enums;

/**
 * @Description : 支付渠道资金划转小类型枚举类
 * @Project : finance-payment-model
 * @Program Name :
 *          com.hongkun.finance.payment.enum.FundtransferSmallTypeStateEnum.java
 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
 */
public enum FundtransferSmallTypeStateEnum {
	/** 本金 **/
	CAPITAL(10, "本金"),
	/** 利息 **/
	INTEREST(11, "利息"),
	/** 手续费 **/
	CHARGE(12, "手续费"),
	/** 罚息 **/
	PENALTY_INTEREST(13, "罚息"),
	/** 加息 **/
	INCREASE_INTEREST(14, "加息"),
	/** 担保费 **/
	GUARANTEE_FEE(15, "担保费"),
	/** 利息差 **/
	INTEREST_DIFFERENTIAL(16, "利息差"),
	/** 储备金 **/
	RESERVE_FUND(17, "储备金"),
	/** 服务费 **/
	SERVICE_CHARGE(18, "服务费"),
	/** 违约金 **/
	LIQUIDATED_DAMAGES(19, "违约金"),
	/** 冻结 **/
	FROZEN(20, "冻结"),
	/** 投资红包 **/
	INVEST_RED_PACKETS(21, "投资红包"),
	/** 冲正 **/
	CORRECT(22, "冲正"),
	/** 积分对应的金额 */
	POINT_MONEY(23, "积分"),
	/**推荐奖励**/
	COMMISSION_MONEY(24, "佣金"),
	/**物业费**/
    PROPERTY_MONEY(25, "物业费");
	
	// 资金划转小类型划分
	private int smallTransferType;
	// 资金划转小类型描述
	private String typeDesc;

	public int getSmallTransferType() {
		return smallTransferType;
	}

	public void setSmallTransferType(int smallTransferType) {
		this.smallTransferType = smallTransferType;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	private FundtransferSmallTypeStateEnum(int smallTransferType, String typeDesc) {
		this.smallTransferType = smallTransferType;
		this.typeDesc = typeDesc;
	}

	/**
	 * @Description :根据资金划转小类型获取对应的类型描述
	 * @Method_Name : getTypeDescBySmallType;
	 * @param type
	 *            资金划转小类型
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月14日 下午2:54:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String getTypeDescBySmallType(int type) {
		for (FundtransferSmallTypeStateEnum s : FundtransferSmallTypeStateEnum.values()) {
			if (s.getSmallTransferType() == type) {
				return s.getTypeDesc();
			}
		}
		return "";
	}
}
