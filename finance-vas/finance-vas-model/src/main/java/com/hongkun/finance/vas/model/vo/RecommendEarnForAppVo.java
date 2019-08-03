package com.hongkun.finance.vas.model.vo;
import java.math.BigDecimal;
import java.util.Date;

import com.yirun.framework.core.model.BaseModel;

/**
 * 
 * @Description   : 奖励vo（app端展示） 
 * @Project       : finance-vas-model
 * @Program Name  : com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */

public class RecommendEarnForAppVo extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Integer regUserId;
	private Integer recommendRegUserId;
	private Long regUserTel;
	private BigDecimal earnAmount;
	private Date grantTime;
	private Integer type;
	private String source;
	
	public Long getRegUserTel() {
		return regUserTel;
	}
	public void setRegUserTel(Long regUserTel) {
		this.regUserTel = regUserTel;
	}
	public Integer getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	public BigDecimal getEarnAmount() {
		return earnAmount;
	}
	public void setEarnAmount(BigDecimal earnAmount) {
		this.earnAmount = earnAmount;
	}
	public Date getGrantTime() {
		return grantTime;
	}
	public void setGrantTime(Date grantTime) {
		this.grantTime = grantTime;
	}
	public Integer getRecommendRegUserId() {
		return recommendRegUserId;
	}
	public void setRecommendRegUserId(Integer recommendRegUserId) {
		this.recommendRegUserId = recommendRegUserId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
