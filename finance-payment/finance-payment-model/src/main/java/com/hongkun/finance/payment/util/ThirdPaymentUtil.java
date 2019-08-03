package com.hongkun.finance.payment.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.factory.PayValidateFactory;
import com.hongkun.finance.payment.llpayvo.BankCardSignInfo;
import com.hongkun.finance.payment.llpayvo.PaymentInfo;
import com.hongkun.finance.payment.model.FinPayConfig;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.util.ThirdPaymentUtils java
 * @Class Name : ThirdPaymentUtils.java
 * @Description : 调用第三方接口工具类
 * @Author : yanbinghuang
 */
public class ThirdPaymentUtil {

	private static final Logger logger = LoggerFactory.getLogger(ThirdPaymentUtil.class);

	/**
	 * @Description : 获取支付配置信息的缓存KEY
	 * @Method_Name : getFinPayConfigKey;
	 * @param systemTypeEnums
	 *            系统类型
	 * @param platformSourceEnums
	 *            平台类型
	 * @param payStyleEnum
	 *            支付方式
	 * @param payChannelEnum
	 *            支付渠道
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年12月28日 下午3:00:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String getFinPayConfigKey(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum, PayChannelEnum payChannelEnum) {
		return systemTypeEnums.getType() + (platformSourceEnums == null ? "" : platformSourceEnums.getType())
				+ payChannelEnum.getChannelKey() + payStyleEnum.getType();
	}

	/**
	 * @Description : 根据卡号，查询不同支付渠道下的银行卡BIN接口
	 * @Method_Name : findCardBin;
	 * @param cardNo
	 *            银行卡号
	 * @param finPayConfig
	 *            支付配置信息
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 上午10:12:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findCardBin(String cardNo, FinPayConfig finPayConfig) {
		logger.info("方法: findCardBin, 根据卡号查询不同支付渠道下的银行卡BIN, 入参: cardNo: {}, finPayConfig: {}", cardNo,
				finPayConfig == null ? "" : finPayConfig.toString());
		try {
			// 1、校验参数合法性
			if (StringUtils.isBlank(cardNo)) {
				return new ResponseEntity<>(Constants.ERROR, "查询卡号不能为空!");
			}
			if (finPayConfig == null) {
				return new ResponseEntity<>(Constants.ERROR, "支付配置信息为空！");
			}
			// 2、调用渠道的查询银行卡卡bin接口
			String payChannel = finPayConfig.getThirdNameCode().substring(0, 1).toUpperCase()
					+ finPayConfig.getThirdNameCode().substring(1);
			if (PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannel)) {
				payChannel = PayChannelEnum.BaoFuProtocol.getChannelKey();
			}
			// 反射方式创建参数支付名称的类;
			Class<?> thirdPayClass = Class.forName("com.hongkun.finance.payment.factory." + payChannel + "PayFactory");
			Method method = thirdPayClass.getDeclaredMethod("findCardBin", String.class, FinPayConfig.class);
			method.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { cardNo, finPayConfig };
			// 3、执行方法
			return (ResponseEntity<?>) method.invoke(thirdPayClass.newInstance(), arguments);
		} catch (Exception e) {
			logger.error("根据卡号查询不同支付渠道下的银行卡BIN, 入参: cardNo: {}, finPayConfig: {}, 查询卡BIN异常信息: ", cardNo,
					finPayConfig == null ? "" : finPayConfig.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "查询卡BIN异常");
		}
	}

	/**
	 * @Description : 根据用户ID，银行卡号，查询用户已绑定银行卡列表信息
	 * @Method_Name : findBankCard;
	 * @param userId
	 *            用户ID
	 * @param cardNo
	 *            银行卡号
	 * @param finPayConfig
	 *            支付配置信息
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 上午10:23:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findBankCard(String userId, String cardNo, FinPayConfig finPayConfig) {
		logger.info("方法: findBankCard, 根据用户ID、银行卡号、查询用户已绑定银行卡列表信息, 入参: userId: {}, cardNo: {}, finPayConfig: {}",
				userId, cardNo, finPayConfig == null ? "" : finPayConfig.toString());
		try {
			// 1、校验参数合法性
			if (StringUtils.isBlank(userId)) {
				return new ResponseEntity<>(Constants.ERROR, "用户id不能为空!");
			}
			if (StringUtils.isBlank(cardNo)) {
				return new ResponseEntity<>(Constants.ERROR, "银行卡号不能为空!");
			}
			if (finPayConfig == null) {
				return new ResponseEntity<>(Constants.ERROR, "支付配置信息为空！");
			}
			// 2、调用渠道的已绑定银行列表查询接口
			String payChannel = finPayConfig.getThirdNameCode().substring(0, 1).toUpperCase()
					+ finPayConfig.getThirdNameCode().substring(1);
			if (PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannel)) {
				payChannel = PayChannelEnum.BaoFuProtocol.getChannelKey();
			}
			// 反射方式创建参数支付名称的类;
			Class<?> thirdPayClass = Class.forName("com.hongkun.finance.payment.factory." + payChannel + "PayFactory");
			Method method = thirdPayClass.getDeclaredMethod("findBankCard", String.class, String.class,
					FinPayConfig.class);
			method.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { userId, cardNo, finPayConfig };
			// 3、执行方法
			return (ResponseEntity<?>) method.invoke(thirdPayClass.newInstance(), arguments);
		} catch (Exception e) {
			logger.error("根据用户ID、银行卡号、查询用户已绑定银行卡列表信息, 入参: userId: {}, cardNo: {}, finPayConfig: {}, 用户已绑定银行列表查询异常信息: ",
					userId, cardNo, finPayConfig == null ? "" : finPayConfig.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "用户已绑定银行列表查询异常");
		}
	}

	/**
	 * @Description : 签约绑卡
	 * @Method_Name : bankCardSigning;
	 * @param bankCardSignInfo
	 *            签约绑卡对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 上午10:38:02;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> bankCardSigning(BankCardSignInfo bankCardSignInfo) {
		logger.info("方法: bankCardSigning, 签约绑卡, 入参: bankCardSignInfo: {}",
				bankCardSignInfo == null ? "" : bankCardSignInfo.toString());
		try {
			// 1、校验参数合法性
			Map<String, String> resultMap = PayValidateFactory.checkBankCardSigningData(bankCardSignInfo);
			if (!SUCCESS_FLAG_NAME.equals(resultMap.get(SERVER_SUCCESS_FLAG))) {
				return new ResponseEntity<>(Constants.ERROR, resultMap.get(SERVER_FAIL_FLAG));
			}
			// 2、组装支付风险对象
			JSONObject riskItemObj = new JSONObject();
			riskItemObj.put("frms_ware_category", "2009");// 商品类目 2009 P2P贷款
			riskItemObj.put("user_info_mercht_userno", bankCardSignInfo.getUserId());// 商户用户唯一标示
			riskItemObj.put("user_info_mercht_userlogin", bankCardSignInfo.getLoginName());// 商户用户登录名
			if (StringUtils.isBlank(bankCardSignInfo.getEmail())) {
				riskItemObj.put("user_info_mail", bankCardSignInfo.getEmail().trim());// 用户邮箱
			} else {
				riskItemObj.put("user_info_mail", "");// 用户邮箱
			}
			riskItemObj.put("user_info_bind_phone", bankCardSignInfo.getTel());// 绑定手机号
			riskItemObj.put("user_info_mercht_usertype", "1");// 商户用户分类
			riskItemObj.put("user_info_dt_register",
					DateUtils.format(bankCardSignInfo.getRegisterDate(), DateUtils.DATE_HHMMSS));// 注册时间
			// 3、 构造支付请求对象
			PaymentInfo paymentInfo = new PaymentInfo();
			paymentInfo.setVersion(bankCardSignInfo.getFinPayConfig().getPayVersion());
			paymentInfo.setOid_partner(bankCardSignInfo.getFinPayConfig().getMerchantNo());
			paymentInfo.setPlatform(bankCardSignInfo.getFinPayConfig().getMerchantNo());
			paymentInfo.setUser_id(bankCardSignInfo.getUserId());
			paymentInfo.setTimestamp(JsonUtils.toJson(PaymentUtil.getCurrentDateTimeStr()));
			paymentInfo.setSign_type(bankCardSignInfo.getFinPayConfig().getSignStyle());
			paymentInfo.setUrl_return(bankCardSignInfo.getUrlReturn());
			paymentInfo.setRisk_item(riskItemObj.toString());
			paymentInfo.setId_type("0");
			paymentInfo.setId_no(bankCardSignInfo.getIdCard().trim());
			paymentInfo.setAcct_name(bankCardSignInfo.getUserName().trim());
			paymentInfo.setCard_no(bankCardSignInfo.getCardNo());
			paymentInfo.setBack_url(bankCardSignInfo.getBackUrl());
			logger.info("签约绑卡, 身份证号标识: {}, 客户银行卡签约请求报文信息: {}", bankCardSignInfo.getIdCard().trim(),
					JSON.parseObject(JSON.toJSONString(paymentInfo)));
			Map<String, Object> paymentInfoMap = ObjChangeUtil.objToMap(paymentInfo);
			// 4、加签名
			String sign = PaymentUtil.addSign(JSON.parseObject(JSON.toJSONString(paymentInfo)),
					bankCardSignInfo.getFinPayConfig().getPrivateKey(),
					bankCardSignInfo.getFinPayConfig().getPayMd5Key());
			paymentInfoMap.put("sign", sign);
			paymentInfoMap.put("reqUrl", bankCardSignInfo.getFinPayConfig().getPayUrl());
			// 5、返回签约组装报文
			return new ResponseEntity<>(Constants.SUCCESS, paymentInfoMap);
		} catch (Exception e) {
			logger.error("签约绑卡, 入参: bankCardSignInfo: {},客户银行卡签约失败信息: ",
					bankCardSignInfo == null ? "" : bankCardSignInfo.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "客户银行卡签约失败");
		}
	}

	/**
	 * @Description : 根据银行CODE，查询银行卡的限额
	 * @Method_Name : findCardlimit;
	 * @param bankCode
	 *            银行CODE
	 * @param finPayConfig
	 *            支付配置信息
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 上午10:43:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findCardlimit(String bankCode, FinPayConfig finPayConfig) {
		logger.info("方法: findCardlimit, 根据银行CODE查询银行卡的限额, 入参: bankCode: {}, finPayConfig: {}", bankCode,
				finPayConfig == null ? "" : finPayConfig.toString());
		try {
			// 1、校验参数合法性
			if (StringUtils.isBlank(bankCode)) {
				return new ResponseEntity<>(Constants.ERROR, "银行编码不能为空！");
			}
			if (finPayConfig == null) {
				return new ResponseEntity<>(Constants.ERROR, "支付配置信息为空！");
			}
			// 2、 调用渠道的查询限额接口
			String payChannel = finPayConfig.getThirdNameCode().substring(0, 1).toUpperCase()
					+ finPayConfig.getThirdNameCode().substring(1);
			if (PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannel)) {
				payChannel = PayChannelEnum.BaoFuProtocol.getChannelKey();
			}
			// 反射方式创建参数支付名称的类;
			Class<?> thirdPayClass = Class.forName("com.hongkun.finance.payment.factory." + payChannel + "PayFactory");

			Method method = thirdPayClass.getDeclaredMethod("findCardlimit", String.class, FinPayConfig.class);
			method.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { bankCode, finPayConfig };
			// 3、执行方法
			return (ResponseEntity<?>) method.invoke(thirdPayClass.newInstance(), arguments);
		} catch (Exception e) {
			logger.error("根据银行CODE查询银行卡的限额, 入参: bankCode: {}, finPayConfig: {}, 银行卡卡限额查询异常信息: ", bankCode,
					finPayConfig == null ? "" : finPayConfig.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "查询限额接口异常");
		}
	}

	/**
	 * @Description : 支付查证接口，用于查询是否支付成功
	 * @Method_Name : findPayCheck;
	 * @param flowId
	 *            支付流水号
	 * @param tradeType
	 *            交易类型
	 * @param finPayConfig
	 *            支付配置信息
	 * @param createTime
	 *            创建时间
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 上午11:24:17;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findPayCheck(String flowId, Integer tradeType, Integer rechargeSource,
			FinPayConfig finPayConfig, Date createTime) {
		logger.info(
				"方法: findPayCheck, 支付查证, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, finPayConfig: {}, createTime: {}",
				flowId, tradeType, rechargeSource, finPayConfig == null ? "" : finPayConfig.toString(), createTime);
		try {
			// 1、 支付查证参数校验
			if (StringUtils.isBlank(flowId)) {
				return new ResponseEntity<>(Constants.ERROR, "查询的支付流水不能为空！");
			}
			if (tradeType == null) {
				return new ResponseEntity<>(Constants.ERROR, "交易类型不能为空！");
			} else {
				if (PayStyleEnum.RECHARGE.getValue() == tradeType && rechargeSource == null) {
					return new ResponseEntity<>(Constants.ERROR, "充值来源不能为空！");
				}
			}
			if (finPayConfig == null) {
				return new ResponseEntity<>(Constants.ERROR, "支付配置信息为空！");
			}
			// 2、 调用渠道的支付查证接口
			String payChannel = finPayConfig.getThirdNameCode().substring(0, 1).toUpperCase()
					+ finPayConfig.getThirdNameCode().substring(1);
			if (PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannel)) {
				payChannel = PayChannelEnum.BaoFuProtocol.getChannelKey();
			}
			// 反射方式创建参数支付名称的类;
			Class<?> thirdPayClass = Class.forName("com.hongkun.finance.payment.factory." + payChannel + "PayFactory");
			Method method = thirdPayClass.getDeclaredMethod("payCheck", String.class, Integer.class, Integer.class,
					FinPayConfig.class, Date.class);
			method.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { flowId, tradeType, rechargeSource, finPayConfig, createTime };
			// 3、执行方法
			return (ResponseEntity<?>) method.invoke(thirdPayClass.newInstance(), arguments);
		} catch (Exception e) {
			logger.error("支付查证, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, finPayConfig: {}, 支付查证异常信息: ",
					flowId, tradeType, rechargeSource, finPayConfig == null ? "" : finPayConfig.toString(), e);
			return new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
	}
}
