package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoQuestionnaire;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.service.InfoQuestionnaireService.java
 * @Class Name : InfoQuestionnaireService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoQuestionnaireService {

	/**
	 * @Described : 单条插入
	 * @param infoQuestionnaire
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertInfoQuestionnaire(InfoQuestionnaire infoQuestionnaire);

	/**
	 * @Described : 批量插入
	 * @param List<InfoQuestionnaire>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoQuestionnaireBatch(List<InfoQuestionnaire> list, int count);

	/**
	 * @Described : 更新数据
	 * @param infoQuestionnaire
	 *            要更新的数据
	 * @return : void
	 */
	void updateInfoQuestionnaire(InfoQuestionnaire infoQuestionnaire);

	/**
	 * @Described : 批量更新数据
	 * @param infoQuestionnaire
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateInfoQuestionnaireBatch(List<InfoQuestionnaire> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoQuestionnaire
	 */
	InfoQuestionnaire findInfoQuestionnaireById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaire
	 *            检索条件
	 * @return List<InfoQuestionnaire>
	 */
	List<InfoQuestionnaire> findInfoQuestionnaireList(InfoQuestionnaire infoQuestionnaire);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaire
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<InfoQuestionnaire>
	 */
	Pager findInfoQuestionnaireList(InfoQuestionnaire infoQuestionnaire, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param infoQuestionnaire
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findInfoQuestionnaireCount(InfoQuestionnaire infoQuestionnaire);

	/**
	 * @Description : 保存调查问卷详情内容
	 * @Method_Name : insertInfoQuestionnaire;
	 * @param questionnaireJson
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月13日 下午7:28:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> insertInfoQuestionnaire(String questionnaireJson);

	/**
	 * @Description : 查询资讯对应的调查问卷
	 * @Method_Name : findInfoQuesetionById;
	 * @param infomationId
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午3:36:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findInfoQuesetionById(Integer infomationId);

	/**
	 * @Description : 查询调查问卷及其选项内容
	 * @Method_Name : findQuestionAndItem;
	 * @param infomationId
	 *            资讯ID
	 * @param questionId
	 *            问题ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午6:45:26;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findQuestionAndItem(Integer infomationId, Integer questionId);

	/**
	 * @Description : 更新调查问卷及选项内容
	 * @Method_Name : updateInfoQuestionnaire;
	 * @param questionnaireJson
	 * @param questionId
	 *            问题ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月15日 下午7:17:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateInfoQuestionnaire(String questionnaireJson, Integer questionId);
}
