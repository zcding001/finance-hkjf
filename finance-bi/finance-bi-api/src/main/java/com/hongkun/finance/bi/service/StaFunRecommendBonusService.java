package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaFunRecommendBonus;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaFunRecommendBonusService.java
 * @Class Name    : StaFunRecommendBonusService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaFunRecommendBonusService {
	/**
	 * @Described			: 条件检索数据
	 * @param staFunRecommendBonus 检索条件
	 * @return	List<StaFunRecommendBonus>
	 */
	List<StaFunRecommendBonus> findStaFunRecommendBonusList(StaFunRecommendBonus staFunRecommendBonus);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunRecommendBonus 检索条件
	 * @param pager	分页数据
	 * @return	List<StaFunRecommendBonus>
	 */
	 Pager findStaFunRecommendBonusList(StaFunRecommendBonus staFunRecommendBonus, Pager pager);
}
