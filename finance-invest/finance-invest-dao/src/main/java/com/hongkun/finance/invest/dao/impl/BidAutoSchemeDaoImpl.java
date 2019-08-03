package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.model.BidAutoScheme;
import com.hongkun.finance.invest.dao.BidAutoSchemeDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project : invest
 * @Program Name  : com.hongkun.finance.invest.dao.impl.BidAutoSchemeDaoImpl.java
 * @Class Name    : BidAutoSchemeDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class BidAutoSchemeDaoImpl extends MyBatisBaseDaoImpl<BidAutoScheme, java.lang.Long> implements BidAutoSchemeDao {

    private static String FIND_AVAILABLE_BID_AUTO_SCHEME_LIST = ".findAvailableBidAutoSchemeList";
    private static String GET_CURR_INDEX = ".getCurrIndex";


    @Override
    public List<BidAutoScheme> findAvailableBidAutoSchemeList(BidAutoScheme bidAutoScheme) {
        return getCurSqlSessionTemplate().selectList(BidAutoScheme.class.getName() + FIND_AVAILABLE_BID_AUTO_SCHEME_LIST, bidAutoScheme);
    }

    @Override
    public Integer getCurrIndex(BidAutoScheme bidAutoScheme) {
        return getCurSqlSessionTemplate().selectOne(BidAutoScheme.class.getName() + GET_CURR_INDEX,bidAutoScheme);
    }
}
