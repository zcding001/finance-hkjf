package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.*;
import com.hongkun.finance.user.dto.MenuDTO;
import com.hongkun.finance.user.dto.MenuPriDTO;
import com.hongkun.finance.user.dto.RoleMenuDTO;
import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.model.SysMenuPriRel;
import com.hongkun.finance.user.model.SysPrivilege;
import com.hongkun.finance.user.query.MenuQuery;
import com.hongkun.finance.user.service.MenuService;
import com.hongkun.finance.user.support.AuthSecurityManager;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static org.apache.commons.lang.math.NumberUtils.INTEGER_ZERO;

/**
 * @Description : 权限服务实现类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.AuthServiceImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysMenuPriRelDao menuPriRelDao;

    @Autowired
    private SysPrivilegeDao privilegeDao;

    @Autowired
    private SysRoleMenuRelDao roleMenuRelDao;

    @Autowired
    private RegUserDao regUserDao;

    @Override
    public Pager listAllMenus(MenuQuery menuQuery, Pager pager) {
        menuQuery.setSortColumns("modify_time desc");
        Pager resultPage = sysMenuDao.findByCondition(menuQuery, pager,SysMenu.class,".findPage");
        return  Optional.ofNullable(resultPage.getData())
                .map((e) -> {
                    //组装DTO
                    resultPage.setData(e.stream().map(k -> new MenuDTO((SysMenu) k)).collect(Collectors.toList()));
                    return resultPage;
                }).orElse(resultPage);

    }

    @Override
    public ResponseEntity saveOrUpdateMenu(SysMenu sysMenu, boolean saveFlag) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: saveOrUpdateMenu, 保存或者更新菜单, sysMenu: {}, saveFlag: {}", sysMenu,saveFlag);
        }
        try {
            return  (saveFlag ?
                     sysMenuDao.save(sysMenu) > INTEGER_ZERO : sysMenuDao.update(sysMenu) > INTEGER_ZERO) ?
                     ResponseEntity.SUCCESS : ResponseEntity.ERROR;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("保存或者更新菜单失败, sysMenu: {}, saveFlag: {}\n异常信息: ", sysMenu,saveFlag, e);
            }
            throw new GeneralException("保存或者更新菜单失败,请重试");
        }
    }

    @Override
    public ResponseEntity delteMenu(SysMenu sysMenu) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: delteMenu, 删除菜单, sysMenu: {}", sysMenu);
        }
        try {
            //如果是二级菜单，检查此菜单是否与权限关联，如果关联，从权限中解除菜单关系
            Integer thisMenuId = sysMenu.getId();
            SysMenu unDelteMenu = findByPK(thisMenuId);
            if(allowDeleteMenu(unDelteMenu)){
                /**
                 * step 1:解除此菜单关联的权限
                 */
                menuPriRelDao.clearMenuPrisRelByMenuIdOrPriId(new MenuPriDTO(Arrays.asList(sysMenu.getId()), null));
                /**
                 * step 2:解除与此菜单关联的角色关系
                 */
                roleMenuRelDao.clearRoleMenuRelByroleIdOrMenuId(new RoleMenuDTO(null, Arrays.asList(sysMenu.getId())));
                /**
                 * step 2:删除菜单
                 */
                sysMenu.setState(INTEGER_ZERO);
                return saveOrUpdateMenu(sysMenu, false);
            }
            return new ResponseEntity(ERROR, "菜单下有子菜单，不允许删除");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("删除菜单失败, sysMenu: {}\n异常信息: ", sysMenu, e);
            }
            throw new GeneralException("删除菜单失败,请重试");
        }
    }


    @Override
    public SysMenu findByPK(Integer id) {
        return this.sysMenuDao.findByPK(Long.valueOf(id), SysMenu.class);
    }

    @Override
    public Integer getTotalCount(SysMenu sysMenu) {
        return sysMenuDao.getTotalCount(sysMenu);
    }

    @Override
    public List<SysMenu> listAllSecondMenusNoPager() {
        return sysMenuDao.findByCondition(new SysMenu()).stream().filter(e->e.getPid()!=0).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity bindPrisToMenu(Integer menuId, Integer currentUserId,Set<Integer> pris) {
        if (logger.isInfoEnabled()) {
            logger.info("方法名: bindPrisToMenu, 绑定权限到菜单, menuId: {}, 权限ID: {}", menuId,pris);
        }
        try {
            //检查用户是否越权使用
            ResponseEntity validateResult = AuthSecurityManager.checkUserMenusAndAuthsWithPris(regUserDao, currentUserId, menuId, pris);
            if (!validateResult.validSuc()) {
                return validateResult;
            }
            //执行鉴权
            return ((BaseUtil.collectionIsEmpty(pris) ?
                    menuPriRelDao.clearMenuPrisRelByMenuIdOrPriId(new MenuPriDTO(new ArrayList(menuId),null)) : dobindPrisToMenu(menuId, pris))> INTEGER_ZERO) ?
                    ResponseEntity.SUCCESS : ResponseEntity.ERROR;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("绑定权限到菜单, menuId: {}\n异常信息: ", menuId, e);
            }
            throw new GeneralException("绑定权限到菜单,请重试");
        }
    }



    /**
     * 列出菜单已经绑定的权限
     * @param menuId
     * @return
     */
    @Override
    public List<SysPrivilege> findPrivigesBindToMenu(Integer menuId) {
        return menuPriRelDao.findByCondition(new SysMenuPriRel(menuId, null))
                .stream().map(e -> e.getSysPriId())
                .map(id -> privilegeDao.findByPK(Long.valueOf(id), SysPrivilege.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> listAllMenusNoPager() {
       return  sysMenuDao.findByCondition(new SysMenu()).stream().collect(Collectors.toList());
    }

    /**
    *  @Description    ：是否允许删除菜单
    *  @Method_Name    ：allowDeleteMenu
    *  @param sysMenu
    *  @return boolean
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private boolean allowDeleteMenu(SysMenu sysMenu) {
        Integer pid = sysMenu.getPid();
        if (INTEGER_ZERO.equals(pid)) {
            //如果是一级菜单，判断其下面有没有子菜单
            return INTEGER_ZERO.equals(getTotalCount(new SysMenu(null,sysMenu.getId())));
        }
        return true;
    }


    /**
    *  @Description    ：绑定菜单到权限
    *  @Method_Name    ：dobindPrisToMenu
    *  @param menuId
    *  @param newPris
    *  @return java.lang.Integer
    *  @Creation Date  ：2018/4/24
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    private Integer dobindPrisToMenu(Integer menuId, Set<Integer> newPris) {
        /**
         * step 1:查询菜单原来的权限集合
         */
        SysMenuPriRel menuPriRel = new SysMenuPriRel();
        menuPriRel.setSysMenuId(menuId);
        return  RelFunction.
                reBindRel(
                        menuPriRelDao.findByCondition(menuPriRel).stream().map(e->e.getSysPriId()).collect(Collectors.toList()),
                        newPris,
                        unDeleteIds->menuPriRelDao.clearMenuPrisRelByMenuIdOrPriId(new MenuPriDTO(null,unDeleteIds)),
                        unSaveId->menuPriRelDao.save(new SysMenuPriRel(menuId,unSaveId))
                );
    }


}
