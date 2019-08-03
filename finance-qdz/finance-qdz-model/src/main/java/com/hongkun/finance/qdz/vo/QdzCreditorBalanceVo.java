package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.model.QdzCreditorBalanceVo.java
 * @Class Name : QdzCreditorBalanceVo.java
 * @Description : 钱袋子债权对账实体类,用于前台展示
 * @Author : yanbinghuang
 */
public class QdzCreditorBalanceVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String bidId;// 标的ID
	private String bidName;// 标的名称
	private BigDecimal userInvestAtm;// 用户持有债权
	private BigDecimal thirdInvestAtm;// 第三方持有债权
	private BigDecimal bidAmount;// 标的总金额
	private BigDecimal investAtm;// 债权总金额
	private int isEqual;// 是否一致， 0 不一致
	private Integer status;// 标的状态

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	public BigDecimal getUserInvestAtm() {
		return userInvestAtm;
	}

	public void setUserInvestAtm(BigDecimal userInvestAtm) {
		this.userInvestAtm = userInvestAtm;
	}

	public BigDecimal getThirdInvestAtm() {
		return thirdInvestAtm;
	}

	public void setThirdInvestAtm(BigDecimal thirdInvestAtm) {
		this.thirdInvestAtm = thirdInvestAtm;
	}

	public BigDecimal getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(BigDecimal bidAmount) {
		this.bidAmount = bidAmount;
	}

	public BigDecimal getInvestAtm() {
		return investAtm;
	}

	public void setInvestAtm(BigDecimal investAtm) {
		this.investAtm = investAtm;
	}

	public int getIsEqual() {
		return isEqual;
	}

	public void setIsEqual(int isEqual) {
		this.isEqual = isEqual;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
