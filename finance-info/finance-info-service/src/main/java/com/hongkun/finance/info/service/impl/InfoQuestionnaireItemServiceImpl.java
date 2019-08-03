package com.hongkun.finance.info.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.info.model.InfoQuestionnaireItem;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.info.dao.InfoQuestionnaireItemDao;
import com.hongkun.finance.info.service.InfoQuestionnaireItemService;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.impl.InfoQuestionnaireItemServiceImpl
 *          .java
 * @Class Name : InfoQuestionnaireItemServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoQuestionnaireItemServiceImpl implements InfoQuestionnaireItemService {

	private static final Logger logger = LoggerFactory.getLogger(InfoQuestionnaireItemServiceImpl.class);

	/**
	 * InfoQuestionnaireItemDAO
	 */
	@Autowired
	private InfoQuestionnaireItemDao infoQuestionnaireItemDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaireItem(InfoQuestionnaireItem infoQuestionnaireItem) {
		this.infoQuestionnaireItemDao.save(infoQuestionnaireItem);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaireItemBatch(List<InfoQuestionnaireItem> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoQuestionnaireItemDao.insertBatch(InfoQuestionnaireItem.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaireItem(InfoQuestionnaireItem infoQuestionnaireItem) {
		this.infoQuestionnaireItemDao.update(infoQuestionnaireItem);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaireItemBatch(List<InfoQuestionnaireItem> list, int count) {
		this.infoQuestionnaireItemDao.updateBatch(InfoQuestionnaireItem.class, list, count);
	}

	@Override
	public InfoQuestionnaireItem findInfoQuestionnaireItemById(int id) {
		return this.infoQuestionnaireItemDao.findByPK(Long.valueOf(id), InfoQuestionnaireItem.class);
	}

	@Override
	public List<InfoQuestionnaireItem> findInfoQuestionnaireItemList(InfoQuestionnaireItem infoQuestionnaireItem) {
		return this.infoQuestionnaireItemDao.findByCondition(infoQuestionnaireItem);
	}

	@Override
	public Pager findInfoQuestionnaireItemList(InfoQuestionnaireItem infoQuestionnaireItem, Pager pager) {
		return this.infoQuestionnaireItemDao.findByCondition(infoQuestionnaireItem, pager);
	}

	@Override
	public int findInfoQuestionnaireItemCount(InfoQuestionnaireItem infoQuestionnaireItem) {
		return this.infoQuestionnaireItemDao.getTotalCount(infoQuestionnaireItem);
	}
}
