package com.hongkun.finance.payment.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentFacade;
import com.hongkun.finance.payment.factory.PayValidateFactory;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.CreateFlowUtil;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.model.QdzVasRuleItem;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.umpay.api.util.StringUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.AppResultUtil;
import com.yirun.framework.core.utils.AppResultUtil.ExtendMap;
import com.yirun.framework.core.utils.ValidateResponsEntityUtil;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.yirun.framework.core.commons.Constants.*;

@Service
public class FinPaymentFacadeImpl implements FinPaymentFacade {
	private static final Logger logger = LoggerFactory.getLogger(FinPaymentFacadeImpl.class);
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private VasCouponDetailService couponDetailService;
	@Reference
	private FinBankCardBindingService finBankCardBindingService;
	@Reference
	private FinChannelGrantService finChannelGrantService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private FinBankReferService finBankReferService;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private RosInfoService rosInfoService;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> clientWithDrawFacade(String transAmt, Integer regUserId,
			PlatformSourceEnums platformSourceEnums, Integer couponDetailId, SystemTypeEnums systemTypeEnums) {
		logger.info(
				"clientWithDrawFacade, 提现申请, transAmt: {}, regUserId: {}, platformSourceEnums: {}, couponDetailId: {}, systemTypeEnums: {}",
				transAmt, regUserId, platformSourceEnums, couponDetailId, systemTypeEnums);
		//提现申请黑名单
		boolean isBlackUser = rosInfoService.validateRoster(regUserId,
				RosterType.getRosterType(RosterType.WITHDRAW.getValue()),
				RosterFlag.getRosterFlag(RosterFlag.BLACK.getValue()));
		if(isBlackUser){
			return new ResponseEntity<>(ERROR, "您的账户信息异常，请咨询管理员！");
		}
		boolean resultFlag = false;
	    VasCouponDetail couponDetail = null;
		ResponseEntity<?> checkResult = null;
		BigDecimal commission = new BigDecimal(TradeTransferConstants.TRADE_WITHDRAWALS_COMMISSION); // 提现手续费
		BigDecimal withdrawsMoney = new BigDecimal(transAmt); // 提现金额
		BigDecimal transMoney = withdrawsMoney.add(commission);// 冻结总金额
		JedisClusterLock userlock = new JedisClusterLock();
		String lockKey = LOCK_PREFFIX + RegUser.class.getSimpleName() + regUserId;
		try {
			if (userlock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME)) {
				// 判断用户可用提现券
				if (couponDetailId != null) {
					couponDetail = couponDetailService.findVasCouponDetailById(couponDetailId);
					if (couponDetail != null && regUserId.equals(couponDetail.getAcceptorUserId())) {
						couponDetailId = couponDetail.getId();
						transMoney = withdrawsMoney;// 如果有优惠提现券，用户冻结金额为提现金额
					} else {
						couponDetailId = null;
					}
				}
				// 根据用户ID查询用户账户信息
				FinAccount finAccount = finAccountService.findByRegUserId(regUserId);
				if (finAccount == null) {
					return new ResponseEntity<>(ERROR, "未查询到该用户的账户信息!");
				}
				BigDecimal userAbleMoney = finAccount.getUseableMoney();
				// 校验冻结总金额的正确性
				checkResult = PayValidateFactory.validateMoney(transMoney, userAbleMoney);
				if (checkResult.getResStatus() == SUCCESS) {
					// 获取可用的提现银行卡
					FinBankCard finBankCard = getUsableBankCard(regUserId);
					if (finBankCard == null) {
						return new ResponseEntity<>(ERROR, "该用户没有绑卡或绑卡信息不完善!");
					}
					if (couponDetailId != null && couponDetail !=null) {
					    if(couponDetail.getState() ==VasCouponConstants.COUPON_DETAIL_SEND_ALREADY){
    						// 更新用户所用的提现券状态为已使用
    						VasCouponDetail updateCouponDetail = new VasCouponDetail();
    						updateCouponDetail.setId(couponDetailId);
    						updateCouponDetail.setState(VasCouponConstants.COUPON_DETAIL_USE_ALREADY);
    						updateCouponDetail.setUsedTime(new Date());
    						couponDetailService.updateVasCouponDetail(updateCouponDetail);
					    }else{
					        return new ResponseEntity<>(ERROR, "提现失败，提现卡券已经失效!");
					    }
					}
					// 增加提现申请
					FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
					String flowId = CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.WITHDRAW.getValue(),
							platformSourceEnums, TradeTransferConstants.COMMON_BUSINESS);
					finPaymentRecord.setFlowId(flowId);
					finPaymentRecord.setRegUserId(regUserId);
					finPaymentRecord.setTransMoney(withdrawsMoney);
					finPaymentRecord.setBankCardId(finBankCard.getId());
					finPaymentRecord.setTradeSource(platformSourceEnums.getValue());
					finPaymentRecord.setTradeType(PayStyleEnum.WITHDRAW.getValue());
					finPaymentRecord.setState(TradeStateConstants.PENDING_PAYMENT);
					finPaymentRecord.setCouponDetailId(couponDetailId);
					if (couponDetailId == null) {// 没有用提现券
						finPaymentRecord.setCommission(commission);
					} else {
						finPaymentRecord.setCommission(BigDecimal.ZERO);//用到提现券，手续费为0
					}
					// 查询优先级最高的提现渠道
					FinChannelGrant finChannelGrant = finChannelGrantService.findFirstFinChannelGrant(systemTypeEnums,
							platformSourceEnums, PayStyleEnum.WITHDRAW);
					if (finChannelGrant != null) {
						finPaymentRecord.setPayChannel(finChannelGrant.getChannelNameCode());
					} else {
						logger.error("提现申请, regUserId: {}, 提现渠道没有开启", regUserId);
					}
					Integer paymentId = finPaymentRecordService.insertFinPaymentRecord(finPaymentRecord);
					// 调用支付服务冻结账户金额及生成流水信息
					FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(finAccount.getRegUserId(), paymentId,
							transMoney, TradeTransferConstants.TRADE_TYPE_WITHDRAW_APPLY, platformSourceEnums);
					checkResult = finConsumptionService.cashPay(finTradeFlow,
							TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
					ValidateResponsEntityUtil validateResponsEntityUtil = new ValidateResponsEntityUtil(logger);
					validateResponsEntityUtil.validate(checkResult,
							"clientWithDrawFacade, 提现申请调用cashPay, transAmt: {}, userId: {}出现异常", "提现申请调用cashPay出现异常信息");
				}
			}
			return checkResult;
		} catch (Exception e) {
			logger.error("clientWithDrawFacade, 提现申请, transAmt: {}, regUserId: {}", transAmt, regUserId, e);
			throw new GeneralException("提现申请出现异常");
		} finally {
			if (resultFlag) {
				userlock.freeLock(lockKey);
			}
		}
	}

	@Override
	public ResponseEntity<?> getDataWithDrawPageForApp(int regUserId) throws Exception {
		logger.info("getDataWithDrawPageForApp, 获取提现页面数据, regUserId: {}", regUserId);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS, "查询成功");
		try {
			// 查询用户可提现的银行卡
			FinBankCard finBankCard = getUsableBankCard(regUserId);
			VasCouponDetail couponDetail = getUsableCoupon(regUserId);
			Map<String, Object> params = new HashMap<>();
			FinAccount finAccount = finAccountService.findByRegUserId(regUserId);
			params.put("bankCard", finBankCard);
			params.put("useableMoney", finAccount == null ? BigDecimal.ZERO : finAccount.getUseableMoney());
			params.put("couponDetailId", couponDetail != null ? couponDetail.getId() : null);
			responseEntity.setParams(params);
		} catch (Exception e) {
			logger.error("getDataWithDrawPageForWeb, 获取提现页面数据, regUserId: {}, 异常信息: ", regUserId, e);
			throw new GeneralException("初始化提现页面失败");
		}
		return responseEntity;
	}

	public ResponseEntity<?> getDataWithDrawPageForWeb(int regUserId) throws Exception {
		logger.info("getDataWithDrawPageForWeb, 获取提现页面数据, userId: {}", regUserId);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS, "查询成功");
		try {
			int count = 0;
			int withdrawlsCoupons = 0;
			// 查询用户绑卡数量
			FinBankCard finBankCard = getUsableBankCard(regUserId);
			if (finBankCard != null) {
				count = 1;
			}
			// 查询用户可用的提现券
			VasCouponDetail couponDetail = getUsableCoupon(regUserId);
			if (couponDetail != null) {
				withdrawlsCoupons = 1;
			}
			Map<String, Object> params = new HashMap<>();
			params.put("bindCardCount", count);
			params.put("withdrawlsCoupons", withdrawlsCoupons);
			params.put("couponDetailId", couponDetail == null ? null : couponDetail.getId());
			responseEntity.setParams(params);
		} catch (Exception e) {
			logger.error("getDataWithDrawPageForWeb, 获取提现页面数据, userId: {}", regUserId, e);
			throw e;
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> toChooseInvestAccount(Integer regUserId, Integer platformSource, Integer userType) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		FinChannelGrant finChannelGrant = null;
		BigDecimal accountMoney = BigDecimal.ZERO;// 账户余额
		BigDecimal qdzAccountMoney = BigDecimal.ZERO;// 钱袋子账户金额
		// 查询用户账户信息
		FinAccount finAccount = finAccountService.findByRegUserId(regUserId);
		if (finAccount == null) {
			return new ResponseEntity<>(ERROR, "用户账户不存在！");
		}
		// 账户余额
		accountMoney = finAccount.getUseableMoney() == null ? BigDecimal.ZERO : finAccount.getUseableMoney();
		// 查询用户钱袋子账户信息
		QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUserId);
		if (qdzAccount != null) {
			// 钱袋子账户金额
			qdzAccountMoney = qdzAccount.getMoney() == null ? BigDecimal.ZERO : qdzAccount.getMoney();
		}
		// 获取当前优先级最高的支付渠道
		List<FinChannelGrant> channelGrantList = finChannelGrantService.findFinChannelGrantList(SystemTypeEnums.HKJF,
				PlatformSourceEnums.typeByValue(platformSource), PayStyleEnum.RECHARGE);
		if (channelGrantList != null && channelGrantList.size() > 0) {
			finChannelGrant = channelGrantList.get(0);
		}
		// 查询用户当前持有的银行卡列表,及银行对应的单日限额
		List<FinBankCard> newBankCardList = new ArrayList<FinBankCard>();
		String regUserType = String.valueOf(
				(userType == UserConstants.USER_TYPE_ENTERPRISE || userType == UserConstants.USER_TYPE_TENEMENT)
						? UserConstants.USER_TYPE_ENTERPRISE : UserConstants.USER_TYPE_GENERAL);
		List<FinBankCard> bankCardList = finBankCardService.findByRegUserId(regUserId);
		if (bankCardList != null && bankCardList.size() > 0) {
			for (FinBankCard finBankCard : bankCardList) {
				FinBankRefer finBankRefer = new FinBankRefer();
				finBankRefer.setBankCode(finBankCard.getBankCode());
				finBankRefer.setThirdCode(
						PayChannelEnum.getPayChannelEnumByCode(finChannelGrant.getChannelNameCode()).getChannelKey());
				finBankRefer.setPaywayCodes(PayStyleEnum.RZ.getType());
				finBankRefer.setRegUserType(regUserType);
				List<FinBankRefer> bankReferList = finBankReferService.findFinBankReferList(finBankRefer);
				if (bankReferList != null && bankReferList.size() > 0) {
					finBankCard.setSingleLimit(bankReferList.get(0).getSingleLimit());
					finBankCard.setBankCode(bankReferList.get(0).getBankThirdCode());
				}
				newBankCardList.add(finBankCard);
			}
		}
		// 校验钱袋子规则是否合法
	   ResponseEntity<?> resEntity = vasRebatesRuleService.checkQdzRule();
        QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) resEntity.getParams().get("qdzVasRuleItem");
        if (resEntity.getResStatus() == SUCCESS) {
            resultMap.put("qdzState", qdzVasRuleItem.getState());
        } else {
            resultMap.put("qdzState", QdzConstants.QDZ_RULR_STATE_COUNTDOWN);
        }
		// 组装返回的MAP集合
		resultMap.put("payChannel", finChannelGrant == null ? "0" : finChannelGrant.getChannelNameCode());
		resultMap.put("accountMoney", accountMoney);
		resultMap.put("qdzAccountMoney", qdzAccountMoney);
		ExtendMap extendMap = AppResultUtil.successOfListInProperties(bankCardList, "处理成功", "bankCard"/** 银行卡号 **/
				, "bankName"/** 银行名称 **/
				, "bankCode"/** 银行编码 **/
				, "singleLimit"/** 单笔限额 **/
		);
		resultMap.put("bankCardList", extendMap.get(AppResultUtil.DATA_LIST));
		responseEntity.setParams(resultMap);
		return responseEntity;
	}

	/**
	 * 查询用户可用的提现银行卡
	 * 
	 * @param regUserId
	 * @return
	 */
	private FinBankCard getUsableBankCard(Integer regUserId) {
		FinBankCard finBankCard = new FinBankCard();
		finBankCard.setRegUserId(regUserId);
		List<FinBankCard> finBankCards = finBankCardService.findByCondition(finBankCard);
		if (finBankCards == null || finBankCards.isEmpty()) {
			return null;
		}
		for (FinBankCard bankCard : finBankCards) {
			if (bankCard.getState() == TradeStateConstants.BANK_CARD_STATE_UNAUTH_FORBIDDEN
					|| bankCard.getState() == TradeStateConstants.BANK_CARD_STATE_AUTH_FORBIDDEN) {
				continue;
			}
			if (StringUtils.isNotEmpty(bankCard.getBankProvince()) && StringUtils.isNotEmpty(bankCard.getBankCity())
					&& StringUtils.isNotEmpty(bankCard.getBranchName())) {
				return bankCard;
			}
		}
		return null;
	}

	/**
	 * 查询用户可用的提现券
	 * 
	 * @param regUserId
	 * @return
	 */
	private VasCouponDetail getUsableCoupon(Integer regUserId) {
		List<VasCouponDetailVO> couponDetailList = couponDetailService.getUserWithdrawUsableCoupon(regUserId);
		if (couponDetailList != null && couponDetailList.size() > 0) {
			return couponDetailList.get(0);
		}
		return null;
	}
	
	@Override
	public ResponseEntity<?> paymentVerificationCode(RechargeCash rechargeCash) {
		logger.info("paymentVerificationCode, 收银台发送短信验证码(预绑卡、预支付), 入参，rechargeCash: {}", JsonUtils.toJson(rechargeCash));
		ResponseEntity<?> resultMap = null;
		// 查询该用户该渠道下银行卡列表
		FinBankCard finBankCard = this.finBankCardService.findById(rechargeCash.getBankCardId());
		FinBankCardBinding finBankCardBinding = this.finBankCardBindingService.findBankCardBinding(
				rechargeCash.getBankCardId(), rechargeCash.getUserId(),
				PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
		if(StringUtils.isBlank(rechargeCash.getBankCode())){ //收银台过来的短验有可能没有bankcode，重新赋值下
			rechargeCash.setBankCode(finBankCard.getBankCode());
		}
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocol.getChannelKey()) || rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocolB.getChannelKey())){
			resultMap = this.protocolPayVerificationCode(rechargeCash , finBankCard , finBankCardBinding);
		}
		
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.Yeepay.getChannelKey())) {
			resultMap = this.yeepayVerificationCode(rechargeCash, finBankCard, finBankCardBinding);
		}
		
		return resultMap;
	}
	/**
	 * @Description : 协议支付充值（确定绑卡+直接支付、确认支付）
	 * @Method_Name : confirmPay;
	 * @param rechargeCash
	 *            发送对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-15 17:09:31;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> agreementPay(RechargeCash rechargeCash) {
		logger.info("FinPaymentFacadeImpl agreementPay method 协议支付。");
		ResponseEntity<?> resultMap = new ResponseEntity<>(SUCCESS);
		ResponseEntity<?> checkResult = validateRechargeCash(rechargeCash);
		if (checkResult.getResStatus() == Constants.ERROR) {
			return checkResult;
		}
		FinBankCard finBankCard = this.finBankCardService.findById(rechargeCash.getBankCardId());
		rechargeCash.setBankCode(finBankCard.getBankCode());
		if (PaymentConstants.PRE_TIED_CARD.equals(rechargeCash.getOperateType())) { // 预绑卡操作，下一步
																				// 确认绑卡+直接支付
			rechargeCash.setOperateType(PaymentConstants.CONFIG_TIED_CARD);
			ResponseEntity<?> confirmResult = this.finConsumptionService.confirmPay(rechargeCash);
			if (confirmResult.getResStatus() == ERROR) {
				resultMap.setParams((Map) confirmResult .getResMsg());
				return resultMap;
			}
			Map confirmMap = (Map) confirmResult.getResMsg();
			String no_agree = confirmMap.get("no_agree").toString();
			rechargeCash.setThirdAccount(no_agree);
			rechargeCash.setOperateType(PaymentConstants.DIRECT_PAYMENT);
			// 直接支付
			ResponseEntity<?> directResult = this.finConsumptionService.confirmPay(rechargeCash);
			Map map = (Map) directResult.getResMsg();
			logger.info("直接支付 方法: FinPaymentFacadeImpl 层  agreementPay method , 返回 map : {},resStatus:{},resMsg{}", map,
					map.get("resStatus"), map.get("resMsg"));
			resultMap.setParams(map);
			return resultMap;
		} else {// 确认交易
			if (StringUtils.isBlank(rechargeCash.getPaymentFlowId())) {
				logger.error("FinPaymentFacadeImpl agreementPay method 协议支付,订单号为空");
				return new ResponseEntity<>(ERROR, "订单号为空");
			}
			rechargeCash.setOperateType(PaymentConstants.CONFIRM_PAYMENT);
			ResponseEntity<?> payResult = this.finConsumptionService.confirmPay(rechargeCash);
			if (payResult.getResStatus() == ERROR) {
				logger.info("确认支付，支付失败，resmsg:{}", payResult.getResMsg());
				resultMap.setParams((Map) payResult.getResMsg());
				return resultMap;
			}
			Map map = (Map) payResult.getResMsg();
			logger.info("确认交易方法: FinPaymentFacadeImpl 层  agreementPay method , 返回 map : {},resStatus:{},resMsg{}", map,
					map.get("resStatus"), map.get("resMsg"));
			resultMap.setParams(map);
			return resultMap;
		}
	}
	/**
	 * @Description : 易宝确认支付（确认支付接口）
	 * @Method_Name : confirmPay;
	 * @param rechargeCash
	 *            发送对象
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-15 17:19:56;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> yeepayConfirmPay(RechargeCash rechargeCash) {
		logger.info("FinPaymentFacadeImpl yeepayConfirmPay method 易宝确认支付。");
		ResponseEntity<?> resultMap = new ResponseEntity<>(SUCCESS);
		FinBankCard finBankCard = this.finBankCardService.findById(rechargeCash.getBankCardId());
		rechargeCash.setBankCode(finBankCard.getBankCode());
		rechargeCash.setOperateType(PaymentConstants.YEEPAY_CONFIRM_PAYMENT);
		//确认支付
		ResponseEntity<?> directResult = this.finConsumptionService.confirmPay(rechargeCash);
		Map map = (Map) directResult.getResMsg();
		logger.info("直接支付 方法: FinPaymentFacadeImpl 层  yeepayConfirmPay method , 返回 map : {},resStatus:{},resMsg{}", map,
				map.get("resStatus"), map.get("resMsg"));
		resultMap.setParams(map);
		return resultMap;
	}
	
	@Override
	public ResponseEntity<?> confirmPay(RechargeCash rechargeCash){
		logger.info("FinPaymentFacadeImpl confirmPay method 确认支付,入参， rechargeCash：{}", rechargeCash.toString());
		ResponseEntity<?> resultMap = new ResponseEntity<>(SUCCESS);
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocol.getChannelKey()) || rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocolB.getChannelKey())){
			resultMap = this.agreementPay(rechargeCash);
		}
		
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.Yeepay.getChannelKey())){
			resultMap = this.yeepayConfirmPay(rechargeCash);
			logger.info("确认充值，返回的信息：" + resultMap.toString());
			if(resultMap.getParams().get("resStatus").equals(String.valueOf(Constants.SUCCESS))){
				// 轮询订单状态，返回支付结果
				ResponseEntity<?> payState = finConsumptionService.payWaiting((String)resultMap.getParams().get("flowId"),resultMap.getParams());
				if (payState.getResStatus() != Constants.SUCCESS){
					return payState;
				}
			}
		}
		
		return resultMap;
	}
	
	/**
	 * @Description : 协议支付- 收银台短验
	 * @Method_Name : yeepayVerificationCode;
	 * @param rechargeCash
	 * @param finBankCard
	 * @param finBankCardBinding
	 * @return
	 * @Creation Date : 2018-09-26 09:22:21;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> yeepayVerificationCode (RechargeCash rechargeCash , FinBankCard finBankCard, FinBankCardBinding finBankCardBinding){
		logger.info("yeepayVerificationCode, 易宝支付-收银台发送短信验证码, 入参，rechargeCash: {}", JsonUtils.toJson(rechargeCash));
		ResponseEntity<?> resultMap = new ResponseEntity<>(SUCCESS);
		FinPaymentRecord finpaymentRecord = null;
		if(StringUtils.isNotBlank(rechargeCash.getPaymentFlowId())){
			finpaymentRecord = this.finPaymentRecordService.findFinPaymentRecordByFlowId(rechargeCash.getPaymentFlowId());
		}
		rechargeCash.setOperateType(PaymentConstants.YEEPAY_SENDSMS_AGAIN); //默认短信重发操作
		//如果手机号和上一次不相等，或者是无流水信息，则调用首次充值接口，生成新流水
		if(finpaymentRecord == null || !rechargeCash.getTel().equals(finBankCard.getBankTel())){
			rechargeCash.setOperateType(PaymentConstants.YEEPAY_FIRST_RECHARGE);
			rechargeCash.setPaymentFlowId(CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.RECHARGE.getValue(),
					PlatformSourceEnums.platformTypeByType(rechargeCash.getPlatformSourceName()),
					rechargeCash.getRechargeFlag() == RECHARGE_FLAG_BANK ? BANK_INVEST_BUSINESS : COMMON_BUSINESS));
			logger.info("易宝首次点击获取验证码，生成流水paymentFlowId={}", rechargeCash.getPaymentFlowId());
		}
		ResponseEntity<?> responseEntity = this.finConsumptionService.yeepayVerificationCode(rechargeCash);
		Map<String, Object> result = (Map) responseEntity.getResMsg();
		result.put("operateType", rechargeCash.getOperateType());
		if (responseEntity.getResStatus() == SUCCESS) {
			logger.info("发送短验成功，用户Id: {}, 传入手机号: {}, 数据库记录手机号: {}", rechargeCash.getUserId(), rechargeCash.getTel(),
					finBankCard.getBankTel());
			if (StringUtils.isBlank(rechargeCash.getTel()) || !rechargeCash.getTel().equals(finBankCard.getBankTel())) {
				FinBankCard updateBankCard = new FinBankCard();
				updateBankCard.setId(finBankCard.getId());
				updateBankCard.setBankTel(rechargeCash.getTel());
				this.finBankCardService.update(updateBankCard);
			}
		}
		logger.info("方法: FinPaymentFacadeImpl 层  yeepayVerificationCode method , 返回 result: {},resStatus:{},resMsg{}",
				result, result.get("resStatus"), result.get("resMsg"));
		resultMap.setParams(result);
		return resultMap;
	}
	
	/**
	 * @Description : 协议支付- 收银台短验
	 * @Method_Name : protocolPayVerificationCode;
	 * @param rechargeCash
	 * @param finBankCard
	 * @param finBankCardBinding
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2018-09-25 14:26:37;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 * @return
	 */
	public ResponseEntity<?> protocolPayVerificationCode (RechargeCash rechargeCash , FinBankCard finBankCard, FinBankCardBinding finBankCardBinding) {
		logger.info("protocolPayVerificationCode, 协议支付-收银台发送短信验证码(预绑卡、预支付), 入参，rechargeCash: {}", JsonUtils.toJson(rechargeCash));
		ResponseEntity<?> resultMap = new ResponseEntity<>(SUCCESS);
		ResponseEntity<?> checkResult = getRechargeCashByBankCard(rechargeCash, finBankCard, finBankCardBinding);
		if (checkResult.getResStatus() == Constants.ERROR) {
			logger.error("paymentVerificationCode,收银台发送短信验证码(预绑卡、预支付), 参数校验及充值对象组装, 用户Id: {}, 异常信息: ", rechargeCash.getUserId(),
					checkResult.getResMsg());
			return checkResult;
		}
		rechargeCash = (RechargeCash) checkResult.getParams().get("rechargeCash");
		ResponseEntity<?> responseEntity = this.finConsumptionService.paymentVerificationCode(rechargeCash);
		Map<String, Object> result = (Map) responseEntity.getResMsg();
		result.put("operateType", rechargeCash.getOperateType());
		
		if (responseEntity.getResStatus() == SUCCESS) {
			logger.info("发送短验成功，用户Id: {}, 传入手机号: {}, 数据库记录手机号: {}", rechargeCash.getUserId(), rechargeCash.getTel(),
					finBankCard.getBankTel());
			if (StringUtils.isBlank(rechargeCash.getTel()) || !rechargeCash.getTel().equals(finBankCard.getBankTel())) {
				FinBankCard updateBankCard = new FinBankCard();
				updateBankCard.setId(finBankCard.getId());
				updateBankCard.setBankTel(rechargeCash.getTel());
				this.finBankCardService.update(updateBankCard);
			}
		}
		logger.info("方法: FinPaymentFacadeImpl 层  protocolPayVerificationCode method , 返回 result: {},resStatus:{},resMsg{}",
				result, result.get("resStatus"), result.get("resMsg"));
		resultMap.setParams(result);
		return resultMap;
	}
	
	/**
	 * @Description : 宝付协议支付-根据绑卡信息，判断发短信的真正操作（绑卡、支付）
	 * @Method_Name : getRechargeCashByBankCard;
	 * @param rechargeCash
	 *            发送短信对象（也是充值对象）
	 * @param finBankCard
	 * 
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05014 19:54:55;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> getRechargeCashByBankCard(RechargeCash rechargeCash, FinBankCard finBankCard,
			FinBankCardBinding finBankCardBinding) {
		if (finBankCard == null) {
			return new ResponseEntity<>(Constants.ERROR, "获取银行卡信息错误");
		}
		rechargeCash.setBankCode(finBankCard.getBankCode());
		if (!finBankCard.getBankCard().equals(rechargeCash.getBankCard())) {
			return new ResponseEntity<>(Constants.ERROR, "银行卡信息不一致");
		}
		if (finBankCardBinding == null){
			rechargeCash.setOperateType(PaymentConstants.PRE_TIED_CARD);
			return new ResponseEntity<>(Constants.SUCCESS).addParam("rechargeCash", rechargeCash);
		}
		// 如果该卡绑卡未认证，则发短信是预绑卡操作
		if (finBankCardBinding.getState() == TradeStateConstants.BANK_CARD_STATE_UNAUTH) {
			rechargeCash.setOperateType(PaymentConstants.PRE_TIED_CARD);
		}
		if (finBankCardBinding.getState() == TradeStateConstants.BANK_CARD_STATE_AUTH) {
			rechargeCash.setOperateType(PaymentConstants.PRE_PAYMENT);
			rechargeCash.setThirdAccount(finBankCardBinding.getThirdAccount());
		}
		if (StringUtil.isEmpty(finBankCard.getBankCode())) {
			return new ResponseEntity<>(Constants.ERROR, "所属银行编码信息缺失");
		}
		return new ResponseEntity<>(Constants.SUCCESS).addParam("rechargeCash", rechargeCash);
	}

	/**
	 * @Description : 协议支付充值判断下一步（绑卡、支付）
	 * @Method_Name : getRechargeCashByOperate;
	 * @param rechargeCash
	 *            充值对象
	 * 
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018-05-14 19:54:55;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> validateRechargeCash(RechargeCash rechargeCash) {
		if (rechargeCash == null) {
			return new ResponseEntity<>(Constants.ERROR, "充值对象为空");
		}
		if (StringUtil.isEmpty(rechargeCash.getPayUnionCode())) {
			return new ResponseEntity<>(Constants.ERROR, "参数唯一码不能为空");
		}

		return new ResponseEntity<>(Constants.SUCCESS).addParam("rechargeCash", rechargeCash);
	}

    @Override
    public ResponseEntity<?> searchRechargeInfo(Integer regUserId) {
        ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        FinAccount finAccount = finAccountService.findByRegUserId(regUserId);
        QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUserId);
        BigDecimal qdzAccountMoney = BigDecimal.ZERO;
        if (qdzAccount != null) {
            // 钱袋子账户金额
            qdzAccountMoney = qdzAccount.getMoney() == null ? BigDecimal.ZERO : qdzAccount.getMoney();
        }
        // 校验钱袋子规则是否合法
        ResponseEntity<?> resEntity = vasRebatesRuleService.checkQdzRule();
        QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) resEntity.getParams().get("qdzVasRuleItem");
        if (resEntity.getResStatus() == SUCCESS) {
            resultMap.put("qdzState", qdzVasRuleItem.getState());
        } else {
            resultMap.put("qdzState", QdzConstants.QDZ_RULR_STATE_COUNTDOWN);
        }
        List<FinBankCard> bankCardList = finBankCardService.findByRegUserId(regUserId);
        // 组装返回的MAP集合
        resultMap.put("userAbleMoney", finAccount == null?BigDecimal.ZERO:finAccount.getUseableMoney());
        resultMap.put("qdzAccountMoney", qdzAccountMoney);
        ExtendMap bankCardMap = AppResultUtil.successOfListInProperties(bankCardList, "查询成功", "id"/** 银行卡Id **/
                , "bankCode"/** 银行编码 **/
                , "bankName"/** 银行名称 **/
                , "bankCard"/** 银行卡号 **/
                , "bankProvince"/** 省 **/
                , "bankCity"/** 市 **/
                , "branchName"/** 支行名称 **/
                , "state"/** 卡的绑定状态 **/
        );
        resultMap.put("bankCardList", bankCardMap.get(AppResultUtil.DATA_LIST));
        responseEntity.setParams(resultMap);
        return responseEntity;
    }

}
