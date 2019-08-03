package com.hongkun.finance.sms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.SmsWebMsgDetail.java
 * @Class Name    : SmsWebMsgDetail.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SmsWebMsgDetail extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 站内信id
	 * 字段: sms_web_msg_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer smsWebMsgId;
	
	/**
	 * 描述: 消息内容
	 * 字段: msg  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String msg;
	
 
	public SmsWebMsgDetail(){
	}

	public SmsWebMsgDetail(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setSmsWebMsgId(java.lang.Integer smsWebMsgId) {
		this.smsWebMsgId = smsWebMsgId;
	}
	
	public java.lang.Integer getSmsWebMsgId() {
		return this.smsWebMsgId;
	}
	
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	
	public java.lang.String getMsg() {
		return this.msg;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

