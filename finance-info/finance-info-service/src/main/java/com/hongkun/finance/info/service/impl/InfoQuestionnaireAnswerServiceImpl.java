package com.hongkun.finance.info.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.info.dao.InfoQuestionnaireAnswerDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireDao;
import com.hongkun.finance.info.service.InfoQuestionnaireAnswerService;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.service.impl.
 *          InfoQuestionnaireAnswerServiceImpl.java
 * @Class Name : InfoQuestionnaireAnswerServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoQuestionnaireAnswerServiceImpl implements InfoQuestionnaireAnswerService {

	private static final Logger logger = LoggerFactory.getLogger(InfoQuestionnaireAnswerServiceImpl.class);

	/**
	 * InfoQuestionnaireAnswerDAO
	 */
	@Autowired
	private InfoQuestionnaireAnswerDao infoQuestionnaireAnswerDao;
	@Autowired
	private InfoQuestionnaireDao infoQuestionnaireDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaireAnswer(InfoQuestionnaireAnswer infoQuestionnaireAnswer) {
		this.infoQuestionnaireAnswerDao.save(infoQuestionnaireAnswer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaireAnswerBatch(List<InfoQuestionnaireAnswer> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoQuestionnaireAnswerDao.insertBatch(InfoQuestionnaireAnswer.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaireAnswer(InfoQuestionnaireAnswer infoQuestionnaireAnswer) {
		this.infoQuestionnaireAnswerDao.update(infoQuestionnaireAnswer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaireAnswerBatch(List<InfoQuestionnaireAnswer> list, int count) {
		this.infoQuestionnaireAnswerDao.updateBatch(InfoQuestionnaireAnswer.class, list, count);
	}

	@Override
	public InfoQuestionnaireAnswer findInfoQuestionnaireAnswerById(int id) {
		return this.infoQuestionnaireAnswerDao.findByPK(Long.valueOf(id), InfoQuestionnaireAnswer.class);
	}

	@Override
	public List<InfoQuestionnaireAnswer> findInfoQuestionnaireAnswerList(
			InfoQuestionnaireAnswer infoQuestionnaireAnswer) {
		return this.infoQuestionnaireAnswerDao.findByCondition(infoQuestionnaireAnswer);
	}

	@Override
	public Pager findInfoQuestionnaireAnswerList(InfoQuestionnaireAnswer infoQuestionnaireAnswer, Pager pager) {
		return this.infoQuestionnaireAnswerDao.findByCondition(infoQuestionnaireAnswer, pager);
	}

	@Override
	public int findInfoQuestionnaireAnswerCount(InfoQuestionnaireAnswer infoQuestionnaireAnswer) {
		return this.infoQuestionnaireAnswerDao.getTotalCount(infoQuestionnaireAnswer);
	}

	@Override
	public void saveInfoQuestionnaireAnswer(String questionnaireAnswer, Integer regUserId) {
		List<InfoQuestionnaireAnswer> anserList = JsonUtils.json2GenericObject(questionnaireAnswer,
				new TypeReference<List<InfoQuestionnaireAnswer>>() {
				}, null);
		for (InfoQuestionnaireAnswer infoQuestionnaireAnswer : anserList) {
			InfoQuestionnaire infoQuestionnaire = infoQuestionnaireDao
					.findByPK(infoQuestionnaireAnswer.getInfoQuestionnaireId().longValue(), InfoQuestionnaire.class);
			if (infoQuestionnaire != null) {
				infoQuestionnaireAnswer.setRegUserId(regUserId);
				this.infoQuestionnaireAnswerDao.save(infoQuestionnaireAnswer);
			}
		}
	}
}
