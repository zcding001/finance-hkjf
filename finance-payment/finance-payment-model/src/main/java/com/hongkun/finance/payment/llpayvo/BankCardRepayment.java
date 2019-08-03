package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;

/**
 * @Description : 银行卡还款扣款接口bean
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.BankCardRepayment.java
 * @Author : yanbinghuang
 */
public class BankCardRepayment implements Serializable {
	private static final long serialVersionUID = 1L;
	// 商户提交参数
	private String platform; // 平台来源
	private String user_id; // 用户唯一编号（商户用户唯一编号）
	private String oid_partner; // 商户编号
	private String sign_type; // 签名方式 RSA或者MD5
	private String sign; // RSA签名

	private String busi_partner;// 商户业务类型 虚拟商品销售：101001 实物商品销售：109001
	private String api_version; // 版本号
	private String no_order; // 商品唯一订单号（商户系统唯一订单号）
	private String dt_order; // 商户订单时间 yyyymmddh24miss
	private String name_goods; // 商品名称
	private String info_order; // 订单描述
	private String money_order; // 还款金额
	private String notify_url; // 服务器异步通知地址
	private String valid_order; // 订单有效时间
	private String risk_item; // 风险控制参数
	private String schedule_repayment_date;// 计划还款日期 2010-07-06
	private String repayment_no; // 还款计划编号

	private String pay_type; // 支付方式
	private String no_agree; // 签约协议号

	// 如果没有协议号，则须传下述银行卡信息
	private String card_no;// 银行卡与
	private String bind_mob;
	private String id_type;// 证件类型
	private String id_no;// 证件号

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getValid_order() {
		return valid_order;
	}

	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getSchedule_repayment_date() {
		return schedule_repayment_date;
	}

	public void setSchedule_repayment_date(String schedule_repayment_date) {
		this.schedule_repayment_date = schedule_repayment_date;
	}

	public String getRepayment_no() {
		return repayment_no;
	}

	public void setRepayment_no(String repayment_no) {
		this.repayment_no = repayment_no;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBind_mob() {
		return bind_mob;
	}

	public void setBind_mob(String bind_mob) {
		this.bind_mob = bind_mob;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

}
