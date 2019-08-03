package com.hongkun.finance.bi.model;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.model.StaFunInvite.java
 * @Class Name    : StaFunInvite.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaFunInvite extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private Integer id;
	
	/**
	 * 描述: regUserId
	 * 字段: reg_user_id  INT(10)
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 真实性名
	 * 字段: real_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private String realName;
	
	/**
	 * 描述: 注册时间
	 * 字段: reg_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date regTime;
	
	//【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date regTimeBegin;
	
	//【非数据库字段，查询时使用】
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date regTimeEnd;
	/**
	 * 描述: 手机号(登录名)
	 * 字段: login  BIGINT(19)
	 * 默认值: 0
	 */
	private Long login;
	
	/**
	 * 描述: 邀请码
	 * 字段: invite_no  VARCHAR(8)
	 */
	private String inviteNo;
	
	/**
	 * 描述: 邀请人数
	 * 字段: invite_sum  INT(10)
	 * 默认值: 0
	 */
	private Integer inviteSum;
	
	/**
	 * 描述: 有效投资金额
	 * 字段: invite_amount  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal inviteAmount;
	
	/**
	 * 描述: 创建时间
	 * 字段: create_time  TIMESTAMP(19)
	 * 默认值: CURRENT_TIMESTAMP
	 */
	private java.util.Date createTime;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeBegin;
	
	//【非数据库字段，查询时使用】
	private java.util.Date createTimeEnd;
 
	public StaFunInvite(){
	}

	public StaFunInvite(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	public Integer getRegUserId() {
		return this.regUserId;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getRealName() {
		return this.realName;
	}
	
	public void setRegTime(java.util.Date regTime) {
		this.regTime = regTime;
	}
	
	public java.util.Date getRegTime() {
		return this.regTime;
	}
	
	public void setRegTimeBegin(java.util.Date regTimeBegin) {
		this.regTimeBegin = regTimeBegin;
	}
	
	public java.util.Date getRegTimeBegin() {
		return this.regTimeBegin;
	}
	
	public void setRegTimeEnd(java.util.Date regTimeEnd) {
		this.regTimeEnd = regTimeEnd;
	}
	
	public java.util.Date getRegTimeEnd() {
		return this.regTimeEnd;
	}	
	public void setLogin(Long login) {
		this.login = login;
	}
	
	public Long getLogin() {
		return this.login;
	}
	
	public void setInviteNo(String inviteNo) {
		this.inviteNo = inviteNo;
	}
	
	public String getInviteNo() {
		return this.inviteNo;
	}
	
	public void setInviteSum(Integer inviteSum) {
		this.inviteSum = inviteSum;
	}
	
	public Integer getInviteSum() {
		return this.inviteSum;
	}
	
	public void setInviteAmount(java.math.BigDecimal inviteAmount) {
		this.inviteAmount = inviteAmount;
	}
	
	public java.math.BigDecimal getInviteAmount() {
		return this.inviteAmount;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

