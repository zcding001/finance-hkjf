package com.hongkun.finance.sms.service;

import java.util.List;

import com.hongkun.finance.sms.model.SmsWebMsgDetail;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.SmsWebMsgDetailService.java
 * @Class Name    : SmsWebMsgDetailService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SmsWebMsgDetailService {
	
	/**
	 *  @Description    : 通过站内信id查询站内信详细信息
	 *  @Method_Name    : findSmsWebMsgDetailBySmsWebMsgId
	 *  @param id
	 *  @return
	 *  @return         : SmsWebMsgDetail
	 *  @Creation Date  : 2017年6月13日 上午10:32:58 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	SmsWebMsgDetail findSmsWebMsgDetailBySmsWebMsgId(int id);
	
	/**
	 * @Described			: 单条插入
	 * @param smsWebMsgDetail 持久化的数据对象
	 * @return				: void
	 */
	void insertSmsWebMsgDetail(SmsWebMsgDetail smsWebMsgDetail);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SmsWebMsgDetail> 批量插入的数据
	 * @return				: void
	 */
	void insertSmsWebMsgDetailBatch(List<SmsWebMsgDetail> list);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SmsWebMsgDetail> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	void insertSmsWebMsgDetailBatch(List<SmsWebMsgDetail> list, int count);
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SmsWebMsgDetail
	 */
	SmsWebMsgDetail findSmsWebMsgDetailById(int id);
}
