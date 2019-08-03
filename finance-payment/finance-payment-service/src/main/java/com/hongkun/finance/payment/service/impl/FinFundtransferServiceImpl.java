package com.hongkun.finance.payment.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.hongkun.finance.payment.constant.TradeTransferConstants;
import com.hongkun.finance.payment.dao.FinAccountDao;
import com.hongkun.finance.payment.dao.FinTradeFlowDao;
import com.hongkun.finance.payment.enums.FundtransferBigTypeStateEnum;
import com.hongkun.finance.payment.enums.FundtransferSmallTypeStateEnum;
import com.hongkun.finance.payment.model.FinAccount;
import com.hongkun.finance.payment.model.FinTradeFlow;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.CompareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinFundtransferDao;
import com.hongkun.finance.payment.model.FinFundtransfer;
import com.hongkun.finance.payment.service.FinFundtransferService;
import com.yirun.framework.core.utils.pager.Pager;

import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_MAKELOAN;
import static com.hongkun.finance.payment.constant.TradeTransferConstants.TRADE_TYPE_REPAY;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinFundtransferServiceImpl.
 *          java
 * @Class Name : FinFundtransferServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinFundtransferServiceImpl implements FinFundtransferService {
	private static final Logger logger = LoggerFactory.getLogger(FinFundtransferServiceImpl.class);

	final int BATCH_SIZE = 50;

	/**
	 * FinFundtransferDAO
	 */
	@Autowired
	private FinFundtransferDao finFundtransferDao;
	@Autowired
	private FinTradeFlowDao finTradeFlowDao;
	@Autowired
	private FinAccountDao finAccountDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insert(FinFundtransfer finFundtransfer) {
		return this.finFundtransferDao.save(finFundtransfer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBatch(List<FinFundtransfer> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i += BATCH_SIZE) {
				if (list.size() - i >= BATCH_SIZE) {
					this.finFundtransferDao.insertBatch(FinFundtransfer.class, list.subList(i, i + BATCH_SIZE),
							BATCH_SIZE);
				} else {
					this.finFundtransferDao.insertBatch(FinFundtransfer.class, list.subList(i, list.size()),
							list.size() - i);
				}
			}
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int update(FinFundtransfer finFundtransfer) {

		return this.finFundtransferDao.update(finFundtransfer);
	}

	@Override
	public FinFundtransfer findById(int id) {
		return this.finFundtransferDao.findByPK(new Long(id), FinFundtransfer.class);
	}

	@Override
	public List<FinFundtransfer> findByCondition(FinFundtransfer finFundtransfer) {
		return this.finFundtransferDao.findByCondition(finFundtransfer);
	}

	@Override
	public List<FinFundtransfer> findByCondition(FinFundtransfer finFundtransfer, int start, int limit) {
		return this.finFundtransferDao.findByCondition(finFundtransfer, start, limit);
	}

	@Override
	public Pager findByCondition(FinFundtransfer finFundtransfer, Pager pager) {
		return this.finFundtransferDao.findByCondition(finFundtransfer, pager);
	}

	@Override
	public int updateByFlowId(List<FinFundtransfer> finFundtransferList, Integer count) {
		return finFundtransferDao.updateByFlowId(finFundtransferList, count);
	}

	@Override
	public int deleteByFlowId(FinFundtransfer finFundtransfer) {
		return finFundtransferDao.deleteByFlowId(finFundtransfer);
	}

	@Override
	public int deleteByFlowIdBatch(List<FinFundtransfer> fundtransferList) {
		List<String> flowIdList = new ArrayList<String>();
		for (FinFundtransfer finFundtransfer : fundtransferList) {
			flowIdList.add(finFundtransfer.getFlowId());
		}
		return finFundtransferDao.deleteByFlowIdBatch(flowIdList);
	}

	@Override
	public BigDecimal findFintransferSumMoney(FinFundtransfer finFundtransfer) {
		return finFundtransferDao.findFintransferSumMoney(finFundtransfer);
	}

	@Override
	public Map<String, Object> findPageAndIncomeTotalMoney(FinFundtransfer finFundtransfer, Pager pager, String items) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Pager pagers = null;
		Integer condition = Integer.valueOf(items, 2);
		if (condition >= 4) {
			pagers = this.findByCondition(finFundtransfer, pager);
			resultMap.put("pager", pagers);
		}
		if (condition % 2 != 0) {
			resultMap.put("totalNum", pagers.getData().size());
		}
		if (condition == 3 || condition == 2 || condition == 6 || condition == 7) {
			resultMap.put("sumMoney", finFundtransferDao.findFintransferSumMoney(finFundtransfer));
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> findParamsForStaIncome(Set<Integer> bidRepayIds, Date beginMonth, Date endMonth) {
		logger.info("findParamsForStaIncome, 为收入统计查询相关流水, 还款笔数: {}, 开始时间: {}, 结束时间: {}", bidRepayIds.size(), beginMonth, endMonth);
		Map<String,Object> result = new HashMap<String, Object>();
		//服务费相关还款流水
		List<FinTradeFlow> finTradeFlowList = finTradeFlowDao.findTradeFlowByPflowIds(bidRepayIds,TRADE_TYPE_REPAY);
		if(CommonUtils.isNotEmpty(finTradeFlowList)){
			List<String> serviceFlowIds = new ArrayList<String>();
			finTradeFlowList.forEach(finTradeFlow -> {
				serviceFlowIds.add(finTradeFlow.getFlowId());
			});

			//服务费相关资金划转
			FinFundtransfer cdt = new FinFundtransfer();
			cdt.setTradeType(TRADE_TYPE_REPAY);
			cdt.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.SERVICE_CHARGE));
			cdt.setTradeFlowIds(serviceFlowIds);
			List<FinFundtransfer> finFundtransfers =  this.finFundtransferDao.findByCondition(cdt);
			result.put("serviceChargeFinFundtransferList",finFundtransfers);
		}
		//查询手续费
		FinFundtransfer chargeCdt = new FinFundtransfer();
		chargeCdt.setTradeType(TRADE_TYPE_MAKELOAN);
		chargeCdt.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.CHARGE));
		chargeCdt.setCreateTimeBegin(beginMonth);
		chargeCdt.setCreateTimeEnd(endMonth);
		List<FinFundtransfer> chargeFundtransfers =  this.finFundtransferDao.findByCondition(chargeCdt);
		//罚息资金划转
		FinFundtransfer penaltyCdt = new FinFundtransfer();
		penaltyCdt.setTradeType(TRADE_TYPE_REPAY);
		penaltyCdt.setSubCode(TradeTransferConstants.getFundTransferSubCodeByType(FundtransferBigTypeStateEnum.PAY, FundtransferSmallTypeStateEnum.PENALTY_INTEREST));
		penaltyCdt.setCreateTimeBegin(beginMonth);
		penaltyCdt.setCreateTimeEnd(endMonth);
		List<FinFundtransfer> penaltyFundtransfers =  this.finFundtransferDao.findByCondition(penaltyCdt);

		result.put("serviceChargeTradeFlowList",finTradeFlowList);
		result.put("chargeFundtransferList",chargeFundtransfers);
		result.put("penaltyFundtransferList",penaltyFundtransfers);
		List<FinFundtransfer> finFundtransfers = new ArrayList<FinFundtransfer>();
		finFundtransfers.addAll(chargeFundtransfers);
		finFundtransfers.addAll(penaltyFundtransfers);
		result.put("chargeAndPenFundtransferList",finFundtransfers);
		return result;
	}

	@Override
	public ResponseEntity<?> findInAndOutMoneyByRegUserId(Integer regUserId) {
	    FinFundtransfer cdt = new FinFundtransfer();
	    cdt.setSubCode(10);
	    cdt.setRegUserId(regUserId);
		BigDecimal inMoney = finFundtransferDao.findFintransferSumMoney(cdt);

		cdt.setSubCode(20);
        BigDecimal outMoney = finFundtransferDao.findFintransferSumMoney(cdt);


		FinAccount account = finAccountDao.findByRegUserId(regUserId);
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("inMoney",inMoney);
        params.put("outMoney",outMoney);
        params.put("freezeMoney",account.getFreezeMoney());
		params.put("useableMoney",account.getUseableMoney());
        params.put("isEqual",CompareUtil.eq(inMoney,outMoney.add(account.getNowMoney()))?1:0);
		return new ResponseEntity<>(Constants.SUCCESS,"查询出入账金额成功",params);
	}

	@Override
	public List<Integer> findRegUserIdListYestoday() {
		return this.finFundtransferDao.findRegUserIdListYestoday();
	}
}
