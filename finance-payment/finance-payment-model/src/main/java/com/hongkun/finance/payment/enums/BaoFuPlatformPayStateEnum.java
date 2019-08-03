package com.hongkun.finance.payment.enums;

/**
 * @Description : 连连提现异步通知状态与平台状态对比枚举类
 * @Project : finance-payment-model
 * @Program Name :
 *          com.hongkun.finance.payment.enum.LianLianPlatformPayStateEnum.java
 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
 */
public enum BaoFuPlatformPayStateEnum {
	/********* 提现响应状态 ****************/
	// 订单处理成功
	PLATSUCCESS("OK", 100),
	/** 订单处理失败 **/
	PLATFAIL("FAILURE", -666),
	/** 订单不存在 **/
	PLATNONEXISTENT("FAILURE", 777),
	/** 订单重复请求 **/
	PLATREPEAT("FAILURE", 888);
	// 第三方支付系统状态
	private String thirdPaymentState;
	// 平台系统状态
	private Integer platFormState;

	BaoFuPlatformPayStateEnum(String thirdPaymentState, Integer platFormState) {
		this.thirdPaymentState = thirdPaymentState;
		this.platFormState = platFormState;
	}

	public String getThirdPaymentState() {
		return thirdPaymentState;
	}

	public void setThirdPaymentState(String thirdPaymentState) {
		this.thirdPaymentState = thirdPaymentState;
	}

	public Integer getPlatFormState() {
		return platFormState;
	}

	public void setPlatFormState(Integer platFormState) {
		this.platFormState = platFormState;
	}

	/**
	 * @Description : 通过第三方状态获取平台对应的状态
	 * @Method_Name : platFormStateByThirdPaymentState;
	 * @param thirdPaymentState
	 *            第三方支付系统状态
	 * @return
	 * @return : Integer;
	 * @Creation Date : 2017年10月31日 下午2:13:36;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Integer platFormStateByThirdPaymentState(String thirdPaymentState) {
		for (BaoFuPlatformPayStateEnum m : BaoFuPlatformPayStateEnum.values()) {
			if (m.getThirdPaymentState().equals(thirdPaymentState)) {
				return m.getPlatFormState();
			}
		}
		return null;
	}

	/**
	 * @Description : 根据平台状态获取第三方对应的响应状态
	 * @Method_Name : thirdPaymentStateByPlatFormState;
	 * @param platFormState
	 *            平台系统状态
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年10月31日 下午2:36:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String thirdPaymentStateByPlatFormState(Integer platFormState) {
		for (BaoFuPlatformPayStateEnum m : BaoFuPlatformPayStateEnum.values()) {
			if (m.getPlatFormState().equals(platFormState)) {
				return m.getThirdPaymentState();
			}
		}
		return null;
	}
}
