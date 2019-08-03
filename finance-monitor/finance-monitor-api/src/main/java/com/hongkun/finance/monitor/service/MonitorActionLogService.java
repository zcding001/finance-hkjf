package com.hongkun.finance.monitor.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.monitor.model.MonitorActionLog;

/**
 * @Project       : user
 * @Program Name  : com.hongkun.finance.monitor.service.MonitorActionLogService.java
 * @Class Name    : MonitorActionLogService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface MonitorActionLogService {
	
	/**
	 * @Described			: 单条插入
	 * @param monitorActionLog 持久化的数据对象
	 * @return				: void
	 */
	void insertMonitorActionLog(MonitorActionLog monitorActionLog);
	
	/**
	 * @Described			: 批量插入
	 * @param List<MonitorActionLog> 批量插入的数据
	 * @return				: void
	 */
	void insertMonitorActionLogBatch(List<MonitorActionLog> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<MonitorActionLog> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertMonitorActionLogBatch(List<MonitorActionLog> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param monitorActionLog 要更新的数据
	 * @return				: void
	 */
	void updateMonitorActionLog(MonitorActionLog monitorActionLog);
	
	/**
	 * @Described			: 批量更新数据
	 * @param monitorActionLog 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateMonitorActionLogBatch(List<MonitorActionLog> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	MonitorActionLog
	 */
	MonitorActionLog findMonitorActionLogById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorActionLog 检索条件
	 * @return	List<MonitorActionLog>
	 */
	List<MonitorActionLog> findMonitorActionLogList(MonitorActionLog monitorActionLog);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorActionLog 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<MonitorActionLog>
	 */
	List<MonitorActionLog> findMonitorActionLogList(MonitorActionLog monitorActionLog, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param monitorActionLog 检索条件
	 * @param pager	分页数据
	 * @return	List<MonitorActionLog>
	 */
	Pager findMonitorActionLogList(MonitorActionLog monitorActionLog, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param monitorActionLog 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findMonitorActionLogCount(MonitorActionLog monitorActionLog);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findMonitorActionLogList(MonitorActionLog monitorActionLog, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findMonitorActionLogCount(MonitorActionLog monitorActionLog, String sqlName);
}
