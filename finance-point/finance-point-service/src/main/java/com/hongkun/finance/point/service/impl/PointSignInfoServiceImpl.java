package com.hongkun.finance.point.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.point.model.PointSignInfo;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.point.dao.PointSignInfoDao;
import com.hongkun.finance.point.service.PointSignInfoService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointSignInfoServiceImpl.java
 * @Class Name    : PointSignInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointSignInfoServiceImpl implements PointSignInfoService {

	private static final Logger logger = LoggerFactory.getLogger(PointSignInfoServiceImpl.class);
	
	/**
	 * PointSignInfoDAO
	 */
	@Autowired
	private PointSignInfoDao pointSignInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignInfo(PointSignInfo pointSignInfo) {
		this.pointSignInfoDao.save(pointSignInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignInfoBatch(List<PointSignInfo> list) {
		this.pointSignInfoDao.insertBatch(PointSignInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointSignInfoBatch(List<PointSignInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.pointSignInfoDao.insertBatch(PointSignInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointSignInfo(PointSignInfo pointSignInfo) {
		this.pointSignInfoDao.update(pointSignInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointSignInfoBatch(List<PointSignInfo> list, int count) {
		this.pointSignInfoDao.updateBatch(PointSignInfo.class, list, count);
	}
	
	@Override
	public PointSignInfo findPointSignInfoById(int id) {
		return this.pointSignInfoDao.findByPK(Long.valueOf(id), PointSignInfo.class);
	}
	
	@Override
	public List<PointSignInfo> findPointSignInfoList(PointSignInfo pointSignInfo) {
		return this.pointSignInfoDao.findByCondition(pointSignInfo);
	}
	
	@Override
	public List<PointSignInfo> findPointSignInfoList(PointSignInfo pointSignInfo, int start, int limit) {
		return this.pointSignInfoDao.findByCondition(pointSignInfo, start, limit);
	}
	
	@Override
	public Pager findPointSignInfoList(PointSignInfo pointSignInfo, Pager pager) {
		return this.pointSignInfoDao.findByCondition(pointSignInfo, pager);
	}
	
	@Override
	public int findPointSignInfoCount(PointSignInfo pointSignInfo){
		return this.pointSignInfoDao.getTotalCount(pointSignInfo);
	}
}
