package com.hongkun.finance.activity.service.impl;

import java.util.*;

import com.hongkun.finance.activity.dao.LotteryItemDao;
import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.hongkun.finance.activity.service.LotteryItemService;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.activity.model.LotteryRecord;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.activity.dao.LotteryRecordDao;
import com.hongkun.finance.activity.service.LotteryRecordService;
import org.springframework.util.CollectionUtils;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.service.impl.LotteryRecordServiceImpl.java
 * @Class Name    : LotteryRecordServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class LotteryRecordServiceImpl implements LotteryRecordService {

	private static final Logger logger = LoggerFactory.getLogger(LotteryRecordServiceImpl.class);
	
	/**
	 * LotteryRecordDAO
	 */
	@Autowired
	private LotteryRecordDao lotteryRecordDao;

	@Autowired
	private LotteryItemDao lotteryItemDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertLotteryRecord(LotteryRecord lotteryRecord) {
		return this.lotteryRecordDao.save(lotteryRecord);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryRecordBatch(List<LotteryRecord> list) {
		this.lotteryRecordDao.insertBatch(LotteryRecord.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryRecordBatch(List<LotteryRecord> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.lotteryRecordDao.insertBatch(LotteryRecord.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateLotteryRecord(LotteryRecord lotteryRecord) {
		return this.lotteryRecordDao.update(lotteryRecord);
	}

	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateLotteryRecordBatch(List<LotteryRecord> list, int count) {
		this.lotteryRecordDao.updateBatch(LotteryRecord.class, list, count);
	}
	
	@Override
	public LotteryRecord findLotteryRecordById(int id) {
		return this.lotteryRecordDao.findByPK(Long.valueOf(id), LotteryRecord.class);
	}
	
	@Override
	public List<LotteryRecord> findLotteryRecordList(LotteryRecord lotteryRecord) {
		return this.lotteryRecordDao.findByCondition(lotteryRecord);
	}
	
	@Override
	public List<LotteryRecord> findLotteryRecordList(LotteryRecord lotteryRecord, int start, int limit) {
		return this.lotteryRecordDao.findByCondition(lotteryRecord, start, limit);
	}
	
	@Override
	public Pager findLotteryRecordList(LotteryRecord lotteryRecord, Pager pager) {
		return this.lotteryRecordDao.findByCondition(lotteryRecord, pager);
	}
	
	@Override
	public int findLotteryRecordCount(LotteryRecord lotteryRecord){
		return this.lotteryRecordDao.getTotalCount(lotteryRecord);
	}
	
	@Override
	public Pager findLotteryRecordList(LotteryRecord lotteryRecord, Pager pager, String sqlName){
		return this.lotteryRecordDao.findByCondition(lotteryRecord, pager, sqlName);
	}
	
	@Override
	public Integer findLotteryRecordCount(LotteryRecord lotteryRecord, String sqlName){
		return this.lotteryRecordDao.getTotalCount(lotteryRecord, sqlName);
	}

    @Override
    public List<String> getLotteryRecordListForShow(LotteryActivity lotteryActivity) {
		List<String> showAwards = new ArrayList<>();
		if (lotteryActivity == null) {
			return showAwards;
		}
		LotteryRecord lotteryRecord = new LotteryRecord();
		lotteryRecord.setLotteryActivityId(lotteryActivity.getId());
		List<LotteryRecord> list = lotteryRecordDao.getRandomList(lotteryRecord);
		if(!CollectionUtils.isEmpty(list)){
			for (LotteryRecord record : list) {
				String tel = record.getTel().toString();
				LotteryItem LotteryItem = lotteryItemDao.findByPK(Long.valueOf(record.getLotteryItemId()), LotteryItem.class);
				String awardInfo = "";
				StringBuffer strBuf = new StringBuffer();
				strBuf.append(tel.substring(0, 3)).append("******").append(tel.substring(9)).append("抽中了")
						.append(LotteryItem.getItemName());
				awardInfo = strBuf.toString();
				showAwards.add(awardInfo);
			}
		}
		return showAwards;
    }

	@Override
	public Map<String, Integer> getDayAndTotalCount(LotteryRecord record) {
		Map<String,Integer> resultMap = new HashMap<>();
		int totalCount = lotteryRecordDao.getDayAndTotalCount(record);
		record.setCreateTimeBegin(DateUtils.getFirstTimeOfDay());
		record.setCreateTimeEnd(DateUtils.getLastTimeOfDay());
		int dayCount = lotteryRecordDao.getDayAndTotalCount(record);
		resultMap.put("totalCount",totalCount);
		resultMap.put("dayCount",dayCount);
		return resultMap;
	}

	@Override
	public int checkVerfication(String verfication) {
		LotteryRecord lotteryRecord = new LotteryRecord();
		lotteryRecord.setVerfication(verfication);
		List<LotteryRecord> list = lotteryRecordDao.findByCondition(lotteryRecord);
		return CollectionUtils.isEmpty(list) ? 0 : 1;
	}

	@Override
	public List<LotteryRecordVo> findLotteryRecordDetailList(LotteryRecord lotteryRecord) {
		return lotteryRecordDao.findLotteryRecordDetailList(lotteryRecord);
	}
}
