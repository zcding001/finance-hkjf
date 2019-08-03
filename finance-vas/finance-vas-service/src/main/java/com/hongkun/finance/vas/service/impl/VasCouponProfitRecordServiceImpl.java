package com.hongkun.finance.vas.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.dao.VasCouponProfitRecordDao;
import com.hongkun.finance.vas.model.VasCouponProfitRecord;
import com.hongkun.finance.vas.service.VasCouponProfitRecordService;
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
 * @Program Name  : com.yirun.finance.vas.service.impl.VasCouponProfitRecordServiceImpl.java
 * @Class Name    : VasCouponProfitRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasCouponProfitRecordServiceImpl implements VasCouponProfitRecordService {

	private static final Logger logger = LoggerFactory.getLogger(VasCouponProfitRecordServiceImpl.class);
	
	/**
	 * VasCouponProfitRecordDAO
	 */
	@Autowired
	private VasCouponProfitRecordDao vasCouponProfitRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasCouponProfitRecord(VasCouponProfitRecord vasCouponProfitRecord) {
		this.vasCouponProfitRecordDao.save(vasCouponProfitRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list) {
		this.vasCouponProfitRecordDao.insertBatch(VasCouponProfitRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.vasCouponProfitRecordDao.insertBatch(VasCouponProfitRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasCouponProfitRecord(VasCouponProfitRecord vasCouponProfitRecord) {
		this.vasCouponProfitRecordDao.update(vasCouponProfitRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list, int count) {
		this.vasCouponProfitRecordDao.updateBatch(VasCouponProfitRecord.class, list, count);
	}
	
	@Override
	public VasCouponProfitRecord findVasCouponProfitRecordById(int id) {
		return this.vasCouponProfitRecordDao.findByPK(Long.valueOf(id), VasCouponProfitRecord.class);
	}
	
	@Override
	public List<VasCouponProfitRecord> findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord) {
		return this.vasCouponProfitRecordDao.findByCondition(vasCouponProfitRecord);
	}
	
	@Override
	public List<VasCouponProfitRecord> findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, int start, int limit) {
		return this.vasCouponProfitRecordDao.findByCondition(vasCouponProfitRecord, start, limit);
	}
	
	@Override
	public Pager findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, Pager pager) {
		return this.vasCouponProfitRecordDao.findByCondition(vasCouponProfitRecord, pager);
	}
	
	@Override
	public int findVasCouponProfitRecordCount(VasCouponProfitRecord vasCouponProfitRecord){
		return this.vasCouponProfitRecordDao.getTotalCount(vasCouponProfitRecord);
	}
	
	@Override
	public Pager findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, Pager pager, String sqlName){
		return this.vasCouponProfitRecordDao.findByCondition(vasCouponProfitRecord, pager, sqlName);
	}
	
	@Override
	public Integer findVasCouponProfitRecordCount(VasCouponProfitRecord vasCouponProfitRecord, String sqlName){
		return this.vasCouponProfitRecordDao.getTotalCount(vasCouponProfitRecord, sqlName);
	}
}
