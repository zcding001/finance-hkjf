
/**
 * 
 */
package com.hongkun.finance.payment.factory;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;

/**
 * @Description : 入参校验工具类
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.payment.factory.ValidateParamsFactory.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class ValidateParamsFactory {
	private static final Logger logger = LoggerFactory.getLogger(ValidateParamsFactory.class);

	/**
	 * @Description : 充值数据校验
	 * @Method_Name : rechargeValidate
	 * @param rechargeCash
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月6日 下午2:48:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static ResponseEntity<?> rechargeValidate(RechargeCash rechargeCash) {
		BigDecimal transMoney = rechargeCash.getTransMoney();// 充值金额
		String bankCard = rechargeCash.getBankCard();// 银行卡号
		String thirdAccount = rechargeCash.getThirdAccount();// 第三方协议号
		String bankCode = rechargeCash.getBankCode();// 银行code 网银充值不能为空
		// 支付渠道
		String payChannel = rechargeCash.getPayChannel();
		// 平台来源
		String platformSourceName = rechargeCash.getPlatformSourceName();
		// 平台系统
		String systemTypeName = rechargeCash.getSystemTypeName();
		// 支付方式
		String payStyle = rechargeCash.getPayStyle();

		if (StringUtils.isBlank(systemTypeName)) {
			rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
		}
		if (SystemTypeEnums.valueByType(systemTypeName) == -1) {
			return new ResponseEntity<>(Constants.ERROR, "充值系统来源不存在！");
		}
		if (StringUtils.isBlank(platformSourceName)) {
			return new ResponseEntity<>(Constants.ERROR, "充值平台不能为空！");
		}
		if (PlatformSourceEnums.valueByType(platformSourceName) == -1) {
			return new ResponseEntity<>(Constants.ERROR, "充值平台来源不存在！");
		}
		if (StringUtils.isBlank(payChannel)) {
			return new ResponseEntity<>(Constants.ERROR, "充值渠道不能为空！");
		}
		if (PayChannelEnum.fromChannelName(payChannel) == -1) {
			return new ResponseEntity<>(Constants.ERROR, "充值渠道不存在！");
		}
		if (StringUtils.isBlank(payStyle)) {
			return new ResponseEntity<>(Constants.ERROR, "充值支付方式不能为空！");
		}
		if (PayStyleEnum.valueByType(payStyle) == -1) {
			return new ResponseEntity<>(Constants.ERROR, "充值支付方式不存在！");
		}
		if (transMoney == null || CompareUtil.lteZero(transMoney)) {
			return new ResponseEntity<>(Constants.ERROR, "充值金额数据异常！");
		}
		if (rechargeCash.getuType() == 0) {
			return new ResponseEntity<>(Constants.ERROR, "用户类型不能为空！");
		}
		// 网银充值
		if (PayStyleEnum.WY.getType().equalsIgnoreCase(payStyle) && StringUtils.isBlank(bankCode)
				&& (StringUtils.isBlank(bankCard) || StringUtils.isBlank(rechargeCash.getThirdAccount()))) {
			return new ResponseEntity<>(Constants.ERROR, "银行选择错误，请重新选择！");
		}
		// 认证绑卡充值（未绑卡）
		if (PayStyleEnum.RZ.getType().equalsIgnoreCase(payStyle) && StringUtils.isBlank(bankCard)) {
			return new ResponseEntity<>(Constants.ERROR, "银行卡号不能为空，请重新绑卡！");
		}
		// 快捷支付
		if (PayStyleEnum.KJ.getType().equalsIgnoreCase(payStyle) && StringUtils.isNotBlank(bankCard)
				&& StringUtils.isBlank(thirdAccount)) {
			return new ResponseEntity<>(Constants.ERROR, "银行选择错误，请重新选择！");
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	/**
	 * @Description : 签名验签
	 * @Method_Name : checkSign;
	 * @param rechargeCash
	 * @param sessionId
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018年3月13日 上午9:50:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean checkSign(RechargeCash rechargeCash, String sessionId) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("transMoney", rechargeCash.getTransMoney());
		reqObj.put("rechargeFlag", rechargeCash.getRechargeFlag());
		if(StringUtils.isNotBlank(rechargeCash.getBankCard())){
			reqObj.put("bankCard", rechargeCash.getBankCard());
		}
		if(StringUtils.isNotBlank(rechargeCash.getBankCode())){
			reqObj.put("bankCode", rechargeCash.getBankCode());
		}
		if(StringUtils.isNotBlank(String.valueOf(rechargeCash.getBankCardId()))){
			reqObj.put("bankCardId", rechargeCash.getBankCardId());
		}
		reqObj.put("systemTypeName", rechargeCash.getSystemTypeName());
		reqObj.put("platformSourceName", rechargeCash.getPlatformSourceName());
		reqObj.put("payChannel", rechargeCash.getPayChannel());
		reqObj.put("sign_type", rechargeCash.getSign_type());
		reqObj.put("sessionId", sessionId);
		reqObj.put("sign", rechargeCash.getSign());
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, rechargeCash.getSign());
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

	/**
	 * @Description : 协议支付-短验验签
	 * @Method_Name : checkVerificationCodeSign;
	 * @param rechargeCash
	 * @param sessionId
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018-05-16 10:01:11;
	 * @Author : liangbin@hongkun.com.cn binliang;
	 */
	public static boolean checkVerificationCodeSign(RechargeCash rechargeCash, String sessionId) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("bankCard", rechargeCash.getBankCard());
		reqObj.put("rechargeFlag", rechargeCash.getRechargeFlag());
		reqObj.put("transMoney", rechargeCash.getTransMoney());
		reqObj.put("systemTypeName", rechargeCash.getSystemTypeName());
		reqObj.put("platformSourceName", rechargeCash.getPlatformSourceName());
		reqObj.put("payChannel", rechargeCash.getPayChannel());
		reqObj.put("bankCardId", rechargeCash.getBankCardId());
		reqObj.put("tel", rechargeCash.getTel());
		if(StringUtils.isNotBlank(rechargeCash.getPaymentFlowId())){
			reqObj.put("paymentFlowId", rechargeCash.getPaymentFlowId());//易宝重发用到，如果没有不参与签名
		}
		reqObj.put("sign_type", rechargeCash.getSign_type());
		reqObj.put("sessionId", sessionId);
		reqObj.put("sign", rechargeCash.getSign());
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, rechargeCash.getSign());
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

	/**
	 * @Description : 协议支付-支付验签
	 * @Method_Name : checkAgreementPaySign;
	 * @param rechargeCash
	 * @param sessionId
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018-05-16 14:44:36;
	 * @Author : liangbin@hongkun.com.cn binliang;
	 */
	public static boolean checkAgreementPaySign(RechargeCash rechargeCash, String sessionId) {
		JSONObject reqObj = new JSONObject();
		if(StringUtils.isNotBlank(String.valueOf(rechargeCash.getOperateType()))){
			reqObj.put("operateType", rechargeCash.getOperateType());
		}
		reqObj.put("payUnionCode", rechargeCash.getPayUnionCode());
		reqObj.put("verificationCode", rechargeCash.getVerificationCode());
		if(StringUtils.isNotBlank(rechargeCash.getPaymentFlowId())){
			reqObj.put("paymentFlowId", rechargeCash.getPaymentFlowId());
		}
		reqObj.put("bankCard", rechargeCash.getBankCard());
		reqObj.put("rechargeFlag", rechargeCash.getRechargeFlag());
		reqObj.put("transMoney", rechargeCash.getTransMoney());
		reqObj.put("systemTypeName", rechargeCash.getSystemTypeName());
		reqObj.put("platformSourceName", rechargeCash.getPlatformSourceName());
		reqObj.put("payChannel", rechargeCash.getPayChannel());
		reqObj.put("bankCardId", rechargeCash.getBankCardId());
		reqObj.put("tel", rechargeCash.getTel());
		reqObj.put("sign_type", rechargeCash.getSign_type());
		reqObj.put("sessionId", sessionId);
		reqObj.put("sign", rechargeCash.getSign());
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, rechargeCash.getSign());
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

	public static boolean checkAgreementPaySignV1(RechargeCash rechargeCash, String sessionId) {
		logger.info("方法: checkAgreementPaySignV1, 签名验证, 入参: rechargeCash: {}, sessionId: {}", rechargeCash.toString(),
				sessionId);
		JSONObject reqObj = new JSONObject();
		reqObj.put("operateType", rechargeCash.getOperateType());
		reqObj.put("payUnionCode", rechargeCash.getPayUnionCode());
		reqObj.put("verificationCode", rechargeCash.getVerificationCode());
		if (rechargeCash != null && StringUtils.isNotBlank(rechargeCash.getFlowId())) {
			logger.info("方法: checkAgreementPaySignV1, 签名验证, flowId: {}", rechargeCash.getFlowId());
			reqObj.put("flowId", rechargeCash.getFlowId());
		}
		reqObj.put("bankCard", rechargeCash.getBankCard());
		reqObj.put("rechargeFlag", rechargeCash.getRechargeFlag());
		reqObj.put("transMoney", rechargeCash.getTransMoney());
		reqObj.put("systemTypeName", rechargeCash.getSystemTypeName());
		reqObj.put("platformSourceName", rechargeCash.getPlatformSourceName());
		reqObj.put("payChannel", rechargeCash.getPayChannel());
		reqObj.put("bankCardId", rechargeCash.getBankCardId());
		reqObj.put("tel", rechargeCash.getTel());
		reqObj.put("sign_type", rechargeCash.getSign_type());
		reqObj.put("sessionId", sessionId);
		reqObj.put("sign", rechargeCash.getSign());
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, rechargeCash.getSign());
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

	/**
	 * @Description : 跳转到收银台页面，参数验签
	 * @Method_Name : checkCashDeskSign;
	 * @param rechargeCash
	 * @param sessionId
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018年5月15日 下午5:55:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean checkCashDeskSign(RechargeCash rechargeCash, String sessionId) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("transMoney", rechargeCash.getTransMoney());
		reqObj.put("bankCard", rechargeCash.getBankCard());
		if (rechargeCash.getBankCardId() != null) {
			reqObj.put("bankCardId", rechargeCash.getBankCardId());
		}
		reqObj.put("systemTypeName", rechargeCash.getSystemTypeName());
		reqObj.put("platformSourceName", rechargeCash.getPlatformSourceName());
		reqObj.put("payChannel", rechargeCash.getPayChannel());
		reqObj.put("sign_type", rechargeCash.getSign_type());
		reqObj.put("sessionId", sessionId);
		reqObj.put("sign", rechargeCash.getSign());
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, rechargeCash.getSign());
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

	/**
	 * @Description :校验提现申请参数
	 * @Method_Name : checkWithDrawSign;
	 * @param transMoney
	 *            提现金额
	 * @param platformSourceName
	 *            平台来源
	 * @param signType
	 *            加密类型
	 * @param sign
	 *            加密值
	 * @param sessionId
	 *            会话ID
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018年5月16日 上午10:11:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean checkWithDrawSign(BigDecimal transMoney, Integer platformSourceName, String signType,
			String sign, String sessionId, Integer couponDetailId, Integer systemCode) {
		JSONObject reqObj = new JSONObject();
		reqObj.put("transMoney", transMoney);
		if (couponDetailId != null) {
			reqObj.put("couponDetailId", couponDetailId);
		}
		reqObj.put("platformSource", platformSourceName);
		reqObj.put("sign_type", signType);
		reqObj.put("sessionId", sessionId);
		reqObj.put("systemCode", systemCode);
		reqObj.put("sign", sign);
		String sourceStr = PaymentUtil.genSignData(reqObj);
		logger.info("客户端充值验签原串: {}, sign: {}", sourceStr, sign);
		return PaymentUtil.checkSignByMd5(reqObj.toString());
	}

}
