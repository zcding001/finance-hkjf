package com.hongkun.finance.user.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.user.dao.RegUserRecommendDao;
import com.hongkun.finance.user.model.RegUserRecommend;
import com.hongkun.finance.user.service.RegUserRecommendService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.RegUserRecommendServiceImpl.
 *          java
 * @Class Name : RegUserRecommendServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class RegUserRecommendServiceImpl implements RegUserRecommendService {

	private static final Logger logger = LoggerFactory.getLogger(RegUserRecommendServiceImpl.class);

	/**
	 * RegUserRecommendDAO
	 */
	@Autowired
	private RegUserRecommendDao regUserRecommendDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserRecommend(RegUserRecommend regUserRecommend) {
		this.regUserRecommendDao.save(regUserRecommend);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserRecommendBatch(List<RegUserRecommend> list) {
		this.regUserRecommendDao.insertBatch(RegUserRecommend.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertRegUserRecommendBatch(List<RegUserRecommend> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.regUserRecommendDao.insertBatch(RegUserRecommend.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserRecommend(RegUserRecommend regUserRecommend) {
		this.regUserRecommendDao.update(regUserRecommend);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateRegUserRecommendBatch(List<RegUserRecommend> list, int count) {
		this.regUserRecommendDao.updateBatch(RegUserRecommend.class, list, count);
	}

	@Override
	public RegUserRecommend findRegUserRecommendById(int id) {
		return this.regUserRecommendDao.findByPK(Long.valueOf(id), RegUserRecommend.class);
	}

	@Override
	public List<RegUserRecommend> findRegUserRecommendList(RegUserRecommend regUserRecommend) {
		return this.regUserRecommendDao.findByCondition(regUserRecommend);
	}

	@Override
	public List<RegUserRecommend> findRegUserRecommendList(RegUserRecommend regUserRecommend, int start, int limit) {
		return this.regUserRecommendDao.findByCondition(regUserRecommend, start, limit);
	}

	@Override
	public Pager findRegUserRecommendList(RegUserRecommend regUserRecommend, Pager pager) {
		return this.regUserRecommendDao.findByCondition(regUserRecommend, pager);
	}

	@Override
	public int findRegUserRecommendCount(RegUserRecommend regUserRecommend) {
		return this.regUserRecommendDao.getTotalCount(regUserRecommend);
	}

	@Override
	public RegUserRecommend findRegUserRecommendByRegUserId(Integer regUserId) {

		return this.regUserRecommendDao.findRegUserRecommendByRegUserId(regUserId);

	}
}
