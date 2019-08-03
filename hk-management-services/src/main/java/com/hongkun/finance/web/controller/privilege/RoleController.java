package com.hongkun.finance.web.controller.privilege;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.dto.UserRoleDTO;
import com.hongkun.finance.user.model.SysRole;
import com.hongkun.finance.user.model.SysUserRoleRel;
import com.hongkun.finance.user.model.vo.UserRolesVo;
import com.hongkun.finance.user.service.RoleService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.support.validate.DELETE;
import com.yirun.framework.core.support.validate.SAVE;
import com.yirun.framework.core.support.validate.UPDATE;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 角色元数据以及相关操作控制类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.web.controller.RoleController
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Controller
@RequestMapping("/roleController")
public class RoleController {
    private static final Logger LOGGER = Logger.getLogger(RoleController.class);

    @Reference
    private RoleService roleService;


    /**
     * 查找所有的角色信息
     *
     * @param sysRole
     * @return
     */
    @RequestMapping("/findAllRoles")
    @ResponseBody
    public ResponseEntity findAllRoles(SysRole sysRole, Pager pager) {
        return new ResponseEntity(SUCCESS, roleService.findAllRoles(sysRole, pager));
    }

    /**
     * 查找所有的角色信息,不带分页
     *
     * @return
     */
    @RequestMapping("/findAllRolesNoPager")
    @ResponseBody
    public ResponseEntity findAllRolesNoPager() {
        return new ResponseEntity(SUCCESS, roleService.findAllRolesNoPager());
    }


    /**
     * 查找绑定到角色的菜单
     *
     * @param sysRole
     * @return
     */
    @RequestMapping("/findMenuBindToRole")
    @ResponseBody
    public ResponseEntity findMenuBindToRole(SysRole sysRole) {
        return new ResponseEntity(SUCCESS, roleService.findRolesMenu(sysRole));
    }

    /**
     * 添加角色
     *
     * @param sysRole
     * @return
     */
    @RequestMapping("/saveRole")
    @ResponseBody
    @ActionLog(msg = "保存角色, 角色名称: {args[0].roleName}")
    public ResponseEntity saveRole(@Validated(SAVE.class) SysRole sysRole) {
        return roleService.saveOrUpdateRole(sysRole, true);
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    @RequestMapping("/updateRole")
    @ResponseBody
    @ActionLog(msg = "修改角色, 角色id: {args[0].id}, 修改为角色名称: {args[0].roleName}")
    public ResponseEntity updateRole(@Validated(UPDATE.class) SysRole sysRole) {
        return roleService.saveOrUpdateRole(sysRole, false);
    }

    /**
     * delete角色
     *
     * @param sysRole
     * @return
     */
    @RequestMapping("/deleteRole")
    @ResponseBody
    @ActionLog(msg = "删除角色, 角色id: {args[0].id}")
    public ResponseEntity deleteRole(@Validated(DELETE.class) SysRole sysRole) {
        return roleService.deleteRole(sysRole);
    }

    /**
     * 为角色绑定菜单（绑定权限到角色中）
     *
     * @param menus
     * @return
     */
    @RequestMapping("/bindMenuToRole")
    @ResponseBody
    @ActionLog(msg = "为角色绑定菜单, 角色id: {args[1]}, 菜单集合: {args[0]}")
    public ResponseEntity bindMenuToRole(@RequestParam("menus") Set<Integer> menus, @RequestParam("roleId") Integer roleId) {
        return roleService.bindMenuToRole(menus, roleId, BaseUtil.getLoginUserId());
    }
    /**
    *  @Description    ：查看用户角色列表（前台用户）
    *  @Method_Name    ：userRolesList
    *  @param userRolesVo
    *  @param pager
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/userRolesList")
    @ResponseBody
    public ResponseEntity userRolesList(UserRolesVo userRolesVo,Pager pager){
        return new ResponseEntity(SUCCESS, roleService.findUserRolesVoList(userRolesVo, pager));
    }

    /**
    *  @Description    ：为用户添加角色（前台用户）
    *  @Method_Name    ：saveUserRoles
    *  @param login
    *  @param sysRoleId
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/addUserRoles")
    @ResponseBody
    @ActionLog(msg = "为用户添加角色, 用户手机号: {args[0]}")
    public ResponseEntity addUserRoles(String login,Integer sysRoleId) {
        return roleService.saveUserRoles(login,sysRoleId);
    }
    /**
    *  @Description    ：删除用户角色关系（前台用户）
    *  @Method_Name    ：delUserRoles
    *  @param id
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/delUserRoles")
    @ResponseBody
    @ActionLog(msg = "为用户删除角色,关系id: {args[0]}")
    public ResponseEntity delUserRoles(Integer id) {
        return roleService.delUserRoles(id);
    }

    /**
    *  @Description    ：前台角色查询
    *  @Method_Name    ：myAccountRoles
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date  ：2018/8/13
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    @RequestMapping("/myAccountRoles")
    @ResponseBody
    public ResponseEntity myAccountRoles() {
        return roleService.myAccountRoles();
    }



}
