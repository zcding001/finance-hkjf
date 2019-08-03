package com.hongkun.finance.user.utils;

import com.hongkun.finance.user.utils.ValidateCodeGeneratorUtil.CodeType;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpSessionUtil;
import com.yirun.framework.core.utils.crypto.MD5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activity.InvalidActivityException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @Description   : 数据验证接口
 * @Project       : finance-user-model
 * @Program Name  : com.hongkun.finance.user.support.ValidateControllerI.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public interface ValidateUtil {

	final Logger LOG = LoggerFactory.getLogger(ValidateUtil.class);
	/**
	 *  @Description    : 验证计算结果
	 *  @Method_Name    : validateCalc
	 *  @param calc
	 *  @return
	 *  @return         : boolean
	 *  @Creation Date  : 2017年5月22日 下午7:18:47 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	static boolean validateCalc(String calc){
		if(!StringUtils.isNotBlank(calc)){
			return false;
		}
		return true;
	}
	
	/**
	 *  @Description    : 验证短信验证码
	 *  @Method_Name    : validateSmsCode
	 *  @param obj 短信接口服务实例
	 *  @param login 手机号
	 *  @param smsCode 客户端的验证码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月25日 下午4:35:56 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validateSmsCode(Object obj, String login, String smsCode){
		String msg = "错误的短信验证码";
		if(StringUtils.isBlank(smsCode)){
			return new ResponseEntity<>(Constants.ERROR, msg);
		}
		if(obj == null){
			LOG.error("未找到指定的短信服务");
			return new ResponseEntity<>(Constants.ERROR, msg);
		}
		Class<?> clazz = obj.getClass();
		try {
			//通过反射查询数据库中的验证码
			Method method = clazz.getDeclaredMethod("validateCode", long.class, String.class);
			if(method != null){
				return (ResponseEntity<?>) method.invoke(obj, Long.parseLong(login), smsCode);
			}
		} catch (Exception e) {
			LOG.error("can't validate smsCode", e);
			return new ResponseEntity<>(Constants.ERROR, "系统异常，请联系管理员");
		}
		return new ResponseEntity<>(Constants.ERROR, msg);
	}
	
	/**
	 *  @Description    : 验证验证码
	 *  @Method_Name    : validateAuthCode
	 *  @param validateCode
	 *  @param CodeType RANDOM:验证随机验证码  CALC：验证计算验证码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月25日 下午4:16:41 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validateCode(String validateCode, CodeType codeType){
		String localCode = (String)(HttpSessionUtil.getSession().getAttribute(ValidateCodeGeneratorUtil.RANDOM_CODE));
		if(CodeType.CALC == codeType){
			localCode = String.valueOf(HttpSessionUtil.getSession().getAttribute(ValidateCodeGeneratorUtil.CALC_CODE));
		}
		if(StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(localCode)){
			return new ResponseEntity<>(Constants.ERROR, CodeType.CALC == codeType ? "错误的计算验证码" : "错误的随机验证码");
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	
	/**
	 *  @Description    : 验证密码格式的合法性
	 *  @Method_Name    : validatePasswd
	 *  @param passwd
	 *  @return
	 *  @return         : ResponseEntity
	 * @throws InvalidActivityException 
	 *  @Creation Date  : 2017年5月26日 上午10:32:58 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validatePasswd(String passwd) {
        String pw = "";
        try {
            LOG.info("need to decryption passwd:{}", passwd);
            LOG.info("format Passwd:{}", trimPasswd(passwd));
            pw = RsaEncryptUtil.decryptByPrivateKey(trimPasswd(passwd));
            if (StringUtils.isBlank(pw) || !Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$", pw)) {
                return new ResponseEntity<>(Constants.ERROR, "登录密码为6至16位字母数字组合!");
            }
        } catch (Exception e) {
            LOG.error("RSA decryption fail", e);
            return new ResponseEntity<>(Constants.ERROR, "无效的密码");
        }
        //密码校验成功后，进行密码进行md5、加盐处理
        return new ResponseEntity<>(Constants.SUCCESS, MD5.encrypt(pw));
    }

    /**
    *  验证密码合法性
    *  @Method_Name             ：validatePasswdWithoutFormat
    *  @param passwd
    *  @return com.yirun.framework.core.model.ResponseEntity
    *  @Creation Date           ：2018/7/2
    *  @Author                  ：zc.ding@foxmail.com
    */
    static ResponseEntity validatePasswdWithoutFormat(String passwd) {
        String pw = "";
        try {
            LOG.info("need to decryption passwd:{}", passwd);
            LOG.info("format Passwd:{}", trimPasswd(passwd));
            pw = RsaEncryptUtil.decryptByPrivateKey(trimPasswd(passwd));
            if (StringUtils.isBlank(pw)) {
                return new ResponseEntity<>(Constants.ERROR, "用户名或密码错误");
            }
        } catch (Exception e) {
            LOG.error("RSA decryption fail", e);
            return new ResponseEntity<>(Constants.ERROR, "无效的密码");
        }
        //密码校验成功后，进行密码进行md5、加盐处理
        return new ResponseEntity<>(Constants.SUCCESS, MD5.encrypt(pw));
    }
	
	/**
	 *  @Description    : 支付密码
	 *  @Method_Name    : validatePayPasswd
	 *  @param passwd
	 *  @return         : ResponseEntity
	 *  @Creation Date  : 2018年3月26日 下午2:50:52 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	static ResponseEntity<?> validatePayPasswd(String passwd) {
		String pw = "";
		try {
			LOG.info("need to decryption passwd:{}", passwd);
			LOG.info("format passwd:{}", trimPasswd(passwd));
			pw = RsaEncryptUtil.decryptByPrivateKey(trimPasswd(passwd));
			if (StringUtils.isBlank(pw) || !Pattern.matches("^[0-9]{6}$", pw)) {
				return new ResponseEntity<>(Constants.ERROR, "支付密码必须为6位数字"); 
			} 
		} catch (Exception e) {
			LOG.error("RSA decryption fail", e);
			return new ResponseEntity<>(Constants.ERROR, "无效的支付密码");
		}
		//密码校验成功后，进行密码进行md5、加盐处理
		return new ResponseEntity<>(Constants.SUCCESS, MD5.encrypt(pw));
	}
	
	/**
	 *  @Description    : 获得解密的密码
	 *  @Method_Name    : decodePasswd
	 *  @param passwd
	 *  @return         : String
	 *  @Creation Date  : 2018年3月20日 下午3:09:53 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	static String decodePasswd(String passwd) {
		//去掉空格
        passwd = trimPasswd(passwd);
		String pw = "";
		try {
			LOG.info("need to decryption passwd:{}", passwd);
			pw = RsaEncryptUtil.decryptByPrivateKey(passwd);
		} catch (Exception e) {
			LOG.error("RSA decryption fail", e);
		}
		return pw;
	}
	
	/**
	 *  @Description    : 去掉空格，对特殊数据进行URLEncode解码
	 *  @Method_Name    : trimPasswd
	 *  @param passwd
	 *  @return         : String
	 *  @Creation Date  : 2018年3月23日 上午10:08:38 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	static String trimPasswd(String passwd) {
		passwd.trim();
		if (passwd != null) {
            Pattern p = Pattern.compile("\t|\r|\n|\\\\x0A");
            Matcher m = p.matcher(passwd);
            //一个或多个空格
            Pattern blank = Pattern.compile("[' ']+");
            Matcher bm = blank.matcher(m.replaceAll(""));
            passwd = bm.replaceAll("+").trim();
            //此处逻辑为了解决android端+变为%2B的情况
            if(passwd.lastIndexOf("%") > 0) {
            	try {
            		passwd = URLDecoder.decode(passwd, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
            }
        }
        return passwd;
	}
	
	/**
	 *  @Description    : 验证密码
	 *  @Method_Name    : validatePasswd
	 *  @param passwd 密码
	 *  @param rePasswd 重复密码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月26日 下午4:38:26 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validatePasswd(String passwd, String rePasswd) {
		try {
			if(StringUtils.isBlank(passwd) || 
					StringUtils.isBlank(rePasswd) || 
					!rePasswd.equals(passwd)){
				return new ResponseEntity<>(Constants.ERROR, "密码无效");
			}
			return validatePasswd(passwd);
		} catch (Exception e) {
			LOG.error("RSA decryption fail", e);
			return new ResponseEntity<>(Constants.ERROR, "无效的密码");
		}
	}
	
	/**
	 *  @Description    : 验证手机格式合法性
	 *  @Method_Name    : validateLogin
	 *  @param login
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月26日 上午10:32:46 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validateLogin(String login){
		//验证手机号合法性
		if (StringUtils.isBlank(login) || !Pattern.matches("^0{0,1}(13[0-9]|15[0-9]|18[0-9]|14[0-9]|16[0-9]|17[0-9]|19[0-9])[0-9]{8}$", login)) {
			return new ResponseEntity<>(Constants.ERROR, "手机号格式不正确"); 
		}
		return ResponseEntity.SUCCESS;
	}
	
	/**
	 *  @Description    : 验证手机号和密码格式的合法性
	 *  @Method_Name    : validateLoginAndPasswd
	 *  @param login
	 *  @param passwd
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月26日 上午10:39:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validateLoginAndPasswd(String login, String passwd){
		if(StringUtils.isBlank(login) || StringUtils.isBlank(passwd)) {
			return new ResponseEntity<>(Constants.ERROR, "无效的用户名或密码");
		}
		ResponseEntity<?> result = validateLogin(login);
		if(result.validSuc()){
			return validatePasswdWithoutFormat(passwd);
		}
		return result;
	}

	/**
	*  验证登录手机号和密码准确定
	*  @Method_Name             ：validateRegistAndPasswd
	*  @param login
	*  @param passwd
	*  @return com.yirun.framework.core.model.ResponseEntity
	*  @Creation Date           ：2018/7/2
	*  @Author                  ：zc.ding@foxmail.com
	*/
    static ResponseEntity validateRegistAndPasswd(String login, String passwd){
        if(StringUtils.isBlank(login) || StringUtils.isBlank(passwd)) {
            return new ResponseEntity<>(Constants.ERROR, "无效的用户名或密码");
        }
        ResponseEntity<?> result = validateLogin(login);
        if(result.validSuc()){
            return validatePasswd(passwd);
        }
        return result;
    }
	
	/**
	 *  @Description    : 验证手机号和验证码
	 *  @Method_Name    : validateLoginAndCode
	 *  @param login
	 *  @param validateCode
	 *  @param CodeType RANDOM:验证随机验证码  CALC：验证计算验证码
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2017年5月26日 下午4:54:47 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("rawtypes")
	static ResponseEntity validateLoginAndCode(String login, String validateCode, CodeType codeType){
		ResponseEntity<?> result = validateLogin(login);
		if(result.validSuc()){
			return validateCode(validateCode, codeType);
		}
		return result;
	}
	
	/**
	 *  @Description    : 验证实名信息
	 *  @Method_Name    : validRealNameInfo
	 *  @param realName : 真实姓名
	 *  @param idCard   : 身份证号
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月9日 下午2:10:19 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	static ResponseEntity<?> validRealNameInfo(String realName, String idCard){
		if(StringUtils.isBlank(realName)) {
			return new ResponseEntity<>(Constants.ERROR, "真实姓名不能为空");
		}
		if(StringUtils.isBlank(idCard)) {
			return new ResponseEntity<>(Constants.ERROR, "身份证号不能为空");
		}
		if(realName.length() > 20) {
			return new ResponseEntity<>(Constants.ERROR, "真实姓名不能超过20位");
		}
		if(idCard.length() != 15 && idCard.length() != 18) {
			return new ResponseEntity<>(Constants.ERROR, "身份证号格式不正确");
		}
		if(!isAdult(idCard)) {
			return new ResponseEntity<>(Constants.ERROR, "未满18周岁");
		}
		return ResponseEntity.SUCCESS;
	}
	
	/**
	 *  @Description    : 判断是否满18岁
	 *  @Method_Name    : isAdult
	 *  @param idCard 	: 身份证号
	 *  @return         : boolean
	 *  @Creation Date  : 2018年3月9日 下午2:09:38 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static boolean isAdult(String idCard){
		if(StringUtils.isBlank(idCard) || (idCard.length() != 15 && idCard.length() != 18)) {
			return false;
		}
		String brithDate = idCard.length() == 15 ? "19" + idCard.substring(6, 12) : idCard.substring(6, 14);
		Date birthDay = DateUtils.parse(brithDate, "yyyyMMdd");
		return DateUtils.addMonth(birthDay, (18 * 12)).compareTo(new Date()) >= 0 ? false : true;
	}
	/**
	 *
	 * @Description: 校验param 是否是realCard的后8位
	 * @param @param realCard
	 * @param @param param
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xuhui.liu
	 * @date 2015年12月4日
	 */
	public static boolean checkIdCard(String realCard,String param){
		if(realCard!=null && realCard.length()>8){
			String substr = realCard.substring(realCard.length()-8,realCard.length());
			if(substr.equals(param)){
				return true;
			}
		}
		return false;
	}
}
