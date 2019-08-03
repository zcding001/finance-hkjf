package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaFunRecommendSend;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaFunRecommendSendService.java
 * @Class Name    : StaFunRecommendSendService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaFunRecommendSendService {
	/**
	 * @Described			: 条件检索数据
	 * @param staFunRecommendSend 检索条件
	 * @return	List<StaFunRecommendSend>
	 */
	List<StaFunRecommendSend> findStaFunRecommendSendList(StaFunRecommendSend staFunRecommendSend);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staFunRecommendSend 检索条件
	 * @param pager	分页数据
	 * @return	List<StaFunRecommendSend>
	 */
	Pager findStaFunRecommendSendList(StaFunRecommendSend staFunRecommendSend, Pager pager);
}
