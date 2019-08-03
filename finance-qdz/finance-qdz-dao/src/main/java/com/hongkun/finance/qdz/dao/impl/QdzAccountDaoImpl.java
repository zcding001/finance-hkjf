package com.hongkun.finance.qdz.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hongkun.finance.qdz.dao.QdzAccountDao;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.impl.QdzAccountDaoImpl.java
 * @Class Name : QdzAccountDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class QdzAccountDaoImpl extends MyBatisBaseDaoImpl<QdzAccount, java.lang.Long> implements QdzAccountDao {
	/**
	 * 通过用户ID，查询钱袋子账户信息
	 */
	private String FIND_QDZACCOUNT_BY_REGUSERID = ".findQdzAccountByRegUserId";
	/**
	 * 通过用户ID，更新钱袋子账户信息
	 */
	private String UPDATE_QDZACCOUNT_BY_REGUSERID = ".updateByRegUserId";
	/**
	 * 查询所有钱袋子异常账户
	 */
	private String FIND_CREDITOR_EXCEPT_QDZACCOUNTS = ".findCreditorExceptQdzAccounts";
	/**
	 * 查询失败债权总额
	 */
	private String FIND_SUM_FAIL_CREDITOR_MONEY = ".findSumFailCreditorMoney";
	/**
	 * 查询所有需要跑批计算利息账户
	 */
	private String FIND_CALCULATE_INTEREST_ACCOUNT = ".findCalculateInterestAccount";
	/**
	 * 根据分片项查询数据
	 */
	private String FIND_QDZ_ACCOUNT_BY_SHARDINGITEM = ".findQdzAccountByShardingItem";

	/*
	 * 更新剩余债券
	 */
	private String UPDATE_CREDITOR_MONEY = ".updateCreditorMoney";

	public QdzAccount findByRegUserId(Integer regUserId) {

		return (QdzAccount) getCurSqlSessionTemplate()
				.selectOne(QdzAccount.class.getName() + FIND_QDZACCOUNT_BY_REGUSERID, regUserId);
	}

	@Override
	public int updateQdzAccountByRegUserId(QdzAccount qdzAccount) {
		return getCurSqlSessionTemplate().update(QdzAccount.class.getName() + UPDATE_QDZACCOUNT_BY_REGUSERID,
				qdzAccount);
	}

	@Override
	public List<QdzAccount> findCreditorExceptQdzAccounts() {
		return getCurSqlSessionTemplate().selectList(QdzAccount.class.getName() + FIND_CREDITOR_EXCEPT_QDZACCOUNTS,
				null);

	}

	@Override

	public BigDecimal findSumFailICreditorMoney(QdzAccount qdzAccount) {
		return getCurSqlSessionTemplate().selectOne(QdzAccount.class.getName() + FIND_SUM_FAIL_CREDITOR_MONEY,
				qdzAccount);
	}

	public List<QdzAccount> findCalculateInterestAccount() {

		return getCurSqlSessionTemplate().selectList(QdzAccount.class.getName() + FIND_CALCULATE_INTEREST_ACCOUNT,
				null);
	}

	@Override
	public int deleteById(Integer qdzAccountId) {
		return this.delete(qdzAccountId.longValue(), QdzAccount.class);
	}

	@Override
	public int updateCreditorMoney(Integer regUserId, BigDecimal transMoney) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("creditorMoney", transMoney);
		parameter.put("regUserId", regUserId);
		return getCurSqlSessionTemplate().update(QdzAccount.class.getName() + UPDATE_CREDITOR_MONEY, parameter);

	}

	@Override
	public List<QdzAccount> findQdzAccountByShardingItem(int shardingItem) {
		return getCurSqlSessionTemplate().selectList(QdzAccount.class.getName() + FIND_QDZ_ACCOUNT_BY_SHARDINGITEM,
				shardingItem);
	}

}
