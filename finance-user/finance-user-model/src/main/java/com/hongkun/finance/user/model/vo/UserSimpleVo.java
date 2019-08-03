package com.hongkun.finance.user.model.vo;

import com.yirun.framework.core.model.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @Description   : 简单用户对象
 * @Project       : finance-user-model
 * @Program Name  : com.hongkun.finance.user.model.vo.UserSimpleVo.java
 * @Author        : xuhuiliu@honghun.com.cn 刘旭辉
 */
public class UserSimpleVo extends BaseModel {
	    
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Long login;
	private String realName;
	private String email;
	private String idCard;
	private String contactAddress;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getLogin() {
		return login;
	}
	public void setLogin(Long login) {
		this.login = login;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
}
