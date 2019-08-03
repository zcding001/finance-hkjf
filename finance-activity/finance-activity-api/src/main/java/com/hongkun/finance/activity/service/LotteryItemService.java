package com.hongkun.finance.activity.service;

import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.model.vo.LotteryActivityItemsVo;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : LotteryItemService.java
 * @Class Name    : LotteryItemService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface LotteryItemService {
	
	/**
	 * @Described			: 单条插入
	 * @param lotteryItem 持久化的数据对象
	 * @return				: void
	 */
	void insertLotteryItem(LotteryItem lotteryItem);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryItem> 批量插入的数据
	 * @return				: void
	 */
	void insertLotteryItemBatch(List<LotteryItem> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryItem> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertLotteryItemBatch(List<LotteryItem> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param lotteryItem 要更新的数据
	 * @return				: void
	 */
	void updateLotteryItem(LotteryItem lotteryItem);
	
	/**
	 * @Described			: 批量更新数据
	 * @param lotteryItem 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateLotteryItemBatch(List<LotteryItem> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	LotteryItem
	 */
	LotteryItem findLotteryItemById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryItem 检索条件
	 * @return	List<LotteryItem>
	 */
	List<LotteryItem> findLotteryItemList(LotteryItem lotteryItem);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryItem 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<LotteryItem>
	 */
	List<LotteryItem> findLotteryItemList(LotteryItem lotteryItem, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryItem 检索条件
	 * @param pager	分页数据
	 * @return	List<LotteryItem>
	 */
	Pager findLotteryItemList(LotteryItem lotteryItem, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param lotteryItem 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findLotteryItemCount(LotteryItem lotteryItem);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findLotteryItemList(LotteryItem lotteryItem, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);

	Integer findLotteryItemCount(LotteryItem lotteryItem, String sqlName);

	/**
	 *  @Description    ：获取抽奖活动奖项
	 *  @Method_Name    ：getLotteryActivityItems
	 *  @param lotteryActivityId
	 *  @return java.util.List<com.hongkun.finance.activity.model.vo.LotteryActivityItemsVo>
	 *  @Creation Date  ：2018年10月08日 09:58
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    List<LotteryActivityItemsVo> getLotteryActivityItems(Integer lotteryActivityId);

	/**
	 *  @Description    ：删除奖项
	 *  @Method_Name    ：deleteLotteryItemsById
	 *  @param tempId
	 *  @param state
	 *  @return void
	 *  @Creation Date  ：2018年10月08日 14:25
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	void deleteLotteryItemsById(Integer tempId, int state);

	/**
	 *  @Description    ：删除用户群
	 *  @Method_Name    ：deleteLotteryItemsByGroupAndLocationFlag
	 *  @param activityId
	 *  @param tempDelLotsGroup
	 *  @param tempDelLocationFlag
	 *  @param state
	 *  @return void
	 *  @Creation Date  ：2018年10月08日 14:27
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	void deleteLotteryItemsByGroupAndLocationFlag(Integer activityId, Integer tempDelLotsGroup, Integer tempDelLocationFlag, int state);

	/**
	 *  @Description    ：根据活动id获取用户群
	 *  @Method_Name    ：getLotteryItemGroupById
	 *  @param id
	 *  @return java.util.List<java.lang.Integer>
	 *  @Creation Date  ：2018年10月15日 17:34
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	List<Integer> getLotteryItemGroupById(Integer id);

	/**
	 *  @Description    ：根据活动id获取京籍/非京籍标识
	 *  @Method_Name    ：getLotteryItemLocationFlagById
	 *  @param id
	 *  @return java.util.List<java.lang.Integer>
	 *  @Creation Date  ：2018年10月15日 17:34
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	List<Integer> getLotteryItemLocationFlagById(Integer id);
}
