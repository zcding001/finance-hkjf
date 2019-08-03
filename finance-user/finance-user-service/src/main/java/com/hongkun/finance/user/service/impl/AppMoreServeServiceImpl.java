package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.AppMoreServeDao;
import com.hongkun.finance.user.model.AppMoreServe;
import com.hongkun.finance.user.service.AppMoreServeService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hongkun.finance.user.constants.UserConstants.APP_SERVE_STATE_HIDE;
import static com.hongkun.finance.user.constants.UserConstants.APP_SERVE_STATE_SHOW;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.impl.AppMoreServeServiceImpl.java
 * @Class Name    : AppMoreServeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class AppMoreServeServiceImpl implements AppMoreServeService {

	private static final Logger logger = LoggerFactory.getLogger(AppMoreServeServiceImpl.class);
	
	/**
	 * AppMoreServeDAO
	 */
	@Autowired
	private AppMoreServeDao appMoreServeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppMoreServe(AppMoreServe appMoreServe) {
		this.appMoreServeDao.save(appMoreServe);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppMoreServeBatch(List<AppMoreServe> list) {
		this.appMoreServeDao.insertBatch(AppMoreServe.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertAppMoreServeBatch(List<AppMoreServe> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.appMoreServeDao.insertBatch(AppMoreServe.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppMoreServe(AppMoreServe appMoreServe) {
		this.appMoreServeDao.update(appMoreServe);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAppMoreServeBatch(List<AppMoreServe> list, int count) {
		this.appMoreServeDao.updateBatch(AppMoreServe.class, list, count);
	}
	
	@Override
	public AppMoreServe findAppMoreServeById(int id) {
		return this.appMoreServeDao.findByPK(Long.valueOf(id), AppMoreServe.class);
	}
	
	@Override
	public List<AppMoreServe> findAppMoreServeList(AppMoreServe appMoreServe) {
        appMoreServe.setSortColumns("SEQ");
		return this.appMoreServeDao.findByCondition(appMoreServe);
	}
	
	@Override
	public List<AppMoreServe> findAppMoreServeList(AppMoreServe appMoreServe, int start, int limit) {
		return this.appMoreServeDao.findByCondition(appMoreServe, start, limit);
	}
	
	@Override
	public Pager findAppMoreServeList(AppMoreServe appMoreServe, Pager pager) {
        appMoreServe.setSortColumns("SEQ");
		return this.appMoreServeDao.findByCondition(appMoreServe, pager);
	}
	
	@Override
	public int findAppMoreServeCount(AppMoreServe appMoreServe){
		return this.appMoreServeDao.getTotalCount(appMoreServe);
	}
	
	@Override
	public Pager findAppMoreServeList(AppMoreServe appMoreServe, Pager pager, String sqlName){
		return this.appMoreServeDao.findByCondition(appMoreServe, pager, sqlName);
	}
	
	@Override
	public Integer findAppMoreServeCount(AppMoreServe appMoreServe, String sqlName){
		return this.appMoreServeDao.getTotalCount(appMoreServe, sqlName);
	}
	@Override
	public ResponseEntity enableAppServe(AppMoreServe appMoreServe) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: enableAppMenu, 启用app菜单, 菜单信息: {}",appMoreServe);
		}
		try {
			//只禁用非删除状态的菜单
			if (findAppMoreServeCount(appMoreServe)>0) {
				appMoreServe.setIsShow(APP_SERVE_STATE_SHOW);
				appMoreServeDao.update(appMoreServe);
			}
			return ResponseEntity.SUCCESS;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("启用app菜单失败, 菜单信息: {}\n异常信息: ", appMoreServe, e);
			}
			throw new GeneralException("启用app菜单失败,请重试");
		}
	}

	@Override
	public ResponseEntity disableAppServe(AppMoreServe appMoreServe) {
		if (logger.isInfoEnabled()) {
			logger.info("方法名: disableAppMenu, 禁用app菜单, 菜单信息: {}",appMoreServe);
		}
		try {
			//只禁用非删除状态的菜单
			if (findAppMoreServeCount(appMoreServe)>0) {
				appMoreServe.setIsShow(APP_SERVE_STATE_HIDE);
				appMoreServeDao.update(appMoreServe);
			}
			return ResponseEntity.SUCCESS;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("禁用app菜单失败, 菜单信息: {}\n异常信息: ", appMoreServe, e);
			}
			throw new GeneralException("禁用app菜单失败,请重试");
		}
	}

	public List<AppMoreServe> findOtherServe(List<Integer> appMoreServeIds){
		return appMoreServeDao.findOtherServe(appMoreServeIds);
	}
}
