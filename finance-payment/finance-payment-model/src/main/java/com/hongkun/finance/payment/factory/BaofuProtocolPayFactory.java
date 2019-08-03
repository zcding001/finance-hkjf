package com.hongkun.finance.payment.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.bfpayvo.TransContent;
import com.hongkun.finance.payment.bfpayvo.TransHead;
import com.hongkun.finance.payment.bfpayvo.TransResResult;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.security.Base64Util;
import com.hongkun.finance.payment.util.CreateFlowUtil;
import com.hongkun.finance.payment.util.FormatUtil;
import com.hongkun.finance.payment.util.HttpUtils;
import com.hongkun.finance.payment.util.RSAUtil;
import com.hongkun.finance.payment.util.SecurityUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.json.JsonUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hongkun.finance.payment.constant.PaymentConstants.BAOFU_RZ_PAY_CHECK_STATE_SUCCESS;
import static com.hongkun.finance.payment.constant.PaymentConstants.BAOFU_WITHDRAW_CHECK_STATE_CANCEL;
import static com.hongkun.finance.payment.constant.PaymentConstants.BAOFU_WITHDRAW_CHECK_STATE_FAILURE;
import static com.hongkun.finance.payment.constant.PaymentConstants.BAOFU_WITHDRAW_CHECK_STATE_SUCCESS;
import static com.hongkun.finance.payment.constant.PaymentConstants.PAY_STATE_FAIL;
import static com.hongkun.finance.payment.constant.PaymentConstants.PAY_STATE_PROCESSING;
import static com.hongkun.finance.payment.constant.PaymentConstants.PAY_STATE_SUCCESS;
import static com.hongkun.finance.payment.constant.PaymentConstants.SUCCESS_FLAG_NAME;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description : 宝付协议支付工厂类
 * @Project : finance-payment-modelReqPay
 * @Program Name : com.hongkun.finance.payment.factory.BaofuXyPayFactory.java
 * @Author : binliang@hongkunjinfu.com.cn 梁彬
 */
public class BaofuProtocolPayFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(BaofuProtocolPayFactory.class);
	/********************* 查询卡BIN交易类型及子类型 *******************************************/
	// 交易类型
	private static final String CARD_BIN_TRADE_TYPE = "3001";
	// 交易子类型
	private static final String CARD_BIN_TRADE_SUB_TYPE = "01361";
	// 数据类型
	private static final String DATA_TYPE = "json";
	// 宝付协议支付AESK 自定义
	private static final String AESK = "6dfe48dsc5fe54d6";
	// 身份证号类型
	private static final String ID_CARD_TYPE = "01";
	// 银行卡类型101 借记卡，102 信用卡
	private static final String BANK_CARD_TYPE = "101";
	// 编码
	private static final String CHARSET = "UTF-8";
	// 标记
	private static final String MARK = "|";
	// 商品名称
	private static final String PRODUCT_NAME = "鸿坤金服充值";
	// 商品数量
	private static final String PRODUCT_NUM = "1";
	/**
	 * @Description : 宝付协议支付预绑卡api
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            充值对象
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-10 15:11:32;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static Map<String, Object> paymentPreTiedCard(RechargeCash rechargeCash, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: paymentPretiedCard, 宝付协议支付预绑卡api, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
		// 1、 宝付协议预绑卡api组装支付报文
		String dgtl_envlp = "01|" + AESK;// 使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
		logger.info("密码dgtl_envlp：{}", dgtl_envlp);

		dgtl_envlp = RSAUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), payConfig.getPublicKey());// 公钥加密
		// 账户信息[银行卡号|持卡人姓名|证件号|手机号|银行卡安全码|银行卡有效期]
		// 例：6212262309006279753|何豪雨|310115199007129776|15823781632||
		String cardInfo = rechargeCash.getBankCard() + MARK + rechargeCash.getUserName() + MARK
				+ rechargeCash.getIdCard() + MARK + rechargeCash.getTel() + MARK + MARK;

		logger.info("SHA-1摘要[Cardinfo]结果：{}", SecurityUtil.sha1X16(cardInfo, CHARSET));

		logger.info("卡信息：{},长度：{}", cardInfo, cardInfo.length());
		cardInfo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(cardInfo), AESK);// 先BASE64后进行AES加密
		logger.info("AES结果:{}", cardInfo);

		Map<String, String> dateArry = new TreeMap<String, String>();
		dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		dateArry.put("msg_id", CreateFlowUtil.createFlow("PR"));// 报文流水号
		dateArry.put("version", "4.0.0.0");
		dateArry.put("terminal_id", payConfig.getTerminalId());
		dateArry.put("txn_type", "01");// 交易类型
		dateArry.put("member_id", payConfig.getMerchantNo());
		dateArry.put("dgtl_envlp", dgtl_envlp);
		dateArry.put("user_id", rechargeCash.getUserId() + "_" + rechargeCash.getLoginTel());// 用户在商户平台唯一ID
		dateArry.put("card_type", BANK_CARD_TYPE);
		dateArry.put("id_card_type", ID_CARD_TYPE);// 证件类型
		dateArry.put("acc_info", cardInfo);

		String SignVStr = FormatUtil.coverMap2String(dateArry);
		logger.info("SHA-1摘要字串：{}", SignVStr);
		String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名

		logger.info("SHA-1摘要结果：{}", signature);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resStatus = "";// 结果码
		String resMsg = "";// 返回描述
		try {
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("RSA签名结果：{}", sign);
			dateArry.put("signature", sign);// 签名域
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("请求返回:{}", postString);
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("缺少验签参数！");
				throw new Exception("缺少验签参数！");
			}
			String rSign = returnData.get("signature");
			logger.info("返回的验签值：{}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (!RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.error("验签失败！");// 验签成功
				throw new Exception("预绑卡，验签失败！");
			}
			logger.info("验签成功！");// 验签成功
			if (!returnData.containsKey("resp_code")) {
				throw new Exception("缺少resp_code参数！");
			}
			// 4、把结果返回给上一层
			if (returnData.get("resp_code") == null) {
				resStatus = "9999";
				resMsg = "预绑卡响应参数为空";
				resultMap.put("resStatus", resStatus);
				resultMap.put("resMsg", resMsg);
				return resultMap;
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				if (!returnData.containsKey("dgtl_envlp")) {
					throw new Exception("缺少dgtl_envlp参数！");
				}
				String RDgtlEnvlp = SecurityUtil.Base64Decode(RSAUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"),
						payConfig.getPrivateKey(), payConfig.getPayMd5Key()));
				logger.info("返回数字信封：{}", RDgtlEnvlp);
				String RAesKey = FormatUtil.getAesKey(RDgtlEnvlp);// 获取返回的AESkey
				logger.info("返回的AESkey:{}", RAesKey);
				if (AESK.equals(RAesKey)) {
					throw new Exception("返回的AESkey值不相等，校验失败！");
				}
				String unique_code = SecurityUtil
						.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("unique_code"), RAesKey));
				logger.info("唯一码:{}", unique_code);
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
				resultMap.put("unique_code", unique_code);// 预签约唯一码
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resStatus = "1001";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resStatus = "1002";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else {
				resStatus = "1003";
				resMsg = "预支付绑卡系统异常";
			}
		} catch (Exception e) {
			logger.error("预绑卡接口异常{}", e);
			resStatus = "999";
			resMsg = "绑卡接口异常";
		}
		resultMap.put("resStatus", resStatus);
		resultMap.put("resMsg", resMsg);
		logger.info("方法: paymentPretiedCard, 宝付协议支付预绑卡api 返回 resultMap: {},resStatus:{},resMsg{}", resultMap,
				resultMap.get("resStatus"), resultMap.get("resMsg"));
		return resultMap;
	}

	/**
	 * @Description : 宝付协议支付-确认绑卡
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            确认绑卡对象
	 * @param payConfig
	 *            支付配置信息
	 * @param uniqueCode
	 *            预绑卡时返回的预签唯一码
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-12 14:14:51;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static Map<String, Object> paymentConfirmTiedCard(RechargeCash rechargeCash, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: paymentConfirmTiedCard, 宝付协议支付-确认绑卡, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
		// 1、 宝付协议确认绑卡api组装支付报文
		String dgtl_envlp = "01|" + AESK;// 使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
		logger.info("密码dgtl_envlp：{}", dgtl_envlp);

		dgtl_envlp = RSAUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), payConfig.getPublicKey());// 公钥加密

		String uniqueCode = rechargeCash.getPayUnionCode() + MARK + rechargeCash.getVerificationCode();// 预签约唯一码(预绑卡返回的值)[格式：预签约唯一码|短信验证码]
		logger.info("预签约唯一码：{}", uniqueCode);
		uniqueCode = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(uniqueCode), AESK);// 先BASE64后进行AES加密
		logger.info("AES结果:{}", uniqueCode);

		Map<String, String> dateArry = new TreeMap<String, String>();
		dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		dateArry.put("msg_id", CreateFlowUtil.createFlow("BK"));// 报文流水号
		dateArry.put("version", "4.0.0.0");
		dateArry.put("terminal_id", payConfig.getTerminalId());
		dateArry.put("txn_type", "02");// 交易类型
		dateArry.put("member_id", payConfig.getMerchantNo());
		dateArry.put("dgtl_envlp", dgtl_envlp);
		dateArry.put("unique_code", uniqueCode);// 预签约唯一码

		String SignVStr = FormatUtil.coverMap2String(dateArry);
		logger.info("SHA-1摘要字串：{}", SignVStr);
		String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名
		logger.info("SHA-1摘要结果：{}", signature);

		String resStatus = "";// 结果码
		String resMsg = "";// 返回描述
		// 4、把结果返回给上一层
		Map<String, Object> resultMap = new HashMap<String, Object>();
		;
		try {
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("RSA签名结果：{}", sign);
			dateArry.put("signature", sign);// 签名域
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("确认绑卡请求返回:{}", postString);
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("确认绑卡同步响应结果返回为空！");
				throw new Exception("确认绑卡同步响应结果返回为空！");
			}
			String rSign = returnData.get("signature");
			logger.info("返回的验签值：{}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (!RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.error("验签失败！");// 验签失败
				throw new Exception("确认绑卡，验签失败！");
			}
			logger.info("验签成功！");// 验签成功
			if (!returnData.containsKey("resp_code")) {
				throw new Exception("缺少resp_code参数！");
			}
			if (returnData.get("resp_code") == null) {
				resStatus = "9999";
				resMsg = "确认绑卡响应参数为空";
				resultMap.put("resStatus", resStatus);
				resultMap.put("resMsg", resMsg);
				return resultMap;
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				if (!returnData.containsKey("dgtl_envlp")) {
					throw new Exception("缺少dgtl_envlp参数！");
				}
				String RDgtlEnvlp = SecurityUtil.Base64Decode(RSAUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"),
						payConfig.getPrivateKey(), payConfig.getPayMd5Key()));
				logger.info("返回数字信封：{}", RDgtlEnvlp);
				String rAesKey = FormatUtil.getAesKey(RDgtlEnvlp);// 获取返回的AESkey
				logger.info("返回的AESkey:{}", rAesKey);
				if (AESK.equals(rAesKey)) {
					throw new Exception("返回的AESkey值不相等，校验失败！");
				}
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				String no_agree = SecurityUtil
						.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("protocol_no").toString(), rAesKey));
				resultMap.put("no_agree", no_agree);
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resStatus = "1001";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resStatus = "1002";
				resMsg = returnData.get("biz_resp_msg").toString();
			}
			if (returnData.get("biz_resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS)) {
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
			}
		} catch (Exception e) {
			logger.error("确认绑卡接口异常{}", e);
			resStatus = "999";
			resMsg = "绑卡接口异常";
		}
		resultMap.put("resStatus", resStatus);
		resultMap.put("resMsg", resMsg);
		logger.info("paymentConfirmTiedCard method , resultMap:{}", resultMap);
		return resultMap;
	}

	/**
	 * @Description : 宝付协议支付-直接支付(无需验证码)
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            确认绑卡对象
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-12 14:52:15;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static Map<String, Object> paymentDirectPay(RechargeCash rechargeCash, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: paymentDirectPay, 宝付协议支付-直接支付, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
		// 1、 宝付协议确认绑卡api组装支付报文
		String dgtl_envlp = "01|" + AESK;// 使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
		logger.info("密码dgtl_envlp：{}", dgtl_envlp);

		dgtl_envlp = RSAUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), payConfig.getPublicKey());// 公钥加密

		String protocolNo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(rechargeCash.getThirdAccount()), AESK);// 先BASE64后进行AES加密
		logger.info("签约协议号AES结果:{}", protocolNo);
		String cardInfo = "";// 信用卡：信用卡有效期|安全码,借记卡：传空
		BigDecimal centMoney = new BigDecimal("100");

		String transMney = rechargeCash.getTransMoney().multiply(centMoney).toString();
		if (transMney.contains(".00") || transMney.contains(".0")) {
			transMney = rechargeCash.getTransMoney().multiply(centMoney).stripTrailingZeros().toPlainString();
		}
		logger.info("交易金额：{}分", transMney);
		Map<String, String> dateArry = new TreeMap<String, String>();
		dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		dateArry.put("msg_id", rechargeCash.getPaymentFlowId());// 报文流水号
		dateArry.put("version", "4.0.0.0");
		dateArry.put("terminal_id", payConfig.getTerminalId());
		dateArry.put("txn_type", "08");// 交易类型(参看：文档中《交易类型枚举》)
		dateArry.put("member_id", payConfig.getMerchantNo());
		dateArry.put("trans_id", rechargeCash.getPaymentFlowId());
		dateArry.put("dgtl_envlp", dgtl_envlp);
		dateArry.put("user_id", rechargeCash.getUserId() + "_" + rechargeCash.getLoginTel());// 用户在商户平台唯一ID
		// (和绑卡时要一致)
		dateArry.put("protocol_no", protocolNo);// 签约协议号（密文）
		dateArry.put("txn_amt", transMney);// 交易金额
											// [单位：分
											// 例：1元则提交100]，此处注意数据类型的转转，建议使用BigDecimal类弄进行转换为字串
		dateArry.put("card_info", cardInfo);// 卡信息

		JSONObject riskItem = new JSONObject();
		/** --------风控基础参数------------- **/
		/**
		 * 说明风控参数必须，按商户开通行业、真实交易信息传，不可传固定值。
		 */
		riskItem.put("goodsCategory", "02");// 商品类目 详见附录《商品类目》
		riskItem.put("userLoginId", rechargeCash.getTel());// 用户在商户系统中的登陆名（手机号、邮箱等标识）
		riskItem.put("userEmail", "");
		riskItem.put("userMobile", "");// 用户手机号
		riskItem.put("registerUserName", rechargeCash.getUserName());// 用户在商户系统中注册使用的名字
		riskItem.put("identifyState", "1");// 用户在平台是否已实名，1：是 ；0：不是
		riskItem.put("userIdNo", rechargeCash.getIdCard());// 用户身份证号
		riskItem.put("registerTime", "");// 格式为：YYYYMMDDHHMMSS
		riskItem.put("registerIp", "");// 用户在商户端注册时留存的IP
		riskItem.put("chName", rechargeCash.getUserName());// 持卡人姓名
		riskItem.put("chIdNo", rechargeCash.getIdCard());// 持卡人身份证号
		riskItem.put("chCardNo", rechargeCash.getBankCard());// 持卡人银行卡号
		riskItem.put("chMobile", rechargeCash.getTel());// 持卡人手机
		riskItem.put("chPayIp", "127.0.0.1");// 持卡人支付IP
		riskItem.put("deviceOrderNo", "");// 加载设备指纹中的订单号

		/** --------行业参数 (以下为游戏行业风控参，请参看接口文档附录风控参数)------------- **/
		riskItem.put("tradeType", "1");
		riskItem.put("customerType", "1");
		riskItem.put("hasBalance", "");
		riskItem.put("hasBindCard", "");
		riskItem.put("repaymentDate", "");
		riskItem.put("lendingRate", "");
		riskItem.put("bidYields", "");
		riskItem.put("latestTradeDate", "");

		dateArry.put("risk_item", riskItem.toString());// 放入风控参数

		dateArry.put("return_url", payConfig.getAsyncNoticeUrl());// 最多填写三个地址,不同地址用间使用‘|’分隔

		String resStatus = "";// 结果码
		String resMsg = "";// 返回描述
		String flowId = "";
		// 4、把结果返回给上一层
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String SignVStr = FormatUtil.coverMap2String(dateArry);
			logger.info("SHA-1摘要字串：{}", SignVStr);
			String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名
			logger.info("SHA-1摘要结果：{}", signature);
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("RSA签名结果：{}", sign);
			dateArry.put("signature", sign);// 签名域
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("宝付协议支付-直接支付请求返回:{}", postString);
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("宝付协议支付-直接支付同步响应结果返回为空！");
				throw new Exception("宝付协议支付-直接支付同步响应结果返回为空！");
			}
			String rSign = returnData.get("signature");
			logger.info("返回的验签值：{}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (!RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.error("直接支付验签失败！");// 验签失败
				throw new Exception("直接支付，验签失败！");
			}
			logger.info("验签成功！");
			if (!returnData.containsKey("resp_code")) {
				throw new Exception("缺少resp_code参数！");
			}
			if (returnData.get("resp_code") == null) {
				resStatus = "9999";
				resMsg = "宝付协议支付-直接支付响应参数为空";
				resultMap.put("resStatus", resStatus);
				resultMap.put("resMsg", resMsg);
				return resultMap;
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resStatus = "1001";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resStatus = "1002";
				resMsg = returnData.get("biz_resp_msg").toString();
			}
			if (returnData.get("biz_resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS)) {
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
				resultMap.put("succ_amt", returnData.get("succ_amt"));
				resultMap.put("trans_id", returnData.get("trans_id"));
				flowId = returnData.get("trans_id");
			}
		} catch (Exception e) {
			logger.error("直接支付接口异常{}", e);
			resStatus = "999";
			resMsg = "支付接口异常";
		}
		resultMap.put("resStatus", resStatus);
		resultMap.put("resMsg", resMsg);
		resultMap.put("flowId", flowId);
		resultMap.put("paymentFlowId", rechargeCash.getPaymentFlowId());
		logger.info("直接支付 paymentDirectPay method , resultMap:{}", resultMap);
		return resultMap;
	}

	/**
	 * @Description : 宝付协议支付-预支付
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            预支付充值对象
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-12 16:08:34;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static Map<String, Object> paymentPrePay(RechargeCash rechargeCash, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: paymentPrePay, 宝付协议支付-预支付, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
		// 1、 宝付协议预支付api组装支付报文
		String dgtl_envlp = "01|" + AESK;// 使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
		logger.info("密码dgtl_envlp：{}", dgtl_envlp);

		dgtl_envlp = RSAUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), payConfig.getPublicKey());// 公钥加密

		String protocolNo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(rechargeCash.getThirdAccount()), AESK);// 先BASE64后进行AES加密
		logger.info("签约协议号AES结果:{}", protocolNo);

		BigDecimal centMoney = new BigDecimal("100");
		String transMney = rechargeCash.getTransMoney().multiply(centMoney).toString();
		if (transMney.contains(".00") || transMney.contains(".0")) {
			transMney = rechargeCash.getTransMoney().multiply(centMoney).stripTrailingZeros().toPlainString();
		}
		logger.info("交易金额：{}分", transMney);
		Map<String, String> dateArry = new TreeMap<String, String>();
		dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		dateArry.put("msg_id", CreateFlowUtil.createFlow("PR"));// 报文流水号
		dateArry.put("version", "4.0.0.0");
		dateArry.put("terminal_id", payConfig.getTerminalId());
		dateArry.put("txn_type", "05");// 交易类型(参看：文档中《交易类型枚举》)
		dateArry.put("member_id", payConfig.getMerchantNo());
		dateArry.put("trans_id", rechargeCash.getPaymentFlowId());
		dateArry.put("dgtl_envlp", dgtl_envlp);
		dateArry.put("user_id", rechargeCash.getUserId() + "_" + rechargeCash.getLoginTel());// 用户在商户平台唯一ID
		// (和绑卡时要一致)
		dateArry.put("protocol_no", protocolNo);// 签约协议号（密文）
		dateArry.put("txn_amt", transMney);// 交易金额
											// [单位：分
											// 例：1元则提交100]，此处注意数据类型的转转，建议使用BigDecimal类弄进行转换为字串

		JSONObject riskItem = new JSONObject();
		/** --------风控基础参数------------- **/
		/**
		 * 说明风控参数必须，按商户开通行业、真实交易信息传，不可传固定值。
		 */
		riskItem.put("goodsCategory", "02");// 商品类目 详见附录《商品类目》
		riskItem.put("userLoginId", rechargeCash.getTel());// 用户在商户系统中的登陆名（手机号、邮箱等标识）
		riskItem.put("userEmail", "");
		riskItem.put("userMobile", "");// 用户手机号
		riskItem.put("registerUserName", rechargeCash.getUserName());// 用户在商户系统中注册使用的名字
		riskItem.put("identifyState", "1");// 用户在平台是否已实名，1：是 ；0：不是
		riskItem.put("userIdNo", rechargeCash.getIdCard());// 用户身份证号
		riskItem.put("registerTime", "");// 格式为：YYYYMMDDHHMMSS
		riskItem.put("registerIp", "");// 用户在商户端注册时留存的IP
		riskItem.put("chName", rechargeCash.getUserName());// 持卡人姓名
		riskItem.put("chIdNo", rechargeCash.getIdCard());// 持卡人身份证号
		riskItem.put("chCardNo", rechargeCash.getBankCard());// 持卡人银行卡号
		riskItem.put("chMobile", rechargeCash.getTel());// 持卡人手机
		riskItem.put("chPayIp", "127.0.0.1");// 持卡人支付IP
		riskItem.put("deviceOrderNo", "");// 加载设备指纹中的订单号

		/** --------行业参数 (以下为游戏行业风控参，请参看接口文档附录风控参数)------------- **/
		riskItem.put("tradeType", "1");
		riskItem.put("customerType", "1");
		riskItem.put("hasBalance", "");
		riskItem.put("hasBindCard", "");
		riskItem.put("repaymentDate", "");
		riskItem.put("lendingRate", "");
		riskItem.put("bidYields", "");
		riskItem.put("latestTradeDate", "");

		dateArry.put("risk_item", riskItem.toString());// 放入风控参数

		dateArry.put("return_url", payConfig.getAsyncNoticeUrl());// 最多填写三个地址,不同地址用间使用‘|’分隔

		String resStatus = "";// 结果码
		String resMsg = "";// 返回描述
		// 4、把结果返回给上一层
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String SignVStr = FormatUtil.coverMap2String(dateArry);
			logger.info("SHA-1摘要字串：{}", SignVStr);
			String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名
			logger.info("SHA-1摘要结果：{}", signature);
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("RSA签名结果：{}", sign);
			dateArry.put("signature", sign);// 签名域
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("预支付请求返回:{}", postString);
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("宝付预支付同步响应结果返回为空！");
				throw new Exception("宝付预支付同步响应结果返回为空！");
			}
			String rSign = returnData.get("signature");
			logger.info("返回的验签值：{}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.info("验签成功！");// 验签成功
			}
			if (!returnData.containsKey("resp_code")) {
				throw new Exception("缺少resp_code参数！");
			}
			if (returnData.get("resp_code") == null) {
				resStatus = "9999";
				resMsg = "宝付预支付响应参数为空";
				resultMap.put("resStatus", resStatus);
				resultMap.put("resMsg", resMsg);
				return resultMap;
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				if (!returnData.containsKey("dgtl_envlp")) {
					throw new Exception("缺少dgtl_envlp参数！");
				}
				String RDgtlEnvlp = SecurityUtil.Base64Decode(RSAUtil.decryptByPriPfxFile(returnData.get("dgtl_envlp"),
						payConfig.getPrivateKey(), payConfig.getPayMd5Key()));
				logger.info("返回数字信封：{}", RDgtlEnvlp);
				String rAesKey = FormatUtil.getAesKey(RDgtlEnvlp);// 获取返回的AESkey
				logger.info("返回的AESkey:{}", rAesKey);
				if (AESK.equals(rAesKey)) {
					throw new Exception("返回的AESkey值不相等，校验失败！");
				}
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
				String unique_code = SecurityUtil
						.Base64Decode(SecurityUtil.AesDecrypt(returnData.get("unique_code").toString(), rAesKey));
				resultMap.put("unique_code", unique_code);
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resStatus = "1001";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resStatus = "1002";
				resMsg = returnData.get("biz_resp_msg").toString();
			}
		} catch (Exception e) {
			logger.error("预支付接口异常{}", e);
			resStatus = "999";
			resMsg = "预支付接口异常";
		}
		resultMap.put("resStatus", resStatus);
		resultMap.put("resMsg", resMsg);
		resultMap.put("paymentFlowId", rechargeCash.getPaymentFlowId());
		logger.info("预支付 paymentPrePay method , resultMap:{}", resultMap);
		return resultMap;
	}

	/**
	 * @Description : 宝付协议支付-确认支付
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            预支付充值对象
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-12 16:08:34;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static Map<String, Object> paymentConfirmPay(RechargeCash rechargeCash, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: paymentConfirmPay, 宝付协议支付-确认支付, 用户标识: {}, 入参: rechargeCash: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), payConfig.toString());
		// 1、 宝付协议预支付api组装支付报文
		String dgtl_envlp = "01|" + AESK;// 使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
		logger.info("密码dgtl_envlp：{}", dgtl_envlp);
		
		dgtl_envlp = RSAUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), payConfig.getPublicKey());// 公钥加密

		String uniqueCode = rechargeCash.getPayUnionCode() + MARK + rechargeCash.getVerificationCode();// 预支付唯一码(预支付返回的值)[格式：预支付一码|短信验证码]
		logger.info("预支付唯一码：{}", uniqueCode);
		uniqueCode = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(uniqueCode), AESK);// 先BASE64后进行AES加密
		logger.info("AES[uniqueCode]结果:" + uniqueCode);

		Map<String, String> dateArry = new TreeMap<String, String>();
		dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		dateArry.put("msg_id", CreateFlowUtil.createFlow("PY"));// 报文流水号
		dateArry.put("version", "4.0.0.0");
		dateArry.put("terminal_id", payConfig.getTerminalId());
		dateArry.put("txn_type", "06");// 交易类型(参看：文档中《交易类型枚举》)
		dateArry.put("member_id", payConfig.getMerchantNo());
		dateArry.put("dgtl_envlp", dgtl_envlp);
		dateArry.put("unique_code", uniqueCode);// 预支付唯一码
		dateArry.put("card_info", "");// 卡信息

		String resStatus = "";// 结果码
		String resMsg = "";// 返回描述
		String flowId = "";
		// 4、把结果返回给上一层
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String SignVStr = FormatUtil.coverMap2String(dateArry);
			logger.info("SHA-1摘要字串：{}", SignVStr);
			String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名
			logger.info("SHA-1摘要结果：{}", signature);
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("RSA签名结果：{}", sign);
			dateArry.put("signature", sign);// 签名域
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("宝付确认支付请求返回:{}", postString);
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("宝付确认支付付同步响应结果返回为空！");
				throw new Exception("宝付确认支付同步响应结果返回为空！");
			}
			String rSign = returnData.get("signature");
			logger.info("返回的验签值：{}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (!RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.error("验签失败！");// 验签成功
				throw new Exception("确认支付，验签失败！");
			}
			logger.info("验签成功！");// 验签成功
			if (!returnData.containsKey("resp_code")) {
				throw new Exception("缺少resp_code参数！");
			}
			if (returnData.get("resp_code") == null) {
				resStatus = "9999";
				resMsg = "宝付确认支付响应参数为空";
				resultMap.put("resStatus", resStatus);
				resultMap.put("resMsg", resMsg);
				return resultMap;
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
				flowId = returnData.get("trans_id").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resStatus = "1001";
				resMsg = returnData.get("biz_resp_msg").toString();
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resStatus = "1002";
				resMsg = returnData.get("biz_resp_msg").toString();
			}
			if (returnData.get("biz_resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS)) {
				resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resStatus = String.valueOf(Constants.SUCCESS);
				}
				resMsg = returnData.get("biz_resp_msg").toString();
			}
		} catch (Exception e) {
			logger.error("确认支付接口异常{}", e);
			resStatus = "999";
			resMsg = "确认支付接口异常";
		}
		resultMap.put("resStatus", resStatus);
		resultMap.put("resMsg", resMsg);
		resultMap.put("flowId", flowId);
		logger.info("确认支付 paymentConfirmPay method , resultMap:{}", resultMap);
		return resultMap;
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
		logger.info("方法: findCardBin, 宝付协议查询卡BIN, 入参: cardNo: {}, payConfig: {}", cardNo, payConfig.toString());
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
				return new ResponseEntity<>(Constants.ERROR, "查询卡信息失败,请联系客服!");
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
	 * @Description :宝付协议支付，支付查证
	 * @Method_Name : payCheck;
	 * @param flowId
	 * @param tradeType
	 * @param rechargeSource
	 * @param payConfig
	 * @param transDate
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月18日 下午5:34:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> payCheck(String flowId, Integer tradeType, Integer rechargeSource,
			FinPayConfig payConfig, Date transDate) throws Exception {
		logger.info(
				"方法: payCheck, 宝付协议支付-查询支付订单结果, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, payConfig: {}, transDate: {}",
				flowId, tradeType, rechargeSource, payConfig.toString(), transDate);
		// 判断交易类型是充值，还是提现操作
		if (PayStyleEnum.RECHARGE.getValue() == tradeType) {
			if (rechargeSource == PayStyleEnum.RZ.getValue()) {
				return rechargeRzPayCheck(flowId, payConfig, transDate);
			} else {
				logger.info("宝付协议支付查证, 查证流水标识: {}, 暂无此支付查证方式: {}", flowId, rechargeSource);
			}
		} else if (PayStyleEnum.WITHDRAW.getValue() == tradeType) {
			return withDrawPayCheck(flowId, payConfig);
		}
		return null;
	}

	/**
	 * @Description : 宝付协议支付-查询支付订单结果
	 * @Method_Name : payCheck;
	 * @param flowId
	 *            订单号
	 * @param tradeType
	 *            交易类型
	 * @param rechargeSource
	 *            充值来源
	 * @param transDate
	 *            交易时间
	 * @param payConfig
	 *            渠道对象
	 * @param transDate
	 *            订单生成时间
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-17 18:14:51;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static ResponseEntity<?> rechargeRzPayCheck(String flowId, FinPayConfig payConfig, Date transDate)
			throws Exception {
		logger.info("方法: rechargeRzPayCheck, 宝付协议支付认证充值查证, 入参: flowId: {}, payConfig: {}, transDate: {}", flowId,
				payConfig, transDate);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 1、 宝付协议查询支付订单结果组装请求报文
			Map<String, String> dateArry = new TreeMap<String, String>();
			dateArry.put("send_time", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
			dateArry.put("msg_id", CreateFlowUtil.createFlow("QO"));// 报文流水号
			dateArry.put("version", payConfig.getPayVersion());
			dateArry.put("terminal_id", payConfig.getTerminalId());
			dateArry.put("txn_type", "07");// 交易类型
			dateArry.put("member_id", payConfig.getMerchantNo());
			dateArry.put("orig_trans_id", flowId);// 交易时的trans_id
			dateArry.put("orig_trade_date", DateUtils.format(transDate, DateUtils.DATE_HH_MM_SS));
			// 2、加密请求明文报文
			String SignVStr = FormatUtil.coverMap2String(dateArry);
			logger.info("宝付协议支付认证充值查证, SHA-1摘要字串: {}", SignVStr);
			String signature = SecurityUtil.sha1X16(SignVStr, CHARSET);// 签名
			logger.info("宝付协议支付认证充值查证, SHA-1摘要结果: {}", signature);
			String sign = RSAUtil.encryptByRSA(signature, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("宝付协议支付认证充值查证, RSA签名结果: {}", sign);
			dateArry.put("signature", sign);// 签名域
			logger.info("宝付协议支付认证充值查证, 查询支付订单结果请求报文: {}", dateArry);
			String postString = HttpClientUtils.httpsPost(payConfig.getPayUrl(), dateArry);
			logger.info("宝付协议支付认证充值查证, 查询支付订单结果请求返回: {}", postString);
			// 3、解析返回报文，处理订单结果
			Map<String, String> returnData = FormatUtil.getParm(postString);
			if (returnData == null || !returnData.containsKey("signature")) {
				logger.error("宝付协议支付认证充值查证, 查询支付订单结果返回为空！");
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,返回信息为空!");
			}
			String rSign = returnData.get("signature");
			logger.info("宝付协议支付认证充值查证, 返回的验签值: {}", rSign);
			returnData.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(returnData);
			logger.info("宝付协议支付认证充值查证, 返回SHA-1摘要字串: {}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("宝付协议支付认证充值查证, 返回SHA-1摘要结果: {}", rSignature);
			if (RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.info("宝付协议支付认证充值查证, 验签成功！");// 验签成功
			}
			if (!returnData.containsKey("resp_code")) {
				logger.error("宝付协议支付认证充值查证, 缺少resp_code参数！");
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,缺少resp_code参数!");
			}
			if (StringUtils.isBlank(returnData.get("resp_code"))) {
				logger.error("宝付协议支付认证充值查证, 查询支付订单结果响应参数为空！");
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,查询支付订单结果响应参数为空!");
			}
			if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_SUCCESS)) {
				String resStatus = returnData.get("biz_resp_code").toString();
				if (PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS.equals(resStatus)) {
					resultMap.put("transState", PAY_STATE_SUCCESS);// 支付成功
					resultMap.put("orderId", returnData.get("trans_id").toString());// 返回订单号
					BigDecimal succAmt = new BigDecimal(returnData.get("succ_amt").toString());
					String transMoney = succAmt.divide(new BigDecimal("100")).toString();// 分转元
					logger.info("分转元，分: {}, 转化为元: {}", succAmt, transMoney);
					resultMap.put("transMoney", transMoney);// 交易金额
				}
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_FAILURE)) {
				resultMap.put("transState", PAY_STATE_FAIL);// 支付失败
				resultMap.put("orderId", returnData.get("trans_id").toString());// 返回订单号
				logger.info("宝付协议支付查证, 支付查证流水标识: {}, 支付查证交易失败: {}", flowId, returnData.get("biz_resp_msg").toString());
			} else if (returnData.get("resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_PROCESSING)) {
				resultMap.put("transState", PAY_STATE_PROCESSING);// 银行处理中
				resultMap.put("orderId", returnData.get("trans_id").toString());// 返回订单号
				logger.info("宝付协议支付查证, 支付查证流水标识: {}, 支付查证交易失败: {}", flowId, returnData.get("biz_resp_msg").toString());
			}
			// if
			// (!returnData.get("biz_resp_code").toString().equals(PaymentConstants.BAOFU_PROTOCAL_CODE_SUCCESS))
			// {
			// return new ResponseEntity<>(Constants.SUCCESS,
			// returnData.get("biz_resp_msg").toString());
			// }
		} catch (Exception e) {
			logger.error("查询支付订单接口异常: {}", e);
			return new ResponseEntity<>(Constants.ERROR, "查证异常");
		}
		logger.info("查询支付订单 rechargeRzPayCheck resultMap: {}", resultMap);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME)
				.addParam("resPayCheck", resultMap);
		return responseEntity;
	}

	/**
	 * @Description :宝付支付协议提现查证接口
	 * @Method_Name : withDrawPayCheck;
	 * @param flowId
	 * @param payConfig
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月18日 下午5:20:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
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
			logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现渠道: {}, 提现查证请求加密前dataContent报文: {}", flowId,
					payConfig.getThirdName(), bean2XmlStr);
			String dataContent = Base64Util.encode(bean2XmlStr);
			dataContent = RSAUtil.encryptByPriPfxFile(dataContent, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现渠道: {}, 提现查证请求加密后dataContent报文: {}", flowId,
					payConfig.getThirdName(), dataContent);
			Map<String, String> reqDataMap = new HashMap<String, String>();
			reqDataMap.put("member_id", payConfig.getMerchantNo());
			reqDataMap.put("terminal_id", payConfig.getTerminalId());
			reqDataMap.put("data_type", DATA_TYPE);
			reqDataMap.put("data_content", dataContent);
			reqDataMap.put("version", payConfig.getPayVersion());
			logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现渠道: {}, 提现查证请求报文: {}", flowId, payConfig.getThirdName(),
					reqDataMap.toString());
			// 调用宝付支付查证接口
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqDataMap);
			logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现渠道: {}, 提现查证响应报文: {}", flowId, payConfig.getThirdName(), resJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "支付查证失败,返回信息为空!");
			}
			// 解析报文，返回给客户端
			resJson = RSAUtil.decryptByPubCerFile(resJson, payConfig.getPublicKey());
			resJson = Base64Util.decode(resJson);
			logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现渠道: {}, 提现查证响应报文解析后的明文: {}", flowId, payConfig.getThirdName(),
					resJson);
			TransContent<TransResResult> resData = new TransContent<TransResResult>(TransContent.DATA_TYPE_JSON);
			resData = (TransContent<TransResResult>) resData.str2Obj(resJson, TransResResult.class);
			TransHead headData = resData.getTrans_head();
			String ret_code = headData.getReturn_code();
			Map<String, Object> result = new HashMap<String, Object>();
			if (BAOFU_RZ_PAY_CHECK_STATE_SUCCESS.equals(ret_code)) {
				TransResResult transData = resData.getTrans_reqDatas().get(0);
				// 如果交易结果为0000,则认为此笔交易请求成功,
				// 如果交易结果为已退款或交易失败，则认为此笔订单为失败，否则认为是交易等待处理中
				if (BAOFU_WITHDRAW_CHECK_STATE_SUCCESS.equals(transData.getState())) {
					result.put("transState", PAY_STATE_SUCCESS);// 支付成功
					result.put("orderId", transData.getTrans_no());// 返回订单号
					result.put("transMoney", transData.getTrans_money());// 交易金额
				} else if (BAOFU_WITHDRAW_CHECK_STATE_FAILURE.equals(transData.getState())
						|| BAOFU_WITHDRAW_CHECK_STATE_CANCEL.equals(transData.getState())) {
					result.put("transState", PAY_STATE_FAIL);// 支付失败
					logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现查证交易失败: {}", flowId, transData.getTrans_remark());
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 银行处理中
					logger.info("宝付协议提现支付查证, 支付查证流水标识: {}, 提现查证交易失败: {}", flowId, transData.getTrans_remark());
				}
			} else if (PaymentConstants.BAOFUP_ROTOCAL_TX_CHECK_STATE_NO_DATA.equals(ret_code)) {
				result.put("transState", PaymentConstants.PAY_STATE_NO_ORDER);// 支付失败
				result.put("orderId", flowId);// 返回订单号
			} else {
				return new ResponseEntity<>(Constants.ERROR, "提现支付查证异常:" + ret_code + "|" + headData.getReturn_msg());
			}
			responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
			resultPay.put("resPayCheck", result);
			responseEntity.setParams(resultPay);
		} catch (Exception e) {
			logger.error("宝付协议提现支付查证, 支付查证流水标识: {}, 入参: flowId: {}, payConfig: {}, 支付查证异常信息: ", flowId, flowId,
					payConfig.toString(), e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
		logger.info("宝付协议提现支付查证 withDrawPayCheck resultPay: {}", resultPay);
		return responseEntity;
	}

	/**
	 * 协议充值订单回调参数验签
	 * 
	 * @param dataArray
	 * @param payConfig
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-18 21:26:56;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static ResponseEntity<?> checkCallBackSing(Map<String, String> dataArray, FinPayConfig payConfig) {
		logger.info("BaofuProtocolPayFactory checkCallBackSing , dataArray={}", dataArray);
		try {
			if (dataArray == null || !dataArray.containsKey("signature")) {
				logger.error("协议充值异步回调 验签参数为空！");
				return new ResponseEntity<>(Constants.ERROR, "协议充值异步回调 验签参数为空！");
			}
			String rSign = dataArray.get("signature");
			logger.info("返回的验签值：{}", rSign);
			dataArray.remove("signature");// 需要删除签名字段
			String rSignVStr = FormatUtil.coverMap2String(dataArray);
			logger.info("返回SHA-1摘要字串：{}", rSignVStr);
			String rSignature = SecurityUtil.sha1X16(rSignVStr, CHARSET);// 签名
			logger.info("返回SHA-1摘要结果：{}", rSignature);
			if (!RSAUtil.verifySignature(payConfig.getPublicKey(), rSignature, rSign)) {
				logger.error("验签失败！");// 验签失败
				return new ResponseEntity<>(Constants.ERROR, "验签失败");
			}
			logger.error("验签成功！");// 验签成功
		} catch (Exception e) {
			logger.error("协议充值异步回调，验签失败，异常信息：{}！", e);
			return new ResponseEntity<>(Constants.ERROR, "验签失败");
		}
		return new ResponseEntity<>(Constants.SUCCESS, "验签成功");
	}

	public static ResponseEntity<?> callRealNameReq(Integer regUserId, String idCard, String userName,
			FinPayConfig payConfig) {
		logger.info("方法: callRealNameReq, 宝付协议实名认证业务, 用户标识: {}, 入参: idCard: {}, userName：{},payConfig: {}", regUserId,
				idCard, userName, payConfig.toString());
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.ERROR);
		Map<String, String> map = new HashMap<String, String>();
		map.put("member_id", payConfig.getMerchantNo());
		map.put("terminal_id", payConfig.getTerminalId());
		// map.put("data_type", "json");
		map.put("trans_id", CreateFlowUtil.createFlow("RN"));
		map.put("trade_date", DateUtils.getCurrentDate(DateUtils.DATE_HHMMSS));
		map.put("id_card", idCard);
		map.put("id_holder", userName);
		map.put("industry_type", "B2");
		map.put("is_photo", "noPhoto");
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回结果
		try {

			String base64str = Base64Util.encode(JSON.toJSONString(map));
			String data_content = RSAUtil.encryptByPriPfxFile(base64str, payConfig.getPrivateKey(),
					payConfig.getPayMd5Key());

			Map<String, String> headers = new HashMap<>();
			String url = payConfig.getPayUrl();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", payConfig.getMerchantNo());
			params.put("terminal_id", payConfig.getTerminalId());
			params.put("data_type", "json");
			params.put("data_content", data_content);
			
			String postString = HttpUtils.doPost(url, headers, params);
			logger.info("实名认证查询返回postString：{}", postString);
			if (StringUtils.isBlank(postString)) {
				logger.error("方法: callRealNameReq, 宝付协议实名认证业务返回异常为空，用户标识: {}, 入参: idCard: {}, userName：{}", regUserId,
						idCard, userName);
				return responseEntity;
			}
			JSONObject jsonObj = JSONObject.parseObject(postString);
			String resutStr = jsonObj.get("success").toString();
			logger.info("resutStr:{}", resutStr);
			if (resutStr.equals("false")) {
				logger.error("方法: callRealNameReq, 宝付协议实名认证业务返回异常为空，用户标识: {}, 入参: idCard: {}, userName：{}", regUserId,
						idCard, userName);
				resultMap.put("state", "9");
				resultMap.put("desc", jsonObj.get("errorMsg").toString());
				responseEntity.setParams(resultMap);
				return responseEntity;
			}
			JSONObject js = JSONObject.parseObject(jsonObj.get("data").toString());
			resultMap.put("state", js.get("code"));
			resultMap.put("desc", js.get("desc"));
			responseEntity = new ResponseEntity<>(Constants.SUCCESS);
			responseEntity.setParams(resultMap);
			logger.info("responseEntity:{}", responseEntity);
			return responseEntity;
		} catch (Exception e) {
			logger.error("宝付协议实名认证业务 异常，用户标识: {}, 异常：{} ", regUserId, e.getMessage());
			return responseEntity;
		}
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
	
}
