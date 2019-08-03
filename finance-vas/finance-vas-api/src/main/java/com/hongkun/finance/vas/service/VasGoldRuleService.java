package com.hongkun.finance.vas.service;

import java.util.List;

import com.hongkun.finance.vas.model.VasGoldRule;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.service.VasGoldRuleService.java
 * @Class Name : VasGoldRuleService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface VasGoldRuleService {

	/**
	 * @Described : 单条插入
	 * @param vasGoldRule
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertVasGoldRule(VasGoldRule vasGoldRule);

	/**
	 * @Described : 批量插入
	 * @param List<VasGoldRule>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertVasGoldRuleBatch(List<VasGoldRule> list);

	/**
	 * @Described : 批量插入
	 * @param List<VasGoldRule>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertVasGoldRuleBatch(List<VasGoldRule> list, int count);

	/**
	 * @Described : 更新数据
	 * @param vasGoldRule
	 *            要更新的数据
	 * @return : void
	 */
	int updateVasGoldRule(VasGoldRule vasGoldRule);

	/**
	 * @Described : 批量更新数据
	 * @param vasGoldRule
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void updateVasGoldRuleBatch(List<VasGoldRule> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return VasGoldRule
	 */
	VasGoldRule findVasGoldRuleById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param vasGoldRule
	 *            检索条件
	 * @return List<VasGoldRule>
	 */
	List<VasGoldRule> findVasGoldRuleList(VasGoldRule vasGoldRule);

	/**
	 * @Described : 条件检索数据
	 * @param vasGoldRule
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<VasGoldRule>
	 */
	List<VasGoldRule> findVasGoldRuleList(VasGoldRule vasGoldRule, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param vasGoldRule
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<VasGoldRule>
	 */
	Pager findVasGoldRuleList(VasGoldRule vasGoldRule, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param vasGoldRule
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findVasGoldRuleCount(VasGoldRule vasGoldRule);

	/**
	 * @Description : 根据事件类型、状态查询体验金信息
	 * @Method_Name : findVasGoldRuleByTypeAndState;
	 * @param state
	 * @param type
	 * @return
	 * @return : VasGoldRule;
	 * @Creation Date : 2017年9月27日 下午7:11:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	VasGoldRule findVasGoldRuleByTypeAndState(int type, int state);

	/**
	 * @Description :通过ID，更新体验金规则
	 * @Method_Name : updateVasGoldRuleById;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月24日 下午1:59:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> updateVasGoldRuleById(VasGoldRule vasGoldRule);

	/**
	 * @Description : 逻缉删除体验金规则状态
	 * @Method_Name : deleteVasGoldRule;
	 * @param vasGoldRule
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月24日 下午2:55:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> deleteVasGoldRuleState(VasGoldRule vasGoldRule);

}
