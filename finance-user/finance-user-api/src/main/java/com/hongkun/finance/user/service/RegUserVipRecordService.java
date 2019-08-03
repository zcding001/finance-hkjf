package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.RegUserVipRecord;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.RegUserVipRecordService.java
 * @Class Name    : RegUserVipRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RegUserVipRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param regUserVipRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertRegUserVipRecord(RegUserVipRecord regUserVipRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserVipRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertRegUserVipRecordBatch(List<RegUserVipRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RegUserVipRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRegUserVipRecordBatch(List<RegUserVipRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param regUserVipRecord 要更新的数据
	 * @return				: void
	 */
	void updateRegUserVipRecord(RegUserVipRecord regUserVipRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param regUserVipRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRegUserVipRecordBatch(List<RegUserVipRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RegUserVipRecord
	 */
	RegUserVipRecord findRegUserVipRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserVipRecord 检索条件
	 * @return	List<RegUserVipRecord>
	 */
	List<RegUserVipRecord> findRegUserVipRecordList(RegUserVipRecord regUserVipRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserVipRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<RegUserVipRecord>
	 */
	List<RegUserVipRecord> findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param regUserVipRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<RegUserVipRecord>
	 */
	Pager findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param regUserVipRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRegUserVipRecordCount(RegUserVipRecord regUserVipRecord);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRegUserVipRecordList(RegUserVipRecord regUserVipRecord, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findRegUserVipRecordCount(RegUserVipRecord regUserVipRecord, String sqlName);
	
	/**
	*  查询最新的用户vip记录
	*  @Method_Name             ：findRegUserVipRecordListByRegUserIds
	*  @param ids
	*  @return java.util.List<com.hongkun.finance.user.model.RegUserVipRecord>
	*  @Creation Date           ：2018/6/7
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	List<RegUserVipRecord> findRegUserVipRecordListByRegUserIds(List<Integer> ids);
}
