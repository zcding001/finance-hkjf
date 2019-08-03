package com.hongkun.finance.user.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.model.BidInfo;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInfoVO;
import com.hongkun.finance.invest.service.BidInfoService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.hongkun.finance.monitor.constants.MonitorConstants;
import com.hongkun.finance.monitor.model.MonitorOperateRecord;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.factory.BaofuProtocolPayFactory;
import com.hongkun.finance.payment.factory.LianlianPayFactory;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.model.FinPayConfig;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.point.constants.PointConstants;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointMerchantInfo;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointMerchantInfoService;
import com.hongkun.finance.qdz.facade.QdzTransferFacade;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.hongkun.finance.qdz.service.QdzTransferService;
import com.hongkun.finance.roster.constants.RosterConstants;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.model.RosDepositInfo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosDepositInfoService;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.roster.service.SysFunctionCfgService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.service.SmsWebMsgService;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.constants.UserRegistSource;
import com.hongkun.finance.user.facade.UserFacade;
import com.hongkun.finance.user.model.*;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.*;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.user.utils.IDCardUtil;
import com.hongkun.finance.user.utils.ValidateUtil;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.service.VasService;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.singleton.SingletonThreadPool;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import com.yirun.framework.oss.OSSLoader;
import com.yirun.framework.oss.model.OSSBuckets;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Description :
 * @Project : finance
 * @Program Name : com.hongkun.finance.user.facade.impl.UserFacadeImpl
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */

@Service
public class UserFacadeImpl implements UserFacade {

	private static final Logger logger = LoggerFactory.getLogger(UserFacadeImpl.class);
	@Autowired
	private RegUserService regUserService;
	@Autowired
	private RegUserInfoService regUserInfoService;
	@Autowired
	private RegUserDetailService regUserDetailService;
	@Autowired
	private RegUserVipRecordService regUserVipRecordService;
	@Reference
	private FinAccountService accountService;
	@Reference
	private BidReceiptPlanService receiptPlanService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Autowired
	private RegUserFriendsService regUserFriendsService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private PointMerchantInfoService pointMerchantInfoService;
	@Reference
	private PointAccountService pointAccoutService;
	@Reference
	private BidReceiptPlanService bidReceiptPlanService;
	@Reference
	private BidRepayPlanService bidRepayPlanService;
	@Reference
	private QdzAccountService qdzAccountService;
	@Reference
	private RosDepositInfoService rosDepositInfoService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private PointAccountService pointAccountService;
	@Reference
	private SmsWebMsgService smsWebMsgService;
	@Reference
	private QdzTransferService qdzTransferService;
	@Reference
	private VasService vasService;
	@Reference
	private AppLoginLogService appLoginLogService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private RosInfoService rosInfoService;
	@Reference
	private RegAuditRuleService regAuditRuleService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinPayConfigService finPayConfigService;
	@Reference
	private FinChannelGrantService finChannelGrantService;
	@Reference
	private AppUserServeService appUserServeService;
	@Reference
	private AppMoreServeService appMoreServeService;
	@Reference
	private BidInfoService bidInfoService;
	@Reference
	private SysFunctionCfgService sysFunctionCfgService;
	@Reference
	private QdzTransferFacade qdzTransferFacade;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity queryUserAccountMoney(Pager pager, UserVO userVO) {
		/**
		 * step 1:查询符合条件的用户分页id集合
		 */
		// 定义账户缓存,减少数据库查询
		Map<Integer, FinAccount> accountCache = MapUtils.EMPTY_MAP;
		if (userVO.getUseableMoneyStart() != null || userVO.getUseableMoneyEnd() != null) {
			FinAccount finAccount = new FinAccount();
			finAccount.setUseableMoneyStart(userVO.getUseableMoneyStart());
			finAccount.setUseableMoneyEnd(userVO.getUseableMoneyEnd());
			accountCache = accountService.findByCondition(finAccount).stream().filter(Objects::nonNull).distinct()
					.collect(Collectors.toMap((account) -> account.getRegUserId(), (account) -> account));
		}
		// 添加限制id
		if (!MapUtils.isEmpty(accountCache)) {
			userVO.getUserIds().addAll(accountCache.keySet());
		}
		// 根据limitIds查出符合情况的第分页数据
		Pager userPage = regUserService.listConditionPage(userVO, pager);
		/**
		 * step 2:对其他属性进行补全
		 */
		if (!BaseUtil.resultPageHasNoData(userPage)) {
			List<UserVO> userVOS;
			Map<Integer, FinAccount> finalAccountCache = accountCache;
			userVOS = pager.getData().stream().map((user) -> {
				// 查询：/*待收本金，待收利息，待加息收益*/
				LoanVO loanVO = receiptPlanService.findAgencyFundByUserId(((UserVO) user).getUserId(), null);
				FinAccount finAccount = finalAccountCache.get(((UserVO) user).getUserId());
				if (finAccount == null) {
					finAccount = accountService.findByRegUserId(((UserVO) user).getUserId());
				}
                // 查询钱袋子待收本金
                BigDecimal total = Optional.ofNullable(finAccount.getNowMoney()).orElse(BigDecimal.ZERO);
                QdzAccount qdzAccount = this.qdzAccountService.findQdzAccountByRegUserId(((UserVO) user).getUserId());
                total = total.add(qdzAccount == null ? BigDecimal.ZERO : qdzAccount.getMoney());
                finAccount.setNowMoney(total);
                
				// 合并RegUer的属性
				return BeanPropertiesUtil.mergeAndReturn((UserVO) user, finAccount,
						loanVO == null ? new Double(1)
								: loanVO);/* 待收本金，待收利息，待加息收益 */

			}).collect(Collectors.toList());
			userPage.setData(userVOS);
		}
		// 没有任何的数据
		return new ResponseEntity(SUCCESS, userPage);
	}

	@Override
	public ResponseEntity<?> findUserVOList(Pager pager, UserVO userVO) {
		//查询已投资用户
		if(userVO.getUserGroup() == 1 || userVO.getUserGroup() == 2) {
			//已投资用户id
			List<Integer> ids = this.bidInvestService.findBidInvestPreferedList();
			if(userVO.getUserGroup() == 1) {
				if(CommonUtils.isEmpty(ids)) {
					return BaseUtil.emptyResult();
				}
				//投资用户
				userVO.setUserIds(ids);
			}else {
			    //实名未投资用户
				userVO.setNotIncludeIds(ids);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, this.regUserService.listConditionPage(userVO, pager));
	}

	@Override
	public ResponseEntity<?> importUsers(String filePath) {
		List<List<String>> dataList = ExcelUtil.getDataList(filePath);
		if (CommonUtils.isEmpty(dataList)) {
			return new ResponseEntity<>(Constants.ERROR, "未找到有效的数据");
		}
		// 校验手机号和身份证的合法性
		List<String> deWeightList = new ArrayList<>();
		for (int i = 0; i < dataList.size(); i++) {
			List<String> l = dataList.get(i);
			String tel = l.get(0);
			String idCard = l.get(3);
			ResponseEntity<?> result = ValidateUtil.validateLogin(tel);
			if (result.getResStatus() == Constants.ERROR) {
				return new ResponseEntity<>(Constants.ERROR, "非法手机号[" + tel + "],出现在第[" + (i + 1) + "]行");
			}
			if (StringUtils.isNotBlank(idCard) && !IDCardUtil.isIDCard(idCard)) {
				return new ResponseEntity<>(Constants.ERROR, "非法身份证号[" + idCard + "],出现在第[" + (i + 1) + "]行");
			}
			if (StringUtils.isNotBlank(l.get(2))) {
				try {
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(l.get(2));
				} catch (ParseException e) {
					return new ResponseEntity<>(Constants.ERROR,
							"时间格式[" + l.get(2) + "]非法(2013-11-11 11:11:11),出现在第[" + (i + 1) + "]行");
				}
			}
			if (StringUtils.isNotBlank(tel) && deWeightList.contains(tel)) {
				return new ResponseEntity<>(Constants.ERROR, "手机号[" + tel + "]重复出现,出现在第[" + (i + 1) + "]行");
			}
			if (StringUtils.isNotBlank(tel)) {
				deWeightList.add(tel);
			}
			if (StringUtils.isNotBlank(idCard) && deWeightList.contains(idCard)) {
				return new ResponseEntity<>(Constants.ERROR, "身份证号[" + idCard + "]重复出现,出现在第[" + (i + 1) + "]行");
			}
			if (StringUtils.isNotBlank(idCard)) {
				deWeightList.add(idCard);
			}
		}
		// 验证手机号&身份证在是否已经存在
		for (int i = 0; i < dataList.size(); i++) {
			List<String> l = dataList.get(i);
			String tel = l.get(0);
			String idCard = l.get(3);
			RegUser regUser = this.regUserService.findRegUserByLogin(Long.parseLong(tel));
			if (regUser != null) {
				return new ResponseEntity<>(Constants.ERROR, "手机号[" + tel + "]已经被注册,出现在第[" + (i + 1) + "]行");
			}
			if (StringUtils.isNotBlank(idCard)) {
				RegUserDetail regUserDetail = new RegUserDetail();
				regUserDetail.setIdCard(idCard);
				if (CommonUtils.isNotEmpty(this.regUserDetailService.findRegUserDetailList(regUserDetail))) {
					return new ResponseEntity<>(Constants.ERROR, "身份证号[" + idCard + "]已经被使用,出现在第[" + (i + 1) + "]行");
				}
			}
		}
		for (int i = 0; i < dataList.size(); i++) {
			try {
				List<String> l = dataList.get(i);
				RegUser regUser = new RegUser();
				regUser.setLogin(Long.parseLong(l.get(0)));
				regUser.setPasswd(UserConstants.USER_DEFAULT_PASSWD);
				regUser.setNickName("鸿坤金服");
				if (StringUtils.isNotBlank(l.get(3))) {
					regUser.setIdentify(1);
				}
				if (StringUtils.isNotBlank(l.get(2))) {
					regUser.setCreateTime(DateUtils.parse(l.get(2), DateUtils.DATE_HH_MM_SS));
				}
				regUser.setType(UserConstants.USER_TYPE_GENERAL);
				RegUserDetail regUserDetail = new RegUserDetail();
				regUserDetail.setRealName(l.get(1));
				if (StringUtils.isNotBlank(l.get(2))) {
					regUserDetail.setCreateTime(regUser.getCreateTime());
				}
				if (StringUtils.isNotBlank(l.get(3))) {
					regUserDetail.setIdCard(l.get(3));
				}
				regUserDetail.setRegistSource(UserRegistSource.IMPORT.getValue());
				if (l.size() >= 5 && StringUtils.isNotBlank(l.get(4))) {
					regUserDetail.setCommendNo(l.get(4));
				}
				if (l.size() >= 6 && StringUtils.isNotBlank(l.get(5))) {
					regUserDetail.setExtenSource(l.get(5));
				}
				return this.insertRegUserForRegist(regUser, regUserDetail);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Constants.ERROR, "导入第[" + (i + 1) + "]行系统异常，第[" + i + "]行已成功导入。");
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, "上传成功");
	}
	
	@Override
	public ResponseEntity<?> findUserDtailInfo(Integer regUserId){
        ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        //查询用户信息
        UserVO userVO = regUserService.findUserWithDetailById(regUserId);
        result.getParams().put("userVO", userVO);
        //查询资金账户
        FinAccount finAccount = this.finAccountService.findByRegUserId(regUserId);
        result.getParams().put("finAccount", finAccount);
        //检索签到数据
        QdzAccount qdzAccount = this.qdzAccountService.findQdzAccountByRegUserId(regUserId);
        result.getParams().put("qdzAccount", qdzAccount == null ? new QdzAccount() : qdzAccount);
        //查询待收款信息
        result.getParams().put("loanVO", this.bidReceiptPlanService.findAgencyFundByUserId(regUserId, null));
        //查询累计投资
        List<BidInvest> bidInvestList = this.bidInvestService.findInvests(null, null, regUserId);
        result.getParams().put("investTotal", bidInvestList.stream().mapToDouble(e -> e.getInvestAmount().floatValue()).sum());
        return result;
    }

	@Override
	public ResponseEntity<?> findUserMyAccount(Integer regUserId) {
		// 加载用户信息
		ResponseEntity<?> result = new ResponseEntity<>();
		RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
		result.getParams().put("regUser", regUser);
		result.getParams().put("regUserDetail", BaseUtil.getRegUserDetail(regUserId,
				() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId)));
		result.getParams().put("regUserInfo", BaseUtil.getRegUserInfo(regUserId,
                () -> this.regUserInfoService.findRegUserInfoByRegUserId(regUserId)));
		regUser.setPasswd(null);
        // 加载可用积分
        result.getParams().put("points",
                this.pointAccoutService.findPointAccountByRegUserId(regUser.getId()).getPoint());
        //加载增值服务：会员等级&成长值、体验金、卡券数量
        result.getParams().putAll(this.vasService.getVasInfo(regUser.getId(), "01011"));
        
		// 加载账户信息
		FinAccount finAccount = this.finAccountService.findByRegUserId(regUser.getId());
		BeanPropertiesUtil.setNullExtend(finAccount, "nowMoney", "freezeMoney", "useableMoney");
//		// 总资产
//		BigDecimal total = BigDecimal.ZERO.add(finAccount.getNowMoney());
//        BidInfoVO bidInfoVO = new BidInfoVO();
//        bidInfoVO.setProductTypeLimitIds(Collections.singletonList(InvestConstants.BID_PRODUCT_CURRENT));
//        List<Integer> bidIds = this.bidInfoService.findBidInfoVOByCondition(bidInfoVO).stream().map(BidInfoVO::getId).collect(Collectors.toList());
//		// 查询待收金额
//		LoanVO loanVO = this.bidReceiptPlanService.findAgencyFundByUserId(regUser.getId(), bidIds);
//        BigDecimal capitalAmount = loanVO != null ? loanVO.getCapitalAmount() : BigDecimal.ZERO;
//        BigDecimal interestAmount = loanVO != null ? loanVO.getInterestAmount() : BigDecimal.ZERO;
//		result.getParams().put("increaseAmount", loanVO != null ? loanVO.getIncreaseAmount() : 0);
//        result.getParams().put("qdzMoney", BigDecimal.ZERO);
//		
//        // 查询钱袋子信息
//        ResponseEntity<?> qdzRes = this.qdzTransferFacade.findMyQdzInfo(regUser);
//        if (qdzRes.validSuc() && qdzRes.getParams().size() > 0) {
//            BigDecimal money = (BigDecimal)qdzRes.getParams().get("nowMoney");
//            result.getParams().put("qdzMoney", money);
//            capitalAmount = capitalAmount.add(money);
////            逾期收益+加息收益
//            interestAmount = interestAmount.add((BigDecimal)qdzRes.getParams().get("expectedEarning"))
//                    .add((BigDecimal)qdzRes.getParams().get("raiseInterestRatesEarnings"));
//        }
//        result.getParams().put("capitalAmount", capitalAmount);
//        result.getParams().put("interestAmount", interestAmount);
//        total = total.add(capitalAmount).add(interestAmount);
//        total = total.add(loanVO != null ? loanVO.getIncreaseAmount() : BigDecimal.ZERO);
//        
//		finAccount.setTotalAmount(total);
//		result.getParams().put("finAccount", finAccount);
        initMoney(result, regUserId);
		// 获得安全级别
		result.getParams().put("scurityLevel", this.securityLevel(result));
		// 加载购房宝&物业宝意向金额
		RosDepositInfo rosDepositInfo = new RosDepositInfo();
		rosDepositInfo.setRegUserId(regUser.getId());
		result.getParams().put("depositInfoCount", this.rosDepositInfoService.findRosDepositInfoCount(rosDepositInfo));
		return result;
	}

    @Override
    public ResponseEntity<?> findUserTotalAccount(Integer regUserId) {
        // 加载用户信息
        ResponseEntity<?> result = new ResponseEntity<>();
//        // 加载账户信息
//        FinAccount finAccount = this.finAccountService.findByRegUserId(regUserId);
//        BeanPropertiesUtil.setNullExtend(finAccount, "nowMoney", "freezeMoney", "useableMoney");
//        BigDecimal total = BigDecimal.ZERO.add(finAccount.getNowMoney());
//        // 查询待收金额
//        LoanVO loanVO = this.bidReceiptPlanService.findAgencyFundByUserId(regUserId, null);
//        result.getParams().put("capitalAmount", loanVO != null ? loanVO.getCapitalAmount() : 0);
//        total = total.add(loanVO != null ? loanVO.getCapitalAmount() : BigDecimal.ZERO);
//        total = total.add(loanVO != null ? loanVO.getInterestAmount() : BigDecimal.ZERO);
//        total = total.add(loanVO != null ? loanVO.getIncreaseAmount() : BigDecimal.ZERO);
//        // 查询钱袋子待收本金
//        QdzAccount qdzAccount = this.qdzAccountService.findQdzAccountByRegUserId(regUserId);
//        total = total.add(qdzAccount == null ? BigDecimal.ZERO : qdzAccount.getMoney());
//        finAccount.setTotalAmount(total);
//        result.getParams().put("finAccount", finAccount);
        initMoney(result, regUserId);
        return result;
    }
    
    /**
    *  初始化资金信息
    *  @param result
    *  @param regUserId
    *  @return void
    *  @date                    ：2019/1/15
    *  @author                  ：zc.ding@foxmail.com
    */
    private void initMoney(ResponseEntity<?> result, Integer regUserId){
        RegUser regUser = new RegUser();
        regUser.setId(regUserId);

        // 加载账户信息
        FinAccount finAccount = this.finAccountService.findByRegUserId(regUser.getId());
        BeanPropertiesUtil.setNullExtend(finAccount, "nowMoney", "freezeMoney", "useableMoney");
        // 总资产
        BigDecimal total = BigDecimal.ZERO.add(finAccount.getNowMoney());
        
        BidInfoVO bidInfoVO = new BidInfoVO();
        bidInfoVO.setProductTypeLimitIds(Collections.singletonList(InvestConstants.BID_PRODUCT_CURRENT));
        List<Integer> bidIds = this.bidInfoService.findBidInfoVOByCondition(bidInfoVO).stream().map(BidInfoVO::getId).collect(Collectors.toList());
        // 查询待收金额
        LoanVO loanVO = this.bidReceiptPlanService.findAgencyFundByUserId(regUserId, bidIds);
        BigDecimal capitalAmount = loanVO != null ? loanVO.getCapitalAmount() : BigDecimal.ZERO;
        BigDecimal interestAmount = loanVO != null ? loanVO.getInterestAmount() : BigDecimal.ZERO;
        result.getParams().put("increaseAmount", loanVO != null ? loanVO.getIncreaseAmount() : 0);
        result.getParams().put("qdzMoney", BigDecimal.ZERO);

        // 查询钱袋子信息
        ResponseEntity<?> qdzRes = this.qdzTransferFacade.findMyQdzInfo(regUser);
        if (qdzRes.validSuc() && qdzRes.getParams().size() > 0) {
            BigDecimal money = (BigDecimal)qdzRes.getParams().get("nowMoney");
            result.getParams().put("qdzMoney", money);
            capitalAmount = capitalAmount.add(money);
//            逾期收益+加息收益
            interestAmount = interestAmount.add((BigDecimal)qdzRes.getParams().get("expectedEarning"))
                    .add((BigDecimal)qdzRes.getParams().get("raiseInterestRatesEarnings"));
        }
        result.getParams().put("capitalAmount", capitalAmount);
        result.getParams().put("interestAmount", interestAmount);
        total = total.add(capitalAmount).add(interestAmount);
        total = total.add(loanVO != null ? loanVO.getIncreaseAmount() : BigDecimal.ZERO);

        finAccount.setTotalAmount(total);
        result.getParams().put("finAccount", finAccount);
    }

	/**
	 * @Description : 计算安全等级
	 * @Method_Name : securityLevel
	 * @param result
	 * @return : int
	 * @Creation Date : 2018年1月2日 下午3:19:25
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private int securityLevel(ResponseEntity<?> result) {
		int score = 70;
		// 完善紧急联系人
		RegUserInfo regUserInfo = (RegUserInfo) result.getParams().get("regUserInfo");
		if (regUserInfo == null) {
			regUserInfo = this.regUserInfoService
					.findRegUserInfoByRegUserId(((RegUser) result.getParams().get("regUser")).getId());
		}
		if (StringUtils.isNotBlank(regUserInfo.getEmergencyContact())
				&& StringUtils.isNotBlank(regUserInfo.getEmergencyTel())) {
			score += 5;
		}
		// 完善联系地址
		if (StringUtils.isNotBlank(regUserInfo.getContactAddress())) {
			score += 5;
		}
		// 完善邮箱验证
		if (regUserInfo.getEmailState() == 1) {
			score += 10;
		}
		// 完善个人信息
		RegUser regUser = (RegUser) result.getParams().get("regUser");
		if (regUser.getIdentify() != UserConstants.USER_IDENTIFY_NO) {
			score += 10;
		}
		return score;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateUserForRealName(Integer regUserId, String idCard, String realName, String email) {
		logger.info("updateUserForRealName, 实名认证, 用户: {}, 身份证号: {}, 真实姓名: {}, 邮箱: {}", regUserId, idCard, realName, email);
		try {
			RegUserDetail regUserDetail = BaseUtil.getRegUserDetail(regUserId,
					() -> this.regUserDetailService.findRegUserDetailByRegUserId(regUserId));
			regUserDetail.setIdCard(idCard);
			regUserDetail.setRealName(realName);
			regUserDetail.setRealNameTime(new Date());
			RegUserInfo regUserInfo = BaseUtil.getRegUserInfo(regUserId,
					() -> this.regUserInfoService.findRegUserInfoByRegUserId(regUserId));
			regUserInfo.setEmail(email);
			
			RegUserDetail cdt = new RegUserDetail();
			cdt.setIdCard(idCard);
			if(Optional.ofNullable(this.regUserDetailService.findRegUserDetailList(cdt)).orElse(new ArrayList<>()).size() > 0) {
				return ResponseEntity.error("此身份证已使用");
			}
			
			Integer state = UserConstants.USER_REAL_NAME_ERROR;
			//获取当前优先级最高的实名认证渠道
			FinChannelGrant finChannelGrant = null;
			List<FinChannelGrant> channelGrantList = finChannelGrantService.findFinChannelGrantList(SystemTypeEnums.HKJF,
					PlatformSourceEnums.PC, PayStyleEnum.REALNAME);
			if (channelGrantList != null && channelGrantList.size() > 0) {
				finChannelGrant = channelGrantList.get(0);
			}
			if (finChannelGrant == null ) {
				logger.error("实名认证, 用户标识: {}, 当前系统暂没有开启实名通道！", regUserId);
				return new ResponseEntity<>(ERROR, "当前系统暂无充值渠道！");
			}
			
			FinPayConfig finPayConfig = this.finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.getPayChannelEnumByCode(finChannelGrant.getChannelNameCode()).toString(), PayStyleEnum.REALNAME.getType());
			if(finPayConfig == null){
				logger.error("finPayConfig 实名认证 配置信息为空");
				return ResponseEntity.error("实名认证 配置信息为空");
			}
			//因为实名验证需要收费，因此设置开关
			String validateRealName = PropertiesHolder.getProperty(UserConstants.OPEN_VALIDATE_REAL_NAME);
			if(StringUtils.isBlank(validateRealName) || "1".equals(validateRealName)) {
				ResponseEntity<?> realNameResult = null;
				if(PayChannelEnum.fromChannelName(finPayConfig.getThirdNameCode()) == PayChannelEnum.BaoFuProtocol.getChannelNameValue()){
					realNameResult = BaofuProtocolPayFactory.callRealNameReq(regUserId, idCard, realName, finPayConfig); //宝付实名
				}else {// 有盾实名
					realNameResult = LianlianPayFactory.callRealNameReq(regUserId, idCard, realName, finPayConfig);
				}
				if(realNameResult.getResStatus() == ERROR){
					logger.error("第三方实名接口返回ERROR");
					return new ResponseEntity(ERROR,realNameResult.getParams().get("desc"));
				}
				
				state = Integer.parseInt(realNameResult.getParams().get("state").toString());
				insertOperateRecord(regUserId,idCard,realName,state);
				
			}else{ //测试环境实名关闭
				state = UserConstants.USER_REAL_NAME_YES;
			}
			ResponseEntity<?> result = this.regUserDetailService.updateRegUserDetailForRealName(regUserDetail, regUserInfo, state);
			if (result.validSuc()) {
				FinAccount finAccount = this.finAccountService.findByRegUserId(regUserId);
				FinAccount newFinAccount = new FinAccount();
				newFinAccount.setId(finAccount.getId());
				newFinAccount.setUserName(realName);
				this.finAccountService.update(newFinAccount);
				RegUserFriends regUserFriends = new RegUserFriends();
				regUserFriends.setRegUserId(regUserId);
				List<RegUserFriends> list = this.regUserFriendsService.findRecommendFriendsList(regUserFriends);
				if(list != null && !list.isEmpty()) {
					list.forEach(o -> {
						o.setRealName(realName);
						this.regUserFriendsService.updateRegUserFriends(o);
					});
				}
				
				//实名赠送2积分  鸿坤金服没有积分模块
//				RealnameAuthPointDTO realnameAuthPointDTO = new RealnameAuthPointDTO();
//				realnameAuthPointDTO.setPoint(2);
//				realnameAuthPointDTO.setUserId(regUserId);
//				jmsService.sendMsg(PointConstants.MQ_QUEUE_POINT_RECORD, DestinationType.QUEUE, realnameAuthPointDTO,
//						JmsMessageType.OBJECT);
//				SmsSendUtil.sendSmsMsgToQueue(new SmsWebMsg(regUserId, SmsMsgTemplate.MSG_REALNAME.getTitle(),
//						SmsMsgTemplate.MSG_REALNAME.getMsg(), SmsConstants.SMS_TYPE_NOTICE, new Object[] { 2 }));
			}
			return result;
		} catch (Exception e) {
			logger.error("实名认证, 用户: {}, 身份证号: {}, 真实姓名: {}, 邮箱: {}\n", regUserId, idCard, realName, email, e);
			throw new BusinessException("实名失败");
		}
	}
	/**
	 * 实名信息记录放入monitor_operate_record表
	 * @param regUserId
	 * @param idCard
	 * @param realName
	 * @param state
	 * @Creation Date : 2018-05-30 16:05:17
	 * @Author : binliang@hongkun.com liangbin
	 */
	public void insertOperateRecord(Integer regUserId, String idCard, String realName,Integer state){
		MonitorOperateRecord record = new  MonitorOperateRecord();
		String desc = "宝付实名认证,身份证："+idCard+",姓名:"+realName+",宝付实名返回："+state;
		record.setRegUserId(regUserId);
		record.setOperateDesc(desc);
		record.setOperateType(MonitorConstants.USER_REAL_NAME_BAOFU);
		record.setOperateState(state);
		jmsService.sendMsg(MonitorConstants.MQ_QUEUE_MONITOR_OPERATE_RECORD,DestinationType.QUEUE,record,JmsMessageType.OBJECT);
	}
	@Override
	public ResponseEntity<?> findUserMyAccountForApp(final Integer regUserId) {
		// 加载用户信息
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
		result.getParams().put("identify", regUser.getIdentify());
        try {
            //加载增值服务：钱袋子规则、用户等级&成长值、红包数量、体验金、卡券
            Future<Map<String, Object>> vasFuture = SingletonThreadPool.callCachedThreadPool(() -> this.vasService.getVasInfo(regUserId, "10111"));
            //站内信条数
            Future<Integer> webMsgFuture = SingletonThreadPool.callCachedThreadPool(() -> this.smsWebMsgService.findUnreadWebMsg(regUserId));
            //加载账户信息 & 累计收益
            Future<Map<String, Object>> accountFuture = SingletonThreadPool.callCachedThreadPool(() -> this.finAccountService.findFinAccontInfo(regUserId));
            BidInfoVO bidInfoVO = new BidInfoVO();
            bidInfoVO.setProductTypeLimitIds(Collections.singletonList(InvestConstants.BID_PRODUCT_CURRENT));
            List<Integer> bidIds = this.bidInfoService.findBidInfoVOByCondition(bidInfoVO).stream().map(BidInfoVO::getId).collect(Collectors.toList());
            //查询待收金额
            Future<LoanVO> loanFuture = SingletonThreadPool.callCachedThreadPool(() -> this.bidReceiptPlanService.findAgencyFundByUserId(regUserId, bidIds));
            //查询钱袋子信息
            Future<Map<String, Object>> qdzFuture = SingletonThreadPool.callCachedThreadPool(() -> this.qdzTransferService.findQdzReceiptInfo(regUserId));
            //查询债权转让标识
            Future<Boolean> bondTransferFuture = SingletonThreadPool.callCachedThreadPool(() -> this.rosInfoService.validateRoster(regUserId, RosterType.BOND_TRANSFER, RosterFlag.WHITE));
            //查询积分数量
            Future<PointAccount> pointFuture = SingletonThreadPool.callCachedThreadPool(() -> this.pointAccountService.findPointAccountByRegUserId(regUserId));
            
            BigDecimal total = BigDecimal.ZERO;
            //存储增值服务
            result.getParams().putAll(vasFuture.get());
            //存储站内信信息
            result.getParams().put("webMsgCount", webMsgFuture.get());
            result.getParams().putAll(accountFuture.get());
            total = total.add((BigDecimal)result.getParams().get("nowMoney"));
            LoanVO loanVO = loanFuture.get();
            BigDecimal waitCapitalAmount = loanVO != null ? loanVO.getCapitalAmount() : BigDecimal.ZERO;
            BigDecimal waitInterestAmount = loanVO != null ? loanVO.getInterestAmount() : BigDecimal.ZERO;
            BigDecimal waitIncreaseAmount = loanVO != null ? loanVO.getIncreaseAmount() : BigDecimal.ZERO;
            
            Map<String, Object> qdzMap = qdzFuture.get();
            waitCapitalAmount = waitCapitalAmount.add((BigDecimal)qdzMap.get("waitCapitalSum"));
            waitInterestAmount = waitInterestAmount.add((BigDecimal) qdzMap.get("waitInterestSum"));
            result.getParams().put("waitIncreaseAmount", waitIncreaseAmount);
            result.getParams().put("waitCapitalAmount", waitCapitalAmount);
            result.getParams().put("waitInterestAmount", waitInterestAmount);
            // 总资产  = nowMoney + 钱袋子本金 + 钱袋子利息 + 待收利息 + 加息收益
            total = total.add(waitCapitalAmount).add(waitInterestAmount).add(waitIncreaseAmount);
            result.getParams().put("total", total);
            // 总收入 = 利息 + 加息 + 钱袋子利息
            result.getParams().put("incomeTotal", ((BigDecimal) result.getParams().remove("interest"))
                    .add((BigDecimal) result.getParams().remove("increase")).add((BigDecimal) qdzMap.get("finishInterest")));
            result.getParams().put("bondTransferFlag", bondTransferFuture.get() ? 1 : 0);
            result.getParams().put("point", Optional.ofNullable(pointFuture.get()).orElse(new PointAccount()).getPoint());
            result.getParams().put("serviceHotline", UserConstants.SERVICE_HOTLINE);
        } catch (Exception e) {
            throw new GeneralException("数据加载异常", e);
        }
		return result;
	}
	
    @Override
	public ResponseEntity<?> insertRegUserForRegist(RegUser regUser, RegUserDetail regUserDetail) {
		logger.info("insertRegUserForRegist, 注册, 手机号: {}, 用户信息: {}, 用户信息(detail): {}", regUser.getLogin(), regUser.toString(), regUserDetail.toString());
		ResponseEntity<?> result = new ResponseEntity<>(Constants.ERROR);
        //解析邀请码准确性
        if(StringUtils.isNoneBlank(regUserDetail.getCommendNo())){
            result = this.parseCommendNo(regUserDetail, null, null);
            if(!result.validSuc()) {
                return result;
            }
        }
        result = ApplicationContextUtils.getBean(UserFacadeImpl.class).doInsertRegUserForRegist(regUser, regUserDetail, 1);
        if(result.validSuc()){
            try{
                //创建积分账户
                this.jmsService.sendMsg(PointConstants.MQ_QUEUE_INIT_POINT_ACCOUNT, DestinationType.QUEUE, regUser.getId(), JmsMessageType.OBJECT);
                // 发送奖励(eg:积分、成长值),不需要进行成长值初始化
//                this.sendGropuValue(regUser.getId());
                //邀请好友注册赠送成长值
                if (StringUtils.isNoneBlank(regUserDetail.getCommendNo())){
                    RegUserDetail inviteUser = regUserDetailService.findRegUserDetailByInviteNo(regUserDetail.getCommendNo());
                    if (inviteUser != null){
                        VasVipGrowRecordMqVO recordMqVO = new VasVipGrowRecordMqVO();
                        recordMqVO.setUserId(inviteUser.getRegUserId());
                        recordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_INVITE_USER_REGIST);
                        VipGrowRecordUtil.sendVipGrowRecordToQueue(recordMqVO);
                        //发送站内信给推荐人
                        SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg(inviteUser.getRegUserId(), SmsMsgTemplate.MSG_RECOMMEND.getTitle(), SmsMsgTemplate.MSG_RECOMMEND.getMsg(), SmsConstants.SMS_TYPE_NOTICE,
                                new String[] {regUser.getLogin().toString().substring(0, 3) +"****"+regUser.getLogin().toString().substring(regUser.getLogin().toString().length()-3)}));
                    }
                }
                // 处理好友关系
                this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, DestinationType.QUEUE, regUser.getId(), JmsMessageType.OBJECT);
                // 异步短信通知
                SmsSendUtil.sendTelMsgToQueue(new SmsTelMsg(regUser.getLogin(), SmsMsgTemplate.MSG_REGIST.getMsg(), SmsConstants.SMS_TYPE_NOTICE));
            }catch (Exception e){
                logger.error("用户: {}, 发送异步消息失败", regUser.getId(), e);
            }
        }
		return result;
	}

	/**
	 *  @Description    : 维护用户关系
	 *  @Method_Name    : doRegist
	 *  @param regUser
	 *  @param regUserDetail
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月9日 下午6:50:49 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> doRegist(RegUser regUser, RegUserDetail regUserDetail) {
		logger.info("用户注册：{}, 用户详情:{}", regUser, regUserDetail);
		// 获得推广来源
		String extenSource = regUserDetail.getExtenSource();
		RegUserDetail extenSourceUser = null;
		if (StringUtils.isNotBlank(extenSource)) {
			extenSourceUser = this.regUserDetailService.findRegUserDetailByGroupCode(extenSource);
			if (extenSourceUser == null) {
				return new ResponseEntity<>(ERROR, "物业推荐用户不存在");
			}
		}
		RegUserDetail commendRegUserDetial = null;
		// 推荐码
		ResponseEntity<?> result = this.parseCommendNo(regUserDetail, commendRegUserDetial, extenSourceUser);
		if(!result.validSuc()) {
			return result;
		}
		// 判断推广来源是不是商户&宅急送&...
		if (StringUtils.isNotBlank(extenSource)) {
			if (extenSource.startsWith("SN")) {// 商户推广
				PointMerchantInfo pointMerchantInfo = new PointMerchantInfo();
				pointMerchantInfo.setMerchantName(extenSource);
				if (this.pointMerchantInfoService.findPointMerchantInfoCount(pointMerchantInfo) < 1) {
					regUserDetail.setExtenSource(null);
				}
			} else if (extenSource.startsWith("zj")) {// 宅急送
				RegUserDetail rud = new RegUserDetail();
				rud.setCommendNo(extenSource);
				List<RegUserDetail> list = this.regUserDetailService.findRegUserDetailList(rud);
				if (list.isEmpty()) {
					regUserDetail.setCommendNo(null);
					regUserDetail.setExtenSource(null);
				} else {
					regUserDetail.setCommendNo(list.get(0).getInviteNo());
				}
			}
		}
		return this.regUserService.insertRegUserForRegist(regUser, regUserDetail, commendRegUserDetial);
	}
	
	/**
	 *  @Description    : 解析推荐码
	 *  @Method_Name    : parseCommendNo
	 *  @param regUserDetail
	 *  @param commendRegUserDetial
	 *  @param extenSourceUser
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月9日 下午7:20:25 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	private ResponseEntity<?> parseCommendNo(RegUserDetail regUserDetail, RegUserDetail commendRegUserDetial, RegUserDetail extenSourceUser) {
		String commendNo = regUserDetail.getCommendNo();
		if (StringUtils.isNotBlank(commendNo)) {
			commendRegUserDetial = this.regUserDetailService.findRegUserDetailByInviteNo(commendNo);
			if (commendRegUserDetial == null) {
				return new ResponseEntity<>(ERROR, "无效的邀请码");
			}
			if(regUserDetail.getRegUserId() != null && commendRegUserDetial.getRegUserId() != null && commendRegUserDetial.getRegUserId().equals(regUserDetail.getRegUserId())) {
				return new ResponseEntity<>(ERROR, "推荐人不能是自己");
			}
			/**
			 * 推荐人&推广码都存在，判断推荐人是否是推广码对应的公司的员工
			 * 推荐人存在&推广码不存在，通过员工表找推荐人对应的企业账户，如果存在企业账户并且企业账户含有机构吗，
			 * 那么将此机构码作为注册用户的推广来源
			 */
			RosStaffInfo rosStaffInfo = this.rosStaffInfoService.findRosStaffInfo(commendRegUserDetial.getRegUserId(),
					RosterConstants.ROSTER_STAFF_TYPE_PROPERTY, 1,null);
			if (extenSourceUser != null) {
				if (rosStaffInfo == null) {
					return new ResponseEntity<>(ERROR, "推荐人与推广来源不匹配");
				}
			} else {
				if (rosStaffInfo != null) {
					RegUserDetail enterprise = this.regUserDetailService
							.findRegUserDetailByRegUserId(rosStaffInfo.getEnterpriseRegUserId());
					if (enterprise != null && StringUtils.isNotBlank(enterprise.getGroupCode())) {
						regUserDetail.setExtenSource(enterprise.getGroupCode());
					}
				}
			}
		}
		return ResponseEntity.SUCCESS;
	}
	
	/**
	 * @Description : 注册赠送成长值
	 * @Method_Name : sendGropuValue
	 * @param userId
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月4日 下午5:00:50
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	private void sendGropuValue(int userId) {
		VasVipGrowRecordMqVO record = new VasVipGrowRecordMqVO();
		record.setUserId(userId);
		record.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_POINT_GIVE);
		record.setGrowValue(0);
		VipGrowRecordUtil.sendVipGrowRecordToQueue(record);
	}
	

	@Override
	public ResponseEntity<?> updateRegUserForSpread(Integer regUserId, String nickName, String commendNo) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
		RegUserDetail regUserDetail = new RegUserDetail();
		RegUser regUser = new RegUser();
		if(StringUtils.isNotBlank(nickName)) {
			regUser.setId(regUserId);
			regUser.setNickName(nickName);
		}
		if(StringUtils.isNotBlank(commendNo)) {
			regUserDetail.setCommendNo(commendNo);
			result = this.parseCommendNo(regUserDetail, null, null);
			if(!result.validSuc()) {
				return result;
			}
		}
		//发送消息处理好友关系
		this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, DestinationType.QUEUE, regUserId, JmsMessageType.OBJECT);
		return result;
	}
	@Override
	public  ResponseEntity<?> appLoginLogList(Pager pager, AppLoginLog appLoginLog){
		boolean isSearchLogin = false;
		if(appLoginLog.getLogin()!=null){
			RegUser user =this.regUserService.findRegUserByLogin(appLoginLog.getLogin());
			if(user!=null){
				appLoginLog.setRegUserId(user.getId());
				isSearchLogin = true;
			}
		}
		Pager result = this.appLoginLogService.findAppLoginLogList(appLoginLog, pager);
		if (result == null || CommonUtils.isEmpty(result.getData())) {
			return BaseUtil.emptyResult();
		}
		List<AppLoginLog> list = (List<AppLoginLog>) result.getData();
		for (AppLoginLog loginLog : list) {
			if(isSearchLogin){
				loginLog.setLogin(appLoginLog.getLogin());
			}else{
				RegUser regUser = regUserService.findRegUserById(loginLog.getRegUserId());
				if (regUser != null) {
					loginLog.setLogin(regUser.getLogin());
				}
			}
		}
		return new ResponseEntity<>(SUCCESS, result);
	}

    @Override
    public ResponseEntity<?> findRegAuditList(Integer regUserId) {
	    ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS);
        RegUser regUser = BaseUtil.getRegUser(regUserId, () -> this.regUserService.findRegUserById(regUserId));
        result.getParams().put("regUser", regUser);
        RegAuditRule regAuditRule = new RegAuditRule();
        regAuditRule.setState(1);
        List<RegAuditRule> list = this.regAuditRuleService.findRegAuditRuleList(regAuditRule);
        String urlPrefix = PropertiesHolder.getProperty("oss.url");
        if(CommonUtils.isNotEmpty(list)){
            for(RegAuditRule rar : list) {
                String path = UserConstants.REG_AUDIT_PREFIX + regUser.getLogin() + "/" + rar.getType();
                List<String> picList = OSSLoader.getInstance().listPathFileName(OSSBuckets.HKJF, path);
                if(CommonUtils.isNotEmpty(picList)){
                    picList.forEach(o -> o = urlPrefix + urlPrefix + o);
                    rar.setPicList(picList);
                }
            }
        }
        result.getParams().put("list", list);
        return result;
    }

    @Override
    public ResponseEntity<?> insertRegUserForCompany(RegUser regUser, RegUserDetail regUserDetail, RegCompanyInfo regCompanyInfo ,FinBankCard finBankCard) {
        ResponseEntity<?> result = this.regUserService.insertRegUserForCompany(regUser, regUserDetail, regCompanyInfo);
        if(result.getResStatus() == Constants.ERROR){
            return result;
        }
        //添加资金账户
        FinAccount finAccount = new FinAccount();
        RegUser rUser = (RegUser) result.getParams().get("regUser");
        finAccount.setRegUserId(rUser.getId());
        this.finAccountService.insert(finAccount);
        //维护企业的银行卡信息
        finBankCard.setRegUserId(rUser.getId());
        this.finBankCardService.insert(finBankCard);
        //创建积分账户
        PointAccount pointAccount = new PointAccount();
        pointAccount.setRegUserId(regUser.getId());
        this.pointAccountService.insertPointAccount(pointAccount);
        if(result.validSuc()){
            // 处理好友关系
            this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, DestinationType.QUEUE, regUser.getId(),
                    JmsMessageType.OBJECT);
            //初始化企业账户成长值
			sendGropuValue(regUser.getId());
        }
        return result;
    }

    @Override
    public void updateRegUserVip() {
	    //处理VIP用户
        RegUser regUser = new RegUser();
        regUser.setVipFlag(UserConstants.USER_IS_VIP);
	    List<RegUser> regUserList = this.regUserService.findRegUserList(regUser);
	    List<RegUserVipRecord> regUserVipRecords = new ArrayList<>();
	    List<RegUser> demoteRegUserList = new ArrayList<>();
	    if(CommonUtils.isNotEmpty(regUserList)){
	        List<Integer> ids = regUserList.stream().map(RegUser::getId).collect(Collectors.toList());
            List<LoanVO> loans = this.bidReceiptPlanService.findAgencyFundByUserId(ids);
            List<FinAccount> finAccounts = this.finAccountService.findFinAccountByRegUserIds(ids);
            List<RegUserVipRecord> regUserVipRecordList = this.regUserVipRecordService.findRegUserVipRecordListByRegUserIds(ids);
            regUserList.forEach(reg -> {
                BigDecimal total = BigDecimal.ZERO;
                BigDecimal nowMoney = BigDecimal.ZERO;
                BigDecimal waitMoney = BigDecimal.ZERO;
                if(CommonUtils.isNotEmpty(loans)){
                    waitMoney = Optional.ofNullable(loans.stream().filter(o -> reg.getId().equals(o.getUserId())).findAny().orElse(new LoanVO()).getAmount()).orElse(BigDecimal.ZERO);
                }
                if(CommonUtils.isNotEmpty(finAccounts)){
                    nowMoney = Optional.ofNullable(finAccounts.stream().filter(o -> reg.getId().equals(o.getRegUserId())).findAny().orElse(new FinAccount()).getNowMoney()).orElse(BigDecimal.ZERO);
                }
                total = total.add(waitMoney).add(nowMoney);
                RegUserVipRecord currRegUserVipRecord = new RegUserVipRecord();
                if(CommonUtils.isNotEmpty(regUserVipRecordList)){
                    currRegUserVipRecord = regUserVipRecordList.stream().filter(o -> reg.getId().equals(o.getRegUserId())).findAny().orElse(new RegUserVipRecord());
                }
                RegUserVipRecord regUserVipRecord = new RegUserVipRecord();
                regUserVipRecord.setRegUserId(reg.getId());
                regUserVipRecord.setNowMoney(total);
                //需要插入降级记录的用户
                if(CompareUtil.gt(UserConstants.USER_VIP_LOW_STANDARD, total)){
                    //查找最新的降级记录
                    Integer times = Optional.ofNullable(currRegUserVipRecord.getTimes()).orElse(0) + 1;
                    //触及降级底线 3次，就开始降级
                    if(times == UserConstants.USER_VIP_LOW_TIMES){
                        regUserVipRecord.setTimes(0);
                        RegUser demoteRegUser = new RegUser();
                        demoteRegUser.setId(reg.getId());
                        demoteRegUser.setVipFlag(UserConstants.USER_IS_NOT_VIP);
                        demoteRegUserList.add(demoteRegUser);
                    }else{
                        regUserVipRecord.setTimes(times);
                    }
                }else{
                    regUserVipRecord.setTimes(0);
                }
                regUserVipRecords.add(regUserVipRecord);
            });
        }
        //插入降级记录
        if(CommonUtils.isNotEmpty(regUserVipRecords)){
	        this.regUserVipRecordService.insertRegUserVipRecordBatch(regUserVipRecords);
        }
        //VIP用户降级
        if(CommonUtils.isNotEmpty(demoteRegUserList)){
	        try{
	            this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_VIP, DestinationType.QUEUE, demoteRegUserList, JmsMessageType.OBJECT);
	        }catch(Exception e){
                logger.error("VIP用户降级, 降级需要降级的用户: {}", JsonUtils.toJson(demoteRegUserList));
	        }
        }
    }

    @Override
    public ResponseEntity<?> insertRecommendUserForRegist(Integer regUserId, RegUser regUser) {
	    logger.info("需要插入注册人的鸿坤金服推荐人信息, 用户: {}, 推荐人信息: {}", regUserId, regUser.toString());
        //先插入推荐人信息
        ResponseEntity<?> result = ApplicationContextUtils.getBean(UserFacadeImpl.class).doInsertRegUserForRegist(regUser, new RegUserDetail(), 2);
        if(result.validSuc()){
            try{
                //创建积分账户
                this.jmsService.sendMsg(PointConstants.MQ_QUEUE_INIT_POINT_ACCOUNT, DestinationType.QUEUE, regUser.getId(), JmsMessageType.OBJECT);
                // 处理好友关系
                Map<String, Object> map = new HashMap<>(4);
                map.put("registRegUserId", regUserId);
                map.put("recommendRegUserId", regUser.getId());
                map.put("investNo", result.getParams().get("investNo"));
                this.jmsService.sendMsg(UserConstants.MQ_QUEUE_USER_FRIEND_REGIST, DestinationType.QUEUE, map, JmsMessageType.MAP);
            }catch (Exception e){
                logger.error("用户: {}, 发送异步消息失败", regUser.getId(), e);
            }
        }
        return result;
    }

    /**
    *  将事务与发送mq分开，保证事务提交后才消费消息，防止消息消费时事务还没有提交导致数据异常
    *  @Method_Name             ：doInsertRegUserForRegist
    *  @param regUser
    *  @param regUserDetail
    *  @param userType          : 用户类型： 1：普通注册， 2：鸿坤金服推荐用户注册
    *  @return com.yirun.framework.core.model.ResponseEntity<?>
    *  @Creation Date           ：2018/7/13
    *  @Author                  ：zc.ding@foxmail.com
    */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> doInsertRegUserForRegist(RegUser regUser, RegUserDetail regUserDetail, Integer userType){
	    ResponseEntity<?> result;
        try {
            JedisClusterLock lock = new JedisClusterLock();
            try {
                if (lock.lock("reg_" + regUser.getLogin())) {
                    RegUser existUser = this.regUserService.findRegUserByLogin(regUser.getLogin());
                    //正常用户注册, 如果存在用户，提示 此手机号已注册
                    //注册用户的鸿坤金服推荐人注册
                    //如果存在要插入的推荐人用户信息，返回已存在用户的邀请码，用于更新注册人的邀请码关系
                    //如果不存在，初始化推荐人信息后，返回新用户的邀请码，用于更新注册人邀请码关系
                    if(existUser != null){
                        return userType == 1 ? ResponseEntity.error("此手机号已注册") : ResponseEntity.SUCCESS.addParam("investNo", BaseUtil.getRegUserDetail(existUser.getId(), () -> this.regUserDetailService.findRegUserDetailByRegUserId(existUser.getId())).getInviteNo());
                    }
                    result = this.doRegist(regUser, regUserDetail);
                    if(result.validSuc()){
                        result.getParams().put("investNo", ((RegUserDetail) result.getParams().get("regUserDetail")).getInviteNo());
                        // 添加资金账户
                        FinAccount finAccount = new FinAccount();
                        regUser = (RegUser) result.getParams().get("regUser");
                        finAccount.setRegUserId(regUser.getId());
                        this.finAccountService.insert(finAccount);
                        result.getParams().put("finAccount", finAccount);
                    }
                }else {
                    logger.error("insertRegUserForRegist, 注册获得锁失败, 手机号: {}, 用户信息: {}, 用户信息(detail): {}", regUser.getLogin(), regUser.toString(), regUserDetail.toString());
                    return ResponseEntity.error("注册人数过多,请稍后重试");
                }
            } finally {
                lock.freeLock("reg_" + regUser.getLogin());
            }
        } catch (Exception e) {
            logger.error("insertRegUserForRegist, 注册, 手机号:{}, 用户信息: {}, 用户信息(detail): {}", regUser.getLogin(), regUser.toString(), regUserDetail.toString(), e);
            throw new GeneralException("注册失败");
        }
	    return result;
    }

	@Override
	public Map<String, Object> customizeServes(Integer regUserId, String serves){
		logger.info("用户id:" + regUserId + ",用户自定义服务：" + serves);

		try {
			List<String> list = Arrays.asList(serves.split(","));

			//判断是否有用户自定义服务信息
			AppUserServe cdt = new AppUserServe();
			cdt.setRegUserId(regUserId);
			List<AppUserServe> appUserServeList =  appUserServeService.findAppUserServeList(cdt);

			//服务逐条增加或更新
			list.stream().forEach(x -> {
				AppUserServe appUserServe = new AppUserServe();
				appUserServe.setRegUserId(regUserId);
				appUserServe.setSort(list.indexOf(x)+1);
				appUserServe.setServeId(Integer.valueOf(x));
				appUserServe.setModifiedTime(new Date());

				if(appUserServeList != null && !appUserServeList.isEmpty()){	//更新自定义服务
					appUserServe.setId(appUserServeList.get(list.indexOf(x)).getId());
					logger.info("更新自定义服务：id="+appUserServe.getId()+","+"用户id="+appUserServe.getRegUserId()+"服务值="+appUserServe.getServeId()+"顺序="+appUserServe.getSort());
					appUserServeService.updateAppUserServe(appUserServe);
				}else {			//新增自定义服务
					appUserServe.setCreateTime(new Date());
					logger.info("新增自定义服务：用户id="+appUserServe.getRegUserId()+"服务值="+appUserServe.getServeId()+"顺序="+appUserServe.getSort());
					appUserServeService.insertAppUserServe(appUserServe);
				}
			});
            return AppResultUtil.successOfMsg("自定义服务成功");
		} catch (Exception e) {
			logger.error("customizeServes, 自定义服务, 用户: {}", regUserId, e);
            return AppResultUtil.errorOfMsg("自定义服务失败");
		}

	}

	@Override
	public List<AppMoreServe> getUserServes(Integer regUserId){
		logger.info("用户id:" + regUserId);

		//判断是否有用户自定义服务信息
		AppUserServe appUserServeCondition = new AppUserServe();
		appUserServeCondition.setRegUserId(regUserId);
		List<AppUserServe> appUserServeList = appUserServeService.findAppUserServeList(appUserServeCondition);

		List<AppMoreServe> appMoreServeList = new ArrayList<>();

		if(appUserServeList == null || appUserServeList.isEmpty()){     //用户没有自定义过服务，默认服务
            AppMoreServe appMoreServeCondition = new AppMoreServe();
            appMoreServeCondition.setType(UserConstants.APP_MENU_TYPE_USER);
            appMoreServeCondition.setIsShow(UserConstants.APP_MENU_SHOW);
			List<AppMoreServe> defaultServeList = appMoreServeService.findAppMoreServeList(appMoreServeCondition);
			if(defaultServeList!=null && defaultServeList.size() == 3){	//后台默认三个
				return defaultServeList;
			}
		}else{      //用户有自定义服务，直接查出来
			List<Integer> appMoreServeIds = new ArrayList<>();
			appUserServeList.forEach(x -> {
				AppMoreServe appMoreServe = appMoreServeService.findAppMoreServeById(x.getServeId());
				if(Integer.valueOf("0").equals(appMoreServe.getIsShow())){
					appMoreServeList.add(appMoreServe);
					appMoreServeIds.add(x.getServeId());
				}
				logger.info("用户自定义过服务，用户自定义服务："+appMoreServe.getServiceName());
			});
			if(appMoreServeList != null && appMoreServeList.size() == 3){	//查到用户自定义三个服务
				return appMoreServeList;
			}
			if(appMoreServeList != null && appMoreServeList.size() < 3){	//用户自定义三个服务被禁用了N个，从别的菜单选N个加进来
				List<AppMoreServe> appMoreServeList1 =  appMoreServeService.findOtherServe(appMoreServeIds);
				for(int i = 0; i < 3 - appMoreServeList.size(); i++){
					appMoreServeList.add(appMoreServeList1.get(i));
				}
				return appMoreServeList;
			}
		}
		return appMoreServeList;
	}
	
}
