package com.hongkun.finance.sms.service;

import com.hongkun.finance.sms.model.SmsTelMsg;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.service.SmsTelMsgService.java
 * @Class Name    : SmsTelMsgService.java
 * @Description   : GENERATOR SERVICE类
 * @Author        : generator
 */
public interface SmsTelMsgService {
	
	/**
	 *  @Description    : 插入短信验证码
	 *  @Method_Name    : insertSmsTelMsgForSmsCode
	 *  @param login    ：手机号
	 *  @param prefix   ：登录验证码前缀
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月9日 上午9:02:19 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertSmsTelMsgForSmsCode(String login, String prefix);
	
	/**
	 *  @Description    : 验证短信验证码有效性
	 *  @Method_Name    : validateCode
	 *  @param login 手机号
	 *  @param smsCode 验证码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月8日 下午3:39:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> validateCode(long login, String smsCode);
	
	/**
	*  验证短信验证码是否存在，不关心是否已经失效
	*  @Method_Name             ：validateExist
	*  @param login             ：手机号
	*  @param smsCode           ：短信验证码
	*  @return boolean
	*  @Creation Date           ：2018/5/14
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    boolean validateExist(long login, String smsCode);

	
	/**
	 *  @Description    : 同步实时短信信息（同步）
	 *  @Method_Name    : insertSmsTelMsgSync
	 *  @param smsTelMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年6月8日 下午6:38:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertSmsTelMsg(SmsTelMsg smsTelMsg);
	
	
	/**
	 *  @Description    : 同步实时短信信息（同步）
	 *  @Method_Name    : insertSmsTelMsgSync
	 *  @param smsTelMsg 短信内容
	 *  @param suffix 短信后缀
	 *  @return         : void
	 *  @Creation Date  : 2017年6月8日 下午6:38:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertSmsTelMsgWithSuffix(SmsTelMsg smsTelMsg, String suffix);

	/**
	 *  @Description    : 同步实时短信信息（同步）
	 *  @Method_Name    : insertSmsTelMsgSync
	 *  @param smsTelMsg  短信内容
	 *  @param suffix 短信后缀
	 *  @param extCode 扩展码
	 *  @param time 定时时间
	 *  @param rrid 唯一标识
	 *  @param msgfmt 内容编码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年6月8日 下午6:55:51 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	ResponseEntity<?> insertSmsTelMsg(SmsTelMsg smsTelMsg, String suffix, String extCode, String time, String rrid, String msgfmt);
	
	/**
	 * @Described			: 批量插入，同时发送短信，返回失败的信息列表
	 * @param List<SmsTelMsg> 批量插入的数据
	 * @param count 多少条数提交一次
	 * @return				: void
	 */
	ResponseEntity<?> insertSmsTelMsgBatch(List<SmsTelMsg> list, int count);
	
	/**
	 * @Described			: 更新数据
	 * @param smsTelMsg 要更新的数据
	 * @return				: void
	 */
	void updateSmsTelMsg(SmsTelMsg smsTelMsg);
	
	
	/**
	 * @Described			: 通过id查询数据
	 * @param id id值
	 * @return	SmsTelMsg
	 */
	SmsTelMsg findSmsTelMsgById(int id);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsTelMsg 检索条件
	 * @return	List<SmsTelMsg>
	 */
	List<SmsTelMsg> findSmsTelMsgList(SmsTelMsg smsTelMsg);
	
	/**
	 * @Described			: 条件检索数据
	 * @param smsTelMsg 检索条件
	 * @param pager	分页数据
	 * @return	List<SmsTelMsg>
	 */
	Pager findSmsTelMsgList(SmsTelMsg smsTelMsg, Pager pager);

	/**
	 * @Described			: 统计条数
	 * @param smsTelMsg 检索条件
	 * @param pager	分页数据
	 * @return	int
	 */
	int findSmsTelMsgCount(SmsTelMsg smsTelMsg);

	/**
	*  直插入短信内容
	*  @Method_Name             ：insertSmsTelMsgOnly
	*  @param smsTelMsg
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date           ：2018/6/5
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    void insertSmsTelMsgOnly(SmsTelMsg smsTelMsg);

    /**
     *  @Description    ：重发手机短信
     *  @Method_Name    ：retryTelSms
     *  @param id 		手机短信消息记录id
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018/9/20
     *  @Author         ：pengwu@hongkun.com.cn
     */
	ResponseEntity<?> retryTelSms(int id);
}
