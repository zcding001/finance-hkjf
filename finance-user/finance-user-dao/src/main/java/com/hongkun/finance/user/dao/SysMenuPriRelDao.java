package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.dto.MenuPriDTO;
import com.hongkun.finance.user.model.SysMenuPriRel;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.user.dao.SysMenuPriRelDao.java
 * @Class Name    : SysMenuPriRelDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SysMenuPriRelDao extends MyBatisBaseDao<SysMenuPriRel, Long> {

    /**
     * 清空菜单权限
     * @param menuId
     */
    Integer clearMenuPrisRelByMenuIdOrPriId(MenuPriDTO menuId);
}
