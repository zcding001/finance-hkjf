package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.dao.StaUserFirstDao;
import com.hongkun.finance.bi.model.StaUserFirst;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.dao.impl.StaUserFirstDaoImpl.java
 * @Class Name    : StaUserFirstDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaUserFirstDaoImpl extends MyBatisBaseDaoImpl<StaUserFirst, Long> implements StaUserFirstDao {

    @Override
    public List<StaUserFirst> findStaUserFirstByType(StaUserFirst staUserFirst) {
        return super.getCurSqlSessionTemplate().selectList(StaUserFirst.class.getName() + ".findStaUserFirstByType", staUserFirst);
    }

    @Override
    public StaUserFirst findStaUserFirstCount(StaUserFirst staUserFirst) {
        return super.getCurSqlSessionTemplate().selectOne(StaUserFirst.class.getName() + ".findStaUserFirstCount", staUserFirst);
    }
}
