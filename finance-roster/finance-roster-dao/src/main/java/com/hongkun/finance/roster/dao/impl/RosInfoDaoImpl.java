package com.hongkun.finance.roster.dao.impl;

import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.dao.RosInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.dao.impl.RosInfoDaoImpl.java
 * @Class Name    : RosInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RosInfoDaoImpl extends MyBatisBaseDaoImpl<RosInfo, java.lang.Long> implements RosInfoDao {

	@Override
	public Long findRosInfoForSmsTel(RosInfo rosInfo) {
		
		return getCurSqlSessionTemplate().selectOne(RosInfo.class.getName() + ".findSmsRoster", rosInfo);
	}

}
