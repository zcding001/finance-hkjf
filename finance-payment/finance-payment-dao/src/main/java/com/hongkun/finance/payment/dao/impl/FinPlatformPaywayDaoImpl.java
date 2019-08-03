package com.hongkun.finance.payment.dao.impl;

import java.util.List;
import com.hongkun.finance.payment.dao.FinPlatformPaywayDao;
import com.hongkun.finance.payment.model.FinPlatformPayway;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.dao.impl.FinPlatformPaywayDaoImpl.java
 * @Class Name : FinPlatformPaywayDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinPlatformPaywayDaoImpl extends MyBatisBaseDaoImpl<FinPlatformPayway, java.lang.Long>
		implements FinPlatformPaywayDao {
	/**
	 * 查询每个系统下的每个平台有哪几种支付渠道
	 */
	public String FIND_PAY_INFO = ".findPayChannelInfo";

	@Override
	public List<FinPlatformPayway> findPayChannelInfo(FinPlatformPayway finPlatformPayway) {
		return getCurSqlSessionTemplate().selectList(FinPlatformPayway.class.getName() + FIND_PAY_INFO,
				finPlatformPayway);
	}

}
