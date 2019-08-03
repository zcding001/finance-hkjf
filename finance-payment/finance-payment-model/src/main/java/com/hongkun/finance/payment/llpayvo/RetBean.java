package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;

/**
 * @Description : 提现通知响应连连信息bean
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.RetBean.java
 * @Author : yanbinghuang
 */
public class RetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ret_code;

	private String ret_msg;

	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

}
