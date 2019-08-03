package com.hongkun.finance.payment.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.enums.PlatformSourceEnums;

/**
 * @Description : 转账VO
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.TransferInfo.java
 * @Author : yanbinghuang
 */
public class TransferVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/***
	 * 发起人用户ID
	 */
	private Integer fromUserId;
	/**
	 * 接收人用户ID
	 */
	private Integer toUserId;
	/**
	 * 交易金额
	 */
	private BigDecimal transMoney;
	/**
	 * 业务CODE
	 */
	private String businessCode;
	/**
	 * 交易类型
	 */
	private Integer tradeType;
	/**
	 * 交易来源 10-PC 11-IOS 12-ANDRIOD 13-WAP(TradeSourceEnum)
	 */
	private PlatformSourceEnums platformSourceEnums;
	/**
	 * 收入划转code
	 */
	private Integer transferInSubCode;
	/**
	 * 支出划转CODE
	 */
	private Integer transferOutSubCode;
	/**
	 * 积分消费标识 1：有积分消费 0:没有积分消费 TradeStateConstants.EXIST_POINT_PAY
	 * 或TradeStateConstants.NO_EXIST_POINT_PAY ,默认 0：没有积分消费
	 */
	private Integer pointPayFlag = 0;
	/**
	 * 积分转换为现金的金额
	 */
	private BigDecimal pointChangeMoney;
	/**
	 * 流水创建时间,用于后续事物的回滚
	 */
	private Date createTime;

	public Integer getPointPayFlag() {
		return pointPayFlag;
	}

	public void setPointPayFlag(Integer pointPayFlag) {
		this.pointPayFlag = pointPayFlag;
	}

	public PlatformSourceEnums getPlatformSourceEnums() {
		return platformSourceEnums;
	}

	public void setPlatformSourceEnums(PlatformSourceEnums platformSourceEnums) {
		this.platformSourceEnums = platformSourceEnums;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getTransferInSubCode() {
		return transferInSubCode;
	}

	public void setTransferInSubCode(Integer transferInSubCode) {
		this.transferInSubCode = transferInSubCode;
	}

	public Integer getTransferOutSubCode() {
		return transferOutSubCode;
	}

	public void setTransferOutSubCode(Integer transferOutSubCode) {
		this.transferOutSubCode = transferOutSubCode;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public BigDecimal getPointChangeMoney() {
		return pointChangeMoney;
	}

	public void setPointChangeMoney(BigDecimal pointChangeMoney) {
		this.pointChangeMoney = pointChangeMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
