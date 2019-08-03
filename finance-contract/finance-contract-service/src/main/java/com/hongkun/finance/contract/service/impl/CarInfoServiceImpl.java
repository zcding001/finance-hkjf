package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.dao.CarInfoDao;
import com.hongkun.finance.contract.model.CarInfo;
import com.hongkun.finance.contract.service.CarInfoService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.impl.CarInfoServiceImpl.java
 * @Class Name    : CarInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class CarInfoServiceImpl implements CarInfoService {

	private static final Logger logger = LoggerFactory.getLogger(CarInfoServiceImpl.class);
	
	/**
	 * CarInfoDAO
	 */
	@Autowired
	private CarInfoDao carInfoDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarInfo(CarInfo carInfo) {
		carInfo.setCreateTime(new Date());
		carInfo.setModifyTime(new Date());
		this.carInfoDao.save(carInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarInfoBatch(List<CarInfo> list) {
		this.carInfoDao.insertBatch(CarInfo.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCarInfoBatch(List<CarInfo> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.carInfoDao.insertBatch(CarInfo.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateCarInfo(CarInfo carInfo) {
		this.carInfoDao.update(carInfo);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateCarInfoBatch(List<CarInfo> list, int count) {
		this.carInfoDao.updateBatch(CarInfo.class, list, count);
	}
	
	@Override
	public CarInfo findCarInfoById(int id) {
		return this.carInfoDao.findByPK(Long.valueOf(id), CarInfo.class);
	}
	
	@Override
	public List<CarInfo> findCarInfoList(CarInfo carInfo) {
		return this.carInfoDao.findByCondition(carInfo);
	}
	
	@Override
	public List<CarInfo> findCarInfoList(CarInfo carInfo, int start, int limit) {
		return this.carInfoDao.findByCondition(carInfo, start, limit);
	}
	
	@Override
	public Pager findCarInfoList(CarInfo carInfo, Pager pager) {
		carInfo.setSortColumns("id DESC");
		return this.carInfoDao.findByCondition(carInfo, pager);
	}
	
	@Override
	public int findCarInfoCount(CarInfo carInfo){
		return this.carInfoDao.getTotalCount(carInfo);
	}
	
	@Override
	public Pager findCarInfoList(CarInfo carInfo, Pager pager, String sqlName){
		return this.carInfoDao.findByCondition(carInfo, pager, sqlName);
	}
	
	@Override
	public Integer findCarInfoCount(CarInfo carInfo, String sqlName){
		return this.carInfoDao.getTotalCount(carInfo, sqlName);
	}

	@Override
	public int deleteCarInfoById(Integer carInfoId){
		return this.carInfoDao.deleteById(carInfoId);
	}
}
