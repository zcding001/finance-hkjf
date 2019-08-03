package com.hongkun.finance.user.support.security.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.yirun.framework.core.exception.InnerErrorException;

/**
 * 加载AOP的调用方法的接口
 * @author 	 zc.ding
 * @since 	 2017年5月21日
 * @version  1.1
 */
public interface ProxyTargetI {

	static final Logger LOG = Logger.getLogger(ProxyTargetI.class);
	
	/**
	 * 解析代理方法的上的注解
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param point
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 */
	default <T extends Annotation> T getAnnotation(JoinPoint point, Class<T> clazz) throws NoSuchMethodException{
		return AnnotationUtils.findAnnotation(getTargetMethod(point), clazz);
	}
	
	/**
	 * 解析代理类上的注解
	 * @author	 zc.ding
	 * @since 	 2017年5月22日
	 * @param point
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 */
	default <T extends Annotation> T getClassAnnotation(JoinPoint point, Class<T> clazz) throws NoSuchMethodException{
		return AnnotationUtils.findAnnotation(point.getTarget().getClass(), clazz);
	}

	/**
	 * 解析代理方法的名称
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param joinPoint
	 * @return
	 * @throws NoSuchMethodException
	 */
	default Method getTargetMethod(JoinPoint joinPoint) throws NoSuchMethodException{
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		return ReflectUtils.findDeclaredMethod(
				joinPoint.getTarget().getClass(), 
				joinPoint.getSignature().getName(),
				methodSignature.getParameterTypes()
				);
	}

	/**
	 * 从参数中获得注解中属性的值
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param key
	 * @param args
	 * @return
	 */
	default String getParamValue(String key, Object[] args) {
		ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("js");
		ScriptContext context = new SimpleScriptContext();
		context.setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
		try {
			String value = String.valueOf(engine.eval(key, context));
			return (value == null || "null".equalsIgnoreCase(value)) ? "" : value;
		} catch (ScriptException e) {
			if(LOG.isDebugEnabled()){
				LOG.debug("fail to eval express", e);
			}
			throw new InnerErrorException("fail to eval express", e);
		}
	}
	
	/**
	 *  @Description    : 执行公式 例如  a == b ? "1" : "0"
	 *  @Method_Name    : getParamValue
	 *  @param formula
	 *  @return         : String
	 *  @Creation Date  : 2017年12月5日 下午3:10:17 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	default String getParamValue(String formula) {
		ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("js");
		try {
			return (String) engine.eval(formula);
		} catch (ScriptException e) {
			if(LOG.isDebugEnabled()){
				LOG.debug("fail to eval express", e);
			}
			throw new InnerErrorException("fail to eval express", e);
		}
	}
}
