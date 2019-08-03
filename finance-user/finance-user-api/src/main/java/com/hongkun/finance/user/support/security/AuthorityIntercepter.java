package com.hongkun.finance.user.support.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.service.MenuService;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.service.RegUserService;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description :
 * <p>
 * 此拦截器用于需要进行权限验证的web工程
 * 使用命令模式设计
 * 在命令模式中承担的角色：Invoker
 * 需要在springmvc容器中做如下配置：
 * <pre>
 *  <auth:enableControl url="****" defalutLevel="DISCRIMINATE_LOGIN" workInConsole="false"/>
 * </pre>
 * @AskForPermission(check = PermissionTypeEnum.NO_LOGIN)来使请求不接受登录和权限检查
 * <p>
 * 特别注意：defalutLevel属性是指明默认拦截级别，其值如果不指定则为需要进行权限验证，如果指定请参考PermissionTypeEnum枚举类型值
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AuthorityIntercepter
 * @Author : zhongpingtang@hongkun.com.cn
 * @see com.hongkun.finance.user.support.security.component.schema.AuthEditorParser 类对该标签进行解析
 * <p>
 * 需要注意的是，必须同时配置：  <mvc:annotation-driven/>，该标签的作用是把映射粒度精确到方法级别.
 * <p>
 * 所有mapping所匹配的path均会经过AuthorityIntercepter，
 * 通常，需要权限验证的方法不需要做任何的配置,因为该拦截器默认拦截所有符合path的请求.
 * 而通常对不需要权限验证的方法进行标注,可以使用：
 * @see com.hongkun.finance.user.support.security.component.annotation.AskForPermission
 */
public class AuthorityIntercepter extends AbstractAuthorityValidator {

    public AuthorityIntercepter() {/*empty*/}

    public AuthorityIntercepter(OperationTypeEnum defalutLevel, boolean workInConsole) {
        super(defalutLevel, workInConsole);
    }

    /**
     * 注入service通过@Reference将对象放到Spring容器中，以便通过ApplicationUtil从Spring容器中可以取到
     */
    @Reference
    private MenuService menuService;

    @Reference
    private RegUserService regUserService;

    @Reference
    private PrivilegeSrvice privilegeSrvice;

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  目标处理方法
     * @return boolean 验证是否通过
     * @Description ：复写父类的拦截器方法，该方法是一个模板方法，提供通用的检查逻辑
     * @Method_Name ：preHandle
     * @Creation Date  ：2018/4/12
     * @Author ：zhongpingtang@hongkun.com.cn
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            //调用执行器
            return AuthUtil.callExecutor(null, request, response, searchForAskForPermission((HandlerMethod) handler,request), workInConsole);
        }
        return true;
    }


}
