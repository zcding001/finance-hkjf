package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.model.PointProductCategory;
import com.hongkun.finance.point.dao.PointProductCategoryDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointProductCategoryDaoImpl.java
 * @Class Name    : PointProductCategoryDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointProductCategoryDaoImpl extends MyBatisBaseDaoImpl<PointProductCategory, Long> implements PointProductCategoryDao {
    /**
     * 级联删除菜单
     */

    public String DELETE_ON_CASCADE = ".deleteoncascade";

    @Override
    public Integer deleteOnCascade(List<Integer> unDeleteIds) {
        return getCurSqlSessionTemplate().update(PointProductCategory.class.getName() + DELETE_ON_CASCADE, unDeleteIds);
    }
}
