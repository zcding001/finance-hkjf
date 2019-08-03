package com.hongkun.finance.user.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.user.model.AppLoginLog;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.AppLoginLogService.java
 * @Class Name    : AppLoginLogService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface AppLoginLogService {
	
	/**
	 * @Described			: 单条插入
	 * @param appLoginLog 持久化的数据对象
	 * @return				: void
	 */
	void insertAppLoginLog(AppLoginLog appLoginLog);
	
	/**
	 * @Described			: 批量插入
	 * @param List<AppLoginLog> 批量插入的数据
	 * @return				: void
	 */
	void insertAppLoginLogBatch(List<AppLoginLog> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<AppLoginLog> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertAppLoginLogBatch(List<AppLoginLog> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param appLoginLog 要更新的数据
	 * @return				: void
	 */
	void updateAppLoginLog(AppLoginLog appLoginLog);
	
	/**
	 * @Described			: 批量更新数据
	 * @param appLoginLog 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateAppLoginLogBatch(List<AppLoginLog> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	AppLoginLog
	 */
	AppLoginLog findAppLoginLogById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appLoginLog 检索条件
	 * @return	List<AppLoginLog>
	 */
	List<AppLoginLog> findAppLoginLogList(AppLoginLog appLoginLog);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appLoginLog 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<AppLoginLog>
	 */
	List<AppLoginLog> findAppLoginLogList(AppLoginLog appLoginLog, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appLoginLog 检索条件
	 * @param pager	分页数据
	 * @return	List<AppLoginLog>
	 */
	Pager findAppLoginLogList(AppLoginLog appLoginLog, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param appLoginLog 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findAppLoginLogCount(AppLoginLog appLoginLog);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findAppLoginLogList(AppLoginLog appLoginLog, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findAppLoginLogCount(AppLoginLog appLoginLog, String sqlName);

	/**
	 * 根据reguser来修改AppLogin
	 * @param unhandLog
	 */
    void updateAppLoginLogByUserId(AppLoginLog unhandLog);

}
