package com.hongkun.finance.qdz.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.facade.QdzConsoleFacade;
import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzInterestDayDetailService;
import com.hongkun.finance.qdz.service.QdzInterestDayService;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.qdz.utils.QdzDateUtils;
import com.hongkun.finance.qdz.vo.*;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.service.SmsEmailMsgService;
import com.hongkun.finance.sms.service.SmsTelMsgService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserInfo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserInfoService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.StringUtilsExtend;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_QDZ_DAY_INTEREST;
import static com.hongkun.finance.roster.constants.RosterType.QDZ_CREDITOR_NOTICE;
import static com.hongkun.finance.sms.constants.SmsConstants.SMS_TYPE_SYS_NOTICE;
import static com.hongkun.finance.sms.constants.SmsMsgTemplate.MSG_QDZ_CREDITOR_NOTICE;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class QdzConsoleFacadeImpl implements QdzConsoleFacade {
	private static final Logger logger = LoggerFactory.getLogger(QdzConsoleFacadeImpl.class);
	@Reference
	private QdzInterestDayService qdzInterestDayService;
	@Reference
	private QdzInterestDayDetailService qdzInterestDayDetailService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserInfoService regUserInfoService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private QdzTransRecordService qdzTransRecordService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private SmsEmailMsgService smsEmailMsgService;
	@Reference
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
    private RegUserDetailService regUserDetailService;

	@Override
	public ResponseEntity<?> qdzInterestBalance(Date startTime, Date endTime, Pager pager) {
		ResponseEntity<Pager> responseParam = new ResponseEntity<>(SUCCESS);
		// 返回分页结果map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Pager pagerInfo = null;// 返回pager对象
		List<String> interertDayList = null;
		try {
			List<QdzInterestDetailVo> qdzInterestDetailList = new ArrayList<QdzInterestDetailVo>();
			// 查询钱袋子每日利息，算出有哪几天需要计算利息
			QdzInterestDay qdzInterestDay = new QdzInterestDay();
			qdzInterestDay.setDayBegin(startTime);
			qdzInterestDay.setDayEnd(endTime);
			pagerInfo = qdzInterestDayService.findQdzInterestDayInfo(qdzInterestDay, pager);
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
				List<QdzInterestDay> qdzInterestDayList = (List<QdzInterestDay>) pagerInfo.getData();
				interertDayList = qdzInterestDayList.stream().map(e->DateUtils.format(e.getDay(), DateUtils.DATE)).collect(Collectors.toList());
				for (QdzInterestDay qdzInterestDayVo : qdzInterestDayList) {
				    //组装查询钱袋子明细的集合
				    QdzInterestDetailVo qdzInterestDetailVo = new QdzInterestDetailVo();
					qdzInterestDetailVo.setDay(qdzInterestDayVo.getDay());
					// 查询前一天17：00到今天17：00之间，资金划转类型为乾袋子每日利息收入总和
					FinFundtransfer finFundtransfer = new FinFundtransfer();
					finFundtransfer.setTradeType(TRADE_TYPE_QDZ_DAY_INTEREST);
					finFundtransfer.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.INTEREST));
					finFundtransfer.setCreateTimeBegin(QdzDateUtils.format(qdzInterestDayVo.getDay(),
							DateUtils.DATE_HH_MM_SS, -1, PropertiesHolder.getProperty("qdz_income_time")));
					finFundtransfer.setCreateTimeEnd(QdzDateUtils.format(qdzInterestDayVo.getDay(),
							DateUtils.DATE_HH_MM_SS, 0, PropertiesHolder.getProperty("qdz_income_time")));
					BigDecimal totalMoney = finFundtransferService.findFintransferSumMoney(finFundtransfer);
					qdzInterestDetailVo.setMoney(totalMoney);
					qdzInterestDetailList.add(qdzInterestDetailVo);
				}
				BigDecimal platformMoney = BigDecimal.ZERO;//平台每日利息金额
				BigDecimal totalInterestMoney = BigDecimal.ZERO;//钱袋子每日利息金额
                QdzInterestDetailInfoVo qdzInterestDetailInfoVo = new QdzInterestDetailInfoVo();
                qdzInterestDetailInfoVo.setDayList(interertDayList);
                // 查询利息明细
                List<QdzInterestDayDetail> platInterestDayDetail = qdzInterestDayDetailService
                        .findQdzInterestDayDetailInfo(qdzInterestDetailInfoVo);
                Map<String,List<QdzInterestDayDetail>> map =platInterestDayDetail.stream().collect(Collectors.groupingBy(k->DateUtils.format(k.getDay(), DateUtils.DATE)));
                for (QdzInterestDetailVo interestDetailVo : qdzInterestDetailList) {
                    List<QdzInterestDayDetail> qdzInterestLists =map.get(DateUtils.format(interestDetailVo.getDay(), DateUtils.DATE));
                    for (QdzInterestDayDetail interestDayDetail : qdzInterestLists) {
                        totalInterestMoney = totalInterestMoney.add(interestDayDetail.getInterest());
                        if(UserConstants.PLATFORM_ACCOUNT_ID ==interestDayDetail.getThirdRegUserId()){
                            platformMoney = platformMoney.add(interestDayDetail.getInterest());
                        }
                    }
                    interestDetailVo.setQdzTotalInterestMoney(totalInterestMoney);
                    interestDetailVo.setPlatSumInterestMoney(platformMoney);
                    interestDetailVo.setThirdBusinesSumInterestMoney(totalInterestMoney.subtract(platformMoney));
                    // 将每日明细平台+第三方垫付总利息，与前一天17:00到当天17：00之间，乾袋子用户收入利息总和作比较，判断是否一致
                    interestDetailVo.setIdentical((interestDetailVo.getMoney() == null ? BigDecimal.ZERO : interestDetailVo.getMoney())
                            .compareTo(totalInterestMoney) != 0 ? false : true);
                }
			}
			pagerInfo.setData(qdzInterestDetailList);
			resultMap.put("startTime", startTime);
			resultMap.put("endTime", endTime);
			responseParam.setParams(resultMap);
			responseParam.setResMsg(pagerInfo);
			return responseParam;
		} catch (Exception e) {
			logger.error("qdzInterestBalance: 查询钱袋子利息对账异常, 入参: startTime: {}, endTime: {}, 异常信息: ",startTime,endTime, e);
			return new ResponseEntity<>(ERROR, "查询钱袋子利息出现异常");
		}
	}

	@Override
	public ResponseEntity<?> qdzInterestDetail(Integer userFlag, Date day, Pager pager) {
		// 返回分页结果map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Pager pagerInfo = null;// 返回pager对象
		List<QdzInterestInfoVo> qdzInterestVoList = new ArrayList<QdzInterestInfoVo>();
		try {
			// 查询条件Vo组装
			QdzInterestDetailInfoVo qdzInterestDetailInfoVo = buildQdzInterestDetailInfoVo(day, userFlag);
			// 按条件查询钱袋子利息明细信息
			pagerInfo = qdzInterestDayDetailService.findQdzInterestDetails(qdzInterestDetailInfoVo, pager);
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
				List<QdzInterestDayDetailVo> qdzInterestDetailsList = (List<QdzInterestDayDetailVo>) pagerInfo.getData();
				for (QdzInterestDayDetailVo qdzInterestDayDetail : qdzInterestDetailsList) {
					QdzInterestInfoVo qdzInterestVo = new QdzInterestInfoVo();
					// 查询投资从用户信息
					qdzInterestVo.setUserName(BaseUtil.getRegUserDetail(qdzInterestDayDetail.getRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(qdzInterestDayDetail.getRegUserId())).getRealName());
					qdzInterestVo.setLoginTel(BaseUtil.getRegUser(qdzInterestDayDetail.getRegUserId(), ()-> this.regUserService.findRegUserById(qdzInterestDayDetail.getRegUserId())).getLogin().toString());
					// 两个对象之间相同Bean属性赋值
					ClassReflection.reflectionAttr(qdzInterestDayDetail, qdzInterestVo);
					qdzInterestVo.setDayInterest(qdzInterestDayDetail.getInterest());
					qdzInterestVo.setRate(qdzInterestDayDetail.getRate());
					qdzInterestVoList.add(qdzInterestVo);
				}
			}
			pagerInfo.setData(qdzInterestVoList);
		    ResponseEntity<?> responseParam = new ResponseEntity<>(SUCCESS,pagerInfo);
			resultMap.put("userFlag", userFlag);
			resultMap.put("day", day);
			responseParam.setParams(resultMap);
			return responseParam;
		} catch (Exception e) {
			logger.error("qdzInterestBalance: 查询钱袋子利息对账异常, 入参: userFlag: {}, day: {}, 异常信息: ",userFlag,day, e);
			return new ResponseEntity<>(ERROR, "查询钱袋子利息对账明细异常");
		}
	}

	@Override
	public ResponseEntity<?> findCurrentCreditor() {
		ResponseEntity<?> resultResponseEntity = new ResponseEntity<>(SUCCESS);
		// 返回结果MAP
		Map<String, Object> result = getCurrentCreditorInfos();
		resultResponseEntity.setParams(result);
		return resultResponseEntity;
	}

	/**
	 * 
	 * @Description : 活期当前债券信息
	 * @Method_Name : getCurrentCreditorInfos
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年7月21日 下午4:57:20
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private Map<String, Object> getCurrentCreditorInfos() {
		Map<String, Object> result = new HashMap<String, Object>();
		BigDecimal residueQqMoney = BigDecimal.ZERO;// 第三方剩余债权
		BigDecimal transInSumMoney = BigDecimal.ZERO;// 前一天17:00至当日17:00转入总金额
		BigDecimal transFailSumMoney = BigDecimal.ZERO;// 债权失败总金额
		BigDecimal currentCreditMoney = BigDecimal.ZERO;// 查询当前债权池金额
		BigDecimal hqRepaySumMoney = BigDecimal.ZERO;// 还款本金总额
		try {
			// 前一天17:00至当日17:00转入总金额
			transInSumMoney = qdzTransRecordService.findInvestSumMoney(
					QdzDateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS, -1,
							PropertiesHolder.getProperty("qdz_income_time")),
					QdzDateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS, 0,
							PropertiesHolder.getProperty("qdz_income_time")),
					TransTypeEnum.PAYIN, QdzConstants.TRANS_RECORD_SUCCSS);
			// 债权失败总金额
			transFailSumMoney = qdzAccountService.findSumFailICreditorMoney(null);
			// 查询当前债权池金额
			VasRebatesRule vasRebatesRule = vasRebatesRuleService
					.findVasRebatesRuleByTypeAndState(VasRuleTypeEnum.CREDITORMONEY.getValue(), 1);
			currentCreditMoney = (vasRebatesRule == null ? BigDecimal.ZERO
					: new BigDecimal(vasRebatesRule.getContent()));
			// 查询活期标的待还款本金
			List<BidInfo> bidInfoList = bidInfoService.findBidInfoList(BID_PRODUCT_CURRENT, BID_STATE_WAIT_REPAY);
			BigDecimal investTotalMoney = BigDecimal.ZERO;
			BigDecimal investTotalTransMoney = BigDecimal.ZERO;//已转让债券
			for (BidInfo bidInfo : bidInfoList) {
				// 活期标的投资总金额(invest_channel=3)
				Map<String, Object> bidSumInvest = bidInvestService.findSumAmountByBidId(bidInfo.getId());
				if(bidSumInvest!=null) {
					investTotalTransMoney = (BigDecimal) bidSumInvest.get("investTransAmount1");
					investTotalMoney = (BigDecimal) bidSumInvest.get("investAmountChannel1");
					investTotalMoney = investTotalMoney.subtract(investTotalTransMoney);//实际持有债券
					residueQqMoney = residueQqMoney.add(investTotalMoney);
				}
				// 查询某个标的的最后一次还款时间,统计待还款本金
				Date lastRepayTime = DateUtils.addMonth(bidInfo.getLendingTime(), bidInfo.getTermValue());
				if (lastRepayTime != null) {
					int days = DateUtils.getDaysBetween(lastRepayTime, new Date());
					// 如果标的还款日期是今天的话，则统计
					if (days == 0) {
						hqRepaySumMoney = hqRepaySumMoney.add(bidInfo.getTotalAmount());
					}
				}
			}
		} catch (Exception e) {
			logger.info("当前债权查询失败：",e);
		}
		BigDecimal lackCreditorMoney = residueQqMoney.subtract(transFailSumMoney).subtract(hqRepaySumMoney);
		result.put("residueQqMoney", residueQqMoney);// 第三方剩余债权
		result.put("transFailSumMoney", transFailSumMoney);// 债权失败总金额
		result.put("transInSumMoney", transInSumMoney);// 前一天17:00至当日17:00转入总金额
		result.put("currentCreditMoney", currentCreditMoney);// 债权池限额
		result.put("hqWaitSumMoney", hqRepaySumMoney);// 还款本金
		result.put("lackCreditorMoney",CompareUtil.gteZero(lackCreditorMoney)? BigDecimal.ZERO:lackCreditorMoney);// 当前缺少债权
		return result;
	}

	@Override
	public ResponseEntity<?> findCreditorBalanceInfo(String bidName,Pager pager) {
		// 返回结果MAP
		Map<String, Object> result = new HashMap<String, Object>();
		// 存放钱袋子债权对账信息的集合
		List<QdzCreditorBalanceVo> qdzCreditorBanlaceList = new ArrayList<QdzCreditorBalanceVo>();
		Pager pagerInfo =null;
		try {
			// 根据产品类型&状态&标的名称，获取标的信息
			List<Integer> integerStates = new ArrayList<Integer>();
			integerStates.add(BID_STATE_WAIT_REPAY);//仅查询还款中的标
			if(!StringUtilsExtend.isEmpty(bidName)) {
				integerStates.add(BID_STATE_WAIT_FINISH);
			}
	         pagerInfo = bidInfoService.findBidInfoList(BID_PRODUCT_CURRENT, integerStates, bidName, pager);
	         List<BidInfo> bidInfoList= (List<BidInfo>) pagerInfo.getData();
			List<Integer> investState = new ArrayList<Integer>();
			for (BidInfo bidInfo : bidInfoList) {
				investState.add(INVEST_STATE_SUCCESS);// 投资成功
				investState.add(INVEST_STATE_AUTO);
				// 查询对应活期标的投资记录， 获取第三方投资记录
				BidInvest thirdBiddInvest = bidInvestService.findInvestRecord(bidInfo.getId(), investState);
				// 第三方投资金额
				BigDecimal thirdInvestMoney = (thirdBiddInvest !=null?thirdBiddInvest.getInvestAmount():BigDecimal.ZERO);
				//第三方释放的债权金额		
				BigDecimal thirdCreditorMoney = (thirdBiddInvest !=null? thirdBiddInvest.getTransAmount(): BigDecimal.ZERO);
				// 活期标的投资总金额(invest_channel=3)
				Map<String, Object> bidSumInvest = bidInvestService.findSumAmountByBidId(bidInfo.getId());
				BigDecimal investTotalMoney = BigDecimal.ZERO;
				BigDecimal investTotalTransMoney = BigDecimal.ZERO;//已转让债券
				if(bidSumInvest!=null) {
					investTotalTransMoney = (BigDecimal) bidSumInvest.get("investTransAmount3");
					investTotalMoney = (BigDecimal) bidSumInvest.get("investAmountChannel3");
					investTotalMoney = investTotalMoney.subtract(investTotalTransMoney);//实际持有债券
				}
				// 组装前台展示列表
				QdzCreditorBalanceVo qdzCreditorBalanceVo = new QdzCreditorBalanceVo();
				qdzCreditorBalanceVo.setBidId(bidInfo.getBidCode());
				qdzCreditorBalanceVo.setBidName(bidInfo.getName());
				qdzCreditorBalanceVo.setBidAmount(bidInfo.getTotalAmount());
				//第三方持有债权金额
				qdzCreditorBalanceVo.setThirdInvestAtm(thirdInvestMoney.subtract(thirdCreditorMoney));
				qdzCreditorBalanceVo.setUserInvestAtm(investTotalMoney);
				qdzCreditorBalanceVo.setInvestAtm(qdzCreditorBalanceVo.getThirdInvestAtm().add(qdzCreditorBalanceVo.getUserInvestAtm()));
				qdzCreditorBalanceVo.setStatus(bidInfo.getState());
				if (CompareUtil.eq(qdzCreditorBalanceVo.getBidAmount(), qdzCreditorBalanceVo.getInvestAtm())) {
					qdzCreditorBalanceVo.setIsEqual(1);// 代表一致
				}else {
					qdzCreditorBalanceVo.setIsEqual(0);// 代表不一致
				}
//				} else if (BID_STATE_WAIT_FINISH == bidInfo.getState() || BID_STATE_WAIT_REPAY == bidInfo.getState()) {
//					// 如果标的状态为还款中或者是已完成，并且投资总和与标的金额不相等，则认为不一致
//					qdzCreditorBalanceVo.setIsEqual(0);// 代表不一致
//				} else {
//					qdzCreditorBalanceVo.setIsEqual(2);// 其它，展示标的状态
//				}
				qdzCreditorBanlaceList.add(qdzCreditorBalanceVo);
			}
		} catch (Exception e) {
			logger.error("findCreditorBalanceInfo: 查询债权对账异常, 入参: bidName: {}, 异常信息: ", bidName, e);
			return new ResponseEntity<>(ERROR, "查询债权对账异常");
		}
	    pagerInfo.setData(qdzCreditorBanlaceList);
	    ResponseEntity<?> resultResponseEntity = new ResponseEntity<>(SUCCESS,pagerInfo);
		result.put("bidName", bidName);
		resultResponseEntity.setParams(result);
		return resultResponseEntity;
	}

	@Override
	public ResponseEntity<?> findQdzBillWater(QdzTransferRecordVo qdzTransferRecordVo, Pager pager) {
		// 存放钱袋子交易账单流水的集合
		List<QdzTransferRecordVo> qdzTransRecordVoList = new ArrayList<QdzTransferRecordVo>();
		// 返回结果MAP
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询条件Vo
		QdzTransferInfo qdzTransferInfo = new QdzTransferInfo();
		Pager pagerInfo = null;// 返回的pager对象
		try {
			// 如果用户名和手机号有一个不为空，则先查询用户信息
			if (StringUtils.isNotBlank(qdzTransferRecordVo.getLoginTel())
					|| StringUtils.isNotBlank(qdzTransferRecordVo.getUserName())) {
				UserVO userVO = new UserVO();
				userVO.setRealName(qdzTransferRecordVo.getUserName());
				if(StringUtils.isNotBlank(qdzTransferRecordVo.getLoginTel())){
				    userVO.setLogin(Long.parseLong(qdzTransferRecordVo.getLoginTel())); 
				}
				List<Integer> userIdList = regUserService.findUserIdsByUserVO(userVO);
				qdzTransferInfo.setRegUserIdList(userIdList);
			}			
			ClassReflection.reflectionAttr(qdzTransferRecordVo,qdzTransferInfo);
			// 调用接口查询账单流水信息
			pagerInfo = qdzTransRecordService.findQdzTransferRecordList(qdzTransferInfo, pager);
			// 用户拼接前台要显示的数据集合
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
				List<QdzTransRecord> recordList = (List<QdzTransRecord>) pagerInfo.getData();
				for (QdzTransRecord qdzTransRecord : recordList) {
					QdzTransferRecordVo qdzRecordVo = new QdzTransferRecordVo();
					qdzRecordVo.setUserName(BaseUtil.getRegUserDetail(qdzTransRecord.getRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(qdzTransRecord.getRegUserId())).getRealName());
					qdzRecordVo.setLoginTel(BaseUtil.getRegUser(qdzTransRecord.getRegUserId(), ()-> this.regUserService.findRegUserById(qdzTransRecord.getRegUserId())).getLogin().toString());
					// 两个对象之间相同Bean属性赋值
					ClassReflection.reflectionAttr(qdzTransRecord, qdzRecordVo);
					qdzTransRecordVoList.add(qdzRecordVo);
				}
			}
			pagerInfo.setData(qdzTransRecordVoList);
		} catch (Exception e) {
			logger.error("qdzInterestBalance: 钱袋子账单流水, 入参: QdzTransferRecordVo: {}, 异常信息: ",
					qdzTransferRecordVo.toString(), e);
			return new ResponseEntity<>(ERROR, "钱袋子账单流水查询失败");
		}
		result.put("qdzTransferRecordVo", qdzTransferRecordVo);
		ResponseEntity<?> resultResponseEntity = new ResponseEntity<>(SUCCESS, pagerInfo);
		resultResponseEntity.setParams(result);
		return resultResponseEntity;
	}

	@Override
	public ResponseEntity<?> findHqProductInfo(String bidName, Pager pager) {
		// 存放活期产品信息集合
		List<QdzHqProductInfoVo> qdzHqProductInfoVoList = new ArrayList<QdzHqProductInfoVo>();
		Pager pagerInfo = null;// 返回的pager对象
		try {
			// 组装标的状态，用于查询
			List<Integer> bidStateList = new ArrayList<Integer>();
			bidStateList.add(BID_STATE_WAIT_REPAY);// 还款中
			bidStateList.add(BID_STATE_WAIT_FINISH);// 已完成
			// 根据产品类型&状态&标的名称，获取标的信息
			pagerInfo = bidInfoService.findBidInfoList(BID_PRODUCT_CURRENT, bidStateList, bidName, pager);
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
				List<BidInfo> bidInfoList = (List<BidInfo>) pagerInfo.getData();
				for (BidInfo bidInfo : bidInfoList) {
					// 查询某个标的的最后一次还款时间
					Date lastRepayTime = bidRepayPlanService.findLastRepayPlanTime(bidInfo.getId());
					if (lastRepayTime == null) {
						continue;
					}
					// 组装要显示的信息
					QdzHqProductInfoVo qdzHqProductInfoVo = new QdzHqProductInfoVo();
					qdzHqProductInfoVo.setBidName(bidInfo.getName());
					qdzHqProductInfoVo.setBorrowMoney(bidInfo.getTotalAmount());
					qdzHqProductInfoVo.setRepayPrincipalTime(lastRepayTime);
					// 如果是还款中的标，则计算当前时间与还款时间相差多少天，如果是已完成的标，则认为还本剩余时间为0
					if (bidInfo.getState() == BID_STATE_WAIT_REPAY) {
						int days = DateUtils.getDaysBetween(lastRepayTime, new Date());
						qdzHqProductInfoVo.setRepaySurplusDays(days);
					} else {
						qdzHqProductInfoVo.setRepaySurplusDays(0);
					}
					qdzHqProductInfoVoList.add(qdzHqProductInfoVo);
				}
			}
		} catch (Exception e) {
			logger.error("findHqProductInfo: 查询钱袋子活期产品, 入参: bidName: {}, 异常信息: ", bidName, e);
			return new ResponseEntity<>(ERROR, "查询钱袋子活期产品出现异常！");
		}
		pagerInfo.setData(qdzHqProductInfoVoList);

		return new ResponseEntity<>(SUCCESS, pagerInfo);
	}

	@Override
	public ResponseEntity<?> creditorNotice() {
		Map<String, Object> result = getCurrentCreditorInfos();
		if (result == null) {
			return BaseUtil.error("债券查询异常！");
		}
		BigDecimal lackCreditorMoney = (BigDecimal) result.get("lackCreditorMoney");
		if (!CompareUtil.gtZero(lackCreditorMoney)) {
			RosInfo rosInfoCdt = new RosInfo();
			rosInfoCdt.setType(QDZ_CREDITOR_NOTICE.getValue());
			List<RosInfo> infos = rosInfoService.findRosInfoList(rosInfoCdt);
			RegUserInfo regUserInfo = null;
			StringBuffer emails = new StringBuffer();
			List<SmsTelMsg> smsTelMsgs = new ArrayList<>();
			for (RosInfo rosInfo : infos) {
				final RegUser regUser = BaseUtil.getRegUser(rosInfo.getRegUserId(),
						() -> this.regUserService.findRegUserById(rosInfo.getRegUserId()));
				if (regUser == null) {
					continue;
				}
				regUserInfo = BaseUtil.getRegUserInfo(regUser.getId(),
						() -> this.regUserInfoService.findRegUserInfoByRegUserId(regUser.getId()));
				if (regUserInfo != null && StringUtilsExtend.isEmpty(regUserInfo.getEmail())) {
					emails.append(regUserInfo.getEmail()).append(",");
				}
				SmsTelMsg smsTelMsg = new SmsTelMsg();
				smsTelMsg.setCreateTime(new Date());
				smsTelMsg.setRegUserId(regUser.getId());
				smsTelMsg.setInfo("债券不足通知");
				smsTelMsg.setType(SMS_TYPE_SYS_NOTICE);
				smsTelMsg.setMsg(MSG_QDZ_CREDITOR_NOTICE.getMsg());
				smsTelMsg.setTel(regUser.getLogin());
				smsTelMsgs.add(smsTelMsg);
			}
			SmsEmailMsg smsEmailMsg = new SmsEmailMsg();
			smsEmailMsg.setCreateTime(new Date());
			smsEmailMsg.setEmail(emails.toString());
			smsEmailMsg.setModifyTime(new Date());
			smsEmailMsg.setMsg(MSG_QDZ_CREDITOR_NOTICE.getMsg());
			smsEmailMsg.setTitle(MSG_QDZ_CREDITOR_NOTICE.getTitle());
			smsEmailMsg.setType(SMS_TYPE_SYS_NOTICE);
			smsEmailMsgService.insertSmsEmailMsg(smsEmailMsg);
			smsTelMsgService.insertSmsTelMsgBatch(smsTelMsgs, smsTelMsgs.size());
		}
		return new ResponseEntity<>(SUCCESS, "债券通知成功！");
	}

	@Override
	public ResponseEntity<?> findQdzInterestInfo(QdzInterestInfoVo qdzInterestInfoVo, Pager pager) {
		List<QdzInterestInfoVo> qdzInterestDetailList = new ArrayList<QdzInterestInfoVo>();
		Pager pagerInfo = null;
		try {
			QdzInterestDay qdzInterestDay = new QdzInterestDay();
			if (StringUtils.isNoneBlank(qdzInterestInfoVo.getStartTime())) {
				qdzInterestDay.setDayBegin(DateUtils.parse(qdzInterestInfoVo.getStartTime(), DateUtils.DATE));
			}
			if (StringUtils.isNoneBlank(qdzInterestInfoVo.getEndTime())) {
				qdzInterestDay.setDayEnd(DateUtils.parse(qdzInterestInfoVo.getEndTime(), DateUtils.DATE));
			}
			// 两个对象之间相同Bean属性赋值
			ClassReflection.reflectionAttr(qdzInterestInfoVo, qdzInterestDay);
			pagerInfo = qdzInterestDayService.findQdzInterestDayInfo(qdzInterestDay, pager);
			// 用户拼接前台要显示的数据集合
			if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
				List<QdzInterestDay> interestDayList = (List<QdzInterestDay>) pagerInfo.getData();
				for (QdzInterestDay interestDay : interestDayList) {
					QdzInterestInfoVo interestInfoVo = new QdzInterestInfoVo();
					interestInfoVo.setUserName(BaseUtil.getRegUserDetail(interestDay.getRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(interestDay.getRegUserId())).getRealName());
					interestInfoVo.setLoginTel(BaseUtil.getRegUser(interestDay.getRegUserId(), ()-> this.regUserService.findRegUserById(interestDay.getRegUserId())).getLogin().toString());
					// 两个对象之间相同Bean属性赋值
					ClassReflection.reflectionAttr(interestDay, interestInfoVo);
					qdzInterestDetailList.add(interestInfoVo);
				}
			}
			pagerInfo.setData(qdzInterestDetailList);
		} catch (Exception e) {
			logger.error("findQdzInterestInfo: 查询钱袋子每日利息, 入参: qdzInterestInfoVo: {}, 异常信息: ",qdzInterestInfoVo.toString(), e);
			return new ResponseEntity<>(ERROR, "查询钱袋子每日利息出现异常");
		}
		return new ResponseEntity<>(SUCCESS, pagerInfo);

	}

	/**
	 * @Description : 组装查询钱袋子利息明细的VO
	 * @Method_Name : buildQdzInterestDetailInfoVo;
	 * @param day
	 * @param userFlag
	 * @return
	 * @return : QdzInterestDetailInfoVo;
	 * @Creation Date : 2017年7月25日 下午3:48:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private QdzInterestDetailInfoVo buildQdzInterestDetailInfoVo(Date day, Integer userFlag) {
		QdzInterestDetailInfoVo qdzInterestDetailInfo = new QdzInterestDetailInfoVo();
		qdzInterestDetailInfo.setDay(day);
		qdzInterestDetailInfo.setUserFlag(userFlag);
		qdzInterestDetailInfo.setThirdRegUserId(UserConstants.PLATFORM_ACCOUNT_ID);
		return qdzInterestDetailInfo;
	}

}
