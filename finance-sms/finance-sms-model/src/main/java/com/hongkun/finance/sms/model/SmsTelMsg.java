package com.hongkun.finance.sms.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.SmsTelMsg.java
 * @Class Name    : SmsTelMsg.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SmsTelMsg extends BaseModel implements SmsMsgInfo{
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 接收用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 接收手机号
	 * 字段: tel  BIGINT(19)
	 * 默认值: 0
	 */
	private java.lang.Long tel;
	
	/**
	 * 描述: 短信内容
	 * 字段: msg  VARCHAR(500)
	 * 默认值: ''
	 */
	private java.lang.String msg;
	
	/**
	 * 描述: 短信类型:1-通知,2-验证码,3-推广
	 * 字段: type  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer type;
	
	/**
	 * 描述: 发送时间
	 * 字段: send_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendTimeEnd;
	/**
	 * 描述: 备注
	 * 字段: info  VARCHAR(50)
	 * 默认值: ''
	 */
	private java.lang.String info;
	
	/**
	 * 描述: 状态:0-初始状态,1-发送正常,2-验证码已使用
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  DATETIME(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
	
	/**
	 * 短信的业务标识，eg：9：还款通知短信， 与黑白名单中的RosterType中的SMS_REAPY_NOTICE的值对应
	 * 可扩展，以便在短信消费中进行短信的屏蔽
	 */
	private Integer busType;
 
	public SmsTelMsg(){
	}
	
	public SmsTelMsg(long tel, String msg, int type){
		this.tel = tel;
		this.msg = msg;
		this.type = type;
	}
	
	/**
	 * msg是模板类型数据，args用来替换模板中标识
	 * @param tel
	 * @param msg
	 * @param type
	 * @param args
	 */
	public SmsTelMsg(long tel, String msg, int type, Object[] args){
		this.tel = tel;
		this.msg = msg;
		if(args != null && args.length > 0){
			this.msg = String.format(msg, args);
		}
		this.type = type;
	}

	public SmsTelMsg(Integer regUserId, long tel, String msg, int type){
		this.regUserId = regUserId;
		this.tel = tel;
		this.msg = msg;
		this.type = type;
	}
	
	/**
	 * msg是模板类型数据，args用来替换模板中标识
	 * @param regUserId
	 * @param tel
	 * @param msg
	 * @param type
	 * @param args
	 */
	public SmsTelMsg(Integer regUserId, long tel, String msg, int type, Object[] args){
		this.regUserId = regUserId;
		this.tel = tel;
		this.msg = msg;
		if(args != null && args.length > 0){
			this.msg = String.format(msg, args);
		}
		this.type = type;
	}

	public SmsTelMsg(java.lang.Integer id){
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
	
	public void setTel(java.lang.Long tel) {
		this.tel = tel;
	}
	
	public java.lang.Long getTel() {
		return this.tel;
	}
	
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	
	public java.lang.String getMsg() {
		return this.msg;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public java.util.Date getSendTime() {
		return this.sendTime;
	}
	
	public void setSendTimeBegin(java.util.Date sendTimeBegin) {
		this.sendTimeBegin = sendTimeBegin;
	}
	
	public java.util.Date getSendTimeBegin() {
		return this.sendTimeBegin;
	}
	
	public void setSendTimeEnd(java.util.Date sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}
	
	public java.util.Date getSendTimeEnd() {
		return this.sendTimeEnd;
	}	
	public void setInfo(java.lang.String info) {
		this.info = info;
	}
	
	public java.lang.String getInfo() {
		return this.info;
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
	
	public Integer getBusType() {
		return busType;
	}

	public void setBusType(Integer busType) {
		this.busType = busType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

