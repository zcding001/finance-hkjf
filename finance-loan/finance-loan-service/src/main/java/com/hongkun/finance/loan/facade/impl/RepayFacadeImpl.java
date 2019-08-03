package com.hongkun.finance.loan.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidMatchUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.constants.RepayType;
import com.hongkun.finance.loan.facade.RepayFacade;
import com.hongkun.finance.loan.facade.util.RepayCalcInterestUtil;
import com.hongkun.finance.loan.facade.util.ValidateRepayAmountUtil;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.BidReturnCapRecord;
import com.hongkun.finance.loan.model.vo.BidCommonPlanVo;
import com.hongkun.finance.loan.model.vo.BidInvestVo;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.loan.util.RepayAndReceiptUtils;
import com.hongkun.finance.loan.vo.RepayPlanPayVo;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.llpayvo.PayDataBean;
import com.hongkun.finance.payment.llpayvo.PayPlan;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.loan.constants.RepayConstants.*;
import static com.hongkun.finance.payment.constant.PaymentConstants.LIAN_SUCESS_SIGN;
import static com.hongkun.finance.payment.constant.TradeStateConstants.BANK_CARD_STATE_AUTH;
import static com.hongkun.finance.user.constants.UserConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description : 还款接口
 * @Project : finance-loan-service
 * @Program Name : cm.yirun.finance.loan.facade.impl.RepayPlanFacadeImpl.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@Service
public class RepayFacadeImpl implements RepayFacade {
	private static final Logger logger = LoggerFactory.getLogger(RepayFacadeImpl.class);

	private static final String PARAM_KEY_FIN_TRADE_FLOW = "finTradeFlow";
	private static final String PARAM_KEY_FIN_FUNDTRANSFER_LIST = "finFundtransferList";
	private static final String PARAM_KEY_FIN_ACCOUNT_LIST = "finAccountList";
	private static final String PARAM_KEY_CURRENT_ACCOUNT_LIST = "currentAccountList";

	@Autowired
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinPayConfigService finPayConfigService;
	@Reference
	private FinTradeFlowService finTradeFlowService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private BidProductService bidProductService;
	@Reference
	private BidMatchService bidMatchService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private FinBankCardBindingService finBankCardBindingService;

	@Override
	public ResponseEntity<?> autoWithHoldRepay() throws Exception {
		ResponseEntity<?> bankCardRepay = null;
		BidInfoDetail infoDetail = null;
		BidInfo bidInfo = null;
		BidProduct bidProduct = null;
		BidRepayPlan repayPlan = null;
		List<BidReceiptPlan> receiptPlanList = null;
		RegUser regUser = null;
		RegUserDetail userDetail = null;
		try {
			// 查询当天还款计划
			BidRepayPlan bidRepayPlan = new BidRepayPlan();
			bidRepayPlan.setPlanTimeBegin(DateUtils.getFirstTimeOfDay());
			bidRepayPlan.setPlanTimeEnd(DateUtils.getLastTimeOfDay());
			bidRepayPlan.setState(REPAY_STATE_NONE);// 查询未还款的标
			List<BidRepayPlan> bidRepayPlans = bidRepayPlanService.findBidRepayPlanList(bidRepayPlan);
			for (BidRepayPlan plan : bidRepayPlans) {
				infoDetail = bidInfoDetailService.findBidInfoDetailByBidId(plan.getBidId());
				if (infoDetail.getWithholdState() != IS_WITHHOLD_STATE) {
					continue;
				}
				repayPlan = plan;
			}
			bidInfo = bidInfoService.findBidInfoById(repayPlan.getBidId());
			regUser = regUserService.findRegUserById(repayPlan.getRegUserId());
			userDetail = regUserDetailService.findRegUserDetailByRegUserId(repayPlan.getRegUserId());
			BidReceiptPlan planCdt = new BidReceiptPlan();
			planCdt.setBidId(bidInfo.getId());
			planCdt.setPeriods(repayPlan.getPeriods());
			planCdt.setState(REPAY_STATE_NONE);
			receiptPlanList = bidReceiptPlanService.findBidReceiptPlanList(planCdt);
			bidProduct = bidProductService.findBidProductById(bidInfo.getBidProductId());
			// 还款是否逾期
			repayPunishAmount(repayPlan, infoDetail, bidInfo);
			FinBankCardBinding bindingCdt = new FinBankCardBinding();
			bindingCdt.setPayChannel(Integer.valueOf(PayChannelEnum.LianLian.getChannelNameValue()));
			bindingCdt.setRegUserId(regUser.getId());
			List<FinBankCardBinding> bankCardBindings = finBankCardBindingService
					.findFinBankCardBindingList(bindingCdt);
			if (CommonUtils.isEmpty(bankCardBindings)) {
				return BaseUtil.error("请先去绑定银行卡！");
			}
			if (bankCardBindings.get(0).getState() != BANK_CARD_STATE_AUTH) {// 绑卡未认证
				return BaseUtil.error("银行卡未认证！");
			}
			bindingCdt = bankCardBindings.get(0);
			// 授权签约成功，直接扣款划转
			PayPlan payPlan = new PayPlan();
			payPlan.setBidId(String.valueOf(repayPlan.getBidId()));
			payPlan.setCurPlayDate(repayPlan.getPlanTime());
			payPlan.setBankCardId(Long.valueOf(bindingCdt.getFinBankCardId()));
			payPlan.setInfo(regUser.getLogin() + "用户同意代扣还款！");
			// payPlan.setNotifyUrl(PropertiesHolder.getProperty("repay_withhold_notify_url"));
			payPlan.setPayChannelEnum(PayChannelEnum.LianLian);
			payPlan.setPayStyleEnum(PayStyleEnum.DK);
			payPlan.setPlatformSourceName(PlatformSourceEnums.PC.getType());
			payPlan.setRegUserId(regUser.getId());
			payPlan.setRepayAmount(repayPlan.getAmount());
			payPlan.setRepayId(String.valueOf(repayPlan.getId()));
			payPlan.setSysNameCode(SystemTypeEnums.HKJF.getType());
			payPlan.setThirdAccount(bindingCdt.getThirdAccount());
			payPlan.setUserName(userDetail.getRealName());
			payPlan.setUserType(regUser.getType());
			payPlan.setIdCard(userDetail.getIdCard());
			bankCardRepay = finConsumptionService.bankCardRepayment(payPlan);
			if (bankCardRepay == null || bankCardRepay.getResStatus() == ERROR) {
				logger.info("用户标识：{}， 代扣请求划转失败！还款计划ID：{}，启动风险储备金；还款", regUser.getId(), repayPlan.getId());
				return doRepay(repayPlan, receiptPlanList, bidProduct, bidInfo, infoDetail,
						RepayType.REPAY_RISK_RESERVE, PlatformSourceEnums.PC);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return BaseUtil.error("自动还款代扣异常！还款计划ID：{" + repayPlan.getId() + "}");
		}
		return bankCardRepay;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public ResponseEntity<?> autoRiskReserveRepay() throws Exception {
		ResponseEntity<?> riskReserveRepay = null;
		BidInfoDetail infoDetail = null;
		BidInfo bidInfo = null;
		BidProduct bidProduct = null;
		List<BidReceiptPlan> receiptPlanList = null;
		RegUser regUser = null;
		try {
			// 查询当天还款计划
			BidRepayPlan bidRepayPlan = new BidRepayPlan();
			bidRepayPlan.setPlanTimeBegin(DateUtils.getFirstTimeOfDay());
			bidRepayPlan.setPlanTimeEnd(DateUtils.getLastTimeOfDay());
			bidRepayPlan.setState(REPAY_STATE_NONE);// 查询未还款的标
			List<BidRepayPlan> bidRepayPlans = bidRepayPlanService.findBidRepayPlanList(bidRepayPlan);
			ResponseEntity<?>  repyaInfos;
			for (BidRepayPlan plan : bidRepayPlans) {
				// 获取还款需要信息
				repyaInfos = getRepayInfo(plan.getId(), regUser.getId());
				if (repyaInfos == null || repyaInfos.getResStatus() != SUCCESS) {
					return repyaInfos;
				}
				receiptPlanList = (List<BidReceiptPlan>) repyaInfos.getParams().get("bidReceiptPlanList");
				bidInfo = (BidInfo) repyaInfos.getParams().get("bidInfo");
				infoDetail = (BidInfoDetail) repyaInfos.getParams().get("bidInfoDetail");
				bidProduct = (BidProduct) repyaInfos.getParams().get("bidProduct");
				riskReserveRepay = doRepay(bidRepayPlan, receiptPlanList, bidProduct, bidInfo, infoDetail,
						RepayType.REPAY_RISK_RESERVE, PlatformSourceEnums.PC);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(ERROR, "启用风险储备金还款异常！");
		}
		return riskReserveRepay;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> repay(int repayId, BigDecimal capital, int withHoldflag, RegUser regUser,
			PlatformSourceEnums platformSourceEnums) {
        logger.info("RepayFacadeImpl#repay, 用户标识: {}, 还款计划标识: {}, 提前还本: {}, 代扣模式: {}", regUser.getId(), repayId, 
                capital, withHoldflag);
		ResponseEntity<?> result = null;
		try {
            // 默认正常还款
			RepayType repayFlag = RepayType.REPAY_NORMAL;
			// 获取还款需要信息
			ResponseEntity repyaInfos = getRepayInfo(repayId, regUser.getId());
			if (repyaInfos == null || repyaInfos.getResStatus() != SUCCESS) {
				return repyaInfos;
			}
			BidRepayPlan repayPlan = (BidRepayPlan) repyaInfos.getParams().get("bidRepayPlan");
			List<BidReceiptPlan> receiptPlans = (List<BidReceiptPlan>) repyaInfos.getParams().get("bidReceiptPlanList");
			BidInfo bidInfo = (BidInfo) repyaInfos.getParams().get("bidInfo");
			BidInfoDetail bidInfoDetail = (BidInfoDetail) repyaInfos.getParams().get("bidInfoDetail");
			BidProduct bidProduct = (BidProduct) repyaInfos.getParams().get("bidProduct");
			FinAccount account = (FinAccount) repyaInfos.getParams().get("finAccount");
			// 还款是否逾期
			repayPunishAmount(repayPlan, bidInfoDetail, bidInfo);
			// 提前还本  本金>0,无罚息
			if (CompareUtil.gtZero(capital) && CompareUtil.lteZero(repayPlan.getPunishAmount())) {
                int repayMentWay = bidInfo.getBiddRepaymentWay();
                if (repayMentWay != RepayConstants.REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH 
                        && repayMentWay != RepayConstants.REPAYTYPE_PRINCIPAL_INTEREST_EQ 
                        && repayMentWay != RepayConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END
                        && repayMentWay != RepayConstants.REPAYTYPE_ONECE_REPAYMENT
                ) {
                    return ResponseEntity.error("此标的还款方式不支持提前还本");
                }
			    repayFlag = RepayType.REPAY_ADVANCE;
				result = ValidateRepayAmountUtil.advanceRepayMoney(bidInfo, bidInfoDetail, repayPlan, account, capital);
				if (BaseUtil.error(result)) {
					return result;
				}
				// 提前还款重新设置还款利息&服务费&还款本金
				repayPlan.setInterestAmount(
						RepayCalcInterestUtil.calForReturnCap(bidInfo, bidInfoDetail, capital, repayPlan, bidInfo.getInterestRate()));
				repayPlan.setServiceCharge(RepayCalcInterestUtil.calForReturnCap(bidInfo, bidInfoDetail,
						capital, repayPlan, bidInfo.getServiceRate()));
				repayPlan.setCapitalAmount(capital);
				repayPlan.setAmount(repayPlan.getCapitalAmount().add(repayPlan.getInterestAmount())
						.add(repayPlan.getServiceCharge()));
			}
			// 首次代扣还款
			if (withHoldflag == IS_WITHHOLD_STATE && repayPlan.getPeriods() == 1) {
				return withHoldRepayMoney(bidInfoDetail, repayPlan, regUser, account);
			}
			// 启动风险储备金
			if (repayPlan.getState() == REPAY_STATE_RISK_RESERVE) {
				repayFlag = RepayType.REPAY_RETURN_RISK_RESERVE;
			}
			// 正常还款金额校验
			result = ValidateRepayAmountUtil.normalRepayMoney(bidInfoDetail, repayPlan.getPunishAmount(),
					repayPlan.getAmount(), account);
			if (BaseUtil.error(result)) {
				return result;
			}
			// 还款操作
			result = ApplicationContextUtils.getBean(RepayFacadeImpl.class).doRepay(repayPlan, receiptPlans, bidProduct,
					bidInfo, bidInfoDetail, repayFlag, platformSourceEnums);
            result.getParams().put("isLastPeriod", repyaInfos.getParams().get("isLastPeriod"));
            if(CompareUtil.gtZero(capital)){
                result.getParams().put("isLastPeriod", 1);
            }
		} catch (Exception e) {
            logger.error("RepayFacadeImpl#repay, 用户标识: {}, 还款计划标识: {}, 提前还本: {}, 代扣模式: {}", regUser.getId(), repayId,
                    capital, withHoldflag, e);
		    throw new GeneralException("还款失败");
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseEntity<?> lianlianRepayNotice(String reqStr) {
		ResponseEntity entity = null;
		try {
			FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.LianLian.getChannelKey(),
					PayStyleEnum.DK.getType());
			if (payConfig == null) {
				logger.info("获取支付配置信息异常！");
				return BaseUtil.error("获取支付配置信息异常！");
			}
			boolean sign = PaymentUtil.checkSign(reqStr, payConfig.getPublicKey(), payConfig.getPayMd5Key());
			if (!sign) {
				logger.info("支付异步通知验签失败！标识：{}", reqStr);
				return BaseUtil.error("支付异步通知验签失败！");
			}
			// 解析异步通知对象
			PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);

			String tradeFlowNo = payDataBean.getNo_order(); // 联联异步回传平台订单号
			String moneyOrder = payDataBean.getMoney_order();// 代扣还款金额
			String payR = payDataBean.getResult_pay();// 支付结果
			if (!LIAN_SUCESS_SIGN.equalsIgnoreCase(payR)) {
				logger.info("异步代扣还款失败：{}", reqStr);
				return BaseUtil.error("异步代扣还款失败！");
			}
			if (PaymentUtil.isNull(tradeFlowNo)) {
				logger.info("异步通知报文原订单号为空{{}}", reqStr);
				return BaseUtil.error("异步通知报文原订单号为空！");
			}
			FinTradeFlow flow = finTradeFlowService.findByFlowId(tradeFlowNo);
			if (flow == null || flow.getTransMoney().compareTo(BigDecimal.valueOf(Double.valueOf(moneyOrder))) != 0) {
				logger.info("流水异常，连连代扣金额:{},流水订单号：", moneyOrder, tradeFlowNo);
				return BaseUtil.error("流水异常，连连代扣金额{" + moneyOrder + "}" + "流水订单号：" + tradeFlowNo);
			}
			BidRepayPlan plan = bidRepayPlanService.findBidRepayPlanById(Integer.parseInt(flow.getPflowId()));
			if (plan == null) {
				logger.info("代扣还款计划异常，还款流水PflowId：{}", flow.getPflowId());
				return BaseUtil.error("代扣还款计划异常，还款流水PflowId：{" + flow.getPflowId() + "}");
			}
			if (plan.getState() == 2) {
				logger.info("代扣还款计划已还款，还款ID：{}", plan.getId());
				return BaseUtil.error("代扣还款计划已还款！");
			}
			// TODO: 曹新邦 需要优化，更新账户、流水、划转调用服务一次，完成此步操作 2017/07/21
			// 更新借款人账户
			FinAccount account = new FinAccount();
			account.setRegUserId(plan.getRegUserId());
			account.setNowMoney(flow.getTransMoney());
			account.setUseableMoney(flow.getTransMoney());
			finAccountService.update(account);
			// 更新流水、资金划转状态
			FinTradeFlow finTradeFlow = new FinTradeFlow();
			finTradeFlow.setId(flow.getId());
			finTradeFlowService.update(finTradeFlow);
			FinFundtransfer finFundtransfer = new FinFundtransfer();
			// TODO需要修改 yanbinghuang
			finFundtransfer.setTradeFlowId(flow.getFlowId());
			finFundtransferService.update(finFundtransfer);
			// 获取还款需要信息
			ResponseEntity repyaInfos = getRepayInfo(plan.getId(), plan.getRegUserId());
			if (repyaInfos==null || repyaInfos.getResStatus() != SUCCESS) {
				return repyaInfos;
			}
			List<BidReceiptPlan> receiptPlans = (List<BidReceiptPlan>) repyaInfos.getParams().get("bidReceiptPlanList");
			BidInfo bidInfo = (BidInfo) repyaInfos.getParams().get("bidInfo");
			BidInfoDetail bidInfoDetail = (BidInfoDetail) repyaInfos.getParams().get("bidInfoDetail");
			BidProduct bidProduct = (BidProduct) repyaInfos.getParams().get("bidProduct");
			// 正常还款金额校验
			entity = ValidateRepayAmountUtil.normalRepayMoney(bidInfoDetail, plan.getPunishAmount(), plan.getAmount(),
					account);
			if (BaseUtil.error(entity)) {
				return entity;
			}
			entity = doRepay(plan, receiptPlans, bidProduct, bidInfo, bidInfoDetail, RepayType.REPAY_NORMAL,
					PlatformSourceEnums.PC);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(ERROR, "代扣还款回调异常！");
		}
		return entity;
	}

	/**
	 * @Description : 首次代扣还款逻辑
	 * @Method_Name : withHoldRepayMoney
	 * @param infoDetail
	 * @param repayPlan
	 * @param regUser
	 * @param account
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月12日 下午3:39:21
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private ResponseEntity<?> withHoldRepayMoney(BidInfoDetail infoDetail, BidRepayPlan repayPlan, RegUser regUser,
			FinAccount account) throws Exception {
		if (infoDetail.getWithholdState() != IS_WITHHOLD_STATE) {
			return BaseUtil.error("该标不支持代扣还款！");
		}
		if (regUser.getIdentify() == USER_IDENTIFY_NO) {// 校验是否实名
			return BaseUtil.error("用户未实名认证！");
		}
		if (repayPlan.getPeriods() == 1) {
			FinBankCardBinding bindingCdt = new FinBankCardBinding();
			bindingCdt.setPayChannel(Integer.valueOf(PayChannelEnum.LianLian.getChannelNameValue()));
			bindingCdt.setRegUserId(regUser.getId());
			List<FinBankCardBinding> bankCardBindings = finBankCardBindingService
					.findFinBankCardBindingList(bindingCdt);
			if (CommonUtils.isEmpty(bankCardBindings)) {
				return BaseUtil.error("请先去绑定银行卡！");
			}
			bindingCdt = bankCardBindings.get(0);
			if (bindingCdt.getState() != BANK_CARD_STATE_AUTH) {// 绑卡未认证
				return BaseUtil.error("银行卡未认证！");
			}
			// 签约代扣还款
			if (StringUtils.isEmpty(bindingCdt.getThirdAccount())) {
				return BaseUtil.error("代扣请求时协议号不能为空！");
			}
			BidRepayPlan plan = new BidRepayPlan();
			plan.setBidId(repayPlan.getBidId());
			List<BidRepayPlan> bidRepayPlans = bidRepayPlanService.findBidRepayPlanList(plan);

			List<RepayPlanPayVo> list = new ArrayList<>();
			for (BidRepayPlan bidRepayPlan : bidRepayPlans) {
				RepayPlanPayVo vo = new RepayPlanPayVo();
				vo.setAmount(String.valueOf(bidRepayPlan.getAmount()));
				vo.setDate(DateUtils.format(bidRepayPlan.getPlanTime(), "yyyy-MM-dd"));
				list.add(vo);
			}
			JSONObject repayInfo = new JSONObject();
			repayInfo.put("repaymentPlan", list);
			// 首次代扣应该先去申请代扣授权
			ResponseEntity<?> agreeNoAuth = finConsumptionService.lianlianAgreeNoAuthApply(
					String.valueOf(repayPlan.getBidId()), regUser.getId(), bindingCdt.getThirdAccount(), repayInfo,
					SystemTypeEnums.HKJF.getType(), PlatformSourceEnums.PC.getType());
			if (agreeNoAuth == null || agreeNoAuth.getResStatus() == ERROR) {
				return BaseUtil.error("银行卡授权申请失败！");
			}
			// 授权签约成功，直接扣款划转
			PayPlan payPlan = new PayPlan();
			payPlan.setBidId(String.valueOf(repayPlan.getBidId()));
			payPlan.setCurPlayDate(repayPlan.getPlanTime());
			payPlan.setInfo(regUser.getLogin() + "用户同意代扣还款！");
			// payPlan.setNotifyUrl(PropertiesHolder.getProperty("repay_withhold_notify_url"));
			payPlan.setPayChannelEnum(PayChannelEnum.LianLian);
			payPlan.setPayStyleEnum(PayStyleEnum.DK);
			payPlan.setPlatformSourceName(PlatformSourceEnums.PC.getType());
			payPlan.setRegUserId(regUser.getId());
			payPlan.setRepayAmount(repayPlan.getAmount());
			payPlan.setRepayId(String.valueOf(repayPlan.getId()));
			payPlan.setSysNameCode(SystemTypeEnums.HKJF.getType());
			payPlan.setThirdAccount(bindingCdt.getThirdAccount());
			payPlan.setUserName(account.getUserName());
			payPlan.setUserType(regUser.getType());
			payPlan.setBankCardId(Long.valueOf(bindingCdt.getFinBankCardId()));
			ResponseEntity<?> bankCardRepay = finConsumptionService.bankCardRepayment(payPlan);
			if (bankCardRepay == null || bankCardRepay.getResStatus() == ERROR) {
				return BaseUtil.error("代扣请求划转失败！");
			}
		}
		return new ResponseEntity<>(SUCCESS, "还款代扣成功！");
	}

	/**
	 * 
	 * @Description : 执行还款操作，事务说明， 操作1调用payment服务，异常时通过TCC进行事务回滚
	 *              操作2是loan服务，与doRepay方法在同一事务中，因此不依赖TCC，发生异常时，事务还没有提交，直接回滚
	 *              操作3是invest服务，异常时invest服务中事务自动回滚抛出异常，doRepay事务捕获异常后自动回滚，
	 *              最后通过TCC回滚payment服务事务
	 * @Method_Name : doRepay
	 * @param repayPlan
	 * @param receiptPlanList
	 * @param bidProduct
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @param repayType
	 * @param platformSourceEnums
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年12月25日 上午11:07:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("unchecked")
	@Compensable
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> doRepay(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail, RepayType repayType,
			PlatformSourceEnums platformSourceEnums) {
		ResponseEntity<?> step1Result = null;
		ResponseEntity<?> step2Result = null;
		int step = 1;
		try{
			logger.info("tcc doRepay entrance, reference qdz#updateQdzAccountBatch, payment#updateAccountInsertTradeAndTransfer, invest#updateForRepay, loan#updateForRepay, 还款操作. 用户标识: {}, 还款计划标识: {}, 标的标识: {}, 还款方式: {}, 还款平台: {}"
					, repayPlan.getRegUserId(), repayPlan.getId(), bidInfo.getId(), repayType, platformSourceEnums.getType());
			// 1、先维护流水、划转、资金账户、活期账户
			logger.info("还款操作步骤: {}, 用户标识: {}, 还款计划标识: {}, 维护流水、划转、资金账户、活期账户.", step++, repayPlan.getRegUserId(), repayPlan.getId());
			step1Result = this.dealFinAccountTradeFlowFundtransfer(repayPlan, receiptPlanList, bidProduct, bidInfo,
					bidInfoDetail, repayType, platformSourceEnums);
			if (step1Result.getResStatus() == ERROR) {
				throw new BusinessException(String.valueOf(step1Result.getResMsg()));
			}
			// 2、维护还款计划、回款计划
			logger.info("还款操作步骤: {}, 用户标识: {}, 还款计划标识: {}, 维护还款计划、回款计划.", step++, repayPlan.getRegUserId(), repayPlan.getId());
			this.dealBidRepayPlanAndReceiptPlan(repayPlan, receiptPlanList, bidInfo, bidInfoDetail, repayType);
			// 3、维护标的状态信息
			logger.info("还款操作步骤: {}, 用户标识: {}, 还款计划标识: {}, 维护标的状态信息, 匹配记录信息.", step++, repayPlan.getRegUserId(), repayPlan.getId());
			step2Result = this.dealBidInfo(repayPlan, bidInfo, bidInfoDetail, repayType);
			if (step2Result.getResStatus() == SUCCESS) {
				List<BidInvest> insertBidInvestList = (List<BidInvest>) step2Result.getParams().get("insertBidInvestList");
				step2Result = this.fitSmsMsg(receiptPlanList, bidProduct, bidInfo);
				// 用于在controller像合同服务发送jms消息、匹配时创建新投资记录对应的合同
				step2Result.getParams().put("insertBidInvestList", insertBidInvestList);
			} else {
				throw new BusinessException(String.valueOf(step2Result.getResMsg()));
			}
			logger.info("tcc doRepay success, reference qdz#updateQdzAccountBatch, payment#updateAccountInsertTradeAndTransfer, invest#updateForRepay, loan#updateForRepay, 还款操作. 用户标识: {}, 还款计划标识: {}, 标的标识: {}, 还款方式: {}, 还款平台: {}"
					, repayPlan.getRegUserId(), repayPlan.getId(), bidInfo.getId(), repayType, platformSourceEnums.getType());
			return step2Result;
		}catch(Exception e){
			logger.error("tcc doRepay error, reference qdz#updateQdzAccountBatch, payment#updateAccountInsertTradeAndTransfer, invest#updateForRepay, loan#updateForRepay, 还款操作, 失败位置: {}. 用户标识: {}, 还款计划标识: {}, 标的标识: {}, 还款方式: {}, 还款平台: {}"
					, --step, repayPlan.getRegUserId(), repayPlan.getId(), bidInfo.getId(), repayType, platformSourceEnums.getType(), e);
			throw new GeneralException("还款失败");
		}
	}

	/**
	 * @Description : 处理账户流水、资金划转、账户金额
	 * @Method_Name : dealFinAccountTradeFlowFundtransfer
	 * @param repayPlan
	 * @param receiptPlanList
	 * @param bidProduct
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @param repayType
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月11日 上午10:26:50
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("unchecked")
	private ResponseEntity<?> dealFinAccountTradeFlowFundtransfer(BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			RepayType repayType, PlatformSourceEnums platformSourceEnums) {
		// 维护流水、划转数据
		ResponseEntity<?> result = this.dealFinTradeFlowAndFundtransfer(repayPlan, receiptPlanList, bidProduct, bidInfo,
				bidInfoDetail, repayType, platformSourceEnums);
		FinTradeFlow finTradeFlow = (FinTradeFlow) result.getParams().get(PARAM_KEY_FIN_TRADE_FLOW);
		List<FinFundtransfer> finFundtransferList = (List<FinFundtransfer>) result.getParams()
				.get(PARAM_KEY_FIN_FUNDTRANSFER_LIST);
		List<QdzAccount> currentAccountList = (List<QdzAccount>) result.getParams().get(PARAM_KEY_CURRENT_ACCOUNT_LIST);
		// 更新活期账户
		if (CommonUtils.isNotEmpty(currentAccountList)) {
			ApplicationContextUtils.getBean(QdzAccountService.class).updateQdzAccountBatch(currentAccountList, currentAccountList.size());
		}
		// 调用支付服务，维护流水、划转
		return ApplicationContextUtils.getBean(FinConsumptionService.class).updateAccountInsertTradeAndTransfer(finTradeFlow, finFundtransferList);
	}

	/**
	 * @Description : 先维护流水、划转
	 * @Method_Name : dealFinTradeFlowAndFundtransfer
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划
	 * @param bidProduct
	 *            标的产品
	 * @param repayType
	 *            还款方式
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月26日 下午3:38:26
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> dealFinTradeFlowAndFundtransfer(BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			RepayType repayType, PlatformSourceEnums platformSourceEnums) {
		logger.info("还款计划标识：{}，组装还款流水、划转开始", repayPlan.getId());
		List<FinFundtransfer> finFundtransferList = new ArrayList<>();
		List<QdzAccount> currentAccountList = new ArrayList<>();
		int repayTradeType = TradeTransferConstants.TRADE_TYPE_REPAY;
		// 初始化还款流水
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(repayPlan.getRegUserId(), repayPlan.getId(),
				repayPlan.getAmount(), repayTradeType, platformSourceEnums);
		switch (repayType) {
		case REPAY_ADVANCE:// 提前还款
            finTradeFlow.setTradeType(TradeTransferConstants.TRADE_TYPE_REPAY_ADVANCE);
            finFundtransferList.addAll(this.dealAdvanceRepayFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList,
					bidProduct, bidInfo, bidInfoDetail, currentAccountList));
			break;
		case REPAY_RISK_RESERVE:// 风险储备金还款
            finTradeFlow.setTradeType(TradeTransferConstants.TRADE_TYPE_REPAY_RISK_RESERVE);
			finFundtransferList.addAll(this.dealRiskReserveRepayFinFundtransfer(finTradeFlow, repayPlan,
					receiptPlanList, bidProduct, currentAccountList));
			break;
		case REPAY_RETURN_RISK_RESERVE:// 还风险储备金
            finTradeFlow.setTradeType(TradeTransferConstants.TRADE_TYPE_REPAY_RETURN_RISK_RESERVE);
            finFundtransferList
					.addAll(this.dealRepayRiskReserveFinFundtransfer(finTradeFlow, repayPlan, bidInfo, bidInfoDetail));
            break;
        default: // 正常还款
            finFundtransferList.addAll(this.dealNormalRepayFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList,
					bidProduct, bidInfo, bidInfoDetail, currentAccountList));
			break;
		}
		//回收体验金
		if (bidProduct.getType() == InvestConstants.BID_PRODUCT_EXPERIENCE
				&& CompareUtil.gtZero(repayPlan.getCapitalAmount())) {
			finFundtransferList.add(FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID,
					repayPlan.getRegUserId(), repayPlan.getCapitalAmount(),
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.CAPITAL)));

		}
		ResponseEntity<?> result = new ResponseEntity<>();
		result.getParams().put(PARAM_KEY_FIN_TRADE_FLOW, finTradeFlow);
		result.getParams().put(PARAM_KEY_FIN_FUNDTRANSFER_LIST, finFundtransferList);
		result.getParams().put(PARAM_KEY_CURRENT_ACCOUNT_LIST, currentAccountList);
		logger.info("还款计划标识：{}，组装还款流水、划转结束", repayPlan.getId());
		return result;
	}

	/**
	 * @Description : 维护正常还款流水、资金划转
	 * @Method_Name : dealNormalRepayFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划
	 * @param bidProduct
	 *            标的产品
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月29日 下午2:18:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> dealNormalRepayFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			List<QdzAccount> currentAccountList) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 处理还款人资金划转
		list.addAll(this.fitRepayFinFundtransfer(finTradeFlow, repayPlan, bidInfo, bidInfoDetail));
		// 处理回款人资金划转
		list.addAll(this.fitReceiptFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, bidProduct,
				currentAccountList));
		// 处理平台账户还资金划转
		list.addAll(this.fitPlatformFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, bidProduct));
		return list;
	}

	/**
	 * @Description : 维护提前还款流水、资金划转
	 * @Method_Name : dealAdvanceRepayFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划
	 * @param bidProduct
	 *            标的产品
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月29日 下午2:19:20
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> dealAdvanceRepayFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			List<QdzAccount> currentAccountList) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 处理还款人资金划转
		list.addAll(this.fitRepayFinFundtransfer(finTradeFlow, repayPlan, bidInfo, bidInfoDetail));
		// 处理回款人资金划转, 设置回款计划的本金
		receiptPlanList.forEach(receiptPlan -> {
			receiptPlan.setCapitalAmount(repayPlan.getCapitalAmount());
			receiptPlan.setInterestAmount(repayPlan.getInterestAmount());
		});
		list.addAll(this.fitReceiptFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, bidProduct,
				currentAccountList));
		// 处理平台账户还资金划转，平台只收取服务费
		if (CompareUtil.gtZero(repayPlan.getServiceCharge())) {
			list.add(FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID,
					repayPlan.getRegUserId(), repayPlan.getServiceCharge(),
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.SERVICE_CHARGE)));
		}
		return list;
	}

	/**
	 * @Description : 维护风险储备金还款的流水、资金划转数据
	 * @Method_Name : dealRiskReserveRepayFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param bidProduct
	 *            标的产品信息
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月29日 下午2:07:23
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> dealRiskReserveRepayFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, List<QdzAccount> currentAccountList) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 处理风险储备金账户还款资金划转
		list.addAll(this.fitPlatformRiskReserveFinFundtransfer(finTradeFlow, repayPlan, RepayType.REPAY_RISK_RESERVE));
		// 处理回款人资金划转
		list.addAll(this.fitReceiptFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, bidProduct,
				currentAccountList));
		// 处理平台账户资金划转
		list.addAll(this.fitPlatformFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, bidProduct));
		return list;
	}

	/**
	 * @Description : 维护借款人还风险储备金的流水、资金划转数据
	 * @Method_Name : dealRepayRiskReserveFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月29日 下午2:06:33
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> dealRepayRiskReserveFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 处理还款人资金划转
		list.addAll(this.fitRepayFinFundtransfer(finTradeFlow, repayPlan, bidInfo, bidInfoDetail));
		// 处理风险储备金账户资金划转
		list.addAll(this.fitPlatformRiskReserveFinFundtransfer(finTradeFlow, repayPlan,
				RepayType.REPAY_RETURN_RISK_RESERVE));
		// 处理平台账户资金划转，平台只收取罚息
		if (CompareUtil.gtZero(repayPlan.getPunishAmount())) {
			list.add(FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID,
					repayPlan.getRegUserId(), repayPlan.getPunishAmount(),
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.PENALTY_INTEREST)));
		}
		return list;
	}

	/**
	 * @Description : 还款人资金流水
	 * @Method_Name : fitRepayFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            :还款计划
	 * @param bidInfo
	 *            :标的信息
	 * @param bidInfoDetail
	 *            :标的详情
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月29日 上午10:14:32
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitRepayFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
		List<FinFundtransfer> list = new ArrayList<>();
		final int regUserId = repayPlan.getRegUserId();
		// 支付本金
		FinTFUtil.fitOutFinFundtransfer(finTradeFlow.getFlowId(), regUserId, repayPlan.getCapitalAmount(), list,
				TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
						FundtransferSmallTypeStateEnum.CAPITAL));
		// 支付利息
		if (bidInfoDetail.getReserveInterest() == BID_DETAIL_RESERVE_INTEREST_YES) { // 预留利息
			// 用冻结金额支付利息
			FinTFUtil.fitOutFinFundtransfer(finTradeFlow.getFlowId(), regUserId, repayPlan.getInterestAmount(), list,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.FROZEN));
			// 判断是否结清 标的金额 = 本次还本金额 + 已还本金额， 如果本次结清，将剩余的冻结利息归还到可用余额中去
            logger.info("还款, 用户标识: {}, 还款标识: {}, 标的总金额: {}, 已还本金: {}, 本次还本: {}", regUserId, repayPlan.getId(), bidInfo.getTotalAmount(), bidInfoDetail.getReturnCapAmount(), repayPlan.getCapitalAmount());
            logger.info("{}", CompareUtil.eq(bidInfo.getTotalAmount(), bidInfoDetail.getReturnCapAmount()) || CompareUtil.eq(bidInfo.getTotalAmount(), repayPlan.getCapitalAmount().add(bidInfoDetail.getReturnCapAmount())));
            if (CompareUtil.eq(bidInfo.getTotalAmount(), bidInfoDetail.getReturnCapAmount()) || CompareUtil.eq
                    (bidInfo.getTotalAmount(), repayPlan.getCapitalAmount().add(bidInfoDetail.getReturnCapAmount()))) {
				// 剩余冻结利息 - 本次已还利息
				BigDecimal residueFreezeInterest = this.calcResidueFreezeMoney(bidInfo)
						.subtract(repayPlan.getInterestAmount());
				logger.info("还款, 用户标识: {}, 还款标识: {}, 剩余冻结利息: {}", residueFreezeInterest);
				// 本次已结清，剩余预留的冻结利息归还到可用余额了
				FinTFUtil.fitOutFinFundtransfer(finTradeFlow.getFlowId(), regUserId, residueFreezeInterest, list,
						TradeTransferConstants.TRANSFER_SUB_CODE_THAW);
			}
		} else {
			FinTFUtil.fitOutFinFundtransfer(finTradeFlow.getFlowId(), regUserId, repayPlan.getInterestAmount(), list,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.INTEREST));
		}
		// 支付服务费
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), regUserId, PLATFORM_ACCOUNT_ID,
				repayPlan.getServiceCharge(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.SERVICE_CHARGE));
		// 支付违约金
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), regUserId, PLATFORM_ACCOUNT_ID,
				repayPlan.getDeditAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.LIQUIDATED_DAMAGES));
		// 支付逾期(罚息付款)
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), regUserId, PLATFORM_ACCOUNT_ID,
				repayPlan.getPunishAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.PENALTY_INTEREST));
		return list;
	}

	/**
	 * @Description : 组装回款人资金流水
	 * @Method_Name : fitReceiptFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param bidProduct
	 *            标的产品
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月29日 下午2:47:14
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitReceiptFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct, List<QdzAccount> currentAccountList) {
		// 处理回款人资金划转
		if (bidProduct.getType() == BID_PRODUCT_CURRENT) {// 活期产品
			return this.fitCurrentReceiptFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList, currentAccountList);
		} else {
			return this.fitNormalReceiptFinFundtransfer(finTradeFlow, repayPlan, receiptPlanList);
		}
	}

	/**
	 * @Description : 组装普通回款人资金流水
	 * @Method_Name : fitReceiptFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月26日 上午10:14:45
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitNormalReceiptFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList) {
		List<FinFundtransfer> list = new ArrayList<>();
		final int regUserId = repayPlan.getRegUserId();
		for (BidReceiptPlan receiptPlan : receiptPlanList) {
			// 收取本金
			FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), receiptPlan.getRegUserId(), regUserId,
					receiptPlan.getCapitalAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.CAPITAL));
			// 收取利息
			FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), receiptPlan.getRegUserId(), regUserId,
					receiptPlan.getInterestAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.INTEREST));
			// 收取加息收益
			FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), receiptPlan.getRegUserId(), PLATFORM_ACCOUNT_ID,
					receiptPlan.getIncreaseAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.INCREASE_INTEREST));
		}
		return list;
	}

	/**
	 * @Description : 组装活期回款人资金流水
	 * @Method_Name : fitReceiptCurrentFinFundtransfer
	 * @param finTradeFlow
	 *            :交易流水
	 * @param repayPlan
	 *            :还款计划
	 * @param receiptPlanList
	 *            :回款计划集合
	 * @param currentAccountList
	 *            :需要更细的钱袋子账户信息
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月29日 上午10:21:17
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitCurrentReceiptFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, List<QdzAccount> currentAccountList) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 第三方账户id
		int thirdPartyRegUserId = receiptPlanList.get(0).getRegUserId();
		// 还款人用户id
		final int regUserId = repayPlan.getRegUserId();
		// 回款利息和
		BigDecimal receiptInterestAmount = BigDecimal.ZERO;
		for (BidReceiptPlan bidReceipPlan : receiptPlanList) {
			// 第三方账户将回款本金,利息直接转入到资金账户
			if (thirdPartyRegUserId == bidReceipPlan.getRegUserId()) {
				// 收取本金
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(), regUserId,
						bidReceipPlan.getCapitalAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
								FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.CAPITAL));
				// 收取利息
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(), regUserId,
						bidReceipPlan.getInterestAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
								FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.INTEREST));
			} else { // 普通活期用户
				// 回款本金：先转入投资人资金账户，再转出到投资人活期账户的剩余债券金额里，用于下一次的债券转让
				// 转入本金
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(), regUserId,
						bidReceipPlan.getCapitalAmount(), list,
						TradeTransferConstants.TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_CAPITAL,PaymentConstants.SHOW_FRONT_NO);
				// 转出本金
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(),
						bidReceipPlan.getRegUserId(), bidReceipPlan.getCapitalAmount(), list,
						TradeTransferConstants.TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_CAPITAL,PaymentConstants.SHOW_FRONT_NO);
				// 回款利息：先转入投资人资金账户，在转出到第三方账户的资金账户里
				// 投资人转入利息
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(), regUserId,
						bidReceipPlan.getInterestAmount(), list,
						TradeTransferConstants.TRANSFER_SUB_CODE_TURNS_IN_QDZ_REPAY_INTEREST,PaymentConstants.SHOW_FRONT_NO);
				// 投资人转出利息
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), bidReceipPlan.getRegUserId(),
						thirdPartyRegUserId, bidReceipPlan.getInterestAmount(), list,
						TradeTransferConstants.TRANSFER_SUB_CODE_TURNS_OUT_QDZ_REPAY_INTEREST,PaymentConstants.SHOW_FRONT_NO);
				// 第三方账户转入利息
				FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), thirdPartyRegUserId,
						bidReceipPlan.getRegUserId(), bidReceipPlan.getInterestAmount(), list,
						TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
								FundtransferSmallTypeStateEnum.INTEREST));

				// 更新活期账户剩余债券
				QdzAccount qdzAccount = new QdzAccount();
				qdzAccount.setRegUserId(bidReceipPlan.getRegUserId());
				qdzAccount.setCreditorMoney(bidReceipPlan.getCapitalAmount());
				currentAccountList.add(qdzAccount);
			}
			receiptInterestAmount = receiptInterestAmount.add(bidReceipPlan.getInterestAmount());
		}
		// 维护第三方账户本次还款操作中获取的总的利息差 差值 = 标还款总利息 - 总的回款利息
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), thirdPartyRegUserId, null,
				repayPlan.getInterestAmount().subtract(receiptInterestAmount), list,
				TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
						FundtransferSmallTypeStateEnum.INTEREST_DIFFERENTIAL));
		return list;
	}

	/**
	 * @Description : 组装平台资金划转
	 * @Method_Name : fitPlatformFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月27日 上午10:25:16
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitPlatformFinFundtransfer(FinTradeFlow finTradeFlow, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList, BidProduct bidProduct) {
		List<FinFundtransfer> list = new ArrayList<>();
		final int regUserId = repayPlan.getRegUserId();
		// 平台收取罚息
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID, regUserId,
				repayPlan.getPunishAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.PENALTY_INTEREST));
		// 平台收取服务费
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID, regUserId,
				repayPlan.getServiceCharge(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.SERVICE_CHARGE));
		// 平台收取违约金
		FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID, regUserId,
				repayPlan.getDeditAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.LIQUIDATED_DAMAGES));
		for (BidReceiptPlan receiptPlan : receiptPlanList) {
			// 平台支付加息付款
			FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID, receiptPlan.getRegUserId(),
					receiptPlan.getIncreaseAmount(), list, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.INCREASE_INTEREST));
			if (bidProduct.getType() != BID_PRODUCT_CURRENT) {// 活期产品
				// 处理匹配时产生的利息差 利息差为正值 则优先标年化率>散标年化率 平台垫付利息差 为负值 则优先标年化率<散标年化率
				// 平台收取
				if (CompareUtil.gtZero(receiptPlan.getYearrateDifferenceMoney().abs())) {
					if (CompareUtil.gtZero(receiptPlan.getYearrateDifferenceMoney())) { // 平台支付利息差
						FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID,
								receiptPlan.getRegUserId(), receiptPlan.getYearrateDifferenceMoney(), list,
								TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
										FundtransferSmallTypeStateEnum.INTEREST_DIFFERENTIAL));
					} else { // 平台收取利息差
						FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_ACCOUNT_ID, regUserId,
								receiptPlan.getYearrateDifferenceMoney(), list,
								TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
										FundtransferSmallTypeStateEnum.INTEREST_DIFFERENTIAL));
					}
				}
			}
		}
		return list;
	}

	/**
	 * @Description : 组装平台风险储备金资金划转
	 * @Method_Name : fitPlatformFinFundtransfer
	 * @param finTradeFlow
	 *            交易流水
	 * @param repayPlan
	 *            还款计划
	 * @param repayType
	 *            储备金资金划转类型 1 ： 风险储备金替用户还款， 0 ： 借款人还款给风险储备金
	 * @return : List<FinFundtransfer>
	 * @Creation Date : 2017年6月29日 上午10:25:16
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinFundtransfer> fitPlatformRiskReserveFinFundtransfer(FinTradeFlow finTradeFlow,
			BidRepayPlan repayPlan, RepayType repayType) {
		List<FinFundtransfer> list = new ArrayList<>();
		// 需要维护的风险出本金金额 = 本金 + 利息 + 服务费
		BigDecimal money = repayPlan.getCapitalAmount().add(repayPlan.getInterestAmount())
				.add(repayPlan.getServiceCharge());
		if (repayType == RepayType.REPAY_RISK_RESERVE) { // 风险储备金还款
			// 风险储备金还款
			FinTFUtil.fitOutFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_RISK_RESERVE_ID, money, list,
					TradeTransferConstants.TRANSFER_SUB_CODE_PAY);
		} else { // 借款人还风险储备金
			// 风险储备金收款
			FinTFUtil.fitFinFundtransfer(finTradeFlow.getFlowId(), PLATFORM_RISK_RESERVE_ID, repayPlan.getRegUserId(),
					money, list, TradeTransferConstants.TRANSFER_SUB_CODE_INCOME);
		}
		return list;
	}

	/**
	 * @Description : 维护借款人、投资人、平台资金账户信息 测试通过后此方法及相关方法均可删除
	 * @Method_Name : dealFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划
	 * @param bidInfoDetail
	 *            标的详情
	 * @return : List<FinAccount>
	 * @Creation Date : 2017年6月27日 下午4:27:55
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Deprecated
	private ResponseEntity<?> dealFinAccount(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail, RepayType repayType) {
		logger.info("还款计划标识：{}，组装资金账户数据开始", repayPlan.getId());
		// 用户资金账户
		List<FinAccount> finAccountList = new ArrayList<>();
		// 活期资金账户
		List<QdzAccount> currentAccountList = new ArrayList<>();
		switch (repayType) {
		case REPAY_ADVANCE:// 提前还款
			this.dealAdvanceRepayFinAccount(repayPlan, receiptPlanList, bidProduct, bidInfo, bidInfoDetail,
					finAccountList, currentAccountList);
			break;
		case REPAY_RISK_RESERVE:// 风险储备金还款
			this.dealRiskReserveRepayFinAccount(repayPlan, receiptPlanList, bidProduct, finAccountList,
					currentAccountList);
			break;
		case REPAY_RETURN_RISK_RESERVE:// 还风险储备金
			this.dealRepayRiskReserveFinAccount(repayPlan, bidInfo, bidInfoDetail, finAccountList);
			break;
		default: // 正常还款
			this.dealNormalRepayFinAccount(repayPlan, receiptPlanList, bidProduct, bidInfo, bidInfoDetail,
					finAccountList, currentAccountList);
			break;
		}
		// 平台账户回收体验金
		if (bidProduct.getType() == InvestConstants.BID_PRODUCT_EXPERIENCE
				&& CompareUtil.gtZero(repayPlan.getCapitalAmount())) {
			finAccountList.add(this.createFinAccount(PLATFORM_ACCOUNT_ID, repayPlan.getCapitalAmount()));
		}
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		result.getParams().put(PARAM_KEY_FIN_ACCOUNT_LIST, finAccountList);
		result.getParams().put(PARAM_KEY_CURRENT_ACCOUNT_LIST, currentAccountList);
		logger.info("还款计划标识：{}，组装资金账户数据结束", repayPlan.getId());
		return result;
	}

	/**
	 * @Description : 正常还款
	 * @Method_Name : dealNormalRepayFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param bidProduct
	 *            标的产品
	 * @param bidInfoDetail
	 *            标的详情
	 * @param finAccountList
	 *            资金账户（还款人、回款人、平台、第三方账户、风险储备金账户）
	 * @param currentAccountList
	 *            活期资金账户
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午4:26:05
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealNormalRepayFinAccount(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail, List<FinAccount> finAccountList,
			List<QdzAccount> currentAccountList) {
		// 组装还款人账户信息
		finAccountList.add(this.fitRepayFinAccount(repayPlan, bidInfo, bidInfoDetail));
		// 组装回款人账户信息
		this.fitReceiptFinAccount(repayPlan, receiptPlanList, bidProduct, finAccountList, currentAccountList);
		// 组装平台账户信息
		finAccountList.add(this.fitPlatformFinAccount(bidProduct, repayPlan, receiptPlanList));
	}

	/**
	 * @Description : 提前还款
	 * @Method_Name : dealAdvanceRepayFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param bidProduct
	 *            标的产品
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午3:33:38
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealAdvanceRepayFinAccount(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, BidInfo bidInfo, BidInfoDetail bidInfoDetail, List<FinAccount> finAccountList,
			List<QdzAccount> currentAccountList) {
		// 组装还款人的账户信息
		finAccountList.add(this.fitAdvanceRepayFinAccount(repayPlan, bidInfo, bidInfoDetail));
		// 组装回款人账户信息
		receiptPlanList.forEach(receiptPlan -> {
			receiptPlan.setAmount(repayPlan.getCapitalAmount().add(repayPlan.getInterestAmount()));
			receiptPlan.setCapitalAmount(repayPlan.getCapitalAmount());
			receiptPlan.setInterestAmount(repayPlan.getInterestAmount());
		});
		this.fitReceiptFinAccount(repayPlan, receiptPlanList, bidProduct, finAccountList, currentAccountList);
		// 组装平台账户信息
		finAccountList.add(this.fitPlatformFinAccount(bidProduct, repayPlan, receiptPlanList));
	}

	/**
	 * @Description : 风险储备金还款
	 * @Method_Name : dealRiskReserveRepayFinAccount
	 * @param repayPlan  还款计划
	 * @param receiptPlanList  回款计划集合
	 * @param bidProduct  标的产品
	 * @param finAccountList  资金账户（还款人、回款人、平台、第三方账户、风险储备金账户）
	 * @param currentAccountList  活期资金账户
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午4:28:01
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealRiskReserveRepayFinAccount(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, List<FinAccount> finAccountList, List<QdzAccount> currentAccountList) {
		// 组装风险储备金还款的账户信息
		finAccountList.add(this.fitPlatformRiskReserveFinAccount(repayPlan, RepayType.REPAY_RISK_RESERVE));
		// 组装回款人账户信息
		this.fitReceiptFinAccount(repayPlan, receiptPlanList, bidProduct, finAccountList, currentAccountList);
		// 组装平台账户信息
		finAccountList.add(this.fitPlatformFinAccount(bidProduct, repayPlan, receiptPlanList));
	}

	/**
	 * 借款人还风险储备金
	 * 
	 * @Description :
	 * @Method_Name : dealRepayRiskReserveFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param bidInfoDetail
	 *            标的详情
	 * @param finAccountList
	 *            资金账户（还款人、回款人、平台、第三方账户、风险储备金账户）
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午4:28:13
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealRepayRiskReserveFinAccount(BidRepayPlan repayPlan, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			List<FinAccount> finAccountList) {
		// 组装还款人账户信息
		finAccountList.add(this.fitRepayFinAccount(repayPlan, bidInfo, bidInfoDetail));
		// 组装风险储备金收款账户信息
		finAccountList.add(this.fitPlatformRiskReserveFinAccount(repayPlan, RepayType.REPAY_RETURN_RISK_RESERVE));
		// 组装平台账户信息，此时平台账户只收取罚息
		if (CompareUtil.gtZero(repayPlan.getPunishAmount())) {
			finAccountList.add(this.createFinAccount(PLATFORM_ACCOUNT_ID, repayPlan.getPunishAmount()));
		}
	}

	/**
	 * @Description : 组装回款人资金账户信息|活期账户的资金账户信息
	 * @Method_Name : fitReceiptFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param bidProduct
	 *            标的产品
	 * @param finAccountList
	 *            资金账户（还款人、回款人、平台、第三方账户、风险储备金账户）
	 * @param currentAccountList
	 *            活期资金账户
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午4:28:22
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void fitReceiptFinAccount(BidRepayPlan repayPlan, List<BidReceiptPlan> receiptPlanList,
			BidProduct bidProduct, List<FinAccount> finAccountList, List<QdzAccount> currentAccountList) {
		// 组装回款人账户信息
		if (bidProduct.getType() == BID_PRODUCT_CURRENT) {// 活期产品
			// 获得回款的第三方账户 ， 获得活期的回款人的活期资金账户集合
			this.fitCurrentReceiptFinAccount(repayPlan, receiptPlanList, finAccountList, currentAccountList);
		} else {
			finAccountList.addAll(this.fitNormalReceiptFinAccount(receiptPlanList));
		}
	}

	/**
	 * @Description : 组装借款人资金账户信息
	 * @Method_Name : fitRepayFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param bidInfoDetail
	 *            标的详情
	 * @return : FinAccount
	 * @Creation Date : 2017年6月29日 下午4:28:35
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private FinAccount fitRepayFinAccount(BidRepayPlan repayPlan, BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
		FinAccount repayFinAccount = new FinAccount();
		repayFinAccount.setRegUserId(repayPlan.getRegUserId());
		BigDecimal amount = repayPlan.getAmount();
		BigDecimal interestAmount = repayPlan.getInterestAmount();
		if (bidInfoDetail.getReserveInterest() == BID_DETAIL_RESERVE_INTEREST_YES) {
			// 利息规则计算规则：如果是最后一期还款，利息 = 总利息 - 已还利息，否则 利息 =
			// 还款计划中的利息，防止部分提前还款时，预留利息的情况下，还有部分利息未解冻的情况
			if (CompareUtil.gtZero(repayPlan.getCapitalAmount())) {
				interestAmount = this.calcResidueFreezeMoney(bidInfo);
			}
			// 预留利息，利息从冻结金额中扣除
			repayFinAccount.setNowMoney(amount.negate());
			repayFinAccount.setUseableMoney(amount.negate().add(interestAmount));
			repayFinAccount.setFreezeMoney(interestAmount.negate());
		} else {
			repayFinAccount.setNowMoney(amount.negate());
			repayFinAccount.setUseableMoney(amount.negate());
		}
		return repayFinAccount;
	}

	/**
	 * @Description :组装提前还款的资金账户数据
	 * @Method_Name : fitAdvanceRepayFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param bidInfo
	 *            标的信息
	 * @param bidInfoDetail
	 *            标的详情
	 * @return : FinAccount
	 * @Creation Date : 2017年7月4日 上午10:41:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private FinAccount fitAdvanceRepayFinAccount(BidRepayPlan repayPlan, BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
		FinAccount finAccount = new FinAccount();
		finAccount.setRegUserId(repayPlan.getRegUserId());
		BigDecimal amount = repayPlan.getAmount();
		BigDecimal interestAmount = repayPlan.getInterestAmount();
		if (bidInfoDetail.getReserveInterest() == BID_DETAIL_RESERVE_INTEREST_YES) { // 预留利息
			// 判断是否结清 标的金额 = 本次还本金额 + 已还本金额
			if (CompareUtil.eq(bidInfo.getTotalAmount(),
					repayPlan.getCapitalAmount().add(bidInfoDetail.getReturnCapAmount()))) {
				BigDecimal residueFreezeInterest = this.calcResidueFreezeMoney(bidInfo);
				// 本次已结清，剩余预留的冻结利息归还到可用余额了
				finAccount.setNowMoney(amount.negate());
				finAccount.setUseableMoney(amount.negate().add(residueFreezeInterest));
				finAccount.setFreezeMoney(residueFreezeInterest.negate());
			} else { // 未结清
				// 预留利息，利息从冻结金额中扣除
				finAccount.setNowMoney(amount.subtract(interestAmount).negate());
				finAccount.setUseableMoney(amount.subtract(interestAmount).negate());
				finAccount.setFreezeMoney(interestAmount.negate());
			}
		} else { // 未预留利息
			finAccount.setNowMoney(amount.negate());
			finAccount.setUseableMoney(amount.negate());
		}
		return finAccount;
	}

	/**
	 * @Description : 组装普通回款人资金账户信息
	 * @Method_Name : fitNormalReceiptFinAccount
	 * @param receiptPlanList
	 *            回款计划集合
	 * @return : List<FinAccount>
	 * @Creation Date : 2017年6月29日 下午4:28:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<FinAccount> fitNormalReceiptFinAccount(List<BidReceiptPlan> receiptPlanList) {
		List<FinAccount> receiptFinAccountList = new ArrayList<>();
		for (BidReceiptPlan receiptPlan : receiptPlanList) {
			receiptFinAccountList.add(this.createFinAccount(receiptPlan.getRegUserId(), receiptPlan.getAmount()));
		}
		return receiptFinAccountList;
	}

	/**
	 * @Description : 组装回款人活期资金账户信息
	 * @Method_Name : fitCurrentReceiptFinAccount
	 * @param bidRepayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @param finAccountList
	 *            资金账户（还款人、回款人、平台、第三方账户、风险储备金账户）
	 * @param currentAccountList
	 *            活期资金账户
	 * @return : void
	 * @Creation Date : 2017年6月29日 下午4:28:54
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void fitCurrentReceiptFinAccount(BidRepayPlan bidRepayPlan, List<BidReceiptPlan> receiptPlanList,
			List<FinAccount> finAccountList, List<QdzAccount> currentAccountList) {
		// 第三方账户id
		int thirdPartyRegUserId = receiptPlanList.get(0).getRegUserId();
		// 普通用户的回款本金和
		BigDecimal total = BigDecimal.ZERO;
		for (BidReceiptPlan receiptPlan : receiptPlanList) {
			// 第三方账户
			if (receiptPlan.getRegUserId() == thirdPartyRegUserId) {
				continue;
			} else { // 普通用户
				// 回款本金归还改投资人的活期资金账户，用于下一次债权转让
				if (CompareUtil.gtZero(receiptPlan.getCapitalAmount())) {
					total = total.add(receiptPlan.getCapitalAmount());
					// 更新活期账户剩余债券
					QdzAccount qdzAccount = new QdzAccount();
					qdzAccount.setRegUserId(receiptPlan.getRegUserId());
					qdzAccount.setCreditorMoney(receiptPlan.getCapitalAmount());
					currentAccountList.add(qdzAccount);
				}
			}
		}
		// 第三方账户的实际回款金额 = 标的回款金额 - 普通用户回收的本金 - 服务费 - 逾期金额 - 违约金
		BigDecimal money = bidRepayPlan.getAmount().subtract(total).subtract(bidRepayPlan.getServiceCharge())
				.subtract(bidRepayPlan.getPunishAmount()).subtract(bidRepayPlan.getDeditAmount());
		finAccountList.add(this.createFinAccount(thirdPartyRegUserId, money));
	}

	/**
	 * @Description : 组装平台资金账户信息
	 * @Method_Name : fitPlatformFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param receiptPlanList
	 *            回款计划集合
	 * @return : FinAccount
	 * @Creation Date : 2017年6月29日 下午4:29:06
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private FinAccount fitPlatformFinAccount(BidProduct bidProduct, BidRepayPlan repayPlan,
			List<BidReceiptPlan> receiptPlanList) {
		BigDecimal amount = BigDecimal.ZERO;
		// 平台收取罚息
		if (CompareUtil.gtZero(repayPlan.getPunishAmount())) {
			amount = amount.add(repayPlan.getPunishAmount());
		}
		// 收取服务费
		if (CompareUtil.gtZero(repayPlan.getServiceCharge())) {
			amount = amount.add(repayPlan.getServiceCharge());
		}
		// 收取违约金
		if (CompareUtil.gtZero(repayPlan.getDeditAmount())) {
			amount = amount.add(repayPlan.getDeditAmount());
		}
		for (BidReceiptPlan receiptPlan : receiptPlanList) {
			// 支付用户的加息收益
			if (CompareUtil.gtZero(receiptPlan.getIncreaseAmount())) {
				amount = amount.subtract(receiptPlan.getIncreaseAmount());
			}
			if (bidProduct.getType() != BID_PRODUCT_CURRENT) {
				// 利息差值计算
				if (CompareUtil.gtZero(receiptPlan.getYearrateDifferenceMoney().abs())) {
					amount = amount.add(receiptPlan.getYearrateDifferenceMoney());
				}
			}
		}
		return this.createFinAccount(PLATFORM_ACCOUNT_ID, amount);
	}

	/**
	 * @Description : 组装平台风险储备金资金账户信息
	 * @Method_Name : fitPlatformRiskReserveFinAccount
	 * @param repayPlan
	 *            还款计划
	 * @param repayType
	 *            还款类型
	 * @return : FinAccount
	 * @Creation Date : 2017年6月29日 下午4:29:14
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private FinAccount fitPlatformRiskReserveFinAccount(BidRepayPlan repayPlan, RepayType repayType) {
		// 需要维护的风险出本金金额 = 本金 + 利息 + 服务费
		BigDecimal amount = repayPlan.getCapitalAmount().add(repayPlan.getInterestAmount())
				.add(repayPlan.getServiceCharge());
		if (repayType == RepayType.REPAY_RISK_RESERVE) {
			return createFinAccount(PLATFORM_RISK_RESERVE_ID, amount.negate());
		} else {
			return createFinAccount(PLATFORM_RISK_RESERVE_ID, amount);
		}
	}

	/**
	 * @Description : 创建资金账户
	 * @Method_Name : createFinAccount
	 * @param regUserId
	 *            用户id
	 * @param amount
	 *            交易金额
	 * @return : FinAccount
	 * @Creation Date : 2017年6月29日 上午11:05:16
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private FinAccount createFinAccount(int regUserId, BigDecimal amount) {
		FinAccount finAccount = new FinAccount();
		finAccount.setRegUserId(regUserId);
		finAccount.setNowMoney(amount);
		finAccount.setUseableMoney(amount);
		return finAccount;
	}

	/**
	 * @Description : 更新标的和标的详情信息
	 * @Method_Name : dealBidInfo
	 * @param bidRepayPlan
	 *            还款计划
	 * @param bidInfo
	 *            标的信息
	 * @param bidInfoDetail
	 *            标的详情
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午2:38:23
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> dealBidInfo(BidRepayPlan bidRepayPlan, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
			RepayType repayType) {
		List<BidMatch> insertBidMatchList = new ArrayList<>();
		List<BidMatch> updateBidMatchList = new ArrayList<>();
		List<BidInvest> insertBidInvestList = new ArrayList<>();
		List<BidInvest> updateBidInvestList = new ArrayList<>();
		List<BidInfoDetail> updateBidInfoDetailList = new ArrayList<>();
		BidInfo bidInfoTmp = new BidInfo();
		bidInfoTmp.setId(bidInfo.getId());
		bidInfoTmp.setState(bidInfo.getState());
		// 本金全部还完
		if (CompareUtil.eq(bidInfo.getTotalAmount(), bidInfoDetail.getReturnCapAmount()) ||
                CompareUtil.eq(bidInfo.getTotalAmount(), bidRepayPlan.getCapitalAmount().add(bidInfoDetail.getReturnCapAmount()))) {
			bidInfoTmp.setState(BID_STATE_WAIT_FINISH);
		}
		if (CompareUtil.gtZero(bidRepayPlan.getCapitalAmount())) {
			bidInfoDetail.setReturnCapAmount(bidRepayPlan.getCapitalAmount());
		}else{
		    //如果不是提前还本，那么此属性设置为null防止，还本字段更新
            bidInfoDetail.setReturnCapAmount(null);
        }
		// 提前还本处理匹配投资记录
		if (repayType == RepayType.REPAY_ADVANCE) {
			logger.info("dealBidInfo, 还款操作, 提前还本处理匹配投资记录. 用户标识: {}, 还款计划标识: {}.", bidRepayPlan.getRegUserId(), bidRepayPlan.getId());
			this.dealMatchBidInvest(insertBidMatchList, updateBidMatchList, insertBidInvestList, updateBidInvestList,
					updateBidInfoDetailList, bidInfo, bidRepayPlan.getCapitalAmount());
		}
		updateBidInfoDetailList.add(bidInfoDetail);
		return this.bidInfoService.updateForRepay(bidInfoTmp, updateBidInfoDetailList, insertBidMatchList,
				updateBidMatchList, insertBidInvestList, updateBidInvestList);
	}

	/**
	 * @Description : 维护还款计划、回款计划
	 * @Method_Name : dealBidRepayPlanAndReceiptPlan
	 * @param bidRepayPlan
	 * @param receiptPlanList
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @param repayType
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午11:16:59
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealBidRepayPlanAndReceiptPlan(BidRepayPlan bidRepayPlan, List<BidReceiptPlan> receiptPlanList,
			BidInfo bidInfo, BidInfoDetail bidInfoDetail, RepayType repayType) {
		List<BidReceiptPlan> list = null;
		BidRepayPlan bidRepayPlanTmp;
		switch (repayType) {
		case REPAY_ADVANCE:// 提前还款
			this.dealAdvanceBidRepayPlanAndReceiptPlan(bidRepayPlan, receiptPlanList, bidInfo, bidInfoDetail);
			return;
		case REPAY_RISK_RESERVE:// 风险储备金还款
			bidRepayPlanTmp = this.fitRepayPlan(bidRepayPlan, REPAY_STATE_RISK_RESERVE, null, new Date());
			list = this.fitReceiptPlan(receiptPlanList);
			break;
		case REPAY_RETURN_RISK_RESERVE:// 还风险储备金
			bidRepayPlanTmp = this.fitRepayPlan(bidRepayPlan, REPAY_STATE_FINISH, new Date(), null);
			break;
		default: // 正常还款
			bidRepayPlanTmp = this.fitRepayPlan(bidRepayPlan, REPAY_STATE_FINISH, new Date(), null);
			list = this.fitReceiptPlan(receiptPlanList);
			break;
		}
		// 更新还款计划、回款计划
		ApplicationContextUtils.getBean(BidRepayPlanService.class).updateForRepay(bidRepayPlanTmp, list);
	}

	/**
	 * @Description : 处理提前还本的的还款计划、回款计划
	 * @Method_Name : dealAdvanceBidRepayPlanAndReceiptPlan
	 * @param bidRepayPlan
	 * @param receiptPlanList
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午11:15:26
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("unchecked")
	private void dealAdvanceBidRepayPlanAndReceiptPlan(BidRepayPlan bidRepayPlan, List<BidReceiptPlan> receiptPlanList,
			BidInfo bidInfo, BidInfoDetail bidInfoDetail) {
		List<BidRepayPlan> insertRepayPlanList = new ArrayList<>();
		List<BidRepayPlan> delRepayPlanList = new ArrayList<>();
		List<BidReceiptPlan> insertReceiptPlanList = new ArrayList<>();
		List<BidReceiptPlan> delReceiptPlanList = new ArrayList<>();
		// 组装同期数的新的提前还本还款计划&回款计划，用于记录在哪一期执行的提前还本操作
		insertRepayPlanList.add(this.fitNewAdvanceRepayPlan(bidRepayPlan));
		insertReceiptPlanList.addAll(this.fitNewAdvanceReceiptPlan(bidRepayPlan, receiptPlanList));
		//组装要删除的还款计划&回款计划
        int periods = bidRepayPlan.getPeriods();
        // 要删除的未还款的还款计划
        BidRepayPlan repayPlanTmp = new BidRepayPlan();
        repayPlanTmp.setBidId(bidRepayPlan.getBidId());
        repayPlanTmp.setPeriods(periods);
        repayPlanTmp.setState(REPAY_STATE_NONE);
        delRepayPlanList.add(repayPlanTmp);
        // 要删除的未回款的回款计划
        BidReceiptPlan receiptPlanTmp = new BidReceiptPlan();
        receiptPlanTmp.setBidId(bidRepayPlan.getBidId());
        receiptPlanTmp.setPeriods(periods);
        receiptPlanTmp.setState(RECEIPT_STATE_NONE);
        delReceiptPlanList.add(receiptPlanTmp);
		// 本金全部还完(提前结清)，直接更新还款计划和回款计划
		bidInfoDetail.setReturnCapAmount(bidInfoDetail.getReturnCapAmount().add(bidRepayPlan.getCapitalAmount()));
        // 部分还本
        if (CompareUtil.gt(bidInfo.getTotalAmount(), bidInfoDetail.getReturnCapAmount())) {
			// 获得新的还款计划、回款计划
			ResponseEntity<?> result = this.getNewRepayPlanAndReceiptPlan(bidRepayPlan, bidInfo, bidInfoDetail);
			List<BidRepayPlan> newBidRepayPlanList = (List<BidRepayPlan>) result.getParams().get("repayPlanList");
			int index = periods;
			// 更新还款计划期数
			for (BidRepayPlan newBidRepayPlan : newBidRepayPlanList) {
				newBidRepayPlan.setPeriods(index++);
			}
			insertRepayPlanList.addAll(newBidRepayPlanList);
			List<BidReceiptPlan> newBidReceiptPlanList = (List<BidReceiptPlan>) result.getParams().get("recieptPlanList");
			// 更新回款计划期数
			index = periods;
			for (BidReceiptPlan newBidReceiptPlan : newBidReceiptPlanList) {
				newBidReceiptPlan.setPeriods(index++);
			}
			insertReceiptPlanList.addAll(newBidReceiptPlanList);
		}
		// 插入提前还本记录
		BidReturnCapRecord bidReturnCapRecord = new BidReturnCapRecord();
		bidReturnCapRecord.setBidId(bidRepayPlan.getBidId());
		bidReturnCapRecord.setReturnCapAmount(bidRepayPlan.getCapitalAmount());
		bidReturnCapRecord.setOriginCapAmount(bidInfo.getTotalAmount().subtract(bidInfoDetail.getReturnCapAmount()));
		// 更新、插入、删除还款计划&回款计划
        ApplicationContextUtils.getBean(BidRepayPlanService.class).updateForAdvanceRepaycaptail(insertRepayPlanList, 
                delRepayPlanList, insertReceiptPlanList, delReceiptPlanList, bidReturnCapRecord);
	}

	/**
	 * @Description : 组装提前还款的还款计划
	 * @Method_Name : fitNewAdvanceRepayPlan
	 * @param bidRepayPlan
	 * @return : BidRepayPlan
	 * @Creation Date : 2017年7月5日 上午10:42:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BidRepayPlan fitNewAdvanceRepayPlan(BidRepayPlan bidRepayPlan) {
		BidRepayPlan newRepayPlan = new BidRepayPlan();
		BeanUtils.copyProperties(bidRepayPlan, newRepayPlan);
		newRepayPlan.setState(REPAY_STATE_ADVANCE_CAPITAL);
		newRepayPlan.setActualTime(new Date());
		newRepayPlan.setResidueAmount(bidRepayPlan.getResidueAmount().subtract(bidRepayPlan.getCapitalAmount()));
		return newRepayPlan;
	}

	/**
	 * @Description : 组装提前还本的回款计划
	 * @Method_Name : fitNewAdvanceReceiptPlan
	 * @param bidRepayPlan
	 * @param receiptPlanList
	 * @return : List<BidReceiptPlan>
	 * @Creation Date : 2017年7月5日 上午10:43:10
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<BidReceiptPlan> fitNewAdvanceReceiptPlan(BidRepayPlan bidRepayPlan,
			List<BidReceiptPlan> receiptPlanList) {
		List<BidReceiptPlan> list = new ArrayList<>();
		for (BidReceiptPlan receiptPlanTmp : receiptPlanList) {
			BidReceiptPlan receiptPlan = new BidReceiptPlan();
			BeanUtils.copyProperties(receiptPlanTmp, receiptPlan);
			receiptPlan.setId(null);
			receiptPlan.setAmount(bidRepayPlan.getCapitalAmount().add(bidRepayPlan.getInterestAmount()));
			receiptPlan.setCapitalAmount(bidRepayPlan.getCapitalAmount());
			receiptPlan.setInterestAmount(bidRepayPlan.getInterestAmount());
			receiptPlan.setActualTime(new Date());
			receiptPlan.setState(RECEIPT_STATE_FINISH);
			list.add(receiptPlan);
		}
		return list;
	}

	/**
	 * @Description : 获得新的还款计划、回款计划
	 * @Method_Name : getNewRepayPlanAndReceiptPlan
	 * @param repayPlan
	 * @param bidInfo
	 * @param bidInfoDetail
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月5日 下午1:43:34
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> getNewRepayPlanAndReceiptPlan(BidRepayPlan repayPlan, BidInfo bidInfo,
			BidInfoDetail bidInfoDetail) {
		BidCommonPlanVo planVo = new BidCommonPlanVo();
		planVo.setBidId(bidInfo.getId());
		planVo.setTotalAmount(bidInfo.getTotalAmount().subtract(bidInfoDetail.getReturnCapAmount()));
		planVo.setTermUnit(bidInfo.getTermUnit());
		planVo.setTermValue(bidInfo.getTermValue() - repayPlan.getPeriods() + 1);
		planVo.setInterestRate(bidInfo.getInterestRate());
		planVo.setServiceRate(bidInfo.getServiceRate());
		planVo.setRegUserId(bidInfo.getBorrowerId());
		planVo.setRepayUserId(bidInfo.getRepayUserId());
		planVo.setRepayType(bidInfo.getBiddRepaymentWay());
		planVo.setCompanyId(bidInfo.getCompanyId());
		planVo.setLendingTime(DateUtils.addMonth(bidInfo.getLendingTime(), repayPlan.getPeriods() - 1));
		List<BidInvest> bidInvestList = bidInvestService.findBidInvestListByBidId(bidInfo.getId());
		List<BidInvestVo> vos = new ArrayList<>();
		for (BidInvest bidInvest : bidInvestList) {
			BidInvestVo vo = new BidInvestVo();
			vo.setRegUserId(bidInvest.getRegUserId());
			// 匹配后回款计划一期的只有一条，即中间账户投资散标的回款计划
			vo.setInvestAmount(bidInvest.getInvestAmount().subtract(bidInfoDetail.getReturnCapAmount()));
			vo.setId(bidInvest.getId());
			vos.add(vo);
		}
		return RepayAndReceiptUtils.initRepayPlan(planVo, vos,RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT);
	}




	/**
	 * @Description : 组装还款计划数据
	 * @Method_Name : fitRepayPlan
	 * @param bidRepayPlan
	 *            还款计划
	 * @param state
	 *            还款状态
	 * @param actualTime
	 *            实际还款时间
	 * @param riskaccountTime
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午11:05:07
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BidRepayPlan fitRepayPlan(BidRepayPlan bidRepayPlan, int state, Date actualTime, Date riskaccountTime) {
		BidRepayPlan bidRepayPlanTmp = new BidRepayPlan();
		bidRepayPlanTmp.setId(bidRepayPlan.getId());
		bidRepayPlanTmp.setAmount(bidRepayPlan.getPunishAmount());
		bidRepayPlanTmp.setPunishAmount(bidRepayPlan.getPunishAmount());
		bidRepayPlanTmp.setState(state);
		if (actualTime != null) {
			bidRepayPlanTmp.setActualTime(actualTime);
		}
		if (CompareUtil.gtZero(bidRepayPlanTmp.getPunishAmount())
				&& actualTime.getTime() > bidRepayPlan.getPlanTime().getTime()) {
			bidRepayPlanTmp.setState(REPAY_STATE_OVERDUE);
		}
		if (riskaccountTime != null) {
			bidRepayPlanTmp.setRiskaccountTime(riskaccountTime);
		}
		return bidRepayPlanTmp;
	}

	/**
	 * @Description : 组装回款计划数据
	 * @Method_Name : fitReceiptPlan
	 * @param receiptPlanList
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午11:10:00
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private List<BidReceiptPlan> fitReceiptPlan(List<BidReceiptPlan> receiptPlanList) {
		List<BidReceiptPlan> result = new ArrayList<>();
		receiptPlanList.forEach(receiptPlan -> {
			BidReceiptPlan bidReceiptPlanTmp = new BidReceiptPlan();
			bidReceiptPlanTmp.setId(receiptPlan.getId());
			bidReceiptPlanTmp.setState(RECEIPT_STATE_FINISH);
			bidReceiptPlanTmp.setActualTime(new Date());
			result.add(bidReceiptPlanTmp);
		});
		return result;
	}

	/**
	 * @Description : 组装回款人的短信站内信
	 * @Method_Name : fitSmsMsg
	 * @param bidReceiptPlanList
	 *            回款计划
	 * @param bidProduct
	 *            标的产品
	 * @param bidInfo
	 *            标的信息
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月11日 下午3:07:08
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> fitSmsMsg(List<BidReceiptPlan> bidReceiptPlanList, BidProduct bidProduct,
			BidInfo bidInfo) {
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		try {
			List<SmsMsgInfo> webMsgList = new ArrayList<>();
			List<SmsMsgInfo> telMsgList = new ArrayList<>();
			for (BidReceiptPlan bidReceiptPlan : bidReceiptPlanList) {
				final int regUserId = bidReceiptPlan.getRegUserId();
				RegUser regUser = this.regUserService.findRegUserById(regUserId);
				final long tel = regUser.getLogin();
				SmsMsgInfo webMsg;
				SmsMsgInfo telMsg;
				String msg = null;
				if (bidProduct.getType() == InvestConstants.BID_PRODUCT_CURRENT
						|| bidProduct.getType() == InvestConstants.BID_PRODUCT_COMMNE) {
				    
				} else if (bidProduct.getType() == InvestConstants.BID_PRODUCT_BUYHOUSE
						|| bidProduct.getType() == InvestConstants.BID_PRODUCT_PROPERTY) {
					// 购房宝&物业宝产品
					if (CompareUtil.gtZero(bidReceiptPlan.getCapitalAmount())) {
						msg = String.format(SmsMsgTemplate.MSG_REPAY_ENTERPRISE_CAP.getMsg(),
								new Object[] { bidInfo.getName(), bidReceiptPlan.getCapitalAmount() });
					} else {
						msg = String.format(SmsMsgTemplate.MSG_REPAY_ENTERPRISE.getMsg(), new Object[] {
								bidInfo.getName(), bidReceiptPlan.getPeriods(), bidReceiptPlan.getInterestAmount() });
					}
				} else {
					BidReceiptPlan bidReceiptPlanTmp = new BidReceiptPlan();
					bidReceiptPlanTmp.setBidId(bidReceiptPlan.getBidId());
					bidReceiptPlanTmp.setRegUserId(bidReceiptPlan.getRegUserId());
					int sum = this.bidReceiptPlanService.findBidReceiptPlanCount(bidReceiptPlanTmp);
                    // 一条回款计划，站内信、短信不显示回款期数
					if (sum == 1) { 
						msg = String.format(SmsMsgTemplate.MSG_REPAY_INCOME_ONE.getMsg(),
								new Object[] { bidInfo.getName(), bidReceiptPlan.getAmount().add(bidReceiptPlan.getIncreaseAmount()),
										bidReceiptPlan.getCapitalAmount(), bidReceiptPlan.getInterestAmount(),
										bidReceiptPlan.getIncreaseAmount() });
					} else {
						msg = String.format(SmsMsgTemplate.MSG_REPAY_INCOME.getMsg(),
								new Object[] { bidInfo.getName(), bidReceiptPlan.getPeriods(),
										bidReceiptPlan.getAmount().add(bidReceiptPlan.getIncreaseAmount()), bidReceiptPlan.getCapitalAmount(),
										bidReceiptPlan.getInterestAmount(), bidReceiptPlan.getIncreaseAmount() });
					}
				}
				if(StringUtils.isNoneBlank(msg)){
                    telMsg = new SmsTelMsg(tel, msg, SmsConstants.SMS_TYPE_NOTICE);
                    webMsg = new SmsWebMsg(regUserId, SmsMsgTemplate.MSG_REPAY_ENTERPRISE.getTitle(), msg,
                            SmsConstants.SMS_TYPE_NOTICE);
                    webMsgList.add(webMsg);
                    if (UserConstants.PLATFORM_ACCOUNT_ID != regUserId) {
                        telMsgList.add(telMsg);
                    }
                }
			}
			result.getParams().put("webMsgList", webMsgList);
			result.getParams().put("telMsgList", telMsgList);
		} catch (Exception e) {
			logger.error("repay,组装站内信、短信消息异常", e);
		}
		return result;
	}

	/**
	 * 
	 * @Description : 计算还款罚息&还款总金额
	 * @Method_Name : repayPunishAmount
	 * @param repayPlan
	 * @param bidInfoDetail
	 * @param bidInfo
	 * @return : void
	 * @Creation Date : 2017年7月13日 上午9:45:03
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void repayPunishAmount(BidRepayPlan repayPlan, BidInfoDetail bidInfoDetail, BidInfo bidInfo) {
		BigDecimal punishAmount;// 计算罚息
        // 计算逾期天数
		int lateDay = RepayCalcInterestUtil.calcPunishDays(repayPlan.getPlanTime(), repayPlan.getPeriods(), bidInfo.getTermUnit(), bidInfo.getTermValue());
		if (bidInfoDetail.getPunishState() == IS_PUNISH_STATE && lateDay > 0
				&& repayPlan.getPlanTime().before(new Date())) {
			punishAmount = CalcInterestUtil.calcOverdue(bidInfo.getTotalAmount().subtract(Optional.ofNullable(bidInfoDetail.getReturnCapAmount()).orElse(BigDecimal.ZERO)), lateDay,
					bidInfo.getAdvanceServiceRate(), bidInfo.getLiquidatedDamagesRate());
			repayPlan.setPunishAmount(punishAmount);
			repayPlan.setAmount(repayPlan.getAmount().add(punishAmount));
		}
	}

	/**
	 * @Description : 获取还款相关信息：还款计划&回款计划&还款人账户&标的信息
	 * @Method_Name : getRepayInfo
	 * @param repayId
	 * @param regUserId
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年7月13日 下午2:43:43
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private ResponseEntity<?> getRepayInfo(int repayId, int regUserId) {
		ResponseEntity<?> repayInfoRestule = new ResponseEntity<>(SUCCESS);
		try {
			// 校验是否本人操作 &校验还款计划 *
			ResponseEntity<?> result = bidRepayPlanService.validateRepayPlan(repayId, regUserId);
			if (BaseUtil.error(result)) {
				return result;
			}
			BidRepayPlan repayPlan = (BidRepayPlan) result.getParams().get("bidRepayPlan");
			repayInfoRestule.getParams().putAll(result.getParams());
			// 校验标的信息
			result = bidInfoService.validateBidInfo(repayPlan.getBidId());
			if (BaseUtil.error(result)) {
				return result;
			}
			repayInfoRestule.getParams().putAll(result.getParams());
			// 校验借款账户是否存在
			FinAccount account = finAccountService.findByRegUserId(repayPlan.getRegUserId());
			if (account == null) {
				return new ResponseEntity<>(ERROR, "借款人账户不存在！");
			}
			repayInfoRestule.getParams().put("finAccount", account);
		} catch (Exception e) {
			 logger.error("getRepayInfo获取还款信息异常,repayId:{} ,regUserId:{}",repayId,regUserId,e);
			return new ResponseEntity<>(ERROR, "获取还款信息异常！");
		}
		return repayInfoRestule;
	}

	/**
	 * @Description : 处理提前还本、匹配、投资、标的的信息维护
	 * @Method_Name : dealMatchBidInvest
	 * @param insertBidMatchList  : 存储需要添加的匹配记录的集合
	 * @param updateBidMatchList  : 存储需要更新的匹配记录的集合
	 * @param insertBidInvestList : 存储需要插入的新的投资记录
	 * @param updateBidInvestList : 存储需要更新的投资记录
	 * @param updateBidInfoDetailList : 存储需要更新的标的详情
	 * @param bidInfo
	 * @param returnCapAmount  : 还款本金
	 * @return : void
	 * @Creation Date : 2017年7月20日 下午5:26:42
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealMatchBidInvest(List<BidMatch> insertBidMatchList, List<BidMatch> updateBidMatchList,
			List<BidInvest> insertBidInvestList, List<BidInvest> updateBidInvestList,
			List<BidInfoDetail> updateBidInfoDetailList, BidInfo bidInfo, BigDecimal returnCapAmount) {
		// 检索散标的匹配关系
		BidMatch bidMatchCdt = new BidMatch();
		bidMatchCdt.setState(MATCH_STATE_SUCCESS);
		bidMatchCdt.setComnBidId(bidInfo.getId());
		List<BidMatch> bidMatchList = this.bidMatchService.findBidMatchList(bidMatchCdt);
		// 用于存储还在匹配中的标的信息
		List<BidMatch> matchingList = new ArrayList<>();
		// 获取已匹配的天数
		int days = DateUtils.getDaysBetween(bidInfo.getLendingTime(), new Date()) + 1;
		if (CommonUtils.isNotEmpty(bidMatchList)) {
			// 获取仍在匹配中的匹配记录
			for (BidMatch bidMatch : bidMatchList) {
				if (days <= BidMatchUtil.getEndTerm(bidMatch, InvestConstants.MATCH_BID_TYPE_COMMON)) {
					matchingList.add(bidMatch);
				}
			}
		}
		if (CommonUtils.isNotEmpty(matchingList)) {
			// 匹配金额降序排序
			matchingList.sort((BidMatch a, BidMatch b) -> b.getMidMoney().compareTo(a.getMidMoney()));
			// 还本金额
			BigDecimal amount = returnCapAmount;
			for (BidMatch bidMatch : matchingList) {
				if (CompareUtil.lteZero(amount)) {
					break;
				}
				amount = this.fitNeedUpdateBidMath(insertBidMatchList, updateBidMatchList, insertBidInvestList,
						updateBidInvestList, bidMatch, days, amount);
				// 更新优选的下次匹配时间及状态
				this.fitNeedUpdateBidInfoDetail(insertBidMatchList, updateBidMatchList, updateBidInfoDetailList, bidMatch);
			}
		}
	}

	/**
	 * @Description : 组装需要插入更新的匹配记录
	 * @Method_Name : fitNeedUpdateBidMath
	 * @param insertBidMatchList
	 *            存储需要添加的匹配记录的集合
	 * @param updateBidMatchList
	 *            存储需要更新的匹配记录的集合
	 * @param bidMatch
	 *            匹配记录
	 * @param days
	 *            匹配的天数
	 * @param amount
	 *            待匹配金额
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月20日 下午2:31:28
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BigDecimal fitNeedUpdateBidMath(List<BidMatch> insertBidMatchList, List<BidMatch> updateBidMatchList,
			List<BidInvest> insertBidInvestList, List<BidInvest> updateBidInvestList, BidMatch bidMatch, int days,
			BigDecimal amount) {
		// 剩余待匹配金额
		BigDecimal residueMatchAmount = amount;
		// 计算散标和优选的新的匹配期数
		int comStartTerm = BidMatchUtil.getStartTerm(bidMatch, InvestConstants.MATCH_BID_TYPE_COMMON);
		String comTermValue = comStartTerm + ":" + days;
		int goodStartTerm = BidMatchUtil.getStartTerm(bidMatch, InvestConstants.MATCH_BID_TYPE_GOOD);
		String goodTermValue = goodStartTerm + ":" + (goodStartTerm + days - comStartTerm);
		BidMatch bidMatchTmp = new BidMatch();
		bidMatchTmp.setId(bidMatch.getId());
		bidMatchTmp.setGoodBidId(bidMatch.getGoodBidId());
		if (CompareUtil.gt(bidMatch.getMidMoney(), residueMatchAmount)) {
			// 匹配的金额 > 还本金额，更新匹配记录插入新的匹配关系
			bidMatchTmp.setMidMoney(residueMatchAmount.negate());
			// 插入新的匹配关系
			BidMatch newBidMatch = new BidMatch();
			newBidMatch.setGoodBidId(bidMatch.getGoodBidId());
			newBidMatch.setGoodTermValue(goodTermValue);
			newBidMatch.setComnBidId(bidMatch.getComnBidId());
			newBidMatch.setCommTermValue(comTermValue);
			newBidMatch.setMidMoney(residueMatchAmount);
			newBidMatch.setState(MATCH_STATE_SUCCESS);
			newBidMatch.setCreateTime(new Date());
			newBidMatch.setModifyTime(new Date());
			residueMatchAmount = BigDecimal.ZERO;
			insertBidMatchList.add(newBidMatch);
			this.fitNeedUpdateBidInvest(insertBidInvestList, updateBidInvestList, newBidMatch, residueMatchAmount,
					comTermValue, goodTermValue);
		} else {
			//优选匹配记录金额全部提前还完
			bidMatchTmp.setCommTermValue(comTermValue);
			bidMatchTmp.setGoodTermValue(goodTermValue);
			residueMatchAmount = residueMatchAmount.subtract(bidMatch.getMidMoney());
		}
		updateBidMatchList.add(bidMatchTmp);
		return residueMatchAmount;
	}

	/**
	 * @Description : 组装需要插入和更新的投资记录
	 * @Method_Name : fitNeedUpdateBidInvest
	 * @param insertBidInvestList
	 *            存储需要添加的投资记录
	 * @param updateBidInvestList
	 *            存储需要更新的投资记录
	 * @param bidMatch
	 *            匹配记录
	 * @param amount
	 *            已匹配金额
	 * @param comTermValue
	 *            散标匹配期数
	 * @param goodTermValue
	 *            优选匹配期数
	 * @return : void
	 * @Creation Date : 2017年7月20日 下午5:10:46
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void fitNeedUpdateBidInvest(List<BidInvest> insertBidInvestList, List<BidInvest> updateBidInvestList,
			BidMatch bidMatch, BigDecimal amount, String comTermValue, String goodTermValue) {
		BidInvest bidInvestCdt = new BidInvest();
		bidInvestCdt.setBidInfoId(bidMatch.getComnBidId());
		bidInvestCdt.setInvestTerm(bidMatch.getCommTermValue());
		bidInvestCdt.setGoodBidId(bidMatch.getGoodBidId());
		bidInvestCdt.setGoodInvestTerm(bidMatch.getGoodTermValue());
		List<BidInvest> list = this.bidInvestService.findBidInvestList(bidInvestCdt);
		if (CommonUtils.isNotEmpty(list)) {
			BigDecimal investAmount = amount;
			for (BidInvest bidInvest : list) {
				if (CompareUtil.ltZero(investAmount)) {
					break;
				}
				BidInvest bidInvestTmp = new BidInvest();
				if (CompareUtil.gt(bidInvest.getInvestAmount(), investAmount)) {
					// 更新匹配投资记录，只更新投资金额不更新匹配期数
					bidInvestTmp.setId(bidInvest.getId());
					bidInvestTmp.setInvestAmount(investAmount.negate());
					// 插入新的匹配投资记录
					BidInvest newBidInvest = new BidInvest();
					BeanUtils.copyProperties(bidInvest, newBidInvest);
					newBidInvest.setId(null);
					newBidInvest.setInvestAmount(investAmount);
					newBidInvest.setInvestTerm(comTermValue);
					newBidInvest.setGoodInvestTerm(goodTermValue);
					newBidInvest.setPayAmount(investAmount);
					insertBidInvestList.add(newBidInvest);
					// 插入新的匹配投资合同合同
					investAmount = BigDecimal.ZERO;
				} else {
					bidInvestTmp.setId(bidInvest.getId());
					bidInvestTmp.setInvestTerm(comTermValue);
					bidInvestTmp.setGoodInvestTerm(goodTermValue);
					investAmount = investAmount.subtract(bidInvest.getInvestAmount());
				}
				updateBidInvestList.add(bidInvestTmp);
				// 更新对应的优选的投资记录的状态为未匹配
				BidInvest goodBidInvest = new BidInvest();
				goodBidInvest.setGoodInvestId(bidInvest.getGoodInvestId());
				goodBidInvest.setState(InvestConstants.MATCH_STATE_PART);
				updateBidInvestList.add(goodBidInvest);
			}
		}
	}

	/**
	 * @Description : 组装需要更新的标的详情数据（更新标的的状态及下次匹配时间）
	 * @Method_Name : fitNeedUpdateBidInfoDetail
	 * @param list  ：需要更新的标的详情集合
	 * @param bidMatch ：匹配记录
	 * @return : void
	 * @Creation Date : 2017年7月20日 下午2:41:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void fitNeedUpdateBidInfoDetail(List<BidMatch> insertBidMatchList, List<BidMatch> updateBidMatchList,
											List<BidInfoDetail> list, BidMatch bidMatch) {
		BidInfoDetail bidInfoDetail = new BidInfoDetail();
		bidInfoDetail.setBiddInfoId(bidMatch.getGoodBidId());
		bidInfoDetail.setMatchState(InvestConstants.MATCH_STATE_PART);
		//获取优选标的匹配记录
		BidMatch bidMatchCdt = new BidMatch();
		bidMatchCdt.setState(MATCH_STATE_SUCCESS);
		bidMatchCdt.setGoodBidId(bidMatch.getGoodBidId());
		List<BidMatch> bidMatchList = this.bidMatchService.findBidMatchList(bidMatchCdt);
		//初始优选对应该散标最新的匹配记录集合
		List<BidMatch> matchList = new ArrayList<>();
		//将本次优选标需要更新的匹配记录集合copy至updateList中
		List<BidMatch> updateList = new ArrayList<>();
		for (BidMatch record:updateBidMatchList){
			if (record.getGoodBidId().equals(bidMatch.getGoodBidId())){
				BidMatch result = new BidMatch();
				BeanUtils.copyProperties(record,result);
				updateList.add(result);
			}
		}
		//给更新匹配记录设置commTerm和midMoney
		for (BidMatch record:updateList){
			for (BidMatch matchRecord:bidMatchList){
				if (record.getId().equals(matchRecord.getId())){
					//record的midMoney为null说明此匹配记录对应的散标本金全部提前还完
					if (null == record.getMidMoney()){
						record.setMidMoney(matchRecord.getMidMoney());
					}else {
						record.setCommTermValue(matchRecord.getCommTermValue());
						record.setMidMoney(matchRecord.getMidMoney().add(record.getMidMoney()));
						record.setGoodTermValue(matchRecord.getGoodTermValue());
					}
					break;
				}
			}
		}
		matchList.addAll(updateList);
		//将匹配记录中关于该散标的记录剔除，并更新为最新的优选对应该散标的匹配记录
		bidMatchList = bidMatchList.stream().filter(record -> !record.getComnBidId().equals(bidMatch.getComnBidId()))
				.collect(Collectors.toList());

		//更新为最新的优选对应该散标的匹配记录
		List<BidMatch> insertList = insertBidMatchList.stream().filter(record -> record.getGoodBidId()
				.equals(bidMatch.getGoodBidId())).collect(Collectors.toList());
		matchList.addAll(insertList);

		bidMatchList.addAll(matchList);
		//获取优选标相关信息
		BidInfo goodBidInfo = bidInfoService.findBidInfoById(bidMatch.getGoodBidId());
		BidInfoDetail goodBiddetailInfo = bidInfoDetailService.findBidInfoDetailByBidId(bidMatch.getGoodBidId());
		//获取优选标的开始期数，根据生成优选对应散标的匹配记录集合中结束期数中最小值即为开始期数
		matchList.sort((BidMatch b1, BidMatch b2) -> Integer.valueOf(b1.getGoodTermValue().split(":")[1]).
				compareTo(Integer.valueOf(b2.getGoodTermValue().split(":")[1])));
		int startTerm = Integer.parseInt(matchList.get(0).getGoodTermValue().split(":")[1]);

		//开始期数时标的金额匹配完毕则更新下次匹配时间，否则更新下次匹配时间
		if (BidMatchUtil.matchComplete(goodBidInfo.getTotalAmount(),bidMatchList,MATCH_BID_TYPE_GOOD,startTerm)){
//			bidInfoDetail.setNextMatchDate(DateUtils.getFirstTimeOfDay(DateUtils.addDays(new Date(), 1)));
			//获取下次匹配时间
			bidInfoDetail.setNextMatchDate(DateUtils.getFirstTimeOfDay(DateUtils.addDays(goodBidInfo.getLendingTime(), startTerm)));
		}else {
			bidInfoDetail.setNextMatchDate(goodBiddetailInfo.getNextMatchDate());
		}
		list.add(bidInfoDetail);
	}

	/**
	 * @Description : 计算剩余冻结金额
	 * @Method_Name : calcResidueFreezeMoney
	 * @param bidInfo
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月7日 上午11:46:46
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BigDecimal calcResidueFreezeMoney(BidInfo bidInfo) {
		BidRepayPlan repayPlanCdt = new BidRepayPlan();
		repayPlanCdt.setBidId(bidInfo.getId());
		repayPlanCdt.setStates(Arrays.asList(RepayConstants.REPAY_STATE_FINISH, RepayConstants.REPAY_STATE_ADVANCE_CAPITAL));
		// 获得标的总利息
		BigDecimal totalInterest = CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(),
				bidInfo.getTotalAmount(), bidInfo.getInterestRate());
		// 已还利息和
		ResponseEntity<?> result = this.bidRepayPlanService.countRepayPlanAmount(repayPlanCdt);
		BigDecimal alreadyReturnInterestSum = new BigDecimal(String.valueOf(result.getParams().get("interestSum")));
		// 剩余冻结利息
		return totalInterest.subtract(alreadyReturnInterestSum);
	}

	@Override
	public ResponseEntity<?> calcAdvanceReapyAmount(Integer regUserId, Integer repayId, BigDecimal capital) {
		logger.info("calcAdvanceReapyAmount, 计算提前还本利息. 用户标识: {}, 还款计划标识: {}, 本金: {}", regUserId, repayId, capital);
		ResponseEntity<?> map = this.getRepayInfo(repayId, regUserId);
		if (map == null || map.getResStatus() != SUCCESS) {
			return map;
		}
		BidRepayPlan repayPlan = (BidRepayPlan) map.getParams().get("bidRepayPlan");
		BidInfo bidInfo = (BidInfo) map.getParams().get("bidInfo");
		BidInfoDetail bidInfoDetail = (BidInfoDetail) map.getParams().get("bidInfoDetail");
		FinAccount account = (FinAccount) map.getParams().get("finAccount");
		return ValidateRepayAmountUtil.advanceRepayMoney(bidInfo, bidInfoDetail, repayPlan, account, capital);
	}
}
