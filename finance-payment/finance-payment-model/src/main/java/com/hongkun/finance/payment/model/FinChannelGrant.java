package com.hongkun.finance.payment.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.model.FinChannelGrant.java
 * @Class Name    : FinChannelGrant.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class FinChannelGrant extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT UNSIGNED(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 系统名称
	 * 字段: sys_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String sysName;
	
	/**
	 * 描述: 系统名称编码
	 * 字段: sys_name_code  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String sysNameCode;
	
	/**
	 * 描述: 支付渠道名称
	 * 字段: channel_name  VARCHAR(20)
	 * 默认值: ''
	 */
	private java.lang.String channelName;
	
	/**
	 * 描述: 排序号
	 * 字段: sort  INT(10)
	 * 默认值: 1000
	 */
	private java.lang.Integer sort;
	
	/**
	 * 描述: 支付渠道标识  1-连连支付  2-联动支付 3-宝付支付
	 * 字段: channel_name_code  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer channelNameCode;
	
	/**
	 * 描述: 支付方式 10 充值 14提现
	 * 字段: pay_style  TINYINT(3)
	 * 默认值: 0
	 */
	private Integer payStyle;
	
	/**
	 * 描述: 平台来源 10-PC 11-IOS 12-ANDRIOD 13-WAP
	 * 字段: platform_source  VARCHAR(20)
	 * 默认值: '10'
	 */
	private java.lang.String platformSource;
	
	/**
	 * 描述: 状态:0-禁用,1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
 
	public FinChannelGrant(){
	}

	public FinChannelGrant(java.lang.Integer id){
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setSysName(java.lang.String sysName) {
		this.sysName = sysName;
	}
	
	public java.lang.String getSysName() {
		return this.sysName;
	}
	
	public void setSysNameCode(java.lang.String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}
	
	public java.lang.String getSysNameCode() {
		return this.sysNameCode;
	}
	
	public void setChannelName(java.lang.String channelName) {
		this.channelName = channelName;
	}
	
	public java.lang.String getChannelName() {
		return this.channelName;
	}
	
	public void setSort(java.lang.Integer sort) {
		this.sort = sort;
	}
	
	public java.lang.Integer getSort() {
		return this.sort;
	}
	
	public void setChannelNameCode(Integer channelNameCode) {
		this.channelNameCode = channelNameCode;
	}
	
	public Integer getChannelNameCode() {
		return this.channelNameCode;
	}
	
	public void setPayStyle(Integer payStyle) {
		this.payStyle = payStyle;
	}
	
	public Integer getPayStyle() {
		return this.payStyle;
	}
	
	public void setPlatformSource(java.lang.String platformSource) {
		this.platformSource = platformSource;
	}
	
	public java.lang.String getPlatformSource() {
		return this.platformSource;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

