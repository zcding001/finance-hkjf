package com.hongkun.finance.point.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.point.model.PointSignRule;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.point.dao.PointSignRuleDao;
import com.hongkun.finance.point.service.PointSignRuleService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointSignRuleServiceImpl.java
 * @Class Name    : PointSignRuleServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointSignRuleServiceImpl implements PointSignRuleService {

	private static final Logger logger = LoggerFactory.getLogger(PointSignRuleServiceImpl.class);
	
	/**
	 * PointSignRuleDAO
	 */
	@Autowired
	private PointSignRuleDao pointSignRuleDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignRule(PointSignRule pointSignRule) {
		this.pointSignRuleDao.save(pointSignRule);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignRuleBatch(List<PointSignRule> list) {
		this.pointSignRuleDao.insertBatch(PointSignRule.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignRuleBatch(List<PointSignRule> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.pointSignRuleDao.insertBatch(PointSignRule.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointSignRule(PointSignRule pointSignRule) {
		this.pointSignRuleDao.update(pointSignRule);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointSignRuleBatch(List<PointSignRule> list, int count) {
		this.pointSignRuleDao.updateBatch(PointSignRule.class, list, count);
	}
	
	@Override
	public PointSignRule findPointSignRuleById(int id) {
		return this.pointSignRuleDao.findByPK(Long.valueOf(id), PointSignRule.class);
	}
	
	@Override
	public List<PointSignRule> findPointSignRuleList(PointSignRule pointSignRule) {
		return this.pointSignRuleDao.findByCondition(pointSignRule);
	}
	
	@Override
	public List<PointSignRule> findPointSignRuleList(PointSignRule pointSignRule, int start, int limit) {
		return this.pointSignRuleDao.findByCondition(pointSignRule, start, limit);
	}
	
	@Override
	public Pager findPointSignRuleList(PointSignRule pointSignRule, Pager pager) {
		return this.pointSignRuleDao.findByCondition(pointSignRule, pager);
	}
	
	@Override
	public int findPointSignRuleCount(PointSignRule pointSignRule){
		return this.pointSignRuleDao.getTotalCount(pointSignRule);
	}
}
