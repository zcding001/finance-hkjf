package com.hongkun.finance.user.support.security.component.annotation;


import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * @Description :
 * 请求权限验证的注解
 * 加于Controller类或者Controller方法中与
 * @see  OperationTypeEnum 配合使用
 * 通常在权限控制中，不需要加入此注解，因为AuthorityIntercepter默认会拦截所有的请求
 * 此注解一般用于对不需要进行权限控制的url,在不需要接受控制的Controller或者其方法上面添加
 * 表明被注解标注的方法不需要接受权限控制
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.component.annotation.AskForPermission
 * @Author : zhongpingtang@hongkun.com.cn
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AskForPermission {

    /**
     * 可以指定验证的类型，默认是需要验证权限
     * @see OperationTypeEnum
     *
     * @return 指定方法需要验证的类型
     */
    OperationTypeEnum operation() default OperationTypeEnum.DISCRIMINATE_AUTHORITY;
    /*
     *指定系统类型 1-鸿坤金服后台 2-鸿坤金服前台 3-BI后台
     */
    String sysType() default UserConstants.MENU_TYPE_SYS_MANAGEMET;
}
