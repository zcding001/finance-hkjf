package com.hongkun.finance.loan.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.loan.model.BidReturnCapRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.BidReturnCapRecordService.java
 * @Class Name    : BidReturnCapRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidReturnCapRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidReturnCapRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertBidReturnCapRecord(BidReturnCapRecord bidReturnCapRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidReturnCapRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertBidReturnCapRecordBatch(List<BidReturnCapRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidReturnCapRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidReturnCapRecordBatch(List<BidReturnCapRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidReturnCapRecord 要更新的数据
	 * @return				: void
	 */
	void updateBidReturnCapRecord(BidReturnCapRecord bidReturnCapRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidReturnCapRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidReturnCapRecordBatch(List<BidReturnCapRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidReturnCapRecord
	 */
	BidReturnCapRecord findBidReturnCapRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidReturnCapRecord 检索条件
	 * @return	List<BidReturnCapRecord>
	 */
	List<BidReturnCapRecord> findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidReturnCapRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidReturnCapRecord>
	 */
	List<BidReturnCapRecord> findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidReturnCapRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<BidReturnCapRecord>
	 */
	Pager findBidReturnCapRecordList(BidReturnCapRecord bidReturnCapRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidReturnCapRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidReturnCapRecordCount(BidReturnCapRecord bidReturnCapRecord);
}
