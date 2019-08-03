package com.hongkun.finance.fund.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.fund.dao.HouseProPicDao;
import com.hongkun.finance.fund.model.HouseProPic;
import com.hongkun.finance.fund.service.HouseProPicService;
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
 * @Program Name  : com.hongkun.finance.fund.service.impl.HouseProPicServiceImpl.java
 * @Class Name    : HouseProPicServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class HouseProPicServiceImpl implements HouseProPicService {

	private static final Logger logger = LoggerFactory.getLogger(HouseProPicServiceImpl.class);
	
	/**
	 * HouseProPicDAO
	 */
	@Autowired
	private HouseProPicDao houseProPicDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer insertHouseProPic(HouseProPic houseProPic) {
		this.houseProPicDao.save(houseProPic);
		return houseProPic.getId();
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProPicBatch(List<HouseProPic> list) {
		this.houseProPicDao.insertBatch(HouseProPic.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertHouseProPicBatch(List<HouseProPic> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.houseProPicDao.insertBatch(HouseProPic.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProPic(HouseProPic houseProPic) {
		this.houseProPicDao.update(houseProPic);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateHouseProPicBatch(List<HouseProPic> list, int count) {
		this.houseProPicDao.updateBatch(HouseProPic.class, list, count);
	}
	
	@Override
	public HouseProPic findHouseProPicById(int id) {
		return this.houseProPicDao.findByPK(Long.valueOf(id), HouseProPic.class);
	}
	
	@Override
	public List<HouseProPic> findHouseProPicList(HouseProPic houseProPic) {
		return this.houseProPicDao.findByCondition(houseProPic);
	}
	
	@Override
	public List<HouseProPic> findHouseProPicList(HouseProPic houseProPic, int start, int limit) {
		return this.houseProPicDao.findByCondition(houseProPic, start, limit);
	}
	
	@Override
	public Pager findHouseProPicList(HouseProPic houseProPic, Pager pager) {
		return this.houseProPicDao.findByCondition(houseProPic, pager);
	}
	
	@Override
	public int findHouseProPicCount(HouseProPic houseProPic){
		return this.houseProPicDao.getTotalCount(houseProPic);
	}
	
	@Override
	public Pager findHouseProPicList(HouseProPic houseProPic, Pager pager, String sqlName){
		return this.houseProPicDao.findByCondition(houseProPic, pager, sqlName);
	}
	
	@Override
	public Integer findHouseProPicCount(HouseProPic houseProPic, String sqlName){
		return this.houseProPicDao.getTotalCount(houseProPic, sqlName);
	}

	@Override
	public Integer deleteHousePic(int id) {
		return this.houseProPicDao.delete(Long.valueOf(id),HouseProPic.class);
	}
}
