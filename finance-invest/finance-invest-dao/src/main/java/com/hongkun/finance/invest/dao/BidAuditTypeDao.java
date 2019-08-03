package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidAuditType;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.dao.BidAuditTypeDao.java
 * @Class Name    : BidAuditTypeDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface BidAuditTypeDao extends MyBatisBaseDao<BidAuditType, Long> {

    /**
     * 查询id和name对应的map
     * @return
     */
    List<Map<Integer, String>> findBidAuditTypeIdAndName();

}
