package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidProduct;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.BidProductDao.java
 * @Class Name    : BidProductDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidProductDao extends MyBatisBaseDao<BidProduct, java.lang.Long> {


    /**
     * 返回分页数据
     * @param bidProduct
     * @param pager
     * @return
     */
    Pager findBidProductWithCondition(BidProduct bidProduct, Pager pager);

    /**
    *  @Description    ：产品id排序，首先根据产品是否一次本息排序，然后通过产品创建时间排序
    *  @Method_Name    ：orderedProductIds
    *  @param orderedProductId
    *  @return java.util.List<java.lang.Integer>
    *  @Creation Date  ：2018/7/4
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<Integer> orderedProductIds(List<Integer> orderedProductId);

    /**
     *  @Description    ：产品id排序，首先根据产品是否一次本息排序，然后通过产品创建时间排序
     *  @Method_Name    ：orderedByPayemntAndCreateTime
     *  @param orderedProductId
     *  @return java.util.List<java.lang.Integer>
     *  @Creation Date  ：2018/7/4
     *  @Author         ：zhongpingtang@hongkun.com.cn
     */
    List<Integer> orderedByPayemntAndCreateTime(List<Integer> orderedProductId);
}
