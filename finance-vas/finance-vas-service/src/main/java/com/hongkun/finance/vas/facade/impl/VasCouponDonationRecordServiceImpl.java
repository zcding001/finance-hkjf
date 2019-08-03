package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.vas.dao.VasCouponDonationRecordDao;
import com.hongkun.finance.vas.service.VasCouponDonationRecordService;
import com.hongkun.finance.vas.model.VasCouponDonationRecord;
import com.yirun.framework.core.exception.GeneralException;
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
 * @Program Name  : com.hongkun.finance.loan.service.impl.VasCouponDonationRecordServiceImpl.java
 * @Class Name    : VasCouponDonationRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class VasCouponDonationRecordServiceImpl implements VasCouponDonationRecordService {

	private static final Logger logger = LoggerFactory.getLogger(VasCouponDonationRecordServiceImpl.class);
	
	/**
	 * VasCouponDonationRecordDAO
	 */
	@Autowired
	private VasCouponDonationRecordDao vasCouponDonationRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertVasCouponDonationRecord(VasCouponDonationRecord vasCouponDonationRecord) {
		try{
			return this.vasCouponDonationRecordDao.save(vasCouponDonationRecord);
		}catch (Exception e){
			logger.error("insertVasCouponDonationRecord, 插入卡券转赠记录异常, 转赠记录信息: {}", vasCouponDonationRecord.toString());
			throw new GeneralException("插入卡券转赠记录异常！");
		}
	}

	@Override
	public VasCouponDonationRecord findVasCouponDonationRecordById(int id) {
		return this.vasCouponDonationRecordDao.findByPK(Long.valueOf(id), VasCouponDonationRecord.class);
	}
	
	@Override
	public List<VasCouponDonationRecord> findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord) {
		return this.vasCouponDonationRecordDao.findByCondition(vasCouponDonationRecord);
	}
	
	@Override
	public List<VasCouponDonationRecord> findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord, int start, int limit) {
		return this.vasCouponDonationRecordDao.findByCondition(vasCouponDonationRecord, start, limit);
	}
	
	@Override
	public Pager findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord, Pager pager) {
		return this.vasCouponDonationRecordDao.findByCondition(vasCouponDonationRecord, pager);
	}
	
	@Override
	public int findVasCouponDonationRecordCount(VasCouponDonationRecord vasCouponDonationRecord){
		return this.vasCouponDonationRecordDao.getTotalCount(vasCouponDonationRecord);
	}

}
