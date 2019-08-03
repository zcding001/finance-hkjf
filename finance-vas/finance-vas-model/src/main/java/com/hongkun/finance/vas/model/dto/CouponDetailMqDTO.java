package com.hongkun.finance.vas.model.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description : 系统补发卡券DTO
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.model.dto.CouponDetailMqDTO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class CouponDetailMqDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 发放卡券用户id */
	private Integer regUserId;
	/** 发放卡券产品 */
	private Integer couponProductId;
	/** 发放卡券原因 */
	private String reason;

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getCouponProductId() {
		return couponProductId;
	}

	public void setCouponProductId(Integer couponProductId) {
		this.couponProductId = couponProductId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
