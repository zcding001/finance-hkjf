package com.hongkun.finance.web.support.menurouters;

import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationReceiver;
import com.hongkun.finance.user.support.security.component.MenuRouter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.AuthUtil.getRegUserService;

/**
 * @Description :
 * 默认的菜单路由器
 * 遵循 用户-角色-菜单 的关系去寻找
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.menurouters.DefaultMenurouter
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class DefaultMenurouter implements MenuRouter {

    /**
     * 默认的菜单加载器,返回菜单id的字符list
     */
    private static final Function<RegUser,List<String>> defaultMenuLoader=
            (user) -> Optional.of(getRegUserService().lookUpUserMenuIds(user.getLogin(), user.getType(),Integer.parseInt(UserConstants.MENU_TYPE_SYS_BI)))
                              .map(e -> e.stream().map(String::valueOf).collect(Collectors.toList()))
                              .orElse(Collections.emptyList());


    @Override
    public void doRoute(OperationReceiver operationReceiver) {
          operationReceiver.setMenuLoader(defaultMenuLoader);
    }
}
