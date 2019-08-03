package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.hongkun.finance.payment.bfpayvo.TransContent;
import com.hongkun.finance.payment.bfpayvo.TransHead;
import com.hongkun.finance.payment.bfpayvo.TransResResult;
import com.hongkun.finance.payment.constant.BankConstants;
import com.hongkun.finance.payment.constant.PaymentConstants;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.dao.*;
import com.hongkun.finance.payment.enums.*;
import com.hongkun.finance.payment.factory.BaofuPayFactory;
import com.hongkun.finance.payment.factory.LianlianPayFactory;
import com.hongkun.finance.payment.factory.PayValidateFactory;
import com.hongkun.finance.payment.factory.YeepayPayFactory;
import com.hongkun.finance.payment.llpayvo.*;
import com.hongkun.finance.payment.model.*;
import com.hongkun.finance.payment.model.vo.RechargeCash;
import com.hongkun.finance.payment.model.vo.TransferVo;
import com.hongkun.finance.payment.model.vo.WithDrawCash;
import com.hongkun.finance.payment.security.Base64Util;
import com.hongkun.finance.payment.service.FinConsumptionService;
import com.hongkun.finance.payment.util.*;
import com.hongkun.finance.user.constants.UserConstants;
import com.hongkun.finance.user.utils.BaseUtil;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.enums.PlatformSourceEnums;
import com.yirun.framework.core.enums.SystemTypeEnums;
import com.yirun.framework.core.exception.BusinessException;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.*;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.redis.JedisClusterLock;
import org.apache.commons.lang3.StringUtils;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongkun.finance.payment.constant.PaymentConstants.*;
import static com.hongkun.finance.payment.constant.TradeStateConstants.ALREADY_PAYMENT;
import static com.hongkun.finance.payment.constant.TradeStateConstants.BANK_TRANSFER;
import static com.hongkun.finance.payment.constant.TradeStateConstants.TRANSIT_FAIL;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.*;
import static com.hongkun.finance.qdz.constant.QdzConstants.CREDITOR_FLAG_BUY;
import static com.yirun.framework.core.commons.Constants.*;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinConsumptionServiceImpl.
 *          java
 * @Class Name : FinConsumptionServiceImpl.java
 * @Description : 金融消费者实现类
 * @Author : yanbinghuang
 */
@Service
public class FinConsumptionServiceImpl implements FinConsumptionService {

	private static final Logger logger = LoggerFactory.getLogger(FinConsumptionServiceImpl.class);
	/**
	 * FinAccountDAO 账户表DAO
	 */
	@Autowired
	private FinAccountDao finAccountDao;
	/**
	 * FinTradeFlowDao 流水表DAO
	 */
	@Autowired
	private FinTradeFlowDao finTradeFlowDao;
	/**
	 * FinPaymentRecordDao 支付记录表DAO
	 */
	@Autowired
	private FinPaymentRecordDao finPaymentRecordDao;
	/**
	 * FinFundtransferDao 资金划转表DAO
	 */
	@Autowired
	private FinFundtransferDao finFundtransferDao;
	/**
	 * finBankCardDao 银行信息DAO
	 */
	@Autowired
	private FinBankCardDao finBankCardDao;
	/**
	 * finCityReferDao 城市DAO
	 */
	@Autowired
	private FinCityReferDao finCityReferDao;
	/**
	 * finChannelGrantDao 提现权限DAO
	 */
	@Autowired
	private FinChannelGrantDao finChannelGrantDao;
	/**
	 * finPayConfigDao 第三方配置 DAO
	 */
	@Autowired
	private FinPayConfigDao finPayConfigDao;

	@Autowired
	private FinBankReferDao finBankReferDao;
	/**
	 * FinBankCardBindingDAO
	 */
	@Autowired
	private FinBankCardBindingDao finBankCardBindingDao;


	@Override
	@Compensable(cancelMethod = "cashPayForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> cashPay(FinTradeFlow finTradeFlow, Integer transferSubCode) {
		logger.info("{}, 用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}", BaseUtil.getTccTryLogPrefix(),
				finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString());
		try {
			/**
			 * 1、校验数据合法性
			 */
			Map<String, String> checkResultMap = PayValidateFactory.checkDataInfo(finTradeFlow, transferSubCode);
			if (!SUCCESS_FLAG_NAME.equals(checkResultMap.get(SERVER_SUCCESS_FLAG))) {
				logger.error("用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}, 参数校验异常信息: {}",
						finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString(),
						checkResultMap.get(SERVER_FAIL_FLAG));
				return new ResponseEntity<>(Constants.ERROR, checkResultMap.get(SERVER_FAIL_FLAG));
			}
			/**
			 * 2 、根据资金划转的transferSubCode修改账户金额
			 */
			ResponseEntity<?> updateAccountResult = updateFinAccountBySubCode(finTradeFlow.getRegUserId(),
					transferSubCode, finTradeFlow.getTransMoney());
			if (updateAccountResult.getResStatus() == Constants.ERROR) {
				logger.error("用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}, 更新账户异常信息: {}",
						finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString(),
						updateAccountResult.getResMsg().toString());
				return updateAccountResult;
			}
			FinAccount finAccount = (FinAccount) updateAccountResult.getResMsg();
			/**
			 * 3、保存交易流水信息
			 */
			// 插入交易流水信息
			finTradeFlowDao.save(finTradeFlow);
			/**
			 * 4、增加资金划转记录信息
			 */
			// 组装资金划转对象
			FinFundtransfer finFundtransfer = buildFinFundtransferData(finAccount, null, finTradeFlow, transferSubCode,
					finTradeFlow.getTransMoney());
			logger.info("用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}, 用户账户现金消费生成的资金划转对象信息: {}",
					finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString(), finFundtransfer.toString());
			// 插入资金划转表
			finFundtransferDao.save(finFundtransfer);
			/**
			 * 5返回操作结果
			 */
			return new ResponseEntity<>(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("{}, 用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {},用户账户现金消费异常信息: ",
					BaseUtil.getTccTryLogPrefix(), finTradeFlow.getRegUserId(), transferSubCode,
					finTradeFlow.toString(), e);
			throw new GeneralException("用户账户现金消费异常:" + CommonUtils.printStackTraceToString(e));
		}
	}

	@Override
	@Compensable(cancelMethod = "transferPayCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> transferPay(TransferVo transferInfo) {
		logger.info("{}, 两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}", BaseUtil.getTccTryLogPrefix(),
				transferInfo != null ? transferInfo.getFromUserId() : "",
				transferInfo != null ? transferInfo.getToUserId() : "",
				transferInfo == null ? "" : transferInfo.toString());
		try {
			/**
			 * 1、校验数据合法性
			 */
			Map<String, String> resultCheckTransferMap = PayValidateFactory.checkTransferDataInfo(transferInfo);
			if (!SUCCESS_FLAG_NAME.equals(resultCheckTransferMap.get(SERVER_SUCCESS_FLAG))) {
				logger.error("两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 参数校验异常信息: {}",
						transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
						resultCheckTransferMap.get(SERVER_FAIL_FLAG));
				return new ResponseEntity<>(Constants.ERROR, resultCheckTransferMap.get(SERVER_FAIL_FLAG));
			}
			/**
			 * 2、根据资金划转subCode，更新账户信息
			 */
			// 更新发起人账户信息
			ResponseEntity<?> orgUpdateResult = updateFinAccountBySubCode(transferInfo.getFromUserId(),
					transferInfo.getTransferOutSubCode(), transferInfo.getTransMoney());
			if (orgUpdateResult.getResStatus() == Constants.ERROR) {
				logger.error("两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 更新发起人账户异常信息: {}",
						transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
						orgUpdateResult.getResMsg().toString());
				return orgUpdateResult;
			}
			FinAccount orgFinAccount = (FinAccount) orgUpdateResult.getResMsg();
			// 判断是否有积分抵扣现金消费，如果有，则接收方更改账户时，需要增加积分转现金的金额
			BigDecimal transMoney = transferInfo.getTransMoney();
			if (transferInfo.getPointPayFlag() == EXIST_POINT_PAY) {
				transMoney.add(transferInfo.getPointChangeMoney());
			}
			// 更新接收人账户信息
			ResponseEntity<?> recUpdateResult = updateFinAccountBySubCode(transferInfo.getToUserId(),
					transferInfo.getTransferInSubCode(), transMoney);
			if (recUpdateResult.getResStatus() == Constants.ERROR) {
				logger.error("两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 更新接收人账户异常信息: {}",
						transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
						recUpdateResult.getResMsg().toString());
				throw new BusinessException("更新接收人账户失败！");
			}
			FinAccount recFinAccount = (FinAccount) recUpdateResult.getResMsg();
			/**
			 * 3、生成转账交易流水
			 */
			FinTradeFlow finTradeFlow = createTradeFlowData(transferInfo);
			logger.info("两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 生成的交易流水信息finTradeFlow: {}",
					transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
					finTradeFlow.toString());
			finTradeFlowDao.save(finTradeFlow);
			/**
			 * 4、生成资金划转流水信息
			 */
			// 生成发起人资金划转对象
			FinFundtransfer orgFinFundtransfer = buildFinFundtransferData(orgFinAccount, recFinAccount.getRegUserId(),
					finTradeFlow, transferInfo.getTransferOutSubCode(), transferInfo.getTransMoney());
			logger.info(
					"两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 生成发起人的资金划转对象信息orgFinFundtransfer: {}",
					transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
					orgFinFundtransfer.toString());
			// 生成接收人资金划转对象
			FinFundtransfer recFinFundtransfer = buildFinFundtransferData(recFinAccount, orgFinAccount.getRegUserId(),
					finTradeFlow, transferInfo.getTransferInSubCode(), transMoney);
			logger.info(
					"两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 生成接收人的资金划转对象信息recFinFundtransfer: {}",
					transferInfo.getFromUserId(), transferInfo.getToUserId(), transferInfo.toString(),
					recFinFundtransfer.toString());
			List<FinFundtransfer> transferList = new ArrayList<FinFundtransfer>();
			transferList.add(orgFinFundtransfer);
			transferList.add(recFinFundtransfer);
			finFundtransferDao.insertBatch(FinFundtransfer.class, transferList);
			/**
			 * 5返回响应结果
			 */
			return new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
		} catch (Exception e) {
			logger.error("{}, 两账户之间进行转账, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 转账异常信息: ",
					BaseUtil.getTccTryLogPrefix(), transferInfo.getFromUserId(), transferInfo.getToUserId(),
					transferInfo.toString(), e);
			throw new GeneralException("两账户之间进行转账异常:" + CommonUtils.printStackTraceToString(e));
		}
	}

	/**
	 * @Description : TCC回滚方法 transferPay
	 * @Method_Name : transferPayCancel
	 * @param transferInfo
	 *            转账VO
	 * @return
	 * @return : ResponseEntity<?>
	 * @throws Exception
	 * @Creation Date : 2017年10月17日 下午1:54:14
	 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> transferPayCancel(TransferVo transferInfo) throws Exception {
		logger.info("tcc cancel transferPay, 两账户之间进行转账回滚方法, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}",
				transferInfo != null ? transferInfo.getFromUserId() : "",
				transferInfo != null ? transferInfo.getToUserId() : "",
				transferInfo == null ? "" : transferInfo.toString());
		/**
		 * 1、删除所有流水和资金划转
		 */
		FinTradeFlow contidion = createTradeFlowData(transferInfo);
		contidion.setFlowId(null);
		List<FinTradeFlow> finTradeFlows = finTradeFlowDao.findByCondition(contidion);
		if (CommonUtils.isNotEmpty(finTradeFlows)) {
			FinTradeFlow finFlow = finTradeFlows.get(0);
			if (finFlow != null) {
				finFundtransferDao.deleteByPflowId(finFlow.getFlowId());
				finTradeFlowDao.deleteByFlowId(finFlow.getFlowId());
			}
			/**
			 * 2、发起人更新账户金额,加钱
			 */
			FinAccount orgUpdateResult = new FinAccount();
			orgUpdateResult.setFreezeMoney(BigDecimal.ZERO);
			orgUpdateResult.setUseableMoney(BigDecimal.ZERO);
			orgUpdateResult.setNowMoney(BigDecimal.ZERO);
			orgUpdateResult.setRegUserId(transferInfo.getFromUserId());
			ResponseEntity<?> orgResResult = dealRollBackTransferAccountBySubcode(transferInfo.getTransferOutSubCode(),
					transferInfo.getTransMoney(), orgUpdateResult);
			if (orgResResult.getResStatus() == ERROR) {
				logger.info(
						"两账户之间进行转账回滚方法, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 根据资金划转SUBCODE初始化发起人的回滚用户账户金额异常: {}",
						transferInfo != null ? transferInfo.getFromUserId() : "",
						transferInfo != null ? transferInfo.getToUserId() : "",
						transferInfo == null ? "" : transferInfo.toString(), orgResResult.getResMsg().toString());
				throw new BusinessException("根据资金划转SUBCODE初始化发起人的回滚用户账户金额异常!");
			}
			ResponseEntity<?> orgAccountResult = updateFinAccount(orgUpdateResult);
			if (orgAccountResult.getResStatus() == Constants.ERROR) {
				logger.info("两账户之间进行转账回滚方法, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 更新发起人的账户金额异常: {}",
						transferInfo != null ? transferInfo.getFromUserId() : "",
						transferInfo != null ? transferInfo.getToUserId() : "",
						transferInfo == null ? "" : transferInfo.toString(), orgAccountResult.getResMsg().toString());
				throw new BusinessException("更新发起人的账户金额异常!");
			}
			/**
			 * 3、判断是否有积分消费，如果有则需要加上积分消费对应的金额
			 */
			BigDecimal transMoney = transferInfo.getTransMoney();
			if (transferInfo.getPointPayFlag() == EXIST_POINT_PAY) {
				transMoney.add(transferInfo.getPointChangeMoney());
			}
			/**
			 * 4、更新接收人账户金额,支出钱
			 */
			FinAccount recUpdateResult = new FinAccount();
			recUpdateResult.setFreezeMoney(BigDecimal.ZERO);
			recUpdateResult.setUseableMoney(BigDecimal.ZERO);
			recUpdateResult.setNowMoney(BigDecimal.ZERO);
			recUpdateResult.setRegUserId(transferInfo.getToUserId());
			ResponseEntity<?> resResult = dealRollBackTransferAccountBySubcode(transferInfo.getTransferInSubCode(),
					transMoney, recUpdateResult);
			if (resResult.getResStatus() == ERROR) {
				logger.info(
						"两账户之间进行转账回滚方法, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 根据资金划转SUBCODE初始化接收人的回滚用户账户金额异常: {}",
						transferInfo != null ? transferInfo.getFromUserId() : "",
						transferInfo != null ? transferInfo.getToUserId() : "",
						transferInfo == null ? "" : transferInfo.toString(), resResult.getResMsg().toString());
				throw new BusinessException("根据资金划转SUBCODE初始化接收人的回滚用户账户金额异常!");
			}
			ResponseEntity<?> recAccountResult = updateFinAccount(orgUpdateResult);
			if (recAccountResult.getResStatus() == Constants.ERROR) {
				logger.info("两账户之间进行转账回滚方法, 发起人用户标识: {}, 接收人用户标识: {}, 入参: transferInfo: {}, 更新接收人的账户金额异常: {}",
						transferInfo != null ? transferInfo.getFromUserId() : "",
						transferInfo != null ? transferInfo.getToUserId() : "",
						transferInfo == null ? "" : transferInfo.toString(), recAccountResult.getResMsg().toString());
				throw new BusinessException("更新接收人的账户金额异常!");
			}
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description :根据updateAccount计算好的待更新金额，更新用户账户金额
	 * @Method_Name : updateFinAccount;
	 * @param updateAccount
	 *            待更新账户金额组装的对象
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月28日 下午9:05:23;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> updateFinAccount(FinAccount updateAccount) {
		logger.info("方法: updateFinAccount, 根据updateAccount对象计算好的待更新金额更新用户账户金额, 用户标识: {}, 入参: updateAccount: {}",
				updateAccount != null ? updateAccount.getRegUserId() : "",
				updateAccount != null ? updateAccount.toString() : "");
		// 判断由updateAccount封装的待更新账户金额的对象和用户ID是否为空
		if (updateAccount == null || updateAccount.getRegUserId() == null) {
			logger.error("根据updateAccount对象计算好的待更新金额更新用户账户金额, 用户标识: {}, 入参: updateAccount: {}, 参数校验异常信息: {}",
					updateAccount != null ? updateAccount.getRegUserId() : "",
					updateAccount != null ? updateAccount.toString() : "", "用户账户和用户ID不能为空！");
			return new ResponseEntity<>(ERROR, "用户账户和用户ID不能为空！");
		}
		boolean resultFlag = false;
		FinAccount currentFinAccount = null;// 用户当前账户信息
		String lockKey = LOCK_PREFFIX + FinAccount.class.getSimpleName() + updateAccount.getRegUserId();
		JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, LOCK_WAITTIME);
			if (result) {
				resultFlag = true;
				// 根据用户ID，查询用户账户信息
				currentFinAccount = finAccountDao.findByRegUserId(updateAccount.getRegUserId());
				if (currentFinAccount == null) {
					return new ResponseEntity<>(ERROR, "未查询到用户账户信息");
				}
				logger.info("根据updateAccount对象计算好的待更新金额更新用户账户金额, 用户标识: {}, 入参: updateAccount: {}, 更新前账户信息: {}",
						updateAccount.getRegUserId(), updateAccount.toString(), currentFinAccount.toString());
				// 如果待更新账户的可用余额<0,普通用户需要判断更新的可用余额是否大于当前账户的可用余额
				if (CompareUtil.ltZero(updateAccount.getUseableMoney())) {
					if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(updateAccount.getRegUserId())
							&& CompareUtil.gt(updateAccount.getUseableMoney().negate(),
									currentFinAccount.getUseableMoney())) {
						return new ResponseEntity<>(ERROR, "账户余额不足!");
					}
				}
				// 如果待更新账户的nowMoney金额<0,则普通用户需要判断更新的nowMoney金额是否大于当前账户的nowMoney金额
				if (CompareUtil.ltZero(updateAccount.getNowMoney())) {
					if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(updateAccount.getRegUserId())
							&& CompareUtil.gt(updateAccount.getNowMoney().negate(), currentFinAccount.getNowMoney())) {
						return new ResponseEntity<>(ERROR, "账户余额不足!");
					}
				}
				// 如果待更新账户的冻结金额<0,则普通用户需要判断更新的冻结金额是否大于当前账户的冻结金额
				if (CompareUtil.ltZero(updateAccount.getFreezeMoney())) {
					if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(updateAccount.getRegUserId())
							&& CompareUtil.gt(updateAccount.getFreezeMoney().negate(),
									currentFinAccount.getFreezeMoney())) {
						return new ResponseEntity<>(ERROR, "冻结余额不足,不可支出冻结金额!");
					}
				}
				// 根据用户ID更新账户信息
				updateAccount.setBeforeNowMoney(currentFinAccount.getNowMoney());
				updateAccount.setId(currentFinAccount.getId());
				logger.info("根据updateAccount对象计算好的待更新金额更新用户账户金额, 用户标识: {}, 待更新账户的参数对象信息: {}",
						updateAccount.getRegUserId(), updateAccount.toString());
				finAccountDao.update(updateAccount);
			} else {
				return new ResponseEntity<>(ERROR, "当前网络太拥挤，请稍候再试！");
			}
			return new ResponseEntity<>(SUCCESS, currentFinAccount);
		} catch (Exception e) {
			logger.error("根据updateAccount对象计算好的待更新金额更新用户账户金额, 用户标识: {}, 入参: updateAccount: {}, 异常信息: ",
					updateAccount.getRegUserId(), updateAccount.toString(), e);
			throw new GeneralException("更新账户金额失败");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	/**
	 * @Description : 根据资金划转的subCode大类型更新用户账户金额
	 *              1、资金划转大类型为：PAY(20,"支出"),TURNS_IN(50, "转入")
	 *              账户金额减少，即useableMoney和nowMoney减少。 // 2、资金划转大类型为:
	 *              INCOME(10,"收入")TURNS_OUT(60, "转出"),
	 *              账户金额增加,即useableMoney和nowMoney增加。 // 3、资金划转大类型为：FREEZE(30,
	 *              "冻结") ----即userableMoney减少，freezeMoney增加 // 4、资金划转大类型为:
	 *              THAW(40, "解冻") ----即userableMoney增加，freezeMoney减少 //
	 *              5、有一种特殊的:资金划转为支出冻结：2020时，----
	 *              账户金额减少，即freezeMoney减少，nowMoney减少。
	 * @Method_Name : updateFinAccountBySubCode;
	 * @param regUserId
	 *            用户ID
	 * @param transferSubCode
	 *            资金划转subCode
	 * @param transMoney
	 *            交易金额
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月28日 下午9:37:21;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> updateFinAccountBySubCode(Integer regUserId, Integer transferSubCode,
			BigDecimal transMoney) {
		logger.info(
				"方法: updateFinAccountBySubCode, 根据资金划转的subCode大类型更新用户账户金额, 用户标识: {}, 入参: regUserId: {}, transferSubCode: {}, transMoney: {}",
				regUserId, regUserId, transferSubCode, transMoney);
		boolean resultFlag = false;
		FinAccount currentFinAccount = null;// 用户当前账户信息
		String lockKey = LOCK_PREFFIX + FinAccount.class.getSimpleName() + regUserId;
		JedisClusterLock jedisLock = new JedisClusterLock();
		try {
			boolean result = jedisLock.lock(lockKey, LOCK_EXPIRES, LOCK_WAITTIME);
			if (result) {
				resultFlag = true;
				/**
				 * 1、 根据用户ID，查询用户账户信息
				 */
				currentFinAccount = finAccountDao.findByRegUserId(regUserId);
				if (currentFinAccount == null) {
					return new ResponseEntity<>(ERROR, "未查询到用户账户信息");
				}
				logger.info(
						"根据资金划转的subCode大类型更新用户账户金额, 用户标识: {}, 入参: regUserId: {}, transferSubCode: {}, transMoney: {}, 更新前账户信息: {}",
						regUserId, regUserId, transferSubCode, transMoney, currentFinAccount.toString());
				// 待更新的账户金额对象
				FinAccount newFinAccount = new FinAccount();
				/**
				 * 2、根据subCode大类型更新用户账户金额
				 */
				// 用户可用余额
				BigDecimal useAbleMoney = currentFinAccount.getUseableMoney() == null ? BigDecimal.ZERO
						: currentFinAccount.getUseableMoney();
				// 用户冻结金额
				BigDecimal freezeMoney = currentFinAccount.getFreezeMoney() == null ? BigDecimal.ZERO
						: currentFinAccount.getFreezeMoney();
				// 如果transferSubCode的前两位的大类型为支出、转入、则认为用户账户资金减少，即可用余额和nowMoney减少.
				if (transferPayOut(transferSubCode)) {
					// 如果资金划转的subCode为支出冻结，用户账户资金减少,冻结金额减少，nowMoney减少
					if (transferSubCode.equals(TradeTransferConstants.getFundTransferSubCodeByType(
							FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.FROZEN))) {
						// 普通用户需要判断，支出冻结的金额是否大于账户冻结金额，并且判断账户冻结金额不能<0
						if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(currentFinAccount.getRegUserId())
								&& (CompareUtil.gt(transMoney, freezeMoney) || CompareUtil.ltZero(freezeMoney))) {
							return new ResponseEntity<>(ERROR, "冻结余额不足,不可支出冻结金额!");
						} else {
							// 冻结金额减少，nowMoney减少
							newFinAccount.setFreezeMoney(transMoney.negate());
							newFinAccount.setNowMoney(transMoney.negate());
						}
					} else {
						// 如果资金划转大类型为支出，则用户账户的可用余额和nowMoney减少,普通用户需要判断，支出金额是否大于可用余额，并且可用余额不能<0
						if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(currentFinAccount.getRegUserId())
								&& (CompareUtil.gt(transMoney, useAbleMoney) || CompareUtil.ltZero(useAbleMoney))) {
							return new ResponseEntity<>(ERROR, "账户余额不足!");
						} else {
							// 用户账户的可用余额和nowMoney减
							newFinAccount.setUseableMoney(transMoney.negate());
							newFinAccount.setNowMoney(transMoney.negate());
						}
					}
				} else if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.FREEZE
						.getBigTransferType()) {
					// 如果资金划转subcode大类型为冻结，则用户的账户资金可用余额减少，冻结金额增加
					if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(currentFinAccount.getRegUserId())
							&& (CompareUtil.gt(transMoney, useAbleMoney) || CompareUtil.ltZero(useAbleMoney))) {
						return new ResponseEntity<>(ERROR, "账户余额不足,不可冻结!");
					} else {
						// 用户的账户资金可用余额减少，冻结金额增加
						newFinAccount.setUseableMoney(transMoney.negate());
						newFinAccount.setFreezeMoney(transMoney);
					}
				} else if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.THAW
						.getBigTransferType()) {
					// 如果资金划转subcode大类型为资金解冻，则用户的账户资金冻结金额减少，可用余额增加
					if (!Integer.valueOf(UserConstants.PLATFORM_ACCOUNT_ID).equals(currentFinAccount.getRegUserId())
							&& (CompareUtil.gt(transMoney, freezeMoney) || CompareUtil.ltZero(freezeMoney))) {
						return new ResponseEntity<>(ERROR, "冻结余额不足,不可资金回退!");
					} else {
						// 用户的账户资金冻结金额减少，可用余额增加
						newFinAccount.setFreezeMoney(transMoney.negate());
						newFinAccount.setUseableMoney(transMoney);
					}
				} else if (transferIncome(transferSubCode)) {
				    //如果资金划转subcode为收入物业费，则冻结金额增加，nowMoney增加
				    if (transferSubCode.equals(TradeTransferConstants.getFundTransferSubCodeByType(
                            FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.PROPERTY_MONEY))) {
				        newFinAccount.setFreezeMoney(transMoney);
				        newFinAccount.setNowMoney(transMoney);
				    }else{
    					// 如果资金划转subcode大类型为收入，钱袋子转出，则用户的账户资金可用余额，nowMoney增加
    					newFinAccount.setUseableMoney(transMoney);
    					newFinAccount.setNowMoney(transMoney);
				    }
				} else {
					return new ResponseEntity<>(ERROR, "资金划转subCode格式有误!");
				}
				/**
				 * 3、 根据用户ID更新账户信息
				 */
				newFinAccount.setBeforeNowMoney(currentFinAccount.getNowMoney());
				newFinAccount.setId(currentFinAccount.getId());
				logger.info(
						"根据资金划转的subCode大类型更新用户账户金额, 用户标识: {}, 入参: regUserId: {}, transferSubCode: {}, transMoney: {}, 待更新账户的参数对象信息: {}",
						regUserId, regUserId, transferSubCode, transMoney, newFinAccount.toString());
				finAccountDao.update(newFinAccount);
				return new ResponseEntity<>(SUCCESS, currentFinAccount);
			} else {
				return new ResponseEntity<>(ERROR, "当前网络太拥挤，请稍候再试！");
			}
		} catch (Exception e) {
			logger.error(
					"根据资金划转的subCode大类型更新用户账户金额, 用户标识: {}, 入参: regUserId: {}, transferSubCode: {}, transMoney: {}, 异常信息: ",
					regUserId, regUserId, transferSubCode, transMoney, e);
			throw new GeneralException("更新账户金额失败");
		} finally {
			if (resultFlag) {
				jedisLock.freeLock(lockKey);
			}
		}
	}

	/**
	 * @Description : 封装资金划转对象，涉及到两账户之间的交易，则 接收方用户ID不为空
	 * @Method_Name : buildFinFundtransferData;
	 * @param finAccount
	 *            更新账户金额前账户对象
	 * @param recRegUserId
	 *            接收方账户用户ID
	 * @param finTradeFlow
	 *            交易流水对象
	 * @param transferSubCode
	 *            划转CODE
	 * @param transMoney
	 *            交易金额
	 * @return
	 * @throws Exception
	 * @return : FinFundtransfer;
	 * @Creation Date : 2017年11月1日 下午5:10:49;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private FinFundtransfer buildFinFundtransferData(FinAccount finAccount, Integer recRegUserId,
			FinTradeFlow finTradeFlow, int transferSubCode, BigDecimal transMoney) throws Exception {
		FinAccount newFinAccount = finAccountDao.findByRegUserId(finAccount.getRegUserId());
		FinFundtransfer finFundtransfer = FinTFUtil.initFinFundtransfer(newFinAccount.getRegUserId(), recRegUserId,
				finTradeFlow.getFlowId().toString(), transMoney, finAccount.getNowMoney(), newFinAccount.getNowMoney(),
				newFinAccount.getUseableMoney(), transferSubCode);
		finFundtransfer.setTradeType(finTradeFlow.getTradeType());
		return finFundtransfer;
	}

	/**
	 * @Description : 封装交易流水对象
	 * @Method_Name : createTradeFlowData;
	 * @param transferInfo
	 *            : 转账对象
	 * @return
	 * @throws Exception
	 * @return : FinTradeFlow;
	 * @Creation Date : 2017年11月1日 下午5:13:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private FinTradeFlow createTradeFlowData(TransferVo transferInfo) throws Exception {
		BigDecimal transMoney = transferInfo.getTransMoney();
		// 如果有积分转现金消费，则交易金额应加上积分转现金的金额
		if (transferInfo.getPointPayFlag() == EXIST_POINT_PAY) {
			transMoney.add(transferInfo.getPointChangeMoney());
		}
		FinTradeFlow finTradeFlow = FinTFUtil.initFinTradeFlow(transferInfo.getFromUserId(),
				transferInfo.getBusinessCode(), transMoney, transferInfo.getTradeType(),
				transferInfo.getPlatformSourceEnums());
		finTradeFlow.setCreateTime(transferInfo.getCreateTime());
		return finTradeFlow;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> withdrawCash(WithDrawCash withDrawCashInfo) {
		logger.info("方法: withdrawCash, 提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}",
				withDrawCashInfo != null ? withDrawCashInfo.getRegUserId() : "",
				withDrawCashInfo == null ? "" : withDrawCashInfo.toString());
		FinPaymentRecord newFinPaymentRecord = null;
		try {
			/**
			 * 1、校验参数正确性
			 */
			Map<String, String> resultDrawCashMap = PayValidateFactory.checkWithDrawCashData(withDrawCashInfo);
			if (!SUCCESS_FLAG_NAME.equals(resultDrawCashMap.get(SERVER_SUCCESS_FLAG))) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 校验参数异常信息: {}", withDrawCashInfo.getRegUserId(),
						withDrawCashInfo.toString(), resultDrawCashMap.get(SERVER_FAIL_FLAG));
				return new ResponseEntity<>(ERROR, resultDrawCashMap.get(SERVER_FAIL_FLAG));
			}
			// 待更新提现记录状态对象信息
			newFinPaymentRecord = new FinPaymentRecord();
			newFinPaymentRecord.setFlowId(withDrawCashInfo.getFlowId());
			/***
			 * 2、根据flowId查询提现交易流水信息
			 */
			FinPaymentRecord finPaymentRecord = finPaymentRecordDao
					.findFinPaymentRecordByFlowId(withDrawCashInfo.getFlowId());
			if (finPaymentRecord == null) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 查询提现记录异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "没有查询到对应的支付流水信息！");
				return new ResponseEntity<>(ERROR, "没有查询到对应的支付流水信息！");
			}
			/**
			 * 3、根据银行卡ID查询用户银行信息
			 */
			FinBankCard finBankCard = finBankCardDao.findByPK(finPaymentRecord.getBankCardId().longValue(),
					FinBankCard.class);
			if (finBankCard == null) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 查询用户银行卡异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "没有查询到用户银行卡信息！");
				return new ResponseEntity<>(ERROR, "没有查询到用户银行卡信息！");
			}
			/***
			 * 4、获取提现操作所需的信息
			 */
			String channel = withDrawCashInfo.getPayChannelEnum().getChannelKey();
			if (StringUtils.isBlank(channel)) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 获取交易渠道异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "交易渠道不存在！");
				return new ResponseEntity<>(ERROR, "交易渠道不存在！");
			}
			// 通过平台银行CODE，查询第三方银行CODE信息
			String bankKey = channel + PayStyleEnum.RZ.getType() + String.valueOf(PaymentConstants.TRADE_TYPE_USER_PERSONAL)
					+ finBankCard.getBankCode() + TradeStateConstants.START_USING_STATE;
			FinBankRefer bankRefer = finBankReferDao.findBankRefer(bankKey, channel, PayStyleEnum.RZ.getType(),
					String.valueOf(PaymentConstants.TRADE_TYPE_USER_PERSONAL), finBankCard.getBankCode(),
					TradeStateConstants.START_USING_STATE, null);
			if (bankRefer == null) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 获取第三方支付返回银行编码异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "第三方支付返回银行编码为空！");
				return new ResponseEntity<>(ERROR, "第三方支付返回银行编码为空,提现失败！");
			}
			finBankCard.setBankCode(bankRefer.getBankThirdCode());
			finBankCard.setBankName(bankRefer.getBankThirdName());
			// 获取第三方支付的省市信息
			FinCityRefer finCityRefer = finCityReferDao.findFinCityRefer(finBankCard.getBankProvince(),
					finBankCard.getBankCity(), channel);
			if (finCityRefer == null) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 获取用户提现的省市县数据异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "用户提现的省市县数据有误！");
				return new ResponseEntity<>(ERROR, "用户提现的省市县数据有误！");
			}
			// 查询支付提现配置信息
			String key = withDrawCashInfo.getSysNameCode() + withDrawCashInfo.getPlatformSourceName()
					+ withDrawCashInfo.getPayChannelEnum().getChannelKey()
					+ withDrawCashInfo.getPayStyleEnum().getType();
			FinPayConfig payConfig = finPayConfigDao.findPayConfigInfo(key, withDrawCashInfo.getSysNameCode(),
					PlatformSourceEnums.PC.getType(), withDrawCashInfo.getPayChannelEnum().getChannelKey(),
					withDrawCashInfo.getPayStyleEnum().getType());
			if (payConfig == null) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 查询支付配置异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), "查询支付配置信息为空！");
				return new ResponseEntity<>(ERROR, "查询支付配置信息为空！");
			}
			/**
			 * 5、根据不同的渠道调用第三方平台进行提现操作
			 */
			Method method = this.getClass().getDeclaredMethod(channel + "ToPay", FinCityRefer.class, FinBankCard.class,
					WithDrawCash.class, FinPaymentRecord.class, FinPayConfig.class);
			method.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { finCityRefer, finBankCard, withDrawCashInfo, finPaymentRecord,
					payConfig };
			// 执行方法
			Map<String, String> result = (Map<String, String>) method.invoke(this, arguments);
			// 返回支付结果响应给客户端
			if (!SUCCESS_FLAG_NAME.equals(result.get(SERVER_SUCCESS_FLAG))) {
				logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 调用第三方提现放款接口异常信息: {}",
						withDrawCashInfo.getRegUserId(), withDrawCashInfo.toString(), result.get(SERVER_FAIL_FLAG));
				// newFinPaymentRecord.setState(TRANSIT_FAIL);
				// finPaymentRecordDao.updateByFlowId(newFinPaymentRecord);
				return new ResponseEntity<>(ERROR, result.get(SERVER_FAIL_FLAG));
			}
			/**
			 * 6、更新支付记录表为划转中状态
			 */
			newFinPaymentRecord.setState(BANK_TRANSFER);
			finPaymentRecordDao.updateByFlowId(newFinPaymentRecord);
			return new ResponseEntity<>(Constants.SUCCESS, SUCCESS_FLAG_NAME);
		} catch (Exception e) {
			logger.error("提现放款, 用户标识: {}, 入参: withDrawCashInfo: {}, 调用第三方提现放款接口异常信息: ", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.toString(), e);
			// 更新提现状态为失败
			/*
			 * newFinPaymentRecord.setState(TRANSIT_FAIL);
			 * finPaymentRecordDao.updateByFlowId(newFinPaymentRecord);
			 */
			throw new GeneralException("提现放款失败:" + CommonUtils.printStackTraceToString(e));
		}
	}

	/**
	 * @Description : 宝付提现操作
	 * @Method_Name : baofuToPay;
	 * @param finBankCard
	 *            银行卡信息对象
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            支付流水对象
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年1月18日 下午5:46:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings({ "unchecked" })
	private Map<String, String> baofuToPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: baofuToPay, 宝付提现, 用户标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}",
				finPaymentRecord.getRegUserId(), finCityRefer.toString(), finBankCard.toString(),
				withDrawCashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
		Map<String, String> result = new HashMap<String, String>();
		try {
			// 组装宝付请求报文
			Map<String, String> reqDataMap = BaofuPayFactory.buildWithDrawReqData(finCityRefer, finBankCard,
					withDrawCashInfo, finPaymentRecord, payConfig);
			logger.info("宝付提现, 用户标识: {}, 提现流水标识: {}, 调用宝付提现接口, 通过流水等信息生成请求宝付接口报文: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), reqDataMap.toString());
			String resJson = HttpClientUtils.httpsPost(payConfig.getPayUrl(), reqDataMap);
			logger.info("宝付提现, 用户标识: {}, 提现流水标识: {}, 调用宝付提现接口, 宝付响应接口报文: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), resJson);
			// 解析报文，返回给客户端
			resJson = RSAUtil.decryptByPubCerFile(resJson, payConfig.getPublicKey());
			resJson = Base64Util.decode(resJson);
			TransContent<TransResResult> resData = new TransContent<TransResResult>(TransContent.DATA_TYPE_JSON);
			resData = (TransContent<TransResResult>) resData.str2Obj(resJson, TransResResult.class);
			TransHead headData = resData.getTrans_head();
			String ret_code = headData.getReturn_code();// 交易码
			String ret_msg = headData.getReturn_msg();// 交易描述
			logger.info("宝付提现, 用户标识: {}, 提现流水标识: {}, 宝付提现返回的交易状态: {}, 交易描述: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), ret_code, ret_msg);
			/**
			 * 如果宝付返回的交易状态为:代付请求交易成功 0000 ; 代付交易成功 200 ; 代付交易未明，请发起该笔订单查询 0300 ;
			 * 代付交易查证信息不存在 0401 ;代付主机系统繁忙 0999
			 * 则认为提现处理成功，但提现结果未知，需要以异步通知或查证得知最后的结果。否则认为是提现失败
			 */
			if (BAOFU_WITHDRAW_STATE_REQ_SUCCESS.equals(ret_code) || BAOFU_WITHDRAW_STATE_PAY_SUCCESS.equals(ret_code)
					|| BAOFU_WITHDRAW_STATE_PAY_PROCESSING.equals(ret_code)
					|| BAOFU_WITHDRAW_STATE_NO_DATA.equals(ret_code) || BAOFU_WITHDRAW_STATE_BUSY.equals(ret_code)) {
				result.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
			} else {
				result.put(SERVER_FAIL_FLAG, ret_code + "|" + ret_msg);
			}
		} catch (Exception e) {
			logger.error(
					"宝付提现, 用户标识: {}, 提现流水标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}, 调用宝付提现接口异常信息: ",
					withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), finCityRefer.toString(),
					finBankCard.toString(), withDrawCashInfo.toString(), finPaymentRecord.toString(),
					payConfig.toString(), e);
			result.put(SERVER_FAIL_FLAG, "提现失败,请联系客服!");
		}
		return result;
	}

	/**
	 * @Description : 宝付提现操作
	 * @Method_Name : baofuToPay;
	 * @param finBankCard
	 *            银行卡信息对象
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            支付流水对象
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年1月18日 下午5:46:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unused")
	private Map<String, String> baofuProtocolToPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: baofuProtocolToPay, 宝付提现, 用户标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}",
				finPaymentRecord.getRegUserId(), finCityRefer.toString(), finBankCard.toString(),
				withDrawCashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
		return baofuToPay(finCityRefer, finBankCard, withDrawCashInfo, finPaymentRecord, payConfig);
	}

	/**
	 * @Description : 宝付提现操作
	 * @Method_Name : baofuToPay;
	 * @param finBankCard
	 *            银行卡信息对象
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            支付流水对象
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018年1月18日 下午5:46:14;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings({ "unused" })
	private Map<String, String> baofuProtocolBToPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: baofuProtocolBToPay, 宝付提现, 用户标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}",
				finPaymentRecord.getRegUserId(), finCityRefer.toString(), finBankCard.toString(),
				withDrawCashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
		return baofuToPay(finCityRefer, finBankCard, withDrawCashInfo, finPaymentRecord, payConfig);
	}

	/***
	 * @Description : 调用连连进行提现
	 * @Method_Name : lianlianToPay;
	 * @param finCityRefer
	 *            城市对象
	 * @param finBankCard
	 *            银行卡信息对象
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            支付流水对象
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2017年5月31日 下午5:51:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@SuppressWarnings("unused")
	private Map<String, String> lianlianToPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: lianlianToPay, 连连提现, 用户标识: {}, 提现流水标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {} , payConfig: {}",
				withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), finCityRefer.toString(),
				finBankCard.toString(), withDrawCashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
		Map<String, String> resultPay = new HashMap<String, String>();// 返回支付结果
		try {
			/**
			 * 1、组装支付报文
			 */
			AgentPayInfo agentPayInfo = LianlianPayFactory.createLiLiPayBean(finBankCard, withDrawCashInfo,
					finPaymentRecord, payConfig, finCityRefer);
			logger.info("连连提现封装调用连连提现对象, 用户标识: {}, 提现流水标识: {}, 提现对象信息: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), agentPayInfo.toString());
			/**
			 * 2、将请求对象转为JSON，加签名，调用第三方接口支付
			 */
			agentPayInfo.setInfo_order("用户提现");
			JSONObject reqObject = (JSONObject) JSONObject.toJSON(agentPayInfo);
			// 添加报文签名
			String sign = PaymentUtil.addSign(reqObject, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
			reqObject.put("sign", sign);
			logger.info("连连提现, 用户标识: {}, 提现流水标识: {}, 请求连连接口报文: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), reqObject.toString());
			// 请求连连提现接口
			String resJosn = HttpClientMessageSender.sendPostJson(payConfig.getPayUrl(), reqObject.toString(), null);
			logger.info("连连提现, 用户标识: {}, 提现流水标识: {}, 连连响应接口报文: {}", withDrawCashInfo.getRegUserId(),
					withDrawCashInfo.getFlowId(), resJosn);
			JSONObject js = (JSONObject) JSON.parse(resJosn);
			String ret_code = js.get("ret_code").toString();// 交易码
			String ret_msg = js.get("ret_msg").toString();// 交易描述
			if (!THIRD_SUCCESS_FLAG.equals(ret_code)) {
				// 测试环境特殊处理
				if ("false".equals(PropertiesHolder.getProperty("isonline"))) {
					if ("商户提现权限不足".equals(ret_msg)) {
						resultPay.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
					} else {
						resultPay.put(SERVER_FAIL_FLAG, ret_code + "|" + ret_msg);
					}
				} else {
					resultPay.put(SERVER_FAIL_FLAG, ret_code + "|" + ret_msg);
				}
			} else {
				// 返回报文签名验证
				boolean verfiy = PaymentUtil.checkSign(resJosn, payConfig.getPublicKey(), payConfig.getPayMd5Key());
				if (verfiy) {
					if (THIRD_SUCCESS_FLAG.equals(ret_code)) {
						resultPay.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
					} else {
						logger.error("连连提现, 用户标识: {}, 提现流水标识: {}, 调用连连提现接口的交易状态: {}, 交易描述: {}",
								withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), ret_code, ret_msg);
						resultPay.put(SERVER_FAIL_FLAG, ret_code + "|" + ret_msg);
					}
				} else {
					logger.error("连连提现, 用户标识: {}, 提现流水标识: {}, 调用连连提现接口的异常信息: {}", withDrawCashInfo.getRegUserId(),
							withDrawCashInfo.getFlowId(), "签名验证失败！");
					resultPay.put(SERVER_FAIL_FLAG, "签名验证失败！");
				}
			}
		} catch (Exception e) {
			logger.error(
					"连连提现, 用户标识: {}, 提现流水标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}, 调用连连提现接口异常信息: ",
					withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), finCityRefer.toString(),
					finBankCard.toString(), withDrawCashInfo.toString(), finPaymentRecord.toString(),
					payConfig.toString(), e);
			resultPay.put(SERVER_FAIL_FLAG, "提现失败，请联系客服!");
		}
		return resultPay;
	}
	/**
	 * @Description : 易宝提现操作
	 * @Method_Name : baofuToPay;
	 * @param finBankCard
	 *            银行卡信息对象
	 * @param withDrawCashInfo
	 *            提现对象
	 * @param finPaymentRecord
	 *            支付流水对象
	 * @param payConfig
	 *            支付配置对象
	 * @return
	 * @throws Exception
	 * @return : Map<String,String>;
	 * @Creation Date : 2018-10-30 17:30:21;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	@SuppressWarnings({ "unchecked" })
	private Map<String, String> yeepayToPay(FinCityRefer finCityRefer, FinBankCard finBankCard,
			WithDrawCash withDrawCashInfo, FinPaymentRecord finPaymentRecord, FinPayConfig payConfig) throws Exception {
		logger.info(
				"方法: yeepayToPay, 易宝提现, 用户标识: {}, 提现流水标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {} , payConfig: {}",
				withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), finCityRefer.toString(),
				finBankCard.toString(), withDrawCashInfo.toString(), finPaymentRecord.toString(), payConfig.toString());
		Map<String, String> resultPay = new HashMap<String, String>();// 返回支付结果
		try {
			ResponseEntity<?> responseEntity = YeepayPayFactory.withDrawPay(finCityRefer, finBankCard, withDrawCashInfo, finPaymentRecord, payConfig);
			if (responseEntity.getResStatus() == Constants.SUCCESS){
				Map<String, Object > withDrawMap = responseEntity.getParams();
				String ret_code = withDrawMap.get("ret_code").toString();
				String ret_msg = withDrawMap.get("ret_msg").toString();
				if(withDrawMap.get("ret_code").equals(YEEPAY_WITHDRAW_CODE_SUCCESS)){
					resultPay.put(SERVER_SUCCESS_FLAG, SUCCESS_FLAG_NAME);
				}else{
					resultPay.put(SERVER_FAIL_FLAG, ret_code + "|" + ret_msg);
				}
			}else{
				resultPay.put(SERVER_FAIL_FLAG, responseEntity.getResMsg().toString());
			}
		} catch (Exception e) {
			logger.error(
					"易宝提现, 用户标识: {}, 提现流水标识: {}, 入参: finCityRefer: {}, finBankCard: {}, withDrawCashInfo: {}, finPaymentRecord: {}, payConfig: {}, 调用连连提现接口异常信息: ",
					withDrawCashInfo.getRegUserId(), withDrawCashInfo.getFlowId(), finCityRefer.toString(),
					finBankCard.toString(), withDrawCashInfo.toString(), finPaymentRecord.toString(),
					payConfig.toString(), e);
			resultPay.put(SERVER_FAIL_FLAG, "提现失败，请联系客服!");
		}
		return resultPay;
	}
	@Override
	public ResponseEntity<?> lianlianAgreeNoAuthApply(String repaymentNo, int regUserId, String noAgree,
			JSONObject repayInfo, String systemType, String platformSource) throws Exception {
		if (StringUtils.isEmpty(repaymentNo)) {
			return new ResponseEntity<>(ERROR, "还款计划编号不能为空！");
		}
		if (regUserId == 0) {
			return new ResponseEntity<>(ERROR, "用户唯一编号不能为空！");
		}
		if (StringUtils.isEmpty(noAgree)) {
			return new ResponseEntity<>(ERROR, "签约协议号不能为空！");
		}
		if (repayInfo == null) {
			return new ResponseEntity<>(ERROR, "还款计划不能为空！");
		}
		if (StringUtils.isEmpty(systemType)) {
			return new ResponseEntity<>(ERROR, "系统不能为空！");
		}
		if (StringUtils.isEmpty(platformSource)) {
			return new ResponseEntity<>(ERROR, "平台不能为空！");
		}
		String key = systemType + platformSource + PayChannelEnum.LianLian.getChannelKey() + PayStyleEnum.FQFK;
		FinPayConfig payConfig = finPayConfigDao.findPayConfigInfo(key, systemType, platformSource,
				PayChannelEnum.LianLian.getChannelKey(), PayStyleEnum.FQFK.getType());
		if (payConfig == null) {
			return new ResponseEntity<>(ERROR, "查询支付配置信息为空！");
		}
		AgreeNoAuthApply noAuthApply = new AgreeNoAuthApply();
		noAuthApply.setPlatform(payConfig.getMerchantNo());
		noAuthApply.setUser_id(String.valueOf(regUserId));
		noAuthApply.setOid_partner(payConfig.getMerchantNo());
		noAuthApply.setSign_type(payConfig.getSignStyle());
		noAuthApply.setApi_version(payConfig.getPayVersion());
		noAuthApply.setRepayment_plan(repayInfo.toString().replace("\\", ""));
		noAuthApply.setRepayment_no(repaymentNo);
		noAuthApply.setPay_type(LianLianPayCardStyleEnum.RZDC.getValue());// D认证支付（借记卡）
		noAuthApply.setNo_agree(noAgree);
		// 添加报文签名
		JSONObject reqObject = (JSONObject) JSONObject.toJSON(noAuthApply);
		String sign = PaymentUtil.addSign(reqObject, payConfig.getPrivateKey(), payConfig.getPayMd5Key());
		noAuthApply.setSign(sign);
		String reqJSON = JSONArray.toJSONString(noAuthApply);
		logger.info("用户标识：{}, 用户申请代扣授权,调用连连代扣授权请求报文：{}", regUserId, reqJSON);
		String resJSON = HttpClientUtils.httpPost(payConfig.getPayUrl(), reqJSON);
		logger.info("用户标识：{}, 用户申请代扣授权,调用连连代扣授权响应报文：{}", regUserId, resJSON);
		JSONObject resJsonObject = (JSONObject) JSON.parse(resJSON);
		if (resJsonObject == null || !LIAN_SUCCESS_CODE.equals(resJsonObject.get("ret_code"))) {
			logger.error("用户标识：{}, 签约还款标的ID:{},代扣请求同步报文异常,连连返回结果描述CODE:{},描述信息：{}", regUserId, repaymentNo,
					resJsonObject.get("ret_code"), resJsonObject.get("ret_msg"));
			return new ResponseEntity<>(ERROR, resJSON);
		}
		return new ResponseEntity<>(SUCCESS, resJSON);
	}

	// TODO 新帮需要修改这块逻缉
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, readOnly = false)
	public ResponseEntity<?> bankCardRepayment(PayPlan payPlan) {
		try {
			String msg = PayValidateFactory.validatePayPlan(payPlan);
			if (msg != null) {
				return new ResponseEntity<>(ERROR, msg);
			}
			FinAccount finAccount = finAccountDao.findByRegUserId(payPlan.getRegUserId());
			FinBankCard card = finBankCardDao.findByPK(payPlan.getBankCardId(), FinBankCard.class);
			RechargeCash rechargeCash = new RechargeCash();
			// rechargeCash.setBussinesCode(payPlan.getRepayId());
			rechargeCash.setUserId(payPlan.getRegUserId());
			rechargeCash.setTransMoney(payPlan.getRepayAmount());
			// rechargeCash.setFlowType(TRADE_TYPE_REPAYMENT);
			// rechargeCash.setSubCode(TRANSFER_TYPE_REPMENT_WITHHOLD_SPEND);
			String info = payPlan.getInfo() == null || "".equals(payPlan.getInfo()) ? "还款代扣" : payPlan.getInfo();
			// rechargeCash.setInfo(info);
			rechargeCash.setBankCard(payPlan.getIdCard());
			rechargeCash.setPayChannel(payPlan.getPayChannelEnum().getChannelKey());
			rechargeCash.setPlatformSourceName(payPlan.getPlatformSourceName());
			rechargeCash.setSystemTypeName(payPlan.getSysNameCode());
			// TODO 新帮需要确认这块逻缉
			rechargeCash.setPayStyle(payPlan.getPayStyleEnum().getType());
			// 处理充值金额
			PayValidateFactory.dealRechargeMoney(rechargeCash);
			// 插入代扣流水
			inMoney(rechargeCash, finAccount, 0);
			String key = payPlan.getSysNameCode() + payPlan.getPlatformSourceName()
					+ PayChannelEnum.LianLian.getChannelKey() + PayStyleEnum.DK.getType();
			FinPayConfig payConfig = finPayConfigDao.findPayConfigInfo(key, PayStyleEnum.DK.getType(),
					payPlan.getPlatformSourceName(), PayChannelEnum.LianLian.getChannelKey(),
					PayStyleEnum.DK.getType());
			if (payConfig == null) {
				return new ResponseEntity<>(ERROR, "查询支付配置信息为空！");
			}
			Date registerDate = finAccount.getCreateTime();
			String register_time = DateUtils.format(registerDate, DateUtils.DATE_HHMMSS);
			JSONObject riskItemObj = new JSONObject();
			riskItemObj.put("frms_ware_category", "2009");// 商品类目 2009 P2P贷款
			riskItemObj.put("user_info_bind_phone", payPlan.getRegUserId());// 用户唯一标示
			riskItemObj.put("user_info_mercht_userno", payConfig.getMerchantNo());// 用户唯一标示
			riskItemObj.put("user_info_dt_register", register_time);// 注册时间
			riskItemObj.put("user_info_full_name", finAccount.getUserName());// risk
																				// state
			riskItemObj.put("user_info_id_no", payPlan.getIdCard().trim());// 身份证号
			riskItemObj.put("user_info_identify_type", "1");// 是否实名认证
			riskItemObj.put("user_info_identify_state", "1");// 银行卡实名认证
			// 构造请求报文
			BankCardRepayment bankCardRepayment = new BankCardRepayment();
			bankCardRepayment.setPlatform(payPlan.getPlatformSourceName());
			bankCardRepayment.setOid_partner(payConfig.getMerchantNo());
			bankCardRepayment.setUser_id(String.valueOf(payPlan.getRegUserId()));
			bankCardRepayment.setSign_type(payConfig.getSignStyle());
			// 订单类型
			bankCardRepayment.setBusi_partner(payConfig.getBusinessType());
			bankCardRepayment.setApi_version("1.0");
			// bankCardRepayment.setNo_order(rechargeCash.getFlowId());
			bankCardRepayment.setDt_order(PaymentUtil.getCurrentDateTimeStr());
			bankCardRepayment.setName_goods(payPlan.getInfo());
			bankCardRepayment.setInfo_order("借款标{" + payPlan.getBidId() + "}还款代扣");
			bankCardRepayment.setMoney_order(String.valueOf(payPlan.getRepayAmount()));
			// bankCardRepayment.setNotify_url(payPlan.getNotifyUrl());
			bankCardRepayment.setRisk_item(riskItemObj.toString());
			bankCardRepayment.setSchedule_repayment_date(DateUtils.format(payPlan.getCurPlayDate(), DateUtils.DATE));
			bankCardRepayment.setRepayment_no(payPlan.getBidId());
			bankCardRepayment.setPay_type("D"); // D认证支付（借记卡）
			bankCardRepayment.setNo_agree(payPlan.getThirdAccount());
			bankCardRepayment.setId_type("0");// 默认为 0:身份证
			bankCardRepayment.setCard_no(card.getBankCard());
			bankCardRepayment.setId_no(payPlan.getIdCard());
			// 加签名
			String sign = PaymentUtil.addSign(JSON.parseObject(JSON.toJSONString(info)), payConfig.getPrivateKey(),
					payConfig.getPayMd5Key());
			bankCardRepayment.setSign(sign);
			String reqJSON = JSONArray.toJSONString(bankCardRepayment);
			// logger.info("用户标识：{},流水标识：{},还款代扣,请求报文：{}",
			// payPlan.getRegUserId(), rechargeCash.getFlowId(), reqJSON);
			String resJSON = HttpClientUtils.httpPost(payConfig.getPayUrl(), reqJSON);
			// logger.info("用户标识：{},流水标识：{},还款代扣,响应报文：{}",
			// payPlan.getRegUserId(), rechargeCash.getFlowId(), resJSON);
			JSONObject re = (JSONObject) JSON.parse(resJSON);
			if (re != null && !LIAN_SUCCESS_CODE.equals(re.get("ret_code"))) {
				logger.info("用户标识：{},代扣还款计划ID：{},代扣请求同步报文异常,连连返回结果CODE:{},描述信息：{}", payPlan.getRegUserId(),
						payPlan.getRepayId(), re.get("ret_code"), re.get("ret_msg"));
			}
			return new ResponseEntity<>(SUCCESS, re);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("用户标识：{},代扣还款计划ID：{},异常错误信息：{}", payPlan.getRegUserId(), payPlan.getRepayId(),
						e.getMessage());
			}
			throw new GeneralException("还款代扣异常！");
		}
	}

	/**
	 * @Description : caoxb 方法描述：生成充值的业务交易流水
	 * @Method_Name : inMoney
	 * @param rechargeCash
	 *            充值封装VO
	 * @param finAccount
	 *            账户对象
	 * @param state
	 *            状态
	 * @return
	 * @return : String
	 * @Creation Date : 2017年6月6日 下午2:46:16
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	private FinPaymentRecord inMoney(RechargeCash rechargeCash, FinAccount finAccount, Integer state) throws Exception {
		FinPaymentRecord finPaymentRecord = new FinPaymentRecord();
		if (StringUtils.isBlank(rechargeCash.getPaymentFlowId())) {
			finPaymentRecord.setFlowId(CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.RECHARGE.getValue(),
					PlatformSourceEnums.platformTypeByType(rechargeCash.getPlatformSourceName()),
					rechargeCash.getRechargeFlag() == RECHARGE_FLAG_BANK ? BANK_INVEST_BUSINESS : COMMON_BUSINESS));
		} else {
			finPaymentRecord.setFlowId(rechargeCash.getPaymentFlowId());
		}
		finPaymentRecord.setBankCardId(rechargeCash.getBankCardId());
		finPaymentRecord.setPayChannel(PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
		finPaymentRecord.setTradeType(PayStyleEnum.RECHARGE.getValue());
		finPaymentRecord.setTradeSource(PlatformSourceEnums.valueByType(rechargeCash.getPlatformSourceName()));
		// 处理充值金额
		finPaymentRecord.setTransMoney(PayValidateFactory.dealRechargeMoney(rechargeCash));
		finPaymentRecord.setReconciliationState(TradeStateConstants.PAY_CHECK_RECONCILIATION_WAIT);
		finPaymentRecord.setRechargeSource(PayStyleEnum.valueByType(rechargeCash.getPayStyle()));
		finPaymentRecord.setRegUserId(rechargeCash.getUserId());
		finPaymentRecord.setState(state);
		rechargeCash.setPaymentFlowId(finPaymentRecord.getFlowId());
		finPaymentRecordDao.save(finPaymentRecord);
		return finPaymentRecord;
	}

	/**
	 * 
	 * @Description : caoxb 方法描述:连连支付请求方法
	 * @Method_Name : lianlianReqPay
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            用户账户对象
	 * @param payConfig
	 *            支付配置信息
	 * @return
	 * @return : Map<String,Object>
	 * @Creation Date : 2017年6月6日 下午4:35:10
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	public Map<String, Object> lianlianReqPay(RechargeCash rechargeCash, FinAccount finAccount, FinPayConfig payConfig)
			throws Exception {
		logger.info("方法: lianlianReqPay, 连连充值, 用户标识: {}, 入参: rechargeCash: {}, finAccount: {}, payConfig: {}",
				rechargeCash.getUserId(), rechargeCash.toString(), finAccount.toString(), payConfig.toString());
		Map<String, Object> payParams = null;
		// 获取连连风险信息
		Map<String, Object> llRiskInfos = LianlianPayFactory.riskInfo(rechargeCash, finAccount, payConfig);
		JSONObject riskItemObj = (JSONObject) llRiskInfos.get("riskInfo");
		OrderInfo orderInfo = (OrderInfo) llRiskInfos.get("orderInfo");
		logger.info("连连充值风险订单信息组装, 用户标识: {}, 组装返回信息: riskInfo: {}, orderInfo: {}", rechargeCash.getUserId(),
				riskItemObj.toString(), orderInfo.toString());
		// 构造支付请求对象
		switch (PlatformSourceEnums.valueByType(rechargeCash.getPlatformSourceName())) {
		case 10:// PC
			payParams = LianlianPayFactory.paymentPcInfo(rechargeCash, finAccount, orderInfo, riskItemObj, payConfig);
			break;
		case 13:// wap
			payParams = LianlianPayFactory.paymentWapInfo(rechargeCash, finAccount, orderInfo, riskItemObj, payConfig);
			break;
		case 11:// IOS
			payParams = LianlianPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		case 12:// ANDROID
			payParams = LianlianPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		default:
			payParams = null;
			logger.info("连连充值, 用户标识: {}, 充值流水标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
					rechargeCash.getPaymentFlowId(), "没有此平台来源！");
			break;
		}
		logger.info("连连充值, 用户标识: {}, 充值流水标识: {}, 充值渠道: {}, 充值请求报文: {}", rechargeCash.getUserId(),
				rechargeCash.getPaymentFlowId(), rechargeCash.getPlatformSourceName(), payParams.toString());
		return payParams;

	}

	@Override
	@Compensable(cancelMethod = "rollBackInsertTradeAndTransfer", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> batchInsertTradeAndTransfer(FinTradeFlow tradeFlow, List<FinFundtransfer> transfersList) {
		logger.info("{}, 插入交易流水多笔资金划转不更新账户, 入参: 用户标识: {}, tradeFlow: {}, transfersList: {}",
				BaseUtil.getTccTryLogPrefix(), tradeFlow.getRegUserId(), tradeFlow.toString(),
				JsonUtils.toJson(transfersList));
		try {
			// 遍历循环资金划转，计算preMoney,nowMoney且组装资金划转对象，插入资金流水
			for (FinFundtransfer transfer : transfersList) {
				FinAccount finAccount = finAccountDao.findByRegUserId(transfer.getRegUserId());
				if (finAccount == null) {
					logger.info("插入交易流水多笔资金划转不更新账户, 划转用户标识: {}, 异常信息: {}", transfer.getRegUserId(), "未查询到用户账户信息!");
					return new ResponseEntity<>(ERROR, "未查询到用户账户信息!");
				}
				transfer.setTradeFlowId(tradeFlow.getFlowId());
				transfer.setTradeType(tradeFlow.getTradeType());
				transfer.setPreMoney(finAccount.getNowMoney());
				transfer.setAfterMoney(finAccount.getUseableMoney());
				transfer.setNowMoney(finAccount.getNowMoney());
				finFundtransferDao.save(transfer);
			}
			// 插入交易流水
			finTradeFlowDao.save(tradeFlow);
			return new ResponseEntity<>(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("{}, 插入交易流水多笔资金划转不更新账户, 入参: 用户标识: {}, tradeFlow: {}, transfersList: {}",
					BaseUtil.getTccTryLogPrefix(), tradeFlow.getRegUserId(), tradeFlow.toString(),
					JsonUtils.toJson(transfersList), e);
			throw new GeneralException("生成一条流水多笔资金划转操作失败");
		}
	}

	/**
	 * @Description :回滚插入一条交易流水，多笔资金划转，不更新账户
	 * @Method_Name : rollBackInsertTradeAndTransfer;
	 * @param tradeFlow
	 *            交易流水
	 * @param transfersList
	 *            资金划转LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年3月30日 下午3:01:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> rollBackInsertTradeAndTransfer(FinTradeFlow tradeFlow,
			List<FinFundtransfer> transfersList) {
		logger.info(
				"tcc cancel batchInsertTradeAndTransfer, 回滚插入交易流水, 多笔资金划转, 不更新账户入参 用户标识: {}, tradeFlow: {}, transfersList: {}",
				tradeFlow.getRegUserId(), tradeFlow.toString(), JsonUtils.toJson(transfersList));
		try {
			// 查询交易流水是否存在，如果存在，则删除交易流水及资金划转流水
			FinTradeFlow finTradeFlow = finTradeFlowDao.findByFlowId(tradeFlow.getFlowId());
			if (finTradeFlow != null) {
				finTradeFlowDao.deleteByFlowId(tradeFlow.getFlowId());
				finFundtransferDao.deleteByPflowId(tradeFlow.getFlowId());
			}
			return new ResponseEntity<>(SUCCESS);
		} catch (Exception e) {
			logger.error(
					"tcc cancel batchInsertTradeAndTransfer, 回滚插入交易流水,多笔资金划转,不更新账户, 入参: 用户标识: {}, tradeFlow: {}, transfersList: {}",
					tradeFlow.getRegUserId(), tradeFlow.toString(), JsonUtils.toJson(transfersList), e);
			throw new GeneralException("生成一条流水多笔资金划转操作回复失败");
		}

	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> rollBackAccountAndDelFlows(FinAccount account, String flowId) throws Exception {
		logger.info("方法: rollBackAccountAndDelFlows, 回滚账户,删除交易流水,资金划转, 入参: account: {}, flowId: {}", account, flowId);
		// 根据flowId,查询用户流水是否存在，如果存在，则进行回滚操作
		FinTradeFlow finTradeFlow = finTradeFlowDao.findByFlowId(flowId);
		if (finTradeFlow != null) {
			// 更新用户账户金额
			ResponseEntity<?> updateResult = updateFinAccount(account);
			if (updateResult.getResStatus() == SUCCESS) {
				// 删除交易流水和资金划转
				finTradeFlowDao.deleteByFlowId(flowId);
				finFundtransferDao.deleteByPflowId(flowId);
			} else {
				throw new BusinessException("更新用户账户失败!");
			}
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @descrition 充值前校验并获取账户、渠道配置信息
	 * @param rechargeCash
	 *            充值对象数据
	 * @return
	 * @Creation Date : 2018-05-14 9:21:27
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	public ResponseEntity<?> rechargeAccountInfo(RechargeCash rechargeCash) {
		try {
			/**
			 * 1、查询账户
			 */
			FinAccount finAccount = finAccountDao.findByRegUserId(rechargeCash.getUserId());
			if (finAccount == null) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), "账户不存在！");
				return new ResponseEntity<>(ERROR, "账户不存在！");
			}
			/**
			 * 2、获取充值渠道配置信息
			 */
			String key = rechargeCash.getSystemTypeName() + rechargeCash.getPlatformSourceName()
					+ rechargeCash.getPayChannel() + rechargeCash.getPayStyle();
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, rechargeCash.getSystemTypeName(),
					rechargeCash.getPlatformSourceName(), rechargeCash.getPayChannel(), rechargeCash.getPayStyle());
			if (finPayConfig == null) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), "查询支付配置信息为空！");
				return new ResponseEntity<>(ERROR, "查询支付配置信息为空！");
			}
			/**
			 * 3、 根据平台的银行编码，转换为第三方支付的银行编码
			 */
			String bankKey = rechargeCash.getPayChannel() + rechargeCash.getPayStyle() + rechargeCash.getuType()
					+ rechargeCash.getBankCode() + TradeStateConstants.START_USING_STATE;
			FinBankRefer bankRefer = finBankReferDao.findBankRefer(bankKey, rechargeCash.getPayChannel(),
					rechargeCash.getPayStyle(), String.valueOf(rechargeCash.getuType()), rechargeCash.getBankCode(),
					TradeStateConstants.START_USING_STATE, null);
			if (bankRefer == null) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), "第三方支付返回银行编码为空！");
				return new ResponseEntity<>(ERROR, "第三方支付返回银行编码为空,充值失败！");
			}
			rechargeCash.setBankCode(bankRefer.getBankThirdCode());
			return new ResponseEntity<>(SUCCESS).addParam("finAccount", finAccount)
					.addParam("finPayConfig", finPayConfig).addParam("rechargeCash", rechargeCash).addParam("bankRefer", bankRefer);
		} catch (Exception e) {
			logger.error("充值业务, 用户标识: {}, 充值入参: rechargeCash: {}, 充值异常信息: ", rechargeCash.toString(),
					rechargeCash.getUserId(), e);
			return new ResponseEntity<>(ERROR, "获取充值用户信息异常");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> rechargeCash(RechargeCash rechargeCash) {
		logger.info("方法: rechargeCash, 充值业务, 用户标识: {}, 入参: {}", rechargeCash.getUserId(), rechargeCash.toString());
		try {
			/**
			 * 1、 首次充值校验
			 */
			ResponseEntity<?> firstResult = firstRecharge(rechargeCash);
			if (firstResult.getResStatus() == ERROR) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
						firstResult.getResMsg().toString());
				return firstResult;
			}
			ResponseEntity<?> result = rechargeAccountInfo(rechargeCash);
			if (result.getResStatus() == ERROR) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), result.getResMsg().toString());
				return result;
			}
			Map<String, Object> map = (Map) result.getParams();
			FinAccount finAccount = (FinAccount) map.get("finAccount");
			rechargeCash = (RechargeCash) map.get("rechargeCash");
			FinPayConfig payConfig = (FinPayConfig) map.get("finPayConfig");
			FinBankRefer bankRefer = (FinBankRefer) map.get("bankRefer");
			FinBankCard finBankCard = (FinBankCard)firstResult.getParams().get("finBankCard");
			//判断是否走自己收银台，协议支付、易宝支付进入收银台
			if(isProtocolPay(rechargeCash)){
				logger.info("走收银台。");
				Map<String, Object> protocolMap = getProtocolPayData(rechargeCash,finAccount,payConfig,bankRefer,finBankCard);
				return new ResponseEntity<>(SUCCESS, protocolMap);
			}
			/**
			 * 5、生成充值的业务流水记录
			 */
			inMoney(rechargeCash, finAccount, TradeStateConstants.BANK_TRANSFER);
			/**
			 * 6、通过反射根据不同支付渠道，调用不同支付方法
			 */
			Method payMethod = this.getClass().getDeclaredMethod(rechargeCash.getPayChannel() + "ReqPay",
					RechargeCash.class, FinAccount.class, FinPayConfig.class);
			payMethod.setAccessible(true);
			Object[] payArguments = new Object[] { rechargeCash, finAccount, payConfig };
			/**
			 * 7、获得支付组装数据
			 */
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的支付对象信息
			Map<String, Object> payReturnResult = (Map<String, Object>) payMethod.invoke(this, payArguments);
			if (payReturnResult == null) {
				throw new BusinessException("组装支付充值对象失败！");
			}
			resultMap.put("paymentflowId", rechargeCash.getPaymentFlowId()); 
			resultMap.put("submitForm", payReturnResult);
			resultMap.put("reqUrl", payConfig.getPayUrl());
			return new ResponseEntity<>(SUCCESS, resultMap);
		} catch (Exception e) {
			logger.error("充值业务, 用户标识: {}, 充值入参: rechargeCash: {}, 充值异常信息: ", rechargeCash.toString(),
					rechargeCash.getUserId(), e);
			throw new GeneralException("充值失败!");
		}
	}
	/**
	 * 判断是否是协议支付
	 * @param rechargeCash
	 * @return
	 * @author binliang@hongkunjinfu.com 梁彬
	 * @date 2018-08-10 10:26:39
	 */
	public boolean isProtocolPay(RechargeCash rechargeCash){
		boolean result = false;
		// 支付渠道
		String payChannel = rechargeCash.getPayChannel();
		// 支付方式
		String payStyle = rechargeCash.getPayStyle();
		//如果是协议支付或者易宝支付，并且非网银渠道，则是协议认证支付，走自己收银台
		if(PayStyleEnum.RZ.getType().equalsIgnoreCase(payStyle) 
				&& (PayChannelEnum.BaoFuProtocol.getChannelKey().equalsIgnoreCase(payChannel) ||
						PayChannelEnum.BaoFuProtocolB.getChannelKey().equalsIgnoreCase(payChannel)
						|| PayChannelEnum.Yeepay.getChannelKey().equalsIgnoreCase(payChannel))){
			result = true;
		}
		return result;
	}
	/**
	 * 组装由充值页进入收银台数据
	 * @param rechargeCash
	 * @param finAccount
	 * @param payConfig
	 * @return map
	 * @author binliang@hongkunjinfu.com 梁彬
	 * @date 2018-08-10 10:33:39
	 */
	public Map<String, Object> getProtocolPayData(RechargeCash rechargeCash,FinAccount finAccount ,FinPayConfig payConfig ,FinBankRefer bankRefer
			,FinBankCard finBankCard){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//走自己平台收银台标记 1宝付协议， 2 易宝支付
		String isgoToCashier = "1";
		if(PayChannelEnum.Yeepay.getChannelKey().equalsIgnoreCase(rechargeCash.getPayChannel())){
			isgoToCashier = "2";
		}
		resultMap.put("isgoToCashier", isgoToCashier);
		//交易时间
		resultMap.put("transDate", DateUtils.getCurrentDate(DateUtils.DATE_HH_MM_SS));
		//支付金额
		resultMap.put("transAmt", rechargeCash.getTransMoney());
		//银行图标地址
		resultMap.put("bankIconAddress", bankRefer.getBankIconAddress());
		//银行卡号展示脱敏数据,显示最后四位
		String cardNo = rechargeCash.getBankCard();
		String entityNo = cardNo.substring(cardNo.length()-4, cardNo.length());
		resultMap.put("entityNo", entityNo);
		//身份证号脱敏数据
		String idCard = rechargeCash.getIdCard();
		resultMap.put("idCard", idCard.substring(0, 4)+"****"+idCard.substring(idCard.length()-4, idCard.length()));
		//银行预留手机号
		resultMap.put("bankTel", rechargeCash.getTel());
		resultMap.put("isBind", finBankCard.getState());
		resultMap.put("bankId", finBankCard.getId());
		resultMap.put("bankCard", cardNo);
		return resultMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> paymentVerificationCode(RechargeCash rechargeCash) {
		logger.info("方法: paymentVerificationCode, 支付短验, 用户标识: {}, 入参: {}", rechargeCash.getUserId(),
				rechargeCash.toString());
		// 短验类需要先生成流水号
		rechargeCash.setPaymentFlowId(CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.RECHARGE.getValue(),
				PlatformSourceEnums.platformTypeByType(rechargeCash.getPlatformSourceName()),
				rechargeCash.getRechargeFlag() == RECHARGE_FLAG_BANK ? BANK_INVEST_BUSINESS : COMMON_BUSINESS));
		ResponseEntity<?> result = rechargeAccountInfo(rechargeCash);
		if (result.getResStatus() == ERROR) {
			logger.error("支付短验, 用户标识: {}, 支付短验异常信息: {}", rechargeCash.getUserId(), result.getResMsg().toString());
			return result;
		}
		Map<String, Object> map = (Map) result.getParams();
		Map<String, Object> payResult;
		try {
			FinAccount finAccount = (FinAccount) map.get("finAccount");
			// 根据充值操作，调用相应的第三方接口方法
			String payMethodName = protrocalPayName(rechargeCash);
			payResult = callThirdPay(payMethodName, map);
			if (payResult == null) {
				throw new BusinessException("充值方法调用失败！");
			}
			if (PaymentConstants.PRE_PAYMENT.equals(rechargeCash.getOperateType()) ) {// 如果是预支付类交易短信验证码，需要将流水落地
				if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
					inMoney(rechargeCash, finAccount, TradeStateConstants.PENDING_PAYMENT);
				}
			}
			logger.info(
					"方法: FinConsumptionServiceImpl 层  paymentVerificationCode method , 返回 payResult: {},resStatus:{},resMsg{}",
					payResult, payResult.get("resStatus"), payResult.get("resMsg"));
			return new ResponseEntity<>(SUCCESS, payResult);
		} catch (Exception e) {
			throw new BusinessException("支付短验方法调用失败！");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> yeepayVerificationCode(RechargeCash rechargeCash) {
		logger.info("方法: yeepayVerificationCode, 易宝支付短验, 用户标识: {}, 入参: {}", rechargeCash.getUserId(),
				rechargeCash.toString());
		ResponseEntity<?> result = rechargeAccountInfo(rechargeCash);
		if (result.getResStatus() == ERROR) {
			logger.error("支付短验, 用户标识: {}, 支付短验异常信息: {}", rechargeCash.getUserId(), result.getResMsg().toString());
			return result;
		}
		Map<String, Object> map = (Map) result.getParams();
		Map<String, Object> payResult;
		try {
			FinAccount finAccount = (FinAccount) map.get("finAccount");
			// 根据充值操作，调用相应的第三方接口方法
			String payMethodName = "";
			if(PaymentConstants.YEEPAY_FIRST_RECHARGE.equals(rechargeCash.getOperateType())){
				payMethodName = "paymentSendSms";
			}
			if(PaymentConstants.YEEPAY_SENDSMS_AGAIN.equals(rechargeCash.getOperateType())){
				payMethodName = "paymentResendSms";
			}
			payResult = callThirdPay(payMethodName, map);
			if (payResult == null) {
				throw new BusinessException("充值方法调用失败！");
			}
			if (PaymentConstants.YEEPAY_FIRST_RECHARGE.equals(rechargeCash.getOperateType())) {// 如果是预支付类交易短信验证码，需要将流水落地
				if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
					inMoney(rechargeCash, finAccount, TradeStateConstants.PENDING_PAYMENT);
				}
			}
			logger.info(
					"方法: FinConsumptionServiceImpl 层  yeepayVerificationCode method , 返回 payResult: {},resStatus:{},resMsg{}",
					payResult, payResult.get("resStatus"), payResult.get("resMsg"));
			return new ResponseEntity<>(SUCCESS, payResult);
		} catch (Exception e) {
			throw new BusinessException("易宝支付短验方法调用失败！");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> confirmPay(RechargeCash rechargeCash) {
		logger.info("方法: rechargeCash, 充值业务, 用户标识: {}, 入参: {}", rechargeCash.getUserId(), rechargeCash.toString());
		ResponseEntity<?> resultMap = null;
		
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocol.getChannelKey()) 
				|| rechargeCash.getPayChannel().equals(PayChannelEnum.BaoFuProtocolB.getChannelKey())){
			resultMap = this.agreementPay(rechargeCash);
		}
		
		if (rechargeCash.getPayChannel().equals(PayChannelEnum.Yeepay.getChannelKey())){
			resultMap = this.yeepayConfirmPay(rechargeCash);
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @Description : 宝付协议支付充值接口(确认绑卡、直接支付、确认支付)
	 * @Method_Name : agreementPay
	 * @param rechargeCash:充值对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018-05-12 16:55:27
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	public ResponseEntity<?> agreementPay(RechargeCash rechargeCash){
		logger.info("baofu agreementPay method。。");
		try {
			if (PaymentConstants.DIRECT_PAYMENT.equals(rechargeCash.getOperateType())) {// 如果是直接支付，则生成流水
				rechargeCash.setPaymentFlowId(CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.RECHARGE.getValue(),
						PlatformSourceEnums.platformTypeByType(rechargeCash.getPlatformSourceName()),
						rechargeCash.getRechargeFlag() == RECHARGE_FLAG_BANK ? BANK_INVEST_BUSINESS : COMMON_BUSINESS));
				logger.info("直接支付，生成流水flowId={}", rechargeCash.getPaymentFlowId());
			}
			ResponseEntity<?> result = rechargeAccountInfo(rechargeCash);
			if (result.getResStatus() == ERROR) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), result.getResMsg().toString());
				return result;
			}
			if (!(rechargeCash.getOperateType().equals(CONFIG_TIED_CARD) || rechargeCash.getOperateType() .equals(DIRECT_PAYMENT)
					|| rechargeCash.getOperateType().equals(CONFIRM_PAYMENT))) {
				logger.error("充值业务, 用户标识: {}, 充值入参: rechargeCash: {}, api operate{}, 充值异常信息: ", rechargeCash.toString(),
						rechargeCash.getUserId(), rechargeCash.getOperateType(), "充值api operate参数必须是确认支付参数");
				return new ResponseEntity<>(ERROR, "充值api operate参数必须是确认支付操作！");
			}
			Map<String, Object> map = (Map) result.getParams();
			FinAccount finAccount = (FinAccount) map.get("finAccount");
			rechargeCash = (RechargeCash) map.get("rechargeCash");

			if (StringUtils.isBlank(rechargeCash.getPaymentFlowId())) { // 如果流水为空，则预生成流水
				rechargeCash.setPaymentFlowId(CreateFlowUtil.createPaymentTradeFlow(PayStyleEnum.RECHARGE.getValue(),
						PlatformSourceEnums.platformTypeByType(rechargeCash.getPlatformSourceName()),
						rechargeCash.getRechargeFlag() == RECHARGE_FLAG_BANK ? BANK_INVEST_BUSINESS : COMMON_BUSINESS));
				logger.info("流水号为空, 生成流水, operateType:{},flowId={}", rechargeCash.getOperateType(),
						rechargeCash.getPaymentFlowId());
			}

			// 根据充值操作，调用相应的第三方接口方法
			String payMethodName = protrocalPayName(rechargeCash);
			Map<String, Object> payResult = callThirdPay(payMethodName, map);
			if (payResult == null) {
				throw new BusinessException("充值方法调用失败！");
			}
			if (!payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
				return new ResponseEntity<>(ERROR, payResult);
			}
			if (PaymentConstants.CONFIG_TIED_CARD.equals(rechargeCash.getOperateType())) {// 如果是确认绑卡，返回了协议号，说明成功，更新手机号、协议号
				if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
					String no_agree = payResult.get("no_agree").toString();
					FinBankCard finBankCard = this.finBankCardDao.findByPK(new Long(rechargeCash.getBankCardId()),
							FinBankCard.class);
					if (finBankCard == null) {
						return new ResponseEntity<>(ERROR, "银行卡信息为空");
					}
					if (finBankCard.getState() != TradeStateConstants.BANK_CARD_STATE_AUTH) {
						FinBankCard updateFinBankCard = new FinBankCard();
						updateFinBankCard.setId(finBankCard.getId());
						updateFinBankCard.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
						this.finBankCardDao.update(updateFinBankCard);
					}
					FinBankCardBinding bankCard = this.finBankCardBindingDao.findBankCardBinding(
							rechargeCash.getBankCardId(), rechargeCash.getUserId(),
							PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
					if (bankCard == null) {
						FinBankCardBinding bankCardBindingInfo = new FinBankCardBinding();
						bankCardBindingInfo.setRegUserId(rechargeCash.getUserId());
						bankCardBindingInfo.setBankThirdCode(rechargeCash.getBankCode());
						bankCardBindingInfo.setFinBankCardId(rechargeCash.getBankCardId());
						bankCardBindingInfo.setThirdAccount(no_agree);
						bankCardBindingInfo.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
						bankCardBindingInfo.setPayChannel(PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
						this.finBankCardBindingDao.insertFinBankCardBinding(bankCardBindingInfo);
					} else {
						FinBankCardBinding bankCardBindingInfo = new FinBankCardBinding();
						bankCardBindingInfo.setId(bankCard.getId());
						bankCardBindingInfo.setThirdAccount(no_agree);
						this.finBankCardBindingDao.updateFinBankCardBinding(bankCardBindingInfo);
					}
				}
			}
			if (PaymentConstants.DIRECT_PAYMENT.equals(rechargeCash.getOperateType())) {// 如果是直接支付则流水落地
				if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
					String paymentFlowId = payResult.get("paymentFlowId").toString();
					rechargeCash.setPaymentFlowId(paymentFlowId);
					inMoney(rechargeCash, finAccount, TradeStateConstants.BANK_TRANSFER);
					logger.info("直接支付，流水落地。flowId={}", rechargeCash.getPaymentFlowId());
				}
			}
			if (PaymentConstants.CONFIRM_PAYMENT.equals(rechargeCash.getOperateType())) {// 如果是确认支付，更新流水状态
				if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
					String flowId = payResult.get("flowId").toString();
					if(flowId.equals(rechargeCash.getPaymentFlowId())){
						logger.info("第三方返回的订单号和自己记录订单号不一致，以第三方订单号为准，并不予更新订单。第三方订单号: {},平台订单号: {}",flowId,rechargeCash.getPaymentFlowId());
						return new ResponseEntity<>(SUCCESS, payResult);
					}
					FinPaymentRecord record = this.finPaymentRecordDao.findFinPaymentRecordByFlowId(flowId);
					logger.info("确认支付，判断流水状态， record.state={}, flowId={}",record.getState(),flowId);
					if(record !=null && record.getState() == TradeStateConstants.PENDING_PAYMENT){
						FinPaymentRecord finpaymentRecord = new FinPaymentRecord();
						finpaymentRecord.setFlowId(rechargeCash.getPaymentFlowId());
						finpaymentRecord.setState(TradeStateConstants.BANK_TRANSFER);
						this.finPaymentRecordDao.updateByFlowId(finpaymentRecord);
						logger.info("确认支付，流水状态更新。flowId={}", flowId);
					}
				}
			}
			return new ResponseEntity<>(SUCCESS, payResult);
		} catch (Exception e) {
			logger.error("充值业务, 用户标识: {}, 充值入参: rechargeCash: {}, 充值异常信息: ", rechargeCash.toString(),
					rechargeCash.getUserId(), e);
			throw new GeneralException("充值失败!");
		}
	}
	
	
	/**
	 * 
	 * @Description : 调用第三方接口
	 * @Method_Name : callThirdPay
	 * @param rechargeCash
	 *            充值数据
	 * @throws Exception
	 * @return : String
	 * @Creation Date : 2018-05-14 11:26:58
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	public Map<String, Object> callThirdPay(String payMethodName, Map<String, Object> map) {
		// FinAccount finAccount = (FinAccount) map.get("finAccount");
		RechargeCash rechargeCash = (RechargeCash) map.get("rechargeCash");
		FinPayConfig payConfig = (FinPayConfig) map.get("finPayConfig");
		try {
			String payChannel = payConfig.getThirdNameCode().substring(0, 1).toUpperCase()
					+ payConfig.getThirdNameCode().substring(1);
			if (PayChannelEnum.BaoFuProtocolB.getChannelKey().equals(payChannel)) {
				payChannel = PayChannelEnum.BaoFuProtocol.getChannelKey();
			}
			// 反射方式创建参数支付名称的类;
			Class<?> thirdPayClass = Class.forName("com.hongkun.finance.payment.factory." + payChannel + "PayFactory");
			/**
			 * 6、通过反射根据不同支付渠道，调用不同支付方法
			 */
			Method payMethod = thirdPayClass.getDeclaredMethod(payMethodName, RechargeCash.class, FinPayConfig.class);
			payMethod.setAccessible(true);
			// 获得参数Object
			Object[] arguments = new Object[] { rechargeCash, payConfig };
			/**
			 * 7、调用第三方接口，并返回同步数据
			 */
			Map<String, Object> payResult = (Map<String, Object>) payMethod.invoke(thirdPayClass.newInstance(),
					arguments);
			return payResult;
		} catch (Exception e) {
			logger.error("调用第三方接口异常, 用户标识: {}, 充值入参: rechargeCash: {}, 异常信息: ", rechargeCash.getUserId(),
					JsonUtils.toJson(rechargeCash), e);
			throw new GeneralException("调用第三方接口异常!");
		}
	}

	/**
	 * 
	 * @Description : 根据操作类型，获取宝付api方法名
	 * @Method_Name : protrocalPayName
	 * @param rechargeCash
	 *            充值数据
	 * @throws Exception
	 * @return : String
	 * @Creation Date : 2018-05-14 11:26:58
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	public String protrocalPayName(RechargeCash rechargeCash) {
		String result = "";
		switch (rechargeCash.getOperateType().intValue()) {
		case 1:// 预绑卡
			result = "paymentPreTiedCard";
			break;
		case 2:// 确认绑卡
			result = "paymentConfirmTiedCard";
			break;
		case 3:// 直接支付
			result = "paymentDirectPay";
			break;
		case 4:// 预支付
			result = "paymentPrePay";
			break;
		case 5:// 确认支付
			result = "paymentConfirmPay";
			break;
		default:
			logger.info("宝付api充值, 用户标识: {}, 充值流水标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
					rechargeCash.getPaymentFlowId(), "没有相应的充值操作方法！");
			break;
		}
		return result;
	}

	/**
	 * 
	 * @Description : 首次绑卡充值,银行卡校验所有渠道已连连认证方式为主
	 * @Method_Name : firstRecharge
	 * @param rechargeCash
	 * @throws Exception
	 * @return : ResponseEntity
	 * @Creation Date : 2017年7月5日 上午11:39:58
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	@SuppressWarnings("unchecked")
	private ResponseEntity<?> firstRecharge(RechargeCash rechargeCash) throws Exception {
		// 如果是网银充值，则直接返回
		if (rechargeCash.getPayStyle().equalsIgnoreCase(PayStyleEnum.WY.getType())) {
			return new ResponseEntity<>(SUCCESS);
		}
		//认证充值判断用户有没有绑定过卡
		FinBankCard finBankCard = null;
		if(rechargeCash.getBankCardId() != null){
			finBankCard = (FinBankCard)finBankCardDao.findByPK(new Long(rechargeCash.getBankCardId()), FinBankCard.class);
		}else{
			finBankCard = finBankCardDao.findByCradNo(rechargeCash.getBankCard(), rechargeCash.getUserId()); 
		}
		//该充值没有绑卡信息，或者是 在卡未认证情况下绑卡信息不一致，则重新校验卡信息 并更新绑卡信息
		if (finBankCard == null || 
				(!rechargeCash.getBankCard().equals(finBankCard.getBankCard()) 
						&& finBankCard.getState() != BankConstants.BANK_CARD_YES_RZ)) {
			// 首次绑卡认证充值，查询银行cardBin接口，校验卡是否存在
			String key = rechargeCash.getSystemTypeName() + PlatformSourceEnums.PC.getType()
					+ rechargeCash.getPayChannel() + PayStyleEnum.KBIN.getType();
			FinPayConfig finPayConfig = finPayConfigDao.findPayConfigInfo(key, rechargeCash.getSystemTypeName(),
					PlatformSourceEnums.PC.getType(), rechargeCash.getPayChannel(), PayStyleEnum.KBIN.getType());
			
			ResponseEntity<?> cardRes = ThirdPaymentUtil.findCardBin(rechargeCash.getBankCard(), finPayConfig);
			if (cardRes == null || cardRes.getResStatus() == ERROR) {
				return new ResponseEntity<>(ERROR, "该银行维护中，暂不支持！");
			}
			Map<String, Object> bankMap = (Map<String, Object>) cardRes.getParams().get("cardBin");
			// 插入用户银行卡信息
			FinBankCard finBankCardNew = new FinBankCard();
			finBankCardNew.setBankCard(rechargeCash.getBankCard());
			// 根据第三方银行CODE，支付渠道，支付方式，用户类型，银行编码，状态 查询平台银行编码信息
			String bankKey = rechargeCash.getPayChannel() + rechargeCash.getPayStyle() + rechargeCash.getuType()
					+ bankMap.get("bankCode").toString() + TradeStateConstants.START_USING_STATE;
			FinBankRefer bankRefer = finBankReferDao.findBankRefer(bankKey, rechargeCash.getPayChannel(),
					rechargeCash.getPayStyle(), String.valueOf(rechargeCash.getuType()), null,
					TradeStateConstants.START_USING_STATE, bankMap.get("bankCode").toString());
			if (bankRefer == null) {
				return new ResponseEntity<>(ERROR, "第三方支付返回银行编码为空,充值失败！");
			}
			finBankCardNew.setBankCode(bankRefer.getBankCode());
			finBankCardNew.setBankName(bankMap.get("bankName").toString());
			finBankCardNew.setRegUserId(rechargeCash.getUserId());
			finBankCardNew.setState(BankConstants.BANK_CARD_NO_RZ);
			if(finBankCard != null && !rechargeCash.getBankCard().equals(finBankCard.getBankCard())){
				logger.info("用户假绑卡记录信息和新充值不一致 则更新,用户id:{},登录手机号:{},原始卡号: {},新卡号: {}" ,rechargeCash.getUserId(), 
						rechargeCash.getLoginTel(),finBankCard.getBankCard(), rechargeCash.getBankCard());
				finBankCardNew.setBankCard(rechargeCash.getBankCard());
				finBankCardNew.setId(rechargeCash.getBankCardId());
				finBankCardDao.update(finBankCardNew);
			}else{
				finBankCardDao.save(finBankCardNew);
			}
			finBankCard = finBankCardNew;
		}
		rechargeCash.setBankCode(finBankCard.getBankCode());
		rechargeCash.setBankCardId(finBankCard.getId());
		return new ResponseEntity<>(SUCCESS).addParam("finBankCard", finBankCard);
	}
	
	@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> cashPayForCancel(FinTradeFlow finTradeFlow, Integer transferSubCode) {
		logger.info("try cancel cashPay, 用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}",
				finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString());
		try {
			/**
			 * 1、查询交易流水是否存在，如果存在，则删除
			 */
			FinTradeFlow finTradeFlowTmp = this.finTradeFlowDao.findByFlowId(finTradeFlow.getFlowId());
			if (finTradeFlowTmp != null) {
				// 删除流水
				finTradeFlowDao.deleteByFlowId(finTradeFlow.getFlowId());
				FinFundtransfer transfterCdt = new FinFundtransfer();
				transfterCdt.setTradeFlowId(finTradeFlowTmp.getFlowId( ));
				/**
				 * 2、 根据交易流水的flowId,查询资金划转是否存在，如果存在，则删除
				 */
				List<FinFundtransfer> transferList = this.finFundtransferDao.findByCondition(transfterCdt);
				if (CommonUtils.isNotEmpty(transferList)) {
					// 根据交易流水的flowID,删除资金划转
					finFundtransferDao.deleteByPflowId(finTradeFlow.getFlowId());
					/**
					 * 3、 根据资金划转的subCod，更新用户账户金额
					 */
					FinAccount newFcccount = new FinAccount();
					newFcccount.setFreezeMoney(BigDecimal.ZERO);
					newFcccount.setUseableMoney(BigDecimal.ZERO);
					newFcccount.setNowMoney(BigDecimal.ZERO);
					newFcccount.setRegUserId(finTradeFlow.getRegUserId());
					// 根据资金划转subCode初始化待更新账户金额对象
					ResponseEntity<?> resResult = dealRollBackTransferAccountBySubcode(transferSubCode,
							finTradeFlow.getTransMoney(), newFcccount);
					if (resResult.getResStatus() == ERROR) {
						logger.error("现金消费回滚操作, 用户标识: {}, 根据资金划转subCode初始化待更新账户金额对象异常: {}", finTradeFlow.getRegUserId(),
								resResult.getResMsg().toString());
						throw new BusinessException("根据资金划转subCode初始化待更新账户金额对象异常!");
					}
					// 执行更新账户金额操作
					ResponseEntity<?> accountResult = updateFinAccount(newFcccount);
					if (accountResult.getResStatus() == ERROR) {
						logger.error("现金消费回滚操作, 用户标识: {}, 根据资金划转subCode更新账户金额异常: {}", finTradeFlow.getRegUserId(),
								accountResult.getResMsg().toString());
						throw new BusinessException("根据资金划转subCode更新账户金额失败");
					}
					return accountResult;
				}
			}
			return new ResponseEntity<>(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("try cancel cashPay, 用户账户现金消费, 用户标识: {}, 入参: transferSubCode: {}, finTradeFlow: {}",
					finTradeFlow.getRegUserId(), transferSubCode, finTradeFlow.toString(), e);
			throw new GeneralException("账户现金消费失败");
		}
	}

	@Override
	@Compensable(cancelMethod = "rollBackAccountAndTradeTransfer", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateAccountInsertTradeAndTransfer(FinTradeFlow tradeFlow,
			List<FinFundtransfer> transfersList) {
		logger.info("{}. 根据资金划转的subCode, 更新用户账户信息, 插入一条流水,多笔资金划转. 交易流水中用户标识: {}, tradeFlow: {}, transfersList: {}",
				BaseUtil.getTccTryLogPrefix(), tradeFlow.getRegUserId(), tradeFlow.toString(),
				JsonUtils.toJson(transfersList));
		try {
			/**
			 * 1、 如果交易流水对象不为空，并且交易流水的flowId不为空，则插入流水对象
			 */
			if (tradeFlow != null && StringUtils.isNotBlank(tradeFlow.getFlowId())) {
				finTradeFlowDao.save(tradeFlow);
			}
			/**
			 * 2、更新账户，插入资金划转
			 */
			ResponseEntity<?> result = dealAccountAndTransers(tradeFlow.getTradeType(), transfersList);
			if (result.getResStatus() == ERROR) {
				logger.error("根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转, 用户标识: {}, 处理失败: {}", tradeFlow.getRegUserId(),
						result.getResMsg().toString());
				throw new BusinessException("根据资金划转subcode,更新账户插入流水及资金划转流水失败");
			}
		} catch (Exception e) {
			logger.error("{}. 根据资金划转的subCode, 更新用户账户信息, 插入一条流水,多笔资金划转. 交易流水中用户标识: {}, tradeFlow: {}, transfersList: {}",
					BaseUtil.getTccTryLogPrefix(), tradeFlow.getRegUserId(), tradeFlow.toString(),
					JsonUtils.toJson(transfersList), e);
			throw new GeneralException("更新账户,生成一条流水,多笔资金划转失败:" + CommonUtils.printStackTraceToString(e));
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> rollBackAccountAndTradeTransfer(FinTradeFlow tradeFlow,
			List<FinFundtransfer> transfersList) {
		logger.info(
				"tcc cancel updateAccountInsertTradeAndTransfer, 根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转. 用户标识: {}, tradeFlow: {}, transfersList: {}",
				tradeFlow.getRegUserId(), tradeFlow.toString(), JsonUtils.toJson(transfersList));
		// 查询流水信息是否存在
		FinTradeFlow finTradeFlow = finTradeFlowDao.findByFlowId(tradeFlow.getFlowId());
		if (finTradeFlow != null) {
			finTradeFlowDao.deleteByFlowId(tradeFlow.getFlowId());
		}
		// 更新账户，删除交易流水
		dealRollBackAccountTransfer(transfersList);
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description :根据资金划转SUBCODE，初始化处理待更新的账户金额
	 * @Method_Name : dealTransferAccountBySubcode;
	 * @param subCode
	 *            资金划转SUBCODE
	 * @param transMoney
	 *            交易金额
	 * @param regUserAccount
	 *            账户
	 * @return : ResponseEntity;
	 * @Creation Date : 2018年3月23日 下午6:01:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> dealTransferAccountBySubcode(Integer subCode, BigDecimal transMoney,
			FinAccount regUserAccount) {
		// 如果用户资金划转为属于支出类型，则可用余额减少，nowMoney减少
		if (transferPayOut(subCode)) {
			// 如何资金划转类型为支出冻结，则冻结金额减少,nowMoney减少
			if (subCode.equals(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
					FundtransferSmallTypeStateEnum.FROZEN))) {
				regUserAccount.setFreezeMoney(regUserAccount.getFreezeMoney().add(transMoney.negate()));
				regUserAccount.setNowMoney(regUserAccount.getNowMoney().add(transMoney.negate()));
			} else {
				// 可用余额减少，nowMoney减少
				regUserAccount.setUseableMoney(regUserAccount.getUseableMoney().add(transMoney.negate()));
				regUserAccount.setNowMoney(regUserAccount.getNowMoney().add(transMoney.negate()));
			}
		} else if (getBigTypeSubCodeByTransferSubCode(subCode) == FundtransferBigTypeStateEnum.FREEZE
				.getBigTransferType()) {
			// 如果资金划转类型为冻结，则可用余额减少，冻结增加
			regUserAccount.setUseableMoney(regUserAccount.getUseableMoney().add(transMoney.negate()));
			regUserAccount.setFreezeMoney(regUserAccount.getFreezeMoney().add(transMoney));
		} else if (getBigTypeSubCodeByTransferSubCode(subCode) == FundtransferBigTypeStateEnum.THAW
				.getBigTransferType()) {
			// 如果资金划转类型为解冻，可用余额增加，冻结金额减少
			regUserAccount.setUseableMoney(regUserAccount.getUseableMoney().add(transMoney));
			regUserAccount.setFreezeMoney(regUserAccount.getFreezeMoney().add(transMoney.negate()));
		} else if (transferIncome(subCode)) {
		    //如果subcode是收入物业费，则冻结金额增加，NOWMONEY增加
		    if (subCode.equals(TradeTransferConstants.getFundTransferSubCodeByType(
                    FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.PROPERTY_MONEY))) {
		        regUserAccount.setFreezeMoney(regUserAccount.getFreezeMoney().add(transMoney));
		        regUserAccount.setNowMoney(regUserAccount.getNowMoney().add(transMoney));
            }else{
    			// 如果资划转类型属于收入类型，则可用余额增加，nowMoney增加
    			regUserAccount.setUseableMoney(regUserAccount.getUseableMoney().add(transMoney));
    			regUserAccount.setNowMoney(regUserAccount.getNowMoney().add(transMoney));
            }
		} else {
			logger.error("dealTransferAccountBySubcode subCode 格式化有误!");
			return new ResponseEntity<>(ERROR, "资金划转subCode格式有误!");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description :根据资金划转SUBCODE,初始化回滚用户账户金额
	 * @Method_Name : dealRollBackTransferBySubcode;
	 * @param transferSubCode
	 *            : 资金划转
	 * @param finAccount
	 *            : 账户
	 * @return : ResponseEntity;
	 * @Creation Date : 2018年3月23日 下午5:23:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> dealRollBackTransferAccountBySubcode(Integer transferSubCode, BigDecimal transMoney,
			FinAccount finAccount) {
		// 如果subCode大类型是支出，更新用户的可用余额，和nowMoney.
		if (transferPayOut(transferSubCode)) {
			if (TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
					FundtransferSmallTypeStateEnum.FROZEN).equals(transferSubCode)) {
				finAccount.setFreezeMoney(finAccount.getFreezeMoney().add(transMoney));
				finAccount.setNowMoney(finAccount.getNowMoney().add(transMoney));
			} else {
				finAccount.setUseableMoney(finAccount.getUseableMoney().add(transMoney));
				finAccount.setNowMoney(finAccount.getNowMoney().add(transMoney));
			}
		} else if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.FREEZE
				.getBigTransferType()) {
			// 如果subCode大类型是冻结，则取返，更新用户的可用余额和冻结金额
			finAccount.setUseableMoney(finAccount.getUseableMoney().add(transMoney));
			finAccount.setFreezeMoney(finAccount.getFreezeMoney().add(transMoney.negate()));
		} else if (getBigTypeSubCodeByTransferSubCode(transferSubCode) == FundtransferBigTypeStateEnum.THAW
				.getBigTransferType()) {
			// 如果subCode大类型是解冻,则取返，更新用户的可用余额和冻结金额
			finAccount.setUseableMoney(finAccount.getUseableMoney().add(transMoney.negate()));
			finAccount.setFreezeMoney(finAccount.getFreezeMoney().add(transMoney));
		} else if (transferIncome(transferSubCode)) {
            //如果资金划转subcode为收入物业费，则取反，冻结金额减少，nowMoney减少
            if (transferSubCode.equals(TradeTransferConstants.getFundTransferSubCodeByType(
                    FundtransferBigTypeStateEnum.INCOME, FundtransferSmallTypeStateEnum.PROPERTY_MONEY))) {
                finAccount.setFreezeMoney(finAccount.getFreezeMoney().add(transMoney.negate()));
                finAccount.setNowMoney(finAccount.getNowMoney().add(transMoney.negate()));
            }else{
    			// 如果subCode大类型是收入，则取返，更新用户的可用余额，和nowMoney.
    			finAccount.setUseableMoney(finAccount.getUseableMoney().add(transMoney.negate()));
    			finAccount.setNowMoney(finAccount.getNowMoney().add(transMoney.negate()));
            }
		} else {
			return new ResponseEntity<>(ERROR, "资金划转subCode格式有误!");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description : 封装宝付充值数据
	 * @Method_Name : baofuReqPay;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            账户信息
	 * @param payConfig
	 *            宝付充值配置文件数据
	 * @return
	 * @return : Map<String,Object>;
	 * @throws Exception
	 * @Creation Date : 2018年1月12日 下午1:55:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public Map<String, Object> baofuReqPay(RechargeCash rechargeCash, FinAccount finAccount, FinPayConfig payConfig)
			throws Exception {
		logger.info("baofuReqPay宝付充值组装对象入参rechargeCash：{},finAccount:{},payConfig:{}", rechargeCash.toString(),
				finAccount.toString(), payConfig.toString());
		Map<String, Object> payParams = null;
		switch (PlatformSourceEnums.valueByType(rechargeCash.getPlatformSourceName())) {
		case 10:// PC
			payParams = BaofuPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		case 13:// wap
			payParams = BaofuPayFactory.paymentWapInfo(rechargeCash, finAccount, payConfig);
			break;
		case 11:// IOS
			payParams = BaofuPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		case 12:// ANDROID
			payParams = BaofuPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		default:
			payParams = null;
			logger.info("宝付充值, 用户标识: {}, 充值流水标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
					rechargeCash.getPaymentFlowId(), "没有此平台来源！");
			break;
		}
		return payParams;
	}
	/**
	 * @Description : 封装宝付协议支付充值数据
	 * @Method_Name : baofuReqPay;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            账户信息
	 * @param payConfig
	 *            宝付充值配置文件数据
	 * @return
	 * @return : Map<String,Object>;
	 * @throws Exception
	 * @Creation Date : 2018年1月12日 下午1:55:38;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public Map<String, Object> baofuProtocolReqPay(RechargeCash rechargeCash, FinAccount finAccount, FinPayConfig payConfig)
			throws Exception {
		logger.info("baofuProtocolReqPay 宝付充值组装对象入参rechargeCash：{},finAccount:{},payConfig:{}", rechargeCash.toString(),
				finAccount.toString(), payConfig.toString());
		Map<String, Object> payParams = null;
		switch (PlatformSourceEnums.valueByType(rechargeCash.getPlatformSourceName())) {
		case 10:// PC
			payParams = BaofuPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		case 13:// wap
			payParams = BaofuPayFactory.paymentWapInfo(rechargeCash, finAccount, payConfig);
			break;
		case 11:// IOS
			payParams = BaofuPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		case 12:// ANDROID
			payParams = BaofuPayFactory.paymentAppInfo(rechargeCash, finAccount, payConfig);
			break;
		default:
			payParams = null;
			logger.info("宝付充值, 用户标识: {}, 充值流水标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
					rechargeCash.getPaymentFlowId(), "没有此平台来源！");
			break;
		}
		return payParams;
	}
	/**
	 * @Description : 封装易宝支付充值数据
	 * @Method_Name : baofuReqPay;
	 * @param rechargeCash
	 *            充值对象
	 * @param finAccount
	 *            账户信息
	 * @param payConfig
	 *            宝付充值配置文件数据
	 * @return
	 * @return : Map<String,Object>;
	 * @throws Exception
	 * @Creation Date : 2018年10月23日 下午5:30:29;
	 * @Author : binliang@hongkun.com.cn 梁彬;
	 */
	public Map<String, Object> yeepayReqPay(RechargeCash rechargeCash, FinAccount finAccount, FinPayConfig payConfig)
			throws Exception {
		logger.info("yeepayReqPay 易宝充值组装对象入参rechargeCash：{},finAccount:{},payConfig:{}", rechargeCash.toString(),
				finAccount.toString(), payConfig.toString());
		Map<String, Object> payParams = null;
		switch (PlatformSourceEnums.valueByType(rechargeCash.getPlatformSourceName())) {
		case 10:// PC
			payParams = YeepayPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		case 13:// wap
			payParams = YeepayPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		case 11:// IOS
			payParams = YeepayPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		case 12:// ANDROID
			payParams = YeepayPayFactory.paymentPcInfo(rechargeCash, finAccount, payConfig);
			break;
		default:
			payParams = null;
			logger.info("宝付充值, 用户标识: {}, 充值流水标识: {}, 充值异常信息: {}", rechargeCash.getUserId(),
					rechargeCash.getPaymentFlowId(), "没有此平台来源！");
			break;
		}
		return payParams;
	}
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> auditWithdrawals(Integer id, SystemTypeEnums systemTypeEnums) {
		logger.info("auditWithdrawals: 运营审核提现, id: {}, systemTypeEnums: {}", id, systemTypeEnums);
		try {
			FinPaymentRecord record = finPaymentRecordDao.findByPK(Long.valueOf(id), FinPaymentRecord.class);
			if (record == null) {
				return new ResponseEntity<>(Constants.ERROR, "没有查询到提现信息");
			}
			if (record.getState() != TradeStateConstants.PENDING_PAYMENT) {
				return new ResponseEntity<>(Constants.ERROR, "该提现信息状态不对,请核查");
			}
			// 检查审核信息
			if (checkWithdrawals(record)) {
				// 修改审核状态为待放款
				FinPaymentRecord tempFlow = new FinPaymentRecord();
				tempFlow.setFlowId(record.getFlowId());
				tempFlow.setState(TradeStateConstants.WAIT_PAY_MONEY);
				// 修改提现通道为默认通道
				FinChannelGrant payChannel = finChannelGrantDao.findFirstFinChannelGrant(systemTypeEnums,
						PlatformSourceEnums.typeByValue(record.getTradeSource()), PayStyleEnum.WITHDRAW);
				if (payChannel == null) {
					return new ResponseEntity<>(Constants.ERROR, "当前暂无启用的提现渠道!");
				}
				tempFlow.setPayChannel(payChannel.getChannelNameCode());
				finPaymentRecordDao.updateByFlowId(tempFlow);
				return new ResponseEntity<>(Constants.SUCCESS, "审核成功");
			} else {
				return new ResponseEntity<>(Constants.ERROR, "用户账户收支总额钱数不一致，请检查！");
			}
		} catch (Exception e) {
			logger.info("auditWithdrawals, 运营审核提现, id: {}, 异常信息: ", id, e);
			throw new BusinessException("运营审核提现出现异常");
		}

	}

	/**
	 * @Description :校验对账信息是否合法
	 * @Method_Name : checkWithdrawals;
	 * @param record
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2018年6月1日 上午10:55:08;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private boolean checkWithdrawals(FinPaymentRecord record) {
		return true;//和线上保持一致，对账结果仅作为参考，不作为通过标准
//		FinFundtransfer cdt = new FinFundtransfer();
//		// 查询收入金额
//		cdt.setSubCode(10);
//		cdt.setRegUserId(record.getRegUserId());
//		BigDecimal inMoney = finFundtransferDao.findFintransferSumMoney(cdt);
//		cdt.setSubCode(20);
//		// 查询支出金额
//		BigDecimal outMoney = finFundtransferDao.findFintransferSumMoney(cdt);
//		FinAccount finAccount = finAccountDao.findByRegUserId(record.getRegUserId());
//		if (finAccount != null) {
//			outMoney = outMoney.add(finAccount.getUseableMoney()).add(finAccount.getFreezeMoney());
//		}
//		// 判断收入金额是否和支出金额相等
//		if (CompareUtil.eq(inMoney, outMoney)) {
//			return true;
//		}
//		return false;
	}

	@Override
	@Compensable(cancelMethod = "dealCreditorMatchFlowsCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public String dealCreditorMatchFlows(int qdzRegUserBiddInvestId, int qdzRegUserId, int thirdRegUserId,
			BigDecimal userTransMoney, Date repairTime, int creditorFlag) {
		logger.info(
				"dealCreditorMatchFlows处理债券匹配流水账户:{} 钱袋子投资记录ID:{} 钱袋子账户ID:{} 第三方账户ID:{} " + "债转金额:{} 处理日期:{} 债转标是:{} ",
				qdzRegUserBiddInvestId, qdzRegUserId, thirdRegUserId, userTransMoney, repairTime, creditorFlag);
		// 生成流水、资金划转、更新第三方账户金额
		FinTradeFlow flow = FinTFUtil.initFinTradeFlow(qdzRegUserId, String.valueOf(qdzRegUserBiddInvestId),
				userTransMoney, TRADE_TYPE_CREDITOR_TRANSFER_AUTO, PlatformSourceEnums.PC);
		flow.setModifyTime(repairTime);
		List<FinFundtransfer> finFundtransfers = new ArrayList<>();
		// 钱袋子用户划转
		FinFundtransfer qdzFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(), qdzRegUserId, null,
				userTransMoney, TRANSFER_SUB_CODE_TURNS_OUT_QDZ_CREDITOR);
		FinFundtransfer qdzFinFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(), qdzRegUserId, null,
				userTransMoney, TRANSFER_SUB_CODE_CREDITOR_TRANSFER_INCOME);
		// 第三方用户债转划转
		FinFundtransfer thirdFundtransfer = FinTFUtil.initFinFundtransfer(flow.getFlowId(), thirdRegUserId, null,
				userTransMoney, TRANSFER_SUB_CODE_PAY);
		finFundtransfers.add(qdzFundtransfer);
		finFundtransfers.add(qdzFinFundtransfer);
		finFundtransfers.add(thirdFundtransfer);
		this.updateAccountInsertTradeAndTransfer(flow, finFundtransfers);
		return flow.getFlowId();
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void dealCreditorMatchFlowsCancel(int qdzRegUserBiddInvestId, int qdzRegUserId, int thirdRegUserId,
			BigDecimal userTransMoney, Date repairTime, int creditorFlag) {
		logger.info(
				"dealCreditorMatchFlows处理债券匹配流水账户:{} 钱袋子投资记录ID:{} 钱袋子账户ID:{} 第三方账户ID:{} " + "债转金额:{} 处理日期:{} 债转标是:{} ",
				qdzRegUserBiddInvestId, qdzRegUserId, thirdRegUserId, userTransMoney, repairTime, creditorFlag);
		try {
			// 更新账户
			FinAccount thirdAccount = new FinAccount();
			if (creditorFlag == CREDITOR_FLAG_BUY) {
				userTransMoney = userTransMoney.multiply(BigDecimal.valueOf(-1));
			}
			thirdAccount.setRegUserId(thirdRegUserId);
			thirdAccount.setNowMoney(userTransMoney);
			thirdAccount.setUseableMoney(userTransMoney);
			FinTradeFlow tradeFlow = finTradeFlowDao.findByFlowId(String.valueOf(qdzRegUserBiddInvestId),
					TRADE_TYPE_CREDITOR_TRANSFER_AUTO);
			// 删除流水、资金划转
			this.rollBackAccountAndDelFlows(thirdAccount, tradeFlow.getFlowId());
		} catch (Exception e) {
			logger.error(
					"dealCreditorMatchFlows处理债券匹配流水账户:{} 钱袋子投资记录ID:{} 钱袋子账户ID:{} 第三方账户ID:{} "
							+ "债转金额:{} 处理日期:{} 债转标是:{} ",
					qdzRegUserBiddInvestId, qdzRegUserId, thirdRegUserId, userTransMoney, repairTime, creditorFlag);
			throw new BusinessException("钱袋子债券流水回滚异常！");
		}
	}

	@Compensable(cancelMethod = "rollBackAccountTransfer", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateAccountInsertTransfer(Integer tradeType, List<FinFundtransfer> transfersList) {
		logger.info("{}, 根据资金划转subcode,更新账户插入资金划转流水, 入参: tradeType: {}, transfersList: {}",
				BaseUtil.getTccTryLogPrefix(), tradeType, JsonUtils.toJson(transfersList));
		try {
			// 根据资金划转subcode更新账户，插入资金划转流水
			ResponseEntity<?> result = dealAccountAndTransers(tradeType, transfersList);
			if (result.getResStatus() == ERROR) {
				throw new BusinessException("根据资金划转subcode,更新账户插入资金划转流水失败");
			}
		} catch (Exception e) {
			logger.error("{}, 根据资金划转subcode,更新账户插入资金划转流水, 入参: tradeType: {}, transfersList: {}, 处理失败: ",
					BaseUtil.getTccTryLogPrefix(), JsonUtils.toJson(transfersList), e);
			throw new GeneralException("根据资金划转subcode,更新账户插入资金划转流水失败");
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description : 根据资金划转subcode,更新账户插入资金划转流水(回滚方法)
	 * @Method_Name : rollBackAccountTransfer;
	 * @param tradeType
	 *            交易类型
	 * @param transfersList
	 *            资金划转LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月19日 下午3:40:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> rollBackAccountTransfer(Integer tradeType, List<FinFundtransfer> transfersList) {
		logger.info(
				"tcc cancel updateAccountInsertTransfer, 根据资金划转subcode,更新账户插入资金划转流水(回滚方法), 入参: tradeType: {}, transfersList: {}",
				tradeType, JsonUtils.toJson(transfersList));
		dealRollBackAccountTransfer(transfersList);
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description : 根据资金划转subcode更新账户，插入资金划转流水
	 * @Method_Name : dealAccountAndTransers;
	 * @param tradeType
	 *            交易流水类型
	 * @param transfersList
	 *            资金划转LIST
	 * @return
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2018年4月19日 下午2:55:50;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private ResponseEntity<?> dealAccountAndTransers(Integer tradeType, List<FinFundtransfer> transfersList) {
		logger.info("方法: dealAccountAndTransers, 根据资金划转subcode更新账户,插入资金划转流水, 入参: transfersList: {}",
				JsonUtils.toJson(transfersList));
		// 用于存放每一个用户需要更新的账户信息(即最后要更新的账户金额信息)
		Map<Integer, FinAccount> regUserMap = new HashMap<Integer, FinAccount>();
		// 用于存放每个用户的资金划转的List(即一个用户有几条资金划转)
		Map<Integer, List<FinFundtransfer>> regUserTransferMap = new HashMap<Integer, List<FinFundtransfer>>();
		/***
		 * 1、遍历资金划转集合，(1)遍历出每个用户需要更新的账户Map
		 * <regUserId,FinAccount>,(2)遍历出每一个用户有几条资金划转Map<Integer, List
		 * <FinFundtransfer>>
		 */
		for (FinFundtransfer fundtransfer : transfersList) {
			List<FinFundtransfer> regUserList = null; // 用于存放用户的资金划转LIST
			FinAccount regUserAccount = null;// 用户需要更新的账户信息
			fundtransfer.setTradeType(tradeType);
			if (regUserMap.get(fundtransfer.getRegUserId()) == null) {
				regUserAccount = new FinAccount();
				regUserAccount.setFreezeMoney(BigDecimal.ZERO);
				regUserAccount.setUseableMoney(BigDecimal.ZERO);
				regUserAccount.setNowMoney(BigDecimal.ZERO);
				regUserAccount.setRegUserId(fundtransfer.getRegUserId());
				regUserList = new LinkedList<FinFundtransfer>();
			} else {
				regUserAccount = regUserMap.get(fundtransfer.getRegUserId());
				regUserList = regUserTransferMap.get(fundtransfer.getRegUserId());
			}
			// 根据SUBCODE，处理用户待更新的账户金额
			ResponseEntity<?> result = dealTransferAccountBySubcode(fundtransfer.getSubCode(),
					fundtransfer.getTransMoney(), regUserAccount);
			if (result.getResStatus() == ERROR) {
				logger.error("根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转, 资金划转中用户标识: {}, 异常信息: {}",
						fundtransfer.getRegUserId(), result.getResMsg().toString());
				return result;
			}
			regUserMap.put(fundtransfer.getRegUserId(), regUserAccount);
			regUserList.add(fundtransfer);
			regUserTransferMap.put(fundtransfer.getRegUserId(), regUserList);
		}
		/**
		 * 2、遍历每一个用户要更新的资金划转的集合Map<regUserId,List
		 * <FinFundtransfer>>,更新用户账户，计算资金划转的preMoney,nowMoney金额
		 */
		for (Integer regUserId : regUserTransferMap.keySet()) {
			List<FinFundtransfer> fundtransferList = regUserTransferMap.get(regUserId);
			// 更新发起人账户金额信息
			ResponseEntity<?> orgUpdateResult = updateFinAccount(regUserMap.get(regUserId));
			if (orgUpdateResult.getResStatus() == ERROR) {
				logger.error("根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转, 用户标识: {}, 异常信息: {}", regUserId,
						orgUpdateResult.getResMsg().toString());
				throw new BusinessException(orgUpdateResult.getResMsg().toString());
			}
			FinAccount orgFinAccount = (FinAccount) orgUpdateResult.getResMsg();
			// 计算资金划转的preMoney,nowMoney金额
			BigDecimal nowMoney = orgFinAccount.getNowMoney();
			BigDecimal preMoney = orgFinAccount.getNowMoney();
			BigDecimal afterMoney = orgFinAccount.getUseableMoney();
			for (FinFundtransfer transfer : fundtransferList) {
				// 根据subcode判断是支出还是收入，计算preMoney,NowMoney
				if (transferPayOut(transfer.getSubCode())) {
					if (TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
							FundtransferSmallTypeStateEnum.FROZEN).equals(transfer.getSubCode())) {
		                   nowMoney = nowMoney.add(transfer.getTransMoney().negate());
					}else{
	                       afterMoney = afterMoney.add(transfer.getTransMoney().negate());
					}
				} else if (transferIncome(transfer.getSubCode())) {
				    if (TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
                            FundtransferSmallTypeStateEnum.PROPERTY_MONEY).equals(transfer.getSubCode())) {
				        nowMoney = nowMoney.add(transfer.getTransMoney());
                    }else{
    					nowMoney = nowMoney.add(transfer.getTransMoney());
    					afterMoney = afterMoney.add(transfer.getTransMoney());
                    }
				} else if (getBigTypeSubCodeByTransferSubCode(
						transfer.getSubCode()) == FundtransferBigTypeStateEnum.FREEZE.getBigTransferType()) {
					afterMoney = afterMoney.add(transfer.getTransMoney().negate());
				} else if (getBigTypeSubCodeByTransferSubCode(
						transfer.getSubCode()) == FundtransferBigTypeStateEnum.THAW.getBigTransferType()) {
					afterMoney = afterMoney.add(transfer.getTransMoney());
				}
				transfer.setPreMoney(preMoney);
				transfer.setAfterMoney(afterMoney);
				transfer.setNowMoney(nowMoney);
				preMoney = nowMoney;
			}
			finFundtransferDao.insertBatch(FinFundtransfer.class, fundtransferList, fundtransferList.size());
		}
		return new ResponseEntity<>(SUCCESS);
	}

	/**
	 * @Description :根据资金划转subcode,回滚账户信息，及资金划转流水信息
	 * @Method_Name : dealRollBackAccountTransfer;
	 * @param tradeType
	 *            交易类型
	 * @param transfersList
	 *            资金划转LIST
	 * @return : void;
	 * @Creation Date : 2018年4月19日 下午3:34:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private void dealRollBackAccountTransfer(List<FinFundtransfer> transfersList) {
		logger.info("方法: dealRollBackAccountTransfer, 根据资金划转subcode,回滚账户信息及资金划转流水信息, 入参: transfersList: {}",
				JsonUtils.toJson(transfersList));
		// 用于存放每个用户，待更新的账户金额
		Map<Integer, FinAccount> regUserMap = new HashMap<Integer, FinAccount>();
		FinAccount finAccount = null;// 待更新的账户对象
		// 遍历资金划转LIST,根据资金划转SUBCODE，获取每个人待更新的账户金额信息
		for (FinFundtransfer finFundtransfer : transfersList) {
			FinFundtransfer transfer = finFundtransferDao.findTransferByFlowId(finFundtransfer.getFlowId());
			if (transfer != null) {
				if (regUserMap.get(finFundtransfer.getRegUserId()) == null) {
					finAccount = new FinAccount();
					finAccount.setFreezeMoney(BigDecimal.ZERO);
					finAccount.setUseableMoney(BigDecimal.ZERO);
					finAccount.setNowMoney(BigDecimal.ZERO);
					finAccount.setRegUserId(finFundtransfer.getRegUserId());
				} else {
					finAccount = regUserMap.get(finFundtransfer.getRegUserId());
				}
				// 根据资金划转的SUBCODE，初始化待更新用户账户金额对象
				ResponseEntity<?> resResult = dealRollBackTransferAccountBySubcode(finFundtransfer.getSubCode(),
						finFundtransfer.getTransMoney(), finAccount);
				if (resResult.getResStatus() == ERROR) {
					logger.error(
							"根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转回滚操作, 资金划转中用户标识: {}, 根据资金划转的SUBCODE,初始化待更新用户账户金额对象异常信息: {}",
							finFundtransfer.getRegUserId(), resResult.getResMsg().toString());
					throw new BusinessException("根据资金划转的SUBCODE,初始化待更新用户账户金额对象异常!");
				}
				regUserMap.put(finFundtransfer.getRegUserId(), finAccount);
			}
		}
		// 删除一条流水，多笔资金划转
		finFundtransferDao.deleteByFlowIdBatch(
				transfersList.stream().map(FinFundtransfer::getFlowId).collect(Collectors.toList()));
		// 更新用户账户信息
		for (Integer regUserId : regUserMap.keySet()) {
			ResponseEntity<?> updateAccountResult = updateFinAccount(regUserMap.get(regUserId));
			if (updateAccountResult.getResStatus() == ERROR) {
				logger.error("根据资金划转的subCode,更新用户账户信息,插入一条流水,多笔资金划转回滚操作, 用户标识: {}, 根据资金划转的SUBCODE,更新用户账户金额异常信息: {}",
						regUserId, updateAccountResult.getResMsg().toString());
				throw new BusinessException("更新账户失败！");
			}
		}
	}

	@Override
	@Compensable(cancelMethod = "cashPayBatchForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> cashPayBatch(List<FinTradeFlow> finTradeFlowList) {
		logger.info("{}, 用户账户批量现金消费, 用户标识: {}, 入参: finTradeFlowList: {}", BaseUtil.getTccTryLogPrefix(),
				JsonUtils.toJson(finTradeFlowList));
		Map<Integer, FinAccount> regUserMap = new HashMap<Integer, FinAccount>();// 用于存放每个用户对应的待更新账户金额
		Map<Integer, List<FinTradeFlow>> regUserTradeFlowMap = new HashMap<Integer, List<FinTradeFlow>>();// 用于存放每个用户有几条流水对象
		FinTradeFlow tradeFlow = null;
		try {
			/**
			 * 1、插入交易流水
			 */
			finTradeFlowDao.insertBatch(FinTradeFlow.class, finTradeFlowList);
			/**
			 * 2、遍历交易流水信息，用于组装要更新的账户信息
			 */
			for (FinTradeFlow finTradeFlow : finTradeFlowList) {
				List<FinTradeFlow> tradeFlowList = null;
				FinAccount regUserAccount = null;// 用户需要更新的账户信息
				tradeFlow = finTradeFlow;
				if (regUserMap.get(finTradeFlow.getRegUserId()) == null) {
					regUserAccount = new FinAccount();
					regUserAccount.setFreezeMoney(BigDecimal.ZERO);
					regUserAccount.setUseableMoney(BigDecimal.ZERO);
					regUserAccount.setNowMoney(BigDecimal.ZERO);
					regUserAccount.setRegUserId(finTradeFlow.getRegUserId());
					tradeFlowList = new LinkedList<FinTradeFlow>();
				} else {
					regUserAccount = regUserMap.get(finTradeFlow.getRegUserId());
					tradeFlowList = regUserTradeFlowMap.get(finTradeFlow.getRegUserId());
				}
				// 根据SUBCODE，处理用户待更新的账户金额
				ResponseEntity<?> result = dealTransferAccountBySubcode(finTradeFlow.getTransferSubCode(),
						finTradeFlow.getTransMoney(), regUserAccount);
				if (result.getResStatus() == ERROR) {
					logger.error("用户账户批量现金消费, 用户标识: {}, 根据SUBCODE处理用户待更新的账户金额异常信息: {}", finTradeFlow.getRegUserId(),
							result.getResMsg().toString());
					return result;
				}
				regUserMap.put(finTradeFlow.getRegUserId(), regUserAccount);
				tradeFlowList.add(finTradeFlow);
				regUserTradeFlowMap.put(finTradeFlow.getRegUserId(), tradeFlowList);
			}
			/**
			 * 3、遍历每个人对应的交易流水集合，用于组装资金划转，及更新账户
			 */
			for (Integer regUserId : regUserTradeFlowMap.keySet()) {
				List<FinFundtransfer> fundtransferList = new ArrayList<FinFundtransfer>();// 用户存放资金划转LIST
				List<FinTradeFlow> tradeFlowLists = regUserTradeFlowMap.get(regUserId);
				// 更新发起人账户金额信息
				ResponseEntity<?> orgUpdateResult = updateFinAccount(regUserMap.get(regUserId));
				if (orgUpdateResult.getResStatus() == ERROR) {
					logger.error("用户账户批量现金消费, 用户标识: {}, 更新账户异常信息: {}", regUserId,
							orgUpdateResult.getResMsg().toString());
					throw new BusinessException(orgUpdateResult.getResMsg().toString());
				}
				FinAccount orgFinAccount = (FinAccount) orgUpdateResult.getResMsg();
				// 计算资金划转的preMoney,nowMoney金额
				BigDecimal nowMoney = orgFinAccount.getNowMoney();
				BigDecimal preMoney = orgFinAccount.getNowMoney();
				BigDecimal afterMoney = orgFinAccount.getUseableMoney();
				for (FinTradeFlow tradeFlows : tradeFlowLists) {
					// 根据subcode判断是支出还是收入，计算preMoney,NowMoney
					if (transferPayOut(tradeFlows.getTransferSubCode())) {
						if (TradeTransferConstants
								.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY,
										FundtransferSmallTypeStateEnum.FROZEN)
								.equals(tradeFlows.getTransferSubCode())) {
							afterMoney = afterMoney.add(tradeFlows.getTransMoney().negate());
						}else{
		                    nowMoney = nowMoney.add(tradeFlows.getTransMoney().negate());
						}
					} else if (transferIncome(tradeFlows.getTransferSubCode())) {
					    if (TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.INCOME,
	                            FundtransferSmallTypeStateEnum.PROPERTY_MONEY).equals(tradeFlows.getTransferSubCode())) {
	                        nowMoney = nowMoney.add(tradeFlows.getTransMoney());
	                    }else{
    						nowMoney = nowMoney.add(tradeFlows.getTransMoney());
    						afterMoney = afterMoney.add(tradeFlows.getTransMoney());
	                    }
					} else if (getBigTypeSubCodeByTransferSubCode(
							tradeFlows.getTransferSubCode()) == FundtransferBigTypeStateEnum.FREEZE
									.getBigTransferType()) {
						afterMoney = afterMoney.add(tradeFlows.getTransMoney().negate());
					} else if (getBigTypeSubCodeByTransferSubCode(
							tradeFlows.getTransferSubCode()) == FundtransferBigTypeStateEnum.THAW
									.getBigTransferType()) {
						afterMoney = afterMoney.add(tradeFlows.getTransMoney());
					}
					// 组装资金划转对象
					FinFundtransfer finFundtransfer = FinTFUtil.initFinFundtransfer(regUserId, null,
							tradeFlows.getFlowId(), tradeFlows.getTransMoney(), preMoney, nowMoney, afterMoney,
							tradeFlows.getTransferSubCode());
					finFundtransfer.setTradeType(tradeFlows.getTradeType());
					fundtransferList.add(finFundtransfer);
					preMoney = nowMoney;
				}
				/**
				 * 4、增加资金划转记录信息
				 */
				finFundtransferDao.insertBatch(FinFundtransfer.class, fundtransferList, fundtransferList.size());
			}
			return new ResponseEntity<>(Constants.SUCCESS);
		} catch (Exception e) {
			logger.error("{}, 用户账户批量现金消费, 用户标识: {},  finTradeFlowList: {}, 用户账户现金消费异常信息: ",
					BaseUtil.getTccTryLogPrefix(), tradeFlow.getRegUserId(), JsonUtils.toJson(finTradeFlowList), e);
			throw new GeneralException("用户批量账户现金消费异常:" + CommonUtils.printStackTraceToString(e));
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> cashPayBatchForCancel(List<FinTradeFlow> finTradeFlowList) {
		logger.info("try cancel cashPayBatch, 用户批量账户现金消费, 入参: finTradeFlowList: {}",
				JsonUtils.toJson(finTradeFlowList));
		if (finTradeFlowList != null) {
			/**
			 * 1、遍历交易流水信息
			 */
			// 用于存放每个用户，待更新的账户金额
			Map<Integer, FinAccount> regUserMap = new HashMap<Integer, FinAccount>();
			FinAccount newFcccount = null;// 待更新的账户对象
			for (FinTradeFlow finTradeFlow : finTradeFlowList) {
				FinTradeFlow finTradeFlowTmp = this.finTradeFlowDao.findByFlowId(finTradeFlow.getFlowId());
				if (finTradeFlowTmp != null) {
					/**
					 * 2、删除交易流水
					 */
					finTradeFlowDao.deleteByFlowId(finTradeFlow.getFlowId());
					FinFundtransfer transfterCdt = new FinFundtransfer();
					transfterCdt.setTradeFlowId(finTradeFlowTmp.getFlowId());
					List<FinFundtransfer> transferList = this.finFundtransferDao.findByCondition(transfterCdt);
					if (CommonUtils.isNotEmpty(transferList)) {
						/**
						 * 3、根据交易流水的flowID,删除资金划转
						 */
						finFundtransferDao.deleteByPflowId(finTradeFlow.getFlowId());
						/**
						 * 4、 根据资金划转的subCod，初始化待更新账户金额
						 */
						if (regUserMap.get(finTradeFlow.getRegUserId()) == null) {
							newFcccount = new FinAccount();
							newFcccount.setFreezeMoney(BigDecimal.ZERO);
							newFcccount.setUseableMoney(BigDecimal.ZERO);
							newFcccount.setNowMoney(BigDecimal.ZERO);
							newFcccount.setRegUserId(finTradeFlow.getRegUserId());
						} else {
							newFcccount = regUserMap.get(finTradeFlow.getRegUserId());
						}
						// 根据资金划转subCode初始化待更新账户金额对象
						ResponseEntity<?> resResult = dealRollBackTransferAccountBySubcode(
								finTradeFlow.getTransferSubCode(), finTradeFlow.getTransMoney(), newFcccount);
						if (resResult.getResStatus() == ERROR) {
							logger.error("现金消费回滚操作, 用户标识: {}, 根据资金划转subCode初始化待更新账户金额对象异常: {}",
									finTradeFlow.getRegUserId(), resResult.getResMsg().toString());
							throw new BusinessException("根据资金划转subCode初始化待更新账户金额对象异常!");
						}
						regUserMap.put(finTradeFlow.getRegUserId(), newFcccount);
					}
				}
			}
			/**
			 * 5、更新账户金额
			 */
			for (Integer regUserId : regUserMap.keySet()) {
				// 执行更新账户金额操作
				ResponseEntity<?> accountResult = updateFinAccount(regUserMap.get(regUserId));
				if (accountResult.getResStatus() == ERROR) {
					logger.error("现金消费回滚操作, 用户标识: {}, 根据资金划转subCode更新账户金额异常: {}", regUserId,
							accountResult.getResMsg().toString());
					throw new BusinessException("根据资金划转subCode更新账户金额失败");
				}
			}
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}
	
	
	/**
	 * 
	 * @Description : 易宝支付(确认支付接口)
	 * @Method_Name : yeepayConfirmPay
	 * @param rechargeCash:充值对象
	 * @return
	 * @return : ResponseEntity<?>
	 * @Creation Date : 2018-09-29 15:36:09
	 * @Author : binliang@hongkun.com.cn 梁彬
	 */
	public ResponseEntity<?> yeepayConfirmPay(RechargeCash rechargeCash){
		logger.info("yeepay yeepayConfirmPay method。。");
		try {
			ResponseEntity<?> result = rechargeAccountInfo(rechargeCash);
			if (result.getResStatus() == ERROR) {
				logger.error("充值业务, 用户标识: {}, 充值异常信息: {}", rechargeCash.getUserId(), result.getResMsg().toString());
				return result;
			}
			Map<String, Object> map = (Map) result.getParams();
//			FinAccount finAccount = (FinAccount) map.get("finAccount");
			rechargeCash = (RechargeCash) map.get("rechargeCash");
			
			// 根据充值操作，调用相应的第三方接口方法
			String payMethodName = "paymentConfirmPay";
			Map<String, Object> payResult = callThirdPay(payMethodName, map);
			if (payResult == null) {
				throw new BusinessException("充值方法调用失败！");
			}
			if (!payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
				return new ResponseEntity<>(ERROR, payResult);
			}
			logger.info("yeepayConfirmPay payResult: {} ,", payResult);
			if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {//确认支付成功，则绑定该卡、更新手机号等操作
				
				FinBankCard finBankCard = this.finBankCardDao.findByPK(new Long(rechargeCash.getBankCardId()),
						FinBankCard.class);
				if (finBankCard == null) {
					return new ResponseEntity<>(ERROR, "银行卡信息为空");
				}
				if (finBankCard.getState() != TradeStateConstants.BANK_CARD_STATE_AUTH) {
					FinBankCard updateFinBankCard = new FinBankCard();
					updateFinBankCard.setId(finBankCard.getId());
					updateFinBankCard.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
					this.finBankCardDao.update(updateFinBankCard);
				}
				FinBankCardBinding bankCard = this.finBankCardBindingDao.findBankCardBinding(
						rechargeCash.getBankCardId(), rechargeCash.getUserId(),
						PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
				if (bankCard == null) {
					FinBankCardBinding bankCardBindingInfo = new FinBankCardBinding();
					bankCardBindingInfo.setRegUserId(rechargeCash.getUserId());
					bankCardBindingInfo.setBankThirdCode(rechargeCash.getBankCode());
					bankCardBindingInfo.setFinBankCardId(rechargeCash.getBankCardId());
					bankCardBindingInfo.setState(TradeStateConstants.BANK_CARD_STATE_AUTH);
					bankCardBindingInfo.setPayChannel(PayChannelEnum.fromChannelName(rechargeCash.getPayChannel()));
					this.finBankCardBindingDao.insertFinBankCardBinding(bankCardBindingInfo);
				}
			}

			if (payResult.get("resStatus").equals(String.valueOf(Constants.SUCCESS))) {
				String flowId = payResult.get("flowId").toString();
				if(flowId.equals(rechargeCash.getPaymentFlowId())){
					logger.info("第三方返回的订单号和自己记录订单号不一致，以第三方订单号为准，并不予更新订单。第三方订单号: {},平台订单号: {}",flowId,rechargeCash.getPaymentFlowId());
					return new ResponseEntity<>(SUCCESS, payResult);
				}
				FinPaymentRecord record = this.finPaymentRecordDao.findFinPaymentRecordByFlowId(flowId);
				logger.info("确认支付，判断流水状态， record.state={}, flowId={}",record.getState(),flowId);
				if(record !=null && record.getState() == TradeStateConstants.PENDING_PAYMENT){
					FinPaymentRecord finpaymentRecord = new FinPaymentRecord();
					finpaymentRecord.setFlowId(rechargeCash.getPaymentFlowId());
					finpaymentRecord.setState(TradeStateConstants.BANK_TRANSFER);
					this.finPaymentRecordDao.updateByFlowId(finpaymentRecord);
					logger.info("确认支付，流水状态更新。flowId={}", flowId);
				}
			}
			
			return new ResponseEntity<>(SUCCESS, payResult);
		} catch (Exception e) {
			logger.error("充值业务, 用户标识: {}, 充值入参: rechargeCash: {}, 充值异常信息: ", rechargeCash.toString(),
					rechargeCash.getUserId(), e);
			throw new GeneralException("充值失败!");
		}
	}

	/** 
	* @Description: 易宝充值中间状态处理
	* @param flowId
	* @param payResult
	* @return: com.yirun.framework.core.model.ResponseEntity<?> 
	* @Author: hanghe@hongkunjinfu.com
	* @Date: 2018/12/12 13:52
	*/
	public ResponseEntity<?> payWaiting(String flowId,Map<String, Object> payResult) {

		//轮循订单的状态，循环10次，每次10秒，10次之后没有成功，返回一个友好提示
		int payResultFlag = 0;	//0(未知),1(成功),2(失败)
		for (int i = 0; i < 10; i++) {
			FinPaymentRecord finPaymentRecord = finPaymentRecordDao
					.findFinPaymentRecordByFlowId(flowId);
			if (ALREADY_PAYMENT.equals(finPaymentRecord.getState())){
				payResultFlag = 1;	//支付成功
				break;
			}
			if (TRANSIT_FAIL.equals(finPaymentRecord.getState())){
				payResultFlag = 2;	//支付失败
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("短信校验确认充值，轮询订单返回的信息，0(未知),1(成功),2(失败)：" + payResultFlag);
		if (!Integer.valueOf("1").equals(payResultFlag)) {
			if (Integer.valueOf("2").equals(payResultFlag)) {
				return new ResponseEntity<>(ERROR, payResult.get("resMsg"));
			}else{
				return new ResponseEntity<>(PAY_STATE_WAITING,"正在等待对方银行返回结果，请稍后查看账户余额");
			}
		}
		return new ResponseEntity<>(SUCCESS);
	}
}
