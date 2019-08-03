package com.hongkun.finance.api.oauth.extend;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.OauthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CustUserDetailServiceImpl implements UserDetailsService{
	@Reference
	private OauthUserService oauthUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("username must be not null");
		}
		String password = Optional.ofNullable(this.oauthUserService.findOauthUserByUserName(username))
				.orElseThrow(() -> new UsernameNotFoundException("Not found any user for username[" + username + "]"))
				.getPassword();
		//默认添加ROLE_MOBILE&ROLE_UNITY权限
		return new User(username, password, Arrays.asList(new SimpleGrantedAuthority("ROLE_MOBILE"), new SimpleGrantedAuthority("ROLE_UNITY")));
	}
}
