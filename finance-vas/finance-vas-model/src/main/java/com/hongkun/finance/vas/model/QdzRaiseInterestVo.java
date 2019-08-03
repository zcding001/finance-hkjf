package com.hongkun.finance.vas.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.model.QdzRaiseInterestVo.java
 * @Class Name : QdzRaiseInterestVo.java
 * @Description : 钱袋子加息VO类
 * @Author : yanbinghuang
 */
public class QdzRaiseInterestVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// 钱袋子加息限额
	private BigDecimal raiseInterestLimit;
	// 钱袋子加息比率
	private BigDecimal raiseInterestRate;
	// 加息天数限额
	private Integer interestDayLimit;

	public BigDecimal getRaiseInterestLimit() {
		return raiseInterestLimit;
	}

	public void setRaiseInterestLimit(BigDecimal raiseInterestLimit) {
		this.raiseInterestLimit = raiseInterestLimit;
	}

	public BigDecimal getRaiseInterestRate() {
		return raiseInterestRate;
	}

	public void setRaiseInterestRate(BigDecimal raiseInterestRate) {
		this.raiseInterestRate = raiseInterestRate;
	}

	public Integer getInterestDayLimit() {
		return interestDayLimit;
	}

	public void setInterestDayLimit(Integer interestDayLimit) {
		this.interestDayLimit = interestDayLimit;
	}

}
