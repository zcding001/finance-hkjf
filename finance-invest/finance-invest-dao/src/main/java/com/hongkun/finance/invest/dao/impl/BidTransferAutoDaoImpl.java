package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.dao.BidTransferAutoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.impl.BidTransferAutoDaoImpl.java
 * @Class Name    : BidTransferAutoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidTransferAutoDaoImpl extends MyBatisBaseDaoImpl<BidTransferAuto, java.lang.Long> implements BidTransferAutoDao {

    @Override
    public void delByNewInvestIds(List<Integer> autoIds) {
        super.getCurSqlSessionTemplate().delete(BidTransferAuto.class.getName()+".delByNewInvestIds",autoIds);
    }
}
