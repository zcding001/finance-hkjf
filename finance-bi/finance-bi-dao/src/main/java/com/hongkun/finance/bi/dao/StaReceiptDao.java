package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaReceipt;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.StaReceiptDao.java
 * @Class Name    : StaReceiptDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaReceiptDao extends MyBatisBaseDao<StaReceipt, java.lang.Long> {

    /**
     *  回款累计查询
     *  @Method_Name             ：findStaReceiptAddup
     *  @param staReceipt
     *  @return com.hongkun.finance.bi.model.StaReceipt
     *  @Creation Date           ：2018/9/20
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaReceipt findStaReceiptAddup(StaReceipt staReceipt);

    /**
     *  查询未来回款统计
     *  @Method_Name             ：findStaReceiptFuture
     *
     *  @return com.hongkun.finance.bi.model.StaReceipt
     *  @Creation Date           ：2018/9/20
     *  @Author                  ：zc.ding@foxmail.com
     */
    StaReceipt findStaReceiptFuture();
	
}
