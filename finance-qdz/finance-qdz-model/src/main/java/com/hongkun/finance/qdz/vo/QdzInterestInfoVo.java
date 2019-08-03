package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.model.QdzInterestInfoVo.java
 * @Class Name : QdzInterestInfoVo.java
 * @Description : 查询钱袋子当天平台与第三方账户利息明细VO,用于前台展示
 * @Author : yanbinghuang
 */
public class QdzInterestInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 流水号
	 */
	private Integer id;
	/**
	 * 交易日期
	 */
	private Date day;
	/**
	 * 债权金额
	 */
	private BigDecimal money;
	/**
	 * 利息
	 */
	private BigDecimal dayInterest;
	/**
	 * 利率
	 */
	private BigDecimal rate;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户手机号
	 */
	private String loginTel;
	/**
	 * 利息生成开始时间
	 */
	private String startTime;
	/**
	 * 利息生成结束时间
	 */
	private String endTime;
	/**
	 * 描述: 划转状态:1成功，2失败
	 * 字段: state  TINYINT(3)
	 */
	private Integer state;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getDayInterest() {
		return dayInterest;
	}

	public void setDayInterest(BigDecimal dayInterest) {
		this.dayInterest = dayInterest;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginTel() {
		return loginTel;
	}

	public void setLoginTel(String loginTel) {
		this.loginTel = loginTel;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "QdzInterestInfoVo{" +
				"id=" + id +
				", day=" + day +
				", money=" + money +
				", dayInterest=" + dayInterest +
				", rate=" + rate +
				", userName='" + userName + '\'' +
				", loginTel='" + loginTel + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", state=" + state +
				'}';
	}
}
