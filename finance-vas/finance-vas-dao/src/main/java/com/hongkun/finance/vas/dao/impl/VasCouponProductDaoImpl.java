package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.dao.VasCouponProductDao;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.loan.dao.impl.VasCouponProductDaoImpl.java
 * @Class Name    : VasCouponProductDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasCouponProductDaoImpl extends MyBatisBaseDaoImpl<VasCouponProduct, Long> implements VasCouponProductDao {
    private static final String COUPON_DETAIL_SEARCH = ".couponDetailSearch";

    @Override
    public Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO, List<Integer> limitsUserIds, Pager pager) {
        //组装参数
        Map paramMap = new HashMap();
        paramMap.put("vas", vasCouponDetailVO);
        paramMap.put("limitsUserIds", limitsUserIds);
        //查询结果
        return this.findByCondition(paramMap,pager,VasCouponDetail.class,COUPON_DETAIL_SEARCH);
    }

    @Override
    public Pager staCouponList(VasCouponProduct vasCouponProduct, Pager pager) {
        return this.findByCondition(vasCouponProduct,pager,VasCouponProduct.class,".staCouponList");
    }
}
