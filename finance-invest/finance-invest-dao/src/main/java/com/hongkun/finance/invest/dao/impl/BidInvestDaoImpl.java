package com.hongkun.finance.invest.dao.impl;

import com.hongkun.finance.invest.dao.BidInvestDao;
import com.hongkun.finance.invest.model.BidInvest;
import com.hongkun.finance.invest.model.vo.BidInvestDetailVO;
import com.hongkun.finance.invest.model.vo.BidInvestForRecommendVo;
import com.hongkun.finance.invest.model.vo.BidInvestVoForApp;
import com.hongkun.finance.invest.model.vo.StaFunInvestVO;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.invest.dao.impl.BidInvestDaoImpl.java
 * @Class Name : BidInvestDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class BidInvestDaoImpl extends MyBatisBaseDaoImpl<BidInvest, java.lang.Long> implements BidInvestDao {

	/**
	 * 根据产品类型，标的状态查询 投资有效总金额
	 */

	public String FIND_INVEST_SUM_AMOUNT = ".findInvestSumAmount";

	/**
	 * 根据产品类型，标的状态查询 投资有效记录
	 */

	public String FIND_INVESTS = ".findInvests";
	/**
	 * 根据产品类型，标的状态，查询标的总额
	 */
	public String FIND_INVEST_BID_SUM_AMOUNT = ".findInvestBidSumAmount";
	/**
	 * 查询个人投资数量
	 */
	public String FIND_INVEST_COUNT = ".findBidInvestCount";
	/**
	 * 根据标的ID及投资状态查询投资记录信息
	 */
	public String FIND_INVEST_RECORD = ".findInvestRecord";
	/**
	 * 根据用户id查询用户投资总金额
	 */
	public String FIND_SUM_INVEST_AMOUNT_BY_USERID = ".findSumInvestAmountByUserId";
	/**
	 * 根据用户id查询用户投资总金额（折价后）
	 */
	public String FIND_SUM_NIGGER_AMOUNT_BY_USERID = ".findSumNiggerAmountByUserId";
	/**
	 * 获取优选标对应可匹配的投资记录集合
	 */
	private final String FIND_TO_BE_MATCHED_BID_INVEST_LIST = ".findToBeMatchedBidInvestList";
	/**
	 * 查询BIDINFOID集合
	 */
	private String FIND_BIDINFOID_BY_CONDIITION = ".findBidInfoIdByCondition";

	@Override
	public List<BidInvest> findByGoodBidId(Integer id) {
		BidInvest contidion = new BidInvest();
		contidion.setId(id);
		List<BidInvest> result = super.findByCondition(contidion);
		if (result != null && result.size() > 0) {
			return result;
		}
		return null;
	}

	@Override
	public Map<String, Object> findSumAmountByBidId(int bidInfoId) {
		return super.getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + ".findSumAmountByBidId",
				bidInfoId);
	}

	@Override
	public BigDecimal findInvestSumAmount(Integer productType, Integer bidState, Integer regUserId) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("productType", productType);
		parameter.put("bidState", bidState);
		parameter.put("regUserId", regUserId);
		return super.getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_INVEST_SUM_AMOUNT,
				parameter);
	}

	@Override
	public List<BidInvest> findInvests(Integer productType, Integer bidState, Integer regUserId) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("productType", productType);
		parameter.put("bidState", bidState);
		parameter.put("regUserId", regUserId);
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + FIND_INVESTS, parameter);
	}

	@Override
	public int findBidInvestCount(Integer bidId, Integer investUserId, Date createTime, Date endTime,
			Integer recommendState) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("bidId", bidId);
		parameter.put("regUserId", investUserId);
		if (createTime != null
				&& !"0001-01-01 00:00:00".equals(DateUtils.format(createTime, DateUtils.DATE_HH_MM_SS))) {
			parameter.put("createTime", createTime);
		}
		parameter.put("endTime", endTime);
		parameter.put("recommendState", recommendState);
		return getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_INVEST_COUNT, parameter);
	}

	@Override
	public BidInvest findInvestRecord(Integer bidId, List<Integer> state) {
		HashMap<String, Object> parameter = new HashMap<>();
		parameter.put("bidInfoId", bidId);
		parameter.put("state", state);
		return getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_INVEST_RECORD, parameter);
	}

	@Override
	public BigDecimal findSumInvestAmountByUserId(Integer regUserId) {
		return super.getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_SUM_INVEST_AMOUNT_BY_USERID,
				regUserId);
	}

	@Override
	public BigDecimal findSumNiggerAmountByUserId(Integer regUserId) {
		return super.getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_SUM_NIGGER_AMOUNT_BY_USERID,
				regUserId);
	}

	@Override
	public List<BidInvestForRecommendVo> findBidInvestByRegUserIds(List<Integer> userIds) {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestByRegUserIds",
				userIds);
	}

	@Override
	public Integer findBidInvestCountForPrefered(Integer regUserId) {
		Long count = super.getCurSqlSessionTemplate()
				.selectOne(BidInvest.class.getName() + ".findBidInvestCountForPrefered", regUserId);
		return count.intValue();
	}

	@Override
	public List<BidInvestVoForApp> findBidInvestListByIds(List<Integer> investIds) {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestListByIds",
				investIds);
	}

	@Override
	public List<Integer> findBidInvestPreferedList() {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestPreferedList");
	}

	@Override
	public List<BidInvest> findToBeMatchedBidInvestList(Integer goodBidId) {
		return super.getCurSqlSessionTemplate()
				.selectList(BidInvest.class.getName() + FIND_TO_BE_MATCHED_BID_INVEST_LIST, goodBidId);
	}

	@Override
	public List<BidInvest> findBidInvestListByIdList(List<Integer> investIds) {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestListByIdList",
				investIds);
	}

	@Override
	public void delBatchByIds(List<Integer> delIds) {
		super.getCurSqlSessionTemplate().delete(BidInvest.class.getName() + ".delBatchByIds", delIds);
	}

	@Override
	public List<BidInvest> findGoodBidInvestListByIdList(Set<Integer> investIds) {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findGoodBidInvestListByIdList",
				investIds);
	}

	@Override
	public List<Integer> findUserThreeMonthInvest(Set<Integer> userIdList) {
		return super.getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findUserThreeMonthInvest",
				userIdList);
	}

	@Override
	public int findBidInvestCount(Integer investUserId, Integer bidType) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("regUserId", investUserId);
		parameter.put("bidType", bidType);
		return getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + FIND_INVEST_COUNT, parameter);
	}

	@Override
	public List<BidInvest> findGoodInvestMatchCommonInvestList(BidInvest condition) {
		return getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findGoodInvestMatchCommonInvestList",
				condition);
	}

    @Override
	public List<Integer> findBidInfoIdByCondition(BidInvest bidInvest) {
		return getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + FIND_BIDINFOID_BY_CONDIITION,
				bidInvest);
	}

    @Override
    public Map<String, Object> findStaFunInvestAddup(StaFunInvestVO staFunInvestVO) {
        return getCurSqlSessionTemplate().selectOne(BidInvest.class.getName() + ".findStaFunInvestAddup", staFunInvestVO);
    }

    @Override
    public List<BidInvestDetailVO> findBidInvestDetailList(BidInvestDetailVO bidInvestDetailVO) {
        return getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestDetailList", bidInvestDetailVO);
    }

	@Override
	public Integer getSelfAndInvitorInvestCount(List<Integer> userIds) {
		return getCurSqlSessionTemplate()
				.selectOne(BidInvest.class.getName() + ".getSelfAndInvitorInvestCount", userIds);
	}

	@Override
	public Integer getSelfAndInvitorTransferCount(List<Integer> userIds) {
		return getCurSqlSessionTemplate()
				.selectOne(BidInvest.class.getName() + ".getSelfAndInvitorTransferCount", userIds);
	}

	@Override
	public List<BidInvest> findBidInvestListByCondition(BidInvest condition) {
		return getCurSqlSessionTemplate().selectList(BidInvest.class.getName() + ".findBidInvestListByCondition", condition);
	}
}
