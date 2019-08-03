package com.hongkun.finance.activity.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.activity.dao.LotteryActivityDao;
import com.hongkun.finance.activity.dao.LotteryRecordDao;
import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.hongkun.finance.activity.service.LotteryActivityService;
import com.hongkun.finance.activity.service.LotteryRecordService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
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
 * @Program Name  : com.hongkun.finance.activity.service.impl.LotteryActivityServiceImpl.java
 * @Class Name    : LotteryActivityServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

	private static final Logger logger = LoggerFactory.getLogger(LotteryActivityServiceImpl.class);
	
	/**
	 * LotteryActivityDAO
	 */
	@Autowired
	private LotteryActivityDao lotteryActivityDao;

	@Autowired
	private LotteryRecordDao lotteryRecordDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryActivity(LotteryActivity lotteryActivity) {
		this.lotteryActivityDao.save(lotteryActivity);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryActivityBatch(List<LotteryActivity> list) {
		this.lotteryActivityDao.insertBatch(LotteryActivity.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertLotteryActivityBatch(List<LotteryActivity> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.lotteryActivityDao.insertBatch(LotteryActivity.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateLotteryActivity(LotteryActivity lotteryActivity) {
		this.lotteryActivityDao.update(lotteryActivity);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateLotteryActivityBatch(List<LotteryActivity> list, int count) {
		this.lotteryActivityDao.updateBatch(LotteryActivity.class, list, count);
	}
	
	@Override
	public LotteryActivity findLotteryActivityById(int id) {
		return this.lotteryActivityDao.findByPK(Long.valueOf(id), LotteryActivity.class);
	}
	
	@Override
	public List<LotteryActivity> findLotteryActivityList(LotteryActivity lotteryActivity) {
		return this.lotteryActivityDao.findByCondition(lotteryActivity);
	}
	
	@Override
	public List<LotteryActivity> findLotteryActivityList(LotteryActivity lotteryActivity, int start, int limit) {
		return this.lotteryActivityDao.findByCondition(lotteryActivity, start, limit);
	}
	
	@Override
	public Pager findLotteryActivityList(LotteryActivity lotteryActivity, Pager pager) {
		return this.lotteryActivityDao.findByCondition(lotteryActivity, pager);
	}
	
	@Override
	public int findLotteryActivityCount(LotteryActivity lotteryActivity){
		return this.lotteryActivityDao.getTotalCount(lotteryActivity);
	}
	
	@Override
	public Pager findLotteryActivityList(LotteryActivity lotteryActivity, Pager pager, String sqlName){
		return this.lotteryActivityDao.findByCondition(lotteryActivity, pager, sqlName);
	}
	
	@Override
	public Integer findLotteryActivityCount(LotteryActivity lotteryActivity, String sqlName){
		return this.lotteryActivityDao.getTotalCount(lotteryActivity, sqlName);
	}

    @Override
    public Pager findLotteryActivityWithCondition(LotteryActivity lotteryActivity, Pager pager) {
        return lotteryActivityDao.findByCondition(lotteryActivity,pager);
    }

    @Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> saveLotteryActivity(LotteryActivity lotteryActivity) {
		if(logger.isDebugEnabled()){
			logger.info("保存抽奖活动信息{}",lotteryActivity.toString());
		}
		try{
			lotteryActivityDao.save(lotteryActivity);
		}catch (Exception e){
			logger.error("保存抽奖活动失败！ ",e);
			throw new GeneralException("保存活动失败");
		}
        return ResponseEntity.SUCCESS;
    }

    @Override
    public List<LotteryActivity> findLotteryActivityByTel(Long login) {
        return lotteryActivityDao.findLotteryActivityByTel(login);
    }

    @Override
    public Pager findLotteryRecordWithCondition(LotteryRecordVo vo, Pager pager) {
		return lotteryRecordDao.findLotteryRecordWithCondition(vo,pager);
    }
}
