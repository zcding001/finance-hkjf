/**
 * @Title: package-info.java
 * @Package com.hongkun.finance.payment.factory
 * @Description: Copyright (c) 2011 Company:北京亿润财富
 * @author Comsys-cxb
 * @date 2017年6月1日 下午3:20:38
 * @version V1.0
 */
package com.hongkun.finance.payment.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.client.YouDun.YouDunEncrty;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.enums.LianLianPayCardStyleEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.enums.SignTypeEnum;
import com.hongkun.finance.payment.llpayvo.AgentPayInfo;
import com.hongkun.finance.payment.llpayvo.OrderInfo;
import com.hongkun.finance.payment.llpayvo.PaymentInfo;
import com.hongkun.finance.payment.llpayvo.PaymentWapInfo;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.hongkun.finance.payment.security.Md5Algorithm;
import com.hongkun.finance.payment.util.CreateFlowUtil;
import com.hongkun.finance.payment.util.ObjChangeUtil;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.payment.util.RSAUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;

public class LianlianPayFactory {

	private static final Logger logger = LoggerFactory.getLogger(LianlianPayFactory.class);

	/**
	 * @Description : caoxb 方法描述：连连风险信息和订单信息
	 * @Method_Name : lianlianRiskInfo
	 * @param rechargeCash
	 *            充值组装对象
	 * @param finAccount
	 *            用户账户信息
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年6月7日 下午6:04:49
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static Map<String, Object> riskInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date registerDate = finAccount.getCreateTime();
		String registerTime = DateUtils.format(registerDate, DateUtils.DATE_HHMMSS);

		// 连连风险信息
		JSONObject riskItemObj = new JSONObject();
		riskItemObj.put("frms_ware_category", "2009");// 商品类目 2009 P2P贷款
		if (rechargeCash.getPlatformSourceName().equalsIgnoreCase(PlatformSourceEnums.WAP.getType())) {
			riskItemObj.put("user_info_mercht_userno", payConfig.getMerchantNo());// 商品类目
		} else {
			riskItemObj.put("user_info_mercht_userno", finAccount.getRegUserId());// 用户唯一标示
		}
		riskItemObj.put("user_info_dt_register", registerTime);// 注册时间
		riskItemObj.put("user_info_full_name", finAccount.getUserName().trim());// 注册姓名
		riskItemObj.put("user_info_id_no", rechargeCash.getIdCard());// 身份证号
		riskItemObj.put("user_info_identify_type", "1");// 是否实名认证
		riskItemObj.put("user_info_identify_state", "1");// 银行卡实名认证
		logger.info("连连充值风险信息组装, 用户标识: {} , 组装信息: {}", finAccount.getRegUserId(), riskItemObj.toString());
		map.put("riskInfo", riskItemObj);
		// 商户创建订单
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setNo_order(rechargeCash.getPaymentFlowId());
		orderInfo.setDt_order(PaymentUtil.getCurrentDateTimeStr());
		orderInfo.setMoney_order(String.valueOf(rechargeCash.getTransMoney()));
		orderInfo.setName_goods(rechargeCash.getSystemTypeName() + "充值");
		orderInfo.setInfo_order(finAccount.getRegUserId() + "用户购买");
		logger.info("连连充值订单信息组装, 用户标识: {} , 组装信息: {}", finAccount.getRegUserId(), orderInfo.toString());
		map.put("orderInfo", orderInfo);
		return map;
	}

	/**
	 * @Description : caoxb 方法描述 连连支付对象
	 * @Method_Name : paymentPcInfo
	 * @param rechargeCash
	 *            充值组装对象
	 * @param finAccount
	 *            用户账户信息
	 * @param orderInfo
	 *            订单信息
	 * @param riskItemObj
	 *            风险对象信息
	 * @return
	 * @return : PaymentInfo
	 * @Creation Date : 2017年6月7日 下午6:32:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static Map<String, Object> paymentPcInfo(RechargeCash rechargeCash, FinAccount finAccount,
			OrderInfo orderInfo, JSONObject riskItemObj, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: paymentPcInfo, 连连充值组装对象, 用户标识:{}, 入参: rechargeCash: {}, finAccount: {}, orderInfo: {}, riskItemObj: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), orderInfo.toString(),
				riskItemObj.toString(), payConfig.toString());
		// 构造支付请求对象
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setVersion(payConfig.getPayVersion());
		paymentInfo.setOid_partner(payConfig.getMerchantNo());
		paymentInfo.setPlatform(payConfig.getPlatformName());
		paymentInfo.setUser_id(String.valueOf(finAccount.getRegUserId()));
		paymentInfo.setSign_type(payConfig.getSignStyle());
		paymentInfo.setBusi_partner(payConfig.getBusinessType());
		paymentInfo.setNo_order(orderInfo.getNo_order());
		paymentInfo.setDt_order(orderInfo.getDt_order());
		paymentInfo.setName_goods(orderInfo.getName_goods());
		paymentInfo.setInfo_order(orderInfo.getInfo_order());
		paymentInfo.setMoney_order(orderInfo.getMoney_order());
		paymentInfo.setNotify_url(payConfig.getAsyncNoticeUrl());
		paymentInfo.setUrl_return(payConfig.getSyncNoticeUrl());
		paymentInfo.setValid_order("10080");// 单位分钟，可以为空，默认7天
		paymentInfo.setRisk_item(riskItemObj.toString());
		paymentInfo.setTimestamp(PaymentUtil.getCurrentDateTimeStr());
		// 商戶从自己系统中获取用户身份信息（认证支付必须将用户身份信息传输给连连，且修改标记flag_modify设置成1：不可修改）
		paymentInfo.setId_type("0");
		paymentInfo.setId_no(rechargeCash.getIdCard().trim());
		paymentInfo.setAcct_name(rechargeCash.getUserName().trim());
		paymentInfo.setFlag_modify("1");
		paymentInfo.setBack_url(payConfig.getBackUrl());
		if (PayStyleEnum.WY.getType().equals(rechargeCash.getPayStyle())) {
			paymentInfo.setBank_code(rechargeCash.getBankCode());
		}
		// 协议号和卡号同时存在时，优先将协议号送给连连，不要将协议号和卡号都送给连连
		if (PayStyleEnum.RZ.getType().equalsIgnoreCase(rechargeCash.getPayStyle())) {
			paymentInfo.setPay_type(LianLianPayCardStyleEnum.RZDC.getValue());
			if (StringUtils.isNotBlank(rechargeCash.getThirdAccount())) {
				paymentInfo.setNo_agree(rechargeCash.getThirdAccount());
			} else {
				paymentInfo.setCard_no(rechargeCash.getBankCard());
			}
		}
		paymentInfo.setPay_type(getPayCardStyle(rechargeCash));
		// 加签名
		String sign = PaymentUtil.addSign(JSON.parseObject(JSON.toJSONString(paymentInfo)), payConfig.getPrivateKey(),
				payConfig.getPayMd5Key());
		paymentInfo.setSign(sign);
		return ObjChangeUtil.objToMap(paymentInfo);
	}

	/**
	 * @Description :连连渠道WAP充值
	 * @Method_Name : paymentWapInfo;
	 * @param rechargeCash
	 *            paymentPcInfo
	 * @param finAccount
	 *            用户账户对象
	 * @param orderInfo
	 *            订单信息
	 * @param riskItemObj
	 *            风险对象信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年6月9日 下午5:33:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> paymentWapInfo(RechargeCash rechargeCash, FinAccount finAccount,
			OrderInfo orderInfo, JSONObject riskItemObj, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: paymentWapInfo, 连连充值组装对象, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, orderInfo: {}, riskItemObj: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), orderInfo.toString(),
				riskItemObj.toString(), payConfig.toString());
		Map<String, Object> resultData = new HashMap<String, Object>();
		PaymentWapInfo payInfo = new PaymentWapInfo();
		payInfo.setApp_request("3");// 1-Android 2-ios 3-WAP
		payInfo.setBg_color("d93f3f");
		payInfo.setBusi_partner(payConfig.getBusinessType());
		payInfo.setCard_no(StringUtils.isBlank(rechargeCash.getBankCard()) ? null : rechargeCash.getBankCard());
		payInfo.setDt_order(orderInfo.getDt_order());
		payInfo.setId_no(StringUtils.isBlank(rechargeCash.getIdCard()) ? null : rechargeCash.getIdCard());
		payInfo.setInfo_order(orderInfo.getInfo_order());
		payInfo.setMoney_order(orderInfo.getMoney_order());
		payInfo.setName_goods(orderInfo.getName_goods());
		payInfo.setNo_agree(StringUtils.isBlank(rechargeCash.getNoAgree()) ? null : rechargeCash.getNoAgree());
		payInfo.setNo_order(orderInfo.getNo_order());
		payInfo.setNotify_url(payConfig.getAsyncNoticeUrl());
		payInfo.setOid_partner(payConfig.getMerchantNo());
		payInfo.setAcct_name(StringUtils.isBlank(finAccount.getUserName()) ? null : finAccount.getUserName());
		payInfo.setRisk_item(riskItemObj.toString());
		payInfo.setSign_type(payConfig.getSignStyle());
		payInfo.setUrl_return(payConfig.getSyncNoticeUrl());
		payInfo.setUser_id(StringUtils.isBlank(String.valueOf(rechargeCash.getUserId())) ? null
				: String.valueOf(rechargeCash.getUserId()));
		payInfo.setValid_order("10080");
		payInfo.setPay_type(getPayCardStyle(rechargeCash));
		String payStr = ObjChangeUtil.objToRequestStr(payInfo, "&");
		String sign = "";// 签名
		if (SignTypeEnum.RSA.getCode().equals(payConfig.getSignStyle())) {
			sign = RSAUtil.sign(payConfig.getPrivateKey(), payStr);
		} else {
			payStr += "&key=" + payConfig.getPayMd5Key();
			sign = Md5Algorithm.getInstance().md5Digest(payStr.getBytes("utf-8"));
		}
		payInfo.setSign(sign);
		String req_data = JSON.toJSONString(payInfo);
		logger.info("连连充值组装对象, 用户标识: {}, 充值流水标识:{}, 充值渠道: {}, 充值MD5加密报文: {}", rechargeCash.getUserId(),
				rechargeCash.getPaymentFlowId(), rechargeCash.getPlatformSourceName(), req_data);
		resultData.put("req_data", req_data);
		return resultData;
	}

	/**
	 * @Description :封装连连渠道APP端充值请求对象
	 * @Method_Name : paymentAppInfo;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            用户账户信息
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年6月9日 下午4:46:48;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, Object> paymentAppInfo(RechargeCash rechargeCash, FinAccount finAccount,
			FinPayConfig payConfig) throws Exception {
		logger.info("方法: paymentAppInfo, 连连充值组装对象, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("bankCode", rechargeCash.getBankCode());// 银行编码
		resultData.put("bankCard", rechargeCash.getBankCard());// 银行卡号
		resultData.put("no_order", rechargeCash.getPaymentFlowId());// 订单号
		resultData.put("name_goods", PayStyleEnum.RECHARGE.getType());// 订单名
		resultData.put("userMobile", rechargeCash.getTel());// 用户手机号
		resultData.put("user_registe_time", finAccount.getCreateTime());// 用户注册时间
		resultData.put("user_info_name", finAccount.getUserName());// 用户姓名
		resultData.put("user_info_id", rechargeCash.getIdCard());// 用户身份证号
		resultData.put("merchantNo", payConfig.getMerchantNo());// 商户编号
		resultData.put("notifyUrl", payConfig.getAsyncNoticeUrl());// 异步通知地址
		return resultData;
	}

	/**
	 * @Description : 封装调用连连提现对象
	 * @Method_Name : createLiLiPayBean;
	 * @param finBankCard
	 *            银行卡信息
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            提现记录对象
	 * @param payConfig
	 *            支付配置信息
	 * @param finCityRefer
	 *            城市信息
	 * @return
	 * @throws Exception
	 * @return : AgentPayInfo;
	 * @Creation Date : 2018年1月18日 下午4:08:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static AgentPayInfo createLiLiPayBean(FinBankCard finBankCard, WithDrawCash withDrawCashInfo,
			FinPaymentRecord finPaymentRecord, FinPayConfig payConfig, FinCityRefer finCityRefer) throws Exception {
		AgentPayInfo agentPayInfo = new AgentPayInfo();
		agentPayInfo.setPlatform(payConfig.getMerchantNo());
		agentPayInfo.setOid_partner(payConfig.getMerchantNo());
		agentPayInfo.setSign_type(payConfig.getSignStyle());
		agentPayInfo.setNo_order(finPaymentRecord.getFlowId());
		agentPayInfo.setDt_order(PaymentUtil.getCurrentDateTimeStr());
		agentPayInfo.setMoney_order(String.valueOf(finPaymentRecord.getTransMoney()));
		if (withDrawCashInfo != null
				&& String.valueOf(PaymentConstants.BUSINESS_USER).equals(withDrawCashInfo.getUserType())) {
			agentPayInfo.setFlag_card("1"); // 对公对私标志 0-对私 1–对公
		} else {
			agentPayInfo.setFlag_card("0");
		}
		agentPayInfo.setAcct_name(withDrawCashInfo.getUserName());
		agentPayInfo.setCard_no(finBankCard.getBankCard());
		agentPayInfo.setBank_code(finBankCard.getBankCode());
		agentPayInfo.setProvince_code(finCityRefer.getProvinceThirdCode());
		agentPayInfo.setCity_code(finCityRefer.getCityThirdCode());
		agentPayInfo.setBrabank_name(finBankCard.getBranchName());
		agentPayInfo.setInfo_order(withDrawCashInfo.getOrderDesc());
		agentPayInfo.setNotify_url(payConfig.getAsyncNoticeUrl());
		agentPayInfo.setApi_version(payConfig.getPayVersion());
		return agentPayInfo;
	}

	/***
	 * @Description : 根据用户及支付方式获取支付卡的方式
	 * @Method_Name : getPayCardStyle;
	 * @param rechargeCash
	 *            充值对象
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月21日 下午3:41:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static String getPayCardStyle(RechargeCash rechargeCash) {
		// 认证支付
		if (PayStyleEnum.valueByType(rechargeCash.getPayStyle()) == PayStyleEnum.RZ.getValue()) {
			return LianLianPayCardStyleEnum.RZDC.getValue();
		}
		if (PayStyleEnum.valueByType(rechargeCash.getPayStyle()) == PayStyleEnum.WY.getValue()) {
			if (rechargeCash.getuType() == PaymentConstants.PERSONAL_USER) {
				// 个人用户网银充值
				return LianLianPayCardStyleEnum.WYDC.getValue();
			} else {
				// 企业用户网银充值
				return LianLianPayCardStyleEnum.QYWY.getValue();
			}
		}
		return null;
	}

	/**
	 * @Description : 连连渠道支付查证接口
	 * @Method_Name : payCheck;
	 * @param flowId
	 *            交易流水
	 * @param tradeType
	 *            交易类型
	 * @param rechargeSource
	 *            充值来源
	 * @param payConfig
	 *            支付配置文件
	 * @param createTime
	 *            订单生成时间
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年8月10日 下午6:14:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> payCheck(String flowId, Integer tradeType, Integer rechargeSource,
			FinPayConfig payConfig, Date createTime) {
		logger.info(
				"方法: payCheck, 连连支付查证, 入参: flowId: {}, tradeType: {}, rechargeSource: {}, payConfig: {}, createTime: {}",
				flowId, tradeType, rechargeSource, payConfig.toString(), createTime);
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			// 组装支付查证接口报文信息
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", payConfig.getMerchantNo());
			reqObj.put("no_order", flowId);
			if (PayStyleEnum.RECHARGE.getValue() == tradeType) {
				reqObj.put("type_dc", "0");
			} else if (PayStyleEnum.WITHDRAW.getValue() == tradeType) {
				reqObj.put("type_dc", "1");
			} else {
				return new ResponseEntity<>(Constants.ERROR, "交易类型不存在！");
			}
			reqObj.put("sign_type", payConfig.getSignStyle());
			// 对请求报文进行签名
			String sign = PaymentUtil.addSign(reqObj, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			reqObj.put("sign", sign);
			String reqJson = reqObj.toString();
			// 调用支付查证接口
			logger.info("连连支付查证, 流水标识: {}, 支付查证请求报文信息: {}", flowId, reqJson);
			String resJosn = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqJson);
			logger.info("连连支付查证, 流水标识: {}, 支付查证响应报文信息: {}", flowId, resJosn);
			JSONObject jsonObject = (JSONObject) JSON.parse(resJosn);
			Map<String, Object> result = new HashMap<String, Object>();
			String ret_code = jsonObject.getString("ret_code");// 0000 返回成功
			String ret_msg = jsonObject.getString("ret_msg");// 交易描述结果
			/**
			 * 支付结果SUCCESS代付成功、PROCESSING银行代付处理中、AITING等待支付、FAILURE代付失败、CANCEL
			 * 代付退款
			 */
			if (LIAN_SUCCESS_CODE.equals(ret_code)) {
				String resultCode = jsonObject.getString("result_pay");// 支付结果
				if (LIANLIAN_PAY_CHECK_STATE_SUCCESS.equals(resultCode)) {
					result.put("transState", PAY_STATE_SUCCESS);// 支付成功
					result.put("orderId", jsonObject.getString("no_order"));// 返回订单号
					result.put("transMoney", jsonObject.getString("money_order"));// 交易金额
				} else if (LIANLIAN_PAY_CHECK_STATE_FAILURE.equals(resultCode)
						|| LIANLIAN_PAY_CHECK_STATE_CANCEL.equals(resultCode)) {
					result.put("transState", PAY_STATE_FAIL);// 支付失败
					logger.info("连连支付查证, 流水标识: {}, 支付查证失败: {}", flowId, ret_msg);
				} else if (LIANLIAN_PAY_CHECK_STATE_WAITING.equals(resultCode)) {
					result.put("transState", PAY_STATE_WAIT);// 等待支付
					logger.info("连连支付查证, 流水标识: {}, 支付查证状态为等待支付中: {}", flowId, ret_msg);
				} else {
					result.put("transState", PAY_STATE_PROCESSING);// 银行处理中
					logger.info("连连支付查证, 流水标识: {}, 支付查证交易状态为银行处理中: {}", flowId, ret_msg);
				}
			} else {
				return new ResponseEntity<>(Constants.ERROR, "支付查证异常:" + ret_code + "|" + ret_msg);
			}
			responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
			// 返回响应报文到客户端
			resultPay.put("resPayCheck", resJosn);
			responseEntity.setParams(resultPay);
		} catch (Exception e) {
			logger.error("连连支付查证, 流水标识: {}, 支付查证异常信息: ", flowId, e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "支付查证异常");
		}
		return responseEntity;
	}

	/**
	 * @Description :连连渠道查询卡BIN
	 * @Method_Name : findCardBin;
	 * @param cardNo
	 *            卡号
	 * @param payConfig
	 *            支付配置
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年11月1日 下午3:40:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findCardBin(String cardNo, FinPayConfig payConfig) {
		logger.info("方法: findCardBin, 连连查询卡BIN, 入参: cardNo: {}, payConfig: {}", cardNo, payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		ResponseEntity<?> responseEntity = null;
		try {
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", payConfig.getMerchantNo());
			reqObj.put("card_no", cardNo);
			reqObj.put("sign_type", payConfig.getSignStyle());
			// 增加签名报文
			String sign = PaymentUtil.addSign(reqObj, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			reqObj.put("sign", sign);
			String reqJson = reqObj.toString();
			logger.info("连连查询卡BIN, 银行卡号标识: {}, 卡bin信息查询请求接口报文信息: {}", cardNo, reqJson);
			// 调用联联查询接口
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqJson);
			logger.info("连连查询卡BIN, 银行卡号标识: {}, 卡bin信息查询响应接口报文信息: {}", cardNo, reqJson);
			if (StringUtils.isBlank(resJson)) {
				return new ResponseEntity<>(Constants.ERROR, "查询卡BIN失败,返回信息为空!");
			}
			JSONObject js = (JSONObject) JSON.parse(resJson);
			String ret_code = js.get("ret_code").toString();// 返回的交易码
			String ret_msg = js.get("ret_msg").toString();// 返回的交易信息
			if (THIRD_SUCCESS_FLAG.equals(ret_code)) {
				String bankName = js.get("bank_name").toString();
				String bankCode = js.get("bank_code").toString();
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
			logger.error("连连查询卡BIN, 银行卡号标识: {}, 查询卡BIN异常信息: ", cardNo, e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "银行维护中,暂不支持此银行！");
		}
		return responseEntity;
	}

	/**
	 * @Description : 用户已绑定银行卡查询
	 * @Method_Name : findBankCard;
	 * @param userId
	 *            用户ID
	 * @param accountNo
	 *            银行卡号
	 * @param payConfig
	 *            支付配置
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年11月1日 下午4:08:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> findBankCard(String userId, String accountNo, FinPayConfig payConfig) {
		logger.info("方法: findBankCard, 连连用户已绑定银行卡查询, 用户标识:{}, 入参: userId: {}, accountNo: {}, payConfig: {}", userId,
				userId, accountNo, payConfig.toString());
		Map<String, Object> resultPay = new HashMap<String, Object>();// 返回支付结果
		Map<String, Object> bankCardMap = new HashMap<String, Object>();
		ResponseEntity<?> responseEntity = null;
		try {
			JSONObject reqObj = new JSONObject();
			reqObj.put("oid_partner", payConfig.getMerchantNo());
			reqObj.put("user_id", userId);
			reqObj.put("platform", payConfig.getMerchantNo());
			reqObj.put("offset", "0");
			reqObj.put("sign_type", payConfig.getSignStyle());
			reqObj.put("pay_type", "D");
			String sign = PaymentUtil.addSign(reqObj, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			reqObj.put("sign", sign);
			String reqJson = reqObj.toString();
			logger.info("连连用户已绑定银行卡查询, 用户标识: {}, 请求连连接口报文信息: {}", userId, reqJson);
			String resJosn = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqJson);
			logger.info("连连用户已绑定银行卡查询, 用户标识: {}, 连连响应接口报文信息: {}", userId, resJosn);
			// 返回查询结果
			JSONObject js = (JSONObject) JSON.parse(resJosn);
			String ret_code = js.get("ret_code").toString();// 返回的交易码
			String ret_msg = js.get("ret_msg").toString();// 返回的交易信息
			if (THIRD_SUCCESS_FLAG.equals(ret_code)) {
				// 获取一个json数组
				JSONArray array = js.getJSONArray("agreement_list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					if (accountNo.equals(object.getString("card_no"))) {
						bankCardMap.put("thirdAccount", object.getString("no_agree"));
						bankCardMap.put("bankCode", object.getString("bank_code"));
						bankCardMap.put("bankName", object.getString("bank_name"));
						break;
					}
				}
				if (bankCardMap.size() <= 0) {
					responseEntity = new ResponseEntity<>(Constants.ERROR, "没有找到匹配的银行卡信息！");
				} else {
					responseEntity = new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
					resultPay.put("bankCard", bankCardMap);
					responseEntity.setParams(resultPay);
				}
			} else {
				responseEntity = new ResponseEntity<>(Constants.ERROR, ret_code + "|" + ret_msg);
			}
		} catch (Exception e) {
			logger.error("连连用户已绑定银行卡查询, 用户标识: {}, 连连用户已绑定银行列表查询异常信息: ", userId, e);
			responseEntity = new ResponseEntity<>(Constants.ERROR, "已绑定银行列表查询异常");
		}
		return responseEntity;
	}
	/**
	 * @Description 有盾实名
	 * @param regUserId
	 * @param idCard
	 * @param userName
	 * @param payConfig
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年10月10日 下午2:33:09;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public static ResponseEntity<?> callRealNameReq(Integer regUserId, String idCard, String userName,
			FinPayConfig payConfig) {
		logger.info("方法: callRealNameReq, 有盾实名认证业务, 用户标识: {}, 入参: idCard: {}, userName：{},payConfig: {}", regUserId,
				idCard, userName, payConfig.toString());
		ResponseEntity<?> responseEntity = null ;
		String status = "";
		String desc = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回结果
		try {
			Map<String, String> body = new HashMap<String, String>();
		      body.put("id_no", idCard);
		      body.put("id_name", userName); 
		      
			String result = YouDunEncrty.apiCall(payConfig.getPayUrl(), payConfig.getPublicKey(), payConfig.getPayMd5Key(),
					"O1001S0201", CreateFlowUtil.createFlow("FN"), body);
			logger.info("YouDun callRealNameReq result :{} ",result);
			JSONObject header = JSONObject.parseObject(result);
		      if(header!=null&&header.get("body")!=null){
		    	  JSONObject bodyJs=  JSONObject.parseObject(header.get("body").toString());
			      status= bodyJs.get("status").toString(); // 1一致、2不一致、3无结果。
		      }
			if("1".equals(status)){
				status = "0";
				desc = "认证成功";
			}else if ("2".equals(status)){
				status = "1";
				desc = "认证信息不一致";
			}else if ("3".equals(status)){
				status = "2";
				desc = "认证信息不存在";
			}else {
				status = "9";
				desc = "认证信息异常";
			}
		} catch (Exception e) {
			status = "9";
			desc = "实名网络繁忙，请稍候重试！";
		}
		resultMap.put("state", status);
		resultMap.put("desc", desc);
		responseEntity = new ResponseEntity<>(Constants.SUCCESS);
		responseEntity.setParams(resultMap);
		logger.info("responseEntity:{}", responseEntity);
		return responseEntity;
	}
	

}
