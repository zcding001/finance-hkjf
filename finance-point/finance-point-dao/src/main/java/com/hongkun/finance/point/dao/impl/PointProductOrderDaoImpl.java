package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.dao.PointProductOrderDao;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointProductOrderDaoImpl.java
 * @Class Name    : PointProductOrderDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointProductOrderDaoImpl extends MyBatisBaseDaoImpl<PointProductOrder, Long> implements PointProductOrderDao {

    private static final String LIST_POINT_ORDER = ".listPointOrder";

    @Override
    public Pager listPointOrder(PointOrderQuery pointOrderQuery, Pager pager) {
       return  findByCondition(pointOrderQuery, pager, PointProductOrder.class, LIST_POINT_ORDER);
    }
}
