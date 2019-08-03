package com.hongkun.finance.activity.service;

import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import jdk.nashorn.internal.ir.annotations.Reference;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : LotteryActivityService.java
 * @Class Name    : LotteryActivityService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface LotteryActivityService {
	
	/**
	 * @Described			: 单条插入
	 * @param lotteryActivity 持久化的数据对象
	 * @return				: void
	 */
	void insertLotteryActivity(LotteryActivity lotteryActivity);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryActivity> 批量插入的数据
	 * @return				: void
	 */
	void insertLotteryActivityBatch(List<LotteryActivity> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryActivity> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertLotteryActivityBatch(List<LotteryActivity> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param lotteryActivity 要更新的数据
	 * @return				: void
	 */
	void updateLotteryActivity(LotteryActivity lotteryActivity);
	
	/**
	 * @Described			: 批量更新数据
	 * @param lotteryActivity 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateLotteryActivityBatch(List<LotteryActivity> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	LotteryActivity
	 */
	LotteryActivity findLotteryActivityById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryActivity 检索条件
	 * @return	List<LotteryActivity>
	 */
	List<LotteryActivity> findLotteryActivityList(LotteryActivity lotteryActivity);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryActivity 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<LotteryActivity>
	 */
	List<LotteryActivity> findLotteryActivityList(LotteryActivity lotteryActivity, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryActivity 检索条件
	 * @param pager	分页数据
	 * @return	List<LotteryActivity>
	 */
	Pager findLotteryActivityList(LotteryActivity lotteryActivity, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param lotteryActivity 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findLotteryActivityCount(LotteryActivity lotteryActivity);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findLotteryActivityList(LotteryActivity lotteryActivity, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findLotteryActivityCount(LotteryActivity lotteryActivity, String sqlName);

	/**
	 *  @Description    ：条件查询抽奖活动
	 *  @Method_Name    ：findLotteryActivityWithCondition
	 *  @param lotteryActivity
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年09月26日 16:24
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    Pager findLotteryActivityWithCondition(LotteryActivity lotteryActivity, Pager pager);

	/**
	 *  @Description    ：保存抽奖活动
	 *  @Method_Name    ：saveLotteryActivity
	 *  @param lotteryActivity
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018年09月27日 10:03
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	ResponseEntity<?> saveLotteryActivity(LotteryActivity lotteryActivity);

	/**
	 *  @Description    ：获取用户抽奖活动列表
	 *  @Method_Name    ：findLotteryActivityByTel
	 *  @param login
	 *  @return java.util.List<com.hongkun.finance.activity.model.LotteryActivity>
	 *  @Creation Date  ：2018年10月15日 11:13
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	List<LotteryActivity> findLotteryActivityByTel(Long login);

	/**
	 *  @Description    ：获取抽奖记录列表
	 *  @Method_Name    ：findLotteryRecordWithCondition
	 *  @param vo
	 *  @param pager
	 *  @return com.yirun.framework.core.utils.pager.Pager
	 *  @Creation Date  ：2018年10月22日 16:26
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Pager findLotteryRecordWithCondition(LotteryRecordVo vo, Pager pager);
}
