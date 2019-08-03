package com.hongkun.finance.qdz.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hongkun.finance.qdz.enums.TransTypeEnum;

public class QdzTransferInOutCondition implements Serializable {

	private static final long serialVersionUID = 1L;
	// 用户ID
	private Integer regUserId;
	// 交易前金额
	private BigDecimal preMoney;
	// 交易金额
	private BigDecimal transMoney;
	// 交易类型
	private TransTypeEnum transFlag;
	// 交易来源
	private Integer source;
	// 交易标识 0-插入钱袋子账户;1-更新钱袋子账户信息
	private Integer bugFlag;
	// 创建时间
	private Date createTime;

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public BigDecimal getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public TransTypeEnum getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(TransTypeEnum transFlag) {
		this.transFlag = transFlag;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getBugFlag() {
		return bugFlag;
	}

	public void setBugFlag(Integer bugFlag) {
		this.bugFlag = bugFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
