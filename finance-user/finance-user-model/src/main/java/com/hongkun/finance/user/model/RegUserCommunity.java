package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.model.RegUserCommunity.java
 * @Class Name    : RegUserCommunity.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class RegUserCommunity extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private java.lang.Integer id;
	
	/**
	 * 描述: 物业用户id
	 * 字段: reg_user_id  INT(10)
	 */
	private java.lang.Integer regUserId;
	
	/**
	 * 描述: communityName
	 * 字段: community_name  VARCHAR(50)
	 */
	private java.lang.String communityName;
	
	/**
	 * 描述: '是否自取 0 非自取 1代表 为自取地址',
	 * 字段: community_type  TINYINT(3)
	 */
	private Integer communityType;
	
	/**
	 * 描述: 小区父id
	 * 字段: p_id  INT(10)
	 * 默认值: 0
	 */
	private java.lang.Integer pid;


	/**
	 * 描述: 小区状态
	 * 字段: state  INT(2)
	 * 默认值: 1
	 */
	private java.lang.Integer state;
	
	private String proName;
	
 
	public RegUserCommunity(){
	}

	public RegUserCommunity(java.lang.Integer id){
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
	
	public void setCommunityName(java.lang.String communityName) {
		this.communityName = communityName;
	}
	
	public java.lang.String getCommunityName() {
		return this.communityName;
	}
	
	public void setCommunityType(Integer communityType) {
		this.communityType = communityType;
	}
	
	public Integer getCommunityType() {
		return this.communityType;
	}
	
	public void setPid(java.lang.Integer pid) {
		this.pid = pid;
	}
	
	public java.lang.Integer getPid() {
		return this.pid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

