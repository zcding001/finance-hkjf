package com.hongkun.finance.payment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinChannelGrantDao;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.service.FinChannelGrantService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinChannelGrantServiceImpl.
 *          java
 * @Class Name : FinChannelGrantServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinChannelGrantServiceImpl implements FinChannelGrantService {

	private static final Logger logger = LoggerFactory.getLogger(FinChannelGrantServiceImpl.class);

	/**
	 * FinChannelGrantDAO
	 */
	@Autowired
	private FinChannelGrantDao finChannelGrantDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinChannelGrant(FinChannelGrant finChannelGrant) {
		this.finChannelGrantDao.save(finChannelGrant);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinChannelGrantBatch(List<FinChannelGrant> list) {
		this.finChannelGrantDao.insertBatch(FinChannelGrant.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinChannelGrantBatch(List<FinChannelGrant> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finChannelGrantDao.insertBatch(FinChannelGrant.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinChannelGrant(FinChannelGrant finChannelGrant) {
		this.finChannelGrantDao.update(finChannelGrant);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinChannelGrantBatch(List<FinChannelGrant> list, int count) {
		this.finChannelGrantDao.updateBatch(FinChannelGrant.class, list, count);
	}

	@Override
	public FinChannelGrant findFinChannelGrantById(int id) {
		return this.finChannelGrantDao.findByPK(Long.valueOf(id), FinChannelGrant.class);
	}

	@Override
	public List<FinChannelGrant> findFinChannelGrantList(FinChannelGrant finChannelGrant) {
		return this.finChannelGrantDao.findByCondition(finChannelGrant);
	}

	@Override
	public List<FinChannelGrant> findFinChannelGrantList(FinChannelGrant finChannelGrant, int start, int limit) {
		return this.finChannelGrantDao.findByCondition(finChannelGrant, start, limit);
	}

	@Override
	public Pager findFinChannelGrantList(FinChannelGrant finChannelGrant, Pager pager) {
		return this.finChannelGrantDao.findByCondition(finChannelGrant, pager);
	}

	@Override
	public int findFinChannelGrantCount(FinChannelGrant finChannelGrant) {
		return this.finChannelGrantDao.getTotalCount(finChannelGrant);
	}

	@Override
	public List<FinChannelGrant> findFinChannelGrantList(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum) {
		return this.finChannelGrantDao.findFinChannelGrantList(systemTypeEnums, platformSourceEnums, payStyleEnum);
	}

	@Override
	public FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum) {
		return this.finChannelGrantDao.findFirstFinChannelGrant(systemTypeEnums, platformSourceEnums, payStyleEnum);
	}
}
