package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.dao.PointMerchantInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointMerchantInfoDaoImpl.java
 * @Class Name    : PointMerchantInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointMerchantInfoDaoImpl extends MyBatisBaseDaoImpl<PointMerchantInfo, Long> implements PointMerchantInfoDao {

    private final String GET_POINT_MERCHANTINFO_BY_USERID = ".getPointMerchantInfoByUserId";

    private final String FIND_MERCHANTIDS_BY_INFO = ".findMerchantIdsByInfo";

    @Override
    public List<PointMerchantInfo> getPointMerchantInfoByUserId(Integer regUserId) {
        return getCurSqlSessionTemplate().selectList(PointMerchantInfo.class.getName() + GET_POINT_MERCHANTINFO_BY_USERID,
                regUserId);
    }

    @Override
    public List<Integer> findMerchantIdsByInfo(PointMerchantInfo pointMerchantInfo) {
        return getCurSqlSessionTemplate().selectList(PointMerchantInfo.class.getName() + FIND_MERCHANTIDS_BY_INFO , pointMerchantInfo);
    }
}
