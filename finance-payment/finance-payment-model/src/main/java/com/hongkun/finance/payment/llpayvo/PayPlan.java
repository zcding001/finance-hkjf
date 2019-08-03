
/**
 * 
 */
package com.hongkun.finance.payment.llpayvo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hongkun.finance.payment.model.vo.WithDrawCash;

/**
 * @Description : 代扣还款
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.llpayvo.PayPlan.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class PayPlan extends WithDrawCash implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 还款金额 **/
	private BigDecimal repayAmount;
	/** 还款ID **/
	private String repayId;
	/** 还款标的ID **/
	private String bidId;
	/** 还款人身份证ID **/
	private String idCard;
	/** h还款信息描述 **/
	private String info;
	/** 当期还款时间 **/
	private Date curPlayDate;
	/** 第三方协议号 **/
	private String thirdAccount;
	/** 银行卡号 */
	private Long bankCardId;

	public Long getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(Long bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public Date getCurPlayDate() {
		return curPlayDate;
	}

	public void setCurPlayDate(Date curPlayDate) {
		this.curPlayDate = curPlayDate;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public String getRepayId() {
		return repayId;
	}

	public void setRepayId(String repayId) {
		this.repayId = repayId;
	}

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

}
