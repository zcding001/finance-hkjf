package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaRepay;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.StaRepayDao.java
 * @Class Name    : StaRepayDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaRepayDao extends MyBatisBaseDao<StaRepay, java.lang.Long> {

    /**
     *  还款累计查询
     *  @Method_Name             ：findStaRepayAddup
     *  @param staRepay
     *  @return com.hongkun.finance.bi.model.StaRepay
     *  @Creation Date           ：2018/9/20
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaRepay findStaRepayAddup(StaRepay staRepay);

    /**
    *  查询未来还款统计
    *  @Method_Name             ：findStaRepayFuture
    * 
    *  @return com.hongkun.finance.bi.model.StaRepay
    *  @Creation Date           ：2018/9/20
    *  @Author                  ：zc.ding@foxmail.com
    */
    StaRepay findStaRepayFuture();
}
