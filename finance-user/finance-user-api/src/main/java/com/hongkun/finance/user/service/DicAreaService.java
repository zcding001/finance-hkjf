package com.hongkun.finance.user.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.DicArea;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.DicAreaService.java
 * @Class Name    : DicAreaService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface DicAreaService {
	
	/**
	 * @Described			: 单条插入
	 * @param dicArea 持久化的数据对象
	 * @return				: void
	 */
	void insertDicArea(DicArea dicArea);
	
	/**
	 * @Described			: 批量插入
	 * @param List<DicArea> 批量插入的数据
	 * @return				: void
	 */
	void insertDicAreaBatch(List<DicArea> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<DicArea> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertDicAreaBatch(List<DicArea> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param dicArea 要更新的数据
	 * @return				: void
	 */
	void updateDicArea(DicArea dicArea);
	
	/**
	 * @Described			: 批量更新数据
	 * @param dicArea 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateDicAreaBatch(List<DicArea> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	DicArea
	 */
	DicArea findDicAreaById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param dicArea 检索条件
	 * @return	List<DicArea>
	 */
	List<DicArea> findDicAreaList(DicArea dicArea);
	
	/**
	 * @Described			: 条件检索数据
	 * @param dicArea 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<DicArea>
	 */
	List<DicArea> findDicAreaList(DicArea dicArea, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param dicArea 检索条件
	 * @param pager	分页数据
	 * @return	List<DicArea>
	 */
	Pager findDicAreaList(DicArea dicArea, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param dicArea 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findDicAreaCount(DicArea dicArea);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findDicAreaList(DicArea dicArea, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findDicAreaCount(DicArea dicArea, String sqlName);
}
