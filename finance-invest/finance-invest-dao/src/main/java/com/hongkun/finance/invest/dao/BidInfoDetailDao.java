package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidInfoDetail;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.BidInfoDetailDao.java
 * @Class Name    : BidInfoDetailDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidInfoDetailDao extends MyBatisBaseDao<BidInfoDetail, java.lang.Long> {
	BidInfoDetail findByBiddInfoId(Integer biddInfoId);
}
