package com.hongkun.finance.user.support.security.component;

import com.hongkun.finance.user.support.security.AuthUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.user.support.security.AuthUtil.getOperationType;

/**
 * @Description : 数据返回验证后置调用处理器
 * AuthUtil.getCallPostExecutorFlag(currentRequest)获取从preHandler中接收信号, 如果获取了信号, 调用后置处理链
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.AuthCallBackHandler
 * @Author : zhongpingtang@hongkun.com.cn
 */
@ControllerAdvice
public class AuthCallBackHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
    *  @Description    ：复写返回ResponseEntity 方法
    *  @Method_Name    ：beforeBodyWrite
    *  @param resultResponseEntity controller 调用之后的结果
    *  @param methodParameter 方法参数
    *  @param mediaType
    *  @param aClass
    *  @param serverHttpRequest
    *  @param serverHttpResponse
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    @Override
    public Object beforeBodyWrite(Object resultResponseEntity, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //获取request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //分别获取当前的request和response
        HttpServletRequest currentRequest = requestAttributes.getRequest();
        HttpServletResponse currentResponse = requestAttributes.getResponse();
        //尝试获取调用拦截器信号
        if (AuthUtil.getCallPostExecutorFlag(currentRequest)) {
            //得到信号,再次进行调用
            AuthUtil.callExecutor(resultResponseEntity, currentRequest, currentResponse, getOperationType(currentRequest), true);
        }
        return resultResponseEntity;
    }

}
