package com.hongkun.finance.qdz.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzRateRecordService;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.qdz.service.QdzTransferService;
import com.hongkun.finance.qdz.vo.QdzAutoCreditorVo;
import com.hongkun.finance.qdz.vo.QdzTransferInOutCondition;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.QdzRaiseInterestVo;
import com.hongkun.finance.vas.model.QdzVasRuleItem;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.yirun.framework.core.commons.Constants.LOCK_EXPIRES;
import static com.yirun.framework.core.commons.Constants.LOCK_PREFFIX;

@Service
public class QdzTransferFacadeImpl implements QdzTransferFacade {
	private static final Logger logger = LoggerFactory.getLogger(QdzTransferFacadeImpl.class);
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private QdzTransferService qdzTransferService;
	@Reference
	private QdzRateRecordService qdzRateRecordService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private FinBankCardService finBankCardService;

	@Override
	@Compensable
	public ResponseEntity<?> transferIn(RegUser regUser, BigDecimal transMoney, String transferSource,
			Integer turnOutType) {
		logger.info("方法: transferIn, 钱袋子转入, 用户标识: {}, 入参: regUser: {}, transMoney: {}, transferSource: {},",
				regUser.getId(), regUser.toString(), transMoney, transferSource, turnOutType);
		Integer bugFlag = QdzConstants.QDZ_INSERT_FALG;// 判断钱袋子账户是否是插入，还是更新标识,默认插入
		QdzAccount qdzAccount = null;// 钱袋子账户
		// 校验钱袋子转入规则
		ResponseEntity<?> resResult = this.vasRebatesRuleService.checkQdzRule();
		if (resResult.getResStatus() == Constants.ERROR) {
			logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), resResult.getResMsg().toString());
			return new ResponseEntity<>(Constants.ERROR, "转入失败,请联系客服人员!");
		}
		QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) resResult.getParams().get("qdzVasRuleItem");
		VasRebatesRule vasRebatesRule = (VasRebatesRule) resResult.getParams().get("vasRebatesRule");
		// 如果钱袋子不在抢购中，则返回提示信息
		if (qdzVasRuleItem.getState() != QdzConstants.QDZ_RULR_STATE_BUYING) {
			return new ResponseEntity<>(Constants.ERROR, "禁止转入转出!");
		}
		logger.info("钱袋子转入, 用户标识: {}, 活期转入规则信息: {}", regUser.getId(), vasRebatesRule.toString());
		boolean resultFlag = false;
		String lockKey = LOCK_PREFFIX + QdzAccount.class.getSimpleName() + regUser.getId();
		JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
			if (result) {
				resultFlag = true;
				// 查询该用户的账户信息，用于判断用户转入金额是否合法
				FinAccount finAccount = finAccountService.findByRegUserId(regUser.getId());
				if (finAccount == null) {
					logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), "未查询到该用户的账户信息!");
					return new ResponseEntity<>(Constants.ERROR, "未查询到该用户的账户信息!");
				}
				// 校验用户转入入金额是否满足条件 (>0 && <可用余额 )
				if (CompareUtil.gtZero(transMoney) && CompareUtil.gte(finAccount.getUseableMoney(), transMoney)
						&& transMoney.divideAndRemainder(new BigDecimal(100))[1].compareTo(BigDecimal.ZERO) == 0) {
					// 判断单笔投资金额是否低于平台限制
					if (CompareUtil.gt(qdzVasRuleItem.getInvestLowest(), transMoney)) {
						logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(),
								"平台单笔投资最低限制为 " + qdzVasRuleItem.getInvestLowest() + "元");
						return new ResponseEntity<>(Constants.ERROR,
								"平台单笔投资最低限制为 " + qdzVasRuleItem.getInvestLowest() + "元");
					}
					// 判断投资金额是否超出每人每天最大投资金额
					//查询用户是否是白名单用户
	                boolean isWhiteUser = rosInfoService.validateRoster(regUser.getId(),
	                        RosterType.getRosterType(RosterType.CURRENT_IN_OUT.getValue()),
	                        RosterFlag.getRosterFlag(RosterFlag.WHITE.getValue()));
	                //债权池金额
	                VasRebatesRule creditorRule = this.vasRebatesRuleService.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.CREDITORMONEY.getValue(), VasConstants.VAS_RULE_STATE_START);
					BigDecimal redisMoney=creditorRule==null?BigDecimal.ZERO:new BigDecimal(creditorRule.getContent());
					//当天转入总金额
					BigDecimal maxMoneyOfDay = qdzTransRecordService.findSumTransMoneyOfDay(regUser.getId(),
                            TransTypeEnum.PAYIN.getValue());
					//可购买的债权金额
					BigDecimal useAbleMoney = qdzVasRuleItem.getInMaxMoneyPPPD().subtract(maxMoneyOfDay);
					useAbleMoney = CompareUtil.ltZero(useAbleMoney)?BigDecimal.ZERO:useAbleMoney;
					if(!isWhiteUser){
					    if(useAbleMoney.compareTo(BigDecimal.ZERO)==0 && redisMoney.compareTo(BigDecimal.ZERO)==1){
	                        logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(),
	                                "今日累计转入金额已超额");
	                        return new ResponseEntity<>(Constants.ERROR,"您今日累计转入金额已超额");
	                    }
	                    if (transMoney.add(maxMoneyOfDay).compareTo(qdzVasRuleItem.getInMaxMoneyPPPD()) == 1) {
	                        logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(),
	                                "平台每人每天最大投资金额限制为" + qdzVasRuleItem.getInMaxMoneyPPPD() + "元");
	                        return new ResponseEntity<>(Constants.ERROR,
	                                "平台每人每天最大投资金额限制为" + qdzVasRuleItem.getInMaxMoneyPPPD() + "元");
	                    }  
					}
					//如果可投金额大于债权池金额或者是白名单用户，那么可投金额就是当前债权池的金额
					if(useAbleMoney.compareTo(redisMoney) == 1 || isWhiteUser){
					    useAbleMoney = redisMoney;
                    }
                    if(transMoney.compareTo(useAbleMoney) == 1){
                        return new ResponseEntity<>(Constants.ERROR,"转入金额不能大于可投金额");
                    }
					// 判断用户是否满足转入条件，最多每月转入多少笔
					/*Integer times = qdzTransRecordService.findTransferInTimesOfMonth(regUser.getId(),
							TransTypeEnum.PAYIN.getValue());
					if (times >= qdzVasRuleItem.getInOPPPerMonth()) {
						logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(),
								"平台每人每月最多转入" + qdzVasRuleItem.getInOPPPerMonth() + "次");
						return new ResponseEntity<>(Constants.ERROR,
								"平台每人每月最多转入" + qdzVasRuleItem.getInOPPPerMonth() + "次");
					}*/
					// 判断投资金额+已有投资金额 是否大于 平台个人最大投资金额最大值
					qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUser.getId());
					if (qdzAccount != null) {
						bugFlag = QdzConstants.QDZ_UPDATE_FALG;// 钱袋子账户更新标识
						if (transMoney.add(qdzAccount.getMoney()).compareTo(qdzVasRuleItem.getHoldInvestMax()) == 1) {
							logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(),
									"平台个人投资最大持有金额限制为 " + qdzVasRuleItem.getHoldInvestMax() + "元");
							return new ResponseEntity<>(Constants.ERROR,
									"平台个人投资最大持有金额限制为 " + qdzVasRuleItem.getHoldInvestMax() + "元");
						}
					}
				} else {
					logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), "转入失败，转入金额必须大于0且小于等于可用金额，且是100的整数倍！");
					return new ResponseEntity<>(Constants.ERROR, "转入失败，转入金额必须大于0且小于等于可用金额，且是100的整数倍！");
				}
				// 用于处理钱袋子转入转出，更新或插入钱袋子账户信息及生成转入转出记录
				logger.info("钱袋子转入, 用户标识: {}, 活期转入步骤2,插入或更新活期账户,生成转入记录", regUser.getId());
				ResponseEntity<?> resultRes = BaseUtil
                        .getTccProxyBean(QdzTransferService.class, getClass(), "transferIn").dealTransferInOut(initQdzTransferInoutCondition(regUser.getId(),
								qdzAccount == null ? BigDecimal.ZERO : qdzAccount.getMoney(), transMoney,
								TransTypeEnum.PAYIN, StringUtils.isNotBlank(transferSource)
										? Integer.parseInt(transferSource) : PlatformSourceEnums.IOS.getValue(),
								bugFlag));
				if (resultRes.getResStatus() == Constants.ERROR) {
					logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), resultRes.getResMsg().toString());
					return new ResponseEntity<>(Constants.ERROR, "钱袋子转入失败!");
				}
				Integer qdzTransRecordId = (Integer) resultRes.getParams().get("qdzTransRecordId");
				// 乾坤袋账户扣减，生成一条流水，一条资金划转
				logger.info("钱袋子转入, 用户标识: {}, 金额: {}, 活期转入步骤3,更新用户账户,插入一条流水,插入一条资金划转", regUser.getId(), transMoney);
				FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(regUser.getId(), qdzTransRecordId, transMoney,
						turnOutType == QdzConstants.QDZ_TURNINOUT_TYPE_INVEST ? TRADE_TYPE_QDZ_TURNS_IN_INVEST
								: TRADE_TYPE_QDZ_TURNS_IN,
						PlatformSourceEnums.typeByValue(Integer.parseInt(transferSource)));
			    ResponseEntity<?> responseEntity = BaseUtil
                        .getTccProxyBean(FinConsumptionService.class, getClass(), "transferIn").cashPay(finTradeFlow,
						TRANSFER_SUB_CODE_TURNS_IN);
				if (responseEntity.getResStatus() == Constants.ERROR) {
					logger.error("钱袋子转入, 用户标识: {}, 转入失败: {}", regUser.getId(), responseEntity.getResMsg().toString());
					throw new BusinessException("更新用户账户,插入一条流水,一条资金划转失败!");
				}
				// 对债权池数据更新
				logger.info("钱袋子转入, 用户标识: {},活期转入步骤4,更新当前债权池金额: {}", regUser.getId(), transMoney);
				ResponseEntity<?> updateResult = vasRebatesRuleService.updateCreditorMoney(transMoney);
				if (updateResult.getResStatus() == Constants.ERROR) {
					throw new BusinessException(updateResult.getResMsg().toString());
				}
				//发送站内信，短信
				sendRushSms(regUser,QdzConstants.QDZ_TURN_IN,transMoney);
			} else {
				logger.error("钱袋子转入, 用户标识: {}, 活期转入异常: {}", regUser.getId(), "当前网络太拥挤，请稍候再试!");
				return new ResponseEntity<>(Constants.ERROR, "当前网络太拥挤，请稍候再试");
			}
		} catch (Exception e) {
			logger.error("用户标识：{},活期转入异常信息：", regUser.getId(), e);
			throw new GeneralException("钱袋子转入失败!");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, "恭喜您,转入成功！");
	}

	@Override
	@Compensable
	public ResponseEntity<?> transferOut(RegUser regUser, BigDecimal transMoney, String transferSource,
			Integer turnOutType) {
		logger.info("方法: transferOut, 钱袋子转出, 用户标识: {}, 入参: regUser: {}, transMoney: {}, transferSource: {},",
				regUser.getId(), regUser.toString(), transMoney, transferSource, turnOutType);
		FinTradeFlow creditorTradeFlow = null;// 钱袋子转出自动债权释放的交易流水
		BigDecimal payoutMoney = BigDecimal.ZERO;// 转出手续费
		// 1、 校验钱袋子转出规则
		ResponseEntity<?> resResult = this.vasRebatesRuleService.checkQdzRule();
		if (resResult.getResStatus() == Constants.ERROR) {
			logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", resResult.getResMsg().toString());
			return new ResponseEntity<>(Constants.ERROR, "转出失败,请联系客服人员!");
		}
		// 获取钱袋子规则对象
		QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) resResult.getParams().get("qdzVasRuleItem");
		VasRebatesRule vasRebatesRule = (VasRebatesRule) resResult.getParams().get("vasRebatesRule");
		logger.info("钱袋子转出, 用户标识: {}, 活期转出,当前转出规则: {}", regUser.getId(), vasRebatesRule.toString());
		// 如果钱袋子不在抢购中，则返回提示信息
        if (qdzVasRuleItem.getState() != QdzConstants.QDZ_RULR_STATE_BUYING) {
            return new ResponseEntity<>(Constants.ERROR, "禁止转入转出!");
        }
		boolean resultFlag = false;
		String lockKey = LOCK_PREFFIX + QdzAccount.class.getSimpleName() + regUser.getId();
		JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
			if (result) {
				resultFlag = true;
				// 获取钱袋子账户
				QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUser.getId());
				if (qdzAccount == null) {
					logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", regUser.getId(), "钱袋子账户不存在!");
					return new ResponseEntity<>(Constants.ERROR, "钱袋子转出失败!");
				}
				// 再次校验用户转出金额是否满足条件 (>0 && <钱袋子账户余额 ),然后进行资金划转操作
				if(CompareUtil.lteZero(transMoney)){
				    return new ResponseEntity<>(Constants.ERROR, "转出金额不能小于等于零");
				}
				if(CompareUtil.gt(transMoney,qdzAccount.getMoney())){
				    return new ResponseEntity<>(Constants.ERROR, "超过账户可转出额度");
				}
				if(transMoney.divideAndRemainder(new BigDecimal(100))[1].compareTo(BigDecimal.ZERO) != 0){
				    return new ResponseEntity<>(Constants.ERROR, "转出金额应为100的整数倍");
				}
				//查询用户是否是白名单用户
				boolean isWhiteUser = rosInfoService.validateRoster(regUser.getId(),
		                RosterType.getRosterType(RosterType.CURRENT_IN_OUT.getValue()),
		                RosterFlag.getRosterFlag(RosterFlag.WHITE.getValue()));
				//白名单用户并且是投资转出时，没有转出最大限制
				if(!isWhiteUser){
				    if(turnOutType != QdzConstants.QDZ_TURNINOUT_TYPE_INVEST){
				      // 判断单笔转出金额是否超出平台限制
	                    if (CompareUtil.gt(transMoney, qdzVasRuleItem.getOutMaxMoneyPPPD())) {
	                        logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", regUser.getId(),
	                                "平台单笔转出最大限制为" + qdzVasRuleItem.getOutMaxMoneyPPPD() + "元");
	                        return new ResponseEntity<>(Constants.ERROR,
	                                "平台单笔转出最大限制为" + qdzVasRuleItem.getOutMaxMoneyPPPD() + "元");
	                    }
				    }
				} 
				// 判断是否满足免费转出次数
				Integer times = qdzTransRecordService.findTransferInTimesOfMonth(regUser.getId(),
						TransTypeEnum.PAYOUT.getValue());
				//查询是否是从钱袋子投资的标的，如果是不算转出次数
				BidInvest bidInvest = new BidInvest();
	            bidInvest.setRegUserId(regUser.getId());
	            bidInvest.setInvestType(InvestConstants.BID_INVEST_TYPE_QDZ);
	            bidInvest.setCreateTimeBegin(DateUtils.getFirstDayOfMonth(new Date()));
	            bidInvest.setCreateTimeEnd(DateUtils.getLastDayOfMonth(new Date()));
	            bidInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
	            //如果不是钱袋子投资转出，则收取手续费
                if(turnOutType != QdzConstants.QDZ_TURNINOUT_TYPE_INVEST){
                 // 如果转出次数大于等于设置的最大转出次数，则收取手续费，如果是白名单用户没有转出次数限制
                    int qdzInvest = bidInvestService.findBidInvestCount(bidInvest);
                    if ((times-qdzInvest) >= qdzVasRuleItem.getOutOPPPerMonth() && !isWhiteUser) {
                        payoutMoney = transMoney.multiply(BigDecimal.valueOf(qdzVasRuleItem.getOutPayRate() / 100));
                    }
                }
				// 2、处理钱袋子账户，生成转入转出记录
				logger.info("用户标识:{},活期转出步骤2,更新活期账户金额:{},插入转出记录", regUser.getId(), transMoney);
				ResponseEntity<?> resultRes = BaseUtil
                        .getTccProxyBean(QdzTransferService.class, getClass(), "transferOut").dealTransferInOut(initQdzTransferInoutCondition(
						regUser.getId(), qdzAccount.getMoney() == null ? BigDecimal.ZERO : qdzAccount.getMoney(),
						transMoney, TransTypeEnum.PAYOUT, StringUtils.isBlank(transferSource)
								? PlatformSourceEnums.IOS.getValue() : Integer.parseInt(transferSource),
						QdzConstants.QDZ_UPDATE_FALG));
				if (resultRes.getResStatus() == Constants.ERROR) {
					logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", regUser.getId(), resultRes.getResMsg().toString());
					return new ResponseEntity<>(Constants.ERROR, "钱袋子转出失败!");
				}
				Integer qdzTransRecordId = (Integer) resultRes.getParams().get("qdzTransRecordId");
				// 已匹配债权，需要释放的债权
				BigDecimal remainderMoney = (BigDecimal) resultRes.getParams().get("remainderMoney");
				logger.info("钱袋子转出, 用户标识: {}, 活期转出步骤3,生成自动债权转让交易流水,插入一条流水，多条资金划转,债权待转让金额：{}", regUser.getId(),
						remainderMoney);
				// 3、如果待匹配债权小于转出金额，需要进行自动债权转让,生成自动债权转让的流水及资金划转
				if (CompareUtil.gtZero(remainderMoney)) {
					// 封装钱袋子转出自动债权释放的交易流水
					creditorTradeFlow = FinTFUtil.initFinTradeFlow(regUser.getId(), qdzTransRecordId, remainderMoney,
							TRADE_TYPE_CREDITOR_TRANSFER_QDZ,
							PlatformSourceEnums.typeByValue(Integer.parseInt(transferSource)));
					List<FinFundtransfer> creditorFundtransferList = initFinTransfers(creditorTradeFlow,
							remainderMoney);
					ResponseEntity<?> responseEntity = BaseUtil
							.getTccProxyBean(FinConsumptionService.class, getClass(), "transferOut")
							.updateAccountInsertTradeAndTransfer(creditorTradeFlow, creditorFundtransferList);
					if (responseEntity.getResStatus() == Constants.ERROR) {
						logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", regUser.getId(),
								responseEntity.getResMsg().toString());
						throw new BusinessException("自动债权释放更新账户,插入交易流水,资金划转失败!");
					}
				}
				// 4、更新账户生成流水及资金划转
				logger.info("钱袋子转出, 用户标识: {}, 金额: {}, 活期转出步骤4,更新乾坤袋账户,插入一条流水，插入一条资金划转", regUser.getId(), transMoney);
				FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(regUser.getId(), qdzTransRecordId, transMoney,
						turnOutType == QdzConstants.QDZ_TURNINOUT_TYPE_INVEST ? TRADE_TYPE_QDZ_TURNS_OUT_INVEST
								: TRADE_TYPE_QDZ_TURNS_OUT,
						PlatformSourceEnums.typeByValue(StringUtils.isBlank(transferSource)
								? PlatformSourceEnums.IOS.getValue() : Integer.parseInt(transferSource)));
				List<FinFundtransfer> transfersList = initFinTransfers(finTradeFlow, regUser.getId(), transMoney,
						payoutMoney);
				ResponseEntity<?> resEntity = BaseUtil
						.getTccProxyBean(FinConsumptionService.class, getClass(), "transferOut")
						.updateAccountInsertTradeAndTransfer(finTradeFlow, transfersList);
				if (resEntity.getResStatus() == Constants.ERROR) {
					logger.error("钱袋子转出, 用户标识: {}, 转出失败: {}", regUser.getId(), resEntity.getResMsg().toString());
					throw new BusinessException("钱袋子转出更新账户,插入交易流水,资金划转失败!");
				}
				// 5、 对债权池数据更新
				logger.info("钱袋子转出, 用户标识: {}, 活期转出步骤5,更新债权池金额: {}", regUser.getId(), transMoney);
				ResponseEntity<?> updateResult = vasRebatesRuleService
						.updateCreditorMoney(transMoney.multiply(new BigDecimal(-1)));
				if (updateResult.getResStatus() == Constants.ERROR) {
					logger.error("钱袋子转出, 用户标识: {}, 活期转出异常: {}", regUser.getId(), updateResult.getResMsg().toString());
					throw new BusinessException(updateResult.getResMsg().toString());
				}
				// 如果转出金额大于待匹配金额，则将已匹配债权金额需要释放的债权,通过MQ进行债权转让
				if (CompareUtil.gtZero(remainderMoney)) {
					logger.info("钱袋子转出, 用户标识: {}, 活期转出步骤6,自动债权转让，债权释放金额: {}", regUser.getId(), remainderMoney);
					// 6、进行转出的债权转让逻缉处理
					jmsService.sendMsg(QdzConstants.MQ_QUEUE_AUTO_CREDITOR_TRANSFER, DestinationType.QUEUE,
							buildQdzAutoCreditorVo(qdzTransRecordId, regUser.getId(), creditorTradeFlow),
							JmsMessageType.OBJECT);
				}
			    //发送站内信，短信
                sendRushSms(regUser,QdzConstants.QDZ_TURN_OUT,transMoney);
			} else {
				logger.error("钱袋子转出, 用户标识: {}, 活期转出异常: {}", regUser.getId(), "当前网络太拥挤，请稍候再试!");
				return new ResponseEntity<>(Constants.ERROR, "当前网络太拥挤，请稍候再试");
			}
		} catch (Exception e) {
			logger.error("钱袋子转出, 用户标识: {}, 活期转出异常信息: ", regUser.getId(), e);
			throw new GeneralException("钱袋子转出失败!");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		ResponseEntity<?> resEntity = new ResponseEntity<>(Constants.SUCCESS, "恭喜您，转出成功！");
		Map<String,Object> resultMap =new HashMap<String,Object>();
		resultMap.put("transActualMoney", transMoney.subtract(payoutMoney));
		resEntity.setParams(resultMap);
		return resEntity;
	}

	/**
	 * @Description :转出自动债权转让，封装MQ处理对象
	 * @Method_Name : buildQdzAutoCreditorVo;
	 * @param qdzTransRecordId
	 *            转出记录ID
	 * @param regUserId
	 *            用户ID
	 * @param creditorTradeFlow
	 *            交易流水
	 * @return
	 * @throws Exception
	 * @return : QdzAutoCreditorVo;
	 * @Creation Date : 2018年3月6日 下午3:24:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private QdzAutoCreditorVo buildQdzAutoCreditorVo(Integer qdzTransRecordId, Integer regUserId,
			FinTradeFlow creditorTradeFlow) throws Exception {
		QdzAutoCreditorVo qdzAutoCreditorVo = new QdzAutoCreditorVo();
		qdzAutoCreditorVo.setQdzTransRecordId(qdzTransRecordId);
		qdzAutoCreditorVo.setFlowId(creditorTradeFlow.getFlowId());
		QdzRateRecord QdzRateRecord = qdzRateRecordService.getQdzRateRecord(new Date());
		qdzAutoCreditorVo.setQdzRateRecordId(QdzRateRecord.getId());
		qdzAutoCreditorVo.setRate(QdzRateRecord.getRate());
		qdzAutoCreditorVo.setRegUserId(regUserId);
		qdzAutoCreditorVo.setTradeType(creditorTradeFlow.getTradeType());
		qdzAutoCreditorVo.setTransMoney(creditorTradeFlow.getTransMoney());
		return qdzAutoCreditorVo;
	}


	/**
	 * @Description : 初始化转入转出处理对象
	 * @Method_Name : initQdzTransferInoutCondition;
	 * @param regUserId
	 *            用户ID
	 * @param preMoney
	 *            交易前金额
	 * @param transMoney
	 *            交易金额
	 * @param transFlag
	 *            交易类型
	 * @param source
	 *            PlatformSourceEnums 交易来源 10-PC 11-IOS 12-Android 13-WAP
	 * @param bugFlag -插入钱袋子账户(QdzConstants.QDZ_INSERT_FALG);1-更新钱袋子账户信息
	 *            (QdzConstants.QDZ_UPDATE_FALG)
	 * @return
	 * @return : QdzTransferInOutCondition;
	 * @Creation Date : 2017年7月18日 上午10:45:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private QdzTransferInOutCondition initQdzTransferInoutCondition(Integer regUserId, BigDecimal preMoney,
			BigDecimal transMoney, TransTypeEnum transFlag, Integer source, Integer bugFlag) {
		QdzTransferInOutCondition qdzTransferInOutCondition = new QdzTransferInOutCondition();
		qdzTransferInOutCondition.setRegUserId(regUserId);
		qdzTransferInOutCondition.setPreMoney(preMoney);
		qdzTransferInOutCondition.setTransMoney(transMoney);
		qdzTransferInOutCondition.setTransFlag(transFlag);
		qdzTransferInOutCondition.setSource(source);
		qdzTransferInOutCondition.setBugFlag(bugFlag);
		return qdzTransferInOutCondition;

	}

	/**
	 * @Description : 初始化自动债权转让的资金划转
	 * @Method_Name : initFinTransfers;
	 * @param creditorTradeFlow
	 *            交易流水
	 * @param remainderMoney
	 *            待释放的债权
	 * @return
	 * @return : List<FinFundtransfer>;
	 * @Creation Date : 2018年3月6日 上午9:40:05;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private List<FinFundtransfer> initFinTransfers(FinTradeFlow creditorTradeFlow, BigDecimal remainderMoney) {
		List<FinFundtransfer> creditorFundtransferList = new LinkedList<FinFundtransfer>();
		FinFundtransfer outFundtransfer = FinTFUtil.initFinFundtransfer(creditorTradeFlow.getFlowId(),
				creditorTradeFlow.getRegUserId(), null, remainderMoney, TRANSFER_SUB_CODE_CREDITOR_TRANSFER_PAY);
		outFundtransfer.setTradeType(TRADE_TYPE_CREDITOR_TRANSFER_QDZ);
		outFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
		FinFundtransfer inFundtransfer = FinTFUtil.initFinFundtransfer(creditorTradeFlow.getFlowId(),
				creditorTradeFlow.getRegUserId(), null, remainderMoney, TRANSFER_SUB_CODE_TURNS_IN_QDZ_CREDITOR);
		inFundtransfer.setTradeType(TRADE_TYPE_CREDITOR_TRANSFER_QDZ);
		inFundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
		creditorFundtransferList.add(outFundtransfer);
		creditorFundtransferList.add(inFundtransfer);
		return creditorFundtransferList;
	}

	/**
	 * @Description : 组装资金划转的对象信息
	 * @Method_Name : initFinTransfers;
	 * @param regUserId
	 *            用户ID
	 * @param transMoney
	 *            交易金额
	 * @param payOutMoney
	 *            支出手续费
	 * @return
	 * @return : List<FinFundtransfer>;
	 * @Creation Date : 2017年7月17日 下午5:09:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private List<FinFundtransfer> initFinTransfers(FinTradeFlow finTradeFlow, Integer regUserId, BigDecimal transMoney,
			BigDecimal payOutMoney) {

		List<FinFundtransfer> finTransfersList = new LinkedList<FinFundtransfer>();
		// 钱袋子转出资金划转
		FinFundtransfer finFundtransfer = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), regUserId, null,
				transMoney, TRANSFER_SUB_CODE_TURNS_OUT);
		finFundtransfer.setTradeType(TRADE_TYPE_QDZ_TURNS_IN);
		finTransfersList.add(finFundtransfer);
		// 如果超出转出次数，则收取手续费，生成两条资金划转（用户支出手续费，平台收入手续费）
		if (payOutMoney != null && CompareUtil.gtZero(payOutMoney)) {
			// 钱袋子转出投资人给平台的手续费
			FinFundtransfer payOutfinFundtransferOut = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
					regUserId, UserConstants.PLATFORM_ACCOUNT_ID, payOutMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.CHARGE));
			payOutfinFundtransferOut.setTradeType(TRADE_TYPE_QDZ_TURNS_OUT);
			finTransfersList.add(payOutfinFundtransferOut);
			// 钱袋子平台收到投资人的转出手续费
			FinFundtransfer payOutfinFundtransferIn = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
					UserConstants.PLATFORM_ACCOUNT_ID, regUserId, payOutMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.CHARGE));
			payOutfinFundtransferIn.setTradeType(TRADE_TYPE_QDZ_TURNS_OUT);
			finTransfersList.add(payOutfinFundtransferIn);
		}
		return finTransfersList;
	}

	@Override
	public ResponseEntity<?> findQdzInfo(Integer regUserId) {
		ResponseEntity<?> qdzInfo = new ResponseEntity<>(Constants.SUCCESS);
        ResponseEntity<?> qdzRule = this.vasRebatesRuleService.checkQdzRule();
        if (qdzRule.getResStatus() == Constants.ERROR) {
            return qdzRule;
        }
        qdzInfo.setParams(getQdzInfo(qdzRule, regUserId));
		return qdzInfo;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseEntity<?> findMyQdzInfo(RegUser regUser) {
		ResponseEntity myQdzInfo = new ResponseEntity<>(Constants.SUCCESS);
		try {
			if(regUser == null ){
				return new ResponseEntity<>(Constants.ERROR, "用户不存在，请重新登录！");
			}
			ResponseEntity qdzRule = this.vasRebatesRuleService.checkQdzRule();
			if (qdzRule.getResStatus() == Constants.ERROR) {
				return qdzRule;
			}
			// 规则
			QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) qdzRule.getParams().get("qdzVasRuleItem");
			// 是否首次购买
			QdzTransRecord qdzTransRecord = new QdzTransRecord();
			qdzTransRecord.setRegUserId(regUser.getId());
			int bugQdzCount = qdzTransRecordService.findQdzTransRecordCount(qdzTransRecord);
			boolean isBugQdzInvest = false;
			if (bugQdzCount > 0) {
				isBugQdzInvest = true;
			}
			Map<String, Object> qdzMap = new HashMap<>();
			// 查询钱袋子利息加息的开关
			QdzRateRecord rateRecord = qdzRateRecordService.findQdzRateRecordByTime(DateUtils.addDays(new Date(), -1));
			BigDecimal yedMillionyuanIncome = BigDecimal.ZERO;// 昨日万元收益
			BigDecimal yedInterest = BigDecimal.ZERO;// 昨日收益利率
			if (rateRecord != null) {
				yedMillionyuanIncome = new BigDecimal(10000).multiply(rateRecord.getRate())
						.divide(new BigDecimal(36000), 2, RoundingMode.DOWN);
				yedInterest = rateRecord.getRate();
			}
			qdzMap.put("yedMillionyuanIncome", yedMillionyuanIncome);
			qdzMap.put("minTransferAmount", qdzVasRuleItem.getInvestLowest());
			qdzMap.put("isBugQdzInvest", isBugQdzInvest);
			QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUser.getId());
			BigDecimal nowMoney = BigDecimal.ZERO;// 钱袋子当前金额
			BigDecimal totalInterest = BigDecimal.ZERO;// 总利息
			BigDecimal interestRate = BigDecimal.ZERO;// 累计加息利率
			BigDecimal raiseInterestRates = BigDecimal.ZERO;// 当天加息利率
			BigDecimal currentIncomeRate = BigDecimal.ZERO;// 当天收益利率
			int interestDay = 0; // 累计加息天数
			if (qdzAccount != null) {
				nowMoney = qdzAccount.getMoney();
				totalInterest = qdzAccount.getTotalInterest();
				interestRate = qdzAccount.getInterestRate();
				yedInterest = qdzAccount.getYedInterest();
				interestDay = qdzAccount.getInterestDay();
				currentIncomeRate = BigDecimal.valueOf(qdzVasRuleItem.getActivityRate()).add(interestRate).add(new BigDecimal(qdzVasRuleItem.getBaseRate()==null ? 0: qdzVasRuleItem.getBaseRate()));
			}
			// 预期今日收益
			qdzMap.put("expectedEarning",
					(nowMoney.multiply(currentIncomeRate)).divide(new BigDecimal(36000), 2, RoundingMode.DOWN));
			// 今日加息收益
			qdzMap.put("raiseInterestRatesEarnings",
					(nowMoney.multiply(raiseInterestRates)).divide(new BigDecimal(36000), 2, RoundingMode.DOWN));
			qdzMap.put("nowMoney", nowMoney);
			qdzMap.put("yedInterest", yedInterest);
			qdzMap.put("allInterest", totalInterest);
			qdzMap.put("interestDay", interestDay);
			qdzMap.put("interestRate", interestRate);
			qdzMap.putAll(getQdzInfo(qdzRule, regUser.getId()));
			myQdzInfo.setParams(qdzMap);
		} catch (Exception e) {
			logger.error("获取我的钱袋子信息失败！", e);
		}
		return myQdzInfo;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseEntity<?> calculateTransferOutFee(RegUser regUser, BigDecimal money, int type) {
		logger.info("用户标识：{}，转出金额{}，转出方式{}", regUser.getId(), money, type);
		ResponseEntity outFeeInfo = new ResponseEntity<>(Constants.SUCCESS);
		Integer state = 0;//判断是否显示提示信息
		try {
			Map<String, Object> outFeeInfoMap = new HashMap<String, Object>();
			QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUser.getId());
			if (qdzAccount == null) {
				return new ResponseEntity<>(Constants.ERROR, "钱袋子账户信息不存在！");
			}
			if (money.divideAndRemainder(new BigDecimal(100))[1].compareTo(BigDecimal.ZERO) != 0) {
				return new ResponseEntity<>(Constants.ERROR, "转出金额不是100的整数倍！");
			}
			// 获取当前钱袋子规则
			VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(
					VasRuleTypeEnum.QDZ.getValue(), VasConstants.VAS_RULE_STATE_START);
			if (vasRebatesRule == null) {
				return new ResponseEntity<>(Constants.ERROR, "系统没有设置规则!");
			}
			QdzVasRuleItem qdzVasRuleItem = JsonUtils.json2Object(vasRebatesRule.getContent(), QdzVasRuleItem.class,
					null);
			// 查询本月转出总次数
			int transferOutSum = qdzTransRecordService.findTransferInTimesOfMonth(regUser.getId(),
					TransTypeEnum.PAYOUT.getValue());
			BidInvest bidInvest = new BidInvest();
			bidInvest.setRegUserId(regUser.getId());
			bidInvest.setInvestType(InvestConstants.BID_INVEST_TYPE_QDZ);
			bidInvest.setCreateTimeBegin(DateUtils.getFirstDayOfMonth(new Date()));
			bidInvest.setCreateTimeEnd(DateUtils.getLastDayOfMonth(new Date()));
			bidInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
			int qdzInvest = bidInvestService.findBidInvestCount(bidInvest);
			BigDecimal outFeeSum = BigDecimal.ZERO;
			BigDecimal qdzOutFee = BigDecimal.ZERO;
			if ((transferOutSum - qdzInvest) >= qdzVasRuleItem.getOutOPPPerMonth()) {
				qdzOutFee = CalcInterestUtil.calFoldAmount(money, BigDecimal.valueOf(qdzVasRuleItem.getOutPayRate()));
			}
			// 计算体现手续费
			BigDecimal cashFee = BigDecimal.ZERO;
			if (type == 2) {
				Map<String, Object> param = new HashMap<>();
				param.put("acceptorUserId", regUser.getId());
				param.put("state", VasCouponConstants.COUPON_DETAIL_SEND_ALREADY);
				param.put("type", VasCouponConstants.COUPON_PRODUCT_TYPE_DEPOSIT);
				int count = vasCouponDetailService.getUserCouponDetailListCount(param);
				if (count == 0) {
					cashFee = BigDecimal.valueOf(1);
				}
			}
			outFeeSum = qdzOutFee.add(cashFee);
			StringBuffer returnMsg = new StringBuffer("本次转出金额：" + money + "元，");
			// 体现转出
			if (CompareUtil.gtZero(qdzOutFee) && CompareUtil.gtZero(cashFee)) {
			    state = 1;
				returnMsg.append("需扣除转出手续费" + qdzOutFee + "元");
				returnMsg.append("和提现手续费" + cashFee + "元，");
			}
			// 仅仅转出可用余额
			if (CompareUtil.gtZero(qdzOutFee) && CompareUtil.eZero(cashFee)) {
			    state = 1;
				returnMsg.append("需扣除转出手续费" + qdzOutFee + "元，");
			}
			// 仅收取体现手续费
			if (CompareUtil.eZero(qdzOutFee) && CompareUtil.gtZero(cashFee) && type != 2) {
			    state = 1;
				returnMsg.append("需扣除提现手续费1元，");
			}
			returnMsg.append("实际到账" + money.subtract(outFeeSum) + "元。");
			outFeeInfoMap.put("state", state);
			outFeeInfo.setParams(outFeeInfoMap);
			outFeeInfo.setResMsg(returnMsg);

		} catch (Exception e) {
			logger.error("获取钱袋子转出手续费:用户标识：{}，转出金额{}，转出方式{}", regUser.getId(), money, type, e);
		}
		return outFeeInfo;
	}

	@Override
	public Map<String, Object> transferOutToBank(RegUser regUser, int source, BigDecimal money, String sign,
			String signType) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			resultMap.put("resStatus", Constants.ERROR);
			FinBankCard finBankCard = new FinBankCard();
			finBankCard.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
			finBankCard.setRegUserId(regUser.getId());
			List<FinBankCard> cards = finBankCardService.findByCondition(finBankCard);
			if (cards == null || cards.isEmpty()) {
				resultMap.put("resMsg", "请先绑定银行卡!");
				return resultMap;
			}
			if (StringUtilsExtend.isEmpty(cards.get(0).getBankProvince())
					|| StringUtilsExtend.isEmpty(cards.get(0).getBankCity())) {
				resultMap.put("resMsg", "请到【我的银行卡】完善【开户城市】信息!");
				return resultMap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<?> autoCreditorMQ(Map<String, Object> map) {
		FinTradeFlow finTradeFlow = null;
		try {
			finTradeFlow = (FinTradeFlow) map.get("creditorTradeFlow");
			logger.info("用户标识: {}, 转出标识: {}, 流水标识: {},释放金额: {}, 自动债权释放MQ放入", map.get("regUserId").toString(),
					map.get("qdzTransRecordId").toString(), finTradeFlow.getFlowId(), finTradeFlow.getTransMoney());
			jmsService
					.sendMsg(QdzConstants.MQ_QUEUE_AUTO_CREDITOR_TRANSFER, DestinationType.QUEUE,
							buildQdzAutoCreditorVo(Integer.parseInt(map.get("qdzTransRecordId").toString()),
									Integer.parseInt(map.get("regUserId").toString()), finTradeFlow),
							JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error("用户标识: {}, 转出标识: {}, 流水标识: {}, 释放金额: {}, 自动债权释放MQ放入失败", map.get("regUserId").toString(),
					map.get("qdzTransRecordId").toString(), finTradeFlow.getFlowId(), finTradeFlow.getTransMoney(),e);
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

    @Override
    public ResponseEntity<?> getQdzUseableAmount(RegUser regUser) {
        ResponseEntity<?> resultEntity = new ResponseEntity<>(Constants.SUCCESS);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try{
            //获取用户账户可用余额
            FinAccount finAccount = finAccountService.findByRegUserId(regUser.getId());
            resultMap.put("useableMoney", finAccount == null ? BigDecimal.ZERO : finAccount.getUseableMoney());        
            //获取当前债权池可投金额
            VasRebatesRule residueBuyCreditor = this.vasRebatesRuleService.findVasRebatesRuleByTypeAndState( VasRuleTypeEnum.CREDITORMONEY.getValue(), VasConstants.VAS_RULE_STATE_START);
            BigDecimal residueBuyAmount = BigDecimal.ZERO;
            if (residueBuyCreditor != null && !StringUtilsExtend.isEmpty(residueBuyCreditor.getContent())) {
                residueBuyAmount = new BigDecimal(residueBuyCreditor.getContent());
            }
            //获取用户可转入金额
            BigDecimal maxTurnInMoney = BigDecimal.ZERO;
            VasRebatesRule vasRebatesRule = this.vasRebatesRuleService.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.QDZ.getValue(), VasConstants.VAS_RULE_STATE_START);
            if (vasRebatesRule == null) {
                logger.error("获取钱袋子信息，系统没有设置规则!");
            }
            QdzVasRuleItem qdzVasRuleItem = JsonUtils.json2Object(vasRebatesRule.getContent(), QdzVasRuleItem.class, null);
            //用户可转入金额
            maxTurnInMoney = (qdzVasRuleItem == null ?BigDecimal.ZERO : qdzVasRuleItem.getInMaxMoneyPPPD());
            //查询用户当天已经转入金额
            BigDecimal currentDayMoney = qdzTransRecordService.findSumTransMoneyOfDay(regUser.getId(), TransTypeEnum.PAYIN.getValue());
            BigDecimal amount = maxTurnInMoney.subtract(currentDayMoney);
            if(amount.compareTo(residueBuyAmount) > 0){
                resultMap.put("turnInMoeny", residueBuyAmount);
            }else{
                resultMap.put("turnInMoeny", amount);
            }
        }catch(Exception e){
            logger.error("用户标识: {}, 获取用户可用余额，及钱袋子可转入金额失败:",regUser.getId(),e);
            resultMap.put("turnInMoeny",BigDecimal.ZERO);
        }
        resultEntity.setParams(resultMap);
        return resultEntity;
    }
    /**
     *  @Description    : 钱袋子转入转出发送站内信，短信
     *  @Method_Name    : sendRushSms;
     *  @param regUser
     *  @param tradeType
     *  @param transMoney
     *  @return         : void;
     *  @Creation Date  : 2018年11月22日 上午11:35:29;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    public void sendRushSms(RegUser regUser,Integer tradeType, BigDecimal transMoney) {
        logger.info("钱袋子转入转出短信，站内信 , 用户id: {}, 转入转出标识: {}, 交易金额: {}",regUser.getId(),tradeType,transMoney);
        String msg = "";
        String msgTitle = "";
        if (QdzConstants.QDZ_TURN_IN == tradeType) {// 没有使用优惠券
            msg = SmsMsgTemplate.MSG_QDZ_TRUN_IN.getMsg();
            msgTitle = SmsMsgTemplate.MSG_QDZ_TRUN_IN.getTitle();
        } else {
            msg = SmsMsgTemplate.MSG_QDZ_TRUN_OUT.getMsg();
            msgTitle = SmsMsgTemplate.MSG_QDZ_TRUN_OUT.getTitle();
        }
        try {
            //发送站内信
            SmsSendUtil.sendSmsMsgToQueue(new SmsWebMsg(regUser.getId(), msgTitle, msg, SmsConstants.SMS_TYPE_NOTICE,
                            new String[] { String.valueOf(transMoney) }));
            //发送短信 钱袋子转入转出金额大于等于10000时才发送短信
            if(transMoney.compareTo(new BigDecimal(100000)) >= 0){
                SmsSendUtil.sendSmsMsgToQueue( new SmsTelMsg(regUser.getId(), regUser.getLogin(), msg,
                        SmsConstants.SMS_TYPE_NOTICE,
                        new String[] { String.valueOf(transMoney) }));
            }
        } catch (Exception e) {
            logger.error("钱袋子转入转出短信，站内信 发送失败, 用户id: {}, 转入转出标识: {}, 交易金额: {}",regUser.getId(),tradeType,transMoney,e);
        }
    }
    /**
     *  @Description    : 获取钱袋子信息及规则信息
     *  @Method_Name    : getQdzInfo;
     *  @param qdzRule
     *  @param regUserId
     *  @return
     *  @return         : Map<String,Object>;
     *  @Creation Date  : 2018年11月30日 下午1:57:56;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    private Map<String, Object> getQdzInfo(ResponseEntity<?> qdzRule,Integer regUserId){
        // 钱袋子规则
        QdzVasRuleItem qdzVasRuleItem = (QdzVasRuleItem) qdzRule.getParams().get("qdzVasRuleItem");
        Map<String, Object> qdzMap = new HashMap<>();
        if (!StringUtilsExtend.isEmpty(qdzRule.getParams().get("qdzRaiseInterest"))) {
            QdzRaiseInterestVo qdzRaiseInterestVo = (QdzRaiseInterestVo) qdzRule.getParams().get("qdzRaiseInterest");
            qdzMap.put("interestDayLimit", qdzRaiseInterestVo.getInterestDayLimit());
            qdzMap.put("raiseInterestLimitAmount", qdzRaiseInterestVo.getRaiseInterestLimit());
            qdzMap.put("raiseInterestRate", qdzRaiseInterestVo.getRaiseInterestRate());
        }
        qdzMap.put("baseRate", qdzVasRuleItem.getBaseRate() == null ? qdzVasRuleItem.getActivityRate()
                : qdzVasRuleItem.getBaseRate());
        qdzMap.put("activityRate",
                qdzVasRuleItem.getActivityRate() == null ? BigDecimal.ZERO : qdzVasRuleItem.getActivityRate());
        BigDecimal currentCreditMoney = qdzVasRuleItem.getResidueBuyAmount();
        qdzMap.put("minTransferAmount", qdzVasRuleItem.getInvestLowest());
        qdzMap.put("state", qdzVasRuleItem.getState());
        qdzMap.put("nextBuyTime", qdzVasRuleItem.getNextBuyTime() == null ? 0 : qdzVasRuleItem.getNextBuyTime());
        qdzMap.put("residueBuyAmount", currentCreditMoney);
        qdzMap.put("systemTime", qdzVasRuleItem.getSystemTime());
        // 初始化
        qdzMap.put("qdzFlag",QdzConstants.QDZ_SHOW_FLAG_TRUE); 
        if(CompareUtil.lteZero(currentCreditMoney)){
            // 是否首次购买
            QdzTransRecord qdzTransRecord = new QdzTransRecord(); 
            qdzTransRecord.setRegUserId(regUserId);
            int bugQdzCount = qdzTransRecordService.findQdzTransRecordCount(qdzTransRecord);
            QdzAccount qdzAccount = qdzAccountService.findQdzAccountByRegUserId(regUserId);
            if(qdzAccount == null || CompareUtil.lteZero(qdzAccount.getMoney()) || bugQdzCount <= 0){
                qdzMap.put("qdzFlag",QdzConstants.QDZ_SHOW_FLAG_FLASE);
            }
            //售罄状态
            qdzMap.put("state", QdzConstants.QDZ_RULR_STATE_SOLDOUT);
        }
        return qdzMap;
    }
}
