package com.hongkun.finance.monitor.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.monitor.model.MonitorOperateRecord;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.monitor.dao.MonitorOperateRecordDao;
import com.hongkun.finance.monitor.service.MonitorOperateRecordService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.monitor.service.impl.MonitorOperateRecordServiceImpl.java
 * @Class Name    : MonitorOperateRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class MonitorOperateRecordServiceImpl implements MonitorOperateRecordService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorOperateRecordServiceImpl.class);
	
	/**
	 * MonitorOperateRecordDAO
	 */
	@Autowired
	private MonitorOperateRecordDao monitorOperateRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorOperateRecord(MonitorOperateRecord monitorOperateRecord) {
		this.monitorOperateRecordDao.save(monitorOperateRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorOperateRecordBatch(List<MonitorOperateRecord> list) {
		this.monitorOperateRecordDao.insertBatch(MonitorOperateRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorOperateRecordBatch(List<MonitorOperateRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.monitorOperateRecordDao.insertBatch(MonitorOperateRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateMonitorOperateRecord(MonitorOperateRecord monitorOperateRecord) {
		this.monitorOperateRecordDao.update(monitorOperateRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateMonitorOperateRecordBatch(List<MonitorOperateRecord> list, int count) {
		this.monitorOperateRecordDao.updateBatch(MonitorOperateRecord.class, list, count);
	}
	
	@Override
	public MonitorOperateRecord findMonitorOperateRecordById(int id) {
		return this.monitorOperateRecordDao.findByPK(Long.valueOf(id), MonitorOperateRecord.class);
	}
	
	@Override
	public List<MonitorOperateRecord> findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord) {
		return this.monitorOperateRecordDao.findByCondition(monitorOperateRecord);
	}
	
	@Override
	public List<MonitorOperateRecord> findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord, int start, int limit) {
		return this.monitorOperateRecordDao.findByCondition(monitorOperateRecord, start, limit);
	}
	
	@Override
	public Pager findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord, Pager pager) {
		return this.monitorOperateRecordDao.findByCondition(monitorOperateRecord, pager);
	}
	
	@Override
	public int findMonitorOperateRecordCount(MonitorOperateRecord monitorOperateRecord){
		return this.monitorOperateRecordDao.getTotalCount(monitorOperateRecord);
	}
}
