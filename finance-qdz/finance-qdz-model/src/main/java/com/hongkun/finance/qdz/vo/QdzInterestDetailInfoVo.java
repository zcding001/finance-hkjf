package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QdzInterestDetailInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 用户标识 0-第三方用户 1-平台
	private Integer userFlag;
	// 计算利息日期
	private Date day;
	// 第三方用户ID
	private Integer thirdRegUserId;
	// 计算利息开始日期
	private Date dayBegin;
	// 计算利息结束日期
	private Date dayEnd;
	//日期集合
	private List<String> dayList;


    public List<String> getDayList() {
        return dayList;
    }

    public void setDayList(List<String> dayList) {
        this.dayList = dayList;
    }

    public Integer getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(Integer userFlag) {
		this.userFlag = userFlag;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Integer getThirdRegUserId() {
		return thirdRegUserId;
	}

	public void setThirdRegUserId(Integer thirdRegUserId) {
		this.thirdRegUserId = thirdRegUserId;
	}

	public Date getDayBegin() {
		return dayBegin;
	}

	public void setDayBegin(Date dayBegin) {
		this.dayBegin = dayBegin;
	}

	public Date getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(Date dayEnd) {
		this.dayEnd = dayEnd;
	}
}
