package com.hongkun.finance.sms.service;

import java.util.List;

import com.yirun.framework.core.model.ResponseEntity;
import com.hongkun.finance.sms.model.SmsEmailMsg;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.SmsEmailMsgService.java
 * @Class Name    : SmsEmailMsgService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SmsEmailMsgService {
	
	/**
	 * @Described			: 单条插入，同时发送邮件消息
	 * @param smsEmailMsg 持久化的数据对象
	 * @return				: void
	 */
	ResponseEntity<?> insertSmsEmailMsg(SmsEmailMsg smsEmailMsg);
	
	/**
	 * @Described			: 批量插入
	 * @param List<SmsEmailMsg> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: ResponseEntity<?>
	 */
	ResponseEntity<?> insertSmsEmailMsgBatch(List<SmsEmailMsg> list, int count);
}
