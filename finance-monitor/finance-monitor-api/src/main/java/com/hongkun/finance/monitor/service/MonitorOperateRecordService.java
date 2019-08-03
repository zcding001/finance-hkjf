package com.hongkun.finance.monitor.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.monitor.model.MonitorOperateRecord;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.monitor.service.MonitorOperateRecordService.java
 * @Class Name    : MonitorOperateRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface MonitorOperateRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param monitorOperateRecord 持久化的数据对象
	 * @return				: void
	 */
	void insertMonitorOperateRecord(MonitorOperateRecord monitorOperateRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<MonitorOperateRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertMonitorOperateRecordBatch(List<MonitorOperateRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<MonitorOperateRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertMonitorOperateRecordBatch(List<MonitorOperateRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param monitorOperateRecord 要更新的数据
	 * @return				: void
	 */
	void updateMonitorOperateRecord(MonitorOperateRecord monitorOperateRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param monitorOperateRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateMonitorOperateRecordBatch(List<MonitorOperateRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	MonitorOperateRecord
	 */
	MonitorOperateRecord findMonitorOperateRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorOperateRecord 检索条件
	 * @return	List<MonitorOperateRecord>
	 */
	List<MonitorOperateRecord> findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorOperateRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<MonitorOperateRecord>
	 */
	List<MonitorOperateRecord> findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorOperateRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<MonitorOperateRecord>
	 */
	Pager findMonitorOperateRecordList(MonitorOperateRecord monitorOperateRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param monitorOperateRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findMonitorOperateRecordCount(MonitorOperateRecord monitorOperateRecord);
}
