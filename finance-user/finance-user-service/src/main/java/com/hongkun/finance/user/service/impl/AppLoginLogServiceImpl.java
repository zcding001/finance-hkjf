package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.AppLoginLogDao;
import com.hongkun.finance.user.model.AppLoginLog;
import com.hongkun.finance.user.service.AppLoginLogService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.AppLoginLogServiceImpl.java
 * @Class Name    : AppLoginLogServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class AppLoginLogServiceImpl implements AppLoginLogService {

	private static final Logger logger = LoggerFactory.getLogger(AppLoginLogServiceImpl.class);
	
	/**
	 * AppLoginLogDAO
	 */
	@Autowired
	private AppLoginLogDao appLoginLogDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppLoginLog(AppLoginLog appLoginLog) {
		this.appLoginLogDao.save(appLoginLog);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppLoginLogBatch(List<AppLoginLog> list) {
		this.appLoginLogDao.insertBatch(AppLoginLog.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppLoginLogBatch(List<AppLoginLog> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.appLoginLogDao.insertBatch(AppLoginLog.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppLoginLog(AppLoginLog appLoginLog) {
		this.appLoginLogDao.update(appLoginLog);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppLoginLogBatch(List<AppLoginLog> list, int count) {
		this.appLoginLogDao.updateBatch(AppLoginLog.class, list, count);
	}
	
	@Override
	public AppLoginLog findAppLoginLogById(int id) {
		return this.appLoginLogDao.findByPK(Long.valueOf(id), AppLoginLog.class);
	}
	
	@Override
	public List<AppLoginLog> findAppLoginLogList(AppLoginLog appLoginLog) {
		return this.appLoginLogDao.findByCondition(appLoginLog);
	}
	
	@Override
	public List<AppLoginLog> findAppLoginLogList(AppLoginLog appLoginLog, int start, int limit) {
		return this.appLoginLogDao.findByCondition(appLoginLog, start, limit);
	}
	
	@Override
	public Pager findAppLoginLogList(AppLoginLog appLoginLog, Pager pager) {
		return this.appLoginLogDao.findByCondition(appLoginLog, pager);
	}
	
	@Override
	public int findAppLoginLogCount(AppLoginLog appLoginLog){
		return this.appLoginLogDao.getTotalCount(appLoginLog);
	}
	
	@Override
	public Pager findAppLoginLogList(AppLoginLog appLoginLog, Pager pager, String sqlName){
		return this.appLoginLogDao.findByCondition(appLoginLog, pager, sqlName);
	}
	
	@Override
	public Integer findAppLoginLogCount(AppLoginLog appLoginLog, String sqlName){
		return this.appLoginLogDao.getTotalCount(appLoginLog, sqlName);
	}

	@Override
	public void updateAppLoginLogByUserId(AppLoginLog unhandLog) {
		this.appLoginLogDao.updateAppLoginLogByUserId(unhandLog);
	}
}
