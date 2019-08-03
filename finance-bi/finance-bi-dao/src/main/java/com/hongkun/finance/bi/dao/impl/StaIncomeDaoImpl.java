package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.model.StaIncome;
import com.hongkun.finance.bi.dao.StaIncomeDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.impl.StaIncomeDaoImpl.java
 * @Class Name    : StaIncomeDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaIncomeDaoImpl extends MyBatisBaseDaoImpl<StaIncome, Long> implements StaIncomeDao {

    @Override
    public BigDecimal getSumCharge(StaIncome staIncome) {
        return  super.getCurSqlSessionTemplate().selectOne(StaIncome.class.getName()+".getSumCharge",staIncome);
    }

    @Override
    public BigDecimal getSumPureMoney(StaIncome staIncome) {
        return super.getCurSqlSessionTemplate().selectOne(StaIncome.class.getName()+".getSumPureMoney",staIncome);
    }
}
