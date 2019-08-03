package com.hongkun.finance.roster.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.roster.model.RosDepositInfo;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.roster.dao.RosDepositInfoDao;
import com.hongkun.finance.roster.service.RosDepositInfoService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.service.impl.RosDepositInfoServiceImpl.java
 * @Class Name    : RosDepositInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RosDepositInfoServiceImpl implements RosDepositInfoService {

	private static final Logger logger = LoggerFactory.getLogger(RosDepositInfoServiceImpl.class);
	
	/**
	 * RosDepositInfoDAO
	 */
	@Autowired
	private RosDepositInfoDao rosDepositInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertRosDepositInfo(RosDepositInfo rosDepositInfo) {
		return this.rosDepositInfoDao.save(rosDepositInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosDepositInfoBatch(List<RosDepositInfo> list) {
		this.rosDepositInfoDao.insertBatch(RosDepositInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRosDepositInfoBatch(List<RosDepositInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.rosDepositInfoDao.insertBatch(RosDepositInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateRosDepositInfo(RosDepositInfo rosDepositInfo) {
		return this.rosDepositInfoDao.update(rosDepositInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRosDepositInfoBatch(List<RosDepositInfo> list, int count) {
		this.rosDepositInfoDao.updateBatch(RosDepositInfo.class, list, count);
	}
	
	@Override
	public RosDepositInfo findRosDepositInfoById(int id) {
		return this.rosDepositInfoDao.findByPK(Long.valueOf(id), RosDepositInfo.class);
	}
	
	@Override
	public List<RosDepositInfo> findRosDepositInfoList(RosDepositInfo rosDepositInfo) {
		return this.rosDepositInfoDao.findByCondition(rosDepositInfo);
	}
	
	@Override
	public List<RosDepositInfo> findRosDepositInfoList(RosDepositInfo rosDepositInfo, int start, int limit) {
		return this.rosDepositInfoDao.findByCondition(rosDepositInfo, start, limit);
	}
	
	@Override
	public Pager findRosDepositInfoList(RosDepositInfo rosDepositInfo, Pager pager) {
		return this.rosDepositInfoDao.findByCondition(rosDepositInfo, pager);
	}
	
	@Override
	public int findRosDepositInfoCount(RosDepositInfo rosDepositInfo){
		return this.rosDepositInfoDao.getTotalCount(rosDepositInfo);
	}
	
	@Override
	public Pager findRosDepositInfoList(RosDepositInfo rosDepositInfo, Pager pager, String sqlName){
		return this.rosDepositInfoDao.findByCondition(rosDepositInfo, pager, sqlName);
	}
	
	@Override
	public Integer findRosDepositInfoCount(RosDepositInfo rosDepositInfo, String sqlName){
		return this.rosDepositInfoDao.getTotalCount(rosDepositInfo, sqlName);
	}
}
