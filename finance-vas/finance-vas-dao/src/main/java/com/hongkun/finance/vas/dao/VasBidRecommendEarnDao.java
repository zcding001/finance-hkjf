package com.hongkun.finance.vas.dao;

import java.util.Map;

import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.VasBiddRecommendEarnDao.java
 * @Class Name : VasBiddRecommendEarnDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface VasBidRecommendEarnDao extends MyBatisBaseDao<VasBidRecommendEarn, java.lang.Long> {
	/***
	 * @Description : 根据条件分页查询推荐记录信息
	 * @Method_Name : findVasBiddRecommendEarnListByInfo;
	 * @param biddRecommendEarnMap
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月25日 下午5:20:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findVasBidRecommendEarnListByInfo(Map<String, Object> bidRecommendEarnMap, Pager pager);

	/**
	 * @Description : 根据条件统计好友推荐奖信息
	 * @Method_Name : bidRecommendEarnInfoCount;
	 * @param vasBiddRecommendEarn
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年10月19日 上午9:41:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Map<String, Object> bidRecommendEarnInfoCount(VasBidRecommendEarn vasBidRecommendEarn);

}
