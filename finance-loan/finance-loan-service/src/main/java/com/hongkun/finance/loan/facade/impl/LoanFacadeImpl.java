package com.hongkun.finance.loan.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.service.ConTypeService;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.loan.facade.util.RepayCalcInterestUtil;
import com.hongkun.finance.loan.facade.util.RepayPlanUtils;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.*;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.loan.util.RepayAndReceiptUtils;
import com.hongkun.finance.payment.service.FinAccountService;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.point.support.CalcPointUtils;
import com.hongkun.finance.qdz.service.QdzTransferService;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosInfo;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.swing.text.PlainDocument;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.IS_PUNISH_STATE;

/**
 * @Description   : 放款服务通用接口实现类
 * @Project       : finance-loan-service
 * @Program Name  : com.hongkun.finance.loan.facade.impl.LoanFacadeImpl.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@Service
public class LoanFacadeImpl implements LoanFacade{

    private static final Logger logger = LoggerFactory.getLogger(LoanFacadeImpl.class);
	@Autowired
	private BidReceiptPlanService bidReceiptPlanService;
	@Autowired
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private QdzTransferService qdzTransferService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private ConTypeService conTypeService;
	@Reference
	private DicDataService dicDataService;
    @Reference
	private BidInvestService bidInvestService;
	@Reference
	private VasCouponDetailService vasCouponDetailService;
	@Reference
	private PointRuleService pointRuleService;

	@Override
	public Pager findPlanCountList(Pager pager, LoanVO loanVO) {
		this.dealSelectItems(loanVO);
		Pager result;
		if(loanVO.getPlanType() == 1){
			result = this.bidRepayPlanService.findRepayPlanCountList(pager, loanVO);
		}else{
			result = this.bidReceiptPlanService.findReceiptPlanCountList(pager, loanVO);
		}
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
            //获得所有标的的id
            List<Integer> ids = result.getData().stream().map(tmp -> ((LoanVO)tmp).getBidId()).collect(Collectors.toList());
            BidInfo bidInfo = new BidInfo();
            bidInfo.setIds(ids);
            List<BidInfo> bidInfoList = this.bidInfoService.findBidInfoList(bidInfo);
            //获得所有的投资id
            result.getData().forEach(vo -> {
                BeanPropertiesUtil.mergeProperties(vo, bidInfoList.stream().filter(e -> e.getId().equals(((LoanVO)vo).getBidId())).findAny().get());
            });
            if(loanVO.getPlanType() == 2){
                List<Integer> investIds = result.getData().stream().map(tmp -> ((LoanVO)tmp).getInvestId()).collect(Collectors.toList());
                BidInvest bidInvest = new BidInvest();
                bidInvest.setIds(investIds);
                List<BidInvest> bidInvestList = this.bidInvestService.findBidInvestList(bidInvest);
				bidInvestList.forEach(e -> {
					if (CommonUtils.gtZero(e.getCouponIdJ())) {
						// 查询加息券的值
						VasCouponDetail vasDetail = vasCouponDetailService.findVasCouponDetailById(e.getCouponIdJ());
						e.setCouponWorthJ(vasDetail.getWorth());
					}
				});
                //获得所有的投资id
                result.getData().forEach(vo -> {
                    BeanPropertiesUtil.mergeProperties(vo, bidInvestList.stream().filter(e -> e.getId().equals(((LoanVO)vo).getInvestId())).findAny().get());
                });
            }
        }
        return result;
	}

	@Override
	public Pager findPlanList(Pager pager, LoanVO loanVO) {
		Pager result;
		if(loanVO.getPlanType() == 1){//查询还款计划
			BidRepayPlan bidRepayPlan = new BidRepayPlan();
			BeanPropertiesUtil.splitProperties(loanVO, bidRepayPlan);
			result = this.bidRepayPlanService.findBidRepayPlanList(bidRepayPlan, pager);
		}else{	//查询回款计划
			BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
			BeanPropertiesUtil.splitProperties(loanVO, bidReceiptPlan);
			result = this.bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlan, pager);
		}
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
			List<Integer> ids = result.getData().stream().map(tmp -> {
				if(loanVO.getPlanType() == 1){//还款计划
					return ((BidRepayPlan)tmp).getRegUserId();
				}
				return ((BidReceiptPlan)tmp).getRegUserId();
			}).collect(Collectors.toList());
			UserVO userVO = new UserVO();
			userVO.setUserIds(ids);
			List<UserVO> userVOList = this.regUserService.findUserWithDetailByInfo(userVO);
			//组装还款计划和用户信息的数据
			List<LoanVO> list = new ArrayList<>();
			result.getData().forEach(plan -> {
				LoanVO vo = new LoanVO();
				Integer userId;
				if(loanVO.getPlanType() == 1){
					userId = ((BidRepayPlan)plan).getRegUserId();
				}else{
					userId = ((BidReceiptPlan)plan).getRegUserId();
				}
				BeanPropertiesUtil.mergeProperties(vo, plan,
						userVOList.stream().filter(e -> e.getUserId().equals(userId)).findAny().get());
				list.add(vo);
			});
			result.setData(list);
		}
		return result;
	}

	@Override
	public ResponseEntity<?> findPlanListWithStatistics(Pager pager, LoanVO loanVO) {
		if(!this.dealSelectItems(loanVO)) {
			return BaseUtil.emptyResult();
		}
		if(loanVO.getCreateTimeEnd() != null){
		    loanVO.setCreateTimeEnd(loanVO.getCreateTimeEnd().split(" +")[0] + " 23:59:59");
        }
		if(loanVO.getPlanType() == 1){	//还款计划
			return this.doFindRepayPlanListWithStatistics(pager, loanVO);
		}else{							//回款计划
			return this.doFindReceiptPlanListWithStatistics(pager, loanVO);
		}
	}

	/**
	 *
	 *  @Description    : 查询回款计划（我的账户-回款计划）
	 *  @Method_Name    : doFindReceiptPlanListWithStatistics
	 *  @param pager
	 *  @param loanVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月4日 下午4:27:10
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> doFindReceiptPlanListWithStatistics(Pager pager, LoanVO loanVO) {
		BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
		BeanPropertiesUtil.splitProperties(loanVO, bidReceiptPlan);
		if(StringUtils.isNotBlank(loanVO.getCreateTimeBegin())) {
			bidReceiptPlan.setPlanTimeBegin(DateUtils.parse(loanVO.getCreateTimeBegin()));
		}
		if(StringUtils.isNotBlank(loanVO.getCreateTimeEnd())) {
			bidReceiptPlan.setPlanTimeEnd(DateUtils.parse(loanVO.getCreateTimeEnd()));
		}
//		ResponseEntity<?> responseEntity = this.bidReceiptPlanService.findReceiptPlanListWithStatistics(pager, bidReceiptPlan);
        bidReceiptPlan.setSortColumns("create_time ASC,periods ASC");
		Pager result = bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlan, pager);
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
			//获得所有标的的idbi
			List<Integer> ids = result.getData().stream().map(tmp -> ((BidReceiptPlan)tmp).getBidId()).distinct().collect(Collectors.toList());
			BidInfoVO bidInfoVO = new BidInfoVO();
			bidInfoVO.setBidLimitIds(ids);
			List<BidInfoVO> bidInfoVOList = this.bidInfoService.findBidInfoVoList(bidInfoVO);
			List<LoanVO> list = new ArrayList<>();
			result.getData().forEach(plan -> {
				BidReceiptPlan planTmp = (BidReceiptPlan)plan;
				LoanVO vo = new LoanVO();
				vo.setId(planTmp.getId());
				BeanPropertiesUtil.mergeProperties(vo, plan);
				//设置标的信息
				BidInfoVO bidVO = bidInfoVOList.stream().filter(e -> e.getId().equals(planTmp.getBidId())).findAny().orElse(new BidInfoVO());
				vo.setBidName(bidVO.getTitle());
				vo.setProductType(bidVO.getProductType());
				list.add(vo);
			});
			result.setData(list);
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	/**
	 *  @Description    : 查询还款计划（我的账户-还款计划）
	 *  @Method_Name    : doFindRepayPlanListWithStatistics
	 *  @param pager
	 *  @param loanVO
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年1月4日 下午4:27:48
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> doFindRepayPlanListWithStatistics(Pager pager, LoanVO loanVO) {
		if(!this.dealSelectItems(loanVO)) {
			return BaseUtil.emptyResult();
		}
		BidRepayPlan bidRepayPlan = new BidRepayPlan();
		BeanPropertiesUtil.splitProperties(loanVO, bidRepayPlan);
		if(bidRepayPlan.getState() == -999){
            bidRepayPlan.setStates(Arrays.asList(RepayConstants.REPAY_STATE_FINISH, RepayConstants.REPAY_STATE_NONE,
                    RepayConstants.REPAY_STATE_OVERDUE));
        }else if(bidRepayPlan.getState() == RepayConstants.REPAY_STATE_FINISH){
            bidRepayPlan.setStates(Arrays.asList(RepayConstants.REPAY_STATE_FINISH, RepayConstants.REPAY_STATE_OVERDUE));
        }
		ResponseEntity<?> responseEntity = this.bidRepayPlanService.findRepayPlanListWithStatistics(pager, bidRepayPlan);
		Pager result = (Pager) responseEntity.getResMsg();
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
			//获得所有标的的id
			List<Integer> ids = result.getData().stream().map(tmp -> ((BidRepayPlan)tmp).getBidId()).distinct().collect(Collectors.toList());
			BidInfoVO bidInfoVO = new BidInfoVO();
			bidInfoVO.setBidLimitIds(ids);
			List<BidInfoVO> bidInfoVOList = this.bidInfoService.findBidInfoVoList(bidInfoVO);
			List<LoanVO> list = new ArrayList<>();
			result.getData().forEach(plan -> {
				BidRepayPlan planTmp = (BidRepayPlan)plan;
				LoanVO vo = new LoanVO();
				vo.setId(planTmp.getId());
				//剩余还款本金
				vo.setResidueAmount(planTmp.getResidueAmount());
				BeanPropertiesUtil.mergeProperties(vo, plan);
				//设置标的信息
				BidInfoVO bidVO = bidInfoVOList.stream().filter(e -> e.getId().equals(planTmp.getBidId())).findAny().orElse(new BidInfoVO());
				vo.setBidName(bidVO.getTitle());
				vo.setProductType(bidVO.getProductType());
				//设置代扣标识，允许代扣且是第一期还款计划
				if(bidVO.getWithholdState() != null && bidVO.getWithholdState() == 1 && planTmp.getPeriods() == 1) {
					vo.setAllowWithholdFlag(1);
				}
				//设置提前还款标识
				if(bidVO.getAdvanceRepayState() == 1) {
					if(DateUtils.getDaysBetween(new Date(), bidVO.getLendingTime()) >= bidVO.getReturnCapDays()) {
						vo.setAdvanceRepayFlag(1);
					}
					if(planTmp.getPlanTime().before(new Date())){
					    vo.setAdvanceRepayFlag(2);
                    }
				}
				vo.setPunishAmount(planTmp.getPunishAmount());
                // 判断是否逾期，计算罚息
				BigDecimal punishAmount = this.calcPunishAmount(planTmp, bidVO);
				if(CompareUtil.gtZero(punishAmount) && planTmp.getState() == RepayConstants.REPAY_STATE_NONE){
                    vo.setPunishAmount(punishAmount);
                    vo.setAdvanceRepayFlag(0);
                }
				list.add(vo);
			});
			//标的对应的当前的应还款回款期
			List<Map<String, Object>> currPlanMap = this.bidRepayPlanService.findCurrRepayPlanIdByBidIds(ids, loanVO.getUserId());
			//设置还款按钮显示标识
			list.stream().forEach(p -> {
				currPlanMap.forEach(m -> {
					if((p.getBidId().equals((Integer)m.get("bidId"))) && p.getPeriods().equals(m.get("periods"))) {
						p.setCurrRepayFlag(1);
					}
				});
			});
			result.setData(list);
		}
		return responseEntity;
	}

	/**
	 *  @Description    : 处理查询查询条件
	 *  @Method_Name    : dealSelectItems
	 *  @param loanVO
	 *  @return         : void
	 *  @Creation Date  : 2017年12月26日 上午9:12:43
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private boolean dealSelectItems(LoanVO loanVO) {
		if(StringUtils.isNotBlank(loanVO.getBidName())){
			BidInfo bidInfo = new BidInfo();
			bidInfo.setName(loanVO.getBidName());
			List<BidInfo> list = this.bidInfoService.findBidInfoList(bidInfo);
			if(list.isEmpty()) {
				return false;
			}
			list.forEach(e -> loanVO.getBidIds().add(e.getId()));
		}
		return true;
	}

    @Override
    public Map<String, Object> findReceiptPlanList(Integer regUserId) {
        BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
        bidReceiptPlan.setRegUserId(regUserId);
        Map<String, Object> map = this.bidReceiptPlanService.findReceiptPlanListStatistics(bidReceiptPlan);
       //查询钱袋子数据
        Map<String, Object> qdzMap = this.qdzTransferService.findQdzReceiptInfo(regUserId);
        if(CompareUtil.gtZero((BigDecimal)qdzMap.get("waitAmountSum"))) {
            qdzMap.put("title", "钱袋子");
            //更新总待回收数据
            map.put("amountSum", ((BigDecimal)map.get("amountSum")).add((BigDecimal)qdzMap.get("waitAmountSum")));
            map.put("capitalSum", ((BigDecimal)map.get("capitalSum")).add((BigDecimal)qdzMap.get("waitCapitalSum")));
            map.put("interestSum", ((BigDecimal)map.get("interestSum")).add((BigDecimal)qdzMap.get("waitInterestSum")));
            map.put("increaseSum", ((BigDecimal)map.get("increaseSum")).add((BigDecimal)qdzMap.get("waitIncreaseSum")));
        }
        map.put("qdz", qdzMap);
        List<Map<String, Object>> result = new ArrayList<>();
        //查询回款中、直投的对应的回款计划
        BidInvestDetailVO cdt = new BidInvestDetailVO();
        cdt.setInvestChannel(InvestConstants.BID_INVEST_CHANNEL_IMMEDIATE);
        cdt.setRegUserId(regUserId);
        cdt.setBidState(InvestConstants.BID_STATE_WAIT_REPAY);
        cdt.setBidInvestStates(Arrays.asList(InvestConstants.INVEST_STATE_SUCCESS, InvestConstants.INVEST_STATE_MANUAL, InvestConstants.INVEST_STATE_TRANSFER,InvestConstants.INVEST_STATE_SUCCESS_BUYER));
        cdt.setSortColumns("b1.create_time ASC");
        List<BidInvestDetailVO> bidInvestList = this.bidInvestService.findBidInvestDetailList(cdt);
        List<Map<String, Object>> list = this.bidReceiptPlanService.findReceiptPlanCountDetail(regUserId);
        if (CommonUtils.isNotEmpty(bidInvestList)) {
            bidInvestList.forEach(investTmp -> {
                list.stream().filter(receiptTmp -> Objects.equals(investTmp.getBidInfoId(), receiptTmp.get("bidId")) && Objects.equals(investTmp.getBidInvestId(), receiptTmp.get("investId"))).findAny().ifPresent(receiptTmp -> {
                    receiptTmp.put("investAmount", investTmp.getInvestAmount().subtract(investTmp.getTransAmount()));
                    receiptTmp.put("title", investTmp.getBidName());
                    result.add(receiptTmp);
                });
            });
        }
        map.put("dataList", result);
        return map;
    }

    @Override
    public ResponseEntity<?> findReceiptPlanDetailList(Integer regUserId, Integer bidId, Integer investId) {
	    ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
		bidReceiptPlan.setBidId(bidId);
		bidReceiptPlan.setRegUserId(regUserId);
		bidReceiptPlan.setInvestId(investId);
		List<BidReceiptPlan> list = this.bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlan);
		list.sort(Comparator.comparing(BidReceiptPlan::getState).thenComparing(BidReceiptPlan::getPeriods));
		//计算预期收益
		BigDecimal exceptAmount = BigDecimal.ZERO;
		for (BidReceiptPlan receiptPlan : list) {
			exceptAmount = exceptAmount.add(receiptPlan.getIncreaseAmount()).add(receiptPlan.getInterestAmount());
		}
		result.getParams().put("exceptAmount", exceptAmount);
		//计算完成时间
		BidInfo bidInfo = this.bidInfoService.findBidInfoById(bidId);
		result.getParams().put("finishTime", BidInfoUtil.getFinishTime(bidInfo.getLendingTime(), bidInfo.getTermValue(), bidInfo.getTermUnit()));
		result.getParams().put("lendingTime", bidInfo.getLendingTime());
		//一次本息的回款计划为1条
		result.getParams().put("periods", bidInfo.getBiddRepaymentWay().equals(InvestConstants.REPAYTYPE_ONECE_REPAYMENT)
				? 1 : BidInfoUtil.getPeriods(bidInfo.getTermValue(), bidInfo.getTermUnit()));
		result.getParams().put("bidRepaymentWayView", this.dicDataService.findNameByValue("invest", "bid_repayment", bidInfo.getBiddRepaymentWay()));
		result.getParams().put("title", bidInfo.getName());
		result.getParams().put("dataList", list);
        return result;
    }

	@Override
	public ResponseEntity<?> findRepayPlanDetailList(Integer regUserId, Integer bidId) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		BidInfoVO vo = new BidInfoVO();
		vo.setBidId(bidId);
		vo.setBidLimitIds(Collections.singletonList(bidId));
		vo.setBorrowerIds(Collections.singletonList(regUserId));
		//只查询还款中的标的
		vo.setState(InvestConstants.BID_STATE_WAIT_REPAY);
		List<BidInfoVO> list = this.bidInfoService.findBidInfoVoList(vo);
		if(CommonUtils.isEmpty(list)) {
			return new ResponseEntity<>(Constants.ERROR, "未找到对应的还款计划");
		}
		BidInfoVO bidInfoVo = list.get(0);
		result.getParams().put("title", bidInfoVo.getTitle());
		result.getParams().put("lendingTime", bidInfoVo.getLendingTime());
		//处理合同信息
        result.getParams().put("contract", this.conTypeService.findContractInfo(bidInfoVo.getContract()));
		BidRepayPlan bidRepayPlan = new BidRepayPlan();
		bidRepayPlan.setBidId(bidId);
		bidRepayPlan.setRegUserId(regUserId);
		List<BidRepayPlan> planList = this.bidRepayPlanService.findBidRepayPlanList(bidRepayPlan);
		if(CommonUtils.isEmpty(planList)) {
			return new ResponseEntity<>(Constants.ERROR, "未找到对应的还款计划");
		}
		result.getParams().put("periodsTotal", planList.stream().mapToInt(BidRepayPlan::getPeriods).distinct().count());
		//代还款金额
        result.getParams().put("capitalAmount", bidInfoVo.getTotalAmount().subtract(bidInfoVo.getReturnCapAmount()));
        //组装还款数据
		List<LoanVO> loanVOList = this.fitRepayPlanData(planList, bidInfoVo);
        //按还款状态排序-1 1 2 4 8
        loanVOList.sort(Comparator.comparing(LoanVO::getState));
        result.getParams().put("advanceRepayState", loanVOList.stream().anyMatch(o -> o.getAdvanceRepayFlag() == 1 && o.getState() == RepayConstants.REPAY_STATE_NONE_BTN) ? 1 : 0);
        loanVOList.stream().filter(o -> o.getState() == RepayConstants.REPAY_STATE_NONE_BTN).findAny().ifPresent(l -> result.getParams().put("advanceRepayPlanId", l.getId()));
		result.getParams().put("dataList", loanVOList);
        result.getParams().put("residueRepayAmount", loanVOList.stream().filter(o -> o.getState().equals(RepayConstants.REPAY_STATE_NONE) || o.getState().equals(RepayConstants.REPAY_STATE_NONE_BTN)).mapToDouble(e -> e.getAmount().doubleValue()).sum());
		return result;
	}

	/**
	 *  @Description    : 封装还款数据
	 *  @Method_Name    : fitRepayPlanData
	 *  @param bidRepayPlanList
	 *  @param bidVO
	 *  @return         : List<LoanVO>
	 *  @Creation Date  : 2018年3月15日 上午10:48:20
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private List<LoanVO> fitRepayPlanData(List<BidRepayPlan> bidRepayPlanList, BidInfoVO bidVO) {
		List<LoanVO> list = new ArrayList<>();
		//过滤掉提前还本的记录
		bidRepayPlanList.stream().filter(o -> o.getState() != RepayConstants.REPAY_STATE_ADVANCE_CAPITAL).forEach(planTmp -> {
			LoanVO vo = new LoanVO();
			vo.setId(planTmp.getId());
			//剩余还款本金
			vo.setResidueAmount(planTmp.getResidueAmount());
			BeanPropertiesUtil.mergeProperties(vo, planTmp);
			vo.setBidName(bidVO.getTitle());
			vo.setProductType(bidVO.getProductType());
			//设置提前还款标识，允许提前还款条件，放款N天，没有逾期，不是还款日
			if(bidVO.getAdvanceRepayState() == 1) {
				if(DateUtils.getDaysBetween(new Date(), bidVO.getLendingTime()) >= bidVO.getReturnCapDays() &&
                        DateUtils.addDays(planTmp.getPlanTime(), -1).after(new Date())) {
//                    DateUtils.addMonth(planTmp.getPlanTime(), -1).before(new Date())
					vo.setAdvanceRepayFlag(1);
				}
			}
			// 计算罚息
			BigDecimal punishAmount;
			// 计算逾期天数
			int lateDay = RepayCalcInterestUtil.calcPunishDays(planTmp.getPlanTime(), planTmp.getPeriods(), bidVO.getTermUnit(), bidVO.getTermValue());
			if (bidVO.getPunishState() != null && bidVO.getPunishState().equals(IS_PUNISH_STATE) && lateDay > 0
					&& planTmp.getPlanTime().before(new Date()) && planTmp.getState() == RepayConstants.REPAY_STATE_NONE) {
				punishAmount = CalcInterestUtil.calcOverdue(bidVO.getTotalAmount().subtract(Optional.ofNullable(bidVO.getReturnCapAmount()).orElse(BigDecimal.ZERO)), lateDay,
						bidVO.getAdvanceServiceRate(), bidVO.getLiquidatedDamagesRate());
				vo.setPunishAmount(punishAmount);
				//还款前动态加上逾期金额
                if(vo.getState() == RepayConstants.REPAY_STATE_NONE){
				    vo.setAmount(vo.getAmount().add(punishAmount));
                }
				vo.setAdvanceRepayFlag(0);
				vo.setPunishDays(lateDay);
			}
			//如果是逾期还款保存逾期天数
			if(planTmp.getState().equals(RepayConstants.REPAY_STATE_OVERDUE)){
                vo.setPunishDays(DateUtils.getDaysBetween(planTmp.getActualTime(), planTmp.getPlanTime()));
            }
			list.add(vo);
		});
		//设置还款按钮显示标识
		Map<Integer, Optional<LoanVO>> map = list.stream().filter(o -> o.getState() == 1).collect(Collectors.groupingBy(LoanVO::getBidId, Collectors.minBy(Comparator.comparing(LoanVO::getPeriods))));
		map.forEach((k, v) -> 
            list.forEach(p -> 
                v.ifPresent(n -> {
                    if(n.getId().equals(p.getId()) && n.getPeriods().equals(p.getPeriods())) {
                        p.setCurrRepayFlag(1);
                        p.setState(RepayConstants.REPAY_STATE_NONE_BTN);
                    }
                })
            )
        );
		return list;
	}

    @Override
    public void findRepayPlanAndNotice() {
        //查询需要还款的列表
        List<BidRepayPlan> list = this.bidRepayPlanService.findNeedNoticeRepayPlanList();
        if(list == null || list.isEmpty()){
            logger.info("未找到需要提醒还款的列表");
            return;
        }
        Map<Integer, List<BidRepayPlan>> map = list.stream().collect(Collectors.groupingBy(BidRepayPlan::getRegUserId));
        //查询黑白名单中还款短信通知人员
        RosInfo cdt = new RosInfo();
        cdt.setType(RosterType.SMS_REAPY_NOTICE.getValue());
        List<RosInfo> rosInfoList = Optional.ofNullable(this.rosInfoService.findRosInfoList(cdt)).orElse(new ArrayList<>());
        //查询用户信息
        List<Integer> userIds = list.stream().map(BidRepayPlan::getRegUserId).collect(Collectors.toList());
        userIds.addAll(rosInfoList.stream().map(RosInfo::getRegUserId).collect(Collectors.toList()));
        userIds = userIds.stream().distinct().collect(Collectors.toList());

        RegUser regUser = new RegUser();
        regUser.setUserIds(userIds);
        List<RegUser> regUserList = this.regUserService.findRegUserList(regUser);
        //查询资金账户信息
//        List<FinAccount> finAccountList = Optional.ofNullable(this.finAccountService.findFinAccountByRegUserIds(userIds)).orElse(new ArrayList<>());

        List<SmsMsgInfo> smsMsgInfoList = new ArrayList<>();
        Date sendTime = DateUtils.parse(DateUtils.getCurrentDate(DateUtils.DATE) + " 10:00:00", DateUtils.DATE_HH_MM_SS);
        map.forEach((regUserId, bidRepayPlanList) -> {
            if(rosInfoList.stream().anyMatch(o -> o.getRegUserId().equals(regUserId) && o.getFlag().equals(RosterFlag.BLACK.getValue()))){
                return;
            }
            StringBuilder sb = new StringBuilder();
            bidRepayPlanList.forEach(o -> {
                sb.append(DateUtils.format(o.getPlanTime(), DateUtils.DATE)).append("日还款").append(o.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).append("元、");
            });
            if(sb.toString().endsWith("、")){
                long login = regUserList.stream().filter(o -> o.getId().equals(regUserId)).findAny().get().getLogin();
                String tel = String.valueOf(login);
//                SmsTelMsg smsTelMsg = new SmsTelMsg(regUserId, login, SmsMsgTemplate.MSG_REPAY_NOTICE.getMsg(), SmsConstants
//                        .SMS_TYPE_SYS_NOTICE, new Object[]{tel.substring(0, 3) + "****" + tel.substring(7, 11),
//                        bidRepayPlanList.size(), sb.toString().substring(0, sb.length() - 1), finAccountList.stream().filter(o -> o.getRegUserId().equals(regUserId)).findAny().get().getUseableMoney()});
                SmsTelMsg smsTelMsg = new SmsTelMsg(regUserId, login, SmsMsgTemplate.MSG_REPAY_NOTICE.getMsg(), SmsConstants
                        .SMS_TYPE_SYS_NOTICE, new Object[]{bidRepayPlanList.size(), sb.toString().substring(0, sb.length() - 1)});
                smsTelMsg.setSendTime(sendTime);
                smsMsgInfoList.add(smsTelMsg);

            }
        });
        if(!smsMsgInfoList.isEmpty()){
			rosInfoList.stream().filter(o -> o.getFlag().equals(RosterFlag.WHITE.getValue())).forEach(o -> {
                SmsTelMsg smsTelMsg = new SmsTelMsg(o.getRegUserId(),
                        regUserList.stream().filter(e -> e.getId().equals(o.getRegUserId())).findAny().get().getLogin(),
                        SmsMsgTemplate.MSG_REPAY_NOTICE_ADMIN.getMsg(), SmsConstants.SMS_TYPE_NOTICE,
                        new Object[] { map.size(), DateUtils.format(DateUtils.addDays(new Date(), 3), DateUtils.DATE_HH_MM) });
                smsTelMsg.setSendTime(sendTime);
                smsMsgInfoList.add(smsTelMsg);
			});
            SmsSendUtil.sendSmsMsgToQueue(smsMsgInfoList);
        }
    }

    @Override
    public Map<String, Object> findReceiptPlanCalendar(Integer regUserId, String month) {
        Map<String, Object> map = new HashMap<>();
	    BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
	    bidReceiptPlan.setRegUserId(regUserId);
	    bidReceiptPlan.setPlanTimeBegin(DateUtils.parse(month + " 00:00:00", DateUtils.DATE_HH_MM_SS));
	    bidReceiptPlan.setPlanTimeEnd(DateUtils.getLastDayOfMonth(DateUtils.parse(month, DateUtils.DATE)));
	    bidReceiptPlan.setState(RepayConstants.RECEIPT_STATE_NONE);
	    List<BidReceiptPlan> bidReceiptPlanList = this.bidReceiptPlanService.findBidReceiptPlanList(bidReceiptPlan);
        map.put("amount", 0);
        map.put("planCount", 0);
	    if(CommonUtils.isNotEmpty(bidReceiptPlanList)){
	        BidInfo bidInfo = new BidInfo();
	        bidInfo.setIds(bidReceiptPlanList.stream().map(BidReceiptPlan::getBidId).distinct().collect(Collectors.toList()));
	        List<BidInfo> bidInfoList = this.bidInfoService.findBidInfoList(bidInfo);
            map.put("amount", bidReceiptPlanList.stream().mapToDouble(o -> o.getAmount().doubleValue()).sum());
            map.put("planCount", bidReceiptPlanList.size());
            List<LoanVO> loanVOList = new ArrayList<>();
            bidReceiptPlanList.forEach(o -> {
                LoanVO loanVO = new LoanVO();
                BeanPropertiesUtil.mergeProperties(loanVO, o);
                bidInfoList.stream().filter(e -> e.getId().equals(o.getBidId())).findAny().ifPresent(b -> loanVO.setBidName(b.getName()));
                loanVOList.add(loanVO);
            });
            map.put("list", loanVOList);
        }
        map.put("month", month.split("-")[1]);
        return map;
    }

    @Override
    public ResponseEntity<?> findRepayPlanListCount(Integer regUserId, Pager pager) {
        BidInfoVO vo = new BidInfoVO();
        vo.setBorrowerIds(Arrays.asList(regUserId));
        vo.setState(InvestConstants.BID_STATE_WAIT_REPAY);
        vo.setSortColumns("lending_time ASC");
        BidRepayPlan bidRepayPlan = new BidRepayPlan();
        bidRepayPlan.setRegUserId(regUserId);
        bidRepayPlan.setState(RepayConstants.REPAY_STATE_NONE);
        ResponseEntity<?> result = this.bidRepayPlanService.countRepayPlanAmount(bidRepayPlan);
        Pager p = this.bidInfoService.findBidInfoDetailVoList(pager, vo);
        List<Map<String, Object>> dataLiat = new ArrayList<>();
        if(p != null && CommonUtils.isNotEmpty(p.getData())){
            List<BidInfoVO> bidInfoVOList = (List<BidInfoVO>) p.getData();
            List<Integer> bidIds = bidInfoVOList.stream().map(BidInfoVO::getId).collect(Collectors.toList());
            //查询总的还款金额和最近一期的还款时间
            List<Map<String, Object>> list = this.bidRepayPlanService.findCurrRepayPlanIdByBidIds(bidIds, regUserId);
            bidInfoVOList.forEach(o -> {
                Map<String, Object> param = new HashMap<>();
                param.put("title", o.getTitle());
                param.put("interestRate", o.getInterestRate());
                param.put("id", o.getId());
                param.put("money", o.getTotalAmount());
                param.put("state", o.getState());
                param.put("advanceRepayState", o.getAdvanceRepayState());
//                param.put("planTime", list.stream().filter(m -> o.getId().equals(m.get("bidId"))).findAny().orElse(new HashMap<>()).get("planTime"));
                param.put("lendingTime", o.getLendingTime());
                dataLiat.add(param);
            });
            result.getParams().put("dataList", dataLiat);
        }
        return result;
    }

    @Override
    public ResponseEntity findRepayPlanListForManageStatistics(Pager pager, BidRepayPlanVo bidRepayPlanVo) {
		// 条件查询标的列表
        if(StringUtils.isNotBlank(bidRepayPlanVo.getBidName())){
            BidInfo bidInfo = new BidInfo();
            bidInfo.setName(bidRepayPlanVo.getBidName());
            List<BidInfo> list = this.bidInfoService.findBidInfoList(bidInfo);
            if(list.isEmpty()) {
                return BaseUtil.emptyResult();
            }
            list.forEach(e -> bidRepayPlanVo.getBidIds().add(e.getId()));
        }
		if(StringUtils.isNotBlank(bidRepayPlanVo.getLogin()) || StringUtils.isNotBlank(bidRepayPlanVo.getRealName())){
            // 条件查询用户
            UserVO userVO = new UserVO();
            userVO.setRealName(bidRepayPlanVo.getRealName());
            if(StringUtils.isNotBlank(bidRepayPlanVo.getLogin())){
                userVO.setLogin(Long.valueOf(bidRepayPlanVo.getLogin()));
            }
            List<Integer> userIds = regUserService.findUserIdsByUserVO(userVO);
            if(CollectionUtils.isEmpty(userIds)){
                return BaseUtil.emptyResult();
            }
            bidRepayPlanVo.setRegUserIds(userIds);
        }
		Pager result = bidRepayPlanService.findRepayPlanListForManageStatistics(pager, bidRepayPlanVo);
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
            List<Integer> bidList = result.getData().stream().map(o -> ((BidRepayPlanVo)o).getBidId()).collect(Collectors.toList());
		    Map<Integer, BidInfoVO> bidMap = this.bidInfoService.findBidInfoDetailVoByIdList(new HashSet<>(bidList));
			result.getData().forEach(planTemp -> {
				BidRepayPlanVo planVo = (BidRepayPlanVo)planTemp;
                final Integer regUserId = planVo.getRegUserId();
                RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
                planVo.setLogin(String.valueOf(regUser.getLogin()));
                RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUserId, ()-> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId));
                planVo.setRealName(regUserDetail.getRealName());
				BigDecimal punishAmount;
				if(bidMap.containsKey(planVo.getBidId())){
                    BidInfoVO bidInfoVo = bidMap.get(planVo.getBidId());
                    planVo.setTotalPeriods(BidInfoUtil.getPeriods(bidInfoVo.getTermValue(),bidInfoVo.getTermUnit()));
                    planVo.setBidName(bidInfoVo.getTitle());
                    // 计算逾期天数
                    int lateDay = DateUtils.getDaysBetween(planVo.getPlanTime(), new Date());
                    if (bidInfoVo.getPunishState() != null && bidInfoVo.getPunishState() == IS_PUNISH_STATE && lateDay > 0
                            && planVo.getPlanTime().before(new Date())) {
                        punishAmount = CalcInterestUtil.calcOverdue(bidInfoVo.getTotalAmount(), lateDay,
                                bidInfoVo.getAdvanceServiceRate(), bidInfoVo.getLiquidatedDamagesRate());
                        planVo.setPunishAmount(punishAmount);
                        planVo.setAmount(planVo.getAmount().add(punishAmount));
                    }
                }
			});
		}
		return new ResponseEntity<>(Constants.SUCCESS,result);
    }


	@Override
    public ResponseEntity findReceiptPlanListForManageStatistics(Pager pager, BidReceiptPlanVo bidReceiptPlanVo) {
        bidReceiptPlanVo.setState(RepayConstants.REPAY_STATE_NONE);
	    if(StringUtils.isNotBlank(bidReceiptPlanVo.getLogin()) || StringUtils.isNotBlank(bidReceiptPlanVo.getRealName())){
            UserVO userVO = new UserVO();
            userVO.setRealName(bidReceiptPlanVo.getRealName());
            if(StringUtils.isNotEmpty(bidReceiptPlanVo.getLogin())){
                userVO.setLogin(Long.valueOf(bidReceiptPlanVo.getLogin()));
            }
            List<Integer> userIds = regUserService.findUserIdsByUserVO(userVO);
            if(CollectionUtils.isEmpty(userIds)){
                return BaseUtil.emptyResult();
            }
            bidReceiptPlanVo.setRegUserIds(userIds);
        }
        long start = System.currentTimeMillis();
        ResponseEntity<?> responseEntity = bidReceiptPlanService.findReceiptPlanListForManageStatistics(pager,bidReceiptPlanVo);
        Pager result = (Pager) responseEntity.getResMsg();
        if(result != null && CommonUtils.isNotEmpty(result.getData())){
            List<Integer> regUserIds = result.getData().stream().map(o -> ((BidReceiptPlanVo)o).getRegUserId()).collect(Collectors.toList());
            Map<Integer, RegUser> regUserMap = BaseUtil.getObjects(regUserIds, RegUser.class, this.regUserService::findRegUserByIds);
            Map<Integer, RegUserDetail> regUserDetailMap = BaseUtil.getObjects(regUserIds, RegUserDetail.class, this.regUserService::findRegUserDetailByIds);
            result.getData().forEach(planTemp -> {
                BidReceiptPlanVo planVo = (BidReceiptPlanVo)planTemp;
                //处理加息
                planVo.setAmount(planVo.getAmount().add(planVo.getIncreaseAmount()));
                final Integer regUserId = planVo.getRegUserId();
                RegUser regUser = regUserMap.get(regUserId);
                planVo.setLogin(String.valueOf(regUser.getLogin()));
                RegUserDetail regUserDetail = regUserDetailMap.get(regUserId);
                planVo.setRealName(regUserDetail.getRealName());
            });
        }
        return responseEntity;
    }

	@Override
	public ResponseEntity findReceiptPlanListByUserId(Pager pager, Integer regUserId) {
		BidReceiptPlanVo bidReceiptPlanVo = new BidReceiptPlanVo();
		bidReceiptPlanVo.setRegUserId(regUserId);
		bidReceiptPlanVo.setSortColumns("id desc");
		Pager result = bidReceiptPlanService.findBidReceiptPlanListByUserId(bidReceiptPlanVo,pager);
		if(result != null && CommonUtils.isNotEmpty(result.getData())){
            List<Integer> bidInfos = result.getData().stream().map(o -> ((BidReceiptPlanVo)o).getBidId()).collect(Collectors.toList());
            Map<Integer, BidInfoVO> bidInfoVOMap = this.bidInfoService.findBidInfoDetailVoByIdList(new HashSet<>(bidInfos));
			result.getData().forEach(planTemp -> {
				BidReceiptPlanVo planVo = (BidReceiptPlanVo) planTemp;
				//处理加息
				planVo.setAmount(planVo.getAmount().add(planVo.getIncreaseAmount()));
				BidInfoVO bidInfoVo = bidInfoVOMap.get(planVo.getBidId());
				if(null != bidInfoVo){
					planVo.setBidName(bidInfoVo.getTitle());
					planVo.setBidAmount(bidInfoVo.getTotalAmount());
				}
                RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(bidInfoVo.getBorrowerId(), () -> this.regUserDetailService.findRegUserDetailByRegUserId(bidInfoVo.getBorrowerId()));
                if(null != regUserDetail){
                    planVo.setBorrowerName(regUserDetail.getRealName());
                }
			});

		}
		return new ResponseEntity<>(Constants.SUCCESS,result);
	}

    @Override
    public BigDecimal findRepayingAmount(Integer regUserId, boolean flag) {
	    BigDecimal amount = BigDecimal.ZERO;
        List<BidInfoVO> bidList = null;
        BidRepayPlan bidRepayPlan = new BidRepayPlan();
        bidRepayPlan.setRegUserId(regUserId);
        bidRepayPlan.setState(RepayConstants.REPAY_STATE_NONE);
        List<BidRepayPlan> list = this.bidRepayPlanService.findBidRepayPlanList(bidRepayPlan);
        if(CommonUtils.isNotEmpty(list)){
            if(flag){
                List<Integer> ids = list.stream().map(BidRepayPlan::getBidId).distinct().collect(Collectors.toList());
                BidInfoVO cdt = new BidInfoVO();
                cdt.setBidLimitIds(ids);
                bidList = bidInfoService.findBidInfoVoList(cdt);
            }
            for(int i = 0; i < list.size(); i++){
                BidRepayPlan planTmp = list.get(i);
                //计算本金+利息+服务费
                amount = amount.add(planTmp.getAmount());
                //计算罚息
                if(flag){
                    amount = amount.add(this.calcPunishAmount(planTmp, bidList.stream().filter(o -> o.getId().equals(planTmp.getBidId())).findAny().get()));
                }
            }
        }
        return amount;
    }
    
    /**
    *  计算罚息
    *  @param bidRepayPlan
    *  @param bidVO
    *  @return java.math.BigDecimal
    *  @Creation Date           ：2018/9/27
    *  @Author                  ：zc.ding@foxmail.com
    */
    private BigDecimal calcPunishAmount(BidRepayPlan bidRepayPlan, BidInfoVO bidVO){
        //判断是否逾期
        if(bidRepayPlan.getPlanTime().before(new Date())){
            int lateDay = RepayCalcInterestUtil.calcPunishDays(bidRepayPlan.getPlanTime(), bidRepayPlan.getPeriods(), bidVO.getTermUnit(), bidVO.getTermValue());
            if (bidVO.getPunishState() != null && bidVO.getPunishState() == IS_PUNISH_STATE && lateDay > 0
                    && bidRepayPlan.getPlanTime().before(new Date())) {
                return CalcInterestUtil.calcOverdue(bidVO.getTotalAmount(), lateDay,
                        bidVO.getAdvanceServiceRate(), bidVO.getLiquidatedDamagesRate());
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public ResponseEntity<?> findExpectAtm(Integer bidId, BigDecimal amount, BigDecimal increaseRate) {
        BidInfoVO bidInfoVO = this.bidInfoService.findBidInfoDetailVo(bidId);
        BidInfo bidInfo = new BidInfo();
        BeanPropertiesUtil.mergeProperties(bidInfo, bidInfoVO);
        BidInvest bidInvest = new BidInvest();
        bidInvest.setInvestAmount(amount);
        bidInvest.setCouponWorthJ(increaseRate);
        ResponseEntity<?> planVos = RepayPlanUtils.initPlanVo(bidInfo, Collections.singletonList(bidInvest));
        ResponseEntity<?> result =
                RepayAndReceiptUtils.initRepayPlan((BidCommonPlanVo) planVos.getParams().get(RepayConstants.REPAY_BIDCOMMONPLAN_VO), (List<BidInvestVo>) planVos.getParams().get(RepayConstants.REPAY_BIDINVEST_VO), RepayConstants.INIT_PLAN_RECEIPT_ONLY);
        BigDecimal interestAtm = BigDecimal.ZERO;
        BigDecimal increaseAtm = BigDecimal.ZERO;
        List<BidReceiptPlan> list = ((List<BidReceiptPlan>) result.getParams().get("recieptPlanList"));
        for (BidReceiptPlan bidReceiptPlan : list) {
            interestAtm = interestAtm.add(bidReceiptPlan.getInterestAmount());
            increaseAtm = increaseAtm.add(bidReceiptPlan.getIncreaseAmount());
        }
        result = new ResponseEntity<>(Constants.SUCCESS);
        result.getParams().put("interestAtm", interestAtm);
        result.getParams().put("increaseAtm", increaseAtm);
        result.getParams().put("totalInterest", increaseAtm.add(interestAtm));
        result.getParams().put("points", 0);
        if (bidInfoVO.getGivingPointState() == InvestConstants.BID_DETAIL_GIVE_POINT_YES) {
            result.getParams().put("points", CalcPointUtils.calculateInvestPoint(amount.doubleValue(),
                    bidInfoVO.getTermValue(), bidInfoVO.getTermUnit(),
                    pointRuleService.getCurrentOnUseRule().getPointInvestBase().intValue()));
        }
        return result;
    }
}
