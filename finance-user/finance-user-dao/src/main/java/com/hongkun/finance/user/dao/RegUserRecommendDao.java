package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUserRecommend;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.dao.RegUserRecommendDao.java
 * @Class Name : RegUserRecommendDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface RegUserRecommendDao extends MyBatisBaseDao<RegUserRecommend, java.lang.Long> {

	/**
	 * @Described : 通过regUserId查询数据
	 * @param regUserId
	 * @return RegUserRecommend
	 */
	RegUserRecommend findRegUserRecommendByRegUserId(Integer regUserId);

}
