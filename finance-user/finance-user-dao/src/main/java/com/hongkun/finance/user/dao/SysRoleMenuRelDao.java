package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.dto.RoleMenuDTO;
import com.hongkun.finance.user.model.SysRoleMenuRel;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Arrays;
import java.util.List;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.SysRoleMenuRelDao.java
 * @Class Name    : SysRoleMenuRelDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SysRoleMenuRelDao extends MyBatisBaseDao<SysRoleMenuRel, Long> {

    /**
     * 清除角色和菜单的关联关系
     * @param roleMenuDTO
     * @return
     */
    Integer clearRoleMenuRelByroleIdOrMenuId(RoleMenuDTO roleMenuDTO);

    /**
     * 找到角色所绑定的菜单id
     * @param roleId
     * @return
     */
    List<Integer> findRolesBindMenuIds(Integer roleId);
    /**
    *  @Description    ：根据角色查询菜单
    *  @Method_Name    ：findMenuByRoleIds
    *  @param roleIds
    *  @return java.util.List<java.lang.Integer>
    *  @Creation Date  ：2018/8/10
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    List<Integer> findMenuByRoleIds(List<Integer> roleIds);
}
