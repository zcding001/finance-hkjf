package com.hongkun.finance.payment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinPlatformPaywayDao;
import com.hongkun.finance.payment.model.FinPlatformPayway;
import com.hongkun.finance.payment.service.FinPlatformPaywayService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinPlatformPaywayServiceImpl.
 *          java
 * @Class Name : FinPlatformPaywayServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinPlatformPaywayServiceImpl implements FinPlatformPaywayService {

	private static final Logger logger = LoggerFactory.getLogger(FinPlatformPaywayServiceImpl.class);

	/**
	 * FinPlatformPaywayDAO
	 */
	@Autowired
	private FinPlatformPaywayDao finPlatformPaywayDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPlatformPayway(FinPlatformPayway finPlatformPayway) {
		this.finPlatformPaywayDao.save(finPlatformPayway);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPlatformPaywayBatch(List<FinPlatformPayway> list) {
		this.finPlatformPaywayDao.insertBatch(FinPlatformPayway.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPlatformPaywayBatch(List<FinPlatformPayway> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finPlatformPaywayDao.insertBatch(FinPlatformPayway.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPlatformPayway(FinPlatformPayway finPlatformPayway) {
		this.finPlatformPaywayDao.update(finPlatformPayway);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPlatformPaywayBatch(List<FinPlatformPayway> list, int count) {
		this.finPlatformPaywayDao.updateBatch(FinPlatformPayway.class, list, count);
	}

	@Override
	public FinPlatformPayway findFinPlatformPaywayById(int id) {
		return this.finPlatformPaywayDao.findByPK(Long.valueOf(id), FinPlatformPayway.class);
	}

	@Override
	public List<FinPlatformPayway> findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway) {
		return this.finPlatformPaywayDao.findByCondition(finPlatformPayway);
	}

	@Override
	public List<FinPlatformPayway> findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway, int start,
			int limit) {
		return this.finPlatformPaywayDao.findByCondition(finPlatformPayway, start, limit);
	}

	@Override
	public Pager findFinPlatformPaywayList(FinPlatformPayway finPlatformPayway, Pager pager) {
		return this.finPlatformPaywayDao.findByCondition(finPlatformPayway, pager);
	}

	@Override
	public int findFinPlatformPaywayCount(FinPlatformPayway finPlatformPayway) {
		return this.finPlatformPaywayDao.getTotalCount(finPlatformPayway);
	}

	@Override
	public List<FinPlatformPayway> findPayChannelInfo(SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums) {
		FinPlatformPayway finPlatformPayway = new FinPlatformPayway();
		finPlatformPayway.setSysNameCode(systemTypeEnums.getType());
		finPlatformPayway.setPlatformName(platformSourceEnums.getType());
		finPlatformPayway.setState(TradeStateConstants.START_USING_STATE);
		return this.finPlatformPaywayDao.findPayChannelInfo(finPlatformPayway);
	}
}
