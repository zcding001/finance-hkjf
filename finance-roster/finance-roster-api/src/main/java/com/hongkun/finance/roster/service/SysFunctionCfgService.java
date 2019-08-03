package com.hongkun.finance.roster.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.roster.model.SysFunctionCfg;

/**
 * @Project       : finance-roster
 * @Program Name  : com.hongkun.finance.roster.service.SysFunctionCfgService.java
 * @Class Name    : SysFunctionCfgService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SysFunctionCfgService {
	
	/**
	 * @Described			: 单条插入
	 * @param sysFunctionCfg 持久化的数据对象
	 * @return				: void
	 */
	void insertSysFunctionCfg(SysFunctionCfg sysFunctionCfg);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SysFunctionCfg> 批量插入的数据
	 * @return				: void
	 */
	void insertSysFunctionCfgBatch(List<SysFunctionCfg> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SysFunctionCfg> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertSysFunctionCfgBatch(List<SysFunctionCfg> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param sysFunctionCfg 要更新的数据
	 * @return				: void
	 */
	void updateSysFunctionCfg(SysFunctionCfg sysFunctionCfg);
	
	/**
	 * @Described			: 批量更新数据
	 * @param sysFunctionCfg 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateSysFunctionCfgBatch(List<SysFunctionCfg> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SysFunctionCfg
	 */
	SysFunctionCfg findSysFunctionCfgById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysFunctionCfg 检索条件
	 * @return	List<SysFunctionCfg>
	 */
	List<SysFunctionCfg> findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysFunctionCfg 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<SysFunctionCfg>
	 */
	List<SysFunctionCfg> findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param sysFunctionCfg 检索条件
	 * @param pager	分页数据
	 * @return	List<SysFunctionCfg>
	 */
	Pager findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param sysFunctionCfg 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findSysFunctionCfgCount(SysFunctionCfg sysFunctionCfg);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findSysFunctionCfgList(SysFunctionCfg sysFunctionCfg, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findSysFunctionCfgCount(SysFunctionCfg sysFunctionCfg, String sqlName);
}
