package com.hongkun.finance.payment.dao.impl;

import com.hongkun.finance.payment.dao.FinPaymentRecordDao;
import com.hongkun.finance.payment.model.FinPaymentRecord;
import com.hongkun.finance.payment.model.vo.PaymentVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.dao.impl.FinPaymentRecordDaoImpl.java
 * @Class Name : FinPaymentRecordDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinPaymentRecordDaoImpl extends MyBatisBaseDaoImpl<FinPaymentRecord, java.lang.Long>
		implements FinPaymentRecordDao {
	/*
	 * 通过flowId查询支付表记录信息
	 */
	public String FIND_FINPAYMENT_RECORD_BY_FLOW_ID = ".findFinPaymentRecordByFlowId";
	/*
	 * 通过flowId,更新支付记录表信息
	 */
	public String UPDATE_BY_FLOW_ID = ".updateByFlowId";

	/**
	 * 对账分页信息查询
	 */
	public String FIND_PAY_CHECK_INFO = ".findPayCheckPage";

	@Override
	public FinPaymentRecord findFinPaymentRecordByFlowId(String flowId) {
		return (FinPaymentRecord) getCurSqlSessionTemplate()
				.selectOne(FinPaymentRecord.class.getName() + FIND_FINPAYMENT_RECORD_BY_FLOW_ID, flowId);
	}

	@Override
	public int updateByFlowId(FinPaymentRecord finPaymentRecord) {
		return getCurSqlSessionTemplate().update(FinPaymentRecord.class.getName() + UPDATE_BY_FLOW_ID,
				finPaymentRecord);
	}

	@Override
	public Pager findPayCheckByCondition(List<Integer> regUserId, FinPaymentRecord finPaymentRecord, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("regUserIdList", regUserId);
		paramMap.put("tradeType", finPaymentRecord.getTradeType());
		paramMap.put("flowId", finPaymentRecord.getFlowId());
		paramMap.put("modifyTimeBegin", finPaymentRecord.getModifyTimeBegin());
		paramMap.put("modifyTimeEnd", finPaymentRecord.getModifyTimeEnd());
		paramMap.put("createTimeBegin", finPaymentRecord.getCreateTimeBegin());
		paramMap.put("createTimeEnd", finPaymentRecord.getCreateTimeEnd());
		paramMap.put("state", finPaymentRecord.getState());
		paramMap.put("payChannel", finPaymentRecord.getPayChannel());
		paramMap.put("reconciliationState", finPaymentRecord.getReconciliationState());
		paramMap.put("sortColumns", "create_time desc");
		return this.findByCondition(paramMap, pager, FinPaymentRecord.class, FIND_PAY_CHECK_INFO);
	}

    @Override
    public PaymentVO findPaymentSum(FinPaymentRecord finPaymentRecord) {
       return getCurSqlSessionTemplate()
        .selectOne(FinPaymentRecord.class.getName() + ".findPaymentSum", finPaymentRecord);
    }
}
