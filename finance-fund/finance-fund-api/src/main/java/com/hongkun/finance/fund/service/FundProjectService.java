package com.hongkun.finance.fund.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.fund.model.FundProject;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundProjectService.java
 * @Class Name    : FundProjectService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundProjectService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundProject 持久化的数据对象
	 * @return				: void
	 */
	void insertFundProject(FundProject fundProject);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundProject> 批量插入的数据
	 * @return				: void
	 */
	void insertFundProjectBatch(List<FundProject> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundProject> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundProjectBatch(List<FundProject> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundProject 要更新的数据
	 * @return				: void
	 */
	void updateFundProject(FundProject fundProject);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundProject 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundProjectBatch(List<FundProject> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundProject
	 */
	FundProject findFundProjectById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundProject 检索条件
	 * @return	List<FundProject>
	 */
	List<FundProject> findFundProjectList(FundProject fundProject);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundProject 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundProject>
	 */
	List<FundProject> findFundProjectList(FundProject fundProject, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundProject 检索条件
	 * @param pager	分页数据
	 * @return	List<FundProject>
	 */
	Pager findFundProjectList(FundProject fundProject, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundProject 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundProjectCount(FundProject fundProject);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundProjectList(FundProject fundProject, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findFundProjectCount(FundProject fundProject, String sqlName);
}
