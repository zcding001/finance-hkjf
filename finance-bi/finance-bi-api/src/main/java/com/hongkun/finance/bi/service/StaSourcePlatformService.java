package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.StaSourcePlatform;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.StaSourcePlatformService.java
 * @Class Name    : StaSourcePlatformService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface StaSourcePlatformService {
	
	/**
	 * @Described			: 单条插入
	 * @param staSourcePlatform 持久化的数据对象
	 * @return				: void
	 */
	void insertStaSourcePlatform(StaSourcePlatform staSourcePlatform);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaSourcePlatform> 批量插入的数据
	 * @return				: void
	 */
	void insertStaSourcePlatformBatch(List<StaSourcePlatform> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<StaSourcePlatform> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertStaSourcePlatformBatch(List<StaSourcePlatform> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param staSourcePlatform 要更新的数据
	 * @return				: void
	 */
	void updateStaSourcePlatform(StaSourcePlatform staSourcePlatform);
	
	/**
	 * @Described			: 批量更新数据
	 * @param staSourcePlatform 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateStaSourcePlatformBatch(List<StaSourcePlatform> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	StaSourcePlatform
	 */
	StaSourcePlatform findStaSourcePlatformById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staSourcePlatform 检索条件
	 * @return	List<StaSourcePlatform>
	 */
	List<StaSourcePlatform> findStaSourcePlatformList(StaSourcePlatform staSourcePlatform);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staSourcePlatform 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<StaSourcePlatform>
	 */
	List<StaSourcePlatform> findStaSourcePlatformList(StaSourcePlatform staSourcePlatform, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param staSourcePlatform 检索条件
	 * @param pager	分页数据
	 * @return	List<StaSourcePlatform>
	 */
	Pager findStaSourcePlatformList(StaSourcePlatform staSourcePlatform, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param staSourcePlatform 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findStaSourcePlatformCount(StaSourcePlatform staSourcePlatform);
}
