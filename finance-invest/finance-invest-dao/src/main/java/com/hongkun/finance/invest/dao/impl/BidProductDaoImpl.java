package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.dao.BidProductDao;
import com.hongkun.finance.invest.model.BidProduct;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.impl.BidProductDaoImpl.java
 * @Class Name    : BidProductDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidProductDaoImpl extends MyBatisBaseDaoImpl<BidProduct, java.lang.Long> implements BidProductDao {



    /**
     * 根据条件模糊查询
     */
    public static final String FIND_BID_PRODUCT_WITH_CONDITION = ".findBidProductWithCondition";
    public static final String ORDERED_PRODUCTIDS = ".orderedProductIds";
    private static final String ORDERED_BY_PAYMENT_TIME = ".orderedByPayemntAndCreateTime";

    @Override
    public Pager findBidProductWithCondition(BidProduct bidProduct, Pager pager) {
        bidProduct.setSortColumns("create_time desc");
        return this.findByCondition(bidProduct, pager, FIND_BID_PRODUCT_WITH_CONDITION);
    }

    @Override
    public List<Integer> orderedProductIds(List<Integer> list) {
        return this.getCurSqlSessionTemplate().selectList(BidProduct.class.getName() + ORDERED_PRODUCTIDS, list);
    }

    @Override
    public List<Integer> orderedByPayemntAndCreateTime(List<Integer> list) {
        return this.getCurSqlSessionTemplate().selectList(BidProduct.class.getName() + ORDERED_BY_PAYMENT_TIME, list);
    }
}
