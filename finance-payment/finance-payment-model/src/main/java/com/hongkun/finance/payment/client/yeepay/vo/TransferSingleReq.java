package com.hongkun.finance.payment.client.yeepay.vo;

import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TransferSingleReq {
	private String cmd;//命令
	private String version;//接口版本
	private String mer_Id;//实际发起付款的交易商户编号
	private String group_Id;//总公司商户编号
	private String product;//产品类型 可以为空
	private String batch_No;//打款批次号
	private String order_Id;//订单号
	private String bank_Code;//收款银行编号
	private String cnaps;//联行号
	private String bank_Name;//收款银行全称
	private String branch_Bank_Name;//收款银行支行名称
	private String amount;//打款金额
	private String account_Name;//账户名称
	private String account_Number;//账户号
	private String account_Type;//账户类型 对公：pu ,对私:pr 默认对私
	private String province;//收款行省份编码
	private String city;//收款行城市编码
	private String fee_Type;//手续费收取方式 取值：“SOURCE” 商户承担  “TARGET” 用户
	private String payee_Email;//收款人邮箱
	private String payee_Mobile;//收款人手机号
	private String leave_Word;// 留言
	private String abstractInfo;//摘要
	private String remarksInfo;//备注
	private String urgency;// 是否加急 是否需要实时出款,只能填写 0 或者 1 。“1”表示实时出款，“0”表示非实时出款。
	
	private String query_Mode;//本级下级标识
	private String page_No;//查询页面 从1开始递增

	
	private String ret_Code;//返回代码
	private String r1_Code;//打款状态码
	private String bank_Status;//银行状态
	private String error_Msg;//错误描述信息
	
	private String status;
	private String message;
	
	private String total_Num; //返回记录数
	
	private String end_Flag; //结束标识  “Y”---表示查询结果已全部输出完毕；“N”---表示查询结果只输出一部分，
	
	
	
	/**
	 * 
	 * 并采用商户证书进行签名
	 */
	private String hmac;
	
	
	private TransferList list;
	
	
	
	
	public TransferList getList() {
		return list;
	}
	public void setList(TransferList list) {
		this.list = list;
	}
	public String getQuery_Mode() {
		return query_Mode;
	}
	public void setQuery_Mode(String query_Mode) {
		this.query_Mode = query_Mode;
	}
	public String getPage_No() {
		return page_No;
	}
	public void setPage_No(String page_No) {
		this.page_No = page_No;
	}
	public String getTotal_Num() {
		return total_Num;
	}
	public void setTotal_Num(String total_Num) {
		this.total_Num = total_Num;
	}
	public String getEnd_Flag() {
		return end_Flag;
	}
	public void setEnd_Flag(String end_Flag) {
		this.end_Flag = end_Flag;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMer_Id() {
		return mer_Id;
	}
	public void setMer_Id(String mer_Id) {
		this.mer_Id = mer_Id;
	}
	public String getGroup_Id() {
		return group_Id;
	}
	public void setGroup_Id(String group_Id) {
		this.group_Id = group_Id;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getBatch_No() {
		return batch_No;
	}
	public void setBatch_No(String batch_No) {
		this.batch_No = batch_No;
	}
	public String getOrder_Id() {
		return order_Id;
	}
	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}
	public String getBank_Code() {
		return bank_Code;
	}
	public void setBank_Code(String bank_Code) {
		this.bank_Code = bank_Code;
	}
	public String getCnaps() {
		return cnaps;
	}
	public void setCnaps(String cnaps) {
		this.cnaps = cnaps;
	}
	public String getBank_Name() {
		return bank_Name;
	}
	public void setBank_Name(String bank_Name) {
		this.bank_Name = bank_Name;
	}
	public String getBranch_Bank_Name() {
		return branch_Bank_Name;
	}
	public void setBranch_Bank_Name(String branch_Bank_Name) {
		this.branch_Bank_Name = branch_Bank_Name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAccount_Name() {
		return account_Name;
	}
	public void setAccount_Name(String account_Name) {
		this.account_Name = account_Name;
	}
	public String getAccount_Number() {
		return account_Number;
	}
	public void setAccount_Number(String account_Number) {
		this.account_Number = account_Number;
	}
	public String getAccount_Type() {
		return account_Type;
	}
	public void setAccount_Type(String account_Type) {
		this.account_Type = account_Type;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFee_Type() {
		return fee_Type;
	}
	public void setFee_Type(String fee_Type) {
		this.fee_Type = fee_Type;
	}
	public String getPayee_Email() {
		return payee_Email;
	}
	public void setPayee_Email(String payee_Email) {
		this.payee_Email = payee_Email;
	}
	public String getPayee_Mobile() {
		return payee_Mobile;
	}
	public void setPayee_Mobile(String payee_Mobile) {
		this.payee_Mobile = payee_Mobile;
	}
	public String getLeave_Word() {
		return leave_Word;
	}
	public void setLeave_Word(String leave_Word) {
		this.leave_Word = leave_Word;
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
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public String getRet_Code() {
		return ret_Code;
	}
	public void setRet_Code(String ret_Code) {
		this.ret_Code = ret_Code;
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
	public String getError_Msg() {
		return error_Msg;
	}
	public void setError_Msg(String error_Msg) {
		this.error_Msg = error_Msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	} 
	
	
	
	
}
