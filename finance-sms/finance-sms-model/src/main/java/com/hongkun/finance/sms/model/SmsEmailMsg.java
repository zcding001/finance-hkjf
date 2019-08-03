package com.hongkun.finance.sms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.SmsEmailMsg.java
 * @Class Name    : SmsEmailMsg.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SmsEmailMsg extends BaseModel implements SmsMsgInfo {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 抄送人邮箱，多个邮箱中间以","分割
	 * 字段: cc_email  TEXT(65535)
	 */
	private java.lang.String ccEmail;
	
	/**
	 * 描述: 邮箱地址
	 * 字段: email  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String email;
	
	/**
	 * 描述: 标题
	 * 字段: title  VARCHAR(200)
	 * 默认值: ''
	 */
	private java.lang.String title;
	
	/**
	 * 描述: 消息内容
	 * 字段: msg  VARCHAR(1000)
	 * 默认值: ''
	 */
	private java.lang.String msg;
	
	/**
	 * 描述: 邮件附件的文件绝对地址
	 * 字段: adjunct_path  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String adjunctPath;
	
	/**
	 * 描述: 邮件消息类型
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 状态:0-发送失败,1-发送成功
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
	/**
	 * 描述: 修改时间
	 * 字段: modify_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date modifyTimeEnd;
 
	public SmsEmailMsg(){/**defalut**/}
	
	public SmsEmailMsg(Integer regUserId, String email, String msg, Integer type){
		this.regUserId = regUserId;
		this.email = email;
		this.msg = msg;
		this.type = type;
	}
	
	public SmsEmailMsg(Integer regUserId, String email, String msg, Integer type, Object[] args){
		this.regUserId = regUserId;
		this.email = email;
		this.msg = msg;
		if(args != null && args.length > 0){
			this.msg = String.format(msg, args);
		}
		this.type = type;
	}
	
	public SmsEmailMsg(Integer regUserId, String email, String title, String msg, Integer type){
		this.regUserId = regUserId;
		this.email = email;
		this.title = title;
		this.msg = msg;
		this.type = type;
	}
	
	public SmsEmailMsg(Integer regUserId, String email, String title, String msg, Integer type, Object[] args){
		this.regUserId = regUserId;
		this.email = email;
		this.title = title;
		this.msg = msg;
		if(args != null && args.length > 0){
			this.msg = String.format(msg, args);
		}
		this.type = type;
	}

	public SmsEmailMsg(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(java.lang.Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public java.lang.Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setCcEmail(java.lang.String ccEmail) {
		this.ccEmail = ccEmail;
	}
	
	public java.lang.String getCcEmail() {
		return this.ccEmail;
	}
	
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	
	public java.lang.String getEmail() {
		return this.email;
	}
	
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	public java.lang.String getTitle() {
		return this.title;
	}
	
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	
	public java.lang.String getMsg() {
		return this.msg;
	}
	
	public void setAdjunctPath(java.lang.String adjunctPath) {
		this.adjunctPath = adjunctPath;
	}
	
	public java.lang.String getAdjunctPath() {
		return this.adjunctPath;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

