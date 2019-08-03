package com.hongkun.finance.contract.model;

import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.yirun.framework.core.model.BaseModel;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.model.ConType.java
 * @Class Name    : ConType.java
 * @Description   : GENERATOR VO类
 * @Author        : generator
 */
public class ConType extends BaseModel {
	
	private static final long serialVersionUID = 1L;
 
	/**
	 * 描述: id
	 * 字段: id  INT(10)
	 */
	private Integer id;
	
	/**
	 * 描述: 合同类型
	 * 字段: type  TINYINT(3)
	 * 默认值: 0
	 */
	@NotNull(message = "合同类型不能为空！",groups = SAVE.class)
	private Integer type;
	
	/**
	 * 描述: 合同名称
	 * 字段: name  VARCHAR(50)
	 * 默认值: ''
	 */
	@NotBlank(message = "合同名称不能为空！",groups = SAVE.class)
	private String name;
	
	/**
	 * 描述: 显示名称
	 * 字段: show_name  VARCHAR(50)
	 * 默认值: ''
	 */
	@NotBlank(message = "合同显示名称不能为空！",groups = SAVE.class)
	private String showName;
	
	/**
	 * 描述: 创建人员ID
	 * 字段: create_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer createUserId;
	
	/**
	 * 描述: 最后修改人员ID
	 * 字段: modify_user_id  INT(10)
	 * 默认值: 0
	 */
	private Integer modifyUserId;
	
	/**
	 * 描述: 合同状态：0-禁用，1-启用
	 * 字段: state  TINYINT(3)
	 * 默认值: 1
	 */
	private Integer state;
	
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
 
	public ConType(){
	}

	public ConType(Integer id){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getShowName() {
		return this.showName;
	}
	
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
	public Integer getCreateUserId() {
		return this.createUserId;
	}
	
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	public Integer getModifyUserId() {
		return this.modifyUserId;
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

