
/**
 * 
 */
package com.hongkun.finance.web.util;

import static com.hongkun.finance.user.constants.UserConstants.USER_IDENTIFY_NO;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_GENERAL;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.user.model.RegUser;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;

/**
 * @Description : 入参校验工具类
 * @Project : hk-financial-services
 * @Program Name : com.hongkun.finance.web.util.ValidateParamsUtil.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class ValidateParamsUtil {

	/**
	 * @Description : 充值数据校验
	 * @Method_Name : rechargeValidate
	 * @param rechargeCash
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月6日 下午2:48:54
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public static ResponseEntity<?> rechargeValidate(RechargeCash rechargeCash, RegUser regUser) throws Exception {

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
		// 充值必须实名认证
		if (rechargeCash.getuType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
			return new ResponseEntity<>(Constants.ERROR, "充值必须实名！");
		}
		// 网银充值
		if (PayStyleEnum.WY.getType().equalsIgnoreCase(payStyle) && StringUtils.isBlank(bankCode)
				&& (StringUtils.isBlank(bankCard) || StringUtils.isBlank(thirdAccount))) {
			return new ResponseEntity<>(Constants.ERROR, "银行选择错误，请重新选择！");
		}
		// 认证绑卡充值（未绑卡）
		if (PayStyleEnum.RZ.getType().equalsIgnoreCase(payStyle) && StringUtils.isBlank(bankCard)) {
			return new ResponseEntity<>(Constants.ERROR, "银行卡号不能为空，请重新绑卡！");
		}
		// 快捷支付
		if (PayStyleEnum.KJ.getType().equalsIgnoreCase(payStyle) && StringUtils.isNotBlank(bankCard)) {
			return new ResponseEntity<>(Constants.ERROR, "银行选择错误，请重新选择！");
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

}
