package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.dao.PointSignInfoDao;
import com.hongkun.finance.point.model.PointSignInfo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointSignInfoDaoImpl.java
 * @Class Name    : PointSignInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointSignInfoDaoImpl extends MyBatisBaseDaoImpl<PointSignInfo, Long> implements PointSignInfoDao {
    @Override
    public PointSignInfo getByRegUserId(int regUserId) {
        return getCurSqlSessionTemplate()
                .selectOne(PointSignInfo.class.getName() + ".getByRegUserId", regUserId);
    }
}
