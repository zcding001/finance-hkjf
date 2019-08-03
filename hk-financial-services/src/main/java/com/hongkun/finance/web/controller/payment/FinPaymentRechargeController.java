package com.hongkun.finance.web.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.factory.ValidateParamsFactory;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.service.FinBankCardFrontService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.web.util.ValidateParamsUtil;
import com.yirun.framework.core.annotation.Token;
import com.yirun.framework.core.annotation.Token.Ope;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.redis.JedisClusterUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.user.constants.UserConstants.USER_IDENTIFY_NO;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_GENERAL;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Description : 金融支付充值接口
 * @Project : hk-financial-services
 * @Program Name :
 *          com.hongkun.finance.web.controller.payment.FinPaymentController.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@Controller
@RequestMapping("/finPaymentRechargeController")
public class FinPaymentRechargeController {
	private static final Logger logger = LoggerFactory.getLogger(FinPaymentRechargeController.class);
	@Reference
	private FinBankCardFrontService finPaymentService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinPaymentFacade finPaymentFacade;
	
	/**
	 * @Description : 跳转到充值页面
	 * @Method_Name : toReCharge;
	 * @param request
	 * @param response
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月20日 上午10:48:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("toRecharge")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> toRecharge(HttpServletRequest request, HttpServletResponse response) {
		// 验证用户有效性
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		if (regUser.getIdentify() == USER_IDENTIFY_NO) {
			return new ResponseEntity<>(ERROR, "请进行实名认证之后再充值!");
		}
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
		// 初始化充值列表页
		return finPaymentService.toRecharge(SystemTypeEnums.HKJF, PlatformSourceEnums.PC, PayStyleEnum.RECHARGE,
				regUser.getId(), basePath, regUser.getType());
	}

	/**
	 * @Description : 根据输入的卡号，查询属于哪个银行，进行显示
	 * @Method_Name : queryBankCardBin;
	 * @param request
	 * @param response
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年12月20日 上午10:48:52;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("queryBankCardBin")
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> queryBankCardBin(HttpServletRequest request, HttpServletResponse response) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		return finPaymentService.findBankCardBin(request.getParameter("cardNo"), SystemTypeEnums.HKJF,
				PlatformSourceEnums.PC, PayStyleEnum.KBIN,
				PayChannelEnum.getPayChannelEnumByChannelKey(request.getParameter("payChannel")), regUser.getType());
	}

	/**
	 * 
	 * @Description : 账户充值
	 * @Method_Name : clientReChargeCash
	 * @param request
	 * @param response
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月4日 下午2:40:02
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@RequestMapping("accountRecharge")
	@Token(operate = Ope.REFRESH)
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_LOGIN)
	@ResponseBody
	public ResponseEntity<?> accountRecharge(HttpServletRequest request, HttpServletResponse response,
			RechargeCash rechargeCash) {
		RegUser regUser = BaseUtil
				.getRegUser(() -> this.regUserService.findRegUserById(BaseUtil.getLoginUser().getId()));
		logger.info("方法: accountRecharge , 充值操作, 用户标识: {}, 充值入参: rechargeCash: {}, tiedCardFlag: {}", regUser.getId(),
				rechargeCash.toString(), request.getParameter("tiedCardFlag"));
		try {
			RegUserDetail userDetail = BaseUtil
					.getRegUserDetail(() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId()));
			String tiedCardFlag = request.getParameter("tiedCardFlag");// 绑卡状态
			String bankCard = rechargeCash.getBankCard();
			// 如果绑卡标识不为空，并且不等于未绑卡，且支付方式不是网银支付，则从缓存中获取加密后对应的明文银行卡号
			if (StringUtils.isNotBlank(tiedCardFlag)
					&& Integer.parseInt(tiedCardFlag) != (TradeStateConstants.BANK_CARD_STATE_INIT)
					&& !PayStyleEnum.WY.getType().equals(rechargeCash.getPayStyle())) {
				bankCard = JedisClusterUtils.get(bankCard);
			}
			// 组装支付VO
			rechargeCash.setBankCard(bankCard);// 银行卡号
			rechargeCash.setUserId(regUser.getId());// 用户ID
			rechargeCash.setUserName(userDetail.getRealName());// 用户真实姓名
			rechargeCash.setTel(String.valueOf(regUser.getLogin()));// 手机号
			rechargeCash.setIdCard(userDetail.getIdCard());// 身份证号码
			// 判断用户类型（企业用户和物业用户统称为企业用户类型，一般用户为普通用户类型）
			String userType = String.valueOf((regUser.getType() == UserConstants.USER_TYPE_ENTERPRISE
					|| regUser.getType() == UserConstants.USER_TYPE_TENEMENT) ? UserConstants.USER_TYPE_ENTERPRISE
							: UserConstants.USER_TYPE_GENERAL);
			rechargeCash.setuType(Integer.valueOf(userType));// 用户类型
			if(regUser.getType() != UserConstants.USER_TYPE_GENERAL){
				rechargeCash.setPayStyle(PayStyleEnum.QYWY.getType());
			}
			// 入参校验
			ResponseEntity<?> validateResult = ValidateParamsUtil.rechargeValidate(rechargeCash, regUser);
			if (validateResult.getResStatus() == SUCCESS) {
				// 调用充值接口进行充值
				ResponseEntity<?> resultRes = finConsumptionService.rechargeCash(rechargeCash);
				if (resultRes.getResStatus() == SUCCESS) {
					return resultRes;
				}
				logger.error("充值操作, 用户标识: {}, 用户发起充值失败: {}", regUser.getId(), resultRes.getResMsg().toString());
			}
			logger.error("充值操作, 用户标识: {}, 用户发起充值参数校验失败: {}", regUser.getId(), validateResult.getResMsg().toString());
			return new ResponseEntity<>(ERROR, "充值失败！");
		} catch (Exception e) {
			logger.error("充值操作, 用户标识: {}, 用户发起充值失败: ", regUser.getId(), e);
			return new ResponseEntity<>(ERROR, "充值失败！");
		}
	}
	/**
	 * @Description : 收银台短信验证码
	 * @Method_Name : toRecharge;
	 * @param rechargeCash
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-14 14:34:59;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/paymentVerificationCode")
	@ResponseBody
	public ResponseEntity<?> paymentVerificationCode(RechargeCash rechargeCash, HttpServletRequest request) {
		logger.info("方法 paymentVerificationCode，收银台短验，入参rechargeCash：{}", rechargeCash);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (regUser == null) {
			return new ResponseEntity<>(ERROR, "请先登录！");
		}
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		rechargeCash.setUserId(regUser.getId());
		rechargeCash.setUserName(userDetail.getRealName());
		rechargeCash.setLoginTel(String.valueOf(regUser.getLogin()));
		rechargeCash.setIdCard(userDetail.getIdCard());
		rechargeCash.setPlatformSourceName(
				PlatformSourceEnums.platFormTypeByValue(Integer.parseInt(rechargeCash.getPlatformSourceName())));
		rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt(rechargeCash.getPayChannel())));
		rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
		rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
		rechargeCash.setuType(regUser.getType());
		rechargeCash.setRegisterTime(regUser.getCreateTime());
		// 充值必须实名认证
		if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
			return new ResponseEntity<>(ERROR, "充值必须实名！");
		}
		// 充值入参校验
		ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
		if (validateResult.getResStatus() == Constants.ERROR) {
			return new ResponseEntity<>(ERROR, validateResult.getResMsg().toString());
		}
		ResponseEntity<?> resultEntity = finPaymentFacade.paymentVerificationCode(rechargeCash);
		logger.info("方法 paymentVerificationCode, 收银台短验 返回, resultEntity {}", resultEntity.getResMsg());
		return resultEntity;
	}
	/**
	 * @Description : 协议充值
	 * @Method_Name : agreementRecharge;
	 * @param rechargeCash
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-05-15 16:51:23;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/agreementRecharge")
	@ResponseBody
	public ResponseEntity<?> agreementRecharge(RechargeCash rechargeCash, HttpServletRequest request) {
		logger.info("方法 agreementRecharge，协议支付，入参rechargeCash：{}", rechargeCash);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		rechargeCash.setUserId(regUser.getId());
		rechargeCash.setUserName(userDetail.getRealName());
		rechargeCash.setLoginTel(String.valueOf(regUser.getLogin()));
		rechargeCash.setIdCard(userDetail.getIdCard());
		rechargeCash.setPlatformSourceName(
				PlatformSourceEnums.platFormTypeByValue(Integer.parseInt(rechargeCash.getPlatformSourceName())));
		rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt(rechargeCash.getPayChannel())));
		rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
		rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
		rechargeCash.setuType(regUser.getType());
		// 充值必须实名认证
		if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
			return new ResponseEntity<>(ERROR, "充值必须实名！");
		}
		logger.info("paymentRechargeController agreementRecharge rechargeCash:{}", rechargeCash);
		// 充值入参校验
		ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
		if (validateResult.getResStatus() == Constants.ERROR) {
			return new ResponseEntity<>(ERROR,validateResult.getResMsg().toString());
		}
		ResponseEntity<?> resultEntity  = finPaymentFacade.confirmPay(rechargeCash);
		logger.info("agreementRecharge resultEntity {}", resultEntity.getResMsg());
		return resultEntity;
	}

}
