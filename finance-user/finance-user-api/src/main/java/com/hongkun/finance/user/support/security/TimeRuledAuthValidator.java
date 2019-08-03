package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.RegUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description : 抽象的权限验证器, 默认实现
 * validateUserHasPermission方法实现核心验证逻辑
 * 分为controller方法执行前和执行后的逻辑,使得验证具有
 * 扩展性(模板用户无法精确控制权限)
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.TimeRuledAuthValidator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public interface TimeRuledAuthValidator extends AuthValidateAble {

    /**
    *  @Description    ：在操作方法前执行鉴权
    *  @Method_Name    ：doPreAuthValidate
    *  @param user
    *  @param operationType
    *  @param workInConsole
    *  @param request
    *  @param response
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    default   boolean doPreAuthValidate(RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response){
        return false;
    }


    /**
    *  @Description    ：在操作方法后执行鉴权,一般用于数据鉴权
    *  @Method_Name    ：doPostAuthValidate
    *  @param resultResponseEntity
    *  @param user
    *  @param operationType
    *  @param workInConsole
    *  @param request
    *  @param response
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    default  boolean  doPostAuthValidate(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response){
        return false;
    }


}
