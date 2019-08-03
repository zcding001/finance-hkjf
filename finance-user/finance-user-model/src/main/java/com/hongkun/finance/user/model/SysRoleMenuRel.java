package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.model.SysRoleMenuRel.java
 * @Class Name    : SysRoleMenuRel.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysRoleMenuRel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: sysRoleId
	 * 字段: sys_role_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sysRoleId;
	
	/**
	 * 描述: sysMenuId
	 * 字段: sys_menu_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sysMenuId;
	
	/**
	 * 描述: 状态：0-删除 1-正常 2-禁用
	 * 字段: state  INT(10)
	 * 默认值: 1
	 */
	private Integer state;
	
 
	public SysRoleMenuRel(){
	}

	public SysRoleMenuRel(Integer id){
		this.id = id;
	}

	public SysRoleMenuRel(Integer sysRoleId,Integer sysMenuId){
		this.sysRoleId = sysRoleId;
		this.sysMenuId = sysMenuId;
		this.state=1;/*默认是有效状态*/
	}



	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setSysRoleId(Integer sysRoleId) {
		this.sysRoleId = sysRoleId;
	}
	
	public Integer getSysRoleId() {
		return this.sysRoleId;
	}
	
	public void setSysMenuId(Integer sysMenuId) {
		this.sysMenuId = sysMenuId;
	}
	
	public Integer getSysMenuId() {
		return this.sysMenuId;
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

