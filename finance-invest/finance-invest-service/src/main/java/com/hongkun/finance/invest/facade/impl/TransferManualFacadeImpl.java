package com.hongkun.finance.invest.facade.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hongkun.finance.contract.constants.ContractConstants;
import com.hongkun.finance.contract.model.ConInfoSelfGenerateDTO;
import com.hongkun.finance.contract.service.ConInfoService;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.BidTransferManual;
import com.hongkun.finance.invest.model.vo.*;
import com.hongkun.finance.invest.service.BidInfoDetailService;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.service.BidTransferManualService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.constants.RepayConstants;
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
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.contract.constants.ContractConstants.*;
import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.hongkun.finance.loan.constants.RepayConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.hongkun.finance.user.constants.UserConstants.PLATFORM_ACCOUNT_ID;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class TransferManualFacadeImpl implements TransferManualFacade {

	private final Logger logger = LoggerFactory.getLogger(TransferManualFacadeImpl.class);

	@Reference
	private BidTransferManualService bidTransferManualService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private ConInfoService conInfoService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;

	@Override
	public ResponseEntity<?> showTransferManualDetailByInvestId(Integer investId) {
		TransferManualAppPreVo transferDetail =  bidTransferManualService.findBidTransferDetailByInvestId(investId);
		if (transferDetail == null) {
			return new ResponseEntity<String>(Constants.ERROR, "未查询到债权信息");
		}
		//计算持有金额 、可转本金、已收收益、未收收益
		Map<String,Object> map = bidReceiptPlanService.findTransferManualDetail(transferDetail.getBidId(),investId);
		Date endDate = (Date) map.get(REPAY_END_DATE);
		Date nextDate = (Date) map.get("nextDate");
		transferDetail.setEndDate(DateUtils.format(endDate,"yyyy-MM-dd"));
		transferDetail.setReceivedIncome((BigDecimal) map.get(RECEIVED_INTEREST));
		transferDetail.setNotReceivedIncome((BigDecimal) map.get(NOT_RECEIVED_INTEREST));
		//持有金额=投资记录金额-已转让金额（在sql中计算）
		//可转本金 = 持有金额-转让中金额
		BigDecimal transferingAmount = bidTransferManualService.findSumTransferedMoney(investId);
		transferDetail.setCreditorAmount(transferDetail.getHoldAmount().subtract(transferingAmount));
		//查询最大筹集天数
		BidInfoDetail bidDetail = bidInfoDetailService.findBidInfoDetailByBidId(transferDetail.getBidId());
		int maxTransferDays = bidDetail!=null && bidDetail.getTransferDays()>0?bidDetail.getTransferDays():0;
		if (nextDate != null ){
			int betweenNextDate = DateUtils.getDaysBetween(new Date(),nextDate);
			maxTransferDays = maxTransferDays < betweenNextDate? maxTransferDays:betweenNextDate;
		}
		transferDetail.setMaxTransferDays(maxTransferDays);
		if (bidDetail != null ){
			transferDetail.setCreditorState(bidDetail.getCreditorState());
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("data",transferDetail);
		return new ResponseEntity<>(SUCCESS,"查询债权成功", params);
	}

	@Override
	public ResponseEntity<?> showTransferManualDetailByTransferId(Integer transferId,Integer regUserId) {
		TransferManualDetailAppVo transferDetail = bidTransferManualService.findBidTransferDetailByTransferId(transferId);
		if (transferDetail != null) {
			Map<String,Object> map = bidReceiptPlanService.findTransferManualDetail(transferDetail.getBidId(),transferDetail.getInvestId());
			Date endDate = (Date) map.get(REPAY_END_DATE);
			transferDetail.setEndDate(DateUtils.format(endDate,"yyyy-MM-dd"));
			if (endDate.before(new Date())){
				transferDetail.setBuyerHoldDays(0);
			}else{
				transferDetail.setBuyerHoldDays(DateUtils.getDaysBetween(new Date(),endDate));
			}
			BigDecimal receivedInterest = (BigDecimal) map.get(RECEIVED_INTEREST);
			//计算转让后转让人年化率
			Map<String,Object> rateParam =  null;
			String viewOriginProjectSwitch =  "0";
			BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(transferDetail.getBidId());
			if(transferDetail.getState() == INVEST_TRANSFER_MANUAL_STATE_IN){
				rateParam = calTransferedRateForTransfering(transferDetail.getTermUnit(), transferDetail.getTermValue(), transferDetail.getInvestId(),
						transferDetail.getInterestRate(), transferDetail.getCreditorAmount(), receivedInterest, transferDetail.getTransferPrice());
				BigDecimal buyerExpectAmount = ((BigDecimal)rateParam.get("buyerSumInterest"));
				transferDetail.setExpectAmount(buyerExpectAmount);
//				transferDetail.setBuyerRate((BigDecimal)rateParam.get("buyerRate"));
				transferDetail.setBuyerRate(bidInfoVO.getInterestRate());
			}else{
				//预期收益
				BigDecimal sumInterest =  bidReceiptPlanService.findSumInterestByInvestId(transferDetail.getNewInvestId());
				transferDetail.setExpectAmount(sumInterest);
				viewOriginProjectSwitch =  bidInfoService.getViewOriginProjectSwitch(bidInfoVO);
			}
			transferDetail.setTransferEndTime(DateUtils.addDays(transferDetail.getCreateTime(),transferDetail.getTransferDays()));
			transferDetail.setCurrentTime(new Date());
			//还款方式
			String bidRepayMentWayShowName = dicDataService.findNameByValue("invest","bid_repayment",transferDetail.getBiddRepaymentWay());
			transferDetail.setBiddRepaymentWayShow(bidRepayMentWayShowName);
			//放款时间到年月日
			transferDetail.setLendingTime(transferDetail.getLendingTime().substring(0,10));
			//查询可用余额
			FinAccount account = finAccountService.findByRegUserId(regUserId);
			transferDetail.setShowConInfoFlag(Integer.parseInt(viewOriginProjectSwitch));
			Map<String,Object> params = new HashMap<String,Object>();
			//剩余期限 如果剩余期限小于0表示已过期
			int resuTransferDays = transferDetail.getTransferDays() - DateUtils.getDaysBetween(new Date(),transferDetail.getCreateTime());
			transferDetail.setResuTransferDays(resuTransferDays>0?resuTransferDays:0);
			params.put("transferDetail", transferDetail);
			params.put("conInfos",conInfoService.initContracts(bidInfoVO.getContract(),ContractConstants.CONTRACT_LOCATION_INVEST));
			//TODO 为了pc页面展示，app设计好之后提示前端此处做相应修改
//			params.put("usableMoney",account.getUseableMoney());
//			params.put("account",account);
//			if(transferDetail.getBiddRepaymentWay() == InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
//				params.put("alertMsg","此债权还款方式为：到期一次还本付息（即到期后一次性返还本金和收益）");
//			}else{
//				params.put("alertMsg","");
//			}
			if (transferDetail.getState()==InvestConstants.INVEST_TRANSFER_MANUAL_STATE_OVER){
				BidTransferManual manual = bidTransferManualService.findBidTransferManualById(transferId);
				TransferManualRecordVo recordVo = new TransferManualRecordVo();
				recordVo.setBuyerTime(manual.getModifyTime());
				recordVo.setCreditorAmount(manual.getCreditorAmount());
				recordVo.setTransferId(transferId);
				RegUserDetail userDetail = regUserDetailService.findRegUserDetailByRegUserId(manual.getBuyUserId());
				String realName = userDetail!=null && StringUtils.isNotBlank(userDetail.getRealName())?userDetail.getRealName().substring(0,1)+"**":"";
				recordVo.setBuyerName(realName);
				params.put("record",recordVo);
			}

			return new ResponseEntity<>(Constants.SUCCESS,"查询转让详情成功",params);
		}
		return new ResponseEntity<>(Constants.ERROR, "未查询到债权信息");
	}
	/**
	 * 计算转让后收益率（针对转让中）
	 * @param termUnit  标的期限单位
	 * @param termValue 标的期限值
	 * @param investId 投资记录id
	 * @param rate      标的利率
	 * @param investAmount  投资金额
	 * @param receivedIncome 已收金额
	 * @param transferPrice 转让价格
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @return  transferedRate 转让人转让后利率
	 *          buyerRate      购买人转让后利率
	 *          buyerSumInterest 购买人总利息
	 *          thisBatchInterest  转让人本期利息
	 *          buyerThisBachInterest 购买人本期利息
	 * @Creation Date  ：2018/4/23
	 * @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	 */
	private Map<String, Object> calTransferedRateForTransfering(int termUnit,int termValue,int investId,
			BigDecimal rate,BigDecimal investAmount,BigDecimal receivedIncome,BigDecimal transferPrice) {
		Map<String,Object> resultMap = new HashMap<String,Object>();

		Map<String,Object> planResult = bidReceiptPlanService.findParamsForTransferInterestRate(investId);
		BidReceiptPlan nextPlan  = (BidReceiptPlan) planResult.get(RECEIPT_NEXT_PLAN);
		BidReceiptPlan lastPlan  = (BidReceiptPlan) planResult.get(RECEIPT_LAST_PLAN);
		BigDecimal notReceivedIncome = (BigDecimal) planResult.get(NOT_RECEIVED_INTEREST);

		BidInvest bidInvest = bidInvestService.findBidInvestById(investId);
		//债权人剩余本金
		BigDecimal investCreditorAmount = bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount()).subtract(investAmount);
		BidInfo bidInfo = bidInfoService.findBidInfoById(bidInvest.getBidInfoId());
		//购买人持有天数
		int buyerThisBatchDays = DateUtils.getDaysBetween(new Date(), nextPlan.getPlanTime());
		int transferHoldDays = 0;
		int buyerHoldDays = 0;
		BigDecimal thisBatchInterest = BigDecimal.ZERO;
		BigDecimal buyerThisBachInterest = BigDecimal.ZERO;
		BigDecimal buyerSumInterest = BigDecimal.ZERO;
		//如果是月标并且购买人持有天数小于30天    或者是天标，按天计算债权人当期利息
		if(termUnit==2){
			int month = 0 ;
			if(bidInfo.getBiddRepaymentWay()==InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
				//1\相差月份
				month = DateUtils.getMonthBetween(bidInfo.getLendingTime(),new Date());
				if (month==0){
					month = 1;
				}
				if (DateUtils.getDays(DateUtils.addMonth(bidInfo.getLendingTime(),month),new Date())<0){
					month = month +1;
				}
				//当前期还款时间
				Date thisBatchRepayTime = DateUtils.addMonth(bidInfo.getLendingTime(),month);
				buyerThisBatchDays = DateUtils.getDaysBetween(new Date(), thisBatchRepayTime);
			}

			if(buyerThisBatchDays<30){
				//购买人当前期利息 = 投资金额*年化率*当前期持有天数/360
				buyerThisBachInterest = investAmount.multiply(rate).multiply(new BigDecimal(buyerThisBatchDays))
						.divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
				if(bidInfo.getBiddRepaymentWay()==InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
					buyerHoldDays = (bidInfo.getTermValue()-month)*30 + buyerThisBatchDays;
					transferHoldDays = (month-1)*30 + 30 - buyerThisBatchDays;
					buyerSumInterest = buyerThisBachInterest.add(investAmount.multiply(rate)
							.multiply(new BigDecimal(bidInfo.getTermValue()-month))
							.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP));
					thisBatchInterest = nextPlan.getInterestAmount().subtract(buyerSumInterest);
				}else{
					//债权人当前期利息 = 本期总利息 - 购买人当前期利息
					thisBatchInterest = nextPlan.getInterestAmount().subtract(buyerThisBachInterest);
					//债权人持有总天数 = 30 - 购买人本期持有天数 + 已回款期数*30
					transferHoldDays = 30 - buyerThisBatchDays;
					//购买人持有天数 = 购买人本期天数 + 未回款期数*30（未回款不包括本期）
					buyerHoldDays = (lastPlan.getPeriods()-nextPlan.getPeriods())*30+buyerThisBatchDays;
					//购买人总利息 = 本期利息 + 未回款利息
					BigDecimal buyerNotReceivedIncome = investAmount.multiply(rate)
							.multiply(new BigDecimal(bidInfo.getTermValue()-nextPlan.getPeriods()))
							.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP);
					buyerSumInterest = buyerThisBachInterest.add(buyerNotReceivedIncome);
				}
			}else{
				//本期利息全部归购买人所有
				buyerThisBachInterest = investAmount.multiply(rate)
						.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP);
				if(bidInfo.getBiddRepaymentWay()==InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
					buyerHoldDays = (bidInfo.getTermValue()-month+1)*30;
					transferHoldDays = (month-1)*30;
					buyerSumInterest = investAmount.multiply(rate)
							.multiply(new BigDecimal(bidInfo.getTermValue()-month+1))
							.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP);
					thisBatchInterest = nextPlan.getInterestAmount().subtract(buyerSumInterest);
				}else{
					thisBatchInterest = nextPlan.getInterestAmount().subtract(buyerThisBachInterest);
					buyerHoldDays = (lastPlan.getPeriods()-nextPlan.getPeriods()+1)*30;
					//购买人总利息 = 本期利息 + 未回款利息
					buyerSumInterest = investAmount.multiply(rate)
							.multiply(new BigDecimal(bidInfo.getTermValue()-nextPlan.getPeriods()))
							.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP).add(buyerThisBachInterest);
				}
			}
		}else if(termUnit==3){
			buyerSumInterest = investAmount.multiply(rate).multiply(new BigDecimal(buyerThisBatchDays))
					.divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
			buyerThisBachInterest = buyerSumInterest;
			thisBatchInterest = nextPlan.getInterestAmount().subtract(buyerSumInterest);
			transferHoldDays = termValue - buyerThisBatchDays;
			buyerHoldDays = buyerThisBatchDays;
		}
//		transferHoldDays = transferHoldDays==0?1:transferHoldDays;
		//债权人利息= 已收利息 + 当期利息 + 转让价格-本金
//		BigDecimal sumInterest = receivedIncome.add(thisBatchInterest).add(transferPrice).subtract(investAmount);

//		//转让人利率 = (总利息*360)/(投资金额*持有天数)
//		BigDecimal transferedRate = sumInterest.multiply(new BigDecimal(360))
//				.divide(investAmount.multiply(new BigDecimal(transferHoldDays)),2,BigDecimal.ROUND_HALF_UP);

		//购买人利率 = (总利息*360)/(转让价格*持有天数)
		//购买人总收益 = 总利息 + 代收本金 - 转让价格
//		BigDecimal buyerRate = buyerSumInterest.add(investAmount).subtract(transferPrice).multiply(new BigDecimal(360))
//				.divide(transferPrice.multiply(new BigDecimal(buyerHoldDays)),2,BigDecimal.ROUND_HALF_UP);

		//返回：转让人转让后利率，购买人转让后利率，购买人总利息，转让人本期利息，购买人本期利息
//		resultMap.put("transferedRate", transferedRate);
		resultMap.put("buyerRate", rate);
		resultMap.put("transferHoldDays",transferHoldDays);
		resultMap.put("buyerSumInterest", buyerSumInterest);
		resultMap.put("thisBatchInterest",thisBatchInterest);
		resultMap.put("buyerThisBachInterest",buyerThisBachInterest);
		resultMap.put("buyerHoldDays",buyerHoldDays);
		resultMap.put("endDate",lastPlan.getPlanTime());
		return resultMap;
	}
	/**
	*  @Description    ： 计算转让后收益率（针对转让后）
	*  @Method_Name    ：calTransferedRateForTransfered
	*  @param termUnit
	*  @param termValue
	*  @param investId
	*  @param investAmount
	*  @param sumInterest
	*  @return java.util.Map<java.lang.String,java.lang.Object>
	*  @Creation Date  ：2018/4/23
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private Map<String, Object> calTransferedRateForTransfered(int termUnit,int termValue,int investId,BigDecimal investAmount,BigDecimal sumInterest) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		BidReceiptPlan lastPlan  = bidReceiptPlanService.findLastReceiptPlan(investId);
		int transferHoldDays = 0;
		if(termUnit==2){
			transferHoldDays = lastPlan.getPeriods()*30;
		}else if(termUnit==3){
			transferHoldDays = lastPlan.getPeriods();
		}
		transferHoldDays = transferHoldDays==0?1:transferHoldDays;
		BigDecimal transferedRate = sumInterest.multiply(new BigDecimal(360))
				.divide(investAmount.multiply(new BigDecimal(transferHoldDays*100)),2,BigDecimal.ROUND_HALF_UP);
		resultMap.put("transferedRate", transferedRate);
		return resultMap;
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<String> saveTransferManual(int userId, BidTransferManual bidTransferManual) {
		logger.info("用户标识:{}，转让债权发布申请，债权信息:{}", userId, bidTransferManual.toString());
		// 校验此债权是否允许转让
		Integer oldInvestId = bidTransferManual.getOldInvestId();
		BidInvest bidInvest = bidInvestService.findBidInvestById(oldInvestId);
		if (bidInvest == null || InvestConstants.INVEST_TRANSSTATE_NOTALLOW == bidInvest.getTransState()) {
			return new ResponseEntity<String>(Constants.ERROR, "此债权不允许转让");
		}
		if (bidInvest.getRegUserId().intValue() != userId){
			return new ResponseEntity<String>(Constants.ERROR, "此债权不存在");
		}
		BidInfoDetail bidInfoDetail = bidInfoDetailService.findBidInfoDetailByBidId(bidInvest.getBidInfoId());
		if (bidInfoDetail == null) {
			return new ResponseEntity<String>(Constants.ERROR, "未查询到标的详情");
		}
		// 校验此债权转让次数  取消次数校验
//		BidTransferManual transferContidion = new BidTransferManual();
//		transferContidion.setOldInvestId(oldInvestId);
//		int transferCount = bidTransferManualService.findBidTransferManualCount(transferContidion);
//		if (transferCount + 1 > bidInfoDetail.getMostTransferCount()) {
//			return new ResponseEntity<String>(Constants.ERROR, "超过最大转让次数");
//		}

		BidInfo bidInfo = bidInfoService.findBidInfoById(bidInvest.getBidInfoId());
		// 转让金额
//		if (bidTransferManual.getCreditorAmount().compareTo(bidInvest.getInvestAmount()) != 0) {
//			return new ResponseEntity<String>(Constants.ERROR, "转让金额有误");
//		}
		if (bidTransferManual.getTransferDays()<1){
			return new ResponseEntity<>(ERROR, "筹集期限至少为1天");
		}
		if (!CompareUtil.residue(bidTransferManual.getCreditorAmount(), BigDecimal.valueOf(100))) {
			return new ResponseEntity<>(ERROR, "转让金额必须为100的整数倍");
		}
		if (CompareUtil.gt(bidTransferManual.getCreditorAmount(), bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount()))) {
			return new ResponseEntity<>(ERROR, "转让金额超过最大可转让金额");
		}
		// 校验此债权持有天数
		Date startTime = bidInfo.getLendingTime();
		BidTransferManual beforeTransfer = bidTransferManualService.findBidTransferManualByUnique(bidInvest.getId(),
				InvestConstants.INVEST_MANUAL_FIELD_NEWINVESTID);
		if (beforeTransfer != null) {
			startTime = beforeTransfer.getCreateTime();
		}
		int betweenDays = DateUtils.getDaysBetween(startTime, new Date());
		if (betweenDays < bidInfoDetail.getCreditorDays()) {
			return new ResponseEntity<String>(Constants.ERROR,
					"无法转让，持有债权天数不足：" + bidInfoDetail.getCreditorDays() + "天");
		}
		Date lastRepayTime = bidRepayPlanService.findLastRepayPlanTime(bidInfo.getId());
		int betweenLastRepayTime = DateUtils.getDaysBetween(new Date(), lastRepayTime);
		if (betweenLastRepayTime < bidInfoDetail.getDealRepayDays()) {
			return new ResponseEntity<String>(Constants.ERROR,
					"无法转让，距最后一次还款日不足：" + bidInfoDetail.getDealRepayDays() + "天");
		}
		Date nextRepayTime = bidRepayPlanService.findNextRepayPlanTime(bidInfo.getId());
		int betweenNextRepayTime = DateUtils.getDays(new Date(), nextRepayTime);
		if (betweenNextRepayTime >= 0) {
			return new ResponseEntity<String>(Constants.ERROR, "无法转让，还款日或逾期标的不能转让");
		}
		int transferDays = bidTransferManual.getTransferDays();
		if (transferDays > bidInfoDetail.getTransferDays()){
			return new ResponseEntity<String>(Constants.ERROR, "无法转让，筹集天数不能超过"+bidInfoDetail.getTransferDays()+"天");
		}
		if (transferDays > DateUtils.getDaysBetween(new Date(), nextRepayTime)) {
			return new ResponseEntity<String>(Constants.ERROR, "无法转让，筹集天数不能越过下次还款日");
		}
		// 校验溢价率、折价率是否符合要求
//		if (new BigDecimal(bidTransferManual.getTransferRate()).compareTo(bidInfoDetail.getConvertRateStart())<0
//				|| new BigDecimal(bidTransferManual.getTransferRate()).compareTo( bidInfoDetail.getOverflowRateEnd())>0) {
//			return new ResponseEntity<String>(Constants.ERROR, "转让比率必须在：" + bidInfoDetail.getConvertRateStart() + "%到"
//					+ bidInfoDetail.getOverflowRateEnd() + "%之间");
//		}
		if (!CompareUtil.eq(bidTransferManual.getTransferRate(),new BigDecimal(100))) {
			return new ResponseEntity<String>(Constants.ERROR, "此债权只支持平价转让，请调整转让比率");
		}
		// 生成转让关系
		//老债权是否是转让而来
		BidTransferManual preTransfer = bidTransferManualService.findBidTransferByNewInvestId(oldInvestId);
		BidTransferManual saveManual = new BidTransferManual();
		saveManual.setOldInvestId(oldInvestId);
		saveManual.setBidInfoId(bidInvest.getBidInfoId());
		saveManual.setInvestUserId(userId);
		saveManual.setCreditorAmount(bidTransferManual.getCreditorAmount());
		saveManual.setTransferDays(bidTransferManual.getTransferDays());
		BigDecimal transferAmount = bidTransferManual.getCreditorAmount()
				.multiply(bidTransferManual.getTransferRate()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
		saveManual.setTransferAmount(transferAmount);
		saveManual.setTransferRate(bidTransferManual.getTransferRate());
		saveManual.setCreateUserId(userId);
		saveManual.setState(INVEST_TRANSFER_MANUAL_STATE_IN);
		if(preTransfer!=null){
			saveManual.setFirstInvestId(preTransfer.getFirstInvestId());
		}else{
			saveManual.setFirstInvestId(oldInvestId);
		}
		bidTransferManualService.saveBidTransferManual(saveManual);

		BidInvest updateInvest = new BidInvest();
		updateInvest.setId(oldInvestId);
		updateInvest.setState(InvestConstants.INVEST_STATE_TRANSFER);
		bidInvestService.updateBidInvest(updateInvest);

		return new ResponseEntity<String>(Constants.SUCCESS, "发布成功");
	}

	@Override
	@Compensable
	public ResponseEntity<String> buyCreditor(int regUserId, String realName,int transferId) {
		logger.info("tcc buyCreditor entrance, reference invest#updateBidTransferManual, payment#updateAccountInsertTradeAndTransfer, loan#buyCreditorForReceiptPlan ." +
				"购买债权, 用户id: {}, 真实姓名: {}, 转让id: {}", regUserId, realName, transferId);
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = "BUY_CREDITOR_TRANSFER_KEY" + transferId;
		try {
			boolean lockResult = lock.lock(lockKey);
			if (lockResult) {
				ResponseEntity<String> result = new ResponseEntity<String>(Constants.SUCCESS, "购买债权成功");
				//1、校验--债权状态
				ResponseEntity checkResult = validateForCreditorTransfer(transferId, regUserId);
				if (BaseUtil.error(checkResult)) {
					return checkResult;
				}
				//2、查询转让金额&转让后年化率
				BidTransferManual bidTransferManual = (BidTransferManual) checkResult.getParams().get("bidTransferManual");
				ResponseEntity<?> calReslult = calTransferMoneyAndInterestRate(bidTransferManual.getOldInvestId(), bidTransferManual.getCreditorAmount(), bidTransferManual.getTransferRate());
				BigDecimal transferAmount = (BigDecimal) calReslult.getParams().get("transferPrice");
				int isAllCreditor = (int) calReslult.getParams().get("isAllCreditor");
				FinAccount account = (FinAccount) checkResult.getParams().get("account");
				if (CompareUtil.gt(transferAmount, account.getUseableMoney())) {
					return new ResponseEntity<>(ERROR, "可用余额不足，请充值");
				}

				bidTransferManual.setBuyerHoldDays((Integer) calReslult.getParams().get("buyerHoldDays"));
				//3、更新转让状态（防止并发问题）
				BidTransferManual cdt = new BidTransferManual();
				cdt.setId(bidTransferManual.getId());
				cdt.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_OVER);
				cdt.setOldState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_IN);
				int updateResult = BaseUtil.getTccProxyBean(BidTransferManualService.class, getClass(), "buyCreditor").updateBidTransferManual(cdt);
				if (updateResult<=0){
					return new ResponseEntity<>(ERROR, "此债权已被别人抢先了");
				}

				//4、处理投资记录&转让记录
				BigDecimal buyerRate = (BigDecimal) calReslult.getParams().get("buyerRate");
				ResponseEntity<?> buyCreditorResult = BaseUtil.getTccProxyBean(BidTransferManualService.class, getClass(),
						"buyCreditor").buyCreditor(bidTransferManual, regUserId, realName, buyerRate);
				if (BaseUtil.error(buyCreditorResult)) {
					return new ResponseEntity<String>(Constants.ERROR, "购买债权失败");
				}

				//处理流水和金额
				if (BaseUtil.error(dealFlowsAndAccountForBuyCreditor(regUserId, bidTransferManual, transferAmount))) {
					throw new GeneralException("债权转让失败");
				}

				BidInvest oldInvest = bidInvestService.findBidInvestById(bidTransferManual.getOldInvestId());
				if (CommonUtils.gtZero(oldInvest.getCouponIdJ())) {
					// 查询加息券的值
					VasCouponDetail vasDetail = vasCouponDetailService.findVasCouponDetailById(oldInvest.getCouponIdJ());
					oldInvest.setCouponWorthJ(vasDetail.getWorth());
				}
				Set<Integer> sanBidIds = (HashSet<Integer>) buyCreditorResult.getParams().get("sanBidIds");
				Integer newInvestId = (Integer) buyCreditorResult.getParams().get("newInvestId");
				BidInvest newInvest = bidInvestService.findBidInvestById(newInvestId);
				BigDecimal buyerFirstInterest = (BigDecimal) calReslult.getParams().get("buyerThisBachInterest");
				BigDecimal transferFirstInterest = (BigDecimal) calReslult.getParams().get("thisBatchInterest");
				BigDecimal buyerSumInterest = (BigDecimal) calReslult.getParams().get("buyerSumInterest");
				int transferHoldDays = (int) calReslult.getParams().get("transferHoldDays");
				if (isAllCreditor == 1) {
					//全部转让
					BidInfo bidInfo = bidInfoService.findBidInfoById(oldInvest.getBidInfoId());
					if (bidInfo.getBiddRepaymentWay() == InvestConstants.REPAYTYPE_ONECE_REPAYMENT) {
						buyerFirstInterest = buyerSumInterest;
					}
					buyCreditorForReceiptPlan(transferId, buyerFirstInterest, transferFirstInterest, oldInvest, transferHoldDays);
				} else {
					//部分转让  重新生成购买人和债权人的回款计划
					getNewRepayPlanAndReceiptPlanForCreditor(oldInvest, newInvest, buyerFirstInterest, transferFirstInterest, transferHoldDays);
				}
				try {
					//发送消息生成新 投资人合同、作废老投资人合同并生成老投资人新合同 、生成新投资人债权转让协议
					List<Integer> goodInvestIds = (List<Integer>) buyCreditorResult.getParams().get("goodInvestIds");
					if (CommonUtils.isNotEmpty(goodInvestIds)) {
						logger.info("buyCreditor 生成合同发送消息, transferId: {},goodInvestIds: {}", transferId, JSON.toJSON(goodInvestIds));
						jmsService.sendMsg(MQ_QUEUE_CONINFO, DestinationType.QUEUE,
								goodInvestIds, JmsMessageType.OBJECT);
					}

					List<Integer> buyerInvestIds = (List<Integer>) buyCreditorResult.getParams().get("buyerInvestIds");
					if (CommonUtils.isNotEmpty(buyerInvestIds)) {
						logger.info("buyCreditor 生成合同发送消息, transferId: {},goodInvestIds: {}", transferId, JSON.toJSON(buyerInvestIds));
						ConInfoSelfGenerateDTO dto = new ConInfoSelfGenerateDTO();
						dto.setContractType(CONTRACT_TYPE_CREDITOR_TRANSFER);
						dto.setInvestIdList(buyerInvestIds);
						jmsService.sendMsg(MQ_QUEUE_SELF_CONINFO, DestinationType.QUEUE,
								dto, JmsMessageType.OBJECT);
					}
					List<Integer> creditorInvestIds = (List<Integer>) buyCreditorResult.getParams().get("creditorInvestIds");
					if (CommonUtils.isNotEmpty(creditorInvestIds)) {
						logger.info("buyCreditor 生成合同发送消息, transferId: {},goodInvestIds: {}", transferId, JSON.toJSON(creditorInvestIds));
						ConInfoSelfGenerateDTO dto = new ConInfoSelfGenerateDTO();
						dto.setContractType(CONTRACT_TYPE_CREDITOR_TRANSFER_TRANSFEROR);
						dto.setInvestIdList(creditorInvestIds);
						jmsService.sendMsg(MQ_QUEUE_SELF_CONINFO, DestinationType.QUEUE,
								dto, JmsMessageType.OBJECT);
					}
					if(sanBidIds!= null && sanBidIds.size()>0){
						Map<Integer,BidInfoVO> map = bidInfoService.findBidInfoDetailVoByIdList(sanBidIds);
						logger.info("buyCreditor 转让成功发送通知, transferId: {},散标id: {}", transferId, JSON.toJSON(map.keySet()));
						if (map!=null && map.size()>0){
							map.forEach((key,value)->{
								SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg(value.getBorrowerId(), SmsMsgTemplate.MSG_BID_TRANSFER_SUCCESS.getTitle(),
										SmsMsgTemplate.MSG_BID_TRANSFER_SUCCESS.getMsg(),
										SmsConstants.SMS_TYPE_NOTICE,
										new String[] {value.getTitle() }));
							});
						}
					}
				} catch (Exception e) {
					logger.error("buyCreditor, 购买债权生成合同失败, 用户id: {}, 真实姓名: {}, 转让id: {}, 异常信息:\n", regUserId, realName, transferId, e);
				}
				return result;
			}else{
				return new ResponseEntity<>(ERROR, "系统繁忙，请稍后再试");
			}
		} catch (GeneralException e) {
			logger.error("buyCreditor, 购买债权, 用户id: {}, 真实姓名: {}, 转让id: {}, 异常信息:\n", regUserId, realName, transferId, e);
			throw new GeneralException("购买债权失败");
		} finally {
			lock.freeLock(lockKey);
		}
	}


	private void getNewRepayPlanAndReceiptPlanForCreditor(BidInvest oldInvest,BidInvest newInvest,
																   BigDecimal buyerFirstInterest,BigDecimal salFirstInterest,int transferHoldDays){
		BidInfo bidInfo = bidInfoService.findBidInfoById(oldInvest.getBidInfoId());
//		BidReceiptPlan cdt = new BidReceiptPlan();
//		cdt.setBidId(bidInfo.getId());
//		cdt.setInvestId(oldInvest.getId());
//		cdt.setState(RepayConstants.REPAY_STATE_NONE);
//		List<BidReceiptPlan> receiptPlans =  bidReceiptPlanService.findBidReceiptPlanList(cdt);
		if (bidInfo.getBiddRepaymentWay() == InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
			initReceiptPlanCreditorOnece(bidInfo,oldInvest,newInvest,buyerFirstInterest,salFirstInterest,transferHoldDays);
		}else{
			initReceiptPlanCreditorMonth(bidInfo,oldInvest,newInvest,buyerFirstInterest,salFirstInterest,transferHoldDays);
		}

	}


	/**
	*  @Description    ：债权转让初始化回款计划（按月付息到期还本，按日计息，到期还本）
	*  @Method_Name    ：initReceiptPlanCreditorMonth
	*  @param oldInvest
	*  @param newInvest
	*  @param buyerFirstInterest
	*  @param salFirstInterest
	*  @return void
	*  @Creation Date  ：2018/7/9
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private void  initReceiptPlanCreditorMonth(BidInfo bidInfo,BidInvest oldInvest,BidInvest newInvest,
											   BigDecimal buyerFirstInterest,BigDecimal salFirstInterest,int transferHoldDays){
		BidReceiptPlan cdt = new BidReceiptPlan();
		cdt.setBidId(bidInfo.getId());
		cdt.setInvestId(oldInvest.getId());
		cdt.setState(RepayConstants.REPAY_STATE_NONE);
		List<BidReceiptPlan> receiptPlans =  bidReceiptPlanService.findBidReceiptPlanList(cdt);
		if (CommonUtils.isNotEmpty(receiptPlans)){
			BidReceiptPlan thisBatchReceiptPlan = receiptPlans.get(0);
			int thisBatchCode = thisBatchReceiptPlan.getPeriods();
			List<BidReceiptPlan> delPlans = new ArrayList<BidReceiptPlan>();
			receiptPlans.forEach(rplan->{
				if (rplan.getPeriods()>thisBatchCode){
					BidReceiptPlan delPlan = new BidReceiptPlan();
					delPlan.setId(rplan.getId());
					delPlans.add(delPlan);
				}
			});
			//处理转让人第一期回款计划
			BidReceiptPlan salReceiptPlanFirst = new BidReceiptPlan();
			salReceiptPlanFirst.setId(thisBatchReceiptPlan.getId());
			salReceiptPlanFirst.setInterestAmount(salFirstInterest);
			BigDecimal increaseAmount = BigDecimal.ZERO;
			if (CompareUtil.gtZero(oldInvest.getCouponWorthJ()) && transferHoldDays>0 ){
				BigDecimal resuMoney = oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount());
//				BigDecimal beforeMoney = newInvest.getInvestAmount().multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(transferHoldDays)).divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal afterMoney = resuMoney.multiply(oldInvest.getCouponWorthJ()).divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal beforeMoney =  initTransferCouponMoney(oldInvest.getCouponWorthJ(),oldInvest.getId(),DateUtils.addMonth(thisBatchReceiptPlan.getPlanTime(),-1));
				increaseAmount  = beforeMoney.add(afterMoney);
			}
			salReceiptPlanFirst.setIncreaseAmount(increaseAmount);
			salReceiptPlanFirst.setCapitalAmount(BigDecimal.ZERO);
			salReceiptPlanFirst.setAmount(salFirstInterest.add(increaseAmount).add(thisBatchReceiptPlan.getCapitalAmount()));

			//处理购买人第一期回款计划
			BidReceiptPlan buyReceiptPlan = new BidReceiptPlan();
			buyReceiptPlan.setBidId(thisBatchReceiptPlan.getBidId());
			buyReceiptPlan.setInvestId(newInvest.getId());
			buyReceiptPlan.setPeriods(thisBatchCode);
			buyReceiptPlan.setAmount(thisBatchReceiptPlan.getCapitalAmount().add(buyerFirstInterest));
			buyReceiptPlan.setInterestAmount(buyerFirstInterest);
			buyReceiptPlan.setCapitalAmount(thisBatchReceiptPlan.getCapitalAmount());
			buyReceiptPlan.setPlanTime(thisBatchReceiptPlan.getPlanTime());
			buyReceiptPlan.setRegUserId(newInvest.getRegUserId());

			if (bidInfo.getTermValue() - thisBatchCode>0){
				//调用工具类生成剩余期限回款计划
				BidCommonPlanVo planVo = new BidCommonPlanVo();
				planVo.setBidId(bidInfo.getId());
				planVo.setTermUnit(bidInfo.getTermUnit());
				planVo.setTermValue(bidInfo.getTermValue() - thisBatchCode);
				planVo.setInterestRate(bidInfo.getInterestRate());
				planVo.setRepayType(bidInfo.getBiddRepaymentWay());
				planVo.setLendingTime(DateUtils.addMonth(bidInfo.getLendingTime(), thisBatchCode));

				List<BidInvestVo> vos = new ArrayList<>();
				BidInvestVo oldInvestVo = new BidInvestVo();
				oldInvestVo.setId(oldInvest.getId());
				oldInvestVo.setRegUserId(oldInvest.getRegUserId());
				oldInvestVo.setInvestAmount(oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount()));
				oldInvestVo.setCouponWorthJ(oldInvest.getCouponWorthJ());
				vos.add(oldInvestVo);

				BidInvestVo newInvestVo = new BidInvestVo();
				newInvestVo.setId(newInvest.getId());
				newInvestVo.setRegUserId(newInvest.getRegUserId());
				newInvestVo.setInvestAmount(newInvest.getInvestAmount());
				vos.add(newInvestVo);
				ResponseEntity planResult =  RepayAndReceiptUtils.initRepayPlan(planVo, vos,RepayConstants.INIT_PLAN_RECEIPT_ONLY);
				if (!BaseUtil.error(planResult)){
					List<BidReceiptPlan> receiptPlanList = (List<BidReceiptPlan>) planResult.getParams().get("recieptPlanList");
					receiptPlanList.forEach(plan->{
						plan.setPeriods(plan.getPeriods()+thisBatchCode);
					});
					receiptPlanList.add(buyReceiptPlan);
					BaseUtil.getTccProxyBean(BidReceiptPlanService.class,getClass(),"buyCreditor").buyCreditorForReceiptPlan(salReceiptPlanFirst,receiptPlanList,delPlans);
				}
			}else{
				List<BidReceiptPlan> receiptPlanList = new ArrayList<>();
				buyReceiptPlan.setCapitalAmount(newInvest.getInvestAmount());
				buyReceiptPlan.setAmount(newInvest.getInvestAmount().add(buyerFirstInterest));
				receiptPlanList.add(buyReceiptPlan);

				salReceiptPlanFirst.setCapitalAmount(oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount()));
				salReceiptPlanFirst.setAmount(salReceiptPlanFirst.getCapitalAmount().add(salReceiptPlanFirst.getInterestAmount()).add(salReceiptPlanFirst.getIncreaseAmount()));
				BaseUtil.getTccProxyBean(BidReceiptPlanService.class,getClass(),"buyCreditor").buyCreditorForReceiptPlan(salReceiptPlanFirst,receiptPlanList,null);
			}

		}
	}


	private void  initReceiptPlanCreditorOnece(BidInfo bidInfo,BidInvest oldInvest,BidInvest newInvest,
											   BigDecimal buyerFirstInterest,BigDecimal salFirstInterest,int transferHoldDays){
		BidReceiptPlan cdt = new BidReceiptPlan();
		cdt.setBidId(bidInfo.getId());
		cdt.setInvestId(oldInvest.getId());
		cdt.setState(RepayConstants.REPAY_STATE_NONE);
		List<BidReceiptPlan> receiptPlans =  bidReceiptPlanService.findBidReceiptPlanList(cdt);
		if (CommonUtils.isNotEmpty(receiptPlans)){
			BidReceiptPlan thisBatchReceiptPlan = receiptPlans.get(0);
			//处理转让人第一期回款计划
			BidReceiptPlan salReceiptPlanFirst = new BidReceiptPlan();
			salReceiptPlanFirst.setId(thisBatchReceiptPlan.getId());
//			salReceiptPlanFirst.setInterestAmount(salFirstInterest);
//			BigDecimal increaseAmount = BigDecimal.ZERO;
//			if (CompareUtil.gtZero(thisBatchReceiptPlan.getInterestAmount())){
//				increaseAmount = thisBatchReceiptPlan.getIncreaseAmount().multiply(salFirstInterest)
//						.divide(thisBatchReceiptPlan.getInterestAmount(),2,BigDecimal.ROUND_HALF_UP);
//			}
//			salReceiptPlanFirst.setIncreaseAmount(increaseAmount);
//			salReceiptPlanFirst.setCapitalAmount(BigDecimal.ZERO);
//			salReceiptPlanFirst.setAmount(salFirstInterest.add(increaseAmount).add(thisBatchReceiptPlan.getCapitalAmount()));

			//处理购买人第一期回款计划
//			BidReceiptPlan buyReceiptPlan = new BidReceiptPlan();
//			buyReceiptPlan.setBidId(thisBatchReceiptPlan.getBidId());
//			buyReceiptPlan.setInvestId(newInvest.getId());
//			buyReceiptPlan.setPeriods(1);
//			buyReceiptPlan.setAmount(thisBatchReceiptPlan.getCapitalAmount().add(buyerFirstInterest));
//			buyReceiptPlan.setInterestAmount(buyerFirstInterest);
//			buyReceiptPlan.setCapitalAmount(thisBatchReceiptPlan.getCapitalAmount());
//			buyReceiptPlan.setPlanTime(thisBatchReceiptPlan.getPlanTime());
//			buyReceiptPlan.setRegUserId(newInvest.getRegUserId());

			//调用工具类生成剩余期限回款计划
			BidCommonPlanVo planVo = new BidCommonPlanVo();
			planVo.setBidId(bidInfo.getId());
			planVo.setTermUnit(bidInfo.getTermUnit());
			if (bidInfo.getTermUnit()==2){
				int month = DateUtils.getMonthBetween(bidInfo.getLendingTime(),new Date());
				if (month==0){
					month = 1;
				}
				if (DateUtils.getDays(DateUtils.addMonth(bidInfo.getLendingTime(),month),new Date())<0){
					month = month +1;
				}
				planVo.setTermValue(bidInfo.getTermValue() - month);
				planVo.setLendingTime(DateUtils.addMonth(bidInfo.getLendingTime(), month));
			}else{
				planVo.setTermValue(bidInfo.getTermValue() - DateUtils.getDaysBetween(bidInfo.getLendingTime(),new Date()));
				planVo.setLendingTime(new Date());
			}
			planVo.setInterestRate(bidInfo.getInterestRate());
			planVo.setRepayType(bidInfo.getBiddRepaymentWay());

			List<BidInvestVo> vos = new ArrayList<>();
			BidInvestVo newInvestVo = new BidInvestVo();
			newInvestVo.setId(newInvest.getId());
			newInvestVo.setRegUserId(newInvest.getRegUserId());
			newInvestVo.setInvestAmount(newInvest.getInvestAmount());
			vos.add(newInvestVo);
			ResponseEntity planResult =  RepayAndReceiptUtils.initRepayPlan(planVo, vos,RepayConstants.INIT_PLAN_RECEIPT_ONLY);
			if (!BaseUtil.error(planResult)){
				List<BidReceiptPlan> resultReceiptList = new ArrayList<BidReceiptPlan>();
				List<BidReceiptPlan> receiptPlanList = (List<BidReceiptPlan>) planResult.getParams().get("recieptPlanList");
				if (receiptPlanList!=null && receiptPlanList.size()==1){
					BidReceiptPlan  buyerPlan = receiptPlanList.get(0);
					BigDecimal capitalAmount = oldInvest.getInvestAmount().subtract(oldInvest.getTransAmount());
					if(bidInfo.getTermUnit()==2){
						buyerPlan.setInterestAmount(buyerPlan.getInterestAmount().add(buyerFirstInterest));
						buyerPlan.setAmount(buyerPlan.getCapitalAmount().add(buyerPlan.getInterestAmount()).add(buyerPlan.getIncreaseAmount()));
						if (CompareUtil.gtZero(oldInvest.getCouponWorthJ())){
							//加息金额 = 本金* 加息利率 * 总天数
							BigDecimal afterMoney = capitalAmount.multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(bidInfo.getTermValue()))
									.divide(new BigDecimal(1200),2,BigDecimal.ROUND_HALF_UP);
							//投资金额 * 加息利率 * 持有天数
//							BigDecimal beforeMoney = newInvest.getInvestAmount().multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(transferHoldDays))
////									.divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
							BigDecimal beforeMoney = initTransferCouponMoney(oldInvest.getCouponWorthJ(),oldInvest.getId(),bidInfo.getLendingTime());
							salReceiptPlanFirst.setIncreaseAmount(afterMoney.add(beforeMoney));
						}
					}else{
						if (CompareUtil.gtZero(oldInvest.getCouponWorthJ())){
							BigDecimal afterMoney = capitalAmount.multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(bidInfo.getTermValue()))
									.divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
//							BigDecimal beforeMoney = newInvest.getInvestAmount().multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(transferHoldDays))
//									.divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
							BigDecimal beforeMoney = initTransferCouponMoney(oldInvest.getCouponWorthJ(),oldInvest.getId(),bidInfo.getLendingTime());
							salReceiptPlanFirst.setIncreaseAmount(afterMoney.add(beforeMoney));
						}
					}
					resultReceiptList.add(buyerPlan);
					salReceiptPlanFirst.setInterestAmount(thisBatchReceiptPlan.getInterestAmount().subtract(buyerPlan.getInterestAmount()));
					salReceiptPlanFirst.setCapitalAmount(thisBatchReceiptPlan.getCapitalAmount().subtract(buyerPlan.getCapitalAmount()));
					salReceiptPlanFirst.setAmount(salReceiptPlanFirst.getCapitalAmount().add(salReceiptPlanFirst.getInterestAmount()));
				}
				BaseUtil.getTccProxyBean(BidReceiptPlanService.class,getClass(),"buyCreditor").buyCreditorForReceiptPlan(salReceiptPlanFirst,resultReceiptList,null);
			}

		}
	}
	/**
	*  @Description    ：转让---计算原债券人加息收益
	*  @Method_Name    ：initTransferCouponMoney
	*  @param couponWorthJ
	*  @param oldInvestId
	*  @param startDate
	*  @return java.math.BigDecimal
	*  @Creation Date  ：2018/7/12
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private BigDecimal initTransferCouponMoney(BigDecimal couponWorthJ,Integer oldInvestId,Date startDate){
		//查询转让记录 oldInvestId
		BidTransferManual bidTransferManual = new BidTransferManual();
		bidTransferManual.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_OVER);
		bidTransferManual.setOldInvestId(oldInvestId);
		bidTransferManual.setModifyTimeBegin(startDate);
		List<BidTransferManual> resultList =   bidTransferManualService.findBidTransferManualList(bidTransferManual);
		BigDecimal resultMoney = BigDecimal.ZERO;
		if (CommonUtils.isNotEmpty(resultList)){
			for (BidTransferManual manual :resultList){
				//转让人持有天数
				int holdDays = DateUtils.getDaysBetween(startDate,manual.getTransferTime());
				BigDecimal creaseMoney = manual.getCreditorAmount().multiply(new BigDecimal(holdDays))
						.multiply(couponWorthJ).divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
				resultMoney = resultMoney.add(creaseMoney);
			}
		}
		return resultMoney;
	}
	/**
	*  @Description    ：购买债权处理流水和金额
	*  @Method_Name    ：dealFlowsAndAccountForBuyCreditor
	*  @param regUserId
	*  @param bidTransferManual
	*  @param transferAmount
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/4/23
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private ResponseEntity<?> dealFlowsAndAccountForBuyCreditor(Integer regUserId,BidTransferManual bidTransferManual,BigDecimal transferAmount){
		logger.info("dealFlowsAndAccountForBuyCreditor, 购买债权处理流水和金额: 用户id{}, 转让信息: {}, 转让金额: {}", regUserId, bidTransferManual, transferAmount);
		List<FinFundtransfer>  fundtransfers = new ArrayList<FinFundtransfer>();
		//初始化流水
		FinTradeFlow tradeFlow = FinTFUtil.initFinTradeFlow(regUserId, bidTransferManual.getId(),
				transferAmount, TRADE_TYPE_CREDITOR_TRANSFER_MANUAL, PlatformSourceEnums.PC);
		//初始化资金划转
		FinFundtransfer buyerFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(), regUserId,
				bidTransferManual.getInvestUserId(),transferAmount, TRANSFER_SUB_CODE_PAY);
		FinFundtransfer transferFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),
				bidTransferManual.getInvestUserId(),regUserId,transferAmount, TRANSFER_SUB_CODE_INCOME);
		fundtransfers.add(buyerFundtransfer);
		fundtransfers.add(transferFundtransfer);

		BidInfoDetail bidInfoDetail = bidInfoDetailService.findBidInfoDetailByBidId(bidTransferManual.getBidInfoId());
		if (bidInfoDetail == null) {
			return new ResponseEntity<String>(ERROR, "未查询到债权相关标的信息");
		}
		if (CompareUtil.gtZero(bidInfoDetail.getTransferRate())) {
			//收取手续费
			BigDecimal chargeMoney = bidTransferManual.getCreditorAmount().multiply(bidInfoDetail.getTransferRate()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
			FinFundtransfer transferToPlatformFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),
					bidTransferManual.getInvestUserId(),PLATFORM_ACCOUNT_ID,chargeMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.CHARGE));
			FinFundtransfer platformFundtransfer = FinTFUtil.initFinFundtransfer(tradeFlow.getFlowId(),
					PLATFORM_ACCOUNT_ID,bidTransferManual.getInvestUserId(),chargeMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
							FundtransferSmallTypeStateEnum.CHARGE));
			fundtransfers.add(transferToPlatformFundtransfer);
			fundtransfers.add(platformFundtransfer);
		}
		//调用支付
		return BaseUtil.getTccProxyBean(FinConsumptionService.class,getClass(),"buyCreditor").updateAccountInsertTradeAndTransfer(tradeFlow,fundtransfers);
	}

	/**
	*  @Description    ：购买债权校验
	*  @Method_Name    ：validateForCreditorTransfer
	*  @param transferId
	*  @param regUserId
	*  @return com.yirun.framework.core.model.ResponseEntity<?>
	*  @Creation Date  ：2018/4/20
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private ResponseEntity<?> validateForCreditorTransfer(Integer transferId,Integer regUserId){
		try {
			BidTransferManual bidTransferManual = bidTransferManualService.findBidTransferManualById(transferId);
			if (bidTransferManual == null) {
				return new ResponseEntity<>(ERROR, "您认购的项目不存在");
			}
			if (bidTransferManual.getState() == INVEST_TRANSFER_MANUAL_STATE_LOSE) {
				return new ResponseEntity<>(ERROR, "您认购的项目已失效");
			}
			if (bidTransferManual.getState() == INVEST_TRANSFER_MANUAL_STATE_OVER) {
				return new ResponseEntity<>(ERROR, "您认购的项目已转让");
			}
			if(regUserId.intValue() == bidTransferManual.getInvestUserId().intValue()){
				return new ResponseEntity<>(ERROR, "您不能对自己的债权进行认购");
			}
			if (DateUtils.getDaysBetween(bidTransferManual.getCreateTime(),new Date())>bidTransferManual.getTransferDays()){
				return new ResponseEntity<>(ERROR, "此债权已过筹集时间，不能购买");
			}

			//校验--账户金额是否充足
			FinAccount account = finAccountService.findByRegUserId(regUserId);
			if (account == null) {
				return new ResponseEntity<>(ERROR, "未查询到您的账户信息，请稍后再试");
			}
			Map<String,Object> params  = new HashMap<String, Object>();
			params.put("bidTransferManual",bidTransferManual);
			params.put("account",account);
			return new ResponseEntity<>(SUCCESS,"购买债权校验成功",params);
		} catch (Exception e) {
			logger.error("validateForCreditorTransfer, 购买债权校验异常, transferId:{}, regUserId: {}, 异常信息:\n",
					transferId, regUserId,e);
			throw new GeneralException("购买债权失败");
		}
	}

	/**
	 * @Description : 债权转让--计算购买人第一期预期收益
	 * @Method_Name : getExpectInterest
	 * @param money
	 * @param rate
	 * @param bidId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月7日 下午5:31:40
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private BigDecimal getExpectInterest(BigDecimal money, BigDecimal rate, int bidId) {
		Date nextRepayTime = bidRepayPlanService.findNextRepayPlanTime(bidId);
		int days = DateUtils.getDaysBetween(new Date(), nextRepayTime);
		return CalcInterestUtil.calExpectInterest(money, rate, days);
	}

	/**
	 * @Description : 债权转让--计算购买人第一期加息收益
	 * @Method_Name : getExpectIncreaseAmount
	 * @param money
	 * @param rate
	 * @param bidId
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年8月3日 上午9:41:09
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private BigDecimal getExpectIncreaseAmount(BigDecimal money, BigDecimal rate, int bidId) {
		Date nextRepayTime = bidRepayPlanService.findNextRepayPlanTime(bidId);
		int days = DateUtils.getDaysBetween(new Date(), nextRepayTime);
		int increseDays = 30 - days;
		if (increseDays <= 0) {
			return new BigDecimal(0);
		}
		return CalcInterestUtil.calExpectInterest(money, rate, days);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public ResponseEntity<?> myCreditorList(RegUser regUser,Integer state,Integer userFlag, Pager pager) {
//		List<TransferManualAppVo> creditorList = null;
//		BidTransferManual tranferContidion = new BidTransferManual();
//		if (userFlag == InvestConstants.INVEST_USER_FLAG_TRANSFER) {
//			tranferContidion.setInvestUserId(regUser.getId());
//			tranferContidion.setInvestState(state);
//		}else{
//			tranferContidion.setBuyUserId(regUser.getId());
//		}
//		tranferContidion.setState(state);
//		Pager resultPager = bidTransferManualService.myCreditor(tranferContidion, pager);
//		if(resultPager!=null){
//					creditorList = (List<TransferManualAppVo>) resultPager.getData();
//					List<Integer> bidIds = new ArrayList<Integer>();
//					List<Integer> investIds = new ArrayList<Integer>();
//					if (CommonUtils.isNotEmpty(creditorList)) {
//						creditorList.forEach(creditor->{
//					bidIds.add(creditor.getBidId());
//					if(userFlag==InvestConstants.INVEST_USER_FLAG_BUYER){
//						investIds.add(creditor.getNewInvestId());
//					}
//				});
//				Map<String,Object> map = bidRepayPlanService.findEndDateAndSumInterest(bidIds,investIds);
//				List<BidRepayPlan> endDateList =  (List<BidRepayPlan>) map.get("endDateList");
//				List<Map<String,Object>> interestList = (List<Map<String, Object>>) map.get("interestList");
//				creditorList.forEach(creditor->{
//					for(BidRepayPlan plan:endDateList){
//						if(creditor.getBidId().equals(plan.getBidId())){
//							creditor.setEndDate(plan.getPlanTime());
//							break;
//						}
//					}
//					if(userFlag==InvestConstants.INVEST_USER_FLAG_BUYER){
//						for(Map<String,Object> interestMap:interestList){
//							Integer investId = (Integer) interestMap.get("investId");
//							if(creditor.getNewInvestId().equals(investId)){
//								creditor.setBuyerExpectAmount((BigDecimal)interestMap.get("interestAmount"));
//								break;
//							}
//						}
//					}
//				});
//				resultPager.setData(creditorList);
//			}
//		}
//		return new ResponseEntity<>(Constants.SUCCESS, resultPager);
//	}



	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> myCreditorList(RegUser regUser,Integer state,Pager pager) {
		List<TransferManualAppVo> creditorList = null;
		BidTransferManual tranferContidion = new BidTransferManual();
		tranferContidion.setInvestUserId(regUser.getId());
		tranferContidion.setState(state);
		Pager resultPager = bidTransferManualService.myCreditor(tranferContidion, pager);
		if(resultPager!=null){
			creditorList = (List<TransferManualAppVo>) resultPager.getData();
			List<Integer> bidIds = new ArrayList<Integer>();
			List<Integer> investIds = new ArrayList<Integer>();
			creditorList.forEach(appVo->{
				if(state==INVEST_TRANSFER_MANUAL_STATE_INIT || state == INVEST_TRANSFER_MANUAL_STATE_IN ){//如果是可转让列表 或者转让中列表
					//计算预期年化率  年化率平价转让保持不变
//					ResponseEntity<?> result = calTransferMoneyAndInterestRate(appVo.getInvestId(),appVo.getCreditorAmount(),new BigDecimal(100));
//					if(!BaseUtil.error(result)){
//						appVo.setBuyerRate((BigDecimal) result.getParams().get("buyerRate"));
//					}a
					BidInfo bidInfo = bidInfoService.findBidInfoById(appVo.getBidId());
					appVo.setBuyerRate(bidInfo.getInterestRate());
				}
				if(state==INVEST_TRANSFER_MANUAL_STATE_IN || state==INVEST_TRANSFER_MANUAL_STATE_OVER){
					//计算筹集到期日期
					String endDate = DateUtils.format(DateUtils.addDays(appVo.getCreateTime(),appVo.getTransferDays()),"yyyy-MM-dd");
					appVo.setEndDate(endDate);
				}else{
					Date endDate = bidRepayPlanService.findLastRepayPlanTime(appVo.getBidId());
					appVo.setEndDate(DateUtils.format(endDate));
				}
				appVo.setState(state);
			});
			resultPager.setData(creditorList);
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultPager);
	}
	/**
	 * 
	 *  @Description    : 处理债权转让回款计划
	 *  @Method_Name    : buyCreditorForReceiptPlan
	 *  @param transferId
	 *  @param buyerFirstInterest  购买人第一期利息
	 *  @param transferFirstInterest 转让人第一期利息
	 *  @return         : void
	 *  @Creation Date  : 2017年12月29日 上午9:09:21 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private void buyCreditorForReceiptPlan(int transferId,BigDecimal buyerFirstInterest,BigDecimal transferFirstInterest,BidInvest oldInvest,int transferHoldDays) {
		BidTransferManual bidTransferManual = bidTransferManualService.findBidTransferManualById(transferId);
		// 查询原债权人，当前期回款计划，更改金额 investid , 未回款，正序排列第一个，
		BidReceiptPlan contidion = new BidReceiptPlan();
		contidion.setInvestId(bidTransferManual.getOldInvestId());
		contidion.setState(RepayConstants.REPAY_STATE_NONE);
		List<BidReceiptPlan> oldReceiptPlans = bidReceiptPlanService.findBidReceiptPlanList(contidion);
		List<BidReceiptPlan> buyReceiptPlans = new ArrayList<BidReceiptPlan>();
		BidReceiptPlan salReceiptPlanFirst = new BidReceiptPlan();
		List<BidReceiptPlan> salReceiptPlans = new ArrayList<BidReceiptPlan>();
		if (!CommonUtils.isEmpty(oldReceiptPlans)) {
			for (int i = 0; i <= oldReceiptPlans.size() - 1; i++) {
				BidReceiptPlan rp = oldReceiptPlans.get(i);
				BidReceiptPlan buyReceiptPlan = new BidReceiptPlan();
				BidReceiptPlan salReceiptPlan = new BidReceiptPlan();
				// 处理购买人回款计划
				BigDecimal buyInterest = i == 0 ? buyerFirstInterest : rp.getInterestAmount();
				buyReceiptPlan.setBidId(rp.getBidId());
				buyReceiptPlan.setInvestId(bidTransferManual.getNewInvestId());
				buyReceiptPlan.setPeriods(rp.getPeriods());
				buyReceiptPlan.setAmount(rp.getCapitalAmount().add(buyInterest));
				buyReceiptPlan.setInterestAmount(buyInterest);
				buyReceiptPlan.setCapitalAmount(rp.getCapitalAmount());
				buyReceiptPlan.setPlanTime(rp.getPlanTime());
				buyReceiptPlan.setRegUserId(bidTransferManual.getBuyUserId());
				buyReceiptPlans.add(buyReceiptPlan);
				// 处理转让人回款计划
				BigDecimal salInterest = rp.getInterestAmount();
				if (i == 0 && CompareUtil.gtZero(transferFirstInterest)) {
					salReceiptPlanFirst.setId(rp.getId());
					salReceiptPlanFirst.setInterestAmount(transferFirstInterest);
					BigDecimal increaseAmount = BigDecimal.ZERO;
					if (CompareUtil.gtZero(oldInvest.getCouponWorthJ()) && transferHoldDays>0 ){
//						BigDecimal resuMoney = bidTransferManual.getCreditorAmount();
//						increaseAmount = resuMoney.multiply(oldInvest.getCouponWorthJ()).multiply(new BigDecimal(transferHoldDays)).divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
						BidInfo bidInfo = bidInfoService.findBidInfoById(oldInvest.getBidInfoId());
						Date startDate = bidInfo.getLendingTime();
						if (bidInfo.getBiddRepaymentWay()!=InvestConstants.REPAYTYPE_ONECE_REPAYMENT){
							//查询本期回款计划
							startDate = DateUtils.addMonth(rp.getPlanTime(),-1);
						}
						increaseAmount = initTransferCouponMoney(oldInvest.getCouponWorthJ(),oldInvest.getId(),startDate);
					}
					salReceiptPlanFirst.setIncreaseAmount(increaseAmount);
					salReceiptPlanFirst.setCapitalAmount(BigDecimal.ZERO);
					salReceiptPlanFirst.setAmount(transferFirstInterest.add(increaseAmount));
				}else{
					salReceiptPlan.setId(rp.getId());
					salReceiptPlans.add(salReceiptPlan);
				}
			}
		}
		BaseUtil.getTccProxyBean(BidReceiptPlanService.class,getClass(),"buyCreditor").buyCreditorForReceiptPlan(salReceiptPlanFirst,buyReceiptPlans,salReceiptPlans);
	}

	@Override
	public ResponseEntity<?> calTransferMoneyAndInterestRate(Integer investId, BigDecimal creditorAmount,
			BigDecimal transferRate) {
		try {
			TransferManualAppPreVo transferDetail =  bidTransferManualService.findBidTransferDetailByInvestId(investId);
			BigDecimal transferPrice = creditorAmount.multiply(transferRate).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
			BigDecimal receivedIncome = bidReceiptPlanService.findSumReceivedInterest(investId);
			Map<String,Object> resultMap = calTransferedRateForTransfering(transferDetail.getTermUnit(), transferDetail.getTermValue(), investId, transferDetail.getInterestRate(),
					creditorAmount, receivedIncome, transferPrice);
			resultMap.put("transferPrice", transferPrice);
			//是否是全部转让
			int isAllCreditor =  CompareUtil.eq(transferDetail.getHoldAmount(),creditorAmount) ? 1 : 0;
			resultMap.put("isAllCreditor", isAllCreditor);
			return new ResponseEntity<>(Constants.SUCCESS,"查询成功",resultMap);
		} catch (Exception e) {
			logger.info("calTransferMoneyAndInterestRate, investId: {}, creditorAmount: {}, transferRate: {},异常信息:\n",
					investId, creditorAmount, transferRate,e);
			throw new GeneralException("计算转让利率异常");
		}
	}
	
	public ResponseEntity<?> calTransferMoneyAndInterestRateOld(Integer investId, BigDecimal transferAmount,
			BigDecimal transferRate) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS,"计算购买后年化率成功");
		BidInvest bidInvest = bidInvestService.findBidInvestById(investId);
		if(bidInvest==null){
			return new ResponseEntity<>(Constants.ERROR,"债权不存在");
		}
		BidInfo bidInfo = bidInfoService.findBidInfoById(bidInvest.getBidInfoId());
		Map<String,Object> map = bidReceiptPlanService.findParamsForTransferInterestRate(investId);
		//待收收益
		BigDecimal notReceivedInterest = (BigDecimal) map.get(NOT_RECEIVED_INTEREST);
		//已收收益
		BigDecimal receivedInterest = bidReceiptPlanService.findSumReceivedInterest(investId);
		//下一期回款计划
		BidReceiptPlan nextReceiptPlan = (BidReceiptPlan) map.get(RECEIPT_NEXT_PLAN);
		BidReceiptPlan lastReceiptPlan = (BidReceiptPlan) map.get(RECEIPT_LAST_PLAN);
		// 购买人本金 = 转让金额 * 转让比率
		BigDecimal capitalMoney = transferAmount.multiply(transferRate).divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP);
		//转让人本金
		BigDecimal transferCapital = bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount()).subtract(transferAmount);
//		if(CompareUtil.eZero(transferCapital)){
//			transferCapital = bidInvest.getInvestAmount().subtract(bidInvest.getTransAmount());
//		}
		//购买人本期持有天数
		int thisBatchDays = DateUtils.getDaysBetween(new Date(), nextReceiptPlan.getPlanTime());
		//转让人本期持有天数
		int transferBatchDays = 0 ;
		//购买人本期利息=每日利息*本期持有天数 ####每日利息 = 标的金额*利率/360
		BigDecimal thisBatchInterest = new BigDecimal(0);
		//购买人持有总天数 = 本期持有天数 + 未回款天数
		int buyerHoldDays = 0;
		//转让人持有总天数 = 本期持有天数 + 已回款天数
		int transferHoldDays = 0;
		//转让人本期持有天数
		if(bidInfo.getTermUnit()==InvestConstants.BID_TERM_UNIT_DAY){
			transferBatchDays = bidInfo.getTermValue() - thisBatchDays;
			transferHoldDays = transferBatchDays;
			thisBatchInterest = transferAmount.multiply(bidInfo.getInterestRate()).multiply(new BigDecimal(transferBatchDays)).
					divide(new BigDecimal(3600),2,BigDecimal.ROUND_HALF_UP);
		}else if(bidInfo.getTermUnit()==InvestConstants.BID_TERM_UNIT_MONTH){
			if(thisBatchDays>=30){
				buyerHoldDays = nextReceiptPlan.getPeriods()*30;
				transferHoldDays = (bidInfo.getTermValue()-nextReceiptPlan.getPeriods())*30;
				thisBatchInterest = nextReceiptPlan.getInterestAmount();
			}else{
				transferBatchDays = 30 - thisBatchDays;
				buyerHoldDays = thisBatchDays + (nextReceiptPlan.getPeriods()-1)*30;
				transferHoldDays = transferBatchDays + (bidInfo.getTermValue()-nextReceiptPlan.getPeriods())*30;
				thisBatchInterest = bidInfo.getTotalAmount().multiply(bidInfo.getInterestRate())
						.multiply(new BigDecimal(thisBatchDays)).divide(new BigDecimal(36000),2,BigDecimal.ROUND_HALF_UP);
			}
		}else{
			if(thisBatchDays>=360){
				thisBatchInterest = nextReceiptPlan.getInterestAmount();
			}else{
				transferBatchDays = 360 - thisBatchDays;
				thisBatchInterest = bidInfo.getTotalAmount().multiply(bidInfo.getInterestRate())
						.multiply(new BigDecimal(thisBatchDays)).divide(new BigDecimal(36000),2);
			}
		}
		//转让人本期利息
		BigDecimal transferThisBatchInterest = nextReceiptPlan.getInterestAmount().subtract(thisBatchInterest);
		//购买人总利息=本期利息+待收收益-第一期利息  
		BigDecimal interestMoney = thisBatchInterest.add(notReceivedInterest).subtract(nextReceiptPlan.getInterestAmount());
		//转让人总利息 = 本期利息 + 已回款利息 + 溢价金额/折价金额
		BigDecimal transferInterestMoney = transferThisBatchInterest.add(receivedInterest).add(capitalMoney.subtract(transferAmount));
		
		//购买人年化率= （利息*360）*100/（本金*持有天数）    
		BigDecimal buyerRateYear = interestMoney.multiply(new BigDecimal(36000))
							.divide(capitalMoney.multiply(new BigDecimal(buyerHoldDays)),2, BigDecimal.ROUND_HALF_UP);
		BigDecimal transferRateYear = BigDecimal.ZERO;
//				transferInterestMoney.multiply(new BigDecimal(36000))
//				.divide(transferCapital.multiply(new BigDecimal(transferHoldDays)),2, BigDecimal.ROUND_HALF_UP);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("transferAbleAmount", capitalMoney);
		params.put("buyerRateYear",buyerRateYear);
		params.put("transferRateYear",transferRateYear);
		params.put("buyThisBatchInterest", thisBatchInterest);
		params.put("transferThisBatchInterest", transferThisBatchInterest);
		params.put("buyerExpectAmount", interestMoney);
		params.put("endDate", lastReceiptPlan.getPlanTime());
		params.put("nextRepayTime", nextReceiptPlan.getPlanTime());
		params.put(NOT_RECEIVED_INTEREST, notReceivedInterest);
		result.setParams(params);
		return result;
	}

	@Override
	public ResponseEntity<?> investListForCreditor(BidTransferManual bidTransferManual, Pager pager) {
		Pager resultPager = bidTransferManualService.myCreditorForTransfer(bidTransferManual, pager);
		Map<String,Object> params = new HashMap<>();
		params.put("creditFlag",0);
		if(resultPager!=null){
			List<CreditorTransferDetailVo> list =  (List<CreditorTransferDetailVo>) resultPager.getData();
			if(CommonUtils.isNotEmpty(list)){
				list.forEach(creditorVo->{
					ResponseEntity<?> rateResult = calTransferMoneyAndInterestRate(creditorVo.getInvestId(),
							creditorVo.getCreditorAmount(), creditorVo.getTransferRate());
					Date endDate = (Date) rateResult.getParams().get("endDate");
					int endNum = DateUtils.getDaysBetween(new Date(),endDate);
					creditorVo.setEndDateNum(endNum);
					creditorVo.setBuyerRate((BigDecimal) rateResult.getParams().get("buyerRate"));
				});
				params.put("creditFlag",1);
			}
			resultPager.setData(list);
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultPager,params);
	}

	@Override
	public ResponseEntity<?> toBuyCreditor(Integer transferId, Integer regUserId) {
		ResponseEntity<?> result = showTransferManualDetailByTransferId(transferId, regUserId);
		if(BaseUtil.error(result)){
			return result;
		}
		TransferManualDetailAppVo transferDetail = (TransferManualDetailAppVo) result.getParams().get("transferDetail");
		if(transferDetail.getState() == INVEST_TRANSFER_MANUAL_STATE_IN){
			//查询筹集期到期日
			Date endDate = DateUtils.addDays(transferDetail.getCreateTime(), transferDetail.getTransferDays());
			long expireTime = DateUtils.betDate(endDate,new Date())>0?DateUtils.betDate(endDate,new Date()):0;
			transferDetail.setExpireTime(expireTime);
		}else{
			transferDetail.setExpireTime(0);
		}
//		transferDetail.setEndDateNum(DateUtils.getDaysBetween(new Date(), transferDetail.getEndDate()));
		FinAccount account = finAccountService.findByRegUserId(regUserId);
		result.getParams().put("account", account);
		RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(transferDetail.getInvestUserId());
		transferDetail.setInvestUserName(regUserDetail.getRealName());
		result.getParams().put("transferDetail", transferDetail);
		return result;
	}

	@Override
	public ResponseEntity<?> creditorTransferList(Pager pager) {
		BidTransferManual contidion = new BidTransferManual();
		contidion.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_IN);
		pager.setPageSize(2);
		Pager resultPager = bidTransferManualService.findInvestTransfering(contidion,pager);
		if(resultPager!=null && CommonUtils.isNotEmpty(resultPager.getData())){
			List<TransferManualAppVo> list = (List<TransferManualAppVo>) resultPager.getData();
			list.forEach(appVo->{
				//计算预期收益
				ResponseEntity<?> result = calTransferMoneyAndInterestRate(appVo.getInvestId(),appVo.getCreditorAmount(),new BigDecimal(100));
				if(!BaseUtil.error(result)){
					appVo.setBuyerRate((BigDecimal) result.getParams().get("buyerRate"));
					//2、计算期限
					appVo.setBuyerHoldDays((Integer) result.getParams().get("buyerHoldDays"));
				}
			});
			resultPager.setData(list);
		}
		return new ResponseEntity<>(SUCCESS,resultPager);
	}

	@Override
	public void dealTransferTimeOut() {
		//1、查询转让中记录
		BidTransferManual cdt = new BidTransferManual();
		cdt.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_IN);
		List<BidTransferManual> transferManuals =  bidTransferManualService.findBidTransferManualList(cdt);

		Set<Integer> transferIds = new HashSet<Integer>();
		List<BidInvest> updateInvestList = new ArrayList<BidInvest>();
		//2、如果超时，修改记录状态为失效
		if (CommonUtils.isNotEmpty(transferManuals)){
			transferManuals.forEach(transferManual->{
				if(DateUtils.getDaysBetween(transferManual.getCreateTime(),new Date())>transferManual.getTransferDays()){
					//实际筹集天数 > 预计筹集天数 则设置失效
					transferIds.add(transferManual.getId());
					//判断投资记录是否在转让中
					BidTransferManual investCdt = new BidTransferManual();
					investCdt.setOldInvestId(transferManual.getOldInvestId());
					investCdt.setState(InvestConstants.INVEST_TRANSFER_MANUAL_STATE_IN);
					List<BidTransferManual>  investTransferList = bidTransferManualService.findBidTransferManualList(investCdt);
					boolean transferingFlag = true;
					for (BidTransferManual bidTransfer:investTransferList){
						if(bidTransfer.getId()!=transferManual.getId()){
							if(DateUtils.getDaysBetween(bidTransfer.getCreateTime(),new Date()) <= bidTransfer.getTransferDays()){
								transferingFlag = false ;
								break;
							}
						}
					}
					if (transferingFlag){
						//需要查询这个投资记录购买债权过来的
						BidInvest updateInvest = new BidInvest();
						updateInvest.setId(transferManual.getOldInvestId());
						BidTransferManual  newTransfer  = bidTransferManualService.findBidTransferByNewInvestId(transferManual.getOldInvestId());
						if (newTransfer!=null){
							updateInvest.setState(InvestConstants.INVEST_STATE_SUCCESS_BUYER);
						}else{
							updateInvest.setState(InvestConstants.INVEST_STATE_SUCCESS);
						}
						updateInvestList.add(updateInvest);
					}
				}
			});
		}
		if (!transferIds.isEmpty()){
			bidTransferManualService.dealTransferTimeOut(transferIds,updateInvestList);
		}
	}

}
