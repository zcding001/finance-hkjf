package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.model.SysUserRoleRel.java
 * @Class Name    : SysUserRoleRel.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysUserRoleRel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: 主键
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 用户id
	 * 字段: reg_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer regUserId;
	
	/**
	 * 描述: 角色id
	 * 字段: sys_role_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sysRoleId;
	
	/**
	 * 描述: 状态:0-删除,1-正常
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
 
	public SysUserRoleRel(){
	}

	public SysUserRoleRel(Integer id){
		this.id = id;
	}
	public SysUserRoleRel(Integer regUserId,Integer sysRoleId){
		this.regUserId = regUserId;
		this.sysRoleId = sysRoleId;
		this.state = 1;/*默认有效*/
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
	
	public void setSysRoleId(Integer sysRoleId) {
		this.sysRoleId = sysRoleId;
	}
	
	public Integer getSysRoleId() {
		return this.sysRoleId;
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

