package com.hongkun.finance.qdz.dao.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.hongkun.finance.qdz.dao.QdzTransRecordDao;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.impl.QdzTransRecordDaoImpl.java
 * @Class Name : QdzTransRecordDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class QdzTransRecordDaoImpl extends MyBatisBaseDaoImpl<QdzTransRecord, java.lang.Long>
		implements QdzTransRecordDao {
	/**
	 * 通过用户ID，查询转入，转出记录信息
	 */
	private String FIND_RECORD_BY_REGUSERID = ".findQdzTransRecordByRegUserId";
	/**
	 * 通过条件查询当天转入金额之和
	 */
	private String FIND_SUM_TRANSMONEY_OF_DAY = ".findSumTransMoneyOfDay";
	/**
	 * 通过条件查询当月转入次数
	 */
	private String FIND_TRANSFER_IN_TIMES_OF_MONTH = ".findTransferInTimesOfMonth";
	/**
	 * 分页查询钱袋子账单流水信息
	 */
	private String FIND_QDZ_TRANSFER_RECORD_LIST = ".findQdzTransferRecordList";

	@Override
	public QdzTransRecord findQdzTransRecordByRegUserId(Integer regUserId) {
		return (QdzTransRecord) getCurSqlSessionTemplate()
				.selectOne(QdzTransRecord.class.getName() + FIND_RECORD_BY_REGUSERID, regUserId);
	}

	@Override
	public BigDecimal findSumTransMoneyOfDay(QdzTransRecord qdzTransRecord) {
		return (BigDecimal) getCurSqlSessionTemplate()
				.selectOne(QdzTransRecord.class.getName() + FIND_SUM_TRANSMONEY_OF_DAY, qdzTransRecord);
	}

	@Override
	public Integer findTransferInTimesOfMonth(QdzTransRecord qdzTransRecord) {
		return (Integer) getCurSqlSessionTemplate()
				.selectOne(QdzTransRecord.class.getName() + FIND_TRANSFER_IN_TIMES_OF_MONTH, qdzTransRecord);
	}

	@Override
	public Pager findQdzTransferRecordList(Map<String, Object> qdzTransRecordMap, Pager pager) {
		return this.findByCondition(qdzTransRecordMap, pager, QdzTransRecord.class, FIND_QDZ_TRANSFER_RECORD_LIST);
	}

	@Override
	public int deleteById(Integer qdzRecordId) {
		return this.delete(qdzRecordId.longValue(), QdzTransRecord.class);
	}

    @Override
    public Integer findTransferUserSum(QdzTransRecord qdzTransRecord) {
       return  getCurSqlSessionTemplate().selectOne(QdzTransRecord.class.getName() + ".findTransferUserSum", qdzTransRecord);
    }

}
