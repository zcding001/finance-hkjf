package com.hongkun.finance.qdz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.constant.QdzConstants;
import com.hongkun.finance.qdz.dao.QdzAccountDao;
import com.hongkun.finance.qdz.dao.QdzRateRecordDao;
import com.hongkun.finance.qdz.dao.QdzTransRecordDao;
import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzTransferService;
import com.hongkun.finance.qdz.vo.QdzTransferInOutCondition;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QdzTransferServiceImpl implements QdzTransferService {

	private static final Logger logger = LoggerFactory.getLogger(QdzTransferServiceImpl.class);
	@Autowired
	private QdzAccountDao qdzAccountDao;
	@Autowired
	private QdzTransRecordDao qdzTransRecordDao;
	@Autowired
	private QdzRateRecordDao qdzRateRecordDao;

	@Compensable(cancelMethod = "cancelTransferInOut", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, readOnly = false)
	@Override
	public ResponseEntity<?> dealTransferInOut(QdzTransferInOutCondition qdzTransferInOutCondition) {
		logger.info("方法: dealTransferInOut, 钱袋子处理转入转出, 入参: qdzTransferInOutCondition: {}",
				qdzTransferInOutCondition.toString());
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal transMoney = BigDecimal.ZERO;
		QdzAccount qdzAccount = null;
		try {
			// 校验传入参数合法性
			ResponseEntity<?> result = checkDataInfo(qdzTransferInOutCondition);
			if (result.getResStatus() == Constants.ERROR) {
				logger.error("钱袋子处理转入转出, 校验参数异常: {}", result.getResMsg());
				return new ResponseEntity<>(Constants.ERROR, result.getResMsg().toString());
			}
			BigDecimal money =BigDecimal.ZERO;
			// 根据交易标识，判断是否是首次转入钱袋子,如果是首次，则生成该用户的钱袋子账户，否则更新钱袋子账户信息 0:插入 1:更新
			if (qdzTransferInOutCondition.getBugFlag() == QdzConstants.QDZ_INSERT_FALG) {
				qdzAccount = buildQdzAccountVo(qdzTransferInOutCondition.getRegUserId(),
						qdzTransferInOutCondition.getTransMoney(), QdzConstants.QDZ_ACCOUNT_STATE_COM,qdzTransferInOutCondition.getTransMoney());
				this.qdzAccountDao.save(qdzAccount);
			} else {
				// 通过用户ID，查询钱袋子账户信息，并返回钱袋子账户ID
				qdzAccount = this.qdzAccountDao.findByRegUserId(qdzTransferInOutCondition.getRegUserId());
				if (qdzAccount == null) {
					logger.error("钱袋子处理转入转出, 用户标识: {}, 处理转入转出异常: {}", qdzTransferInOutCondition.getRegUserId(),
							"未查询到该用户的钱袋子账户信息！");
					return new ResponseEntity<>(Constants.ERROR, "未查询到该用户的钱袋子账户信息！");
				}
				if (qdzTransferInOutCondition.getTransFlag().getValue() == TransTypeEnum.PAYOUT.getValue()) {
					// 如果转出金额大于剩余债权，则直接释放掉所有的待匹配的金额，将待匹配置为0,否则释放转出的待匹配金额
					BigDecimal creditorMoney = qdzAccount.getCreditorMoney() == null ? BigDecimal.ZERO
							: qdzAccount.getCreditorMoney();
					if (CompareUtil.gt(qdzTransferInOutCondition.getTransMoney(), creditorMoney)) {
						transMoney = qdzAccount.getCreditorMoney().negate();
					} else {
						transMoney = qdzTransferInOutCondition.getTransMoney().negate();
					}
					money =qdzTransferInOutCondition.getTransMoney().negate();
					// 已匹配债权待释放的债权金额，交给MQ处理
					resultMap.put("remainderMoney", qdzTransferInOutCondition.getTransMoney().add(transMoney));
				} else {
				    money =qdzTransferInOutCondition.getTransMoney();
					transMoney = qdzTransferInOutCondition.getTransMoney();
				}
				// 更新用户钱袋子账户金额（总债权，待匹配债权）
				if (this.qdzAccountDao.updateQdzAccountByRegUserId(
						buildQdzAccountVo(qdzTransferInOutCondition.getRegUserId(), transMoney, null,money)) <= 0) {
					logger.error("钱袋子处理转入转出, 用户标识: {}, 处理转入转出异常: {}", qdzTransferInOutCondition.getRegUserId(),
							"更新钱袋子账户信息失败！");
					return new ResponseEntity<>(Constants.ERROR, "更新钱袋子账户信息失败！");
				}
			}
			resultMap.put("qdzAccountId", qdzAccount.getId());
			// 生成转入转出记录
			QdzTransRecord qdzTransRecord = initTransRecord(qdzTransferInOutCondition);
			this.qdzTransRecordDao.save(qdzTransRecord);
			resultMap.put("qdzTransRecordId", qdzTransRecord.getId());
			responseEntity.setParams(resultMap);
			return responseEntity;
		} catch (Exception e) {
			logger.error("钱袋子处理转入转出, 用户标识: {}, 钱袋子转入转出处理,更新钱袋子账户插入转入转出记录异常: ",
					qdzTransferInOutCondition.getRegUserId(), e);
			throw new GeneralException("处理钱袋子账户插入转入转出记录异常:" + CommonUtils.printStackTraceToString(e));
		}
	}

	/**
	 * @Description : 组装钱袋子账户VO
	 * @Method_Name : buildQdzAccountVo;
	 * @param regUserId
	 *            用户ID
	 * @param transMoney
	 *            交易金额
	 * @param state
	 *            钱袋子账户状态
	 *  @param money
	 *            钱袋子MONEY更新的值
	 * @return
	 * @return : QdzAccount;
	 * @Creation Date : 2017年7月18日 上午9:51:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private QdzAccount buildQdzAccountVo(Integer regUserId, BigDecimal transMoney, Integer state,BigDecimal money) {
		QdzAccount qdzAccount = new QdzAccount();
		qdzAccount.setRegUserId(regUserId);
		qdzAccount.setMoney(money);
		qdzAccount.setCreditorMoney(transMoney);
		if (state != null) {
			qdzAccount.setState(QdzConstants.QDZ_ACCOUNT_STATE_COM);
		}
		return qdzAccount;
	}

	/**
	 * @Description :初始化转入记录表数据
	 * @Method_Name : initTransRecord;
	 * @param qdzTransferInOutCondition
	 * @return : QdzTransRecord;
	 * @Creation Date : 2017年7月16日 下午9:35:10;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	private QdzTransRecord initTransRecord(QdzTransferInOutCondition qdzTransferInOutCondition) {
		QdzTransRecord qdzTransRecord = new QdzTransRecord();
		qdzTransRecord.setRegUserId(qdzTransferInOutCondition.getRegUserId());
		qdzTransRecord.setType(qdzTransferInOutCondition.getTransFlag().getValue());
		qdzTransRecord.setTransMoney(qdzTransferInOutCondition.getTransMoney());
		qdzTransRecord.setPreMoney(qdzTransferInOutCondition.getPreMoney());
		qdzTransRecord.setSource(qdzTransferInOutCondition.getSource());
		if (qdzTransferInOutCondition.getTransFlag().getValue() == TransTypeEnum.PAYOUT.getValue()) {
			qdzTransRecord.setAfterMoney(
					qdzTransferInOutCondition.getPreMoney().subtract(qdzTransferInOutCondition.getTransMoney()));
		} else {
			qdzTransRecord.setAfterMoney(
					qdzTransferInOutCondition.getPreMoney().add(qdzTransferInOutCondition.getTransMoney()));
		}
		qdzTransRecord.setState(QdzConstants.QDZ_RECORD_STATE_SUCCESS);
		qdzTransRecord.setCreateTime(qdzTransferInOutCondition.getCreateTime());
		return qdzTransRecord;
	}

	/**
	 * @Description : 校验转入转出参数合法性
	 * @Method_Name : checkDataInfo;
	 * @param qdzTransferInOutCondition
	 * @return
	 * @throws Exception
	 * @return : ResponseEntity<?>;
	 * @Creation Date : 2017年7月18日 上午10:21:12;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static ResponseEntity<?> checkDataInfo(QdzTransferInOutCondition qdzTransferInOutCondition) {
		ResponseEntity<?> ResponseEntity = new ResponseEntity<>(Constants.SUCCESS);
		if (qdzTransferInOutCondition.getRegUserId() == null) {
			return new ResponseEntity<>(Constants.ERROR, "用户id不能为空!");
		}
		if (qdzTransferInOutCondition.getTransMoney() == null
				|| CompareUtil.eZero(qdzTransferInOutCondition.getTransMoney())) {
			return new ResponseEntity<>(Constants.ERROR, "交易金额不能为空或为零!");
		}
		if (qdzTransferInOutCondition.getTransFlag() == null) {
			return new ResponseEntity<>(Constants.ERROR, "交易类型不能为空");
		}
		return ResponseEntity;
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, readOnly = false)
	public ResponseEntity<?> cancelTransferInOut(QdzTransferInOutCondition qdzTransferInOutCondition) {
		QdzAccount qdzAccount = this.qdzAccountDao.findByRegUserId(qdzTransferInOutCondition.getRegUserId());
		// 如果为0代表上一次对钱袋子账户的操作是插入,否则认为是更新
		if (qdzTransferInOutCondition.getBugFlag() == 0) {
			if (qdzAccount != null) {
				// 删除钱袋子账户
				qdzAccountDao.delete(qdzAccount.getId().longValue(), QdzAccount.class);
			}
		} else {
			// 更新钱袋子账户
			QdzAccount account = new QdzAccount();
			account.setId(qdzAccount.getId());
			account.setMoney(qdzAccount.getMoney().negate());
			qdzAccountDao.update(account);
		}
		// 按条件查询转入转出记录
		QdzTransRecord qdzTransRecord = initTransRecord(qdzTransferInOutCondition);
		List<QdzTransRecord> qdzRecordList = qdzTransRecordDao.findByCondition(qdzTransRecord);
		if (qdzRecordList != null && qdzRecordList.size() > 0) {
			// 删除钱袋子转入转出记录
			qdzRecordList
					.forEach(record -> qdzTransRecordDao.delete(Long.valueOf(record.getId()), QdzTransRecord.class));
		}
		return new ResponseEntity<>(Constants.SUCCESS);
	}

	@Override
	public Map<String, Object> findQdzReceiptInfo(Integer regUserId) {
		BigDecimal rate = BigDecimal.ZERO;// 钱袋子利率
		BigDecimal totalMoney = BigDecimal.ZERO;// 待回款总金额
		BigDecimal totalCapitalMoney = BigDecimal.ZERO;// 待回款本金
		BigDecimal totalInterestMoney = BigDecimal.ZERO;// 待回款收益
		BigDecimal totalEarningMoney = BigDecimal.ZERO;// 待回款增值收益
		BigDecimal finishInterest = BigDecimal.ZERO;// 已收益
		// 查询用户账户信息
		QdzAccount qdzAccount = qdzAccountDao.findByRegUserId(regUserId);

		if (qdzAccount != null) {
			totalCapitalMoney = qdzAccount.getMoney() == null ? BigDecimal.ZERO : qdzAccount.getMoney();
			rate = qdzAccount.getInterestRate() == null ? BigDecimal.ZERO : qdzAccount.getInterestRate();
			totalEarningMoney = totalCapitalMoney.multiply(rate).divide(new BigDecimal(36000), 2, RoundingMode.DOWN);
			// 查询钱袋子每日利息
			QdzRateRecord qdzRateRecord = qdzRateRecordDao.getQdzRateRecord(new Date());
			if (qdzRateRecord != null) {
				rate = rate.add(qdzRateRecord.getRate() == null ? BigDecimal.ZERO : qdzRateRecord.getRate());
			}
			totalInterestMoney = totalCapitalMoney.multiply(rate).divide(new BigDecimal(36000), 2, RoundingMode.DOWN);
			totalMoney = totalCapitalMoney.add(totalInterestMoney);
			finishInterest = qdzAccount.getTotalInterest();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		// 钱袋子待回款总金额
		result.put("waitAmountSum", totalMoney);
		// 钱袋子待回款本金
		result.put("waitCapitalSum", totalCapitalMoney);
		// 钱袋子待回款收益
		result.put("waitInterestSum", totalInterestMoney);
		// 钱袋子待回款增值收益
		result.put("waitIncreaseSum", totalEarningMoney);
		// 钱袋子总收益
		result.put("finishInterest", finishInterest);
		return result;
	}

}
