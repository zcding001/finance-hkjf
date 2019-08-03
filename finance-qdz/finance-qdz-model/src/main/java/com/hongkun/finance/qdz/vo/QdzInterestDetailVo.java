package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.model.QdzInterestDetailVo.java
 * @Class Name : QdzInterestDetailVo.java
 * @Description : 查询钱袋子当天平台与第三方账户利息VO,用于前台展示
 * @Author : yanbinghuang
 */
public class QdzInterestDetailVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 平台垫付利息
	 */
	private BigDecimal platSumInterestMoney;
	/**
	 * 第三方垫付利息
	 */
	private BigDecimal thirdBusinesSumInterestMoney;
	/**
	 * 钱袋子总利息支付
	 */
	private BigDecimal qdzTotalInterestMoney;
	/**
	 * 交易日志
	 */
	private Date day;
	/**
	 * 是否一致 true:一致；false:不一致
	 */
	private boolean identical;
	//记录某天的资金划转中钱袋子的总利息
	private BigDecimal money;

	public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getPlatSumInterestMoney() {
		return platSumInterestMoney;
	}

	public void setPlatSumInterestMoney(BigDecimal platSumInterestMoney) {
		this.platSumInterestMoney = platSumInterestMoney;
	}

	public BigDecimal getThirdBusinesSumInterestMoney() {
		return thirdBusinesSumInterestMoney;
	}

	public void setThirdBusinesSumInterestMoney(BigDecimal thirdBusinesSumInterestMoney) {
		this.thirdBusinesSumInterestMoney = thirdBusinesSumInterestMoney;
	}

	public BigDecimal getQdzTotalInterestMoney() {
		return qdzTotalInterestMoney;
	}

	public void setQdzTotalInterestMoney(BigDecimal qdzTotalInterestMoney) {
		this.qdzTotalInterestMoney = qdzTotalInterestMoney;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public boolean isIdentical() {
		return identical;
	}

	public void setIdentical(boolean identical) {
		this.identical = identical;
	}
}
