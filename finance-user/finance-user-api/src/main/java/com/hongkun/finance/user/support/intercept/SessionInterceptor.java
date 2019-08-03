package com.hongkun.finance.user.support.intercept;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.utils.CookieUtil;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.core.utils.response.ResponseUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.hongkun.finance.user.support.security.SecurityConstants.NO_LOGIN_MESSAGE;
import static com.yirun.framework.core.commons.Constants.TICKET_EXPIRES_TIME;

/**
 * @Description   : APP端session拦截器，不需要处理复杂逻辑因此与PC端分开
 * @Project       : finance-user-api
 * @Program Name  : com.hongkun.finance.user.support.intercept.SessionInterceptor.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class SessionInterceptor implements HandlerInterceptor , HandlerMethodArgumentResolver {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!HandlerMethod.class.isInstance(handler)) {
			//静态资源放行
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean refushSessionFlag = this.reflushSessionTime(request);
        AskForPermission askForPermission = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AskForPermission.class);
		//含有OperationTypeEnum.DISCRIMINATE_NO_LOGIN不需要验证即可访问
		if(askForPermission != null && askForPermission.operation() == OperationTypeEnum.DISCRIMINATE_NO_LOGIN) {
			return true;
		}
		//未登录
        if(!refushSessionFlag){
		    return noLogin(response);
        }
		return true;
	}
	
	/**
	*  刷新session过期时间
	*  @Method_Name             ：reflushSessionTime
	*  @param request
	*  @return boolean
	*  @Creation Date           ：2018/6/29
	*  @Author                  ：zc.ding@foxmail.com
	*/
	private boolean reflushSessionTime(HttpServletRequest request){
        String sessionId = request.getParameter(Constants.SESSION_ID_KEY);
        //如果用户已经登录、刷新session超时时间
        if(StringUtils.isBlank(sessionId)) {
            Cookie cookie = CookieUtil.getCookie(HttpSessionUtil.getRequest(), Constants.TICKET);
            if(cookie == null || StringUtils.isBlank(cookie.getValue())){
                return false;
            }
            sessionId = cookie.getValue();
        }
        RegUser regUser = JedisClusterUtils.getObjectForJson(Constants.SESSION_ID_KEY_PREFIX + sessionId, RegUser.class);
        if(regUser == null) {
            return false;
        }
        HttpSessionUtil.addLoginUser(regUser);
        JedisClusterUtils.setExpireTime(Constants.SESSION_ID_KEY_PREFIX + sessionId, TICKET_EXPIRES_TIME);
        JedisClusterUtils.setExpireTime(Constants.SESSION_ID_KEY_PREFIX + regUser.getLogin(), TICKET_EXPIRES_TIME);
	    return true;
    }

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> pagerClass =   parameter.getParameterType();
		//只对Pager的对象进行解析
		if (pagerClass != null && pagerClass.equals(Pager.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter,
								  ModelAndViewContainer modelAndViewContainer,
								  NativeWebRequest nativeWebRequest,
								  WebDataBinderFactory webDataBinderFactory) throws Exception {

		//设置无限分页参数(因为App端都是瀑布式的分页),注意: 请求方法可能不是一个需要分页的方法,此处简单处理,减少许多判断
		Pager pager = new Pager();
		BeanUtils.populate(pager,nativeWebRequest.getParameterMap());
		pager.setInfiniteMode(true);
		return pager;
	}






	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
	/**
	 *  @Description    : 返回未登录的数据 
	 *  @Method_Name    : noLogin
	 *  @param response
	 *  @return         : boolean
	 *  @Creation Date  : 2018年3月20日 下午1:58:07 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private boolean noLogin(HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		map.put("resStatus", UserConstants.NO_LOGIN + "");
		map.put("resMsg", NO_LOGIN_MESSAGE);
		JsonUtils.toJson(map);
		ResponseUtils.responseJson(response, JsonUtils.toJson(map));
		return false;
	}
	
	

}
