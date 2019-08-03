package com.hongkun.finance.web.support.operationreceivers;

import com.hongkun.finance.user.support.security.AbstractRouteAbleAuthReceiver;

import java.util.Arrays;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_CONSOLE;

/**
 * @Description :
 * 普通的后台用户,使用默认的用户-角色-权限授权的用户
 * 支持的用户类型:
 * USER_TYPE_CONSOLE:后台账户
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.operationreceivers.ConsoleRoledUserReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class ConsoleRoledUserReceiver extends AbstractRouteAbleAuthReceiver {


    public ConsoleRoledUserReceiver() {
        super(Arrays.asList(USER_TYPE_CONSOLE));
    }

}
