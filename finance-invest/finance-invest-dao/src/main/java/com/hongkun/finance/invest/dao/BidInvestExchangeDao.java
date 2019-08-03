package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidInvestExchange;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Project       : finance_hkjf
 * @Program Name  : com.hongkun.finance.invest.dao.BidInvestExchangeDao.java
 * @Class Name    : BidInvestExchangeDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidInvestExchangeDao extends MyBatisBaseDao<BidInvestExchange, Long> {
    /**
    *  @Description    ：方法描述信息
        *  @Method_Name    ：findInvestWating
        *
        *  @return java.util.List<com.hongkun.finance.invest.model.BidInvestExchange>
        *  @Creation Date  ：2019/1/16
        *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
        */
        List<BidInvestExchange> findInvestWating();
    /**
    *  @Description    ：查询金额小于250000的预匹配记录
    *  @Method_Name    ：findInvestSubWating
    *  @param subWatingMoney
    *  @return java.util.List<com.hongkun.finance.invest.model.BidInvestExchange>
    *  @Creation Date  ：2019/1/17
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    List<BidInvestExchange> findInvestSubWating(BigDecimal subWatingMoney);

    Pager investExchangeListForPager(Integer bidId, Pager pager);
}
