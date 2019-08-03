package com.hongkun.finance.contract.model;

import com.yirun.framework.core.model.BaseModel;

import java.util.Date;

/**
 * @Project       : finance-hkjf
 * @Description   : 汽车金融用户信息表（甲乙方）
 * @Author        : HeHang
 * @Date 		  : 2018-8-9
 */
public class CarUser extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private Long id;  			//id
	private String userName;  	//用户姓名
	private String corporate;  //法定代表人
	private String license;    //企业代码
	private String idCard;  	//用户身份证号
	private String userTel;  	//用户联系电话
	private String userAdress;  	//联系地址car
	private String bankAdress;  	//银行账户开户地址
	private String bankCardNo;  	//银行卡号
	private Date createTime;   	 	//创建时间
	private Date modifTime;   	 	//最后修改时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCorporate() {
		return corporate;
	}
	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getUserAdress() {
		return userAdress;
	}
	public void setUserAdress(String userAdress) {
		this.userAdress = userAdress;
	}
	public String getBankAdress() {
		return bankAdress;
	}
	public void setBankAdress(String bankAdress) {
		this.bankAdress = bankAdress;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifTime() {
		return modifTime;
	}
	public void setModifTime(Date modifTime) {
		this.modifTime = modifTime;
	}

	public CarUser() {
	}
}
