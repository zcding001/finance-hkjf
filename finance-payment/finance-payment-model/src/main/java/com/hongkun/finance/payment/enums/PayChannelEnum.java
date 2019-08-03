package com.hongkun.finance.payment.enums;

/**
 * @Description : 支付渠道枚举类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.enum.PayChannelEnum.java
 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
 */
public enum PayChannelEnum {

	LianLian("lianlian", 1), LianDong("liandong", 2), BaoFu("baofu", 3), BaoFuProtocol("baofuProtocol",
			4), BaoFuProtocolB("baofuProtocolB", 5), Yeepay("yeepay", 6);
	// 渠道名称
	private String channelKey;
	// 渠道代码
	private int channelNameValue;

	private PayChannelEnum(String channelKey, int channelNameValue) {
		this.channelKey = channelKey;
		this.channelNameValue = channelNameValue;
	}

	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public int getChannelNameValue() {
		return channelNameValue;
	}

	public void setChannelNameValue(int channelNameValue) {
		this.channelNameValue = channelNameValue;
	}

	/**
	 * @Description : 通过渠道名称获取渠道code
	 * @Method_Name : fromChannelName;
	 * @param channel
	 *            渠道名称
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年11月1日 上午10:04:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static int fromChannelName(String channel) {
		for (PayChannelEnum type : PayChannelEnum.values()) {
			if (type.getChannelKey().equals(channel)) {
				return type.getChannelNameValue();
			}
		}
		return -1;
	}

	/**
	 * @Description : 通过渠道CODE获取渠道名称
	 * @Method_Name : fromChannelCode;
	 * @param value
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:05:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String fromChannelCode(int value) {
		for (PayChannelEnum type : PayChannelEnum.values()) {
			if (type.getChannelNameValue() == value) {
				return type.getChannelKey();
			}
		}
		return "";
	}

	/**
	 * @Description : 通过渠道CODE获取支付渠道的枚举类
	 * @Method_Name : getPayChannelEnumByCode;
	 * @param value
	 *            渠道值
	 * @return
	 * @return : PayChannelEnum;
	 * @Creation Date : 2017年12月6日 下午6:11:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static PayChannelEnum getPayChannelEnumByCode(int value) {
		for (PayChannelEnum type : PayChannelEnum.values()) {
			if (type.getChannelNameValue() == value) {
				return type;
			}
		}
		return null;
	}

	/***
	 * @Description : 通过渠道名称获取支付渠道的枚举类
	 * @Method_Name : getPayChannelEnumByChannelKey;
	 * @param channelKey
	 *            渠道名称
	 * @return
	 * @return : PayChannelEnum;
	 * @Creation Date : 2017年12月6日 下午6:13:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static PayChannelEnum getPayChannelEnumByChannelKey(String channelKey) {
		for (PayChannelEnum type : PayChannelEnum.values()) {
			if (type.getChannelKey().equals(channelKey)) {
				return type;
			}
		}
		return null;
	}
}
