package com.hongkun.finance.payment.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.payment.dao.FinTradeFlowDao;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinTradeFlowDaoImpl.java
 * @Class Name : FinTradeFlowDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinTradeFlowDaoImpl extends MyBatisBaseDaoImpl<FinTradeFlow, java.lang.Long> implements FinTradeFlowDao {
	/***
	 * 根据FLOWID查询交易流水对象信息
	 */
	public String FIND_BY_FLOWID = ".findByFlowId";
	/**
	 * 通过FLOWID，更新流水信息
	 */
	public String UPDATE_BY_FLOW_ID = ".updateByFlowId";
	/**
	 * 通过FLOWID,删除流水信息
	 */
	public String DEL_BY_FLOW_ID = ".deleteByFlowId";
	/**
	 * 通过FLOWID，批量删除流水信息
	 */
	public String DEL_BY_FLOW_ID_BATCH = ".deleteBatchByFlowId";
	/**
	 * 对账分页信息查询
	 */
	public String FIND_PAY_CHECK_INFO = ".findPayCheckPage";
	/**
	 * pFlowId+tradeType返回唯一一条流水
	 */
	public String FIND_BY_P_FLOW_ID_TRADETYPE = ".findByPflowIdTradetype";

	@Override
	public FinTradeFlow findByFlowId(String flowId) {
		return (FinTradeFlow) getCurSqlSessionTemplate().selectOne(FinTradeFlow.class.getName() + FIND_BY_FLOWID,
				flowId);
	}

	@Override
	public int updateByFlowId(FinTradeFlow finTradeFlow) {
		return getCurSqlSessionTemplate().update(FinTradeFlow.class.getName() + UPDATE_BY_FLOW_ID, finTradeFlow);
	}

	@Override
	public int deleteByFlowId(String flowId) {
		return getCurSqlSessionTemplate().delete(FinTradeFlow.class.getName() + DEL_BY_FLOW_ID, flowId);
	}

	@Override
	public int deleteByFlowIdBatch(List<String> flowIdList) {
		return getCurSqlSessionTemplate().delete(FinTradeFlow.class.getName() + DEL_BY_FLOW_ID_BATCH, flowIdList);
	}

	@Override
	public FinTradeFlow findFreezeTradeFlow(String pFlowId) {
		return (FinTradeFlow) getCurSqlSessionTemplate().selectOne(FinTradeFlow.class.getName() + ".findFreezeTradeFlow",
				pFlowId);
	}

	@Override
	public List<FinTradeFlow> findFreezeTradeFlowByPflowIds(List<Integer> bidInvestIds) {
		List<String> params = new ArrayList<String>();
		bidInvestIds.forEach(investId->{
			params.add(String.valueOf(investId));
		});
		return getCurSqlSessionTemplate().selectList(FinTradeFlow.class.getName() + ".findFreezeTradeFlowByPflowIds", params);
	}

	@Override
	public FinTradeFlow findByFlowId(String pFlowId, int tradeType) {
		FinTradeFlow tradeFlow = new FinTradeFlow();
		tradeFlow.setPflowId(pFlowId);
		tradeFlow.setTradeType(tradeType);
		return (FinTradeFlow) getCurSqlSessionTemplate().selectOne(FinTradeFlow.class.getName() + FIND_BY_P_FLOW_ID_TRADETYPE,
				tradeFlow);
	}
	public FinTradeFlow findByPflowIdAndTradeType(FinTradeFlow cdt) {
		return getCurSqlSessionTemplate().selectOne(FinTradeFlow.class.getName() + ".findByPflowIdAndTradeType", cdt);
	}

	@Override
	public List<FinTradeFlow> findTradeFlowByPflowIds(Set<Integer> pflowIds, Integer tradeType) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pflowIds",pflowIds);
		params.put("tradeType",tradeType);
		return getCurSqlSessionTemplate().selectList(FinTradeFlow.class.getName() + ".findTradeFlowByPflowIds", params);
	}
}
