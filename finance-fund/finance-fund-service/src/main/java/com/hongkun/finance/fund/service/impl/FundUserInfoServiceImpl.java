package com.hongkun.finance.fund.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.fund.model.FundUserInfo;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.fund.dao.FundUserInfoDao;
import com.hongkun.finance.fund.service.FundUserInfoService;
import org.springframework.util.CollectionUtils;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.FundUserInfoServiceImpl.java
 * @Class Name    : FundUserInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class FundUserInfoServiceImpl implements FundUserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(FundUserInfoServiceImpl.class);
	
	/**
	 * FundUserInfoDAO
	 */
	@Autowired
	private FundUserInfoDao fundUserInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundUserInfo(FundUserInfo fundUserInfo) {
		this.fundUserInfoDao.save(fundUserInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundUserInfoBatch(List<FundUserInfo> list) {
		this.fundUserInfoDao.insertBatch(FundUserInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundUserInfoBatch(List<FundUserInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.fundUserInfoDao.insertBatch(FundUserInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundUserInfo(FundUserInfo fundUserInfo) {
		this.fundUserInfoDao.update(fundUserInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundUserInfoBatch(List<FundUserInfo> list, int count) {
		this.fundUserInfoDao.updateBatch(FundUserInfo.class, list, count);
	}
	
	@Override
	public FundUserInfo findFundUserInfoById(int id) {
		return this.fundUserInfoDao.findByPK(Long.valueOf(id), FundUserInfo.class);
	}
	
	@Override
	public List<FundUserInfo> findFundUserInfoList(FundUserInfo fundUserInfo) {
		return this.fundUserInfoDao.findByCondition(fundUserInfo);
	}
	
	@Override
	public List<FundUserInfo> findFundUserInfoList(FundUserInfo fundUserInfo, int start, int limit) {
		return this.fundUserInfoDao.findByCondition(fundUserInfo, start, limit);
	}
	
	@Override
	public Pager findFundUserInfoList(FundUserInfo fundUserInfo, Pager pager) {
		return this.fundUserInfoDao.findByCondition(fundUserInfo, pager);
	}
	
	@Override
	public int findFundUserInfoCount(FundUserInfo fundUserInfo){
		return this.fundUserInfoDao.getTotalCount(fundUserInfo);
	}
	
	@Override
	public Pager findFundUserInfoList(FundUserInfo fundUserInfo, Pager pager, String sqlName){
		return this.fundUserInfoDao.findByCondition(fundUserInfo, pager, sqlName);
	}
	
	@Override
	public Integer findFundUserInfoCount(FundUserInfo fundUserInfo, String sqlName){
		return this.fundUserInfoDao.getTotalCount(fundUserInfo, sqlName);
	}

    @Override
    public FundUserInfo findFundUserInfo(FundUserInfo fundUserInfo) {
		List<FundUserInfo> list = this.fundUserInfoDao.findByCondition(fundUserInfo);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
    }
}
