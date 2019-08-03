package com.hongkun.finance.payment.client.yeepay.vo;

import java.io.Serializable;

public class HeePaymentInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	//参数*为必填否则不为必填项
	//商户提交参数
	private String agent_id;//*给商户分配的唯一标识
	private String version;//*版本号，固定为 1
	private String user_identity;//商户用户唯一标识
	private String hy_auth_uid;//用户快捷支付的授权码备注：如果提交该参数并该授权码已经授权过，可扩展银行业务参数无实际意义
	private String device_type;//*设备类型：0=Wap1=Web2=AndroidApp
	private String device_id;//设备唯一标识
	private String display;//终端显示样式：0= Wap（默认）
	private String custom_page;//自定义支付页面：0=否，支付页面使用汇付宝提供（默认）
	private String return_url;//*支付页面返回地址：custom_page = 0：表示支付完成后的返回地址
	private String notify_url;//*异步通知地址
	private String timestamp;//*时间戳
	//业务参数
	private String agent_bill_id;//*商户订单号，必须唯一
	private String agent_bill_time;//*商户订单时间，格式为“yyyyMMddhhmmss”,例 20130910170601
	private String pay_amt;//*支付金额，单位：元，保留二位小数
	private String goods_name;//*商品名称
	private String goods_note;//商品描述
	private String goods_num;//*商品数量
	private String user_ip;//*用户IP地址
	private String auth_card_type;//银行卡类型：0=储蓄卡（默认）1=信用-1=未知
	private String ext_param1;//商户保留，支付完成后的通知会将该参数返回给商户
	private String ext_param2;//商户保留，支付完成后的通知会将该参数返回给商户
	//可扩展业务参数
	private String bank_card_no;//银行卡号码
	private String bank_user;//银行卡户名
	private String cert_no;//身份证号
	private String mobile;//手机号
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUser_identity() {
		return user_identity;
	}
	public void setUser_identity(String user_identity) {
		this.user_identity = user_identity;
	}
	public String getHy_auth_uid() {
		return hy_auth_uid;
	}
	public void setHy_auth_uid(String hy_auth_uid) {
		this.hy_auth_uid = hy_auth_uid;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getCustom_page() {
		return custom_page;
	}
	public void setCustom_page(String custom_page) {
		this.custom_page = custom_page;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAgent_bill_id() {
		return agent_bill_id;
	}
	public void setAgent_bill_id(String agent_bill_id) {
		this.agent_bill_id = agent_bill_id;
	}
	public String getAgent_bill_time() {
		return agent_bill_time;
	}
	public void setAgent_bill_time(String agent_bill_time) {
		this.agent_bill_time = agent_bill_time;
	}
	public String getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(String pay_amt) {
		this.pay_amt = pay_amt;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_note() {
		return goods_note;
	}
	public void setGoods_note(String goods_note) {
		this.goods_note = goods_note;
	}
	public String getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getAuth_card_type() {
		return auth_card_type;
	}
	public void setAuth_card_type(String auth_card_type) {
		this.auth_card_type = auth_card_type;
	}
	public String getExt_param1() {
		return ext_param1;
	}
	public void setExt_param1(String ext_param1) {
		this.ext_param1 = ext_param1;
	}
	public String getExt_param2() {
		return ext_param2;
	}
	public void setExt_param2(String ext_param2) {
		this.ext_param2 = ext_param2;
	}
	public String getBank_card_no() {
		return bank_card_no;
	}
	public void setBank_card_no(String bank_card_no) {
		this.bank_card_no = bank_card_no;
	}
	public String getBank_user() {
		return bank_user;
	}
	public void setBank_user(String bank_user) {
		this.bank_user = bank_user;
	}
	public String getCert_no() {
		return cert_no;
	}
	public void setCert_no(String cert_no) {
		this.cert_no = cert_no;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	
}
