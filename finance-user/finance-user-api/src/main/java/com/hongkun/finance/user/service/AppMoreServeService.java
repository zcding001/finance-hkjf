package com.hongkun.finance.user.service;

import com.hongkun.finance.user.model.AppMoreServe;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.AppMoreServeService.java
 * @Class Name    : AppMoreServeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface AppMoreServeService {
	
	/**
	 * @Described			: 单条插入
	 * @param appMoreServe 持久化的数据对象
	 * @return				: void
	 */
	void insertAppMoreServe(AppMoreServe appMoreServe);
	
	/**
	 * @Described			: 批量插入
	 * @param List<AppMoreServe> 批量插入的数据
	 * @return				: void
	 */
	void insertAppMoreServeBatch(List<AppMoreServe> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<AppMoreServe> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertAppMoreServeBatch(List<AppMoreServe> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param appMoreServe 要更新的数据
	 * @return				: void
	 */
	void updateAppMoreServe(AppMoreServe appMoreServe);
	
	/**
	 * @Described			: 批量更新数据
	 * @param appMoreServe 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateAppMoreServeBatch(List<AppMoreServe> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	AppMoreServe
	 */
	AppMoreServe findAppMoreServeById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appMoreServe 检索条件
	 * @return	List<AppMoreServe>
	 */
	List<AppMoreServe> findAppMoreServeList(AppMoreServe appMoreServe);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appMoreServe 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<AppMoreServe>
	 */
	List<AppMoreServe> findAppMoreServeList(AppMoreServe appMoreServe, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param appMoreServe 检索条件
	 * @param pager	分页数据
	 * @return	List<AppMoreServe>
	 */
	Pager findAppMoreServeList(AppMoreServe appMoreServe, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param appMoreServe 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findAppMoreServeCount(AppMoreServe appMoreServe);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findAppMoreServeList(AppMoreServe appMoreServe, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findAppMoreServeCount(AppMoreServe appMoreServe, String sqlName);

	/**
	 * 启用app菜单
	 * @param appMoreServe
	 * @return
	 */
	ResponseEntity enableAppServe(AppMoreServe appMoreServe);

	/**
	 * 禁用app菜单
	 * @param appMoreServe
	 * @return
	 */
	ResponseEntity disableAppServe(AppMoreServe appMoreServe);

	/**
	 * 查询参数数据以外的菜单
	 * @param appMoreServeIds
	 * @return
	 */
	List<AppMoreServe> findOtherServe(List<Integer> appMoreServeIds);
}
