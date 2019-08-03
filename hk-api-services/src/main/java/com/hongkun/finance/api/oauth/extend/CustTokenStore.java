package com.hongkun.finance.api.oauth.extend;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

public interface CustTokenStore extends TokenStore {

    /**
    *  @Method_Name             ：callCustStoreAccessToken
    *  @param token
    *  @param authentication
    *  @return void
    *  @Creation Date           ：2018/7/9
    *  @Author                  ：zc.ding@foxmail.com
    */
    void callCustStoreAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication);
}
