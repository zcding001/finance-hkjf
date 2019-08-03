package com.hongkun.finance.payment.facade;

import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;

public interface FinPaymentFacade {
	/**
	 * @Description :提现申请业务处理
	 * @Method_Name : clientWithDrawFacade;
	 * @param transAmt
	 *            提现金额
	 * @param regUserId
	 *            用户Id
	 * @param platformSourceEnums
	 *            平台来源
	 * @param 卡券ID
	 * @param systemTypeEnums
	 *            系统类型
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月4日 上午10:42:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> clientWithDrawFacade(String transAmt, Integer regUserId, PlatformSourceEnums platformSourceEnums,
			Integer couponDetailId, SystemTypeEnums systemTypeEnums);

	/**
	 * @Description : 获得提现页面所需要的数据，包括用户绑卡数量和提现券数量
	 * @Method_Name : getDataForWithDrawPage
	 * @Date : 2017/12/6 11:28
	 * @Author : ruilima@hongkun.com.cn 马瑞丽
	 * @param regUserId
	 * @return
	 * @throws Exception
	 */
	ResponseEntity<?> getDataWithDrawPageForWeb(int regUserId) throws Exception;

	/**
	 * 获取提现页面所需要的数据，包括用户绑定的银行卡和提现券数量
	 * 
	 * @param regUserId
	 * @return
	 * @throws Exception
	 */
	ResponseEntity<?> getDataWithDrawPageForApp(int regUserId) throws Exception;

	/**
	 * @Description : 投资的时候选择付款方式接口
	 * @Method_Name : toChooseInvestAccount;
	 * @param regUserId
	 *            用户ID
	 * @param platformSource
	 *            平台来源 10-PC 11-IOS 12-ANDRIOD 13-WAP
	 * @param userType
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月13日 下午2:09:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> toChooseInvestAccount(Integer regUserId, Integer platformSource, Integer userType);
	
	/**
	 * @Description : 收银台发送短信验证码（预绑卡、预支付）
	 * @Method_Name : paymentVerificationCode;
	 * @param rechargeCash
	 *            发送对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-14 17:22:38;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> paymentVerificationCode(RechargeCash rechargeCash);

	/**
	 * @Description : 确认支付（收银台充值确认按钮功能）
	 * @Method_Name : confirmPay;
	 * @param rechargeCash
	 *            发送对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-09-28 17:15:26;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	ResponseEntity<?> confirmPay(RechargeCash rechargeCash);
	/**     
	 *  @Description    : 查询充值信息
	 *  @Method_Name    : searchRechargeInfo;
	 *  @param regUserId
	 *  @return
	 *  @return         : ResponseEntity<?>;
	 *  @Creation Date  : 2018年11月14日 下午4:01:32;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	ResponseEntity<?> searchRechargeInfo(Integer regUserId);
}
