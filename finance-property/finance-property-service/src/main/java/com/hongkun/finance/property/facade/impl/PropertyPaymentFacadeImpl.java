package com.hongkun.finance.property.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.invest.service.BidInvestService;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.service.PointAccountService;
import com.hongkun.finance.point.service.PointCommonService;
import com.hongkun.finance.point.service.PointRecordService;
import com.hongkun.finance.point.service.PointRuleService;
import com.hongkun.finance.property.constants.PropertyContants;
import com.hongkun.finance.property.facade.PropertyPaymentFacade;
import com.hongkun.finance.property.model.ProAddress;
import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.property.service.ProAddressService;
import com.hongkun.finance.property.service.ProPayRecordService;
import com.hongkun.finance.property.vo.PropertySalesVo;
import com.hongkun.finance.roster.model.RosStaffInfo;
import com.hongkun.finance.roster.service.RosStaffInfoService;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserSimpleVo;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.redis.JedisClusterLock;
import com.yirun.framework.redis.JedisClusterUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.Serializers;
import org.mengyun.tcctransaction.api.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hongkun.finance.payment.constant.TradeStateConstants.ALREADY_PAYMENT;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.hongkun.finance.point.constants.PointConstants.POINT_TYPE_TENEMENT;
import static com.hongkun.finance.point.constants.PointConstants.POINT_TYPE_TENEMENT_BACK;
import static com.hongkun.finance.property.constants.PropertyContants.*;
import static com.hongkun.finance.roster.constants.RosterConstants.ROSTER_STAFF_STATE_VALID;
import static com.hongkun.finance.roster.constants.RosterConstants.ROSTER_STAFF_TYPE_PROPERTY;
import static com.hongkun.finance.user.constants.UserConstants.USER_TYPE_TENEMENT;
import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class PropertyPaymentFacadeImpl implements PropertyPaymentFacade {

	private final Logger logger = LoggerFactory.getLogger(PropertyPaymentFacadeImpl.class);
	@Reference
	private ProPayRecordService proPayRecordService;
	@Reference
	private PointAccountService pointAccountService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private RosStaffInfoService rosStaffInfoService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private BidInvestService bidInvestService;
	@Reference
	private PointCommonService pointCommonService;
	@Reference
	private PointRuleService pointRuleService;
	@Reference
	private PointRecordService pointRecordService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinBankCardBindingService finBankCardBindingService;
	@Reference
	private FinBankReferService finBankReferService;
	@Reference
	private ProAddressService proAddressService;
    @Reference
	private FinTradeFlowService finTradeFlowService;
    @Reference
    private FinPaymentRecordService finPaymentRecordService;
	@Override
	public ResponseEntity<?> propertyPayment(RegUser regUser, ProPayRecord proPayRecord,Integer usePoints) {
		logger.info("tcc propertyPayment entrance, reference point#transferPoints,payment#updateAccountInsertTradeAndTransfer. 物业缴费, regUserId: {}, proPayRecord: {}, usePoints",
				regUser.getId(), proPayRecord.toString(),usePoints);
		//校验地址信息
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = PropertyContants.PROPERTY_PAYMENT_LOCK_PREFIX + regUser.getId();
		String secLockKey = "SEC:" + lockKey;
		try {
			if(proPayRecord==null||!CommonUtils.gtZero(proPayRecord.getAddressId())){
				return new ResponseEntity<>(ERROR, "请选择缴费地址");
			}
			ProAddress address = proAddressService.findProAddressById(proPayRecord.getAddressId());
			if(address==null){
				return new ResponseEntity<>(ERROR, "缴费地址不存在");
			}
			if(lock.lock(lockKey)){
			    if(JedisClusterUtils.exists(secLockKey)){
			        return new ResponseEntity<>(ERROR, "您的订单正在处理中，请勿重复操作");
                }
				JedisClusterUtils.set(secLockKey,"1");
				//校验账户信息
				PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUser.getId());
				FinAccount account = finAccountService.findByRegUserId(regUser.getId());
				if (account == null) {
					return new ResponseEntity<>(ERROR, "缴费账户不存在");
				}
				if (pointAccount == null) {
					return new ResponseEntity<>(ERROR, "积分账户不存在");
				}
				logger.info("propertyPayment, 物业缴费, 账户信息account: {},积分账户: {}",account.toString(),pointAccount.toString());
				//剩余需要缴费金额
				BigDecimal proUseableMoney = BigDecimal.ZERO;
				if(usePoints==PropertyContants.PAY_PROPERTY_USEPOINTS_YES){
					//如果使用积分支付
					BigDecimal pointMoney = pointCommonService.pointToMoney(pointAccount.getPoint());
					if (CompareUtil.gt(proPayRecord.getMoney(), pointMoney)) {
						// 部分使用积分抵扣，部分使用可用余额
						proPayRecord.setPoint(pointAccount.getPoint());
						proPayRecord.setPointMoney(pointMoney);
						// 剩余缴费金额
						proUseableMoney = proPayRecord.getMoney().subtract(pointMoney);
						//判断可用余额够不够
						if(CompareUtil.gt(account.getUseableMoney(),proUseableMoney)){
							//剩余部分全部使用余额支付
							proPayRecord.setUseableMoney(proUseableMoney);
							proUseableMoney = BigDecimal.ZERO;
						}else{
							//把余额全部支付，计算剩余金额返回给客户进行充值
							proPayRecord.setUseableMoney(account.getUseableMoney());
							proUseableMoney = proUseableMoney.subtract(account.getUseableMoney());
						}
					} else {
						// 全部使用积分抵扣 剩余缴费金额为0
						int point = pointRuleService.proMoneyToPoint(proPayRecord.getMoney());
						proPayRecord.setPoint(point);
						proPayRecord.setPointMoney(proPayRecord.getMoney());
					}

				}else{
					proPayRecord.setPoint(0);
					proPayRecord.setPointMoney(BigDecimal.ZERO);
					proUseableMoney = proPayRecord.getMoney();
					//判断可用余额够不够
					if(CompareUtil.gt(account.getUseableMoney(),proUseableMoney)){
						//剩余部分全部使用余额支付
						proPayRecord.setUseableMoney(proUseableMoney);
						proUseableMoney = BigDecimal.ZERO;
					}else{
						//把余额全部支付，计算剩余金额返回给客户进行充值
						proPayRecord.setUseableMoney(account.getUseableMoney());
						proUseableMoney = proUseableMoney.subtract(account.getUseableMoney());
					}
				}
//                proPayRecord.setRechargeMoney(proUseableMoney); //充值金额注掉，改为先充值，后进行缴费
				proPayRecord.setState(PROPERTY_RECORD_STATE_ING);
				//改成充值金额>0, 则需要提示用户金额不足，先去充值 再来缴费。
				if(CompareUtil.gtZero(proUseableMoney)){
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("proUseableMoney", proUseableMoney);
					result.put("state", PROPERTY_RECORD_STATE_NO_USEABLE);
					return new ResponseEntity<>(SUCCESS, result);
				}
				// 插入缴费记录表
                BaseUtil.getTccProxyBean(ProPayRecordService.class,getClass(),"propertyPayment").insertProPayRecord(proPayRecord);
			    Map<String,Object> params = new HashMap<String,Object>();
                params.put("proPayRecordId", proPayRecord.getId());
                //处理金额&积分&流水
                if (proPayRecord.getPoint() > 0&&CompareUtil.gtZero(proPayRecord.getPointMoney())) {
                    ResponseEntity<?> pointResult =
                            pointRecordService.transferPoints(proPayRecord.getPoint() * -1, proPayRecord.getPointMoney(), proPayRecord.getRegUserId(),
                                    POINT_TYPE_TENEMENT, proPayRecord.getId(),PROPERTY_RECORD_POINT_DEDUCTION);
                    if(BaseUtil.error(pointResult)){
                        return new ResponseEntity<>(ERROR, "物业缴费抵扣积分失败");
                    }
                    params.put("pointRecordId",  String.valueOf(pointResult.getResMsg()));
                }
                params.put("state",PROPERTY_RECORD_STATE_FREE);
                BaseUtil.getTccProxyBean(ProPayRecordService.class,getClass(),"propertyPayment").updateState( params);
                // 3、账户变更 业主金额减少&物业账户冻结金额增加&相关流水
                dealAccountAndTradeFlowForProperty(regUser.getId(), address.getProId(), proPayRecord.getUseableMoney(),
                        proPayRecord.getMoney(), proPayRecord.getPointMoney(),String.valueOf(proPayRecord.getId()));
                
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("state", PROPERTY_RECORD_STATE_SUCCESS);
				result.put("proUseableMoney", proUseableMoney);
				result.put("recordId", proPayRecord.getId());
				return new ResponseEntity<>(SUCCESS,result);
			}else{
				logger.error("propertyPayment, 物业缴费未获取到锁, 用户: {}, 缴费信息: {}, 是否使用积分: {}", regUser.getId(), proPayRecord.toString(),usePoints);
				return new ResponseEntity<>(ERROR,"您的操作已受理，请勿重复操作");
			}
		} catch (Exception e) {
			logger.error("tcc propertyPayment error, reference point#transferPoints,payment#updateAccountInsertTradeAndTransfer. 物业缴费异常, regUserId: {}, proPayRecord: {}, usePoints:{}, 异常信息: \n",
					regUser.getId(), proPayRecord.toString(),usePoints, e);
			throw new GeneralException("物业缴费失败");
		}finally {
			lock.freeLock(lockKey);
			JedisClusterUtils.delete(secLockKey);
		}

	}

	/**
	 *
	 * @Description : 物业缴费--处理账户和流水
	 * @Method_Name : dealAccountAndTradeFlowForProperty
	 * @param regUserId  用户id
	 * @param propertyId  物业id
	 * @param outMoney   用户需要支出的金额
	 * @param inMoney    物业账户收入的金额
	 * @param pFlowId  业务id  -- 缴费记录id
	 * @return : void
	 * @Creation Date : 2018年2月1日 下午4:42:59
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private void dealAccountAndTradeFlowForProperty(Integer regUserId, Integer propertyId, BigDecimal outMoney,
			BigDecimal inMoney,BigDecimal pointMoney, String pFlowId ) {
        //生成流水
        FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(regUserId, String.valueOf(pFlowId), inMoney, TRADE_TYPE_PROPERTY_FEE,
                PlatformSourceEnums.PC);
		List<FinFundtransfer> transfersList = new ArrayList<FinFundtransfer>();
		// 插入资金流水
		if (CompareUtil.gtZero(outMoney)) {
			FinFundtransfer fundtransferOut = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), regUserId,
					propertyId, outMoney, TRANSFER_SUB_CODE_PAY);
			transfersList.add(fundtransferOut);
		}
		FinFundtransfer fundtransferIn = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(), propertyId, regUserId,
				inMoney, TradeTransferConstants.getFundTransferSubCodeByType(
	                    FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.PROPERTY_MONEY));
		fundtransferIn.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
		transfersList.add(fundtransferIn);
		// 平台账户出的钱
		if (CompareUtil.gtZero(pointMoney)) {
			FinFundtransfer fundtransferPoint = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
					UserConstants.PLATFORM_ACCOUNT_ID, regUserId, pointMoney,
					TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.POINT_MONEY));
			transfersList.add(fundtransferPoint);
		}
		BaseUtil.getTccProxyBean(FinConsumptionService.class,getClass(),"dealAccountAndTradeFlowForProperty").updateAccountInsertTradeAndTransfer(finTradeFlow, transfersList);
	}
	
	@Override
	@Compensable
	public ResponseEntity<?> auditProPayment(int proPayRecordId, String opinion, int state, RegUser regUser) {
        logger.info("tcc auditProPayment entrance, reference payment#updateAccountInsertTradeAndTransfer,point#transferPoints. 物业缴费审核, 缴费记录id: {}, 审核理由: {}, 审核状态: {}, 审核人: {}", proPayRecordId,opinion,state,regUser.getId());
		JedisClusterLock lock = new JedisClusterLock();
		String lockKey = PROPERTY_AUDIT_LOCK_KEY;
		try {
			ProPayRecord proPayRecord = proPayRecordService.findProPayRecordById(proPayRecordId);
			if (proPayRecord == null) {
				return new ResponseEntity<>(ERROR, "未查询缴费记录信息");
             }
			if (proPayRecord.getState() != PROPERTY_RECORD_STATE_FREE) {
				return new ResponseEntity<>(ERROR, "缴费记录不是待审核状态");
			}
			lockKey = lockKey + proPayRecord.getId();
			boolean lockResult = lock.lock(lockKey, Constants.LOCK_EXPIRES, Constants.LOCK_WAITTIME);
            logger.info("auditProPayment, 物业缴费审核, 缴费记录信息: {}, 审核理由: {}, 审核状态: {}, 审核人: {}, 获取锁状态: {}", proPayRecord.toString(),opinion,state,regUser.getId(),lockResult);
			if (lockResult) {
				//ProAddress address = proAddressService.findProAddressById(proPayRecord.getAddressId());
                // 缴费记录状态修改
                Map<String,Object> params = new HashMap<String, Object>();
                params.put("proPayRecordId",proPayRecordId);
                params.put("state",state);
                params.put("reviewReason",opinion);
                BaseUtil.getTccProxyBean(ProPayRecordService.class,getClass(),"auditProPayment").updateState(params);
				if (state == PROPERTY_RECORD_STATE_REFUSE) {
					// 审核拒绝
					// 用户钱划转，物业冻结钱扣除，加入退款流水，资金划转流水，
					ResponseEntity<?> result = dealCancelForProperty(proPayRecord.getRegUserId(),
							proPayRecord.getProId(), proPayRecord.getMoney(), proPayRecordId,
							proPayRecord.getPointMoney());
					if (BaseUtil.error(result)) {
                        throw new GeneralException(String.valueOf(result.getResMsg()));
					}
					// 积分退还
					if (proPayRecord.getPoint() > 0) {
					    BaseUtil.getTccProxyBean(PointRecordService.class,getClass(),"auditProPayment")
                                .transferPoints(proPayRecord.getPoint(), proPayRecord.getPointMoney(),
                                        proPayRecord.getRegUserId(), POINT_TYPE_TENEMENT_BACK, proPayRecord.getId(),PROPERTY_RECORD_POINT_FALLBACK);
					}
				} else if (state == PROPERTY_RECORD_STATE_SUCCESS) {
					// 审核成功
					// 商户冻结金额解冻，加入解冻流水
					FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(proPayRecord.getProId(),
							proPayRecord.getId(), proPayRecord.getMoney(), TRADE_TYPE_PROPERTY_FEE_AUDIT_SUCCESS,
							PlatformSourceEnums.PC);
                    BaseUtil.getTccProxyBean(FinConsumptionService.class,getClass(),"auditProPayment")
                            .cashPay(finTradeFlow, TRANSFER_SUB_CODE_THAW);
				} else {
					return new ResponseEntity<>(ERROR, "审核异常");
				}
			} else {
				return new ResponseEntity<>(ERROR, "此笔审核正在操作中，请勿重复操作");
			}
			return new ResponseEntity<>(SUCCESS, "审核完成");
		} catch (Exception e) {
            logger.info("tcc error auditProPayment, 物业缴费审核, 缴费记录id: {}, 审核理由: {}, 审核状态: {}, 审核人: {}", proPayRecordId,opinion,state,regUser.getId());
			throw new GeneralException("物业缴费审核失败");
		} finally {
			lock.freeLock(lockKey);
		}
	}

	/**
	 *
	 *  @Description    : 物业缴费退款
	 *  @Method_Name    : dealCancelForProperty
	 *  @param regUserId
	 *  @param propertyId
	 *  @param money
	 *  @param propertyRecordId
	 *  @param pointMoney
	 *  @return
	 *  @return         : ResponseEntity<?>
	 *  @Creation Date  : 2018年3月27日 上午10:23:03
	 *  @Author         : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private ResponseEntity<?> dealCancelForProperty(Integer regUserId, Integer propertyId, BigDecimal money,
			Integer propertyRecordId, BigDecimal pointMoney){
        logger.info("tcc dealCancelForProperty entrance, reference payment#updateAccountInsertTradeAndTransfer. 物业缴费退款, regUserId: {}, propertyId: {}, money: {},propertyRecordId: {}, pointMoney: {}",
                regUserId, propertyId,money,propertyRecordId,pointMoney);
        try {
            BigDecimal cancelMoney = money;// 退款金额
            if (CompareUtil.gtZero(pointMoney)) {
                cancelMoney = money.subtract(pointMoney);
            }
            // 处理退款账户
            FinAccount propertyAccount = finAccountService.findByRegUserId(propertyId);
            if (CompareUtil.gt(cancelMoney, propertyAccount.getNowMoney())
                    || CompareUtil.gt(cancelMoney, propertyAccount.getFreezeMoney())) {
                return new ResponseEntity<>(ERROR, "物业账户冻结金额不足，不能退款");
            }
            // 添加退款流水
            FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(regUserId, propertyRecordId, money,
                    TRADE_TYPE_PROPERTY_FEE_AUDIT_REFUSE, PlatformSourceEnums.PC);
            String flowId = finTradeFlow.getFlowId();
            
            FinFundtransfer fundtransfer = FinTFUtil.initFinFundtransfer(flowId,regUserId, propertyId, cancelMoney, TRANSFER_SUB_CODE_INCOME);
            if(CompareUtil.eZero(cancelMoney)){//退款金额为0 设置不展示
            	fundtransfer.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
            }
            FinFundtransfer fundtransferOut = FinTFUtil.initFinFundtransfer(flowId, propertyId, regUserId, money, TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
                    FundtransferSmallTypeStateEnum.FROZEN));
            if(CompareUtil.eZero(money)){//退款金额为0 设置不展示
            	fundtransferOut.setShowFlag(PaymentConstants.SHOW_FRONT_NO);
    		}
            List<FinFundtransfer> transfersList  = new ArrayList<FinFundtransfer>();
            transfersList.add(fundtransfer);
            transfersList.add(fundtransferOut);
            
            if (CompareUtil.gtZero(pointMoney)) {
                // 平台账户退款
                FinFundtransfer plateFundtransferIn =FinTFUtil.initFinFundtransfer(flowId,UserConstants.PLATFORM_ACCOUNT_ID, propertyId, pointMoney,
                		TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
    							FundtransferSmallTypeStateEnum.POINT_MONEY));
                transfersList.add(plateFundtransferIn);
            }
            finConsumptionService.updateAccountInsertTradeAndTransfer(finTradeFlow, transfersList);
        } catch (Exception e) {
            logger.info("tcc error dealCancelForProperty, 物业缴费退款, regUserId: {}, propertyId: {}, money: {},propertyRecordId: {}, pointMoney: {}",
                    regUserId, propertyId,money,propertyRecordId,pointMoney);
            throw new GeneralException("物业缴费退款失败");
        }        return new ResponseEntity<>(SUCCESS);
	}

	@Override
	public ResponseEntity<?> findPropertyRecordList(ProPayRecord proPayRecord, Pager pager) {
		// 通过条件查询缴费记录
		Pager result = proPayRecordService.findProPayRecordList(proPayRecord, pager);
		if (result == null || CommonUtils.isEmpty(result.getData())) {
			return new ResponseEntity<>(ERROR, "未查询到相关记录");
		}
		List<ProPayRecord> list = (List<ProPayRecord>) result.getData();
		for (ProPayRecord pp : list) {
			UserVO regUser = regUserService.findUserWithDetailById(pp.getRegUserId());
			if (regUser != null) {
				pp.setTel(String.valueOf(regUser.getLogin()));
				pp.setUserName(regUser.getRealName());
			}
		}
		result.setData(list);
		return new ResponseEntity<>(SUCCESS, result);
	}

	@Override
	public ResponseEntity<?> findPropertyStaffList(RosStaffInfo rosStaffInfo, Pager pager, RegUser regUser) {
		RosStaffInfo contidion = rosStaffInfo;
		List<PropertySalesVo> resultList = new ArrayList<PropertySalesVo>();
		if (USER_TYPE_TENEMENT != regUser.getType()) {
			// 查询物业账户
			RosStaffInfo info = rosStaffInfoService.findRosStaffInfo(regUser.getId(), ROSTER_STAFF_TYPE_PROPERTY,
					ROSTER_STAFF_STATE_VALID,null);
			if (info == null) {
				return new ResponseEntity<>(ERROR, "当前用户不是物业员工");
			}

			contidion.setEnterpriseRegUserId(info.getEnterpriseRegUserId());
		} else {
			contidion.setEnterpriseRegUserId(regUser.getId());
		}
		Pager resultPager = rosStaffInfoService.findRosStaffInfoList(rosStaffInfo, pager);
		if (resultPager != null && CommonUtils.isNotEmpty(resultPager.getData())) {
			List<RosStaffInfo> staffInfos = (List<RosStaffInfo>) resultPager.getData();
			for (RosStaffInfo info : staffInfos) {
				PropertySalesVo vo = new PropertySalesVo();
				vo.setUserId(info.getRegUserId());
				vo.setTel(String.valueOf(info.getLogin()));
				vo.setRealName(info.getRealName());
				// 推荐累计投资
				BigDecimal investAmount = new BigDecimal(0);
				// 折标后金额
				BigDecimal niggerAmount = new BigDecimal(0);
				// 查询推荐人
				RegUserDetail regUserDetail = regUserDetailService.findRegUserDetailByRegUserId(info.getRegUserId());
				RegUserDetail usercontidion = new RegUserDetail();
				usercontidion.setCommendNo(regUserDetail.getInviteNo());
				List<RegUserDetail> inviteList = regUserDetailService.findRegUserDetailList(usercontidion);
				if (CommonUtils.isNotEmpty(inviteList)) {
					for (RegUserDetail user : inviteList) {
						BigDecimal sumInvestAmount = bidInvestService.findSumInvestAmountByUserId(user.getRegUserId());
						BigDecimal sumNiggerAmount = bidInvestService.findSumNiggerAmountByUserId(user.getRegUserId());
						investAmount = investAmount.add(sumInvestAmount);
						niggerAmount = niggerAmount.add(sumNiggerAmount);
					}
				}
				vo.setInvestAmount(investAmount);
				vo.setNiggerAmount(niggerAmount);
				resultList.add(vo);
			}
		}
		resultPager.setData(resultList);

		return new ResponseEntity<>(SUCCESS, resultPager);
	}


	@Override
	public ResponseEntity<?> findPropertyRecordVoList(ProPayRecord proPayRecordVo, Pager pager) {
		logger.info("findPropertyRecordVoList, 条件查询物业缴费待审核列表, 条件: {}, pager: {}",
				proPayRecordVo.toString(),pager.toString());
		// 通过条件查询缴费记录
		ProPayRecord cdt = initContidionForPropertyRecord(proPayRecordVo);
		if (cdt == null){
			return new ResponseEntity<>(SUCCESS, new Pager());
		}
		cdt.setSortColumns("id desc ");
		Pager result = proPayRecordService.findProPayRecordList(cdt, pager);
		if (result == null || CommonUtils.isEmpty(result.getData())) {
			return BaseUtil.emptyResult();
		}
		List<ProPayRecord> list = (List<ProPayRecord>) result.getData();
		List<ProPayRecord> resultList = new ArrayList<ProPayRecord>();
		List<Integer> regUserIds = new ArrayList<Integer>();
		if(CommonUtils.isNotEmpty(list)){
            list.forEach(proVo->{
                regUserIds.add(proVo.getRegUserId());
            });
        }
        if(CommonUtils.isNotEmpty(regUserIds)){
            List<UserVO> userVos = regUserService.findUserWithDetailByInfo(regUserIds);
            list.forEach(pp->{
                for(UserVO uvo:userVos){
                    if(pp.getRegUserId().equals(uvo.getUserId())){
                        pp.setTel(String.valueOf(uvo.getLogin()));
                        pp.setUserName(uvo.getRealName());
                        break;
                    }
                }
                resultList.add(pp);
            });
        }
		result.setData(resultList);
		return new ResponseEntity<>(SUCCESS, result);
	}

	/**
	 * @Description : 封装查询条件
	 * @Method_Name : initContidionForPropertyRecord
	 * @param proPayRecordVo
	 * @return
	 * @return : ProPayRecordVo
	 * @Creation Date : 2018年1月12日 下午2:32:15
	 * @Author : xuhuiliu@honghun.com.cn 刘旭辉
	 */
	private ProPayRecord initContidionForPropertyRecord(ProPayRecord proPayRecord) {
		if (proPayRecord != null) {
			if (StringUtils.isNotBlank(proPayRecord.getUserName())
					|| StringUtils.isNotBlank(proPayRecord.getTel())) {
				UserVO contidion = new UserVO();
				if (StringUtils.isNotBlank(proPayRecord.getUserName())) {
					contidion.setRealName(proPayRecord.getUserName());
				}
				if (StringUtils.isNotBlank(proPayRecord.getTel())) {
					contidion.setLogin(Long.valueOf(proPayRecord.getTel()));
				}
				List<Integer> userIds = regUserService.findUserIdsByUserVO(contidion);
				if (CommonUtils.isNotEmpty(userIds)) {
					proPayRecord.setRegUserId(userIds.get(0));
				}else{
					return null;
				}
			}
		}
		return proPayRecord;
	}

	@Override
	public ResponseEntity<?> findPropertyRecordListByPropertyId(ProPayRecord proPayRecord, Pager pager) {
		ProPayRecord cdt = initContidionForPropertyRecord(proPayRecord);
		if (cdt == null) {
			return BaseUtil.emptyResult();
		}
		Pager result = proPayRecordService.findProPayRecordList(cdt, pager);
		if (result == null || CommonUtils.isEmpty(result.getData())) {
			return BaseUtil.emptyResult();
		}
		List<ProPayRecord> list = (List<ProPayRecord>) result.getData();
		List<Integer> regUserIds = new ArrayList<Integer>();
		list.forEach(record -> {
			regUserIds.add(record.getRegUserId());
		});
		List<UserSimpleVo> users = regUserService.findUserSimpleVoList(regUserIds);
		list.forEach(record -> {
			for (UserSimpleVo vo : users) {
				if (vo.getId().equals(record.getRegUserId())) {
					record.setTel(String.valueOf(vo.getLogin()));
					record.setUserName(vo.getRealName());
					break;
				}
			}
		});
		result.setData(list);
		return new ResponseEntity<>(Constants.SUCCESS, result);
	}

	@Override
	public List<ProPayRecord> findPropertyRecordForExport(ProPayRecord proPayRecordVo, int start, int pageSize) {
		ProPayRecord cdt = initContidionForPropertyRecord(proPayRecordVo);
		if (cdt == null) {
			return null;
		}
		Pager pager = new Pager();
		pager.setPageSize(pageSize);
		pager.setStartNum(start);
		Pager result = proPayRecordService.findProPayRecordList(cdt, pager);
		if (result == null || CommonUtils.isEmpty(result.getData())) {
			return null;
		}
		List<ProPayRecord> list = (List<ProPayRecord>) result.getData();
		List<Integer> regUserIds = new ArrayList<Integer>();
		list.forEach(record -> {
			String stateStr = "进行中";
			if (record.getState() == PROPERTY_RECORD_STATE_FAIL || record.getState() == PROPERTY_RECORD_STATE_REFUSE) {
				stateStr = "失败";
			} else if (record.getState() == PROPERTY_RECORD_STATE_SUCCESS) {
				stateStr = "成功";
			}
			record.setStateDesc(stateStr);
			record.setCreatedTimeStr(DateUtils.format(record.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
			String payTypeStr = record.getPayType() == 1 ? "物业费" : "车位费";
			record.setPayTypeDesc(payTypeStr);
			regUserIds.add(record.getRegUserId());
		});
		List<UserSimpleVo> users = regUserService.findUserSimpleVoList(regUserIds);
		list.forEach(record -> {
			for (UserSimpleVo vo : users) {
				if (vo.getId() == record.getRegUserId()) {
					record.setTel(String.valueOf(vo.getLogin()));
					record.setUserName(vo.getRealName());
					break;
				}
			}
		});
		return list;
	}


	@Override
    public ProPayRecord findPropertyRecordById(Integer propayRecordId) {
		ProPayRecord proPayRecordVo = proPayRecordService.findProPayRecordById(propayRecordId);
		return proPayRecordVo;
	}
	@Override
	public ResponseEntity<?> getPointMoney(Integer regUserId,BigDecimal money) {
		Map<String,Object> map=new HashMap<>();
		PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUserId);
		BigDecimal accountPointMoney = pointCommonService.pointToMoney(pointAccount.getPoint());
		Integer usePoint;	//使用的积分
		BigDecimal pointMoney;//积分抵扣的金额
		Integer residuePoint;	//剩余积分
		if (CompareUtil.gt(money, accountPointMoney)) {
			//积分不足，返回可抵扣金额，剩余积分为0
			usePoint=pointAccount.getPoint();
			pointMoney=accountPointMoney;
			residuePoint=0;
		} else {
			//积分充足，结算时抵扣金额，并计算使用多少积分
			usePoint=pointRuleService.proMoneyToPoint(money);
			pointMoney=money;
			residuePoint=pointAccount.getPoint()-usePoint;
		}
		map.put("usePoint", usePoint);
		map.put("pointMoney", pointMoney);
		map.put("residuePoint", residuePoint);
		return new ResponseEntity<>(Constants.SUCCESS, map);
	}

    @Override
    @Compensable
    public ResponseEntity<?> payPropertyCallBack(RegUser regUser, Integer proPayRecordId,String paymentFlowId) {
        logger.info("payPropertyCallBack, 物业缴费充值回调, regUserId: {},proPayRecordId: {}",regUser.getId(),proPayRecordId);
		JedisClusterLock lock = new JedisClusterLock();
        String lockKey = PropertyContants.PROPERTY_PAYMENT_LOCK_PREFIX + regUser.getId();
        try {
            //1、校验缴费记录是否存在
            ProPayRecord proPayRecord =  proPayRecordService.findProPayRecordById(proPayRecordId);
            if(proPayRecord == null){
                return new ResponseEntity<>(Constants.ERROR,"缴费失败：缴费记录不存在");
            }
            if(lock.lock(lockKey)){
                //2、查询异步回调是否成功
                if(checkIncomeResult(paymentFlowId)){
                    //3、如果成功，执行缴费成功逻辑，更改记录状态为已冻结，扣除充值金额
                    //校验积分 校验账户信息是否正确
                    PointAccount pointAccount = pointAccountService.findPointAccountByRegUserId(regUser.getId());
                    FinAccount  account = finAccountService.findByRegUserId(regUser.getId());
                    if(CommonUtils.gtZero(proPayRecord.getPoint()) && proPayRecord.getPoint() > pointAccount.getPoint()){
                        throw new BusinessException("物业缴费失败,您的账户积分不足");
                    }
                    if(CompareUtil.gt(proPayRecord.getMoney().subtract(proPayRecord.getPointMoney()),account.getUseableMoney())){
                        throw new BusinessException("物业缴费失败,您的账户可用余额不足");
                    }
                    Map<String,Object> params = new HashMap<String,Object>();
                    params.put("proPayRecordId",proPayRecordId);
                    params.put("state",PROPERTY_RECORD_STATE_FREE);
                    params.put("oldState",PROPERTY_RECORD_STATE_ING);
                    params.put("paymentFlowId",paymentFlowId);
                    Integer updateResult = BaseUtil.getTccProxyBean(ProPayRecordService.class, getClass(), "payPropertyCallBack").updateState(params);
                    ProAddress address = proAddressService.findProAddressById(proPayRecord.getAddressId());
                    if (address==null){
                        throw  new BusinessException("缴费地址不存在");
                    }
                    if(CommonUtils.gtZero(updateResult)){
                        //积分划转
                        if (proPayRecord.getPoint() > 0&&CompareUtil.gtZero(proPayRecord.getPointMoney())) {
                            ResponseEntity<?> pointResult = BaseUtil.getTccProxyBean(PointRecordService.class, getClass(), "payPropertyCallBack")
                                    .transferPoints(proPayRecord.getPoint() * -1, proPayRecord.getPointMoney(), proPayRecord.getRegUserId(),
                                            POINT_TYPE_TENEMENT, proPayRecord.getId(),PROPERTY_RECORD_POINT_DEDUCTION);
                            if(BaseUtil.error(pointResult)){
                                return new ResponseEntity<>(ERROR, "物业缴费抵扣积分失败");
                            }
                            Map<String,Object> upParams = new HashMap<String,Object>();
                            upParams.put("proPayRecordId",proPayRecordId);
                            upParams.put("pointRecordId",String.valueOf(pointResult.getResMsg()));
                            BaseUtil.getTccProxyBean(ProPayRecordService.class,getClass(),"payPropertyCallBack").updateState(upParams);
                        }
                        //处理金额&流水  【物业冻结金额，用户支余额，平台支出积分余额】
                        dealAccountAndTradeFlowForProperty(regUser.getId(),address.getProId(),proPayRecord.getMoney().subtract(proPayRecord.getPointMoney()),
                                proPayRecord.getMoney(),proPayRecord.getPointMoney(),String.valueOf(proPayRecordId));
                    }else{
                        return new ResponseEntity<>(Constants.ERROR,"您的缴费订单正在处理，请勿重复提交");
                    }
                }else{
                    //如果充值失败,更信缴费记录状态
                    Map<String,Object> params = new HashMap<String,Object>();
                    params.put("proPayRecordId",proPayRecordId);
                    params.put("state",PROPERTY_RECORD_STATE_FAIL);
                    params.put("oldState",PROPERTY_RECORD_STATE_ING);
                    Integer updateResult = BaseUtil.getTccProxyBean(ProPayRecordService.class, getClass(), "payPropertyCallBack").updateState(params);
                    return new ResponseEntity<>(Constants.ERROR,"缴费失败");
                }
                return new ResponseEntity<>(Constants.SUCCESS,"缴费成功");
            }else{
                return new ResponseEntity<>(Constants.ERROR,"您的缴费订单正在处理，请勿重复提交");
            }
        } catch (Exception e) {
            logger.error("payPropertyCallBack, 物业缴费充值回调用, regUserId: {},proPayRecordId: {}, 异常 \n",regUser.getId(),proPayRecordId,e);
            throw new GeneralException("物业缴费失败");
        } finally {
            lock.freeLock(lockKey);
        }
    }

    /**
    *  @Description    ：物业缴费校验充值异步回调
    *  @Method_Name    ：checkIncomeResult
    *  @param paymentFlowId  支付id
    *  @return boolean
    *  @Creation Date  ：2018/4/20
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    private boolean  checkIncomeResult(String paymentFlowId){
	    logger.info("checkIncomeResult, 物业缴费校验充值异步回调, paymentFlowId: {}", paymentFlowId);
	    boolean payResult = false;
        try {
            for (int i = 1; i <= 10; i++) {
                FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(paymentFlowId);
                if(finPaymentRecord==null){
                    break;
                }
                if (finPaymentRecord.getState() == ALREADY_PAYMENT) {
                    payResult = true;
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            logger.error("checkIncomeResult, 物业缴费校验充值异步回调异常, paymentFlowId: {}, 异常信息: \n",paymentFlowId,e);
            throw  new GeneralException("物业缴费失败");
        }
        return payResult;
    }


}
