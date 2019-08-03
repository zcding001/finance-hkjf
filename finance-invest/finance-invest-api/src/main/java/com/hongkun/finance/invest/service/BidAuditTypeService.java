package com.hongkun.finance.invest.service;

import com.hongkun.finance.invest.model.BidAuditType;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.service.BidAuditTypeService.java
 * @Class Name    : BidAuditTypeService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidAuditTypeService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidAuditType 持久化的数据对象
	 * @return				: void
	 */
	void insertBidAuditType(BidAuditType bidAuditType);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidAuditType> 批量插入的数据
	 * @return				: void
	 */
	void insertBidAuditTypeBatch(List<BidAuditType> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidAuditType> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidAuditTypeBatch(List<BidAuditType> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidAuditType 要更新的数据
	 * @return				: void
	 */
	void updateBidAuditType(BidAuditType bidAuditType);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidAuditType 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidAuditTypeBatch(List<BidAuditType> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidAuditType
	 */
	BidAuditType findBidAuditTypeById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAuditType 检索条件
	 * @return	List<BidAuditType>
	 */
	List<BidAuditType> findBidAuditTypeList(BidAuditType bidAuditType);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAuditType 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidAuditType>
	 */
	List<BidAuditType> findBidAuditTypeList(BidAuditType bidAuditType, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidAuditType 检索条件
	 * @param pager	分页数据
	 * @return	List<BidAuditType>
	 */
	Pager findBidAuditTypeList(BidAuditType bidAuditType, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidAuditType 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidAuditTypeCount(BidAuditType bidAuditType);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	public Pager findBidAuditTypeList(BidAuditType bidAuditType, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	public Integer findBidAuditTypeCount(BidAuditType bidAuditType, String sqlName);

	/**
	 * 查找认证类型id和name对应
	 * @return
	 */
	List<Map<Integer, String>> findBidAuditTypeIdAndName();

}
