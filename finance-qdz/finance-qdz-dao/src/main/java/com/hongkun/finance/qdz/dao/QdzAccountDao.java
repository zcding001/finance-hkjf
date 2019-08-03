package com.hongkun.finance.qdz.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hongkun.finance.qdz.model.QdzAccount;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.QdzAccountDao.java
 * @Class Name : QdzAccountDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface QdzAccountDao extends MyBatisBaseDao<QdzAccount, java.lang.Long> {
	/**
	 * @Description :通过用户ID，查询钱袋子账户信息
	 * @Method_Name : findByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : QdzAccount;
	 * @Creation Date : 2017年7月11日 下午4:31:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	QdzAccount findByRegUserId(Integer regUserId);

	/**
	 * @Description : 通过用户ID，更新钱袋子账户信息
	 * @Method_Name : updateQdzAccountByRegUserId;
	 * @param qdzAccount
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月11日 下午4:49:04;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int updateQdzAccountByRegUserId(QdzAccount qdzAccount);

	/**
	 * 
	 * @Description : 查询所有钱袋子异常账户
	 * @Method_Name : findCreditorExceptQdzAccounts
	 * @return
	 * @return : List<QdzAccount>
	 * @Creation Date : 2017年7月17日 下午3:05:09
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	List<QdzAccount> findCreditorExceptQdzAccounts();

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
	 * @Description : 查询跑批需要计算利息账户
	 * @Method_Name : findCalculateInterestAccount
	 * @return
	 * @return : List<QdzAccount>
	 * @Creation Date : 2017年7月19日 上午9:34:07
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
	 * @Description : 更新债券
	 * @Method_Name : updateCreditorMoney
	 * @param regUserId
	 * @param transMoney
	 * @return : void
	 * @Creation Date : 2017年8月9日 上午10:20:06
	 * @Author : caoxinbang@hongkun.com.cn 曹新帮
	 */
	int updateCreditorMoney(Integer regUserId, BigDecimal transMoney);

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
