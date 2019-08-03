package com.hongkun.finance.activity.service;

import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.model.LotteryRecord;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : LotteryRecordService.java
 * @Class Name    : LotteryRecordService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface LotteryRecordService {
	
	/**
	 * @Described			: 单条插入
	 * @param lotteryRecord 持久化的数据对象
	 * @return				: void
	 */
	int insertLotteryRecord(LotteryRecord lotteryRecord);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryRecord> 批量插入的数据
	 * @return				: void
	 */
	void insertLotteryRecordBatch(List<LotteryRecord> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<LotteryRecord> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertLotteryRecordBatch(List<LotteryRecord> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param lotteryRecord 要更新的数据
	 * @return				: void
	 */
	int updateLotteryRecord(LotteryRecord lotteryRecord);
	
	/**
	 * @Described			: 批量更新数据
	 * @param lotteryRecord 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateLotteryRecordBatch(List<LotteryRecord> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	LotteryRecord
	 */
	LotteryRecord findLotteryRecordById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryRecord 检索条件
	 * @return	List<LotteryRecord>
	 */
	List<LotteryRecord> findLotteryRecordList(LotteryRecord lotteryRecord);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryRecord 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<LotteryRecord>
	 */
	List<LotteryRecord> findLotteryRecordList(LotteryRecord lotteryRecord, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param lotteryRecord 检索条件
	 * @param pager	分页数据
	 * @return	List<LotteryRecord>
	 */
	Pager findLotteryRecordList(LotteryRecord lotteryRecord, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param lotteryRecord 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findLotteryRecordCount(LotteryRecord lotteryRecord);
	
	/**
	 * 自定义sql查询count
	 * @param obj
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findLotteryRecordList(LotteryRecord lotteryRecord, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
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
	Integer findLotteryRecordCount(LotteryRecord lotteryRecord, String sqlName);


	/**
	 *  @Description    ：随机获取中奖记录
	 *  @Method_Name    ：getLotteryRecordListForShow
	 *  @param lotteryActivity
	 *  @return java.util.List<java.lang.String>
	 *  @Creation Date  ：2018年10月15日 16:01
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
    List<String> getLotteryRecordListForShow(LotteryActivity lotteryActivity);

	/**
	 *  @Description    ：获取当前日期已经抽取的奖品数
	 *  @Method_Name    ：getDayAndTotalCount
	 *  @param record
	 *  @return java.util.Map<java.lang.String,java.lang.Integer>
	 *  @Creation Date  ：2018年10月15日 16:30
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	Map<String,Integer> getDayAndTotalCount(LotteryRecord record);

	/**
	 *  @Description    ：校验兑换码
	 *  @Method_Name    ：checkVerfication
	 *  @param verfication
	 *  @return int
	 *  @Creation Date  ：2018年10月15日 17:17
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	int checkVerfication(String verfication);

	/**
	 *  @Description    ：获取活动详情中奖列表
	 *  @Method_Name    ：findLotteryRecordDetailList
	 *  @param lotteryRecord
	 *  @return java.util.List<com.hongkun.finance.activity.model.vo.LotteryRecordVo>
	 *  @Creation Date  ：2018年10月15日 17:20
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	List<LotteryRecordVo> findLotteryRecordDetailList(LotteryRecord lotteryRecord);

}
