package com.hongkun.finance.user.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

public class CustSimpleGrantedAuthority implements GrantedAuthority{
	
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private String role;

	public CustSimpleGrantedAuthority() {}
	
	public CustSimpleGrantedAuthority(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
	}

	public String getAuthority() {
		return role;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SimpleGrantedAuthority) {
			return role.equals(((CustSimpleGrantedAuthority) obj).role);
		}

		return false;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int hashCode() {
		return this.role.hashCode();
	}

	public String toString() {
		return this.role;
	}

	
}
