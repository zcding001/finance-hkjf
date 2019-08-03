package com.hongkun.finance.qdz.dao.impl;

import com.hongkun.finance.qdz.dao.QdzInterestDayDao;
import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.impl.QdzInterestDayDaoImpl.java
 * @Class Name : QdzInterestDayDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class QdzInterestDayDaoImpl extends MyBatisBaseDaoImpl<QdzInterestDay, java.lang.Long>
		implements QdzInterestDayDao {
	/**
	 * 分页查询钱袋子每日利息信息
	 */
	public String FIND_QDZ_INTEREST_DAY_INFO = ".findQdzInterestDay";

	@Override
	public Pager findQdzInterestDayInfo(QdzInterestDay qdzInterestDay, Pager pager) {
		qdzInterestDay.setSortColumns("day desc");
		return this.findByCondition(qdzInterestDay, pager, QdzInterestDay.class, FIND_QDZ_INTEREST_DAY_INFO);
	}

}
