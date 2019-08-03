package com.hongkun.finance.payment.facade;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;

import java.util.Map;

public interface FinPayMentNoticeFacade {
	/**
	 * @Description :连连签约绑卡同步通知
	 * @Method_Name : lianlianSignNotice;
	 * @param reqStr
	 * @param bankCardId
	 *            银行卡ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月4日 下午4:59:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> lianlianSignNotice(String reqStr, String bankCardId);

	/**
	 * @Description :连连提现成功后，异步通知
	 * @Method_Name : lianlianAsyncNotice;
	 * @param reqStr
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月4日 下午5:45:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> lianlianAsyncNotice(String reqStr) throws Exception;

	/**
	 * @Description : 宝付提现成功后，异步通知
	 * @Method_Name : baofuAsyncNotice;
	 * @param reqStr
	 * @param systemTypeEnums
	 *            系统类型
	 * @param payChannelEnum
	 *            支付渠道
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月23日 上午10:49:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> baofuAsyncNotice(String reqStr, SystemTypeEnums systemTypeEnums, PayChannelEnum payChannelEnum);

	/**
	 * @Description : 连连充值异步通知
	 * @Method_Name : lianlianRechargeAsyncNotifyUrl
	 * @param reqStr
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月5日 下午6:02:42
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	ResponseEntity<?> lianlianRechargeAsyncNotifyUrl(String reqStr, Integer incomeType) throws Exception;

	/**
	 * @Description : 宝付认证充值异步通知
	 * @Method_Name : baofuRechargeAsyncNotifyUrl;
	 * @param reqStr
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月24日 下午3:24:57;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> baofuRechargeAsyncNotifyUrl(String reqStr, Integer incomeType);

	/**
	 * @Description : 宝付协议支付异步通知
	 * @Method_Name : baofuAgreePayAsyncNotifyUrl;
	 * @param map
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-17 15:32:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> baofuAgreePayAsyncNotifyUrl(Map<String, String> map, Integer incomeType);

	/**
	 * @Description : 连连充值同步通知
	 * @Method_Name : lianlianRechargeSyncNotifyUrl;
	 * @param reqStr
	 *            连连同步通知的信息
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月5日 上午9:47:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> lianlianRechargeSyncNotifyUrl(String reqStr);

	/**
	 * @Description : 宝付网银充值，同步回调
	 * @Method_Name : baofuWyRechargePageNotice;
	 * @param resultMap
	 * @param md5Sign
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月24日 下午5:35:36;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> baofuWyRechargePageNotice(Map<String, String> resultMap, String md5Sign);

	/**
	 * @Description : 宝付网银充值，异步通知
	 * @Method_Name : baofuWyRechargeAsyncNotifyUrl;
	 * @param resultMap
	 * @param md5Sign
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月24日 下午6:19:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> baofuWyRechargeAsyncNotifyUrl(Map<String, String> resultMap, String md5Sign, Integer incomeType);

	/**
	 * @Description : 宝付充值同步通知
	 * @Method_Name : baofuRechargeSyncNotifyUrl;
	 * @param reqStr
	 *            宝付同步通知的信息
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月24日 上午11:25:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> baofuRechargeSyncNotifyUrl(String reqStr);
	
	/**
	 * @Description : 易宝认证支付异步通知
	 * @Method_Name : yeepayRzAsyncNotifyUrl;
	 * @param paramsMap
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-10-12 17:46:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> yeepayRzAsyncNotifyUrl(Map<String, String> paramsMap,Integer incomeType);
	/**
	 * @Description : 易宝网银支付异步通知
	 * @Method_Name : yeepayWyAsyncNotifyUrl;
	 * @param paramsMap
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-10-15 17:46:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> yeepayWyAsyncNotifyUrl(Map<String, String> paramsMap);
	/**
	 * @Description : 易宝网银支付异步通知
	 * @Method_Name : yeepayRushAsyncNotifyUrl;
	 * @param paramStr
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-10-31 16:13:29;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> yeepayRushAsyncNotifyUrl(String paramStr);
}
