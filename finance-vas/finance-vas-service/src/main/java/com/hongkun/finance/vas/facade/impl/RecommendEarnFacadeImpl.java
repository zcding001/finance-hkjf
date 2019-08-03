package com.hongkun.finance.vas.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.model.vo.BidInvestForRecommendVo;
import com.hongkun.finance.invest.service.BidInfoDetailService;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.invest.util.CalcInterestUtil;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.vo.TransferVo;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.roster.vo.RosStaffInfoVo;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.RegUserFriends;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserFriendsService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.enums.VasRuleTypeEnum;
import com.hongkun.finance.vas.facade.RecommendEarnFacade;
import com.hongkun.finance.vas.model.*;
import com.hongkun.finance.vas.model.vo.RecommendEarnForAppVo;
import com.hongkun.finance.vas.model.vo.RecommendEarnVO;
import com.hongkun.finance.vas.service.VasBidRecommendEarnService;
import com.hongkun.finance.vas.service.VasRebatesRuleChildService;
import com.hongkun.finance.vas.service.VasRebatesRuleService;
import com.hongkun.finance.vas.utils.ClassReflection;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class RecommendEarnFacadeImpl implements RecommendEarnFacade {
	private static final Logger logger = LoggerFactory.getLogger(RecommendEarnFacadeImpl.class);
	@Reference
	private VasBidRecommendEarnService vasBidRecommendEarnService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private BidInfoDetailService bidInfoDetailService;
	@Reference
	private VasRebatesRuleService vasRebatesRuleService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private VasRebatesRuleChildService vasRebatesRuleChildService;
	@Override
	public ResponseEntity<?> findRecommendEarnInfo(RecommendEarnVO recommendEarnVO, Pager pager) {
		// 拼接查询参数map
		Map<String, Object> biddRecommendEarnMap = new HashMap<String, Object>();
		Pager pagerInfo = null;
		try {
			// 判断查询条件是否选择了标的投资开始时间和投资结束时间，只要选择了一项，则查询标的信息
			if (recommendEarnVO.getGrantTimeBegin() != null || recommendEarnVO.getGrantTimeEnd() != null) {
				// 查询时间段的投资记录，按照BIDINFOID分组，获取时间段内的标的ID集合
				BidInvest bidInvest = new BidInvest();
				bidInvest.setCreateTimeBegin(recommendEarnVO.getGrantTimeBegin());
				bidInvest.setCreateTimeEnd(recommendEarnVO.getGrantTimeEnd());
				List<Integer> bidInfoIds = bidInvestService.findBidInfoIdByCondition(bidInvest);
				if (bidInfoIds != null && bidInfoIds.size() > 0) {
					biddRecommendEarnMap.put("biddIdList", bidInfoIds);
				} else {
					List<Integer> bidList = new ArrayList<Integer>();
					bidList.add(0);
					biddRecommendEarnMap.put("biddIdList", bidList);
				}
			}
			// 判断用户是否输入了推荐人手机号作为查询条件，如果输入，则查询推荐人对应的用户信息
			if (recommendEarnVO.getRecommedTel() != null || StringUtils.isNotBlank(recommendEarnVO.getGroupCode())
					|| StringUtils.isNotBlank(recommendEarnVO.getCommendNo())) {
				// 获取推荐人用户信息
				List<Integer> recommendRegUserIdList = new ArrayList<Integer>();
				UserVO userVO = new UserVO();
				if (StringUtils.isNotBlank(recommendEarnVO.getCommendNo())) {
					userVO.setInviteNo(recommendEarnVO.getCommendNo());
				}
				userVO.setGroupCode(recommendEarnVO.getGroupCode());
				userVO.setLogin(recommendEarnVO.getRecommedTel());
				recommendRegUserIdList.add(0);// 默认查询条件
				recommendRegUserIdList.addAll(regUserService.findUserIdsByUserVO(userVO));
				biddRecommendEarnMap.put("recommendRegUserId", recommendRegUserIdList);
				// 判断是否选择了用户角色
				if (recommendEarnVO.getRecommendType() != null
						&& !"-999".equals(String.valueOf(recommendEarnVO.getRecommendType()))) {
					List<Integer> resterRecommendRegUserIdList = new ArrayList<Integer>();
					resterRecommendRegUserIdList.add(0);
					if (recommendEarnVO.getRecommendType() != VasConstants.RECOMMEND_USER_TYPE_COMMON) {
						resterRecommendRegUserIdList
								.addAll(rosStaffInfoService.findRosStaffInfoLists(recommendRegUserIdList,
										recommendEarnVO.getRecommendType(), RosterConstants.ROSTER_STAFF_STATE_VALID));
						biddRecommendEarnMap.put("recommendRegUserId", resterRecommendRegUserIdList);
					}
				}
			} else {
				if (recommendEarnVO.getRecommendType() != null
						&& !"-999".equals(String.valueOf(recommendEarnVO.getRecommendType()))) {
					List<Integer> rosterRegUserIdList = new ArrayList<Integer>();
					rosterRegUserIdList.add(0);
					// 查询普通用户的集合
					if (VasConstants.RECOMMEND_USER_TYPE_COMMON == recommendEarnVO.getRecommendType()) {
						List<Integer> rostaffTypeList = new ArrayList<Integer>();
						rostaffTypeList.add(RosterConstants.ROSTER_STAFF_TYPE_PROPERTY);// 物业员工
						rostaffTypeList.add(RosterConstants.ROSTER_STAFF_TYPE_SALES);// 销售员工
						rostaffTypeList.add(RosterConstants.ROSTER_STAFF_TYPE_INNER_STAFF);// 内部员工
						rostaffTypeList.add(RosterConstants.ROSTER_STAFF_TYPE_INNER_MANAGER);// 理财家
						List<Integer> rostaffList = rosStaffInfoService.findRosStaffInfoByTypes(rostaffTypeList);
						rosterRegUserIdList.addAll(regUserService.findCommonRecommendUser(rostaffList));
					} else {
						// 查询对应的角色的集合
						rosterRegUserIdList.addAll(rosStaffInfoService.findRosStaffInfoLists(null,
								recommendEarnVO.getRecommendType(), RosterConstants.ROSTER_STAFF_STATE_VALID));
					}
					biddRecommendEarnMap.put("recommendRegUserId", rosterRegUserIdList);
				}
			}
			if (recommendEarnVO.getRecommendRegUserId() != null) {
				List<Integer> recomRegUser = new ArrayList<Integer>();
				recomRegUser.add(recommendEarnVO.getRecommendRegUserId());
				biddRecommendEarnMap.put("recommendRegUserId", recomRegUser);
			}
			// 判断用户是否输入了被推荐人手机号作为查询条件，如果输入，则查询被推荐人用户信息
			if (recommendEarnVO.getReferraTel() != null) {
				// 获 取被推荐人用户信息
				List<Integer> regUserIdList = new ArrayList<Integer>();
				regUserIdList.add(0);// 默认查询条件
				regUserIdList.addAll(regUserService.findUserIdsByTel(recommendEarnVO.getReferraTel()));
				biddRecommendEarnMap.put("regUserId", regUserIdList);
				// 判断是否选择了用户角色
				if (recommendEarnVO.getRecommendType() != null
						&& !"-999".equals(String.valueOf(recommendEarnVO.getRecommendType()))) {
					List<Integer> rosterRegUserIdList = new ArrayList<Integer>();
					rosterRegUserIdList.add(0);
					if (recommendEarnVO.getRecommendType() != VasConstants.RECOMMEND_USER_TYPE_COMMON) {
						rosterRegUserIdList.addAll(rosStaffInfoService.findRosStaffInfoLists(regUserIdList,
								recommendEarnVO.getRecommendType(), RosterConstants.ROSTER_STAFF_STATE_VALID));
						biddRecommendEarnMap.put("regUserId", rosterRegUserIdList);
					}
				}
			}
			// 其它推荐记录表中的查询条件
			biddRecommendEarnMap.put("state", recommendEarnVO.getState());
			biddRecommendEarnMap.put("recommendState", recommendEarnVO.getRecommendState());
			biddRecommendEarnMap.put("createTimeBegin", recommendEarnVO.getCreateTimeBegin());
			biddRecommendEarnMap.put("createTimeEnd", recommendEarnVO.getCreateTimeEnd());
	        biddRecommendEarnMap.put("sortColumns", "create_time desc");
			// 查询推荐记录分页信息
			pagerInfo = vasBidRecommendEarnService.findVasBidRecommendEarnListByInfo(biddRecommendEarnMap, pager);
			// 组装返回页面信息
			return new ResponseEntity<>(SUCCESS, buildBiddRecommendEarnVo(pagerInfo));
		} catch (Exception e) {
			logger.error("查询推荐好友信息异常: ", e);
			return new ResponseEntity<>(ERROR, "查询异常");
		}
	}

	/**
	 * @Description :封装返回给页面的VO对象
	 * @Method_Name : buildBiddRecommendEarnVo;
	 * @param result
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年6月28日 上午10:43:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	private Pager buildBiddRecommendEarnVo(Pager result) throws Exception {
		List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
		if (BaseUtil.resultPageHasNoData(result)) {
			return result;
		}
		List<VasBidRecommendEarn> biddRecommendList = (List<VasBidRecommendEarn>) result.getData();
		for (VasBidRecommendEarn biddRecommendEarn : biddRecommendList) {
		    // 获取被推荐人用户信息
            RegUser regUser = BaseUtil.getRegUser(biddRecommendEarn.getRegUserId(), ()-> this.regUserService.findRegUserById(biddRecommendEarn.getRegUserId()));
            RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(biddRecommendEarn.getRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(biddRecommendEarn.getRegUserId()));
            // 获取推荐人用户信息
            RegUser recomRegUser = BaseUtil.getRegUser(biddRecommendEarn.getRecommendRegUserId(), ()-> this.regUserService.findRegUserById(biddRecommendEarn.getRecommendRegUserId()));
            RegUserDetail recomRegUserDetail = BaseUtil.getRegUserDetail(biddRecommendEarn.getRecommendRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(biddRecommendEarn.getRecommendRegUserId()));
			// 获取标的信息
			BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(biddRecommendEarn.getBidId());
			// 查询投资记录
			BidInvest bidInvest = bidInvestService.findBidInvestById(biddRecommendEarn.getResourceId());
			VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleById(bidInfoVO.getRuleId());
			int investNum = bidInvestService.findBidInvestCount(null, bidInvest.getRegUserId(),
					vasRebatesRule == null ? null : vasRebatesRule.getBeginTime(), bidInvest.getCreateTime(),
					InvestConstants.BID_DETAIL_RECOMMEND_YES);
			// 通过反射给返回页面实体对象赋值
			RecommendEarnVO biddRecommendEarnVo = new RecommendEarnVO();
			RosStaffInfo rosStaffInfo = rosStaffInfoService.findRosStaffInfo(biddRecommendEarn.getRecommendRegUserId(),
					null, RosterConstants.ROSTER_STAFF_STATE_VALID,RosterConstants.ROSTER_STAFF_RECOM_STATE_SEND);
			// 查询用户是否投资了新手标的
			int jackarooInvestNum = bidInvestService.findBidInvestCount(bidInvest.getRegUserId(),
					InvestConstants.BID_PRODUCT_PREFERRED);
			if (rosStaffInfo != null && jackarooInvestNum > 0) {
				investNum = investNum - 1;
			}
			VasRebatesRuleChild vasRebatesRuleChild = vasRebatesRuleChildService
					.findVasRebatesRuleChildById(biddRecommendEarn.getRuleId());
			// 将字符串的JSON格式，转换为对象，获取一、二级好友的最大投资笔数，和利率的限额，
			RebatesRuleChildItem rebatesRuleChildItem = JsonUtils.json2Object(vasRebatesRuleChild.getContent(),
					RebatesRuleChildItem.class, null);
			// 如果好友等级是一级好友，获取一级好友投资笔数对应的利率
			if (VasConstants.RECOMMEND_FRIEND_LEVEL_ONE == biddRecommendEarn.getFriendLevel()) {
				String rebatesRate1 = null;
				List<RuleItemVo> friendLevelOneRule = rebatesRuleChildItem.getFriendLevelOne();
				boolean countLevelOneFlag = false;
				if (friendLevelOneRule != null && friendLevelOneRule.size() > 0) {
					for (RuleItemVo ruleItemVo : friendLevelOneRule) {
						if (ruleItemVo.getInvestStrokeCount().equals(String.valueOf(investNum))) {
							countLevelOneFlag = true;
							rebatesRate1 = ruleItemVo.getRate();
							break;
						}
					}
				}
				// 如果没有设置投资笔数指定的利率，则判断默认利率是否符合
				if (!countLevelOneFlag
						&& Integer.valueOf(investNum)
								.compareTo(Integer.valueOf(rebatesRuleChildItem.getFromInvNumOne())) >= 0
						&& Integer.valueOf(investNum)
								.compareTo(Integer.valueOf(rebatesRuleChildItem.getToInvNumOne())) <= 0) {
					rebatesRate1 = rebatesRuleChildItem.getRebatesRateLevelOne();
				}
				biddRecommendEarnVo.setRate(new BigDecimal(rebatesRate1 == null ? "0" : rebatesRate1));
			} else if (VasConstants.RECOMMEND_FRIEND_LEVEL_TWO == biddRecommendEarn.getFriendLevel()) {
				// 如果好友等级是二级好友，获取二级好友投资笔数对应的利率
				List<RuleItemVo> friendLevelTwoRule = rebatesRuleChildItem.getFriendLevelTwo();
				String rebatesRate2 = null;
				boolean countLevelTwoFlag = false;
				if (friendLevelTwoRule != null && friendLevelTwoRule.size() > 0) {
					for (RuleItemVo ruleItemVo : friendLevelTwoRule) {
						if (ruleItemVo.getInvestStrokeCount().equals(String.valueOf(investNum))) {
							countLevelTwoFlag = true;
							rebatesRate2 = ruleItemVo.getRate();
							break;
						}
					}
				}
				// 如果没有设置投资笔数指定的利率，则判断默认利率是否符合
				if (!countLevelTwoFlag
						&& Integer.valueOf(investNum)
								.compareTo(Integer.valueOf(rebatesRuleChildItem.getFromInvNumTwo())) >= 0
						&& Integer.valueOf(investNum)
								.compareTo(Integer.valueOf(rebatesRuleChildItem.getToInvNumTwo())) <= 0) {
					rebatesRate2 = rebatesRuleChildItem.getRebatesRateLevelTwo();
				}
				biddRecommendEarnVo.setRate(new BigDecimal(rebatesRate2 == null ? "0" : rebatesRate2));
			} else {
				biddRecommendEarnVo.setRate(BigDecimal.ZERO);
			}
			// 两个对象之间相同Bean属性赋值
			ClassReflection.reflectionAttr(biddRecommendEarn, biddRecommendEarnVo);
			// 如果推荐用户不为空，则进行页面展示信息组装
			if (recomRegUser != null && recomRegUserDetail!=null) {
				biddRecommendEarnVo.setRecommedTel(recomRegUser.getLogin());
				biddRecommendEarnVo.setRecommendName(recomRegUserDetail.getRealName());
				biddRecommendEarnVo.setGroupCode(recomRegUserDetail.getGroupCode());
				biddRecommendEarnVo.setCommendNo(recomRegUserDetail.getInviteNo());
			}
			// 如果被推荐用户不为空，则进行页面展示信息组装
			if (regUser != null && regUserDetail != null) {
				biddRecommendEarnVo.setReferraTel(regUser.getLogin());
				biddRecommendEarnVo.setReferraName(regUserDetail.getRealName());
			}
			// 如果标的信息不为空,则进行页面展示信息组装
			if (bidInfoVO != null) {
			    biddRecommendEarnVo.setLendingTime(bidInfoVO.getLendingTime());
				biddRecommendEarnVo.setTermUnit(bidInfoVO.getTermUnit());
				biddRecommendEarnVo.setTermValue(bidInfoVO.getTermValue());
				biddRecommendEarnVo.setBiddName(bidInfoVO.getTitle());
				biddRecommendEarnVo.setInvestTime(bidInvest.getCreateTime());
				biddRecommendEarnVo.setBackStepMoney(bidInvest.getInvestAmount()
						.multiply(CalcInterestUtil.calFoldRatio(bidInfoVO.getTermUnit(), bidInfoVO.getTermValue())));
				biddRecommendEarnVo.setBackStepRatio(
						CalcInterestUtil.calFoldRatio(bidInfoVO.getTermUnit(), bidInfoVO.getTermValue()));
				biddRecommendEarnVo.setBidRate(bidInfoVO.getInterestRate());
			}
			// 设置用户推荐类型
			if (rosStaffInfo == null) {
				biddRecommendEarnVo.setRecommendType(VasConstants.RECOMMEND_USER_TYPE_COMMON);
			} else {
				biddRecommendEarnVo.setRecommendType(rosStaffInfo.getType());
			}
			biddRecommendEarnVo.setInvestCount(investNum);
			biddRecommendEarnVoList.add(biddRecommendEarnVo);
		}
		result.setData(biddRecommendEarnVoList);
		return result;
	}

	@Override
	public void createRecommendRecordInfo(RecommendEarnBuild recommendEarnBuild) {
		logger.info("方法: {}, 生成好友推荐奖, 入参: recommendEarnBuild: {}", recommendEarnBuild.toString());
		if (recommendEarnBuild.getRecommendRecordList() != null
				&& recommendEarnBuild.getRecommendRecordList().size() > 0) {
			List<RcommendEarnInfo> recommendInfoList = null;
			// 根据推荐奖类型，来判断如何生成推荐奖
			if (recommendEarnBuild.getVasRuleTypeEnum().getValue() == VasRuleTypeEnum.RECOMMEND.getValue()) {
				// 要生成的投资推荐奖记录
				recommendInfoList = buildInvestRecommendEarn(recommendEarnBuild.getRecommendRecordList(),
						recommendEarnBuild.getSystemTypeEnums());
			}
			// 生成推荐奖记录，插入推荐记录表
			for (RcommendEarnInfo rcommendEarnInfo : recommendInfoList) {
				ResponseEntity<?> resultRes = vasBidRecommendEarnService.insertVasBidRecommendEarn(rcommendEarnInfo);
				logger.info("生成好友推荐奖, 推荐奖类型: {}, 生成推荐奖状态: {}, 描述:{}",
						recommendEarnBuild.getVasRuleTypeEnum().getValue(), resultRes.getResStatus(),
						resultRes.getResMsg().toString());
			}
		}

	}

	/**
	 * @Description : 用户校验及生成投资推荐奖信息
	 * @Method_Name : buildInvestRecommendEarn;
	 * @param bidInvestList
	 *            投资记录LIST
	 * @return
	 * @return : List<RcommendEarnInfo>;
	 * @Creation Date : 2017年7月31日 下午5:52:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings({ "unchecked" })
	private List<RcommendEarnInfo> buildInvestRecommendEarn(List<?> bidInvestList, SystemTypeEnums systemTypeEnums) {
		List<Object> bidInvestLists = (List<Object>) bidInvestList;
		BidInvest bidInvests = null;
		List<RcommendEarnInfo> recommendEarnList = new ArrayList<RcommendEarnInfo>();
		for (Object obj : bidInvestLists) {
			try {
				bidInvests = (BidInvest) obj;
				logger.info("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录: {}", bidInvests.getRegUserId(), bidInvests.toString());
				// 查询该投资用户是否存在
				RegUser investRegUser = regUserService.findRegUserById(bidInvests.getRegUserId());
				if (investRegUser == null) {
					logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 校验生成好友推荐奖信息,查询投资记录对应投资人信息不存在则不生成推荐奖",
							bidInvests.getRegUserId(), bidInvests.getId());
					continue;
				}
				// 查询是否有该笔投资记录
				BidInvest bidInvest = bidInvestService.findBidInvestById(bidInvests.getId());
				if (bidInvest == null) {
					logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 校验生成好友推荐奖信息,查询投资记录存在则不生成推荐奖",
							bidInvests.getRegUserId(), bidInvests.getId());
					continue;
				}
				// 查询该笔投资记录对应的标的是否支持发放推荐奖
				BidInfoVO bidInfoVo = bidInfoService.findBidInfoDetailVo(bidInvest.getBidInfoId());
				if (bidInfoVo == null || bidInfoVo.getRecommendState() == InvestConstants.BID_DETAIL_RECOMMEND_NO) {
					logger.error(
							"构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 标的ID: {}, 校验生成好友推荐奖信息,查询投资记录对应标的信息不符合推荐规则不生成推荐奖",
							bidInvests.getRegUserId(), bidInvest.getId(), bidInvest.getBidInfoId());
					continue;
				}
				// 查询这笔投资是否已经产生了推荐奖，如果已经产生了推荐奖，那么不再产生推荐奖
				int count = vasBidRecommendEarnService.findVasBidRecommendEarnCountByBidInvestId(bidInvests.getId());
				if (count > 0) {
					logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 校验生成好友推荐奖信息,查询该投资记录已经产生了推荐奖不符合推荐规则不生成推荐奖",
							bidInvests.getRegUserId(), bidInvest.getId());
					continue;
				}
				// 查询当前标的对应的好友推荐规则
				VasRebatesRule vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleById(bidInfoVo.getRuleId());
				// 查询该投资人的投资笔数,投资时间大于活动开始时间,投资时间小于当前投资时间,并且是好友推荐的标
				int investNum = bidInvestService.findBidInvestCount(null, bidInvest.getRegUserId(),
						vasRebatesRule == null ? null : vasRebatesRule.getBeginTime(), bidInvest.getCreateTime(),
						InvestConstants.BID_DETAIL_RECOMMEND_YES);
				// 查询该投资人的一级推荐好友，以及二级推荐好友
				RegUserFriends regUserFriends = new RegUserFriends();
				regUserFriends.setRegUserId(bidInvests.getRegUserId());
				List<RegUserFriends> regUserRecommendList = regUserFriendsService
						.findRecommendFriendsList(regUserFriends);
				if (regUserRecommendList == null || regUserRecommendList.size() <= 0) {
					logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 校验生成好友推荐奖信息,没有查询到该投资人的一级、二级好友,不生成推荐奖",
							bidInvests.getRegUserId(), bidInvest.getId());
					continue;
				}
				for (RegUserFriends regUserRecommend : regUserRecommendList) {
					// 如果推荐关系不为空，并且一级推荐好友或二级推荐好友不为空,则生成推荐记录
					if (regUserRecommend != null && regUserRecommend.getRecommendId() != null) {
						RcommendEarnInfo rcommendEarnInfo = new RcommendEarnInfo();
						// 查询推荐好友是否存在
						RegUser regUser = regUserService.findRegUserById(regUserRecommend.getRecommendId());
						// 如果存在，则生成推荐记录，否则打印日志，不生成推荐记录
						if (regUser != null) {
							rcommendEarnInfo = buildRcommendEarnInfo(regUserRecommend.getRecommendId(), bidInvests,
									rcommendEarnInfo, regUserRecommend, investNum, bidInfoVo,
									investRegUser.getCreateTime(), regUserRecommend.getGrade(), systemTypeEnums);
							if (rcommendEarnInfo != null) {
								recommendEarnList.add(rcommendEarnInfo);
							}
						} else {
							logger.error("用户标识: {}, 推荐好友等级: {}, 校验生成好友推荐奖信息,没有查询到投资用户的推荐好友,则不生成推荐奖",
									bidInvests.getRegUserId(), regUserRecommend.getGrade());
						}
					}
				}
			} catch (Exception e) {
				logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 投资记录ID: {}, 构造推荐奖信息失败: ", bidInvests.getRegUserId(),
						bidInvests.getId(), e);
			}
		}
		return recommendEarnList;
	}

	/**
	 * @Description :组装生成好友推荐奖记录信息
	 * @Method_Name : buildRcommendEarnInfo;
	 * @param regUserId
	 *            用户ID
	 * @param bidInvest
	 *            投资记录
	 * @param rcommendEarnInfo
	 *            推荐奖记录
	 * @param regUserFriends
	 *            好友关系
	 * @param investNum
	 *            投资笔数
	 * @param bidInfoVo
	 *            标的信息
	 * @param ruleTime
	 *            注册时间
	 * @param friendLevel
	 *            好友等级
	 * @return
	 * @return : RcommendEarnInfo;
	 * @Creation Date : 2017年7月31日 下午5:45:01;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private RcommendEarnInfo buildRcommendEarnInfo(Integer regUserId, BidInvest bidInvest,
			RcommendEarnInfo rcommendEarnInfo, RegUserFriends regUserFriends, int investNum, BidInfoVO bidInfoVo,
			Date ruleTime, int friendLevel, SystemTypeEnums systemTypeEnums) {
		// 查询该用户是否属于内部员工
		RosStaffInfo rosStaffInfo = rosStaffInfoService.findRosStaffInfo(regUserId, null,
				RosterConstants.ROSTER_STAFF_STATE_VALID,RosterConstants.ROSTER_STAFF_RECOM_STATE_SEND);
		// 如果是新手标，并且用户类型不是普通用户，则不生成推荐奖
		if (rosStaffInfo != null && bidInfoVo.getProductType() == InvestConstants.BID_PRODUCT_PREFERRED) {
			logger.error("用户标识: {}, 产品类型: {}, 如果是新手标,并且用户类型不是普通用户,则不生成推荐奖则", regUserId, bidInfoVo.getProductType());
			return null;
		}
		// 查询用户是否投资了新手标的
		int jackarooInvestNum = bidInvestService.findBidInvestCount(bidInvest.getRegUserId(),
				InvestConstants.BID_PRODUCT_PREFERRED);
		rcommendEarnInfo.setRegUserId(regUserFriends.getRegUserId());
		rcommendEarnInfo.setBiddId(bidInvest.getBidInfoId());
		rcommendEarnInfo.setFriendLevel(friendLevel);
		rcommendEarnInfo.setInvestAmount(bidInvest.getInvestAmount());
		rcommendEarnInfo.setRecommendRegUserId(regUserFriends.getRecommendId());
		rcommendEarnInfo.setType(VasConstants.INVEST_EARN);
		rcommendEarnInfo.setRuleTime(ruleTime);
		rcommendEarnInfo.setTermUnit(bidInfoVo.getTermUnit());
		rcommendEarnInfo.setTermValue(bidInfoVo.getTermValue());
		rcommendEarnInfo.setVasRuleId(bidInfoVo.getRuleId());
		rcommendEarnInfo.setRegistTime(ruleTime);
		rcommendEarnInfo.setInvestTime(bidInvest.getCreateTime());
		rcommendEarnInfo.setInvestNum(investNum);
		if (rosStaffInfo != null) {
			// 如果不是普通用户，并且投资过新手标的，则不算投资笔数
			if (jackarooInvestNum > 0) {
				rcommendEarnInfo.setInvestNum(investNum - 1);
			}
			// 如果用户类型是理财家，只有用户在成为理财家之后注册的用户才给推荐奖。之前推荐的用户不产生推荐奖
			if (RosterConstants.ROSTER_STAFF_TYPE_INNER_MANAGER == rosStaffInfo.getType()
					&& ruleTime.compareTo(rosStaffInfo.getCreateTime()) < 0) {
			    logger.error("构造需要生成的推荐奖记录信息, 用户标识: {}, 用户注册时间 : {}, 成为理财家时间  : {}, 不生成推荐奖，只有用户在成为理财家之后注册的用户才给推荐奖 ",regUserId,ruleTime,rosStaffInfo.getCreateTime());
				return null;
			}
			rcommendEarnInfo.setUserType(rosStaffInfo.getType());
			rcommendEarnInfo.setNote("内部员工");
			rcommendEarnInfo.setState(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
		} else {
			rcommendEarnInfo.setUserType(VasConstants.RECOMMEND_USER_TYPE_COMMON);
			rcommendEarnInfo.setNote("好友推荐");
			rcommendEarnInfo.setState(VasConstants.RECOMMEND_EARN_STATE_INIT);
		}
		rcommendEarnInfo.setResourceId(bidInvest.getId());
		return rcommendEarnInfo;
	}

	@Override
	public ResponseEntity<?> recommendEarnList(RecommendEarnVO recommendEarnVO, Pager pager) {
		List<RecommendEarnVO> result = new ArrayList<>();
		List<RegUserFriends> regUserFriends = regUserFriendsService
				.findFirstFriendsByRecommendId(recommendEarnVO.getRecommendRegUserId(),UserConstants.USER_FRIEND_GRADE_FIRST);
		if (regUserFriends != null) {
			VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();
			vasBiddRecommendEarn.setRecommendRegUserId(recommendEarnVO.getRecommendRegUserId());
			List<VasBidRecommendEarn> list = this.vasBidRecommendEarnService
					.findVasBidRecommendEarnList(vasBiddRecommendEarn);
			if (list != null) {
				Map<Integer, List<VasBidRecommendEarn>> map = list.stream()
						.collect(Collectors.groupingBy(VasBidRecommendEarn::getRegUserId));
				for (RegUserFriends ruf : regUserFriends) {
					List<VasBidRecommendEarn> tmp = map.get(ruf.getRegUserId());
					if (CommonUtils.isEmpty(tmp)) {
						RecommendEarnVO vo = new RecommendEarnVO();
						vo.setRegUserId(ruf.getRegUserId());
						vo.setRecommedTel(Long.parseLong(ruf.getTel()));
						vo.setCreateTime(DateUtils.parse("0000-00-00 00:00:00"));
						result.add(vo);
					} else {
						tmp.forEach(o -> {
							RecommendEarnVO vo = new RecommendEarnVO();
							vo.setRegUserId(ruf.getRegUserId());
							vo.setCreateTime(o.getCreateTime());
							vo.setInvestAmount(o.getInvestAmount());
							vo.setEarnAmount(o.getEarnAmount());
							vo.setRecommedTel(Long.parseLong(ruf.getTel()));
							result.add(vo);
						});
					}

				}
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	@Override
	public ResponseEntity<?> updateRecommendEarnByIds(List<Integer> recommendEarnIds, Integer state, String note) {
		logger.info("方法: updateRecommendEarnByIds, 好友推荐奖金审核, 入参: recommendEarnIds: {}, state: {}, note: {}",
				recommendEarnIds.toString(), state, note);
		VasBidRecommendEarn biddRecommendEarn = null;// 推荐奖VO
		// 判断是否有需要审核的推荐信息
		if (recommendEarnIds == null || recommendEarnIds.size() <= 0) {
			logger.info("好友推荐奖金审核, 没有需要审核的推荐信息！");
			return new ResponseEntity<>(Constants.ERROR, "好友推荐奖金审核,没有需要审核的推荐信息！");
		}
		int sucCount = 0; // 审核成功的推荐奖记录数
		// 将需要审核的推荐奖ID信息放在LIST中，审核成功以后移除，用于统计审核失败的记录ID
		List<Integer> bidIdList = new ArrayList<Integer>();
		bidIdList.addAll(recommendEarnIds);
		for (int i = 0; i < recommendEarnIds.size(); i++) {
			try {
				// 查询需要审核的好友推荐信息是否存在
				biddRecommendEarn = vasBidRecommendEarnService.findVasBidRecommendEarnById(recommendEarnIds.get(i));
				if (biddRecommendEarn != null) {
					if (biddRecommendEarn.getState() == VasConstants.RECOMMEND_EARN_STATE_INIT) {
						VasBidRecommendEarn vbr = new VasBidRecommendEarn();
						vbr.setId(recommendEarnIds.get(i));
						vbr.setState(state);
						// 审核拒绝时，记录审核拒绝的原因
						if (state == VasConstants.RECOMMEND_EARN_STATE_REVIEW_FAIL) {
							vbr.setNote(note);
						}
						// 通过ID更新好友推荐审核状态，并移除审核成功的推荐ID
						if (vasBidRecommendEarnService.updateVasBidRecommendEarn(vbr) > 0) {
							sucCount++;
							if (bidIdList.contains(biddRecommendEarn.getId())) {
								bidIdList.remove(biddRecommendEarn.getId());
							}
						} else {
							logger.error("好友推荐奖金审核, 好友推荐标识: {}, 审核好友推荐失败!", biddRecommendEarn.getId());
							throw new BusinessException("更新审核好友推荐奖失败！");
						}
					} else {
						logger.error("好友推荐奖金审核, 好友推荐标识: {}, 推荐记录状态: {}, 审核好友推荐失败,该数据已经审核！", biddRecommendEarn.getId(),
								biddRecommendEarn.getState());
					}
				} else {
					logger.error("好友推荐奖金审核, 好友推荐标识: {}, 审核好友推荐失败，没有需要审核数据！", recommendEarnIds.get(i));
				}
			} catch (Exception e) {
				logger.error("好友推荐奖金审核, 好友推荐标识: {}, 更新审核好友推荐失败: ", biddRecommendEarn.getId(), e);
			}
		}
		// 全部处理完成为审核成功，否则返回错误信息
		return bidIdList.size() == 0 ? new ResponseEntity<>(Constants.SUCCESS)
				: new ResponseEntity<>(Constants.ERROR,
						"审核成功数:" + sucCount + ";审核失败数：" + (recommendEarnIds.size() - sucCount) + ";失败的id:" + bidIdList);

	}

	@Override
	public ResponseEntity<?> sendReconmmendEarn(List<Integer> recommendEarnIds) {
		logger.info("方法: sendReconmmendEarn, 好友推荐奖金发放, 入参: recommendEarnIds: {}", JsonUtils.toJson(recommendEarnIds));
		Integer bidRecommendEarnId = 0;
		// 1、判断是否有需要发放的推荐信息
		if (recommendEarnIds == null || recommendEarnIds.size() <= 0) {
			logger.error("好友推荐奖金发放,没有需要发放的推荐信息！");
			return new ResponseEntity<>(Constants.ERROR, "好友推荐奖金发放，没有需要发放的推荐信息！");
		}
		// 2、将需要发放的推荐奖ID信息放在LIST中，发放成功以后移除，用于统计发放失败的记录ID
		List<Integer> bidRecommendEarnIdList = new ArrayList<Integer>();
		bidRecommendEarnIdList.addAll(recommendEarnIds);
		// 3、遍历循环需要发放的推荐奖列表信息，进行推荐奖发放主流程（更新推荐奖状态，以及生成发放流水信息）
		for (int i = 0; i < recommendEarnIds.size(); i++) {
			try {
				bidRecommendEarnId = recommendEarnIds.get(i);
				ApplicationContextUtils.getBean(RecommendEarnFacadeImpl.class).sendRecommendEarn(bidRecommendEarnId,
						bidRecommendEarnIdList);
			} catch (Exception e) {
				logger.error("好友推荐奖金发放, 好友推荐标识: {}, 好友推荐奖发放失败: {}", bidRecommendEarnId, e);
			}
		}
		return bidRecommendEarnIdList.size() == 0 ? new ResponseEntity<>(Constants.SUCCESS)
				: new ResponseEntity<>(Constants.SUCCESS,
						"发放成功数：" + (recommendEarnIds.size() - bidRecommendEarnIdList.size()) + ";发放失败数："
								+ bidRecommendEarnIdList.size() + ";失败的id：" + bidRecommendEarnIdList);
	}

	/***
	 * @Description : 推荐奖发放主流程
	 * @Method_Name : sendRecommendEarn;
	 * @param bidRecommendEarnId
	 *            推荐奖发放Id
	 * @param bidRecommendEarnIdList
	 *            推荐奖发放记录LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年10月25日 上午11:09:09;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	// @Compensable
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void sendRecommendEarn(Integer bidRecommendEarnId, List<Integer> bidRecommendEarnIdList) {
		int step = 1;// 推荐奖发放主业务流程
		// 3-(1)、查询需要审核的好友推荐信息是否存在
		VasBidRecommendEarn biddRecommendEarn = vasBidRecommendEarnService
				.findVasBidRecommendEarnById(bidRecommendEarnId);
		if (biddRecommendEarn != null) {
			// 只有审核成功的才可以发放推荐奖金
			if (biddRecommendEarn.getState() == VasConstants.RECOMMEND_EARN_STATE_REVIEW_SUCCESS) {
				// step1: 3-(2)更新推荐奖金发放状态为已发放
				VasBidRecommendEarn vbr = new VasBidRecommendEarn();
				vbr.setId(bidRecommendEarnId);
				vbr.setState(VasConstants.RECOMMEND_EARN_STATE_GRANT_SUCCESS);
				vbr.setGrantTime(new Date());
				vbr.setModifyTime(new Date());
				// 如果更新成功，则生成发放流水信息
				logger.info("好友推荐奖金发放, 用户标识: {}, 好友推荐奖金发放标识: {},  推荐奖金发放操作步骤: {}, 更新体验金发放状态为已发放",
						biddRecommendEarn.getRecommendRegUserId(), bidRecommendEarnId, step);
				if (vasBidRecommendEarnService.updateVasBidRecommendEarn(vbr) > 0) {
					step++;
					// 平台给用户发放推荐奖金到用户平台账户中
					TransferVo transferInfo = buildTransferInfo(biddRecommendEarn,TRADE_TYPE_RECOMMEND);
					// step2: 3-(3)生成推荐奖发放流水信息
					logger.info("好友推荐奖金发放, 用户标识: {}, 好友推荐奖金发放标识: {}, 推荐奖金发放操作步骤: {}, 生成推荐奖发放流水",
							biddRecommendEarn.getRecommendRegUserId(), bidRecommendEarnId, step);
					ResponseEntity<?> payResult = finConsumptionService.transferPay(transferInfo);
					if (payResult.getResStatus() == Constants.SUCCESS) {
						// 如果生成流水成功，移除成功的推荐奖发放ID，然后发送短信及站内信
						if (bidRecommendEarnIdList.contains(biddRecommendEarn.getId())) {
							bidRecommendEarnIdList.remove(biddRecommendEarn.getId());
						}
						// 查询推荐用户信息，发送短信及站内信
						RegUser regUser = regUserService.findRegUserById(biddRecommendEarn.getRecommendRegUserId());
						if (regUser != null) {
							// 发送短信
							/*SmsSendUtil.sendTelMsgToQueue(new SmsTelMsg(regUser.getLogin(),
									SmsMsgTemplate.MSG_RECOMMEND_EARN_GRANT.getMsg(), SmsConstants.SMS_TYPE_NOTICE,
									new String[] { String.valueOf(biddRecommendEarn.getEarnAmount()) }));*/
							// 发送站内信
							SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg(regUser.getId(),
									SmsMsgTemplate.MSG_RECOMMEND_EARN_GRANT.getTitle(),
									SmsMsgTemplate.MSG_RECOMMEND_EARN_GRANT.getMsg(),
									SmsConstants.SMS_TYPE_NOTICE,
									new String[] { String.valueOf(biddRecommendEarn.getEarnAmount()) }));
						} else {
							logger.error("好友推荐奖金发放, 用户标识: {}, 好友推荐奖发放标识: {}, 发放推荐奖成功,发送站内信,短信失败,失败原因为未查询到推荐人用户信息",
									biddRecommendEarn.getRecommendRegUserId(), bidRecommendEarnId);
						}
					} else {
						logger.error("好友推荐奖金发放, 用户标识: {}, 好友推荐奖发放标识: {}, 发放推荐奖失败: {}",
								biddRecommendEarn.getRecommendRegUserId(), bidRecommendEarnId, payResult.getResMsg());
						throw new BusinessException("好友推荐奖发放失败!");
					}
				}
			} else {
				logger.error("好友推荐奖金发放, 用户标识: {}, 好友推荐标识: {}，好友推荐佣金发放失败，不符合发放推荐奖的状态: {}",
						biddRecommendEarn.getRecommendRegUserId(), bidRecommendEarnId, biddRecommendEarn.getState());
			}
		} else {
			logger.info("好友推荐奖金发放, 好友推荐标识: {}, 好友推荐佣金发放失败,没有查询到要发放的佣金数据！", bidRecommendEarnId);
		}
	}

	/**
	 * @Description : 组装转账接口的VO对象
	 * @Method_Name : buildTransferInfo;
	 * @param biddRecommendEarn
	 * @return
	 * @return : TransferInfo;
	 * @Creation Date : 2017年10月25日 上午9:17:16;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private TransferVo buildTransferInfo(VasBidRecommendEarn biddRecommendEarn,Integer tradeType) {
		TransferVo transferInfo = new TransferVo();
		transferInfo.setFromUserId(UserConstants.PLATFORM_ACCOUNT_ID);
		transferInfo.setToUserId(biddRecommendEarn.getRecommendRegUserId());
		transferInfo.setBusinessCode(biddRecommendEarn.getId().toString());
		transferInfo.setTradeType(tradeType);
		transferInfo.setTransMoney(biddRecommendEarn.getEarnAmount());
		transferInfo.setTransferInSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.COMMISSION_MONEY));
		transferInfo.setTransferOutSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.COMMISSION_MONEY));
		transferInfo.setPlatformSourceEnums(PlatformSourceEnums.PC);
		transferInfo.setCreateTime(new Date());
		return transferInfo;
	}

	@Override
	public ResponseEntity<?> findRecommendEarnCountInfo(RecommendEarnVO recommendEarnVO, Pager page) {
		try {
			// 拼接查询参数map
			Map<String, Object> biddRecommendEarnMap = new HashMap<String, Object>();
			// 模糊查询，如果推荐人姓名不为空，则按照推荐人姓名模拟查询用户信息
			if (StringUtils.isNotBlank(recommendEarnVO.getRecommendName())) {
				List<Integer> recommendRegUserIds = regUserService
						.findUserIdsByFuzzyName(recommendEarnVO.getRecommendName());
				biddRecommendEarnMap.put("recommendRegUserId", recommendRegUserIds);
			}
			// 模糊查询，如果推荐人手机号不为空，则按照手机号查询用户信息
			if (recommendEarnVO.getRecommedTel() != null) {
				List<Integer> recomRegUsers = new ArrayList<Integer>();
				recomRegUsers.add(0);
				recomRegUsers.addAll(regUserService.findUserIdsByTel(recommendEarnVO.getRecommedTel()));
				biddRecommendEarnMap.put("recommendRegUserId", recomRegUsers);
			}
			// 按照推荐人ID分组
			biddRecommendEarnMap.put("groupColumns", "recommend_reg_user_id");
			// 查询推荐记录分页信息
			Pager pagerInfo = vasBidRecommendEarnService.findVasBidRecommendEarnListByInfo(biddRecommendEarnMap, page);
			return new ResponseEntity<>(Constants.SUCCESS, buildRecommendCountInfo(pagerInfo));
		} catch (Exception e) {
			logger.error("佣金查询失败, 失败错误信息：", e);
			return new ResponseEntity<>(Constants.ERROR, "佣金查询失败！");
		}
	}

	/**
	 * @Description : 组装佣金查询信息
	 * @Method_Name : buildRecommendCountInfo;
	 * @param pagerInfo
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2017年10月19日 上午11:09:33;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	public Pager buildRecommendCountInfo(Pager pagerInfo) throws Exception {
		List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
		if (BaseUtil.resultPageHasNoData(pagerInfo)) {
			return pagerInfo;
		}
		// 获取佣金查询的分页信息
		List<VasBidRecommendEarn> bidRecommendList = (List<VasBidRecommendEarn>) pagerInfo.getData();
		for (VasBidRecommendEarn vasBiddRecommendEarn : bidRecommendList) {
			// 获取推荐人用户信息
			UserVO recommendUserVO = regUserService
					.findUserWithDetailById(vasBiddRecommendEarn.getRecommendRegUserId());
			// 获取推荐人的一级好友的总人数，及总奖励金额
			VasBidRecommendEarn biddRecommendEarn = new VasBidRecommendEarn();
			biddRecommendEarn.setFriendLevel(VasConstants.RECOMMEND_FRIEND_LEVEL_ONE);
			biddRecommendEarn.setRecommendRegUserId(vasBiddRecommendEarn.getRecommendRegUserId());
			ResponseEntity<?> bidRecomEarnFriendLevelOne = vasBidRecommendEarnService
					.bidRecommendEarnInfoCount(biddRecommendEarn);
			// 获取推荐人的二级好友的总人数，及总奖励金额
			VasBidRecommendEarn biddRecommendEarn2 = new VasBidRecommendEarn();
			biddRecommendEarn2.setFriendLevel(VasConstants.RECOMMEND_FRIEND_LEVEL_TWO);
			biddRecommendEarn2.setRecommendRegUserId(vasBiddRecommendEarn.getRecommendRegUserId());
			ResponseEntity<?> bidRecomEarnFriendLevelTwo = vasBidRecommendEarnService
					.bidRecommendEarnInfoCount(biddRecommendEarn2);
			// 通过反射给返回页面实体对象赋值
			RecommendEarnVO biddRecommendEarnVo = new RecommendEarnVO();
			// 一级好友赋值
			if (bidRecomEarnFriendLevelOne.getResMsg() != null) {
				Map<String, Object> levenOne = (Map<String, Object>) bidRecomEarnFriendLevelOne.getResMsg();
				biddRecommendEarnVo.setSumFriendLevelOne((Integer.parseInt(levenOne.get("sumFriendLevel").toString())));
				biddRecommendEarnVo.setTotalEarnAmount(new BigDecimal(levenOne.get("sumEarnAmount").toString()));
			}
			// 二级好友赋值
			if (bidRecomEarnFriendLevelTwo.getResMsg() != null) {
				Map<String, Object> levenTwo = (Map<String, Object>) bidRecomEarnFriendLevelTwo.getResMsg();
				biddRecommendEarnVo.setSumFriendLevelTwo(Integer.parseInt(levenTwo.get("sumFriendLevel").toString()));
				BigDecimal totalMoney = (biddRecommendEarnVo.getTotalEarnAmount() == null ? BigDecimal.ZERO
						: biddRecommendEarnVo.getTotalEarnAmount());
				BigDecimal sumLevelTwoMoney = new BigDecimal(levenTwo.get("sumEarnAmount").toString());
				biddRecommendEarnVo.setTotalEarnAmount(totalMoney.add(sumLevelTwoMoney));
			}
			// 两个对象之间相同Bean属性赋值
			ClassReflection.reflectionAttr(biddRecommendEarn, biddRecommendEarnVo);
			if (recommendUserVO != null) {
				biddRecommendEarnVo.setRecommedTel(recommendUserVO.getLogin());
				biddRecommendEarnVo.setRecommendName(recommendUserVO.getRealName());
				biddRecommendEarnVo.setGroupCode(recommendUserVO.getGroupCode());
				biddRecommendEarnVo.setCommendNo(recommendUserVO.getCommendNo());
			}
			biddRecommendEarnVoList.add(biddRecommendEarnVo);
		}
		pagerInfo.setData(biddRecommendEarnVoList);
		return pagerInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findMyRecommendEarn(RecommendEarnVO recommendEarnVO, Pager pager) {
		List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
		try {
			// 1、分页查询我的推荐好友信息
			VasBidRecommendEarn vasBidRecommendEarn = new VasBidRecommendEarn();
			List<Integer> stateList = new ArrayList<Integer>();
			stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_TRANT);
			stateList.add(VasConstants.RECOMMEND_EARN_STATE_GRANT_SUCCESS);
			stateList.add(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
			//stateList.add(VasConstants.RECOMMEND_EARN_STATE_REVIEW_SUCCESS);
			vasBidRecommendEarn.setSortColumns("create_time desc");
			vasBidRecommendEarn.setStateList(stateList);
			vasBidRecommendEarn.setRecommendRegUserId(recommendEarnVO.getRecommendRegUserId());
			vasBidRecommendEarn
					.setFriendLevel(recommendEarnVO.getFriendLevel());
			Pager pagers = vasBidRecommendEarnService.findVasBidRecommendEarnList(vasBidRecommendEarn, pager);
			if (BaseUtil.resultPageHasNoData(pagers)) {
				return new ResponseEntity<>(SUCCESS, pagers);
			}
			// 2、组装前台展示数据
			List<VasBidRecommendEarn> biddRecommendList = (List<VasBidRecommendEarn>) pager.getData();
			for (VasBidRecommendEarn biddRecommendEarn : biddRecommendList) {
				// 获取被推荐人用户信息
				RegUser regUser = BaseUtil.getRegUser(biddRecommendEarn.getRegUserId(), ()->this.regUserService.findRegUserById(biddRecommendEarn.getRegUserId()));
				RegUserDetail regUserDetail= BaseUtil.getRegUserDetail(biddRecommendEarn.getRegUserId(), ()->this.regUserDetailService.findRegUserDetailByRegUserId(biddRecommendEarn.getRegUserId()));

				// 通过反射给返回页面实体对象赋值
				RecommendEarnVO biddRecommendEarnVo = new RecommendEarnVO();
				// 两个对象之间相同Bean属性赋值
				ClassReflection.reflectionAttr(biddRecommendEarn, biddRecommendEarnVo);
				// 如果被推荐用户不为空，则进行页面展示信息组装
				if (regUser != null) {
					biddRecommendEarnVo.setReferraTel(regUser.getLogin());
					if(regUser.getIdentify() == 0){
		                 biddRecommendEarnVo.setReferraName(StringUtils.isBlank(regUserDetail.getRealName())?regUser.getNickName():regUserDetail.getRealName());
					}else{
		                 biddRecommendEarnVo.setReferraName("**" + regUserDetail.getRealName().substring(1));
					}
				}
				biddRecommendEarnVoList.add(biddRecommendEarnVo);
			}
			pager.setData(biddRecommendEarnVoList);
			return new ResponseEntity<>(SUCCESS, pager);
		} catch (Exception e) {
			logger.error("用户标识: {}, 前台查询我的推荐奖金明细异常: " + recommendEarnVO.getRecommendRegUserId(), e);
			return new ResponseEntity<>(SUCCESS, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> recommendEarnListForApp(Integer regUserId, Pager pager) {
		RecommendEarnForAppVo vo = new RecommendEarnForAppVo();
		vo.setRecommendRegUserId(regUserId);
		Pager result = this.vasBidRecommendEarnService.recommendEarnListForApp(vo, pager);
		if (result != null && CommonUtils.isNotEmpty(result.getData())) {
			List<RecommendEarnForAppVo> list = (List<RecommendEarnForAppVo>) result.getData();
			list.forEach(tmp->{
				if (tmp.getType() == 0){
					tmp.setSource("好友定期投资");
				}
				if (tmp.getType() == 3){
					tmp.setSource("好友投资钱袋子");
				}
			});
			return new ResponseEntity<>(Constants.SUCCESS, list);
		}
		return new ResponseEntity<>(Constants.ERROR, "未查询到奖励记录");
	}

    @Override
    public ResponseEntity<?> findPropertyAchievement(RecommendEarnVO recommendEarnVO, Pager pager) {
        List<Integer> investUserIdList=new ArrayList<Integer>();
        Map<Integer, RosStaffInfoVo> rosMap = new HashMap<Integer,RosStaffInfoVo>();
        if((StringUtils.isNotBlank(recommendEarnVO.getGroupCode()) && !"-999".equals(recommendEarnVO.getGroupCode()) ) || recommendEarnVO.getRecommedTel() != null){
            //1查询物业公司集合
            RegUserDetail regUserDetail=regUserDetailService.findRegUserDetailByGroupCode(recommendEarnVO.getGroupCode());
            //2、查询物业公司下的员工信息
            RosStaffInfoVo rosStaffInfoVo=new RosStaffInfoVo();
            rosStaffInfoVo.setLogin(recommendEarnVO.getRecommedTel());
            if(regUserDetail!=null){
                rosStaffInfoVo.setEnterpriseRegUserIdList(Arrays.asList(regUserDetail.getRegUserId()));
            }
            List<RosStaffInfoVo> rosStaffInfoVoList=rosStaffInfoService.findRosStaffInfoList(rosStaffInfoVo);
            
            //3、遍历员工信息，组装员工ID集合
            List<Integer> friendIds = new ArrayList<Integer>();
            if(CommonUtils.isNotEmpty(rosStaffInfoVoList)){
                rosStaffInfoVoList.forEach(rosInfoVo->{
                    friendIds.add(rosInfoVo.getRegUserId());
                    rosMap.put(rosInfoVo.getRegUserId(), rosInfoVo);
                });
            }
            //4、查询员工推荐的用户集合
            List<RegUserFriends> regUserFriends = regUserFriendsService.findFirstFriendsByRecommendIds(friendIds.size() == 0 ? Arrays.asList(0) : friendIds);
            if(CommonUtils.isNotEmpty(regUserFriends)){
                regUserFriends.forEach(userFriend->{
                    investUserIdList.add(userFriend.getRegUserId());
                });
            }
          //5、查询投资人的投资信息
            BidInvestDetailVO bidInvestDetailVO = new BidInvestDetailVO();
            bidInvestDetailVO.setRegUserIdList(investUserIdList.size() == 0 ? null : investUserIdList);
            bidInvestDetailVO.setCreateTimeBegin(DateUtils.format(recommendEarnVO.getCreateTimeBegin(), DateUtils.DATE_HH_MM_SS));
            bidInvestDetailVO.setCreateTimeEnd(DateUtils.format(recommendEarnVO.getCreateTimeEnd(), DateUtils.DATE_HH_MM_SS));
            Pager pagerInfo=bidInvestService.findBidInvestDetailList(bidInvestDetailVO, pager);
            //6、组装渲染页面
            return new ResponseEntity<>(SUCCESS,buildRecommendInfo(pagerInfo,rosMap));
        }else{
            return new ResponseEntity<>(SUCCESS,null);
        }
        
    }
    /**
     *  @Description    : 组装返回物业推荐信息
     *  @Method_Name    : buildRecommendInfo;
     *  @param pagerInfo
     *  @return
     *  @return         : Pager;
     *  @Creation Date  : 2018年8月21日 下午5:18:56;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    private Pager buildRecommendInfo(Pager pagerInfo,Map<Integer, RosStaffInfoVo> rosMap ){
        List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
        if (BaseUtil.resultPageHasNoData(pagerInfo)) {
            return pagerInfo;
        }
        // 获取投资记录分页信息
        List<BidInvestDetailVO> bidRecommendList = (List<BidInvestDetailVO>) pagerInfo.getData();
        bidRecommendList.stream().forEach(bidInvestVo->{
            RegUserFriends recomendUser=regUserFriendsService.findRegUserFriendsByRegUserId(bidInvestVo.getRegUserId(), UserConstants.USER_FRIEND_GRADE_FIRST);
            UserVO userVo=this.regUserService.findUserWithDetailById(recomendUser.getRecommendId());
            RosStaffInfoVo rosStaffInfoVo = rosMap.get(recomendUser.getRecommendId());
            RecommendEarnVO recomendVo = new RecommendEarnVO();
            if(rosStaffInfoVo!=null){
                UserVO enterVo = this.regUserService.findUserWithDetailById(rosStaffInfoVo.getEnterpriseRegUserId());
                recomendVo.setEnterpriseName(enterVo.getRealName());
                recomendVo.setGroupCode(enterVo.getGroupCode() );
            }
            recomendVo.setRecommedTel(userVo.getLogin());
            recomendVo.setRecommendName(userVo.getRealName());
            recomendVo.setBiddName(bidInvestVo.getBidName());
            recomendVo.setInvestTime(bidInvestVo.getCreateTime());
            recomendVo.setReferraTel(Long.parseLong(recomendUser.getTel()));
            recomendVo.setReferraName(recomendUser.getRealName());
            recomendVo.setInvestAmount(bidInvestVo.getInvestAmount());
            recomendVo.setBackStepMoney(bidInvestVo.getInvestAmount()
                    .multiply(CalcInterestUtil.calFoldRatio(bidInvestVo.getTermUnit(), bidInvestVo.getTermValue())));
            biddRecommendEarnVoList.add(recomendVo);
        });
        pagerInfo.setData(biddRecommendEarnVoList);
        return pagerInfo;
    }

    @Override
    public ResponseEntity<?> findRecommendEarn(RecommendEarnVO recommendEarnVO, Pager page) {
     // 拼接查询参数map
        Map<String, Object> biddRecommendEarnMap = new HashMap<String, Object>();
        Pager pagerInfo = null;
        try {
            // 判断查询条件是否选择了标的投资开始时间和投资结束时间，只要选择了一项，则查询标的信息
            if (recommendEarnVO.getGrantTimeBegin() != null || recommendEarnVO.getGrantTimeEnd() != null) {
                // 查询时间段的投资记录，按照BIDINFOID分组，获取时间段内的标的ID集合
                BidInvest bidInvest = new BidInvest();
                bidInvest.setCreateTimeBegin(recommendEarnVO.getGrantTimeBegin());
                bidInvest.setCreateTimeEnd(recommendEarnVO.getGrantTimeEnd());
                List<Integer> bidInfoIds = bidInvestService.findBidInfoIdByCondition(bidInvest);
                if (bidInfoIds != null && bidInfoIds.size() > 0) {
                    biddRecommendEarnMap.put("biddIdList", bidInfoIds);
                } else {
                    List<Integer> bidList = new ArrayList<Integer>();
                    bidList.add(0);
                    biddRecommendEarnMap.put("biddIdList", bidList);
                }
            }
            // 判断用户是否输入了推荐人手机号作为查询条件，如果输入，则查询推荐人对应的用户信息
            if (recommendEarnVO.getRecommedTel() != null || StringUtils.isNotBlank(recommendEarnVO.getGroupCode())
                    || StringUtils.isNotBlank(recommendEarnVO.getCommendNo())) {
                // 获取推荐人用户信息
                List<Integer> recommendRegUserIdList = new ArrayList<Integer>();
                UserVO userVO = new UserVO();
                if (StringUtils.isNotBlank(recommendEarnVO.getCommendNo())) {
                    userVO.setInviteNo(recommendEarnVO.getCommendNo());
                }
                userVO.setGroupCode(recommendEarnVO.getGroupCode());
                userVO.setLogin(recommendEarnVO.getRecommedTel());
                recommendRegUserIdList.add(0);// 默认查询条件
                recommendRegUserIdList.addAll(regUserService.findUserIdsByUserVO(userVO));
                biddRecommendEarnMap.put("recommendRegUserId", recommendRegUserIdList);
               
            }
            // 判断用户是否输入了被推荐人手机号作为查询条件，如果输入，则查询被推荐人用户信息
            if (recommendEarnVO.getReferraTel() != null) {
                // 获 取被推荐人用户信息
                List<Integer> regUserIdList = new ArrayList<Integer>();
                regUserIdList.add(0);// 默认查询条件
                regUserIdList.addAll(regUserService.findUserIdsByTel(recommendEarnVO.getReferraTel()));
                biddRecommendEarnMap.put("regUserId", regUserIdList);
            }
            // 其它推荐记录表中的查询条件
            biddRecommendEarnMap.put("state", recommendEarnVO.getState());
            biddRecommendEarnMap.put("recommendState", recommendEarnVO.getRecommendState());
            biddRecommendEarnMap.put("createTimeBegin", recommendEarnVO.getCreateTimeBegin());
            biddRecommendEarnMap.put("createTimeEnd", recommendEarnVO.getCreateTimeEnd());
            biddRecommendEarnMap.put("sortColumns", "create_time desc");
            // 查询推荐记录分页信息
            pagerInfo = vasBidRecommendEarnService.findVasBidRecommendEarnListByInfo(biddRecommendEarnMap, page);
            // 组装返回页面信息
            return new ResponseEntity<>(SUCCESS, createBiddRecommendEarnVo(pagerInfo));
        } catch (Exception e) {
            logger.error("查询推荐好友信息异常: ", e);
            return new ResponseEntity<>(ERROR, "查询异常");
        }
    }
    /**
     *  @Description    : 组装返回推荐奖审核信息
     *  @Method_Name    : createBiddRecommendEarnVo;
     *  @param result
     *  @return
     *  @throws Exception
     *  @return         : Pager;
     *  @Creation Date  : 2018年9月27日 上午11:18:37;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    private Pager createBiddRecommendEarnVo(Pager result) throws Exception {
        List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
        if (BaseUtil.resultPageHasNoData(result)) {
            return result;
        }
        List<VasBidRecommendEarn> biddRecommendList = (List<VasBidRecommendEarn>) result.getData();
        for (VasBidRecommendEarn biddRecommendEarn : biddRecommendList) {
            // 获取被推荐人用户信息
            RegUser regUser = BaseUtil.getRegUser(biddRecommendEarn.getRegUserId(), ()-> this.regUserService.findRegUserById(biddRecommendEarn.getRegUserId()));
            RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(biddRecommendEarn.getRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(biddRecommendEarn.getRegUserId()));
            // 获取推荐人用户信息
            RegUser recomRegUser = BaseUtil.getRegUser(biddRecommendEarn.getRecommendRegUserId(), ()-> this.regUserService.findRegUserById(biddRecommendEarn.getRecommendRegUserId()));
            RegUserDetail recomRegUserDetail = BaseUtil.getRegUserDetail(biddRecommendEarn.getRecommendRegUserId(), ()-> this.regUserDetailService.findRegUserDetailByRegUserId(biddRecommendEarn.getRecommendRegUserId()));
            // 获取标的信息
            BidInfoVO bidInfoVO = bidInfoService.findBidInfoDetailVo(biddRecommendEarn.getBidId());
            // 查询投资记录
            BidInvest bidInvest = bidInvestService.findBidInvestById(biddRecommendEarn.getResourceId());
            RecommendEarnVO biddRecommendEarnVo = new RecommendEarnVO();
            // 两个对象之间相同Bean属性赋值
            ClassReflection.reflectionAttr(biddRecommendEarn, biddRecommendEarnVo);
            // 如果推荐用户不为空，则进行页面展示信息组装
            if(recomRegUser != null && recomRegUserDetail != null){
                biddRecommendEarnVo.setRecommedTel(recomRegUser.getLogin());
                biddRecommendEarnVo.setRecommendName(recomRegUserDetail.getRealName());
                biddRecommendEarnVo.setGroupCode(recomRegUserDetail.getGroupCode());
                biddRecommendEarnVo.setCommendNo(recomRegUserDetail.getInviteNo());
            }
            // 如果被推荐用户不为空，则进行页面展示信息组装
            if (regUser != null && regUserDetail != null) {
                biddRecommendEarnVo.setReferraTel(regUser.getLogin());
                biddRecommendEarnVo.setReferraName(regUserDetail.getRealName());
            }
            // 如果标的信息不为空,则进行页面展示信息组装
            if (bidInfoVO != null) {
                biddRecommendEarnVo.setTermUnit(bidInfoVO.getTermUnit());
                biddRecommendEarnVo.setTermValue(bidInfoVO.getTermValue());
                biddRecommendEarnVo.setBiddName(bidInfoVO.getTitle());
                biddRecommendEarnVo.setInvestTime(bidInvest.getCreateTime());
                biddRecommendEarnVo.setBackStepMoney(bidInvest.getInvestAmount()
                        .multiply(CalcInterestUtil.calFoldRatio(bidInfoVO.getTermUnit(), bidInfoVO.getTermValue())));
                biddRecommendEarnVo.setBackStepRatio(
                        CalcInterestUtil.calFoldRatio(bidInfoVO.getTermUnit(), bidInfoVO.getTermValue()));
                biddRecommendEarnVo.setBidRate(bidInfoVO.getInterestRate());
            }
            biddRecommendEarnVoList.add(biddRecommendEarnVo);
        }
        result.setData(biddRecommendEarnVoList);
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> buildQdzRecommendEarn(RcommendEarnInfo rcommendEarnInfo)throws Exception {
        ResponseEntity<?> responseEntity = checkBidRecommendEarnInfo(rcommendEarnInfo);
        if (responseEntity.getResStatus() == Constants.ERROR) {
            logger.error("生成钱袋子推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 校验参数异常: {}",
                    rcommendEarnInfo.getRecommendRegUserId(), rcommendEarnInfo.getRegUserId(),
                    responseEntity.getResMsg().toString());
            return responseEntity;
        }
        // 查询推荐奖规则
        VasRebatesRule  vasRebatesRule = vasRebatesRuleService.findVasRebatesRuleByTypeAndState(rcommendEarnInfo.getType(),VasConstants.VAS_RULE_STATE_START);
        if (vasRebatesRule == null || StringUtils.isBlank(vasRebatesRule.getContent())) {
            logger.error("生成钱袋子推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖异常: {}",
                    rcommendEarnInfo.getRecommendRegUserId(), rcommendEarnInfo.getRegUserId(),
                    "没有查询到对应推荐规则记录!");
            return new ResponseEntity<>(Constants.ERROR, "没有查询到对应规则记录!");
        }
        logger.info("生成钱袋子推荐奖操作 推荐人标识: {}, 被推荐人标识: {}, 好友推荐规则 vasRebatesRule: {}",
                rcommendEarnInfo.getRecommendRegUserId(), rcommendEarnInfo.getRegUserId(),
                vasRebatesRule.toString());
        RebatesRuleItem qdzVsaRuleItem = JsonUtils.json2Object(vasRebatesRule.getContent(), RebatesRuleItem.class, null);
        if (qdzVsaRuleItem != null) {
            BigDecimal recomMoney = CalcInterestUtil.calDayOfInterest(rcommendEarnInfo.getInvestAmount(),new BigDecimal(qdzVsaRuleItem.getRebatesRateOne()));
            // 查询该用户是否属于内部员工
            RosStaffInfo rosStaffInfo = rosStaffInfoService.findRosStaffInfo(rcommendEarnInfo.getRecommendRegUserId(), null,
                       RosterConstants.ROSTER_STAFF_STATE_VALID,RosterConstants.ROSTER_STAFF_RECOM_STATE_SEND);
            VasBidRecommendEarn vasBiddRecommendEarn = new VasBidRecommendEarn();
            ClassReflection.reflectionAttr(rcommendEarnInfo, vasBiddRecommendEarn);
            vasBiddRecommendEarn.setRuleId(vasRebatesRule.getId());
            vasBiddRecommendEarn.setType(VasConstants.QDZ_INVEST_EARN);
            //该用户是内部员工,只生成推荐奖记录，不生成流水
            if(rosStaffInfo!=null){
                vasBiddRecommendEarn.setNote("内部员工钱袋子推荐奖");
                vasBiddRecommendEarn.setState(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_WAIT_TRANT);
                vasBiddRecommendEarn.setEarnAmount(recomMoney);
                vasBidRecommendEarnService.insertVasBidRecommendEarn(vasBiddRecommendEarn);
            }else{
                //普通用户，生成推荐奖记录，及交易流水信息
                vasBiddRecommendEarn.setNote("普通用户钱袋子推荐奖");
                vasBiddRecommendEarn.setState(VasConstants.RECOMMEND_EARN_STATE_NOT_REVIEW_TRANT);
                vasBiddRecommendEarn.setEarnAmount(recomMoney);
                vasBidRecommendEarnService.insertVasBidRecommendEarn(vasBiddRecommendEarn);
                if (recomMoney.compareTo(BigDecimal.ZERO) == 0) {
                    logger.info("生成钱袋子推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 给用户发放的奖金为 : {} ,生成推荐奖记录,不生成交易流水信息",rcommendEarnInfo.getRecommendRegUserId(), rcommendEarnInfo.getRegUserId(),recomMoney);
                } else {
                  // 平台给用户发放推荐奖金到用户平台账户中
                    TransferVo transferInfo = buildTransferInfo(vasBiddRecommendEarn,TRADE_TYPE_QDZ_COMMISSION);
                    logger.info("生成钱袋子推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖发放流水",
                            vasBiddRecommendEarn.getRecommendRegUserId(), rcommendEarnInfo.getRegUserId());
                    ResponseEntity<?> payResult = finConsumptionService.transferPay(transferInfo);
                    if(payResult.getResStatus() ==Constants.ERROR){
                        logger.error("生成钱袋子推荐奖操作, 推荐人标识: {}, 被推荐人标识: {}, 生成推荐奖发放流水异常: ",vasBiddRecommendEarn.getRecommendRegUserId(),rcommendEarnInfo.getRegUserId(),payResult.getResMsg().toString());
                        throw new BusinessException(payResult.getResMsg().toString());
                    }
                }
            }
        }
        return new ResponseEntity<>(Constants.SUCCESS) ;
    }
    /**
     * @Description :校验推荐奖参数有效性
     * @Method_Name : checkBidRecommendEarnInfo;
     * @param bidRcommendEarnInfo
     * @return
     * @return : ResponseEntity<?>;
     * @Creation Date : 2017年7月3日 上午9:48:12;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    private ResponseEntity<?> checkBidRecommendEarnInfo(RcommendEarnInfo bidRcommendEarnInfo) {
        if (bidRcommendEarnInfo.getRegUserId() == null || bidRcommendEarnInfo.getRegUserId() == 0) {
            return new ResponseEntity<>(Constants.ERROR, "被推荐人ID不能为空,不发推荐奖");
        }
        if (bidRcommendEarnInfo.getRecommendRegUserId() == null || bidRcommendEarnInfo.getRecommendRegUserId() == 0) {
            return new ResponseEntity<>(Constants.ERROR, "推荐人ID不能为空,不发推荐奖");
        }
        if (bidRcommendEarnInfo.getResourceId() == null || bidRcommendEarnInfo.getResourceId() == 0) {
            return new ResponseEntity<>(Constants.ERROR, "来源ID不能为空,不发推荐奖！");
        }
        if (bidRcommendEarnInfo.getInvestAmount() == null
                || bidRcommendEarnInfo.getInvestAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ResponseEntity<>(Constants.ERROR, "投资金额不能为空！");
        }
        if (bidRcommendEarnInfo.getFriendLevel() == null || bidRcommendEarnInfo.getFriendLevel() == 0) {
            return new ResponseEntity<>(Constants.ERROR, "推荐好友级别不能为空！");
        }
        if (bidRcommendEarnInfo.getType() == null) {
            return new ResponseEntity<>(Constants.ERROR, "推荐奖类型不能为空！");
        }
        if (bidRcommendEarnInfo.getRegUserId().equals(bidRcommendEarnInfo.getRecommendRegUserId())) {
            return new ResponseEntity<>(Constants.ERROR, "推荐人是自己不发推荐奖！");
        }
        return new ResponseEntity<>(Constants.SUCCESS);
    }

    @Override
    public ResponseEntity<?> searchRecommendEarnByUserId(RecommendEarnVO recommendEarnVO,
            Pager page) {
        Map<String,Object> biddRecommendEarnMap=new HashMap<String,Object>();
        Pager  pagerInfo =null;
        try{
            biddRecommendEarnMap.put("recommendRegUserId", Arrays.asList(recommendEarnVO.getRecommendRegUserId()));
            biddRecommendEarnMap.put("sortColumns", "create_time desc");
            pagerInfo = vasBidRecommendEarnService.findVasBidRecommendEarnListByInfo(biddRecommendEarnMap, page);
            if (BaseUtil.resultPageHasNoData(pagerInfo)) {
                return new ResponseEntity<>(SUCCESS,pagerInfo);
            }
            List<RecommendEarnVO> biddRecommendEarnVoList = new ArrayList<RecommendEarnVO>();
            List<VasBidRecommendEarn> biddRecommendList = (List<VasBidRecommendEarn>) pagerInfo.getData();
            for (VasBidRecommendEarn biddRecommendEarn : biddRecommendList) {
                RecommendEarnVO recomendVo=new RecommendEarnVO();
                UserVO userVo=this.regUserService.findUserWithDetailById(biddRecommendEarn.getRegUserId());
                ClassReflection.reflectionAttr(biddRecommendEarn, recomendVo);
                recomendVo.setReferraTel(userVo.getLogin());
                recomendVo.setReferraName(userVo.getRealName());
                biddRecommendEarnVoList.add(recomendVo);
            }
            pagerInfo.setData(biddRecommendEarnVoList);
        }catch(Exception e){
            logger.error("查询推荐人的奖金明细失败, 推荐人id: {}, 失败信息: {}",recommendEarnVO.getRecommendRegUserId(),e);
        }
        return new ResponseEntity<>(SUCCESS,pagerInfo);
    }
}
