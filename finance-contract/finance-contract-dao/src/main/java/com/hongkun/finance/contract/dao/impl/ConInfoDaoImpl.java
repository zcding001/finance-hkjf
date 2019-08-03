package com.hongkun.finance.contract.dao.impl;

import com.hongkun.finance.contract.model.ConInfo;
import com.hongkun.finance.contract.dao.ConInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.dao.impl.ConInfoDaoImpl.java
 * @Class Name    : ConInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class ConInfoDaoImpl extends MyBatisBaseDaoImpl<ConInfo, Long> implements ConInfoDao {

    @Override
    public ConInfo findConInfo(ConInfo conInfo) {
        return this.getCurSqlSessionTemplate().selectOne(ConInfo.class.getName() + ".findConInfo", conInfo);
    }

    @Override
    public List<ConInfo> findConInfoListByInvestIds(List<Integer> listInvestIds) {
        return this.getCurSqlSessionTemplate().selectList(ConInfo.class.getName() + ".findConInfoListByInvestIds",listInvestIds);
    }
}
