package com.hongkun.finance.web.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.SpecialCodeGenerateUtils;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 用户信息维护接口
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.controller.UserController.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
@Controller
@RequestMapping("/userController/")
public class UserController {

	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserInfoService regUserInfoService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private UserFacade userFacade;
	@Reference
	private FinAccountService finAccountService;

	/**
	 * @Description : 预更新用户信息
	 * @Method_Name : preUpdateUserInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月23日 下午8:10:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("preUpdateUserInfo")
	@ResponseBody
	@Token(operate = Ope.ADD)
	public ResponseEntity<?> preUpdateUserInfo() {
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		// 从redis加载用户信息
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(BaseUtil.getLoginUser().getId()));
		RegUserInfo regUserInfo = BaseUtil.getRegUserInfo(() -> this.regUserInfoService.findRegUserInfoByRegUserId(BaseUtil.getLoginUser().getId()));
		result.getParams().put("regUser", regUser);
		result.getParams().put("regUserDetail", regUserDetail);
		result.getParams().put("regUserInfo", regUserInfo);
		// 从服务端查询用户信息
		return result;
	}

	/**
	 * @Description : 更新用户基本信息
	 * @Method_Name : updateUserInfo
	 * @param regUserInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月23日 下午8:15:12
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateUserInfo")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity<?> updateUserInfo(RegUserInfo regUserInfo) {
		RegUserInfo regUserInfoTmp = BaseUtil.getRegUserInfo(() -> this.regUserInfoService.findRegUserInfoByRegUserId(BaseUtil.getLoginUser().getId()));
		regUserInfo.setId(regUserInfoTmp.getId());
		regUserInfo.setRegUserId(regUserInfoTmp.getRegUserId());
		this.regUserInfoService.updateRegUserInfo(regUserInfo);
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description : 更新昵称
	 * @Method_Name : updateNicName
	 * @param nickName
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月23日 下午8:18:27
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("updateNicName")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity<?> updateNicName(String nickName) {
		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		regUser.setNickName(nickName);
		ResponseEntity<?> result = this.regUserService.updateRegUser(regUser);
		if(result.getResStatus() == Constants.SUCCESS) {
			RegUser r = (RegUser)HttpSessionUtil.getLoginUser();
			r.setNickName(nickName);
			HttpSessionUtil.addLoginUser(r);
		}
		return result;
	}

	/**
	 * @Description : 实名认证
	 * @Method_Name : realName
	 * @param idCard
	 *            身份证号
	 * @param realName
	 *            真实姓名
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月20日 下午4:40:50
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("realName")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity<?> realName(@RequestParam("idCard") String idCard, @RequestParam("realName")String realName, String email) {
		ResponseEntity<?> result = ValidateUtil.validRealNameInfo(realName, idCard);
		if(result.validSuc()) {
			RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
			if(regUser.getIdentify() != UserConstants.USER_IDENTIFY_NO) {
				result = new ResponseEntity<>(Constants.ERROR, "用户已实名");
			}else {
				result = this.userFacade.updateUserForRealName(BaseUtil.getLoginUser().getId(), idCard, realName, email);
			}
		}
		return result;
	}
	
	/**
	 *  @Description    : 重置密码操作
	 *  @Method_Name    : resetPwd
	 *  @param oldPasswd: 原始密码
	 *  @param passwd 	: 新密码
	 *  @param rePasswd : 确认密码
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月29日 上午9:15:15 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("resetPwd")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity<?> resetPwd(@RequestParam("oldPasswd") String oldPasswd, 
			@RequestParam("passwd")String passwd, @RequestParam("rePasswd")String rePasswd) {
		//验证密码的有效性
		ResponseEntity<?> result = ValidateUtil.validatePasswd(oldPasswd);
		if(result.getResStatus() == Constants.SUCCESS) {
			oldPasswd = String.valueOf(result.getResMsg());
			result = ValidateUtil.validatePasswd(passwd);
			if(result.getResStatus() == Constants.SUCCESS) {
				String newPasswd = String.valueOf(result.getResMsg());
				RegUser regUser = BaseUtil.getLoginUser();
				result = this.regUserService.validateLoginAndPasswd(String.valueOf(regUser.getLogin()), oldPasswd);
				if(result.getResStatus() == Constants.ERROR) {
					return new ResponseEntity<>(Constants.ERROR, "旧密码不正确!");
				}
				regUser.setPasswd(newPasswd);
				return this.regUserService.updateRegUser(regUser);
			}
		}
		return result;
	}
	
	/**
	 *  @Description    : 发送验证邮件
	 *  @Method_Name    : sendVerifyEmail
	 *  @param email	: 验证邮箱
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月29日 上午10:00:33 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("sendVerifyEmail")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity<?> sendVerifyEmail(@RequestParam("email") String email) {
		String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    Pattern regex = Pattern.compile(check);
	    Matcher matcher = regex.matcher(email);
	    if(!matcher.matches()) {
	    	new ResponseEntity<>(Constants.ERROR, "请输入正确邮箱地址!");
	    }
		RegUserInfo regUserInfo = BaseUtil.getRegUserInfo(() -> this.regUserInfoService.findRegUserInfoByRegUserId(BaseUtil.getLoginUser().getId()));
	    if(regUserInfo.getEmailState() == 1) {
	    	new ResponseEntity<>(Constants.ERROR, "邮箱已绑定!");
	    }
	    String exist = JedisClusterUtils.get("verifyEmail_" + BaseUtil.getLoginUser().getId());
	    if(StringUtils.isNotBlank(exist) && "1".equals(exist)) {
	    	return new ResponseEntity<>(Constants.ERROR, "验证邮件已经发送，请注意查收!");
	    }
	    try {
	    	String vid = SpecialCodeGenerateUtils.getSpecialNumCode(12);
	    	HttpServletRequest request = HttpSessionUtil.getRequest();
		    String msg = "您好：尊敬鸿坤金服" + BaseUtil.getLoginUser().getNickName() + "<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;您正在完成优先验证操作，<a href='" + request.getRequestURL().toString().replaceAll(request.getRequestURI(), "") + request.getContextPath() +
		    		"/register/verifyEmail.html?vid=" + vid + "' target='_blank'>请点击此链接完成邮箱验证</a>。";
	    	SmsSendUtil.sendEmailMsgToQueue(new SmsEmailMsg(regUserInfo.getRegUserId(), email, "邮箱验证", msg, SmsConstants.SMS_TYPE_NOTICE));
	    	RegUserInfo r = new RegUserInfo();
	    	r.setId(regUserInfo.getId());
	    	r.setRegUserId(BaseUtil.getLoginUser().getId());
	    	r.setEmail(email);
	    	r.setEmailState(1);
			JedisClusterUtils.setAsJson("verifyEmail_" + vid, r, 300);
			JedisClusterUtils.set("verifyEmail_" + BaseUtil.getLoginUser().getId(), "1", 300);
	    	return new ResponseEntity<>(Constants.SUCCESS, "验证邮件已发送，注意查收!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return new ResponseEntity<>(Constants.ERROR, "验证邮件发送失败,请稍后重试!") ;
	}
	
	/**
	 *  @Description    : 执行优先验证操作
	 *  @Method_Name    : doVerifyEmail
	 *  @param vid		: 验证标识			
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月29日 上午10:20:27 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("doVerifyEmail")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ResponseEntity<?> doVerifyEmail(@RequestParam("vid") String vid) {
		RegUserInfo regUserInfo = JedisClusterUtils.getObjectForJson("verifyEmail_" + vid, RegUserInfo.class);
		if(regUserInfo == null || regUserInfo.getId() <= 0) {
			return new ResponseEntity<>(Constants.ERROR, "无效的邮箱验证!");
		}
		this.regUserInfoService.updateRegUserInfo(regUserInfo);
		JedisClusterUtils.delete("verifyEmail_" + vid);
		JedisClusterUtils.delete("verifyEmail_" + regUserInfo.getId());
		return new ResponseEntity<>(Constants.SUCCESS, "邮箱验证成功");
	}
	
	/**
	 * @Description : 获取当前用户一级好友信息
	 * @Method_Name : findUserFirstLevelFriends
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年12月25日 下午14:52:27
	 * @Author : pengwu@hongkun.com.cn
	 */
	@RequestMapping("findUserFirstLevelFriends")
	@ResponseBody
	public ResponseEntity<?> findUserFirstLevelFriends(){
		RegUser regUser =  BaseUtil.getLoginUser();
		RegUserFriends condition = new RegUserFriends();
		condition.setRecommendId(regUser.getId());
		condition.setState(UserConstants.USER_FRIENDS_STATE_VAL);
		condition.setGrade(UserConstants.USER_FRIEND_GRADE_FIRST);
		List<RegUserFriends> list = regUserFriendsService.findRecommendFriendsList(condition);
		return new ResponseEntity(SUCCESS, AppResultUtil.successOfListInProperties(list,SUCCESS,
				"regUserId","realName","tel").get(AppResultUtil.DATA_LIST));
	}

	/**
	 * @Description : 根据手机号积分转赠用户信息，(模糊的姓名，手机号，id)例如：*鹏，17001279697,66
	 * @Method_Name : findPointReceiveUserByTel
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年12月25日 下午15:52:27
	 * @Author : pengwu@hongkun.com.cn
	 */
	@RequestMapping("findPointReceiveUserByTel")
	@ResponseBody
	public ResponseEntity<?> findPointReceiveUserByTel(@RequestParam String tel){
		RegUser regUser =  BaseUtil.getLoginUser();
		return regUserService.findPointReceiveUserByTel(regUser.getId(),tel);
	}

	/**
	 *  @Description    : 加载我的账户
	 *  @Method_Name    : loadActData
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月17日 上午11:50:15 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("loadMyAccount")
	@ResponseBody
	public ResponseEntity<?> loadMyAccount(){
		return this.userFacade.findUserMyAccount(BaseUtil.getLoginUser().getId());
	}

	/**
	 *  @Description    ：获取用户可用余额
	 *  @Method_Name    ：getUseAbleMoney
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/11/7
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	@RequestMapping("getUseAbleMoney")
	@ResponseBody
	public ResponseEntity<?> getUseAbleMoney(){
		FinAccount finAccount = this.finAccountService.findByRegUserId(BaseUtil.getLoginUser().getId());
		Map<String,Object> params = new HashMap<>(1);
		params.put("useAbleMoney",finAccount.getUseableMoney());
		return new ResponseEntity<>(SUCCESS,null,params);
	}
}
