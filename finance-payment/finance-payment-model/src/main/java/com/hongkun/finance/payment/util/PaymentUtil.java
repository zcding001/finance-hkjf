package com.hongkun.finance.payment.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.enums.SignTypeEnum;
import com.hongkun.finance.payment.security.Md5Algorithm;
import com.hongkun.finance.payment.security.TraderRSAUtil;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description : 连连支付MD5工具类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.util.LLPayMD5Util.java
 * @Author : yanbinghuang
 */
public class PaymentUtil {
	private static final Logger logger = LoggerFactory.getLogger(PaymentUtil.class);

	/**必须与app端统一*/
	private static final String MD5_KEY = "AMvFWdickzxK/sCwiG5BVKu8HkqxcrQEjSKmvCfZ44QfNsKiV7pG1";

	/**
	 * @Description : str空判断
	 * @Method_Name : isNull;
	 * @param str
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:32:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean isNull(String str) {
		if (null == str || str.equalsIgnoreCase("null") || str.equals("")) {
			return true;
		} else
			return false;
	}

	/**
	 * @Description : 获取当前时间str，格式yyyyMMddHHmmss
	 * @Method_Name : getCurrentDateTimeStr;
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:31:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String getCurrentDateTimeStr() {
		return DateUtils.format(new Date(), DateUtils.DATE_HHMMSS);
	}

	/**
	 * @Description : 功能描述：获取真实的IP地址
	 * @Method_Name : getIpAddr;
	 * @param request
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:32:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (!isNull(ip) && ip.contains(",")) {
			String[] ips = ip.split(",");
			ip = ips[ips.length - 1];
		}
		// 转换IP 格式
		if (!isNull(ip)) {
			ip = ip.replace(".", "_");
		}
		return ip;
	}

	/**
	 * @Description : 获取签名原串
	 * @Method_Name : genSignData;
	 * @param jsonObject
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:33:06;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if ("sign".equals(key)) {
				continue;
			}
			String value = jsonObject.getString(key);
			// 空串不参与签名
			if (isNull(value)) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

	/**
	 * @Description : 加签名
	 * @Method_Name : addSign;
	 * @param reqObj
	 *            jsonObj
	 * @param rsa_private
	 *            私钥
	 * @param md5_key
	 *            md5KEY值
	 * @return
	 * @throws Exception
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:33:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String addSign(JSONObject reqObj, String rsa_private, String md5_key) throws Exception {
		String sign = "";
		if (reqObj == null) {
			return "";
		}
		String sign_type = reqObj.getString("sign_type");
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			sign = addSignMD5(reqObj, md5_key);
		} else {
			sign = addSignRSA(reqObj, rsa_private);
		}
		if (StringUtils.isBlank(sign)) {
			logger.error("加签不能为空！");
			throw new GeneralException("加签不能为空！");
		}
		return sign;
	}

	/**
	 * @Description : 签名验证
	 * @Method_Name : checkSign;
	 * @param reqStr
	 *            验签的值
	 * @param rsa_public
	 *            公钥
	 * @param md5_key
	 *            md5KEY值
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:34:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean checkSign(String reqStr, String rsa_public, String md5_key) {
		JSONObject reqObj = JSON.parseObject(reqStr);
		if (reqObj == null) {
			return false;
		}
		String sign_type = reqObj.getString("sign_type");
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			return checkSignMD5(reqObj, md5_key);
		} else {
			return checkSignRSA(reqObj, rsa_public);
		}
	}

	/**
	 * @Description : MD5验证签名
	 * @Method_Name : checkSignByMd5
	 * @param reqStr
	 * @param md5_key
	 * @return : boolean
	 * @Creation Date : 2018年3月14日 下午2:50:16
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	public static boolean checkSignByMd5(String reqStr) {
		JSONObject reqObj = JSON.parseObject(reqStr);
		if (reqObj == null) {
			return false;
		}
		return checkSignMD5(reqObj, MD5_KEY);
	}

	/**
	 * @Description :校验MD5值
	 * @Method_Name : checkSignMD5;
	 * @param md5Str
	 *            md5值
	 * @param sign
	 *            标记
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:36:41;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean checkSignMD5(String md5Str, String sign) {
		logger.info("方法: checkSignMD5, 校验MD5值, 入参: md5str, sign: {}", md5Str, sign);
		try {
			if (sign.equals(Md5Algorithm.getInstance().md5Digest(md5Str.getBytes("utf-8")))) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("校验MD5值, 校验MD5值失败: ", e);
			return false;
		}
	}

	/**
	 * @Description : RSA签名验证
	 * @Method_Name : checkSignRSA;
	 * @param reqObj
	 *            jsonObj
	 * @param rsa_public
	 *            公钥
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:36:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unused")
	private static boolean checkSignRSA(JSONObject reqObj, String rsa_public) {
		logger.info("方法: checkSignRSA, RSA签名验证, 入参: reqObj: {}, rsa_public: {}, 商户: {}",
				reqObj == null ? "" : reqObj.toString(), rsa_public, reqObj.getString("oid_partner"));
		if (reqObj == null) {
			return false;
		}
		String sign = reqObj.getString("sign");
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		logger.info("RSA签名验证, 商户: {}, 待签名原串: {}", reqObj.getString("oid_partner"), sign_src);
		logger.info("RSA签名验证, 商户: {}, 签名串: {}", reqObj.getString("oid_partner"), sign);
		try {
			if (TraderRSAUtil.checksign(rsa_public, sign_src, sign)) {
				logger.info("RSA签名验证, 商户: {}, RSA签名验证通过", reqObj.getString("oid_partner"));
				return true;
			} else {
				logger.error("RSA签名验证, 商户: {}, RSA签名验证未通过", reqObj.getString("oid_partner"));
				return false;
			}
		} catch (Exception e) {
			logger.error("RSA签名验证, 商户: {}, RSA签名验证异常: ", reqObj.getString("oid_partner"), e);
			return false;
		}
	}

	/**
	 * @Description : MD5签名验证
	 * @Method_Name : checkSignMD5;
	 * @param reqObj
	 *            jsonObj
	 * @param md5_key
	 *            md5KEY值
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年11月1日 上午10:37:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unused")
	private static boolean checkSignMD5(JSONObject reqObj, String md5_key) {
		logger.info("方法: checkSignMD5, MD5签名验证, 入参: reqObj: {}, md5_key: {}, 商户: {}",
				reqObj == null ? "" : reqObj.toString(), md5_key, reqObj.getString("oid_partner"));
		if (reqObj == null) {
			return false;
		}
		String sign = reqObj.getString("sign");
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		logger.info("MD5签名验证, 商户: {}, 待签名原串: {}", reqObj.getString("oid_partner"), sign_src);
		logger.info("MD5签名验证, 商户: {}, 签名串: {}", reqObj.getString("oid_partner"), sign);
		sign_src += "&key=" + md5_key;
		try {
			if (sign.equals(Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8")))) {
				logger.info("MD5签名验证, 商户: {}, MD5签名验证通过", reqObj.getString("oid_partner"));
				return true;
			} else {
				logger.error("MD5签名验证, 商户: {}, MD5签名验证未通过", reqObj.getString("oid_partner"));
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("MD5签名验证, 商户: {}, MD5签名验证异常: ", reqObj.getString("oid_partner"), e);
			return false;
		}
	}

	/**
	 * @Description : RSA加签名
	 * @Method_Name : addSignRSA;
	 * @param reqObj
	 *            jsonObj
	 * @param rsa_private
	 *            私钥
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:38:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unused")
	private static String addSignRSA(JSONObject reqObj, String rsa_private) {
		logger.info("方法: addSignRSA, RSA加签名, 入参: reqObj: {}, rsa_private: {}, 商户: {}",
				reqObj == null ? "" : reqObj.toString(), rsa_private, reqObj.getString("oid_partner"));
		if (reqObj == null) {
			return "";
		}
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		logger.info("RSA加签名, 商户: {}, 加签原串: {}", reqObj.getString("oid_partner"), sign_src);
		try {
			return TraderRSAUtil.sign(rsa_private, sign_src);
		} catch (Exception e) {
			logger.error("RSA加签名, 商户: {}, RSA加签名异常: ", reqObj.getString("oid_partner"), e);
			return "";
		}
	}

	/**
	 * @Description : MD5加签名
	 * @Method_Name : addSignMD5;
	 * @param reqObj
	 *            jsonObj
	 * @param md5_key
	 *            md5KEY值
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:39:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String addSignMD5(JSONObject reqObj, String md5_key) {
		if (reqObj == null) {
			return "";
		}
		logger.info("方法: addSignMD5, MD5加签名, 入参: reqObj: {}, md5_key: {}, 商户: {}", reqObj.toString(), md5_key,
				reqObj.getString("oid_partner"));
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		logger.info("MD5加签名, 商户: {}, 加签原串: {}", reqObj.getString("oid_partner"), sign_src);
		sign_src += "&key=" + md5_key;
		try {
			return Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("MD5加签名, 商户: {}, MD5加签名异常: {}", reqObj.getString("oid_partner"), e);
			return "";
		}
	}

	/***
	 * @Description : 读取request流
	 * @Method_Name : readReqStr;
	 * @param request
	 *            request对象
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月1日 上午10:40:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error("读取流数据失败: {}", e);
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error("关闭流数据失败: ", e);
			}
		}
		return sb.toString();
	}
}
