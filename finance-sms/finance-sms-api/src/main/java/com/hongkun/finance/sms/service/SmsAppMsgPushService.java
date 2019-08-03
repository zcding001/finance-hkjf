package com.hongkun.finance.sms.service;

import java.util.Date;
import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.sms.model.SmsAppMsgPush;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.sms.service.SmsAppMsgPushService.java
 * @Class Name    : SmsAppMsgPushService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SmsAppMsgPushService {
	
	/**
	 * @Described			: 单条插入
	 * @param smsAppMsgPush 持久化的数据对象
	 * @return				: int
	 */
	int insertSmsAppMsgPush(SmsAppMsgPush smsAppMsgPush);
	
	/**
	 * @Described			: 批量插入
	 * @param list 批量插入的数据
	 * @return				: void
	 */
	void insertSmsAppMsgPushBatch(List<SmsAppMsgPush> list);
	
	/**
	 * @Described			: 批量插入
	 * @param list 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertSmsAppMsgPushBatch(List<SmsAppMsgPush> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param smsAppMsgPush 要更新的数据
	 * @return				: int
	 */
	int updateSmsAppMsgPush(SmsAppMsgPush smsAppMsgPush);
	
	/**
	 * @Described			: 批量更新数据
	 * @param list 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateSmsAppMsgPushBatch(List<SmsAppMsgPush> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SmsAppMsgPush
	 */
	SmsAppMsgPush findSmsAppMsgPushById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsAppMsgPush 检索条件
	 * @return	List<SmsAppMsgPush>
	 */
	List<SmsAppMsgPush> findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsAppMsgPush 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<SmsAppMsgPush>
	 */
	List<SmsAppMsgPush> findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsAppMsgPush 检索条件
	 * @param pager	分页数据
	 * @return	List<SmsAppMsgPush>
	 */
	Pager findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param smsAppMsgPush 检索条件
	 * @return	int
	 */
	int findSmsAppMsgPushCount(SmsAppMsgPush smsAppMsgPush);
	
	/**
	 * 自定义sql查询count
	 * @param smsAppMsgPush
	 * @param pager
	 * @param sqlName
	 * @return
	 */
	Pager findSmsAppMsgPushList(SmsAppMsgPush smsAppMsgPush, Pager pager, String sqlName/*添加一个自定义的sql的名字*/);
	
	/**
	 * 
	 *  @Description    : 
	 *  @Method_Name    : getTotalCount
	 *  @param smsAppMsgPush
	 *  @param sqlName
	 *  @return
	 *  @return         : Integer
	 *  @Creation Date  : 2017年6月15日 上午10:14:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	Integer findSmsAppMsgPushCount(SmsAppMsgPush smsAppMsgPush, String sqlName);


	/**
	 *  @Description    : 添加app端推送消息
	 *  @Method_Name    : addAppMsgPush
	 *  @param smsAppMsgPush
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月02日 下午13:05:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
    ResponseEntity addAppMsgPush(SmsAppMsgPush smsAppMsgPush);

	/**
	 *  @Description    : 删除app端推送消息
	 *  @Method_Name    : deleteAppMsgPush
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月05日 上午11:23:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
    ResponseEntity deleteAppMsgPush(int id);

	/**
	 *  @Description    : 停止app端推送消息
	 *  @Method_Name    : stopAppMsgPush
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月05日 上午11:36:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	ResponseEntity stopAppMsgPush(int id);

	/**
	 *  @Description    : 停止app端推送消息
	 *  @Method_Name    : stop
	 *  @param smsAppMsgPush
	 *  @return         : String
	 *  @Creation Date  : 2018年02月06日 上午11:00:00
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	boolean stop(SmsAppMsgPush smsAppMsgPush);

	/**
	 *  @Description    ：定时执行app消息推送
	 *  @Method_Name    ：appMsgPush
	 *  @param currentDate  当前时间
	 *  @param shardingItem
	 *  @return void
	 *  @Creation Date  ：2018/4/27
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
    void appMsgPush(Date currentDate, int shardingItem);
}
