package com.hongkun.finance.vas.model.vo;

import com.hongkun.finance.vas.model.VasRedpacketInfo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包信息的前台展示类
 */
public class VasRedpacketVO extends VasRedpacketInfo {

	/**发送人手机号*/
	private  Long  senderTel;
	/**兑换用户手机号*/
	private Long  acceptorTel;
	/**兑换用户姓名*/
	private String  acceptorName;
	/**领取或失效时间开始-用于数据查询*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiveTimeBegin;
	/**领取或失效时间结束-用于数据查询*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiveTimeEnd;
	/**红包金额起始-用于数据查询*/
	private BigDecimal valueStart;
	/**红包金额结束-用于数据查询*/
	private BigDecimal valueEnd;


	public Date getReceiveTimeBegin() {
		return receiveTimeBegin;
	}

	public void setReceiveTimeBegin(Date receiveTimeBegin) {
		this.receiveTimeBegin = receiveTimeBegin;
	}

	public Date getReceiveTimeEnd() {
		return receiveTimeEnd;
	}

	public void setReceiveTimeEnd(Date receiveTimeEnd) {
		this.receiveTimeEnd = receiveTimeEnd;
	}

	public BigDecimal getValueStart() {
		return valueStart;
	}

	public void setValueStart(BigDecimal valueStart) {
		this.valueStart = valueStart;
	}

	public BigDecimal getValueEnd() {
		return valueEnd;
	}

	public void setValueEnd(BigDecimal valueEnd) {
		this.valueEnd = valueEnd;
	}

	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
	}

	public Long getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(Long senderTel) {
		this.senderTel = senderTel;
	}

	public Long getAcceptorTel() {
		return acceptorTel;
	}

	public void setAcceptorTel(Long acceptorTel) {
		this.acceptorTel = acceptorTel;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

