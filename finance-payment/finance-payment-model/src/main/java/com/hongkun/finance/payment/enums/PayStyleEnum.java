
/**
 * 
 */
package com.hongkun.finance.payment.enums;

/**
 * @Description : caoxb 类描述：支付方式枚举
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.enums.LianLianPayStyleEnum.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public enum PayStyleEnum {
	/** 充值 **/
	RECHARGE("CZ", 10),
	/** 提现 **/
	WITHDRAW("TX", 14),
	/** 快捷支付 **/
	KJ("KJ", 20),
	/** 认证支付 **/
	RZ("RZ", 21),
	/** 网银支付 **/
	WY("WY", 22),
	/** 企业网银支付 **/
	QYWY("QYWY", 37),
	/** 实时付款 **/
	SSFK("SSFK", 23),
	/** 分期付款 **/
	FQFK("FQFK", 24),
	/** 批次付款 **/
	PCFK("PCFK", 25),
	/** 代扣 **/
	DK("DK", 26),

	/** 限额 **/
	XE("XE", 27),
	/** 签约 **/
	QY("QY", 28),
	/** 卡BIN **/
	KBIN("KBIN", 29),
	/** 绑卡列表 **/
	BKLB("BKLB", 30),
	/** 分期代扣 **/
	FQDK("FQDK", 32),
	/** 支付查证 **/
	PAYCHECK("PAYCHECK", 33),
	/** 支付对账 **/
	PAYDZ("PAYDZ", 34),
	/** 物业充值 **/
	PRORECHARGE("PRORECHARGE", 35),
	/** 实名认证**/
	REALNAME("REALNAME", 36);
	/**
	 * 标识
	 */
	private String type;
	/**
	 * 名称
	 */
	private int value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private PayStyleEnum(String type, int value) {

		this.type = type;
		this.value = value;

	}

	/**
	 * 
	 * @Description : 获得支付方式
	 * @Method_Name : nameByType
	 * @param type
	 * @return
	 * @return : String
	 * @Creation Date : 2017年6月7日 下午2:07:36
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static int valueByType(String type) {
		for (PayStyleEnum s : PayStyleEnum.values()) {
			if (s.getType().equals(type)) {
				return s.getValue();
			}
		}
		return -1;
	}

	/**
	 * 
	 * @Description : 获得支付方式
	 * @Method_Name : nameByType
	 * @param value
	 * @return
	 * @return : String
	 * @Creation Date : 2017年6月7日 下午2:07:36
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static String typeByValue(int value) {
		for (PayStyleEnum s : PayStyleEnum.values()) {
			if (s.getValue() == value) {
				return s.getType();
			}
		}
		return null;
	}

	/**
	 * @Description : 通过value获取枚举类
	 * @Method_Name : payStyleEnumByValue;
	 * @param value
	 * @return
	 * @return : PayStyleEnum;
	 * @Creation Date : 2018年3月8日 下午6:27:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static PayStyleEnum payStyleEnumByValue(int value) {
		for (PayStyleEnum pse : PayStyleEnum.values()) {
			if (pse.getValue() == value) {
				return pse;
			}
		}
		return null;
	}
}
