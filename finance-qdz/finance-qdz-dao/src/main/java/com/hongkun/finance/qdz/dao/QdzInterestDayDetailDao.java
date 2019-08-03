package com.hongkun.finance.qdz.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.qdz.model.QdzInterestDayDetail;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.QdzInterestDayDetailDao.java
 * @Class Name : QdzInterestDayDetailDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface QdzInterestDayDetailDao extends MyBatisBaseDao<QdzInterestDayDetail, java.lang.Long> {
	/**
	 * @Description : 查询钱袋子明细
	 * @Method_Name : findQdzInterestDayDetailInfo;
	 * @param qdzInterestDayDetailMap
	 * @return
	 * @return : QdzInterestDayDetail;
	 * @Creation Date : 2017年7月19日 上午10:46:39;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
    List<QdzInterestDayDetail> findQdzInterestDayDetailInfo(Map<String, Object> qdzInterestDayDetailMap);

	/**
	 * @Description : 查询钱袋子明细信息，返回List
	 * @Method_Name : findQdzInterestDetails;
	 * @param qdzInterestDayDetailMap
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月19日 上午10:47:13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzInterestDetails(Map<String, Object> qdzInterestDayDetailMap, Pager pager);

	/**
	 * @Described : 查询钱袋子每日利息成功的明细
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findSuccQdzInterestDayDetails(QdzInterestDayDetail qdzInterestDayDetail);

	/**
	 * @Described : 查询钱袋子每日利息成功的明细通过分片
	 * @param qdzInterestDayDetail
	 *            检索条件
	 * @return List<QdzInterestDayDetail>
	 */
	List<QdzInterestDayDetail> findQdzInterestDayDetailsByShardingItem(Date day, int shardingItem);
}
