package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.dao.StaFunProDisDao;
import com.hongkun.finance.bi.model.StaFunProDis;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.dao.impl.StaFunProDisDaoImpl.java
 * @Class Name    : StaFunProDisDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaFunProDisDaoImpl extends MyBatisBaseDaoImpl<StaFunProDis, Long> implements StaFunProDisDao {

    @Override
    public List<StaFunProDis> findStaFunProDisListByBidProType(StaFunProDis staFunProDis) {
        return getCurSqlSessionTemplate().selectList(StaFunProDis.class.getName() + ".findStaFunProDisListByBidProType", staFunProDis);
    }
}
