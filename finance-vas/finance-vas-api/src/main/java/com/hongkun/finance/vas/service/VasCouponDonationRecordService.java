package com.hongkun.finance.vas.service;

import com.hongkun.finance.vas.model.VasCouponDonationRecord;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.service.VasCouponDonationRecordService.java
 * @Class Name    : VasCouponDonationRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface VasCouponDonationRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param vasCouponDonationRecord 持久化的数据对象
	 * @return				: void
	 */
	int insertVasCouponDonationRecord(VasCouponDonationRecord vasCouponDonationRecord);

	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	VasCouponDonationRecord
	 */
	VasCouponDonationRecord findVasCouponDonationRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponDonationRecord 检索条件
	 * @return	List<VasCouponDonationRecord>
	 */
	List<VasCouponDonationRecord> findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponDonationRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<VasCouponDonationRecord>
	 */
	List<VasCouponDonationRecord> findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param vasCouponDonationRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<VasCouponDonationRecord>
	 */
	Pager findVasCouponDonationRecordList(VasCouponDonationRecord vasCouponDonationRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param vasCouponDonationRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findVasCouponDonationRecordCount(VasCouponDonationRecord vasCouponDonationRecord);
}
