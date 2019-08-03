package com.hongkun.finance.point.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointSignRule;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointSignRuleService.java
 * @Class Name    : PointSignRuleService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointSignRuleService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointSignRule 持久化的数据对象
	 * @return				: void
	 */
	void insertPointSignRule(PointSignRule pointSignRule);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointSignRule> 批量插入的数据
	 * @return				: void
	 */
	void insertPointSignRuleBatch(List<PointSignRule> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointSignRule> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertPointSignRuleBatch(List<PointSignRule> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param pointSignRule 要更新的数据
	 * @return				: void
	 */
	void updatePointSignRule(PointSignRule pointSignRule);
	
	/**
	 * @Described			: 批量更新数据
	 * @param pointSignRule 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updatePointSignRuleBatch(List<PointSignRule> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointSignRule
	 */
	PointSignRule findPointSignRuleById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignRule 检索条件
	 * @return	List<PointSignRule>
	 */
	List<PointSignRule> findPointSignRuleList(PointSignRule pointSignRule);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignRule 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointSignRule>
	 */
	List<PointSignRule> findPointSignRuleList(PointSignRule pointSignRule, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointSignRule 检索条件
	 * @param pager	分页数据
	 * @return	List<PointSignRule>
	 */
	Pager findPointSignRuleList(PointSignRule pointSignRule, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointSignRule 检索条件
	 * @return	int
	 */
	int findPointSignRuleCount(PointSignRule pointSignRule);
}
