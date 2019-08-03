package com.hongkun.finance.invest.util;

import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.hongkun.finance.invest.model.BidMatch;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.hongkun.finance.invest.constants.InvestConstants.BID_STATE_WAIT_REPAY;
import static com.hongkun.finance.invest.constants.InvestConstants.MATCH_BID_TYPE_GOOD;

/**
 * @Description : 匹配工具类
 * @Project : finance-invest-model
 * @Program Name : com.hongkun.finance.invest.util.BidMatchUtil.java
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BidMatchUtil {
	/**
	 * @Description : 判断某个时间点，标的是否匹配完毕
	 * @Method_Name : matchComplete
	 * @param matchAmount  匹配总金额（标的金额-提前还本金额）
	 * @param matchList  匹配关系列表
	 * @param bidType  标的类型 1-优选，2-散标
	 * @param matchTerm  待匹配期数
	 * @return : boolean
	 * @Creation Date : 2017年7月17日 下午3:00:14
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static boolean matchComplete(BigDecimal matchAmount, List<BidMatch> matchList, int bidType, int matchTerm) {
		BigDecimal sumMoney = remainMatchMoney(matchAmount, matchList, bidType, matchTerm);
		return CompareUtil.eZero(sumMoney);
	}

	/**
	 * @Description : 查询某个时间点剩余匹配金额
	 * @Method_Name : remainMatchMoney
	 * @param matchAmount 匹配总金额
	 * @param matchList 已匹配列表
	 * @param bidType 标的类型
	 * @param matchTerm 待匹配期数
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月17日 下午3:34:16
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal remainMatchMoney(BigDecimal matchAmount, List<BidMatch> matchList, int bidType,
			int matchTerm) {
		BigDecimal sumMoney = new BigDecimal(0);
		for (BidMatch bidMatch : matchList) {
			int start = getStartTerm(bidMatch, bidType);
			int end = getEndTerm(bidMatch, bidType);
			if (matchTerm <= end && matchTerm >= start) {
				sumMoney = sumMoney.add(bidMatch.getMidMoney().abs());
			}
		}
		return matchAmount.subtract(sumMoney);
	}

	/**
	 * @Description : 计算下次匹配时间
	 * @Method_Name : getNextMatchDate
	 * @param bidInfo  标的信息
	 * @param matchAmount  匹配总金额（标的金额-提前还本金额）
	 * @param matchList  匹配列表（根据匹配期数end从小到大排序过的）
	 * @param bidType  标地类型：1-优选，2-散标
	 * @return : Date
	 * @Creation Date : 2017年7月18日 上午9:06:54
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static Date getNextMatchDate(int startMatchTerm, BidInfo bidInfo, BidInfoDetail bidInfoDetail,
										BigDecimal matchAmount, List<BidMatch> matchList, int bidType) {
		//获取标地当天匹配的开始期数
		int minTerm = Integer.MAX_VALUE;
		/*
		 * 标地当前时间金额匹配完毕，并且标的状态为还款中，则计算出匹配记录中距当前时间匹配时间最短的天数，该天数+标的放款时间=标的下次匹配时间；
		 * 其他状态：1.标地已放款，当前时间金额未完全匹配，下次匹配时间为当天，当天可以继续匹配；2.散标未放款，当前时间金额匹配完全，下次匹配时间为当天，因为放款时会更新散标的下次匹配时间；
		 */
		if (matchComplete(matchAmount, matchList, bidType, startMatchTerm) && bidInfo.getState() == BID_STATE_WAIT_REPAY) {
			for (BidMatch bidMatch : matchList) {
				int end = getEndTerm(bidMatch, bidType);
				if (minTerm > end && end >= startMatchTerm) {
					minTerm = end;
				}
			}
			return DateUtils.addDays(bidInfo.getLendingTime(), minTerm);
		}
		//优选标提前部分匹配，不更新优选标的下次匹配时间，返回优选标当前下次匹配时间
		boolean flag = bidType == MATCH_BID_TYPE_GOOD &&
				bidInfo.getState() == BID_STATE_WAIT_REPAY &&
				DateUtils.getDays(bidInfoDetail.getNextMatchDate(),new Date()) > 0 &&
				!matchComplete(matchAmount, matchList, bidType, startMatchTerm);
		if (flag){
			return bidInfoDetail.getNextMatchDate();
		}
		return new Date();
	}

	/**
	 * @Description : 匹配散标放款时，计算下次匹配时间
	 * @Method_Name : getNextMatchDate
	 * @param matchList
	 * @return
	 * @return : Date
	 * @Creation Date : 2017年7月19日 下午2:48:34
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static Date getNextMatchDate(List<BidMatch> matchList) {
		matchList.sort((BidMatch b1, BidMatch b2) -> Integer.valueOf(b1.getCommTermValue().split(":")[1])
				.compareTo(Integer.valueOf(b2.getCommTermValue().split(":")[1])));
		int days = Integer.parseInt(matchList.get(0).getCommTermValue().split(":")[1]);
		return DateUtils.addDays(new Date(), days);
	}

	/**
	 * @Description : 获取待匹配的开始天数和待匹配金额和标的总天数
	 * @Method_Name : getMatchTermAndMoney
	 * @param bidInfo
	 * @param nextMatchDate  下次匹配时间
	 * @param matchAmount  匹配总金额（标的金额-提前还本金额）
	 * @param matchList  匹配列表
	 * @param bidType 标的类型 1-优选，2-散标
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2017年7月18日 下午2:12:36
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static ResponseEntity<?> getMatchTermAndMoney(BidInfo bidInfo, Date nextMatchDate, BigDecimal matchAmount,
			List<BidMatch> matchList, int bidType,Date searchDate) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		//默认开始期数为1
		int start = 1;
		if (bidInfo.getState() == BID_STATE_WAIT_REPAY) {
			int matchTerm = DateUtils.getDaysBetween(bidInfo.getLendingTime(), searchDate);
			start = matchTerm + 1;
			BigDecimal mAmount = remainMatchMoney(matchAmount, matchList, bidType, start);
			//待匹配金额为0说明提前进行了匹配
			if (CompareUtil.eZero(mAmount)) {
				//开始期数为下次匹配时间-放款时间
				start = DateUtils.getDaysBetween(bidInfo.getLendingTime(), nextMatchDate) + 1;
				matchAmount = remainMatchMoney(matchAmount, matchList, bidType, start);
			}else{
				matchAmount = mAmount;
			}
		} else {
			matchAmount = remainMatchMoney(matchAmount, matchList, bidType, start);
		}
		int totalDays = CalcInterestUtil.calInvestDays(bidInfo.getTermUnit(), bidInfo.getTermValue());
		result.getParams().put(InvestConstants.MATCH_WILL_TERM_START, start);
		result.getParams().put(InvestConstants.MATCH_WILL_MONEY, matchAmount);
		result.getParams().put(InvestConstants.MATCH_WILL_TERM_END, totalDays);
		return result;
	}


	/**
	 * @Description : 根据标的状态确定当前时间到标的开始时间过去的天数，标地已放款，term=(当前时间-标的放款时间)+1;散标首次匹配返回1
	 * @Method_Name : getTermByBidState
	 * @param bidInfo  标的信息
	 * @param currentTime  当前时间
	 * @return : int
	 * @Creation Date : 2017年7月18日 上午9:10:31
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
//	private static int getTermByBidState(BidInfo bidInfo, Date currentTime) {
//		int result = 1;
//		if (bidInfo.getState() == BID_STATE_WAIT_REPAY) {
//			result = DateUtils.getDaysBetween(bidInfo.getLendingTime(), currentTime) + 1;
//		}
//		return result;
//	}

	/**
	 * @Description : 获取匹配记录中标的开始期数
	 * @Method_Name : getStartTerm
	 * @param bidMatch
	 *            匹配记录
	 * @param bidType
	 *            标的类型 ：1-优选，2-散标
	 * @return
	 * @return : int
	 * @Creation Date : 2017年7月17日 下午3:10:48
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static int getStartTerm(BidMatch bidMatch, int bidType) {
		return getTerm(bidMatch, bidType, InvestConstants.MATCH_TERM_START);
	}

	/**
	 * @Description : 获取匹配记录中标的结束期数
	 * @Method_Name : getEndTerm
	 * @param bidMatch
	 *            匹配记录
	 * @param bidType
	 *            标的类型 ：1-优选，2-散标
	 * @return
	 * @return : int
	 * @Creation Date : 2017年7月17日 下午3:10:48
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static int getEndTerm(BidMatch bidMatch, int bidType) {
		return getTerm(bidMatch, bidType, InvestConstants.MATCH_TERM_END);
	}

	/**
	 * @Description : 获取匹配记录中标的开始/结束期数
	 * @Method_Name : getTerm
	 * @param bidMatch
	 *            匹配记录
	 * @param bidType
	 *            标的类型 ：1-优选，2-散标
	 * @param flag
	 *            标识位：1-查询开始天数 2-查询结束天数
	 * @return
	 * @return : int
	 * @Creation Date : 2017年7月17日 下午3:21:56
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	private static int getTerm(BidMatch bidMatch, int bidType, int flag) {
		String term = null;
		if (bidType == InvestConstants.MATCH_BID_TYPE_GOOD) {
			term = bidMatch.getGoodTermValue();
		}
		if (bidType == InvestConstants.MATCH_BID_TYPE_COMMON) {
			term = bidMatch.getCommTermValue();
		}
		if (StringUtils.isNotBlank(term)) {
			if (flag == InvestConstants.MATCH_TERM_START) {
				return Integer.parseInt(term.split(":")[0]);
			}
			return Integer.parseInt(term.split(":")[1]);
		}
		return 0;
	}

	/**
	 * @Description : 获取某一天待匹配金额，只包含正常到期的情况
	 * @Method_Name : getMatchMoneyForOneDay
	 * @param matchList
	 *            匹配列表
	 * @param term
	 *            到期的天数
	 * @param bidType
	 *            标的类型 ：1-优选，2-散标
	 * @return
	 * @return : BigDecimal
	 * @Creation Date : 2017年7月24日 下午2:08:30
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	public static BigDecimal getMatchMoneyForOneDay(List<BidMatch> matchList, int term, int bidType) {
		BigDecimal result = new BigDecimal(0);
		for (BidMatch bidMatch : matchList) {
			if (term == getEndTerm(bidMatch, bidType)) {
				result.add(bidMatch.getMidMoney());
			}
		}
		return result;
	}

}
