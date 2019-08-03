package com.hongkun.finance.info.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.info.model.InfoFeedback;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.info.dao.InfoFeedbackDao;
import com.hongkun.finance.info.service.InfoFeedbackService;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.impl.InfoFeedbackServiceImpl.java
 * @Class Name : InfoFeedbackServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoFeedbackServiceImpl implements InfoFeedbackService {

	private static final Logger logger = LoggerFactory.getLogger(InfoFeedbackServiceImpl.class);

	/**
	 * InfoFeedbackDAO
	 */
	@Autowired
	private InfoFeedbackDao infoFeedbackDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoFeedback(InfoFeedback infoFeedback) {
		this.infoFeedbackDao.save(infoFeedback);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoFeedbackBatch(List<InfoFeedback> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoFeedbackDao.insertBatch(InfoFeedback.class, list, count);
	}

	@Override
	public InfoFeedback findInfoFeedbackById(int id) {
		return this.infoFeedbackDao.findByPK(Long.valueOf(id), InfoFeedback.class);
	}

	@Override
	public List<InfoFeedback> findInfoFeedbackList(InfoFeedback infoFeedback) {
		return this.infoFeedbackDao.findByCondition(infoFeedback);
	}

	@Override
	public Pager findInfoFeedbackList(InfoFeedback infoFeedback, Pager pager) {
		return this.infoFeedbackDao.findByCondition(infoFeedback, pager);
	}
}
