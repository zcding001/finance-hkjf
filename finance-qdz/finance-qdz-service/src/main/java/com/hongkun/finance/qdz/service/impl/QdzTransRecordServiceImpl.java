package com.hongkun.finance.qdz.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.qdz.dao.QdzTransRecordDao;
import com.hongkun.finance.qdz.enums.TransTypeEnum;
import com.hongkun.finance.qdz.model.QdzTransRecord;
import com.hongkun.finance.qdz.service.QdzTransRecordService;
import com.hongkun.finance.qdz.vo.QdzTransferInfo;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.qdz.service.impl.QdzTransRecordServiceImpl.java
 * @Class Name : QdzTransRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class QdzTransRecordServiceImpl implements QdzTransRecordService {

	private static final Logger logger = LoggerFactory.getLogger(QdzTransRecordServiceImpl.class);

	/**
	 * QdzTransRecordDAO
	 */
	@Autowired
	private QdzTransRecordDao qdzTransRecordDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertQdzTransRecord(QdzTransRecord qdzTransRecord) {
		return this.qdzTransRecordDao.save(qdzTransRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzTransRecordBatch(List<QdzTransRecord> list) {
		this.qdzTransRecordDao.insertBatch(QdzTransRecord.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertQdzTransRecordBatch(List<QdzTransRecord> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.qdzTransRecordDao.insertBatch(QdzTransRecord.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzTransRecord(QdzTransRecord qdzTransRecord) {
		this.qdzTransRecordDao.update(qdzTransRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateQdzTransRecordBatch(List<QdzTransRecord> list, int count) {
		this.qdzTransRecordDao.updateBatch(QdzTransRecord.class, list, count);
	}

	@Override
	public QdzTransRecord findQdzTransRecordById(int id) {
		return this.qdzTransRecordDao.findByPK(Long.valueOf(id), QdzTransRecord.class);
	}

	@Override
	public List<QdzTransRecord> findQdzTransRecordList(QdzTransRecord qdzTransRecord) {
		return this.qdzTransRecordDao.findByCondition(qdzTransRecord);
	}

	@Override
	public List<QdzTransRecord> findQdzTransRecordList(QdzTransRecord qdzTransRecord, int start, int limit) {
		return this.qdzTransRecordDao.findByCondition(qdzTransRecord, start, limit);
	}

	@Override
	public Pager findQdzTransRecordList(QdzTransRecord qdzTransRecord, Pager pager) {
		return this.qdzTransRecordDao.findByCondition(qdzTransRecord, pager);
	}

	@Override
	public int findQdzTransRecordCount(QdzTransRecord qdzTransRecord) {
		return this.qdzTransRecordDao.getTotalCount(qdzTransRecord);
	}

	@Override
	public QdzTransRecord findQdzTransRecordByRegUserId(Integer regUserId) {
		return this.qdzTransRecordDao.findQdzTransRecordByRegUserId(regUserId);
	}

	@Override
	public BigDecimal findSumTransMoneyOfDay(Integer regUserId, Integer type) {
		QdzTransRecord qdzTransRecord = new QdzTransRecord();
		qdzTransRecord.setRegUserId(regUserId);
		qdzTransRecord.setType(type);
		qdzTransRecord.setCreateTimeBegin(DateUtils.getFirstTimeOfDay());
		qdzTransRecord.setCreateTimeEnd(DateUtils.getLastTimeOfDay());
		return qdzTransRecordDao.findSumTransMoneyOfDay(qdzTransRecord);
	}

	@Override
	public Integer findTransferInTimesOfMonth(Integer regUserId, Integer type) {
		QdzTransRecord qdzTransRecord = new QdzTransRecord();
		qdzTransRecord.setRegUserId(regUserId);
		qdzTransRecord.setType(type);
		qdzTransRecord.setCreateTimeBegin(DateUtils.getFirstDayOfMonth(new Date()));
		qdzTransRecord.setCreateTimeEnd(DateUtils.getLastDayOfMonth(new Date()));
		return qdzTransRecordDao.findTransferInTimesOfMonth(qdzTransRecord);
	}

	@Override
	public BigDecimal findInvestSumMoney(Date startTime, Date endTime, TransTypeEnum transTypeEnum, Integer state) {
		QdzTransRecord qdzTransRecord = new QdzTransRecord();
		qdzTransRecord.setState(state);
		qdzTransRecord.setType(transTypeEnum.getValue());
		qdzTransRecord.setCreateTimeBegin(startTime);
		qdzTransRecord.setCreateTimeEnd(endTime);
		return qdzTransRecordDao.findSumTransMoneyOfDay(qdzTransRecord);
	}

	@Override
	public Pager findQdzTransferRecordList(QdzTransferInfo qdzTransferInfo, Pager pager) {
		Map<String, Object> qdzTransRecordMap = new HashMap<String, Object>();
		qdzTransRecordMap.put("flowId", qdzTransferInfo.getFlowId());
		qdzTransRecordMap.put("regUserIdList", qdzTransferInfo.getRegUserIdList());
		qdzTransRecordMap.put("type", qdzTransferInfo.getType());
		qdzTransRecordMap.put("transMoney", qdzTransferInfo.getTransMoney());
		qdzTransRecordMap.put("preMoney", qdzTransferInfo.getPreMoney());
		qdzTransRecordMap.put("afterMoney", qdzTransferInfo.getAfterMoney());
		qdzTransRecordMap.put("state", qdzTransferInfo.getState());
		qdzTransRecordMap.put("createTimeBegin", StringUtils.isBlank(qdzTransferInfo.getCreateTimeBegin())?null:qdzTransferInfo.getCreateTimeBegin());
		qdzTransRecordMap.put("createTimeEnd",StringUtils.isBlank( qdzTransferInfo.getCreateTimeEnd())?null: qdzTransferInfo.getCreateTimeEnd());
	    qdzTransRecordMap.put("sortColumns", "create_time desc");

		return qdzTransRecordDao.findQdzTransferRecordList(qdzTransRecordMap, pager);
	}

	@Override
	public int deleteById(Integer qdzRecordId) {
		return qdzTransRecordDao.deleteById(qdzRecordId);
	}

    @Override
    public Integer findTransferUserSum(QdzTransRecord qdzTransRecord) {
        return qdzTransRecordDao.findTransferUserSum(qdzTransRecord);
    }
}
