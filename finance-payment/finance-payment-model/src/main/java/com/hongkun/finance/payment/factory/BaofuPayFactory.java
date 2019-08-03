/**
 * @Description: 宝付支付相关业务
 * @Package com.hongkun.finance.payment.factory
 * @author yanbinghuang
 * @version V1.0
 */
package com.hongkun.finance.payment.factory;

import com.hongkun.finance.payment.bfpayvo.TransContent;
import com.hongkun.finance.payment.bfpayvo.TransHead;
import com.hongkun.finance.payment.bfpayvo.TransReqData;
import com.hongkun.finance.payment.bfpayvo.TransResResult;
import com.hongkun.finance.payment.client.baofu.util.JXMConvertUtil;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.hongkun.finance.payment.security.Base64Util;
import com.hongkun.finance.payment.util.CreateFlowUtil;
import com.hongkun.finance.payment.util.RSAUtil;
import com.hongkun.finance.payment.util.SecurityUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;

/**
 * @Description : 宝付支付工厂类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.factory.BaofuPayFactory.java
 * @Author : yanbinghuang
 */
public class BaofuPayFactory {

	private static final Logger logger = LoggerFactory.getLogger(BaofuPayFactory.class);
	/*********************
	 * PC 充值交易类型及子类型
	 *******************************************/
	// 交易类型
	private static final String RECHARGE_TRADE_TYPE = "03311";
	// 交易子类型
	private static final String RECHARGE_TRADE_SUB_TYPE = "03";

	/*********************
	 * APP 充值交易类型及子类型
	 *******************************************/
	// 交易类型
	private static final String RECHARGE_APP_TRADE_TYPE = "03311";
	// 交易子类型
	private static final String RECHARGE_APP_TRADE_SUB_TYPE = "02";
	/********************* 查询卡BIN交易类型及子类型 *******************************************/
	// 交易类型
	private static final String CARD_BIN_TRADE_TYPE = "3001";
	// 交易子类型
	private static final String CARD_BIN_TRADE_SUB_TYPE = "01361";
	/****************************************************************/
	/*
	 * // 网银支付终端号 public static final String WY_TERMINAL_ID = "40268"; //
	 * 查询卡BIN终端号 public static final String KBIN_TERMINAL_ID = "40268"; //
	 * 提现查证接口终端号 public static final String WITHDRAW_TERMINAL_ID = "40395";
	 */
	// 接入类型 -储蓄卡支付
	private static final String BIZ_TYPE = "0000";
	// 数据类型
	private static final String DATA_TYPE = "json";
	// 代表中文
	private static final String LANGUAGE = "1";
	// 字符集(1-UTF-8;2-GBK;3-GB2312)
	private static final String INPUT_CHARSET = "1";
	// 身份证号类型
	private static final String ID_CARD_TYPE = "01";
	// 编码
	private static final String CHARSET = "UTF-8";
	// 商品名称
	private static final String PRODUCT_NAME = "鸿坤金服充值";
	// 商品数量
	private static final String PRODUCT_NUM = "1";
	// 标记
	private static final String MARK = "|";

	/**
	 * @Description :构造宝付PC端充值对象
	 * @Method_Name : paymentPcInfo;
	 * @param rechargeCash
	 *            充值VO
	 * @param finAccount
	 *            账户
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年1月12日 下午2:23:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> paymentPcInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		// 1、判断是否是网银支付
		if (PayStyleEnum.WY.getType().equals(rechargeCash.getPayStyle()) 
				||  PayStyleEnum.QYWY.getType().equals(rechargeCash.getPayStyle())) {
			// 宝付网银支付组装支付报文
			return paymentWyPCInfo(rechargeCash, finAccount, payConfig);
		}
		// 宝付认证支付组装支付报文
		return paymentInfo(rechargeCash, finAccount, payConfig);

	}

	/**
	 * @Description :构造宝付认证支付的充值对象(PC\WAP)
	 * @Method_Name : paymentInfo;
	 * @param rechargeCash
	 *            充值VO
	 * @param finAccount
	 *            账户
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年1月15日 下午4:27:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static Map<String, Object> paymentInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		logger.info("方法: paymentInfo, 构造宝付认证支付的充值对象, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
		// 1、 宝付认证支付组装支付报文
		Map<String, Object> rechargeData = new HashMap<String, Object>();
		rechargeData.put("txn_sub_type", RECHARGE_TRADE_SUB_TYPE);// 交易子类型
		rechargeData.put("biz_type", BIZ_TYPE);// 接入类型 -储蓄卡支付
		rechargeData.put("terminal_id", payConfig.getTerminalId());// 终端号
		rechargeData.put("member_id", payConfig.getMerchantNo());// 商户ID
		rechargeData.put("pay_code", rechargeCash.getBankCode());// 银行CODE
		rechargeData.put("acc_no", rechargeCash.getBankCard());// 银行卡号
		rechargeData.put("id_card_type", ID_CARD_TYPE);// 证件类型
		rechargeData.put("id_card", rechargeCash.getIdCard());// 证件号码
		rechargeData.put("id_holder", rechargeCash.getUserName());// 持卡人姓名
		rechargeData.put("mobile", "");// 银行卡预留手机号
		rechargeData.put("trans_id", rechargeCash.getPaymentFlowId());// 商户订单号
		// 交易金额 单位：分(1 元则提交100)
		rechargeData.put("txn_amt", rechargeCash.getTransMoney());
		rechargeData.put("trade_date", DateUtils.format(new Date(), DateUtils.DATE_HHMMSS));
		rechargeData.put("commodity_name", PRODUCT_NAME);
		rechargeData.put("commodity_amount", "1");// 商品数量（默认为1）
		rechargeData.put("user_name", rechargeCash.getUserName());
		rechargeData.put("page_url", payConfig.getSyncNoticeUrl());
		rechargeData.put("return_url", payConfig.getAsyncNoticeUrl());
		rechargeData.put("additional_info", "附加信息");
		rechargeData.put("req_reserved", "保留");
		logger.info("构造宝付认证支付的充值对象, 流水标识: {}, 认证充值渠道: {}, 充值明文报文: {}", rechargeCash.getPaymentFlowId(),
				rechargeCash.getPlatformSourceName(), rechargeData);
		// 2、 宝付充值数据加密
		String dataContent = JsonUtils.toJson(rechargeData);
		dataContent = Base64Util.encode(dataContent);
		dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
		logger.info("构造宝付认证支付的充值对象, 流水标识: {}, 认证充值渠道: {}, 充值加密报文: {}", rechargeCash.getPaymentFlowId(),
				rechargeCash.getPlatformSourceName(), dataContent);
		// 3、返回宝付认证支付的请求报文
		Map<String, Object> paymentReqData = new HashMap<String, Object>();
		paymentReqData.put("version", payConfig.getPayVersion());
		paymentReqData.put("input_charset", INPUT_CHARSET);
		paymentReqData.put("language", LANGUAGE);
		paymentReqData.put("terminal_id", payConfig.getTerminalId());
		paymentReqData.put("txn_type", RECHARGE_TRADE_TYPE);
		paymentReqData.put("txn_sub_type", RECHARGE_TRADE_SUB_TYPE);
		paymentReqData.put("member_id", payConfig.getMerchantNo());
		paymentReqData.put("data_type", DATA_TYPE);
		paymentReqData.put("data_content", dataContent);
		paymentReqData.put("back_url", payConfig.getBackUrl());
		logger.info("构造宝付认证支付的充值对象, 流水标识: {}, 认证充值渠: {}, 构造的充值报文: {}", rechargeCash.getPaymentFlowId(),
				rechargeCash.getPlatformSourceName(), paymentReqData.toString());
		return paymentReqData;
	}

	/**
	 * @Description : 组装宝付网银支付请求报文
	 * @Method_Name : paymentWyPCInfo;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            账户
	 * @param payConfig
	 *            支付配置文件对象
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年1月15日 下午2:29:18;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static Map<String, Object> paymentWyPCInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		logger.info("方法: paymentWyPCInfo, 构造宝付网银支付的充值对象, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
		String md5Key = payConfig.getPayMd5Key();// MD5加密KEY
		StringBuffer sbMd5 = new StringBuffer();
		// 宝付网银支付，加签名字段
		Map<String, Object> reqDataMap = new LinkedHashMap<String, Object>();
		reqDataMap.put("MemberID", payConfig.getMerchantNo());// 商户ID
		reqDataMap.put("PayID", rechargeCash.getBankCode());// 银行CODE
		reqDataMap.put("TradeDate", DateUtils.format(new Date(), DateUtils.DATE_HHMMSS));// 交易日期
		reqDataMap.put("TransID", rechargeCash.getPaymentFlowId());// 商户订单号
		reqDataMap.put("OrderMoney", rechargeCash.getTransMoney());// 订单金额
		reqDataMap.put("PageUrl", payConfig.getSyncNoticeUrl());// 页面跳转地址
		reqDataMap.put("ReturnUrl", payConfig.getAsyncNoticeUrl());// 服务器底层通知地址
		reqDataMap.put("NoticeType", "1");// 通知类型
		logger.info("构造宝付网银支付的充值对象, 充值流水标识: {}, 网银充值渠道: {}, 充值MD5加密前报文: {}", rechargeCash.getPaymentFlowId(),
				rechargeCash.getPlatformSourceName(), reqDataMap.toString());
		reqDataMap.forEach((payKey, payValue) -> {
			sbMd5.append(reqDataMap.get(payKey) + MARK);
		});
		String signature = RSAUtil.MD5(sbMd5.append(md5Key).toString());
		// 组装支付报文
		Map<String, Object> reqCommMap = new HashMap<String, Object>();
		reqCommMap.put("TerminalID", payConfig.getTerminalId());// 终端号
		reqCommMap.put("input_charset", CHARSET);// 编码
		reqCommMap.put("InterfaceVersion", payConfig.getPayVersion());
		reqCommMap.put("KeyType", "1");// 加密类型
		reqCommMap.put("ProductName", PRODUCT_NAME);// 商品名称
		reqCommMap.put("Amount", PRODUCT_NUM);// 商品数量
		reqCommMap.put("Username", rechargeCash.getUserName());// 支付用户名称
		reqCommMap.put("AdditionalInfo", PRODUCT_NAME);// 订单附加信息
		reqCommMap.put("Signature", signature);
		reqDataMap.putAll(reqCommMap);
		logger.info("构造宝付网银支付的充值对象, 充值流水标识: {}, 网银充值渠道: {}, 充值请求报文: {}", rechargeCash.getPaymentFlowId(),
				rechargeCash.getPlatformSourceName(), reqDataMap.toString());
		return reqDataMap;
	}

	/**
	 * @Description : 查询卡BIN信息
	 * @Method_Name : findCardBin;
	 * @param cardNo
	 *            卡号
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @throws UnsupportedEncodingException
	 * @Creation Date : 2018年1月12日 下午5:47:17;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findCardBin(String cardNo, FinPayConfig payConfig) {
		logger.info("方法: findCardBin, 宝付查询卡BIN, 入参: cardNo: {}, payConfig: {}", cardNo, payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			// 1、组装查询卡BIN的请求数据
			Map<String, String> cardBinData = new HashMap<String, String>();
			cardBinData.put("txn_type", CARD_BIN_TRADE_TYPE);// 交易类型
			cardBinData.put("txn_sub_type", CARD_BIN_TRADE_SUB_TYPE);// 交易子类型
			cardBinData.put("member_id", payConfig.getMerchantNo());// 商户号
			cardBinData.put("terminal_id", payConfig.getTerminalId());// 终端号
			Map<String, String> cardBinMap = new HashMap<String, String>();
			cardBinMap.put("trans_serial_no", CreateFlowUtil.createFlow("CB"));// 交易流水
			// 14 位定长格式：yyyyMMddHHmmss
			cardBinMap.put("trade_date", DateUtils.DATE_HHMMSS);// 交易日期
			cardBinMap.put("bank_card_no", cardNo);// 查询卡号
			cardBinMap.putAll(cardBinData);
			// 加密请求报文
			String dataContent = JsonUtils.toJson(cardBinMap);
			dataContent = new BASE64Encoder().encode(dataContent.getBytes(CHARSET));
			dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("version", payConfig.getPayVersion());// 版本号
			dataMap.put("data_type", DATA_TYPE);// 数据类型
			dataMap.put("data_content", dataContent);
			dataMap.putAll(cardBinData);
			logger.info("宝付查询卡BIN, 银行卡号标识: {}, 卡bin信息查询请求接口报文信息: {}", cardNo, dataMap.toString());
			// 2、调用宝付接口，进行查询
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dataMap);
			logger.info("宝付查询卡BIN, 银行卡号标识: {}, 卡bin信息查询响应接口报文信息: {}", cardNo, resJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "查询卡BIN失败,返回信息为空!");
			}
			// 3、解析宝付响应报文
			resJson = RSAUtil.decryptByPubCerFile(resJson, payConfig.getPublicKey());
			resJson = new String(new BASE64Decoder().decodeBuffer(resJson), CHARSET);
			logger.info("宝付查询卡BIN, 银行卡号标识: {}, 卡bin信息查询响应接口报文解密信息: {}", cardNo, resJson);
			Map<String, Object> resMap = JsonUtils.json2GenericObject(resJson,
					new TypeReference<Map<String, Object>>() {
					}, null);
			String ret_code = resMap.get("resp_code").toString();// 返回的交易码
			String ret_msg = resMap.get("resp_msg").toString();// 返回的交易信息
			// 组装返回响应给客户端的数据
			if (PaymentConstants.THIRD_SUCCESS_FLAG.equals(ret_code)) {
				String bankName = resMap.get("bank_description").toString();
				String bankCode = resMap.get("pay_code").toString();
				if (StringUtils.isBlank(bankCode)) {
					return new ResponseEntity<>(Constants.ERROR, "查询卡BIN失败,暂不支持" + bankName);
				}
				responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
				Map<String, Object> resultCardBinMap = new HashMap<String, Object>();
				resultCardBinMap.put("bankName", bankName);
				resultCardBinMap.put("bankCode", bankCode);
				resultPay.put("cardBin", resultCardBinMap);
				responseEntity.setParams(resultPay);
			} else {
				responseEntity = new ResponseEntity<>(Constants.ERROR, ret_code + "|" + ret_msg);
			}
		} catch (Exception e) {
			logger.error("宝付查询卡BIN, 银行卡号标识: {}, 宝付银行卡卡bin查询异常信息: ", cardNo, e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持！");
		}
		return responseEntity;
	}

	/**
	 * @Description : 宝付WAP充值
	 * @Method_Name : paymentWapInfo;
	 * @param rechargeCash
	 *            充值VO
	 * @param finAccount
	 *            账户
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @return : Map<String,Object>;
	 * @throws Exception
	 * @Creation Date : 2018年1月15日 下午4:18:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> paymentWapInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		return paymentInfo(rechargeCash, finAccount, payConfig);
	}

	/**
	 * @Description : 宝付APP充值接口
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            用户账户
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年1月22日 下午2:49:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> paymentAppInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		logger.info("方法: paymentAppInfo, 宝付APP充值, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
		// 1、 宝付app支付组装支付报文
		Map<String, Object> rechargeData = new HashMap<String, Object>();
		rechargeData.put("txn_sub_type", RECHARGE_APP_TRADE_SUB_TYPE);// 交易子类型
		rechargeData.put("biz_type", BIZ_TYPE);// 接入类型 -储蓄卡支付
		rechargeData.put("terminal_id", payConfig.getTerminalId());// 终端号
		rechargeData.put("member_id", payConfig.getMerchantNo());// 商户ID
		rechargeData.put("pay_code", rechargeCash.getBankCode());// 银行CODE
		rechargeData.put("acc_no", rechargeCash.getBankCard());// 银行卡号
		rechargeData.put("id_card_type", ID_CARD_TYPE);// 证件类型
		rechargeData.put("id_card", rechargeCash.getIdCard());// 证件号码
		rechargeData.put("id_holder", rechargeCash.getUserName());// 持卡人姓名
		rechargeData.put("mobile", rechargeCash.getTel());// 银行卡预留手机号
		rechargeData.put("trans_id", rechargeCash.getPaymentFlowId());// 商户订单号
		// 交易金额 单位：分(1 元则提交100)
		rechargeData.put("txn_amt", rechargeCash.getTransMoney());
		rechargeData.put("trade_date", DateUtils.format(new Date(), DateUtils.DATE_HHMMSS));
		rechargeData.put("commodity_name", PRODUCT_NAME);
		rechargeData.put("commodity_amount", "1");// 商品数量（默认为1）
		rechargeData.put("user_name", rechargeCash.getUserName());
		rechargeData.put("page_url", payConfig.getSyncNoticeUrl());
		rechargeData.put("return_url", payConfig.getAsyncNoticeUrl());
		rechargeData.put("additional_info", "附加信息");
		rechargeData.put("req_reserved", "保留");
		// 2、 宝付充值数据加密
		String dataContent = JsonUtils.toJson(rechargeData);
		dataContent = Base64Util.encode(dataContent);
		dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
		logger.info("宝付APP充值, 用户标识: {}, 充值流水标识: {}, 充值渠道: {}, 充值加密报文: {}", rechargeCash.getUserId(),
				rechargeCash.getPaymentFlowId(), rechargeCash.getPlatformSourceName(), dataContent);
		// 3、调用宝付app支付的请求报文
		Map<String, Object> paymentReqData = new HashMap<String, Object>();
		paymentReqData.put("version", payConfig.getPayVersion());
		paymentReqData.put("input_charset", INPUT_CHARSET);
		paymentReqData.put("language", LANGUAGE);
		paymentReqData.put("terminal_id", payConfig.getTerminalId());
		paymentReqData.put("txn_type", RECHARGE_APP_TRADE_TYPE);
		paymentReqData.put("txn_sub_type", RECHARGE_APP_TRADE_SUB_TYPE);
		paymentReqData.put("member_id", payConfig.getMerchantNo());
		paymentReqData.put("data_type", DATA_TYPE);
		paymentReqData.put("data_content", dataContent);
		logger.info("APP调用宝付预充值, 用户标识: {}, 充值流水标识: {}, 充值渠道: {}, 充值请求报文: {}", rechargeCash.getUserId(),
				rechargeCash.getPaymentFlowId(), rechargeCash.getPlatformSourceName(), paymentReqData.toString());
		String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), paymentReqData);
		logger.info("APP调用宝付预充值, 用户标识: {}, 充值流水标识: {}, 充值渠道: {}, 充值响应报文:{}", rechargeCash.getUserId(),
				rechargeCash.getPaymentFlowId(), rechargeCash.getPlatformSourceName(), resJson);
		Map<String, Object> resMap = JsonUtils.json2GenericObject(resJson, new TypeReference<Map<String, Object>>() {
		}, null);

		// 4、返回给APP客户端的数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String retMsg = "";// 交易返回信息
		String tradeFlowId = "";// 第三方预支付订单号
		String retCode = "";// 交易码
		if (!resMap.containsKey("retCode")) {
			retMsg = "宝付未返回retCode交易码";
		} else {
			retCode = resMap.get("retCode").toString();
			retMsg = resMap.get("retMsg").toString();
			// 如果返回0000，则认为预支付交易成功
			if (BAOFU_RZ_PAY_CHECK_STATE_SUCCESS.equals(retCode)) {
				tradeFlowId = resMap.get("tradeNo").toString();
			}
		}
		resultMap.put("retCode", retCode);// 交易码
		resultMap.put("retMsg", retMsg);// 交易描述信息
		resultMap.put("tradeFlowId", tradeFlowId);// 第三方返回的流水号
		resultMap.put("no_order", rechargeCash.getPaymentFlowId());// 支付订单号
		resultMap.put("merchantNo", payConfig.getMerchantNo());// 商户号
		resultMap.put("notifyUrl", payConfig.getAsyncNoticeUrl());// 异步通知地址
		return resultMap;
	}

	/**
	 * @Description : 宝付支付查证接口
	 * @Method_Name : payCheck;
	 * @param flowId
	 *            支付流水ID
	 * @param tradeType
	 *            交易类型
	 * @param rechargeSource
	 *            充值来源
	 * @param payConfig
	 *            支付配置信息
	 * @param createTime
	 *            订单生成时间
	 * @return
	 * @return : ResponseEntity<?>;
	 * @throws Exception
	 * @Creation Date : 2018年1月17日 上午9:14:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> payCheck(String flowId, Integer tradeType, Integer rechargeSource,
			FinPayConfig payConfig, Date createTime) throws Exception {
		logger.info("方法: payCheck, 宝付支付查证, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, payConfig: {}", flowId,
				tradeType, rechargeSource, payConfig.toString());
		// 判断交易类型是充值，还是提现操作
		if (PayStyleEnum.RECHARGE.getValue() == tradeType) {
			if (rechargeSource == PayStyleEnum.WY.getValue()) {
				return rechargeWyPayCheck(flowId, payConfig);
			} else if (rechargeSource == PayStyleEnum.RZ.getValue()) {
				return rechargeRzPayCheck(flowId, payConfig);
			} else {
				logger.info("宝付支付查证, 查证流水标识: {}, 暂无此支付查证方式: {}", flowId, rechargeSource);
			}
		} else if (PayStyleEnum.WITHDRAW.getValue() == tradeType) {
			return withDrawPayCheck(flowId, payConfig);
		} else {
			logger.info("宝付支付查证, 查证流水标识: {}, 暂无此交易类型: {}", flowId, tradeType);
		}
		return null;
	}

	/**
	 * @Description : 宝付网银充值，支付查证接口
	 * @Method_Name : rechargeWyPayCheck;
	 * @param flowId
	 *            订单号
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月17日 下午4:07:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> rechargeWyPayCheck(String flowId, FinPayConfig payConfig) throws Exception {
		logger.info("方法: rechargeWyPayCheck, 宝付网银充值支付查证, 查证流水标识: {}, 入参: flowId: {}, payConfig: {}", flowId, flowId,
				payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			// 网银支付查询请求数据封装
			Map<String, String> reqData = new LinkedHashMap<String, String>();
			StringBuffer sbMd5 = new StringBuffer();
			reqData.put("MemberID", payConfig.getMerchantNo());
			reqData.put("TerminalID", payConfig.getTerminalId());
			reqData.put("TransID", flowId);
			reqData.forEach((payKey, payValue) -> {
				sbMd5.append(payValue + MARK);
			});
			String md5Sign = RSAUtil.MD5(sbMd5.append(payConfig.getPayMd5Key()).toString());
			reqData.put("MD5Sign", md5Sign);
			logger.info("宝付网银充值支付查证, 支付查证流水标识: {}, 网银查询渠道: {}, 支付查证加密报文: {}", flowId, payConfig.getThirdName(),
					reqData.toString());
			// 调用宝付网银支付查证接口
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqData);
			logger.info("宝付网银充值支付查证, 支付查证流水标识: {}, 网银查询渠道: {}, 支付查证响应报文: {}", flowId, payConfig.getThirdName(),
					resJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,返回信息为空!");
			}
			// 进行签名验证
			Map<String, Object> result = new HashMap<String, Object>();
			String[] arrayResData = resJson.split("[|]");
			if (arrayResData.length == 7) {
				String resData = resJson.substring(0, resJson.length() - arrayResData[6].length());
				String md5sign = RSAUtil.MD5(resData + payConfig.getPayMd5Key());
				if (!md5sign.equals(arrayResData[6])) {
					return new ResponseEntity<>(Constants.ERROR, "支付查证，交易信息被篡改！");
				}
				String orderId = arrayResData[2];// 订单号
				String transState = arrayResData[3];// 交易状态
				String transMoney = arrayResData[4];// 支付金额
				if (BAOFU_WY_PAY_CHECK_STATE_SUCCESS.equals(transState)) {
					// 如果交易结果为Y,则认为支付成功
					result.put("transState", PAY_STATE_SUCCESS);// 支付成功
					result.put("orderId", orderId);// 返回订单号
					result.put("transMoney", transMoney);// 交易金额
				} else if (BAOFU_WY_PAY_CHECK_STATE_FAILURE.equals(transState)
						|| BAOFU_WY_PAY_CHECK_STATE_NO_DATA.equals(transState)) {
					// 如果交易结果为订单不存在或交易失败，则认为此笔订单为失败，否则认为是交易等待处理中
					result.put("transState", PAY_STATE_FAIL);// 支付失败
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 等待处理中
				}
			} else {
				return new ResponseEntity<>(Constants.ERROR, "网银支付查询返回数据有误");
			}
			// 返回支付查证状态
			responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
			resultPay.put("resPayCheck", result);
			responseEntity.setParams(resultPay);
		} catch (Exception e) {
			logger.error("宝付网银充值支付查证, 支付查证流水标识: {}, 入参: flowId: {},  payConfig: {}, 网银查询渠道: {}, 支付查证异常信息: ", flowId,
					flowId, payConfig.toString(), payConfig.getThirdName(), e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
		return responseEntity;
	}

	/**
	 * @Description : 宝付认证充值，支付查证接口
	 * @Method_Name : rechargeWyPayCheck;
	 * @param flowId
	 *            订单号
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月17日 下午4:07:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static ResponseEntity<?> rechargeRzPayCheck(String flowId, FinPayConfig payConfig) throws Exception {
		logger.info("方法: rechargeRzPayCheck, 宝付认证充值支付查证, 支付查证流水标识: {}, 入参: flowId: {}, payConfig: {}", flowId, flowId,
				payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			// 组装宝付支付查询证请求数据
			Map<String, String> reqData = new HashMap<String, String>();
			reqData.put("orig_trans_id", flowId);
			reqData.put("trans_serial_no", CreateFlowUtil.createFlow("FC"));
			reqData.put("terminal_id", payConfig.getTerminalId());
			reqData.put("member_id", payConfig.getMerchantNo());
			// 加密请求报文
			String dataContent = JsonUtils.toJson(reqData);
			dataContent = Base64Util.encode(dataContent);
			dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			Map<String, String> reqDataMap = new HashMap<String, String>();
			reqDataMap.put("version", payConfig.getPayVersion());
			reqDataMap.put("input_charset", INPUT_CHARSET);
			reqDataMap.put("language", LANGUAGE);
			reqDataMap.put("terminal_id", payConfig.getTerminalId());
			reqDataMap.put("member_id", payConfig.getMerchantNo());
			reqDataMap.put("data_type", DATA_TYPE);
			reqDataMap.put("data_content", dataContent);
			logger.info("宝付认证充值支付查证, 支付查证流水标识: {}, 认证查询渠道: {}, 支付查证加密报文: {}", flowId, payConfig.getThirdName(),
					dataContent);
			// 调用宝付支付查证接口
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqDataMap);
			logger.info("宝付认证充值支付查证, 支付查证流水标识: {}, 认证查询渠道: {}, 支付查证响应报文: {}", flowId, payConfig.getThirdName(),
					resJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,返回信息为空!");
			}
			// 解析宝付支付查证响应报文，返回给客户端
			resJson = RSAUtil.decryptByPubCerFile(resJson, payConfig.getPublicKey());
			resJson = Base64Util.decode(resJson);
			logger.info("宝付认证充值支付查证, 支付查证流水标识: {}, 认证查询渠道: {}, 支付查证响应报文解析后的明文: {}", flowId, payConfig.getThirdName(),
					resJson);
			Map<String, Object> resMap = JsonUtils.json2GenericObject(resJson,
					new TypeReference<Map<String, Object>>() {
					}, null);
			// 注：成功金额succ_amt字段和宝付交易号trade_no字段只有订单支付成功的时候才有返回值，其他情况是没有返回值的
			Map<String, Object> result = new HashMap<String, Object>();
			String ret_code = resMap.get("resp_code").toString();// 交易返回结果
			result.put("transMsg", resMap.get("resp_msg").toString());// 交易描述结果
			if (StringUtils.isBlank(ret_code)) {
				return new ResponseEntity<>(Constants.ERROR, "支付查证响应数据无返回码!");
			}
			if (BAOFU_RZ_PAY_CHECK_STATE_SUCCESS.equals(ret_code)) {
				// 如果交易结果为0000,并且返回交易金额和 商户订单号，则认为支付成功
				if (StringUtils.isNotBlank(resMap.get("trade_no").toString())
						&& StringUtils.isNotBlank(resMap.get("succ_amt").toString())) {
					result.put("transState", PAY_STATE_SUCCESS);// 支付成功
					result.put("orderId", resMap.get("orig_trans_id").toString());// 返回订单号
					result.put("transMoney", resMap.get("succ_amt").toString());// 交易金额
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 银行处理中
				}
			} else {
				// 如果交易结果为订单不存在或交易失败，则认为此笔订单为失败，否则认为是交易等待处理中
				if (BAOFU_RZ_PAY_CHECK_STATE_NO_DATA.equals(ret_code)
						|| BAOFU_RZ_PAY_CHECK_STATE_FAILURE.equals(ret_code)) {
					result.put("transState", PAY_STATE_FAIL);// 支付失败
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 银行处理中
				}
			}
			// 返回支付查证状态
			responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
			resultPay.put("resPayCheck", result);
			responseEntity.setParams(resultPay);
		} catch (Exception e) {
			logger.error("宝付认证充值支付查证, 支付查证流水标识: {}, 入参: flowId: {}, payConfig: {}, 认证查询渠道: {}, 支付查证异常信息: ", flowId,
					flowId, payConfig.toString(), payConfig.getThirdName(), e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
		return responseEntity;
	}

	/**
	 * @Description : 宝付提现，支付查证接口
	 * @Method_Name : rechargeWyPayCheck;
	 * @param flowId
	 *            订单号
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月17日 下午4:07:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	public static ResponseEntity<?> withDrawPayCheck(String flowId, FinPayConfig payConfig) throws Exception {
		logger.info("方法: withDrawPayCheck, 宝付提现支付查证, 支付查证流水标识: {}, 入参: flowId: {}, payConfig: {}", flowId, flowId,
				payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			TransContent<TransResResult> transContent = new TransContent<TransResResult>(TransContent.DATA_TYPE_JSON);
			List<TransResResult> trans_reqDatas = new ArrayList<TransResResult>();
			TransResResult transReqData = new TransResResult();
			transReqData.setTrans_no(flowId);
			trans_reqDatas.add(transReqData);
			transContent.setTrans_reqDatas(trans_reqDatas);
			String bean2XmlStr = transContent.obj2Str(transContent);
			logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证请求加密前dataContent报文: {}", flowId, payConfig.getThirdName(),
					transContent.toString());
			String dataContent = Base64Util.encode(bean2XmlStr);
			dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证请求加密后dataContent报文: {}", flowId, payConfig.getThirdName(),
					dataContent);
			Map<String, String> reqDataMap = new HashMap<String, String>();
			reqDataMap.put("member_id", payConfig.getMerchantNo());
			reqDataMap.put("terminal_id", payConfig.getTerminalId());
			reqDataMap.put("data_type", DATA_TYPE);
			reqDataMap.put("data_content", dataContent);
			reqDataMap.put("version", payConfig.getPayVersion());
			logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证请求报文: {}", flowId, payConfig.getThirdName(),
					reqDataMap.toString());
			// 调用宝付支付查证接口
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqDataMap);
			logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证响应报文: {}", flowId, payConfig.getThirdName(), resJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,返回信息为空!");
			}
			// 解析报文，返回给客户端
			resJson = RSAUtil.decryptByPubCerFile(resJson, payConfig.getPublicKey());
			resJson = Base64Util.decode(resJson);
			logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证响应报文解析后的明文: {}", flowId, payConfig.getThirdName(), resJson);
			TransContent<TransResResult> resData = new TransContent<TransResResult>(TransContent.DATA_TYPE_JSON);
			resData = (TransContent<TransResResult>) resData.str2Obj(resJson, TransResResult.class);
			TransHead headData = resData.getTrans_head();
			TransResResult transData = resData.getTrans_reqDatas().get(0);
			String ret_code = headData.getReturn_code();
			Map<String, Object> result = new HashMap<String, Object>();
			if (BAOFU_RZ_PAY_CHECK_STATE_SUCCESS.equals(ret_code)) {
				// 如果交易结果为0000,则认为此笔交易请求成功,
				// 如果交易结果为已退款或交易失败，则认为此笔订单为失败，否则认为是交易等待处理中
				if (BAOFU_WITHDRAW_CHECK_STATE_SUCCESS.equals(transData.getState())) {
					result.put("transState", PAY_STATE_SUCCESS);// 支付成功
					result.put("orderId", transData.getTrans_orderid());// 返回订单号
					result.put("transMoney", transData.getTrans_money());// 交易金额
				} else if (BAOFU_WITHDRAW_CHECK_STATE_FAILURE.equals(transData.getState())
						|| BAOFU_WITHDRAW_CHECK_STATE_CANCEL.equals(transData.getState())) {
					result.put("transState", PAY_STATE_FAIL);// 支付失败
					logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证交易失败: {}", flowId, transData.getTrans_remark());
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 银行处理中
					logger.info("宝付提现支付查证, 支付查证流水标识: {}, 提现查证交易失败: {}", flowId, transData.getTrans_remark());
				}
			} else {
				return new ResponseEntity<>(Constants.ERROR, "提现支付查证异常:" + ret_code + "|" + headData.getReturn_msg());
			}
			responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
			resultPay.put("resPayCheck", result);
			responseEntity.setParams(resultPay);
		} catch (Exception e) {
			logger.error("宝付提现支付查证, 支付查证流水标识: {}, 入参: flowId: {}, payConfig: {}, 支付查证异常信息: ", flowId, flowId,
					payConfig.toString(), e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
		return responseEntity;
	}

	/**
	 * @Description : 封装宝付提现对象
	 * @Method_Name : buildWithDrawReqData;
	 * @param finBankCard
	 *            银行卡信息
	 * @param withDrawCashInfo
	 *            提现信息
	 * @param finPaymentRecord
	 *            支付记录信息
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年1月19日 上午10:53:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> buildWithDrawReqData(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		TransContent<TransReqData> transContent = new TransContent<TransReqData>(TransContent.DATA_TYPE_JSON);
		List<TransReqData> trans_reqDatas = new ArrayList<TransReqData>();
		TransReqData reqData = new TransReqData();
		reqData.setTrans_no(finPaymentRecord.getFlowId());// 商户订单号
		reqData.setTrans_money(String.valueOf(finPaymentRecord.getTransMoney()));// 交易金额
		reqData.setTo_acc_no(finBankCard.getBankCard());// 收款人账号
		reqData.setTo_bank_name(finBankCard.getBankName());// 收款人银行名称
		reqData.setTo_acc_name(withDrawCashInfo.getUserName());// 收款人姓名
		if(withDrawCashInfo.getUserType() == PaymentConstants.TRADE_TYPE_USER_PERSONAL) {
			reqData.setTrans_card_id(withDrawCashInfo.getIdCard());// 身份证号码
		}else {
			reqData.setTo_pro_name(finCityRefer.getProvince());// 收款人开户行省名
			reqData.setTo_city_name(finCityRefer.getCity());// 收款人开户行市名
			reqData.setTo_acc_dept(finBankCard.getBranchName());// 收款人开户行机构名
		}
		trans_reqDatas.add(reqData);
		transContent.setTrans_reqDatas(trans_reqDatas);
		String reqStr = transContent.obj2Str(transContent);
		logger.info("宝付提现组装对象, 用户标识: {}, 提现流水标识: {}, 宝付提现data_content字段加密前报文信息: {}", withDrawCashInfo.getRegUserId(),
				withDrawCashInfo.getFlowId(), reqStr);
		String dataContent = Base64Util.encode(reqStr);
		dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
		Map<String, String> reqDataMap = new HashMap<String, String>();
		reqDataMap.put("member_id", payConfig.getMerchantNo());
		reqDataMap.put("terminal_id", payConfig.getTerminalId());
		reqDataMap.put("data_type", BaofuPayFactory.DATA_TYPE);
		reqDataMap.put("data_content", dataContent);
		reqDataMap.put("version", payConfig.getPayVersion());
		logger.info("宝付提现组装对象, 用户标识: {}, 提现流水标识: {}, 宝付提现请求报文信息: {}", withDrawCashInfo.getRegUserId(),
				withDrawCashInfo.getFlowId(), reqDataMap.toString());
		return reqDataMap;
	}
	/**
	 * @Description : 封装宝付提现对象
	 * @Method_Name : buildWithDrawReqData;
	 * @param finBankCard
	 *            银行卡信息
	 * @param withDrawCashInfo
	 *            提现信息
	 * @param finPaymentRecord
	 *            支付记录信息
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年1月19日 上午10:53:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	
	/**
	 * @Description : 宝付认证验证签名并解析数据
	 * @param data_content 验签数据
	 * 	
	 * @param finPayConfig
	 * 			支付配置信息
	 * @return
	 * @Creation Date : 2018-10-29 10:01:23
	 * @author : binliang@hongkun.com.cn 梁彬; 
	 */
	public static ResponseEntity<?> checkSignRechargeRz(String data_content , FinPayConfig finPayConfig){
		logger.info("方法，checkSignRechargeRz，入参：data_content : {} , finPayConfig : {}", data_content,finPayConfig );
		ResponseEntity<?> responseEntity = null; 
		data_content = RSAUtil.decryptByPubCerFile(data_content,finPayConfig.getPublicKey());
		if(StringUtils.isBlank(data_content)){
			return new ResponseEntity<>(Constants.ERROR, "解密公钥错误");
		}
		Map<String,Object> result =  null ;
		try {
			data_content = SecurityUtil.Base64Decode(data_content);
			if(StringUtils.isBlank(data_content)){
				return new ResponseEntity<>(Constants.ERROR, "签名错误");
			}
			result = JXMConvertUtil.JsonConvertHashMap((Object)data_content);//将JSON转化为Map对象。
			//logger.info("解密的参数转为map：{} " , result);
			if(result == null || !result.containsKey("resp_code")){
				return new ResponseEntity<>(Constants.ERROR, "解析后缺失参数resp_code");
			}
		} catch (Exception e) {
			logger.error("方法，checkSignRechargeRz , 解析宝付回调参数异常，描述：{}" , e);
			return new ResponseEntity<>(Constants.ERROR, "验签错误");
		}
		responseEntity = new ResponseEntity<>(Constants.SUCCESS);
		responseEntity.setParams(result);
		logger.info("方法, checkSignRechargeRz, 返回：result:{} ", result);
		return responseEntity;
	}
}
