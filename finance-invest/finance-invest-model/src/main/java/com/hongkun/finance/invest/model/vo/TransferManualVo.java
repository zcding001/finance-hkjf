package com.hongkun.finance.invest.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransferManualVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//转让记录id
	private Integer id;
	private Integer newInvestId;
	private Integer oldInvestId;
	private Integer firstInvestId;
	private String oldRealName;
	private String newRealName;
	private Date oldInvestTime;
	private Date newInvestTime;
	private BigDecimal  creditorAmount;
	private BigDecimal oldInvestAmount;
	private Integer state;
	private Date transferTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNewInvestId() {
		return newInvestId;
	}
	public void setNewInvestId(Integer newInvestId) {
		this.newInvestId = newInvestId;
	}
	public Integer getOldInvestId() {
		return oldInvestId;
	}
	public void setOldInvestId(Integer oldInvestId) {
		this.oldInvestId = oldInvestId;
	}
	public Integer getFirstInvestId() {
		return firstInvestId;
	}
	public void setFirstInvestId(Integer firstInvestId) {
		this.firstInvestId = firstInvestId;
	}
	public String getOldRealName() {
		return oldRealName;
	}
	public void setOldRealName(String oldRealName) {
		this.oldRealName = oldRealName;
	}
	public String getNewRealName() {
		return newRealName;
	}
	public void setNewRealName(String newRealName) {
		this.newRealName = newRealName;
	}
	public Date getOldInvestTime() {
		return oldInvestTime;
	}
	public void setOldInvestTime(Date oldInvestTime) {
		this.oldInvestTime = oldInvestTime;
	}
	public Date getNewInvestTime() {
		return newInvestTime;
	}
	public void setNewInvestTime(Date newInvestTime) {
		this.newInvestTime = newInvestTime;
	}
	public BigDecimal getCreditorAmount() {
		return creditorAmount;
	}
	public void setCreditorAmount(BigDecimal creditorAmount) {
		this.creditorAmount = creditorAmount;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	
	public BigDecimal getOldInvestAmount() {
		return oldInvestAmount;
	}
	public void setOldInvestAmount(BigDecimal oldInvestAmount) {
		this.oldInvestAmount = oldInvestAmount;
	}
	
	
	
}
