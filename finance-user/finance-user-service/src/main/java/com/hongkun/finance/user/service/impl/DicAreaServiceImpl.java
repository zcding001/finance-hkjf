package com.hongkun.finance.user.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.user.model.DicArea;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.user.dao.DicAreaDao;
import com.hongkun.finance.user.service.DicAreaService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.DicAreaServiceImpl.java
 * @Class Name    : DicAreaServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class DicAreaServiceImpl implements DicAreaService {

	private static final Logger logger = LoggerFactory.getLogger(DicAreaServiceImpl.class);
	
	/**
	 * DicAreaDAO
	 */
	@Autowired
	private DicAreaDao dicAreaDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertDicArea(DicArea dicArea) {
		this.dicAreaDao.save(dicArea);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertDicAreaBatch(List<DicArea> list) {
		this.dicAreaDao.insertBatch(DicArea.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertDicAreaBatch(List<DicArea> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.dicAreaDao.insertBatch(DicArea.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateDicArea(DicArea dicArea) {
		this.dicAreaDao.update(dicArea);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateDicAreaBatch(List<DicArea> list, int count) {
		this.dicAreaDao.updateBatch(DicArea.class, list, count);
	}
	
	@Override
	public DicArea findDicAreaById(int id) {
		return this.dicAreaDao.findByPK(Long.valueOf(id), DicArea.class);
	}
	
	@Override
	public List<DicArea> findDicAreaList(DicArea dicArea) {
		return this.dicAreaDao.findByCondition(dicArea);
	}
	
	@Override
	public List<DicArea> findDicAreaList(DicArea dicArea, int start, int limit) {
		return this.dicAreaDao.findByCondition(dicArea, start, limit);
	}
	
	@Override
	public Pager findDicAreaList(DicArea dicArea, Pager pager) {
		return this.dicAreaDao.findByCondition(dicArea, pager);
	}
	
	@Override
	public int findDicAreaCount(DicArea dicArea){
		return this.dicAreaDao.getTotalCount(dicArea);
	}
	
	@Override
	public Pager findDicAreaList(DicArea dicArea, Pager pager, String sqlName){
		return this.dicAreaDao.findByCondition(dicArea, pager, sqlName);
	}
	
	@Override
	public Integer findDicAreaCount(DicArea dicArea, String sqlName){
		return this.dicAreaDao.getTotalCount(dicArea, sqlName);
	}
}
