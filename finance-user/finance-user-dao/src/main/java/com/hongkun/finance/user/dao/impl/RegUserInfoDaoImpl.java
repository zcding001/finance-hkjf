package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.RegUserInfoDao;
import com.hongkun.finance.user.model.RegUserInfo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserInfoDaoImpl.java
 * @Class Name    : RegUserInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserInfoDaoImpl extends MyBatisBaseDaoImpl<RegUserInfo, java.lang.Long> implements RegUserInfoDao {

	@Override
	public RegUserInfo findRegUserInfoByRegUserId(int regUserId) {
        return getCurSqlSessionTemplate().selectOne(RegUserInfo.class.getName() + ".getByRegUserId", regUserId);
	}
	
	@Override
	public void deleteUserInfoByUserId(Integer userId) {
		getCurSqlSessionTemplate().update(RegUserInfo.class.getName() + ".deleteUserInfoByUserId", userId);
	}

	@Override
	public void updateByRegUserId(RegUserInfo regUserInfo) {
		getCurSqlSessionTemplate().update(RegUserInfo.class.getName() + ".updateByRegUserId", regUserInfo);
	}
}
