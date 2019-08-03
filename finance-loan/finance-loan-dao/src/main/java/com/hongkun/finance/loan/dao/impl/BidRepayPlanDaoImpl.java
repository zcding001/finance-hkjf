package com.hongkun.finance.loan.dao.impl;

import com.hongkun.finance.loan.dao.BidRepayPlanDao;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.loan.dao.impl.BidRepayPlanDaoImpl.java
 * @Class Name : BidRepayPlanDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class BidRepayPlanDaoImpl extends MyBatisBaseDaoImpl<BidRepayPlan, java.lang.Long> implements BidRepayPlanDao {

	/** 查询标的最后一次还款时间 */
	private static final String FIND_BID_LAST_REPAYPLANTIME = ".findLastRepayPlanTime";
	/** 查询标的下一次还款时间 */
	private static final String FIND_BID_NEXT_REPAYPLANTIME = ".findNextRepayPlanTime";
	/** 获取标的当前还款计划 */
	private static final String FIND_CURR_REPAYPLAN = ".findCurrRepayPlan";
	/** 统计当前还款计划 */
	private static final String COUNT_REPAYPLAN_AMOUNT = ".countRepayPlanAmount";
	/**条件删除还款计划*/
	private static final String DEL_REPAYPLAN_BY_CONDITION = ".delByCondtion";
	/**提前结清操作更新还款计划**/
	private static final String UPDATE_FOR_REPAY_SETTLE = ".updateForRepaySettle";
	/** 批量删除*/
	private static final String DELETE_REPAYPLAN_BATCH = ".deleteRepayPlanBatch";
	private static final String FIND_BY_BIDINFO_ID = ".findByBidinfoId";
	private static final String FIND_LASTREPAYPLANTIME_BY_IDS = ".findLastRepayPlanTimeByIds";

	@Override
	public Date findLastRepayPlanTime(Integer bidId) {
		return super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName() + FIND_BID_LAST_REPAYPLANTIME,
				bidId);
	}

	@Override
	public Date findNextRepayPlanTime(Integer bidId) {
		return super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName() + FIND_BID_NEXT_REPAYPLANTIME,
				bidId);
	}

	@Override
	public BidRepayPlan findCurrRepayPlan(Integer bidId) {
		return super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName() + FIND_CURR_REPAYPLAN,
				bidId);
	}

	@Override
	public Map<String, Object> countRepayPlanAmount(BidRepayPlan bidRepayPlan) {
		return super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName() + COUNT_REPAYPLAN_AMOUNT,
				bidRepayPlan);

	}

	@Override
	public void delete(BidRepayPlan bidRepayPlan) {
		super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName() + DEL_REPAYPLAN_BY_CONDITION,
				bidRepayPlan);
	}

	@Override
	public int updateRepayPlanForRepaySettle(BidRepayPlan bidRepayPlan) {
		return super.getCurSqlSessionTemplate().update(BidRepayPlan.class.getName() + UPDATE_FOR_REPAY_SETTLE,
				bidRepayPlan);
	}

	@Override
	public int delBidRepayPlanBatch(List<BidRepayPlan> repayPlans) {
		return super.getCurSqlSessionTemplate().delete(BidRepayPlan.class.getName()+DELETE_REPAYPLAN_BATCH,repayPlans);
	}

	@Override
	public List<BidRepayPlan> findReplanListByBidId(Integer bidInfoId) {
		return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName()+FIND_BY_BIDINFO_ID, bidInfoId);
	}

	@Override
	public List<BidRepayPlan> findLastRepayPlanTimeByIds(List<Integer> bidIds) {
		return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName()+FIND_LASTREPAYPLANTIME_BY_IDS, bidIds);
	}

	@Override
	public List<Map<String, Object>> findCurrRepayPlanIdByBidIds(List<Integer> bidIds, Integer regUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("bidIds", bidIds);
		params.put("regUserId", regUserId);
		return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName()+".findCurrRepayPlanIdByBidIds", params);
	}

    @Override
    public List<BidRepayPlan> findNeedNoticeRepayPlanList() {
        return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName()+".findNeedNoticeRepayPlanList");
    }

	@Override
	public List<BidRepayPlan> findBidRepayPlanListForStaIncomes(BidRepayPlan planCdt) {
		return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName()+".findBidRepayPlanListForStaIncomes",planCdt);
	}

	@Override
	public BigDecimal findSumRepayAtmByBidId(Integer bidId) {
		return super.getCurSqlSessionTemplate().selectOne(BidRepayPlan.class.getName()+".findSumRepayAtmByBidId",bidId);
	}

	@Override
	public Pager findRepayPlanListForManageStatistics(BidRepayPlanVo bidRepayPlanVo, Pager pager) {
		return super.findByCondition(bidRepayPlanVo, pager, BidRepayPlan.class, ".findRepayPlanListForManageStatistics");
	}

    @Override
    public List<BidRepayPlan> findRepayedSta(List<Integer> bidIds) {
        return super.getCurSqlSessionTemplate().selectList(BidRepayPlan.class.getName() + ".findRepayedSta", bidIds);
    }
}
