package com.hongkun.finance.vas.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.model.VasRedpacketInfo.java
 * @Class Name    : VasRedpacketInfo.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class VasRedpacketInfo extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 接收人员ID
	 * 字段: acceptor_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer acceptorUserId;
	
	/**
	 * 描述: 红包兑换码
	 * 字段: key  VARCHAR(10)
	 * 默认值: ''
	 */
	private String key;
	
	/**
	 * 描述: 金额
	 * 字段: value  DECIMAL(20)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal value;
	
	/**
	 * 描述: 红包名称
	 * 字段: name  VARCHAR(50)
	 * 默认值: ''
	 */
	private String name;
	
	/**
	 * 描述: 红包类型
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer type;
	
	/**
	 * 描述: 红包来源：1-线下生成，2-运营派发，3-跑批生成,4-个人派发
	 * 字段: redpacket_source  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer redpacketSource;
	
	/**
	 * 描述: 有效截止时间
	 * 字段: end_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endTimeEnd;
	/**
	 * 描述: 发送人员ID（出钱账户）
	 * 字段: sender_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer senderUserId;
	
	/**
	 * 描述: 发送人员名称
	 * 字段: sender_user_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private String senderUserName;
	
	/**
	 * 描述: 派发红包原因
	 * 字段: send_reason  VARCHAR(100)
	 * 默认值: ''
	 */
	private String sendReason;
	
	/**
	 * 描述: 审核通过或者拒绝的原因
	 * 字段: check_reason  VARCHAR(100)
	 * 默认值: ''
	 */
	private String checkReason;
	
	/**
	 * 描述: 创建人员ID
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer createUserId;
	
	/**
	 * 描述: 修改人员ID
	 * 字段: modified_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer modifiedUserId;
	
	/**
	 * 状态:0-未领取，1-已领取，2-已过期，4-待审核，5-财务拒绝，9-失效, 10-已删除(后台不展示)
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date modifyTimeEnd;
 
	public VasRedpacketInfo(){
	}

	public VasRedpacketInfo(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setAcceptorUserId(Integer acceptorUserId) {
		this.acceptorUserId = acceptorUserId;
	}
	
	public Integer getAcceptorUserId() {
		return this.acceptorUserId;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public void setValue(java.math.BigDecimal value) {
		this.value = value;
	}
	
	public java.math.BigDecimal getValue() {
		return this.value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setRedpacketSource(Integer redpacketSource) {
		this.redpacketSource = redpacketSource;
	}
	
	public Integer getRedpacketSource() {
		return this.redpacketSource;
	}
	
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTimeBegin(java.util.Date endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
	
	public java.util.Date getEndTimeBegin() {
		return this.endTimeBegin;
	}
	
	public void setEndTimeEnd(java.util.Date endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	
	public java.util.Date getEndTimeEnd() {
		return this.endTimeEnd;
	}	
	public void setSenderUserId(Integer senderUserId) {
		this.senderUserId = senderUserId;
	}
	
	public Integer getSenderUserId() {
		return this.senderUserId;
	}
	
	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}
	
	public String getSenderUserName() {
		return this.senderUserName;
	}
	
	public void setSendReason(String sendReason) {
		this.sendReason = sendReason;
	}
	
	public String getSendReason() {
		return this.sendReason;
	}
	
	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}
	
	public String getCheckReason() {
		return this.checkReason;
	}
	
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifiedUserId(Integer modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}
	
	public Integer getModifiedUserId() {
		return this.modifiedUserId;
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

