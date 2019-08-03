package com.hongkun.finance.info.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.info.constant.InfomationConstants;
import com.hongkun.finance.info.dao.InfoInformationNewsDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireDao;
import com.hongkun.finance.info.dao.InfoQuestionnaireItemDao;
import com.hongkun.finance.info.model.InfoInformationNews;
import com.hongkun.finance.info.model.InfoQuestionnaire;
import com.hongkun.finance.info.model.InfoQuestionnaireItem;
import com.hongkun.finance.info.service.InfoQuestionnaireService;
import com.hongkun.finance.info.vo.InformationNewsItem;
import com.hongkun.finance.info.vo.Questionnaire;
import com.hongkun.finance.info.vo.QuestionnaireItem;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.impl.InfoQuestionnaireServiceImpl.
 *          java
 * @Class Name : InfoQuestionnaireServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class InfoQuestionnaireServiceImpl implements InfoQuestionnaireService {

	private static final Logger logger = LoggerFactory.getLogger(InfoQuestionnaireServiceImpl.class);

	/**
	 * InfoQuestionnaireDAO
	 */
	@Autowired
	private InfoQuestionnaireDao infoQuestionnaireDao;
	@Autowired
	private InfoQuestionnaireItemDao infoQuestionnaireItemDao;
	@Autowired
	private InfoInformationNewsDao infoInformationNewsDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaire(InfoQuestionnaire infoQuestionnaire) {
		this.infoQuestionnaireDao.save(infoQuestionnaire);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertInfoQuestionnaireBatch(List<InfoQuestionnaire> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.infoQuestionnaireDao.insertBatch(InfoQuestionnaire.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaire(InfoQuestionnaire infoQuestionnaire) {
		this.infoQuestionnaireDao.update(infoQuestionnaire);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateInfoQuestionnaireBatch(List<InfoQuestionnaire> list, int count) {
		this.infoQuestionnaireDao.updateBatch(InfoQuestionnaire.class, list, count);
	}

	@Override
	public InfoQuestionnaire findInfoQuestionnaireById(int id) {
		return this.infoQuestionnaireDao.findByPK(Long.valueOf(id), InfoQuestionnaire.class);
	}

	@Override
	public List<InfoQuestionnaire> findInfoQuestionnaireList(InfoQuestionnaire infoQuestionnaire) {
		return this.infoQuestionnaireDao.findByCondition(infoQuestionnaire);
	}

	@Override
	public Pager findInfoQuestionnaireList(InfoQuestionnaire infoQuestionnaire, Pager pager) {
		return this.infoQuestionnaireDao.findByCondition(infoQuestionnaire, pager);
	}

	@Override
	public int findInfoQuestionnaireCount(InfoQuestionnaire infoQuestionnaire) {
		return this.infoQuestionnaireDao.getTotalCount(infoQuestionnaire);
	}

	@Override
	public ResponseEntity<?> insertInfoQuestionnaire(String questionnaireJson) {
		logger.info("方法: insertInfoQuestionnaire, 添加调查问卷, 入参: questionnaireJson: {}", questionnaireJson);
		try {
			InformationNewsItem informationNewsItem = JSON.parseObject(questionnaireJson, InformationNewsItem.class);
			if (informationNewsItem == null || informationNewsItem.getProblemsList() == null
					|| informationNewsItem.getProblemsList().size() == 0) {
				logger.error("添加调查问卷, 调查问卷内容为空!");
				return new ResponseEntity<>(Constants.ERROR, "请添加调查问卷内容！");
			}
			for (Questionnaire questionnaire : informationNewsItem.getProblemsList()) {
				InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
				infoQuestionnaire.setContent(questionnaire.getContent());
				infoQuestionnaire.setType(questionnaire.getType());
				infoQuestionnaire.setInfoInformationNewsId(informationNewsItem.getInformationNewsId());
				infoQuestionnaire.setSort(questionnaire.getSort());
				infoQuestionnaire.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
				infoQuestionnaireDao.save(infoQuestionnaire);
				for (QuestionnaireItem questionnaireItem : questionnaire.getQuestionnaireItems()) {
					InfoQuestionnaireItem infoQuestionnaireItem = new InfoQuestionnaireItem();
					infoQuestionnaireItem.setOptionContent(questionnaireItem.getOptionContent());
					infoQuestionnaireItem.setOptionTitle(questionnaireItem.getOptionTitle());
					infoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
					infoQuestionnaireItem.setInfoQuestionnaireId(infoQuestionnaire.getId());
					infoQuestionnaireItemDao.save(infoQuestionnaireItem);
				}
			}
		} catch (Exception e) {
			logger.error("添加调查问卷, 添加失败: ", e);
			throw new GeneralException("添加调查问卷失败");
		}
		return new ResponseEntity<>(Constants.SUCCESS, "添加成功");
	}

	@Override
	public ResponseEntity<?> findInfoQuesetionById(Integer infomationId) {

		InfoInformationNews informationNews = infoInformationNewsDao.findByPK(infomationId.longValue(),
				InfoInformationNews.class);
		if (informationNews == null) {
			return new ResponseEntity<>(Constants.ERROR, "没有查询到该资讯信息");
		}
		InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
		infoQuestionnaire.setInfoInformationNewsId(informationNews.getId());

		InformationNewsItem informationNewsItem = new InformationNewsItem();
		informationNewsItem.setInformationNewsId(informationNews.getId());
		informationNewsItem.setContent(informationNews.getContent());

		List<InfoQuestionnaire> questionnaireList = infoQuestionnaireDao.findByCondition(infoQuestionnaire);
		List<Questionnaire> questionList = new ArrayList<Questionnaire>();
		if (questionnaireList != null && questionnaireList.size() > 0) {
			for (InfoQuestionnaire questionnaire : questionnaireList) {
				InfoQuestionnaireItem infoQuestionnaireItem = new InfoQuestionnaireItem();
				infoQuestionnaireItem.setInfoQuestionnaireId(questionnaire.getId());
				infoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
				infoQuestionnaireItem.setSortColumns("id desc");
				Questionnaire newQuestionnaire = new Questionnaire();
				BeanPropertiesUtil.mergeAndReturn(newQuestionnaire, questionnaire);
				newQuestionnaire.setQuestionnaireId(questionnaire.getId());
				questionList.add(newQuestionnaire);
				List<InfoQuestionnaireItem> questionItemList = infoQuestionnaireItemDao
						.findByCondition(infoQuestionnaireItem);
				if (questionItemList != null && questionItemList.size() > 0) {
					List<QuestionnaireItem> questItemList = new ArrayList<QuestionnaireItem>();
					for (InfoQuestionnaireItem infoQuestionnaireIte : questionItemList) {
						QuestionnaireItem questionnaireItem = new QuestionnaireItem();
						BeanPropertiesUtil.mergeAndReturn(questionnaireItem, infoQuestionnaireIte);
						questItemList.add(questionnaireItem);
					}
					newQuestionnaire.setQuestionnaireItems(questItemList);
				}
			}
			informationNewsItem.setProblemsList(questionList);
		}
		return new ResponseEntity<>(Constants.SUCCESS, informationNewsItem);
	}

	@Override
	public ResponseEntity<?> findQuestionAndItem(Integer infomationId, Integer questionId) {
		InfoQuestionnaire infoQuestionnaire = infoQuestionnaireDao.findByPK(questionId.longValue(),
				InfoQuestionnaire.class);
		if (infoQuestionnaire == null) {
			return new ResponseEntity<>(Constants.ERROR, "没有查询到此调查问卷");
		}
		Questionnaire newQuestionnaire = new Questionnaire();
		BeanPropertiesUtil.mergeAndReturn(newQuestionnaire, infoQuestionnaire);
		newQuestionnaire.setQuestionnaireId(infoQuestionnaire.getId());
		InfoQuestionnaireItem infoQuestionnaireItem = new InfoQuestionnaireItem();
		infoQuestionnaireItem.setInfoQuestionnaireId(infoQuestionnaire.getId());
		infoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
		infoQuestionnaireItem.setSortColumns("id desc");
		List<InfoQuestionnaireItem> questionItemList = infoQuestionnaireItemDao.findByCondition(infoQuestionnaireItem);
		if (questionItemList != null && questionItemList.size() > 0) {
			List<QuestionnaireItem> questItemList = new ArrayList<QuestionnaireItem>();
			for (InfoQuestionnaireItem infoQuestionnaireIte : questionItemList) {
				QuestionnaireItem questionnaireItem = new QuestionnaireItem();
				BeanPropertiesUtil.mergeAndReturn(questionnaireItem, infoQuestionnaireIte);
				questItemList.add(questionnaireItem);
			}
			newQuestionnaire.setQuestionnaireItems(questItemList);
		}
		return new ResponseEntity<>(Constants.SUCCESS, newQuestionnaire);
	}

	@Override
	public ResponseEntity<?> updateInfoQuestionnaire(String questionnaireJson, Integer questionId) {
		logger.info("方法: updateInfoQuestionnaire, 更新调查问卷, 入参: questionnaireJson: {}, questionId: {}", questionnaireJson,
				questionId);
		try {
			InformationNewsItem informationNewsItem = JSON.parseObject(questionnaireJson, InformationNewsItem.class);
			if (informationNewsItem == null || informationNewsItem.getProblemsList() == null
					|| informationNewsItem.getProblemsList().size() == 0) {
				logger.error("更新调查问卷, 调查问卷内容为空!");
				return new ResponseEntity<>(Constants.ERROR, "请添加调查问卷");
			}
			for (Questionnaire questionnaire : informationNewsItem.getProblemsList()) {
				InfoQuestionnaire infoQuestionnaire = new InfoQuestionnaire();
				infoQuestionnaire.setContent(questionnaire.getContent());
				infoQuestionnaire.setType(questionnaire.getType());
				infoQuestionnaire.setId(questionId);
				infoQuestionnaireDao.update(infoQuestionnaire);
				if (questionnaire.getQuestionnaireItems() != null && questionnaire.getQuestionnaireItems().size() > 0) {
					InfoQuestionnaireItem infoQuestionnaireItem = new InfoQuestionnaireItem();
					infoQuestionnaireItem.setInfoQuestionnaireId(questionId);
					infoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_DEL);
					infoQuestionnaireItemDao.updateQuestionItemByQuestionId(infoQuestionnaireItem);
					for (QuestionnaireItem questionnaireItem : questionnaire.getQuestionnaireItems()) {
						InfoQuestionnaireItem newInfoQuestionnaireItem = new InfoQuestionnaireItem();
						newInfoQuestionnaireItem.setOptionContent(questionnaireItem.getOptionContent());
						newInfoQuestionnaireItem.setOptionTitle(questionnaireItem.getOptionTitle());
						newInfoQuestionnaireItem.setState(InfomationConstants.INFO_QUESTIONNAIRE_STATE_SHOW);
						newInfoQuestionnaireItem.setInfoQuestionnaireId(infoQuestionnaire.getId());
						infoQuestionnaireItemDao.save(newInfoQuestionnaireItem);
					}
				}
			}
		} catch (Exception e) {
			logger.error("更新调查问卷, 更新失败: ", e);
			throw new GeneralException("更新调查问卷失败!");
		}
		return new ResponseEntity<>(Constants.SUCCESS, "操作成功");
	}
}
