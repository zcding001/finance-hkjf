package com.hongkun.finance.loan.dao.impl;

import com.hongkun.finance.loan.dao.BidReceiptPlanDao;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.loan.dao.impl.BidReceiptPlanDaoImpl.java
 * @Class Name    : BidReceiptPlanDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class BidReceiptPlanDaoImpl extends MyBatisBaseDaoImpl<BidReceiptPlan, java.lang.Long> implements BidReceiptPlanDao {
	
	/** 获取利息总和*/
	public String GET_SUM_INTEREST = ".getSumInterest";
	/**条件删除回款计划*/
	public String DEL_BY_CONDITION = ".delByCondition";
	/**提前结清**/
	public String UPDATE_FOR_REAPY_SETTLE = ".updateForRepaySettle";
	public String DEL_BIDRECEIPTPLAN_BATCH = ".delBidReceiptPlanBatch";
	
	@Override
	public BigDecimal getSumInterest(BidReceiptPlan bidReceiptPlan) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName()+GET_SUM_INTEREST, bidReceiptPlan);
	}

	@Override
	public void delete(BidReceiptPlan bidReceiptPlan) {
		super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + DEL_BY_CONDITION, bidReceiptPlan);
	}

	@Override
	public void delBidReceiptPlanBatch(List<BidReceiptPlan> salReceiptPlans) {
		super.getCurSqlSessionTemplate().delete(BidReceiptPlan.class.getName()+DEL_BIDRECEIPTPLAN_BATCH,salReceiptPlans);
	}
	
	@Override
	public int updateReceiptPlanForRepaySettle(BidReceiptPlan bidRecepitPlan) {
		return super.getCurSqlSessionTemplate().update(BidReceiptPlan.class.getName() + UPDATE_FOR_REAPY_SETTLE, bidRecepitPlan);
	}

	@Override
	public LoanVO findAgencyFundByUserId(Integer userId, List<Integer> bidIds) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("userId", userId);
        param.put("bidIds", bidIds);
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".findAgencyFundByUserId", param);
	}

    @Override
    public List<LoanVO> findAgencyFundByUserId(List<Integer> regUserIds) {
        return super.getCurSqlSessionTemplate().selectList(BidReceiptPlan.class.getName() + ".findAgencyFundByUserIds", regUserIds);
    }

	@Override
	public BidReceiptPlan findNextReceiptPlan(Integer investId) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".findNextReceiptPlan", investId);
	}

	@Override
	public BidReceiptPlan findLastReceiptPlan(Integer investId) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".findLastReceiptPlan", investId);
	}

	@Override
	public Map<String, Object> countWaitReceiptPlanAmount(BidReceiptPlan bidReceiptPlan) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".countWaitReceiptPlanAmount",
				bidReceiptPlan);
	}

	@Override
	public List<Map<String, Object>> findSumInterestByInvestIds(List<Integer> investIds) {
		return super.getCurSqlSessionTemplate().selectList(BidReceiptPlan.class.getName()+".findSumInterestByInvestIds",investIds);
	}

	@Override
	public List<Integer> findUserThreeMonthPlan(Set<Integer> userIdList) {
		return super.getCurSqlSessionTemplate().selectList(BidReceiptPlan.class.getName() +
				".findUserThreeMonthPlan",userIdList);
	}

    @Override
    public Pager findReceiptPlanListForManageStatistics(BidReceiptPlanVo bidReceiptPlanVo, Pager pager) {
        return super.findByCondition(bidReceiptPlanVo,pager, BidReceiptPlan.class,".findReceiptPlanListForManageStatistics");

    }

    @Override
    public Pager findBidReceiptPlanListByUserId(BidReceiptPlanVo bidReceiptPlanVo, Pager pager) {
		return super.findByCondition(bidReceiptPlanVo,pager, BidReceiptPlan.class,".findBidReceiptPlanListByUserId");
    }

	@Override
	public BigDecimal findSumInterestByInvestId(Integer newInvestId) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".findSumInterestByInvestId",
				newInvestId);
	}

	@Override
	public Date findMinPlanTimeDue(Integer regUserId) {
		return super.getCurSqlSessionTemplate().selectOne(BidReceiptPlan.class.getName() + ".findMinPlanTimeDue",
				regUserId);
	}

    @Override
    public List<Map<String, Object>> findReceiptPlanCountDetail(Integer regUserId) {
        return super.getCurSqlSessionTemplate().selectList(BidReceiptPlan.class.getName() + ".findReceiptPlanCountDetail", regUserId);
    }
}
