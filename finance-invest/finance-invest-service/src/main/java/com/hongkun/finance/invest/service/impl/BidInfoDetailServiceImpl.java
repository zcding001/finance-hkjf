package com.hongkun.finance.invest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.dao.BidInfoDetailDao;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.service.BidInfoDetailService;
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
 * @Program Name  : com.hongkun.finance.user.service.impl.BidInfoDetailServiceImpl.java
 * @Class Name    : BidInfoDetailServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidInfoDetailServiceImpl implements BidInfoDetailService {

	private static final Logger logger = LoggerFactory.getLogger(BidInfoDetailServiceImpl.class);
	
	/**
	 * BidInfoDetailDAO
	 */
	@Autowired
	private BidInfoDetailDao bidInfoDetailDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertBidInfoDetail(BidInfoDetail bidInfoDetail) {
		return this.bidInfoDetailDao.save(bidInfoDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidInfoDetailBatch(List<BidInfoDetail> list) {
		this.bidInfoDetailDao.insertBatch(BidInfoDetail.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidInfoDetailBatch(List<BidInfoDetail> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidInfoDetailDao.insertBatch(BidInfoDetail.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer updateBidInfoDetail(BidInfoDetail bidInfoDetail) {
		return this.bidInfoDetailDao.update(bidInfoDetail);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidInfoDetailBatch(List<BidInfoDetail> list, int count) {
		this.bidInfoDetailDao.updateBatch(BidInfoDetail.class, list, count);
	}
	
	@Override
	public BidInfoDetail findBidInfoDetailById(int id) {
		return this.bidInfoDetailDao.findByPK(Long.valueOf(id), BidInfoDetail.class);
	}
	
	@Override
	public List<BidInfoDetail> findBidInfoDetailList(BidInfoDetail bidInfoDetail) {
		return this.bidInfoDetailDao.findByCondition(bidInfoDetail);
	}
	
	@Override
	public List<BidInfoDetail> findBidInfoDetailList(BidInfoDetail bidInfoDetail, int start, int limit) {
		return this.bidInfoDetailDao.findByCondition(bidInfoDetail, start, limit);
	}
	
	@Override
	public Pager findBidInfoDetailList(BidInfoDetail bidInfoDetail, Pager pager) {
		return this.bidInfoDetailDao.findByCondition(bidInfoDetail, pager);
	}
	
	@Override
	public int findBidInfoDetailCount(BidInfoDetail bidInfoDetail){
		return this.bidInfoDetailDao.getTotalCount(bidInfoDetail);
	}

	@Override
	public BidInfoDetail findBidInfoDetailByBidId(int bidId) {
		BidInfoDetail bidInfoDetail = new BidInfoDetail();
		bidInfoDetail.setBiddInfoId(bidId);
		List<BidInfoDetail> resultList = this.bidInfoDetailDao.findByCondition(bidInfoDetail);
		if(resultList!=null&&resultList.size()>0){
			return resultList.get(0);
		}
		return null;
	}
}
