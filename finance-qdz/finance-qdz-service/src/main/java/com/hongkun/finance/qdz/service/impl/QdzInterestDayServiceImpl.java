package com.hongkun.finance.qdz.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.dao.QdzInterestDayDao;
import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.hongkun.finance.qdz.service.QdzInterestDayService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.service.impl.QdzInterestDayServiceImpl.java
 * @Class Name : QdzInterestDayServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class QdzInterestDayServiceImpl implements QdzInterestDayService {

	private static final Logger logger = LoggerFactory.getLogger(QdzInterestDayServiceImpl.class);

	/**
	 * QdzInterestDayDAO
	 */
	@Autowired
	private QdzInterestDayDao qdzInterestDayDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertQdzInterestDay(QdzInterestDay qdzInterestDay) {
		return this.qdzInterestDayDao.save(qdzInterestDay);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzInterestDayBatch(List<QdzInterestDay> list) {
		this.qdzInterestDayDao.insertBatch(QdzInterestDay.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzInterestDayBatch(List<QdzInterestDay> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.qdzInterestDayDao.insertBatch(QdzInterestDay.class, list, count);
	}

	@Override
	public void updateQdzInterestDay(QdzInterestDay qdzInterestDay) {
		this.qdzInterestDayDao.update(qdzInterestDay);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzInterestDayBatch(List<QdzInterestDay> list, int count) {
		this.qdzInterestDayDao.updateBatch(QdzInterestDay.class, list, count);
	}

	@Override
	public QdzInterestDay findQdzInterestDayById(int id) {
		return this.qdzInterestDayDao.findByPK(Long.valueOf(id), QdzInterestDay.class);
	}

	@Override
	public List<QdzInterestDay> findQdzInterestDayList(QdzInterestDay qdzInterestDay) {
		return this.qdzInterestDayDao.findByCondition(qdzInterestDay);
	}

	@Override
	public List<QdzInterestDay> findQdzInterestDayList(QdzInterestDay qdzInterestDay, int start, int limit) {
		return this.qdzInterestDayDao.findByCondition(qdzInterestDay, start, limit);
	}

	@Override
	public Pager findQdzInterestDayList(QdzInterestDay qdzInterestDay, Pager pager) {
		return this.qdzInterestDayDao.findByCondition(qdzInterestDay, pager);
	}

	@Override
	public int findQdzInterestDayCount(QdzInterestDay qdzInterestDay) {
		return this.qdzInterestDayDao.getTotalCount(qdzInterestDay);
	}

	@Override
	public Pager findQdzInterestDayInfo(QdzInterestDay qdzInterestDay, Pager pager) {
		return this.qdzInterestDayDao.findQdzInterestDayInfo(qdzInterestDay, pager);
	}
}
