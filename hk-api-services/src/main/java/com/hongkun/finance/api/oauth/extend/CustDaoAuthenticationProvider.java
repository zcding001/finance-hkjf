package com.hongkun.finance.api.oauth.extend;

import com.hongkun.finance.user.utils.ValidateUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description   : 授权提供者，处理自定义属性
 * @Project       : hk-api-services
 * @Program Name  : com.hongkun.finance.api.oauth.CustDaoAuthenticationProvider.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class CustDaoAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if(authentication.getDetails() != null && authentication.getDetails() instanceof HashMap<?, ?>) {
			Map<String, String> map = (Map<String, String>) authentication.getDetails();
			//对密码进行RSA解密
//			Integer pwdLength = Integer.parseInt(Optional.ofNullable(map.get("pwdLength")).orElse("0"));
			String pwd = authentication.getCredentials().toString();
			pwd = ValidateUtil.decodePasswd(pwd);
			authentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), pwd);
					((AbstractAuthenticationToken) authentication).setDetails(map);
		}
		super.additionalAuthenticationChecks(userDetails, authentication);
	}
}
