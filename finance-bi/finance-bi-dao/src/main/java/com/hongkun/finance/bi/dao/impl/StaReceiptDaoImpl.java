package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.model.StaReceipt;
import com.hongkun.finance.bi.dao.StaReceiptDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.impl.StaReceiptDaoImpl.java
 * @Class Name    : StaReceiptDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaReceiptDaoImpl extends MyBatisBaseDaoImpl<StaReceipt, java.lang.Long> implements StaReceiptDao {

    @Override
    public StaReceipt findStaReceiptAddup(StaReceipt staReceipt) {
        return getCurSqlSessionTemplate().selectOne(StaReceipt.class.getName() + ".findStaReceiptAddup", staReceipt);
    }

    @Override
    public StaReceipt findStaReceiptFuture() {
        return getCurSqlSessionTemplate().selectOne(StaReceipt.class.getName() + ".findStaReceiptFuture");
    }
}
