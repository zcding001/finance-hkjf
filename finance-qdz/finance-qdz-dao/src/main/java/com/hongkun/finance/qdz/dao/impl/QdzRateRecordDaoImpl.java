package com.hongkun.finance.qdz.dao.impl;

import java.util.Date;
import java.util.List;

import com.hongkun.finance.qdz.dao.QdzRateRecordDao;
import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.impl.QdzRateRecordDaoImpl.java
 * @Class Name : QdzRateRecordDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class QdzRateRecordDaoImpl extends MyBatisBaseDaoImpl<QdzRateRecord, java.lang.Long>
		implements QdzRateRecordDao {

	@Override
	public QdzRateRecord getQdzRateRecord(Date day) {
		QdzRateRecord qdzRateRecord = new QdzRateRecord();
		//查询最近4天钱袋子利率
		qdzRateRecord.setCreateTimeBegin(DateUtils.getFirstTimeOfDay(DateUtils.addDays(day, -4)));
		qdzRateRecord.setCreateTimeEnd(DateUtils.getLastTimeOfDay(day));
		qdzRateRecord.setSortColumns("id desc");
		List<QdzRateRecord> list = this.findByCondition(qdzRateRecord);
        return CommonUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	@Override
	public QdzRateRecord findQdzRateRecordByTime(Date createTime) {
		return this.getCurSqlSessionTemplate().selectOne(QdzRateRecord.class.getName()+".findQdzRateRecordByTime", createTime);
	}

}
