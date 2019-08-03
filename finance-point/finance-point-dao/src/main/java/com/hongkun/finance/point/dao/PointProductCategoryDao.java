package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointProductCategory;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointProductCategoryDao.java
 * @Class Name    : PointProductCategoryDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointProductCategoryDao extends MyBatisBaseDao<PointProductCategory, Long> {

    /**
     * 级联删除菜单（逻辑删除）
     * @param unDeleteIds
     * @return
     */
    Integer deleteOnCascade(List<Integer> unDeleteIds);
}
