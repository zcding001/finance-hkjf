package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.model.SysPrivilege;
import com.hongkun.finance.user.query.MenuQuery;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Set;

/**
 * @Description :权限服务
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.AuthService
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface MenuService {


    /**
     *  @Description    : 分页查询所有的Menus
     *  @Method_Name    : listAllMenus
     *  @param menuQuery  :菜单查询条件
     *  @param pager        :分页信息
     *  @return           ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager listAllMenus(MenuQuery menuQuery, Pager pager);


    /**
     *  @Description    : 保存或者更新菜单
     *  @Method_Name    : saveOrUpdateMenu
     *  @param sysMenu  :菜单信息
     *  @param saveFlag  :保存状态状态符
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity saveOrUpdateMenu(SysMenu sysMenu, boolean saveFlag);

    /**
     *  @Description    : 删除菜单
     *  @Method_Name    : delteMenu
     *  @param sysMenu  :菜单信息
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity delteMenu(SysMenu sysMenu);

    SysMenu findByPK(Integer id);

    Integer getTotalCount(SysMenu sysMenu);

    /**
     *  @Description    : 列出所有的二级菜单-不带分页
     *  @Method_Name    : listAllSecondMenusNoPager
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<SysMenu> listAllSecondMenusNoPager();

    /**
     *  @Description    : 绑定权限到菜单
     *  @Method_Name    : listAllSecondMenusNoPager
     *  @param menuId   : 菜单id
     *  @param pris    : 续保被绑定到菜单上面的权限
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity bindPrisToMenu(Integer menuId,Integer currentUserId, Set<Integer> pris);

    /**
     *  @Description    : 查询绑定到菜单上面的权限
     *  @Method_Name    : findPrivigesBindToMenu
     *  @param menuId   : 菜单id
     *  @return          : List<SysPrivilege>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<SysPrivilege> findPrivigesBindToMenu(Integer menuId);


    /**
     *  @Description    : 列出所有的菜单，包括一级菜单
     *  @Method_Name    : listAllMenusNoPager
     *  @return          : List<SysMenu>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<SysMenu> listAllMenusNoPager();

}
