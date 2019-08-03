package com.hongkun.finance.user.support.security.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CookieUtil;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.redis.JedisClusterUtils;

/**
 * token切面，添加、验证、移除token<br/>
 * 逻辑：<br/>
 * 1、js中通过ajax，添加submitToken到cookie中，即使提交的请求中参数中没有携带，那么服务端会从cookie中获取<br/>
 * 2、先获得参数或是cookie中的submitToken中，服务端先获得session中的submitToken值，<br/>
 * 如果没有以submitToken为key从redis中加载，然后进行对比，结果存储到request中
 *
 * @author zc.ding
 * @version 1.1
 * @since 2017年5月21日
 */
@Component
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TokenValidateAop implements ProxyTargetI {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidateAop.class);

    @Around("@annotation(com.yirun.framework.core.annotation.Token)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Token token = getAnnotation(point, Token.class);
        String tokenValue = HttpSessionUtil.getRequest().getParameter(Constants.WEB_SUBMIT_TOKEN);
        logger.info("client params submit token value: {}", tokenValue);
        if (token != null) {
            if(!this.validateToken(token, tokenValue)){
            	return new ResponseEntity<>(Constants.ERROR, "无效的token参数");
            }
        }
        return point.proceed();
    }

    /**
     * 验证Token
     *
     * @param token
     * @param tokenValue
     * @author zc.ding
     * @since 2017年5月21日
     */
    private boolean validateToken(Token token, String tokenValue) {
    	boolean flag = true;
    	Ope type = token.operate();
        //加载cookie中的submitToken
        String submitTokenKey = CookieUtil.getCookieValue(BaseUtil.getSubmitTokenKey());
        logger.info("get submit token from cookie is {}", submitTokenKey);
        switch (type) {
            case ADD://添加
                BaseUtil.refreshSumbToken();
                break;
            case REFRESH://验证&刷新
                flag = doValidateToken(submitTokenKey, tokenValue);
                BaseUtil.refreshSumbToken();
                break;
            case REMOVE://删除
            	flag = doValidateToken(submitTokenKey, tokenValue);
                break;
            default:
                break;
        }
        if(StringUtils.isNotBlank(submitTokenKey)) {
        	//redis中的submitToken使用后删除
            JedisClusterUtils.delete(submitTokenKey);
        }
        return flag;
    }

    /**
     * 验证token是否有效
     *
     * @param submitTokenKey 服务端缓存token值的的key
     * @param tokenValue     客户端传递的token值
     * @author zc.ding
     * @since 2017年5月21日
     */
    private boolean doValidateToken(String submitTokenKey, String tokenValue) {
        //获得服务端存储的submitToken
        String tv = HttpSessionUtil.getAttrFromSession(BaseUtil.getSubmitTokenKey());
        // 删除使用过的session中的submitToken
        HttpSessionUtil.removeAttrFromSession(BaseUtil.getSubmitTokenKey());
        if (StringUtils.isBlank(tv) && StringUtils.isNotBlank(submitTokenKey)) {
            tv = JedisClusterUtils.get(submitTokenKey);
        }
        //获得客户端传过来的submitToken
        String tokenValueTmp = tokenValue;
        if (StringUtils.isBlank(tokenValueTmp) && StringUtils.isNotBlank(submitTokenKey)) {
            tokenValueTmp = submitTokenKey;
        }
        if (StringUtils.isBlank(tv) || StringUtils.isBlank(tokenValueTmp) || !tv.equals(tokenValueTmp)) {
            HttpSessionUtil.addAttrToRequest(Constants.WEB_SUBMIT_TOKEN_STATUS, "0");
            logger.info("submit_token is invalid. value : {}", tokenValueTmp);
            return false;
        } else {
            logger.info("submit_token verify successfully");
            return true;
        }
    }
}
