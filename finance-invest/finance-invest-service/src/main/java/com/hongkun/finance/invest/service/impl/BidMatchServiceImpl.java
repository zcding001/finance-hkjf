package com.hongkun.finance.invest.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.vo.BidMatchVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.dao.BidMatchDao;
import com.hongkun.finance.invest.service.BidMatchService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.impl.BidMatchServiceImpl.java
 * @Class Name    : BidMatchServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidMatchServiceImpl implements BidMatchService {

	private static final Logger logger = LoggerFactory.getLogger(BidMatchServiceImpl.class);
	
	/**
	 * BidMatchDAO
	 */
	@Autowired
	private BidMatchDao bidMatchDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidMatch(BidMatch bidMatch) {
		this.bidMatchDao.save(bidMatch);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidMatchBatch(List<BidMatch> list) {
		this.bidMatchDao.insertBatch(BidMatch.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidMatchBatch(List<BidMatch> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.bidMatchDao.insertBatch(BidMatch.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidMatch(BidMatch bidMatch) {
		this.bidMatchDao.update(bidMatch);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidMatchBatch(List<BidMatch> list, int count) {
		this.bidMatchDao.updateBatch(BidMatch.class, list, count);
	}
	
	@Override
	public BidMatch findBidMatchById(int id) {
		return this.bidMatchDao.findByPK(Long.valueOf(id), BidMatch.class);
	}
	
	@Override
	public List<BidMatch> findBidMatchList(BidMatch bidMatch) {
		return this.bidMatchDao.findByCondition(bidMatch);
	}
	
	@Override
	public List<BidMatch> findBidMatchList(BidMatch bidMatch, int start, int limit) {
		return this.bidMatchDao.findByCondition(bidMatch, start, limit);
	}
	
	@Override
	public Pager findBidMatchList(BidMatch bidMatch, Pager pager) {
		return this.bidMatchDao.findByCondition(bidMatch, pager);
	}
	
	@Override
	public int findBidMatchCount(BidMatch bidMatch){
		return this.bidMatchDao.getTotalCount(bidMatch);
	}

	@Override
	public List<BidMatch> findBidMatchListByCommonBidId(Integer bidInfoId) {
		BidMatch bidMatch = new BidMatch();
		bidMatch.setComnBidId(bidInfoId);
		bidMatch.setState(InvestConstants.MATCH_STATE_SUCCESS);
		return findBidMatchList(bidMatch);
	}

	@Override
	public List<BidMatchVo> findMatchListByContidion(BidMatchVo contidion) {
		return this.bidMatchDao.findMatchListByContidion(contidion);
	}

	@Override
	public Pager findMatchVoListByContidion(BidMatchVo contidion,Pager pager) {
		if(StringUtils.isNotBlank(contidion.getCreateTimeBegin())){
			contidion.setCreateTimeBegin(contidion.getCreateTimeBegin()+ " 00:00:00");
		}
		if (StringUtils.isNotBlank(contidion.getCreateTimeEnd())){
			contidion.setCreateTimeEnd(contidion.getCreateTimeEnd()+" 23:59:59");
		}
		return this.bidMatchDao.findMatchVoListByContidion(contidion, pager);
	}

	@Override
	public List<BidMatch> findBidMatchListByGoodIds(List<Integer> goodIds) {
		BidMatch bidMatch = new BidMatch();
		bidMatch.setGoodIds(goodIds);
		bidMatch.setState(InvestConstants.MATCH_STATE_SUCCESS);
		return findBidMatchList(bidMatch);
	}

	@Override
	public List<BidMatch> findBidMatchListByCommonBidIds(List<Integer> commonIds) {
		BidMatch bidMatch = new BidMatch();
		bidMatch.setCommIds(commonIds);
		bidMatch.setState(InvestConstants.MATCH_STATE_SUCCESS);
		return findBidMatchList(bidMatch);
	}
}
