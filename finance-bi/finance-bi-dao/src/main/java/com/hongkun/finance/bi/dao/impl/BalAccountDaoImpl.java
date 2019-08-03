package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.model.BalAccount;
import com.hongkun.finance.bi.dao.BalAccountDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.impl.BalAccountDaoImpl.java
 * @Class Name    : BalAccountDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BalAccountDaoImpl extends MyBatisBaseDaoImpl<BalAccount, Long> implements BalAccountDao {

    @Override
    public BalAccount findBalAccountByRegUserId(Integer regUserId) {
        return super.getCurSqlSessionTemplate().selectOne(BalAccount.class.getName()+".findBalAccountByRegUserId",regUserId);
    }
}
