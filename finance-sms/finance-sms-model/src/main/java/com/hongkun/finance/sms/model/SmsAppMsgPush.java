package com.hongkun.finance.sms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.sms.model.SmsAppMsgPush.java
 * @Class Name    : SmsAppMsgPush.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SmsAppMsgPush extends BaseModel implements SmsMsgInfo{
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 消息标题
	 * 字段: title  VARCHAR(100)
	 * 默认值: ''
	 */
	private String title;
	
	/**
	 * 描述: 消息内容
	 * 字段: content  VARCHAR(200)
	 * 默认值: ''
	 */
	private String content;
	
	/**
	 * 描述: 发送状态:1-待发送,2-发送成功,3-发送失败,4-发送停止,5-发送删除
	 * 字段: state  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer state;

	/**
	 * 描述: 消息收件人id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;

	/**
	 * 描述: 目标平台:1-andriod,2-ios
	 * 字段: target_platform  VARCHAR(4)
	 * 默认值: '1,2'
	 */
	private String targetPlatform;
	
	/**
	 * 描述: 目标用户:0-全部用户,1-标签用户
	 * 字段: target_user  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer targetUser;
	
	/**
	 * 描述: 用户标签:1-男,2-女,3-未投资,4-已投资
	 * 字段: target_user_tag  VARCHAR(8)
	 * 默认值: '1,2,3,4'
	 */
	private String targetUserTag;
	
	/**
	 * 描述: 用户标签设置交集还是并集:1-并集,2-交集
	 * 字段: target_user_tag_set  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer targetUserTagSet;
	
	/**
	 * 描述: 推送方式:0-即时,1-定时
	 * 字段: push_mode  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer pushMode;
	
	/**
	 * 描述: 推送时间(push_mode设置为定时推送时使用)
	 * 字段: push_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date pushTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date pushTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date pushTimeEnd;
	/**
	 * 描述: 推送消息有效期设置:0-无,1-需设置消息有效时间
	 * 字段: msg_expire_set  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer msgExpireSet;
	
	/**
	 * 描述: 推送消息的有效期时间（正整数小时数）
	 * 字段: msg_expire_time  SMALLINT(5)
	 * 默认值: 0
	 */
	private Integer msgExpireTime;
	
	/**
	 * 描述: 查看推送消息时的动作:1-启动应用,2-打开链接
	 * 字段: msg_action  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer msgAction;
	
	/**
	 * 描述: 查看推送消息时打开的链接地址
	 * 字段: msg_action_link  VARCHAR(100)
	 * 默认值: ''
	 */
	private String msgActionLink;

	/**
	 * 描述: 推送任务id
	 * 字段: task_id  VARCHAR(50)
	 */
	private java.lang.String taskId;

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

	//【非数据库字段，业务标识封装使用】
	private Integer type;  //0默认打开app；1商户收款；2好友券转赠(参见app后台)；3系统后台发送：跳转链接 ; 4:好友投资通知

	private String customParams;  //存储业务需要的推送自定义参数，用^分割
 
	public SmsAppMsgPush(){
	}
	public SmsAppMsgPush(Integer regUserId,String title,String content){
		this.regUserId = regUserId;
		this.title = title;
		this.content = content;
	}

	/**
	 * msg是模板类型数据，args用来替换模板中标识
	 * @param regUserId
	 * @param title
	 * @param msg
	 * @param args
	 */
	public SmsAppMsgPush(Integer regUserId, String title, String msg, Object[] args){
		this.regUserId = regUserId;
		this.title = title;
		if(args != null){
			this.content = String.format(msg, args);
		}else{
			this.content = msg;
		}
	}

	public SmsAppMsgPush(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setTargetPlatform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}
	
	public String getTargetPlatform() {
		return this.targetPlatform;
	}
	
	public void setTargetUser(Integer targetUser) {
		this.targetUser = targetUser;
	}
	
	public Integer getTargetUser() {
		return this.targetUser;
	}
	
	public void setTargetUserTag(String targetUserTag) {
		this.targetUserTag = targetUserTag;
	}
	
	public String getTargetUserTag() {
		return this.targetUserTag;
	}
	
	public void setTargetUserTagSet(Integer targetUserTagSet) {
		this.targetUserTagSet = targetUserTagSet;
	}
	
	public Integer getTargetUserTagSet() {
		return this.targetUserTagSet;
	}
	
	public void setPushMode(Integer pushMode) {
		this.pushMode = pushMode;
	}
	
	public Integer getPushMode() {
		return this.pushMode;
	}
	
	public void setPushTime(java.util.Date pushTime) {
		this.pushTime = pushTime;
	}
	
	public java.util.Date getPushTime() {
		return this.pushTime;
	}
	
	public void setPushTimeBegin(java.util.Date pushTimeBegin) {
		this.pushTimeBegin = pushTimeBegin;
	}
	
	public java.util.Date getPushTimeBegin() {
		return this.pushTimeBegin;
	}
	
	public void setPushTimeEnd(java.util.Date pushTimeEnd) {
		this.pushTimeEnd = pushTimeEnd;
	}
	
	public java.util.Date getPushTimeEnd() {
		return this.pushTimeEnd;
	}	
	public void setMsgExpireSet(Integer msgExpireSet) {
		this.msgExpireSet = msgExpireSet;
	}
	
	public Integer getMsgExpireSet() {
		return this.msgExpireSet;
	}
	
	public void setMsgExpireTime(Integer msgExpireTime) {
		this.msgExpireTime = msgExpireTime;
	}
	
	public Integer getMsgExpireTime() {
		return this.msgExpireTime;
	}
	
	public void setMsgAction(Integer msgAction) {
		this.msgAction = msgAction;
	}
	
	public Integer getMsgAction() {
		return this.msgAction;
	}
	
	public void setMsgActionLink(String msgActionLink) {
		this.msgActionLink = msgActionLink;
	}
	
	public String getMsgActionLink() {
		return this.msgActionLink;
	}

	public void setTaskId(java.lang.String taskId) {
		this.taskId = taskId;
	}

	public java.lang.String getTaskId() {
		return this.taskId;
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

	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCustomParams() {
		return customParams;
	}

	public void setCustomParams(String customParams) {
		this.customParams = customParams;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

