package com.hongkun.finance.api.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.service.PointMerchantInfoService;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.constants.UserRegistSource;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.*;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.annotation.ActionLog;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.ImgVerifyCodeUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.FileInfo;
import com.yirun.framework.oss.model.FileState;
import com.yirun.framework.oss.model.FileType;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.util.*;

import static com.hongkun.finance.sms.constants.SmsConstants.REDIS_SMS_LOGIN_PREFIX;
import static com.hongkun.finance.sms.constants.SmsConstants.REDIS_SMS_PRFFIX;
import static com.yirun.framework.core.commons.Constants.SUCCESS;
import static com.yirun.framework.core.commons.Constants.TICKET_EXPIRES_TIME;

@Controller
@RequestMapping("/userController/")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Reference
    private RegUserService regUserService;
    @Reference
    private RegUserDetailService regUserDetailService;
    @Reference
    private RegUserInfoService regUserInfoService;
    @Reference
    private PointMerchantInfoService pointMerchantInfoService;
    @Reference
    private BidInvestService bidInvestService;
    @Reference
    private RosStaffInfoService rosStaffInfoService;
    @Reference
    private UserFacade userFacade;
    @Reference
    private SmsTelMsgService smsTelMsgService;
    @Reference
    private FinAccountService finAccountService;
    @Reference
    private RosInfoService rosInfoService;
    @Autowired
    private JmsService jmsService;
    @Reference
    private RegCompanyInfoService regCompanyInfoService;
    @Reference
    private AppMoreServeService appMoreServeService;
    @Reference
    private LoanFacade loanFacade;
    @Reference
    private AppUserServeService appUserServeService;
    @Reference
    private BidInvestFacade bidInvestFacade;
    @Reference
    private QdzTransferFacade qdzTransferFacade;
    @Reference
    private RegUserFriendsService regUserFriendsService;
    @Value("${oss.url.universal}")
    private String ossUrl;
    @Value("${oss.url.hkjf}")
    private String hkjfOssUrl;

    /**
     * @return : Map<String,Object>
     * @Description : 我的账户
     * @Method_Name : account
     * @Creation Date : 2018年3月12日 上午10:23:54
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("/account")
    @ResponseBody
    public Map<String, Object> account() {
        final int regUserId = BaseUtil.getLoginUserId();
        ResponseEntity<?> result = this.userFacade.findUserMyAccountForApp(regUserId);
        //查询还款金额
        result.getParams().put("repayPlanAmount", this.loanFacade.findRepayingAmount(regUserId, true));
        //设置钱袋子入口标识
        if(this.bidInvestFacade.validQdzEnable(regUserId) == QdzConstants.QDZ_SHOW_FLAG_TRUE){
            //查询钱袋子信息
            result.getParams().put("qdzInfo", qdzTransferFacade.findQdzInfo(regUserId).getParams());
        }
        logger.info("我的账户信息: {}", result.toString());
        return AppResultUtil.mapOfResponseEntity(result, "growValue", "1", "2", "3", "0");
    }

    /**
     * 登录-短信验证码方式
     *
     * @param login   ：手机号
     * @param smsCode ：短信验证码
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Method_Name ：loginSmsCode
     * @Creation Date ：2018/4/27
     * @Author ：zhichaoding@hongkun.com.cn
     */
    @RequestMapping(value = "loginSmsCode", method = RequestMethod.POST)
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> loginSmsCode(@RequestParam("login") String login,
                                            @RequestParam("smsCode") String smsCode,AppLoginLog logAppInfo) {
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);

        if (result.getResStatus() == Constants.SUCCESS) {
            String key = REDIS_SMS_LOGIN_PREFIX + REDIS_SMS_PRFFIX + login;
            if (StringUtils.isBlank(smsCode) || smsCode.length() < 6 || !smsCode.equals(JedisClusterUtils.get(key))) {
                return AppResultUtil.errorOfMsg("无效的短信验证码");
            }
            JedisClusterUtils.delete(key);
            result.getParams().put("regUser", this.regUserService.findRegUserByLogin(Long.parseLong(login)));
            this.fitLoginData(result);
        }
        //记录设备信息
        logAppInfo(logAppInfo);
        return AppResultUtil.mapOfResponseEntity(result, "regUser", "lastLoginTime");
    }

    /**
     * @param login
     * @param passwd
     * @return : Map<String,Object>
     * @Description : 登录
     * @Method_Name : login
     * @Creation Date : 2018年3月9日 上午10:19:01
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> login(@RequestParam("login") String login, @RequestParam("passwd") String passwd,AppLoginLog appLoginLog) {
        ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(login, passwd);
        if (result.getResStatus() == Constants.SUCCESS) {
            result = this.regUserService.validateLoginAndPasswd(login, String.valueOf(result.getResMsg()));
            if (result.getResStatus() == Constants.SUCCESS) {
                RegUser regUser = (RegUser) result.getParams().get("regUser");
                if (regUser.getType().equals(UserConstants.USER_TYPE_CONSOLE)
                        || regUser.getType().equals(UserConstants.USER_TYPE_ROOT)) {
                    return AppResultUtil.errorOfMsg("管理员不允许登录");
                }
                this.fitLoginData(result);
            }
        }
        //记录设备信息
        logAppInfo(appLoginLog);
        return AppResultUtil.mapOfResponseEntity(result, "regUser", "lastLoginTime");
    }

    /**
     * @Description : 注册，注册成功后表示用户已经登录
     * @Method_Name : reg
     * @param login     : 手机号
     * @param passwd    : 密码
     * @param smsCode   : 短信验证码
     * @param commendNo : 推荐码
     * @param source    : 注册来源33-(cxj-ios), 34(cxj-andirod)
     * @return : Map<String,Object>
     * @Description : 注册，注册成功后表示用户已经登录
     * @Method_Name : reg
     * @Creation Date : 2018年3月21日 下午3:34:33
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping(value = "reg", method = RequestMethod.POST)
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> reg(@RequestParam("login") String login, @RequestParam("passwd") String passwd,
                                   @RequestParam("smsCode") String smsCode, String commendNo, AppLoginLog appLoginLog, Integer source) {
        ResponseEntity<?> result = ValidateUtil.validateRegistAndPasswd(login, passwd);
        if (result.validSuc()) {
            if (!this.smsTelMsgService.validateExist(Long.parseLong(login), smsCode)) {
                return AppResultUtil.errorOfMsg("手机号已被注册，请重新输入");
            }
            passwd = (String) result.getResMsg();
            // 验证密码
            RegUser regUser = new RegUser();
            regUser.setLogin(Long.parseLong(login));
            regUser.setPasswd(passwd);
            RegUserDetail regUserDetail = new RegUserDetail();
            regUserDetail.setRegistSource(UserRegistSource.CXJ_APP.getValue());
            regUserDetail.setCommendNo(commendNo);
            regUserDetail.setRegistSource(source != null ? source : UserRegistSource.IMPORT.getValue());
            result = this.userFacade.insertRegUserForRegist(regUser, regUserDetail);
            if (result.validSuc()) {
                regUser = (RegUser) result.getParams().get("regUser");
                //维护鸿坤金服推荐人信息
                try {
                    if(StringUtils.isBlank(commendNo)){
                        jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_RECOMMEND_REGIST, DestinationType.QUEUE, regUser.getId(), JmsMessageType.OBJECT);
                    }
                }catch (Exception e){
                    logger.error("用户: {}, 更新从鸿坤金服获取的推荐人信息", login, e);
                }
                //不通过链接注册，加入黑名单
                RosInfo rosInfo = new RosInfo();
                rosInfo.setRegUserId(regUser.getId());
                rosInfo.setLogin(regUser.getLogin());
                rosInfo.setFlag(RosterFlag.BLACK.getValue());
                rosInfo.setType(RosterType.INVEST_CTL.getValue());
                rosInfoService.insertRosInfo(rosInfo);
                //判断是否以后投资资格
                result.getParams().put("userInvestQualification", this.bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ? 1 : 0);
                String sessionId = HttpSessionUtil.getSession().getId();
                // 保存到session中
                HttpSessionUtil.addLoginUser(regUser);
                // 缓存ticket到redis
                JedisClusterUtils.setAsJson(Constants.SESSION_ID_KEY_PREFIX + sessionId, regUser, TICKET_EXPIRES_TIME);
                result.getParams().put("isMerchant", 0);
                result.getParams().put("realName", "");
                result.getParams().put("staffType", 0);
                result.getParams().put(Constants.SESSION_ID_KEY, sessionId);
                //客户端添加seesionId
                BaseUtil.addTicket(sessionId);
                //推送数据返回标签  注册不再绑定标签
                //this.setPushData(result);
            }
        }
        //记录设备信息
        logAppInfo(appLoginLog);
        return AppResultUtil.mapOfResponseEntity(result, "regUser", "lastLoginTime", "finAccount");
    }

    /**
     * @return : void
     * @Description : 采集app用户终端设备数据
     * @Method_Name : appLoginLog
     * @Creation Date : 2018年3月27日 上午9:43:52
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    private void logAppInfo(AppLoginLog loginLog) {
        try {
            // 发消息到消息队列，记录用户登录信息
            loginLog.setRegUserId(BaseUtil.getLoginUserId());
            jmsService.sendMsg(UserConstants.MQ_QUEUE_APP_LOGIN_LOG, DestinationType.QUEUE, loginLog, JmsMessageType.OBJECT);
        } catch (Exception e) {
           //do nothing...
        }

    }

    /**
     * @Description : 完善注册扩展信息
     * @Method_Name : regSpread
     * @param nickName   : 昵称
     * @param commendNo: 推荐码
     * @return : Map<String,Object>
     * @Description : 完善注册扩展信息
     * @Method_Name : regSpread
     * @Creation Date : 2018年3月9日 下午7:03:15
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("regSpread")
    @ResponseBody
    public Map<String, Object> regSpread(@RequestParam("nickName") String nickName, String commendNo) {
        Map<String, Object> result = AppResultUtil.SUCCESS_MAP;
        if (StringUtils.isNotBlank(nickName) || StringUtils.isNotBlank(commendNo)) {
            return AppResultUtil.mapOfResponseEntity(
                    this.userFacade.updateRegUserForSpread(BaseUtil.getLoginUserId(), nickName, commendNo));
        }
        return result;
    }

    /**
     * @Description : 实名验证
     * @param realName : 真实姓名
     * @param idCard   : 身份证
     * @return : Map<String,Object>
     * @Description : 实名验证
     * @Method_Name : realName
     * @Creation Date : 2018年3月9日 下午1:38:18
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping(value = "realName", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(msg = "实名操作, 真实姓名: {args[0]}, 身份证号: {args[1]}")
    public Map<String, Object> realName(@RequestParam("realName") String realName,
                                        @RequestParam("idCard") String idCard) {
        ResponseEntity<?> result = ValidateUtil.validRealNameInfo(realName, idCard);
        if (result.validSuc()) {
            RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
            if (regUser.getIdentify() != UserConstants.USER_IDENTIFY_NO) {
                result = new ResponseEntity<>(Constants.ERROR, "用户已实名");
            } else {
                result = this.userFacade.updateUserForRealName(BaseUtil.getLoginUserId(), idCard, realName, null);
            }
        }
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @Description : 加载推送标识 废弃
     * @Method_Name : getPushData
     * @Creation Date : 2018年3月9日 上午11:13:06
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("getPushData")
    @ResponseBody
    public Map<String, Object> getPushData() {
        Map<String, Object> result = AppResultUtil.SUCCESS_MAP;
        final Integer regUserId = BaseUtil.getLoginUserId();
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(regUserId));
        result.put("realName", "");
        // 推送数据 姓名+性别
        if (regUser.getIdentify() != UserConstants.USER_IDENTIFY_NO) {
            RegUserDetail regUserDetail = BaseUtil
                    .getRegUserDetail(() -> regUserDetailService.findRegUserDetailByRegUserId(regUserId));
            result.put("realName", regUserDetail.getRealName());
            String idCard = regUserDetail.getIdCard();
            int gender = Integer.valueOf(idCard.substring(idCard.length() - 2, idCard.length() - 1));
            result.put("sex", gender % 2 == 0 ? "女" : "男");
        } else {
            RegUserInfo regUserInfo = BaseUtil
                    .getRegUserInfo(() -> this.regUserInfoService.findRegUserInfoByRegUserId(regUserId));
            result.put("sex", regUserInfo.getSex() == 0 ? "未知" : regUserInfo.getSex() == 2 ? "男" : "女");
        }
        // 是否投资
        BidInvest bidInvest = new BidInvest();
        bidInvest.setRegUserId(regUserId);
        result.put("isInvest", this.bidInvestService.findBidInvestCount(bidInvest) > 0 ? "已投资" : "未投资");
        //有无投资资质
        result.put("userInvestQualification", this.bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ? true : false);
        return result;
    }

    /**
     * @Description :
     * @Method_Name : getValidateCode
     * @Creation Date : 2018年3月9日 下午4:05:56
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("getValidateCode")
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public void getValidateCode(@RequestParam("login") String login) {
        // 生成问答式验证码图片
        ImgVerifyCodeUtil.generatePictureVerificationCode(HttpSessionUtil.getRequest(), HttpSessionUtil.getResponse(),
                login);
    }

    /**
     * @Description : 退出操作
     * @Method_Name : logout
     * @Creation Date : 2018年3月12日 下午3:31:04
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    @RequestMapping("logout")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> logout(@RequestParam("sessionId") String sessionId) {
        JedisClusterUtils.delete(sessionId);
        HttpSessionUtil.removeAttrFromSession(Constants.CURRENT_USER);
        HttpSessionUtil.getSession().invalidate();
        return AppResultUtil.SUCCESS_MAP;
    }

    /**
     * 修改或设置昵称，调用同一个接口
     *
     * @param nickName
     * @return
     */
    @RequestMapping("updateNickName")
    @ResponseBody
    public Map<String, Object> resetNickName(@RequestParam("nickName") String nickName) {
        RegUser regUser = BaseUtil
                .getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        regUser.setNickName(nickName);
        ResponseEntity<?> result = this.regUserService.updateRegUser(regUser);
        if (result.getResStatus() == Constants.SUCCESS) {
            RegUser r = (RegUser) HttpSessionUtil.getLoginUser();
            r.setNickName(nickName);
            HttpSessionUtil.addLoginUser(r);
            return AppResultUtil.successOfMsg("设置昵称成功！");
        } else {
            return AppResultUtil.errorOfMsg(result.getResMsg());
        }
    }

    /**
     * 修改密码
     *
     * @param oldPasswd
     * @param newPasswd
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(msg = "更新密码操作")
    public Map<String, Object> updateUserPassword(@RequestParam String oldPasswd, @RequestParam String newPasswd) {
        try {
            // 校验是否登录
            RegUser regUser = BaseUtil
                    .getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
            // 判断新密码格式是否正确
            ResponseEntity<?> result = ValidateUtil.validatePasswd(newPasswd);
            if (result.validSuc()) {
                String newMd5Pass = String.valueOf(result.getResMsg());
                result = ValidateUtil.validatePasswdWithoutFormat(oldPasswd);
                if (!result.validSuc()) {
                    return AppResultUtil.errorOfMsg("原密码输入错误");
                }
                // 判断原密码是否正确
                String md5Pass = String.valueOf(result.getResMsg());
                if (newMd5Pass.equalsIgnoreCase(md5Pass)) {
                    return AppResultUtil.errorOfMsg("新密码不能和旧密码相同");
                }
                result = this.regUserService.validateLoginAndPasswd(String.valueOf(regUser.getLogin()), md5Pass);
                if (result.getResStatus() == Constants.ERROR) {
                    return AppResultUtil.errorOfMsg("原密码输入错误");
                }
                // 修改密码
                RegUser regUserTemp = new RegUser();
                regUserTemp.setId(regUser.getId());
                regUserTemp.setPasswd(newMd5Pass);
                result = this.regUserService.updateRegUser(regUserTemp);
            }
            if (result.getResStatus() == Constants.SUCCESS) {
                return AppResultUtil.successOfMsg("修改密码成功！");
            } else {
                return AppResultUtil.errorOfMsg(result.getResMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppResultUtil.errorOfMsg("修改密码出现错误");
        }
    }

    @RequestMapping(value = "/setPayPassword", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(msg = "设置支付密码")
    public Map<String, Object> setPayPassword(@RequestParam String payPassword) {
        ResponseEntity<?> result = ValidateUtil.validatePayPasswd(payPassword);
        if (result.validSuc()) {
            result = finAccountService.setPayPassword(BaseUtil.getLoginUserId(), (String) result.getResMsg());
            if (result.getResStatus() == Constants.SUCCESS) {
                return AppResultUtil.successOfMsg("支付密码设置成功");
            } else {
                return AppResultUtil.errorOfMsg(result.getResMsg());
            }
        }
        // 设置支付密码
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     *  @Description    ：判断是否有支付密码
     *  @Method_Name    ：hasPayPassword
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/25
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping(value = "/hasPayPassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> hasPayPassword(){
        RegUser regUser = BaseUtil.getLoginUser();
        FinAccount finAccount = this.finAccountService.findByRegUserId(regUser.getId());
        if(StringUtils.isBlank(finAccount.getPasswd())) {
            return AppResultUtil.errorOfMsg(UserConstants.NO_PAY_PWD, "请去设置支付密码");
        }
        Map<String,Object> result = new HashMap<>(1);
        String serviceHotline = PropertiesHolder.getProperty("service.hotline");
        //判断是否有热线电话
        if (StringUtils.isNotBlank(serviceHotline)){
            String[] hotlines = serviceHotline.split("/");
            result.put("hotline",hotlines);
        }else {
            result.put("hotline","");
        }
        //返回客服电话
        return AppResultUtil.successOf(result);
    }
    /**
     * 找回密码
     *
     * @param login 手机号
     * @param idCard 身份证号
     * @param smsCode 短信code
     * @param passwd 密码
     * @author maruili
     */
    @RequestMapping("/findMyPassword")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> findMyPassword(@RequestParam String login, String idCard, @RequestParam String smsCode, @RequestParam String passwd) {
        logger.info("findMyPassword:  mobile:" + String.valueOf(login) + "idCard:" + String.valueOf(idCard) + "verCode:" + String.valueOf(smsCode));
        // 校验请求类型（用于判定是校验还是修改）
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);
        if (result.validSuc()) {
            // 验证短信验证码
            result = ValidateUtil.validateSmsCode(this.smsTelMsgService, login, smsCode);
            if (result.validSuc()) {
                UserVO userVO = this.regUserService.findUserWithDetailByLogin(Long.parseLong(login));
                if (userVO == null) {
                    return AppResultUtil.errorOfMsg("用户不存在");
                }
                if (userVO.getIdentify() == UserConstants.USER_IDENTIFY_YES) {
                    if (StringUtils.isBlank(idCard) || !userVO.getIdCard().endsWith(idCard)) {
                        return AppResultUtil.errorOfMsg("身份证号与手机号不匹配");
                    }
                }
                // 密码验证
                result = ValidateUtil.validatePasswd(passwd);
                if (result.validSuc()) {
                    // 修改密码
                    String md5Pass = String.valueOf(result.getResMsg());
                    RegUser regUserTemp = new RegUser();
                    regUserTemp.setId(userVO.getUserId());
                    regUserTemp.setPasswd(md5Pass);
                    result = this.regUserService.updateRegUser(regUserTemp);
                }
            }
        }
        return result.validSuc() ? AppResultUtil.successOfMsg("找回密码成功！") : AppResultUtil.errorOfMsg(result.getResMsg());
    }

    /**
     * 找回密码, 第一步, 验证手机号、身份证号
     *
     * @param login   ：手机号
     * @param idCard  ：身份证号后8位
     * @param smsCode ：短信验证码
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Method_Name ：findPwdStepOne
     * @Creation Date ：2018/5/14
     * @Author ：zhichaoding@hongkun.com.cn
     */
    @RequestMapping("/findPwdStepOne")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> findPwdStepOne(@RequestParam String login, @RequestParam("smsCode") String smsCode,
                                              @RequestParam(required = false) String idCard) {
        logger.info("找回密码第一步, 手机号: {}, 短信验证码: {}, 身份证号: {}", login, smsCode, idCard);
        // 验证手机号
        ResponseEntity<?> result = ValidateUtil.validateLogin(login);
        if (result.validSuc()) {
            UserVO userVO = this.regUserService.findUserWithDetailByLogin(Long.parseLong(login));
            if (userVO == null) {
                return AppResultUtil.errorOfMsg("用户不存在");
            }
            if (userVO.getIdentify() == UserConstants.USER_IDENTIFY_YES) {
                if (StringUtils.isBlank(idCard) || !userVO.getIdCard().endsWith(idCard)) {
                    return AppResultUtil.errorOfMsg("身份证号与手机号不匹配");
                }
            }
            // 验证短信验证码
            result = ValidateUtil.validateSmsCode(this.smsTelMsgService, login, smsCode);
            if (result.validSuc()) {
                if (userVO.getIdentify() == UserConstants.USER_IDENTIFY_YES && StringUtils.isBlank(idCard)) {
                    return AppResultUtil.errorOfMsg("身份证号必填");
                }
            }
        }
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * 找回密码, 第二步, 设置密码
     *
     * @param login
     * @param smsCode
     * @param passwd
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Method_Name ：findPwdStepTwo
     * @Creation Date ：2018/5/14
     * @Author ：zhichaoding@hongkun.com.cn
     */
    @RequestMapping("/findPwdStepTwo")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
    public Map<String, Object> findPwdStepTwo(@RequestParam String login,
                                              @RequestParam(value = "smsCode") String smsCode, @RequestParam("passwd") String passwd) {
        logger.info("找回密码第二步, 手机号: {}, 短信验证码: {}", login, smsCode);
        ResponseEntity<?> result = ValidateUtil.validateLoginAndPasswd(login, passwd);
        if (result.validSuc()) {
            if (!this.smsTelMsgService.validateExist(Long.parseLong(login), smsCode)) {
                return AppResultUtil.errorOfMsg("手机号已被修复，请重新注册");
            }
            String md5Pwd = (String) result.getResMsg();
            RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(login));
            if (regUser == null) {
                return AppResultUtil.errorOfMsg("用户不存在");
            }
            RegUser r = new RegUser();
            r.setId(regUser.getId());
            r.setPasswd(md5Pwd);
            result = this.regUserService.updateRegUser(r);
            if (result.validSuc()) {
                return AppResultUtil.successOfMsg("");
            } else {
                return AppResultUtil.errorOfMsg("密码更新失败");
            }
        }
        return AppResultUtil.mapOfResponseEntity(result);
    }

    /**
     * @return com.yirun.framework.core.model.ResponseEntity
     * @Description ：设置用户的头像
     * @Method_Name ：setUserHeadPortrait
     * @Creation Date ：2018/4/28
     * @Author ：zhongpingtang@hongkun.com.cn
     */
    @RequestMapping("/setUserHeadPortrait")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public Map<String, Object> setUserHeadPortrait(String headPortrait,
                                                   @RequestParam(value = "source", required = false) Integer source) {

        if (StringUtils.isBlank(headPortrait)) {
            return AppResultUtil.errorOfMsg("图片流不能为空！");
        }
        if (source != null && BaseUtil.equelsIntWraperPrimit(source, PlatformSourceEnums.ANDROID.getValue())) {
            // 安卓端重新解码
            headPortrait = headPortrait.replace("%2B", "+");
        }

        // 处理图片流
        // step1:上传文件
        FileInfo fileInfo = OSSLoader.getInstance().setUseRandomName(true)
                                     .bindingUploadFile(new FileInfo(new ByteArrayInputStream(Base64.decode(headPortrait.getBytes()))))
                                     .setAllowUploadType(FileType.EXT_TYPE_IMAGE).setFileState(FileState.UN_UPLOAD)
                                     .setFileName("whatever.jpg").setUseRandomName(true)
                                     // 存放通用平台
                                     .setBucketName(OSSBuckets.UNIVERSAL).setFilePath("head_portrait").doUpload();
        // 校验文件是否上传成功
        if (fileInfo.getFileState() != FileState.SAVED) {
            throw new GeneralException("头像更新失败,请重试");

        }
        RegUser unUpdateUser = new RegUser();
        unUpdateUser.setId(BaseUtil.getLoginUserId());
        unUpdateUser.setHeadPortrait(fileInfo.getSaveKey());
        // 更新用户的头像信息
        regUserService.updateRegUser(unUpdateUser);
        // 返回用户已经上传过的头像
        return AppResultUtil.successOfMsg("头像更新成功").addResParameter("headPortrait", ossUrl + fileInfo.getSaveKey());

    }

    /**
     * 加载登录后的数据信息
     *
     * @param result
     * @return void
     * @Method_Name ：fitLoginData
     * @Creation Date ：2018/4/27
     * @Author ：zhichaoding@hongkun.com.cn
     */
    private void fitLoginData(ResponseEntity<?> result) {
        RegUser regUser = (RegUser) result.getParams().get("regUser");
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUser.getId(),
                () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        // 删除login已经登录的session状态信息，保证一个账户只能在一个设备中登录
        String hasLoginSessionId = JedisClusterUtils.get(Constants.SESSION_ID_KEY_PREFIX + regUser.getLogin());
        if (StringUtils.isNotBlank(hasLoginSessionId)) {
            JedisClusterUtils.delete(Constants.SESSION_ID_KEY_PREFIX + hasLoginSessionId);
        }
        // 是否是商户
        PointMerchantInfo pointMerchantInfo = new PointMerchantInfo();
        pointMerchantInfo.setRegUserId(regUser.getId());
        result.getParams().put("isMerchant",
                this.pointMerchantInfoService.findPointMerchantInfoCount(pointMerchantInfo) > 0 ? 1 : 0);
        // 员工类型
        result.getParams().put("staffType", 0);
        RosStaffInfo rosStaffInfo = this.rosStaffInfoService.findRosStaffInfo(regUser.getId(), null, null,null);
        if (rosStaffInfo != null) {
            result.getParams().put("staffType", rosStaffInfo.getType());
        }
        // 设置头像
        result.getParams().put("headPortrait", ossUrl + regUser.getHeadPortrait());
        result.getParams().put("realName",
                StringUtils.isNotBlank(regUserDetail.getRealName()) ? regUserDetail.getRealName() : "");
        // 如果用户类型不是一般用户，查询企业用户信息
        if (regUser.getType() != UserConstants.USER_TYPE_GENERAL) {
            RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(regUser.getId());
            if (regCompanyInfo != null) {
                result.getParams()
                      .put("realName",
                              StringUtils.isNotBlank(regCompanyInfo.getEnterpriseName().length() <= 3
                                      ? regCompanyInfo.getEnterpriseName()
                                      : ("***" + regCompanyInfo.getEnterpriseName().substring(3)))
                                      ? regCompanyInfo.getEnterpriseName() : "");
            }
        }
        HttpSessionUtil.getSession().invalidate();
        String sessionId = HttpSessionUtil.getSession(true).getId();
        result.getParams().put("sessionId", sessionId);
        // 是否为ios版查看黑名单
        boolean flag = rosInfoService.validateRoster(regUser.getId(), RosterType.IOS_VERSION_VIEW, RosterFlag.BLACK);
        result.getParams().put("iosVersionView", flag ? 1 : 0);
        //保存用户类型
        result.getParams().put("type", regUser.getType());
        //保存昵称
        result.getParams().put("nickName", regUser.getNickName());
        //判断是否以后投资资格
        result.getParams().put("userInvestQualification", this.bidInvestFacade.validInvestQualification(regUser.getId()).validSuc() ? 1 : 0);
        // 推送数据 ：是否有投资资质 ^^^^
        //其他推送数据
        this.setPushData(result);
        // 保存到session中
        HttpSessionUtil.addLoginUser(regUser);
        // 缓存ticket到redis
        JedisClusterUtils.setAsJson(Constants.SESSION_ID_KEY_PREFIX + sessionId, regUser, TICKET_EXPIRES_TIME);
        JedisClusterUtils.set(Constants.SESSION_ID_KEY_PREFIX + regUser.getLogin(), sessionId, TICKET_EXPIRES_TIME);
        //存储sessionId到客户端
        BaseUtil.addTicket(sessionId);
    }

    /**
     * 设置推送标签，及投资资质
     * @param result
     */
    private void setPushData(ResponseEntity<?> result){
        RegUser regUser = (RegUser) result.getParams().get("regUser");
        RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUser.getId(),
                () -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
        //用户id，app端消息推送注册使用
        result.getParams().put("userId", regUser.getId());
        // 推送数据 ：性别
        if (regUser.getIdentify() != UserConstants.USER_IDENTIFY_NO) {
            String idCard = regUserDetail.getIdCard();
            int gender = Integer.valueOf(idCard.substring(idCard.length() - 2, idCard.length() - 1));
            result.getParams().put("sex", gender % 2 == 0 ? "女" : "男");
        } else {
            RegUserInfo regUserInfo = this.regUserInfoService.findRegUserInfoByRegUserId(regUser.getId());
            result.getParams().put("sex", regUserInfo.getSex() == 0 ? "未知" : regUserInfo.getSex() == 2 ? "男" : "女");
        }
        // 推送数据 ：是否投资(包含钱袋子转入)
        BidInvest bidInvest = new BidInvest();
        bidInvest.setRegUserId(regUser.getId());
        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(regUser.getId());
        result.getParams().put("isInvest", (this.bidInvestService.findBidInvestCount(bidInvest) > 0 ||
                this.bidInvestService.getSelfAndInvitorTransferCount(userIdList) > 0)? "已投资" : "未投资");
    }

    /** 
    * @Description: 更多服务页面数据
    * @Param: [] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: HeHang
    * @Date: 2018/9/18 
    */
    @RequestMapping("/getMoreServes")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public Map<String, Object> getMoreServes(){
        AppMoreServe appMoreServe = new AppMoreServe();
        appMoreServe.setIsShow(UserConstants.APP_MENU_SHOW);
        List<AppMoreServe> appMoreServeList = appMoreServeService.findAppMoreServeList(appMoreServe);
        //去掉总菜单，补全图标链接
        Iterator<AppMoreServe> it = appMoreServeList.iterator();
        while(it.hasNext()){
            AppMoreServe ams = it.next();
            if(ams.getType() == UserConstants.APP_MENU_TYPE_TOTAL || ams.getType() == UserConstants.APP_MENU_TYPE_ACTIVITY){
                it.remove();
            }
            ams.setIcoUrl(hkjfOssUrl+ams.getIcoUrl());
        }
        return AppResultUtil.successOfListInProperties(appMoreServeList, "查询成功", "id","serviceName","serviceValue","serviceUrl","icoUrl");
    }

    /** 
    * @Description: 用户自定义服务 
    * @Param: [serves] 服务值
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: HeHang
    * @Date: 2018/9/26 
    */
    @RequestMapping("/userCustomizeServes")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public Map<String, Object> userCustomizeServes(String serves){
        logger.info("用户自定义服务："+serves);
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        return userFacade.customizeServes(regUser.getId(),serves);
    }

    /** 
    * @Description: 获取用户自定义服务
    * @Param: [] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: HeHang
    * @Date: 2018/9/26 
    */
    @RequestMapping("/getUserServes")
    @ResponseBody
    @AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
    public Map<String, Object> getUserServes(@RequestParam(value = "position", required = false)Integer position){
        RegUser regUser = BaseUtil.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
        List<AppMoreServe> appMoreServeList = userFacade.getUserServes(regUser.getId());
        //首页多返回一个全部功能菜单
        if(position != null && position == UserConstants.APP_MENU_POSITION_INDEX){
            //查询总菜单
            AppMoreServe cdt = new AppMoreServe();
            cdt.setType(UserConstants.APP_MENU_TYPE_TOTAL);
            AppMoreServe totalMenu = appMoreServeService.findAppMoreServeList(cdt).get(0);
            if(totalMenu != null){
                appMoreServeList.add(totalMenu);
            }

            //查询活动菜单
            cdt.setType(UserConstants.APP_MENU_TYPE_ACTIVITY);
            cdt.setIsShow(UserConstants.APP_MENU_SHOW);
            List<AppMoreServe> activityServeList = appMoreServeService.findAppMoreServeList(cdt);
            if(activityServeList != null && activityServeList.size() >= 4){
                for(int i = 0; i < 4; i++){
                    appMoreServeList.get(i).setIcoUrl(activityServeList.get(i).getIcoUrl());
                }
            }
        }

        appMoreServeList.forEach(e->{e.setIcoUrl(hkjfOssUrl+e.getIcoUrl());});
        return AppResultUtil.successOfListInProperties(appMoreServeList, "查询成功", "id","serviceName","serviceUrl","icoUrl","seq");
    }

    /**
     *  @Description    ：获取热线电话
     *  @Method_Name    ：getServiceHotline
     *  @return java.util.Map<java.lang.String,java.lang.Object>
     *  @Creation Date  ：2018/10/26
     *  @Author         ：pengwu@hongkun.com.cn
     */
    @RequestMapping("/getServiceHotline")
    @ResponseBody
    public Map<String,Object> getServiceHotline(){
        Map<String,Object> result = new HashMap<>(1);
        String serviceHotline = PropertiesHolder.getProperty("service.hotline");
        //判断是否有热线电话
        if (StringUtils.isNotBlank(serviceHotline)){
            String[] hotlines = serviceHotline.split("/");
            result.put("hotline",hotlines);
            return AppResultUtil.successOf(result);
        }
        return AppResultUtil.errorOfMsg("暂无客服电话");
    }

    /**
     * @Description : 获取当前用户一级好友信息
     * @Method_Name : findUserFirstLevelFriends
     * @return : Map<String,Object>
     * @Creation Date : 2018年11月21日 下午14:52:27
     * @Author : pengwu@hongkun.com.cn
     */
    @RequestMapping("findUserFirstLevelFriends")
    @ResponseBody
    public Map<String,Object> findUserFirstLevelFriends(){
        RegUser regUser =  BaseUtil.getLoginUser();
        RegUserFriends condition = new RegUserFriends();
        condition.setRecommendId(regUser.getId());
        condition.setState(UserConstants.USER_FRIENDS_STATE_VAL);
        condition.setGrade(UserConstants.USER_FRIEND_GRADE_FIRST);
        List<RegUserFriends> list = regUserFriendsService.findRecommendFriendsList(condition);
        return AppResultUtil.successOfListInProperties(list,SUCCESS,"regUserId","realName","tel");
    }
}
