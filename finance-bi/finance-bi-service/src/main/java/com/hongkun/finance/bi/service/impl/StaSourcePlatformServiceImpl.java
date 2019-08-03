package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaSourcePlatform;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaSourcePlatformDao;
import com.hongkun.finance.bi.service.StaSourcePlatformService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaSourcePlatformServiceImpl.java
 * @Class Name    : StaSourcePlatformServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaSourcePlatformServiceImpl implements StaSourcePlatformService {

	private static final Logger logger = LoggerFactory.getLogger(StaSourcePlatformServiceImpl.class);
	
	/**
	 * StaSourcePlatformDAO
	 */
	@Autowired
	private StaSourcePlatformDao staSourcePlatformDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaSourcePlatform(StaSourcePlatform staSourcePlatform) {
		this.staSourcePlatformDao.save(staSourcePlatform);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaSourcePlatformBatch(List<StaSourcePlatform> list) {
		this.staSourcePlatformDao.insertBatch(StaSourcePlatform.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaSourcePlatformBatch(List<StaSourcePlatform> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.staSourcePlatformDao.insertBatch(StaSourcePlatform.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaSourcePlatform(StaSourcePlatform staSourcePlatform) {
		this.staSourcePlatformDao.update(staSourcePlatform);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaSourcePlatformBatch(List<StaSourcePlatform> list, int count) {
		this.staSourcePlatformDao.updateBatch(StaSourcePlatform.class, list, count);
	}
	
	@Override
	public StaSourcePlatform findStaSourcePlatformById(int id) {
		return this.staSourcePlatformDao.findByPK(Long.valueOf(id), StaSourcePlatform.class);
	}
	
	@Override
	public List<StaSourcePlatform> findStaSourcePlatformList(StaSourcePlatform staSourcePlatform) {
		return this.staSourcePlatformDao.findByCondition(staSourcePlatform);
	}
	
	@Override
	public List<StaSourcePlatform> findStaSourcePlatformList(StaSourcePlatform staSourcePlatform, int start, int limit) {
		return this.staSourcePlatformDao.findByCondition(staSourcePlatform, start, limit);
	}
	
	@Override
	public Pager findStaSourcePlatformList(StaSourcePlatform staSourcePlatform, Pager pager) {
		return this.staSourcePlatformDao.findByCondition(staSourcePlatform, pager);
	}
	
	@Override
	public int findStaSourcePlatformCount(StaSourcePlatform staSourcePlatform){
		return this.staSourcePlatformDao.getTotalCount(staSourcePlatform);
	}
}
