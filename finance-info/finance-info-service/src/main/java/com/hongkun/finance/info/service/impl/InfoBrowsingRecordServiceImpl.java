package com.hongkun.finance.info.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.info.model.InfoBrowsingRecord;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.info.dao.InfoBrowsingRecordDao;
import com.hongkun.finance.info.enums.InfomationNewsEnum;
import com.hongkun.finance.info.service.InfoBrowsingRecordService;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.impl.InfoBrowsingRecordServiceImpl.
 *          java
 * @Class Name : InfoBrowsingRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoBrowsingRecordServiceImpl implements InfoBrowsingRecordService {

	private static final Logger logger = LoggerFactory.getLogger(InfoBrowsingRecordServiceImpl.class);

	/**
	 * InfoBrowsingRecordDAO
	 */
	@Autowired
	private InfoBrowsingRecordDao infoBrowsingRecordDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertInfoBrowsingRecord(InfoBrowsingRecord infoBrowsingRecord) {
		return this.infoBrowsingRecordDao.save(infoBrowsingRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list) {
		this.infoBrowsingRecordDao.insertBatch(InfoBrowsingRecord.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoBrowsingRecordDao.insertBatch(InfoBrowsingRecord.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateInfoBrowsingRecord(InfoBrowsingRecord infoBrowsingRecord) {
		return this.infoBrowsingRecordDao.update(infoBrowsingRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoBrowsingRecordBatch(List<InfoBrowsingRecord> list, int count) {
		this.infoBrowsingRecordDao.updateBatch(InfoBrowsingRecord.class, list, count);
	}

	@Override
	public InfoBrowsingRecord findInfoBrowsingRecordById(int id) {
		return this.infoBrowsingRecordDao.findByPK(Long.valueOf(id), InfoBrowsingRecord.class);
	}

	@Override
	public List<InfoBrowsingRecord> findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord) {
		return this.infoBrowsingRecordDao.findByCondition(infoBrowsingRecord);
	}

	@Override
	public List<InfoBrowsingRecord> findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord, int start,
			int limit) {
		return this.infoBrowsingRecordDao.findByCondition(infoBrowsingRecord, start, limit);
	}

	@Override
	public Pager findInfoBrowsingRecordList(InfoBrowsingRecord infoBrowsingRecord, Pager pager) {
		return this.infoBrowsingRecordDao.findByCondition(infoBrowsingRecord, pager);
	}

	@Override
	public int findInfoBrowsingRecordCount(InfoBrowsingRecord infoBrowsingRecord) {
		return this.infoBrowsingRecordDao.getTotalCount(infoBrowsingRecord);
	}

	@Override
	public int findInfoBrowsingRecordCount(int type, int infomationsNewsId) {
		InfoBrowsingRecord infoBrowsingRecord = new InfoBrowsingRecord();
		infoBrowsingRecord.setType(type);
		infoBrowsingRecord.setInfoInformationNewsId(infomationsNewsId);
		return this.infoBrowsingRecordDao.getTotalCount(infoBrowsingRecord);
	}

	@Override
	public int findInfoBrowsingRecordCount(int type, int infomationsNewsId, int regUserId) {
		InfoBrowsingRecord infoBrowsingRecord = new InfoBrowsingRecord();
		infoBrowsingRecord.setType(type);
		infoBrowsingRecord.setInfoInformationNewsId(infomationsNewsId);
		infoBrowsingRecord.setRegUserId(regUserId);
		return this.infoBrowsingRecordDao.getTotalCount(infoBrowsingRecord);
	}
}
