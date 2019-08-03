package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointProductOrder;
import com.hongkun.finance.point.model.query.PointOrderQuery;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointProductOrderDao.java
 * @Class Name    : PointProductOrderDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointProductOrderDao extends MyBatisBaseDao<PointProductOrder, Long> {

    Pager listPointOrder(PointOrderQuery pointProductOrder, Pager pager);
}
