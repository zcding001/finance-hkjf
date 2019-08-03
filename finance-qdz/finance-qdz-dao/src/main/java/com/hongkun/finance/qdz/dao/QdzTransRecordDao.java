package com.hongkun.finance.qdz.dao;

import java.math.BigDecimal;
import java.util.Map;

import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.QdzTransRecordDao.java
 * @Class Name : QdzTransRecordDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface QdzTransRecordDao extends MyBatisBaseDao<QdzTransRecord, java.lang.Long> {
	/**
	 * @Description :通过用户ID，查询钱袋子记录信息
	 * @Method_Name : findQdzTransRecordByRegUserId;
	 * @param regUserId
	 * @return
	 * @return : QdzTransRecord;
	 * @Creation Date : 2017年7月11日 下午5:54:25;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	QdzTransRecord findQdzTransRecordByRegUserId(Integer regUserId);

	/**
	 * @Description : 按条件查询当天转入总金额
	 * @Method_Name : findSumTransMoneyOfDay;
	 * @param qdzTransRecord
	 * @return
	 * @return : BigDecimal;
	 * @Creation Date : 2017年7月16日 下午8:30:37;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	BigDecimal findSumTransMoneyOfDay(QdzTransRecord qdzTransRecord);

	/**
	 * @Description : 按条件查询当天转入次数
	 * @Method_Name : findTransferInTimesOfMonth;
	 * @param qdzTransRecord
	 * @return
	 * @return : Integer;
	 * @Creation Date : 2017年7月16日 下午8:31:42;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Integer findTransferInTimesOfMonth(QdzTransRecord qdzTransRecord);

	/**
	 * @Description : 分页查询钱袋子账单流水信息
	 * @Method_Name : findQdzTransferRecordList;
	 * @param qdzTransRecordMap
	 * @param pager
	 * @return
	 * @return : Pager;
	 * @Creation Date : 2017年7月20日 上午10:09:32;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Pager findQdzTransferRecordList(Map<String, Object> qdzTransRecordMap, Pager pager);

	/**
	 * @Description :根据钱袋子记录ID，删除钱袋子记录
	 * @Method_Name : deleteById;
	 * @param qdzRecordId
	 * @return
	 * @return : int;
	 * @Creation Date : 2017年7月28日 下午4:43:45;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	int deleteById(Integer qdzRecordId);
	/**
	 *  @Description    : 按条件查询转入转出人数
	 *  @Method_Name    : findTransferUserSum;
	 *  @param qdzTransRecord
	 *  @return
	 *  @return         : Integer;
	 *  @Creation Date  : 2018年9月21日 上午11:47:59;
	 *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	Integer findTransferUserSum(QdzTransRecord qdzTransRecord);
}
