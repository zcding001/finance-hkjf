package com.hongkun.finance.invest.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidTransferRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.BidTransferRecordService.java
 * @Class Name    : BidTransferRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidTransferRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidTransferRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertBidTransferRecord(BidTransferRecord bidTransferRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidTransferRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertBidTransferRecordBatch(List<BidTransferRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidTransferRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidTransferRecordBatch(List<BidTransferRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidTransferRecord
	 */
	BidTransferRecord findBidTransferRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidTransferRecord 检索条件
	 * @return	List<BidTransferRecord>
	 */
	List<BidTransferRecord> findBidTransferRecordList(BidTransferRecord bidTransferRecord);
	
	
}
