package com.hongkun.finance.vas.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.vas.model.VasRebatesRuleChild;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.vas.dao.VasRebatesRuleChildDao;
import com.hongkun.finance.vas.service.VasRebatesRuleChildService;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.service.impl.VasRebatesRuleChildServiceImpl.
 *          java
 * @Class Name : VasRebatesRuleChildServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class VasRebatesRuleChildServiceImpl implements VasRebatesRuleChildService {

	private static final Logger logger = LoggerFactory.getLogger(VasRebatesRuleChildServiceImpl.class);

	/**
	 * VasRebatesRuleChildDAO
	 */
	@Autowired
	private VasRebatesRuleChildDao vasRebatesRuleChildDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasRebatesRuleChild(VasRebatesRuleChild vasRebatesRuleChild) {
		this.vasRebatesRuleChildDao.save(vasRebatesRuleChild);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasRebatesRuleChildBatch(List<VasRebatesRuleChild> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.vasRebatesRuleChildDao.insertBatch(VasRebatesRuleChild.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasRebatesRuleChild(VasRebatesRuleChild vasRebatesRuleChild) {
		this.vasRebatesRuleChildDao.update(vasRebatesRuleChild);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasRebatesRuleChildBatch(List<VasRebatesRuleChild> list, int count) {
		this.vasRebatesRuleChildDao.updateBatch(VasRebatesRuleChild.class, list, count);
	}

	@Override
	public VasRebatesRuleChild findVasRebatesRuleChildById(int id) {
		return this.vasRebatesRuleChildDao.findByPK(Long.valueOf(id), VasRebatesRuleChild.class);
	}

	@Override
	public List<VasRebatesRuleChild> findVasRebatesRuleChildList(VasRebatesRuleChild vasRebatesRuleChild) {
		return this.vasRebatesRuleChildDao.findByCondition(vasRebatesRuleChild);
	}

	@Override
	public Pager findVasRebatesRuleChildList(VasRebatesRuleChild vasRebatesRuleChild, Pager pager) {
		return this.vasRebatesRuleChildDao.findByCondition(vasRebatesRuleChild, pager);
	}

	@Override
	public int findVasRebatesRuleChildCount(VasRebatesRuleChild vasRebatesRuleChild) {
		return this.vasRebatesRuleChildDao.getTotalCount(vasRebatesRuleChild);
	}

	@Override
	public List<VasRebatesRuleChild> findVasRebatesRuleChildByRuleId(int vasRebatesRuleId) {
		return this.vasRebatesRuleChildDao.findVasRebatesRuleChildByRuleId(vasRebatesRuleId);
	}

	@Override
	public VasRebatesRuleChild findRuleChildByUserTypeAndRuleId(Integer userType, int vasRebatesRuleId) {
		return this.vasRebatesRuleChildDao.findRuleChildByUserTypeAndRuleId(userType, vasRebatesRuleId);
	}
}
