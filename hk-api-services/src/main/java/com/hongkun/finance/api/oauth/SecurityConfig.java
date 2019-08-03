package com.hongkun.finance.api.oauth;

import com.hongkun.finance.api.oauth.extend.CustDaoAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @Description   : Securit认证服务器
 * @Project       : hk-api-services
 * @Program Name  : com.hongkun.finance.api.oauth.SecurityConfig.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService custUserDetailService;
	
	/**
	 * 初始化用户验证服务
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(custUserDetailService);
		//创建盐源
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		saltSource.setUserPropertyToUse("username");
		//定义加密算法
		Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		//使用自定义的验证提供者，以便进行RSA解密
		DaoAuthenticationProvider authenticationProvider = new CustDaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(auth.getDefaultUserDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setSaltSource(saltSource);
		auth.authenticationProvider(authenticationProvider);
	}

    @Override
    public void configure(WebSecurity web) throws Exception {
    	//配置不需要验证权限的资源
        web.ignoring().antMatchers("/oauth/uncache_approvals", "/oauth/cache_approvals");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
                .exceptionHandling().accessDeniedPage("/login.jsp?authorization_error=true")
                .and()
                .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable().logout().logoutUrl("/logout").logoutSuccessUrl("/login.jsp")
                .and()
                .formLogin().loginProcessingUrl("/login").failureUrl("/login.jsp").loginPage("/login.jsp").passwordParameter("pwd").usernameParameter("userName")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().requestCache().disable();
	}
	
}
