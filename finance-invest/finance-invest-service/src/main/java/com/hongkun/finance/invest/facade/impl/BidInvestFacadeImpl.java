package com.hongkun.finance.invest.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.invest.util.BidMatchUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzRateRecordService;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.model.QdzVasRuleItem;
import com.hongkun.finance.vas.model.VasBidRecommendEarn;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.VasSimRecord;
import com.hongkun.finance.vas.model.vo.VasCouponDetailVO;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.singleton.SingletonThreadPool;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.pager.PageHelper;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.redis.JedisClusterLock;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE;
import static com.yirun.framework.core.commons.Constants.*;

@Service
public class BidInvestFacadeImpl implements BidInvestFacade {

	private static final Logger logger = LoggerFactory.getLogger(BidInvestFacadeImpl.class);

	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidProductService bidProductService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private VasSimRecordService vasSimRecordService;
	@Reference
	private BidMatchService bidMatchService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private BidTransferAutoService bidTransferAutoService;
	@Reference
	private BidTransferManualService bidTransferManualService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Autowired
	private BidAutoSchemeService bidAutoSchemeService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private VasBidRecommendEarnService vasBidRecommendEarnService;
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private QdzRateRecordService qdzRateRecordService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Autowired
	private JmsService jmsService;
    @Reference
    private VasRebatesRuleService vasRebatesRuleService;
    @Reference
    private QdzAccountService qdzAccountService;
    @Reference
    private RegUserInfoService regUserInfoService;

    @Override
    public Map<String, Object> preInvest(Integer regUserId, Integer bidId) {
        Map<String, Object> result = AppResultUtil.successOfMsg("");
        //查询标的信息
        BidInfoVO bidInfoVO = this.bidInfoService.findBidInfoDetailVo(bidId);
        //加载账户信息
        Future<FinAccount> accountFuture = SingletonThreadPool.callCachedThreadPool(() -> this.finAccountService.findByRegUserId(regUserId));
        //查询钱袋子账户
        Future<QdzAccount> qdzAccountFuture = SingletonThreadPool.callCachedThreadPool(() -> this.qdzAccountService.findQdzAccountByRegUserId(regUserId));
        //查询可用的卡劵列表
        Future<Map<String, Object>> vasCouponDetailFuture = null;
        if(!bidInfoVO.getAllowCoupon().equals(InvestConstants.BID_ALLOW_COUPON_NO)){
            vasCouponDetailFuture = SingletonThreadPool.callCachedThreadPool(() -> this.vasCouponDetailService.getUserInvestUsableCoupon(bidInfoVO.getProductType(), regUserId));
        }
        try{
            //可用余额
            result.put("useableMoney", accountFuture.get().getUseableMoney());
            //钱袋子余额
            result.put("qdzAmount", qdzAccountFuture.get().getMoney());
            //可用卡劵列表
            result.put("interestCouponList", Collections.emptyList());
            result.put("redPacketsCouponList", Collections.emptyList());
            if(!bidInfoVO.getAllowCoupon().equals(InvestConstants.BID_ALLOW_COUPON_NO) && vasCouponDetailFuture != null){
                result.putAll(vasCouponDetailFuture.get());
            }
        }catch(Exception e){
            throw new GeneralException("数据加载异常");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
	@Override
	@Compensable
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> 
    invest(RegUser regUser, int investRedPacketId, int investRaiseInterestId, BigDecimal money,
			int bidId, int investWay, Integer investType, PlatformSourceEnums platformSourceEnums) {
		logger.info(
				"tcc invest entrance, reference vas#updateVasCouponDetailBatch | updateBatchVasSimRecordByRegUserId, invest#insertBidInvest, payment#cashPay | batchInsertTradeAndTransfer. 投资操作, 用户: {}, 投资金额: {}, 投资标的: {}, 投资方式: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}",
				regUser.getId(), money, bidId, investWay, investType, platformSourceEnums.getType(), investRedPacketId,
				investRaiseInterestId);
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		final Integer regUserId = regUser.getId();
		int step = 1;
		try {
			RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUserId,
					() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId));
			// 锁定标的信息
			JedisClusterLock lock = new JedisClusterLock();
			String lockKey = LOCK_PREFFIX + BidInfo.class.getSimpleName() + bidId;
			BidInfo bidInfo = null;
			BidInvest bidInvest = null;
			BidProduct bidProduct = null;
			VasCouponDetail investRedPacket = null;
			VasCouponDetail investRaiseInterest = null;
			BigDecimal investRaiseWorth = BigDecimal.ZERO;
			// 加息券收益金额
			BigDecimal investRaiseInterestAtm = BigDecimal.valueOf(0);
			BigDecimal investRedPacketAtm = BigDecimal.valueOf(0);
			// 体验金记录
			List<VasSimRecord> vasSimRecordList = null;
			try {
				if (lock.lock(lockKey)) {
					// 验证数据有效性
					result = this.doValidate(regUser, investRedPacketId, investRaiseInterestId, bidId, money, investWay, platformSourceEnums);
					if (!result.validSuc()) {
						return result;
					}
					bidInfo = (BidInfo) result.getParams().get("bidInfo");
					bidProduct = (BidProduct) result.getParams().get("bidProduct");
					vasSimRecordList = (List<VasSimRecord>) result.getParams().get("vasSimRecordList");
					Object obj = result.getParams().get("investRedPacket");
					if (obj != null) {
						investRedPacket = (VasCouponDetail) obj;
						investRedPacketAtm = investRedPacket.getWorth();
					}
					obj = result.getParams().get("investRaiseInterest");
					if(obj != null) {
						investRaiseInterest = (VasCouponDetail) obj;
                        investRaiseWorth = investRaiseInterest.getWorth();
					}
					logger.info("投资操作步骤: {}, 用户: {}, 标的: {}, 更新卡券使用状态", step++, regUserId, bidInfo.toString());
					this.dealVasCouponDetail(investRedPacket, investRaiseInterest);
					logger.info("投资操作步骤: {}, 用户: {}, 标的: {}, 插入投资记录, 更新标的状态", step++, regUserId, bidInfo.toString());
					result = BaseUtil.getTccProxyBean(BidInvestService.class, getClass(), "invest").insertBidInvest(regUserId, regUserDetail.getRealName(), money,
//					result = this.bidInvestService.insertBidInvest(regUserId, regUserDetail.getRealName(), money,
							bidInfo, investRedPacket == null ? 0 : investRedPacket.getId(),
							investRaiseInterest == null ? 0 : investRaiseInterest.getId(),
							investRaiseInterest == null ? BigDecimal.ZERO : investRaiseInterest.getWorth(), new Date(),
							investType, platformSourceEnums);
					if(!result.validSuc()) {
						logger.info("投资操作步骤: {}失败, 用户: {}, 标的: {}, 重复更新标的信息.", step++, regUserId, bidInfo.toString());
						throw new BusinessException("投资已受理，请稍后查看投资记录");
					}
					bidInvest = (BidInvest) result.getParams().get("bidInvest");
					investRaiseInterestAtm = (BigDecimal) result.getParams().get("investRaiseInterestAtm");
					logger.info("投资操作步骤: {}, 用户: {}, 标的: {}, 冻结用户资金, 插入流水、划转", step, regUserId, bidId);
					result = this.dealFinAccount(regUserId, bidProduct, bidInfo, bidInvest.getId(), money,
							vasSimRecordList, investWay, platformSourceEnums, investRedPacket);
					if (BaseUtil.error(result)) {
						logger.info("投资操作步骤: {}失败, 用户: {}, 标的: {}, 验证冻结用户资金, 插入流水、划转操作失败", step, regUserId, bidId);
						throw new BusinessException("投资失败");
					}
				} else {
					return ResponseEntity.error("当前投资人数过多，请重试!");
				}
			} finally {
				lock.freeLock(lockKey);
			}
			if (result.validSuc()) {
				// 发送短信&站内信
				this.sendSmsMsg(bidInfo, regUser, money, investRedPacketAtm, investRaiseInterestAtm, investRaiseWorth);
				this.sendVipSms(regUser);
				this.sendInvestPreserve(bidInvest, bidInfo, regUser, regUserDetail, investRedPacketAtm);
			}
		} catch (Exception e) {
			logger.error(
					"tcc invest error, reference vas#updateVasCouponDetailBatch | updateBatchVasSimRecordByRegUserId, invest#insertBidInvest, payment#cashPay | batchInsertTradeAndTransfer. 投资操作, 失败位置: {}, 用户: {}, 投资金额: {}, 投资标的: {}, 投资方式: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}\n",
					--step, regUser.getId(), money, bidId, investWay, investType, platformSourceEnums.getType(), investRedPacketId,
					investRaiseInterestId, e);
			throw new GeneralException("投资失败");
		}
		logger.info(
				"tcc invest suceess, reference vas#updateVasCouponDetailBatch | updateBatchVasSimRecordByRegUserId, invest#insertBidInvest, payment#cashPay | batchInsertTradeAndTransfer. 投资操作, 用户: {}, 投资金额: {}, 投资标的: {}, 投资方式: {}, 投资类型: {}, 投资平台: {}, 投资红包: {}, 加息券: {}",
				regUser.getId(), money, bidId, investWay, investType, platformSourceEnums.getType(), investRedPacketId,
				investRaiseInterestId);
		return result;
	}

	/**
	 * @Description : 验证投资的合法性
	 * @Method_Name : doValidate
	 * @param regUser
	 * @param investRedPacketId
	 * @param investRaiseInterestId
	 * @param bidId
	 * @param money
	 * @param investWay 投资方式 1101:投资  1102:体验金投资 1103:钱袋子投资  1104:充值投资  1105:匹配投资
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月19日 下午5:55:53
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("unchecked")
	private ResponseEntity<?> doValidate(RegUser regUser, int investRedPacketId, int investRaiseInterestId, int bidId,
			BigDecimal money, int investWay, PlatformSourceEnums platformSourceEnums) {
		BidProduct bidProduct = null;
		BidInfo bidInfo = null;
		final Integer regUserId = regUser.getId();
		List<VasSimRecord> vasSimRecordList = null;
		//验证投资资格
//        ResponseEntity<?> result = this.validVipStatus(regUser);
        ResponseEntity<?> result = this.validInvestQualification(regUserId);
		if(result.validSuc()){
			result = this.bidInfoService.validateBidInfo(bidId);
			if (result.getResStatus() == SUCCESS) {
				bidInfo = (BidInfo) result.getParams().get("bidInfo");
				bidProduct = (BidProduct) result.getParams().get("bidProduct");
				// 验证项目的有效性
				result = this.validateStatus(regUser, bidProduct, bidInfo, (BidInfoDetail) result.getParams().get("bidInfoDetail"), investWay, platformSourceEnums);
				if (result.validSuc()) {
					// 验证项目投资金额
					result = this.validateBidScheme(bidInfo, bidProduct, money);
					if (result.validSuc()) {
						// 验证账户余额和体验金投资金额
						result = this.validateAccount(bidProduct, regUserId, money);
						if (result.validSuc()) {
							vasSimRecordList = (List<VasSimRecord>) result.getParams().get("vasSimRecordList");
							// 验证节日、活动项目
							result = this.validateFestival(investRedPacketId, investRaiseInterestId, regUserId, bidInfo);
							if (result.validSuc()) {
								// 校验新手标
								result = this.validateFledglingBid(regUserId, bidProduct.getType(), money, investRedPacketId, investRaiseInterestId, bidInfo.getAllowCoupon());
								if (result.validSuc()) {
									// 检验购房宝|物业宝项目信息
									result = this.validateBuyHourseOrProertyBid(regUserId, bidProduct, bidInfo, money);
									if (result.validSuc() && (investRedPacketId > 0 || investRaiseInterestId > 0)) {
										// 校验卡券有效性
										result = this.vasCouponDetailService.validateCoupon(investRedPacketId,
												investRaiseInterestId, regUserId, bidProduct.getType(),
												bidInfo.getAllowCoupon(), money);
									}
								}
							}
						}
					}
				}
			}
        }
		result.getParams().put("bidProduct", bidProduct);
		result.getParams().put("vasSimRecordList", vasSimRecordList);
		result.getParams().put("bidInfo", bidInfo);
		return result;
	}

	/**
	 * @Description : 处理卡券使用装填
	 * @Method_Name : dealVasCouponDetail
	 * @param investRedPacket
	 * @param investRaiseInterest
	 * @return : void
	 * @Creation Date : 2017年7月12日 上午9:50:44
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void dealVasCouponDetail(VasCouponDetail investRedPacket, VasCouponDetail investRaiseInterest) {
		List<VasCouponDetail> couponList = new ArrayList<>();
		if (investRedPacket != null) {
			VasCouponDetail couponTmp = new VasCouponDetail();
			couponTmp.setId(investRedPacket.getId());
			couponTmp.setState(VasCouponConstants.COUPON_DETAIL_USE_ALREADY);
            couponTmp.setUsedTime(new Date());
			couponList.add(couponTmp);
		}
		if (investRaiseInterest != null) {
			VasCouponDetail couponTmp = new VasCouponDetail();
			couponTmp.setId(investRaiseInterest.getId());
			couponTmp.setState(VasCouponConstants.COUPON_DETAIL_USE_ALREADY);
			couponTmp.setUsedTime(new Date());
			couponList.add(couponTmp);
		}
		if (!couponList.isEmpty()) {
			BaseUtil.getTccProxyBean(VasCouponDetailService.class, getClass(), "invest").updateVasCouponDetailBatch(couponList);
		}
	}

	/**
	 * @Description : 处理资金账户 或体验金记录
	 * @Method_Name : dealFinAccount
	 * @param regUserId
	 * @param bidProduct
	 * @param bidInvestId : 投资记录id
	 * @param money	: 投自己金额
	 * @param vasSimRecordList : 体验金记录
	 * @param investWay : 投资的方式
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月12日 下午2:18:24
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> dealFinAccount(Integer regUserId, BidProduct bidProduct, BidInfo bidInfo,
			Integer bidInvestId, BigDecimal money, List<VasSimRecord> vasSimRecordList, int investWay,
			PlatformSourceEnums platformSourceEnums, VasCouponDetail investRedPacket) {
		if (bidProduct.getType() == InvestConstants.BID_PRODUCT_EXPERIENCE) {// 体验金
			BaseUtil.getTccProxyBean(VasSimRecordService.class, getClass(), "invest").updateBatchVasSimRecordById(VasConstants.VAS_SIM_STATE_USES, vasSimRecordList);
			FinTradeFlow flow = FinTFUtil.initFinTradeFlow(regUserId, bidInvestId, money,
					TradeTransferConstants.TRADE_TYPE_INVEST_SIM, platformSourceEnums);
			//体验金投资的接收用户设置为平台账户id
			FinFundtransfer finFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(), regUserId,
					UserConstants.PLATFORM_ACCOUNT_ID, money, TRANSFER_SUB_CODE_FREEZE);
            finFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
			return BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(), "invest").batchInsertTradeAndTransfer(flow, Arrays.asList(finFundtransfer));
		} else {
			FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(regUserId, bidInvestId, money, investWay, platformSourceEnums);
            final String pFlowId = finTradeFlow.getFlowId();
            List<FinFundtransfer> fundtransfersList = new ArrayList<>();
            //冻结流水
            FinFundtransfer freezeFinFundtransfer = FinTFUtil.initFinFundtransfer(pFlowId, regUserId, null, money, TRANSFER_SUB_CODE_FREEZE);
            fundtransfersList.add(freezeFinFundtransfer);
			if(investRedPacket != null){
                final BigDecimal worth = investRedPacket.getWorth();
                // 投资人--收入投资红包流水
				FinFundtransfer finFundtransferCouponKIn = FinTFUtil.initFinFundtransfer(pFlowId, regUserId, UserConstants.PLATFORM_ACCOUNT_ID, worth,
						TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.INVEST_RED_PACKETS));
				FinFundtransfer finFundtransferCouponKOut = FinTFUtil.reverse(finFundtransferCouponKIn, TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.INVEST_RED_PACKETS));
                fundtransfersList.add(finFundtransferCouponKIn);
                fundtransfersList.add(finFundtransferCouponKOut);
            }
//			return BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(), "invest").cashPay(finTradeFlow, TRANSFER_SUB_CODE_FREEZE);
			return BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(), "invest").updateAccountInsertTradeAndTransfer(finTradeFlow, fundtransfersList);
		}
	}

	/**
	 * @Description : 验证标的的投资状态
	 * @Method_Name : validateStatus
	 * @param bidInfo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午10:07:34
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateStatus(RegUser regUser, BidProduct bidProduct, BidInfo bidInfo,
			BidInfoDetail bidInfoDetail, int investWay, PlatformSourceEnums platformSourceEnums) {
		//标的类型是匹配投资并且投资方式是匹配投资，直接放行
		if (bidInfoDetail.getMatchType() == InvestConstants.BID_MATCH_TYPE_YES && investWay == 1105) {
			return new ResponseEntity<>(SUCCESS);
		}
		// 活期产品只能第三方账户投资
		if (bidProduct.getType() == InvestConstants.BID_PRODUCT_CURRENT) {
			// 第三方账户&标的时未上架状态
			if (regUser.getType() != UserConstants.USER_TYPE_ESCROW_ACCOUNT
					&& bidInfo.getState() != InvestConstants.BID_STATE_WAIT_NEW) {
				return ResponseEntity.error("非活期用户不能投资");
			}
		} else {
			// 标的的状态不为2都认为是无效的投资项目
			if (bidInfo.getState() != InvestConstants.BID_STATE_WAIT_INVEST) {
				return ResponseEntity.error("该项目不是有效的投资项目");
			}
		}
		if (bidInfo.getEndTime().getTime() > 0 && System.currentTimeMillis() > bidInfo.getEndTime().getTime()) {
			return ResponseEntity.error("项目已过期");
		}
		if (bidInfo.getStartTime().getTime() > 0 && System.currentTimeMillis() < bidInfo.getStartTime().getTime()) {
			return ResponseEntity.error("项目未开始");
		}
		//投资来源是PC但标的只允许在APP端进行投资
		if (platformSourceEnums.getValue() == 10 && bidInfo.getInvestPosition().indexOf('1') < 0) {
			return ResponseEntity.error("该项目只允许在APP端投资");
		}
		//投资人与借款人相同
		if(bidInfo.getBorrowerId().equals(regUser.getId())){
            return ResponseEntity.error("暂不支持投资本项目，请尝试其他产品");
        }

        // 校验海外投资
		if(validOverseaInvestor(regUser.getId()).validSuc()
				&& BaseUtil.equelsIntWraperPrimit(bidInfo.getBidProductId(),InvestConstants.BID_PRODUCT_OVERSEA)){
			return ResponseEntity.error("暂不支持投资本项目，请尝试其他产品");
		}

		return ResponseEntity.SUCCESS;
	}

	/**
	 * @Description : 验证节日、活动项目
	 * @Method_Name : validateFestival
	 * @param regUserId
	 * @param bidInfo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午11:41:32
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateFestival(int investRedPacketId, int investRaiseInterestId, final Integer regUserId,
			BidInfo bidInfo) {
		// 周年庆活动
		if (bidInfo.getType() != InvestConstants.BID_TYPE_HOT) {
			return new ResponseEntity<>(SUCCESS);
		}
		return validateNoSupportCoupon(investRedPacketId, investRaiseInterestId, bidInfo.getAllowCoupon());
	}

	/**
	 * @Description : 3.8妇女节活动
	 * @Method_Name : festivalForThreeEight
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月13日 下午4:13:06
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Deprecated
	private ResponseEntity<?> validateFestivalForThreeEight(int investRedPacketId, int investRaiseInterestId, final Integer regUserId, BidInfo bidInfo) {
		// 推荐标 && reate = 7,6
		if (bidInfo.getType() != InvestConstants.BID_TYPE_RECOMMEND
				&& !CompareUtil.eq(bidInfo.getInterestRate(), 7.6)) {
			return new ResponseEntity<>(SUCCESS);
		}
		RegUserDetail regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(regUserId);
		String idCard = regUserDetail.getIdCard();
		int gender = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));
		// 仅限女性进行投资
		if (gender % 2 != 0) {
			return new ResponseEntity<>(ERROR, "此标仅限女性用户投资");
		}
		return validateNoSupportCoupon(investRedPacketId, investRaiseInterestId, bidInfo.getAllowCoupon());
	}


	/**
	 * @Description : 新手标数据校验
	 * @Method_Name : fledglingBid
	 * @param regUserId
	 * @param prodoctType
	 * @param money
	 * @param investRedPacketId
	 * @param investRaiseInterestId
	 * @param allowCoupon		: 是否支持卡券
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午9:17:23
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateFledglingBid(Integer regUserId, Integer prodoctType, BigDecimal money,
			Integer investRedPacketId, Integer investRaiseInterestId, Integer allowCoupon) {
		// 判断是否是新手标
		if (prodoctType != InvestConstants.BID_PRODUCT_PREFERRED) {
			return ResponseEntity.SUCCESS;
		}
		//此验证卡券操作一定会执行
		ResponseEntity<?> result = validateNoSupportCoupon(investRedPacketId, investRaiseInterestId, allowCoupon);
		if(!result.validSuc()) {
			return result;
		}
		if (CompareUtil.gt(money, INVEST_FLEDGLING_INVEST_MAX_ATM)) {
			return ResponseEntity.error("新手标最多投资" + INVEST_FLEDGLING_INVEST_MAX_ATM + "元");
		}
		//投资新手标白名单存在，直接通过验证
		if (this.rosInfoService.validateRoster(regUserId, RosterType.FLEDGLING_BID, RosterFlag.WHITE)) {
			return ResponseEntity.SUCCESS;
		}
		// 查询投资记录，体验金&活期不作为新手标判断依据
		Integer count = this.bidInvestService.findBidInvestCountForPrefered(regUserId);
		if (count > 0) {
			return ResponseEntity.error("新手标仅限未成功投资过的新用户");
		}
		return ResponseEntity.SUCCESS;
	}

	/**
	 * @Description : 通过产品招标方案，验证投资金额
	 * @Method_Name : validateBidScheme
	 * @param bidInfo
	 * @param bidProduct
	 * @param money
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午10:01:51
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateBidScheme(BidInfo bidInfo, BidProduct bidProduct, BigDecimal money) {
		if (!CompareUtil.gte(bidInfo.getResidueAmount(), money)) {
			return new ResponseEntity<>(ERROR, "您的投资金额大于剩余投资金额");
		}
		// 活期产品只能第三方账户投资
		if (bidProduct.getType() == InvestConstants.BID_PRODUCT_CURRENT
				&& !CompareUtil.eq(bidInfo.getResidueAmount(), money)) {
			return ResponseEntity.error("投资金额必须与标的金额相等");
		}
        int bidSchemeValue = bidProduct.getBidSchemeValue();
        if (CompareUtil.gt(bidSchemeValue, bidInfo.getResidueAmount())
                && !CompareUtil.eq(bidInfo.getResidueAmount(), money)) {
            return new ResponseEntity<>(ERROR, "本次须购买金额" + bidInfo.getResidueAmount().doubleValue() + "元");
        }
        // 平均金额招标
		if (bidProduct.getBidScheme() == 0) {
			if (!CompareUtil.residue(money, BigDecimal.valueOf(bidSchemeValue))) {
				return new ResponseEntity<>(ERROR, "投资金额为" + bidSchemeValue + "整数倍");
			}
		} else {// 最低金额招标
            // 剩余金额 >= 最低起投金额 & ( 投资金额 < 起投金额 | (投资金额 > 起投金额 && (投资金额-起投金额)与步长取模 不等于 0))
			if(CompareUtil.gte(bidInfo.getResidueAmount(), bidSchemeValue) && 
                    (CompareUtil.gt(bidSchemeValue, money) || 
                            (CompareUtil.gt(money, BigDecimal.valueOf(bidSchemeValue)) && 
                                    !CompareUtil.residue(money.subtract(BigDecimal.valueOf(bidSchemeValue)), BigDecimal.valueOf(bidInfo.getStepValue()))))){
			    return ResponseEntity.error("最低起投金额为" + bidSchemeValue + "元,且必须为" + bidInfo.getStepValue() + "整数倍递增!");
            }
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description : 验证购房宝、物业宝标的信息
	 * @Method_Name : validateBuyHourseOrTenementBid
	 * @param regUserId
	 * @param bidProduct
	 * @param bidInfo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月14日 上午11:32:11
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateBuyHourseOrProertyBid(final Integer regUserId, BidProduct bidProduct, BidInfo bidInfo,
			BigDecimal money) {
		if (bidProduct.getType() != InvestConstants.BID_PRODUCT_BUYHOUSE
				&& bidProduct.getType() != InvestConstants.BID_PRODUCT_PROPERTY) {
			return ResponseEntity.SUCCESS;
		}
		BidInvest bidInvest = new BidInvest();
		bidInvest.setBidInfoId(bidInfo.getId());
		bidInvest.setRegUserId(regUserId);
		List<BidInvest> list = this.bidInvestService.findBidInvestList(bidInvest);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity
					.error("不能重复投资同一" + (bidProduct.getType() == InvestConstants.BID_PRODUCT_BUYHOUSE ? "购房宝" : "物业宝"));
		}
		RosDepositInfo rosDepositInfo = new RosDepositInfo();
		rosDepositInfo.setRegUserId(regUserId);
		rosDepositInfo.setBidId(bidInfo.getId());
		rosDepositInfo.setMoney(money);
		rosDepositInfo.setState(1);
		rosDepositInfo.setType(bidProduct.getType() == InvestConstants.BID_PRODUCT_BUYHOUSE
				? RosterConstants.ROSTER_DEPOSIT_TYPE_BUYHOURSE : RosterConstants.ROSTER_DEPOSIT_TYPE_PROPERTY);
		if (this.rosDepositInfoService.findRosDepositInfoCount(rosDepositInfo) != 1) {
			return ResponseEntity.error("投资金额与意向金金额不符");
		}
		return ResponseEntity.SUCCESS;
	}

	/**
	 * @Description : 不支持增值服务(卡券)的校验
	 * @Method_Name : noSupportCoupon
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月13日 下午4:51:38
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateNoSupportCoupon(int investRedPacketId, int investRaiseInterestId, int allowCoupon) {
		if(allowCoupon == InvestConstants.BID_ALLOW_COUPON_NO && (investRedPacketId > 0 || investRaiseInterestId > 0)) {
			return ResponseEntity.error("该项目不支持加息券或投资红包");
		}
		if(allowCoupon == InvestConstants.BID_ALLOW_COUPON_RAISE_INTEREST && investRedPacketId > 0)  {
			return ResponseEntity.error("该项目仅支持加息券");
		}
		if(allowCoupon == InvestConstants.BID_ALLOW_COUPON_RED_PACKET && investRaiseInterestId > 0)  {
			return ResponseEntity.error("该项目仅支持投资红包");
		}
		return ResponseEntity.SUCCESS;
	}

	/**
	 * @Description : 验证账户金额&体验金金额
	 * @Method_Name : validateSim
	 * @param product
	 * @param regUserId
	 * @param money
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月12日 下午2:09:42
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> validateAccount(BidProduct product, final Integer regUserId, BigDecimal money) {
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		if (product.getType() == InvestConstants.BID_PRODUCT_EXPERIENCE) {
			result = this.vasSimRecordService.findVasSimRecordByRegUserId(regUserId);
			BigDecimal totalMoney = (BigDecimal) result.getParams().get("totalMoney");
			if (!CompareUtil.eq(totalMoney, money)) {
				return new ResponseEntity<>(ERROR, "体验金金额不匹配");
			}
		} else {
			FinAccount finAccount = this.finAccountService.findByRegUserId(regUserId);
			if (finAccount == null) {
				return new ResponseEntity<>(ERROR, "未查询到您的鸿坤金服账户");
			}
			if (CompareUtil.gt(money, finAccount.getUseableMoney())) {
				return new ResponseEntity<>(ERROR, "余额不足，您的可用余额为" + finAccount.getUseableMoney().doubleValue() + "元，请去充值");
			}
		}
		return result;
	}

	@Override
	public ResponseEntity<?> match(Integer oneBidId, List<Integer> moreBidIdList, Integer matchType, Integer userId) {
		logger.info("match, 匹配, 用户标识: {}, oneBidId: {}, moreBidIdList: {}, matchType: {}",
				userId, oneBidId, moreBidIdList, matchType);
		String lockKey = LOCK_PREFFIX + "BidInvestFacadeImpl_match";
		JedisClusterLock lock = new JedisClusterLock();
		if (!lock.lock(lockKey,LOCK_EXPIRES,LOCK_WAITTIME)){
			logger.error("match, 匹配获取锁超时, 用户标识: {}, oneBidId: {}, moreBidIdList: {}, matchType: {}", userId,
					oneBidId, moreBidIdList, matchType);
			return new ResponseEntity<>(ERROR,"匹配获取锁超时！");
		}
		//一个标匹配一个或多个标，oneBid为优选则moreBids为散标，oneBid为散标则moreBids为优选标
		BidInfo oneBidInfo = bidInfoService.findBidInfoById(oneBidId);
		BidInfoDetail oneBidInfoDetail = bidInfoDetailService.findBidInfoDetailByBidId(oneBidId);
		//获取匹配标地产品类型，判断与传递的产品类型是否一致，控制匹配双方标的产品正常
		BidProduct oneBidProduct = bidProductService.findBidProductById(oneBidInfo.getBidProductId());
		//匹配标的产品类型与传递的产品类型不一致，返回提示信息
		if (!matchType.equals(BidInfoUtil.matchBidTypeByProdutType(oneBidProduct.getType()))){
			return new ResponseEntity<>(ERROR,"[" + oneBidInfo.getName() + "]: 产品类型不正确！");
		}
		//oneBid下次匹配时间
		Date oneBidNextMatchDate = oneBidInfoDetail.getNextMatchDate();
		//oneBid标地实际金额 = 标地金额 - 已提前还款本金
		BigDecimal oneBidActualMoney = oneBidInfo.getTotalAmount().subtract(oneBidInfoDetail.getReturnCapAmount());
		//oneBid的匹配记录集合
		BidMatch condition = new BidMatch();
		if (matchType == MATCH_BID_TYPE_GOOD) {
			condition.setGoodBidId(oneBidId);
		} else {
			condition.setComnBidId(oneBidId);
		}
		condition.setState(MATCH_STATE_SUCCESS);
		//匹配标的匹配记录集合
		List<BidMatch> oneBidMatchList = bidMatchService.findBidMatchList(condition);
		//获取oneBid的开始匹配天数：startTerm，待匹配金额：money，总天数：endTerm
		ResponseEntity oneBidResponse = BidMatchUtil.getMatchTermAndMoney(oneBidInfo, oneBidNextMatchDate,
				oneBidActualMoney, oneBidMatchList, matchType,new Date());
		//oneBid开始匹配天数
		Integer oneBidStartTerm = (Integer) oneBidResponse.getParams().get("startTerm");
		//oneBid待匹配金额
		BigDecimal oneBidMathMoney = (BigDecimal) oneBidResponse.getParams().get("money");

		//记录oneBid待匹配金额，用于生成匹配记录时进行金额计算
		BigDecimal oneBidMathMoneyTmp = oneBidMathMoney;
		//oneBid匹配总天数
		Integer oneBidEndTerm = (Integer) oneBidResponse.getParams().get("endTerm");
		//oneBid可匹配天数
		Integer oneBidMatchTerm = oneBidEndTerm - oneBidStartTerm + 1;

		//校验匹配标相关参数信息，校验标的还款方式、提前匹配天数、可匹配金额、可匹配天数
		ResponseEntity validateResult = validateMatchCondition(oneBidInfo,oneBidNextMatchDate,oneBidMatchTerm,oneBidMathMoney);
		if (validateResult.getResStatus() == ERROR){
			return validateResult;
		}
		//moreBid总待匹配金额
		BigDecimal moreBidTotalMatchMoney = BigDecimal.ZERO;
		//oneBid对应moreBids的匹配记录集合
		List<BidMatch> oneToMoreBidMatchList = new ArrayList<>();
		//批量更新标地的下次匹配时间及匹配状态
		List<BidInfoDetail> updateBidInfoDetailList = new ArrayList<>();

		for (Integer moreBidId : moreBidIdList) {
			BidInfo moreBidInfo = bidInfoService.findBidInfoById(moreBidId);
			BidInfoDetail moreBidInfoDetail = bidInfoDetailService.findBidInfoDetailByBidId(moreBidId);
			//获取待匹配标地产品类型，判断待匹配标的产品类型是否正确
			BidProduct moreBidProduct = bidProductService.findBidProductById(moreBidInfo.getBidProductId());
			if (matchType.equals(BidInfoUtil.matchBidTypeByProdutType(moreBidProduct.getType()))){
				return new ResponseEntity<>(ERROR,"[" + oneBidInfo.getName() + "]: 产品类型不正确！");
			}
			//moreBid下次匹配时间
			Date moreBidNextMatchDate = moreBidInfoDetail.getNextMatchDate();
			//moreBid标地实际金额 = 标地金额 - 已提前还款本金
			BigDecimal moreBidActualMoney = moreBidInfo.getTotalAmount()
					.subtract(moreBidInfoDetail.getReturnCapAmount());
			//moreBid的匹配记录集合
			BidMatch moreCondition = new BidMatch();
			//oneBid为优选moreBid为散标，oneBid为散标moreBid为优选
			if (matchType == MATCH_BID_TYPE_GOOD) {
				moreCondition.setComnBidId(moreBidId);
			} else {
				moreCondition.setGoodBidId(moreBidId);
			}
			moreCondition.setState(MATCH_STATE_SUCCESS);
			//待匹配标的匹配记录集合
			List<BidMatch> moreBidMatchList = bidMatchService.findBidMatchList(moreCondition);
			//moreBid的开始匹配天数：startTerm 待匹配金额：money 总天数：endTerm
			ResponseEntity moreBidResponse = BidMatchUtil.getMatchTermAndMoney(moreBidInfo, moreBidNextMatchDate,
					moreBidActualMoney, moreBidMatchList,
					matchType == MATCH_BID_TYPE_GOOD ? MATCH_BID_TYPE_COMMON : MATCH_BID_TYPE_GOOD,new Date());
			//moreBid开始匹配天数
			Integer moreBidStartTerm = (Integer) moreBidResponse.getParams().get("startTerm");
			//moreBid待匹配金额
			BigDecimal moreBidMathMoney = (BigDecimal) moreBidResponse.getParams().get("money");
			//moreBid匹配总天数
			Integer moreBidEndTerm = (Integer) moreBidResponse.getParams().get("endTerm");
			//moreBid可匹配天数
			Integer moreBidMatchTerm = moreBidEndTerm - moreBidStartTerm + 1;
			//校验待匹配标相关参数信息，校验标的还款方式、提前匹配天数、可匹配金额、可匹配天数
			ResponseEntity moreBidValidateResult = validateMatchCondition(moreBidInfo,moreBidNextMatchDate,moreBidMatchTerm,moreBidMathMoney);
			if (moreBidValidateResult.getResStatus() == ERROR){
				return moreBidValidateResult;
			}
			//计算moreBid总待匹配金额
			moreBidTotalMatchMoney = moreBidTotalMatchMoney.add(moreBidMathMoney);
			if (CompareUtil.gt(oneBidMathMoneyTmp, moreBidMathMoney)) {
				oneBidMathMoneyTmp = oneBidMathMoneyTmp.subtract(moreBidMathMoney);
			} else {
				moreBidMathMoney = oneBidMathMoneyTmp;
			}
			//比较oneBid与moreBid的可匹配天数取最小值
			Integer oneAndMoreMinTerm = moreBidMatchTerm;
			if (moreBidMatchTerm > oneBidMatchTerm) {
				oneAndMoreMinTerm = oneBidMatchTerm;
			}
			//生成oneBid与moreBid匹配记录
			BidMatch oneAndMoreBidMatch = new BidMatch();
			oneAndMoreBidMatch.setMidMoney(moreBidMathMoney);
			oneAndMoreBidMatch.setState(MATCH_STATE_SUCCESS);
			oneAndMoreBidMatch.setReturnCapState(0);
			oneAndMoreBidMatch.setCreateTime(new Date());
			oneAndMoreBidMatch.setModifyTime(new Date());
			oneAndMoreBidMatch.setCreateUserId(userId);
			oneAndMoreBidMatch.setModifyUserId(userId);
			if (matchType == MATCH_BID_TYPE_GOOD) {
				oneAndMoreBidMatch.setGoodTermValue(oneBidStartTerm + ":" + (oneBidStartTerm + oneAndMoreMinTerm - 1));
				oneAndMoreBidMatch
						.setCommTermValue(moreBidStartTerm + ":" + (moreBidStartTerm + oneAndMoreMinTerm - 1));
				oneAndMoreBidMatch.setComnBidId(moreBidId);
				oneAndMoreBidMatch.setGoodBidId(oneBidId);
			} else {
				oneAndMoreBidMatch.setCommTermValue(oneBidStartTerm + ":" + (oneBidStartTerm + oneAndMoreMinTerm - 1));
				oneAndMoreBidMatch
						.setGoodTermValue(moreBidStartTerm + ":" + (moreBidStartTerm + oneAndMoreMinTerm - 1));
				oneAndMoreBidMatch.setComnBidId(oneBidId);
				oneAndMoreBidMatch.setGoodBidId(moreBidId);
			}
			//存储oneBid对应MoreBid的匹配记录
			oneToMoreBidMatchList.add(oneAndMoreBidMatch);

			//获取moreBid的下次匹配时间,将当前生成的匹配记录与之前查询出来的匹配记录组合
			moreBidMatchList.add(oneAndMoreBidMatch);
			//获取moreBid更新下次匹配时间和标地匹配状态
			BidInfoDetail moreBidInfoDetailUpdate = this.getBidInfoDetailUpdate(moreBidStartTerm, moreBidInfo,
					moreBidInfoDetail, moreBidActualMoney, moreBidMatchList,
					matchType == MATCH_BID_TYPE_GOOD ? MATCH_BID_TYPE_COMMON : MATCH_BID_TYPE_GOOD, moreBidEndTerm);
			updateBidInfoDetailList.add(moreBidInfoDetailUpdate);
		}
		//获取oneBid下次匹配时间,将oneBid此次生成的匹配记录集合与查询出的匹配记录集合组合
		oneBidMatchList.addAll(oneToMoreBidMatchList);

		BidInfoDetail oneBidInfoDetailUpdate = this.getBidInfoDetailUpdate(oneBidStartTerm, oneBidInfo,
				oneBidInfoDetail, oneBidActualMoney, oneBidMatchList, matchType, oneBidEndTerm);
		updateBidInfoDetailList.add(oneBidInfoDetailUpdate);
		//一个优选匹配多个散标时，优选标待匹配金额 >= 多个散标待匹配金额
		if (matchType == MATCH_BID_TYPE_GOOD) {
			if (CompareUtil.gt(moreBidTotalMatchMoney, oneBidMathMoney)) {
				return new ResponseEntity<>(Constants.ERROR, "优选标匹配金额需大于等于散标匹配金额");
			}
		} else {
			//一个散标匹配多个优选时，散标待匹配金额 <= 多个优选待匹配金额
			if (CompareUtil.gt(oneBidMathMoney, moreBidTotalMatchMoney)) {
				return new ResponseEntity<>(Constants.ERROR, "优选标匹配金额需大于等于散标匹配金额");
			}
		}
		//返回优选投资记录id对应优选投资记录更新对象的Map，匹配记录hashCode对应优选投资记录对应散标投资记录的Map
		Map<String, Object> paramMap = investForMatch(oneToMoreBidMatchList);
		//优选投资记录id对应优选投资记录更新对象的Map
		Map<Integer, BidInvest> goodInvestUpdateCondition = (Map<Integer, BidInvest>) paramMap.get("goodInvestUpdateCondition");
		//初始化优选投资记录更新对象集合
		List<BidInvest> goodInvestUpdateTmpList = new ArrayList<>();
		goodInvestUpdateCondition.forEach((k, v) -> goodInvestUpdateTmpList.add(v));
		//匹配记录hashCode对应优选投资记录对应散标投资记录的Map
		Map<Integer, List<BidInvest>> bidMatchToCommonInvestListMap = (Map<Integer, List<BidInvest>>) paramMap.get("bidMatchToCommonInvestListMap");
		//初始化批量处理债权转让集合
		List<TransferAutoVo> transferAutoVoList = new ArrayList<>();
		//初始化投资成功返回的数据集合
		List<ResponseEntity> investResultList = new ArrayList<>();
		ResponseEntity<?> result = new ResponseEntity<>(SUCCESS);
		//初始化flag为false，用于处理散标匹配优选时，只操作一次投资散标操作
		boolean flag = false;
		try {
			//循环标地匹配记录
			for (BidMatch bidMatch : oneToMoreBidMatchList) {
				//获取匹配的优选
				BidInfo goodBidInfo = this.bidInfoService.findBidInfoById(bidMatch.getGoodBidId());
				//获得匹配的散标
				BidInfo comBidInfo = this.bidInfoService.findBidInfoById(bidMatch.getComnBidId());
				//获取匹配散标详情
				BidInfoDetail comBidInfoDetail = this.bidInfoDetailService
						.findBidInfoDetailByBidId(bidMatch.getComnBidId());
				//散标的开始天数
				final int commonStartMatchTerm = BidMatchUtil.getStartTerm(bidMatch, MATCH_BID_TYPE_COMMON);
				//判断是否需要生成债权转让关系；散标在此次匹配之前有匹配记录则为债权转让
				BidMatch matchCountCondition = new BidMatch();
				condition.setState(InvestConstants.MATCH_STATE_SUCCESS);
				condition.setComnBidId(comBidInfo.getId());
				int count = bidMatchService.findBidMatchCount(matchCountCondition);
				//获取该匹配记录对应的优选投资记录对应的散标投资记录集合
				List<BidInvest> goodMatchBidInvestList = bidMatchToCommonInvestListMap.get(bidMatch.hashCode());
				//该散标匹配记录数>0且开始匹配期数>1；一个散标第一次匹配多个个优选标地时，不为债权转让
				if (count > 0 && commonStartMatchTerm > 1) {
					//删除该散标对应的记录，删除的投资记录将在生成债权转让关系时插入；该Map用于获取不为债权转让的散标投资记录集合
					bidMatchToCommonInvestListMap.remove(bidMatch.hashCode());
					//初始化该散标原债权转让投资记录列表
					List<BidInvest> transferInvestList = new ArrayList<>();
					BidInvest commonInvestCondition = new BidInvest();
					commonInvestCondition.setInvestChannel(BID_INVEST_CHANNEL_MATCH);
					commonInvestCondition.setState(INVEST_STATE_SUCCESS);
					commonInvestCondition.setBidInfoId(comBidInfo.getId());
					//获取该散标对应的投资记录
					List<BidInvest> commonInvestList = bidInvestService.findBidInvestList(commonInvestCondition);
					//将散标投资记录中可债权转让的投资记录过滤出来；散标当前开始匹配天数>投资记录散标结束匹配天数；
					for (BidInvest bidInvest : commonInvestList) {
						String commonInvestTerm = bidInvest.getInvestTerm();
						int commonInvestEndTerm = Integer.valueOf(commonInvestTerm.split(":")[1]);
						if (commonStartMatchTerm > commonInvestEndTerm) {
							transferInvestList.add(bidInvest);
						}
					}
					//获取散标债权转让时间
					Date transferTime = new Date();
					Date nextMatchDate = comBidInfoDetail.getNextMatchDate();
					//如果下次匹配时间大于当前时间，则债权转让时间为下次匹配时间
					if (nextMatchDate.after(transferTime)) {
						transferTime = nextMatchDate;
					}
					TransferAutoVo transferAutoVo = new TransferAutoVo();
					transferAutoVo.setCommBidInfo(comBidInfo);
					transferAutoVo.setTransferInvestList(transferInvestList);
					transferAutoVo.setReceiveInvestList(goodMatchBidInvestList);
					transferAutoVo.setTransferTime(transferTime);
					transferAutoVoList.add(transferAutoVo);
				} else {
					if (!flag) {
						//散标匹配优选时，只生成一条投资散标记录
						if (MATCH_BID_TYPE_COMMON.equals(matchType)) {
							flag = true;
						}
						//获取当前操作用户信息
						RegUser regUser = this.regUserService.findRegUserById(goodBidInfo.getBorrowerId());
						//散标首次匹配，生成优选对散标的投资
						ResponseEntity<?> resultTmp = this.invest(regUser, -1, -1, comBidInfo.getTotalAmount(),
								comBidInfo.getId(), TradeTransferConstants.TRADE_TYPE_INVEST_MATCH,
								InvestConstants.BID_INVEST_TYPE_MANUAL, PlatformSourceEnums.PC);
						if (resultTmp.getResStatus() == SUCCESS) {
							investResultList.add(resultTmp);
						} else {
							result = resultTmp;
							break;
						}
					}
				}
			}
			//任一投资散标失败，若有投资散标成功的记录则循环处理回滚操作
			if (result.getResStatus() != SUCCESS) {
				if (!investResultList.isEmpty()) {
					for (ResponseEntity<?> tmp : investResultList) {
						this.bidInvestService.updateBidInvestForRollback((BidInvest) tmp.getParams().get("bidInvest"),
								(BidInfo) tmp.getParams().get("bidInfo"));
					}
				}
				logger.error("match, 匹配生成投资记录失败, 用户标识: {}, oneBidId: {}, moreBidIdList: {}, matchType: {}, 异常投资信息: {}",
						userId, oneBidId, moreBidIdList, matchType, result.toString());
				return new ResponseEntity<>(Constants.ERROR, result.getResMsg());
			} else {
				//初始化需要批量插入的优选投资记录对应散标的投资记录集合
				List<BidInvest> bidInvestList = new ArrayList<>();
				//遍历bidMatchToCommonInvestListMap，组装散标投资记录集合；债权转让相关的散标投资记录在生成债权转让关系时批量插入
				bidMatchToCommonInvestListMap.forEach((k, v) -> bidInvestList.addAll(v));
				//批量插入散标投资记录集合、生成散标投资记录间债权转让关系、批量插入标地匹配记录、批量更新优选投资记录匹配状态、批量更新标地匹配状态
				ResponseEntity matchResult = bidTransferAutoService.match(bidInvestList, transferAutoVoList, oneToMoreBidMatchList,
						goodInvestUpdateTmpList, updateBidInfoDetailList, userId);
				//返回生成合同用的投资记录id集合
				ResponseEntity<?> resultRes = new ResponseEntity<>();
				Map<String, Object> params = new HashMap<>(2);
				params.put("investListForCommonContract", matchResult.getParams().get("investListForCommonContract"));
				params.put("investListForTransferContract", matchResult.getParams().get("investListForTransferContract"));
				resultRes.setParams(params);
				resultRes.setResStatus(Constants.SUCCESS);
				return resultRes;
			}
		} catch (Exception e) {
			logger.error("match, 匹配异常, 用户标识: {}, oneBidId: {}, moreBidIdList: {}, matchType: {}, 异常信息: ",
					userId, oneBidId, moreBidIdList, matchType, e);
			if (CommonUtils.isNotEmpty(investResultList)) {
				for (ResponseEntity<?> tmp : investResultList) {
					this.bidInvestService.updateBidInvestForRollback((BidInvest) tmp.getParams().get("bidInvest"),
							(BidInfo) tmp.getParams().get("bidInfo"));
				}
			}
			throw new GeneralException("标地匹配异常！");
		}finally {
			lock.freeLock(lockKey);
		}
	}

	/**
	 *  @Description    ：匹配时校验标地相关参数是否正确
	 *  @Method_Name    ：validateMatchCondition
	 *  @param bidInfo  标地信息
	 *  @param bidNextMatchDate  标的下次匹配时间
	 *  @param bidMatchTerm  标的可匹配天数
	 *  @param bidMathMoney  标的可匹配金额
	 *  @return com.yirun.framework.core.model.ResponseEntity
	 *  @Creation Date  ：2018/5/21
	 *  @Author         ：pengwu@hongkun.com.cn
	 */
	private ResponseEntity validateMatchCondition(BidInfo bidInfo,Date bidNextMatchDate,
												  Integer bidMatchTerm,BigDecimal bidMathMoney){
		//判断标地还款方式是否为：2-按月付息，3-到期还本付息,6-按月付息，本金划归企业,7-按月付息，按日计息，否则不允许匹配
		if (!MATCH_VALID_REPAYMENT_WAY.contains(bidInfo.getBiddRepaymentWay())){
			return new ResponseEntity<>(ERROR,"[" + bidInfo.getName() + "]: 还款方式不正确！");
		}
		//判断匹配标是否为下次匹配时间的前三天内进行的匹配
		if (DateUtils.getDays(bidNextMatchDate,new Date()) > 3){
			return new ResponseEntity<>(ERROR,"[" + bidInfo.getName() + "]: 只能提前3天进行匹配！");
		}
		//匹配金额必须大于0
		if (CompareUtil.lteZero(bidMathMoney)) {
			return new ResponseEntity<>(ERROR, "[" + bidInfo.getName() + "]: 匹配金额不大于0，无法匹配！");
		}
		//可匹配天数必须大于0
		if (bidMatchTerm <= 0){
			return new ResponseEntity<>(ERROR,"[" + bidInfo.getName() + "]: 可匹配天数需大于0！");
		}
		return new ResponseEntity(SUCCESS);
	}
	/**
	 * @Description : 生成优选投资人对应散标的投资记录
	 * @Method_Name : investForMatch
	 * @param bidMatchList 匹配记录集合
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年7月26日 上午11:17:21
	 * @Author : pengwu@hongkun.com.cn
	 */
	private Map<String, Object> investForMatch(List<BidMatch> bidMatchList) {
		Map<String, Object> paramMap = new HashMap<>(2);
		//Key：优选投资记录id，Value：优选投资记录对应散标投资记录；优选投资记录对应散标的投资记录集合
		Map<Integer, List<BidInvest>> goodInvestToCommonInvestListMap = new HashMap<>();
		//Key：优选投资记录id，Value：优选投资记录更新条件；优选投资记录对应更新条件，由于一条优选投资记录可能会产生多个更新，使用该Map只记录最新一次更新条件
		Map<Integer, BidInvest> goodInvestUpdateCondition = new HashMap<>();
		//Key：匹配记录hashCode，Value：优选投资记录对应散标投资记录；记录匹配记录对应的优选投资记录对应散标投资记录,用于债券转让时使用；
		Map<Integer, List<BidInvest>> bidMatchToCommonInvestListMap = new HashMap<>();
		//循环处理匹配记录
		for (BidMatch bidMatch : bidMatchList) {
			//优选投资记录对应散标投资记录集合
			List<BidInvest> goodMatchBidInvestList = new ArrayList<>();
			//获取匹配记录的优选标信
			BidInfo goodBidInfo = this.bidInfoService.findBidInfoById(bidMatch.getGoodBidId());
			//优选标的匹配总天数
			int goodTotalMatchTerm = CalcInterestUtil.calInvestDays(goodBidInfo.getTermUnit(),
					goodBidInfo.getTermValue());
			//优选标的开始匹配天数
			final int goodStartMatchTerm = BidMatchUtil.getStartTerm(bidMatch, MATCH_BID_TYPE_GOOD);
			//优选标的结束匹配天数
			final int goodEndMatchTerm = BidMatchUtil.getEndTerm(bidMatch, MATCH_BID_TYPE_GOOD);
			//获得优选的可匹配的投资记录集合(投资状态为1&(未匹配|部分匹配)&创建时间的降序)
			List<BidInvest> goodBidInvestList = this.bidInvestService.findToBeMatchedBidInvestList(bidMatch.getGoodBidId());
			//待匹配金额，生成投资记录时，金额将减少;待匹配金额 = 生成投资记录金额总和
			BigDecimal expectMatchAmount = bidMatch.getMidMoney();

			for (BidInvest goodBidInvest : goodBidInvestList) {
				//待匹配金额匹配完毕跳出循环
				if (CompareUtil.ltZero(expectMatchAmount)) {
					break;
				}
				//初始化投资金额
				BigDecimal investAmount;
				//获取该优选投资记录对应的散标投资记录列表
				BidInvest comBidInvestCdt = new BidInvest();
				comBidInvestCdt.setGoodInvestId(goodBidInvest.getId());
				List<BidInvest> comBidInvestList = this.bidInvestService.findBidInvestList(comBidInvestCdt);
				//如果该集合中包含该优选投资记录id，将对应的散标投资记录集合合并
				if (goodInvestToCommonInvestListMap.containsKey(goodBidInvest.getId())) {
					comBidInvestList.addAll(goodInvestToCommonInvestListMap.get(goodBidInvest.getId()));
				}
				//优选投资记录的已匹配金额
				BigDecimal alreadyMatchAmount = this.clacAlreadyMathAmount(goodStartMatchTerm, comBidInvestList);
				//优选投资记录的待匹配金额 = 优选投资记录金额 - 已匹配金额
				BigDecimal waitMatchAmount = goodBidInvest.getInvestAmount().subtract(alreadyMatchAmount)
						.subtract(goodBidInvest.getTransAmount());
				//优选投资记录的待匹配金额不大于0则继续循环
				if (CompareUtil.lteZero(waitMatchAmount)) {
					continue;
				}
				//优选投资记录的待匹配金额 >= 匹配记录待匹配金额，该笔投资记录金额为匹配记录待匹配金额
				if (CompareUtil.gte(waitMatchAmount, expectMatchAmount)) {
					investAmount = expectMatchAmount;
				} else {
					//优选投资记录的待匹配金额 < 匹配记录待匹配金额，该笔投资记录金额为优选投资记录待匹配金额
					investAmount = waitMatchAmount;
				}
				//匹配记录待匹配金额 = 匹配记录待匹配金额 - 投资记录金额
				expectMatchAmount = expectMatchAmount.subtract(investAmount);
				if (CompareUtil.gtZero(investAmount)) {
					//组装优选投资记录对应散标的投资记录
					BidInvest goodMatchBidInvest = this.fitGoodMatchBidInvest(goodBidInvest, bidMatch, investAmount);
					goodMatchBidInvestList.add(goodMatchBidInvest);
					//初始化优选投资记录对应散标投资记录集合
					List<BidInvest> commonInvestList;
					//如果Map中有优选投资记录对应散标的集合则获取Map中对应的集合，否则创建一个新的集合
					if (goodInvestToCommonInvestListMap.containsKey(goodBidInvest.getId())) {
						commonInvestList = goodInvestToCommonInvestListMap.get(goodBidInvest.getId());
					} else {
						commonInvestList = new ArrayList<>();
					}
					commonInvestList.add(goodMatchBidInvest);
					//由于优选投资记录可能循环多次，需要将此次生成的散标投资记录和以前的散标投资记录组合
					goodInvestToCommonInvestListMap.put(goodBidInvest.getId(), commonInvestList);
					//初始化优选投资记录更新条件，用于更新优选投资记录匹配状态
					BidInvest goodBidInvestTmp = new BidInvest();
					goodBidInvestTmp.setId(goodBidInvest.getId());
					//优选投资记录匹配状态默认为2-部分匹配
					goodBidInvestTmp.setMatchState(InvestConstants.MATCH_STATE_PART);
					/*
					 * 判断当前优选投资记录是否为完全匹配；此次优选匹配结束天数=优选匹配总天数，再进行判断该优选投资记录；
					 * 优选投资记录对应的散标投资记录在优选标最大匹配天数，当前投资散标金额+已匹配金额=优选投资记录金额则为完全匹配；
					 */
					if (goodEndMatchTerm == goodTotalMatchTerm) {
						//获取优选投资记录对应散标投资记录endTerm = totalTerm记录总金额
						BigDecimal alreadyMatchMoney = getEndTermMatchMoney(comBidInvestList, goodTotalMatchTerm);
						//当前投资散标金额+已匹配金额=优选投资记录金额则为完全匹配
						if (goodBidInvest.getInvestAmount().compareTo(investAmount.add(alreadyMatchMoney)) == 0) {
							goodBidInvestTmp.setMatchState(InvestConstants.MATCH_STATE_FINISH);
						}
					}
					//Key：优选投资记录id，Value：优选投资记录更新条件；保存优选投资记录更新条件对象
					goodInvestUpdateCondition.put(goodBidInvest.getId(), goodBidInvestTmp);
				}
			}
			//Key：匹配记录hashCode，Value：优选投资记录对应散标投资记录；保存匹配记录生成的优选投资记录对应散标的投资记录
			bidMatchToCommonInvestListMap.put(bidMatch.hashCode(), goodMatchBidInvestList);
		}
		paramMap.put("goodInvestUpdateCondition", goodInvestUpdateCondition);
		paramMap.put("bidMatchToCommonInvestListMap", bidMatchToCommonInvestListMap);
		return paramMap;
	}

	/**
	 * @Description : 计算优选投资记录已匹配的金额
	 * @Method_Name : clacAlreadyMathAmount
	 * @param goodStartMatchTerm 优选匹配的开始天数
	 * @param comBidInvestList 优选投资记录对应散标投资记录
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月19日 下午3:38:21
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BigDecimal clacAlreadyMathAmount(int goodStartMatchTerm, List<BidInvest> comBidInvestList) {
		//初始化优选投资记录待匹配金额
		BigDecimal amount = BigDecimal.ZERO;
		/*
		 * 遍历优选投资记录对应的散标投资记录计算累计已匹配金额；
		 * 优选投资记录对应散标的投资记录：开始匹配天数<=当前匹配天数<=结束匹配天数，该散标投资记录的投资金额则为该优选投资记录的已匹配金额；
		 */
		for (BidInvest comBidInvest : comBidInvestList) {
			String term = comBidInvest.getGoodInvestTerm();
			int investGoodStartMatchTerm = Integer.parseInt(term.split(":")[0]);
			int investGoodEndMatchTerm = Integer.parseInt(term.split(":")[1]);
			//满足以下条件为已匹配金额
			if (goodStartMatchTerm >= investGoodStartMatchTerm && goodStartMatchTerm <= investGoodEndMatchTerm) {
				amount = amount.add(comBidInvest.getInvestAmount().subtract(comBidInvest.getTransAmount()));
			}
		}
		return amount;
	}

	/**
	 * @Description : 组装匹配的投资记录
	 * @Method_Name : fitBidInvestForGoodMatch
	 * @param bidInvest  优选标投资记录
	 * @param bidMatch  匹配记录
	 * @param investAmount  投资散标的金额
	 * @return : BidInvest
	 * @Creation Date : 2017年7月19日 下午2:42:27
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private BidInvest fitGoodMatchBidInvest(BidInvest bidInvest, BidMatch bidMatch, BigDecimal investAmount) {
		BidInvest result = new BidInvest();
		result.setRegUserId(bidInvest.getRegUserId());
		result.setRealName(bidInvest.getRealName());
		result.setInvestAmount(investAmount);
		result.setPayAmount(investAmount);
		result.setBidInfoId(bidMatch.getComnBidId());
		result.setInvestTerm(bidMatch.getCommTermValue());
		result.setGoodBidId(bidMatch.getGoodBidId());
		result.setGoodInvestTerm(bidMatch.getGoodTermValue());
		result.setGoodInvestId(bidInvest.getId());
		result.setTransAmount(BigDecimal.ZERO);
		result.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_MATCH);
		return result;
	}

	/**
	 * @Description : 计算优选投资记录对应散标投资记录endTerm=totalTerm的总金额
	 * @Method_Name : getEndTermMatchMoney
	 * @param comBidInvestList
	 *            优选投资对应的散标投资记录
	 * @param goodTotalMatchTerm
	 *            优选标总天数
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月26日 上午10:11:21
	 * @Author : pengwu@hongkun.com.cn
	 */
	private BigDecimal getEndTermMatchMoney(List<BidInvest> comBidInvestList, int goodTotalMatchTerm) {
		BigDecimal amount = BigDecimal.ZERO;

		// 遍历优选投资记录对应的散标投资记录计算累计待匹配金额
		for (BidInvest comBidInvest : comBidInvestList) {
			String term = comBidInvest.getGoodInvestTerm();
			int investGoodEndMatchTerm = Integer.parseInt(term.split(":")[1]);
			// 满足以下条件累加金额
			if (investGoodEndMatchTerm == goodTotalMatchTerm) {
				amount = amount.add(comBidInvest.getInvestAmount().subtract(comBidInvest.getTransAmount()));
			}
		}
		return amount;
	}

	/**
	 * @Description : 组装标地匹配状态、标地下次匹配时间的标的详情
	 * @Method_Name : getBidInfoDetailUpdate
	 * @param bidInfo  标地信息
	 * @param bidInfoDetail  标地详情信息
	 * @param matchAmount  匹配总金额（标的金额-提前还本金额）
	 * @param bidMatchList  该标匹配记录集合
	 * @param bidType  标地类型：1-优选，2-散标
	 * @param bidTotalTerm  该标匹配总天数
	 * @return : BidInfoDetail
	 * @Creation Date : 2017年7月27日 上午10:57:21
	 * @Author : pengwu@hongkun.com.cn
	 */
	private BidInfoDetail getBidInfoDetailUpdate(int startMatchTerm,BidInfo bidInfo, BidInfoDetail bidInfoDetail,
												 BigDecimal matchAmount, List<BidMatch> bidMatchList,
												 Integer bidType, Integer bidTotalTerm) {
		Date bidUpdateNextMatchDate = BidMatchUtil.getNextMatchDate(startMatchTerm,bidInfo, bidInfoDetail, matchAmount,
				bidMatchList, bidType);
		BidInfoDetail bidInfoDetailUpdate = new BidInfoDetail();
		bidInfoDetailUpdate.setId(bidInfoDetail.getId());
		bidInfoDetailUpdate.setNextMatchDate(bidUpdateNextMatchDate);
		//标的最大匹配天数的可匹配金额为0则为完全匹配，否则为部分匹配
		if (BidMatchUtil.matchComplete(matchAmount, bidMatchList, bidType, bidTotalTerm)) {
			bidInfoDetailUpdate.setMatchState(MATCH_STATE_FINISH);
		} else {
			bidInfoDetailUpdate.setMatchState(MATCH_STATE_PART);
		}
		return bidInfoDetailUpdate;
	}

	@Override
	public List<BidInvestDetailVO> findBidInvestListForApp(BidInvestDetailVO vo) {
		//只查询直投
        vo.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
		List<BidInvestDetailVO> result = this.bidInvestService.findBidInvestDetailList(vo);
		result.forEach(investTmp -> {
            //投资金额=投资金额-已转让金额
            investTmp.setInvestAmount(investTmp.getInvestAmount().subtract(investTmp.getTransAmount()));
            investTmp.setFinishTime(BidInfoUtil.getFinishTime(investTmp.getLendingTime(), investTmp.getTermValue(), investTmp.getTermUnit()));
            investTmp.setBiddRepaymentWayView(this.dicDataService.findNameByValue("invest", "bid_repayment", investTmp.getBiddRepaymentWay()));
            //预期年化率 & 投资期限
            if(investTmp.getBidInvestState()== InvestConstants.INVEST_STATE_SUCCESS_BUYER){
                //查询转让记录
                BidTransferManual bidTransferManual = bidTransferManualService.findBidTransferByNewInvestId(investTmp.getBidInvestId());
                investTmp.setTransferId(bidTransferManual.getId());
                investTmp.setTermUnit(BID_TERM_UNIT_DAY);
                investTmp.setTermValue(bidTransferManual.getBuyerHoldDays());
            }
        });
		return result;
	}

	@Override
	public Pager findRecommendListForInvest(Pager pager, Integer regUserId) {
		List<RegUserFriends> friends = regUserFriendsService.findFirstFriendsByRecommendId(regUserId,UserConstants.USER_FRIEND_GRADE_FIRST);
		List<Integer> friendIds = new ArrayList<Integer>();
		if (CommonUtils.isNotEmpty(friends)) {
			friends.forEach(friend -> {
				friendIds.add(friend.getRegUserId());
			});
			List<BidInvestForRecommendVo> list = bidInvestService.findBidInvestByRegUserIds(friendIds);
			if (CommonUtils.isNotEmpty(list)) {
				list.forEach(vo -> {
					vo.setInvestBackStepMoney(vo.getInvestAmount()
							.multiply(CalcInterestUtil.calNiggerRate(vo.getTermUnit(), vo.getTermValue())));
					for (RegUserFriends ff : friends) {
						if (ff.getRegUserId().equals(vo.getRegUserId())) {
							vo.setTel(ff.getTel());
							vo.setRealName(ff.getRealName());
							break;
						}
					}
				});
			}
			pager.setStartRow(pager.getCurrentPage() == 1 ? pager.getCurrentPage()-1:(pager.getCurrentPage()-1) *10);
			return PageHelper.getCurrentPager(list, pager);
		}
		return pager;
	}

	/**
	 * @Description : 发送短信、站内信消息
	 * @Method_Name : sendSmsMsg
	 * @param bidInfo
	 *            标的信息
	 * @param regUser
	 * @param money
	 * @param kAtm
	 *            投资红包金额
	 * @param jAtm
	 *            加息券加息金额
	 * @return : void
	 * @Creation Date : 2017年6月15日 下午5:03:33
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void sendSmsMsg(BidInfo bidInfo, RegUser regUser, BigDecimal money, BigDecimal kAtm, BigDecimal jAtm, BigDecimal investRaiseWorth) {
		try {
            //发送短信站内信
			String msg = SmsMsgTemplate.MSG_INVEST.getMsg();
			List<Object> list = new ArrayList<>();
			list.add(bidInfo.getName());
			list.add(String.valueOf(bidInfo.getTermValue()));
			list.add(this.dicDataService.findNameByValue("invest", "bid_term_unit", bidInfo.getTermUnit()));
			list.add(String.valueOf(bidInfo.getInterestRate()));
			list.add(String.valueOf(money));
			list.add(CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(), money,
					bidInfo.getInterestRate()));
			if (CompareUtil.gtZero(kAtm) && CompareUtil.gtZero(jAtm)) {
				msg = SmsMsgTemplate.MSG_INVEST_K_J.getMsg();
				list.add(String.valueOf(investRaiseWorth.stripTrailingZeros().toPlainString()));
				list.add(String.valueOf(jAtm));
				list.add(String.valueOf(kAtm));
			} else if (CompareUtil.gtZero(kAtm)) {
				msg = SmsMsgTemplate.MSG_INVEST_K.getMsg();
				list.add(String.valueOf(kAtm));
			} else if (CompareUtil.gtZero(jAtm)) {
				msg = SmsMsgTemplate.MSG_INVEST_J.getMsg();
                list.add(String.valueOf(investRaiseWorth.stripTrailingZeros().toPlainString()));
				list.add(String.valueOf(jAtm));
			}
			SmsSendUtil.sendSmsMsgToQueue(
					new SmsWebMsg(regUser.getId(), SmsMsgTemplate.MSG_INVEST.getTitle(), msg,
							SmsConstants.SMS_TYPE_NOTICE, list.toArray()),
					new SmsTelMsg(regUser.getId(), regUser.getLogin(), msg, SmsConstants.SMS_TYPE_NOTICE,
							list.toArray()));
		} catch (Exception e) {
			logger.error("投资成功, 站内信、短信发送失败. 用户: {}\n", regUser.getId(), e);
		}
	}
	
	/**
	*  发送用户升级VIP消息
	*  @Method_Name             ：sendVipSms
	*  @param regUser
	*  @return void
	*  @Creation Date           ：2018/6/6
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	private void sendVipSms(RegUser regUser){
	    try{
            //发送会员升级消息
            RegUser vipRegUser = new RegUser();
            vipRegUser.setId(regUser.getId());
            vipRegUser.setVipFlag(UserConstants.USER_IS_VIP);
            this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_VIP, DestinationType.QUEUE, vipRegUser, JmsMessageType.OBJECT);
	    }catch(Exception e){
            logger.error("投资成功, 用户升级为VIP用户. 用户: {}\n", regUser.getId(), e);
	    }
    }
	
	/**
	 * 自动投资流程 加载所有自动投资配置->验证投资配置有效性->验证投资用户&账户信息有效性->加载所有可投资的标的
	 * ->再次验证投资用户&账户信息有效性->加载可用的红包&加息券->执行投资操作
	 */
	@Override
	public void autoInvest() {
		BidAutoScheme bidAutoScheme = new BidAutoScheme();
		bidAutoScheme.setState(1);
		bidAutoScheme.setSortColumns("modify_time asc");
		List<BidAutoScheme> list = this.bidAutoSchemeService.findAvailableBidAutoSchemeList(bidAutoScheme);
		// 过滤海外投资 及 定期投资限制
		List<BidAutoScheme> autoList = list.stream().filter(e -> {
			if(!validInvestQualification(e.getRegUserId()).validSuc() || validOverseaInvestor(e.getRegUserId()).validSuc()){
				return false;
			}
			return true;
		}).collect(Collectors.toList());

		if (autoList != null && !autoList.isEmpty()) {
			for (BidAutoScheme basCdt : autoList) {
				final Integer bidAuotSchemeId = basCdt.getId();
				/**
				 * 此处使用线程池，需要权衡如下情况 好处：提升处理效率 弊端：导致自动投资配置的排名失效
				 */
				/*CompletableFuture.runAsync(() -> {*/
				BidAutoScheme bidAutoSchemeCdt = new BidAutoScheme();
					bidAutoSchemeCdt.setId(bidAuotSchemeId);
					List<BidAutoScheme> resultList = this.bidAutoSchemeService.findBidAutoSchemeList(bidAutoSchemeCdt);
					if (resultList == null || resultList.isEmpty()) {
						logger.info("自动投资配置标识：{}, 已经被删除!", bidAuotSchemeId);
						return;
					}
					BidAutoScheme bas = resultList.get(0);
					final Integer regUserId = bas.getRegUserId();
					RegUser regUser = BaseUtil.getRegUser(bas.getRegUserId(),
							() -> this.regUserService.findRegUserById(regUserId));
					if (regUser == null) {
						logger.error("自动投资配置:{},未找到对应的用户:{}", bas.getId(), bas.getRegUserId());
						return;
					}
					// TODO 执行自动投资
					this.doAutoInvest(bas,regUser);
				/*}, executors);*/
			}
		}
	}

	/**
	 *  @Description    ：执行具体自动投资
	 *  @Method_Name    ：doAutoInvest
	 *  @param bas
	 *  @param regUser
	 *  @return void
	 *  @Creation Date  ：2018年09月06日 15:26
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	private void doAutoInvest(BidAutoScheme bas,RegUser regUser) {
		// 验证账户是否有足够投资的资金
		if (getAutoInvestMoney(bas, null).getResStatus() != Constants.SUCCESS) {
			logger.info("自动配置标识:{}，用户标识:{},未找到可投资的标!", bas.getId(), bas.getRegUserId());
			return;
		}
		// 加载可以投资的标的
		List<BidInfoVO> bidVoList = this.bidInfoService.findAutoInvestBidList(bas);
		bidVoList = this.getOptionalBidList(bidVoList,bas);
		if(CollectionUtils.isEmpty(bidVoList)){
			logger.info("自动配置标识:{}，用户标识:{},未找到可投资的标!", bas.getId(), bas.getRegUserId());
			return;
		}
		// 设置标识
		int breakBid = bidVoList.size();
		for (BidInfoVO bidVo : bidVoList) {
			breakBid--;
			// 再次验证账户是否有足够投资的资金，并获得可用于投资的金额
			ResponseEntity<?> result = this.getAutoInvestMoney(bas, bidVo);
			if (result.getResStatus() != Constants.SUCCESS) {
				logger.info("自动投资配置: {},对应的用户: {}, 投资标的: {}, 已没有足够的投资金额", bas.getId(), bas.getRegUserId(), bidVo.getId());
				continue;
			}
			// 获得本次投资金额
			BigDecimal money = (BigDecimal) result.getResMsg();
			// 加载可用的红包&加息券
			Map<String, Integer> coupons = this.getBestFit(bas, bidVo, regUser.getId(), money);
			// 执行投资操作
			ResponseEntity<?> res = ApplicationContextUtils.getBean(BidInvestFacade.class)
					.invest(regUser, coupons.get("investRedPacketId"),
							coupons.get("investRaiseInterestId"), money, bidVo.getId(),
							TradeTransferConstants.TRADE_TYPE_INVEST,
							InvestConstants.BID_INVEST_TYPE_AUTO, PlatformSourceEnums.PC);
			logger.info("自动配置{}, 自动投资结果{}", bas.getId(), res);

		}
		if(breakBid <= 0) {
			return;
		}
		doAutoInvest(bas,regUser);

	}


	/**
	 *  @Description    ：获取自动投资最优的标的排序
	 *  @Method_Name    ：getOptionalBidList
	 *  @param bidVoList
	 *  @param bas
	 *  @return java.util.List<com.hongkun.finance.invest.model.vo.BidInfoVO>
	 *  @Creation Date  ：2018年09月06日 14:42
	 *  @Author         ：yunlongliu@hongkun.com.cn
	 */
	private List<BidInfoVO> getOptionalBidList(List<BidInfoVO> bidVoList, BidAutoScheme bas) {
		List<BidInfoVO> resultList = new ArrayList<>();
		FinAccount finAccount = this.finAccountService.findByRegUserId(bas.getRegUserId());
		BigDecimal money = finAccount.getUseableMoney().subtract(bas.getReserveAmount())
				.divideToIntegralValue(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(100));
		Map<Integer, List<BidInfoVO>> listMap = bidVoList.stream().collect(Collectors.groupingBy(BidInfoVO::getProductType));
		listMap.forEach((k,v) ->{
			List<BidInfoVO> tempList = v.stream().filter(e -> CompareUtil.gt(e.getResidueAmount(), money)).collect(Collectors.toList());
			// 拆标
			if(CollectionUtils.isEmpty(tempList)){
				List<BidInfoVO> list;
				if(BaseUtil.equelsIntWraperPrimit(bas.getPriorityType(),2)){
					list = v.stream().sorted(
							Comparator.comparing(BidInfoVO::getResidueAmount).reversed()
									.thenComparing(BidInfoVO::getTermValue)
									.thenComparing((BidInfoVO b) -> b.getCreateTime().getTime()))
									.collect(Collectors.toList());
				}else{
					list = v.stream().sorted(
							Comparator.comparing(BidInfoVO::getResidueAmount).reversed()
									.thenComparing((BidInfoVO e) -> e.getInterestRate().doubleValue()).reversed()
									.thenComparing((BidInfoVO b) -> b.getCreateTime().getTime()))
									.collect(Collectors.toList());
				}

				resultList.addAll(list);
			}
			// 不拆标
			resultList.addAll(tempList);
		});
		return resultList;
	}


	/**
	 * @Description : 获得用户可投资金额的资金
	 * @Method_Name : getAutoInvestMoney
	 * @param bas
	 * @param bidVo
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018年1月24日 下午9:58:14
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> getAutoInvestMoney(BidAutoScheme bas, BidInfoVO bidVo) {
		if (this.rosInfoService.validateRoster(bas.getRegUserId(), RosterType.INVEST_CTL, RosterFlag.BLACK)) {
			logger.error("自动投资配置:{},对应用户：{},无投资权限!", bas.getId(), bas.getRegUserId());
			return ResponseEntity.ERROR;
		}
		FinAccount finAccount = this.finAccountService.findByRegUserId(bas.getRegUserId());
		if (finAccount == null) {
			logger.error("自动投资配置:{},对应用户：{}的账户不存在!", bas.getId(), bas.getRegUserId());
			return ResponseEntity.ERROR;
		}
		if (CompareUtil.gt(BigDecimal.valueOf(100), finAccount.getUseableMoney()) || CompareUtil
				.gt(BigDecimal.valueOf(100), finAccount.getUseableMoney().subtract(bas.getReserveAmount()))) {
			logger.warn("自动投资配置:{},对应用户：{}的账户没有足够的金额", bas.getId(), bas.getRegUserId());
			return ResponseEntity.ERROR;
		}
		BigDecimal money = finAccount.getUseableMoney().subtract(bas.getReserveAmount())
				.divideToIntegralValue(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(100));
		// 通过标的校验投资金额
		if (bidVo != null) {
			if (CompareUtil.gt(money, bidVo.getResidueAmount())) {
				money = bidVo.getResidueAmount();
			}
			if (this.bidInvestService.findBidInvestCountForPrefered(bas.getRegUserId()) > 0
					&& bidVo.getProductType() == InvestConstants.BID_PRODUCT_PREFERRED) {
				logger.warn("自动投资配置:{},对应用户：{}已投资过新手标.", bas.getId(), bas.getRegUserId());
				return ResponseEntity.ERROR;
			}
			if (bidVo.getProductType() == InvestConstants.BID_PRODUCT_PREFERRED
					&& CompareUtil.gt(money, InvestConstants.INVEST_FLEDGLING_INVEST_MAX_ATM)) {
				money = BigDecimal.valueOf(INVEST_FLEDGLING_INVEST_MAX_ATM);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, money);
	}

	/**
	 * @Description : 加载可用的投资红包&加息券
	 * @Method_Name : getBestFit
	 * @param bidAutoScheme
	 *            ：自动投资配置
	 * @param bidVO
	 *            : 标的信息
	 * @param regUserId:
	 *            用户标识
	 * @param money
	 *            : 投资金额
	 * @return : Map<String,Integer>
	 * @Creation Date : 2018年1月25日 上午11:34:42
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> getBestFit(BidAutoScheme bidAutoScheme, BidInfoVO bidVO, Integer regUserId,
			BigDecimal money) {
		Map<String, Integer> map = new HashMap<>();
		map.put("investRedPacketId", 0);
		map.put("investRaiseInterestId", 0);
		// 使用卡券情况 规则不使用 新手标、推荐标、爆款标、标的支持卡券
		if (bidAutoScheme.getUseCouponFlag() == InvestConstants.BID_ALLOW_COUPON_NO
				|| bidVO.getType() == InvestConstants.BID_TYPE_HOT
				|| bidVO.getType() == InvestConstants.BID_TYPE_RECOMMEND
				|| bidVO.getProductType() == InvestConstants.BID_PRODUCT_PREFERRED || bidVO.getAllowCoupon() == 0) {
			return map;
		}
		Map<String, Object> result = this.vasCouponDetailService.getUserInvestUsableCoupon(bidVO.getProductType(),
				regUserId);
		// 获得投资金额满足条件的红包&加息券
		List<VasCouponDetailVO> interestCouponList = ((List<VasCouponDetailVO>) result.get("interestCouponList"))
				.stream()
				.filter(o -> (CompareUtil.gte(money, o.getAmountMin()) && CompareUtil.gte(o.getAmountMax(), money)))
				.collect(Collectors.toList());
		List<VasCouponDetailVO> redPacketsCouponList = ((List<VasCouponDetailVO>) result.get("redPacketsCouponList"))
				.stream()
				.filter(o -> (CompareUtil.gte(money, o.getAmountMin()) && CompareUtil.gte(o.getAmountMax(), money)))
				.collect(Collectors.toList());
		// 价值排序
		interestCouponList.sort((a,b) -> b.getWorth().compareTo(a.getWorth()));
		redPacketsCouponList.sort((a,b) -> b.getWorth().compareTo(a.getWorth()));
		// 标的只支持加息券
		if (!interestCouponList.isEmpty()
				&& bidVO.getAllowCoupon() == InvestConstants.BID_ALLOW_COUPON_RAISE_INTEREST) {
			map.put("investRaiseInterestId", interestCouponList.get(0).getId());
		}
		// 标的只支持红包
		if (!redPacketsCouponList.isEmpty() && bidVO.getAllowCoupon() == InvestConstants.BID_ALLOW_COUPON_RED_PACKET) {
			map.put("investRedPacketId", redPacketsCouponList.get(0).getId());
		}
		// 红包&加息券都支持
		if (bidVO.getAllowCoupon() == InvestConstants.BID_ALLOW_COUPON_YES) {
			if (redPacketsCouponList.isEmpty() && !interestCouponList.isEmpty() ) {
				map.put("investRaiseInterestId", interestCouponList.get(0).getId());
			}
			if(!redPacketsCouponList.isEmpty() && interestCouponList.isEmpty()){
				map.put("investRedPacketId", redPacketsCouponList.get(0).getId());
			}
			if (!redPacketsCouponList.isEmpty() && !interestCouponList.isEmpty()) {
				map.put("investRaiseInterestId", interestCouponList.get(0).getId());
				// 优先使用加息券， 两者都使用是 投资金额必须小于 红包+加息券两者最小的值的和
				for (VasCouponDetailVO redPackageVo : redPacketsCouponList) {
					BigDecimal min = redPackageVo.getAmountMin().add(interestCouponList.get(0).getAmountMin());
					BigDecimal max = redPackageVo.getAmountMax().add(interestCouponList.get(0).getAmountMax());
					if (CompareUtil.gte(money, min) && CompareUtil.gte(max, min)) {
						map.put("investRedPacketId", redPackageVo.getId());
						break;
					}
				}

			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findBidInvestListForApp(Integer regUserId, Integer friendUserId, Pager pager) {
		// 1、查询好友推荐奖励列表
		VasBidRecommendEarn cdt = new VasBidRecommendEarn();
		cdt.setRegUserId(friendUserId);
		cdt.setRecommendRegUserId(regUserId);
		List<VasBidRecommendEarn> bidEarnList = vasBidRecommendEarnService.findVasBidRecommendEarnList(cdt);
		List<BidInvestVoForApp> resultList = new ArrayList<BidInvestVoForApp>();
		if (CommonUtils.isNotEmpty(bidEarnList)) {
			List<Integer> investIds = new ArrayList<Integer>();
			bidEarnList.forEach(bidEarn -> {
				investIds.add(bidEarn.getResourceId());
			});
			// 查询投资记录
			List<BidInvestVoForApp> investList = bidInvestService.findBidInvestListByIds(investIds);
			// 查询活期转入记录
			if (CommonUtils.isNotEmpty(investList)) {
				resultList.addAll(investList);
			}
		}
		List<QdzTransRecord> qdzList = (List<QdzTransRecord>) qdzTransRecordService
				.findQdzTransRecordByRegUserId(friendUserId);
		if (CommonUtils.isNotEmpty(qdzList)) {
			qdzList.forEach(record -> {
				// 查询利率
				QdzRateRecord qdzRateRecord = qdzRateRecordService.findQdzRateRecordByTime(record.getCreateTime());
				BidInvestVoForApp investVo = new BidInvestVoForApp();
				investVo.setMoney(record.getTransMoney());
				investVo.setInvestTime(record.getCreateTime());
				investVo.setTermUnit(4);
				investVo.setRate(qdzRateRecord.getRate());
				resultList.add(investVo);
			});
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultList);
	}

	@Override
	public ResponseEntity<?> friendInvestListForSales(Integer regUserId,Integer state, Integer friendUserId, Pager pager) {
		// 1、判断是否是销售
		RosStaffInfo rosInfo = rosStaffInfoService.findRosStaffInfo(regUserId, RosterConstants.ROSTER_STAFF_TYPE_SALES,
				RosterConstants.ROSTER_STAFF_STATE_VALID,RosterConstants.ROSTER_STAFF_RECOM_STATE_SEND);
		if (rosInfo == null) {
			new ResponseEntity<>(Constants.ERROR, "您没有权限查看好友投资信息");
		}
		// 2、查询投资记录
		Pager resultPager = bidInvestService.friendInvestListForSales(friendUserId, state,pager);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 3、查询到期日和预期收益
		if (!BaseUtil.resultPageHasNoData(resultPager)) {
			List<Integer> investIds = new ArrayList<Integer>();
			List<BidInvestVoForSales> volist = (List<BidInvestVoForSales>) resultPager.getData();
			if (CommonUtils.isNotEmpty(volist)) {
				volist.forEach(bidVo -> {
					if (bidVo.getBidState() == InvestConstants.BID_STATE_WAIT_REPAY
							|| bidVo.getBidState() == InvestConstants.BID_STATE_WAIT_FINISH) {
						investIds.add(bidVo.getInvestId());
						//到期日是最大还款日
						BidReceiptPlan lastPlan = bidReceiptPlanService.findLastReceiptPlan(bidVo.getInvestId());
						bidVo.setExpireDate(lastPlan.getPlanTime());
					} else {
						bidVo.setExpectAmount(CalcInterestUtil.calcInterest(bidVo.getTermUnit(), bidVo.getTermValue(),
								bidVo.getInvestAmount(), bidVo.getRate()));
						//到期日 = 当前日期 + 期数
						Date expireDate = null;
						if(bidVo.getTermUnit() == BID_TERM_UNIT_DAY){
							expireDate = DateUtils.addDays(new Date(),bidVo.getTermValue());
						}else if(bidVo.getTermUnit() == BID_TERM_UNIT_MONTH){
							expireDate = DateUtils.addMonth(new Date(),bidVo.getTermValue());
						}else{
							expireDate = DateUtils.addDays(new Date(),360);
						}
						bidVo.setExpireDate(expireDate);
					}
				});
			}
			if (CommonUtils.isNotEmpty(investIds)) {
				// 查询每笔投资预期收益
				List<Map<String, Object>> list = bidReceiptPlanService.findSumInterestByInvestIds(investIds);
				if (CommonUtils.isNotEmpty(list)) {
					list.forEach(map -> {
						for (BidInvestVoForSales vo : volist) {
							if (map.get("investId") == vo.getInvestId()) {
								vo.setExpectAmount((BigDecimal) map.get("interestAmount"));
								break;
							}
						}
					});
				}
			}
			resultMap.put("investList", volist);
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultMap);
	}
	
	/**
	*  验证用户是不是VIP，或是满足VIP标准
	*  @Method_Name             ：validVipStatus
	*  @param regUser
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date           ：2018/6/6
	*  @Author                  ：zhichaoding@hongkun.com.cn
	*/
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> validVipStatus(RegUser regUser){
		if (logger.isInfoEnabled()) {
			logger.info("为当前用户校验是否VIP用户, 用户: {}",regUser);
		}
	    //是VIP
	    if(Integer.valueOf(UserConstants.USER_IS_VIP).equals(regUser.getVipFlag())) {
            return ResponseEntity.SUCCESS;
	    }
	    //用户在白名单
		if(rosInfoService.validateRoster(regUser.getId(),RosterType.VIP_INVESTOR,RosterFlag.WHITE)){
			return ResponseEntity.SUCCESS;
		}
        //如果不是VIP用户， 查询用户资产是(余额 + 待收金额)， 按需求添加钱袋子账户金额
        BigDecimal total = BigDecimal.ZERO;
        FinAccount finAccount = this.finAccountService.findByRegUserId(regUser.getId());
		if (logger.isInfoEnabled()) {
			logger.info("为当前用户校验是否VIP用户, 用户账户: {}",finAccount);
		}
        if(finAccount != null){
            total = total.add(finAccount.getNowMoney());
        }
        LoanVO loanVO = this.bidReceiptPlanService.findAgencyFundByUserId(regUser.getId(), null);
        if(loanVO != null){
            total = total.add(Optional.ofNullable(loanVO.getAmount()).orElse(BigDecimal.ZERO));
        }
        if(CompareUtil.gte(total, UserConstants.USER_VIP_LOW_STANDARD)) {
            return ResponseEntity.SUCCESS;
        }
        return new ResponseEntity<>(UserConstants.NOT_VIP, "您还不是我们的特定服务用户，请拨打客服电话：" + PropertiesHolder.getProperty("service.hotline"));
    }

	@Override
	public ResponseEntity<?> updateTransState(Integer bidInvestId, Integer transState) {
		//查询还款方式
		try {
			BidInvest bidInvest = this.bidInvestService.findBidInvestById(bidInvestId);
			BidInfoVO bidInfoVO = this.bidInfoService.findBidInfoDetailVo(bidInvest.getBidInfoId());
			int biddRepaymentWay = bidInfoVO.getBiddRepaymentWay();
			if (biddRepaymentWay == InvestConstants.REPAYTYPE_INTEREST_MONTH_PRINCIPAL_END
					|| biddRepaymentWay == InvestConstants.REPAYTYPE_ONECE_REPAYMENT
						|| biddRepaymentWay == InvestConstants.REPAYTYPE_INTEREST_CAL_DAY_REPAY_MONTH){
				BidInvest contidion = new BidInvest();
				contidion.setId(bidInvestId);
				contidion.setTransState(transState);
				bidInvestService.updateBidInvest(contidion);
				return new ResponseEntity<>(Constants.SUCCESS,"设置成功");
			}
			return new ResponseEntity<>(Constants.ERROR,"此债权暂不支持转让");
		} catch (Exception e) {
			logger.error("updateTransState, bidInvestId: {}, transState: {},异常信息 \n", bidInvestId, transState,e);
			throw new GeneralException("设置失败");
		}
	}

    @Override
    public ResponseEntity<?> validInvestQualification(Integer regUserId) {
		// 验证用户是否在投资控制黑名单中
		if(this.rosInfoService.validateRoster(regUserId, RosterType.INVEST_CTL, RosterFlag.BLACK)){
			return ResponseEntity.error("您不是我们特定投资用户，请联系管理员！");
		}
		//验证用户是否在海外产品投资白名单中
		if(this.rosInfoService.validateRoster(regUserId, RosterType.OVERSEA_INVESTOR, RosterFlag.WHITE)){
			return ResponseEntity.SUCCESS;
		}
	    //验证用户是否在投资控制白名单中
        if(this.rosInfoService.validateRoster(regUserId, RosterType.INVEST_CTL, RosterFlag.WHITE)){
            return ResponseEntity.SUCCESS;
        }
        // 用户和用户邀请人是否有投资记录
        if(getUserSelfHasInvest(regUserId)){
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.error("您不是我们特定投资用户，请联系管理员！");
    }

    @Override
    public ResponseEntity<?> validOverseaInvestor(Integer regUserId) {
		// 验证海外用户是否在白名单中
		if(!this.rosInfoService.validateRoster(regUserId, RosterType.OVERSEA_INVESTOR, RosterFlag.WHITE)){
			return ResponseEntity.error("您不是我们特定投资用户，请联系管理员！");
		}
		return ResponseEntity.SUCCESS;
    }

	@Override
	public ResponseEntity<?> findFriendsUseAbleMoney(Integer regUserId, Integer friendUserId) {
		// 1、判断是否是销售
		RosStaffInfo rosInfo = rosStaffInfoService.findRosStaffInfo(regUserId, RosterConstants.ROSTER_STAFF_TYPE_SALES,
				RosterConstants.ROSTER_STAFF_STATE_VALID,RosterConstants.ROSTER_STAFF_RECOM_STATE_SEND);
		if (rosInfo == null) {
			new ResponseEntity<>(Constants.ERROR, "您没有权限查看好友投资信息");
		}
		FinAccount account = finAccountService.findByRegUserId(friendUserId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("useAbleMoney", account.getUseableMoney());
		return new ResponseEntity<>(Constants.SUCCESS,resultMap);
	}
    @Override
    public Integer validQdzEnable(Integer regUserId) {
        int enable = QdzConstants.QDZ_SHOW_FLAG_FLASE;
        ResponseEntity qdzRule = this.vasRebatesRuleService.checkQdzRule();
        if (!qdzRule.validSuc()) {
            return enable;
        }
        // 钱袋子规则
        QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) qdzRule.getParams().get("qdzVasRuleItem");
        BigDecimal currentCreditMoney = qdzVasRuleItem.getResidueBuyAmount();
        QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUserId);
        // 显示规则  (债权池 <= 0 && 用户钱袋子余额 > 0) || 债权池 > 0
        if(CompareUtil.lteZero(currentCreditMoney)){
            if(qdzAccount != null && CompareUtil.gtZero(qdzAccount.getMoney())){
                enable = QdzConstants.QDZ_SHOW_FLAG_TRUE;
            }
        }else{
            enable = QdzConstants.QDZ_SHOW_FLAG_TRUE;
        }
        return (enable > 0) && this.validInvestQualification(regUserId).validSuc() && !validOverseaInvestor(regUserId).validSuc() ? enable : 0;
    }
	@Override
	public boolean getUserSelfHasInvest(Integer regUserId) {
		List<Integer> userIdList = new ArrayList<>();
		//先查询该用户的邀请人
		userIdList.add(regUserId);
        RegUser commendRegUser = this.regUserService.findCommendRegUserById(regUserId);
        if (commendRegUser != null) {
            userIdList.add(commendRegUser.getId());
        }
        //用户和用户邀请人是否有投资记录
        int investNum = bidInvestService.getSelfAndInvitorInvestCount(userIdList);
        if(investNum > 0){
            return true;
        }
        //用户和用户邀请人是否有钱袋子转入记录
        return bidInvestService.getSelfAndInvitorTransferCount(userIdList) > 0;
	}
	
	/**
	*  发送投资数据实时保全信息
	*  @Method_Name             ：sendInvestPreserve
	*  @param regUser
	*  @return void
	*  @Creation Date           ：2019-01-23 10:56:33
	*  @Author                  ：binliang@hongkun.com.cn 梁彬
	*/
	private void sendInvestPreserve(BidInvest invest, BidInfo bidInfo,RegUser regUser, RegUserDetail userDetail ,BigDecimal investRedPacketAtm){
	    try{
            //发送投资数据保全
	    	InvestPreServeTemplate template = new InvestPreServeTemplate();
	    	template.setFlowNo(ContractConstants.INVEST_ANCUN_FLOWNO);
	    	template.setInvestCode(invest.getId().toString());
	    	// 投资人身份信息
	    	template.setInvestUserName(userDetail.getRealName());
	    	template.setInvestPhoneId(regUser.getLogin().toString());
	    	template.setInvestIdCard(userDetail.getIdCard());
	    	template.setInvestRegTime(DateUtils.format(regUser.getCreateTime(), DateUtils.DATE_HH_MM_SS));
	    	// 项目信息
			template.setBiddTitle(bidInfo.getName());
			template.setLoanNo(bidInfo.getId().toString());
			template.setLoanRate(String.valueOf(bidInfo.getRaiseRate()));
			template.setLoanMoney(bidInfo.getTotalAmount());
			template.setLoanDay(bidInfo.getTermValue()+dicDataService.findNameByValue(INVEST, TERM_UNIT, bidInfo.getTermUnit()));
			template.setLoanRepayType(dicDataService.findNameByValue(INVEST, REPAYMENT_MODE, bidInfo.getBiddRepaymentWay()));
			template.setReleaseTime(DateUtils.format(bidInfo.getStartTime(), DateUtils.DATE_HH_MM_SS));
//			template.setRaiseEndTime(DateUtils.format(bidInfo.getLendingTime(), DateUtils.DATE_HH_MM_SS));
			template.setSmallBeginMoney(new BigDecimal(bidInfo.getBidSchemeValue()));
			// 借款人信息
			if (bidInfo.getBorrowerId() > 0) {
				RegUser borrowUser = this.regUserService.findRegUserById(bidInfo.getBorrowerId());
				RegUserDetail borrowUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(bidInfo.getBorrowerId());
				RegUserInfo borrowUserInfo = this.regUserInfoService.findRegUserInfoByRegUserId(bidInfo.getBorrowerId());
				template.setLoanUserName(borrowUserDetail.getRealName());
				template.setLoanIdCard(borrowUserDetail.getIdCard());
				template.setLoanPhoneId(borrowUser.getLogin().toString());
				template.setLoanUserAddress(borrowUserInfo.getContactAddress());
			}
			// 投资信息
			template.setInvestUserId(regUser.getId().toString());
			template.setInvestMoeny(invest.getInvestAmount());
			template.setInvestTime(DateUtils.format(invest.getCreateTime(), DateUtils.DATE_HH_MM_SS));
			template.setInvestPaymentTime(DateUtils.format(invest.getCreateTime(), DateUtils.DATE_HH_MM_SS));
			template.setRedDeduction(investRedPacketAtm);
            this.jmsService.sendMsg(ContractConstants.MQ_QUEUE_INVEST_PRESERVE, DestinationType.QUEUE, template, JmsMessageType.OBJECT);
	    }catch(Exception e){
            logger.error("投资成功, 投资数据进行保全数据异常. 用户: {}\n", regUser.getId(), e);
	    }
    }
	
}
