package com.hongkun.finance.loan.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.loan.model.BidReturnCapRecord;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.loan.dao.BidReturnCapRecordDao;
import com.hongkun.finance.loan.service.BidReturnCapRecordService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.impl.BidReturnCapRecordServiceImpl.java
 * @Class Name    : BidReturnCapRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidReturnCapRecordServiceImpl implements BidReturnCapRecordService {

	private static final Logger logger = LoggerFactory.getLogger(BidReturnCapRecordServiceImpl.class);
	
	/**
	 * BidReturnCapRecordDAO
	 */
	@Autowired
	private BidReturnCapRecordDao bidReturnCapRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReturnCapRecord(BidReturnCapRecord bidReturnCapRecord) {
		this.bidReturnCapRecordDao.save(bidReturnCapRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReturnCapRecordBatch(List<BidReturnCapRecord> list) {
		this.bidReturnCapRecordDao.insertBatch(BidReturnCapRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReturnCapRecordBatch(List<BidReturnCapRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidReturnCapRecordDao.insertBatch(BidReturnCapRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidReturnCapRecord(BidReturnCapRecord bidReturnCapRecord) {
		this.bidReturnCapRecordDao.update(bidReturnCapRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidReturnCapRecordBatch(List<BidReturnCapRecord> list, int count) {
		this.bidReturnCapRecordDao.updateBatch(BidReturnCapRecord.class, list, count);
	}
	
	@Override
	public BidReturnCapRecord findBidReturnCapRecordById(int id) {
		return this.bidReturnCapRecordDao.findByPK(Long.valueOf(id), BidReturnCapRecord.class);
	}
	
	@Override
	public List<BidReturnCapRecord> findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord) {
		return this.bidReturnCapRecordDao.findByCondition(bidReturnCapRecord);
	}
	
	@Override
	public List<BidReturnCapRecord> findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord, int start, int limit) {
		return this.bidReturnCapRecordDao.findByCondition(bidReturnCapRecord, start, limit);
	}
	
	@Override
	public Pager findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord, Pager pager) {
		return this.bidReturnCapRecordDao.findByCondition(bidReturnCapRecord, pager);
	}
	
	@Override
	public int findBidReturnCapRecordCount(BidReturnCapRecord bidReturnCapRecord){
		return this.bidReturnCapRecordDao.getTotalCount(bidReturnCapRecord);
	}
}
