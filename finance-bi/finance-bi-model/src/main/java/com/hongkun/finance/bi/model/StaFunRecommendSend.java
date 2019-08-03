package com.hongkun.finance.bi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.model.StaFunRecommendSend.java
 * @Class Name    : StaFunRecommendSend.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class StaFunRecommendSend extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 被推荐人
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: 被推荐人手机号
	 * 字段: login  BIGINT(19)
	 * 默认值: 0
	 */
	private java.lang.Long login;
	
	/**
	 * 描述: 被推荐人真实姓名
	 * 字段: real_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String realName;
	
	/**
	 * 描述: 推荐人标识
	 * 字段: commend_reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer commendRegUserId;
	
	/**
	 * 描述: 推荐人手机号
	 * 字段: commend_login  BIGINT(19)
	 * 默认值: 0
	 */
	private java.lang.Long commendLogin;
	
	/**
	 * 描述: 推荐人真实姓名
	 * 字段: commend_real_name  VARCHAR(30)
	 * 默认值: ''
	 */
	private java.lang.String commendRealName;
	
	/**
	 * 描述: 推荐来源  0-正常投资 1-钱袋子投资
	 * 字段: recommend_source  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer recommendSource;
	
	/**
	 * 描述: 奖金
	 * 字段: money  DECIMAL(10)
	 * 默认值: 0.00
	 */
	private java.math.BigDecimal money;
	
	/**
	 * 描述: 发放时间
	 * 字段: send_time  DATETIME(19)
	 * 默认值: 0000-00-00 00:00:00
	 */
	private java.util.Date sendTime;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendTimeBegin;
	
	//【非数据库字段，查询时使用】
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendTimeEnd;
 
	public StaFunRecommendSend(){
	}

	public StaFunRecommendSend(java.lang.Integer id){
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
	
	public void setLogin(java.lang.Long login) {
		this.login = login;
	}
	
	public java.lang.Long getLogin() {
		return this.login;
	}
	
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}
	
	public java.lang.String getRealName() {
		return this.realName;
	}
	
	public void setCommendRegUserId(java.lang.Integer commendRegUserId) {
		this.commendRegUserId = commendRegUserId;
	}
	
	public java.lang.Integer getCommendRegUserId() {
		return this.commendRegUserId;
	}
	
	public void setCommendLogin(java.lang.Long commendLogin) {
		this.commendLogin = commendLogin;
	}
	
	public java.lang.Long getCommendLogin() {
		return this.commendLogin;
	}
	
	public void setCommendRealName(java.lang.String commendRealName) {
		this.commendRealName = commendRealName;
	}
	
	public java.lang.String getCommendRealName() {
		return this.commendRealName;
	}
	
	public void setRecommendSource(Integer recommendSource) {
		this.recommendSource = recommendSource;
	}
	
	public Integer getRecommendSource() {
		return this.recommendSource;
	}
	
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	
	public java.math.BigDecimal getMoney() {
		return this.money;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

