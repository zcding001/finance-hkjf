package com.hongkun.finance.invest.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;

/**
 * @Description   : 匹配--需要进行债权转让的参数对象
 * @Project       : finance-invest-model
 * @Program Name  : com.hongkun.finance.invest.model.vo.TransferAutoVo.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferAutoVo implements Serializable {

	//散标标的
	public BidInfo commBidInfo;
	//原债权投资列表
	public List<BidInvest> transferInvestList;
	//新债权投资列表
	public List<BidInvest> receiveInvestList;
	//债权转让时间
	public Date transferTime;

	public Date getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}

	public BidInfo getCommBidInfo() {
		return commBidInfo;
	}
	public void setCommBidInfo(BidInfo commBidInfo) {
		this.commBidInfo = commBidInfo;
	}
	public List<BidInvest> getTransferInvestList() {
		return transferInvestList;
	}
	public void setTransferInvestList(List<BidInvest> transferInvestList) {
		this.transferInvestList = transferInvestList;
	}
	public List<BidInvest> getReceiveInvestList() {
		return receiveInvestList;
	}
	public void setReceiveInvestList(List<BidInvest> receiveInvestList) {
		this.receiveInvestList = receiveInvestList;
	}
	
}
