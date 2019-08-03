package com.hongkun.finance.contract.service.impl;

import java.util.Date;
import java.util.List;

import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.contract.model.CarContract;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.contract.dao.CarContractDao;
import com.hongkun.finance.contract.service.CarContractService;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.impl.CarContractServiceImpl.java
 * @Class Name    : CarContractServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class CarContractServiceImpl implements CarContractService {

	private static final Logger logger = LoggerFactory.getLogger(CarContractServiceImpl.class);
	
	/**
	 * CarContractDAO
	 */
	@Autowired
	private CarContractDao carContractDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarContract(CarContract carContract) {
		carContract.setId(null);
		carContract.setCreateTime(new Date());
		String tempStr =  "CL-JKHT-2018-"+ DateUtils.getCurrentDate("yyyyMMddHHmmssSSS").substring(2, 14);
		carContract.setNo(tempStr);
		String contractTitle=carContract.getPartyBname()+"借款";
		carContract.setTitle(contractTitle);
		this.carContractDao.save(carContract);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarContractBatch(List<CarContract> list) {
		this.carContractDao.insertBatch(CarContract.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarContractBatch(List<CarContract> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.carContractDao.insertBatch(CarContract.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateCarContract(CarContract carContract) {
		carContract.setModifyTime(new Date());
		this.carContractDao.update(carContract);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateCarContractBatch(List<CarContract> list, int count) {
		this.carContractDao.updateBatch(CarContract.class, list, count);
	}
	
	@Override
	public CarContract findCarContractById(int id) {
		return this.carContractDao.findByPK(Long.valueOf(id), CarContract.class);
	}
	
	@Override
	public List<CarContract> findCarContractList(CarContract carContract) {
		carContract.setSortColumns("id DESC");
		return this.carContractDao.findByCondition(carContract);
	}
	
	@Override
	public List<CarContract> findCarContractList(CarContract carContract, int start, int limit) {
		return this.carContractDao.findByCondition(carContract, start, limit);
	}
	
	@Override
	public Pager findCarContractList(CarContract carContract, Pager pager) {
		carContract.setSortColumns("id DESC");
		return this.carContractDao.findByCondition(carContract, pager);
	}
	
	@Override
	public int findCarContractCount(CarContract carContract){
		return this.carContractDao.getTotalCount(carContract);
	}
	
	@Override
	public Pager findCarContractList(CarContract carContract, Pager pager, String sqlName){
		return this.carContractDao.findByCondition(carContract, pager, sqlName);
	}
	
	@Override
	public Integer findCarContractCount(CarContract carContract, String sqlName){
		return this.carContractDao.getTotalCount(carContract, sqlName);
	}
}
