package com.hongkun.finance.payment.facade.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JKey;
import com.hongkun.finance.payment.bfpayvo.TransContent;
import com.hongkun.finance.payment.bfpayvo.TransRushResultAsyn;
import com.hongkun.finance.payment.client.yeepay.utils.Digest;
import com.hongkun.finance.payment.client.yeepay.utils.XStreamUtil;
import com.hongkun.finance.payment.client.yeepay.vo.TransferSingleReq;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.enums.*;
import com.hongkun.finance.payment.facade.FinPayMentNoticeFacade;
import com.hongkun.finance.payment.factory.BaofuPayFactory;
import com.hongkun.finance.payment.factory.BaofuProtocolPayFactory;
import com.hongkun.finance.payment.factory.YeepayPayFactory;
import com.hongkun.finance.payment.llpayvo.PayDataBean;
import com.hongkun.finance.payment.llpayvo.RetBean;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.hongkun.finance.payment.security.Base64Util;
import com.hongkun.finance.payment.service.*;
import com.hongkun.finance.payment.util.FinTFUtil;
import com.hongkun.finance.payment.util.PaymentUtil;
import com.hongkun.finance.payment.util.RSAUtil;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.constants.SmsMsgTemplate;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.model.RegUser;
import com.hongkun.finance.user.model.RegUserDetail;
import com.hongkun.finance.user.service.RegUserDetailService;
import com.hongkun.finance.user.service.RegUserService;
import com.hongkun.finance.vas.constants.VasVipConstants;
import com.hongkun.finance.vas.model.vo.VasVipGrowRecordMqVO;
import com.hongkun.finance.vas.utils.VipGrowRecordUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import static com.hongkun.finance.payment.constant.TradeStateConstants.*;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.yirun.framework.core.commons.Constants.*;

@Service
public class FinPayMentNoticeFacadeImpl implements FinPayMentNoticeFacade {
	private static final Logger logger = LoggerFactory.getLogger(FinPayMentNoticeFacadeImpl.class);
	@Reference
	private FinPayConfigService finPayConfigService;
	@Reference
	private RegUserService regUserService;
	@Reference
	private FinBankCardService finBankCardService;
	@Reference
	private FinTradeFlowService finTradeFlowService;
	@Reference
	private FinPaymentRecordService finPaymentRecordService;
	@Reference
	private FinConsumptionService finConsumptionService;
	@Reference
	private FinFundtransferService finFundtransferService;
	@Reference
	private RegUserDetailService regUserDetailService;
	@Reference
	private FinAccountService finAccountService;
	@Reference
	private FinBankCardBindingService finBankCardBindingService;
	@Reference
	private FinBankReferService finBankReferService;

	@Override
	public ResponseEntity<?> lianlianSignNotice(String reqStr, String bankCardId) {
		boolean resultFlag = false;
		JedisClusterLock jedisLock = new JedisClusterLock();
		String lockKey = "";
		if (PaymentUtil.isNull(reqStr)) {
			return new ResponseEntity<>(Constants.ERROR, "签约失败,通知数据为空");
		}
		FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.LianLian.getChannelKey(), PayStyleEnum.QY.getType());
		if (payConfig == null) {
			return new ResponseEntity<>(Constants.ERROR, "获取支付配置信息异常！");
		}
		if (!PaymentUtil.checkSign(reqStr, payConfig.getPublicKey(), payConfig.getPayMd5Key())) {
			return new ResponseEntity<>(Constants.ERROR, "签约失败,签约同步通知验签失败！");
		}
		// 解析异步通知对象
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		// 此处更新银行卡签约状态
		String resultSign = payDataBean.getResult_sign();// 签约结果
		String userId = payDataBean.getUser_id(); // 连连回传的商户用户唯一编号
		String noAgree = payDataBean.getNo_agree();// 签约协议编号
		lockKey = LOCK_PREFFIX + RegUser.class.getSimpleName() + userId;
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				RegUser regUser = regUserService.findRegUserById(Integer.valueOf(userId));
				if (regUser == null) {
					return new ResponseEntity<>(Constants.ERROR, "签约用户:" + userId + "不存在！");
				}
				if (SUCCESS_FLAG_NAME.toUpperCase().equals(resultSign)) {
					if (StringUtils.isEmpty(noAgree)) {
						return new ResponseEntity<>(Constants.ERROR, "签约没有返回签约协议号！");
					}
					BankCardVo bankCard = finBankCardService.findBankCardInfo(regUser.getId(),
							Integer.parseInt(bankCardId), PayChannelEnum.LianLian);
					if (bankCard != null) {
						FinBankCardBinding finBankCardBinding = new FinBankCardBinding();
						finBankCardBinding.setThirdAccount(noAgree);
						finBankCardBinding.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
						finBankCardBinding.setId(bankCard.getId());
						finBankCardBindingService.updateFinBankCardBinding(finBankCardBinding);
					}
				} else if (SUCCESS_FLAG_NAME.toUpperCase().equals(resultSign)) {
					return new ResponseEntity<>(Constants.ERROR, "银行签约处理中！");
				} else {
					return new ResponseEntity<>(Constants.ERROR, "签约失败！");
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.ERROR, "签约失败:" + e.getMessage());
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS, "签约成功!");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> lianlianAsyncNotice(String reqStr) throws Exception {
		ResponseEntity<?> resResult = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resMap = new HashMap<String, Object>();
		String tradeFlowNo = "";// 支付流水号
		try {
			// 测试环境测试用
			if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
				String str[] = reqStr.split("[|]");
				String respCode = str[0];// 支付结果code
				tradeFlowNo = str[1]; // 联联异步回传平台订单号
				String retMsg = str[2];// 返回结果描述信息
				String transMoney = str[3];// 提现金额
				logger.info("提现流水标识: {}, 连连提现异步通知,通知结果: {}, 通知描述: {}, 订单金额: {}", tradeFlowNo, respCode, retMsg,
						transMoney);
				// 提现异步通知处理账户流水信息
				ResponseEntity<?> result = this.withDrawAsyncNotice(tradeFlowNo,
						LianLianPlatformPayStateEnum.platFormStateByThirdPaymentState(respCode),
						new BigDecimal(transMoney), retMsg, PayChannelEnum.LianLian.getChannelKey());

				RetBean retBean = new RetBean();// 通知响应对象
				retBean.setRet_code(
						LianLianPlatformPayStateEnum.thirdPaymentStateByPlatFormState(result.getResStatus()));
				retBean.setRet_msg((String) result.getResMsg());
				resMap.put("retBean", retBean);
				resResult.setParams(resMap);
			} else {
				// 查询提现支付配置信息
				FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
						PlatformSourceEnums.PC.getType(), PayChannelEnum.LianLian.getChannelKey(),
						PayStyleEnum.WITHDRAW.getType());
				if (payConfig == null) {
					logger.error("连连提现异步通知, 获取支付配置信息异常！");
					return new ResponseEntity<>(Constants.ERROR, "获取支付配置信息异常！");
				}
				// 支付异步通知验签
				if (!PaymentUtil.checkSign(reqStr, payConfig.getPublicKey(), payConfig.getPayMd5Key())) {
					logger.error("连连提现异步通知, 支付异步通知验签失败！");
					return new ResponseEntity<>(Constants.ERROR, "支付异步通知验签失败！");
				}
				// 解析异步通知对象
				PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
				if (payDataBean == null) {
					logger.error("连连提现异步通知, 异步通知报文解析异常！");
					return new ResponseEntity<>(Constants.ERROR, "异步通知报文解析异常！");
				}
				String respCode = payDataBean.getResult_pay();// 支付结果code
				tradeFlowNo = payDataBean.getNo_order(); // 联联异步回传平台订单号
				String retMsg = payDataBean.getRet_msg();// 返回结果描述信息
				String transMoney = payDataBean.getMoney_order();// 提现金额
				logger.info("连连提现异步通知, 提现流水标识: {}, 通知结果: {}, 通知描述: {}, 订单金: {}", tradeFlowNo, respCode, retMsg,
						transMoney);
				// 提现异步通知处理账户流水信息
				ResponseEntity<?> result = ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
						.withDrawAsyncNotice(tradeFlowNo,
								LianLianPlatformPayStateEnum.platFormStateByThirdPaymentState(respCode),
								new BigDecimal(transMoney), retMsg, PayChannelEnum.LianLian.getChannelKey());
				logger.info("连连提现异步通知, 提现流水标识: {}, 异步通知处理结果状态: {}, 结果描述: {}", tradeFlowNo, result.getResStatus(),
						result.getResMsg());
				RetBean retBean = new RetBean();// 通知响应对象
				retBean.setRet_code(
						LianLianPlatformPayStateEnum.thirdPaymentStateByPlatFormState(result.getResStatus()));
				retBean.setRet_msg((String) result.getResMsg());
				resMap.put("retBean", retBean);
				resResult.setParams(resMap);
			}
		} catch (Exception e) {
			resResult.setResStatus(ERROR);
			logger.error("连连提现异步通知, 提现流水标识: {}, 连连提现异步通知处理失败: ", tradeFlowNo, e);
			resMap.put("retBean", this.dealErrorMes(e.getMessage()));
			resResult.setParams(resMap);
		}
		return resResult;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> lianlianRechargeAsyncNotifyUrl(String reqStr, Integer incomeType) throws Exception {
		logger.info("方法: lianlianRechargeAsyncNotifyUrl, 连连充值异步回调操作, 入参: reqStr: {}, incomeType: {}", reqStr,
				incomeType);
		PayDataBean payDataBean = null;
		BigDecimal rechargeMoney = BigDecimal.ZERO;
		String payResCode = "";// 支付结果
		String tradeFlowNo = "";// 交易流水
		String agreeNo = "";// 协议号
		// 测试环境测试用
		if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
			String str[] = reqStr.split("[|]");
			tradeFlowNo = str[0]; // 联联异步回传平台订单号
			String moneyOrder = str[1];// 代扣还款金额
			rechargeMoney = BigDecimal.valueOf(Double.valueOf(moneyOrder));
			payResCode = str[2];// 支付结果
			agreeNo = str[3];// 协议号
			payDataBean = new PayDataBean();
			payDataBean.setNo_order(tradeFlowNo);
			payDataBean.setMoney_order(moneyOrder);
			payDataBean.setResult_pay(payResCode);
			payDataBean.setNo_agree(agreeNo);
			payDataBean.setAcct_name(str[4]);
			payDataBean.setId_no(str[5]);
			if (PaymentUtil.isNull(tradeFlowNo)) {
				logger.error("连连充值异步通知操作, 异步通知异常: {}", "异步充值通知报文原订单号为空!");
				return new ResponseEntity<String>(ERROR, "异步充值通知报文原订单号为空!");
			}
		} else {
			// 查询充值支付配置信息
			FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.LianLian.getChannelKey(),
					PayStyleEnum.RZ.getType());
			if (payConfig == null) {
				logger.error("连连充值异步通知操作, 异常通知失败: {}", "获取支付配置信息异常！");
				return new ResponseEntity<String>(ERROR, "获取支付配置信息异常！");
			}
			// 响应报文进行验签
			boolean sign = PaymentUtil.checkSign(reqStr, payConfig.getPublicKey(), payConfig.getPayMd5Key());
			if (!sign) {
				logger.error("连连充值异步通知操作, 异常通知失败: {}", "支付异步通知验签失败！");
				return new ResponseEntity<String>(ERROR, "支付异步通知验签失败！");
			}
			// 解析异步通知对象
			payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
			tradeFlowNo = payDataBean.getNo_order(); // 联联异步回传平台订单号
			String moneyOrder = payDataBean.getMoney_order();// 代扣还款金额
			rechargeMoney = BigDecimal.valueOf(Double.valueOf(moneyOrder));
			payResCode = payDataBean.getResult_pay();// 支付结果
			agreeNo = payDataBean.getNo_agree();// 协议号
			if (PaymentUtil.isNull(tradeFlowNo)) {
				logger.error("连连充值异步通知操作, 异步通知异常: {}", "异步充值通知报文原订单号为空!");
				return new ResponseEntity<String>(ERROR, "异步充值通知报文原订单号为空!");
			}
		}
		logger.info("连连充值异步通知操作, 交易流水ID: {}, 交易金额: {}, 协议号: {}, 交易状态: {}, 交易描述: {}", tradeFlowNo, rechargeMoney,
				agreeNo, payResCode, payDataBean.getRet_msg());
		// 根据连连返回的流水号，查询此笔订单是否存在
		FinPaymentRecord paymentFlowRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(tradeFlowNo);
		if (paymentFlowRecord == null) {
			logger.error("连连充值异步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", tradeFlowNo);
			return new ResponseEntity<>(ERROR, "交易流水查询平台的订单不存在！");
		}
		logger.info("连连充值异步通知操作, 用户标识: {}, 通过交易流水ID, 查询到的平台订单: {}", paymentFlowRecord.getRegUserId(),
				paymentFlowRecord.toString());
		// 充值异步通知主逻缉处理 校验第三方支付异步通知返回的信息，更新用户账户，生成充值的交易流水和资金划转，发送站内信，短信，生成成长值
		FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
		finPaymentRecord.setState(TRANSIT_FAIL);
		finPaymentRecord.setFlowId(paymentFlowRecord.getFlowId());
		JedisClusterLock jedisLock = new JedisClusterLock();
		RegUserDetail userDetail = null;
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + tradeFlowNo;
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				paymentFlowRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(tradeFlowNo);
				if (rechargeMoney == null || paymentFlowRecord.getTransMoney().compareTo(rechargeMoney) != 0
						|| CompareUtil.lteZero(rechargeMoney)) {
					logger.error("连连充值异步通知操作, 用户标识: {}, 流水号标识：{}, 连连充值异步通知失败: {}", tradeFlowNo, rechargeMoney,
							"交易金额被篡改！");
					// 更新支付记录流水状态 为失败
					finPaymentRecordService.updateByFlowId(finPaymentRecord);
					return new ResponseEntity<>(ERROR, "交易金额被篡改！");
				}
				// 判断第三方返回的支付结果是否成功，如果不成功，直接更新支付记录状态为失败
				if (!LIAN_SUCESS_SIGN.equalsIgnoreCase(payResCode)) {
					logger.info("连连充值异步通知操作, 用户标识: {}, 流水号标识：{}, 连连充值异步通知失败: {}", tradeFlowNo, rechargeMoney,
							payDataBean.getRet_msg());
					// 更新支付记录流水状态 为失败
					finPaymentRecordService.updateByFlowId(finPaymentRecord);
					return new ResponseEntity<>(ERROR, "异步充值处理失败!");
				}
				// 判断支付流水对应的当前支付记录的状态，如果第三方交易状态返回成功，并且平台状态不是已划转或划转失败，则进行充值处理
				if (paymentFlowRecord.getState() == TradeStateConstants.ALREADY_PAYMENT
						&& paymentFlowRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 认证方式判断用户是否同卡进出
					userDetail = regUserDetailService.findRegUserDetailByRegUserId(paymentFlowRecord.getRegUserId());
					if (userDetail == null || StringUtils.isBlank(userDetail.getIdCard())
							|| StringUtils.isBlank(userDetail.getRealName())
							|| !userDetail.getRealName().equalsIgnoreCase(payDataBean.getAcct_name())
							|| !userDetail.getIdCard().equalsIgnoreCase(payDataBean.getId_no())) {
						logger.error("连连充值异步通知操作,  用户标识: {},流水号标识: {}, 用户身份证号: {}, 用户姓名: {}, 连连异步通知失败: {}",
								paymentFlowRecord.getRegUserId(), tradeFlowNo, payDataBean.getId_no(),
								payDataBean.getAcct_name(), "用户实名校验异常!");
						return new ResponseEntity<>(ERROR, "为保证资金安全，必须同卡进出!");
					}
					// 更新支付记录，更新账户，更新银行信息，生成交易流水及资金划转,发送短信，站内信，首次充值赠送成长值
					this.dealRechargeNotice(paymentFlowRecord, agreeNo, payDataBean.getBank_code(), incomeType);
				} else {
					// 如果为已划转或划转失败，则认为重复请求
					logger.error("连连充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", paymentFlowRecord.getRegUserId(),
							tradeFlowNo, "订单号已经存在,不允许重复支付！");
					return new ResponseEntity<>(ERROR, "订单号已经存在,不允许重复支付！");
				}
			} else {
				logger.error("连连充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", paymentFlowRecord.getRegUserId(),
						tradeFlowNo, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前网络有些拥挤，请稍候再试！！");
			}
		} catch (Exception e) {
			logger.error("连连充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 第三方异步通知异常: ", paymentFlowRecord.getRegUserId(), tradeFlowNo,
					e);
			finPaymentRecordService.updateByFlowId(finPaymentRecord);
			throw new GeneralException("异步通知处理失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(SUCCESS, paymentFlowRecord.getId());
	}

	/**
	 * @Description :校验第三方支付异步通知返回的信息，更新用户账户，生成充值的交易流水和资金划转，发送站内信，短信，生成成长值
	 * @Method_Name : dealSyncRecharge;
	 * @param tradeFlowNo
	 *            支付流水号
	 * @param rechargeMoney
	 *            充值金额
	 * @param userName
	 *            用户姓名
	 * @param idCard
	 *            身份证号
	 * @param agreeNo
	 *            第三方协议号
	 * @param bankCode
	 *            银行CODE
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月5日 下午3:07:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> dealSyncRecharge(String tradeFlowNo, BigDecimal rechargeMoney, String userName,
			String idCard, String agreeNo, String bankCode, String payResult, Integer incomeType) throws Exception {
		JedisClusterLock jedisLock = new JedisClusterLock();
		RegUserDetail userDetail = null;
		FinPaymentRecord paymentFlowRecord = null;
		FinAccount finAccount = null;
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + tradeFlowNo;
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				// 根据连连返回的流水号，查询此笔订单是否存在
				paymentFlowRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(tradeFlowNo);
				if (paymentFlowRecord == null || paymentFlowRecord.getTransMoney().compareTo(rechargeMoney) != 0
						|| CompareUtil.lteZero(rechargeMoney)) {
					logger.info("流水号标识：{},连连充值异步通知异常,第三方异步充值金额:{}", tradeFlowNo, rechargeMoney);
					return new ResponseEntity<>(ERROR,
							"流水异常，第三方异步充值金额{" + rechargeMoney + "}" + "流水订单号：" + tradeFlowNo);
				}
				// 判断第三方返回的支付结果是否成功，如果不成功，直接更新支付记录状态为失败
				if (!LIAN_SUCESS_SIGN.equalsIgnoreCase(payResult)) {
					logger.info("流水号标识：{},连连充值异步通知失败,第三方异步充值金额:{},连连返回交易状态码：{}", tradeFlowNo, rechargeMoney, payResult);
					// 更新支付记录流水状态
					FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
					finPaymentRecord.setState(TRANSIT_FAIL);
					finPaymentRecord.setFlowId(paymentFlowRecord.getFlowId());
					finPaymentRecordService.updateByFlowId(finPaymentRecord);
					return new ResponseEntity<>(ERROR, "异步充值失败!");
				}
				// 判断支付流水对应的当前支付记录的状态，如果为已划转或划转失败，则认为重复请求
				if (paymentFlowRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& paymentFlowRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 认证方式判断用户是否同卡进出
					userDetail = regUserDetailService.findRegUserDetailByRegUserId(paymentFlowRecord.getRegUserId());
					if (userDetail == null || StringUtils.isBlank(userDetail.getIdCard())
							|| StringUtils.isBlank(userDetail.getRealName())
							|| !userDetail.getRealName().equalsIgnoreCase(userName)
							|| !userDetail.getIdCard().equalsIgnoreCase(idCard)) {
						logger.info("流水号标识：{},用户标识：{},用户身份证号：{},用户姓名：{},连连异步通知,用户实名校验异常", tradeFlowNo,
								paymentFlowRecord.getRegUserId(), idCard, userName);
						return new ResponseEntity<>(ERROR, "为保证资金安全，必须同卡进出!");
					}
					// 更新支付记录，更新账户，更新银行信息，生成交易流水及资金划转
					this.dealRechargeNotice(paymentFlowRecord, agreeNo, bankCode, incomeType);
					// 查询用户账户的可用余额，用作短信通知
					finAccount = finAccountService.findByRegUserId(userDetail.getRegUserId());
					if (finAccount == null) {
						logger.info("支付记录流水标识：{}，用户标识：{}，第三方充值异步通知，获取用户账号信息为空！", tradeFlowNo,
								userDetail.getRegUserId());
						throw new BusinessException("获取用户{" + userDetail.getRegUserId() + "}账号信息为空！");
					}
				} else {
					logger.info("支付记录标识：{}，第三方充值异步通知，订单号已经存在,不允许重复支付！", tradeFlowNo);
					return new ResponseEntity<>(ERROR, "支付记录标识：{" + tradeFlowNo + "}，第三方充值异步通知，订单号已经存在,不允许重复支付！");
				}
			} else {
				logger.info("当前网络有些拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前网络有些拥挤，请稍候再试！！");
			}
		} catch (Exception e) {
			logger.error("支付记录标识：{}，第三方异步通知异常：", paymentFlowRecord.getFlowId(), e);
			throw new GeneralException("支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		// 如果是正常充值，则发送站内信，短信,首次充值增送成长值
		dealSmsMsgAndVipGrow(userDetail.getRegUserId(), paymentFlowRecord.getTransMoney(), finAccount.getUseableMoney(),
				incomeType);
		return new ResponseEntity<>(SUCCESS, paymentFlowRecord.getId());

	}

	/**
	 * @Description : 更新支付记录状态，更新账户，生成交易流水及资金划转
	 * @Method_Name : dealRechargeNotice;
	 * @param paymentRecord
	 *            支付记录
	 * @param agreeNo
	 *            协议号
	 * @param bankCode
	 *            银行编码
	 * @param incomeType
	 *            充值类型：普通充值 0 ; 物业充值1
	 * @throws Exception
	 * @return : void;
	 * @Creation Date : 2018年4月4日 下午5:42:28;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealRechargeNotice(FinPaymentRecord paymentRecord, String agreeNo, String bankCode, Integer incomeType)
			throws BusinessException {
		logger.info(
				"方法: dealRechargeNotice, 充值异步回调,更新支付记录状态,更新账户,生成交易流水及资金划转, 入参: paymentRecord: {}, agreeNo: {}, bankCode: {}, incomeType: {}",
				paymentRecord, agreeNo, bankCode, incomeType);
		// 如果是认证支付，则需要处理银行信息和判断协议号是否为空,目前宝付协议支付逻缉改变，不走此方法，暂且注释掉
		
		 if (PayStyleEnum.RZ.getValue() == paymentRecord.getRechargeSource())
		 {
			 // 插入或更新银行卡信息 
			 updateBankCarding(paymentRecord, agreeNo, bankCode);
		 }
		 
		// 更新支付记录流水状态为已划转
		FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
		finPaymentRecord.setState(ALREADY_PAYMENT);
		finPaymentRecord.setFlowId(paymentRecord.getFlowId());
		finPaymentRecordService.updateByFlowId(finPaymentRecord);
		// 查询用户账户的可用余额，用作短信通知
		FinAccount finAccount = finAccountService.findByRegUserId(paymentRecord.getRegUserId());
		if (finAccount == null) {
			logger.error("充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 第三方充值异步通知，获取用户账号信息为空！", paymentRecord.getRegUserId(),
					paymentRecord.getFlowId());
			throw new BusinessException("获取用户{" + paymentRecord.getRegUserId() + "}账号信息为空！");
		}
		// 根据支付记录flowId,获取交易流水中组装flowId需要的交易类型和来源
		Map<String, String> resultMap = FinTFUtil.getTradeTypeAndTradeSourceByIncomeType(paymentRecord.getFlowId(),
				incomeType);
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(paymentRecord.getRegUserId(), paymentRecord.getId(),
				paymentRecord.getTransMoney(), Integer.parseInt(resultMap.get("tradeType")),
				PlatformSourceEnums.typeByValue(Integer.parseInt(resultMap.get("tradeSource"))));
		// 更新用户账户，生成交易流水，资金划转流水
		ResponseEntity<?> payResult = finConsumptionService.cashPay(finTradeFlow, TRANSFER_SUB_CODE_INCOME);
		if (payResult.getResStatus() == ERROR) {
			logger.error("充值异步回调, 更新支付记录状态,更新账户,生成交易流水及资金划转, 支付记录流水标识: {}, 支付失败: {}", paymentRecord.getFlowId(),
					payResult.getResMsg().toString());
			throw new BusinessException("支付失败！");
		}
		// 发送站内信，短信，首次充值增送成长值
		dealSmsMsgAndVipGrow(paymentRecord.getRegUserId(), paymentRecord.getTransMoney(), finAccount.getUseableMoney(),
				incomeType);
	}

	/**
	 *
	 * @Description : 更新银行卡是否绑定
	 * @Method_Name : updateBankCarding
	 * @param finBankCard
	 *            银行信息
	 * @param finPaymentRecord
	 *            支付记录
	 * @param thirdAccount
	 *            第三方协议号
	 * @param bankCode
	 *            银行CODE
	 * @return : void
	 * @Creation Date : 2017年9月30日 下午4:41:25
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private void updateBankCarding(FinPaymentRecord finPaymentRecord, String thirdAccount, String bankCode) {
		if (StringUtils.isBlank(thirdAccount)) {
			logger.info("充值异步回调,更新支付记录状态,更新账户,生成交易流水及资金划转, 支付记录流水标识: {}, 第三方充值异步通知,获取协议号为空！",
					finPaymentRecord.getFlowId());
		}
		FinBankCard finBankCard = finBankCardService.findById(finPaymentRecord.getBankCardId());
		bankCode = StringUtils.isBlank(bankCode) ? finBankCard.getBankCode() : bankCode;
		// 如果用户绑卡状态为未认证，则更新为已认证状态
		if (finBankCard.getState() != BANK_CARD_STATE_AUTH) {
			FinBankCard bankCard = new FinBankCard();
			bankCard.setId(finBankCard.getId());
			bankCard.setState(BANK_CARD_STATE_AUTH);
			finBankCardService.update(bankCard);
		}
		// 查询银行bankCardBinding信息,如果不存在，则插入一条信息，否则更新信息
		FinBankCardBinding finBankCardBinding = finBankCardBindingService.findBankCardBinding(
				finPaymentRecord.getBankCardId(), finPaymentRecord.getRegUserId(), finPaymentRecord.getPayChannel());
		if (finBankCardBinding == null) {
			FinBankCardBinding finBankCardBindingCdt = new FinBankCardBinding();
			RegUser regUser = regUserService.findRegUserById(finPaymentRecord.getRegUserId());
			// 如果充值来源是宝付，则获取宝付的第三方银行CODE信息，保存在银行信息表中
			if (finPaymentRecord.getRechargeSource() == PayChannelEnum.BaoFu.getChannelNameValue()
					|| finPaymentRecord.getRechargeSource() == PayChannelEnum.BaoFuProtocol.getChannelNameValue()
					|| finPaymentRecord.getRechargeSource() == PayChannelEnum.BaoFuProtocolB.getChannelNameValue()) {
				FinBankRefer finBankRefer = finBankReferService.findBankRefer(
						PayChannelEnum.getPayChannelEnumByCode(finPaymentRecord.getPayChannel()), bankCode,
						PayStyleEnum.RZ, regUser.getType());
				finBankCardBindingCdt.setBankThirdCode(finBankRefer.getBankThirdCode());
			} else {
				finBankCardBindingCdt.setBankThirdCode(bankCode);
			}
			finBankCardBindingCdt.setRegUserId(finPaymentRecord.getRegUserId());
			finBankCardBindingCdt.setPayChannel(finPaymentRecord.getPayChannel());
			finBankCardBindingCdt.setState(BANK_CARD_STATE_AUTH);
			finBankCardBindingCdt.setFinBankCardId(finPaymentRecord.getBankCardId());
			finBankCardBindingCdt.setThirdAccount(thirdAccount);
			finBankCardBindingService.insertFinBankCardBinding(finBankCardBindingCdt);
		} else {
			FinBankCardBinding cardBinding = new FinBankCardBinding();
			cardBinding.setId(finBankCardBinding.getId());
			cardBinding.setThirdAccount(thirdAccount);
			cardBinding.setState(BANK_CARD_STATE_AUTH);
			finBankCardBindingService.updateFinBankCardBinding(cardBinding);
		}
	}

	/***
	 * @Description : 提现异步通知，修改订单状态
	 * @Method_Name : withDrawAsyncNotice;
	 * @param transFlowId
	 *            第三方支付系统返回的平台订单号
	 * @param int
	 *            第三方支付系统的订单状态 划转中(0),成功(1),失败(-1),退款(2) PaymentConstants
	 * @param transMoney
	 *            订单的交易金额
	 * @param payMsg
	 *            订单的交易描述
	 * @param payChannelEnum
	 *            支付渠道
	 * @return : ResponseEntity; 返回处理状态：
	 *         订单处理成功(100),订单处理失败(-666),订单不存在(777),订单重复请求(888)PaymentConstants
	 * @throws Exception
	 * @Creation Date : 2017年10月30日 下午5:02:03;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> withDrawAsyncNotice(String transFlowId, int payState, BigDecimal transMoney, String payMsg,
			String channel) throws Exception {
		logger.info(
				"方法: withDrawAsyncNotice, 提现异步通知, 提现流水标识: {}, 入参: transFlowId: {}, payState: {}, transMoney: {}, payMsg: {}, channel: {}",
				transFlowId, transFlowId, payState, transMoney, payMsg, channel);
		// 根据第三方系统返回的订单号查询平台对应订单的支付记录流水
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(transFlowId);
		if (finPaymentRecord == null) {
			// 没有查询到对应的支付记录流水信息
			logger.error("提现异步通知, 提现流水标识: {}, 渠道标识: {}, 提现异步通知,流水号不存在!", transFlowId, channel);
			return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_NON_EXISTENT,
					"订单" + transFlowId + "不存在！");
		}
		logger.info("提现异步通知, 提现流水标识: {}, 提现流水对象: {}", transFlowId, finPaymentRecord.toString());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + transFlowId;
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(transFlowId);
				if (CompareUtil.lteZero(transMoney) || transMoney.compareTo(finPaymentRecord.getTransMoney()) != 0) {
					// 交易金额被篡改
					logger.error("提现异步通知, 用户标识: {}, 流水标识: {}, 渠道标识: {}, 第三方返回的交易金额: {}, 提现异步通知,交易金额被篡改",
							finPaymentRecord.getRegUserId(), transFlowId, channel, transMoney);
					return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_FAIL, "交易金额被篡改!");
				}
				// 查询用户是否存在
				RegUser regUser = regUserService.findRegUserById(finPaymentRecord.getRegUserId());
				if (regUser == null) {
					logger.error("提现异步通知, 用户标识: {}, 流水标识: {}, 渠道标识: {}, 提现异步通知,获取用户信息为空",
							finPaymentRecord.getRegUserId(), transFlowId, channel);
					return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_FAIL, "获取用户信息为空！");
				}
				if (!PENDING_PAYMENT.equals(finPaymentRecord.getState())
						&& !WAIT_PAY_MONEY.equals(finPaymentRecord.getState())
						&& !ALREADY_PAYMENT.equals(finPaymentRecord.getState())
						&& !FINANCE_AUDIT_REJECT.equals(finPaymentRecord.getState())
						&& !CORRECT_MONEY.equals(finPaymentRecord.getState())
						&& !OPERATION_AUDIT_REJECT.equals(finPaymentRecord.getState())) {
					// 判断支付结果是否成功
					if (PAYMENT_THIRD_BANK_SYSTEM_STATE_SUCCESS == payState) {
						logger.info("提现异步通知, 用户标识: {}, 流水标识: {}, 渠道标识: {}, 提现异步通知,第三方支付系统返回状态为成功,提现成功,更新平台账户及流水信息!",
								finPaymentRecord.getRegUserId(), finPaymentRecord.getFlowId(), channel);
						// 如何第三方系统支付成功，则更新用户支付记录流水，并插入一条流水，多条资金划转，解冻用户的账户金额
						Map<String, Object> resultMap = this.buildWithDrawTradeFlowAndTransfers(finPaymentRecord,
								transMoney.add(finPaymentRecord.getCommission() == null ? BigDecimal.ZERO
										: finPaymentRecord.getCommission()),
								TRADE_TYPE_WITHDRAW_LOAN,
								TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
										FundtransferSmallTypeStateEnum.FROZEN),
								ALREADY_PAYMENT);
						// 更新支付记录表状态为已划转
						this.finPaymentRecordService
								.updateByFlowId((FinPaymentRecord) resultMap.get("finPaymentRecord"));
						// 生成一条提现放款的交易流水
						FinTradeFlow finTradeFlow = (FinTradeFlow) resultMap.get("finTradeFlow");
						// 生成多条资金划转
						List<FinFundtransfer> transferList = (List<FinFundtransfer>) resultMap.get("transferList");
						// 更新用户账户生成一条流水多笔资金划转
						ResponseEntity<?> payResult = this.finConsumptionService
								.updateAccountInsertTradeAndTransfer(finTradeFlow, transferList);
						if (payResult.getResStatus() == Constants.ERROR) {
							logger.error("提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 提现异步通知,更新用户账户生成一条流水多笔资金划转失败: {}",
									finPaymentRecord.getRegUserId(), transFlowId, channel, payResult.getResMsg());
							throw new BusinessException(payResult.getResMsg().toString());
						}
						sendRushSms(regUser, finPaymentRecord, transMoney);
					} else if (PAYMENT_THIRD_BANK_SYSTEM_STATE_FAIL == payState
							|| PAYMENT_THIRD_BANK_SYSTEM_STATE_REFUND == payState) {
						// 如何第三方系统支付失败，或已退款，则更新平台账户信息,更新支付表记录状态，将冻结金额解冻到可用余额中，生成一条流水，一条资金划转
						logger.info(
								"提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 提现异步通知,第三方支付系统返回状态为退款或失败,则资金回退,更新平台账户及流水信息",
								finPaymentRecord.getRegUserId(), transFlowId, channel);
						// 更新支付记录表状态为划转失败状态
						FinPaymentRecord newFinPaymentRecord = new FinPaymentRecord();
						newFinPaymentRecord.setFlowId(transFlowId);
						newFinPaymentRecord.setState(TRANSIT_FAIL);
						this.finPaymentRecordService.updateByFlowId(newFinPaymentRecord);
						// 生成一条交易流水
						FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(finPaymentRecord.getRegUserId(),
								transFlowId,
								transMoney.add(finPaymentRecord.getCommission() == null ? BigDecimal.ZERO
										: finPaymentRecord.getCommission()),
								TRADE_TYPE_WITHDRAW_LOAN,
								PlatformSourceEnums.typeByValue(finPaymentRecord.getTradeSource()));
						List<FinFundtransfer> transferList = new ArrayList<FinFundtransfer>();
						// 生成一条资金划转
						FinFundtransfer fundtransfer = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
								finPaymentRecord.getRegUserId(), null,
								transMoney.add(finPaymentRecord.getCommission() == null ? BigDecimal.ZERO
										: finPaymentRecord.getCommission()),
								TRANSFER_SUB_CODE_THAW);
						transferList.add(fundtransfer);
						ResponseEntity<?> resultRes = this.finConsumptionService
								.updateAccountInsertTradeAndTransfer(finTradeFlow, transferList);
						if (resultRes.getResStatus() == Constants.ERROR) {
							logger.error("提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 提现异步通知失败: {}",
									finPaymentRecord.getRegUserId(), transFlowId, channel, resultRes.getResMsg());
							throw new BusinessException(resultRes.getResMsg().toString());
						}
					}
				} else if (ALREADY_PAYMENT.equals(finPaymentRecord.getState())
						&& PAYMENT_THIRD_BANK_SYSTEM_STATE_SUCCESS == payState) {
					logger.error("提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 提现异步通知,重复收到请求，则不处理",
							finPaymentRecord.getRegUserId(), transFlowId, channel);
					return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_REPEAT, "此订单重复请求!");
				} else {
					logger.error("提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 第三方提现状态: {}, 提现异步通知,状态异常，则不处理",
							finPaymentRecord.getRegUserId(), transFlowId, channel, payState);
					return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_REPEAT, "此订单状态异常!");
				}
			}
			return new ResponseEntity<>(PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_SUCESS, "订单处理成功!");
		} catch (Exception e) {
			logger.error("提现异步通知, 用户标识: {}, 提现流水标识: {}, 渠道标识: {}, 提现异步通知,异步通知失败信息: ", finPaymentRecord.getRegUserId(),
					transFlowId, channel, e);
			throw new GeneralException(
					PAYMENT_PLATFORM_SYSTEM_WITHDRAW_STATE_FAIL + "|订单处理异常:" + CommonUtils.printStackTraceToString(e));
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	/***
	 * @Description :提现异步通知成功后发送站内信、短信
	 * @Method_Name : sendRushSms;
	 * @param regUser
	 *            资金划转subCode
	 * @param finPaymentRecord
	 *            支付记录对象
	 * @param transMoney
	 *            交易金额
	 * @return
	 * @return ;
	 * @Creation Date : 2018-05=25 17:21:;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public void sendRushSms(RegUser regUser, FinPaymentRecord finPaymentRecord, BigDecimal transMoney) {
		logger.info("提现异步发送短信，站内信 start. ");
		String msg = "";
		String msgTitle = "";
		if (finPaymentRecord.getCouponDetailId() != null) {// 没有使用优惠券
			msg = SmsMsgTemplate.MSG_PAYMENT_TX_TICKET_SUCCESS.getMsg();
			msgTitle = SmsMsgTemplate.MSG_PAYMENT_TX_TICKET_SUCCESS.getTitle();
		} else {
			msg = SmsMsgTemplate.MSG_PAYMENT_TX_SUCCESS.getMsg();
			msgTitle = SmsMsgTemplate.MSG_PAYMENT_TX_SUCCESS.getTitle();
		}
		try {
			SmsSendUtil.sendSmsMsgToQueue(
					new SmsWebMsg(regUser.getId(), msgTitle, msg, SmsConstants.SMS_TYPE_NOTICE,
							new String[] { DateUtils.format(finPaymentRecord.getCreateTime(), "yyyy-MM-dd HH:mm"),
									String.valueOf(transMoney) }),
					new SmsTelMsg(regUser.getId(), regUserService.findRegUserById(regUser.getId()).getLogin(), msg,
							SmsConstants.SMS_TYPE_NOTICE,
							new String[] { DateUtils.format(finPaymentRecord.getCreateTime(), "yyyy-MM-dd HH:mm"),
									String.valueOf(transMoney) }));
		} catch (Exception e) {
			logger.error("提现成功发送短信、站内信 异常、sendRushSms。message:{}", e);
		}
	}

	/***
	 * @Description :组装更新提现相关的支付记录，和新生成的交易流水及资金划转信息
	 * @Method_Name : buildWithDrawTradeFlowAndTransfers;
	 * @param finPaymentRecord
	 *            支付记录对象
	 * @param transMoney
	 *            交易金额
	 * @param tradeType
	 *            交易类型
	 * @param transferSubCode
	 *            资金划转subCode
	 * @param payState
	 *            支付记录状态
	 * @return
	 * @return : Map<String,Object>;
	 * @Creation Date : 2017年12月13日 下午4:42:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private Map<String, Object> buildWithDrawTradeFlowAndTransfers(FinPaymentRecord finPaymentRecord,
			BigDecimal transMoney, Integer tradeType, Integer transferSubCode, Integer payState) {
		logger.info(
				"方法: buildWithDrawTradeFlowAndTransfers, 组装更新提现相关的支付记录,和新生成的交易流水及资金划转信息, 入参: finPaymentRecord: {}, transMoney: {}, tradeType: {}, transferSubCode: {}, payState: {}",
				finPaymentRecord.toString(), transMoney, tradeType, transferSubCode, payState);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		// 更新支付记录表状态
		FinPaymentRecord newFinPaymentRecord = new FinPaymentRecord();
		newFinPaymentRecord.setFlowId(finPaymentRecord.getFlowId());
		newFinPaymentRecord.setState(payState);
		// 如果提现成功，则 生成一条提现放款的交易流水,如果是提现冲正，则生成一条冲正的交易流水
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(finPaymentRecord.getRegUserId(),
				finPaymentRecord.getId(), transMoney, tradeType,
				PlatformSourceEnums.typeByValue(finPaymentRecord.getTradeSource()));
		// 如果提现成功，则 生成一条提现放款的资金划转流水，如果是提现冲正，则生成一条冲正的资金划转流水
		List<FinFundtransfer> transferList = new ArrayList<FinFundtransfer>();
		// 如果是提现成功，已划转状态， 并且没有使用提现券
		if (payState == ALREADY_PAYMENT) {
			// 用户支出冻结
			FinFundtransfer fundtransfer = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
					finPaymentRecord.getRegUserId(), null, transMoney, transferSubCode);
			// 如果用户没有使用提现券,则收取手续费
			if (finPaymentRecord.getCouponDetailId() == null) {
				// 平台收入提现手续费
				FinFundtransfer platFundtransferCharge = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
						UserConstants.PLATFORM_ACCOUNT_ID, finPaymentRecord.getRegUserId(),
						finPaymentRecord.getCommission(), TradeTransferConstants.getFundTransferSubCodeByType(
								FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.CHARGE));
				transferList.add(platFundtransferCharge);
			}
			transferList.add(fundtransfer);
			resultMap.put("finTradeFlow", finTradeFlow);
		}
		// 如果是冲正交易，并且用户没有使用提现券
		if (payState == CORRECT_MONEY) {
			// 用户收入冲正
			FinFundtransfer userFundtransferCharge = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
					finPaymentRecord.getRegUserId(), UserConstants.PLATFORM_ACCOUNT_ID, transMoney, transferSubCode);
			// 如果没有提现券
			if (finPaymentRecord.getCouponDetailId() == null) {
				// 平台支出提现手续费
				FinFundtransfer platFundtransferCharge = FinTFUtil.initFinFundtransfer(finTradeFlow.getFlowId(),
						UserConstants.PLATFORM_ACCOUNT_ID, finPaymentRecord.getRegUserId(),
						finPaymentRecord.getCommission(), TradeTransferConstants.getFundTransferSubCodeByType(
								FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.CHARGE));
				transferList.add(platFundtransferCharge);
			}
			transferList.add(userFundtransferCharge);
			resultMap.put("finTradeFlow", finTradeFlow);
		}
		resultMap.put("finPaymentRecord", newFinPaymentRecord);
		resultMap.put("transferList", transferList);
		return resultMap;
	}

	@Override
	public ResponseEntity<?> lianlianRechargeSyncNotifyUrl(String reqStr) {
		logger.info("方法: lianlianRechargeSyncNotifyUrl, 连连充值同步回调操作, 入参: reqStr: {}", reqStr);
		// 进行签名校验
		FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.LianLian.getChannelKey(), PayStyleEnum.RZ.getType());
		if (payConfig == null) {
			logger.error("连连充值同步回调操作, 获取支付配置信息异常！");
			return new ResponseEntity<String>(ERROR, "获取支付配置信息异常！");
		}
		boolean sign = PaymentUtil.checkSign(reqStr, payConfig.getPublicKey(), payConfig.getPayMd5Key());
		if (!sign) {
			logger.error("连连充值同步回调操作, 支付同步通知验签失败！");
			return new ResponseEntity<String>(ERROR, "支付同步通知验签失败！");
		}
		// 解析同步通知对象
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		String respCode = payDataBean.getResult_pay();// 支付结果
		String flowId = payDataBean.getNo_order(); // 联联同步回传平台订单号
		logger.info("连连充值同步回调操作, 交易流水ID: {}, 同步通知对象: {}", flowId, payDataBean.toString());
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("连连充值同步回调操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "交易流水号不存在!");
		}
		logger.info("连连充值同步回调操作, 用户标识: {}, 通过交易流水ID, 查询到的平台订单: {}", finPaymentRecord.getRegUserId(),
				finPaymentRecord.toString());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					if (StringUtils.isBlank(payDataBean.getMoney_order())
							|| !CompareUtil.eq(finPaymentRecord.getTransMoney(),
									new BigDecimal(payDataBean.getMoney_order()))
							|| CompareUtil.lteZero(new BigDecimal(payDataBean.getMoney_order()))) {
						logger.error("连连充值同步回调操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
								flowId, "交易金额被篡改,支付失败!");
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					// 连连支付成功
					if (LIAN_SUCESS_SIGN.equalsIgnoreCase(respCode)) {
						return new ResponseEntity<>(SUCCESS, "支付成功!");
					} else {
						logger.error("连连充值同步回调操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
								flowId, "支付失败!");
						return new ResponseEntity<>(ERROR, "支付失败!");
					}
				} else {
					logger.error("连连充值同步回调操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "订单号已经存在,不允许重复支付！");
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("连连充值同步回调操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(), flowId,
						"当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("连连充值同步回调操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: ", finPaymentRecord.getRegUserId(), flowId, e);
			return new ResponseEntity<>(ERROR, "支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	@Override
	public ResponseEntity<?> baofuAsyncNotice(String reqStr, SystemTypeEnums systemTypeEnums,
			PayChannelEnum payChannelEnum) {
		logger.info("方法: baofuAsyncNotice, 宝付提现异步回调操作, 入参: reqStr: {},systemTypeEnums{},{}", reqStr, systemTypeEnums,
				payChannelEnum);
		ResponseEntity<?> resResult = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resMap = new HashMap<String, Object>();
		String tradeFlowNo = "";// 支付流水ID
		try {
			// 查询宝付提现支付配置信息
			FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(systemTypeEnums.getType(),
					PlatformSourceEnums.PC.getType(), payChannelEnum.getChannelKey(), PayStyleEnum.WITHDRAW.getType());
			if (payConfig == null) {
				logger.error("宝付提现异步通知, 获取支付配置信息为空！");
				return new ResponseEntity<>(ERROR, "支付配置信息为空！");
			}
			// 解析提现异步通知的报文
			String resJson = RSAUtil.decryptByPubCerFile(reqStr, payConfig.getPublicKey());
			resJson = Base64Util.decode(resJson);
			logger.info("宝付提现异步通知, 解析报文内容: {}", resJson);
			if (StringUtils.isBlank(resJson)) {
				logger.error("宝付提现异步通知, 解析异步通知报文失败！");
				return new ResponseEntity<>(ERROR, "解析异步通知报文失败！");
			}
			TransContent<TransRushResultAsyn> str2Obj = new TransContent<TransRushResultAsyn>("XML");
			str2Obj = (TransContent<TransRushResultAsyn>) str2Obj.str2Obj(resJson, TransRushResultAsyn.class);
			TransRushResultAsyn transInfo = str2Obj.getTrans_reqDatas().get(0);
			tradeFlowNo = transInfo.getTrans_no();// 商户订单号，即鸿坤金服流水号
			String transMoney = transInfo.getTrans_money();// 订单金额金额元
			// 交易状态 0：转账中；1：转账成功；-1：转账失败；2：转账退款
			int tradeState = Integer.parseInt(transInfo.getState());
			String retMsg = "";
			logger.info("宝付提现异步通知, 提现流水标识: {}, 宝付提现异步通知结果: {}, 通知描述: {}, 订单金额: {}", tradeFlowNo, tradeState, retMsg,
					transMoney);
			// 提现异步通知处理主逻缉，修改订单状态，生成流水及资金划转
			ResponseEntity<?> result = ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
					.withDrawAsyncNotice(tradeFlowNo, tradeState, new BigDecimal(transMoney), retMsg,
							payChannelEnum.getChannelKey());
			resMap.put("ret_msg", BaoFuPlatformPayStateEnum.thirdPaymentStateByPlatFormState(result.getResStatus()));
			resResult.setParams(resMap);
		} catch (Exception e) {
			resResult.setResStatus(ERROR);
			logger.error("宝付提现异步通知, 提现流水标识: {}, 宝付提现异步通知处理失败: ", tradeFlowNo, e);
			resMap.put("ret_msg", this.dealErrorMes(e.getMessage()).getRet_code());
			resResult.setParams(resMap);
		}
		return resResult;
	}

	@Override
	public ResponseEntity<?> baofuRechargeSyncNotifyUrl(String reqStr) {
		logger.info("方法: baofuRechargeSyncNotifyUrl, 宝付认证充值同步通知操作, 入参: reqStr: {}", reqStr);
		// 查询支付配置信息
		FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.BaoFu.getChannelKey(), PayStyleEnum.RZ.getType());
		if (payConfig == null) {
			logger.error("解析宝付认证充值报文, 获取支付配置信息异常！");
			return new ResponseEntity<>(ERROR, "获取支付配置信息异常！");
		}
		// 解析同步通知对象
		ResponseEntity<?> responseEntity = BaofuPayFactory.checkSignRechargeRz(reqStr, payConfig);
		if (responseEntity.getResStatus() != SUCCESS) {
			logger.error("宝付认证充值同步通知操作, 解析报文异常: {}", responseEntity.getResMsg().toString());
			return new ResponseEntity<>(ERROR, "支付失败!");
		}
		// 获取宝付认证充值同步返回的数据信息
		Map<String, Object> resMap = responseEntity.getParams();
		String respCode = resMap.get("resp_code").toString();// 同步返回交易状态
		String resp_msg = resMap.get("resp_msg").toString();// 同步返回的交易描述
		String flowId = resMap.get("trans_id").toString();// 交易流水ID
		String orderMoney = resMap.get("succ_amt").toString();// 交易金额（元）
		logger.info("宝付认证充值同步通知操作, 交易流水ID: {}, 交易金额: {}, 交易状态: {}, 交易描述: {}", flowId, orderMoney, respCode, resp_msg);
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("宝付认证充值同步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "支付失败!");
		}
		logger.info("宝付认证充值同步通知操作, 通过交易流水ID,查询到的平台订单: {}", finPaymentRecord.toString());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
				// 判断如果平台订单状态是不是已划转，或划转失败的
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 判断交易金额是否被篡改
					if (StringUtils.isBlank(orderMoney)
							|| !CompareUtil.eq(finPaymentRecord.getTransMoney(), new BigDecimal(orderMoney))
							|| CompareUtil.lteZero(new BigDecimal(orderMoney))) {
						logger.error("宝付认证充值同步通知操作, 交易流水ID: {}, 交易金额被篡改!", flowId);
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					// 宝付支付成功
					if (LIAN_SUCCESS_CODE.equalsIgnoreCase(respCode)) {
						return new ResponseEntity<>(SUCCESS, "支付成功!");
					} else if (BAOFU_RZ_NOTICE_STATE_NO_OPEN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_OVERTIME.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_PROCESSING.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_UNKNOWN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_RESULT_UNKNOWN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_PAYING.equals(respCode)) {
						// 宝付返回的状态为中间状态(就是不能确定是否支付成功)，则通知用户稍候查询充值结果
						return new ResponseEntity<>(SUCCESS, "系统处理中，请稍候查询充值结果！");
					} else {
						// 宝付返回的其他状态，都认为支付失败
						logger.error("宝付认证充值同步通知操作, 交易流水ID: {}, 支付失败!", flowId);
						return new ResponseEntity<>(ERROR, "支付失败！");
					}
				} else {
					// 如果平台支付状态为成功，则认为重复支付
					logger.error("宝付认证充值同步通知操作, 交易流水ID: {}, 该订单已经支付，不允许重复支付!", flowId);
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("宝付认证充值同步通知操作, 交易流水ID: {}, 当前支付渠道拥挤，请稍候再试！", flowId);
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("宝付认证充值同步通知操作, 支付记录标识: {}, 连连同步通知异常: ", finPaymentRecord.getFlowId(), e);
			return new ResponseEntity<>(ERROR, "支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}
	
	@Override
	public ResponseEntity<?> baofuRechargeAsyncNotifyUrl(String reqStr, Integer incomeType) {
		logger.info("方法: baofuRechargeAsyncNotifyUrl, 宝付认证充值异步回调操作, 入参: reqStr: {}, incomeType: {}", reqStr,
				incomeType);
		String flowId = "";// 交易流水ID
		String orderMoney = "";// 交易金额
		String respCode = "";// 异步返回交易状态
		String resp_msg = "";// 异步返回的交易描述
		// 测试环境测试用
//		if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
//			String str[] = reqStr.split("[|]");
//			respCode = str[0];// 异步返回交易状态
//			resp_msg = str[1];// 异步返回的交易描述
//			flowId = str[2];// 交易流水ID
//			orderMoney = str[3];// 交易金额
//		} else {
			// 查询支付配置信息
			FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.BaoFu.getChannelKey(), PayStyleEnum.RZ.getType());
			if (payConfig == null) {
				logger.error("解析宝付认证充值报文, 获取支付配置信息异常！");
				return new ResponseEntity<>(ERROR, "获取支付配置信息异常！");
			}
			// 解析异步通知对象
			ResponseEntity<?> responseEntity = BaofuPayFactory.checkSignRechargeRz(reqStr, payConfig);
			if (responseEntity.getResStatus()  == Constants.ERROR) {
				logger.error("宝付认证充值异步通知操作, 解析报文异常: {}", responseEntity.getResMsg().toString());
				return new ResponseEntity<>(ERROR, "解析报文失败!");
			}
			Map<String, Object> resMap = responseEntity.getParams();
			respCode = resMap.get("resp_code").toString();// 异步返回交易状态
			resp_msg = resMap.get("resp_msg").toString();// 异步返回的交易描述
			flowId = resMap.get("trans_id").toString();// 交易流水ID
			orderMoney = resMap.get("succ_amt").toString();// 交易金额
//		}
		logger.info("宝付认证充值异步通知操作, 交易流水ID: {}, 交易金额: {}, 交易状态: {}, 交易描述: {}", flowId, orderMoney, respCode, resp_msg);
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("宝付认证充值异步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "通过交易流水查询平台的订单不存在！");
		}
		logger.info("宝付认证充值异步通知操作, 通过交易流水ID, 查询到的平台订单: {}", finPaymentRecord.toString());
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TRANSIT_FAIL);
		paymentRecord.setFlowId(finPaymentRecord.getFlowId());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
				// 如果平台的状态不是已划转或划转失败，则根据第三方的状态进行判断，作充值处理
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 判断交易金额是否正确
					if (StringUtils.isBlank(orderMoney)
							|| !CompareUtil.eq(finPaymentRecord.getTransMoney(), new BigDecimal(orderMoney))
							|| CompareUtil.lteZero(new BigDecimal(orderMoney))) {
						logger.error("宝付认证充值异步通知操作,  用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					// 宝付返回的异步通知结果为支付成功
					if (BAOFU_SUCCESS_CODE.equalsIgnoreCase(respCode)) {
						// 更新支付记录，更新账户，生成交易流水及资金划转
						ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
								.dealRechargeNotice(finPaymentRecord, null, null, incomeType);
					} else if (BAOFU_RZ_NOTICE_STATE_NO_OPEN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_OVERTIME.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_PROCESSING.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_UNKNOWN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_RESULT_UNKNOWN.equals(respCode)
							|| BAOFU_RZ_NOTICE_STATE_PAYING.equals(respCode)) {
						// 宝付返回的状态为中间状态(就是不能确定是否支付成功)，则充值结果为划转中，后续进行查证
						FinPaymentRecord payRecord = new FinPaymentRecord();
						payRecord.setState(BANK_TRANSFER);
						payRecord.setFlowId(finPaymentRecord.getFlowId());
						finPaymentRecordService.updateByFlowId(payRecord);
						return new ResponseEntity<>(SUCCESS, "系统处理中，请稍候查询充值结果！");
					} else {
						logger.error("宝付认证充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
								flowId, resp_msg);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						// 宝付返回的其他状态，都认为支付失败
						return new ResponseEntity<>(ERROR, "支付失败:" + resp_msg);
					}
				} else {
					logger.error("宝付认证充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("宝付认证充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(), flowId,
						"当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("宝付认证充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: ", finPaymentRecord.getRegUserId(),
					finPaymentRecord.getFlowId(), e);
			finPaymentRecordService.updateByFlowId(paymentRecord);
			throw new GeneralException("支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(SUCCESS, "充值成功！");
	}

	@Override
	public ResponseEntity<?> baofuAgreePayAsyncNotifyUrl(Map<String, String> map, Integer incomeType) {
		logger.info("方法： baofuAgreePayAsyncNotifyUrl,宝付协议支付充值异步通知操作, 入参 map：{}, incomeType: {}", map, incomeType);
		String respCode = map.get("biz_resp_code").toString();// 异步返回交易状态
		String resp_msg = map.get("biz_resp_msg").toString();// 异步返回的交易描述
		String flowId = map.get("trans_id").toString();// 交易流水ID
		String transMoney = map.get("succ_amt").toString();// 交易金额
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("宝付协议支付充值异步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "通过交易流水查询平台的订单不存在！");
		}
		logger.info("宝付协议支付充值异步通知操作, 通过交易流水ID, 查询到的平台订单: {}", finPaymentRecord.toString());
		// 调用验签方法
		// ResponseEntity<?> checkSign =
		// BaofuProtocolPayFactory.checkCallBackSing(map, payConfig);
		if (finPaymentRecord.getState() == TradeStateConstants.ALREADY_PAYMENT
				|| finPaymentRecord.getState() == TradeStateConstants.TRANSIT_FAIL) {
			logger.error("宝付协议支付充值异步通知操作,该订单已处理, 交易流水ID: {}, 该订单已处理！", flowId);
			return new ResponseEntity<>(ERROR, "该订单已处理！");
		}

		String orderMoney = "0";
		if (!StringUtils.isBlank(transMoney)) {
			orderMoney = new BigDecimal(transMoney).divide(new BigDecimal("100")).toString();
		}
		// 查找协议支付通道配置
		FinPayConfig finPayConfig = this.finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.fromChannelCode(finPaymentRecord.getPayChannel()),
				PayStyleEnum.RZ.getType());

		ResponseEntity<?> checkSignResult = BaofuProtocolPayFactory.checkCallBackSing(map, finPayConfig);
		if (checkSignResult.getResStatus() == ERROR) {
			logger.error("宝付协议支付充值异步通知操作,签名未通过, 交易流水ID: {}", flowId);
			return new ResponseEntity<>(ERROR, "签名没有通过！");
		}
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TRANSIT_FAIL);
		paymentRecord.setFlowId(finPaymentRecord.getFlowId());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
				// 如果平台的状态不是已划转或划转失败，则根据第三方的状态进行判断，作充值处理
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 判断交易金额是否正确
					if (StringUtils.isBlank(orderMoney)
							|| !CompareUtil.eq(finPaymentRecord.getTransMoney(), new BigDecimal(orderMoney))
							|| CompareUtil.lteZero(new BigDecimal(orderMoney))) {
						logger.error("宝付协议支付充值异步通知操作,  用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					// 宝付协议支付返回异步通知结果为支付成功
					if (BAOFU_PROTOCAL_CODE_SUCCESS.equalsIgnoreCase(respCode)) {
						// 更新支付记录，更新账户，生成交易流水及资金划转
						ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
								.dealRechargeNotice(finPaymentRecord, null, null, incomeType);
					} else {
						logger.error("宝付协议支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}",
								finPaymentRecord.getRegUserId(), flowId, resp_msg);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						// 宝付返回的其他状态，都认为支付失败
						return new ResponseEntity<>(ERROR, "支付失败:" + resp_msg);
					}
				} else {
					logger.error("宝付协议支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("宝付协议支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
						flowId, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("宝付协议支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: ", finPaymentRecord.getRegUserId(),
					finPaymentRecord.getFlowId(), e);
			// finPaymentRecordService.updateByFlowId(paymentRecord);
			throw new GeneralException("支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(SUCCESS, "充值成功！");
	}

	/***
	 * @Description : 发送站内信，短信，首次充值增送成长值
	 * @Method_Name : dealSmsMsgAndVipGrow;
	 * @param regUserId
	 *            用户ID
	 * @param trannsMoney
	 *            交易金额
	 * @param userableMoney
	 *            可用余额
	 * @param incomeType
	 *            充值类型
	 * @return : void;
	 * @Creation Date : 2018年4月7日 下午12:50:59;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void dealSmsMsgAndVipGrow(Integer regUserId, BigDecimal trannsMoney, BigDecimal userableMoney,
			int incomeType) {
		try {
			// 如果是正常充值，则发送站内信，短信
			if (incomeType == INCOME_TYPE_COMMON) {
				// 发送站内信，短信
				String msg = SmsMsgTemplate.MSG_PAYMENT_RECHARGE_SUCCESS.getMsg();
				SmsSendUtil.sendSmsMsgToQueue(
						new SmsWebMsg(regUserId, SmsMsgTemplate.MSG_PAYMENT_RECHARGE_SUCCESS.getTitle(), msg,
								SmsConstants.SMS_TYPE_NOTICE, new String[] { String.valueOf(trannsMoney) }),
						new SmsTelMsg(regUserId, regUserService.findRegUserById(regUserId).getLogin(), msg,
								SmsConstants.SMS_TYPE_NOTICE, new String[] { String.valueOf(trannsMoney) }));
			}
			// 首次充值赠送成长值
			VasVipGrowRecordMqVO recordMqVO = new VasVipGrowRecordMqVO();
			recordMqVO.setUserId(regUserId);
			recordMqVO.setGrowType(VasVipConstants.VAS_VIP_GROW_TYPE_FIRST_RECHARGE);
			VipGrowRecordUtil.sendVipGrowRecordToQueue(recordMqVO);
		} catch (Exception e) {
			logger.error(
					"发送站内信短信,首次充值增送成长值, 用户标识: {}, 入参: regUserId: {}, trannsMoney: {}, userableMoney: {}, incomeType: {}, 发送异常信息: ",
					regUserId, trannsMoney, userableMoney, incomeType, e);
		}
	}

	@Override
	public ResponseEntity<?> baofuWyRechargePageNotice(Map<String, String> resultMap, String md5Sign) {
		logger.info("方法: baofuWyRechargePageNotice, 宝付网银充值同步通知, 入参: resultMap: {}, md5Sign: {}", resultMap.toString(),
				md5Sign);
		// 数据验签
		ResponseEntity<?> responseEntity = this.validateResData(resultMap, md5Sign);
		if (responseEntity.getResStatus() == ERROR) {
			logger.error("宝付网银充值同步通知, 网银同步通知失败: {}", responseEntity.getResMsg().toString());
			return responseEntity;
		}
		String flowId = resultMap.get("TransID=");// 交易流水号
		String factMoney = resultMap.get("FactMoney=");// 交易金额
		String transState = resultMap.get("Result=");// 交易状态
		String transMsg = resultMap.get("ResultDesc=");// 交易描述
		logger.info("宝付网银充值同步通知操作, 解析同步数据, flowId: {}, factMoney: {}, transState: {}, transMsg: {}", flowId, factMoney,
				transState, transMsg);
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("宝付网银充值同步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "交易流水号不存在!");
		}
		logger.info("宝付网银充值同步通知操作, 通过交易流水ID,查询到的平台订单: {}", finPaymentRecord.toString());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					if (StringUtils.isBlank(factMoney) || !CompareUtil.eq(finPaymentRecord.getTransMoney(),
							new BigDecimal(factMoney).divide(new BigDecimal(100)))) {
						logger.error("宝付网银充值同步通知操作, 用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						return new ResponseEntity<>(ERROR, "交易金额被篡改!");
					}
					// 宝付支付成功
					if (BAOFU_WY_SUCCESS.equalsIgnoreCase(transState)) {
						return new ResponseEntity<>(SUCCESS, "支付成功!");
					} else {
						logger.error("宝付网银充值同步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}",
								finPaymentRecord.getRegUserId(), flowId, transMsg);
						// 宝付返回的其他状态，都认为支付失败
						return new ResponseEntity<>(ERROR, "支付失败!");
					}
				} else {
					logger.error("宝付网银充值同步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("宝付网银充值同步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}", finPaymentRecord.getRegUserId(),
						flowId, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("宝付网银充值同步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: ", finPaymentRecord.getRegUserId(), flowId, e);
			return new ResponseEntity<>(ERROR, "支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	@Override
	public ResponseEntity<?> baofuWyRechargeAsyncNotifyUrl(Map<String, String> resultMap, String md5Sign,
			Integer incomeType) {
		// 数据验签
		ResponseEntity<?> responseEntity = this.validateResData(resultMap, md5Sign);
		if (responseEntity.getResStatus() == ERROR) {
			logger.error("宝付网银充值异步通知操作, 网银异步通知处理失败: {}", responseEntity.getResMsg().toString());
			return responseEntity;
		}
		String flowId = resultMap.get("TransID=");// 交易流水号
		String factMoney = resultMap.get("FactMoney=");// 交易金额
		String transState = resultMap.get("Result=");// 交易状态
		String transMsg = resultMap.get("ResultDesc=");// 交易描述
		logger.info("宝付网银充值异步通知操作, 解析同步数据, flowId: {}, factMoney: {}, transState: {}, transMsg: {}", flowId, factMoney,
				transState, transMsg);
		// 根据连连传回的订单号，查询平台是否存在此笔订单
		FinPaymentRecord finPaymentRecord = finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if (finPaymentRecord == null) {
			logger.error("宝付网银充值异步通知操作, 交易流水ID: {}, 通过交易流水查询平台的订单不存在！", flowId);
			return new ResponseEntity<>(ERROR, "交易流水号不存在!");
		}
		logger.info("宝付网银充值异步通知操作, 用户标识: {}, 通过交易流水ID,查询到的平台订单: {}", finPaymentRecord.getRegUserId(),
				finPaymentRecord.toString());
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TRANSIT_FAIL);
		paymentRecord.setFlowId(finPaymentRecord.getFlowId());
		JedisClusterLock jedisLock = new JedisClusterLock();
		boolean resultFlag = false;
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					if (StringUtils.isBlank(factMoney) || !CompareUtil.eq(finPaymentRecord.getTransMoney(),
							new BigDecimal(factMoney).divide(new BigDecimal(100)))) {
						logger.error("宝付网银充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						// 更新支付记录流水状态为失败
						finPaymentRecordService.updateByFlowId(paymentRecord);
						return new ResponseEntity<>(ERROR, "交易金额被篡改!");
					}
					// 宝付支付成功
					if (BAOFU_WY_SUCCESS.equalsIgnoreCase(transState)) {
						// 更新支付记录，更新账户，生成交易流水及资金划转,发送短信，站内信，首次充值增送成长值
						ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
								.dealRechargeNotice(finPaymentRecord, null, null, incomeType);
						return new ResponseEntity<>(SUCCESS, "支付成功!");
					} else {
						logger.error("宝付网银充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}",
								finPaymentRecord.getRegUserId(), flowId, transMsg);
						// 更新支付记录流水状态为失败
						finPaymentRecordService.updateByFlowId(paymentRecord);
						// 宝付返回的其他状态，都认为支付失败
						return new ResponseEntity<>(ERROR, "支付失败:" + transMsg);
					}
				} else {
					logger.error("宝付网银充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("宝付网银充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: {}", finPaymentRecord.getRegUserId(),
						flowId, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("宝付网银充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 网银同步通知失败: ", finPaymentRecord.getRegUserId(), flowId, e);
			// 更新支付记录流水状态为失败
			finPaymentRecordService.updateByFlowId(paymentRecord);
			return new ResponseEntity<>(ERROR, "支付失败！");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	/**
	 * @Description : 宝付网银充值，验签
	 * @Method_Name : validateResData;
	 * @param resultMap
	 * @param md5Sign
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年1月24日 下午6:32:35;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public ResponseEntity<?> validateResData(Map<String, String> resultMap, String md5Sign) {
		logger.info("方法 , validateResData 网银通知 解析参数, resultMap : {}" , resultMap);
		try {
			// 进行支付配置信息
			FinPayConfig payConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
					PlatformSourceEnums.PC.getType(), PayChannelEnum.BaoFu.getChannelKey(), PayStyleEnum.WY.getType());
			if (payConfig == null) {
				logger.error("宝付网银充值同步/异步通知操作, 获取支付配置信息异常！");
				return new ResponseEntity<>(ERROR, "获取支付配置信息异常！");
			}
			Map<String, String> dataMap = new LinkedHashMap<String, String>();
			dataMap.put("MemberID=", payConfig.getMerchantNo());
			dataMap.put("TerminalID=", payConfig.getTerminalId());
			dataMap.putAll(resultMap);
			StringBuffer sbMd5 = new StringBuffer();
			String MARK = "~|~";// 标记
			dataMap.forEach((payKey, payValue) -> {
				sbMd5.append(payKey + payValue + MARK);
			});
			String md5Key = payConfig.getPayMd5Key();
			String signature = RSAUtil.MD5(sbMd5.append(md5Key).toString());
			if (!signature.equals(md5Sign)) {
				logger.error("宝付网银充值同步/异步通知操作,  验签失败");
				return new ResponseEntity<>(ERROR, "验签失败！");
			}
			return new ResponseEntity<>(SUCCESS, "验签成功！");
		} catch (Exception e) {
			logger.error("宝付网银充值同步/异步通知操作,  验签失败: ", e);
			return new ResponseEntity<>(ERROR, "验签失败！");
		}
	}
	
	@Override
	public ResponseEntity<?> yeepayRzAsyncNotifyUrl(Map<String, String> paramsMap, Integer incomeType) {
		logger.info("易宝充值异步方法, yeepayRechargeAsyncNotifyUrl mehtod , 入参paramsMap：{},incomeType:{}",paramsMap,incomeType);
		FinPayConfig finPayConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.Yeepay.getChannelKey(), PayStyleEnum.RZ.getType());
		if(finPayConfig == null){
			logger.error("易宝异步通知支付配置找不到");
			return new ResponseEntity<>(Constants.ERROR,"支付配置为空");
		}
//		String data = paramsMap.get("data");
//		String encryptkey = paramsMap.get("encryptkey");
		
		ResponseEntity<?> checkSignResult = YeepayPayFactory.checkSignRechargeRz(paramsMap, finPayConfig);
		if (checkSignResult.getResStatus() == ERROR) {
			logger.error("宝付协议支付充值异步通知操作,签名未通过");
			return new ResponseEntity<>(ERROR, "签名没有通过！");
		}
		Map<String, Object> callbackResult = checkSignResult.getParams();
//		String yborderid=callbackResult.get("yborderid").toString();
		String flowId=callbackResult.get("requestno").toString();
		String status=callbackResult.get("status").toString();
		String amount=callbackResult.get("amount").toString();
//		String cardtop=callbackResult.get("cardtop").toString();
//		String cardlast=callbackResult.get("cardlast").toString();
//		String bankcode=callbackResult.get("bankcode").toString();
//		String errorcode=callbackResult.get("errorcode").toString();
		String errormsg=callbackResult.get("errormsg").toString();
		if(StringUtils.isBlank(flowId)){
			return new ResponseEntity<>(Constants.ERROR,"金额或者订单号参数为空");
		}
		BigDecimal orderMoney = BigDecimal.ZERO;
		if(!StringUtils.isBlank(amount)){
			orderMoney = new BigDecimal(amount);
		}
		FinPaymentRecord finPaymentRecord = this.finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if(finPaymentRecord == null ){
			return new ResponseEntity<>(Constants.ERROR,"没有此订单");
		}
		
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TRANSIT_FAIL);
		paymentRecord.setFlowId(finPaymentRecord.getFlowId());
		boolean resultFlag = false;
		JedisClusterLock jedisLock = new JedisClusterLock();
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = this.finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);//在redis里需要重新查一遍，保证状态正确
				// 如果平台的状态不是已划转或划转失败，则根据第三方的状态进行判断，作充值处理
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 判断交易金额是否正确
					if (!CompareUtil.eq(finPaymentRecord.getTransMoney(), orderMoney)
							|| CompareUtil.lteZero(orderMoney)) {
						logger.error("易宝支付充值异步通知操作,  用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					// 易宝支付返回异步通知结果为支付成功
					if (YEEPAY_RZ_SUCCESS_CODE.equalsIgnoreCase(status)) {
						// 更新支付记录，更新账户，生成交易流水及资金划转
						ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
								.dealRechargeNotice(finPaymentRecord, null, null, incomeType);
					} else {
						logger.error("易宝支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}",
								finPaymentRecord.getRegUserId(), flowId, errormsg);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						// 易宝返回的其他状态，都认为支付失败
						return new ResponseEntity<>(ERROR, "支付失败:" + errormsg);
					}
				} else {
					logger.error("易宝支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("易宝支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
						flowId, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("易宝认证支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: ", finPaymentRecord.getRegUserId(),
					finPaymentRecord.getFlowId(), e);
			throw new GeneralException("支付失败！");
		}finally{
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	
	@Override
	public ResponseEntity<?> yeepayWyAsyncNotifyUrl(Map<String, String> paramsMap) {
		logger.info("易宝网银充值异步通知处理方法, yeepayWyAsyncNotifyUrl mehtod , 入参paramsMap：{}",paramsMap);
		FinPayConfig finPayConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.Yeepay.getChannelKey(), PayStyleEnum.WY.getType());
		if(finPayConfig == null){
			logger.error("易宝网银异步通知支付配置找不到");
			return new ResponseEntity<>(Constants.ERROR,"易宝网银支付配置为空");
		}
		String p1_MerId = paramsMap.get("p1_MerId");
		String r0_Cmd = paramsMap.get("r0_Cmd");
		String r1_Code = paramsMap.get("r1_Code");
		String r2_TrxId = paramsMap.get("r2_TrxId");
		String r3_Amt = paramsMap.get("r3_Amt");
		String r4_Cur = paramsMap.get("r4_Cur");
		String r5_Pid = paramsMap.get("r5_Pid");
		String r6_Order = paramsMap.get("r6_Order");
		String r7_Uid = paramsMap.get("r7_Uid");
		String r8_MP = paramsMap.get("r8_MP");
		String r9_BType = paramsMap.get("r9_BType");
		String hmac_safe = paramsMap.get("hmac_safe");
		String hmac = paramsMap.get("hmac");
		String[] strArr			= new String[] {p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, 
				r4_Cur,r5_Pid,r6_Order,r7_Uid,r8_MP,r9_BType};
		String flowId=r6_Order;
		String amount=r3_Amt;
		String third_state=r1_Code;
		if(StringUtils.isBlank(flowId)||StringUtils.isBlank(amount)
				 ||StringUtils.isBlank(third_state)){
		 	logger.info("参数订单号、金额、订单状态三者都不能为空！");
		 	return new ResponseEntity<>(Constants.ERROR,"易宝网银回调参数缺失");
		 }
		ResponseEntity<?> checkResult =  YeepayPayFactory.checkHmacRechargeWy(strArr, hmac, hmac_safe, finPayConfig);
		if(checkResult.getResStatus() == Constants.ERROR){
			return new ResponseEntity<>(Constants.ERROR,checkResult.getResMsg());
		}
		
		BigDecimal orderMoney = BigDecimal.ZERO;
		if(!StringUtils.isBlank(amount)){
			orderMoney = new BigDecimal(amount);
		}
		FinPaymentRecord finPaymentRecord = this.finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);
		if(finPaymentRecord == null ){
			return new ResponseEntity<>(Constants.ERROR,"没有此订单");
		}
		FinPaymentRecord paymentRecord = new FinPaymentRecord();
		paymentRecord.setState(TRANSIT_FAIL);
		paymentRecord.setFlowId(finPaymentRecord.getFlowId());
		boolean resultFlag = false;
		JedisClusterLock jedisLock = new JedisClusterLock();
		String lockKey = "";// redis锁的KEY
		lockKey = LOCK_PREFFIX + FinPaymentRecord.class.getSimpleName() + finPaymentRecord.getFlowId();
		boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, Constants.LOCK_WAITTIME);
		try {
			if (result) {
				resultFlag = true;
				finPaymentRecord = this.finPaymentRecordService.findFinPaymentRecordByFlowId(flowId);//在redis里需要重新查一遍，保证状态正确
				// 如果平台的状态不是已划转或划转失败，则根据第三方的状态进行判断，作充值处理
				if (finPaymentRecord.getState() != TradeStateConstants.ALREADY_PAYMENT
						&& finPaymentRecord.getState() != TradeStateConstants.TRANSIT_FAIL) {
					// 判断交易金额是否正确
					if (!CompareUtil.eq(finPaymentRecord.getTransMoney(), orderMoney)
							|| CompareUtil.lteZero(orderMoney)) {
						logger.error("易宝网银支付充值异步通知操作,  用户标识: {}, 交易流水ID: {}, 交易金额被篡改!", finPaymentRecord.getRegUserId(),
								flowId);
						finPaymentRecordService.updateByFlowId(paymentRecord);
						return new ResponseEntity<>(ERROR, "交易金额被篡改,支付失败!");
					}
					//易宝网银 只会发 支付成功订单 异步通知
					// 更新支付记录，更新账户，生成交易流水及资金划转
					ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
							.dealRechargeNotice(finPaymentRecord, null, null, INCOME_TYPE_COMMON);
				} else {
					logger.error("易宝支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
							flowId, "该订单已经支付，不允许重复支付!");
					// 如果平台支付状态为成功，则认为重复支付
					return new ResponseEntity<>(ERROR, "该订单已经支付，不允许重复支付!");
				}
			} else {
				logger.error("易宝支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: {}", finPaymentRecord.getRegUserId(),
						flowId, "当前支付渠道拥挤，请稍候再试！");
				return new ResponseEntity<>(ERROR, "当前支付渠道拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error("易宝认证支付充值异步通知操作, 用户标识: {}, 交易流水ID: {}, 异步通知失败: ", finPaymentRecord.getRegUserId(),
					finPaymentRecord.getFlowId(), e);
			throw new GeneralException("支付失败！");
		}finally{
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	
	@Override
	public ResponseEntity<?> yeepayRushAsyncNotifyUrl(String paramStr) {
		logger.info("易宝网银充值异步通知处理方法, yeepayWyAsyncNotifyUrl mehtod , 入参paramStr：{}",paramStr);
		ResponseEntity<?> resResult = null ;
		Map<String, Object> resMap = new HashMap<String, Object>();
		FinPayConfig finPayConfig = finPayConfigService.findPayConfigInfo(SystemTypeEnums.HKJF.getType(),
				PlatformSourceEnums.PC.getType(), PayChannelEnum.Yeepay.getChannelKey(), PayStyleEnum.WITHDRAW.getType());
		if(finPayConfig == null){
			logger.error("易宝网银异步通知支付配置找不到");
			return new ResponseEntity<>(Constants.ERROR,"易宝网银支付配置为空");
		}
		
		ResponseEntity<?> checkResult =  YeepayPayFactory.checkSignRush(paramStr, finPayConfig);
		if(checkResult.getResStatus() == Constants.ERROR){
			return new ResponseEntity<>(Constants.ERROR,checkResult.getResMsg());
		}
		
		TransferSingleReq req = (TransferSingleReq)checkResult.getParams().get("transferSingleReq");
		com.cfca.util.pki.cipher.Session tempsession = (com.cfca.util.pki.cipher.Session)checkResult.getParams().get("tempsession");
		int state = 0;  // 交易状态 0：转账中；1：转账成功；-1：转账失败；2：转账退款
		if(YEEPAY_WITHDRAW_CODE_SUCCESS.equals(req.getStatus())){
			state = 1;
		}
		ResponseEntity<?> result = null;
		try {
			result = ApplicationContextUtils.getBean(FinPayMentNoticeFacadeImpl.class)
					.withDrawAsyncNotice(req.getOrder_Id(), state, new BigDecimal(req.getAmount()), "",
							PayChannelEnum.Yeepay.getChannelKey());
			logger.info("易宝提现账户处理结果status: {}", result.getResStatus());
			if(result != null && result.getResStatus() == Constants.SUCCESS){ //处理成功， 生成 成功 同步数据
				resResult = new ResponseEntity<>(Constants.SUCCESS);
				JKey jkey = KeyUtil.getPriKey(finPayConfig.getPayMd5Key(), finPayConfig.getTerminalId());
				String ALGORITHM = SignatureUtil.SHA1_RSA;
				X509Cert cert = CertUtil.getCert(finPayConfig.getPayMd5Key(), finPayConfig.getTerminalId());
				X509Cert[] cs=new X509Cert[1];
		    	cs[0]=cert;
				TransferSingleReq res = new TransferSingleReq(); //需要回显的数据
				res.setMer_Id(req.getMer_Id());
				res.setBatch_No(req.getBatch_No());
				res.setOrder_Id(req.getOrder_Id());
				res.setRet_Code(YEEPAY_RUSH_SUCCESS);
				StringBuffer str = new StringBuffer();
				str.append(res.getCmd()).append(res.getMer_Id()).append(res.getBatch_No())
				.append(res.getOrder_Id()).append(res.getRet_Code()).append(finPayConfig .getPrivateKey());
				SignatureUtil signUtil = new SignatureUtil();
		 		String hmac = Digest.hmacSign(str.toString());
		 		res.setHmac(new String(signUtil.p7SignMessage(true, hmac.getBytes(),ALGORITHM, jkey, cs, tempsession)));
				String resxml = XStreamUtil.beanToXml(res);
		 		logger.info("回写易宝数据："+resxml);
			 	resResult.addParam("returnStr", resxml);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.ERROR,"宝付提现处理失败");
		}
		
		return resResult;
	}
	
	/**
	 * @Description : 处理失败的给第三方响应的返回码及返回信息
	 * @Method_Name : dealErrorMes;
	 * @param message
	 * @return
	 * @return : RetBean;
	 * @Creation Date : 2018年2月2日 下午5:32:55;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private RetBean dealErrorMes(String message) {
		RetBean retBean = new RetBean();// 通知响应对象
		try {
			if (!StringUtils.isBlank(message)) {
				String[] mesStr = message.split("[|]");
				if (mesStr.length >= 2) {
					retBean.setRet_code(
							LianLianPlatformPayStateEnum.thirdPaymentStateByPlatFormState(Integer.parseInt(mesStr[0])));
					retBean.setRet_msg(mesStr[1]);
				} else {
					retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
					retBean.setRet_msg("处理失败");
				}
			} else {
				retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
				retBean.setRet_msg("处理失败");
			}
		} catch (Exception e) {
			logger.error("处理失败的给第三方响应的返回码及返回信息异常: ", e);
			retBean.setRet_code(LianLianPlatformPayStateEnum.PLATFAIL.getThirdPaymentState());
			retBean.setRet_msg("交易失败");
		}
		return retBean;
	}

	
}
