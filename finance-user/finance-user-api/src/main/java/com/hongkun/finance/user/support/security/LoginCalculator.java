package com.hongkun.finance.user.support.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description : 是否需要登录的计算器
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.LoginCalculator
 * @Author : zhongpingtang@hongkun.com.cn
 */
public interface LoginCalculator {

    /**
    *  @Description    ：计算操作类型是否需要登录
    *  @Method_Name    ：calculatorIsNeedLogin
    *  @param operationType
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    boolean calculatorIsNeedLogin(OperationTypeEnum operationType);


    /**
    *  @Description    ：判定用户是否登录
    *  @Method_Name    ：userHasLogin
    *  @param request
    *  @param response
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    boolean userHasLogin(HttpServletRequest request, HttpServletResponse response);


  /**
  *  @Description    ：是否能跳过登录验证
  *  @Method_Name    ：canSkipLogin
  *  @param operationType
  *  @return boolean
  *  @Creation Date  ：2018/4/12
  *  @Author         ：zhongpingtang@hongkun.com.cn
  */
    boolean canSkipLogin(OperationTypeEnum operationType);

    /**
    *  @Description    ：是否支持当前的的计算器的类型
    *  @Method_Name    ：isSupportCurrentTypes
    *  @param types
    *  @return boolean
    *  @Creation Date  ：2018/4/12
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    boolean isSupportCurrentTypes(List<OperationTypeEnum> types);


   /**
   *  @Description    ：获取所有支持的类型
   *  @Method_Name    ：getSupportTypes
   *
   *  @return java.util.List<com.hongkun.finance.user.support.security.OperationTypeEnum>
   *  @Creation Date  ：2018/4/12
   *  @Author         ：zhongpingtang@hongkun.com.cn
   */
    List<OperationTypeEnum> getSupportTypes();

    /**
    *  @Description    ：单纯判断用户是否登录
    *  @Method_Name    ：userHasLogin
    *  @param request
    *  @return boolean
    *  @Creation Date  ：2018/4/13
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    boolean userHasLogin(HttpServletRequest request);
}
