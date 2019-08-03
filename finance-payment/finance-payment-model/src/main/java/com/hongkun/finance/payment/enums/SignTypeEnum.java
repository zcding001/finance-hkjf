package com.hongkun.finance.payment.enums;

/**
 * @Description : 签名方式枚举
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.enum.SignTypeEnum.java
 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
 */
public enum SignTypeEnum {

	RSA("RSA", "RSA签名"), MD5("MD5", "MD5签名");
	// 签名方式
	private final String code;
	// 签名描述
	private final String msg;

	SignTypeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * @Description :判断是否属于某种签名
	 * @Method_Name : isSignType;
	 * @param code
	 *            签名方式代码
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:10:31;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean isSignType(String code) {
		for (SignTypeEnum s : SignTypeEnum.values()) {
			if (s.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
