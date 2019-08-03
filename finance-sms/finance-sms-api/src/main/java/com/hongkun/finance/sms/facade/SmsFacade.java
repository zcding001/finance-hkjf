package com.hongkun.finance.sms.facade;

import com.hongkun.finance.sms.model.SmsWebMsg;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

public interface SmsFacade {

	/**
	 *  @Description    : 给指定的用户集合发送短信
	 *  @Method_Name    : sendTelMsg
	 *  @param ids	用户更集合，多个用户id中间用,分割
	 *  @param msg	短信消息
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月11日 下午4:15:58 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public ResponseEntity<?> sendTelMsg(String ids, String msg);
	
	/**
	 *  @Description    : 条件检索站内信信息
	 *  @Method_Name    : findWebMsgList
	 *  @param pager
	 *  @param smsWebMsg
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月16日 下午1:38:36 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public ResponseEntity<?> findWebMsgList(Pager pager, SmsWebMsg smsWebMsg);

    /**
     *  批量发送短信
     *  @Method_Name    ：sendTelMsgBatch
     *  @param msg      ：短信内容
     *  @param userGroup：用户群体
     *  @return void
     *  @Creation Date  ：2018/4/24
     *  @Author         ：zhichaoding@hongkun.com.cn
     */
    void sendTelMsgBatch(String msg, Integer userGroup);
}
