package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dto.UserRoleDTO;
import com.hongkun.finance.user.model.SysUserRoleRel;
import com.hongkun.finance.user.dao.SysUserRoleRelDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.impl.SysUserRoleRelDaoImpl.java
 * @Class Name    : SysUserRoleRelDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SysUserRoleRelDaoImpl extends MyBatisBaseDaoImpl<SysUserRoleRel, Long> implements SysUserRoleRelDao {

    public static final String CLEAR_USER_ROLER_EL = ".clearUserRoleRelByUserIdOrRoleId";
    @Override
    public Integer clearUserRoleRelByUserIdOrRoleId(UserRoleDTO userRoleDTO) {
        return getCurSqlSessionTemplate().update(SysUserRoleRel.class.getName() + CLEAR_USER_ROLER_EL, userRoleDTO);
    }
}
