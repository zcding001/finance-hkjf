package com.hongkun.finance.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegUserVipRecordDao;
import com.hongkun.finance.user.model.RegUserVipRecord;
import com.hongkun.finance.user.service.RegUserVipRecordService;
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
 * @Program Name  : com.hongkun.finance.user.service.impl.RegUserVipRecordServiceImpl.java
 * @Class Name    : RegUserVipRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class RegUserVipRecordServiceImpl implements RegUserVipRecordService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserVipRecordServiceImpl.class);
	
	/**
	 * RegUserVipRecordDAO
	 */
	@Autowired
	private RegUserVipRecordDao regUserVipRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserVipRecord(RegUserVipRecord regUserVipRecord) {
		this.regUserVipRecordDao.save(regUserVipRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserVipRecordBatch(List<RegUserVipRecord> list) {
		this.regUserVipRecordDao.insertBatch(RegUserVipRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserVipRecordBatch(List<RegUserVipRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.regUserVipRecordDao.insertBatch(RegUserVipRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserVipRecord(RegUserVipRecord regUserVipRecord) {
		this.regUserVipRecordDao.update(regUserVipRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserVipRecordBatch(List<RegUserVipRecord> list, int count) {
		this.regUserVipRecordDao.updateBatch(RegUserVipRecord.class, list, count);
	}
	
	@Override
	public RegUserVipRecord findRegUserVipRecordById(int id) {
		return this.regUserVipRecordDao.findByPK(Long.valueOf(id), RegUserVipRecord.class);
	}
	
	@Override
	public List<RegUserVipRecord> findRegUserVipRecordList(RegUserVipRecord regUserVipRecord) {
		return this.regUserVipRecordDao.findByCondition(regUserVipRecord);
	}
	
	@Override
	public List<RegUserVipRecord> findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, int start, int limit) {
		return this.regUserVipRecordDao.findByCondition(regUserVipRecord, start, limit);
	}
	
	@Override
	public Pager findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, Pager pager) {
		return this.regUserVipRecordDao.findByCondition(regUserVipRecord, pager);
	}
	
	@Override
	public int findRegUserVipRecordCount(RegUserVipRecord regUserVipRecord){
		return this.regUserVipRecordDao.getTotalCount(regUserVipRecord);
	}
	
	@Override
	public Pager findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, Pager pager, String sqlName){
		return this.regUserVipRecordDao.findByCondition(regUserVipRecord, pager, sqlName);
	}
	
	@Override
	public Integer findRegUserVipRecordCount(RegUserVipRecord regUserVipRecord, String sqlName){
		return this.regUserVipRecordDao.getTotalCount(regUserVipRecord, sqlName);
	}

    @Override
    public List<RegUserVipRecord> findRegUserVipRecordListByRegUserIds(List<Integer> ids) {
        return this.regUserVipRecordDao.findRegUserVipRecordListByRegUserIds(ids);
    }
}
