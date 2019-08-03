package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.dao.PointAccountDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointAccountDaoImpl.java
 * @Class Name    : PointAccountDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointAccountDaoImpl extends MyBatisBaseDaoImpl<PointAccount, Long> implements PointAccountDao {
	/**
	 * 通过userId查询积分账户
	 */
	private final String FIND_POINTACCOUNT_BY_REGUSERID = ".findPointAccountByRegUserId";

	private final String UPDATE_BY_REGUSERID = ".updateByRegUserId";

	private final String UPDATE_POINTACCOUNT_BASEONORGIN = ".updatePointAccountBaseOnOrgin";
	@Override
	public PointAccount findPointAccountByRegUserId(int regUserId) {
		return getCurSqlSessionTemplate()
        .selectOne(PointAccount.class.getName() + FIND_POINTACCOUNT_BY_REGUSERID, regUserId);
	}
	
	@Override
	public void updateByRegUserId(PointAccount pointAccount) {
		getCurSqlSessionTemplate().update(PointAccount.class.getName() + UPDATE_BY_REGUSERID, pointAccount);
	}


}
