package com.hongkun.finance.web.support.operationreceivers;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.AbstractValiableReceiver;
import com.hongkun.finance.user.support.security.OperationTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.constants.UserConstants.MENU_TYPE_HKJF_CONSOLE;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ROOT;
import static com.hongkun.finance.user.support.security.AuthUtil.deleteAuthListTotallyThenSet;
import static com.hongkun.finance.user.support.security.AuthUtil.getMenuDataSource;
import static com.hongkun.finance.user.support.security.SecurityConstants.ALL_SECOND_MENU_ID_CONSOLE;
import static com.hongkun.finance.user.utils.BaseUtil.tryLoadDataFromRedis;


/**
 * @Description :
 * 后台root用户
 * USER_TYPE_ROOT:后台root用户
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.operationreceivers.ConsoleRootReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class ConsoleRootReceiver extends AbstractValiableReceiver {

    public ConsoleRootReceiver() {
        super(Arrays.asList(USER_TYPE_ROOT));
    }

    /**
     * 后台超级用户默认加载全部菜单
     * @param loginUser
     */
    @Override
    public void decideMenuLoaderForReceiver(RegUser loginUser) {
        setMenuLoader(user->(List<String>) tryLoadDataFromRedis(
                ALL_SECOND_MENU_ID_CONSOLE,
                true,
                () -> getMenuDataSource().values().stream()
                                         .filter(e -> e.getPid() != 0 && MENU_TYPE_HKJF_CONSOLE.equals(e.getType()))
                                         .map(k -> String.valueOf(k.getId()))
                                         .collect(Collectors.toList()),
                (allScendMenuInConsole) ->
                        deleteAuthListTotallyThenSet(ALL_SECOND_MENU_ID_CONSOLE,
                                ((List<String>) allScendMenuInConsole).stream().map(String::valueOf).collect(Collectors.toList()))
        ));
    }

    @Override
    public void cacheUserPrivileges(RegUser currentLoginUser, String authStr) {
        //不需要缓存超级管理员的权限
        return;
    }


    @Override
    public boolean validateUserHasPermission(Object resultResponseEntity, RegUser user, OperationTypeEnum operationType, boolean workInConsole, HttpServletRequest request, HttpServletResponse response) {
        /*
         防止后台root用户处理非后台请求
         * 假如其他地方没有做验证,这是最后一道验证
         */
        return workInConsole;
    }
}
