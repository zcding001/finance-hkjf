package com.hongkun.finance.roster.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.roster.model.RosDepositInfo;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.roster.service.RosDepositInfoService.java
 * @Class Name    : RosDepositInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface RosDepositInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param rosDepositInfo 持久化的数据对象
	 * @return				: void
	 */
	int insertRosDepositInfo(RosDepositInfo rosDepositInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosDepositInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertRosDepositInfoBatch(List<RosDepositInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<RosDepositInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertRosDepositInfoBatch(List<RosDepositInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param rosDepositInfo 要更新的数据
	 * @return				: void
	 */
	int updateRosDepositInfo(RosDepositInfo rosDepositInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param rosDepositInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateRosDepositInfoBatch(List<RosDepositInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	RosDepositInfo
	 */
	RosDepositInfo findRosDepositInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosDepositInfo 检索条件
	 * @return	List<RosDepositInfo>
	 */
	List<RosDepositInfo> findRosDepositInfoList(RosDepositInfo rosDepositInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosDepositInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<RosDepositInfo>
	 */
	List<RosDepositInfo> findRosDepositInfoList(RosDepositInfo rosDepositInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param rosDepositInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<RosDepositInfo>
	 */
	Pager findRosDepositInfoList(RosDepositInfo rosDepositInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param rosDepositInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findRosDepositInfoCount(RosDepositInfo rosDepositInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findRosDepositInfoList(RosDepositInfo rosDepositInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 自定义sql查询统计信息
	 *  @Method_Name    : getTotalCount
	 *  @param object
	 *  @param sqlName
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findRosDepositInfoCount(RosDepositInfo rosDepositInfo, String sqlName);
	
}
