package com.hongkun.finance.web.support.menurouters;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.OperationReceiver;
import com.hongkun.finance.user.support.security.component.MenuRouter;

import java.util.List;
import java.util.function.Function;

import static com.hongkun.finance.user.support.security.AuthUtil.getRegUserService;

/**
 * @Description : 默认的模板角色策略
 * @Project : finance
 * @Program Name : com.hongkun.finance.web.support.menurouters.TemplateMenurouter
 * @Author : zhongpingtang@hongkun.com.cn
 */
public class TemplateMenurouter implements MenuRouter {

    /**
     * 默认的样板加载
     */
    public static final Function<RegUser, List<String>> defalutTemplateLoader =
            (regUser) -> getRegUserService().findUserMenuIdsWithTemplateBackUp(regUser);


    @Override
    public void doRoute(OperationReceiver operationReceiver) {
          operationReceiver.setMenuLoader(defalutTemplateLoader);
    }
}
