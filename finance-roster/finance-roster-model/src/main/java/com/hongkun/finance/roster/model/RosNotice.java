package com.hongkun.finance.roster.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.model.RosNotice.java
 * @Class Name    : RosNotice.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RosNotice extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 功能模块：1对账 2 发送现金红包 3、发送加息券 4、发送投资红包
	 * 字段: type  TINYINT(3)
	 */
	private Integer type;
	
	/**
	 * 描述: 提醒方式：1邮件,2短信,3站内信
	 * 字段: notice_way  TINYINT(3)
	 */
	private Integer noticeWay;
	
	/**
	 * 描述: 接收邮箱
	 * 字段: receive_email  VARCHAR(100)
	 */
	private String receiveEmail;
	
	/**
	 * 描述: 接收手机号
	 * 字段: receive_tel  VARCHAR(100)
	 */
	private String receiveTel;
	
	/**
	 * 描述: 接收手机短信时间
	 * 字段: receive_tel_time  DATETIME(19)
	 */
	private java.util.Date receiveTelTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date receiveTelTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date receiveTelTimeEnd;
	/**
	 * 描述: 创建时间
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  TIMESTAMP(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
	/**
	 * 描述: 备注
	 * 字段: note  VARCHAR(200)
	 */
	private String note;
	
 
	public RosNotice(){
	}

	public RosNotice(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setNoticeWay(Integer noticeWay) {
		this.noticeWay = noticeWay;
	}
	
	public Integer getNoticeWay() {
		return this.noticeWay;
	}
	
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	
	public String getReceiveEmail() {
		return this.receiveEmail;
	}
	
	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}
	
	public String getReceiveTel() {
		return this.receiveTel;
	}
	
	public void setReceiveTelTime(java.util.Date receiveTelTime) {
		this.receiveTelTime = receiveTelTime;
	}
	
	public java.util.Date getReceiveTelTime() {
		return this.receiveTelTime;
	}
	
	public void setReceiveTelTimeBegin(java.util.Date receiveTelTimeBegin) {
		this.receiveTelTimeBegin = receiveTelTimeBegin;
	}
	
	public java.util.Date getReceiveTelTimeBegin() {
		return this.receiveTelTimeBegin;
	}
	
	public void setReceiveTelTimeEnd(java.util.Date receiveTelTimeEnd) {
		this.receiveTelTimeEnd = receiveTelTimeEnd;
	}
	
	public java.util.Date getReceiveTelTimeEnd() {
		return this.receiveTelTimeEnd;
	}	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}
	
	public java.util.Date getCreateTimeBegin() {
		return this.createTimeBegin;
	}
	
	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	public java.util.Date getCreateTimeEnd() {
		return this.createTimeEnd;
	}	
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTimeBegin(java.util.Date modifyTimeBegin) {
		this.modifyTimeBegin = modifyTimeBegin;
	}
	
	public java.util.Date getModifyTimeBegin() {
		return this.modifyTimeBegin;
	}
	
	public void setModifyTimeEnd(java.util.Date modifyTimeEnd) {
		this.modifyTimeEnd = modifyTimeEnd;
	}
	
	public java.util.Date getModifyTimeEnd() {
		return this.modifyTimeEnd;
	}	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNote() {
		return this.note;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

