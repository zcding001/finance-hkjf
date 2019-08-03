package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.model.QdzHqProductInfoVo.java
 * @Class Name : QdzHqProductInfoVo.java
 * @Description : 活期产品信息VO,用于后台展示
 * @Author : yanbinghuang
 */
public class QdzHqProductInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	// 标的名称
	private String bidName;
	// 借款金额
	private BigDecimal borrowMoney;
	// 还本时间
	private Date repayPrincipalTime;
	// 距离归还本金剩余天数
	private Integer repaySurplusDays;

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public Date getRepayPrincipalTime() {
		return repayPrincipalTime;
	}

	public void setRepayPrincipalTime(Date repayPrincipalTime) {
		this.repayPrincipalTime = repayPrincipalTime;
	}

	public Integer getRepaySurplusDays() {
		return repaySurplusDays;
	}

	public void setRepaySurplusDays(Integer repaySurplusDays) {
		this.repaySurplusDays = repaySurplusDays;
	}

}
