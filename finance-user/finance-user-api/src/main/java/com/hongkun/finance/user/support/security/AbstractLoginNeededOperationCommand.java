package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.utils.BaseUtil;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description : 带有登录性质的操作命令
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AbstractLoginNeededOperationCommand
 * @Author : zhongpingtang@hongkun.com.cn
 */
public abstract class AbstractLoginNeededOperationCommand extends AbstractOperationCommand {

    /**
     * 是否需要登录的校验器
     */
    private LoginCalculator loginCalculator;

    public AbstractLoginNeededOperationCommand(List<OperationTypeEnum> typeEnums,LoginCalculator loginCalculator) {
        super(typeEnums);
        //初始化是否需要登录校验器
        Assert.notNull(loginCalculator,"请指定登录校验器!");
        this.loginCalculator = loginCalculator;

        //判断当前的登录计算器是否支持判断
        if (!loginCalculator.isSupportCurrentTypes(typeEnums)) {
            throw new SecurityException("当前登录校验类型集合: " + loginCalculator.getSupportTypes() + " 无法校验集合: " + getSuportOpertionTypes());
        }
    }

    @Override
    public boolean doOpertion(Object resultResponseEntity, OperationTypeEnum operationType, HttpServletRequest request, HttpServletResponse response, boolean workInConsole) {
        //可以跳过登录的操作
        boolean canSkipLogin = loginCalculator.canSkipLogin(operationType);
        if (!AuthUtil.getCallPostExecutorFlag(request)) {
            //处于前置验证中
            if (canSkipLogin) {
                    if (!loginCalculator.userHasLogin(request)) {
                    //处于前置验证中,请求能够跳过登录的操作并且用户没有登录,直接返回true
                    return true;
                }
            }else{
                //处于前置验证中,需要验证登录,但是验证为没有登录,截断请求
                if (!loginCalculator.userHasLogin(request,response)){
                    return false;
                }
            }

            //执行到此处说明已经登录,进入验证链
        }
        /*
        进入下一步处理逻辑,包含的情况:
        1.正常需要登录,并且已经验证通过的逻辑
        2.不要登录,但是此时用户已经登录,可能需要做后置数据验证,所以也进入登录之后的逻辑
        3.处于后置验证中
        */

        //验证当前用户
        RegUser currentUser = BaseUtil.getLoginUser();
        if (currentUser==null) {
            if (AuthUtil.getCallPostExecutorFlag(request)) {
                  /*
            猜测情况：
            1.处于后置验证中,并且Controller方法已经做了清除登录用户的操作(比如：logout方法),返回true,截断请求,即不进行后置验证
            此时不能继续进行,直接返回true
             */
                return true;
            }
            //未知情况截断请求(假如存在的话)
            return false;

        }
        return afterLoginIntecepter(resultResponseEntity,currentUser, operationType, request, response, workInConsole);

    }

    /**
    *  @Description    ：登录拦截之后的操作
    *  @Method_Name    ：afterLoginIntecepter
    *  @param resultResponseEntity
    *  @param operationType
    *  @param request
    *  @param response
    *  @param workInConsole
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    public abstract boolean afterLoginIntecepter(Object resultResponseEntity, RegUser regUser,OperationTypeEnum operationType, HttpServletRequest request, HttpServletResponse response, boolean workInConsole);




}
