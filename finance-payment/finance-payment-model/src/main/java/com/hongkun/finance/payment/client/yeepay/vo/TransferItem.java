package com.hongkun.finance.payment.client.yeepay.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class TransferItem {
	private String mer_Id;//实际发起付款的交易商户编号
	private String order_Id;//订单号
	private String r1_Code;//打款状态码
	private String bank_Status;//银行状态
	private String request_Date;//发起时间
	private String payee_Name; //收款人名称
	private String payee_BankName;//开户行
	private String payee_Bank_Account;//收款账号
	private String amount;//余额
	private String note;//留言
	private String fee;//手续费
	private String real_pay_amount;//实付金额
	private String complete_Date; //处理时间
	private String refund_Date;//退款时间
	private String fail_Desc;//失败原因
	private String abstractInfo;//摘要
	private String remarksInfo;//备注
	public String getMer_Id() {
		return mer_Id;
	}
	public void setMer_Id(String mer_Id) {
		this.mer_Id = mer_Id;
	}
	public String getOrder_Id() {
		return order_Id;
	}
	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}
	public String getR1_Code() {
		return r1_Code;
	}
	public void setR1_Code(String r1_Code) {
		this.r1_Code = r1_Code;
	}
	public String getBank_Status() {
		return bank_Status;
	}
	public void setBank_Status(String bank_Status) {
		this.bank_Status = bank_Status;
	}
	public String getRequest_Date() {
		return request_Date;
	}
	public void setRequest_Date(String request_Date) {
		this.request_Date = request_Date;
	}
	public String getPayee_Name() {
		return payee_Name;
	}
	public void setPayee_Name(String payee_Name) {
		this.payee_Name = payee_Name;
	}
	public String getPayee_BankName() {
		return payee_BankName;
	}
	public void setPayee_BankName(String payee_BankName) {
		this.payee_BankName = payee_BankName;
	}
	public String getPayee_Bank_Account() {
		return payee_Bank_Account;
	}
	public void setPayee_Bank_Account(String payee_Bank_Account) {
		this.payee_Bank_Account = payee_Bank_Account;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getReal_pay_amount() {
		return real_pay_amount;
	}
	public void setReal_pay_amount(String real_pay_amount) {
		this.real_pay_amount = real_pay_amount;
	}
	public String getComplete_Date() {
		return complete_Date;
	}
	public void setComplete_Date(String complete_Date) {
		this.complete_Date = complete_Date;
	}
	public String getRefund_Date() {
		return refund_Date;
	}
	public void setRefund_Date(String refund_Date) {
		this.refund_Date = refund_Date;
	}
	public String getFail_Desc() {
		return fail_Desc;
	}
	public void setFail_Desc(String fail_Desc) {
		this.fail_Desc = fail_Desc;
	}
	public String getAbstractInfo() {
		return abstractInfo;
	}
	public void setAbstractInfo(String abstractInfo) {
		this.abstractInfo = abstractInfo;
	}
	public String getRemarksInfo() {
		return remarksInfo;
	}
	public void setRemarksInfo(String remarksInfo) {
		this.remarksInfo = remarksInfo;
	}
	
}
