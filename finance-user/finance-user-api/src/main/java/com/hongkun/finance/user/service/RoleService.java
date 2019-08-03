package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.SysMenu;
import com.hongkun.finance.user.model.SysRole;
import com.hongkun.finance.user.model.vo.UserRolesVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Set;

/**
 * @Description :角色服务
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.service.AuthService
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public interface RoleService {


    /**
     *  @Description    : 分页查询所有的Roles
     *  @Method_Name    : listAllRoles
     * @param sysRole   :角色信息
     * @param pager     :分页信息
     *  @return          :Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager listAllRoles(SysRole sysRole, Pager pager);


    /**
     *  @Description    : 更新或者删除角色
     *  @Method_Name    : saveOrUpdateRole
     * @param sysRole   :角色信息
     * @param saveFlag  :是否保存
     *  @return          :ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity saveOrUpdateRole(SysRole sysRole, boolean saveFlag);


    /**
     *  @Description    : 根据角色信息条件查询角色信息
     *  @Method_Name    : findAllRoles
     * @param sysRole   :角色信息
     * @param pager  :分页信息
     *  @return          :Pager
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Pager findAllRoles(SysRole sysRole, Pager pager);

    /**
     *  @Description    : 查询绑定到用户的角色数量
     *  @Method_Name    : roleUnioToUserCount
     * @param id   :用户id
     *  @return          :Integer
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    Integer roleUnioToUserCount(Integer id);


    /**
     *  @Description    : 查询角色绑定的菜单
     *  @Method_Name    : findRolesMenu
     * @param sysRole   :用户角色
     *  @return          : List<SysMenu>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<SysMenu> findRolesMenu(SysRole sysRole);

    /**
     *  @Description    : 删除一个角色
     *  @Method_Name    : deleteRole
     * @param sysRole   : 用户角色
     *  @return          : List<SysMenu>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity deleteRole(SysRole sysRole);


    /**
     *  @Description    : 绑定菜单到角色中
     *  @Method_Name    : bindMenuToRole
     * @param menus   : 菜单id集合
     * @param roleId   : 角色id
     *  @return          : ResponseEntity
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    ResponseEntity bindMenuToRole(Set<Integer> menus, Integer roleId,Integer userId);

    /**
     *  @Description    : 查询所有的角色，不带分页
     *  @Method_Name    : findAllRolesNoPager
     *  @return          : List<SysRole>
     *  @Creation Date  : 2018年4月8日10:26:34
     *  @Author         : zhongpingtang@hongkun.com.cn
     */
    List<SysRole> findAllRolesNoPager();
    /**
    *  @Description    ：根据用户手机号和角色id查询用户角色信息
    *  @Method_Name    ：findUserRolesVoList
    *  @param userRolesVo
    *  @param pager
    *  @return com.yirun.framework.core.utils.pager.Pager
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    Pager findUserRolesVoList(UserRolesVo userRolesVo,Pager pager);

    /**
    *  @Description    ：为用户添加角色关系
    *  @Method_Name    ：saveUserRoles
    *  @param login
    *  @param sysRoleId
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity saveUserRoles(String login, Integer sysRoleId);
    /**
    *  @Description    ：删除用户角色关系
    *  @Method_Name    ：delUserRoles
    *  @param id
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity delUserRoles(Integer id);
    /**
    *  @Description    ：查询前台菜单
    *  @Method_Name    ：myAccountRoles
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    ResponseEntity myAccountRoles();
}
