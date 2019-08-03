package com.hongkun.finance.api.oauth.extend;

import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.JedisClusterUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 在createAccessToken加上分布式锁解决获取access_token并发问题
 *
 * @author zc.ding
 * @create 2018/7/9
 */
public class CustTokenServices extends DefaultTokenServices {

    private CustTokenStore tokenStore;

    private TokenEnhancer accessTokenEnhancer;
    
    @Transactional
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            }
            else {
                // Re-store the access token in case the authentication has changed
                //关闭accessToken刷新操作，防止数据库并发时死锁
//                tokenStore.storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }
        }

        // Only create a new refresh token if there wasn't an existing one
        // associated with an expired access token.
        // Clients might be holding existing refresh tokens, so we re-use it in
        // the case that the old access token
        // expired.
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        }
        // But the refresh token itself might need to be re-issued if it has
        // expired.
        else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }

        //先从缓存中获取access_token，若果没有，通过分布式锁创建access_token对象
        OAuth2AccessToken accessToken = null;
        AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
        String key = authenticationKeyGenerator.extractKey(authentication);
        String accessTokenKey = "ACCESS_TOKEN:" + key;
        accessToken = this.getExistAccessToken(accessTokenKey);
        if(accessToken != null){
            return accessToken;
        }
        //创建access_token创建时并发锁
        String lockKey = "LOCK_ACCESS_TOKEN_" + key;
        JedisClusterLock lock = new JedisClusterLock();
        try {
            if (lock.lock(lockKey)) {
                accessToken = getExistAccessToken(accessTokenKey);
                if (accessToken != null) {
                    return accessToken;
                }else{
                    accessToken = createAccessToken(authentication, refreshToken);
//                    tokenStore.storeAccessToken(accessToken, authentication);
                    tokenStore.callCustStoreAccessToken(accessToken, authentication);
                    // In case it was modified
                    refreshToken = accessToken.getRefreshToken();
                    if (refreshToken != null) {
                        tokenStore.storeRefreshToken(refreshToken, authentication);
                    }
                    JedisClusterUtils.setAsJson(accessTokenKey, accessToken);
                    JedisClusterUtils.setExpireTime(accessTokenKey, accessToken.getExpiresIn());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.freeLock(lockKey);
        }
        return accessToken;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());

        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }

    /**
     *  从redis获取有效的access_token
     *  @Method_Name             ：getExistAccessToken
     *  @param accessTokenKey
     *  @return org.springframework.security.oauth2.common.OAuth2AccessToken
     *  @Creation Date           ：2018/7/9
     *  @Author                  ：zc.ding@foxmail.com
     */
    private OAuth2AccessToken getExistAccessToken(String accessTokenKey){
        OAuth2AccessToken accessTokenTmp = JedisClusterUtils.getObjectForJson(accessTokenKey, DefaultOAuth2AccessToken.class);
        long time = JedisClusterUtils.getRemainTime(accessTokenKey);
        if(accessTokenTmp != null && JedisClusterUtils.getRemainTime(accessTokenKey) > 0){
            ((DefaultOAuth2AccessToken) accessTokenTmp).setExpiration(new Date(System.currentTimeMillis() + (time * 1000L)));
            return accessTokenTmp;
        }
        return null;
    }

//    @Override
    public void setTokenStore(CustTokenStore tokenStore) {
        this.tokenStore = tokenStore;
        super.setTokenStore(tokenStore);
    }

    public void setAccessTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
    }

    @Override
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        super.setClientDetailsService(clientDetailsService);
    }
}
