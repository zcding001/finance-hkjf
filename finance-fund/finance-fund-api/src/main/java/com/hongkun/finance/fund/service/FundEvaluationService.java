package com.hongkun.finance.fund.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.fund.model.FundEvaluation;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundEvaluationService.java
 * @Class Name    : FundEvaluationService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundEvaluationService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundEvaluation 持久化的数据对象
	 * @return				: void
	 */
	void insertFundEvaluation(FundEvaluation fundEvaluation);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundEvaluation> 批量插入的数据
	 * @return				: void
	 */
	void insertFundEvaluationBatch(List<FundEvaluation> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundEvaluation> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundEvaluationBatch(List<FundEvaluation> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundEvaluation 要更新的数据
	 * @return				: void
	 */
	void updateFundEvaluation(FundEvaluation fundEvaluation);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundEvaluation 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundEvaluationBatch(List<FundEvaluation> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundEvaluation
	 */
	FundEvaluation findFundEvaluationById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundEvaluation 检索条件
	 * @return	List<FundEvaluation>
	 */
	List<FundEvaluation> findFundEvaluationList(FundEvaluation fundEvaluation);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundEvaluation 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundEvaluation>
	 */
	List<FundEvaluation> findFundEvaluationList(FundEvaluation fundEvaluation, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundEvaluation 检索条件
	 * @param pager	分页数据
	 * @return	List<FundEvaluation>
	 */
	Pager findFundEvaluationList(FundEvaluation fundEvaluation, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundEvaluation 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundEvaluationCount(FundEvaluation fundEvaluation);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundEvaluationList(FundEvaluation fundEvaluation, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findFundEvaluationCount(FundEvaluation fundEvaluation, String sqlName);


	/**
	 *  @Description    ：保存用户股权测评信息
	 *  @Method_Name    ：saveRiskEvalution
	 *  @param regUserId
	 *  @param answers
	 *  @param score
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年05月09日 10:54
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity<?> saveRiskEvalution(Integer regUserId, String answers, Integer score);
}
