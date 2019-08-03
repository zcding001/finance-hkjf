package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinBankCardBindingDao;
import com.hongkun.finance.payment.model.FinBankCardBinding;
import com.hongkun.finance.payment.service.FinBankCardBindingService;
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
 *          com.hongkun.finance.payment.service.impl.FinBankCardBindingServiceImpl
 *          .java
 * @Class Name : FinBankCardBindingServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinBankCardBindingServiceImpl implements FinBankCardBindingService {

	private static final Logger logger = LoggerFactory.getLogger(FinBankCardBindingServiceImpl.class);

	/**
	 * FinBankCardBindingDAO
	 */
	@Autowired
	private FinBankCardBindingDao finBankCardBindingDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinBankCardBinding(FinBankCardBinding finBankCardBinding) {
		logger.info("方法: insertFinBankCardBinding, 保存银行信息, 入参: finBankCardBinding: {}", finBankCardBinding.toString());
		try {
			this.finBankCardBindingDao.save(finBankCardBinding);
		} catch (Exception e) {
			logger.error("保存银行信息, 保存失败: ", e);
			throw new GeneralException("保存银行信息失败!");
		}

	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinBankCardBindingBatch(List<FinBankCardBinding> list) {
		this.finBankCardBindingDao.insertBatch(FinBankCardBinding.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinBankCardBindingBatch(List<FinBankCardBinding> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finBankCardBindingDao.insertBatch(FinBankCardBinding.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinBankCardBinding(FinBankCardBinding finBankCardBinding) {
		logger.info("方法: updateFinBankCardBinding, 通过银行信息ID,更新银行信息, 入参: finBankCardBinding: {}",
				finBankCardBinding.toString());
		try {
			this.finBankCardBindingDao.update(finBankCardBinding);
		} catch (Exception e) {
			logger.error("通过银行信息ID,更新银行信息, 更新失败: ", e);
			throw new GeneralException("通过银行信息ID,更新银行信息失败！");
		}

	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinBankCardBindingBatch(List<FinBankCardBinding> list, int count) {
		this.finBankCardBindingDao.updateBatch(FinBankCardBinding.class, list, count);
	}

	@Override
	public FinBankCardBinding findFinBankCardBindingById(int id) {
		return this.finBankCardBindingDao.findByPK(Long.valueOf(id), FinBankCardBinding.class);
	}

	@Override
	public List<FinBankCardBinding> findFinBankCardBindingList(FinBankCardBinding finBankCardBinding) {
		return this.finBankCardBindingDao.findByCondition(finBankCardBinding);
	}

	@Override
	public List<FinBankCardBinding> findFinBankCardBindingList(FinBankCardBinding finBankCardBinding, int start,
			int limit) {
		return this.finBankCardBindingDao.findByCondition(finBankCardBinding, start, limit);
	}

	@Override
	public Pager findFinBankCardBindingList(FinBankCardBinding finBankCardBinding, Pager pager) {
		return this.finBankCardBindingDao.findByCondition(finBankCardBinding, pager);
	}

	@Override
	public int findFinBankCardBindingCount(FinBankCardBinding finBankCardBinding) {
		return this.finBankCardBindingDao.getTotalCount(finBankCardBinding);
	}

	@Override
	public FinBankCardBinding findBankCardBinding(Integer bankCardId, Integer regUserId, Integer payChannel) {
		return this.finBankCardBindingDao.findBankCardBinding(bankCardId, regUserId, payChannel);
	}

    @Override
    public int updateFinBankCardBindingByCardId(Integer bankCardId, Integer state) {
        return this.finBankCardBindingDao.updateFinBankCardBindingByCardId(bankCardId,state);
    }

}
