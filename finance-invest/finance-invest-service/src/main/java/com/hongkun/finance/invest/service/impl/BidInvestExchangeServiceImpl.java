package com.hongkun.finance.invest.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.hongkun.finance.invest.dao.BidInfoDao;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.invest.model.BidInvestExchange;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.invest.dao.BidInvestExchangeDao;
import com.hongkun.finance.invest.service.BidInvestExchangeService;

/**
 * @Project       : finance_hkjf
 * @Program Name  : com.hongkun.finance.invest.service.impl.BidInvestExchangeServiceImpl.java
 * @Class Name    : BidInvestExchangeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class BidInvestExchangeServiceImpl implements BidInvestExchangeService {

	private static final Logger logger = LoggerFactory.getLogger(BidInvestExchangeServiceImpl.class);

	@Autowired
	private  BidInvestExchangeDao bidInvestExchangeDao;

	@Autowired
	private BidInfoDao bidInfoDao;

	@Override
	public Pager findExchangeBidList(String bidName, Pager pager) {
		return bidInfoDao.findExchangeBidList(bidName,pager);
	}

	@Override
	public List<BidInvestExchange> findInvestWating() {
		return bidInvestExchangeDao.findInvestWating();
	}

	@Override
	public List<BidInvestExchange>  findExchangeInvestListByBidId(Integer bidId) {
		BidInvestExchange bie = new BidInvestExchange();
		bie.setBidId(bidId);
		return bidInvestExchangeDao.findByCondition(bie);
	}

	@Override
	public List<BidInvestExchange> findInvestSubWating(BigDecimal subWatingMoney) {
		return bidInvestExchangeDao.findInvestSubWating(subWatingMoney);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void addBatch(List<BidInvestExchange> investLists) {
		bidInvestExchangeDao.insertBatch(BidInvestExchange.class,investLists,50);
	}

	@Override
	public Pager investExchangeListForPager(Integer bidId, Pager pager) {
		return bidInvestExchangeDao.investExchangeListForPager(bidId,pager);
	}
}
