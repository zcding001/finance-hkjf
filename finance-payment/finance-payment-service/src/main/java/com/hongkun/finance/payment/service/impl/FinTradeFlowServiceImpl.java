package com.hongkun.finance.payment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.yirun.framework.core.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinTradeFlowDao;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.hongkun.finance.payment.service.FinTradeFlowService;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinTradeFlowServiceImpl.java
 * @Class Name : FinTradeFlowServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinTradeFlowServiceImpl implements FinTradeFlowService {
	private static final Logger logger = LoggerFactory.getLogger(FinTradeFlowServiceImpl.class);

	final int BATCH_SIZE = 50;

	/**
	 * FinTradeFlowDAO
	 */
	@Autowired
	private FinTradeFlowDao finTradeFlowDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insert(FinTradeFlow finTradeFlow) {
		return this.finTradeFlowDao.save(finTradeFlow);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBatch(List<FinTradeFlow> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i += BATCH_SIZE) {
				if (list.size() - i >= BATCH_SIZE) {
					this.finTradeFlowDao.insertBatch(FinTradeFlow.class, list.subList(i, i + BATCH_SIZE), BATCH_SIZE);
				} else {
					this.finTradeFlowDao.insertBatch(FinTradeFlow.class, list.subList(i, list.size()), list.size() - i);
				}
			}
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int update(FinTradeFlow finTradeFlow) {
		return this.finTradeFlowDao.update(finTradeFlow);
	}

	@Override
	public FinTradeFlow findById(int id) {
		return this.finTradeFlowDao.findByPK(new Long(id), FinTradeFlow.class);
	}

	@Override
	public List<FinTradeFlow> findByCondition(FinTradeFlow finTradeFlow) {
		return this.finTradeFlowDao.findByCondition(finTradeFlow);
	}

	@Override
	public List<FinTradeFlow> findByCondition(FinTradeFlow finTradeFlow, int start, int limit) {
		return this.finTradeFlowDao.findByCondition(finTradeFlow, start, limit);
	}

	@Override
	public Pager findByCondition(FinTradeFlow finTradeFlow, Pager pager) {
		return this.finTradeFlowDao.findByCondition(finTradeFlow, pager);
	}

	@Override
	public FinTradeFlow findByFlowId(String flowId) {
		return finTradeFlowDao.findByFlowId(flowId);
	}

	@Override
	public int updateByFlowId(FinTradeFlow finTradeFlow) {
		return finTradeFlowDao.updateByFlowId(finTradeFlow);
	}

	@Override
	public int deleteByFlowId(String finTradeFlowId) {
		return finTradeFlowDao.deleteByFlowId(finTradeFlowId);
	}

	@Override
	public int deleteByFlowIdBatch(List<FinTradeFlow> tradeFlowList) {
		List<String> flowIdList = new ArrayList<String>();
		for (FinTradeFlow finTradeFlow : tradeFlowList) {
			flowIdList.add(finTradeFlow.getFlowId());
		}
		return finTradeFlowDao.deleteByFlowIdBatch(flowIdList);
	}

	@Override
	public Pager findPaymentVoList(Pager pager, PaymentVO paymentVO) {
		return this.finTradeFlowDao.findByCondition(paymentVO, pager, FinTradeFlow.class, ".findPaymentVOList");
	}

	@Override
	public FinTradeFlow findFreezeTradeFlow(String pFlowId) {
		return finTradeFlowDao.findFreezeTradeFlow(pFlowId);
	}

	@Override
	public List<FinTradeFlow> findFreezeTradeFlowByPflowIds(List<Integer> bidInvestIds) {
		return finTradeFlowDao.findFreezeTradeFlowByPflowIds(bidInvestIds);
	}

	@Override
	public FinTradeFlow findByPflowIdAndTradeType(String pFlowId, Integer tradeType) {
		if(StringUtils.isNotBlank(pFlowId) && CommonUtils.gtZero(tradeType)){
			FinTradeFlow cdt = new FinTradeFlow();
			cdt.setTradeType(tradeType);
			cdt.setPflowId(pFlowId);
			return finTradeFlowDao.findByPflowIdAndTradeType(cdt);
		}
		return null;
	}

	@Override
	public List<FinTradeFlow> findTradeFlowByPflowIds(Set<Integer> pflowIds, Integer tradeType) {
		return finTradeFlowDao.findTradeFlowByPflowIds(pflowIds,tradeType);
	}
}
