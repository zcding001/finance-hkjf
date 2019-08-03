package com.hongkun.finance.api.controller.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gexin.fastjson.JSONObject;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.sms.service.SmsWebMsgDetailService;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ImgVerifyCodeUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description   : 短信、站内信控制层
 * @Project       : hk-api-services
 * @Program Name  : com.hongkun.finance.api.controller.sms.SmsController.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/smsController/")
public class SmsController {

    private final Logger logger = LoggerFactory.getLogger(SmsController.class);
    
	@Reference
	private SmsWebMsgService smsWebMsgService;
	@Reference
	private SmsWebMsgDetailService smsWebMsgDetailService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RosInfoService rosInfoService;

	/**
	 *  @Description    : 条件检索站内信信息,含有站内信详情
	 *  @Method_Name    : webMsgList
	 *  @param pager
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月28日 上午11:39:53 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("webMsgList")
	@ResponseBody
	public Map<String, Object> webMsgList(Pager pager) {
		SmsWebMsg smsWebMsg = new SmsWebMsg();
		smsWebMsg.setRegUserId(BaseUtil.getLoginUser().getId());
		return AppResultUtil.successOfList(this.smsWebMsgService.findSmsWebMsgWithDetailList(smsWebMsg, pager).getData(), "regUserId", "type");
	}
	
	/**
	 * 
	 *  @Description    : 更新站内信状态
	 *  @Method_Name    : updateWebMsgState
	 *  @param ids
	 *  @param state
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月11日 下午3:37:05 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateWebMsgState")
	@ResponseBody
    @ActionLog(msg = "更新站内信状态, 状态: {args[1]}")
	public Map<String, Object> updateWebMsgState(String[] ids, Integer state) {
	    logger.info("更新站内信状态, 用户标识: {}, 状态: {}, 集合: {}", BaseUtil.getLoginUserId(), state, ids == null ? "所有未读" : 
                JsonUtils.toJson(ids));
        if(ids != null && ids.length > 0){
            if(Arrays.asList(ids).stream().anyMatch(e -> !e.matches("[0-9]+"))) {
                return  AppResultUtil.errorOfMsg("含有非法数据!");
            }
        }
		return this.smsWebMsgService.updateSmsWebMsg(ids, state, BaseUtil.getLoginUser().getId()) > 0
				? AppResultUtil.SUCCESS_MAP
				: AppResultUtil.ERROR_MAP;
	}
	
	/**
	 *  @Description    : 获得短信验证码
	 *  @Method_Name    : sendSmsCode
	 *  @param login	: 手机号
	 *  @param validCode: 计算验证码
	 *  @param type		: 1:注册 2：找回密码
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月9日 下午4:20:36 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("sendSmsCode")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public Map<String, Object> sendSmsCode(@RequestParam("login")String login, @RequestParam("validCode")String validCode, @RequestParam("type")String type) {
	    logger.info("获取短信验证码, 用户: {}, 类型: {}", login, type);
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if (result.getResStatus() == Constants.SUCCESS) {
			String localValidCode = JedisClusterUtils.get(ImgVerifyCodeUtil.VERIFY_CODE_PREFIX + login);
			if(StringUtils.isBlank(validCode) || StringUtils.isBlank(localValidCode)) {
				return AppResultUtil.errorOfMsg("无效的验证码");
			}
			if(!validCode.equals(localValidCode)) {
				return AppResultUtil.errorOfMsg("验证码错误");
			}
			result = this.validSmsCodeType(login, type);
			if(result.validSuc()){
			    result = this.smsTelMsgService.insertSmsTelMsgForSmsCode(login, null);
            }
		}
		return AppResultUtil.mapOfResponseEntity(result);
	}

	/**
	*  获取短信验证码，不需要验证计算验证码或是图片验证码
	*  @Method_Name             ：sendSmsCodeSimple
	*  @param login             ：手机号
	*  @param type              : 验证码类型 1:注册 2：找回密码
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date           ：2018/5/3
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    @RequestMapping("sendSmsCodeSimple")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> sendSmsCodeSimple(@RequestParam("login")String login, @RequestParam("type")String type) {
        logger.info("获取短信验证码, 用户: {}, 类型: {}", login, type);
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);
        if (result.getResStatus() == Constants.SUCCESS) {
            result = this.validSmsCodeType(login, type);
            if(result.validSuc()){
                result = this.smsTelMsgService.insertSmsTelMsgForSmsCode(login, null);
            }
        }
        return AppResultUtil.mapOfResponseEntity(result);
    }

	/**
	*  发送登录验证码
	*  @Method_Name             ：sendLoginSmsCode
	*  @param login
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date           ：2018/4/27
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
    @RequestMapping("sendLoginSmsCode")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> sendLoginSmsCode(@RequestParam("login")String login) {
        logger.info("获取登录手机验证码, 用户: {}", login);
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);
        if (result.getResStatus() == Constants.SUCCESS) {
            RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
            if(regUser == null) {
                return AppResultUtil.errorOfMsg("此手机号未注册");
            }
            result = this.smsTelMsgService.insertSmsTelMsgForSmsCode(login, SmsConstants.REDIS_SMS_LOGIN_PREFIX);
        }
        return AppResultUtil.mapOfResponseEntity(result);
    }
    
    /**
    *  验证短信验证码，只验证准确定，不更新验证码状态
    *  @Method_Name             ：validSmsCode
    *  @param login
    *  @param smsCode
    *  @return java.util.Map<java.lang.String,java.lang.Object>
    *  @Creation Date           ：2018/5/3
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    @RequestMapping("validSmsCode")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> validSmsCode(@RequestParam("login")String login, @RequestParam("smsCode")String smsCode){
        logger.info("验证短信验证码, 用户: {}, 短信验证码: {}", login, smsCode);
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);
        if(result.validSuc()){
            if(StringUtils.isBlank(smsCode)){
                return AppResultUtil.errorOfMsg("请输入短信验证码");
            }
            //只从redis中获取短信验证码
            String cacheSmsCode = JedisClusterUtils.get(SmsConstants.REDIS_SMS_PRFFIX + login);
            if(StringUtils.isBlank(cacheSmsCode) || !cacheSmsCode.equals(smsCode)){
                return AppResultUtil.errorOfMsg("短信验证码不正确");
            }
        }
        return AppResultUtil.successOfMsg("验证通过");
    }
	
	/**
	 *  @Description    : 发送短信消息
	 *  @Method_Name    : sendSmsTelMsg
	 *  @param login	: 接收人手机号
	 *  @param type		: 短信类型     1：好友邀请	待扩展
	 *  @return         : Map<String,Object>
	 *  @Creation Date  : 2018年3月16日 下午3:11:59 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("sendSmsTelMsg")
	@ResponseBody
    @ActionLog(msg = "发送短信邀请, 邀请人: {args[0]}, 类型: {args[1]}")
	public Map<String, Object> sendSmsTelMsg(@RequestParam("login") String login, @RequestParam("type") Integer type) {
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (!regUser.hasIdentify()) {
			return AppResultUtil.errorOfMsg(UserConstants.NO_IDENTIFY, "用户信息不完整,请先完善用户信息");
		}
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if (result.validSuc()) {
			if (type == 1) {
				RegUser r = this.regUserService.findRegUserByLogin(Long.parseLong(login));
				if (r != null && r.getId() > 0) {
					return AppResultUtil.errorOfMsg("该用户已是鸿坤金服好友");
				} else {
					RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(BaseUtil.getLoginUserId(),
							() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
					return AppResultUtil.mapOfResponseEntity(this.smsTelMsgService.insertSmsTelMsg(new SmsTelMsg(
							Long.parseLong(login), SmsMsgTemplate.MSG_INVITE.getMsg(), SmsConstants.SMS_TYPE_NOTICE,
							new Object[] { regUserDetail.getRealName(), regUserDetail.getInviteNo() })));
				}
			}
		}
		return AppResultUtil.mapOfResponseEntity(result);
	}
	
	/**
	*  验证验证码类型
	*  @Method_Name             ：validSmsCodeType
	*  @param login             ：手机号
	*  @param type              ：验证码类型
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date           ：2018/5/3
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	private ResponseEntity<?> validSmsCodeType(String login, String type){
        RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
        if("1".equals(type)) {
            if(regUser != null){
                return ResponseEntity.error("此手机号已经存在");
            }
            //验证是否在注册白名单中
//            if(!this.rosInfoService.validateRoster(Long.parseLong(login), RosterType.REGISTER, RosterFlag.WHITE)) {
//                ResponseEntity<?> result = new ResponseEntity<>(UserConstants.NOT_VIP, UserConstants.NOT_VIP_MSG);
//                result.getParams().put("hotLine", UserConstants.SERVICE_HOTLINE);
//				return result;
//			}
        }else {
            if(regUser == null) {
                return ResponseEntity.error("此手机号未注册");
            }
        }
        return ResponseEntity.SUCCESS;
    }
}
