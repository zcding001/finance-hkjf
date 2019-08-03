package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.bi.dao.BalAccountRecordDao;
import com.hongkun.finance.bi.model.BalAccountRecord;
import com.hongkun.finance.bi.service.BalAccountRecordService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.BalAccountRecordServiceImpl.java
 * @Class Name    : BalAccountRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BalAccountRecordServiceImpl implements BalAccountRecordService {

	private static final Logger logger = LoggerFactory.getLogger(BalAccountRecordServiceImpl.class);
	
	/**
	 * BalAccountRecordDAO
	 */
	@Autowired
	private BalAccountRecordDao balAccountRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccountRecord(BalAccountRecord balAccountRecord) {
		this.balAccountRecordDao.save(balAccountRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccountRecordBatch(List<BalAccountRecord> list) {
		this.balAccountRecordDao.insertBatch(BalAccountRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBalAccountRecordBatch(List<BalAccountRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.balAccountRecordDao.insertBatch(BalAccountRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBalAccountRecord(BalAccountRecord balAccountRecord) {
		this.balAccountRecordDao.update(balAccountRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBalAccountRecordBatch(List<BalAccountRecord> list, int count) {
		this.balAccountRecordDao.updateBatch(BalAccountRecord.class, list, count);
	}
	
	@Override
	public BalAccountRecord findBalAccountRecordById(int id) {
		return this.balAccountRecordDao.findByPK(Long.valueOf(id), BalAccountRecord.class);
	}
	
	@Override
	public List<BalAccountRecord> findBalAccountRecordList(BalAccountRecord balAccountRecord) {
		return this.balAccountRecordDao.findByCondition(balAccountRecord);
	}
	
	@Override
	public List<BalAccountRecord> findBalAccountRecordList(BalAccountRecord balAccountRecord, int start, int limit) {
		return this.balAccountRecordDao.findByCondition(balAccountRecord, start, limit);
	}
	
	@Override
	public Pager findBalAccountRecordList(BalAccountRecord balAccountRecord, Pager pager) {
		return this.balAccountRecordDao.findByCondition(balAccountRecord, pager);
	}
	
	@Override
	public int findBalAccountRecordCount(BalAccountRecord balAccountRecord){
		return this.balAccountRecordDao.getTotalCount(balAccountRecord);
	}
	
	@Override
	public Pager findBalAccountRecordList(BalAccountRecord balAccountRecord, Pager pager, String sqlName){
		return this.balAccountRecordDao.findByCondition(balAccountRecord, pager, sqlName);
	}
	
	@Override
	public Integer findBalAccountRecordCount(BalAccountRecord balAccountRecord, String sqlName){
		return this.balAccountRecordDao.getTotalCount(balAccountRecord, sqlName);
	}
}
