package com.hongkun.finance.fund.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.fund.model.HouseProDetail;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.fund.dao.HouseProDetailDao;
import com.hongkun.finance.fund.service.HouseProDetailService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.HouseProDetailServiceImpl.java
 * @Class Name    : HouseProDetailServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class HouseProDetailServiceImpl implements HouseProDetailService {

	private static final Logger logger = LoggerFactory.getLogger(HouseProDetailServiceImpl.class);
	
	/**
	 * HouseProDetailDAO
	 */
	@Autowired
	private HouseProDetailDao houseProDetailDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProDetail(HouseProDetail houseProDetail) {
		this.houseProDetailDao.save(houseProDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProDetailBatch(List<HouseProDetail> list) {
		this.houseProDetailDao.insertBatch(HouseProDetail.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProDetailBatch(List<HouseProDetail> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.houseProDetailDao.insertBatch(HouseProDetail.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProDetail(HouseProDetail houseProDetail) {
		this.houseProDetailDao.update(houseProDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProDetailBatch(List<HouseProDetail> list, int count) {
		this.houseProDetailDao.updateBatch(HouseProDetail.class, list, count);
	}
	
	@Override
	public HouseProDetail findHouseProDetailById(int id) {
		return this.houseProDetailDao.findByPK(Long.valueOf(id), HouseProDetail.class);
	}
	
	@Override
	public List<HouseProDetail> findHouseProDetailList(HouseProDetail houseProDetail) {
		return this.houseProDetailDao.findByCondition(houseProDetail);
	}
	
	@Override
	public List<HouseProDetail> findHouseProDetailList(HouseProDetail houseProDetail, int start, int limit) {
		return this.houseProDetailDao.findByCondition(houseProDetail, start, limit);
	}
	
	@Override
	public Pager findHouseProDetailList(HouseProDetail houseProDetail, Pager pager) {
		return this.houseProDetailDao.findByCondition(houseProDetail, pager);
	}
	
	@Override
	public int findHouseProDetailCount(HouseProDetail houseProDetail){
		return this.houseProDetailDao.getTotalCount(houseProDetail);
	}
	
	@Override
	public Pager findHouseProDetailList(HouseProDetail houseProDetail, Pager pager, String sqlName){
		return this.houseProDetailDao.findByCondition(houseProDetail, pager, sqlName);
	}
	
	@Override
	public Integer findHouseProDetailCount(HouseProDetail houseProDetail, String sqlName){
		return this.houseProDetailDao.getTotalCount(houseProDetail, sqlName);
	}
}
