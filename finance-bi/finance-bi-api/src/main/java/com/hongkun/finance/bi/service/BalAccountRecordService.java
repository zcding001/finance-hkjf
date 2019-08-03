package com.hongkun.finance.bi.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.bi.model.BalAccountRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.BalAccountRecordService.java
 * @Class Name    : BalAccountRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BalAccountRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param balAccountRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertBalAccountRecord(BalAccountRecord balAccountRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BalAccountRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertBalAccountRecordBatch(List<BalAccountRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BalAccountRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBalAccountRecordBatch(List<BalAccountRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param balAccountRecord 要更新的数据
	 * @return				: void
	 */
	void updateBalAccountRecord(BalAccountRecord balAccountRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param balAccountRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBalAccountRecordBatch(List<BalAccountRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BalAccountRecord
	 */
	BalAccountRecord findBalAccountRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccountRecord 检索条件
	 * @return	List<BalAccountRecord>
	 */
	List<BalAccountRecord> findBalAccountRecordList(BalAccountRecord balAccountRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccountRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BalAccountRecord>
	 */
	List<BalAccountRecord> findBalAccountRecordList(BalAccountRecord balAccountRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param balAccountRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<BalAccountRecord>
	 */
	Pager findBalAccountRecordList(BalAccountRecord balAccountRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param balAccountRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBalAccountRecordCount(BalAccountRecord balAccountRecord);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findBalAccountRecordList(BalAccountRecord balAccountRecord, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findBalAccountRecordCount(BalAccountRecord balAccountRecord, String sqlName);
}
