package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.dto.UserRoleDTO;
import com.hongkun.finance.user.model.SysUserRoleRel;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.SysUserRoleRelDao.java
 * @Class Name    : SysUserRoleRelDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SysUserRoleRelDao extends MyBatisBaseDao<SysUserRoleRel, Long> {

    /**
     * 清楚用户与角色的关联关系
     * @param userRoleDTO
     * @return
     */
    Integer clearUserRoleRelByUserIdOrRoleId(UserRoleDTO userRoleDTO);
}
