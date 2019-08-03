package com.hongkun.finance.payment.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinPayConfigDao;
import com.hongkun.finance.payment.model.FinPayConfig;
import com.hongkun.finance.payment.service.FinPayConfigService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinPayConfigServiceImpl.java
 * @Class Name : FinPayConfigServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinPayConfigServiceImpl implements FinPayConfigService {

	private static final Logger logger = LoggerFactory.getLogger(FinPayConfigServiceImpl.class);

	/**
	 * FinPayConfigDAO
	 */
	@Autowired
	private FinPayConfigDao finPayConfigDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPayConfig(FinPayConfig finPayConfig) {
		this.finPayConfigDao.save(finPayConfig);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPayConfigBatch(List<FinPayConfig> list) {
		this.finPayConfigDao.insertBatch(FinPayConfig.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPayConfigBatch(List<FinPayConfig> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finPayConfigDao.insertBatch(FinPayConfig.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPayConfig(FinPayConfig finPayConfig) {
		this.finPayConfigDao.update(finPayConfig);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPayConfigBatch(List<FinPayConfig> list, int count) {
		this.finPayConfigDao.updateBatch(FinPayConfig.class, list, count);
	}

	@Override
	public FinPayConfig findFinPayConfigById(int id) {
		return this.finPayConfigDao.findByPK(Long.valueOf(id), FinPayConfig.class);
	}

	@Override
	public List<FinPayConfig> findFinPayConfigList(FinPayConfig finPayConfig) {
		return this.finPayConfigDao.findByCondition(finPayConfig);
	}

	@Override
	public List<FinPayConfig> findFinPayConfigList(FinPayConfig finPayConfig, int start, int limit) {
		return this.finPayConfigDao.findByCondition(finPayConfig, start, limit);
	}

	@Override
	public Pager findFinPayConfigList(FinPayConfig finPayConfig, Pager pager) {
		return this.finPayConfigDao.findByCondition(finPayConfig, pager);
	}

	@Override
	public int findFinPayConfigCount(FinPayConfig finPayConfig) {
		return this.finPayConfigDao.getTotalCount(finPayConfig);
	}

	@Override
	public FinPayConfig findPayConfigInfo(String systemTypeName, String platformSourceName, String payChannel,
			String payStyle) {
		String key = systemTypeName + platformSourceName + payChannel + payStyle;
		return this.finPayConfigDao.findPayConfigInfo(key, systemTypeName, platformSourceName, payChannel, payStyle);
	}
}
