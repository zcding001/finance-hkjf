package com.hongkun.finance.web.controller;

import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.user.utils.BaseUtil.commExceptionMsg;

/**
 * @Description : 错误处理器
 * @Project : finance
 * @Program Name : com.hongkun.finance.api.controller.ExceptionHandler
 * @Author : zhongpingtang@hongkun.com.cn
 */
@ControllerAdvice
public class ExceptionResponse {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResponse.class);

    /**
     *  @Description    ：处理通用异常
     *  @Method_Name    ：validExceptionHandler
     *  @param exception
     *  @param request
     *  @param response
     *  @return java.lang.String
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity handlerGeneralException(RuntimeException exception, WebRequest request, HttpServletResponse response) {
        //解析错误信息
        String returnMsg = BaseUtil.resolveGeneralExceptionFromRemoteRuntimeExcetion(exception.getMessage());
        if (returnMsg==null) {
            returnMsg = commExceptionMsg;
            //controller中的异常抛出
            logger.error("{}, 异常信息: {}",request.toString(),exception);
        }
        return ResponseEntity.error(returnMsg);
    }



}
