package com.hongkun.finance.web.controller.privilege;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.query.MenuQuery;
import com.hongkun.finance.user.service.MenuService;
import com.hongkun.finance.user.service.PrivilegeSrvice;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hongkun.finance.user.support.security.AuthUtil.getMenuDataSource;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 维护菜单元数据的controller
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.MenuController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("/menuController")
public class MenuController {
    private static final Logger LOGGER = Logger.getLogger(MenuController.class);

    @Reference
    private MenuService menuService;

    @Reference
    private PrivilegeSrvice privilegeSrvice;



    /**
     * 列出所有的菜单
     * @return
     */
    @RequestMapping("/listAllMenus")
    @ResponseBody
    public ResponseEntity listAllMenus(MenuQuery menuQuery, Pager pager){
        return new ResponseEntity(SUCCESS, menuService.listAllMenus(menuQuery, pager));
    }

    /**
     * 列出某个菜单的子菜单
     * @return
     */
    @RequestMapping("/listChildMenus")
    @ResponseBody
    public ResponseEntity listChildMenus(@RequestParam(value = "forChildMenusSearchId",required = false) Integer menuId){
        Pager resutPage=new Pager();
        if (menuId==null) {
            return new ResponseEntity(SUCCESS,resutPage);
        }
        //从缓存中拿到菜单
        Map<Integer, SysMenu> menuDataSource;
        menuDataSource = getMenuDataSource();
        List<SysMenu> collect = menuDataSource.values().stream().filter(e -> menuId.equals(e.getPid())).collect(Collectors.toList());

        if (!BaseUtil.collectionIsEmpty(collect)) {
            resutPage = new Pager(collect.size(), 100);
        }
        resutPage.setData(collect);
        return new ResponseEntity(SUCCESS,resutPage);
    }

    /**
     * 列出所有的菜单(不带分页)
     * @return
     */
    @RequestMapping("/listAllMenusNoPager")
    @ResponseBody
    public ResponseEntity listAllSecondMenusNoPager(){
        return new ResponseEntity(SUCCESS, menuService.listAllSecondMenusNoPager());
    }


    /**
     * 新增菜单
     * @param sysMenu
     * @return
     */
    @RequestMapping("/addMenu")
    @ResponseBody
    @ActionLog(msg = "添加系统菜单, 菜单名称: {args[0].menuName}")
    public ResponseEntity addMenu(@Validated(SAVE.class) SysMenu sysMenu){
        //保证url的正确性
        sysMenu.setMenuUrl(StringUtils.trim(sysMenu.getMenuUrl()));
        return menuService.saveOrUpdateMenu(sysMenu,true);
    }

    /**
     * 删除菜单
     * @param sysMenu
     * @return
     */
    @RequestMapping("/delteMenu")
    @ResponseBody
    @ActionLog(msg = "删除系统菜单, 菜单id: {args[0].id}")
    public ResponseEntity delteMenu(@Validated(DELETE.class) SysMenu sysMenu){
        return menuService.delteMenu(sysMenu);
    }


    /**
     * 更新菜单信息
     * @param unUpdateMenu
     * @return
     */
    @RequestMapping("/updateMenu")
    @ResponseBody
    @ActionLog(msg = "更新系统菜单, 菜单id: {args[0].id} ,修改后的菜单名称: {args[0].menuName}")
    public ResponseEntity updateMenu(@Validated(UPDATE.class)SysMenu unUpdateMenu){
        //保证url的正确性
        unUpdateMenu.setMenuUrl(StringUtils.trim(unUpdateMenu.getMenuUrl()));
        return menuService.saveOrUpdateMenu(unUpdateMenu,false);
    }


    /**
     * 绑定权限到菜单
     * @return
     */
    @RequestMapping("/bindPrisToMenu")
    @ResponseBody
    @ActionLog(msg = "绑定权限到菜单, menuId: {args[0]}, pris:{args[1]}")
    public ResponseEntity bindPrisToMenu(@RequestParam("menuId") Integer menuId,
                                         @RequestParam("pris")Set<Integer> pris){
         return this.menuService.bindPrisToMenu(menuId,BaseUtil.getLoginUserId(),pris);
    }

    /**
     * 列出菜单已经绑定的权限
     * @return
     */
    @RequestMapping("/findPrivigeIdBindToMenu")
    @ResponseBody
    public ResponseEntity findPrivigeIdBindToMenu(@RequestParam("menuId") Integer menuId){
        return new ResponseEntity(SUCCESS, this.menuService.findPrivigesBindToMenu(menuId));
    }





}
