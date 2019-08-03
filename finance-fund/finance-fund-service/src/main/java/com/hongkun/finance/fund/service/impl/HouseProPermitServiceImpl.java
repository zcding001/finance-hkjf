package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.dao.HouseProPermitDao;
import com.hongkun.finance.fund.model.HouseProPermit;
import com.hongkun.finance.fund.service.HouseProPermitService;
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
 * @Program Name  : com.hongkun.finance.fund.service.impl.HouseProPermitServiceImpl.java
 * @Class Name    : HouseProPermitServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class HouseProPermitServiceImpl implements HouseProPermitService {

	private static final Logger logger = LoggerFactory.getLogger(HouseProPermitServiceImpl.class);
	
	/**
	 * HouseProPermitDAO
	 */
	@Autowired
	private HouseProPermitDao houseProPermitDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer insertHouseProPermit(HouseProPermit houseProPermit) {
		this.houseProPermitDao.save(houseProPermit);
		return houseProPermit.getId();
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProPermitBatch(List<HouseProPermit> list) {
		this.houseProPermitDao.insertBatch(HouseProPermit.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProPermitBatch(List<HouseProPermit> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.houseProPermitDao.insertBatch(HouseProPermit.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProPermit(HouseProPermit houseProPermit) {
		this.houseProPermitDao.update(houseProPermit);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProPermitBatch(List<HouseProPermit> list, int count) {
		this.houseProPermitDao.updateBatch(HouseProPermit.class, list, count);
	}
	
	@Override
	public HouseProPermit findHouseProPermitById(int id) {
		return this.houseProPermitDao.findByPK(Long.valueOf(id), HouseProPermit.class);
	}
	
	@Override
	public List<HouseProPermit> findHouseProPermitList(HouseProPermit houseProPermit) {
		return this.houseProPermitDao.findByCondition(houseProPermit);
	}
	
	@Override
	public List<HouseProPermit> findHouseProPermitList(HouseProPermit houseProPermit, int start, int limit) {
		return this.houseProPermitDao.findByCondition(houseProPermit, start, limit);
	}
	
	@Override
	public Pager findHouseProPermitList(HouseProPermit houseProPermit, Pager pager) {
		return this.houseProPermitDao.findByCondition(houseProPermit, pager);
	}
	
	@Override
	public int findHouseProPermitCount(HouseProPermit houseProPermit){
		return this.houseProPermitDao.getTotalCount(houseProPermit);
	}
	
	@Override
	public Pager findHouseProPermitList(HouseProPermit houseProPermit, Pager pager, String sqlName){
		return this.houseProPermitDao.findByCondition(houseProPermit, pager, sqlName);
	}
	
	@Override
	public Integer findHouseProPermitCount(HouseProPermit houseProPermit, String sqlName){
		return this.houseProPermitDao.getTotalCount(houseProPermit, sqlName);
	}

	@Override
	public Integer deleteHousePermit(int id) {
		return houseProPermitDao.delete(Long.valueOf(id),HouseProPermit.class);
	}
}
