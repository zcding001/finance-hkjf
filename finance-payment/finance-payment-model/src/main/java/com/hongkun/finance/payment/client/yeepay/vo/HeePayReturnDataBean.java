package com.hongkun.finance.payment.client.yeepay.vo;

import java.io.Serializable;

/**
 * @author 	jiadong.wen
 * @date	2016-04-13
 * **/
public class HeePayReturnDataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String            result;			//必填	支付结果: 1=成功 其它为未知
    private String            pay_message;		//可选	支付结果信息，支付成功时为空
    private String            agent_id;			//必填	商户编号 如1234567
    private String            jnet_bill_no;		//必填	汇付宝交易号(订单号)
    private String            agent_bill_id;	//必填	商户系统内部的订单号 
    private String            pay_type;			//必填	支付类型
    private String            pay_amt;			//必填	订单实际支付金额(注意：此金额是用户的实付金额)
    private String            remark;			//必填	商家数据包，原样返回
    private String            sign;				//必填	MD5签名结果
    private String 			  key;				//必填	商户密钥
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPay_message() {
		return pay_message;
	}
	public void setPay_message(String pay_message) {
		this.pay_message = pay_message;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public String getJnet_bill_no() {
		return jnet_bill_no;
	}
	public void setJnet_bill_no(String jnet_bill_no) {
		this.jnet_bill_no = jnet_bill_no;
	}
	public String getAgent_bill_id() {
		return agent_bill_id;
	}
	public void setAgent_bill_id(String agent_bill_id) {
		this.agent_bill_id = agent_bill_id;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(String pay_amt) {
		this.pay_amt = pay_amt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
