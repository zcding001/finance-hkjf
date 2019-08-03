package com.hongkun.finance.qdz.dao;

import java.util.Date;

import com.hongkun.finance.qdz.model.QdzRateRecord;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.qdz.dao.QdzRateRecordDao.java
 * @Class Name : QdzRateRecordDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface QdzRateRecordDao extends MyBatisBaseDao<QdzRateRecord, java.lang.Long> {
	/**
	 * @Description : 查询每日利息
	 * @Method_Name : getQdzRateRecord;
	 * @param day
	 * @return
	 * @return : QdzRateRecord;
	 * @Creation Date : 2018年3月12日 下午3:11:27;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	QdzRateRecord getQdzRateRecord(Date day);

	QdzRateRecord findQdzRateRecordByTime(Date createTime);
}
