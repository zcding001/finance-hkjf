package com.hongkun.finance.vas.model.vo;

import com.hongkun.finance.vas.model.VasCouponDetail;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @Description : 卡券详情前台展示类
 * @Project : framework
 * @Program Name : com.hongkun.finance.vas.model.vo.VasCouponDetailVO
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class VasCouponDetailVO extends VasCouponDetail {

	/** 卡券产品名称 **/
	private String name;

	/** 卡券产品类型 **/
	private Integer type;

	/** 卡券详情用户手机号 **/
	private Long tel;

	/** 卡券详情是否有转赠记录,0无，1有 **/
	private Integer hasDonationRecord;

	/**
	 * 描述: 最小投资金额 字段: amount_min DOUBLE(20) 默认值: 0.00
	 */
	private BigDecimal amountMin;

	/**
	 * 描述: 最大投资金额 字段: amount_max DOUBLE(20) 默认值: 0.00
	 */
	private BigDecimal amountMax;
	/**
	 * 卡券用户Id
	 */
	private Integer regUserId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getTel() {
		return tel;
	}

	public void setTel(Long tel) {
		this.tel = tel;
	}

	public Integer getHasDonationRecord() {
		return hasDonationRecord;
	}

	public void setHasDonationRecord(Integer hasDonationRecord) {
		this.hasDonationRecord = hasDonationRecord;
	}

	public BigDecimal getAmountMin() {
		return amountMin;
	}

	public void setAmountMin(BigDecimal amountMin) {
		this.amountMin = amountMin;
	}

	public BigDecimal getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(BigDecimal amountMax) {
		this.amountMax = amountMax;
	}

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
