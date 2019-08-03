package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.dao.BidAuditTypeDao;
import com.hongkun.finance.invest.model.BidAuditType;
import com.hongkun.finance.invest.service.BidAuditTypeService;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.service.impl.BidAuditTypeServiceImpl.java
 * @Class Name    : BidAuditTypeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidAuditTypeServiceImpl implements BidAuditTypeService {

	private static final Logger logger = LoggerFactory.getLogger(BidAuditTypeServiceImpl.class);
	
	/**
	 * BidAuditTypeDAO
	 */
	@Autowired
	private BidAuditTypeDao bidAuditTypeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidAuditType(BidAuditType bidAuditType) {
		this.bidAuditTypeDao.save(bidAuditType);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidAuditTypeBatch(List<BidAuditType> list) {
		this.bidAuditTypeDao.insertBatch(BidAuditType.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidAuditTypeBatch(List<BidAuditType> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidAuditTypeDao.insertBatch(BidAuditType.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidAuditType(BidAuditType bidAuditType) {
		this.bidAuditTypeDao.update(bidAuditType);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidAuditTypeBatch(List<BidAuditType> list, int count) {
		this.bidAuditTypeDao.updateBatch(BidAuditType.class, list, count);
	}
	
	@Override
	public BidAuditType findBidAuditTypeById(int id) {
		return this.bidAuditTypeDao.findByPK(Long.valueOf(id), BidAuditType.class);
	}
	
	@Override
	public List<BidAuditType> findBidAuditTypeList(BidAuditType bidAuditType) {
		return this.bidAuditTypeDao.findByCondition(bidAuditType);
	}
	
	@Override
	public List<BidAuditType> findBidAuditTypeList(BidAuditType bidAuditType, int start, int limit) {
		return this.bidAuditTypeDao.findByCondition(bidAuditType, start, limit);
	}
	
	@Override
	public Pager findBidAuditTypeList(BidAuditType bidAuditType, Pager pager) {
		return this.bidAuditTypeDao.findByCondition(bidAuditType, pager);
	}
	
	@Override
	public int findBidAuditTypeCount(BidAuditType bidAuditType){
		return this.bidAuditTypeDao.getTotalCount(bidAuditType);
	}
	
	@Override
	public Pager findBidAuditTypeList(BidAuditType bidAuditType, Pager pager, String sqlName){
		return this.bidAuditTypeDao.findByCondition(bidAuditType, pager, sqlName);
	}
	
	@Override
	public Integer findBidAuditTypeCount(BidAuditType bidAuditType, String sqlName){
		 return this.bidAuditTypeDao.getTotalCount(bidAuditType, sqlName);
	}

	@Override
	public List<Map<Integer, String>> findBidAuditTypeIdAndName() {
		return this.bidAuditTypeDao.findBidAuditTypeIdAndName();
	}
}
