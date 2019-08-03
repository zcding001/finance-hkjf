package com.hongkun.finance.payment.util;

import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;
import java.util.zip.CRC32;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;

/**
 * @Description : 创建流水 通用工具类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.model.util.CreateFlowUtils.java
 * @Author : yanbinghuang
 */
public class CreateFlowUtil {
	private static final Logger logger = LoggerFactory.getLogger(CreateFlowUtil.class);

	/** 交易流水前缀 **/
	private static final String TF = "TF";
	/** 资金划转前缀 **/
	private static final String FT = "FT";
	/** 支付记录流水转前缀 **/
	private static final String PY = "PY";
	
	/**
	 * @Description : 生成交易流水:pattern+YYYYMMDDH24mmssSSS+五位流水
	 * @Method_Name : createFlow;
	 * @param pattern
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月20日 上午10:35:24;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public synchronized static String createFlow(String pattern) {
		String subNum = "";// 五位流水
		try {
			String flowId = DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
			String randomGlobalID = UUID.randomUUID().toString();
			CRC32 crc32 = new CRC32();
			crc32.update(randomGlobalID.getBytes());
			long globalNumberID = crc32.getValue();
			String strGlobalID = String.valueOf(globalNumberID);
			if (strGlobalID.length() >= 5) {
				subNum = strGlobalID.substring(strGlobalID.length() - 5, strGlobalID.length());
			} else {
				subNum = strGlobalID;
				for (int i = 1; i <= 5 - strGlobalID.length(); i++) {
					subNum = subNum + "0";
				}
			}
			return pattern.concat(flowId.concat(subNum));
		} catch (Exception e) {
			logger.error("生成交易流水失败: ", e);
			throw new GeneralException("生成流水信息失败！");
		}
	}
	
	/**
	 * @Description :生成支付记录流水
	 * @Method_Name : createPaymentTradeFlow;
	 * @param tradeType
	 *            交易类型
	 * @param platformSourceEnums
	 *            平台类型枚举
	 * @param businessType
	 *            业务组
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月22日 下午1:54:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public synchronized static String createPaymentTradeFlow(Integer tradeType, PlatformSourceEnums platformSourceEnums,
			String businessType) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(PY).append(tradeType).append(platformSourceEnums.getValue()).append(businessType);
		return createFlow(pattern.toString());
	}

	/***
	 * @Description :创建交易流水表的交易流水 格式：{TF+交易类型+交易来源+业务组+YYYYMMDDH24mmssSSS+五位流水}
	 * @Method_Name : createTradeFlow;
	 * @param tradeType
	 * @param platformSourceEnums
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月20日 上午10:25:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public synchronized static String createTradeFlow(Integer tradeType, PlatformSourceEnums platformSourceEnums) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(TF).append(tradeType).append(platformSourceEnums.getValue()).append(getBusinessValue(tradeType));
		return createFlow(pattern.toString());
	}

	/***
	 * @Description : 创建资金划转流水 格式：{FT+交易类型+资金划转类型+交易来源+YYYYMMDDH24mmssSSS+五位流水}
	 * @Method_Name : createTransferFlow;
	 * @param tradeType
	 *            交易类型
	 * @param subCode
	 *            资金划转类型
	 * @param platformSourceEnums
	 *            交易来源
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年11月20日 上午10:17:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public synchronized static String createTransferFlow(Integer tradeType, Integer subCode,
			PlatformSourceEnums platformSourceEnums) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(FT).append(tradeType).append(subCode).append(platformSourceEnums.getValue());
		return createFlow(pattern.toString());
	}

	/**
	 * @Description : 根据交易类型返回业务组值
	 * @Method_Name : getBusinessValue;
	 * @param tradeType
	 *            交易类型
	 * @return
	 * @return : String;
	 * @Creation Date : 2018年3月16日 下午3:19:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private static String getBusinessValue(Integer tradeType) {
		if (String.valueOf(TRADE_TYPE_QDZ_TURNS_OUT_INVEST).equals(tradeType)
				|| String.valueOf(TRADE_TYPE_INVEST_QDZ).equals(tradeType)
				|| String.valueOf(TRADE_TYPE_QDZ_TURNS_IN_INVEST).equals(tradeType)) {
			return QDZ_INVEST_BUSINESS;
		} else if (String.valueOf(TRADE_TYPE_INVEST_RECHARGE).equals(tradeType)
				|| String.valueOf(TRADE_TYPE_RECHARGE_INVEST).equals(tradeType)) {
			return BANK_INVEST_BUSINESS;
		} else {
			return COMMON_BUSINESS;
		}
	}
	
	public static void main(String[] args) {
		String flowid = CreateFlowUtil.createTradeFlow(1011, PlatformSourceEnums.PC);
		System.out.println(flowid);
		System.out.println(flowid.substring(2, 6));
		System.out.println(flowid.substring(6, 7));
	}
}
