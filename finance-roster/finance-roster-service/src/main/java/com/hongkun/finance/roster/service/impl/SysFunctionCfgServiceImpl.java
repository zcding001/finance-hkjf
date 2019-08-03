package com.hongkun.finance.roster.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.roster.model.SysFunctionCfg;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.roster.dao.SysFunctionCfgDao;
import com.hongkun.finance.roster.service.SysFunctionCfgService;

/**
 * @Project       : finance-roster
 * @Program Name  : com.hongkun.finance.roster.service.impl.SysFunctionCfgServiceImpl.java
 * @Class Name    : SysFunctionCfgServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class SysFunctionCfgServiceImpl implements SysFunctionCfgService {

	private static final Logger logger = LoggerFactory.getLogger(SysFunctionCfgServiceImpl.class);
	
	/**
	 * SysFunctionCfgDAO
	 */
	@Autowired
	private SysFunctionCfgDao sysFunctionCfgDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSysFunctionCfg(SysFunctionCfg sysFunctionCfg) {
		this.sysFunctionCfgDao.save(sysFunctionCfg);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSysFunctionCfgBatch(List<SysFunctionCfg> list) {
		this.sysFunctionCfgDao.insertBatch(SysFunctionCfg.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertSysFunctionCfgBatch(List<SysFunctionCfg> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.sysFunctionCfgDao.insertBatch(SysFunctionCfg.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateSysFunctionCfg(SysFunctionCfg sysFunctionCfg) {
		this.sysFunctionCfgDao.update(sysFunctionCfg);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateSysFunctionCfgBatch(List<SysFunctionCfg> list, int count) {
		this.sysFunctionCfgDao.updateBatch(SysFunctionCfg.class, list, count);
	}
	
	@Override
	public SysFunctionCfg findSysFunctionCfgById(int id) {
		return this.sysFunctionCfgDao.findByPK(Long.valueOf(id), SysFunctionCfg.class);
	}
	
	@Override
	public List<SysFunctionCfg> findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg) {
		return this.sysFunctionCfgDao.findByCondition(sysFunctionCfg);
	}
	
	@Override
	public List<SysFunctionCfg> findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, int start, int limit) {
		return this.sysFunctionCfgDao.findByCondition(sysFunctionCfg, start, limit);
	}
	
	@Override
	public Pager findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, Pager pager) {
		return this.sysFunctionCfgDao.findByCondition(sysFunctionCfg, pager);
	}
	
	@Override
	public int findSysFunctionCfgCount(SysFunctionCfg sysFunctionCfg){
		return this.sysFunctionCfgDao.getTotalCount(sysFunctionCfg);
	}
	
	@Override
	public Pager findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, Pager pager, String sqlName){
		return this.sysFunctionCfgDao.findByCondition(sysFunctionCfg, pager, sqlName);
	}
	
	@Override
	public Integer findSysFunctionCfgCount(SysFunctionCfg sysFunctionCfg, String sqlName){
		return this.sysFunctionCfgDao.getTotalCount(sysFunctionCfg, sqlName);
	}
}
