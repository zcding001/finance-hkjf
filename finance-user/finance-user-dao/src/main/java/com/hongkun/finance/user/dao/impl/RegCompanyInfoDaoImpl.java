package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.dao.RegCompanyInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegCompanyInfoDaoImpl.java
 * @Class Name    : RegCompanyInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegCompanyInfoDaoImpl extends MyBatisBaseDaoImpl<RegCompanyInfo, java.lang.Long> implements RegCompanyInfoDao {
    @Override
    public RegCompanyInfo findRegCompanyInfoByRegUserId(Integer regUserId) {
        return getCurSqlSessionTemplate().selectOne(RegCompanyInfo.class.getName() + ".findRegCompanyInfoByRegUserId", regUserId);
    }

    @Override
    public List<RegCompanyInfo> findRegCompanyInfoByLegalTel(String legalTel) {
        return getCurSqlSessionTemplate().selectList(RegCompanyInfo.class.getName() + ".findRegCompanyInfoByLegalTel", legalTel);
    }
}
