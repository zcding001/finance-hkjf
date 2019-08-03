package com.hongkun.finance.web.support;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.dto.PrivilgeDTO;
import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.service.MenuService;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.AuthUtil;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.SecurityConstants.*;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ZERO;

/**
 * @Description : 权限url收集器，同时，引入权限拦截器所需Service
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.support.AuthUrlCollector
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@RequestMapping("/authController")
@AskForPermission
@Controller
public class AuthUrlCollector {

    /**
     * 权限收集器
     */
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Reference
    private PrivilegeSrvice privilegeSrvice;

    @Reference
    private MenuService menuService;

    @Reference
    private RegUserService regUserService;

    /**
     * 存储所有的urls
     */
    private Set<String> urls = new HashSet();

    @RequestMapping("/refreshAuthAndMenu")
    @ResponseBody
    public ResponseEntity refreshAuthAndMenu(){
        //AuthUtil.deleteAuthListTotallyThenSet(AUTH_URLS,null);
        AuthUtil.deleteAuthListTotallyThenSet(ALL_MENUS,null);
        AuthUtil.deleteAuthListTotallyThenSet(ALL_SECOND_MENU_ID_CONSOLE,null);

        /**
         * 删除所有人的菜单
         */
        JedisClusterUtils.deleteKeysByPrefix(USER_MENU_PREFIX);
        return ResponseEntity.SUCCESS;
    }


    /**
     * 列出所有的权限url
     *
     * @return
     */
    @PostConstruct
    public void listMappedUrls() {
        final Integer[] count = {INTEGER_ONE};
        Set<String> urls = handlerMapping.getHandlerMethods().keySet().stream()
                .map(e -> e.getPatternsCondition().getPatterns())
                .collect(Collectors.reducing((pre, next) -> {
                    if (count[0].equals(INTEGER_ONE)) {
                        this.urls.addAll(pre);
                        count[0]++;
                    }
                    this.urls.addAll(next);
                    return this.urls;
                })).get();

        AuthUtil.deleteAuthListTotallyThenSet(AUTH_URLS_HKJF,new ArrayList<>(urls));
    }

    /**
     * 所有已经入库的url,每条权限的构成为：URL_AUTH_PREFIX+e.getPrivUrl()，值为：authId
     *
     * @return
     */
    @PostConstruct
    public void listSavedAuthUrl() {
        List<PrivilgeDTO> privilgeDTOS = privilegeSrvice.listPrivilegesNoPager();
        privilgeDTOS.stream().forEach((e)->{
            //存入redis
            JedisClusterUtils.set(URL_AUTH_PREFIX +e.getPrivUrl(), String.valueOf(e.getId()));
        });

    }


    /**
     * 所有已经入库的菜单
     *
     * @return
     */
    @PostConstruct
    public void listAllMenus() {
        //删除原有的id
        AuthUtil.deleteAuthListTotallyThenSet(ALL_SECOND_MENU_ID_CONSOLE,null);


        List<SysMenu> sysMenus = menuService.listAllMenusNoPager();
        //存储所有的菜单数据
        List<String> allMenuJsonData = sysMenus.stream().map(e -> JsonUtils.toJson(e)).collect(Collectors.toList());
        AuthUtil.deleteAuthListTotallyThenSet(ALL_MENUS,allMenuJsonData);
        //存储所有的后台二级菜单id
        Set<Integer> secondConsoleMenuIds = sysMenus.stream().filter(k->1==k.getType()/*只要后台管理的菜单*/).map(e -> INTEGER_ZERO.equals(e.getPid()) ? 0 : e.getId()).collect(Collectors.toSet());
        secondConsoleMenuIds.remove(INTEGER_ZERO);
        AuthUtil.deleteAuthListTotallyThenSet(ALL_SECOND_MENU_ID_CONSOLE,secondConsoleMenuIds.stream().map(e -> String.valueOf(e)).collect(Collectors.toList()));
    }
}
