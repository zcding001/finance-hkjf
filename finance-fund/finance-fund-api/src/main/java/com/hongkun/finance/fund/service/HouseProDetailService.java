package com.hongkun.finance.fund.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.fund.model.HouseProDetail;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.HouseProDetailService.java
 * @Class Name    : HouseProDetailService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface HouseProDetailService {
	
	/**
	 * @Described			: 单条插入
	 * @param houseProDetail 持久化的数据对象
	 * @return				: void
	 */
	void insertHouseProDetail(HouseProDetail houseProDetail);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProDetail> 批量插入的数据
	 * @return				: void
	 */
	void insertHouseProDetailBatch(List<HouseProDetail> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<HouseProDetail> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertHouseProDetailBatch(List<HouseProDetail> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param houseProDetail 要更新的数据
	 * @return				: void
	 */
	void updateHouseProDetail(HouseProDetail houseProDetail);
	
	/**
	 * @Described			: 批量更新数据
	 * @param houseProDetail 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateHouseProDetailBatch(List<HouseProDetail> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	HouseProDetail
	 */
	HouseProDetail findHouseProDetailById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProDetail 检索条件
	 * @return	List<HouseProDetail>
	 */
	List<HouseProDetail> findHouseProDetailList(HouseProDetail houseProDetail);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProDetail 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<HouseProDetail>
	 */
	List<HouseProDetail> findHouseProDetailList(HouseProDetail houseProDetail, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param houseProDetail 检索条件
	 * @param pager	分页数据
	 * @return	List<HouseProDetail>
	 */
	Pager findHouseProDetailList(HouseProDetail houseProDetail, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param houseProDetail 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findHouseProDetailCount(HouseProDetail houseProDetail);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findHouseProDetailList(HouseProDetail houseProDetail, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findHouseProDetailCount(HouseProDetail houseProDetail, String sqlName);
}
