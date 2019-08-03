package com.hongkun.finance.user.support.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 解决不能直接获得HttpServletResponse的问题,添加后通过{@linkcom.yirun.framework.core.utils.HttpSessionUtil#getResponse()}
 * Spring3.x通过将response添加request中
 * Spring4.x以上支持 ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
 * @author 	 zc.ding
 * @since 	 2017年5月21日
 * @version  1.1
 */
public class AddResponseInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		//
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		arg0.removeAttribute("ATTR_RESPONSE");
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		arg0.setAttribute("ATTR_RESPONSE", arg1);
		return true;
	}

}
