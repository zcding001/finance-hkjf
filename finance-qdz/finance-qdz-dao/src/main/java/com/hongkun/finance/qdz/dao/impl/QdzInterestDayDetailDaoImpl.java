package com.hongkun.finance.qdz.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.qdz.dao.QdzInterestDayDetailDao;
import com.hongkun.finance.qdz.model.QdzAccount;
import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.dao.impl.QdzInterestDayDetailDaoImpl.java
 * @Class Name : QdzInterestDayDetailDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class QdzInterestDayDetailDaoImpl extends MyBatisBaseDaoImpl<QdzInterestDayDetail, java.lang.Long>
		implements QdzInterestDayDetailDao {
	/**
	 * 查询钱袋子利息明细信息
	 */
	public String FIND_INTEREST_DETAIL_INFO = ".findQdzInterestDetailInfo";
	/**
	 * 查询钱袋子利息明细信息，返回LIST
	 */
	public String FIND_QDZ_INTEREST_DETAILS = ".findQdzInterestDetails";
	/**
	 * 查询钱袋子利息明细信息，返回LIST
	 */
	public String FIND_SUCC_QDZ_INTEREST_DAY_DETAILS = ".findSuccQdzInterestDayDetails";
	/**
	 * 查询钱袋子利息明细信息通过分片项，返回LIST
	 */
	public String FIND_QDZ_INTERESTDAYDETAILS_BY_SHARDINGITEM = ".findQdzInterestDayDetailsByShardingItem";

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailInfo(Map<String, Object> qdzInterestDayDetailMap) {
		return getCurSqlSessionTemplate()
				.selectList(QdzInterestDayDetail.class.getName() + FIND_INTEREST_DETAIL_INFO, qdzInterestDayDetailMap);
	}

	@Override
	public Pager findQdzInterestDetails(Map<String, Object> qdzInterestDayDetailMap, Pager pager) {
		return this.findByCondition(qdzInterestDayDetailMap, pager, QdzInterestDayDetail.class,
				FIND_QDZ_INTEREST_DETAILS);
	}

	@Override
	public List<QdzInterestDayDetail> findSuccQdzInterestDayDetails(QdzInterestDayDetail qdzInterestDayDetail) {
		return this.getCurSqlSessionTemplate().selectList(
				QdzInterestDayDetail.class.getName() + FIND_SUCC_QDZ_INTEREST_DAY_DETAILS, qdzInterestDayDetail);

	}

	@Override
	public List<QdzInterestDayDetail> findQdzInterestDayDetailsByShardingItem(Date day, int shardingItem) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("day", day);
		parameter.put("shardingItem", shardingItem);
		return this.getCurSqlSessionTemplate().selectList(
				QdzInterestDayDetail.class.getName() + FIND_QDZ_INTERESTDAYDETAILS_BY_SHARDINGITEM, parameter);

	}

}
