package com.hongkun.finance.user.support.security;

import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.utils.PropertiesHolder;

/**
 * @author zhongping
 * @date 2017/5/19
 * <p>
 * 权限验证模块所需要的常量
 */
public interface SecurityConstants {
    String ENV_AUTH_PRE = PropertiesHolder.getProperty("env_auth");
    /*前缀相关*/
    String URL_AUTH_PREFIX = "AUTH_";/*已经入库的权限的url前缀*/
    String USER_AUTH_PREFIX = ENV_AUTH_PRE+"USER_AUTH";/*用户权限在redis中存放名称前缀*/
    String USER_MENU_PREFIX = ENV_AUTH_PRE+"USER_MENU";/*用户菜单在redis中存放名称前缀*/
    String AUTH_URLS =ENV_AUTH_PRE+ "AUTH_URLS";
    String ALL_MENUS= ENV_AUTH_PRE+"ALL_MENUS";/*所有的菜单数据*/
    String ALL_SECOND_MENU_ID_CONSOLE = ENV_AUTH_PRE+"ALL_SECOND_MENU_ID_CONSOLE";/*所有的二级菜单id(后台)*/

    String AUTH_URLS_HKJF =ENV_AUTH_PRE+ "AUTH_URLS_HKJF";/**前台权限集合 */
    String AUTH_URLS_BI =ENV_AUTH_PRE+ "AUTH_URLS_BI";/**前台权限集合 */


    /*提示信息相关*/
    String NO_LOGIN_MESSAGE = "对不起，请您先登录";
    String NO_AUTHORITY_MESSAGE = "对不起，您没有相应的权限";
    String NOT_CONSOLE_USER_MESSAGE = "您不是后台用户";

    /*时间相关*/
    int AUTH_MENU_EXPIRE_TIME = Constants.TICKET_EXPIRES_TIME;/*用户权限和菜单的过期时间*/

}
