package com.hongkun.finance.sms.service;

import java.util.List;

import com.yirun.framework.core.utils.pager.Pager;
import com.hongkun.finance.sms.model.SmsWebMsg;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.SmsWebMsgService.java
 * @Class Name    : SmsWebMsgService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SmsWebMsgService {
	
	/**
	 * @Described			: 单条插入,同时插入SmsWebDetailMsg信息
	 * @param smsWebMsg 持久化的数据对象
	 * @return				: void
	 */
	void insertSmsWebMsg(SmsWebMsg smsWebMsg);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SmsWebMsg> 批量插入的数据
	 * @return				: void
	 */
	void insertSmsWebMsgBatch(List<SmsWebMsg> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SmsWebMsg> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertSmsWebMsgBatch(List<SmsWebMsg> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param smsWebMsg 要更新的数据
	 * @return				: void
	 */
	int updateSmsWebMsg(SmsWebMsg smsWebMsg);
	
	/**
	 * @Described			: 批量更新数据
	 * @param smsWebMsg 要更新的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void updateSmsWebMsgBatch(List<SmsWebMsg> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SmsWebMsg
	 */
	SmsWebMsg findSmsWebMsgById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsWebMsg 检索条件
	 * @return	List<SmsWebMsg>
	 */
	List<SmsWebMsg> findSmsWebMsgList(SmsWebMsg smsWebMsg);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsWebMsg 检索条件
	 * @param start	起始页
	 * @param limit 检索条数
	 * @return	List<SmsWebMsg>
	 */
	List<SmsWebMsg> findSmsWebMsgList(SmsWebMsg smsWebMsg, int start, int limit);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsWebMsg 检索条件
	 * @param pager	分页数据
	 * @return	List<SmsWebMsg>
	 */
	Pager findSmsWebMsgList(SmsWebMsg smsWebMsg, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param smsWebMsg 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findSmsWebMsgCount(SmsWebMsg smsWebMsg);
	
	/**
	 * @Described			: 条件检索数据,含有详细信息
	 * @param smsWebMsg 检索条件
	 * @param pager	分页数据
	 * @return	List<SmsWebMsg>
	 */
	Pager findSmsWebMsgWithDetailList(SmsWebMsg smsWebMsg, Pager pager);
	
	/**
	 *  @Description    : 批量更新站内信
	 *  @Method_Name    : updateSmsWebMsg
	 *  @param ids		: 站内信id集合，中间,区分
	 *  @param state	: 更新状态
	 *  @param regUserId
	 *  @return         : int
	 *  @Creation Date  : 2018年3月8日 下午2:58:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	int updateSmsWebMsg(String[] ids, Integer state, Integer regUserId);
	
	/**
	 *  @Description    : 查询未读的站内信条数
	 *  @Method_Name    : findUnreadWebMsg
	 *  @param regUser
	 *  @return         : int
	 *  @Creation Date  : 2018年3月8日 下午3:11:00 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	int findUnreadWebMsg(Integer regUser);
}
