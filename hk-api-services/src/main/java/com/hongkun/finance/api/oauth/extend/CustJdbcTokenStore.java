package com.hongkun.finance.api.oauth.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 解决获取access_token并发问题
 *
 * @author zc.ding
 * @create 2018/7/8
 */
public class CustJdbcTokenStore extends JdbcTokenStore implements CustTokenStore{

    private static final Log LOG = LogFactory.getLog(CustJdbcTokenStore.class);
    
    private static final String DEFAULT_ACCESS_TOKEN_STATEMENT = "insert into oauth_access_token (token_id, token, authentication_id, user_name, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)";

    private static final String DEFAULT_ACCESS_TOKEN_FROM_AUTHENTICATION_SELECT_STATEMENT = "select token_id, token from oauth_access_token where authentication_id = ?";
    
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    private final JdbcTemplate jdbcTemplateTmp;
    
    public CustJdbcTokenStore(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplateTmp = new JdbcTemplate(dataSource);
        super.setInsertAccessTokenSql(DEFAULT_ACCESS_TOKEN_STATEMENT);
    }
    
    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;

        String key = authenticationKeyGenerator.extractKey(authentication);
        try {
            accessToken = jdbcTemplateTmp.queryForObject(DEFAULT_ACCESS_TOKEN_FROM_AUTHENTICATION_SELECT_STATEMENT,
                    new RowMapper<OAuth2AccessToken>() {
                        @Override
                        public OAuth2AccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return deserializeAccessToken(rs.getBytes(2));
                        }
                    }, key);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.error("Could not extract access token for authentication " + authentication, e);
        }

        if (accessToken != null) {
            OAuth2Authentication authenticationTmp = readAuthentication(accessToken.getValue());
            if(authenticationTmp == null || !key.equals(authenticationKeyGenerator.extractKey(authenticationTmp))){
                removeAccessToken(accessToken.getValue());
                // Keep the store consistent (maybe the same user is represented by this authentication but the details have
                // changed)
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

//    @Override
//    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//        //先从缓存中获取access_token，若果没有，通过分布式锁创建access_token对象
//        OAuth2AccessToken accessToken = null;
//        String key = authenticationKeyGenerator.extractKey(authentication);
//        String accessTokenKey = "ACCESS_TOKEN:" + key;
//        if(this.getExistAccessToken(accessTokenKey) != null){
//            return;
//        }
//        //创建access_token创建时并发锁
//        String lockKey = "LOCK_INIT_ACCESS_TOKEN_" + key;
//        JedisClusterLock lock = new JedisClusterLock();
//        try {
//            if (lock.lock(lockKey)) {
//                if (getExistAccessToken(accessTokenKey) != null) {
//                    return;
//                }
//                String refreshToken = null;
//                if (token.getRefreshToken() != null) {
//                    refreshToken = token.getRefreshToken().getValue();
//                }
//                if (readAccessToken(token.getValue())!=null) {
//                    removeAccessToken(token.getValue());
//                }
//                jdbcTemplateTmp.update(DEFAULT_ACCESS_TOKEN_STATEMENT, new Object[] { extractTokenKey(token.getValue()),
//                        new SqlLobValue(serializeAccessToken(token)), authenticationKeyGenerator.extractKey(authentication),
//                        authentication.isClientOnly() ? null : authentication.getName(),
//                        authentication.getOAuth2Request().getClientId(),
//                        new SqlLobValue(serializeAuthentication(authentication)), extractTokenKey(refreshToken) }, new int[] {
//                        Types.VARCHAR, Types.BLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.VARCHAR });
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            lock.freeLock(lockKey);
//        }
//    }
//
//    /**
//     *  从redis获取有效的access_token
//     *  @Method_Name             ：getExistAccessToken
//     *  @param accessTokenKey
//     *  @return org.springframework.security.oauth2.common.OAuth2AccessToken
//     *  @Creation Date           ：2018/7/9
//     *  @Author                  ：zc.ding@foxmail.com
//     */
//    private OAuth2AccessToken getExistAccessToken(String accessTokenKey){
//        OAuth2AccessToken accessTokenTmp = JedisClusterUtils.getObjectForJson(accessTokenKey, DefaultOAuth2AccessToken.class);
//        long time = JedisClusterUtils.getRemainTime(accessTokenKey);
//        if(accessTokenTmp != null && JedisClusterUtils.getRemainTime(accessTokenKey) > 0){
//            ((DefaultOAuth2AccessToken) accessTokenTmp).setExpiration(new Date(System.currentTimeMillis() + (time * 1000L)));
//            return accessTokenTmp;
//        }
//        return null;
//    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        
    }
    
    @Override
    public void callCustStoreAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication){
        super.storeAccessToken(token, authentication);
    }
}
