package com.hongkun.finance.qdz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.dao.QdzInterestDayDetailDao;
import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.hongkun.finance.qdz.service.QdzInterestDayDetailService;
import com.hongkun.finance.qdz.vo.QdzInterestDetailInfoVo;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.service.impl.QdzInterestDayDetailServiceImpl.
 *          java
 * @Class Name : QdzInterestDayDetailServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class QdzInterestDayDetailServiceImpl implements QdzInterestDayDetailService {

	private static final Logger logger = LoggerFactory.getLogger(QdzInterestDayDetailServiceImpl.class);

	/**
	 * QdzInterestDayDetailDAO
	 */
	@Autowired
	private QdzInterestDayDetailDao qdzInterestDayDetailDao;

	@Override
	public int insertQdzInterestDayDetail(QdzInterestDayDetail qdzInterestDayDetail) {
		return this.qdzInterestDayDetailDao.save(qdzInterestDayDetail);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list) {
		this.qdzInterestDayDetailDao.insertBatch(QdzInterestDayDetail.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.qdzInterestDayDetailDao.insertBatch(QdzInterestDayDetail.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateQdzInterestDayDetail(QdzInterestDayDetail qdzInterestDayDetail) {
		return this.qdzInterestDayDetailDao.update(qdzInterestDayDetail);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzInterestDayDetailBatch(List<QdzInterestDayDetail> list, int count) {
		this.qdzInterestDayDetailDao.updateBatch(QdzInterestDayDetail.class, list, count);
	}

	@Override
	public QdzInterestDayDetail findQdzInterestDayDetailById(int id) {
		return this.qdzInterestDayDetailDao.findByPK(Long.valueOf(id), QdzInterestDayDetail.class);
	}

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail) {
		return this.qdzInterestDayDetailDao.findByCondition(qdzInterestDayDetail);
	}

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail, int start,
			int limit) {
		return this.qdzInterestDayDetailDao.findByCondition(qdzInterestDayDetail, start, limit);
	}

	@Override
	public Pager findQdzInterestDayDetailList(QdzInterestDayDetail qdzInterestDayDetail, Pager pager) {
		return this.qdzInterestDayDetailDao.findByCondition(qdzInterestDayDetail, pager);
	}

	@Override
	public int findQdzInterestDayDetailCount(QdzInterestDayDetail qdzInterestDayDetail) {
		return this.qdzInterestDayDetailDao.getTotalCount(qdzInterestDayDetail);
	}

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailInfo(QdzInterestDetailInfoVo qdzInterestDetailInfoVo) {
		Map<String, Object> thirdInterestDayDetailMap = new HashMap<String, Object>();
		thirdInterestDayDetailMap.put("day", qdzInterestDetailInfoVo.getDay());
		thirdInterestDayDetailMap.put("userflag", qdzInterestDetailInfoVo.getUserFlag());
		thirdInterestDayDetailMap.put("thirdRegUserId", qdzInterestDetailInfoVo.getThirdRegUserId());
		thirdInterestDayDetailMap.put("dayBegin", qdzInterestDetailInfoVo.getDayBegin());
		thirdInterestDayDetailMap.put("dayEnd", qdzInterestDetailInfoVo.getDayEnd());
		thirdInterestDayDetailMap.put("dayList", qdzInterestDetailInfoVo.getDayList());
		return this.qdzInterestDayDetailDao.findQdzInterestDayDetailInfo(thirdInterestDayDetailMap);
	}

	@Override
	public Pager findQdzInterestDetails(QdzInterestDetailInfoVo qdzInterestDetailInfoVo, Pager pager) {
		Map<String, Object> thirdInterestDayDetailMap = new HashMap<String, Object>();
		thirdInterestDayDetailMap.put("day", qdzInterestDetailInfoVo.getDay());
		thirdInterestDayDetailMap.put("userflag", qdzInterestDetailInfoVo.getUserFlag());
		thirdInterestDayDetailMap.put("thirdRegUserId", qdzInterestDetailInfoVo.getThirdRegUserId());
		return this.qdzInterestDayDetailDao.findQdzInterestDetails(thirdInterestDayDetailMap, pager);
	}

	@Override
	public List<QdzInterestDayDetail> findSuccQdzInterestDayDetails(QdzInterestDayDetail qdzInterestDayDetail) {

		return this.qdzInterestDayDetailDao.findSuccQdzInterestDayDetails(qdzInterestDayDetail);
	}

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailsByShardingItem(Date day, int shardingItem) {
		return qdzInterestDayDetailDao.findQdzInterestDayDetailsByShardingItem(day, shardingItem);

	}
}
