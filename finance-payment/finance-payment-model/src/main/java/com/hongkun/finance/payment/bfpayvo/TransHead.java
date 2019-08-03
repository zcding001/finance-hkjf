package com.hongkun.finance.payment.bfpayvo;

/**
 * @Description : 代付响应数据头信息
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.bfpayvo.TransHead.java
 * @Author :yanbinghuang
 */
public class TransHead {

	private String return_code;// 响应码元素返回交易处理状态码

	private String return_msg;// 响应信息元素交易处理状态中文信息

	private String trans_count;// 交易总笔数

	private String trans_totalMoney;// 交易总金额

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getTrans_count() {
		return trans_count;
	}

	public void setTrans_count(String trans_count) {
		this.trans_count = trans_count;
	}

	public String getTrans_totalMoney() {
		return trans_totalMoney;
	}

	public void setTrans_totalMoney(String trans_totalMoney) {
		this.trans_totalMoney = trans_totalMoney;
	}
}
