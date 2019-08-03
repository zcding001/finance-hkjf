/**
 * Copyright 2017 http://www.yiruntz.com/
 */
package com.hongkun.finance.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.SecurityConstants;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.RsaEncryptUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CookieUtil;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_CONSOLE;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_ROOT;
import static com.hongkun.finance.user.support.security.SecurityConstants.USER_AUTH_PREFIX;
import static com.hongkun.finance.user.support.security.SecurityConstants.USER_MENU_PREFIX;
import static com.yirun.framework.core.commons.Constants.*;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * @author zhongping
 * @version 1.1
 * @since 2017年5月22日
 */
@Controller
@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
@RequestMapping("/managementLoginController")
public class ManagementLoginController {
    private final Logger logger = LoggerFactory.getLogger(ManagementLoginController.class);

    @Reference
    private RegUserService regUserService;


    /***
     * 后台用户登录
     * @param response
     * @param login 登录账号
     * @param passwd 登录密码
     * @param randomCode 验证码
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "login") String login,
            @RequestParam(value = "passwd") String passwd,
            @RequestParam(value = "randomCode") String randomCode,
            @RequestParam(value = "rememberMe") String rememberMe) {

        //账号密码校验
        ResponseEntity validateResult = validateUserInfo(login, passwd, randomCode);
        if (validateResult.getResStatus() != SUCCESS) {
            return validateResult;
        }
        //保存登录状态信息
        BaseUtil.storeLoginData(response, validateResult, rememberMe);
        return ResponseEntity.SUCCESS;

    }

    /**
     * 用户信息认证过程
     *
     * @param login
     * @param passwd
     * @param randomCode
     */
    private ResponseEntity validateUserInfo(String login, String passwd, String randomCode) {
        ResponseEntity continuResult = null;
        //校验验证码
//        continuResult= ValidateUtil.validateCode(randomCode, CodeType.RANDOM);
//        if(result.getResStatus() == ERROR){
//            return result;
//        }
        //校验用户名格式的正确性，同时解密密码
        String fakePix = "135";
        String tempLogin= fakePix +String.valueOf(login).substring(3);
        continuResult = ValidateUtil.validateLoginAndPasswd(tempLogin, passwd);
        if (continuResult.getResStatus() != SUCCESS) {
            return continuResult;
        }
        //校验用户名密码，与库中的是否一致
        continuResult = this.regUserService.validateLoginAndPasswd(login, String.valueOf(continuResult.getResMsg()));
        if (continuResult.getResStatus() != SUCCESS) {
            return continuResult;
        }
        RegUser regUser = (RegUser) continuResult.getParams().get("regUser");
        if (!regUser.getType().equals(USER_TYPE_CONSOLE)&&(!regUser.getType().equals(USER_TYPE_ROOT))) {
            //说明不是后台用户，直接返回
            return new ResponseEntity(UserConstants.REQUEST_FORBID, SecurityConstants.NOT_CONSOLE_USER_MESSAGE);
        }

        return continuResult;
    }



    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        //清除用户登录状态
        BaseUtil.cleanLoginData(request, response);

        // 清除用的菜单信息 >规则，前缀+type+Login ,权限用 “-”分开的字符
        if(BaseUtil.getLoginUser() != null){
            JedisClusterUtils.delete(join(asList(USER_MENU_PREFIX, BaseUtil.getLoginUser().getType(), BaseUtil.getLoginUser().getLogin()), "_"));
            JedisClusterUtils.delete(join(asList(USER_AUTH_PREFIX, BaseUtil.getLoginUser().getType(), BaseUtil.getLoginUser().getLogin()), "_"));
        }
        // 使cookie失效
        Cookie cookie_bi_admin = CookieUtil.getCookie(request, TICKET + "_bi-admin");
        if (cookie_bi_admin != null) {
            Cookie cookie = CookieUtil.createCookie(cookie_bi_admin.getName(), cookie_bi_admin.getValue(), 0);
            // 清除缓存用户登录信息
            JedisClusterUtils.delete(cookie.getValue());
            response.addCookie(cookie);
        }
        // 清楚用户登录状态
        HttpSessionUtil.getSession().removeAttribute(CURRENT_USER);
        HttpSessionUtil.getSession().invalidate();
        return new ResponseEntity<>(SUCCESS, "success");
    }


    /**
     * 获取用户的菜单
     *
     * @return
     */
    @RequestMapping("/loadUserMenu")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOAD_MENU)
    public ResponseEntity loadUserMenu() {
        return ResponseEntity.SUCCESS;
    }


    /**
     * 校验验证码
     *
     * @param login    登录账号
     * @param authCode 验证码
     * @return
     * @author zc.ding
     * @since 2017年5月19日
     */
    private boolean validateAuthCode(Long login, String authCode) {
        if (!StringUtils.isNotBlank(authCode)) {
            return false;
        }
        return true;
    }


    /**
     * @param request
     * @param response
     * @return : ResponseEntity<String>
     * @Description : 添加submitToken
     * @Method_Name : addSubmitToken
     * @Creation Date  : 2017年10月27日 上午11:45:06
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/addSubmitToken")
    @ResponseBody
    public ResponseEntity<String> addSubmitToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<String> result = new ResponseEntity<>(Constants.SUCCESS, "");
        result.getParams().put("submitToken", BaseUtil.refreshSumbToken());
        return result;
    }


    /**
     * @return : ResponseEntity
     * @Description : 获得加密公钥,用于密码的加密
     * @Method_Name : getPulibKey
     * @Creation Date : 2017年5月24日 下午5:26:58
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("getPulibKey")
    @ResponseBody
//    @Token(operate = Token.Ope.REFRESH)
    public ResponseEntity getPulibKey() {
        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        try {
            result.getParams().put("publicKey", RsaEncryptUtil.DEFAULT_PUBLIC_KEY);
        } catch (Exception e) {
            logger.info("RSA exception", e);
        }
        return result;
    }


    /** 
    * @Description: 获得需要同步登录状态的地址列表
    * @return: com.yirun.framework.core.model.ResponseEntity<?> 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2018/10/8 15:38
    */
    @RequestMapping("/syncStateList")
    @ResponseBody
    public ResponseEntity<?> syncStateList() {
        String urls = PropertiesHolder.getProperty("sso_sync_state_list");
        if(StringUtils.isNotBlank(urls)) {
            return new ResponseEntity<>(Constants.SUCCESS, urls.replaceAll(" +", "").split(","));
        }
        return new ResponseEntity<>(Constants.ERROR, "未找到需要同步的地址");
    }

    /** 
    * @Description: SSO登录状态的同步
    * @param request
    * @param response
    * @param state 登录状态
    * @param ticket 票据值
    * @return: java.lang.String 
    * @Author: hanghe@hongkunjinfu.com
    * @Date: 2018/10/8 15:37
    */
    @RequestMapping("/syncState")
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
     * @Description: ticket验证登录
     * @param request
     * @param response
     * @return: com.yirun.framework.core.model.ResponseEntity<?>
     * @Author: hanghe@hongkunjinfu.com
     * @Date: 2018/10/8 16:33
     */
    @RequestMapping("validTicket")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DO_LOGIN)
    public ResponseEntity<?> validTicket(HttpServletRequest request, HttpServletResponse response) {
        //获得ticket,以redis中用户作为用户登录的判断唯一凭证
        Cookie cookie = CookieUtil.getCookie(request, TICKET + "_bi-admin");
        logger.info("拿到登录cookie："+cookie);
        if (cookie != null) {
            String ticket = cookie.getValue();
            logger.info("ticket信息："+ticket);
            //从redis中加载ticket对应的用户信息
            RegUser regUser = JedisClusterUtils.getObjectForJson(ticket, RegUser.class);
            logger.info("账号信息："+regUser);
            if (regUser != null && regUser.getId() != null && regUser.getId() > 0) {
                ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
                result.getParams().put("regUser", regUser);
                if (!regUser.getType().equals(USER_TYPE_CONSOLE)&&(!regUser.getType().equals(USER_TYPE_ROOT))) {
                    //说明不是后台用户，直接返回
                    return new ResponseEntity(UserConstants.REQUEST_FORBID, SecurityConstants.NOT_CONSOLE_USER_MESSAGE);
                }
                // 存储登录状态
                BaseUtil.storeLoginData(response, result, null);
                return result;
            }
        }
        return new ResponseEntity<>(Constants.ERROR, "未登录");
    }
}
