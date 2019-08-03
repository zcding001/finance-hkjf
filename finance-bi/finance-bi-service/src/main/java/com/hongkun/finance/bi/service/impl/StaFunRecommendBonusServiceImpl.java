package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaFunRecommendBonus;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaFunRecommendBonusDao;
import com.hongkun.finance.bi.service.StaFunRecommendBonusService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaFunRecommendBonusServiceImpl.java
 * @Class Name    : StaFunRecommendBonusServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaFunRecommendBonusServiceImpl implements StaFunRecommendBonusService {

	private static final Logger logger = LoggerFactory.getLogger(StaFunRecommendBonusServiceImpl.class);
	
	/**
	 * StaFunRecommendBonusDAO
	 */
	@Autowired
	private StaFunRecommendBonusDao staFunRecommendBonusDao;
	
	@Override
	public List<StaFunRecommendBonus> findStaFunRecommendBonusList(StaFunRecommendBonus staFunRecommendBonus) {
		return this.staFunRecommendBonusDao.findByCondition(staFunRecommendBonus);
	}
	
	@Override
	public Pager findStaFunRecommendBonusList(StaFunRecommendBonus staFunRecommendBonus, Pager pager) {
		return this.staFunRecommendBonusDao.findByCondition(staFunRecommendBonus, pager);
	}
}
