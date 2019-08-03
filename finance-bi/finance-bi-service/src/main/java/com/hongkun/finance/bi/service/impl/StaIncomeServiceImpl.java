package com.hongkun.finance.bi.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hongkun.finance.bi.service.StaIncomeService;
import com.yirun.framework.core.utils.CommonUtils;
import com.yirun.framework.core.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaIncome;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaIncomeDao;

import javax.servlet.http.HttpServletResponse;

import static com.hongkun.finance.bi.constants.StaIncomeConstants.STA_INCOME_TRADE_TYPE_LATE_CHARGE;
import static com.hongkun.finance.bi.constants.StaIncomeConstants.STA_INCOME_TRADE_TYPE_POUNDAGE;
import static com.hongkun.finance.bi.constants.StaIncomeConstants.STA_INCOME_TRADE_TYPE_SERVICE_CHARGE;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaIncomeServiceImpl.java
 * @Class Name    : StaIncomeServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaIncomeServiceImpl implements StaIncomeService {

	private static final Logger logger = LoggerFactory.getLogger(StaIncomeServiceImpl.class);
	
	/**
	 * StaIncomeDAO
	 */
	@Autowired
	private StaIncomeDao staIncomeDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaIncome(StaIncome staIncome) {
		this.staIncomeDao.save(staIncome);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaIncomeBatch(List<StaIncome> list) {
		this.staIncomeDao.insertBatch(StaIncome.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertStaIncomeBatch(List<StaIncome> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.staIncomeDao.insertBatch(StaIncome.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaIncome(StaIncome staIncome) {
		this.staIncomeDao.update(staIncome);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateStaIncomeBatch(List<StaIncome> list, int count) {
		this.staIncomeDao.updateBatch(StaIncome.class, list, count);
	}
	
	@Override
	public StaIncome findStaIncomeById(int id) {
		return this.staIncomeDao.findByPK(Long.valueOf(id), StaIncome.class);
	}
	
	@Override
	public List<StaIncome> findStaIncomeList(StaIncome staIncome) {
		return this.staIncomeDao.findByCondition(staIncome);
	}
	
	@Override
	public List<StaIncome> findStaIncomeList(StaIncome staIncome, int start, int limit) {
		return this.staIncomeDao.findByCondition(staIncome, start, limit);
	}
	
	@Override
	public Pager findStaIncomeList(StaIncome staIncome, Pager pager) {
		return this.staIncomeDao.findByCondition(staIncome, pager);
	}
	
	@Override
	public int findStaIncomeCount(StaIncome staIncome){
		return this.staIncomeDao.getTotalCount(staIncome);
	}
	
	@Override
	public Pager findStaIncomeList(StaIncome staIncome, Pager pager, String sqlName){
		return this.staIncomeDao.findByCondition(staIncome, pager, sqlName);
	}
	
	@Override
	public Integer findStaIncomeCount(StaIncome staIncome, String sqlName){
		return this.staIncomeDao.getTotalCount(staIncome, sqlName);
	}

	@Override
	public Map<String, Object> findSumStaCharges(StaIncome staIncome) {
		BigDecimal sumIncome = BigDecimal.ZERO;//总收入
		BigDecimal sumPoundage = BigDecimal.ZERO;//1、查询总手续费
		BigDecimal sumServiceCharge = BigDecimal.ZERO;//2、查询总服务费
		BigDecimal sumLateCharge = BigDecimal.ZERO;//3、查询总罚息
		BigDecimal sumPureMoney = BigDecimal.ZERO;//4、服务费扣息后
		int tradeType = staIncome.getTradeType() == null?-999:staIncome.getTradeType().intValue();
		switch(tradeType){
			case STA_INCOME_TRADE_TYPE_POUNDAGE :
				sumPoundage = staIncomeDao.getSumCharge(staIncome);
				break;
			case STA_INCOME_TRADE_TYPE_SERVICE_CHARGE:
				sumServiceCharge= staIncomeDao.getSumCharge(staIncome);
				sumPureMoney = staIncomeDao.getSumPureMoney(staIncome);
				break;
			case STA_INCOME_TRADE_TYPE_LATE_CHARGE:
				sumLateCharge= staIncomeDao.getSumCharge(staIncome);
				break;
			default:
				staIncome.setTradeType(STA_INCOME_TRADE_TYPE_POUNDAGE);
				sumPoundage = staIncomeDao.getSumCharge(staIncome);
				staIncome.setTradeType(STA_INCOME_TRADE_TYPE_SERVICE_CHARGE);
				sumServiceCharge= staIncomeDao.getSumCharge(staIncome);
				sumPureMoney = staIncomeDao.getSumPureMoney(staIncome);
				staIncome.setTradeType(STA_INCOME_TRADE_TYPE_LATE_CHARGE);
				sumLateCharge= staIncomeDao.getSumCharge(staIncome);
				break;
		}
		sumIncome = sumPoundage.add(sumPureMoney).add(sumLateCharge);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sumIncome",sumIncome);
		params.put("sumPoundage",sumPoundage);
		params.put("sumServiceCharge",sumServiceCharge);
		params.put("sumLateCharge",sumLateCharge);
		params.put("sumPureMoney",sumPureMoney);
		return params;
	}

	@Override
	public void exportExcelForStaIncomeList(StaIncome staIncome, HttpServletResponse response, String exportMonth) {
		try {
			List<StaIncome> staIncomeList =  this.staIncomeDao.findByCondition(staIncome);
			if (CommonUtils.isNotEmpty(staIncomeList)){
				String fileName = "收入统计"+exportMonth;
				String sheetName = exportMonth;
				LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
				params.put("flowId","流水id");
				ExcelUtil.exportExcel(fileName,sheetName,staIncomeList,params,65535,response);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("exportExcelForStaIncomeList, 查询参数staIncome: {}, 当前月份: {}, 异常信息:\n", staIncome.toString(), exportMonth,e);
		}
	}
}
