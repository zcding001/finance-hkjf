package com.hongkun.finance.qdz.facade.impl;

import static com.hongkun.finance.invest.constants.InvestConstants.BID_PRODUCT_CURRENT;
import static com.hongkun.finance.invest.constants.InvestConstants.BID_STATE_WAIT_REPAY;
import static com.hongkun.finance.invest.constants.InvestConstants.INVEST_STATE_AUTO;
import static com.hongkun.finance.invest.constants.InvestConstants.INVEST_STATE_SUCCESS;
import static com.hongkun.finance.loan.constants.RepayConstants.REPAY_STATE_NONE;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_CREDITOR_TRANSFER_QDZ;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_QDZ_DAY_INTEREST;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_CREDITOR_TRANSFER_INCOME;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_INCOME;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_PAY;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRANSFER_SUB_CODE_TURNS_OUT_QDZ_CREDITOR;
import static com.hongkun.finance.qdz.constant.QdzConstants.CREDITOR_FLAG_BUY;
import static com.hongkun.finance.qdz.constant.QdzConstants.CREDITOR_FLAG_SELL;
import static com.hongkun.finance.qdz.constant.QdzConstants.QDZ_INOUT_OFF;
import static com.hongkun.finance.qdz.constant.QdzConstants.QDZ_INOUT_ON;
import static com.hongkun.finance.qdz.constant.QdzConstants.QDZ_INTEREST_DAY_FAIL;
import static com.hongkun.finance.qdz.constant.QdzConstants.QDZ_INTEREST_DAY_SUCCSS;
import static com.hongkun.finance.qdz.constant.QdzConstants.TRANS_RECORD_SUCCSS;
import static com.hongkun.finance.user.constants.UserConstants.FRIEND_LEVEL_ONE;
import static com.hongkun.finance.user.constants.UserConstants.PLATFORM_ACCOUNT_ID;
import static com.hongkun.finance.vas.constants.VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT;
import static com.hongkun.finance.vas.constants.VasConstants.VAS_RULE_STATE_START;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferAuto;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.service.BidTransferManualService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.BidCommonPlanVo;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.util.RepayAndReceiptUtils;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.payment.service.FinTradeFlowService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzInterestDayDetailService;
import com.hongkun.finance.qdz.service.QdzInterestDayService;
import com.hongkun.finance.qdz.service.QdzRateRecordService;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.qdz.utils.QdzDateUtils;
import com.hongkun.finance.qdz.vo.QdzAutoCreditorVo;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.QdzRaiseInterestVo;
import com.hongkun.finance.vas.model.QdzVasRuleItem;
import com.hongkun.finance.vas.model.RcommendEarnInfo;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.redis.JedisClusterUtils;

/**
 * 
 * @Description : 钱袋子跑批实现类
 * @Project : finance-qdz-service
 * @Program Name : com.hongkun.finance.qdz.facade.impl.QdzTaskJobFacadeImpl.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */
@Service
public class QdzTaskJobFacadeImpl implements QdzTaskJobFacade {
	private static final Logger logger = LoggerFactory.getLogger(QdzTaskJobFacadeImpl.class);
	@Autowired
	private QdzTransRecordService qdzTransRecordService;
	@Autowired
	private QdzAccountService qdzAccountService;
	@Autowired
	private QdzRateRecordService qdzRateRecordService;
	@Autowired
	private QdzInterestDayService qdzInterestDayService;
	@Autowired
	private QdzInterestDayDetailService qdzInterestDayDetailService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private RegUserService regUserService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidTransferManualService bidTransferManualService;
	@Reference
	private FinTradeFlowService finTradeFlowService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Override
	public ResponseEntity<?> creditorMatch(Date date) {
		String currentJobDate = DateUtils.format(date, DateUtils.DATE_HH_MM_SS);
		logger.info("creditorMatch钱袋子债券跑批,当前跑批时间:{}", currentJobDate);
		try {
			if (StringUtilsExtend.isEmpty(currentJobDate)) {
				return new ResponseEntity<>(ERROR, "当前跑批时间不能为空！");
			}
			// 在跑债权转让的时候，是不允许转入，转出的，所以设置转入转出标识为0：不允许转入转出操作 1：允许转入转出
			VasRebatesRule qdzInOutKey = vasRebatesRuleService
					.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZINOUTONOFF.getValue(), VAS_RULE_STATE_START);
			// 刷新缓存,设置不允许转入转出
			if (qdzInOutKey != null && QDZ_INOUT_ON.equals(qdzInOutKey.getContent())) {
				JedisClusterUtils.setAsJson(VasRebatesRule.class.getSimpleName() + "_VAS_"
						+ VasRuleTypeEnum.QDZINOUTONOFF.getValue() + "_" + VAS_RULE_STATE_START, QDZ_INOUT_OFF);
			}
			// 查询当天跑批利率记录
			QdzRateRecord rateRecord = qdzRateRecordService.getQdzRateRecord(date);
			if(rateRecord==null) {
				logger.info("creditorMatch钱袋子债券跑批,当前跑批时间:{}, 当前跑批利率记录不存在:{}", currentJobDate,rateRecord);
				return new ResponseEntity<>(ERROR, "当天跑批利率记录不存在！");	
			}
			// 开始债券匹配
			long starMatchTime = System.currentTimeMillis();
			List<QdzAccount> accounts = qdzAccountService.findCreditorMatchQdzAccounts();
			for (QdzAccount qdzAccount : accounts) {
				if (CompareUtil.lteZero(qdzAccount.getCreditorMoney())) {
					logger.info("buyCreditorMatchByRecord当前钱袋子用户购买债券数据异常:钱袋子账户标识:{}",qdzAccount.getRegUserId());
					continue;
				}
				try {  
					//购买债券
					buyCreditorMatchByRecord(qdzAccount, rateRecord, CREDITOR_FLAG_BUY, date);
					} catch (Exception e) {
						logger.error("buyCreditorMatchByRecord钱袋子购买债券:钱袋子用户:{} ,当前利率:{}, 处理日期:{}",
								qdzAccount.toString(),rateRecord.getRate(),currentJobDate,e);
					}
				}
			
			long endMatchTime = System.currentTimeMillis();
			logger.info("creditorMatch钱袋子跑批债券结束,共用了:{}" + DateUtils.formatTime((endMatchTime - starMatchTime)));
			// 更新钱袋子数据字典开关
			JedisClusterUtils.setAsJson(VasRebatesRule.class.getSimpleName() + "_VAS_"
					+ VasRuleTypeEnum.QDZINOUTONOFF.getValue() + "_" + VAS_RULE_STATE_START, QDZ_INOUT_ON);
			resetQdzCreditorPool();
		} catch (Exception e) {
			logger.error("creditorMatch钱袋子跑批债券跑批失败:当前跑批时间：{}", currentJobDate, e);
			throw new BusinessException("creditorMatch钱袋子跑批债券跑批失败!");
		}
		return new ResponseEntity<>(SUCCESS, "钱袋子跑批债券成功！");
	}

	/**
	 * 
	 * @Description : 重置钱袋子债券池
	 * @Method_Name : resetCreditorPool
	 * @return : void
	 * @Creation Date : 2018年4月3日 下午7:22:13
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void resetQdzCreditorPool() {
		// 如果当前债池规则key存在，则删除，重置债权池金额
		String creditorRuleKey = VasConstants.VAR_RULE_KEY + String.valueOf(VasRuleTypeEnum.CREDITORMONEY.getValue())
		+ VasConstants.VAS_RULE_STATE_START;
		if (JedisClusterUtils.exists(creditorRuleKey)) {
			JedisClusterUtils.delete(creditorRuleKey);
		}
	}

	/**
	 * 
	 * @Description : 根据记录购买债券
	 * @Method_Name : creditorMatchByRecord
	 * @param d：记录
	 * @param creditorFlag：购买债券标识
	 * @param repairTime
	 * @return : void
	 * @Creation Date : 2017年7月17日 上午11:56:43
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void buyCreditorMatchByRecord(QdzAccount qdzAccount, QdzRateRecord rateRecord, int creditorFlag,
			Date repairTime) {
		BigDecimal userTransMoney = qdzAccount.getCreditorMoney();// 用户待匹配总债券
		BigDecimal qdzRate = rateRecord.getRate();
		BidInvest thirdBiddInvest = null;// 第三方投资记录
		RegUser thirdRegUser = null;// 第三方投资人用户信息
		logger.info("buyCreditorMatchByRecord开始购买债券：用户标识{}, 用户待匹配总债券{}, 当前利率{}", qdzAccount.getRegUserId(),
				userTransMoney, qdzRate);
		try {
			// 查询还款中的活期标
			List<BidInfo> bidInfoLists = bidInfoService.findBidInfoList(BID_PRODUCT_CURRENT, BID_STATE_WAIT_REPAY);
			// 如果没有查询到还款的中的活期标，则认为第三方没有债权，不做任何处理，否则进行正常的债权转让逻辑
			if (bidInfoLists == null || bidInfoLists.isEmpty()) {
				return;
			}
			// 第三方投资金额
			BigDecimal thirdInvestAmount = BigDecimal.ZERO;
			// 查询投资记录信息条件VO
			List<Integer> investState = new ArrayList<Integer>();
			// 查询当前投资用户信息
			RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(qdzAccount.getRegUserId());
			// 遍历循环活期标，进行正常的债权转让
			Map<String,Object> creditorInfoMap = new HashMap<String,Object>();
			for (BidInfo bidInfo : bidInfoLists) {
				if(CompareUtil.lteZero(userTransMoney)) {
					break;
				}
				investState.add(INVEST_STATE_SUCCESS);// 投资成功
				investState.add(INVEST_STATE_AUTO);
				// 查询对应活期标的投资记录， 获取第三方投资记录
				thirdBiddInvest = bidInvestService.findInvestRecord(bidInfo.getId(), investState);
				// 如果没有查询到第三方投资记录，则循环下一条记录 ||获取第三方投资人的投资金额,如果为0，则认为此活期标目前已经转让完毕，不可以进行转让
				if (thirdBiddInvest == null) {
					continue;
				}
				//第三方剩余债券金额
				thirdInvestAmount = thirdBiddInvest.getInvestAmount().subtract(thirdBiddInvest.getTransAmount());
				if(CompareUtil.eq(thirdInvestAmount, BigDecimal.ZERO)) {
					continue;
				}
				// 获取第三方投资人的用户信息
				thirdRegUser = regUserService.findRegUserById(thirdBiddInvest.getRegUserId());
				// 用于判断是不是第三方投资人,如果不是则退出当前循环，进行下个活期标的第三方用户的判断
				if (thirdRegUser == null || thirdRegUser.getType() == null
						|| thirdRegUser.getType().intValue() != UserConstants.USER_TYPE_ESCROW_ACCOUNT) {
				logger.info("buyCreditorMatchByRecord查询第三方账户信息, 用户标识{}, 标的Id:{}, 查询活期标的用户类型,该用户不是第三方用户不允许债权转让",
							thirdBiddInvest.getRegUserId(), bidInfo.getId());
					continue;
				}
				logger.info("buyCreditorMatchByRecord债券转让：用户标识{},用户待匹配金额：{},第三方持有标的Id{},第三方债券金额{}",
						qdzAccount.getRegUserId(), userTransMoney, bidInfo.getId(), thirdInvestAmount);
				// 查询第三方回款计划
				BidReceiptPlan bidReceiptPlanCdt = new BidReceiptPlan();
				bidReceiptPlanCdt.setState(REPAY_STATE_NONE);
				bidReceiptPlanCdt.setRegUserId(thirdBiddInvest.getRegUserId());
				bidReceiptPlanCdt.setBidId(bidInfo.getId());
				bidReceiptPlanCdt.setSortColumns("id asc");
				List<BidReceiptPlan> thirdOldReceiptPlans = bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlanCdt);
				if(thirdOldReceiptPlans.isEmpty()) {
					logger.error("buyCreditorMatchByRecord:回款计划异常：{}",thirdBiddInvest.getId());
					continue;
				}
				creditorInfoMap.put("bidInfo", bidInfo);
				creditorInfoMap.put("regUserDetail", regUserDetail);
				creditorInfoMap.put("thirdBiddInvest", thirdBiddInvest);
				creditorInfoMap.put("thirdReceiptPlans", thirdOldReceiptPlans);
				// 当前第三方债权足够转让
				if (CompareUtil.gte(thirdInvestAmount, userTransMoney)) {
					ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealBuyCreditorMatch(qdzAccount, userTransMoney, 
							thirdInvestAmount, qdzRate, repairTime, creditorInfoMap);
					userTransMoney = BigDecimal.ZERO;
					break;
				} else {// 此第三方债权只够一部分，全部转让此笔债权，继续循环
					ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealBuyCreditorMatch(qdzAccount, thirdInvestAmount, 
							thirdInvestAmount, qdzRate, repairTime, creditorInfoMap);
					userTransMoney = userTransMoney.subtract(thirdInvestAmount);
				}
			}
			logger.info("buyCreditorMatchByRecord债券后：用户标识{},转让后用户剩余待匹配债权金额{}", qdzAccount.getRegUserId(),
					userTransMoney);
		} catch (Exception e) {
			logger.error("buyCreditorMatchByRecord债券异常：用户标识{}, 自动债权转让异常: ", qdzAccount.getRegUserId(), e);
			throw new BusinessException("buyCreditorMatchByRecord钱袋子购买债券失败！");
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Compensable
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> dealBuyCreditorMatch(QdzAccount qdzAccount,BigDecimal creditorTransMoney,BigDecimal thirdInvestAmount,
			BigDecimal qdzRate,Date dealTime,Map creditorInfoMap) throws Exception {
		int step = 1;// 操作步骤
		BidInvest thirdBiddInvest = (BidInvest) creditorInfoMap.get("thirdBiddInvest");
		RegUserDetail regUserDetail = (RegUserDetail) creditorInfoMap.get("regUserDetail");
		BidInfo bidInfo = (BidInfo) creditorInfoMap.get("bidInfo");
		List<BidReceiptPlan> thirdOldReceiptPlans  = (List<BidReceiptPlan>) creditorInfoMap.get("thirdReceiptPlans");
		logger.info("tcc (qdzTaskJob#)dealBuyCreditorMatch entrence,reference qdz#updateCreditorMoney|bidTransferManualService#dealBuyCreditorMatchManualRecords|"
				+ "payment#dealCreditorMatchFlows,操作参数:钱袋子用户账户id:{}, 第三方投资记录id:{}, 钱袋子用户id:{}, "
				+ "标的id:{}, 钱袋子用户当前购买金额:{}, 第三方投资金额:{},  钱袋子利率:{}",qdzAccount.getId(),
				thirdBiddInvest.getId(),regUserDetail.getId(), bidInfo.getId(),creditorTransMoney,
				thirdInvestAmount, qdzRate);
		Integer regUserId = qdzAccount.getRegUserId();// 转入转出记录ID
		BidInvest qdzBidInvest = new BidInvest();// 钱袋子投资记录
		qdzBidInvest.setRegUserId(qdzAccount.getRegUserId());
		qdzBidInvest.setRealName(regUserDetail.getRealName());
		BidTransferAuto tranferAuto = new BidTransferAuto();// 债券关系
		tranferAuto.setBidInfoId(bidInfo.getId());
		logger.info("dealBuyCreditorMatch债券转让开始:转让记录标识{}, 用户标识{}, 转让金额{}, 第三方投资标识{}, 第三方投资金额{}, 钱袋子利率{}", regUserId,
				creditorTransMoney, thirdBiddInvest.getId(), thirdInvestAmount, qdzRate);
		try {
			// 1、更新钱袋子债券
			qdzAccountService.updateCreditorMoney(qdzAccount.getRegUserId(), creditorTransMoney, CREDITOR_FLAG_SELL);
			step++;
			// 2. 生成债券转让关系、处理投资记录
			ResponseEntity<?>  responseEntity = BaseUtil.getTccProxyBean(BidTransferManualService.class,getClass(),
					"dealBuyCreditorMatch").dealCreditorMatchRecords(qdzBidInvest, thirdBiddInvest, tranferAuto, 
							bidInfo, creditorTransMoney, qdzRate, dealTime,CREDITOR_FLAG_BUY);
			if(responseEntity.getResStatus()!=SUCCESS) {
				throw new BusinessException("dealBuyCreditorMatch钱袋子债券异常！");
			}
			Map<String, Object> transferManualRecord = responseEntity.getParams();
			qdzBidInvest.setId((Integer) transferManualRecord.get("bidInvestId"));
			qdzBidInvest.setRegUserId(regUserDetail.getRegUserId());
			qdzBidInvest.setInvestAmount(creditorTransMoney);
			step++;
			// 3、生成流水、资金划转、更新第三方账户金额
			this.dealCreditorMatchFlows(qdzBidInvest.getId(),
					regUserId, thirdBiddInvest.getRegUserId(), creditorTransMoney,dealTime, CREDITOR_FLAG_BUY);
			step++;
			// 4、生产、更新回款计划
			dealBuyCreditorMatchReceiptPlans(qdzBidInvest, thirdBiddInvest, bidInfo, qdzRate, thirdInvestAmount,thirdOldReceiptPlans);
			step++;
			// 5、生产合同
			jmsService.sendMsg(ContractConstants.MQ_QUEUE_CONINFO, DestinationType.QUEUE, qdzBidInvest.getId(),
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("tcc error (qdzTaskJob#)dealBuyCreditorMatch entrence,reference qdz#updateCreditorMoney|bidTransferManualService#dealBuyCreditorMatchManualRecords|"
					+ "payment#dealCreditorMatchFlows,操作参数:钱袋子用户账户id:{}, 第三方投资记录id:{}, 钱袋子用户id:{}, "
					+ "标的id:{}, 钱袋子用户当前购买金额:{}, 第三方投资金额:{},  钱袋子利率:{}, 完成步骤:{}",qdzAccount.getId(),
					thirdBiddInvest.getId(),regUserDetail.getId(), bidInfo.getId(),creditorTransMoney,
					thirdInvestAmount, qdzRate,step,e);
			throw new BusinessException("dealBuyCreditorMatch钱袋子债券异常！");
		}
		return new ResponseEntity<>(SUCCESS,"钱袋子债转跑批成功!");
	}
	/**
	 * 
	 *  @Description    : 处理匹配债转流水
	 *  @Method_Name    : dealCreditorMatchFlows;
	 *  @param qdzRegUserBiddInvestId
	 *  @param qdzRegUserId
	 *  @param thirdRegUserId
	 *  @param userTransMoney
	 *  @param repairTime
	 *  @param creditorFlag
	 *  @throws Exception
	 *  @return         : void;
	 *  @Creation Date  : 2018年5月16日 上午11:29:47;
	 *  @Author         : xinbangcao@hongkun.com.cn 曹新帮;
	 */
	public void dealCreditorMatchFlows(int qdzRegUserBiddInvestId,int qdzRegUserId,
			int thirdRegUserId, BigDecimal userTransMoney, Date repairTime,
			int creditorFlag) throws Exception{
		logger.info("dealCreditorMatchFlows处理债券匹配流水账户:{} 钱袋子投资记录ID:{} 钱袋子账户ID:{} 第三方账户ID:{} "
				+ "债转金额:{} 处理日期:{} 债转标是:{} ",qdzRegUserBiddInvestId,qdzRegUserId,thirdRegUserId,userTransMoney,repairTime,
				creditorFlag);
		        // 生成流水、资金划转、更新第三方账户金额
		        FinTradeFlow flow = FinTFUtil.initFinTradeFlow(qdzRegUserId,
								String.valueOf(qdzRegUserBiddInvestId), userTransMoney, TRADE_TYPE_CREDITOR_TRANSFER_QDZ,
								PlatformSourceEnums.PC); 
		        flow.setModifyTime(repairTime);
				List<FinFundtransfer> finFundtransfers = new ArrayList<>();
				FinFundtransfer qdzFinFundtransfer = null;
				// 第三方用户债转划转
				FinFundtransfer thirdFundtransfer = null;
				if( CREDITOR_FLAG_BUY == creditorFlag) {
					// 钱袋子用户划转
					FinFundtransfer qdzFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(), qdzRegUserId,
							null, userTransMoney, TRANSFER_SUB_CODE_TURNS_OUT_QDZ_CREDITOR);
					qdzFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
					qdzFundtransfer.setRecRegUserId(thirdRegUserId);
					finFundtransfers.add(qdzFundtransfer);
					qdzFinFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(),
							qdzRegUserId, null, userTransMoney, TRANSFER_SUB_CODE_CREDITOR_TRANSFER_INCOME);
					qdzFinFundtransfer.setRecRegUserId(qdzRegUserId);
					qdzFinFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
					finFundtransfers.add(qdzFinFundtransfer);
					 thirdFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(),
							thirdRegUserId, null, userTransMoney, TRANSFER_SUB_CODE_INCOME);
					 thirdFundtransfer.setRecRegUserId(qdzRegUserId);
		
				}else {
					 thirdFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(),
							thirdRegUserId, null, userTransMoney, TRANSFER_SUB_CODE_PAY);
					 thirdFundtransfer.setRecRegUserId(qdzRegUserId);
				}
				finFundtransfers.add(thirdFundtransfer);
				BaseUtil.getTccProxyBean(FinConsumptionService.class,getClass(),
						"dealCreditorMatchFlows").updateAccountInsertTradeAndTransfer(flow, finFundtransfers);
	}
	/**
	 * 
	 * @Description : 处理债券转让回款计划
	 * @Method_Name : dealCreditorMatchReceiptPlans
	 * @param qdzBidInvest
	 * @param thirdBiddInvest
	 * @param bidInfo
	 * @param qdzRate
	 * @return : void
	 * @Creation Date : 2017年7月26日 下午2:15:26
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void dealBuyCreditorMatchReceiptPlans(BidInvest qdzBidInvest, BidInvest thirdBiddInvest, BidInfo bidInfo,
			BigDecimal qdzRate, BigDecimal thirdInvestAmount,List<BidReceiptPlan> thirdOldReceiptPlans) throws Exception{
		logger.info("dealBuyCreditorMatchReceiptPlans 钱袋子债券匹配，处理回款计划:用户标识 {},用户投资记录标识:{}, 第三方投资记录标识:{}, "
				+ "钱袋子利率:{}, 转让金额:{}", qdzBidInvest.getRegUserId(),qdzBidInvest.getId(),thirdBiddInvest.getId(),qdzRate,thirdInvestAmount);
		BidCommonPlanVo planVo = new BidCommonPlanVo();
		planVo.setBidId(bidInfo.getId());
		planVo.setInvestId(qdzBidInvest.getId());
		planVo.setRegUserId(qdzBidInvest.getRegUserId());
		planVo.setTermUnit(bidInfo.getTermUnit());
		planVo.setTermValue(bidInfo.getTermValue());
		planVo.setTotalAmount(qdzBidInvest.getInvestAmount());
		planVo.setInterestRate(qdzRate);
		planVo.setLendingTime(bidInfo.getLendingTime());
		planVo.setRepayType(bidInfo.getBiddRepaymentWay());
		// 插入投资用户回款计划
		List<BidReceiptPlan> qdzReceiptPlans = RepayAndReceiptUtils.getReceiptPlanForInterestMonth(planVo, null,
				bidInfo.getInterestRate().subtract(qdzRate), thirdOldReceiptPlans.size());
		// 更新第三方回款计划
		List<BidReceiptPlan> thirdNewReceiptPlans = RepayAndReceiptUtils.getReceiptPlanForInterestMonth(
				thirdOldReceiptPlans, thirdInvestAmount.subtract(qdzBidInvest.getInvestAmount()), bidInfo.getTermValue(), bidInfo.getInterestRate(),
				BigDecimal.ZERO, BigDecimal.ZERO);
		bidReceiptPlanService.updateOrInsertBidReceiptPlanBatch(thirdNewReceiptPlans, qdzReceiptPlans);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Compensable
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?>  dealSellCreditorMatch(QdzAutoCreditorVo qdzAutoCreditorVo,BigDecimal userTransMoney,
			BigDecimal qdzRate,Date dealTime,Map creditorInfoMap) throws Exception {
		int step = 1;// 操作步骤
		BidInvest thirdOldBiddInvest = (BidInvest) creditorInfoMap.get("thirdBiddInvest");
		BidInvest qdzOldBidInvest = (BidInvest) creditorInfoMap.get("qdzBidInvest");
		BidInfo bidInfo = (BidInfo) creditorInfoMap.get("bidInfo");
		List<BidReceiptPlan> qdzReceiptPlans = (List<BidReceiptPlan>) creditorInfoMap.get("qdzReceiptPlans");
		List<BidReceiptPlan> thirdReceiptPlans = (List<BidReceiptPlan>) creditorInfoMap.get("thirdReceiptPlans");
		logger.info("tcc (qdzTaskJob#)dealSellCreditorMatch entrence,reference qdz#updateCreditorMoney|bidTransferManualService#dealCreditorMatchRecords|"
				+ "payment#dealCreditorMatchFlows,操作参数:钱袋子转让用户:{}, 钱袋子账户投资记录标识:{}",qdzAutoCreditorVo.getRegUserId(),qdzOldBidInvest.getId());
		BidTransferAuto tranferAuto = new BidTransferAuto();
		tranferAuto.setBidInfoId(bidInfo.getId());
		try {
			step++;
			// 1. 生成债券转让关系、处理投资记录
			ResponseEntity<?>  responseEntity = BaseUtil.getTccProxyBean(BidTransferManualService.class,getClass(),
					"dealSellCreditorMatch").dealCreditorMatchRecords(qdzOldBidInvest, thirdOldBiddInvest, tranferAuto, 
							bidInfo, userTransMoney, qdzRate, dealTime,CREDITOR_FLAG_SELL);
			if(responseEntity== null || responseEntity.getResStatus()!=SUCCESS) {
				throw new BusinessException("dealSellCreditorMatch钱袋子债券异常！");
			}
			tranferAuto.setId(Integer.parseInt(responseEntity.getParams().get("bidTransferAutoId").toString()));
			step++;
			logger.info("dealSellCreditorMatch:用户标识：{}，自动债权转让，插入第三方资金债转收入的资金划转，更新账户，step:{}", qdzAutoCreditorVo.getRegUserId());
			// 2、处理流水和资金划转
			dealCreditorMatchFlows(qdzOldBidInvest.getId(),
			      qdzOldBidInvest.getRegUserId(), thirdOldBiddInvest.getRegUserId(), userTransMoney,dealTime,CREDITOR_FLAG_SELL);
			step++;
			logger.info("dealSellCreditorMatch:用户标识：{}，自动债权转让，生成或更新回款计划，step:{}", qdzAutoCreditorVo.getRegUserId(), step);
			// 3、生成或更新回款计划
			dealSellCreditorMatchReceiptPlans(thirdReceiptPlans, qdzReceiptPlans, bidInfo, qdzAutoCreditorVo.getRate(),
					qdzOldBidInvest.getInvestAmount().subtract(qdzOldBidInvest.getTransAmount()).subtract(userTransMoney),
					thirdOldBiddInvest.getInvestAmount().subtract(thirdOldBiddInvest.getTransAmount()).add(userTransMoney));
			step++;
			logger.info("dealSellCreditorMatch:用户标识：{}，自动债权转让，生成合同，step:{}", qdzAutoCreditorVo.getRegUserId(), step);
			// 4、生成合同
			ConInfoSelfGenerateDTO dto = new ConInfoSelfGenerateDTO();
			List<Integer> investListForTransferContract = new ArrayList<>();
			investListForTransferContract.add(thirdOldBiddInvest.getId());
			dto.setInvestIdList(investListForTransferContract);
			dto.setContractType(ContractConstants.CONTRACT_TYPE_CREDITOR_TRANSFER);
			jmsService.sendMsg(ContractConstants.MQ_QUEUE_SELF_CONINFO, DestinationType.QUEUE, dto,
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("tcc error (qdzTaskJob#)dealSellCreditorMatch entrence,reference qdz#updateCreditorMoney|bidTransferManualService#dealCreditorMatchRecords|"
					+ "payment#dealCreditorMatchFlows,操作参数:操作参数:钱袋子转让用户:{}, 钱袋子账户投资记录标识:{}",qdzAutoCreditorVo.toString(),qdzOldBidInvest.getId(),step,e);
			throw new BusinessException("dealSellCreditorMatch钱袋子债券异常！");
		}
		return new ResponseEntity<>(SUCCESS);
	}
	/**
	 * 
	 * @Description : 处理债券转让回款计划
	 * @Method_Name : dealCreditorMatchReceiptPlans
	 * @param qdzBidInvest
	 * @param thirdBiddInvest
	 * @param bidInfo
	 * @param qdzRate
	 * @return : void
	 * @Creation Date : 2017年7月26日 下午2:15:26
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void dealSellCreditorMatchReceiptPlans(List<BidReceiptPlan> thirdOldReceiptPlans ,List<BidReceiptPlan> qdzOldReceiptPlans, BidInfo bidInfo,
			BigDecimal qdzRate, BigDecimal qdzActualInvestAmount, BigDecimal thirdActualInvestAmount) throws Exception {
		List<BidReceiptPlan> allBidReceiptPlans = new ArrayList<BidReceiptPlan>();
		// 更新第三方用户回款计划
		List<BidReceiptPlan> thirdNewReceiptPlans = RepayAndReceiptUtils.getReceiptPlanForInterestMonth(
				thirdOldReceiptPlans,thirdActualInvestAmount,bidInfo.getTermValue(), bidInfo.getInterestRate(), BigDecimal.ZERO, BigDecimal.ZERO);
		allBidReceiptPlans.addAll(thirdNewReceiptPlans);
		// 更新投资用户回款计划
		List<BidReceiptPlan> qdzNewReceiptPlans = RepayAndReceiptUtils.getReceiptPlanForInterestMonth(
				qdzOldReceiptPlans, qdzActualInvestAmount, bidInfo.getTermValue(),
				qdzRate, BigDecimal.ZERO, bidInfo.getInterestRate().subtract(qdzRate).abs());
		allBidReceiptPlans.addAll(qdzNewReceiptPlans);
		bidReceiptPlanService.updateOrInsertBidReceiptPlanBatch(allBidReceiptPlans, null);
	}

	@Override
	public ResponseEntity<?> calculateInterest(Date date, int shardingItem) {
		String currentJobDate = DateUtils.format(date, DateUtils.DATE_HH_MM_SS);
		try {
			logger.info("calculateInterest开始跑批计算钱袋子利息：计算利息时间{}, 处理分片项{}", currentJobDate, shardingItem);
			// 查询所有钱袋子账户信息
			List<QdzAccount> accounts = qdzAccountService.findQdzAccountByShardingItem(shardingItem);
			if (accounts == null || accounts.isEmpty()) {
				return new ResponseEntity<>(SUCCESS, "跑批计算钱袋子利息,没有查询到钱袋子用户！");
			}
			List<QdzTransRecord> records = null;
			QdzTransRecord calcInterestR = null;// 计算利息当前记录
			BigDecimal calcInterestMoney = BigDecimal.ZERO;// 计算利息当前记录aferMoney
			List<QdzAccount> currentInterestZeroAccounts = new ArrayList<>();// 利息为零的钱袋子账户集合
			QdzAccount currentInterestZeroAccount = new QdzAccount();// 利息为零的钱袋子账户
			// 组装查询个人截至当天时间17：00，最近一条记录的查询条件
			QdzTransRecord dtc = new QdzTransRecord();
			Date endTime = QdzDateUtils.format(date, DateUtils.DATE_HH_MM, 0,
					PropertiesHolder.getProperty("qdz_income_time"));
			dtc.setCreateTimeEnd(endTime);
			dtc.setState(TRANS_RECORD_SUCCSS);
			dtc.setSortColumns("id desc limit 1");
			// 钱袋子利率记录
			QdzRateRecord rateRecord = qdzRateRecordService.getQdzRateRecord(date);
			for (QdzAccount account : accounts) {
				dtc.setRegUserId(account.getRegUserId());
				QdzInterestDay qdzInterestDay = new QdzInterestDay();
				qdzInterestDay.setDayBegin(DateUtils.getFirstTimeOfDay(date));
				qdzInterestDay.setDayEnd(DateUtils.getLastTimeOfDay(date));
				qdzInterestDay.setRegUserId(account.getRegUserId());
				int qdzInterestCurrentDay = qdzInterestDayService.findQdzInterestDayCount(qdzInterestDay);
				if (qdzInterestCurrentDay > 0) {
					logger.info("用户{}，当天已经计息{}", account.getRegUserId(), date);
					continue;
				}
				records = qdzTransRecordService.findQdzTransRecordList(dtc);
				// 该用户只开通账户，没有进行转入转出操作
				if (records == null || records.isEmpty()) {
					continue;
				}
				// 获取个人截至当天时间17：00，最近一条记录
				calcInterestR = records.get(0);
				calcInterestMoney = calcInterestR.getAfterMoney();
				// 如果afterMoney的值大于0，则计算利息,否则不给此用户发放利息
				if (CompareUtil.gtZero(calcInterestMoney)) {
					calculateInterestByRecord(calcInterestR, date, rateRecord, account);// 开始计算利息
				} else if (CompareUtil.eZero(account.getMoney()) && CompareUtil.gtZero(account.getYedInterest())) {// 更新昨天利息，昨天利息已经是0的不需要继续更新
					currentInterestZeroAccount.setRegUserId(account.getRegUserId());
					currentInterestZeroAccount.setYedInterest(BigDecimal.ZERO);
					currentInterestZeroAccount.setModifyTime(new Date());
					currentInterestZeroAccount.setInterestRate(BigDecimal.valueOf(0));
					currentInterestZeroAccount.setInterestDay(0);
					currentInterestZeroAccounts.add(account);
				}
			}
			// 更新所有当天利息为0的账户
			if (currentInterestZeroAccounts.size() > 0) {
				qdzAccountService.updateQdzAccountBatch(currentInterestZeroAccounts, currentInterestZeroAccounts.size());
			}
		} catch (Exception e) {
		logger.error("calculateInterest开始跑批计算利息失败！"+currentJobDate,e);
		}
		return new ResponseEntity<>(SUCCESS, "钱袋子跑批利息成功！");
	}

	/**
	 * 
	 * @Description : 根据记录计算利息
	 * @Method_Name : calculateInterestByRecord
	 * @param record
	 * @param day
	 * @param qdzRecommendAwardRule
	 * @return : void
	 * @throws Exception
	 * @Creation Date : 2017年7月19日 上午10:38:33
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void calculateInterestByRecord(QdzTransRecord record, Date day, QdzRateRecord rateRecord,
			QdzAccount qdzAccount) {
		logger.info("开始计算利息，记录Id{},计息用户{},计息金额{},计息利率{},计息时间{}", record.getId(), record.getRegUserId(),
				record.getAfterMoney(), rateRecord.getRate(), day);
		QdzInterestDay qdzInterestDay = null;
		try {
			// 1. 钱袋子每日利息表
			logger.info("钱袋子每日利息，生成利息记录： {}, 计息步骤{}", record.getId());
			qdzInterestDay = getQdzInterestDay(qdzAccount, record, day, rateRecord, QDZ_INTEREST_DAY_FAIL);
			ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealCalcInterest(qdzInterestDay, rateRecord,day);
		} catch (Exception e) {
			logger.error("QDZ生成利息异常：记录Id{},计息时间{},计息步骤{}", record.getId(), day,e);
		}
	}

	/**
	 * @Description : 用于计算用于当天计算利息的利率值
	 * @Method_Name : calculateRate;
	 * @param qdzAccount
	 *            钱袋子账户
	 * @param qdzRateRecord
	 *            钱袋子利率记录
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年8月2日 下午2:48:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private BigDecimal calculateRate(QdzAccount qdzAccount, QdzRateRecord qdzRateRecord) {
		BigDecimal currentRate = BigDecimal.ZERO;// 钱袋子计算利息的利率值
		// 查询钱袋子利息加息的开关
		VasRebatesRule vasRebatesRuleOnOff = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
				VasRuleTypeEnum.QDZINTERESTADDONOFF.getValue(), VasConstants.VAS_RULE_STATE_START);
		// 如果可以查询到，则钱袋子计算利息的利率=钱袋子账户累识加息利率值+前一天钱袋子利率跑批的利率值
		if (vasRebatesRuleOnOff != null) {
			currentRate = (qdzAccount != null ? qdzAccount.getInterestRate() : BigDecimal.ZERO)
					.add(qdzRateRecord.getRate());
		} else {
			// 如果钱袋子利息加息的开关关闭，则钱袋子计算利息的利率=前一天钱袋子利率跑批的利率值
			currentRate = qdzRateRecord.getRate();
		}
		return currentRate;
	}

	/**
	 * @Description : 生成每天利息记录,默认状态为失败，正常处理完更新为成功
	 * @Method_Name : getQdzInterestDay;
	 * @param record：钱袋子账户
	 * @param record：钱袋子转入转出记录
	 * @param day：计算利息日期
	 * @param rateRecord：利率
	 * @param state：状态
	 * @return
	 * @return : QdzInterestDay;
	 * @Creation Date : 2017年8月2日 下午2:07:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public QdzInterestDay getQdzInterestDay(QdzAccount qdzAccount, QdzTransRecord record, Date day,
			QdzRateRecord rateRecord, int state) throws Exception{
		// 利率
	    QdzInterestDay qdzInterestDay = new QdzInterestDay();
	    qdzInterestDay.setRate(calculateRate(qdzAccount, rateRecord));
		// 当天利息
		BigDecimal dayInterest = CalcInterestUtil.calDayOfInterest(record.getAfterMoney(), qdzInterestDay.getRate());
		qdzInterestDay.setCreateTime(day);
		qdzInterestDay.setDay(DateUtils.parse(DateUtils.format(day)));
		qdzInterestDay.setDayInterest(dayInterest);
		qdzInterestDay.setModifyTime(new Date());
		qdzInterestDay.setMoney(record.getAfterMoney());
		qdzInterestDay.setRegUserId(record.getRegUserId());
		qdzInterestDay.setState(state);// 默认为失败，成功后会更新为成功
		qdzInterestDayService.insertQdzInterestDay(qdzInterestDay);
		qdzInterestDay.setId(qdzInterestDay.getId());
		return qdzInterestDay;
	}

	/**
	 * 
	 * @Description : 更新钱袋子每日利息成功
	 * @Method_Name : updateQdzInterestDay
	 * @param id
	 * @param state
	 * @return : void
	 * @Creation Date : 2017年7月21日 下午4:28:29
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void updateQdzInterestDay(Integer id, Integer state) {
		QdzInterestDay qdzInterestDay = new QdzInterestDay();
		qdzInterestDay.setId(id);
		qdzInterestDay.setModifyTime(new Date());
		qdzInterestDay.setState(state);
		qdzInterestDayService.updateQdzInterestDay(qdzInterestDay);
	}

	/**
	 * 
	 * @Description :钱袋子收益明细表
	 * @Method_Name : insertInterestDayDetail
	 * @return : void
	 * @throws Exception
	 * @Creation Date : 2017年7月19日 下午2:36:35
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void insertInterestDayDetail(QdzInterestDay qdzInterestDay) throws Exception {
		logger.info("钱袋子开始记录收入明细：" + qdzInterestDay);
		// 用户当日应收利息
		BigDecimal dayInterest = qdzInterestDay.getDayInterest();
		// 统计匹配活期标的具体支出利息
		Map<String, BigDecimal> interests = matchBidInterest(qdzInterestDay.getMoney(), qdzInterestDay.getRegUserId());
		Iterator<Entry<String, BigDecimal>> entries = interests.entrySet().iterator();
		int size = interests.size();
		String thUserId = "";
		String biddId = "";
		BigDecimal amount = BigDecimal.ZERO;// 活期标，垫付利息金额
		int i = 1;
		// 当日利率
		BigDecimal rate = qdzInterestDay.getRate();
		BigDecimal interestSum = BigDecimal.ZERO;
		while (entries.hasNext()) {
			Entry<String, BigDecimal> next = entries.next();
			String key = next.getKey();
			// 第三方用户id
			thUserId = key.split("_")[0];
			// 投资的标的ID
			biddId = key.split("_")[1];
			// 活期标，垫付利息金额
			amount = next.getValue();
			// 当日利息
			BigDecimal interest = CalcInterestUtil.calDayOfInterest(amount, rate);
			if (i == size && interest.compareTo(dayInterest.subtract(interestSum)) != 0) {
				interest = dayInterest.subtract(interestSum);
			} else {
				logger.info("用户ID&实际计息&第三方{" + qdzInterestDay.getRegUserId() + "," + interest + "," + thUserId + "}");
			}
			i++;
			interestSum = interestSum.add(interest);
			// 服务费
			BigDecimal interestFee = new BigDecimal(0.00);
			// 如果是平台账户则服务费为将垫付的利息
			if (PLATFORM_ACCOUNT_ID == Integer.parseInt(thUserId)) {
				interestFee = interest.multiply(new BigDecimal(-1));
			} else {
				BidInfo bidInfo = bidInfoService.findBidInfoById(Integer.parseInt(biddId));
				BigDecimal biddRate = bidInfo.getInterestRate();// 标的年利率
				interestFee = CalcInterestUtil.calInterestDiffer(amount, biddRate, rate);// 服务费
			}
			QdzInterestDayDetail interestDayDetail = new QdzInterestDayDetail();
			interestDayDetail.setBidId(Integer.parseInt(biddId));
			interestDayDetail.setCreateTime(qdzInterestDay.getCreateTime());
			interestDayDetail.setDay(qdzInterestDay.getDay());
			interestDayDetail.setInterest(interest);
			interestDayDetail.setInterestFee(interestFee);
			interestDayDetail.setMoney(amount);
			interestDayDetail.setQdzInterestDayId(qdzInterestDay.getId());
			interestDayDetail.setRegUserId(qdzInterestDay.getRegUserId());
			interestDayDetail.setThirdRegUserId(Integer.parseInt(thUserId));
			qdzInterestDayDetailService.insertQdzInterestDayDetail(interestDayDetail);
		}
	}

	/**
	 * 
	 * @Description : 统计匹配活期标的具体支出利息
	 * @Method_Name : matchBidInterest
	 * @param afterMoney
	 *            计算利息的本金
	 * @param regUserId
	 *            用户ID
	 * @return
	 * @return : Map<String,BigDecimal>
	 * @Creation Date : 2017年7月19日 下午2:55:45
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private Map<String, BigDecimal> matchBidInterest(BigDecimal afterMoney, Integer regUserId) throws Exception {
		// 不同第三方需要垫付利息的本金集合
		Map<String, BigDecimal> userThMap = new LinkedHashMap<String, BigDecimal>();
		// 查询用户放款中的活期标的投资记录信息
		List<BidInvest> bidInvests = bidInvestService.findInvests(BID_PRODUCT_CURRENT, BID_STATE_WAIT_REPAY, regUserId);
		// 用户没有投资记录，则当天转入的钱的利息由平台垫付
		if (bidInvests == null || bidInvests.isEmpty()) {
			userThMap.put(String.valueOf(PLATFORM_ACCOUNT_ID) + "_" + "-1", afterMoney);
			return userThMap;
		}
		// 每个用户投资总钱数
		BigDecimal totalInvestAtm = BigDecimal.ZERO;
		int bidId = 0;
		BidInvest dtc = new BidInvest();
		BidInvest tBidInvest = null;
		// 遍历用户投资记录，计算利息由谁来垫付
		for (BidInvest bidInvest : bidInvests) {
			// 获取投资金额
			BigDecimal investAmount = bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount());
			bidId = bidInvest.getBidInfoId();
			totalInvestAtm = totalInvestAtm.add(investAmount);
			// 获取第三方账户投资记录
			dtc.setBidInfoId(bidId);
			dtc.setSortColumns("id asc");
			List<BidInvest> tBidInvests = bidInvestService.findBidInvestList(dtc);
			tBidInvest = tBidInvests.get(0);
			int tRegUserId = tBidInvest.getRegUserId();
			// 第三方的userId 和 标的id 做为key, 投资金额做为 value
			String key = String.valueOf(tRegUserId) + "_" + String.valueOf(bidId);
			if (userThMap.get(key) == null) {
				userThMap.put(key, investAmount);
			} else {
				userThMap.put(key, investAmount.add(userThMap.get(key)));
			}
		}
		// 如果计息金额>实际持有活期投资总金额，平台需要垫息
		if (CompareUtil.gt(afterMoney, totalInvestAtm)) {
			userThMap.put(String.valueOf(PLATFORM_ACCOUNT_ID) + "_" + "-1", afterMoney.subtract(totalInvestAtm));
		}
		return userThMap;
	}

	/**
	 * 
	 * @Description : 处理钱袋子用户每日利息收入流水
	 * @Method_Name : insertQdzTradeAndTransfer
	 * @param qdzInterestDay
	 * @return : void
	 * @Creation Date : 2017年7月19日 下午5:49:25
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public void insertQdzTradeAndTransfer(QdzInterestDay qdzInterestDay,Date day) throws Exception {
		// 生成一条每日利息流水信息
		FinTradeFlow flow = FinTFUtil.initFinTradeFlow(qdzInterestDay.getRegUserId(),
				String.valueOf(qdzInterestDay.getId()), qdzInterestDay.getDayInterest(), TRADE_TYPE_QDZ_DAY_INTEREST,
				PlatformSourceEnums.PC);
		List<FinFundtransfer> transfers = new ArrayList<>();
		// 用户收入的钱袋子的每日利息的资金划转
		FinFundtransfer userFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(),
		        qdzInterestDay.getRegUserId(), null, qdzInterestDay.getDayInterest(),
				TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
						FundtransferSmallTypeStateEnum.INTEREST));
		if(!DateUtils.isSameDay(day,new Date())) {
			flow.setCreateTime(day);
			flow.setModifyTime(day);
			userFundtransfer.setCreateTime(day);
			userFundtransfer.setModifyTime(day);
		}
		transfers.add(userFundtransfer);
		// 更新用户的账户信息，生成交易流水及资金划转
		BaseUtil.getTccProxyBean(FinConsumptionService.class,getClass(),
				"dealInsertQdzInterestDayTradeAndTransfer").updateAccountInsertTradeAndTransfer(flow, transfers);
	}

	/**
	 * 
	 * @Description : 处理第三方每日利息支出流水
	 * @Method_Name : insertThirdTradeAndTransfer
	 * @param qdzInterestDay
	 * @return : void
	 * @Creation Date : 2017年7月19日 下午5:49:25
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private FinFundtransfer insertThirdTradeAndTransfer(QdzInterestDayDetail qdzInterestDayDetail) {
		FinTradeFlow qdzInterestDayFinTradeFlow = finTradeFlowService.findByPflowIdAndTradeType(
				String.valueOf(qdzInterestDayDetail.getQdzInterestDayId()),TRADE_TYPE_QDZ_DAY_INTEREST);
		if (qdzInterestDayFinTradeFlow == null) {
			logger.error("insertThirdTradeAndTransfer用户利息未生成，第三方不能垫息", String.valueOf(qdzInterestDayDetail.getId()));
			return null;
		}
		FinFundtransfer fundtransferDtc = new FinFundtransfer();
		fundtransferDtc.setTradeType(TRADE_TYPE_QDZ_DAY_INTEREST);
		fundtransferDtc.setRegUserId(qdzInterestDayDetail.getThirdRegUserId());
		fundtransferDtc.setTradeFlowId(qdzInterestDayFinTradeFlow.getFlowId());
		List<FinFundtransfer> transfers = finFundtransferService.findByCondition(fundtransferDtc);
		if (transfers != null && !transfers.isEmpty()) {
			logger.error("insertThirdTradeAndTransfer第三方已经垫息:钱袋子利息明细id:{}, 已垫息流水标识:{}", 
					String.valueOf(qdzInterestDayDetail.getId()),
					transfers.get(0).getFlowId());
			return null;
		}
		// 创建资金划转对象信息
		FinFundtransfer fundtransfer = FinTFUtil.initFinFundtransfer(qdzInterestDayFinTradeFlow.getFlowId(),
				qdzInterestDayDetail.getThirdRegUserId(), qdzInterestDayDetail.getRegUserId(),
				qdzInterestDayDetail.getInterest(), TradeTransferConstants.getFundTransferSubCodeByType(
						FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.INTEREST));
		fundtransfer.setCreateTime(qdzInterestDayDetail.getCreateTime());
		return fundtransfer;
	}

	/**
	 * 
	 * @Description : 更新钱袋子账户表
	 * @Method_Name : updateQdzAccount
	 * @param qdzInterestDay
	 * @return : void
	 * @Creation Date : 2017年7月19日 下午5:53:13
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void updateQdzAccount(QdzInterestDay qdzInterestDay) throws Exception {
		QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(qdzInterestDay.getRegUserId());
		QdzAccount account = new QdzAccount();
		account.setRegUserId(qdzInterestDay.getRegUserId());
		account.setModifyTime(new Date());
		account.setYedInterest(qdzAccount != null
				? qdzInterestDay.getDayInterest().subtract(qdzAccount.getYedInterest()) : BigDecimal.ZERO);
		account.setTotalInterest(qdzInterestDay.getDayInterest());
		
		// 查询钱袋子利息加息的开关
		VasRebatesRule vasRebatesRuleOnOff = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
				VasRuleTypeEnum.QDZINTERESTADDONOFF.getValue(), VasConstants.VAS_RULE_STATE_START);
		// 如果钱袋子加息开关不为空，并且当前债权金额大于等于5W,则加息0.01,如果累计加息天数大于360,则重新计算
		if (vasRebatesRuleOnOff != null && StringUtils.isNotBlank(vasRebatesRuleOnOff.getContent())) {
			QdzRaiseInterestVo qdzRaiseInterestVo = JsonUtils.json2Object(vasRebatesRuleOnOff.getContent(),
					QdzRaiseInterestVo.class, null);
			if (CompareUtil.gte(qdzInterestDay.getMoney(), qdzRaiseInterestVo.getRaiseInterestLimit() == null
					? BigDecimal.ZERO : qdzRaiseInterestVo.getRaiseInterestLimit())) {
				if (qdzAccount.getInterestDay() >= (qdzRaiseInterestVo.getInterestDayLimit() == null ? 360
						: qdzRaiseInterestVo.getInterestDayLimit())) {
					// 如果钱袋子累计加息天数大于等于360，则重新计算
					account.setInterestDay(0);
					account.setInterestRate(BigDecimal.ZERO.subtract(qdzAccount.getInterestRate()));
				} else {
					// 累计利率加0.01
					account.setInterestRate(qdzRaiseInterestVo.getRaiseInterestRate() == null ? BigDecimal.valueOf(0.01)
							: qdzRaiseInterestVo.getRaiseInterestRate());
					// 累计天数加1
					account.setInterestDay(qdzAccount.getInterestDay() + 1);
				}
			}else{
			    account.setInterestDay(0);
		        account.setInterestRate(BigDecimal.ZERO.subtract(qdzAccount.getInterestRate()));
			}
		}
		qdzAccountService.updateQdzAccountByRegUserId(account);
	}

	/**
	 * 
	 * @Description : 第三方账户垫息
	 * @Method_Name : thirdAccountPadBearing
	 * @param day
	 * @return
	 * @return : String
	 * @Creation Date : 2017年7月20日 上午9:50:12
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Override
	public void thirdAccountPadBearing(Date day) {
		QdzInterestDayDetail dtc = new QdzInterestDayDetail();
		dtc.setDay(DateUtils.parse(DateUtils.format(day)));
		FinFundtransfer fundtransfer = null;
		List<FinFundtransfer> fundtransfers = new ArrayList<FinFundtransfer>();
		try {
			List<QdzInterestDayDetail> details = qdzInterestDayDetailService.findSuccQdzInterestDayDetails(dtc);
			if (details.isEmpty()) {
				logger.info("当前时间没有需要第三方账户垫息数据：{}", day);
			}
			for (QdzInterestDayDetail d : details) {
				fundtransfer = insertThirdTradeAndTransfer(d);
				if(fundtransfer != null) {
					fundtransfers.add(fundtransfer);
				}
			}
			finConsumptionService.updateAccountInsertTransfer(TRADE_TYPE_QDZ_DAY_INTEREST, fundtransfers);
		} catch (Exception e) {
			logger.error("第三方垫息计算异常！{}" + day);
		}
	}

	@Override
	public ResponseEntity<?> qdzRateRecord() {
		Date day = DateUtils.getCurrentDate();
		try {
			QdzRateRecord rateRecord  = qdzRateRecordService.findQdzRateRecordByTime(day);
			if(rateRecord != null) {
				return new ResponseEntity<>(SUCCESS, "钱袋子当日利率记录已存在！");
			}
			VasRebatesRule qdzRule = vasRebatesRuleService
					.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZ.getValue(), VAS_RULE_STATE_START);
			QdzRateRecord  newRateRecord = new QdzRateRecord();
			if (qdzRule != null) {
				QdzVasRuleItem qdzVasRuleItem = JsonUtils.json2Object(qdzRule.getContent(), QdzVasRuleItem.class, null);
				newRateRecord.setCreateTime(DateUtils.parse(DateUtils.format(day)));
				newRateRecord.setRate(BigDecimal.valueOf(qdzVasRuleItem.getCurrInterestRate()));
				newRateRecord.setVasRebatesRuleId(qdzRule.getId());
			} else {
				//如果钱袋子规则不存在，或者不小心禁用，则取最近一天的规则利率记录,缺点：钱袋子下线以后，定时应一起停止，否则生成垃圾数据
				rateRecord = qdzRateRecordService.getQdzRateRecord(day);
				newRateRecord.setCreateTime(DateUtils.parse(DateUtils.format(day)));
				newRateRecord.setRate(rateRecord.getRate());
				newRateRecord.setVasRebatesRuleId(newRateRecord.getVasRebatesRuleId());
			}
			qdzRateRecordService.insertQdzRateRecord(newRateRecord);
		} catch (Exception e) {
			logger.error("钱袋子当日利率记录失败！" + DateUtils.format(day),e);
		}
		return new ResponseEntity<>(SUCCESS, "钱袋子当日利率记录成功！");
	}

	/**
	 * 
	 * @Description :发送推荐奖
	 * @Method_Name : qdzRecommendAward
	 * @param interestDay
	 * @param qdzRecommendAwardRule
	 * @return : void
	 * @Creation Date : 2017年7月21日 上午10:37:19
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void qdzRecommendAward(QdzInterestDay interestDay) throws Exception{
		RcommendEarnInfo rcommendEarnInfo = new RcommendEarnInfo();
			RegUserFriends regUserFriends = new RegUserFriends();
			regUserFriends.setGrade(VasConstants.RECOMMEND_FRIEND_LEVEL_ONE);
			regUserFriends.setRegUserId(interestDay.getRegUserId());
			List<RegUserFriends> levelOneFriend = regUserFriendsService.findRecommendFriendsList(regUserFriends);
			if (levelOneFriend == null || levelOneFriend.isEmpty()) {
				logger.info("qdzRecommendAward:one level friend is exist,RegUserId:{}",interestDay.getRegUserId());
				return;
			}
			rcommendEarnInfo.setFriendLevel(FRIEND_LEVEL_ONE);
			rcommendEarnInfo.setNote("钱袋子好友推荐奖金");
			rcommendEarnInfo.setInvestAmount(interestDay.getMoney());
			rcommendEarnInfo.setRegUserId(interestDay.getRegUserId());
			rcommendEarnInfo.setResourceId(interestDay.getId());
			rcommendEarnInfo.setType(VasRuleTypeEnum.QDZRECOMMONEY.getValue());
			rcommendEarnInfo.setState(RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
			rcommendEarnInfo.setRecommendRegUserId(levelOneFriend.get(0).getRecommendId());
			jmsService.sendMsg(VasConstants.MQ_QUEUE_RECOMMEND_EARN_RECORD, DestinationType.QUEUE, rcommendEarnInfo,
					JmsMessageType.OBJECT);
	}

	@Override
	public ResponseEntity<?> repairCalcInterest(String qdzInterestDayIds) {
		StringBuffer fails = new StringBuffer();
		String interestDayId = "";
		QdzInterestDay qdzInterestDay = null;
		QdzRateRecord rateRecord = null;
		try {
			String interestDayIds[] = qdzInterestDayIds.split(",");
			if (interestDayIds == null || interestDayIds.length == 0) {
				return BaseUtil.error("恢复利息ID不存在！");
			}
			for (int i = 0; i < interestDayIds.length; i++) {
				interestDayId = interestDayIds[i];
				qdzInterestDay = qdzInterestDayService.findQdzInterestDayById(Integer.parseInt(interestDayId));
				// 钱袋子利率记录
				rateRecord = qdzRateRecordService.getQdzRateRecord(qdzInterestDay.getDay());
				if (qdzInterestDay.getState() != QDZ_INTEREST_DAY_FAIL) {
					logger.info("恢复利息ID已经处理，" + interestDayId);
				}
				boolean flag = false;
				try {
					flag = ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealCalcInterest(qdzInterestDay, rateRecord,qdzInterestDay.getDay());
				} catch (Exception e) {
					logger.error("repairCalcInterest恢复利息失败:利息标识:{}",interestDayId,e);
				}
				if (!flag) {
					fails.append(interestDayId).append(",");
					continue;
				}
				// 恢复当天第三方计息
				QdzInterestDayDetail qdzInterestDayDetailCdt = new QdzInterestDayDetail();
				qdzInterestDayDetailCdt.setQdzInterestDayId(Integer.parseInt(interestDayId));
				List<QdzInterestDayDetail> details = qdzInterestDayDetailService
						.findQdzInterestDayDetailList(qdzInterestDayDetailCdt);
				for (QdzInterestDayDetail qdzInterestDayDetail : details) {
					insertThirdTradeAndTransfer(qdzInterestDayDetail);
				}
			}
		} catch (Exception e) {
			return BaseUtil.error("恢复异常");
		}
		return new ResponseEntity<>(SUCCESS, fails.toString());
	}

	/**
	 * 
	 * @Description : 处理利息
	 * @Method_Name : repairCalcInterest
	 * @param qdzInterestDay
	 * @param step
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月27日 下午2:13:04
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@Override
	@Compensable
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public boolean dealCalcInterest(QdzInterestDay qdzInterestDay, QdzRateRecord rateRecord,Date day) {
		logger.info("tcc (qdzTaskJob#)dealCalcInterest entrence,reference qdz#insertInterestDayDetail|qdz#updateQdzAccount|"
				+ "qdz#updateQdzInterestDay|payment#insertQdzTradeAndTransfer,操作参数:钱袋子用户账户id:{}, 第三方投资记录id:{}, 钱袋子用户id:{}, "
				+ "利息id:{}, 钱袋子利率:{}, 时间:{}",qdzInterestDay.getId(),rateRecord.getRate(), day);
		int step = 1;
		try {
				// 1. 钱袋子收益明细表
			    this.insertInterestDayDetail(qdzInterestDay);
				logger.info("repairCalcInterest钱袋子收益明细表，生成收益明细记录： 利息ID{},计息步骤{}", qdzInterestDay.getId(), step);
				step++;
				// 2. 更新钱袋子账户表
				this.updateQdzAccount(qdzInterestDay);
				logger.info("repairCalcInterest更新钱袋子账户表：利息ID{},计息步骤{}", qdzInterestDay.getId(), step);
				step++;
				// 3、更新记录成功
				this.updateQdzInterestDay(qdzInterestDay.getId(), QDZ_INTEREST_DAY_SUCCSS);
				logger.info("repairCalcInterest更新每日利息记录成功：利息ID{},计息步骤{}", qdzInterestDay.getId(), step);
				step++;
				//4、钱袋子用户的账户加钱 、添加流水
				this.insertQdzTradeAndTransfer(qdzInterestDay,day);
				logger.info("repairCalcInterest钱袋子用户的账户加钱 、添加流水：利息ID:{},计息步骤:{}", qdzInterestDay.getId(), step);
				step++;
				//5、发送推荐奖
				qdzRecommendAward(qdzInterestDay);
				step++;
		} catch (Exception e) {
			logger.error("tcc error (qdzTaskJob#)dealCalcInterest entrence,reference qdz#insertInterestDayDetail|qdz#updateQdzAccount|"
					+ "qdz#updateQdzInterestDay|payment#insertQdzTradeAndTransfer,操作参数:钱袋子用户账户id:{}, 第三方投资记录id:{}, 钱袋子用户id:{}, "
					+ "利息id:{}, 钱袋子利率:{}, 时间:{}",qdzInterestDay.getId(),rateRecord.getRate(), day,e);
			throw new BusinessException("repairCalcInterest计息异常!");
		}
		return true;
	}
	
	@Override
	public ResponseEntity<?> sellAutoCreditorByMQ(QdzAutoCreditorVo qdzAutoCreditorVo) {
		try {
			// 开始债券回购,查询转出用户的还款中的活期标的投资记录
			List<BidInvest> bidInvests = bidInvestService.findInvests(BID_PRODUCT_CURRENT, BID_STATE_WAIT_REPAY,
					qdzAutoCreditorVo.getRegUserId());
			// 投资金额
			BigDecimal investAtm = BigDecimal.ZERO;
			List<Integer> investState = new ArrayList<Integer>();
			investState.add(InvestConstants.INVEST_STATE_SUCCESS);// 投资成功
			investState.add(InvestConstants.INVEST_STATE_AUTO);// 自动转让
			BigDecimal residueTransMoney = qdzAutoCreditorVo.getTransMoney();// 待释放债权金额
			logger.info("转出自动债权释放, 用户标识: {}, 转出钱袋子自动债权转让, 待释放总金额: {}", qdzAutoCreditorVo.getRegUserId(),
					residueTransMoney);
			for (BidInvest bidInvest : bidInvests) {
				if(CompareUtil.lteZero(residueTransMoney)) {
					break;
				}
				//用户实际持有钱袋子债券
				investAtm = bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount());
				if (CompareUtil.lteZero(investAtm)) {
					continue;
				}
				BidInvest thirdInvest = bidInvestService.findInvestRecord(bidInvest.getBidInfoId(), investState);
				if (thirdInvest == null) {
					continue;
				}
				BidInfo bidInfo = bidInfoService.findBidInfoById(thirdInvest.getBidInfoId());
				// 查询第三方用户回款计划
				BidReceiptPlan bidReceiptPlanCdt = new BidReceiptPlan();
				bidReceiptPlanCdt.setState(REPAY_STATE_NONE);
				bidReceiptPlanCdt.setBidId(bidInfo.getId());
				bidReceiptPlanCdt.setSortColumns("periods asc");
				bidReceiptPlanCdt.setInvestId(thirdInvest.getId());
				bidReceiptPlanCdt.setRegUserId(thirdInvest.getRegUserId());
				List<BidReceiptPlan> thirdOldReceiptPlans = bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlanCdt);
				// 查询投资用户回款计划
				bidReceiptPlanCdt.setInvestId(bidInvest.getId());
				bidReceiptPlanCdt.setState(REPAY_STATE_NONE);
				bidReceiptPlanCdt.setSortColumns("periods asc");
				bidReceiptPlanCdt.setRegUserId(bidInvest.getRegUserId());
				List<BidReceiptPlan> qdzOldReceiptPlans = bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlanCdt);
				if(thirdOldReceiptPlans.isEmpty() || qdzOldReceiptPlans.isEmpty()) {
					logger.error("sellAutoCreditorByMQ:回款计划异常：{}",thirdInvest.getId(),bidInvest.getRegUserId());
					continue;
				}
				Map<String,Object> creditorInfoMap = new HashMap<String, Object>();
				// 当前第三方债权足够转让
				creditorInfoMap.put("bidInfo", bidInfo);
				creditorInfoMap.put("qdzBidInvest", bidInvest);
				creditorInfoMap.put("thirdBiddInvest", thirdInvest);
				creditorInfoMap.put("qdzReceiptPlans", qdzOldReceiptPlans);
				creditorInfoMap.put("thirdReceiptPlans", thirdOldReceiptPlans);
				// 当前投资金额大于需要释放的债权金额，则将债权全部释放
				try {
				if (CompareUtil.gte(investAtm, residueTransMoney)) {
					ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealSellCreditorMatch(qdzAutoCreditorVo, residueTransMoney, qdzAutoCreditorVo.getRate(), qdzAutoCreditorVo.getRepairTime(), creditorInfoMap);
					residueTransMoney = BigDecimal.ZERO;
					break;
				} else {// 当前投资金额小于需要释放的债权金额，则部分释放债权
					ApplicationContextUtils.getBean(QdzTaskJobFacade.class).dealSellCreditorMatch(qdzAutoCreditorVo, investAtm, qdzAutoCreditorVo.getRate(), qdzAutoCreditorVo.getRepairTime(), creditorInfoMap);
					residueTransMoney = residueTransMoney.subtract(investAtm);
				}
				} catch (Exception e) {
					logger.error("dealSellCreditorMatch：债券释放异常:用户标识:{} ,债转金额:{} ,投资记录标识:{}",bidInvest.getRegUserId(),residueTransMoney,bidInvest.getId(),e);
				}
				if(CompareUtil.gtZero(residueTransMoney)) {
					// 1、更新钱袋子债券
					qdzAccountService.updateCreditorMoney(qdzAutoCreditorVo.getRegUserId(), residueTransMoney, CREDITOR_FLAG_SELL);
				}
			}
		} catch (Exception e) {
			logger.error("转出自动债权释放, 用户标识: {}, 转让金额: {}, 转让失败: {}", qdzAutoCreditorVo.getRegUserId(),
					qdzAutoCreditorVo.getTransMoney(), e);
			return new ResponseEntity<>(ERROR, "转出自动债权转让失败");
		}
		return new ResponseEntity<>(SUCCESS);
	}
}
