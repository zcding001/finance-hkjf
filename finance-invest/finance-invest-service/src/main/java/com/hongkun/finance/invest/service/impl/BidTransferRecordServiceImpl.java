package com.hongkun.finance.invest.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hongkun.finance.invest.model.BidTransferRecord;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.dao.BidTransferRecordDao;
import com.hongkun.finance.invest.service.BidTransferRecordService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.impl.BidTransferRecordServiceImpl.java
 * @Class Name    : BidTransferRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidTransferRecordServiceImpl implements BidTransferRecordService {

	private static final Logger logger = LoggerFactory.getLogger(BidTransferRecordServiceImpl.class);
	
	/**
	 * BidTransferRecordDAO
	 */
	@Autowired
	private BidTransferRecordDao bidTransferRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferRecord(BidTransferRecord bidTransferRecord) {
		this.bidTransferRecordDao.save(bidTransferRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferRecordBatch(List<BidTransferRecord> list) {
		this.bidTransferRecordDao.insertBatch(BidTransferRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidTransferRecordBatch(List<BidTransferRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidTransferRecordDao.insertBatch(BidTransferRecord.class, list, count);
	}
	
	@Override
	public BidTransferRecord findBidTransferRecordById(int id) {
		return this.bidTransferRecordDao.findByPK(Long.valueOf(id), BidTransferRecord.class);
	}
	
	@Override
	public List<BidTransferRecord> findBidTransferRecordList(BidTransferRecord bidTransferRecord) {
		return this.bidTransferRecordDao.findByCondition(bidTransferRecord);
	}
}
