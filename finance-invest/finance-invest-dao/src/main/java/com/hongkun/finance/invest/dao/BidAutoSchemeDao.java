package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidAutoScheme;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Project       : invest
 * @Program Name  : com.hongkun.finance.invest.dao.BidAutoSchemeDao.java
 * @Class Name    : BidAutoSchemeDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidAutoSchemeDao extends MyBatisBaseDao<BidAutoScheme, java.lang.Long> {


    /**
     *  @Description    ：获取可用的自动投资方案列表
     *  @Method_Name    ：findAvailableBidAutoSchemeList
     *  @param bidAutoScheme
     *  @return java.util.List<com.hongkun.finance.invest.model.BidAutoScheme>
     *  @Creation Date  ：2018年08月29日 10:45
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    List<BidAutoScheme> findAvailableBidAutoSchemeList(BidAutoScheme bidAutoScheme);

    /**
     *  @Description    ：获取当前排名
     *  @Method_Name    ：getCurrIndex
     *  @param cdt
     *  @return java.lang.Integer
     *  @Creation Date  ：2018年08月29日 11:10
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    Integer getCurrIndex(BidAutoScheme cdt);
}
