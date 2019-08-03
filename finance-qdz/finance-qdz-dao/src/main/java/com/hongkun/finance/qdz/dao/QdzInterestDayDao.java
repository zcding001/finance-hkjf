package com.hongkun.finance.qdz.dao;

import com.hongkun.finance.qdz.model.QdzInterestDay;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.QdzInterestDayDao.java
 * @Class Name : QdzInterestDayDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface QdzInterestDayDao extends MyBatisBaseDao<QdzInterestDay, java.lang.Long> {
	/**
	 * @Description : 分页查询钱袋子每日利息
	 * @Method_Name : findQdzInterestDayInfo;
	 * @param qdzInterestDay
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月20日 下午3:25:19;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzInterestDayInfo(QdzInterestDay qdzInterestDay, Pager pager);
}
