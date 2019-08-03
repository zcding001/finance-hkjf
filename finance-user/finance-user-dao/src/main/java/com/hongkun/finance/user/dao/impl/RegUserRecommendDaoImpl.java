package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.model.RegUserRecommend;
import com.hongkun.finance.user.dao.RegUserRecommendDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.dao.impl.RegUserRecommendDaoImpl.java
 * @Class Name : RegUserRecommendDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class RegUserRecommendDaoImpl extends MyBatisBaseDaoImpl<RegUserRecommend, java.lang.Long>
		implements RegUserRecommendDao {
	private static final String FIND_REGUSER_RECOMMEND_BY_REGUSERID = ".findRegUserRecommendByRegUserId";

	@Override
	public RegUserRecommend findRegUserRecommendByRegUserId(Integer regUserId) {
		return getCurSqlSessionTemplate()
				.selectOne(RegUserRecommend.class.getName() + FIND_REGUSER_RECOMMEND_BY_REGUSERID, regUserId);
	}

}
