package com.hongkun.finance.loan.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.loan.constants.RepayConstants;
import com.hongkun.finance.loan.dao.BidReceiptPlanDao;
import com.hongkun.finance.loan.dao.BidRepayPlanDao;
import com.hongkun.finance.loan.dao.BidReturnCapRecordDao;
import com.hongkun.finance.loan.model.BidReceiptPlan;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.loan.model.BidReturnCapRecord;
import com.hongkun.finance.loan.model.vo.BidRepayPlanVo;
import com.hongkun.finance.loan.model.vo.LoanVO;
import com.hongkun.finance.loan.service.BidRepayPlanService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.json.JsonUtils;
import com.yirun.framework.core.utils.pager.Pager;
import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.dubbo.context.DubboTransactionContextEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.loan.service.impl.BidRepayPlanServiceImpl.java
 * @Class Name : BidRepayPlanServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class BidRepayPlanServiceImpl implements BidRepayPlanService {

	private static final Logger logger = LoggerFactory.getLogger(BidRepayPlanServiceImpl.class);

	/**
	 * BidRepayPlanDAO
	 */
	@Autowired
	private BidRepayPlanDao bidRepayPlanDao;
	@Autowired
	private BidReceiptPlanDao bidReceiptPlanDao;
	@Autowired
	private BidReturnCapRecordDao bidReturnCapRecordDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidRepayPlan(BidRepayPlan bidRepayPlan) {
		this.bidRepayPlanDao.save(bidRepayPlan);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidRepayPlanBatch(List<BidRepayPlan> list) {
		this.bidRepayPlanDao.insertBatch(BidRepayPlan.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidRepayPlanBatch(List<BidRepayPlan> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.bidRepayPlanDao.insertBatch(BidRepayPlan.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidRepayPlan(BidRepayPlan bidRepayPlan) {
		this.bidRepayPlanDao.update(bidRepayPlan);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBidRepayPlanBatch(List<BidRepayPlan> list, int count) {
		this.bidRepayPlanDao.updateBatch(BidRepayPlan.class, list, count);
	}

	@Override
	public BidRepayPlan findBidRepayPlanById(int id) {
		return this.bidRepayPlanDao.findByPK(Long.valueOf(id), BidRepayPlan.class);
	}

	@Override
	public List<BidRepayPlan> findBidRepayPlanList(BidRepayPlan bidRepayPlan) {
		return this.bidRepayPlanDao.findByCondition(bidRepayPlan);
	}

	@Override
	public List<BidRepayPlan> findBidRepayPlanList(BidRepayPlan bidRepayPlan, int start, int limit) {
		return this.bidRepayPlanDao.findByCondition(bidRepayPlan, start, limit);
	}

	@Override
	public Pager findBidRepayPlanList(BidRepayPlan bidRepayPlan, Pager pager) {
		return this.bidRepayPlanDao.findByCondition(bidRepayPlan, pager);
	}

	@Override
	public int findBidRepayPlanCount(BidRepayPlan bidRepayPlan) {
		return this.bidRepayPlanDao.getTotalCount(bidRepayPlan);
	}

	public List<BidRepayPlan> generateRepayPlan(int repayType) {
		return null;
	}

	@Override
	public Date findLastRepayPlanTime(Integer bidId) {
		return this.bidRepayPlanDao.findLastRepayPlanTime(bidId);
	}

	@Override
	public Date findNextRepayPlanTime(Integer bidId) {
		return this.bidRepayPlanDao.findNextRepayPlanTime(bidId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseEntity<?> validateRepayPlan(int repayId, int regUserId) {
        ResponseEntity result = new ResponseEntity<>(SUCCESS);
        result.getParams().put("isLastPeriod", 0);
		/**
		 * 1、校验还款计划
		 **/
		BidRepayPlan repayPlan = bidRepayPlanDao.findByPK(new Long(repayId), BidRepayPlan.class);
		if (repayPlan == null) {
			return new ResponseEntity<>(ERROR, "还款计划不存在");
		}
		if (repayPlan.getState() == 2) {
			return new ResponseEntity<>(ERROR, "该期已还款！");
		}
		if (repayPlan.getRegUserId() == null || !repayPlan.getRegUserId().equals(regUserId)) {
			return new ResponseEntity<>(ERROR, "非本人还款！");
		}
		// 校验本期还款是否当前还款期数
		BidRepayPlan currRepayPlan = bidRepayPlanDao.findCurrRepayPlan(repayPlan.getBidId());
		if (repayPlan.getId() != currRepayPlan.getId().intValue()) {
			return new ResponseEntity<>(RepayConstants.REPAY_RETURN_STATE, "请先还第" + currRepayPlan.getPeriods() + "期的借款！");
		}
		//标识是否为最后一期
        if(this.bidRepayPlanDao.findLastRepayPlanTime(repayPlan.getBidId()).compareTo(currRepayPlan.getPlanTime()) == 0){
            result.getParams().put("isLastPeriod", 1);
        }
		BidReceiptPlan receiptPlan = new BidReceiptPlan();
		receiptPlan.setBidId(repayPlan.getBidId());
		receiptPlan.setPeriods(repayPlan.getPeriods());
		receiptPlan.setState(1);
		List<BidReceiptPlan> receiptPlans = bidReceiptPlanDao.findByCondition(receiptPlan);
		if (receiptPlans == null || receiptPlans.isEmpty()) {
			return new ResponseEntity<>(ERROR, "回款计划数据异常！");
		}
		result.getParams().put("bidRepayPlan", repayPlan);
		result.getParams().put("bidReceiptPlanList", receiptPlans);
		return result;
	}

	@Override
	public ResponseEntity<?> countRepayPlanAmount(BidRepayPlan bidRepayPlan) {
		Map<String, Object> map = bidRepayPlanDao.countRepayPlanAmount(bidRepayPlan);
		if (map == null) {
			return new ResponseEntity<>(ERROR, "无有效数据！");
		}
		ResponseEntity<?> responseEntity = new ResponseEntity<>(SUCCESS);
		responseEntity.setParams(map);
		return responseEntity;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateForAdvanceRepaycaptail(List<BidRepayPlan> insertRepayPlanList, List<BidRepayPlan> delRepayPlanList,
			List<BidReceiptPlan> insertReceiptPlanList, List<BidReceiptPlan> delReceiptPlanList, BidReturnCapRecord bidReturnCapRecord) {
        logger.info("tcc try updateForAdvanceRepaycaptail, reference RepayFacadeImpl#doRepay. 提前还本. 新的还款计划: {}, " +
                        "删除的还款计划: {}, 插入的回款计划: {}, 删除的回款计划: {}, 还本记录:{}", JsonUtils
                        .toJson(insertRepayPlanList), JsonUtils.toJson(delRepayPlanList), JsonUtils.toJson(insertReceiptPlanList), 
                JsonUtils.toJson(delReceiptPlanList), JsonUtils.toJson(bidReturnCapRecord));
        try{
            if (delRepayPlanList != null && !delRepayPlanList.isEmpty()) {
                delRepayPlanList.forEach(this.bidRepayPlanDao::delete);
            }
            if (insertRepayPlanList != null && !insertRepayPlanList.isEmpty()) {
                this.bidRepayPlanDao.insertBatch(BidRepayPlan.class, insertRepayPlanList);
            }
            if (delReceiptPlanList != null && !delReceiptPlanList.isEmpty()) {
                delReceiptPlanList.forEach(this.bidReceiptPlanDao::delete);
            }
            if (insertReceiptPlanList != null && !insertReceiptPlanList.isEmpty()) {
                this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, insertReceiptPlanList);
            }
            this.bidReturnCapRecordDao.save(bidReturnCapRecord);
        }catch(Exception e){
            logger.error("tcc try updateForAdvanceRepaycaptail, reference RepayFacadeImpl#doRepay. 提前还本. 新的还款计划: {}, " +
                            "删除的还款计划: {}, 插入的回款计划: {}, 删除的回款计划: {}, 还本记录:{}", JsonUtils
                            .toJson(insertRepayPlanList), JsonUtils.toJson(delRepayPlanList), JsonUtils.toJson(insertReceiptPlanList),
                    JsonUtils.toJson(delReceiptPlanList), JsonUtils.toJson(bidReturnCapRecord), e);
            throw new GeneralException("更新还款、回款计划失败");
        }
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateForRepay(BidRepayPlan bidRepayPlan, List<BidReceiptPlan> bidReceiptPlanList) {
	    try{
            this.bidRepayPlanDao.update(bidRepayPlan);
            this.bidReceiptPlanDao.updateBatch(BidReceiptPlan.class, bidReceiptPlanList, 1000);
	    }catch(Exception e){
	        throw new GeneralException("更新还款计划失败");
	    }
	}

	@Override
	public Pager findRepayPlanCountList(Pager pager, LoanVO loanVO) {
		return this.bidRepayPlanDao.findByCondition(loanVO, pager, BidRepayPlan.class, ".countRepayPlan");
	}

	@Override
	@Compensable(cancelMethod = "insertBidRepayPlanBatchCancel", transactionContextEditor = DubboTransactionContextEditor.class)
	public void insertBidRepayPlanBatch(List<BidRepayPlan> repayPlans, List<BidReceiptPlan> receiptPlans) {
		if(CommonUtils.isNotEmpty(repayPlans)){
			this.bidRepayPlanDao.insertBatch(BidRepayPlan.class, repayPlans);
		}
		if(CommonUtils.isNotEmpty(receiptPlans)){
			this.bidReceiptPlanDao.insertBatch(BidReceiptPlan.class, receiptPlans);
		}
	}
	
	
	/**
	 *  @Description    : 回滚方法  insertBidRepayPlanBatch
	 *  @Method_Name    : insertBidRepayPlanBatchCancel
	 *  @param repayPlans
	 *  @return         : void
	 *  @Creation Date  : 2017年10月17日 上午9:47:08 
	 *  @Author         : xuhuiliu@hongkun.com.cn 劉旭輝
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBidRepayPlanBatchCancel(List<BidRepayPlan> repayPlans,List<BidReceiptPlan> receiptPlans) {
		if(CommonUtils.isNotEmpty(repayPlans)){
			this.bidRepayPlanDao.delBidRepayPlanBatch(repayPlans);
		}
		if(CommonUtils.isNotEmpty(receiptPlans)){
			this.bidReceiptPlanDao.delBidReceiptPlanBatch(receiptPlans);
		}
	}

	@Override
	public ResponseEntity<?> findRepayPlanListWithStatistics(Pager pager, BidRepayPlan bidRepayPlan) {
		ResponseEntity<?> result = new ResponseEntity<>(Constants.SUCCESS, this.bidRepayPlanDao.findByCondition(bidRepayPlan, pager));
		result.getParams().putAll(this.bidRepayPlanDao.countRepayPlanAmount(bidRepayPlan));
		return result;
	}

	@Override
	public ResponseEntity<?> findReplanListByBidId(Integer bidInfoId) {
		Pager pager = new Pager();
		pager.setData(bidRepayPlanDao.findReplanListByBidId(bidInfoId));
		return new ResponseEntity<>(Constants.SUCCESS,pager);
	}

	@Override
	public List<BidRepayPlan> findLastRepayPlanTimeByIds(List<Integer> bidIds) {
		return bidRepayPlanDao.findLastRepayPlanTimeByIds(bidIds);
	}

	@Override
	public Map<String, Object> findEndDateAndSumInterest(List<Integer> bidIds, List<Integer> investIds) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<BidRepayPlan> endDateList = CommonUtils.isNotEmpty(bidIds)?bidRepayPlanDao.findLastRepayPlanTimeByIds(bidIds):null;
		List<Map<String,Object>> interestList = CommonUtils.isNotEmpty(investIds)?
				bidReceiptPlanDao.findSumInterestByInvestIds(investIds):null;
		result.put("endDateList", endDateList);
		result.put("interestList", interestList);
		return result;
	}

	@Override
	public List<Map<String, Object>> findCurrRepayPlanIdByBidIds(List<Integer> bidIds, Integer regUserId) {
		return this.bidRepayPlanDao.findCurrRepayPlanIdByBidIds(bidIds, regUserId);
	}

    @Override
    public List<BidRepayPlan> findNeedNoticeRepayPlanList() {
            return this.bidRepayPlanDao.findNeedNoticeRepayPlanList();
    }

	@Override
	public List<BidRepayPlan> findBidRepayPlanListForStaIncomes(BidRepayPlan planCdt) {
		return this.bidRepayPlanDao.findBidRepayPlanListForStaIncomes(planCdt);
	}

	@Override
	public BigDecimal findSumRepayAtmByBidId(Integer bidId) {
		return this.bidRepayPlanDao.findSumRepayAtmByBidId(bidId);
	}

	@Override
	public Pager findRepayPlanListForManageStatistics(Pager pager, BidRepayPlanVo bidRepayPlanVo) {
		return this.bidRepayPlanDao.findRepayPlanListForManageStatistics(bidRepayPlanVo, pager);
	}

    @Override
    public List<BidRepayPlan> findRepayedSta(List<Integer> bidIds) {
        return this.bidRepayPlanDao.findRepayedSta(bidIds);
    }
}
