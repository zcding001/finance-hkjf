package com.hongkun.finance.payment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinCityReferDao;
import com.hongkun.finance.payment.model.FinCityRefer;
import com.hongkun.finance.payment.service.FinCityReferService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinCityReferServiceImpl.java
 * @Class Name : FinCityReferServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinCityReferServiceImpl implements FinCityReferService {

	private static final Logger logger = LoggerFactory.getLogger(FinCityReferServiceImpl.class);

	/**
	 * FinCityReferDAO
	 */
	@Autowired
	private FinCityReferDao finCityReferDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinCityRefer(FinCityRefer finCityRefer) {
		this.finCityReferDao.save(finCityRefer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinCityReferBatch(List<FinCityRefer> list) {
		this.finCityReferDao.insertBatch(FinCityRefer.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinCityReferBatch(List<FinCityRefer> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finCityReferDao.insertBatch(FinCityRefer.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinCityRefer(FinCityRefer finCityRefer) {
		this.finCityReferDao.update(finCityRefer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinCityReferBatch(List<FinCityRefer> list, int count) {
		this.finCityReferDao.updateBatch(FinCityRefer.class, list, count);
	}

	@Override
	public FinCityRefer findFinCityReferById(int id) {
		return this.finCityReferDao.findByPK(Long.valueOf(id), FinCityRefer.class);
	}

	@Override
	public List<FinCityRefer> findFinCityReferList(FinCityRefer finCityRefer) {
		return this.finCityReferDao.findByCondition(finCityRefer);
	}

	@Override
	public List<FinCityRefer> findFinCityReferList(FinCityRefer finCityRefer, int start, int limit) {
		return this.finCityReferDao.findByCondition(finCityRefer, start, limit);
	}

	@Override
	public Pager findFinCityReferList(FinCityRefer finCityRefer, Pager pager) {
		return this.finCityReferDao.findByCondition(finCityRefer, pager);
	}

	@Override
	public int findFinCityReferCount(FinCityRefer finCityRefer) {
		return this.finCityReferDao.getTotalCount(finCityRefer);
	}
}
