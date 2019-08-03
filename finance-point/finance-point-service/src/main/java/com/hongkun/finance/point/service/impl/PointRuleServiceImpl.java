package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.dao.PointRuleDao;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.point.utils.PointUtils;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointRuleServiceImpl.java
 * @Class Name    : PointRuleServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointRuleServiceImpl implements PointRuleService {
	
	private static final Logger logger = LoggerFactory.getLogger(PointRuleServiceImpl.class);
	
	/**
	 * PointRuleDAO
	 */
	@Autowired
	private PointRuleDao pointRuleDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertPointRule(PointRule pointRule) {
		Date currentTime = new Date();
		//设置规则状态是未启用状态
		pointRule.setState(PointConstants.UN_CHECK);
		pointRule.setCreateTime(currentTime);
		pointRule.setModifyTime(currentTime);
		return  this.pointRuleDao.save(pointRule);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointRuleBatch(List<PointRule> list) {
		this.pointRuleDao.insertBatch(PointRule.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointRuleBatch(List<PointRule> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.pointRuleDao.insertBatch(PointRule.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updatePointRule(PointRule pointRule) {
		return this.pointRuleDao.update(pointRule);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointRuleBatch(List<PointRule> list, int count) {
		this.pointRuleDao.updateBatch(PointRule.class, list, count);
	}
	
	@Override
	public PointRule findPointRuleById(int id) {
		return this.pointRuleDao.findByPK(Long.valueOf(id), PointRule.class);
	}
	
	@Override
	public List<PointRule> findPointRuleList(PointRule pointRule) {
		return this.pointRuleDao.findByCondition(pointRule);
	}
	
	@Override
	public List<PointRule> findPointRuleList(PointRule pointRule, int start, int limit) {
		return this.pointRuleDao.findByCondition(pointRule, start, limit);
	}
	
	@Override
	public Pager findPointRuleList(PointRule pointRule, Pager pager) {
		return this.pointRuleDao.findByCondition(pointRule, pager);
	}
	
	@Override
	public int findPointRuleCount(PointRule pointRule){
		return this.pointRuleDao.getTotalCount(pointRule);
	}
	
	@Override
	public Pager findPointRuleList(PointRule pointRule, Pager pager, String sqlName){
		return this.pointRuleDao.findByCondition(pointRule, pager, sqlName);
	}
	
	@Override
	public Integer findPointRuleCount(PointRule pointRule, String sqlName){
		return this.pointRuleDao.getTotalCount(pointRule, sqlName);
	}
	
	@Override
	public int moneyToPoint(BigDecimal money) {
		int result = 0 ;
		PointRule pointRule = new PointRule();
		pointRule.setState(PointConstants.CHECK_PASS);
		List<PointRule> pointRules = this.pointRuleDao.findByCondition(pointRule);
		if(CommonUtils.isNotEmpty(pointRules)){
			PointRule currentRule = pointRules.get(0);
			result = PointUtils.moneyToPoint(money,currentRule.getPerMoneyToPoint());
		}
		return result;
	}
	@Override
	public int proMoneyToPoint(BigDecimal money) {
		int result = 0 ;
		PointRule pointRule = new PointRule();
		pointRule.setState(PointConstants.CHECK_PASS);
		List<PointRule> pointRules = this.pointRuleDao.findByCondition(pointRule);
		if(CommonUtils.isNotEmpty(pointRules)){
			PointRule currentRule = pointRules.get(0);
			result = PointUtils.moneyToPoint(money,currentRule.getProMoneyToPoint());
		}
		return result;
	}

	@Override
	public PointRule getCurrentOnUseRule() {
		return pointRuleDao.getCurrentOnUseRule();
	}
	
	
}
