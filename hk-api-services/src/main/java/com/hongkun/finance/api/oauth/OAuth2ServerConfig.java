package com.hongkun.finance.api.oauth;

import com.hongkun.finance.api.oauth.extend.CustJdbcTokenStore;
import com.hongkun.finance.api.oauth.extend.CustTokenServices;
import com.hongkun.finance.api.oauth.extend.CustTokenStore;
import com.yirun.framework.core.utils.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

@Configuration
public class OAuth2ServerConfig {

	/**
	 * @Description : 资源服务,多个资源服务配置多个MobileResourceServerConfig即可
	 * @Project : hk-api-services
	 * @Program Name : com.hongkun.finance.api.oauth.OAuth2ServerConfig.java
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Configuration
	@EnableResourceServer
	protected static class MobileResourceServerConfig extends ResourceServerConfigurerAdapter {
		private static final String DEFAULT_RESOURCE_ID = "mobile-resource";
		private final static String OAUTH2_SWITCH = "oauth2.switch";

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			// stateless为false那么只要有一次access_token验证通过，其余的请求都不会再验证access_token
			resources.resourceId(DEFAULT_RESOURCE_ID).stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http = http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
					.requestCache().disable();
			String oauth2Switch = PropertiesHolder.getProperty(OAUTH2_SWITCH);
			if (StringUtils.isNotBlank(oauth2Switch) && "0".equals(oauth2Switch)) {
				// 不开启oauth2验证，所有请求允许通过
				http.authorizeRequests().antMatchers("/**").permitAll();
			} else {
				// 除了*Controller/**下的请求其他请求全部拒绝
				http
						// .authorizeRequests().antMatchers("/oauth/**").permitAll().and()
						// 需要放行的请求
						.authorizeRequests()
						.antMatchers("/paymentRechargeController/searchBankCardList",
								     "/contractController/*",
								     "/fundController/getUserInfoForRiskEvaluation",
								     "/fundController/saveRiskEvaluation",
								     "/finPaymenCallBackController/*"/**充值异步通知回调**/,
								     "/informationController/listInformationRecord",
								     "/fundController/toFundAgreementPage",
								     "/fundController/initFundAgreement",
								     "/fundController/toFundAgreementStep2",
								     "/fundController/toFundAgreementStep3",
								     "/fundController/showPdf",
								     "/fundController/ossPdfToInputStream",
								     "/informationController/searchInfomationDetail"/**资讯详情H5页面**/,
								     "/informationController/aboutUsPage"/**关于我们H5页面**/,
								     "/informationController/searchQuestionnaireDetail"/**调查问卷H5页面**/,
								     "/informationController/findQuestionnaireInfo"/**查询调查问卷信息**/,
								     "/informationController/saveInfoQuestionnaireAnswer"/**保存调查问卷信息**/,
								     "/informationController/findExistQuestion"/**查询问卷问题**/,
								     "/bidInfoController/firstBidDirect"/**新手标指引页**/,
									"/houseController/showHouseDetailById"/**房产分享详情页**/
								)
						.permitAll().and()
						// 此处配置是业务层oauth2的拦截 start
						.authorizeRequests().antMatchers("/*Controller/**").hasRole("MOBILE").and()
						// 此处配置是业务层oauth2的拦截 end
						.authorizeRequests().antMatchers("/**").permitAll();
			}
		}
	}

	/**
	 * @Description : 授权服务器
	 * @Project : hk-api-services
	 * @Program Name : com.hongkun.finance.api.oauth.OAuth2ServerConfig.java
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
		@Autowired
		private DataSource dataSource;

		@Autowired
		private CustTokenStore tokenStore;
		
        @Autowired
		private CustTokenServices custTokenServices;

		@Autowired
		private JdbcClientDetailsService jdbcClientDetailsService;

		/**
		 * authenticationManagerBean在SecurityConfig完成初始化
		 */
		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(this.jdbcClientDetailsService);
		}
		

		/**
		 * @Description : token存储介质，可自定义
		 * @Method_Name : tokenStore
		 * @return : TokenStore
		 * @Creation Date : 2018年3月20日 下午4:20:42
		 * @Author : zhichaoding@hongkun.com zc.ding
		 */
		@Bean
		public CustTokenStore tokenStore() {
			return new CustJdbcTokenStore(dataSource);
		}

		/**
		 * @Description : clinetDetail维护实现类,可自定义
		 * @Method_Name : jdbcClientDetailsService
		 * @return : JdbcClientDetailsService
		 * @Creation Date : 2018年3月20日 下午4:18:11
		 * @Author : zhichaoding@hongkun.com zc.ding
		 */
		@Bean
		public JdbcClientDetailsService jdbcClientDetailsService() {
			return new JdbcClientDetailsService(dataSource);
		}

		/**
		*  用于解决获取access_token并发问题
		*  @Method_Name             ：custTokenServices
		* 
		*  @return com.hongkun.finance.api.oauth.extend.CustTokenServices
		*  @Creation Date           ：2018/7/9
		*  @Author                  ：zc.ding@foxmail.com
		*/
		@Bean
        public CustTokenServices custTokenServices(){
            CustTokenServices custTokenServices = new CustTokenServices();
            custTokenServices.setTokenStore(tokenStore);
            custTokenServices.setClientDetailsService(jdbcClientDetailsService);
		    return custTokenServices;    
        }
        
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		    //由于加载顺序问题，此处需要设置下面属性
            custTokenServices.setClientDetailsService(jdbcClientDetailsService);
            custTokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
            custTokenServices.setTokenStore(tokenStore);
            endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
					// 支持POST和GET请求，生产环境支持POST请求, HttpMethod.GET
					.allowedTokenEndpointRequestMethods(HttpMethod.POST).tokenServices(custTokenServices);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			// 允许security和aouth使用相同身份验证
			oauthServer.allowFormAuthenticationForClients();
		}
	}
}
