package com.hongkun.finance.info.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.info.model.InfoQuestionnaireItem;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.info.service.InfoQuestionnaireItemService.java
 * @Class Name : InfoQuestionnaireItemService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface InfoQuestionnaireItemService {

	/**
	 * @Described : 单条插入
	 * @param infoQuestionnaireItem
	 *            持久化的数据对象
	 * @return : void
	 */
	void insertInfoQuestionnaireItem(InfoQuestionnaireItem infoQuestionnaireItem);

	/**
	 * @Described : 批量插入
	 * @param List<InfoQuestionnaireItem>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertInfoQuestionnaireItemBatch(List<InfoQuestionnaireItem> list, int count);

	/**
	 * @Described : 更新数据
	 * @param infoQuestionnaireItem
	 *            要更新的数据
	 * @return : void
	 */
	void updateInfoQuestionnaireItem(InfoQuestionnaireItem infoQuestionnaireItem);

	/**
	 * @Described : 批量更新数据
	 * @param infoQuestionnaireItem
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateInfoQuestionnaireItemBatch(List<InfoQuestionnaireItem> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return InfoQuestionnaireItem
	 */
	InfoQuestionnaireItem findInfoQuestionnaireItemById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaireItem
	 *            检索条件
	 * @return List<InfoQuestionnaireItem>
	 */
	List<InfoQuestionnaireItem> findInfoQuestionnaireItemList(InfoQuestionnaireItem infoQuestionnaireItem);

	/**
	 * @Described : 条件检索数据
	 * @param infoQuestionnaireItem
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<InfoQuestionnaireItem>
	 */
	Pager findInfoQuestionnaireItemList(InfoQuestionnaireItem infoQuestionnaireItem, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param infoQuestionnaireItem
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findInfoQuestionnaireItemCount(InfoQuestionnaireItem infoQuestionnaireItem);
}
