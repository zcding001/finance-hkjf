package com.hongkun.finance.fund.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.fund.model.HouseProIntroduce;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.fund.dao.HouseProIntroduceDao;
import com.hongkun.finance.fund.service.HouseProIntroduceService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.HouseProIntroduceServiceImpl.java
 * @Class Name    : HouseProIntroduceServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class HouseProIntroduceServiceImpl implements HouseProIntroduceService {

	private static final Logger logger = LoggerFactory.getLogger(HouseProIntroduceServiceImpl.class);
	
	/**
	 * HouseProIntroduceDAO
	 */
	@Autowired
	private HouseProIntroduceDao houseProIntroduceDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProIntroduce(HouseProIntroduce houseProIntroduce) {
		this.houseProIntroduceDao.save(houseProIntroduce);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProIntroduceBatch(List<HouseProIntroduce> list) {
		this.houseProIntroduceDao.insertBatch(HouseProIntroduce.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProIntroduceBatch(List<HouseProIntroduce> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.houseProIntroduceDao.insertBatch(HouseProIntroduce.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProIntroduce(HouseProIntroduce houseProIntroduce) {
		this.houseProIntroduceDao.update(houseProIntroduce);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProIntroduceBatch(List<HouseProIntroduce> list, int count) {
		this.houseProIntroduceDao.updateBatch(HouseProIntroduce.class, list, count);
	}
	
	@Override
	public HouseProIntroduce findHouseProIntroduceById(int id) {
		return this.houseProIntroduceDao.findByPK(Long.valueOf(id), HouseProIntroduce.class);
	}
	
	@Override
	public List<HouseProIntroduce> findHouseProIntroduceList(HouseProIntroduce houseProIntroduce) {
		return this.houseProIntroduceDao.findByCondition(houseProIntroduce);
	}
	
	@Override
	public List<HouseProIntroduce> findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, int start, int limit) {
		return this.houseProIntroduceDao.findByCondition(houseProIntroduce, start, limit);
	}
	
	@Override
	public Pager findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, Pager pager) {
		return this.houseProIntroduceDao.findByCondition(houseProIntroduce, pager);
	}
	
	@Override
	public int findHouseProIntroduceCount(HouseProIntroduce houseProIntroduce){
		return this.houseProIntroduceDao.getTotalCount(houseProIntroduce);
	}
	
	@Override
	public Pager findHouseProIntroduceList(HouseProIntroduce houseProIntroduce, Pager pager, String sqlName){
		return this.houseProIntroduceDao.findByCondition(houseProIntroduce, pager, sqlName);
	}
	
	@Override
	public Integer findHouseProIntroduceCount(HouseProIntroduce houseProIntroduce, String sqlName){
		return this.houseProIntroduceDao.getTotalCount(houseProIntroduce, sqlName);
	}
}
