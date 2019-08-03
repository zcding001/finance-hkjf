package com.hongkun.finance.payment.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.FinChannelGrant;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.PayCheckVo;
import com.hongkun.finance.payment.model.vo.PaymentRecordVo;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.FinPayCheckUtil;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.roster.constants.RosterFlag;
import com.hongkun.finance.roster.constants.RosterType;
import com.hongkun.finance.roster.service.RosInfoService;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegCompanyInfo;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.model.vo.UserVO;
import com.hongkun.finance.user.service.DicDataService;
import com.hongkun.finance.user.service.RegCompanyInfoService;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.user.utils.BaseUtil;
import com.hongkun.finance.vas.constants.VasCouponConstants;
import com.hongkun.finance.vas.model.VasCouponDetail;
import com.hongkun.finance.vas.model.dto.CouponDetailMqDTO;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.BeanPropertiesUtil;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.ValidateResponsEntityUtil;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.hongkun.finance.payment.constant.PaymentConstants.PAYMENT_DIC_BUSINESS;
import static com.hongkun.finance.payment.constant.PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_CHANNEL;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

@Service
public class FinPaymentConsoleFacadeImpl implements FinPaymentConsoleFacade {
	private static final Logger logger = LoggerFactory.getLogger(FinPaymentConsoleFacadeImpl.class);
	private static final ValidateResponsEntityUtil validateResponsEntityUtil = new ValidateResponsEntityUtil(logger);
	@Reference
	private FinTradeFlowService finTradeFlowService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinChannelGrantService finChannelGrantService;
	@Reference
	private DicDataService dicDataService;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private VasCouponDetailService couponDetailService;
	@Autowired
	private JmsService jmsService;
	@Reference
	private RegCompanyInfoService regCompanyInfoService;
	@Reference
	private RosInfoService rosInfoService;
	
	@Override
	public ResponseEntity<?> findPayReconliciation(PayCheckVo payCheckCondition, Pager pager) {
		// 组装用户ID集合
		List<Integer> regUserIdList = new ArrayList<Integer>();
		try {
			// 判断用户是否输入了用户姓名或手机号作为查询条件
			if (payCheckCondition.getLogin() != null || StringUtils.isNotBlank(payCheckCondition.getRealName())) {
				UserVO userVO = new UserVO();
				if (StringUtils.isNotBlank(payCheckCondition.getRealName())) {
					userVO.setRealName(payCheckCondition.getRealName());
				}
				if (payCheckCondition.getLogin() != null) {
					userVO.setLogin(payCheckCondition.getLogin());
				}
				// 查询用户信息
				regUserIdList = regUserService.findUserIdsByUserVO(userVO);
			}
			// 按照条件查询对账流水分页信息
			FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
			finPaymentRecord.setFlowId(payCheckCondition.getFlowId());
			finPaymentRecord.setCreateTimeBegin(payCheckCondition.getCreateTimeBegin());
			finPaymentRecord.setCreateTimeEnd(payCheckCondition.getCreateTimeEnd());
			finPaymentRecord.setTradeType(payCheckCondition.getTradeType() == null ? PayStyleEnum.RECHARGE.getValue()
					: payCheckCondition.getTradeType());
			finPaymentRecord.setReconciliationState(payCheckCondition.getReconciliationState());
			Pager pagerInfo = finPaymentRecordService.findPayCheckByCondition(regUserIdList, finPaymentRecord, pager);
			return new ResponseEntity<>(SUCCESS, buildReconliciationPageVo(pagerInfo));
		} catch (Exception e) {
			logger.error("查询支付对账异常信息: ", e);
			return new ResponseEntity<>(Constants.ERROR);
		}
	}

	/**
	 * @Description : 平台对账查询，组装页面数据
	 * @Method_Name : buildReconliciationPageVo;
	 * @param pagerInfo
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年10月25日 下午4:56:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public Pager buildReconliciationPageVo(Pager pagerInfo) {
		// 如果列表信息为空，直接返回
		if (BaseUtil.resultPageHasNoData(pagerInfo)) {
			return pagerInfo;
		}
		// 组装页面显示的数据
		List<PayCheckVo> payCheckVoList = new ArrayList<PayCheckVo>();
		if (pagerInfo != null && CommonUtils.isNotEmpty(pagerInfo.getData())) {
			pagerInfo.getData().forEach(vo -> {
				PayCheckVo payCheckVo = new PayCheckVo();
				UserVO userVo = this.regUserService.findUserWithDetailById(((FinPaymentRecord) vo).getRegUserId());
				BeanPropertiesUtil.mergeAndReturn(payCheckVo, (FinPaymentRecord) vo, userVo);
				if (userVo != null && UserConstants.USER_TYPE_GENERAL != userVo.getType()) {
					RegCompanyInfo regCompanyInfo = regCompanyInfoService
							.findRegCompanyInfoByRegUserId(((FinPaymentRecord) vo).getRegUserId());
					payCheckVo.setRealName(regCompanyInfo == null ? "企业用户" : regCompanyInfo.getEnterpriseName());
				}
				payCheckVo.setCreateTime(((FinPaymentRecord) vo).getCreateTime());
				payCheckVoList.add(payCheckVo);
			});
		}
		pagerInfo.setData(payCheckVoList);
		return pagerInfo;
	}

	@Override
	public ResponseEntity<?> findPayRecord(PayCheckVo payCheckCondition, Pager pager) {
		Pager pagerInfo = null;// 返回pager对象
		try {
			// 按照条件查询对账流水分页信息
			FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
			finPaymentRecord.setFlowId(payCheckCondition.getFlowId());
			finPaymentRecord.setCreateTimeBegin(payCheckCondition.getCreateTimeBegin());
			finPaymentRecord.setCreateTimeEnd(payCheckCondition.getCreateTimeEnd());
			finPaymentRecord.setTradeType(payCheckCondition.getTradeType());
			finPaymentRecord.setState(payCheckCondition.getState());
			if (!"-999".equals(payCheckCondition.getPayChannel())) {
				finPaymentRecord.setPayChannel(payCheckCondition.getPayChannel());
			}
			pagerInfo = finPaymentRecordService.findPayCheckByCondition(payCheckCondition.getUserIds(),
					finPaymentRecord, pager);
			return new ResponseEntity<>(SUCCESS, buildReconliciationPageVo(pagerInfo));
		} catch (Exception e) {
			logger.error("findPayRecord, 查询支付记录, 查询条件 payCheckVo: {}, 异常信息: ", payCheckCondition.toString(), e);
			return new ResponseEntity<>(Constants.ERROR);
		}
	}

	@Override
	public Pager findPaymentVoList(Pager pager, PaymentVO paymentVO) {
		// 条件查询用户
		UserVO userVO = new UserVO();
		userVO.setRealName(paymentVO.getRealName());
		if(null != paymentVO.getLogin()){
			userVO.setLogin(paymentVO.getLogin());
		}
		List<Integer> userIds = regUserService.findUserIdsByUserVO(userVO);
		paymentVO.setUserIds(userIds);
		Pager result = this.finTradeFlowService.findPaymentVoList(pager, paymentVO);
		if (result != null && CommonUtils.isNotEmpty(result.getData())) {
			result.getData().forEach(vo -> BeanPropertiesUtil.mergeProperties(vo,
					this.regUserService.findUserWithDetailById(((PaymentVO) vo).getUserId())));
		}
		return result;
	}

	@Override
	public PayCheckVo getPayRecord(Integer id) {
		logger.info("方法: getPayRecord, 提现审核查询支付对账是否成功, 提现记录id: {}", id);
		try {
			FinPaymentRecord record = finPaymentRecordService.findFinPaymentRecordById(id);
			if (record != null) {
				PayCheckVo payCheckVo = new PayCheckVo();
				UserVO userVo = this.regUserService.findUserWithDetailById(record.getRegUserId());
				FinAccount account = this.finAccountService.findByRegUserId(userVo.getUserId());
				// 查询银行卡信息
				FinBankCard finBankCard = finBankCardService.findById(record.getBankCardId());
				BeanPropertiesUtil.mergeAndReturn(payCheckVo, record, userVo, finBankCard, account);
				payCheckVo.setCreateTime(record.getCreateTime());
				payCheckVo.setRealName(userVo.getRealName());
				if (userVo != null && UserConstants.USER_TYPE_GENERAL != userVo.getType()) {
					RegCompanyInfo regCompanyInfo = regCompanyInfoService
							.findRegCompanyInfoByRegUserId(record.getRegUserId());
					payCheckVo.setRealName(regCompanyInfo == null ? "企业用户" : regCompanyInfo.getEnterpriseName());
				}
//				payCheckVo.setUseableMoney(account.getUseableMoney());
//				payCheckVo.setFreezeMoney(account.getFreezeMoney());
//				FinFundtransfer cdt = new FinFundtransfer();
//				// 查询收入金额
//				cdt.setSubCode(10);
//				cdt.setRegUserId(record.getRegUserId());
//				BigDecimal inMoney = finFundtransferService.findFintransferSumMoney(cdt);
//				cdt.setSubCode(20);
//				// 查询支出金额
//				BigDecimal outMoney = finFundtransferService.findFintransferSumMoney(cdt);
//				payCheckVo.setIncomeMoney(inMoney);
//				payCheckVo.setOutMoney(outMoney);
//				if (account != null) {
//					outMoney = outMoney.add(account.getUseableMoney()).add(account.getFreezeMoney());
//				}
//				payCheckVo.setReconciliationDesc("对账成功");
//				if (!CompareUtil.eq(inMoney, outMoney)) {
//					payCheckVo.setReconciliationDesc("对账失败");
//				}
				return payCheckVo;
			}
		} catch (Exception e) {
			logger.error("提现审核查询支付对账是否成功, 异常信息: ", e);
		}
		return null;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> loanWithdrawals(List<Integer> ids) {
		logger.info("loanWithdrawals: 财务放款审核提现, ids: {}", JsonUtils.toJson(ids));
		Integer finPaymentId = null;
		try {
			for (Integer paymentId : ids) {
				finPaymentId = paymentId;
				FinPaymentRecord record = this.finPaymentRecordService.findFinPaymentRecordById(paymentId);
				if (record == null) {
					logger.error("财务放款审核提现, 提现ID: {}, 异常信息没有查询到提现信息！", paymentId);
					return new ResponseEntity<>(Constants.ERROR, "没有查询到提现信息");
				}
				logger.info("财务放款审核提现, 提现ID: {}, 用户ID: {}, 提现状态: {}", paymentId, record.getRegUserId(),
						record.getState());
				if (record.getState() != TradeStateConstants.WAIT_PAY_MONEY) {
					return new ResponseEntity<>(Constants.ERROR, "该提现信息状态不对,请核查");
				}
				//判断是否在提现白名单中
				boolean isWhiteUser = rosInfoService.validateRoster(record.getRegUserId(),
						RosterType.getRosterType(RosterType.WITHDRAW_LOAN.getValue()),
						RosterFlag.getRosterFlag(RosterFlag.WHITE.getValue()));
				
				if(isWhiteUser){
					this.withdrawCash(record);//实时提现
				}else { //定时提现
					FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
					finPaymentRecord.setFlowId(record.getFlowId());
					finPaymentRecord.setState(TradeStateConstants.FINANCE_AUDIT_SUCCESS);
					finPaymentRecordService.updateByFlowId(finPaymentRecord);
				}
			}
			return new ResponseEntity<>(Constants.SUCCESS, "放款审核成功");
		} catch (Exception e) {
			logger.error("loanWithdrawals: 财务放款审核提现, finPaymentId: {}, 异常信息:  ", finPaymentId, e);
			throw new BusinessException("财务放款审核提现出现异常, 异常Id: " + finPaymentId);
		}

	}

	/**
	 * @Description :通过提现记录掉用提现放款方法
	 * @Method_Name : withdrawCash;
	 * @param FinPaymentRecord
	 *            提现记录
	 * @param pager
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年5月4日 10:30:19;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public ResponseEntity<?> withdrawCash(FinPaymentRecord record) {
		logger.info("通过提现记录掉用提现放款方法 FinPaymentConsoleFacadeImpl  withdrawCash method,  入参finPaymentRecord: {}",
				JsonUtils.toJson(record));
		ResponseEntity<?> responseEntity = null;
		RegUser regUser = this.regUserService.findRegUserById(record.getRegUserId());
		RegUserDetail regUserDetail = this.regUserDetailService.findRegUserDetailByRegUserId(record.getRegUserId());
		WithDrawCash info = new WithDrawCash();
		info.setRegUserId(record.getRegUserId());
		info.setLoginTel(String.valueOf(regUser.getLogin()));
		info.setPayChannelEnum(PayChannelEnum.getPayChannelEnumByCode(record.getPayChannel()));
		info.setPlatformSourceName(PlatformSourceEnums.typeByValue(record.getTradeSource()).name());
		info.setPayStyleEnum(PayStyleEnum.WITHDRAW);
		info.setFlowId(record.getFlowId());
		
		info.setSysNameCode(SystemTypeEnums.HKJF.getType());
		if (regUser.getType() == UserConstants.USER_TYPE_GENERAL) {// 对私
			info.setIdCard(regUserDetail.getIdCard());
			info.setUserName(regUserDetail.getRealName());
			info.setUserType(PaymentConstants.TRADE_TYPE_USER_PERSONAL);
		} else {// 对公
			RegCompanyInfo regCompanyInfo = regCompanyInfoService.findRegCompanyInfoByRegUserId(info.getRegUserId());
			info.setUserName(regCompanyInfo.getEnterpriseName());
			info.setUserType(PaymentConstants.TRADE_TYPE_USER_BUSINESS);
		}
		
		responseEntity = finConsumptionService.withdrawCash(info);
		if (responseEntity.getResStatus() == Constants.SUCCESS) {
			return new ResponseEntity<>(Constants.SUCCESS, "提现放款成功");
		} else {
			return (ResponseEntity<?>) validateResponsEntityUtil.validate(responseEntity, "提现放款失败");
		}
	}

	@Override
	public ResponseEntity<?> insertPaymentChannel(FinChannelGrant finChannelGrant) {
		logger.info("方法: insertPaymentChannel, 添加支付渠道, 入参: finChannelGrant: {}", finChannelGrant);
		String channelName = "";// 渠道名称
		try {
			// 根据渠道ID，获取渠道名称
			channelName = dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS, PAYMENT_DIC_BUSINESS_SUBJECT_CHANNEL,
					finChannelGrant.getChannelNameCode());
			// 获取系统名称
			String sysName = dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
					PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_SYSCODE,
					Integer.parseInt(finChannelGrant.getSysName()));
			finChannelGrant.setChannelNameCode(finChannelGrant.getChannelNameCode());
			finChannelGrant.setChannelName(channelName);
			finChannelGrant
					.setSysNameCode(SystemTypeEnums.sysTypeByValue(Integer.parseInt(finChannelGrant.getSysName())));
			finChannelGrant.setSysName(sysName);
			FinChannelGrant channelGrantCdt = new FinChannelGrant();
			channelGrantCdt.setSysNameCode(finChannelGrant.getSysNameCode());
			channelGrantCdt.setChannelNameCode(finChannelGrant.getChannelNameCode());
			channelGrantCdt.setSysNameCode(finChannelGrant.getSysNameCode());
			channelGrantCdt.setPayStyle(finChannelGrant.getPayStyle());
			// 判断平台下的渠道对应的支付方式是否已经存在，如果存在不允许添加
			int count = this.finChannelGrantService.findFinChannelGrantCount(channelGrantCdt);
			if (count > 0) {
				return new ResponseEntity<>(Constants.ERROR,
						finChannelGrant.getSysName() + "平台" + channelName + "渠道下的该支付模式已经存在！");
			}
			// 保存支付模式
			this.finChannelGrantService.insertFinChannelGrant(finChannelGrant);
			return new ResponseEntity<>(Constants.SUCCESS, "保存成功！");
		} catch (Exception e) {
			logger.error("平台标识: {}, 渠道标识: {}, 支付方式标识: {}, 添加支付方式失败, 失败原因: ", finChannelGrant.getSysName(), channelName,
					finChannelGrant.getPayStyle(), e);
			return new ResponseEntity<>(Constants.ERROR, "保存失败！");
		}
	}

	@Override
	public ResponseEntity<?> updatePaymentChannel(FinChannelGrant finChannelGrant) {
		logger.info("方法: updatePaymentChannel, 更新支付渠道, 入参: finChannelGrant: {}", finChannelGrant);
		String channelName = "";// 渠道名称
		try {
			channelName = dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS, PAYMENT_DIC_BUSINESS_SUBJECT_CHANNEL,
					finChannelGrant.getChannelNameCode());
			// 获取系统名称
			String sysName = dicDataService.findNameByValue(PAYMENT_DIC_BUSINESS,
					PaymentConstants.PAYMENT_DIC_BUSINESS_SUBJECT_SYSCODE,
					Integer.parseInt(finChannelGrant.getSysName()));
			finChannelGrant.setChannelNameCode(finChannelGrant.getChannelNameCode());
			finChannelGrant.setChannelName(channelName);
			finChannelGrant
					.setSysNameCode(SystemTypeEnums.sysTypeByValue(Integer.parseInt(finChannelGrant.getSysName())));
			finChannelGrant.setSysName(sysName);

			// 判断平台下的渠道对应的支付方式是否已经存在，如果存在不允许修改
			FinChannelGrant fChannelGrant = this.finChannelGrantService
					.findFinChannelGrantById(finChannelGrant.getId());
			if (fChannelGrant != null && fChannelGrant.getSysNameCode().equals(finChannelGrant.getSysNameCode())
					&& fChannelGrant.getSysNameCode().equals(finChannelGrant.getSysNameCode())
					&& fChannelGrant.getChannelNameCode().equals(finChannelGrant.getChannelNameCode())
					&& fChannelGrant.getPayStyle().equals(finChannelGrant.getPayStyle())) {
				this.finChannelGrantService.updateFinChannelGrant(finChannelGrant);
			} else {
				return new ResponseEntity<>(Constants.ERROR,
						finChannelGrant.getSysName() + "平台" + channelName + "渠道下的该支付模式已经存在！");
			}
			return new ResponseEntity<>(Constants.SUCCESS, "更新成功！");
		} catch (Exception e) {
			logger.error("平台标识: {}, 渠道标识: {}, 支付方式标识: {}, 更新支付方式失败, 失败原因: ", finChannelGrant.getSysName(), channelName,
					finChannelGrant.getPayStyle(), e);
			return new ResponseEntity<>(Constants.ERROR, "更新失败！");
		}
	}

	@Override
	public void executeWithDrawFacade(String startTime, String endTime) {
		boolean dateFlag = FinPayCheckUtil.dateMatcher(startTime);
		if (!dateFlag) {
			logger.error("提现日期格式错误，对账失败！");
		}
		FinPaymentRecord finpaymentRecord = new FinPaymentRecord();
		finpaymentRecord.setState(TradeStateConstants.FINANCE_AUDIT_SUCCESS);
		finpaymentRecord.setModifyTimeBegin(DateUtils.parse(startTime, DateUtils.DATE));
		finpaymentRecord.setModifyTimeEnd(DateUtils.parse(endTime, DateUtils.DATE_HH_MM_SS));
		List<FinPaymentRecord> list = this.finPaymentRecordService.findFinPaymentRecordList(finpaymentRecord);
		logger.info("FinPaymentConsoleFacadeImpl executeWithDrawFacade method 需要处理提现条数: {}",
				list != null ? list.size() : 0);
		for (FinPaymentRecord finPaymentRecord2 : list) {
			try {
				ResponseEntity<?> result = this.withdrawCash(finPaymentRecord2);
				logger.info("executeWithDrawFacade result status: {}, 描述: {} ", result.getResStatus(),
						result.getResMsg());
			} catch (Exception e) {
				logger.error("executeWithDrawFacade 提现失败，flowId: {} , 异常信息: ", finPaymentRecord2.getFlowId(), e);
			}
		}
	}

	@Override
	public ResponseEntity<?> loanRejectWithdrawals(List<Integer> ids, String rejectInfo) {
		logger.info("方法: loanRejectWithdrawals,  提现审核放款拒绝, 入参: ids: {}, rejectInfo: {}", JsonUtils.toJson(ids),
				rejectInfo);
		List<FinPaymentRecord> finPaymentRecordList = new ArrayList<FinPaymentRecord>();
		for (Integer paymentId : ids) {
			FinPaymentRecord record = finPaymentRecordService.findFinPaymentRecordById(paymentId);
			if (record == null) {
				logger.error("提现审核放款拒绝, 提现记录ID: {}, 没有查询到提现信息!", paymentId);
				return new ResponseEntity<>(Constants.ERROR, "没有查询到提现信息,提现记录ID:" + paymentId);
			}
			if (record.getState() != TradeStateConstants.WAIT_PAY_MONEY) {
				logger.error("提现审核放款拒绝, 提现记录ID: {}, 该提现信息状态不对,请核查!", paymentId);
				return new ResponseEntity<>(Constants.ERROR, "该提现信息状态不对,请核查");
			}
			finPaymentRecordList.add(record);
			// 修改审核状态财务审核拒绝,插入解冻流水，更新账户信息
			FinPaymentRecord newFinPaymentRecord = new FinPaymentRecord();
			newFinPaymentRecord.setFlowId(record.getFlowId());
			newFinPaymentRecord.setRejectInfo(rejectInfo);
			newFinPaymentRecord.setState(TradeStateConstants.FINANCE_AUDIT_REJECT);
			finPaymentRecordService.updateByFlowId(newFinPaymentRecord);
			BigDecimal transMoney = record.getTransMoney()
					.add(record.getCommission() == null ? BigDecimal.ZERO : record.getCommission());
			// 生成解冻流水
			FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(record.getRegUserId(), record.getId(), transMoney,
					TradeTransferConstants.TRADE_TYPE_WITHDRAW_AUDIT_REJECT,
					PlatformSourceEnums.typeByValue(record.getTradeSource()));
			ResponseEntity<?> resEntity = finConsumptionService.cashPay(finTradeFlow,
					TradeTransferConstants.TRANSFER_SUB_CODE_THAW);
			if (resEntity.getResStatus() == Constants.ERROR) {
				throw new BusinessException("提现审核放款拒绝, 失败记录Id: " + paymentId);
			}
		}
		// 审核拒绝发送站内信
		sendMsg(finPaymentRecordList);
		return new ResponseEntity<>(Constants.SUCCESS, "审核成功！");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> auditRejectWithdrawals(Integer id, String rejectInfo) {
		logger.info("方法: auditRejectWithdrawals,  提现运营审核拒绝, 入参: id: {}, rejectInfo: {}", id, rejectInfo);
		FinPaymentRecord record = finPaymentRecordService.findFinPaymentRecordById(id);
		if (record == null) {
			logger.error("提现运营审核拒绝, 提现记录ID: {}, 没有查询到提现信息!", id);
			return new ResponseEntity<>(Constants.ERROR, "没有查询到提现信息");
		}
		if (record.getState() != TradeStateConstants.PENDING_PAYMENT) {
			logger.error("提现运营审核拒绝, 提现记录ID: {}, 该提现信息状态不对,请核查!", id);
			return new ResponseEntity<>(Constants.ERROR, "该提现信息状态不对,请核查");
		}
		// 修改审核状态运营审核拒绝,插入解冻流水，更新账户信息
		FinPaymentRecord newFinPaymentRecord = new FinPaymentRecord();
		newFinPaymentRecord.setFlowId(record.getFlowId());
		newFinPaymentRecord.setState(TradeStateConstants.OPERATION_AUDIT_REJECT);
		finPaymentRecordService.updateByFlowId(newFinPaymentRecord);
		BigDecimal transMoney = record.getTransMoney()
				.add(record.getCommission() == null ? BigDecimal.ZERO : record.getCommission());
		// 生成解冻流水
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(record.getRegUserId(), record.getId(), transMoney,
				TradeTransferConstants.TRADE_TYPE_WITHDRAW_AUDIT_REJECT,
				PlatformSourceEnums.typeByValue(record.getTradeSource()));
		ResponseEntity<?> resEntity = finConsumptionService.cashPay(finTradeFlow,
				TradeTransferConstants.TRANSFER_SUB_CODE_THAW);
		if (resEntity.getResStatus() == Constants.ERROR) {
			throw new BusinessException("提现运营审核拒绝, 失败记录Id: " + id);
		}
		try {
			// 如果用户使用了提现券，则回退用户的提现券，如果没有使用提现券，则回退用户手续费
			if (record.getCouponDetailId() != null) {
				sendCouponToMq(record.getRegUserId(), record.getCouponDetailId(), "运营审核拒绝补发");
			}
			// 发送审核拒绝的站内信
			sendMsgToUser(record.getRegUserId(), SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_FAIL.getTitle(),
					SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_FAIL.getMsg(), record.getTransMoney());
		} catch (Exception e) {
			logger.error("提现审核操作发送站内信, 用户id: {}, transMoney: {}, 失败:", record.getRegUserId(), record.getTransMoney(),
					e);
		}
		return new ResponseEntity<>(Constants.SUCCESS, "审核成功");
	}

	/**
	 * @Description : 发送提现券到Mq
	 * @Method_Name : sendCouponToMq;
	 * @param regUserId
	 *            用户Id
	 * @param couponProductId
	 *            提现券产品Id
	 * @param reason
	 *            派发原因
	 * @return : void;
	 * @Creation Date : 2018年6月1日 上午10:24:43;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void sendCouponToMq(Integer regUserId, Integer couponDetailId, String reason) {
		logger.info("方法: sendCouponToMq,  发送提现券到Mq, regUserId: {}, couponProductId: {}, reason: {}", regUserId,
				couponDetailId, reason);
		try {
			VasCouponDetail vasCouponDetail = couponDetailService.findVasCouponDetailById(couponDetailId);
			// step1.将之前提现记录使用的提现券记录修改为已失效状态
			VasCouponDetail updateDetail = new VasCouponDetail();
			updateDetail.setId(vasCouponDetail.getId());
			updateDetail.setState(VasCouponConstants.COUPON_DETAIL_FAILURE);
			couponDetailService.updateVasCouponDetail(updateDetail);
			// step2.给用户补发提现券，根据已使用的提现券的卡券产品进行补发
			CouponDetailMqDTO couponDetailMqDTO = new CouponDetailMqDTO();
			couponDetailMqDTO.setReason(reason);
			couponDetailMqDTO.setRegUserId(regUserId);
			couponDetailMqDTO.setCouponProductId(vasCouponDetail.getCouponProductId());
			jmsService.sendMsg(VasCouponConstants.MQ_QUEUE_VAS_COUPON_DETAIL, DestinationType.QUEUE, couponDetailMqDTO,
					JmsMessageType.OBJECT);
		} catch (Exception e) {
			logger.error(" 发送提现券到Mq, regUserId: {}, couponProductId: {}, reason: {}, 失败原因: ", regUserId,
					couponDetailId, reason, e);
		}

	}

	/**
	 * @Description :发送站内信给用户
	 * @Method_Name : sendMsgToUser;
	 * @param regUserId
	 *            用户ID
	 * @param title
	 *            站内信标题
	 * @param Msg
	 *            站内信内容
	 * @param transMoney
	 *            交易金额
	 * @return : void;
	 * @Creation Date : 2018年6月1日 上午10:32:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void sendMsgToUser(Integer regUserId, String title, String Msg, BigDecimal transMoney) {
		logger.info("方法: sendMsgToUser, 发送站内信, regUserId: {}, title: {}, Msg: {}, transMoney: {}", regUserId, title,
				Msg, transMoney);
		try {
			SmsSendUtil.sendWebMsgToQueue(new SmsWebMsg(regUserId, title, Msg, SmsConstants.SMS_TYPE_NOTICE,
					new String[] { String.valueOf(transMoney) }));
		} catch (Exception e) {
			logger.error("发送站内信, regUserId: {}, title: {}, Msg: {}, transMoney: {}, 失败原因: ", regUserId, title, Msg,
					transMoney, e);
		}
	}

	/**
	 * @Description :提现审核失败发送站内信
	 * @Method_Name : sendMsg;
	 * @param finPaymentRecordList
	 *            支付记录List
	 * @return : void;
	 * @Creation Date : 2018年5月29日 下午4:49:53;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void sendMsg(List<FinPaymentRecord> finPaymentRecordList) {
		logger.error("sendMsg, 提现审核操作发送站内信, finPaymentRecordListy: {}", JsonUtils.toJson(finPaymentRecordList));
		FinPaymentRecord paymentRecord = null;
		try {
			for (FinPaymentRecord finPaymentRecord : finPaymentRecordList) {
				paymentRecord = finPaymentRecord;
				// 如果用户使用了提现券，则 补发提现券
				if (finPaymentRecord.getCouponDetailId() != null) {
					sendCouponToMq(finPaymentRecord.getRegUserId(), finPaymentRecord.getCouponDetailId(), "财务审核拒绝补发");
				}
				// 发送站内信
				sendMsgToUser(finPaymentRecord.getRegUserId(), SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_FAIL.getTitle(),
						SmsMsgTemplate.MSG_PAYMENT_TX_APPLY_FAIL.getMsg(),
						finPaymentRecord.getTransMoney() == null ? BigDecimal.ZERO : finPaymentRecord.getTransMoney());
			}
		} catch (Exception e) {
			logger.error("提现审核操作发送站内信, 用户id: {}, transMoney: {}, 失败:", paymentRecord.getRegUserId(),
					paymentRecord.getTransMoney() == null ? BigDecimal.ZERO : paymentRecord.getTransMoney(), e);
		}
	}

	@Override
	public ResponseEntity<?> findPaymentRecordCountList(Pager pager, PayCheckVo payCheckVo) {
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		// 1、如果用户姓名和手机号不为空，则查询用户集合
		if (StringUtils.isNotBlank(payCheckVo.getRealName()) || payCheckVo.getLogin() != null) {
			UserVO userVoData = new UserVO();
			userVoData.setRealName(payCheckVo.getRealName());
			userVoData.setLogin(payCheckVo.getLogin());
			List<Integer> regUserIdList = this.regUserService.findUserIdsByUserVO(userVoData);
			paymentRecord.setRegUserIdList(regUserIdList);
		}
		// 2、查询充值提现支付记录信息
		BeanPropertiesUtil.mergeAndReturn(paymentRecord, payCheckVo);
		paymentRecord.setSortColumns("create_time desc");
		Pager pagerInfo = finPaymentRecordService.findFinPaymentRecordList(paymentRecord, pager);
		List<PayCheckVo> payCheckVoList = new ArrayList<PayCheckVo>();
		// 3、组装返回前台数据
		if (pagerInfo != null && CommonUtils.isNotEmpty(pagerInfo.getData())) {
			List<FinPaymentRecord> paymentRecordList = (List<FinPaymentRecord>) pagerInfo.getData();
			if (paymentRecordList != null && paymentRecordList.size() > 0) {
				for (FinPaymentRecord finPaymentRecord : paymentRecordList) {
					PayCheckVo payCheckData = new PayCheckVo();
					BeanPropertiesUtil.mergeAndReturn(payCheckData, finPaymentRecord);
					UserVO userVO = this.regUserService.findUserWithDetailById(finPaymentRecord.getRegUserId());
					if (userVO != null) {
						payCheckData.setRealName(userVO.getRealName());
						if (userVO.getType() != UserConstants.USER_TYPE_GENERAL) {
							RegCompanyInfo regCompanyInfo = regCompanyInfoService
									.findRegCompanyInfoByRegUserId(finPaymentRecord.getRegUserId());
							payCheckData
									.setRealName(regCompanyInfo == null ? "企业用户" : regCompanyInfo.getEnterpriseName());
						}
						payCheckData.setLogin(userVO.getLogin());
					}
					payCheckVoList.add(payCheckData);
				}
			}
		}
		pagerInfo.setData(payCheckVoList);
		return new ResponseEntity<>(SUCCESS, pagerInfo);
	}

    @Override
    public ResponseEntity<?> findPaymentCountList(Pager pager, PaymentRecordVo paymentRecordVo) {
        List<Integer> regUserIdList = null;
        // 判断用户是否输入了用户姓名或手机号作为查询条件
        if (paymentRecordVo.getLogin() != null || StringUtils.isNotBlank(paymentRecordVo.getRealName())) {
            UserVO userVO = new UserVO();
            if (StringUtils.isNotBlank(paymentRecordVo.getRealName())) {
                userVO.setRealName(paymentRecordVo.getRealName());
            }
            if (paymentRecordVo.getLogin() != null) {
                userVO.setLogin(paymentRecordVo.getLogin());
            }
            // 查询用户信息
            regUserIdList = regUserService.findUserIdsByUserVO(userVO);
        }
        //分页查询充值提现记录
        FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
        BeanPropertiesUtil.mergeAndReturn(finPaymentRecord,paymentRecordVo);
        finPaymentRecord.setRegUserIdList(regUserIdList);
        finPaymentRecord.setSortColumns("create_time desc");
        Pager pagerInfo = finPaymentRecordService.findFinPaymentRecordList(finPaymentRecord, pager);
        List<PaymentRecordVo> paymentRecordList = new ArrayList<PaymentRecordVo>();
        //组装返回给前台的充值提现信息
        if (!BaseUtil.resultPageHasNoData(pagerInfo)) {
            List<FinPaymentRecord> recordList = (List<FinPaymentRecord>) pagerInfo.getData();
           for (FinPaymentRecord record : recordList) {
                PaymentRecordVo paymentVo = new PaymentRecordVo();
                UserVO userInfo = regUserService.findUserWithDetailById(record.getRegUserId());
                FinBankCard finBankCard = finBankCardService.findById(record.getBankCardId());
                if(finBankCard == null){
                    BeanPropertiesUtil.mergeAndReturn(paymentVo,record,userInfo);
                }else{
                    BeanPropertiesUtil.mergeAndReturn(paymentVo,record,userInfo,finBankCard);
                }
                paymentVo.setCreateTime(record.getCreateTime());
                paymentVo.setState(record.getState());
                paymentRecordList.add(paymentVo);
           }
        }
        pagerInfo.setData(paymentRecordList);
        return new ResponseEntity<>(SUCCESS,pagerInfo);
    }
}
