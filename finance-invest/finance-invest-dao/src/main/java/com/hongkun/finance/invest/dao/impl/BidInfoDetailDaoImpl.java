package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.dao.BidInfoDetailDao;
import com.hongkun.finance.invest.model.BidInfoDetail;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.BidInfoDetailDaoImpl.java
 * @Class Name    : BidInfoDetailDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidInfoDetailDaoImpl extends MyBatisBaseDaoImpl<BidInfoDetail, java.lang.Long> implements BidInfoDetailDao {

	@Override
	public BidInfoDetail findByBiddInfoId(Integer biddInfoId) {
		return getCurSqlSessionTemplate().selectOne(BidInfoDetail.class.getName() + ".findByBiddInfoId", biddInfoId);
	}

}
