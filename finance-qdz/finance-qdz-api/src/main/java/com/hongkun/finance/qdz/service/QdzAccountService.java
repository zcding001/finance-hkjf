package com.hongkun.finance.qdz.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mengyun.tcctransaction.api.Compensable;

import com.hongkun.finance.qdz.model.QdzAccount;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.service.QdzAccountService.java
 * @Class Name : QdzAccountService.java
 * @Description : GENERATOR SERVICE类
 * @Author : generator
 */
public interface QdzAccountService {

	/**
	 * @Described : 单条插入
	 * @param qdzAccount
	 *            持久化的数据对象
	 * @return : void
	 */
	int insertQdzAccount(QdzAccount qdzAccount);

	/**
	 * @Description : 插入QDZ账户
	 * @Method_Name : insertQdzAccount;
	 * @param regUserId
	 * @param transMoney
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月17日 上午10:27:34;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int insertQdzAccount(Integer regUserId, BigDecimal transMoney);

	/**
	 * @Described : 批量插入
	 * @param List<QdzAccount>
	 *            批量插入的数据
	 * @return : void
	 */
	void insertQdzAccountBatch(List<QdzAccount> list);

	/**
	 * @Described : 批量插入
	 * @param List<QdzAccount>
	 *            批量插入的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	void insertQdzAccountBatch(List<QdzAccount> list, int count);

	/**
	 * @Description :通过用户ID，更新钱袋子账户信息
	 * @Method_Name : updateQdzAccountByUserId;
	 * @param qdzAccount
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月11日 下午4:47:00;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateQdzAccountByRegUserId(QdzAccount qdzAccount);

	/**
	 * @Description : 通过用户ID，更新钱袋子账户信息
	 * @Method_Name : updateQdzAccountByRegUserId;
	 * @param regUserId
	 * @param transMoney
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月17日 上午10:20:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateQdzAccountByRegUserId(Integer regUserId, BigDecimal transMoney);

	/**
	 * @Described : 批量更新数据
	 * @param qdzAccount
	 *            要更新的数据
	 * @param count
	 *            多少条数提交一次
	 * @return : void
	 */
	@Compensable
	void updateQdzAccountBatch(List<QdzAccount> list, int count);

	/**
	 * @Described : 通过id查询数据
	 * @param id
	 *            id值
	 * @return QdzAccount
	 */
	QdzAccount findQdzAccountById(int id);

	/**
	 * @Described : 条件检索数据
	 * @param qdzAccount
	 *            检索条件
	 * @return List<QdzAccount>
	 */
	List<QdzAccount> findQdzAccountList(QdzAccount qdzAccount);

	/**
	 * @Described : 条件检索数据
	 * @param qdzAccount
	 *            检索条件
	 * @param start
	 *            起始页
	 * @param limit
	 *            检索条数
	 * @return List<QdzAccount>
	 */
	List<QdzAccount> findQdzAccountList(QdzAccount qdzAccount, int start, int limit);

	/**
	 * @Described : 条件检索数据
	 * @param qdzAccount
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return List<QdzAccount>
	 */
	Pager findQdzAccountList(QdzAccount qdzAccount, Pager pager);

	/**
	 * @Described : 统计条数
	 * @param qdzAccount
	 *            检索条件
	 * @param pager
	 *            分页数据
	 * @return int
	 */
	int findQdzAccountCount(QdzAccount qdzAccount);

	/**
	 * @Description : 通过用户ID查询钱袋子账户信息
	 * @Method_Name : findQdzAccountByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : QdzAccount;
	 * @Creation Date : 2017年7月11日 下午4:22:20;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	QdzAccount findQdzAccountByRegUserId(Integer regUserId);

	/**
	 * 
	 * @Description : 查询所有钱袋子异常账户
	 * @Method_Name : findCreditorMatchQdzAccounts
	 * @return
	 * @return : List<QdzAccount>
	 * @Creation Date : 2017年7月17日 下午3:05:09
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<QdzAccount> findCreditorMatchQdzAccounts();

	/**
	 * @Description : 更新CreditorMoney
	 * @Method_Name : updateCreditorMoney
	 * @param regUserId
	 * @param transMoney
	 * @param creditorFlag
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月18日 下午2:15:32
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	void updateCreditorMoney(Integer regUserId, BigDecimal transMoney, int creditorFlag) throws Exception;

	/**
	 * @Description : 更新CreditorMoney
	 * @Method_Name : updateCreditorMoney
	 * @param regUserId
	 * @param transMoney
	 * @throws Exception
	 * @return : void
	 * @Creation Date : 2017年7月18日 下午2:15:32
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	int updateCreditorMoney(Integer regUserId, BigDecimal transMoney) throws Exception;

	/**
	 * @Description : 按条件查询失败债权金额总和
	 * @Method_Name : findSumFailICreditorMoney;
	 * @param qdzAccount
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月19日 下午3:37:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findSumFailICreditorMoney(QdzAccount qdzAccount);

	/**
	 * 
	 * @Description : 查询需要计算利息账户
	 * @Method_Name : findCalculateInterestAccount
	 * @return
	 * @return : List<QdzAccount>
	 * @Creation Date : 2017年7月19日 上午9:32:48
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<QdzAccount> findCalculateInterestAccount();

	/**
	 * @Description : 通过ID，删除钱袋子账户
	 * @Method_Name : deleteById;
	 * @param qdzAccountId
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月28日 下午4:40:07;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteById(Integer qdzAccountId);

	/**
	 * 
	 * @Description : 根据分片项取数据
	 * @Method_Name : findQdzAccountByShardingItem
	 * @param shardingItem
	 * @return
	 * @return : List<QdzAccount>
	 * @Creation Date : 2017年9月20日 下午5:05:26
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<QdzAccount> findQdzAccountByShardingItem(int shardingItem);
}
