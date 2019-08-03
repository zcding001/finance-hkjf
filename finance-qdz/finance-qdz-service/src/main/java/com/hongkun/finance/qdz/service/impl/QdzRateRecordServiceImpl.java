package com.hongkun.finance.qdz.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.dao.QdzRateRecordDao;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.service.QdzRateRecordService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.service.impl.QdzRateRecordServiceImpl.java
 * @Class Name : QdzRateRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class QdzRateRecordServiceImpl implements QdzRateRecordService {

	private static final Logger logger = LoggerFactory.getLogger(QdzRateRecordServiceImpl.class);

	/**
	 * QdzRateRecordDAO
	 */
	@Autowired
	private QdzRateRecordDao qdzRateRecordDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzRateRecord(QdzRateRecord qdzRateRecord) {
		this.qdzRateRecordDao.save(qdzRateRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzRateRecordBatch(List<QdzRateRecord> list) {
		this.qdzRateRecordDao.insertBatch(QdzRateRecord.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzRateRecordBatch(List<QdzRateRecord> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.qdzRateRecordDao.insertBatch(QdzRateRecord.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzRateRecord(QdzRateRecord qdzRateRecord) {
		this.qdzRateRecordDao.update(qdzRateRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzRateRecordBatch(List<QdzRateRecord> list, int count) {
		this.qdzRateRecordDao.updateBatch(QdzRateRecord.class, list, count);
	}

	@Override
	public QdzRateRecord findQdzRateRecordById(int id) {
		return this.qdzRateRecordDao.findByPK(Long.valueOf(id), QdzRateRecord.class);
	}

	@Override
	public List<QdzRateRecord> findQdzRateRecordList(QdzRateRecord qdzRateRecord) {
		return this.qdzRateRecordDao.findByCondition(qdzRateRecord);
	}

	@Override
	public List<QdzRateRecord> findQdzRateRecordList(QdzRateRecord qdzRateRecord, int start, int limit) {
		return this.qdzRateRecordDao.findByCondition(qdzRateRecord, start, limit);
	}

	@Override
	public Pager findQdzRateRecordList(QdzRateRecord qdzRateRecord, Pager pager) {
		return this.qdzRateRecordDao.findByCondition(qdzRateRecord, pager);
	}

	@Override
	public int findQdzRateRecordCount(QdzRateRecord qdzRateRecord) {
		return this.qdzRateRecordDao.getTotalCount(qdzRateRecord);
	}

	@Override
	public QdzRateRecord findQdzRateRecordByTime(Date createTime) {
		return this.qdzRateRecordDao.findQdzRateRecordByTime(createTime);
	}

	@Override
	public QdzRateRecord getQdzRateRecord(Date day) {
		return this.qdzRateRecordDao.getQdzRateRecord(day);
	}
}
