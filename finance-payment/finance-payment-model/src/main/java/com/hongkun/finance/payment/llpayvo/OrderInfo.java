package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description : 商户订单信息bean
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.OrderInfo.java
 * @Author : yanbinghuang
 */
public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String no_order; // 商户唯一订单号
	private String dt_order; // 商户订单时间
	private String name_goods; // 商品名称
	private String info_order; // 订单描述
	private String money_order; // 交易金额 单位为RMB-元

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
