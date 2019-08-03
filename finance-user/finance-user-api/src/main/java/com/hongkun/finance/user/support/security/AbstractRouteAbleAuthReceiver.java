package com.hongkun.finance.user.support.security;

import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.support.security.component.MenuRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 可实现菜单策略的接受者,负责加载所有已经注册过的菜单策略
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.support.security.AbstractRouteAbleAuthReceiver
 * @Author : zhongpingtang@hongkun.com.cn
 */
public abstract class AbstractRouteAbleAuthReceiver extends AbstractPreValiableReceiver {


    /**
     * menuRouter数据源
     */
    private List<MenuRouter> menuRouters = new ArrayList(16);

    /**
     * 添加路由策略
     * @param menuRouter
     */
    protected void addRouter(MenuRouter menuRouter){
        this.menuRouters.add(menuRouter);
    }

    public AbstractRouteAbleAuthReceiver(List<Integer> userTypes) {
        super(userTypes);
    }
    /**
     * 初始化菜单策略
     *
     * @param menuRouters
     */
    @Autowired(required = false)
    protected void initMenuRouters(List<MenuRouter> menuRouters) {
        if (!CollectionUtils.isEmpty(menuRouters)) {
            this.menuRouters.addAll(menuRouters);
            //菜单策略排序
            AnnotationAwareOrderComparator.sort(this.menuRouters);
        }
    }

    @Override
    public void decideMenuLoaderForReceiver(RegUser loginUser) {
        this.menuRouters.stream().filter(router -> router.useCurrentRouter(loginUser))
                        //找到第一优先级的路由器, 因为有默认的路由器, 一定会存在
                        .findFirst().get().doRoute(this);
    }



}
