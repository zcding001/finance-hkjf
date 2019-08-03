package com.hongkun.finance.invest.dao.impl;


import com.hongkun.finance.invest.dao.BidAuditTypeDao;
import com.hongkun.finance.invest.model.BidAuditType;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.dao.impl.BidAuditTypeDaoImpl.java
 * @Class Name    : BidAuditTypeDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class BidAuditTypeDaoImpl extends MyBatisBaseDaoImpl<BidAuditType, Long> implements BidAuditTypeDao {

    /**
     * 根据条件模糊查询
     */
    public String FIND_BID_AUDIT_TYPE_ID_AND_NAME = ".findBidAuditTypeIdAndName";


    @Override
    public List<Map<Integer, String>> findBidAuditTypeIdAndName() {
        return getCurSqlSessionTemplate().selectList(BidAuditType.class.getName()+FIND_BID_AUDIT_TYPE_ID_AND_NAME);
    }
}
