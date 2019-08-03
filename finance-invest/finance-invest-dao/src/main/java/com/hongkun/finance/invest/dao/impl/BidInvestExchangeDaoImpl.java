package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.model.BidInvestExchange;
import com.hongkun.finance.invest.dao.BidInvestExchangeDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project       : finance_hkjf
 * @Program Name  : com.hongkun.finance.invest.dao.impl.BidInvestExchangeDaoImpl.java
 * @Class Name    : BidInvestExchangeDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidInvestExchangeDaoImpl extends MyBatisBaseDaoImpl<BidInvestExchange, Long> implements BidInvestExchangeDao {

    @Override
    public List<BidInvestExchange> findInvestWating() {
        return  super.getCurSqlSessionTemplate().selectList(BidInvestExchange.class.getName()+".findInvestWating");
    }

    @Override
    public List<BidInvestExchange> findInvestSubWating(BigDecimal subWatingMoney) {
        return super.getCurSqlSessionTemplate().selectList(BidInvestExchange.class.getName()+".findInvestSubWating",subWatingMoney);
    }

    @Override
    public Pager investExchangeListForPager(Integer bidId, Pager pager) {
        BidInvestExchange exchange = new BidInvestExchange();
        exchange.setBidId(bidId);
        return  this.findByCondition(exchange,pager);
    }
}
