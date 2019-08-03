package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.AppUserServeDao;
import com.hongkun.finance.user.model.AppUserServe;
import com.hongkun.finance.user.service.AppUserServeService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.service.impl.AppUserServeServiceImpl.java
 * @Class Name    : AppUserServeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class AppUserServeServiceImpl implements AppUserServeService {

	private static final Logger logger = LoggerFactory.getLogger(AppUserServeServiceImpl.class);
	
	/**
	 * AppUserServeDAO
	 */
	@Autowired
	private AppUserServeDao appUserServeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppUserServe(AppUserServe appUserServe) {
		this.appUserServeDao.save(appUserServe);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppUserServeBatch(List<AppUserServe> list) {
		this.appUserServeDao.insertBatch(AppUserServe.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppUserServeBatch(List<AppUserServe> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.appUserServeDao.insertBatch(AppUserServe.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppUserServe(AppUserServe appUserServe) {
		this.appUserServeDao.update(appUserServe);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppUserServeBatch(List<AppUserServe> list, int count) {
		this.appUserServeDao.updateBatch(AppUserServe.class, list, count);
	}
	
	@Override
	public AppUserServe findAppUserServeById(int id) {
		return this.appUserServeDao.findByPK(Long.valueOf(id), AppUserServe.class);
	}
	
	@Override
	public List<AppUserServe> findAppUserServeList(AppUserServe appUserServe) {
		appUserServe.setSortColumns(appUserServe.getSort()+"");
		return this.appUserServeDao.findByCondition(appUserServe);
	}
	
	@Override
	public List<AppUserServe> findAppUserServeList(AppUserServe appUserServe, int start, int limit) {
		return this.appUserServeDao.findByCondition(appUserServe, start, limit);
	}
	
	@Override
	public Pager findAppUserServeList(AppUserServe appUserServe, Pager pager) {
		appUserServe.setSortColumns(appUserServe.getSort()+"");
		return this.appUserServeDao.findByCondition(appUserServe, pager);
	}
	
	@Override
	public int findAppUserServeCount(AppUserServe appUserServe){
		return this.appUserServeDao.getTotalCount(appUserServe);
	}
	
	@Override
	public Pager findAppUserServeList(AppUserServe appUserServe, Pager pager, String sqlName){
		return this.appUserServeDao.findByCondition(appUserServe, pager, sqlName);
	}
	
	@Override
	public Integer findAppUserServeCount(AppUserServe appUserServe, String sqlName){
		return this.appUserServeDao.getTotalCount(appUserServe, sqlName);
	}
}
