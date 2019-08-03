package com.hongkun.finance.property.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.property.model.ProAddress;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.property.dao.ProAddressDao;
import com.hongkun.finance.property.service.ProAddressService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.service.impl.ProAddressServiceImpl.java
 * @Class Name    : ProAddressServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class ProAddressServiceImpl implements ProAddressService {

	private static final Logger logger = LoggerFactory.getLogger(ProAddressServiceImpl.class);
	
	/**
	 * ProAddressDAO
	 */
	@Autowired
	private ProAddressDao proAddressDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertProAddress(ProAddress proAddress) {
		this.proAddressDao.save(proAddress);
		return proAddress.getId();
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertProAddressBatch(List<ProAddress> list) {
		this.proAddressDao.insertBatch(ProAddress.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertProAddressBatch(List<ProAddress> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.proAddressDao.insertBatch(ProAddress.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProAddress(ProAddress proAddress) {
		this.proAddressDao.update(proAddress);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateProAddressBatch(List<ProAddress> list, int count) {
		this.proAddressDao.updateBatch(ProAddress.class, list, count);
	}
	
	@Override
	public ProAddress findProAddressById(int id) {
		return this.proAddressDao.findByPK(Long.valueOf(id), ProAddress.class);
	}
	
	@Override
	public List<ProAddress> findProAddressList(ProAddress proAddress) {
		return this.proAddressDao.findByCondition(proAddress);
	}
	
	@Override
	public List<ProAddress> findProAddressList(ProAddress proAddress, int start, int limit) {
		return this.proAddressDao.findByCondition(proAddress, start, limit);
	}
	
	@Override
	public Pager findProAddressList(ProAddress proAddress, Pager pager) {
		return this.proAddressDao.findByCondition(proAddress, pager);
	}
	
	@Override
	public int findProAddressCount(ProAddress proAddress){
		return this.proAddressDao.getTotalCount(proAddress);
	}

	@Override
	public int delProAddress(int proAddressId) {
		 return this.proAddressDao.delete(Long.valueOf(proAddressId), ProAddress.class);
	}
}
