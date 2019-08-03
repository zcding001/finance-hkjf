package com.hongkun.finance.payment.service;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;

/**
 * @Description : 银行卡相关操作接口服务
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.FinBankCardFrontService.java
 * @Author : maruili on 2017/12/28 11:14
 */
public interface FinBankCardFrontService {
	/**
	 * @Description : 用户充值，组装充值页面数据
	 * @Method_Name : toRecharge;
	 * @param systemTypeEnums
	 *            系统名称
	 * @param platformSourceEnums
	 *            平台名称
	 * @param payStyleEnum
	 *            支付方式
	 * @param regUserId
	 *            用户ID
	 * @param userType
	 *            用户类型
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月7日 下午3:46:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> toRecharge(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum, Integer regUserId, String basePath, Integer userType);

	/**
	 * @Description : 用户绑定银行卡
	 * @Method_Name : bindBankCard
	 * @Date : 2017/12/28 13:58
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param finBankCard
	 * @throws Exception
	 */
	ResponseEntity<?> bindBankCard(FinBankCard finBankCard, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, RegUser regUser) throws Exception;

	/**
	 * @Description : 用户修改银行卡信息
	 * @Method_Name : updateBankCard
	 * @Date : 2017/12/28 13:58
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param finBankCard
	 * @throws Exception
	 */
	ResponseEntity<?> updateBankCard(FinBankCard finBankCard) throws Exception;

	/**
	 * @Description :根据卡号和支付渠道查询对应渠道下的银行卡BIN信息
	 * @Method_Name : findBankCardBin;
	 * @param cardNo
	 *            银行卡号
	 * @param systemTypeEnums
	 *            系统类型
	 * @param platformSourceEnums
	 *            平台类型
	 * @param payStyleEnum
	 *            支付方式
	 * @param payChannelEnum
	 *            支付渠道
	 * @param userType
	 *            用户类型
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月28日 下午3:26:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findBankCardBin(String cardNo, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, PayChannelEnum payChannelEnum,
			Integer userType);

	/**
	 * @Description : 查询用户绑定的银行卡信息
	 * @Method_Name : findBingBankCardInfo;
	 * @param platformSourceEnums
	 *            平台来源
	 * @param payStyleEnum
	 *            支付方式
	 * @param regUserId
	 *            用户Id
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月9日 下午3:43:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findBingBankCardInfo(PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum,
			Integer regUserId);

	/**
	 * 查询优先级最高支付渠道
	 * 
	 * @param systemTypeEnums
	 * @param platformSourceEnums
	 * @param payStyleEnum
	 * @return
	 */
	FinChannelGrant findFirstFinChannelGrant(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum);

	/**
	 * @Description :投资时点击充值查询充值信息
	 * @Method_Name : findRechargeInfo;
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月11日 下午4:15:54;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findRechargeInfo(Integer regUserId);

	/**
	 * @Description :查询银行卡BIN及银行信息通过银行卡号
	 * @Method_Name : findBankInfoByCardNo;
	 * @param cardNo
	 *            银行卡号
	 * @param systemTypeEnums
	 *            系统名称
	 * @param platformSourceEnums
	 *            平台来源
	 * @param payStyleEnum
	 *            支付方式
	 * @param payChannelEnum
	 *            支付渠道
	 * @param userType
	 *            用户类型
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月12日 上午10:15:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findBankInfoByCardNo(String cardNo, SystemTypeEnums systemTypeEnums,
			PlatformSourceEnums platformSourceEnums, PayStyleEnum payStyleEnum, PayChannelEnum payChannelEnum,
			Integer userType, Integer regUserId);

	/**
	 * @Description : 查询银行信息
	 * @Method_Name : findBankInfo;
	 * @param payChannelEnum
	 *            支付渠道
	 * @param userType
	 *            用户类型
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月12日 上午11:51:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findBankInfo(PayChannelEnum payChannelEnum, Integer userType, Integer regUserId);

	/**
	 * @Description :根据系统类型，平台来源，支付方式查询支付渠道
	 * @Method_Name : findPayChannel;
	 * @param systemTypeEnums
	 * @param platformSourceEnums
	 * @param payStyleEnum
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月14日 上午9:16:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> findPayChannel(SystemTypeEnums systemTypeEnums, PlatformSourceEnums platformSourceEnums,
			PayStyleEnum payStyleEnum);

	/**
	 * @Description : 初始化收银台页面信息
	 * @Method_Name : initCashDeshPage;
	 * @param rechargeCash
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月15日 下午3:12:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> initCashDeshPage(RechargeCash rechargeCash);

}
