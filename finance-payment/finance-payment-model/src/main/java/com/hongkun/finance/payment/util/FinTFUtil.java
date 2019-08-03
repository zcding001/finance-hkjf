package com.hongkun.finance.payment.util;

import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.CompareUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.INCOME_TYPE_COMMON;

/**
 * @Description : 流水、划转对象组装工具
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.util.FinTFUtil.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */
public class FinTFUtil {
	private static final Logger logger = LoggerFactory.getLogger(FinTFUtil.class);

	private FinTFUtil() {
	}

	/**
	 * @Description : 初始化交易流水
	 * @Method_Name : initFinTradeFlow;
	 * @param regUserId
	 *            用户ID
	 * @param pflowId
	 *            业务操作ID
	 * @param money
	 *            交易金额
	 * @param tradeType
	 *            交易类型
	 * @param platformSourceEnums
	 *            交易来源
	 * @return
	 * @return : FinTradeFlow;
	 * @Creation Date : 2017年11月14日 上午11:00:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static FinTradeFlow initFinTradeFlow(Integer regUserId, Object pflowId, BigDecimal money, int tradeType,
			PlatformSourceEnums platformSourceEnums) {
		FinTradeFlow finTradeFlow = new FinTradeFlow();
		finTradeFlow.setFlowId(CreateFlowUtil.createTradeFlow(tradeType, platformSourceEnums));
		finTradeFlow.setPflowId(String.valueOf(pflowId));
		finTradeFlow.setRegUserId(regUserId);
		finTradeFlow.setTransMoney(money);
		finTradeFlow.setTradeType(tradeType);
		finTradeFlow.setTradeSource(platformSourceEnums.getValue());
		return finTradeFlow;
	}

	/**
	 * @Description : 初始化收款资金划转
	 * @Method_Name : fitInFinFundtransfer;
	 * @param regUserId
	 *            账户ID
	 * @param transferRegUserId
	 *            划转账户ID
	 * @param money
	 *            交易金额
	 * @param transferList
	 *            资金划转LIST
	 * @param subCode
	 *            资金划转类型
	 * @return : void;
	 * @Creation Date : 2017年11月14日 上午11:03:13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static void fitFinFundtransfer(String tradeFlowId, Integer regUserId, Integer transferRegUserId,
			BigDecimal money, List<FinFundtransfer> transferList, int subCode) {
		if (CompareUtil.gtZero(money)) {
			transferList.add(initFinFundtransfer(tradeFlowId, regUserId, transferRegUserId, money, subCode));
		}
	}
	/**
	 *  @Description    : 
	 *  @Method_Name    : fitFinFundtransfer;
	 *  @param tradeFlowId 交易流水
	 *  @param regUserId 账户ID
	 *  @param transferRegUserId 资金划转ID
	 *  @param money   交易金额
	 *  @param transferList 资金划转LIST
	 *  @param subCode   资金划转类型
	 *  @param showFlag 是否在前台展示 0展示，1不展示
	 *  @return         : void;
	 *  @Creation Date  : 2018年12月11日 下午6:04:51;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static void fitFinFundtransfer(String tradeFlowId, Integer regUserId, Integer transferRegUserId,
            BigDecimal money, List<FinFundtransfer> transferList, int subCode,int showFlag) {
        if (CompareUtil.gtZero(money)) {
            FinFundtransfer finFundtransfer=initFinFundtransfer(tradeFlowId, regUserId, transferRegUserId, money, subCode);
            finFundtransfer.setShowFlag(showFlag);
            transferList.add(finFundtransfer);
        }
    }

	/**
	 * @Description : 初始化付款资金划转
	 * @Method_Name : fitOutFinFundtransfer;
	 * @param regUserId
	 *            账户ID
	 * @param money
	 *            交易金额
	 * @param list
	 *            资金划转LIST
	 * @param subCode
	 *            资金划转类型
	 * @return : void;
	 * @Creation Date : 2017年11月14日 上午11:21:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static void fitOutFinFundtransfer(String tradeFlowId, Integer regUserId, BigDecimal money,
			List<FinFundtransfer> list, int subCode) {
		if (CompareUtil.gtZero(money)) {
			list.add(initFinFundtransfer(tradeFlowId, regUserId, null, money, subCode));
		}
	}

	/**
	 * @Description : 初始化付款资金划转
	 * @Method_Name : initFinFundtransfer;
	 * @param tradeFlowId
	 *            交易流水ID
	 * @param regUserId
	 *            用户Id
	 * @param transferRegUserId
	 *            接收人用户ID
	 * @param money
	 *            交易金额
	 * @param subCode
	 *            资金划转SUBCODE
	 * @return
	 * @return : FinFundtransfer;
	 * @Creation Date : 2017年12月13日 下午3:46:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static FinFundtransfer initFinFundtransfer(String tradeFlowId, Integer regUserId, Integer transferRegUserId,
			BigDecimal money, int subCode) {
		logger.info("初始化资金划转对象, 入参: tradeFlowId: {}, regUserId: {}, transferRegUserId: {}, money: {}, subCode: {}",
				tradeFlowId, regUserId, transferRegUserId, money, subCode);
		FinFundtransfer finFundtransfer = new FinFundtransfer();
		try {
			// 根据交易流水ID，获取资金划转所需的交易类型和交易来源
			Map<String, String> map = getTradeTypeAndTradeSourceByTradeFlowId(tradeFlowId);
			finFundtransfer.setFlowId(CreateFlowUtil.createTransferFlow(Integer.parseInt(map.get("tradeType")), subCode,
					PlatformSourceEnums.typeByValue(Integer.parseInt(map.get("tradeSource")))));
			finFundtransfer.setTradeFlowId(tradeFlowId);
			finFundtransfer.setRegUserId(regUserId);
			finFundtransfer.setTransMoney(money);
			finFundtransfer.setSubCode(subCode);
			if (transferRegUserId != null && transferRegUserId > 0) {
				finFundtransfer.setRecRegUserId(transferRegUserId);
			}
			return finFundtransfer;
		} catch (Exception e) {
			logger.error(
					"初始化资金划转对象, 入参: tradeFlowId: {}, regUserId: {}, transferRegUserId: {}, money: {}, subCode: {}, 初始化资金划转对象失败: ",
					tradeFlowId, regUserId, transferRegUserId, money, subCode, e);
			throw new GeneralException("初始化资金划转对象失败!");
		}
	}

	/**
	 * @Description : 用于初始化资金划转对象
	 * @Method_Name : initFinFundtransfer;
	 * @param regUserId
	 *            用户ID
	 * @param pflowId
	 *            交易流水ID
	 * @param money
	 *            交易金额
	 * @param preMoney
	 *            交易前的金额
	 * @param nowMoney
	 *            交易后的金额
	 * @param subCode
	 *            资金节转类型
	 * @return
	 * @return : FinFundtransfer;
	 * @throws Exception
	 * @Creation Date : 2017年11月14日 上午11:09:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static FinFundtransfer initFinFundtransfer(Integer regUserId, Integer recUserId, String pflowId,
			BigDecimal money, BigDecimal preMoney, BigDecimal nowMoney, BigDecimal afterMoney, int subCode) {
		logger.info(
				"方法: initFinFundtransfer, 初始化资金划转对象, 入参: regUserId: {}, recUserId: {}, pflowId: {}, money: {}, preMoney: {}, nowMoney: {}, subCode: {}",
				regUserId, recUserId, pflowId, money, preMoney, nowMoney, subCode);
		FinFundtransfer finFundtransfer = new FinFundtransfer();
		try {
			// 根据交易流水ID，获取资金划转所需的交易类型和交易来源
			Map<String, String> map = getTradeTypeAndTradeSourceByTradeFlowId(pflowId);
			finFundtransfer.setFlowId(CreateFlowUtil.createTransferFlow(Integer.parseInt(map.get("tradeType")), subCode,
					PlatformSourceEnums.typeByValue(Integer.parseInt(map.get("tradeSource")))));
			finFundtransfer.setTradeFlowId(pflowId);
			if (recUserId != null) {
				finFundtransfer.setRecRegUserId(recUserId);
			}
			finFundtransfer.setRegUserId(regUserId);
			finFundtransfer.setTransMoney(money);
			finFundtransfer.setPreMoney(preMoney);
			finFundtransfer.setNowMoney(nowMoney);
			finFundtransfer.setAfterMoney(afterMoney);
			finFundtransfer.setSubCode(subCode);
			return finFundtransfer;
		} catch (Exception e) {
			logger.error(
					"初始化资金划转对象, 入参: regUserId: {}, recUserId: {}, pflowId: {}, money: {}, preMoney: {}, nowMoney: {},subCode: {}, 初始化资金划转对象失败: ",
					regUserId, recUserId, pflowId, money, preMoney, nowMoney, subCode, e);
			throw new GeneralException("初始化资金划转对象失败!");
		}
	}

	/***
	 * @Description : 根据交易流水ID，获取资金划转所需的交易类型和交易来源
	 * @Method_Name : getTradeTypeAndTradeSourceByTradeFlowId;
	 * @param tradeFlowId
	 *            交易流水ID
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2017年11月20日 下午3:31:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> getTradeTypeAndTradeSourceByTradeFlowId(String tradeFlowId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(tradeFlowId)) {
			throw new BusinessException("交易流水ID为空！");
		}
		// 格式：{TF+交易类型(4)+交易来源(2)+业务组+YYYYMMDDH24mmssSSS+五位流水}
		map.put("tradeType", tradeFlowId.substring(2, 6));
		map.put("tradeSource", tradeFlowId.substring(6, 8));
		return map;
	}

	/**
	 * @Description : 通过支付流水ID，获取交易类型和交易来源
	 * @Method_Name : getTradeTypeAndTradeSourceByPayFlowId;
	 * @param tradeFlowId
	 *            支付流水ID
	 * @param incomeType
	 *            充值类型
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年2月27日 下午5:21:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static Map<String, String> getTradeTypeAndTradeSourceByIncomeType(String tradeFlowId, Integer incomeType) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(tradeFlowId)) {
			throw new BusinessException("交易流水ID为空！");
		}
		// 判断充值类型tradeFlowId
		// 格式：{PY+交易类型(2位)+交易来源(2位)+业务组(2位)+YYYYMMDDH24mmssSSS+五位流水}
		if (incomeType == INCOME_TYPE_COMMON) {
			if (TradeTransferConstants.BANK_INVEST_BUSINESS.equals(tradeFlowId.substring(6, 8))) {
				map.put("tradeType", String.valueOf(TradeTransferConstants.TRADE_TYPE_RECHARGE_INVEST));
			} else {
				map.put("tradeType", String.valueOf(TradeTransferConstants.TRADE_TYPE_RECHARGE));
			}
		} else {
			map.put("tradeType", String.valueOf(TradeTransferConstants.TRADE_TYPE_RECHARGE_PROPERTY));
		}
		map.put("tradeSource", tradeFlowId.substring(4, 6));
		return map;
	}

	/**
	 * 翻转资金划转，翻转内容: reg_user_id <==> rec_reg_user_id, 重置subCode
	 * 
	 * @method_name : reverse
	 * @param finFundtransfer
	 * @param subCode
	 * @return
	 * @Author : zc.ding@foxmail.com
	 * @Date : 2018年5月24日 下午8:25:01
	 */
	public static FinFundtransfer reverse(FinFundtransfer finFundtransfer, int subCode) {
		logger.info("初始化资金划转对象, 原来资金划转对象: {}", finFundtransfer.toString());
		FinFundtransfer newFinFundtransfer = new FinFundtransfer();
		try {
			String tradeFlowId = finFundtransfer.getTradeFlowId();
			BeanPropertiesUtil.mergeAndReturn(newFinFundtransfer, finFundtransfer);
			// 根据交易流水ID，获取资金划转所需的交易类型和交易来源
			Map<String, String> map = getTradeTypeAndTradeSourceByTradeFlowId(tradeFlowId);
			newFinFundtransfer.setFlowId(CreateFlowUtil.createTransferFlow(Integer.parseInt(map.get("tradeType")),
					subCode, PlatformSourceEnums.typeByValue(Integer.parseInt(map.get("tradeSource")))));
			newFinFundtransfer.setRegUserId(finFundtransfer.getRecRegUserId());
			newFinFundtransfer.setRecRegUserId(finFundtransfer.getRegUserId());
			newFinFundtransfer.setSubCode(subCode);
			return newFinFundtransfer;
		} catch (Exception e) {
			logger.error("初始化资金划转对象, 原来资金划转对象: {}", finFundtransfer.toString(), e);
			throw new GeneralException("初始化资金划转对象失败!");
		}
	}
}
