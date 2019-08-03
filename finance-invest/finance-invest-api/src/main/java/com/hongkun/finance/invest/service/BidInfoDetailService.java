package com.hongkun.finance.invest.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.invest.model.BidInfoDetail;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.BidInfoDetailService.java
 * @Class Name    : BidInfoDetailService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface BidInfoDetailService {
	
	/**
	 * @Described			: 单条插入
	 * @param bidInfoDetail 持久化的数据对象
	 * @return				: void
	 */
	int insertBidInfoDetail(BidInfoDetail bidInfoDetail);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidInfoDetail> 批量插入的数据
	 * @return				: void
	 */
	void insertBidInfoDetailBatch(List<BidInfoDetail> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<BidInfoDetail> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertBidInfoDetailBatch(List<BidInfoDetail> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param bidInfoDetail 要更新的数据
	 * @return				: void
	 */
	Integer updateBidInfoDetail(BidInfoDetail bidInfoDetail);
	
	/**
	 * @Described			: 批量更新数据
	 * @param bidInfoDetail 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateBidInfoDetailBatch(List<BidInfoDetail> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	BidInfoDetail
	 */
	BidInfoDetail findBidInfoDetailById(int id);
	/**
	 *  @Description    : 通过标的Id查询详情
	 *  @Method_Name    : findBidInfoDetailByRegUserId
	 *  @param bidId
	 *  @return         : BidInfoDetail
	 *  @Creation Date  : 2017年6月16日 下午5:42:47 
	 *  @Author         : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	BidInfoDetail findBidInfoDetailByBidId(int bidId);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidInfoDetail 检索条件
	 * @return	List<BidInfoDetail>
	 */
	List<BidInfoDetail> findBidInfoDetailList(BidInfoDetail bidInfoDetail);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidInfoDetail 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<BidInfoDetail>
	 */
	List<BidInfoDetail> findBidInfoDetailList(BidInfoDetail bidInfoDetail, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param bidInfoDetail 检索条件
	 * @param pager	分页数据
	 * @return	List<BidInfoDetail>
	 */
	Pager findBidInfoDetailList(BidInfoDetail bidInfoDetail, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param bidInfoDetail 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findBidInfoDetailCount(BidInfoDetail bidInfoDetail);
}
