package com.hongkun.finance.web.controller.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.facade.SmsFacade;
import com.hongkun.finance.sms.model.SmsAppMsgPush;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsAppMsgPushService;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.sms.service.SmsWebMsgDetailService;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description   : 消息控制层
 * @Project       : management-financial-services
 * @Program Name  : com.hongkun.finance.web.controller.sms.SmsController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */

@Controller
@RequestMapping("smsController/")
public class SmsController {

	@Reference
	private UserFacade userFacade;
	@Reference
	private RegUserService regUserService;
	@Reference
	private SmsFacade smsFacade;
	@Reference
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private SmsWebMsgDetailService smsWebMsgDetailService;
	@Reference
	private SmsAppMsgPushService smsAppMsgPushService;
	
	/**
	 *  @Description    : 检索需要发送短信的用户
	 *  @Method_Name    : sendTelMsgList
	 *  @param pager
	 *  @param userVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月11日 下午4:13:15 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("sendTelMsgList")
	@ResponseBody
	public ResponseEntity<?> sendTelMsgList(Pager pager, UserVO userVO){
		if ((userVO.getUserGroup() == null || userVO.getUserGroup() == -999)
				&& (userVO.getLogin() == null || userVO.getLogin() <= 0) && StringUtils.isBlank(userVO.getRealName())) {
			return BaseUtil.emptyResult();
		}
		return new ResponseEntity<>(Constants.SUCCESS, this.regUserService.listConditionPage(userVO, pager));
	}
	
	/**
	 *  @Description    : 发送短信
	 *  @Method_Name    : sendTelMsg
	 *  @param ids      : 用户集合
	 *  @param msg      : 短信内容
	 *  @param type 1:正常选择用户发送， 2：所有实名用户，3：所有用户
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月11日 下午4:14:04 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("sendTelMsg")
	@ResponseBody
    @ActionLog(msg = "批量发送短信, 内容: {args[1]}")
	public ResponseEntity<?> sendTelMsg(String ids, String msg){
		if(StringUtils.isBlank(ids)){
			return new ResponseEntity<>(Constants.ERROR, "请选择短息接收的用户!");
		}
		return smsFacade.sendTelMsg(ids, msg);
	}

	/**
	*  批量发送短信
	*  @Method_Name    ：sendTelBatch
	*  @param msg      : 短信内容
	*  @param userGroup: 用户群体
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/4/24
	*  @Author         ：zhichaoding@hongkun.com.cn
	*/
    @RequestMapping("sendTelMsgBatch")
    @ResponseBody
    @ActionLog(msg = "批量发送短信, 内容: {args[0]}")
	public ResponseEntity<?> sendTelMsgBatch(String msg, Integer userGroup) {
        smsFacade.sendTelMsgBatch(msg, userGroup);
	    return new ResponseEntity<>(Constants.SUCCESS, "短信已发送，耐心等待");
    }
	
	/**
	 *  @Description    : 检索用户的短信验证码
	 *  @Method_Name    : authCodeTelMsgList
	 *  @param login	手机号
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月16日 上午11:20:27 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("authCodeTelMsgList")
	@ResponseBody
	public ResponseEntity<?> authCodeTelMsgList(String login){
		if(StringUtils.isBlank(login)){
			return new ResponseEntity<>(SUCCESS, null);
		}
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if(result.getResStatus() == Constants.ERROR){
			return result;
		}
		SmsTelMsg smsTelMsg = new SmsTelMsg(Long.parseLong(login), null, SmsConstants.SMS_TYPE_VALIDATE_CODE);
		smsTelMsg.setSortColumns("create_time DESC");
		return new ResponseEntity<>(SUCCESS, this.smsTelMsgService.findSmsTelMsgList(smsTelMsg));
	}
	
	/**
	 *  @Description    : 条件检索站内信息信息
	 *  @Method_Name    : webMsgList
	 *  @param pager
	 *  @param smsWebMsg
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月16日 下午1:37:16 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("webMsgList")
	@ResponseBody
	public ResponseEntity<?> webMsgList(Pager pager, SmsWebMsg smsWebMsg){
		return this.smsFacade.findWebMsgList(pager, smsWebMsg);
	}
	
	/**
	 *  @Description    : 检索站内信详情
	 *  @Method_Name    : webMsgDetail
	 *  @param id			
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年10月16日 下午2:16:08 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("webMsgDetail")
	@ResponseBody
	public ResponseEntity<?> webMsgDetail(int id){
		return new ResponseEntity<>(SUCCESS, this.smsWebMsgDetailService.findSmsWebMsgDetailBySmsWebMsgId(id));
	}

	/**
	 *  @Description    : 检索app端推送消息
	 *  @Method_Name    : appMsgPushList
	 *  @param pager
	 *  @param smsAppMsgPush
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月01日 下午18:05:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	@RequestMapping("appMsgPushList")
	@ResponseBody
	public ResponseEntity appMsgPushList(Pager pager, SmsAppMsgPush smsAppMsgPush){
		smsAppMsgPush.setSortColumns("create_time desc");
		return new ResponseEntity<>(SUCCESS,this.smsAppMsgPushService.findSmsAppMsgPushList(smsAppMsgPush,pager));
	}

	/**
	 *  @Description    : 添加app端推送消息
	 *  @Method_Name    : addAppMsgPush
	 *  @param smsAppMsgPush
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月02日 下午13:05:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	@RequestMapping("addAppMsgPush")
	@ResponseBody
	public ResponseEntity addAppMsgPush(SmsAppMsgPush smsAppMsgPush){
		return this.smsAppMsgPushService.addAppMsgPush(smsAppMsgPush);
	}

	/**
	 *  @Description    : 删除app端推送消息
	 *  @Method_Name    : deleteAppMsgPush
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月05日 上午11:23:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	@RequestMapping("deleteAppMsgPush")
	@ResponseBody
	public ResponseEntity deleteAppMsgPush(int id){
		return this.smsAppMsgPushService.deleteAppMsgPush(id);
	}

	/**
	 *  @Description    : 停止app端推送消息
	 *  @Method_Name    : stopAppMsgPush
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月05日 上午11:36:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	@RequestMapping("stopAppMsgPush")
	@ResponseBody
	public ResponseEntity stopAppMsgPush(int id){
		return this.smsAppMsgPushService.stopAppMsgPush(id);
	}

	/**
	 *  @Description    : 获取app端推送消息详情
	 *  @Method_Name    : appMsgPushDetail
	 *  @param id
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年02月05日 上午11:36:08
	 *  @Author         : pengwu@hongkunjinfu.com
	 */
	@RequestMapping("appMsgPushDetail")
	@ResponseBody
	public ResponseEntity appMsgPushDetail(int id){
		return new ResponseEntity(SUCCESS,this.smsAppMsgPushService.findSmsAppMsgPushById(id));
	}

	/**
	 *  @Description    ：获取手机短信管理列表
	 *  @Method_Name    ：telMsgManageList
	 *  @param pager
	 *  @param smsTelMsg
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018/9/20
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	@RequestMapping("telMsgManageList")
	@ResponseBody
	public ResponseEntity<?> telMsgManageList(Pager pager, SmsTelMsg smsTelMsg){
		return new ResponseEntity(SUCCESS,smsTelMsgService.findSmsTelMsgList(smsTelMsg,pager));
	}

	/**
	 *  @Description    ：重发手机短信
	 *  @Method_Name    ：retryTelSms
	 *  @param id  手机短信id
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/9/20
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	@RequestMapping("retryTelSms")
	@ResponseBody
	public ResponseEntity<?> retryTelSms(int id){
		return smsTelMsgService.retryTelSms(id);
	}
}
