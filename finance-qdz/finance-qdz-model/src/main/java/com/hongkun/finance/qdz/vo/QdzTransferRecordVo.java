package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.model.QdzTransferRecordVo.java
 * @Class Name : QdzTransferRecordVo.java
 * @Description : 钱袋子账单流水记录VO,用于后台展示
 * @Author : yanbinghuang
 */
public class QdzTransferRecordVo implements Serializable {

	private static final long serialVersionUID = 1L;

	// 流水号
	private String flowId;
	// 交易时间
	private Date createTime;
	// 交易时间开始
	private String createTimeBegin;
	// 交易时间结束
	private String createTimeEnd;
	// 用户姓名
	private String userName;
	// 用户手机号
	private String loginTel;
	// 交易金额
	private BigDecimal transMoney;
	// 交易类型
	private Integer type;
	// 交易前金额
	private BigDecimal preMoney;
	// 账户余额
	private BigDecimal afterMoney;
	// 状态
	private Integer state;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
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

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}

	public BigDecimal getAfterMoney() {
		return afterMoney;
	}

	public void setAfterMoney(BigDecimal afterMoney) {
		this.afterMoney = afterMoney;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "QdzTransferRecordVo{" +
				"flowId='" + flowId + '\'' +
				", createTime=" + createTime +
				", createTimeBegin='" + createTimeBegin + '\'' +
				", createTimeEnd='" + createTimeEnd + '\'' +
				", userName='" + userName + '\'' +
				", loginTel='" + loginTel + '\'' +
				", transMoney=" + transMoney +
				", type=" + type +
				", preMoney=" + preMoney +
				", afterMoney=" + afterMoney +
				", state=" + state +
				'}';
	}
}
