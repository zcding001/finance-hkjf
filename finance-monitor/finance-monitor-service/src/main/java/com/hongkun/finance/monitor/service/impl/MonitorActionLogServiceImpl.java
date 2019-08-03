package com.hongkun.finance.monitor.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.monitor.model.MonitorActionLog;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.monitor.dao.MonitorActionLogDao;
import com.hongkun.finance.monitor.service.MonitorActionLogService;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.monitor.service.impl.MonitorActionLogServiceImpl.java
 * @Class Name    : MonitorActionLogServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class MonitorActionLogServiceImpl implements MonitorActionLogService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorActionLogServiceImpl.class);
	
	/**
	 * MonitorActionLogDAO
	 */
	@Autowired
	private MonitorActionLogDao monitorActionLogDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorActionLog(MonitorActionLog monitorActionLog) {
		this.monitorActionLogDao.save(monitorActionLog);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorActionLogBatch(List<MonitorActionLog> list) {
		this.monitorActionLogDao.insertBatch(MonitorActionLog.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertMonitorActionLogBatch(List<MonitorActionLog> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.monitorActionLogDao.insertBatch(MonitorActionLog.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateMonitorActionLog(MonitorActionLog monitorActionLog) {
		this.monitorActionLogDao.update(monitorActionLog);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateMonitorActionLogBatch(List<MonitorActionLog> list, int count) {
		this.monitorActionLogDao.updateBatch(MonitorActionLog.class, list, count);
	}
	
	@Override
	public MonitorActionLog findMonitorActionLogById(int id) {
		return this.monitorActionLogDao.findByPK(Long.valueOf(id), MonitorActionLog.class);
	}
	
	@Override
	public List<MonitorActionLog> findMonitorActionLogList(MonitorActionLog monitorActionLog) {
		return this.monitorActionLogDao.findByCondition(monitorActionLog);
	}
	
	@Override
	public List<MonitorActionLog> findMonitorActionLogList(MonitorActionLog monitorActionLog, int start, int limit) {
		return this.monitorActionLogDao.findByCondition(monitorActionLog, start, limit);
	}
	
	@Override
	public Pager findMonitorActionLogList(MonitorActionLog monitorActionLog, Pager pager) {
		monitorActionLog.setSortColumns("create_time DESC");
		return this.monitorActionLogDao.findByCondition(monitorActionLog, pager);
	}
	
	@Override
	public int findMonitorActionLogCount(MonitorActionLog monitorActionLog){
		return this.monitorActionLogDao.getTotalCount(monitorActionLog);
	}
	
	@Override
	public Pager findMonitorActionLogList(MonitorActionLog monitorActionLog, Pager pager, String sqlName){
		return this.monitorActionLogDao.findByCondition(monitorActionLog, pager, sqlName);
	}
	
	@Override
	public Integer findMonitorActionLogCount(MonitorActionLog monitorActionLog, String sqlName){
		return this.monitorActionLogDao.getTotalCount(monitorActionLog, sqlName);
	}
}
