package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.vo.QdzAutoCreditorVo.java
 * @Class Name : QdzAutoCreditorVo.java
 * @Description : 钱袋子自动债权转让实体
 * @Author : yanbinghuang
 */
public class QdzAutoCreditorVo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 交易流水ID
	private String flowId;
	// 交易类型
	private Integer tradeType;
	// 用户ID
	private Integer regUserId;
	// 债权金额
	private BigDecimal transMoney;
	// 钱袋子转出记录ID
	private Integer qdzTransRecordId;
	// 钱袋子利率
	private BigDecimal rate;
	// 钱袋子利率记录ID
	private Integer qdzRateRecordId;
	// 修复时间
	private Date repairTime;

	public Integer getQdzTransRecordId() {
		return qdzTransRecordId;
	}

	public void setQdzTransRecordId(Integer qdzTransRecordId) {
		this.qdzTransRecordId = qdzTransRecordId;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getQdzRateRecordId() {
		return qdzRateRecordId;
	}

	public void setQdzRateRecordId(Integer qdzRateRecordId) {
		this.qdzRateRecordId = qdzRateRecordId;
	}

	public Date getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

}
