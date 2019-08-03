package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoQuestionnaireAnswer;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.InfoQuestionnaireAnswerService.java
 * @Class Name : InfoQuestionnaireAnswerService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoQuestionnaireAnswerService {

	/**
	 * @Described : 单条插入
	 * @param infoQuestionnaireAnswer
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertInfoQuestionnaireAnswer(InfoQuestionnaireAnswer infoQuestionnaireAnswer);

	/**
	 * @Described : 批量插入
	 * @param List<InfoQuestionnaireAnswer>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoQuestionnaireAnswerBatch(List<InfoQuestionnaireAnswer> list, int count);

	/**
	 * @Described : 更新数据
	 * @param infoQuestionnaireAnswer
	 *            要更新的数据
	 * @return : void
	 */
	void updateInfoQuestionnaireAnswer(InfoQuestionnaireAnswer infoQuestionnaireAnswer);

	/**
	 * @Described : 批量更新数据
	 * @param infoQuestionnaireAnswer
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateInfoQuestionnaireAnswerBatch(List<InfoQuestionnaireAnswer> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoQuestionnaireAnswer
	 */
	InfoQuestionnaireAnswer findInfoQuestionnaireAnswerById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaireAnswer
	 *            检索条件
	 * @return List<InfoQuestionnaireAnswer>
	 */
	List<InfoQuestionnaireAnswer> findInfoQuestionnaireAnswerList(InfoQuestionnaireAnswer infoQuestionnaireAnswer);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaireAnswer
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<InfoQuestionnaireAnswer>
	 */
	Pager findInfoQuestionnaireAnswerList(InfoQuestionnaireAnswer infoQuestionnaireAnswer, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param infoQuestionnaireAnswer
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findInfoQuestionnaireAnswerCount(InfoQuestionnaireAnswer infoQuestionnaireAnswer);

	/**
	 * @Description : 保存问卷调查的试题答案
	 * @Method_Name : saveInfoQuestionnaireAnswer;
	 * @param questionnaireAnswer
	 * @param regUserId
	 *            用户ID
	 * @return : void;
	 * @Creation Date : 2018年3月26日 下午2:18:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	void saveInfoQuestionnaireAnswer(String questionnaireAnswer, Integer regUserId);
}
