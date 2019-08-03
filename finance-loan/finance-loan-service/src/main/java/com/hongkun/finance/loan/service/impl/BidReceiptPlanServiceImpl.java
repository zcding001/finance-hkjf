package com.hongkun.finance.loan.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.loan.dao.BidReceiptPlanDao;
import com.hongkun.finance.loan.dao.BidRepayPlanDao;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.vo.BidReceiptPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidReceiptPlanService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.hongkun.finance.loan.constants.RepayConstants.*;
/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.loan.service.impl.BidReceiptPlanServiceImpl.java
 * @Class Name : BidReceiptPlanServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidReceiptPlanServiceImpl implements BidReceiptPlanService {

	private static final Logger logger = LoggerFactory.getLogger(BidReceiptPlanServiceImpl.class);

	/**
	 * BidReceiptPlanDAO
	 */
	@Autowired
	private BidReceiptPlanDao bidReceiptPlanDao;
	@Autowired
	private BidRepayPlanDao bidRepayPlanDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReceiptPlan(BidReceiptPlan bidReceiptPlan) {
		this.bidReceiptPlanDao.save(bidReceiptPlan);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReceiptPlanBatch(List<BidReceiptPlan> list) {
		this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReceiptPlanBatch(List<BidReceiptPlan> list, int count) {
		this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, list, count);
	}
	/**
	 * 
	 *  @Description    : tcc回滚方法  insertBidReceiptPlanBatch
	 *  @Method_Name    : insertBidReceiptPlanBatchCancel
	 *  @param list
	 *  @param count
	 *  @return         : void
	 *  @Creation Date  : 2017年10月17日 上午11:40:31 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidReceiptPlanBatchCancel(List<BidReceiptPlan> list, int count) {
		if(CommonUtils.isNotEmpty(list)){
			this.bidReceiptPlanDao.delBidReceiptPlanBatch(list);
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidReceiptPlan(BidReceiptPlan bidReceiptPlan) {
		this.bidReceiptPlanDao.update(bidReceiptPlan);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidReceiptPlanBatch(List<BidReceiptPlan> list, int count) {
		this.bidReceiptPlanDao.updateBatch(BidReceiptPlan.class, list, count);
	}

	@Override
	public BidReceiptPlan findBidReceiptPlanById(int id) {
		return this.bidReceiptPlanDao.findByPK(Long.valueOf(id), BidReceiptPlan.class);
	}

	@Override
	public List<BidReceiptPlan> findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan) {
		bidReceiptPlan.setSortColumns("periods asc");
		return this.bidReceiptPlanDao.findByCondition(bidReceiptPlan);
	}

	@Override
	public List<BidReceiptPlan> findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan, int start, int limit) {
		return this.bidReceiptPlanDao.findByCondition(bidReceiptPlan, start, limit);
	}

	@Override
	public Pager findBidReceiptPlanList(BidReceiptPlan bidReceiptPlan, Pager pager) {
		return this.bidReceiptPlanDao.findByCondition(bidReceiptPlan, pager);
	}

	@Override
	public int findBidReceiptPlanCount(BidReceiptPlan bidReceiptPlan) {
		return this.bidReceiptPlanDao.getTotalCount(bidReceiptPlan);
	}

	@Override
	public BigDecimal findSumReceivedInterest(Integer investId) {
		BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
		bidReceiptPlan.setInvestId(investId);
		bidReceiptPlan.setState(2);
		return this.bidReceiptPlanDao.getSumInterest(bidReceiptPlan);
	}

	@Override
	public BigDecimal findSumNotReceivedInterest(Integer investId) {
		BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
		bidReceiptPlan.setInvestId(investId);
		bidReceiptPlan.setState(1);
		return this.bidReceiptPlanDao.getSumInterest(bidReceiptPlan);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void delBidReceiptPlanBatch(List<BidReceiptPlan> salReceiptPlans) {
		this.bidReceiptPlanDao.delBidReceiptPlanBatch(salReceiptPlans);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateOrInsertBidReceiptPlanBatch(List<BidReceiptPlan> updatePlans, List<BidReceiptPlan> insertPlans) {
		if (updatePlans != null) {
			this.bidReceiptPlanDao.updateBatch(BidReceiptPlan.class, updatePlans, 1000);
		}
		if (insertPlans != null) {
			this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, insertPlans);
		}
	}

	@Override
	public LoanVO findAgencyFundByUserId(Integer userId, List<Integer> bidIds) {
		return this.bidReceiptPlanDao.findAgencyFundByUserId(userId, bidIds);
	}

    @Override
    public List<LoanVO> findAgencyFundByUserId(List<Integer> regUserIds) {
        return this.bidReceiptPlanDao.findAgencyFundByUserId(regUserIds);
    }

	@Override
	public Pager findReceiptPlanCountList(Pager pager, LoanVO loanVO) {
		return this.bidReceiptPlanDao.findByCondition(loanVO, pager, BidReceiptPlan.class, ".countReceiptPlan");
	}

	@Override
	public BidReceiptPlan findNextReceiptPlan(Integer investId) {
		return this.bidReceiptPlanDao.findNextReceiptPlan(investId);
	}

	@Override
	public BidReceiptPlan findLastReceiptPlan(Integer investId) {
		return this.bidReceiptPlanDao.findLastReceiptPlan(investId);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void buyCreditorForReceiptPlan(BidReceiptPlan salReceiptPlanFirst, List<BidReceiptPlan> buyReceiptPlans,
			List<BidReceiptPlan> salReceiptPlans) {
		if (salReceiptPlanFirst.getId() !=null && salReceiptPlanFirst.getId()>0){
			this.bidReceiptPlanDao.update(salReceiptPlanFirst);
		}
		if (CommonUtils.isNotEmpty(buyReceiptPlans)){
			this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, buyReceiptPlans);
		}
		if (CommonUtils.isNotEmpty(salReceiptPlans)){
			this.bidReceiptPlanDao.delBidReceiptPlanBatch(salReceiptPlans);
		}
	}

	@Override
	public BigDecimal findSumInterest(Integer newInvestId) {
		BidReceiptPlan bidReceiptPlan = new BidReceiptPlan();
		bidReceiptPlan.setInvestId(newInvestId);
		return this.bidReceiptPlanDao.getSumInterest(bidReceiptPlan);
	}

	@Override
	public Map<String, Object> findParamsForTransferInterestRate(Integer investId) {
		Map<String,Object> result = new HashMap<String,Object>();
		BigDecimal notReceivedInterest = findSumNotReceivedInterest(investId);
		BidReceiptPlan nextReceiptPlan = this.bidReceiptPlanDao.findNextReceiptPlan(investId);
		BidReceiptPlan lastReceiptPlan =  this.bidReceiptPlanDao.findLastReceiptPlan(investId);
		result.put(NOT_RECEIVED_INTEREST, notReceivedInterest);
		result.put(RECEIPT_NEXT_PLAN, nextReceiptPlan);
		result.put(RECEIPT_LAST_PLAN, lastReceiptPlan);
		return result;
	}

	@Override
	public Map<String, Object> findTransferManualDetail(Integer bidId, Integer investId) {
		Map<String,Object> result = new HashMap<String,Object>();
		Date endDate = this.bidRepayPlanDao.findLastRepayPlanTime(bidId);
		Date nextDate = bidRepayPlanDao.findNextRepayPlanTime(bidId);
		BigDecimal receivedInterest = findSumReceivedInterest(investId);
		BigDecimal notReceivedInterest = findSumNotReceivedInterest(investId);
		result.put(REPAY_END_DATE, endDate);
		result.put(NOT_RECEIVED_INTEREST, notReceivedInterest);
		result.put(RECEIVED_INTEREST, receivedInterest);
		result.put("nextDate",nextDate);
		return result;
	}
	
	@Override
	public Map<String, Object> findReceiptPlanListWithStatistics(Pager pager, BidReceiptPlan bidReceiptPlan) {
		Map<String, Object> map = this.bidReceiptPlanDao.countWaitReceiptPlanAmount(bidReceiptPlan);
		map.put("pager", this.bidReceiptPlanDao.findByCondition(bidReceiptPlan, pager, ".countReceiptPlanAmountList"));
		return map;
	}

    @Override
    public Map<String, Object> findReceiptPlanListStatistics(BidReceiptPlan bidReceiptPlan) {
        return this.bidReceiptPlanDao.countWaitReceiptPlanAmount(bidReceiptPlan);
    }

	@Override
	public List<Map<String, Object>> findSumInterestByInvestIds(List<Integer> investIds) {
		return this.bidReceiptPlanDao.findSumInterestByInvestIds(investIds);
	}

	@Override
	public List<Integer> findUserThreeMonthPlan(Set<Integer> userIdList) {
		return this.bidReceiptPlanDao.findUserThreeMonthPlan(userIdList);
	}

	@Override
	public BidReceiptPlan findGoodBidReceiptPlan(BidReceiptPlan bidReceiptPlan) {
		List<BidReceiptPlan> plans = this.bidReceiptPlanDao.findByCondition(bidReceiptPlan);
		return CommonUtils.isNotEmpty(plans)?plans.get(0):null;
	}

	@Override
	public ResponseEntity<?> findReceiptPlanListForManageStatistics(Pager pager, BidReceiptPlanVo bidReceiptPlanVo) {
		return new ResponseEntity<>(Constants.SUCCESS, this.bidReceiptPlanDao.findReceiptPlanListForManageStatistics(bidReceiptPlanVo, pager));

	}

    @Override
    public Pager findBidReceiptPlanListByUserId(BidReceiptPlanVo bidReceiptPlanVo, Pager pager) {
		return this.bidReceiptPlanDao.findBidReceiptPlanListByUserId(bidReceiptPlanVo,pager);
    }

	@Override
	public BigDecimal findSumInterestByInvestId(Integer newInvestId) {
		return this.bidReceiptPlanDao.findSumInterestByInvestId(newInvestId);
	}

	@Override
	public Date findMinPlanTimeDue(Integer regUserId) {
		return this.bidReceiptPlanDao.findMinPlanTimeDue(regUserId);
	}

    @Override
    public List<Map<String, Object>> findReceiptPlanCountDetail(Integer regUserId) {
        return this.bidReceiptPlanDao.findReceiptPlanCountDetail(regUserId);
    }
}
