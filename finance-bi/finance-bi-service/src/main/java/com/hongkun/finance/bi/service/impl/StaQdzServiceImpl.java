package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaQdz;
import com.hongkun.finance.bi.model.vo.StaQdzInOutVo;
import com.hongkun.finance.bi.model.vo.StaWithdrawVo;
import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaQdzDao;
import com.hongkun.finance.bi.service.StaQdzService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaQdzServiceImpl.java
 * @Class Name    : StaQdzServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaQdzServiceImpl implements StaQdzService {

	private static final Logger logger = LoggerFactory.getLogger(StaQdzServiceImpl.class);
	
	/**
	 * StaQdzDAO
	 */
	@Autowired
	private StaQdzDao staQdzDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaQdz(StaQdz staQdz) {
		this.staQdzDao.save(staQdz);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaQdzBatch(List<StaQdz> list) {
		this.staQdzDao.insertBatch(StaQdz.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaQdzBatch(List<StaQdz> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.staQdzDao.insertBatch(StaQdz.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaQdz(StaQdz staQdz) {
		this.staQdzDao.update(staQdz);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaQdzBatch(List<StaQdz> list, int count) {
		this.staQdzDao.updateBatch(StaQdz.class, list, count);
	}
	
	@Override
	public StaQdz findStaQdzById(int id) {
		return this.staQdzDao.findByPK(Long.valueOf(id), StaQdz.class);
	}
	
	@Override
	public List<StaQdz> findStaQdzList(StaQdz staQdz) {
		return this.staQdzDao.findByCondition(staQdz);
	}
	
	@Override
	public List<StaQdz> findStaQdzList(StaQdz staQdz, int start, int limit) {
		return this.staQdzDao.findByCondition(staQdz, start, limit);
	}
	
	@Override
	public Pager findStaQdzList(StaQdz staQdz, Pager pager) {
		return this.staQdzDao.findByCondition(staQdz, pager);
	}
	
	@Override
	public int findStaQdzCount(StaQdz staQdz){
		return this.staQdzDao.getTotalCount(staQdz);
	}

    @Override
    public StaQdzInOutVo findStaQdzSum(StaQdz staQdz) {
        return this.staQdzDao.findStaQdzSum(staQdz);
    }
}
