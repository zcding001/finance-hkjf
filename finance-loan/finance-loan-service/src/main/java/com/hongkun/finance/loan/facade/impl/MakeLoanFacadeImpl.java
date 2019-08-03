package com.hongkun.finance.loan.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.gexin.fastjson.JSON;
import com.hongkun.finance.contract.model.ConInfo;
import com.hongkun.finance.contract.service.ConInfoService;
import com.hongkun.finance.contract.service.ConTemplateService;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.*;
import com.hongkun.finance.invest.model.dto.InvestPointDTO;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidMatchUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.MakeLoanFacade;
import com.hongkun.finance.loan.facade.util.RepayPlanUtils;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.BidCommonPlanVo;
import com.hongkun.finance.loan.model.vo.BidInvestVo;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.loan.util.RepayAndReceiptUtils;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.RecommendEarnBuild;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE;
import static com.hongkun.finance.user.constants.UserConstants.PLATFORM_ACCOUNT_ID;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class MakeLoanFacadeImpl implements MakeLoanFacade {

	private final Logger logger = LoggerFactory.getLogger(MakeLoanFacadeImpl.class);
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private BidProductService bidProductService;
	@Reference
	private FinTradeFlowService finTradeFlowService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private ConInfoService conInfoService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private RegUserInfoService regUserInfoService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private ConTemplateService conTemplateService;
	@Reference
	private BidMatchService bidMatchService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;

	private String MAKELOANS_LOCK_PREFIX = Constants.LOCK_PREFFIX + "bid_";

	@SuppressWarnings("unchecked")
	@Override
	@Compensable
	public ResponseEntity<?> makeLoans(Integer bidInfoId, Integer regUserId){
		logger.info(
				"tcc makeLoans entrance, reference payment#updateAccountInsertTradeAndTransfer. 放款, regUserId: {}, bidInfoId: {}",
				regUserId, bidInfoId);
		BidInfo bidInfo = bidInfoService.findBidInfoById(bidInfoId);
		if (bidInfo == null) {
			return new ResponseEntity<>(Constants.ERROR, "放款标不存在");
		}
		if (bidInfo.getState() != InvestConstants.BID_STATE_WAIT_LOAN) {
			return new ResponseEntity<>(Constants.ERROR, "标的未满标或已放款");
		}
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = MAKELOANS_LOCK_PREFIX + bidInfo.getBidCode();
		try {
			boolean lockResult = lock.lock(lockKey);
			logger.info("makeLoans, 放款, regUserId: {}, 标的信息: {}, 获取锁结果: {}", regUserId, bidInfo.toString(), lockResult);
			if (lockResult) {
				ResponseEntity<?> basicResult = bidInvestService.findInvestAndProduct(bidInfo);
				if (BaseUtil.error(basicResult)) {
					return basicResult;
				}
				BidProduct bidProduct = (BidProduct) basicResult.getParams().get(MAKELOAN_BIDPRODUCT);
				List<BidInvest> bidInvests = (List<BidInvest>) basicResult.getParams().get(MAKELOAN_BIDINVESTS);
				BidInfoDetail bidDetail = (BidInfoDetail) basicResult.getParams().get(MAKELOAN_BIDDETAIL);
				// 1、更新标的状态、下次匹配时间
				updateBidDetailForMakeLoan(bidDetail, bidInfo);
				logger.info("makeLoans, 放款(修改标的状态&下次匹配时间), regUserId: {}, 标的信息:{}, 标的详情", regUserId, bidInfo.toString(),
						bidDetail.toString());
				// 2、处理流水和账户金额
				ResponseEntity<?> makeLResult = null;
				if (bidProduct.getType() == InvestConstants.BID_PRODUCT_EXPERIENCE) {
					makeLResult = makeLoanSimBid(bidInvests, bidInfo);
					logger.info("makeLoans, 放款, regUserId: {}, 标的id:{}, 体验金标放款处理账户&流水结果: {}", regUserId,
							bidInfo.getId(), makeLResult.getResStatus());
				} else {
					makeLResult = makeLoanCommBid(bidInvests, bidInfo);
					logger.info("makeLoans, 放款, regUserId: {}, 标的id:{}, 普通标放款处理账户&流水结果: {}", regUserId, bidInfo.getId(),
							makeLResult.getResStatus());
				}
				if (BaseUtil.error(makeLResult)) {
					throw new GeneralException((String)makeLResult.getResMsg());
				}
				// 3、封装mq消息需要的相关参数
				Map<String, Object> params = initMqMessageForMakeLoan(bidProduct, bidDetail, bidInvests, bidInfo);
				return new ResponseEntity<>(SUCCESS, "放款成功", params);
			} else {
				return new ResponseEntity<String>(ERROR, "放款失败，请稍后再试");
			}
		}catch (GeneralException e){
			logger.error("makeLoans, 放款, regUserId: {}, bidInfoId: {}, 异常信息: ", regUserId, bidInfoId, e);
			throw e;
		}catch (Exception e) {
			logger.error("makeLoans, 放款, regUserId: {}, bidInfoId: {}, 异常信息: ", regUserId, bidInfoId, e);
			throw new GeneralException("放款失败");
		} finally {
			lock.freeLock(lockKey);
		}
	}

	/**
	 * @Description : 放款--封装mq消息参数
	 * @Method_Name : initMqMessageForMakeLoan
	 * @param bidProduct
	 * @param bidDetail
	 * @param bidInvests
	 * @param bidInfo
	 * @return
	 * @throws Exception
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年10月27日 上午8:46:19
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private Map<String, Object> initMqMessageForMakeLoan(BidProduct bidProduct, BidInfoDetail bidDetail,
			List<BidInvest> bidInvests, BidInfo bidInfo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		// 处理成长值
		UserVO borrowerVo = regUserService.findUserWithDetailById(bidInfo.getBorrowerId());
		List<VasVipGrowRecordMqVO> vasVipList = makeLoanForVas(bidInvests, bidInfo, borrowerVo);
		params.put("vasVipList", vasVipList);
		if (bidDetail.getRecommendState() == InvestConstants.BID_DETAIL_RECOMMEND_YES) {
			RecommendEarnBuild recommend = new RecommendEarnBuild();
			recommend.setSystemTypeEnums(SystemTypeEnums.HKJF);
			recommend.setVasRuleTypeEnum(VasRuleTypeEnum.RECOMMEND);
			recommend.setRecommendRecordList(bidInvests);
			params.put("recommend", recommend);
		}
		if (bidDetail.getGivingPointState() == InvestConstants.BID_DETAIL_GIVE_POINT_YES) {
			InvestPointDTO investPointDTO = new InvestPointDTO();
			investPointDTO.setBidInfo(bidInfo);
			investPointDTO.setBidInvests(bidInvests);
			params.put("investPointDTO", investPointDTO);
		}
		List<Integer> bidInvestIds = new ArrayList<Integer>();
		List<Integer> regUserIds = new ArrayList<Integer>();
		bidInvests.forEach(bidInvest -> {
			bidInvestIds.add(bidInvest.getId());
			regUserIds.add(bidInvest.getRegUserId());
		});
		params.put("bidInvestIds", bidInvestIds);
		params.put("regUserIds", regUserIds);
		return params;
	}

	/**
	 * @Description : 放款--修改标的相关信息
	 * @Method_Name : updateBidDetailForMakeLoan
	 * @param bidDetail
	 * @param bidInfo
	 * @return : void
	 * @Creation Date : 2017年10月25日 下午2:16:46
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private void updateBidDetailForMakeLoan(BidInfoDetail bidDetail, BidInfo bidInfo) {
		BidInfoDetail updateDetail = new BidInfoDetail();
		updateDetail.setId(bidDetail.getId());
		updateDetail.setBiddInfoId(bidInfo.getId());
		if (bidDetail.getMatchType() == InvestConstants.BID_MATCH_TYPE_YES) {
			// 查询匹配记录
			List<BidMatch> matchList = bidMatchService.findBidMatchListByCommonBidId(bidInfo.getId());
			Date nextMatchDate = BidMatchUtil.getNextMatchDate(matchList);
			updateDetail.setNextMatchDate(nextMatchDate);
		} else {
			updateDetail.setNextMatchDate(new Date());
		}
		BaseUtil.getTccProxyBean(BidInfoService.class, getClass(), "makeloans").updateBidInfoForMakeLoan(updateDetail);
	}

	@Override
	public ResponseEntity<?> initRepayPlans(int bidInfoId) {
		try {
			logger.info("initRepayPlans, 生成还款计划&回款计划, bidInfoId: {}", bidInfoId);
			BidInfo bidInfo = bidInfoService.findBidInfoById(bidInfoId);
			List<BidInvest> bidInvests = bidInvestService.findBidInvestListByBidId(bidInfoId);
			if (bidInfo != null && CommonUtils.isNotEmpty(bidInvests)) {
				return initRepayPlan(bidInfo, bidInvests);
			}
			return new ResponseEntity<>(ERROR, "标的或投资记录不存在");
		} catch (Exception e) {
			logger.error("initRepayPlans, 生成还款计划&回款计划, bidInfoId: {}, 异常信息:\n", bidInfoId,e);
			throw new GeneralException("生成还款计划和回款计划失败");
		}
	}

	/**
	 * @Description : 放款生成成长值
	 * @Method_Name : makeLoanForVas
	 * @param bidInvests
	 * @param bidInfo
	 * @param borrowerVo
	 *            借款人信息
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午3:01:12
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private List<VasVipGrowRecordMqVO> makeLoanForVas(List<BidInvest> bidInvests, BidInfo bidInfo, UserVO borrowerVo)
			throws Exception {
		// 发放成长值奖励集合
		List<VasVipGrowRecordMqVO> vasVipList = new ArrayList<VasVipGrowRecordMqVO>();
		// 投资天数，为推荐奖励使用
		int investDays = CalcInterestUtil.calInvestDays(bidInfo.getTermUnit(), bidInfo.getTermValue());
		// 获取所有投资人的一级推荐好友列表
		Set<Integer> regUserIds = bidInvests.stream().map(BidInvest::getRegUserId).collect(Collectors.toSet());
		//投资人对应其一级好友数据
		Map<Integer, RegUserFriends> dataMap = new HashMap<>();
		if (regUserIds.size() > 0){
			dataMap = regUserFriendsService.findRecommendFriendsByRegUserIds(regUserIds);
		}
		for (BidInvest bidInvest : bidInvests) {
			// 生成成长值奖励
			VasVipGrowRecordMqVO vasVo = new VasVipGrowRecordMqVO();
			vasVo.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_INVEST);
			vasVo.setUserId(bidInvest.getRegUserId());
			vasVo.setInvestMoney(bidInvest.getInvestAmount().doubleValue());
			vasVo.setInvestDay(investDays);
			vasVipList.add(vasVo);
			//生成邀请好友投资成长值奖励
			if (dataMap.containsKey(bidInvest.getRegUserId())){
				VasVipGrowRecordMqVO recordMqVO = new VasVipGrowRecordMqVO();
				recordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_INVITE_USER_INVEST);
				recordMqVO.setUserId(dataMap.get(bidInvest.getRegUserId()).getRecommendId());
				recordMqVO.setInvestMoney(bidInvest.getInvestAmount().doubleValue());
				recordMqVO.setInvestDay(investDays);
				vasVipList.add(recordMqVO);
			}
		}
		//获取投资
		return vasVipList;
	}

	/**
	 * @Description : 放款处理流水&账户----普通标（区别于体验金）
	 * @Method_Name : makeLoanCommBid
	 * @param bidInvests
	 * @param bidInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月30日 下午5:12:33
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	private ResponseEntity<?> makeLoanCommBid(List<BidInvest> bidInvests, BidInfo bidInfo) {
		logger.info("makeLoanCommBid, 普通标的放款, bidInvests.size: {}, bidInfo: {}", bidInvests.size(), bidInfo.toString());
		// 定义需要更新的流水和账户集合
		List<FinFundtransfer> thawFundtransfers = new ArrayList<FinFundtransfer>();
		// step1：生成放款流水
		Integer borrowerId = bidInfo.getBorrowerId();
		FinTradeFlow thawTradeFlow = FinTFUtil.initFinTradeFlow(borrowerId, String.valueOf(bidInfo.getId()),
				bidInfo.getTotalAmount(), TradeTransferConstants.TRADE_TYPE_MAKELOAN, PlatformSourceEnums.PC);
		String flowId = thawTradeFlow.getFlowId();
		// 查询冻结流水，用于校验用户资金冻结是否正确
		ResponseEntity<?> tradeResult = getAccountAndFreeTradeFlow(bidInvests);
		if (BaseUtil.error(tradeResult)) {
			return tradeResult;
		}
		Map<String, Object> tradeFlowsMap = (Map<String, Object>) tradeResult.getParams().get("freezeTradeFlows");
		Map<Integer, Object> accountMap = (Map<Integer, Object>) tradeResult.getParams().get("accounts");
		// step2 : 循环投资记录，处理投资人、借款人流水&投资人账户更新
		for (BidInvest bidInvest : bidInvests) {
			Integer investUserId = bidInvest.getRegUserId();
			ResponseEntity<String> validateResult = validateTradeForMakeLoan(
					(FinTradeFlow) tradeFlowsMap.get(String.valueOf(bidInvest.getId())), bidInvest);
			if (BaseUtil.error(validateResult)) {
				return validateResult;
			}
			// step2-2：投资账户校验
			FinAccount account = (FinAccount) accountMap.get(investUserId);
			if (CompareUtil.gt(bidInvest.getInvestAmount(), account.getNowMoney())
					|| CompareUtil.gt(bidInvest.getInvestAmount(), account.getFreezeMoney())) {
				logger.info("makeLoanCommBid, 普通标的放款, bidInfo: {},存在用户冻结金额不足, 用户{}", bidInfo.getId(),
						account.getRegUserId());
				return new ResponseEntity<String>(Constants.ERROR, "存在用户冻结金额不足");
			}
			// step2-3：放款资金划转--投资人(放款付款)
			FinFundtransfer finFundtransferOut = FinTFUtil.initFinFundtransfer(flowId, investUserId, borrowerId,
					bidInvest.getInvestAmount(), TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.FROZEN));
			thawFundtransfers.add(finFundtransferOut);
			// step2-4：放款资金划转--借款人（放款收款）
			FinFundtransfer finFundtransferIn = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(), borrowerId,
					investUserId, bidInvest.getInvestAmount(), TradeTransferConstants.TRANSFER_SUB_CODE_INCOME);
			thawFundtransfers.add(finFundtransferIn);

			// 判断是否存在投资红包
//			if (CommonUtils.gtZero(bidInvest.getCouponIdK())) {
//				// 查询投资红包的值
//				VasCouponDetail vasDetail = vasCouponDetailService.findVasCouponDetailById(bidInvest.getCouponIdK());
//				if (vasDetail != null) {
//					// 投资人--收入投资红包
//					FinFundtransfer finFundtransferCouponKIn = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(),
//							investUserId, PLATFORM_ACCOUNT_ID, vasDetail.getWorth(),
//							TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
//									FundtransferSmallTypeStateEnum.INVEST_RED_PACKETS));
//					FinFundtransfer finFundtransferCouponKOut = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(),
//							PLATFORM_ACCOUNT_ID, investUserId, vasDetail.getWorth(),
//							TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
//									FundtransferSmallTypeStateEnum.INVEST_RED_PACKETS));
//					thawFundtransfers.add(finFundtransferCouponKIn);
//					thawFundtransfers.add(finFundtransferCouponKOut);
//				}
//			}
		}
		// step3：预留利息冻结
		BigDecimal reservelInterest = new BigDecimal(0);
		BidInfoDetail bidDetail = bidInfoDetailService.findBidInfoDetailByBidId(bidInfo.getId());
		if (bidDetail.getReserveInterest() == InvestConstants.BID_DETAIL_RESERVE_INTEREST_YES) {
			reservelInterest = CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(),
					bidInfo.getTotalAmount(), bidInfo.getInterestRate());
			// 预留利息冻结-资金划转
			FinFundtransfer fundtransferReservel = FinTFUtil.initFinFundtransfer(flowId, borrowerId, null,
					reservelInterest, TRANSFER_SUB_CODE_FREEZE);
			thawFundtransfers.add(fundtransferReservel);
		}
		logger.info("makeLoanCommBid, 普通标的放款, bidInfo: {},放款操作冻结预留利息完成, 预留利息: {}", bidInfo.getId(), reservelInterest);
		// step4：处理手续费
		BigDecimal commissionMoney = new BigDecimal(0);
		if (CompareUtil.gtZero(bidInfo.getCommissionRate())) {
			commissionMoney = bidInfo.getTotalAmount().multiply(bidInfo.getCommissionRate()).divide(new BigDecimal(100),
					2);
			logger.info("makeLoanCommBid, 普通标的放款, bidInfo: {},放款操作收取手续费, 手续费率: {}, 手续费: {}", bidInfo.getId(),
					bidInfo.getCommissionRate(), commissionMoney);
			// 手续费流水 --平台账户入账
			FinFundtransfer fundtransferCommissionIn = FinTFUtil.initFinFundtransfer(flowId, PLATFORM_ACCOUNT_ID,
					borrowerId, commissionMoney, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.CHARGE));
			thawFundtransfers.add(fundtransferCommissionIn);
			// 手续费流水 --借款人出账
			FinFundtransfer fundtransferCommissionOut = FinTFUtil.initFinFundtransfer(flowId, borrowerId,
					PLATFORM_ACCOUNT_ID, commissionMoney, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.CHARGE));
			thawFundtransfers.add(fundtransferCommissionOut);
		}
		// step5：如果存在本金接收人，处理本金接收人转账
		if (CommonUtils.gtZero(bidInfo.getReceiptUserId())) {
			BigDecimal transMoney = bidInfo.getTotalAmount().subtract(commissionMoney).subtract(reservelInterest);
			logger.info("makeLoanCommBid, 普通标的放款, bidInfo: {},放款操作本金接收人本金转账, 转账本金: {}", bidInfo.getId(), transMoney);
			FinFundtransfer fundtransferCipalOut = FinTFUtil.initFinFundtransfer(flowId, borrowerId,
					bidInfo.getReceiptUserId(), transMoney, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.CAPITAL));
			FinFundtransfer fundtransferCipalIn = FinTFUtil.initFinFundtransfer(flowId, bidInfo.getReceiptUserId(),
					borrowerId, transMoney, TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.CAPITAL));
			thawFundtransfers.add(fundtransferCipalOut);
			thawFundtransfers.add(fundtransferCipalIn);
		}
		// step6：执行数据更新
		return BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(), "makeLoanCommBid")
				.updateAccountInsertTradeAndTransfer(thawTradeFlow, thawFundtransfers);
	}

	/**
	 * 
	 * @Description : 根据投资记录获取投资用户列表和其冻结流水列表--用于放款校验
	 * @Method_Name : validateTradeForMakeLoan
	 * @return
	 * @return : ResponseEntity<String>
	 * @Creation Date : 2017年10月27日 上午9:00:55
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private ResponseEntity<?> getAccountAndFreeTradeFlow(List<BidInvest> bidInvests) {
		List<Integer> bidInvestIds = new ArrayList<Integer>();
		List<Integer> regUserIds = new ArrayList<Integer>();
		bidInvests.forEach(bidInvest -> {
			bidInvestIds.add(bidInvest.getId());
			regUserIds.add(bidInvest.getRegUserId());
		});
		List<FinTradeFlow> freezeTradeFlows = finTradeFlowService.findFreezeTradeFlowByPflowIds(bidInvestIds);
		if (CommonUtils.isEmpty(freezeTradeFlows)) {
			return new ResponseEntity<String>(Constants.ERROR, "此标的投资用户不存在冻结流水");
		}
		Map<String, Object> tradeFlowsMap = new HashMap<String, Object>();
		Map<Integer, Object> accountMap = new HashMap<Integer, Object>();
		freezeTradeFlows.forEach(freeFlow -> {
			tradeFlowsMap.put(freeFlow.getPflowId(), freeFlow);
		});
		List<FinAccount> accounts = finAccountService.findFinAccountByRegUserIds(regUserIds);
		accounts.forEach(account -> {
			accountMap.put(account.getRegUserId(), account);
		});
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("freezeTradeFlows", tradeFlowsMap);
		params.put("accounts", accountMap);
		return new ResponseEntity<>(Constants.SUCCESS, "查询冻结流水&账户成功", params);
	}

	/**
	 * 
	 * @Description : 校验--放款冻结流水和金额
	 * @Method_Name : validateTradeForMakeLoan
	 * @param bidInvest
	 * @return
	 * @return : ResponseEntity<String>
	 * @Creation Date : 2017年10月27日 上午9:00:55
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private ResponseEntity<String> validateTradeForMakeLoan(FinTradeFlow freezeTradeFlow, BidInvest bidInvest) {

		if (freezeTradeFlow == null) {
			return new ResponseEntity<String>(Constants.ERROR, "此标的存在投资用户没有冻结记录,投资id: " + bidInvest.getId());
		}
		if (!CompareUtil.eq(freezeTradeFlow.getTransMoney(), bidInvest.getInvestAmount())) {
			return new ResponseEntity<String>(Constants.ERROR, "此标的存在投资用户冻结金额不正确, 投资id: " + bidInvest.getId());
		}
		return new ResponseEntity<String>(Constants.SUCCESS, "校验通过");
	}

	/**
	 * @Description : 放款处理流水&账户----体验金标的
	 * @Method_Name : makeLoanSimBid
	 * @param bidInvests
	 * @param bidInfo
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月30日 下午5:35:06
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	private ResponseEntity<?> makeLoanSimBid(List<BidInvest> bidInvests, BidInfo bidInfo) {
		logger.info("makeLoanSimBid, 体验金放款处理账户和流水, bidInvests.size: {}, bidInfo: {}", bidInvests.size(),
				bidInfo.toString());
		List<FinFundtransfer> thawFundtransfers = new ArrayList<FinFundtransfer>();
		// 需要更新的账户集合
		Integer borrowerId = bidInfo.getBorrowerId();
		// step1：生成放款流水
		FinTradeFlow thawTradeFlow = FinTFUtil.initFinTradeFlow(borrowerId, bidInfo.getId(), bidInfo.getTotalAmount(),
				TradeTransferConstants.TRADE_TYPE_MAKELOAN_SIM, PlatformSourceEnums.PC);
		// step2: 放款资金划转--平台 放款付款
		FinFundtransfer finFundtransferOut = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(),
				UserConstants.PLATFORM_ACCOUNT_ID, borrowerId, bidInfo.getTotalAmount(),
				TradeTransferConstants.TRANSFER_SUB_CODE_PAY);
		// step3: 放款资金划转--借款人 放款收款
		FinFundtransfer finFundtransferIn = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(), borrowerId,
				UserConstants.PLATFORM_ACCOUNT_ID, bidInfo.getTotalAmount(),
				TradeTransferConstants.TRANSFER_SUB_CODE_INCOME);
		thawFundtransfers.add(finFundtransferOut);
		thawFundtransfers.add(finFundtransferIn);
		// 平台账户更新
		FinAccount platformAccount = new FinAccount();
		platformAccount.setId(UserConstants.PLATFORM_ACCOUNT_ID);
		platformAccount.setNowMoney(bidInfo.getTotalAmount().multiply(new BigDecimal(-1)));
		platformAccount.setUseableMoney(bidInfo.getTotalAmount().multiply(new BigDecimal(-1)));

		BidInfoDetail bidDetail = bidInfoDetailService.findBidInfoDetailByBidId(bidInfo.getId());
		BigDecimal reservelInterest = new BigDecimal(0);
		// step5 :借款人预留利息冻结
		if (bidDetail.getReserveInterest() == InvestConstants.BID_DETAIL_RESERVE_INTEREST_YES) {
			reservelInterest = CalcInterestUtil.calcInterest(bidInfo.getTermUnit(), bidInfo.getTermValue(),
					bidInfo.getTotalAmount(), bidInfo.getInterestRate());
			// 生成冻结流水
			FinFundtransfer fundtransferReservel = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(), borrowerId,
					null, reservelInterest, TradeTransferConstants.TRANSFER_SUB_CODE_FREEZE);
			thawFundtransfers.add(fundtransferReservel);
		}
		// step6：借款人手续费处理
		if (CompareUtil.gtZero(bidInfo.getCommissionRate())) {
			BigDecimal commissionMoney = bidInfo.getTotalAmount().multiply(bidInfo.getCommissionRate())
					.divide(new BigDecimal(100), 2);
			// 手续费流水 --平台账户入账
			FinFundtransfer fundtransferCommissionIn = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(),
					UserConstants.PLATFORM_ACCOUNT_ID, borrowerId, commissionMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.CHARGE));
			// 手续费流水 --借款人出账
			FinFundtransfer fundtransferCommissionOut = FinTFUtil.initFinFundtransfer(thawTradeFlow.getFlowId(),
					borrowerId, UserConstants.PLATFORM_ACCOUNT_ID, commissionMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.CHARGE));
			thawFundtransfers.add(fundtransferCommissionIn);
			thawFundtransfers.add(fundtransferCommissionOut);
		}
		// step7：执行数据更新
		return BaseUtil.getTccProxyBean(FinConsumptionService.class, getClass(), "makeLoanSimBid")
				.updateAccountInsertTradeAndTransfer(thawTradeFlow, thawFundtransfers);
	}

	/**
	 * @Description : 封装合同
	 * @Method_Name : initConInfoList
	 * @param bidInvest
	 * @param bidInfo
	 * @param borrowerVo
	 *            借款人信息
	 * @param receiveUserVo
	 *            本金接收人信息
	 * @param contranctTypes
	 * @return
	 * @return : List<ConInfo>
	 * @Creation Date : 2017年7月4日 下午3:06:15
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private List<ConInfo> initConInfoList(BidInvest bidInvest, BidInfo bidInfo, UserVO borrowerVo, UserVO receiveUserVo,
			String contranctTypes) {
		List<ConInfo> resultList = new ArrayList<ConInfo>();
		String[] contractType = contranctTypes.split(",");
		for (String contract : contractType) {
			RegUserDetail investUserDetail = regUserDetailService
					.findRegUserDetailByRegUserId(bidInvest.getRegUserId());
			RegUser investUser = regUserService.findRegUserById(bidInvest.getRegUserId());
			RegUserInfo investUserInfo = regUserInfoService.findRegUserInfoByRegUserId(bidInvest.getRegUserId());
			ConInfo ci = new ConInfo();
			ci.setBidId(bidInvest.getBidInfoId());
			ci.setBidInvestId(bidInvest.getId());
			ci.setContractType(Integer.parseInt(contract));
			ci.setEffectiveDate(new Date());
			if (bidInfo.getTermUnit() == InvestConstants.BID_TERM_UNIT_DAY) {
				ci.setExpireDate(DateUtils.addDays(new Date(), bidInfo.getTermValue()));
			}
			if (bidInfo.getTermUnit() == InvestConstants.BID_TERM_UNIT_MONTH) {
				ci.setExpireDate(DateUtils.addMonth(new Date(), bidInfo.getTermValue()));
			}
			ci.setInvestUserId(bidInvest.getRegUserId());
			ci.setInvestName(bidInvest.getRealName());
			ci.setInvestCardId(investUserDetail.getIdCard());
			ci.setInvestAmount(bidInvest.getInvestAmount());
			ci.setInvestEmail(investUserInfo.getEmail());
			ci.setInvestTel(investUser.getLogin());

			ci.setBidName(bidInfo.getName());
			ci.setBidRate(bidInfo.getInterestRate());
			ci.setBidTerm(bidInfo.getTermUnit());
			ci.setBidTermValue(bidInfo.getTermValue());
			ci.setBidLoanUse(bidInfo.getLoanUse());
			ci.setBiddRepaymentWay(bidInfo.getBiddRepaymentWay());
			ci.setBidProjectNo(bidInfo.getBidCode());

			ci.setBorrowName(borrowerVo.getRealName());
			ci.setBorrowCardId(borrowerVo.getIdCard());
			ci.setBorrowAddress(borrowerVo.getContactAddress());

			ci.setPayeeName(receiveUserVo.getRealName());
			if (StringUtils.isNotBlank(receiveUserVo.getBankName())) {
				ci.setPayeeBankName(receiveUserVo.getBankName());
			}
			if (StringUtils.isNotBlank(receiveUserVo.getBankCard())) {
				ci.setPayeeBankCard(receiveUserVo.getBankCard());
			}
			resultList.add(ci);
		}
		return resultList;
	}

	/**
	 * @Description : 根据不同的还款方式生成对应的还款/回款计划
	 * @Method_Name : initRepayPlan
	 * @param bidInfo
	 * @param invests
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年6月27日 上午9:29:08
	 * @Author : xuhuiliu@hongkun.com.cn 刘旭辉
	 */
	private ResponseEntity<?> initRepayPlan(BidInfo bidInfo, List<BidInvest> invests) {
		logger.info("initRepayPlan, 放款生成还款&回款计划, 标的信息：{}, 投资记录: {}", bidInfo.toString(), JSON.toJSON(invests));
		invests.forEach(bidInvest -> {
			if (CommonUtils.gtZero(bidInvest.getCouponIdJ())) {
				// 查询加息券的值
				VasCouponDetail vasDetail = vasCouponDetailService.findVasCouponDetailById(bidInvest.getCouponIdJ());
				bidInvest.setCouponWorthJ(vasDetail.getWorth());
			}
		});
		ResponseEntity<?> planVos = RepayPlanUtils.initPlanVo(bidInfo, invests);
		if (BaseUtil.error(planVos)) {
			return planVos;
		}
		BidCommonPlanVo planVo = (BidCommonPlanVo) planVos.getParams().get(RepayConstants.REPAY_BIDCOMMONPLAN_VO);
		List<BidInvestVo> vos = (List<BidInvestVo>) planVos.getParams().get(RepayConstants.REPAY_BIDINVEST_VO);
		ResponseEntity<?> result = RepayAndReceiptUtils.initRepayPlan(planVo, vos,RepayConstants.INIT_PLAN_REPAY_AND_RECEIPT);
		if (!BaseUtil.error(result)) {
			List<BidRepayPlan> repayPlans = (List<BidRepayPlan>) result.getParams().get("repayPlanList");
			List<BidReceiptPlan> receiptPlans = (List<BidReceiptPlan>) result.getParams().get("recieptPlanList");
			if (CommonUtils.isEmpty(repayPlans)) {
				return new ResponseEntity<>(Constants.ERROR, "未生成还款计划");
			}
			if (CommonUtils.isEmpty(receiptPlans)) {
				return new ResponseEntity<>(Constants.ERROR, "未生成回款计划");
			}
			bidRepayPlanService.insertBidRepayPlanBatch(repayPlans, receiptPlans);
		}
		return result;
	}
}
