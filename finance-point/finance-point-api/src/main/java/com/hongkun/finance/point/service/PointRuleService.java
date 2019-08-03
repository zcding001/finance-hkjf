package com.hongkun.finance.point.service;

import java.math.BigDecimal;
import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.point.model.PointRule;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.PointRuleService.java
 * @Class Name    : PointRuleService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface PointRuleService {
	
	/**
	 * @Described			: 单条插入
	 * @param pointRule 持久化的数据对象
	 * @return				: void
	 */
	int insertPointRule(PointRule pointRule);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointRule> 批量插入的数据
	 * @return				: void
	 */
	void insertPointRuleBatch(List<PointRule> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<PointRule> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertPointRuleBatch(List<PointRule> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param pointRule 要更新的数据
	 * @return				: void
	 */
	int updatePointRule(PointRule pointRule);
	
	/**
	 * @Described			: 批量更新数据
	 * @param pointRule 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updatePointRuleBatch(List<PointRule> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	PointRule
	 */
	PointRule findPointRuleById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRule 检索条件
	 * @return	List<PointRule>
	 */
	List<PointRule> findPointRuleList(PointRule pointRule);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRule 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<PointRule>
	 */
	List<PointRule> findPointRuleList(PointRule pointRule, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param pointRule 检索条件
	 * @param pager	分页数据
	 * @return	List<PointRule>
	 */
	Pager findPointRuleList(PointRule pointRule, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param pointRule 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findPointRuleCount(PointRule pointRule);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findPointRuleList(PointRule pointRule, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findPointRuleCount(PointRule pointRule, String sqlName);

	/**
	 *  @Description    : 支付支付-钱兑换积分
	 *  @Method_Name    : moneyToPoint
	 *  @param money
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2017年8月16日 上午9:06:35 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	int moneyToPoint(BigDecimal money);
	/**
	 *  @Description    : 物业缴费钱兑换积分
	 *  @Method_Name    : proMoneyToPoint
	 *  @param money
	 *  @return
	 *  @return         : int
	 *  @Creation Date  : 2019-01-09 10:54:31 
	 *  @Author         : binliang@hongkun.com.cn 梁彬
	 */
	int proMoneyToPoint(BigDecimal money);
	/**
	 * 获取当前启用的积分规则
	 * @return
	 */
	PointRule getCurrentOnUseRule();


}
