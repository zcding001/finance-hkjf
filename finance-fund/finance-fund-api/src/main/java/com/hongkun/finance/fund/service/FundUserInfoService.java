package com.hongkun.finance.fund.service;

import com.hongkun.finance.fund.model.FundUserInfo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.FundUserInfoService.java
 * @Class Name    : FundUserInfoService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface FundUserInfoService {
	
	/**
	 * @Described			: 单条插入
	 * @param fundUserInfo 持久化的数据对象
	 * @return				: void
	 */
	void insertFundUserInfo(FundUserInfo fundUserInfo);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundUserInfo> 批量插入的数据
	 * @return				: void
	 */
	void insertFundUserInfoBatch(List<FundUserInfo> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<FundUserInfo> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertFundUserInfoBatch(List<FundUserInfo> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param fundUserInfo 要更新的数据
	 * @return				: void
	 */
	void updateFundUserInfo(FundUserInfo fundUserInfo);
	
	/**
	 * @Described			: 批量更新数据
	 * @param fundUserInfo 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateFundUserInfoBatch(List<FundUserInfo> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	FundUserInfo
	 */
	FundUserInfo findFundUserInfoById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundUserInfo 检索条件
	 * @return	List<FundUserInfo>
	 */
	List<FundUserInfo> findFundUserInfoList(FundUserInfo fundUserInfo);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundUserInfo 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<FundUserInfo>
	 */
	List<FundUserInfo> findFundUserInfoList(FundUserInfo fundUserInfo, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param fundUserInfo 检索条件
	 * @param pager	分页数据
	 * @return	List<FundUserInfo>
	 */
	Pager findFundUserInfoList(FundUserInfo fundUserInfo, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param fundUserInfo 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findFundUserInfoCount(FundUserInfo fundUserInfo);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findFundUserInfoList(FundUserInfo fundUserInfo, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findFundUserInfoCount(FundUserInfo fundUserInfo, String sqlName);

	/**
	 *  @Description    ：获取海外基金用户详情
	 *  @Method_Name    ：findFundUserInfo
	 *  @param fundUserInfo
	 *  @return com.hongkun.finance.fund.model.FundUserInfo
	 *  @Creation Date  ：2018年07月05日 11:08
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    FundUserInfo findFundUserInfo(FundUserInfo fundUserInfo);
}
