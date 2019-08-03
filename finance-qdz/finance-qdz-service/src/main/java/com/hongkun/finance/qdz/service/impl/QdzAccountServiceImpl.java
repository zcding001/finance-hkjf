package com.hongkun.finance.qdz.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.dao.QdzAccountDao;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.service.QdzAccountService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.CompareUtil;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.hongkun.finance.qdz.constant.QdzConstants.CREDITOR_FLAG_BUY;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.service.impl.QdzAccountServiceImpl.java
 * @Class Name : QdzAccountServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class QdzAccountServiceImpl implements QdzAccountService {

	private static final Logger logger = LoggerFactory.getLogger(QdzAccountServiceImpl.class);

	/**
	 * QdzAccountDAO
	 */
	@Autowired
	private QdzAccountDao qdzAccountDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertQdzAccount(QdzAccount qdzAccount) {
		return this.qdzAccountDao.save(qdzAccount);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzAccountBatch(List<QdzAccount> list) {
		this.qdzAccountDao.insertBatch(QdzAccount.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzAccountBatch(List<QdzAccount> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.qdzAccountDao.insertBatch(QdzAccount.class, list, count);
	}

	@Override
	@Compensable(cancelMethod = "updateQdzAccountBatchForCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzAccountBatch(List<QdzAccount> list, int count) {
		logger.info("tcc try updateQdzAccountBatch. 批量钱袋子账户, 账户列表: {}", JsonUtils.toJson(list));
		try{
			this.qdzAccountDao.updateBatch(QdzAccount.class, list, count);
		}catch(Exception e){
			logger.error("tcc try updateQdzAccountBatch. 批量钱袋子账户, 账户列表: {}", JsonUtils.toJson(list), e);
		    new GeneralException("批量钱袋子账户失败");
		}
	}

	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzAccountBatchForCancel(List<QdzAccount> list, int count) {
		logger.info("tcc cancel updateQdzAccountBatch. 批量钱袋子账户, 账户列表: {}", JsonUtils.toJson(list));
		try{
			if (list != null) {
				list.forEach(e -> e.setCreditorMoney(e.getCreditorMoney().negate()));
				this.qdzAccountDao.updateBatch(QdzAccount.class, list, count);
			}
		}catch(Exception e){
			logger.error("tcc cancel updateQdzAccountBatch. 批量钱袋子账户, 账户列表: {}", JsonUtils.toJson(list), e);
			new GeneralException("批量钱袋子账户失败");
		}
	}

	@Override
	public QdzAccount findQdzAccountById(int id) {
		return this.qdzAccountDao.findByPK(Long.valueOf(id), QdzAccount.class);
	}

	@Override
	public List<QdzAccount> findQdzAccountList(QdzAccount qdzAccount) {
		return this.qdzAccountDao.findByCondition(qdzAccount);
	}

	@Override
	public List<QdzAccount> findQdzAccountList(QdzAccount qdzAccount, int start, int limit) {
		return this.qdzAccountDao.findByCondition(qdzAccount, start, limit);
	}

	@Override
	public Pager findQdzAccountList(QdzAccount qdzAccount, Pager pager) {
		return this.qdzAccountDao.findByCondition(qdzAccount, pager);
	}

	@Override
	public int findQdzAccountCount(QdzAccount qdzAccount) {
		return this.qdzAccountDao.getTotalCount(qdzAccount);
	}

	@Override
	public QdzAccount findQdzAccountByRegUserId(Integer regUserId) {
		return this.qdzAccountDao.findByRegUserId(regUserId);
	}

	@Override
	public int updateQdzAccountByRegUserId(QdzAccount qdzAccount) {
		return this.qdzAccountDao.updateQdzAccountByRegUserId(qdzAccount);
	}

	@Override
	public int updateQdzAccountByRegUserId(Integer regUserId, BigDecimal transMoney) {
		QdzAccount qdzAccount = new QdzAccount();
		qdzAccount.setRegUserId(regUserId);
		qdzAccount.setMoney(transMoney);
		return this.qdzAccountDao.updateQdzAccountByRegUserId(qdzAccount);
	}

	@Override
	public int insertQdzAccount(Integer regUserId, BigDecimal transMoney) {
		QdzAccount qdzAccount = new QdzAccount();
		qdzAccount.setRegUserId(regUserId);
		qdzAccount.setMoney(transMoney);
		qdzAccount.setState(1);
		qdzAccount.setCreateTime(new Date());
		qdzAccount.setModifyTime(new Date());
		return this.qdzAccountDao.save(qdzAccount);
	}

	public List<QdzAccount> findCreditorMatchQdzAccounts() {
		return qdzAccountDao.findCreditorExceptQdzAccounts();
	}

	@Override
	public void updateCreditorMoney(Integer regUserId, BigDecimal transMoney, int creditorFlag) throws Exception {
		if (regUserId == null || regUserId <= 0) {
			throw new Exception("用户ID不能为空！");
		}
		if (CompareUtil.lteZero(transMoney)) {
			throw new Exception("更新待匹配债券不能小于0！");
		}
		QdzAccount qdzAccount = new QdzAccount();
		qdzAccount.setRegUserId(regUserId);
		qdzAccount.setModifyTime(new Date());
		BigDecimal creditorMoney = (creditorFlag == CREDITOR_FLAG_BUY) ? transMoney
				: transMoney.multiply(new BigDecimal(-1));
		qdzAccount.setCreditorMoney(creditorMoney);
		qdzAccountDao.updateQdzAccountByRegUserId(qdzAccount);
	}

	@Override

	public BigDecimal findSumFailICreditorMoney(QdzAccount qdzAccount) {
		return qdzAccountDao.findSumFailICreditorMoney(qdzAccount);
	}

	public List<QdzAccount> findCalculateInterestAccount() {

		return qdzAccountDao.findCalculateInterestAccount();
	}

	@Override
	public int deleteById(Integer qdzAccountId) {
		return qdzAccountDao.deleteById(qdzAccountId);
	}

	@Override
	public int updateCreditorMoney(Integer regUserId, BigDecimal transMoney) {
		try {
			return qdzAccountDao.updateCreditorMoney(regUserId, transMoney.multiply(BigDecimal.valueOf(-1)));
		} catch (Exception e) {
			logger.info("更新钱袋子剩余债券异常参数：用户标识{},债券金额{}", regUserId, transMoney);
		}
		return 0;
	}

	@Override
	public List<QdzAccount> findQdzAccountByShardingItem(int shardingItem) {
		return qdzAccountDao.findQdzAccountByShardingItem(shardingItem);
	}
}
