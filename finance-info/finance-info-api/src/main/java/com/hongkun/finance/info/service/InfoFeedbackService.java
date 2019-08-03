package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoFeedback;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.info.service.InfoFeedbackService.java
 * @Class Name : InfoFeedbackService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoFeedbackService {

	/**
	 * @Described : 单条插入
	 * @param infoFeedback
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertInfoFeedback(InfoFeedback infoFeedback);

	/**
	 * @Described : 批量插入
	 * @param List<InfoFeedback>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoFeedbackBatch(List<InfoFeedback> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoFeedback
	 */
	InfoFeedback findInfoFeedbackById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoFeedback
	 *            检索条件
	 * @return List<InfoFeedback>
	 */
	List<InfoFeedback> findInfoFeedbackList(InfoFeedback infoFeedback);

	/**
	 * @Described : 条件检索数据
	 * @param infoFeedback
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<InfoFeedback>
	 */
	Pager findInfoFeedbackList(InfoFeedback infoFeedback, Pager pager);

}
