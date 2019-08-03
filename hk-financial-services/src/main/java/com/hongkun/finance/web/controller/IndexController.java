
/**
 * Copyright 2017 http://www.yiruntz.com/
 */
package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.fund.facade.FundInfoFacade;
import com.hongkun.finance.invest.facade.BidInfoFacade;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.facade.RosterFacade;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.SysFunctionCfg;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.SysFunctionCfgService;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.constants.UserRegistSource;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.RsaEncryptUtil;
import com.hongkun.finance.user.utils.ValidateCodeGeneratorUtil.CodeType;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CookieUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.roster.constants.RosterConstants.SYS_FUNCTION_CFG_HOUSE_INVEST;
import static com.hongkun.finance.roster.constants.RosterConstants.SYS_FUNCTION_CFG_INABLE;
import static com.yirun.framework.core.commons.Constants.TICKET;

/**
 * 首页控制层
 * @author zc.ding
 * @since 2017年5月18日
 * @version 1.1
 */
@Controller
@RequestMapping("/indexController/")
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
public class IndexController {

	private final Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private PointAccountService pointAccountService;
	@Reference
	private UserFacade userFacade;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private BidInvestFacade bidInvestFacade;
    @Reference
	private BidInfoFacade bidInfoFacade;
    @Reference
    private FundInfoFacade fundInfoFacade;
	@Reference
	private RosterFacade rosterFacade;
	@Reference
	private SysFunctionCfgService sysFunctionCfgService;

	/**
	 *
	 * @Description : 快速登录
	 * @Method_Name : fasterLogin
	 * @param request
	 * @param response
	 * @param login
	 * @param passwd
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月22日 下午5:33:49
	 * @Author : zc.ding
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("fasterLogin")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
	public ResponseEntity fasterLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "login") String login, @RequestParam(value = "passwd") String passwd) {
		ResponseEntity<?> result = this.validateLoginAndPasswd(login, passwd);
		if (result.getResStatus() == Constants.SUCCESS) {
            // 保存登录状态信息
            this.storeLoginData(response, result, null);
		}
		return result;
	}

	/**
	 * 加载用菜单
	 * @return
	 */
	@RequestMapping("loadUserMenu")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DO_LOAD_MENU,sysType = UserConstants.MENU_TYPE_USER_INDEX)
	public ResponseEntity<?> loadUserMenu(){
//		RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
//		return rosterFacade.loadMyAccountMenu(regUser);
		return ResponseEntity.SUCCESS;
	}

	/**
	 * 正常登录
	 *
	 * @author zc.ding
	 * @since 2017年5月18日
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("login")
	@ResponseBody
	public ResponseEntity login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "login") String login, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "randomCode") String randomCode,
			String rememberMe) {
		// 校验验证码
		ResponseEntity<?> result = ValidateUtil.validateCode(randomCode, CodeType.RANDOM);
		if (result.getResStatus() == Constants.SUCCESS) {
			// 验证密码
			result = this.validateLoginAndPasswd(login, passwd);
			if (result.getResStatus() == Constants.SUCCESS) {
				// 保存登录状态信息
                this.storeLoginData(response, result, rememberMe);
			}
		}
		return result;
	}

	/**
	 * 退出
	 *
	 * @author zc.ding
	 * @since 2017年5月18日
	 * @return
	 */
	@RequestMapping("logout")
	@ResponseBody
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		BaseUtil.cleanLoginData(request, response);
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@RequestMapping("isExchangeUser")
	@ResponseBody
	public ResponseEntity<?> isExchangeUser(){
		RegUser regUser = BaseUtil.getRegUser(null	);
		//判断是否是交易所匹配查询白名单
		boolean isExchange  = false;
		if (regUser!=null){
			isExchange  =  this.rosInfoService.validateRoster(regUser.getId(), RosterType.EXCHANGE_INVEST_SEACH, RosterFlag.WHITE);
		}
		return new ResponseEntity<>(Constants.SUCCESS,isExchange?1:0);
	}

	/**
	 * 注册
	 *
	 * @author zc.ding
	 * @since 2017年5月19日
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("reg")
	@ResponseBody
	public ResponseEntity reg(HttpServletRequest request, HttpServletResponse response, RegUser regUser,
			RegUserDetail regUserDetail, @RequestParam(value = "smsCode") String smsCode,
			@RequestParam(value = "calcCode") String calcCode) {
		// 验证计算结果
		ResponseEntity<?> result = ValidateUtil.validateCode(calcCode, CodeType.CALC);
		if (result.getResStatus() == Constants.SUCCESS) {
			// 验证短信验证码
			result = ValidateUtil.validateSmsCode(this.smsTelMsgService, String.valueOf(regUser.getLogin()), smsCode);
			if (result.getResStatus() == Constants.SUCCESS) {
				result = ValidateUtil.validateLoginAndPasswd(String.valueOf(regUser.getLogin()), regUser.getPasswd());
				if (result.getResStatus() == Constants.SUCCESS) {
					// 保存加密后的密码
					regUser.setPasswd(String.valueOf(result.getResMsg()));
					regUserDetail.setRegistSource(UserRegistSource.HKJF_PC.getValue());
					result = this.userFacade.insertRegUserForRegist(regUser, regUserDetail);
					if(result.validSuc()){
						//判断是否通过链接注册，否则加入黑名单
						String isByInviteUrl = request.getParameter("isByInviteUrl");
						if(StringUtils.isBlank(isByInviteUrl)){
							RosInfo rosInfo = new RosInfo();
							RegUserDetail userDetail = (RegUserDetail)result.getParams().get("regUserDetail");
							rosInfo.setRegUserId(userDetail.getRegUserId());
							rosInfo.setLogin(regUser.getLogin());
							rosInfo.setFlag(RosterFlag.BLACK.getValue());
							rosInfo.setType(RosterType.INVEST_CTL.getValue());
							rosInfoService.insertRosInfo(rosInfo);
						}
                        this.storeLoginData(response, result, null);
                    }
				}
			}
		}
		return result;
	}

	/**
	*  存储登录信息
	*  @Method_Name             ：storeLoginData
	*  @param response
	*  @param result
	*  @param remeberMe
	*  @return void
	*  @Creation Date           ：2018/8/1
	*  @Author                  ：zc.ding@foxmail.com
	*/
	private void storeLoginData(HttpServletResponse response, ResponseEntity<?> result, String remeberMe){
        // 存储登录状态
        BaseUtil.storeLoginData(response, result, null);
        RegUser regUser = (RegUser) result.getParams().get("regUser");
        //判断是否显示定期产品
        result.addParam("accInvest", this.bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ? 1 : 0);
        //判断是否为海外投资用户
		result.addParam("overseaInvest",this.bidInvestFacade.validOverseaInvestor(regUser.getId()).validSuc() ? 1 : 0);
		//判断是否显示房产投资
		SysFunctionCfg cfg = new SysFunctionCfg();
		cfg.setFuncCode(SYS_FUNCTION_CFG_HOUSE_INVEST);
		cfg.setIsEnable(SYS_FUNCTION_CFG_INABLE);
		int  count = sysFunctionCfgService.findSysFunctionCfgCount(cfg);
		result.addParam("houseInvest",count >0 ?1:0);
    }

	/**
	 * @Description : 忘记密码-step1 验证手机号是否存在
	 * @Method_Name : forgotPasswdStep1
	 * @param login
	 * @param randomCode 验证码
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月24日 下午4:06:08
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("forgotPasswdStep1")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity forgotPasswdStep1(@RequestParam(value = "login") String login,
			@RequestParam(value = "randomCode") String randomCode) {
		ResponseEntity<?> result = ValidateUtil.validateLoginAndCode(login, randomCode, CodeType.RANDOM);
		if (result.getResStatus() == Constants.SUCCESS) {
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
			if (regUser == null) {
				return new ResponseEntity<>(Constants.ERROR, "用户不存在");
			}
		}
		return result;
	}

	/**
	 * @Description : 忘记密码-step2 验证手机号是否有效及短信验证码
	 * @Method_Name : forgotPasswdStep2
	 * @param login
	 * @param calcCode 图片验证码
	 * @param smsCode 短信验证码
	 * @return : ResponseEntity
	 * @Creation Date : 2017年5月24日 下午4:36:47
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("forgotPasswdStep2")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity forgotPasswdStep2(@RequestParam(value = "login") String login, @RequestParam(value = "calcCode") String calcCode,
			@RequestParam(value = "smsCode") String smsCode) {
		ResponseEntity<?> result = ValidateUtil.validateCode(calcCode, CodeType.CALC);
		if (result.getResStatus() == Constants.SUCCESS) {
			// 验证短信验证码
			result = ValidateUtil.validateSmsCode(this.smsTelMsgService, login, smsCode);
		}
		return result;
	}

	/**
	 * @Description : 忘记密码-step3 重置密码
	 * @Method_Name : forgotPasswdStep3
	 * @param login
	 * @param passwd
	 * @param rePasswd
	 * @param randomCode
	 * @param smsCode
	 * @return : ResponseEntity
	 * @Creation Date : 2017年5月24日 下午4:37:21
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("forgotPasswdStep3")
	@ResponseBody
	@Token(operate = Ope.REFRESH)
	public ResponseEntity forgotPasswdStep3(
			@RequestParam(value = "login") String login, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "rePasswd") String rePasswd, @RequestParam(value = "randomCode") String randomCode,
			@RequestParam(value = "smsCode") String smsCode) {
		// 验证验证码
		ResponseEntity<?> result = ValidateUtil.validateCode(randomCode, CodeType.RANDOM);
		if (result.getResStatus() == Constants.SUCCESS) {
			// 验证短信验证码
			if(StringUtils.isBlank(smsCode) || StringUtils.isBlank(login)) {
				return new ResponseEntity<>(Constants.ERROR, "用户信息已被篡改，重新找回密码!");
			}
			SmsTelMsg smsTelMsg = new SmsTelMsg();
			smsTelMsg.setTel(Long.valueOf(login));
			smsTelMsg.setMsg(smsCode);
			if (this.smsTelMsgService.findSmsTelMsgCount(smsTelMsg) > 0) {
				// 解密密码、验证有效性
				result = ValidateUtil.validatePasswd(passwd);
				if (result.getResStatus() == Constants.SUCCESS) {
					RegUser regUser = new RegUser();
					regUser.setLogin(Long.valueOf(login));
					regUser.setPasswd(String.valueOf(result.getResMsg()));
					return this.regUserService.updateRegUser(regUser);
				}
			}
		}
		return result;
	}

	/**
	 * @Description : 获得加密公钥,用于密码的加密
	 * @Method_Name : getPulibKey
	 * @return : ResponseEntity
	 * @Creation Date : 2017年5月24日 下午5:26:58
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
    @CrossOrigin
	@RequestMapping("getPulibKey")
	@ResponseBody
	public ResponseEntity<?> getPulibKey() {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		try {
			result.getParams().put("publicKey", RsaEncryptUtil.DEFAULT_PUBLIC_KEY);
		} catch (Exception e) {
			logger.error("RSA exception", e);
		}
		return result;
	}


	/**
	 *  @Description    : 获得需要同步登录状态的地址列表
	 *  @Method_Name    : syncStateList
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年11月23日 下午3:36:39
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("syncStateList")
	@ResponseBody
	public ResponseEntity<?> syncStateList() {
		String urls = PropertiesHolder.getProperty("sso_sync_state_list");
		if(StringUtils.isNotBlank(urls)) {
			return new ResponseEntity<>(Constants.SUCCESS, urls.replaceAll(" +", "").split(","));
		}
		return new ResponseEntity<>(Constants.ERROR, "未找到需要同步的地址");
	}

	/**
	 * SSO登录状态的同步
	 * @author zc.ding
	 * @since 2017年5月20日
	 * @param request
	 * @param response
	 * @param state :登录状态
	 * @param ticket: 票据直
	 * @return
	 */
	@RequestMapping("syncState")
	@ResponseBody
	public String syncState(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "state") String state, @RequestParam(value = "ticket") String ticket) {
		Cookie cookie = null;
		// 同步登录
		if (Constants.SYNC_LOGIN_STATE.equals(state)) {
			cookie = CookieUtil.createCookie(Constants.TICKET, ticket);
			response.addCookie(cookie);
		} else {// 同步退出
			logger.info("同步退出状态。。");
			BaseUtil.cleanLoginData(request, response);
		}
		return "jsonpcallback( {success: true})";
	}

	/**
	 * @Description : 发送短信时校验验证码
	 * @Method_Name : sendSmsCodeWithCode
	 * @param login : 手机号
	 * @param calcCode : 验证码
	 * @param type : 1:注册 非1:其他
	 * @return : ResponseEntity
	 * @Creation Date : 2017年6月9日 上午8:52:19
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("sendSmsCodeWithCode")
	@ResponseBody
	public ResponseEntity sendSmsCodeWithCode(String login, String calcCode, Integer type) {
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if (result.getResStatus() == Constants.SUCCESS) {
			result = ValidateUtil.validateCode(calcCode, CodeType.CALC);
			if (result.getResStatus() == Constants.SUCCESS) {
                RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
			    if(type != null && type == 1){
			        if(regUser != null){
			            return ResponseEntity.error("此手机号已经存在");
                    }
                    //验证是否在注册白名单中
//                    if(!this.rosInfoService.validateRoster(Long.parseLong(login), RosterType.REGISTER, RosterFlag.WHITE)) {
//                        return new ResponseEntity<>(UserConstants.NOT_VIP, UserConstants.NOT_VIP_MSG);
//                    }
                }
                if((type == null || type != 1) && regUser == null){
			        return ResponseEntity.error("此手机号未注册");
                }
				return this.smsTelMsgService.insertSmsTelMsgForSmsCode(login, null);
			}
		}
		return result;
	}

	/**
	 * 添加submitToken到cookie中
	 *
	 * @author zc.ding
	 * @since 2017年5月21日
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addSubmitToken")
	@ResponseBody
	public ResponseEntity<String> addSubmitToken(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<String> result = new ResponseEntity<>(Constants.SUCCESS, "");
		result.getParams().put("submitToken", BaseUtil.refreshSumbToken());
		return result;
	}

	/** 
	* @Description: ticket验证登录
	* @param request
	* @param response
	* @return: com.yirun.framework.core.model.ResponseEntity<?> 
	* @Author: hanghe@hongkunjinfu.com
	* @Date: 2018/9/29 16:33
	*/
	@RequestMapping("validTicket")
	@ResponseBody
	public ResponseEntity<?> validTicket(HttpServletRequest request, HttpServletResponse response) {
		// 校验是否登录
		RegUser regUser = BaseUtil.getLoginUser();
		if(regUser == null){
			logger.info("用户登录信息已注销，处于未登录状态");
			return new ResponseEntity<>(Constants.ERROR, "未登录");
		}
		//获得ticket,以redis中用户作为用户登录的判断唯一凭证
        Cookie cookie = CookieUtil.getCookie(request, TICKET);
        if (cookie != null) {
        	String ticket = cookie.getValue();
	        //从redis中加载ticket对应的用户信息
	        regUser = JedisClusterUtils.getObjectForJson(ticket, RegUser.class);
	        if (regUser != null && regUser.getId() != null && regUser.getId() > 0) {
	        	ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
				ResponseEntity<?> tmp = this.userFacade.findUserTotalAccount(regUser.getId());
				FinAccount finAccount = (FinAccount) tmp.getParams().get("finAccount");
				FinAccount finAccountTmp = new FinAccount();
				finAccountTmp.setNowMoney(finAccount.getNowMoney());
				finAccountTmp.setUseableMoney(finAccount.getUseableMoney());
				result.getParams().put("finAccount", finAccount);
				result.getParams().put("regUser", regUser);
	        	//保存登录信息
				this.storeLoginData(response, result, null);
	        	return result;
	        }
        }
		return new ResponseEntity<>(Constants.ERROR, "未登录");
	}

	/**
	 *  @Description    : 验证手机号是否存在
	 *  @Method_Name    : validLogin
	 *  @param login
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年12月8日 下午5:32:06
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@RequestMapping("validLogin")
	@ResponseBody
	public ResponseEntity<?> validLogin(@RequestParam("login") String login){
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if(result.getResStatus() == Constants.SUCCESS) {
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.valueOf(login));
			if(regUser != null && regUser.getId() > 0) {
				return new ResponseEntity<>(Constants.ERROR, "此手机号已被注册");
			}
		}
		return result;
	}

	/**
	 * @Description : 验证登录信息
	 * @Method_Name : validateLoginAndPasswd
	 * @param login
	 * @param passwd
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年5月26日 下午4:43:52
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateLoginAndPasswd(String login, String passwd) {
		// 验证密码合法性
		ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(login, passwd);
		if (result.getResStatus() == Constants.SUCCESS) {
			// 验证密码有效性
			result = this.regUserService.validateLoginAndPasswd(login, String.valueOf(result.getResMsg()));
			if (result.validSuc()) {
				RegUser regUser = (RegUser)result.getParams().get("regUser");
				if(regUser.getType().equals(UserConstants.USER_TYPE_ROOT) || regUser.getType().equals(UserConstants.USER_TYPE_CONSOLE)) {
		        	return ResponseEntity.error("非投资用户类型，不允许登录前台");
		        }
				//过滤成功后的敏感数据
                ResponseEntity<?> tmp = this.userFacade.findUserTotalAccount(regUser.getId());
				FinAccount finAccount = (FinAccount) tmp.getParams().get("finAccount");
				FinAccount finAccountTmp = new FinAccount();
				finAccountTmp.setNowMoney(finAccount.getNowMoney());
				finAccountTmp.setUseableMoney(finAccount.getUseableMoney());
				result.getParams().put("finAccount", finAccount);
				result.getParams().put("regUser", regUser);
			}
		}
		return result;
	}

	/**
	 *  @Description    : wap端邀请用户注册时根据邀请人推荐码获取用户信息
	 *  @Method_Name    : getUserInfoByCommendNo
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年8月14日 下午17:53:15
	 *  @Author         : pengwu@hongkun.com
	 */
	@RequestMapping("getUserInfoByInviteNo")
	@ResponseBody
	public ResponseEntity<?> getUserInfoByInviteNo(@RequestParam String inviteNo){
		return this.regUserService.getUserInfoByInviteNo(inviteNo);
	}

    /**
     *  @Description    ：首页搜索功能
     *  @Method_Name    ：productSearch
     *  @param type     ：类型
     *  @param keyWord  ：关键字
     *  @return com.yirun.framework.core.model.ResponseEntity<?>
     *  @Creation Date  ：2018年08月16日 09:43
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
	@RequestMapping("productSearch")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	public ResponseEntity<?> productSearch(@RequestParam(required = false) Integer type,
                                           @RequestParam(required = false) String keyWord){
        RegUser regUser = BaseUtil.getLoginUser();
		Integer fixFlag = regUser != null && bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ? 1 : 0;
		Integer authenticationFlag = fundInfoFacade.getFundAuthentication(regUser);
		ResponseEntity<?> res = this.bidInfoFacade.productSearch(regUser, type, keyWord,fixFlag);
		res.getParams().put("fixFlag",fixFlag);
		res.getParams().put("authenticationFlag",authenticationFlag);
		return res;
	}

	/**
	 *  @Description    ：wap端找回密码, 第一步, 验证手机号、身份证号
	 *  @Method_Name    ：findPwdStepOne
	 *  @param login  手机号
	 *  @param smsCode  短信验证码
	 *  @param idCard  身份证号后8位
	 *  @return com.yirun.framework.core.model.ResponseEntity<?>
	 *  @Creation Date  ：2018/5/14
	 *  @Author         ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("/findPwdStepOne")
	@ResponseBody
	public ResponseEntity<?> findPwdStepOne(@RequestParam String login, @RequestParam("smsCode") String smsCode,
											@RequestParam(required = false) String idCard){
		logger.info("找回密码第一步, 手机号: {}, 短信验证码: {}, 身份证号: {}", login, smsCode, idCard);
		// 验证手机号
		ResponseEntity<?> result = ValidateUtil.validateLogin(login);
		if (result.validSuc()) {
			UserVO userVO = this.regUserService.findUserWithDetailByLogin(Long.parseLong(login));
			if (userVO == null) {
				return new ResponseEntity<>(Constants.ERROR,"用户不存在!");
			}
			if (userVO.getIdentify() == UserConstants.USER_IDENTIFY_YES) {
				if (org.apache.commons.lang3.StringUtils.isBlank(idCard) || !userVO.getIdCard().endsWith(idCard)) {
					return new ResponseEntity<>(Constants.ERROR,"身份证号与手机号不匹配!");
				}
			}
			// 验证短信验证码
			result = ValidateUtil.validateSmsCode(this.smsTelMsgService, login, smsCode);
			if (result.validSuc()) {
				if (userVO.getIdentify() == UserConstants.USER_IDENTIFY_YES && org.apache.commons.lang3.StringUtils.isBlank(idCard)) {
					return new ResponseEntity<>(Constants.ERROR,"身份证号必填!");
				}
			}
		}
		return result;
	}

	/**
	 *  @Description    ：wap端找回密码, 第二步, 设置密码
	 *  @Method_Name    ：findPwdStepTwo
	 *  @param login  手机号
	 *  @param smsCode  短信验证码
	 *  @param passwd  密码
	 *  @return java.util.Map<java.lang.String,java.lang.Object>
	 *  @Creation Date  ：2018/8/17
	 *  @Author         ：zhichaoding@hongkun.com.cn
	 */
	@RequestMapping("/findPwdStepTwo")
	@ResponseBody
	public ResponseEntity<?> findPwdStepTwo(@RequestParam String login,
											  @RequestParam(value = "smsCode") String smsCode, @RequestParam("passwd") String passwd) {
		logger.info("找回密码第二步, 手机号: {}, 短信验证码: {}", login, smsCode);
		ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(login, passwd);
		if (result.validSuc()) {
			if (!this.smsTelMsgService.validateExist(Long.parseLong(login), smsCode)) {
				return new ResponseEntity<>(Constants.ERROR,"手机号已被修复，请重新注册!");
			}
			String md5Pwd = (String) result.getResMsg();
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
			if (regUser == null) {
				return new ResponseEntity<>(Constants.ERROR,"用户不存在!");
			}
			RegUser r = new RegUser();
			r.setId(regUser.getId());
			r.setPasswd(md5Pwd);
			result = this.regUserService.updateRegUser(r);
			if (result.validSuc()) {
				return new ResponseEntity<>(Constants.SUCCESS);
			} else {
				return new ResponseEntity<>(Constants.ERROR,"密码更新失败!");
			}
		}
		return result;
	}
}