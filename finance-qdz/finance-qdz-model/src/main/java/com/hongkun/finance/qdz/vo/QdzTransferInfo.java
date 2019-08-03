package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class QdzTransferInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 转入记录流水ID
	private String flowId;
	// 用户ID集合
	private List<Integer> regUserIdList;
	// 类型
	private Integer type;
	// 交易金额
	private BigDecimal transMoney;
	// 交易之前的金额
	private BigDecimal preMoney;
	// 交易之后金额
	private BigDecimal afterMoney;
	// 状态
	private Integer state;
	// 创建时间开始
	private String createTimeBegin;
	// 创建时间结束
	private String createTimeEnd;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public List<Integer> getRegUserIdList() {
		return regUserIdList;
	}

	public void setRegUserIdList(List<Integer> regUserIdList) {
		this.regUserIdList = regUserIdList;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
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
}
