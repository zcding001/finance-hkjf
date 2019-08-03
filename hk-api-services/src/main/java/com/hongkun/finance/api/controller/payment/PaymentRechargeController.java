package com.hongkun.finance.api.controller.payment;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.factory.ValidateParamsFactory;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.support.security.OperationTypeEnum;
import com.hongkun.finance.user.support.security.component.annotation.AskForPermission;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.AppResultUtil.ExtendMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.payment.constant.PaymentConstants.BAOFU_RZ_PAY_CHECK_STATE_SUCCESS;
import static com.hongkun.finance.payment.constant.TradeStateConstants.TRANSIT_FAIL;
import static com.hongkun.finance.user.constants.UserConstants.*;

/**
 * 支付充值Controller
 * 
 * @author yanbinghuang
 *
 */
@Controller
@RequestMapping("/paymentRechargeController")
public class PaymentRechargeController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentRechargeController.class);

	@Reference
	private FinBankCardFrontService finBankCardFrontService;
	@Reference
	private FinBankReferService finBankReferService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinChannelGrantService finChannelGrantService;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinPayConfigService finPayConfigService;
	@Reference
	private FinPaymentFacade finPaymentFacade;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private RegCompanyInfoService regCompanyInfoService;

	/**
	 * @Description :根据银行卡号，支付渠道，查询银行卡BIN
	 * @Method_Name : searchBankCardBin;
	 * @param cardNo
	 *            银行卡号
	 * @param payChannel
	 *            支付渠道 1-lianlian；2-liandong；3-baofu
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 下午1:54:15;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchBankCardBin")
	@ResponseBody
	public Map<String, Object> searchBankCardBin(@RequestParam(value = "cardNo", required = false) String cardNo,
			@RequestParam(value = "payChannel", required = false) String payChannel) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		ResponseEntity<?> result = finBankCardFrontService.findBankCardBin(cardNo, SystemTypeEnums.HKJF,
				PlatformSourceEnums.PC, PayStyleEnum.KBIN,
				PayChannelEnum.getPayChannelEnumByCode(Integer.parseInt(payChannel == null ? "-1" : payChannel)),
				regUser.getType());
		return AppResultUtil.mapOfResponseEntity(result);
	}

	/***
	 * @Description : 根据支付渠道查询渠道下支持的银行列表
	 * @Method_Name : searchBankCardList;
	 * @param payChannel
	 *            支付渠道 1- lianlian ；2- liandong ；3- baofu
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 下午5:09:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchBankCardList")
	@ResponseBody
	@AskForPermission(operation = OperationTypeEnum.DISCRIMINATE_NO_LOGIN)
	public ModelAndView searchBankCardList(HttpServletRequest request, @RequestParam("payChannel") String payChannel) {
		/** 默认一般账户 **/
		String userType = String.valueOf(UserConstants.USER_TYPE_GENERAL);
		ModelAndView mav = new ModelAndView("bank/bankCardList");
		List<FinBankRefer> bankReferList = finBankReferService.findBankInfo(PayChannelEnum
				.getPayChannelEnumByCode(Integer.parseInt(payChannel == null ? "-1" : payChannel)).getChannelKey(),
				PayStyleEnum.RZ.getType(), userType);
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
		bankReferList.stream().forEach((e) -> {
			e.setBankIconAddress(e.getBankCode() + ".png");
			e.setSingleLimit(e.getSingleLimit().divide(BigDecimal.valueOf(10000)));
			e.setSingleDayLimit(e.getSingleDayLimit().divide(BigDecimal.valueOf(10000)));
		});
		mav.addObject("basePath", basePath);
		mav.addObject("bankReferList", bankReferList);
		return mav;

	}

	/**
	 * @Description : 查询当前的支付渠道
	 * @Method_Name : searchPayChannelList;
	 * @param payStyle
	 *            支付方式
	 * @param platformNource
	 *            交易来源 10-PC 11-IOS 12-ANDRIOD 13-WAP
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月8日 下午6:31:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/searchPayChannelList")
	@ResponseBody
	public Map<String, Object> searchPayChannelList(
			@RequestParam(value = "payStyle", required = false) Integer payStyle,
			@RequestParam(value = "platformSource", required = false) Integer platformSource) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		// 查询当前启用的支付渠道，以及用户绑定的银行列表
		ResponseEntity<?> result = finBankCardFrontService.findBingBankCardInfo(
				PlatformSourceEnums.typeByValue(platformSource), PayStyleEnum.payStyleEnumByValue(payStyle),
				regUser.getId());
		// 过滤返回的字段值
		ExtendMap channelMap = AppResultUtil.successOfListInProperties(
				(List<FinChannelGrant>) result.getParams().get("payChannelList"), "查询成功", "channelName"/** 支付渠道名称 **/
				, "channelNameCode"/** 支付渠道编码 **/
				, "sort");
		ExtendMap bankCardMap = AppResultUtil.successOfListInProperties(
				(List<FinBankCard>) result.getParams().get("bankCardlList"), "查询成功", "bankCode"/** 银行编码 **/
				, "bankName"/** 银行名称 **/
				, "bankCard"/** 银行卡号 **/
				, "state"/** 卡的绑定状态 **/
		);
		return AppResultUtil.successOfMsg("查询成功")
				.addResParameter("payChannelList", channelMap.get(AppResultUtil.DATA_LIST))
				.addResParameter("bankCardlList", bankCardMap.get(AppResultUtil.DATA_LIST));
	}

	/**
	 * @Description :更新支付订单状态
	 * @Method_Name : updateOrderState;
	 * @param orderId
	 *            订单号
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月9日 上午9:12:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/updateOrderState")
	@ResponseBody
	public Map<String, Object> updateOrderState(@RequestParam(value = "orderId", required = false) String orderId) {
		logger.info("方法: updateOrderState, 入参: orderId: {}", orderId);
		try {
			// 判断用户订单是否存在
			FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(orderId);
			if (finPaymentRecord == null) {
				return AppResultUtil.errorOfMsg("没有查询到此订单:" + orderId);
			}
			if (finPaymentRecord.getState() != TradeStateConstants.PENDING_PAYMENT){
				return AppResultUtil.errorOfMsg("此订单状态不能置为失败:" + orderId);
			}
			// 更新用户订单状态为失败
			FinPaymentRecord paymentRecord = new FinPaymentRecord();
			paymentRecord.setFlowId(orderId);
			paymentRecord.setState(TRANSIT_FAIL);
			finPaymentRecordService.updateByFlowId(paymentRecord);
		} catch (Exception e) {
			logger.error("订单标识: {}, 更新用户支付订单状态失败: ", orderId, e);
			return AppResultUtil.errorOfMsg("更新订单失败:" + orderId);
		}
		return AppResultUtil.successOfMsg("处理成功");
	}

	/**
	 * @Description : 客户端充值
	 * @Method_Name : toRecharge;
	 * @param rechargeCash
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月9日 上午11:35:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/toRecharge")
	@ResponseBody
	public Map<String, Object> toRecharge(RechargeCash rechargeCash, HttpServletRequest request) {
		logger.info("方法: toRecharge, 入参: rechargeCash: {}", rechargeCash);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		// 充值参数验签
		boolean result = ValidateParamsFactory.checkSign(rechargeCash, request.getParameter(Constants.SESSION_ID_KEY));
		if (result == false) {
			return AppResultUtil.errorOfMsg("签名验签失败！");
		}
		rechargeCash.setUserId(regUser.getId());
		rechargeCash.setUserName(userDetail.getRealName());
		rechargeCash.setTel(String.valueOf(regUser.getLogin()));
		rechargeCash.setIdCard(userDetail.getIdCard());
		rechargeCash.setPlatformSourceName(
				PlatformSourceEnums.platFormTypeByValue(Integer.parseInt(rechargeCash.getPlatformSourceName())));
		rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt(rechargeCash.getPayChannel())));
		rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
		rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
		rechargeCash.setuType(regUser.getType());
		// 充值必须实名认证
		if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
			return AppResultUtil.errorOfMsg("充值必须实名!");
		}
		// 充值入参校验
		ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
		if (validateResult.getResStatus() == Constants.ERROR) {
			return AppResultUtil.errorOfMsg(validateResult.getResMsg().toString());
		}
		// 充值组装数据
		ResponseEntity<?> responseEntity = finConsumptionService.rechargeCash(rechargeCash);
		if (responseEntity.getResStatus() == Constants.ERROR) {
			return AppResultUtil.errorOfMsg(responseEntity.getResMsg().toString());
		}
		Map<String, Object> resultMap = (Map<String, Object>) responseEntity.getResMsg();
		Map<String, Object> results = (Map<String, Object>) resultMap.get("submitForm");
		// 如果是宝付支付，则根据返回的retCode,特殊处理
		if (PayChannelEnum.BaoFu.getChannelKey().equals(rechargeCash.getPayChannel())) {
			if (!BAOFU_RZ_PAY_CHECK_STATE_SUCCESS.equals(results.get("retCode").toString())) {
				return AppResultUtil.errorOfMsg(results.get("retMsg").toString());
			}
		}
		return AppResultUtil.successOf(results);
	}

	/**
	 * @Description : 协议支付-收银台短信验证码
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
	public Map<String, Object> paymentVerificationCode(RechargeCash rechargeCash, HttpServletRequest request) {
		logger.info("方法 paymentVerificationCode，收银台短验，入参rechargeCash：{}", rechargeCash);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		// 充值参数验签
		boolean result = ValidateParamsFactory.checkVerificationCodeSign(rechargeCash,
				request.getParameter(Constants.SESSION_ID_KEY));
		if (result == false) {
			logger.error("方法 paymentVerificationCode,收银台短验,签名验签失败");
			return AppResultUtil.errorOfMsg("签名验签失败！");
		}
		rechargeCash.setUserId(regUser.getId());
		rechargeCash.setUserName(userDetail.getRealName());
		rechargeCash.setLoginTel(String.valueOf(regUser.getLogin()));
		rechargeCash.setIdCard(userDetail.getIdCard());
		rechargeCash.setPlatformSourceName(
				PlatformSourceEnums.platFormTypeByValue(Integer.parseInt(rechargeCash.getPlatformSourceName())));
		rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt(rechargeCash.getPayChannel())));
		rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
		rechargeCash.setSystemTypeName(SystemTypeEnums.HKJF.getType());
		rechargeCash.setuType(1);
		rechargeCash.setRegisterTime(regUser.getCreateTime());
		// 充值必须实名认证
		if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
			return AppResultUtil.errorOfMsg("充值必须实名!");
		}
		// 充值入参校验
		ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
		if (validateResult.getResStatus() == Constants.ERROR) {
			return AppResultUtil.errorOfMsg(validateResult.getResMsg().toString());
		}
		ResponseEntity<?> resultEntity = finPaymentFacade.paymentVerificationCode(rechargeCash);
		logger.info("方法 paymentVerificationCode, 收银台短验 返回, resultEntity {}", resultEntity.getResMsg());
		return AppResultUtil.mapOfResponseEntity(resultEntity);
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
	public Map<String, Object> agreementRecharge(RechargeCash rechargeCash, HttpServletRequest request) {
		logger.info("方法 agreementRecharge，协议支付，入参rechargeCash：{}", rechargeCash);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		// 充值参数验签
		boolean result = ValidateParamsFactory.checkAgreementPaySign(rechargeCash,
				request.getParameter(Constants.SESSION_ID_KEY));
		if (result == false) {
			logger.error("方法 agreementRecharge,协议支付,签名验签失败");
			return AppResultUtil.errorOfMsg("签名验签失败！");
		}
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
			return AppResultUtil.errorOfMsg("充值必须实名!");
		}
		logger.info("paymentRechargeController agreementRecharge rechargeCash:{}", rechargeCash);
		// 充值入参校验
		ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
		if (validateResult.getResStatus() == Constants.ERROR) {
			return AppResultUtil.errorOfMsg(validateResult.getResMsg().toString());
		}
		ResponseEntity<?> resultEntity = finPaymentFacade.confirmPay(rechargeCash);
		logger.info("agreementRecharge resultEntity {}", resultEntity.getResMsg());
		return AppResultUtil.mapOfResponseEntity(resultEntity);
	}

	/**
	 * @Description : 投资选择付款方式
	 * @Method_Name : toChooseInvestAccount;
	 * @param platformSource
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年3月13日 下午4:05:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/toChooseInvestAccount")
	@ResponseBody
	public Map<String, Object> toChooseInvestAccount(
			@RequestParam(value = "platformSource", required = false) Integer platformSource) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		return AppResultUtil.mapOfResponseEntity(finPaymentFacade.toChooseInvestAccount(regUser.getId(), platformSource,
                regUser.getType()))
				.addResParameter("userName"/** 用户姓名 **/
						, userDetail.getRealName())
				.addResParameter("idCard"/** 身份证号 **/
						, userDetail.getIdCard());
	}

	/**
	 * @Description : 投资点击充值获取充值信息
	 * @Method_Name : searchRechargeInfo;
	 * @param payStyle
	 * @param platformSource
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月11日 下午4:06:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/searchRechargeInfo")
	@ResponseBody
	public Map<String, Object> searchRechargeInfo() {
		try {
			RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
					() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
			RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
			String userName = userDetail.getRealName();
			if (regUser.getType() != UserConstants.USER_TYPE_GENERAL) {
				RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(regUser.getId());
				if (regCompanyInfo == null || StringUtils.isBlank(regCompanyInfo.getEnterpriseName())) {
					return AppResultUtil.errorOfMsg("请维护企业名称");
				} else {
					userName = regCompanyInfo.getEnterpriseName().length() <= 3 ? regCompanyInfo.getEnterpriseName()
							: ("***" + regCompanyInfo.getEnterpriseName().substring(3));
				}
			}
	        return AppResultUtil.mapOfResponseEntity(finPaymentFacade.searchRechargeInfo(regUser.getId()))
					.addResParameter("identify"/** 实名状态:0-未实名,1-已实名 **/
							, regUser.getIdentify())
					.addResParameter("userName"/** 用户姓名 **/
							, userName);
		} catch (Exception e) {
			logger.error("投资点击充值获取充值信息失败: ", e);
			return AppResultUtil.errorOfMsg("系统繁忙请稍候再试");
		}
	}

	/**
	 * @Description : 未绑卡时，通过卡号查询银行卡BIN及银行信息
	 * @Method_Name : findBankInfoByCardNo;
	 * @param systemCode
	 *            系统类型
	 * @param cardNo
	 *            银行卡号
	 * @param payChannel
	 *            支付渠道 支付渠道 1- lianlian ；2- liandong ；3- baofu 4-baofuprotocol
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月12日 上午10:09:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findBankInfoByCardNo")
	@ResponseBody
	public Map<String, Object> findBankInfoByCardNo(HttpServletRequest request,
			@RequestParam(value = "systemCode", required = false) String systemCode,
			@RequestParam(value = "cardNo", required = false) String cardNo,
			@RequestParam(value = "payChannel", required = false) String payChannel) {
		logger.info("方法: findBankInfoByCardNo, 未绑卡时通过卡号查询银行卡BIN及银行信息, 入参: systemCode: {}, cardNo: {}, payChannel: {}",
				systemCode, cardNo, payChannel);
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (regUser.getType() == USER_TYPE_ENTERPRISE || regUser.getType() == USER_TYPE_TENEMENT || regUser.getType() == USER_TYPE_ESCROW_ACCOUNT) {
			return AppResultUtil.errorOfMsg("暂不支持更改卡号，请联系客服!");
		}
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		// 注意，通过卡BIN查询银行信息时，因为卡BIN不区分平台来源，所以默认是PC渠道的
		ResponseEntity<?> result = finBankCardFrontService.findBankInfoByCardNo(cardNo,
				SystemTypeEnums.getSystemTypeEnumsByValue(Integer.parseInt(systemCode)), PlatformSourceEnums.PC,
				PayStyleEnum.KBIN,
				PayChannelEnum.getPayChannelEnumByCode(Integer.parseInt(payChannel == null ? "-1" : payChannel)),
				regUser.getType(), regUser.getId());
		return AppResultUtil.mapOfResponseEntity(result)
				.addResParameter("userName"/** 用户姓名 **/
						, userDetail.getRealName())
				.addResParameter("basePath", request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath() + "/");
	}

	/***
	 * @Description : 已绑卡的，通过支付渠道获取银行信息
	 * @Method_Name : findBanInfoByPayChannel;
	 * @param request
	 * @param payChannel
	 *            支付渠道
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月12日 上午11:48:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findBanInfoByPayChannel")
	@ResponseBody
	public Map<String, Object> findBanInfoByPayChannel(HttpServletRequest request,
			@RequestParam(value = "payChannel", required = false) String payChannel) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (regUser.getType() != UserConstants.USER_TYPE_GENERAL) {
			return AppResultUtil.errorOfMsg("企业用户暂不支持充值!");
		}
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		ResponseEntity<?> result = finBankCardFrontService.findBankInfo(
				PayChannelEnum.getPayChannelEnumByCode(Integer.parseInt(payChannel == null ? "-1" : payChannel)),
				regUser.getType(), regUser.getId());
		ExtendMap bankCardMap = AppResultUtil.successOfListInProperties(
				(List<BankCardVo>) result.getParams().get("bankList"), "查询成功", "bankCode"/** 银行编码 **/
				, "bankName"/** 银行名称 **/
				, "bankCard"/** 银行卡号 **/
				, "finBankCardId"/** 银行卡ID **/
				, "state"/** 卡的绑定状态 **/
				, "singleLimit"/** 单笔限额 **/
				, "singleDayLimit"/** 单日限额 **/
				, "singleMonthLimit"/** 单月限额 **/
				, "bankIconAddress"/** 银行图标地址 **/
		);
		return AppResultUtil.successOfMsg("查询成功").addResParameter("bankList", bankCardMap.get(AppResultUtil.DATA_LIST))
				.addResParameter("userName"/** 用户姓名 **/
						, userDetail.getRealName())
				.addResParameter("userAbleMoney"/** 可用余额 **/
						, result.getParams().get("userAbleMoney"))
				.addResParameter("basePath", request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath() + "/");
	}

	/**
	 * @Description :根据系统类型，平台来源，支付方式查询支付渠道
	 * @Method_Name : findPayChannel;
	 * @param request
	 * @param systemCode
	 *            系统类型SystemTypeEnums
	 * @param platformSource
	 *            平台来源PlatformSourceEnums 10-PC 11-IOS 12-ANDRIOD 13-WAP
	 * @param payStyle
	 *            支付方式 PayStyleEnum 10 充值 14提现
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月14日 上午9:27:29;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/findPayChannel")
	@ResponseBody
	public Map<String, Object> findPayChannel(HttpServletRequest request,
			@RequestParam(value = "systemCode", required = false) String systemCode,
			@RequestParam(value = "platformSource", required = false) String platformSource,
			@RequestParam(value = "payStyle", required = false) String payStyle) {
		// 查询启用的支付渠道
		ResponseEntity<?> result = finBankCardFrontService.findPayChannel(
				SystemTypeEnums.getSystemTypeEnumsByValue(Integer.parseInt(systemCode)),
				PlatformSourceEnums.typeByValue(Integer.parseInt(platformSource)),
				PayStyleEnum.payStyleEnumByValue(Integer.parseInt(payStyle)));
		if (result.getResStatus() == Constants.ERROR) {
			return AppResultUtil.errorOfMsg(result.getResMsg());
		}
		// 过滤返回的字段值
		ExtendMap channelMap = AppResultUtil.successOfListInProperties(
				(List<FinChannelGrant>) result.getParams().get("payChannelList"), "查询成功", "channelName"/** 支付渠道名称 **/
				, "channelNameCode"/** 支付渠道编码 **/
				, "sort");
		return AppResultUtil.successOfMsg("查询成功").addResParameter("payChannelList",
				channelMap.get(AppResultUtil.DATA_LIST));
	}

	/**
	 * @Description :跳转到收银台页面
	 * @Method_Name : toCashDesk;
	 * @param rechargeCash
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018年5月15日 上午9:07:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@RequestMapping("/toCashDesk")
	@ResponseBody
	public Map<String, Object> toCashDesk(HttpServletRequest request, RechargeCash rechargeCash) {
		RegUser regUser = BaseUtil.getRegUser(BaseUtil.getLoginUserId(),
				() -> this.regUserService.findRegUserById(BaseUtil.getLoginUserId()));
		if (regUser.getType() != UserConstants.USER_TYPE_GENERAL) {
			return AppResultUtil.errorOfMsg("企业用户暂不支持充值!");
		}
		RegUserDetail userDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUser.getId());
		try {
			// 充值参数验签
			boolean result = ValidateParamsFactory.checkCashDeskSign(rechargeCash,
					request.getParameter(Constants.SESSION_ID_KEY));
			if (result == false) {
				logger.error("跳转到收银台页面, 签名验签失败！");
				return AppResultUtil.errorOfMsg("该银行维护中，暂不支持！");
			}
			rechargeCash.setUserId(regUser.getId());
			rechargeCash.setUserName(userDetail.getRealName());
			rechargeCash.setTel(String.valueOf(regUser.getLogin()));
			rechargeCash.setIdCard(userDetail.getIdCard());
			// 注意，通过卡BIN查询银行信息时，因为卡BIN不区分平台来源，所以默认是PC渠道的,该方法中其它查询不依赖此平台来源
			rechargeCash.setPlatformSourceName(PlatformSourceEnums.PC.getType());
			rechargeCash.setPayChannel(PayChannelEnum.fromChannelCode(Integer.parseInt(rechargeCash.getPayChannel())));
			rechargeCash.setPayStyle(PayStyleEnum.RZ.getType());
			rechargeCash.setSystemTypeName(
					SystemTypeEnums.sysTypeByValue(Integer.parseInt(rechargeCash.getSystemTypeName())));
			rechargeCash.setuType(regUser.getType());
			// 充值必须实名认证
			if (regUser.getType() == USER_TYPE_GENERAL && regUser.getIdentify() == USER_IDENTIFY_NO) {
				return AppResultUtil.errorOfMsg("请完成实名后再充值!");
			}
			// 充值入参校验
			ResponseEntity<?> validateResult = ValidateParamsFactory.rechargeValidate(rechargeCash);
			if (validateResult.getResStatus() == Constants.ERROR) {
				return AppResultUtil.errorOfMsg(validateResult.getResMsg().toString());
			}
			// 初始化组装充值页面信息
			ResponseEntity<?> resultMap = finBankCardFrontService.initCashDeshPage(rechargeCash);
			return AppResultUtil.mapOfResponseEntity(resultMap).addResParameter("idCard", userDetail.getIdCard())
					.addResParameter("basePath", request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort() + request.getContextPath() + "/");
		} catch (Exception e) {
			logger.error("跳转到收银台页面, 用户Id: {}, 初始化数据失败:", regUser.getId(), e);
			return AppResultUtil.errorOfMsg("系统繁忙请稍候再试");
		}
	}

}
