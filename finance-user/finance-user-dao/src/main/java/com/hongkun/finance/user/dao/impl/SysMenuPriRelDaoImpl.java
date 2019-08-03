package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.SysMenuPriRelDao;
import com.hongkun.finance.user.dto.MenuPriDTO;
import com.hongkun.finance.user.model.SysMenuPriRel;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.impl.SysMenuPriRelDaoImpl.java
 * @Class Name    : SysMenuPriRelDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SysMenuPriRelDaoImpl extends MyBatisBaseDaoImpl<SysMenuPriRel, Long> implements SysMenuPriRelDao {

    private static final String CLEAR_MENU_PRIS = ".clearMenuPrisRelByMenuIdOrPriId";
    @Override
    public Integer clearMenuPrisRelByMenuIdOrPriId(MenuPriDTO menuIds) {
       return  getCurSqlSessionTemplate().update(SysMenuPriRel.class.getName() + CLEAR_MENU_PRIS, menuIds);
    }
}
