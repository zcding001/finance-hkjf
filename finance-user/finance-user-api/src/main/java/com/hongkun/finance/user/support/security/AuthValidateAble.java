package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.RegUser;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description : 可进行权限验证的接口, 要求实现类使用链式结构
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AuthValidateAble
 * @Author : zhongpingtang@hongkun.com.cn
 */
public interface AuthValidateAble extends Ordered{

    /**
    *  @Description    ：执行验证的条件
    *  @Method_Name    ：canValidateOn
    *  @param user
    *  @param operationType
    *  @param workInConsole
    *  @param request
    *  @param response
    *  @return boolean
    *  @Creation Date  ：2018/4/16
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
   default boolean canValidateOn(RegUser user,
                          OperationTypeEnum operationType,
                          boolean workInConsole,
                          HttpServletRequest request, HttpServletResponse response){
       return true;
   }

   /**
   *  @Description    ：需要被控制的url,如果为null说明是对任何都适用
   *  @Method_Name    ：getValidateUrl
   *  @return java.lang.String
   *  @Creation Date  ：2018/4/16
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
   default String getValidateUrl(){
       return null;
   }


    /**
     *  @Description    ：权限的验证优先级
     *  @Method_Name    ：authOrder 数值越大, 优先级越小
     *  @return int
     *  @Creation Date  ：2018/4/12
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    @Override
    default int getOrder(){return Integer.MIN_VALUE;}

    /**
    *  @Description    ：是否是后置验证器,如果是true的时候,在controller后之后执行
    *  @Method_Name    ：isPostValidate
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    default boolean isPostValidate(){return false;}

   /**
   *  @Description    ：执行用户鉴权
   *  @Method_Name    ：validateUserHasPermission
   *  @param resultResponseEntity 在controller后之后执行后的记过
   *  @param user 权限用户
   *  @param operationType 操作类型
   *  @param workInConsole 是否在后台工作
   *  @param request
   *  @param response
   *  @return boolean  验证是否通过
   *  @Creation Date  ：2018/4/12
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    default boolean validateUserHasPermission(Object resultResponseEntity, RegUser user,
                                      OperationTypeEnum operationType,
                                      boolean workInConsole,
                                      HttpServletRequest request, HttpServletResponse response){
        return false;
    }




}
