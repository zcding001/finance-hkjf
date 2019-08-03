
/**
 * 
 */
package com.hongkun.finance.payment.factory;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.llpayvo.BankCardSignInfo;
import com.hongkun.finance.payment.llpayvo.PayPlan;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.TransferVo;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;

/**
 * @Description : caoxb 类描述:支付校验工厂类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.factory.PayValidateFactory.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class PayValidateFactory {

	/**
	 * @Description : 校验查询银行信息
	 * @Method_Name : checkBankInfo;
	 * @param sysNameCode
	 *            系统CODE
	 * @param platfromCode
	 * @param payStyle
	 *            支付方式
	 * @param channnel
	 *            交易渠道
	 * @return
	 * @return : Map<String,String>;
	 * @Creation Date : 2017年6月15日 上午11:26:22;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> checkBankInfo(String sysNameCode, String platfromCode, String payStyle,
			String channnel) throws Exception {
		Map<String, String> checkResultMap = new HashMap<String, String>();
		if (StringUtils.isBlank(sysNameCode)) {
			checkResultMap.put(SERVER_FAIL_FLAG, "系统类型不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(channnel)) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易渠道不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(payStyle)) {
			checkResultMap.put(SERVER_FAIL_FLAG, "支付方式不能为空！");
			return checkResultMap;
		}
		checkResultMap.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
		return checkResultMap;

	}

	/**
	 * @Description : 校验数据合法性
	 * @Method_Name : checkDataInfo;
	 * @param finTradeFlow
	 *            交易流水对象
	 * @param transferSubCode
	 *            资金划转SUBCODE
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 *         key:SERVER_FAIL_FLAG(失败)或SERVER_SUCCESS_FLAG(成功)
	 * @Creation Date : 2018年4月19日 上午9:36:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> checkDataInfo(FinTradeFlow finTradeFlow, Integer transferSubCode)
			throws Exception {
		Map<String, String> checkResultMap = new HashMap<String, String>();
		if (StringUtils.isBlank(finTradeFlow.getPflowId())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "业务交易流水为空！");
			return checkResultMap;
		}
		if (finTradeFlow.getTradeSource() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易来源不能为空！");
			return checkResultMap;
		}
		if (finTradeFlow.getRegUserId() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易发起方id不能为空！");
			return checkResultMap;
		}
		if (finTradeFlow.getTransMoney() == null || CompareUtil.lteZero(finTradeFlow.getTransMoney())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易金额不能为空或小于等于零！");
			return checkResultMap;
		}
		if (finTradeFlow.getTradeType() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易类型不能为空！");
			return checkResultMap;
		}
		if (transferSubCode == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "资金划转code不能为空！");
			return checkResultMap;
		}
		checkResultMap.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
		return checkResultMap;
	}

	/**
	 * @Description : 校验数据合法性
	 * @Method_Name : checkTransferDataInfo;
	 * @param transferInfo
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年4月24日 下午2:41:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> checkTransferDataInfo(TransferVo transferInfo) throws Exception {
		Map<String, String> checkResultMap = new HashMap<String, String>();
		if (transferInfo == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "转账对象不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getFromUserId() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "发起人用户ID不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getToUserId() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "接收人用户id不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(transferInfo.getBusinessCode())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "业务code不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getTransMoney() == null || CompareUtil.lteZero(transferInfo.getTransMoney())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易金额不能为空或为零为负！");
			return checkResultMap;
		}
		if (transferInfo.getPlatformSourceEnums() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易来源不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getTradeType() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易类型不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getTransferInSubCode() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "收入划转subCode不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getTransferOutSubCode() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "支出划转subCode不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getPointPayFlag() == null || transferInfo.getPointPayFlag() < 0
				|| transferInfo.getPointPayFlag() > 1) {
			checkResultMap.put(SERVER_FAIL_FLAG, "积分转现金标识不能为空！");
			return checkResultMap;
		}
		if (transferInfo.getPointPayFlag() == EXIST_POINT_PAY) {
			if (transferInfo.getPointChangeMoney() == null || CompareUtil.lteZero(transferInfo.getPointChangeMoney())) {
				checkResultMap.put(SERVER_FAIL_FLAG, "积分转现金金额不能为负或为零！");
				return checkResultMap;
			}
		}
		checkResultMap.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
		return checkResultMap;
	}

	/***
	 * @Description : 校验提现放款参数安全性
	 * @Method_Name : checkWithDrawCashData;
	 * @param withDrawCashInfo
	 * @return
	 * @return : Map<String,String>;
	 * @Creation Date : 2017年5月31日 下午2:32:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> checkWithDrawCashData(WithDrawCash withDrawCashInfo) throws Exception {
		Map<String, String> checkResultMap = new HashMap<String, String>();
		if (withDrawCashInfo == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "提现对象不能为空！");
			return checkResultMap;
		}
		if (withDrawCashInfo.getRegUserId() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户ID不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(withDrawCashInfo.getFlowId())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "支付记录表ID不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(withDrawCashInfo.getUserName())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户姓名不能为空！");
			return checkResultMap;
		}
		if (withDrawCashInfo.getPayChannelEnum() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "交易渠道不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(withDrawCashInfo.getSysNameCode())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "系统类型不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(withDrawCashInfo.getPlatformSourceName())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "平台类型不能为空！");
			return checkResultMap;
		}
		if (withDrawCashInfo.getPayStyleEnum() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "支付方式不能为空！");
			return checkResultMap;
		}
		if (withDrawCashInfo.getUserType() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户类型不能为空！");
			return checkResultMap;
		}
		checkResultMap.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
		return checkResultMap;
	}

	/***
	 * @Description :校验提现金额的正确性
	 * @Method_Name : validateMoney;
	 * @param transMoney
	 *            提现金额
	 * @param userAbleMoney
	 *            用户可用余额
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月20日 上午9:11:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> validateMoney(BigDecimal transMoney, BigDecimal userAbleMoney) {
		if (CompareUtil.lteZero(transMoney)) {
			return new ResponseEntity<>(ERROR, "提现金额必须为正整数!");
		}
		if (CompareUtil.gt(transMoney, userAbleMoney)) {
			return new ResponseEntity<>(ERROR, "提现金额不能大于可用金额!");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/****
	 * @Description : 校验签约对象信息参数合法性
	 * @Method_Name : checkBankCardSigningData;
	 * @param bankCardSignInfo
	 * @return
	 * @return : Map<String,String>;
	 * @Creation Date : 2017年6月8日 上午11:54:24;*
	 * 
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> checkBankCardSigningData(BankCardSignInfo bankCardSignInfo) throws Exception {
		Map<String, String> checkResultMap = new HashMap<String, String>();
		if (StringUtils.isBlank(bankCardSignInfo.getCardNo())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户卡号不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(bankCardSignInfo.getIdCard())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "身份证号不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(bankCardSignInfo.getTel())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "手机号不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(bankCardSignInfo.getUserId())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户ID不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(bankCardSignInfo.getUserName())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "用户姓名不能为空！");
			return checkResultMap;
		}
		if (bankCardSignInfo.getRegisterDate() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "注册时间不能为空！");
			return checkResultMap;
		}
		if (StringUtils.isBlank(bankCardSignInfo.getLoginName())) {
			checkResultMap.put(SERVER_FAIL_FLAG, "商户用户登录名不能为空！");
			return checkResultMap;
		}
		if (bankCardSignInfo.getFinPayConfig() == null) {
			checkResultMap.put(SERVER_FAIL_FLAG, "支付配置信息不能为空！");
			return checkResultMap;
		}
		checkResultMap.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
		return checkResultMap;
	}

	/**
	 * 
	 * @Description : 校验代扣还款
	 * @Method_Name : validatePayPlan
	 * @param payPlan
	 * @return
	 * @return : String
	 * @throws Exception
	 * @Creation Date : 2017年6月28日 下午5:22:35
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static String validatePayPlan(PayPlan payPlan) {

		try {
			Map<String, String> cash = checkWithDrawCashData(payPlan);
			if (cash != null) {
				return cash.get(SERVER_FAIL_FLAG);
			}
			if (StringUtils.isBlank(payPlan.getBidId())) {
				return "标的不能为空！";
			}
			if (StringUtils.isBlank(payPlan.getRepayId())) {
				return "还款计划ID不能为空！";
			}
			if (StringUtils.isBlank(payPlan.getIdCard())) {
				return "还款人身份证号不能为空！";
			}
			if (payPlan.getRepayAmount() == null || CompareUtil.lteZero(payPlan.getRepayAmount())) {
				return "还款金额异常！";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Description : 校验是否线上环境、处理金额
	 * @Method_Name : isOnlineRecharge
	 * @param rechargeCash
	 * @throws Exception
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月11日 下午4:35:24
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static BigDecimal dealRechargeMoney(RechargeCash rechargeCash) throws Exception {
		BigDecimal transMoney = rechargeCash.getTransMoney();
		BigDecimal money = rechargeCash.getTransMoney();
		// if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
		// if
		// (rechargeCash.getPayChannel().equalsIgnoreCase(PayChannelEnum.LianLian.getChannelKey()))
		// {
		// transMoney = new BigDecimal("0.01");
		// money = new BigDecimal("0.01");
		// } else {
		// transMoney = new BigDecimal("1");
		// money = new BigDecimal("1").divide(new BigDecimal(100));
		// }
		// } else {
		if (rechargeCash.getPayChannel().equalsIgnoreCase(PayChannelEnum.BaoFu.getChannelKey())) {
			transMoney = rechargeCash.getTransMoney().multiply(new BigDecimal(100)); // 交易金额，以分为单位（前台元在此转化为分）
		}
		// }
		rechargeCash.setTransMoney(transMoney);
		return money;
	}
}
