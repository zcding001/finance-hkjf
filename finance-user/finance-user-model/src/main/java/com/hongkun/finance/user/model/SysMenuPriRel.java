package com.hongkun.finance.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.model.SysMenuPriRel.java
 * @Class Name    : SysMenuPriRel.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class SysMenuPriRel extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: sysMenuId
	 * 字段: sys_menu_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sysMenuId;
	
	/**
	 * 描述: sysPriId
	 * 字段: sys_pri_id  INT(10)
	 * 默认值: 0
	 */
	private Integer sysPriId;
	
	/**
	 * 描述: 状态：0-删除 1-正常 2-禁用
	 * 字段: state  INT(10)
	 * 默认值: 1
	 */
	private Integer state;
	
 
	public SysMenuPriRel(){
	}

	public SysMenuPriRel(Integer id){
		this.id = id;
	}
	public SysMenuPriRel(Integer sysMenuId,Integer sysPriId){
		this.sysMenuId = sysMenuId;
		this.sysPriId = sysPriId;
		this.state=1;/*默认是有效状态*/
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setSysMenuId(Integer sysMenuId) {
		this.sysMenuId = sysMenuId;
	}
	
	public Integer getSysMenuId() {
		return this.sysMenuId;
	}
	
	public void setSysPriId(Integer sysPriId) {
		this.sysPriId = sysPriId;
	}
	
	public Integer getSysPriId() {
		return this.sysPriId;
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

