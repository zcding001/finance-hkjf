package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinPaymentRecordDao;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.service.FinPaymentRecordService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinPaymentRecordServiceImpl.
 *          java
 * @Class Name : FinPaymentRecordServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinPaymentRecordServiceImpl implements FinPaymentRecordService {

	private static final Logger logger = LoggerFactory.getLogger(FinPaymentRecordServiceImpl.class);

	/**
	 * FinPaymentRecordDAO
	 */
	@Autowired
	private FinPaymentRecordDao finPaymentRecordDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public Integer insertFinPaymentRecord(FinPaymentRecord finPaymentRecord) {
		logger.info("方法: insertFinPaymentRecord, 保存支付记录信息, 入参: finPaymentRecord: {}", finPaymentRecord.toString());
		try {
			this.finPaymentRecordDao.save(finPaymentRecord);
			return finPaymentRecord.getId();
		} catch (Exception e) {
			logger.error("保存支付记录信息失败: ", e);
			throw new GeneralException("保存支付记录信息失败!");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPaymentRecordBatch(List<FinPaymentRecord> list) {
		this.finPaymentRecordDao.insertBatch(FinPaymentRecord.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinPaymentRecordBatch(List<FinPaymentRecord> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finPaymentRecordDao.insertBatch(FinPaymentRecord.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPaymentRecord(FinPaymentRecord finPaymentRecord) {
		this.finPaymentRecordDao.update(finPaymentRecord);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinPaymentRecordBatch(List<FinPaymentRecord> list, int count) {
		this.finPaymentRecordDao.updateBatch(FinPaymentRecord.class, list, count);
	}

	@Override
	public FinPaymentRecord findFinPaymentRecordById(int id) {
		return this.finPaymentRecordDao.findByPK(Long.valueOf(id), FinPaymentRecord.class);
	}

	@Override
	public List<FinPaymentRecord> findFinPaymentRecordList(FinPaymentRecord finPaymentRecord) {
		return this.finPaymentRecordDao.findByCondition(finPaymentRecord);
	}

	@Override
	public List<FinPaymentRecord> findFinPaymentRecordList(FinPaymentRecord finPaymentRecord, int start, int limit) {
		return this.finPaymentRecordDao.findByCondition(finPaymentRecord, start, limit);
	}

	@Override
	public Pager findFinPaymentRecordList(FinPaymentRecord finPaymentRecord, Pager pager) {
		return this.finPaymentRecordDao.findByCondition(finPaymentRecord, pager);
	}

	@Override
	public int findFinPaymentRecordCount(FinPaymentRecord finPaymentRecord) {
		return this.finPaymentRecordDao.getTotalCount(finPaymentRecord);
	}

	@Override
	public FinPaymentRecord findFinPaymentRecordByFlowId(String flowId) {
		return this.finPaymentRecordDao.findFinPaymentRecordByFlowId(flowId);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateByFlowId(FinPaymentRecord finPaymentRecord) {
		logger.info("方法: updateByFlowId, 通过flowID更新支付记录, 入参: finPaymentRecord: {}", finPaymentRecord.toString());
		try {
			return this.finPaymentRecordDao.updateByFlowId(finPaymentRecord);
		} catch (Exception e) {
			logger.error("通过flowID更新支付记录, 更新失败: ", e);
			throw new GeneralException("通过flowID更新支付记录失败!");
		}
	}

	@Override
	public Pager findPayCheckByCondition(List<Integer> regUserId, FinPaymentRecord finPaymentRecord, Pager pager) {
		return this.finPaymentRecordDao.findPayCheckByCondition(regUserId, finPaymentRecord, pager);
	}

    @Override
    public PaymentVO findPaymentSum(FinPaymentRecord finPaymentRecord) {
        return this.finPaymentRecordDao.findPaymentSum(finPaymentRecord);
    }
}
