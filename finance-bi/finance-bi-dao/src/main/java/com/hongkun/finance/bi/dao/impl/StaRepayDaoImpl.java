package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.model.StaRepay;
import com.hongkun.finance.bi.dao.StaRepayDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.impl.StaRepayDaoImpl.java
 * @Class Name    : StaRepayDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaRepayDaoImpl extends MyBatisBaseDaoImpl<StaRepay, java.lang.Long> implements StaRepayDao {

    @Override
    public StaRepay findStaRepayAddup(StaRepay staRepay) {
        return getCurSqlSessionTemplate().selectOne(StaRepay.class.getName() + ".findStaRepayAddup", staRepay);
    }
    
    @Override
    public StaRepay findStaRepayFuture(){
        return getCurSqlSessionTemplate().selectOne(StaRepay.class.getName() + ".findStaRepayFuture");
    }
}
