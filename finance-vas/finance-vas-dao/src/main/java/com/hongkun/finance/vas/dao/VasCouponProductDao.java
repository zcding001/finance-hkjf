package com.hongkun.finance.vas.dao;


import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.model.VasCouponProduct;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.dao.VasCouponProductDao.java
 * @Class Name    : VasCouponProductDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasCouponProductDao extends MyBatisBaseDao<VasCouponProduct, Long> {


    Pager couponDetailSearch(VasCouponDetailVO vasCouponDetailVO
            , List<Integer> limitsUserIds, Pager pager);

    /**
     * @Description      ：卡券统计 
     * @Method_Name      ：staCouponList 
     * @param vasCouponProduct
     * @param pager
     * @return com.yirun.framework.core.utils.pager.Pager    
     * @Creation Date    ：2019/1/7        
     * @Author           ：pengwu@hongkunjinfu.com
     */
    Pager staCouponList(VasCouponProduct vasCouponProduct, Pager pager);
}
