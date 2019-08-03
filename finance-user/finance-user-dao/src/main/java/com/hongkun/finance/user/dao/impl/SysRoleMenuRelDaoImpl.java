package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.SysRoleMenuRelDao;
import com.hongkun.finance.user.dto.RoleMenuDTO;
import com.hongkun.finance.user.model.SysRoleMenuRel;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.impl.SysRoleMenuRelDaoImpl.java
 * @Class Name    : SysRoleMenuRelDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SysRoleMenuRelDaoImpl extends MyBatisBaseDaoImpl<SysRoleMenuRel, Long> implements SysRoleMenuRelDao {


    private static final String CLEAR_ROLE_MENU = ".clearRoleMenuRelByroleIdOrMenuId";
    private static final String FIND_ROLES_BIND_MENUIDS = ".findRolesBindMenuIds";
    @Override
    public Integer clearRoleMenuRelByroleIdOrMenuId(RoleMenuDTO roleMenuDTO) {
        return  getCurSqlSessionTemplate().update(SysRoleMenuRel.class.getName() + CLEAR_ROLE_MENU, roleMenuDTO);
    }


    @Override
    public List<Integer> findRolesBindMenuIds(Integer roleId) {
        return  getCurSqlSessionTemplate().selectList(SysRoleMenuRel.class.getName() + FIND_ROLES_BIND_MENUIDS, roleId);
    }

    @Override
    public List<Integer> findMenuByRoleIds(List<Integer> roleIds) {
        return getCurSqlSessionTemplate().selectList(SysRoleMenuRel.class.getName() + ".findMenuByRoleIds", roleIds);
    }
}
