package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasCouponProfitRecord;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.vas.service.VasCouponProfitRecordService.java
 * @Class Name    : VasCouponProfitRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasCouponProfitRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasCouponProfitRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertVasCouponProfitRecord(VasCouponProfitRecord vasCouponProfitRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<VasCouponProfitRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<VasCouponProfitRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param vasCouponProfitRecord 要更新的数据
	 * @return				: void
	 */
	void updateVasCouponProfitRecord(VasCouponProfitRecord vasCouponProfitRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param vasCouponProfitRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateVasCouponProfitRecordBatch(List<VasCouponProfitRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasCouponProfitRecord
	 */
	VasCouponProfitRecord findVasCouponProfitRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponProfitRecord 检索条件
	 * @return	List<VasCouponProfitRecord>
	 */
	List<VasCouponProfitRecord> findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponProfitRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<VasCouponProfitRecord>
	 */
	List<VasCouponProfitRecord> findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponProfitRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<VasCouponProfitRecord>
	 */
	Pager findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param vasCouponProfitRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findVasCouponProfitRecordCount(VasCouponProfitRecord vasCouponProfitRecord);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findVasCouponProfitRecordList(VasCouponProfitRecord vasCouponProfitRecord, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findVasCouponProfitRecordCount(VasCouponProfitRecord vasCouponProfitRecord, String sqlName);
}
