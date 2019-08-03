package com.hongkun.finance.vas.dao.impl;

import java.util.Map;

import com.hongkun.finance.vas.dao.VasBidRecommendEarnDao;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.dao.impl.VasBiddRecommendEarnDaoImpl.java
 * @Class Name : VasBiddRecommendEarnDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasBidRecommendEarnDaoImpl extends MyBatisBaseDaoImpl<VasBidRecommendEarn, java.lang.Long>
		implements VasBidRecommendEarnDao {
	/**
	 * 根据条件分页查询
	 */
	public String FIND_BIDDRECOMMENDEARN_INFO = ".searchPageLists";
	/**
	 * 根据条件统计好友推荐奖信息
	 */
	public String BID_RECOMMEND_EARN_INFO_COUNT = ".bidRecommendEarnInfoCount";

	@Override
	public Pager findVasBidRecommendEarnListByInfo(Map<String, Object> bidRecommendEarnMap, Pager pager) {
		return this.findByCondition(bidRecommendEarnMap, pager, VasBidRecommendEarn.class, FIND_BIDDRECOMMENDEARN_INFO);
	}

	@Override
	public Map<String, Object> bidRecommendEarnInfoCount(VasBidRecommendEarn vasBidRecommendEarn) {
		return getCurSqlSessionTemplate().selectOne(VasBidRecommendEarn.class.getName() + BID_RECOMMEND_EARN_INFO_COUNT,
				vasBidRecommendEarn);
	}
}
