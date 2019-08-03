package com.hongkun.finance.monitor.service;

import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.jms.polling.JmsFailMsg;

/**
 * @Description   : JMS异常消息监控
 * @Project       : finance-monitor-api
 * @Program Name  : com.hongkun.finance.monitor.JmsMonitorService.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public interface JmsMonitorService {

	/**
	 *  @Description    : 条件检索异常的JMS消息
	 *  @Method_Name    : findJmsFailMsgList
	 *  @param jmsFailMsg
	 *  @return         : List<JmsFailMsg>
	 *  @Creation Date  : 2017年11月22日 上午10:45:25 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> findJmsFailMsgList(JmsFailMsg jmsFailMsg);
	
	/**
	 *  @Description    : 批量修复JMS异常消息
	 *  @Method_Name    : recoverJmsFailMsg
	 *  @param codes
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月22日 上午10:47:01 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> recoverJmsFailMsg(String codes);
	
	/**
	*  删除指定的JMS
	*  @Method_Name             ：delJmsFailMsg
	*  @param codes
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date           ：2018/5/30
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	ResponseEntity<?> delJmsFailMsg(String codes);
}
