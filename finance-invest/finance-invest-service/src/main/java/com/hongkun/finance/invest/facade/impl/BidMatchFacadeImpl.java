package com.hongkun.finance.invest.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidMatchFacade;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidMatch;
import com.hongkun.finance.invest.model.BidProduct;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.BidMatchStatisticsVo;
import com.hongkun.finance.invest.model.vo.BidMatchVo;
import com.hongkun.finance.invest.service.*;
import com.hongkun.finance.invest.util.BidInfoUtil;
import com.hongkun.finance.invest.util.BidMatchUtil;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.user.service.DicDataService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.invest.constants.InvestConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class BidMatchFacadeImpl implements BidMatchFacade {
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private BidMatchService bidMatchService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private BidProductService bidProductService;

	@Reference
	private DicDataService dicDataService;

	@Override
	public ResponseEntity<?> matchBidInfoList(String title, int bidType, Pager pager) {
		//初始化获取优选或散标待匹配标地信息条件
		BidInfoVO condition = new BidInfoVO();
		if (StringUtils.isNotBlank(title)) {
			condition.setTitle(title);
		}
		condition.setBidType(bidType);
		condition.setResiDays(10);
		condition.setServerSysDate(new Date());
		Pager resultPager  =  bidInfoService.findMatchBidInfoList(condition, pager);
		List<BidInfoVO> resultList = initBidInfoVOs((List<BidInfoVO>) resultPager.getData(),bidType,BigDecimal.ZERO,MATCH_SHOW_TYPE_BASIC_PAGE);
		resultPager.setData(resultList);
		return new ResponseEntity<>(Constants.SUCCESS, resultPager);
	}

	@Override
	public ResponseEntity<?> allMatchBidInfoList(String title, Pager pager) {
		BidInfoVO contidion = new BidInfoVO();
		if (StringUtils.isNotBlank(title)) {

			contidion.setTitle(title);
		}
		Pager resultPager = bidInfoService.findBidInfoVOByCondition(contidion, pager);
		List<BidInfoVO> resultList = (List<BidInfoVO>) resultPager.getData();
		if (!CommonUtils.isEmpty(resultList)) {
			for (BidInfoVO vo : resultList) {
				vo.setInterestAmount(CalcInterestUtil.calcInterest(vo.getTermUnit(), vo.getTermValue(),
						vo.getTotalAmount(), vo.getInterestRate()));
			}
		}
		resultPager.setData(resultList);
		return new ResponseEntity<>(Constants.SUCCESS, resultPager);
	}

	@Override
	public ResponseEntity<?> findMatchListByBidId(Integer bidId,Pager pager) {
		BidInfo bidInfo = bidInfoService.findBidInfoById(bidId);
		BidProduct bidProduct = bidProductService.findBidProductById(bidInfo.getBidProductId());
		BidMatchVo contidion = new BidMatchVo();
		if (bidInfo != null) {
			if (BidInfoUtil.isCommonBid(bidProduct.getType())) {
				contidion.setCommonBidId(bidId);
			} else {
				contidion.setGoodBidId(bidId);
			}
			pager = bidMatchService.findMatchVoListByContidion(contidion,pager);
		}
		return new ResponseEntity<>(Constants.SUCCESS,pager);
	}

	@Override
	public ResponseEntity<?> bidMatchAmountStatistics(Date startDate, Date endDate, Integer limitDays) {
		List<BidMatchStatisticsVo> resultList = new ArrayList<BidMatchStatisticsVo>();
		List<BidInfoVO> bidList = bidInfoService.findBidInfoDetailsByEndDate(endDate);
		// 需要统计的总天数
		int sumDays = DateUtils.getDaysBetween(startDate, endDate);
		for (int i = 0; i <= sumDays; i++) {
			BigDecimal goodAmount = new BigDecimal(0);
			BigDecimal commonAmount = new BigDecimal(0);
			// 当前时间
			Date now = DateUtils.addDays(startDate, i);

			Map<Integer,List<BidMatch>>  bidMatchMap = initBidMatchMap(bidList);
			// 计算当前时间待匹配金额
			for (BidInfoVO vo : bidList) {
				List<BidMatch> matchList = (List<BidMatch>)bidMatchMap.get(vo.getBidId());
				if (bidMatchMap == null || CommonUtils.isEmpty(matchList)){
					continue;
				}
				// 判断此优选标的是否是limitDay时间内的
				if (limitDays != null) {
					int totalDays = CalcInterestUtil.calInvestDays(vo.getTermUnit(), vo.getTermValue());
					int betweenNow = DateUtils.getDaysBetween(vo.getLendingTime(), now);
					if (totalDays - betweenNow > limitDays) {
						continue;
					}
				}
//				BidMatch bidMathContidion = new BidMatch();

				Integer bidType = BidInfoUtil.matchBidTypeByProdutType(vo.getProductType());
				if(bidType==null){
					continue;
				}
				if (now.before(vo.getNextMatchDate())){
					continue;
				}
				// 计算此标的待匹配金额
				if (DateUtils.isSameDay(new Date(), now)) {
					// 如果是今天，计算金额包含过期未匹配的情况
//					if (MATCH_BID_TYPE_GOOD == bidType) {
//						bidMathContidion.setGoodBidId(vo.getId());
//					}
//					if (InvestConstants.MATCH_BID_TYPE_COMMON == bidType) {
//						if(vo.getMatchType()==BID_MATCH_TYPE_NO){
//							continue;
//						}
//						bidMathContidion.setComnBidId(vo.getId());
//					}

					BidInfo bidInfo = new BidInfo();
					bidInfo.setId(vo.getId());
					bidInfo.setLendingTime(vo.getLendingTime());
					bidInfo.setTermUnit(vo.getTermUnit());
					bidInfo.setTermValue(vo.getTermValue());
					bidInfo.setState(vo.getState());
					ResponseEntity<?> goodMoneyResult = BidMatchUtil.getMatchTermAndMoney(bidInfo,
							vo.getNextMatchDate(), vo.getTotalAmount(), matchList, bidType,new Date());
					BigDecimal money = (BigDecimal) goodMoneyResult.getParams()
							.get(InvestConstants.MATCH_WILL_MONEY);
					Integer start = (Integer) goodMoneyResult.getParams().get(MATCH_WILL_TERM_START);
					Integer end = (Integer) goodMoneyResult.getParams().get(MATCH_WILL_TERM_END);
					if (start<=end){
						if (MATCH_BID_TYPE_GOOD == bidType) {
							goodAmount = goodAmount.add(money);
						}else{
							commonAmount = commonAmount.add(money);
						}
					}
					continue;
				}
				//当前时间在今天之后
				if(now.after(new Date())) {
					// 只计算存在当天匹配到期的金额
					if (DateUtils.isSameDay(now, vo.getNextMatchDate())) {
						if (MATCH_BID_TYPE_GOOD == bidType) {
//							bidMathContidion.setGoodBidId(vo.getId());
//							matchList = bidMatchService.findBidMatchList(bidMathContidion);
							int term = DateUtils.getDaysBetween(vo.getLendingTime(), now);
							BigDecimal money = BidMatchUtil.getMatchMoneyForOneDay(matchList, term, 1);
							goodAmount = goodAmount.add(money);
						}
						if (InvestConstants.MATCH_BID_TYPE_COMMON == bidType) {
							if(vo.getMatchType()==BID_MATCH_TYPE_NO){
								continue;
							}
//							bidMathContidion.setComnBidId(vo.getId());
//							matchList = bidMatchService.findBidMatchList(bidMathContidion);
							int term = DateUtils.getDaysBetween(vo.getLendingTime(), now);
							BigDecimal money = BidMatchUtil.getMatchMoneyForOneDay(matchList, term, 1);
							commonAmount = commonAmount.add(money);
						}
					}
				}

			}
			BidMatchStatisticsVo bidMatchStatisticsVo = new BidMatchStatisticsVo();
			bidMatchStatisticsVo.setCurrentDate(now);
			bidMatchStatisticsVo.setCommonMatchMoney(commonAmount);
			bidMatchStatisticsVo.setGoodMatchMoney(goodAmount);
			resultList.add(bidMatchStatisticsVo);
		}
		return new ResponseEntity<>(Constants.SUCCESS, resultList);
	}

	/**
	*  @Description    ：匹配统计---查询标的匹配列表
	*  @Method_Name    ：initBidMatchMap
	*  @param bidList
	*  @return java.util.Map<java.lang.Integer,java.util.List<com.hongkun.finance.invest.model.BidMatch>>
	*  @Creation Date  ：2019/1/28
	*  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
	*/
	private Map<Integer, List<BidMatch>> initBidMatchMap(List<BidInfoVO> bidList) {
		List<Integer> goodIds = new ArrayList<Integer>();
		List<Integer> commonIds = new ArrayList<Integer>();

		for(BidInfoVO infoVO :bidList){
			Integer bidType = BidInfoUtil.matchBidTypeByProdutType(infoVO.getProductType());
			if(bidType == null){
				continue;
			}
			if (MATCH_BID_TYPE_GOOD == bidType) {
				goodIds.add(infoVO.getId());
			}
			if (InvestConstants.MATCH_BID_TYPE_COMMON == bidType) {
				if(infoVO.getMatchType()==BID_MATCH_TYPE_NO){
					continue;
				}
				commonIds.add(infoVO.getId());
			}
		}
		//查询匹配记录
		List<BidMatch> goodBidMathList = bidMatchService.findBidMatchListByGoodIds(goodIds);
		List<BidMatch> commBidMathList = bidMatchService.findBidMatchListByCommonBidIds(commonIds);
		Map<Integer, List<BidMatch>> goodMap = goodBidMathList.stream().collect(Collectors.groupingBy(BidMatch::getGoodBidId));
		Map<Integer, List<BidMatch>> coomMap = commBidMathList.stream().collect(Collectors.groupingBy(BidMatch::getComnBidId));
		goodMap.putAll(coomMap);
		return goodMap;
	}

	@Override
	public ResponseEntity<?> matchBidInfoDetail(Integer bidId,Integer bidType) {
		
		BidInfoVO vo = bidInfoService.findBidInfoDetailVo(bidId);
		BidInfo bidInfo = new BidInfo();
		bidInfo.setLendingTime(vo.getLendingTime());
		bidInfo.setState(vo.getState());
		bidInfo.setTermUnit(vo.getTermUnit());
		bidInfo.setTermValue(vo.getTermValue());
		BidMatch bidMatch = new BidMatch();
		bidMatch.setGoodBidId(vo.getId());
		List<BidMatch> matchList = bidMatchService.findBidMatchList(bidMatch);
		BigDecimal matchAmount = vo.getTotalAmount().subtract(vo.getReturnCapAmount());
		ResponseEntity<?> remainResult = BidMatchUtil.getMatchTermAndMoney(bidInfo, vo.getNextMatchDate(),
				matchAmount, matchList, bidType,new Date());
		int startTerm = (int) remainResult.getParams().get(MATCH_WILL_TERM_START);
		int endTerm = (int) remainResult.getParams().get(InvestConstants.MATCH_WILL_TERM_END);
		BigDecimal matchingMoney = (BigDecimal) remainResult.getParams().get(InvestConstants.MATCH_WILL_MONEY);
		vo.setStartTerm(startTerm);
		vo.setEndTerm(endTerm);
		vo.setMatchingMoney(matchingMoney);
		vo.setBidType(bidType);
		return new ResponseEntity<>(Constants.SUCCESS, vo);
	}

	@Override
	public ResponseEntity<?> matchedDetails(Integer bidMatchId) {
		ResponseEntity result = new ResponseEntity<>();
		BidMatch bidMatch = bidMatchService.findBidMatchById(bidMatchId);
		if(bidMatch!=null){
			BidInfoVO goodBid = bidInfoService.findBidInfoDetailVo(bidMatch.getGoodBidId());
			BigDecimal goodInterestAmount = CalcInterestUtil.calcInterest(goodBid.getTermUnit(), goodBid.getTermValue(),
					goodBid.getTotalAmount(), goodBid.getInterestRate());
			goodBid.setInterestAmount(goodInterestAmount);
			BidInfoVO commonBid = bidInfoService.findBidInfoDetailVo(bidMatch.getComnBidId());
			BigDecimal commonInterestAmount = CalcInterestUtil.calcInterest(commonBid.getTermUnit(), commonBid.getTermValue(),
					commonBid.getTotalAmount(), commonBid.getInterestRate());
			commonBid.setInterestAmount(commonInterestAmount);
			Map<String,BidInfoVO> params  = new HashMap<String,BidInfoVO>();
			params.put("goodBid", goodBid);
			params.put("commonBid", commonBid);
			result.setResStatus(SUCCESS);
			result.setParams(params);
			return result;
		}
		return new ResponseEntity<>(ERROR,"未查询到相关匹配记录");
	}

	@Override
	public ResponseEntity<?> miniMatchBidInfoList(String title, Integer bidType, BigDecimal matchingMoney) {
		BidInfo bidInfo = new BidInfo();
		//初始化获取优选或散标待匹配标地信息条件
		BidInfoVO condition = new BidInfoVO();
		if (StringUtils.isNotBlank(title)) {
			condition.setTitle(title);
		}
		condition.setBidType(bidType);
		condition.setResiDays(10);
		condition.setServerSysDate(new Date());
		List<BidInfoVO> resultList = initBidInfoVOs(bidInfoService.findMatchBidInfoList(condition),bidType,matchingMoney,MATCH_SHOW_TYPE_MINI_PAGE);
		return new ResponseEntity<>(Constants.SUCCESS, resultList);
	}

	@Override
	public List<BidInfoVO> findMatchBidListForExport(Integer bidType) {
		BidInfoVO condition = new BidInfoVO();
		condition.setBidType(bidType);
		condition.setResiDays(10);
		condition.setServerSysDate(new Date());
		return initBidInfoVOsForExport(bidInfoService.findMatchBidInfoList(condition),bidType);
	}


	private List<BidInfoVO> initBidInfoVOs(List<BidInfoVO> bidInfoVOS,int bidType,BigDecimal matchMoney,int showType){
		BidInfo bidInfo = new BidInfo();
		List<BidInfoVO> resultList = new ArrayList<BidInfoVO>();
		if (!CommonUtils.isEmpty(bidInfoVOS)) {
			for (BidInfoVO vo : bidInfoVOS) {
				// 带匹配天数和金额
				bidInfo.setLendingTime(vo.getLendingTime());
				bidInfo.setState(vo.getState());
				bidInfo.setTermUnit(vo.getTermUnit());
				bidInfo.setTermValue(vo.getTermValue());
				//初始化获取标地匹配记录的条件
				BidMatch bidMatch = new BidMatch();
				if (bidType == MATCH_BID_TYPE_GOOD){
					bidMatch.setGoodBidId(vo.getId());
				}else {
					bidMatch.setComnBidId(vo.getId());
				}
				List<BidMatch> matchList = bidMatchService.findBidMatchList(bidMatch);
				//如果标地没有匹配记录则该标为首次匹配，否则为非首次匹配,0-没有匹配记录，1-有匹配记录
				if (matchList.size() > 0){
					vo.setHasMatch(1);
				}else {
					vo.setHasMatch(0);
				}
				BigDecimal matchAmount = vo.getTotalAmount().subtract(vo.getReturnCapAmount());
				ResponseEntity<?> remainResult = BidMatchUtil.getMatchTermAndMoney(bidInfo, vo.getNextMatchDate(),
						matchAmount, matchList, bidType,new Date());
				int startTerm = (int) remainResult.getParams().get(MATCH_WILL_TERM_START);
				int endTerm = (int) remainResult.getParams().get(InvestConstants.MATCH_WILL_TERM_END);
				BigDecimal matchingMoney = (BigDecimal) remainResult.getParams().get(InvestConstants.MATCH_WILL_MONEY);
				if (showType == MATCH_SHOW_TYPE_MINI_PAGE && bidType == MATCH_BID_TYPE_COMMON && CompareUtil.gt(matchingMoney,matchMoney)){
					continue;
				}
				vo.setStartTerm(startTerm);
				vo.setEndTerm(endTerm);
				vo.setMatchingMoney(matchingMoney);
				vo.setBidType(bidType);
				vo.setResiDays(DateUtils.getDays(vo.getNextMatchDate(), new Date()));
				//如果为待上架状态，则认为时散标待上架首次匹配，剩余天数则设置为0
				if (vo.getState() == InvestConstants.BID_STATE_WAIT_NEW){
					vo.setResiDays(0);
				}
				resultList.add(vo);
			}
		}
		return resultList;
	}



	private List<BidInfoVO> initBidInfoVOsForExport(List<BidInfoVO> bidInfoVOS,int bidType){
		BidInfo bidInfo = new BidInfo();
		List<BidInfoVO> resultList = new ArrayList<BidInfoVO>();
		if (!CommonUtils.isEmpty(bidInfoVOS)) {
			for (BidInfoVO vo : bidInfoVOS) {
				// 带匹配天数和金额
				bidInfo.setLendingTime(vo.getLendingTime());
				bidInfo.setState(vo.getState());
				bidInfo.setTermUnit(vo.getTermUnit());
				bidInfo.setTermValue(vo.getTermValue());
				//初始化获取标地匹配记录的条件
				BidMatch bidMatch = new BidMatch();
				if (bidType == MATCH_BID_TYPE_GOOD){
					bidMatch.setGoodBidId(vo.getId());
				}else {
					bidMatch.setComnBidId(vo.getId());
				}
				List<BidMatch> matchList = bidMatchService.findBidMatchList(bidMatch);
				//如果标地没有匹配记录则该标为首次匹配，否则为非首次匹配,0-没有匹配记录，1-有匹配记录
				if (matchList.size() > 0){
					vo.setHasMatch(1);
				}else {
					vo.setHasMatch(0);
				}
				BigDecimal matchAmount = vo.getTotalAmount().subtract(vo.getReturnCapAmount());
				ResponseEntity<?> remainResult = BidMatchUtil.getMatchTermAndMoney(bidInfo, vo.getNextMatchDate(),
						matchAmount, matchList, bidType,new Date());
				int startTerm = (int) remainResult.getParams().get(MATCH_WILL_TERM_START);
				int endTerm = (int) remainResult.getParams().get(InvestConstants.MATCH_WILL_TERM_END);
				BigDecimal matchingMoney = (BigDecimal) remainResult.getParams().get(InvestConstants.MATCH_WILL_MONEY);
				vo.setStartTerm(startTerm);
				vo.setEndTerm(endTerm);
				vo.setMatchingMoney(matchingMoney);
				vo.setBidType(bidType);
				vo.setResiDays(DateUtils.getDays(vo.getNextMatchDate(), new Date()));
				//如果为待上架状态，则认为时散标待上架首次匹配，剩余天数则设置为0
				if (vo.getState() == InvestConstants.BID_STATE_WAIT_NEW){
					vo.setResiDays(0);
				}
				String termUnitName = dicDataService.findNameByValue("invest","bid_term_unit",vo.getTermUnit());
				vo.setTermValueStr(String.valueOf(vo.getTermValue())+termUnitName);
				vo.setMatchingDaysStr(startTerm+"-"+endTerm);
				if(startTerm>endTerm){
					vo.setMatchingDetailsStr("不可匹配");
				}else{
					if(vo.getResiDays()==0){
						vo.setMatchingDetailsStr("今天匹配");
					}else if (vo.getResiDays()<0){
						vo.setMatchingDetailsStr("已过期"+vo.getResiDays()*-1+"天");
					}else{
						vo.setMatchingDetailsStr(vo.getResiDays()+"天后可以匹配");
					}
				}
				vo.setCreateTimeStr(DateUtils.format(vo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setLendingTimeStr(DateUtils.format(vo.getLendingTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setNextMatchDateStr(DateUtils.format(vo.getNextMatchDate(),"yyyy-MM-dd"));
				resultList.add(vo);
			}
		}
		return resultList;
	}
}
