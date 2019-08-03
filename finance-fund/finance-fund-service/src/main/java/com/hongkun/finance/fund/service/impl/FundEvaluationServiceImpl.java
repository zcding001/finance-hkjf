package com.hongkun.finance.fund.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.fund.model.FundEvaluation;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.fund.dao.FundEvaluationDao;
import com.hongkun.finance.fund.service.FundEvaluationService;
import org.springframework.util.CollectionUtils;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.service.impl.FundEvaluationServiceImpl.java
 * @Class Name    : FundEvaluationServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class FundEvaluationServiceImpl implements FundEvaluationService {

	private static final Logger logger = LoggerFactory.getLogger(FundEvaluationServiceImpl.class);
	
	/**
	 * FundEvaluationDAO
	 */
	@Autowired
	private FundEvaluationDao fundEvaluationDao;
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundEvaluation(FundEvaluation fundEvaluation) {
		this.fundEvaluationDao.save(fundEvaluation);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundEvaluationBatch(List<FundEvaluation> list) {
		this.fundEvaluationDao.insertBatch(FundEvaluation.class, list);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFundEvaluationBatch(List<FundEvaluation> list, int count) {
		if(logger.isDebugEnabled()){
			logger.debug("default batch insert size is " + count);
		}
		this.fundEvaluationDao.insertBatch(FundEvaluation.class, list, count);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundEvaluation(FundEvaluation fundEvaluation) {
		this.fundEvaluationDao.update(fundEvaluation);
	}
	
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFundEvaluationBatch(List<FundEvaluation> list, int count) {
		this.fundEvaluationDao.updateBatch(FundEvaluation.class, list, count);
	}
	
	@Override
	public FundEvaluation findFundEvaluationById(int id) {
		return this.fundEvaluationDao.findByPK(Long.valueOf(id), FundEvaluation.class);
	}
	
	@Override
	public List<FundEvaluation> findFundEvaluationList(FundEvaluation fundEvaluation) {
		return this.fundEvaluationDao.findByCondition(fundEvaluation);
	}
	
	@Override
	public List<FundEvaluation> findFundEvaluationList(FundEvaluation fundEvaluation, int start, int limit) {
		return this.fundEvaluationDao.findByCondition(fundEvaluation, start, limit);
	}
	
	@Override
	public Pager findFundEvaluationList(FundEvaluation fundEvaluation, Pager pager) {
		return this.fundEvaluationDao.findByCondition(fundEvaluation, pager);
	}
	
	@Override
	public int findFundEvaluationCount(FundEvaluation fundEvaluation){
		return this.fundEvaluationDao.getTotalCount(fundEvaluation);
	}
	
	@Override
	public Pager findFundEvaluationList(FundEvaluation fundEvaluation, Pager pager, String sqlName){
		return this.fundEvaluationDao.findByCondition(fundEvaluation, pager, sqlName);
	}
	
	@Override
	public Integer findFundEvaluationCount(FundEvaluation fundEvaluation, String sqlName){
		return this.fundEvaluationDao.getTotalCount(fundEvaluation, sqlName);
	}

	@Override
	public ResponseEntity<?> saveRiskEvalution(Integer regUserId, String answers, Integer score) {
		try {
			String resultType = "进取型";
			if (score <= 20) {
				resultType = "保守型";
			} else if (score >= 21 && score <= 40) {
				resultType = "稳健型";
			} else if (score >= 41 && score <= 60) {
				resultType = "平衡型";
			} else if (score >= 61 && score <= 80) {
				resultType = "积极型";
			} else {
				resultType = "进取型";
			}

			Map<String,Object> param = new HashMap<>();
			param.put("resultType",resultType);
			param.put("score",score);
			param.put("passFlag",1);
			if(score < 81){
				param.put("passFlag",0);
				return new ResponseEntity<>(Constants.SUCCESS, "请重新测评",param);
			}
			FundEvaluation fundEvaluation = new FundEvaluation();
			fundEvaluation.setRegUserId(regUserId);
			fundEvaluation.setType(1);
			List<FundEvaluation> fundEvaluationList = this.findFundEvaluationList(fundEvaluation);
			fundEvaluation.setScore(score);
			fundEvaluation.setOptions(answers);
			fundEvaluation.setResultType(resultType);
			fundEvaluation.setCreateTime(new Date());
			// 更新操作
			if(!CollectionUtils.isEmpty(fundEvaluationList)){
				fundEvaluation.setId(fundEvaluationList.get(0).getId());
				fundEvaluationDao.update(fundEvaluation);
				return new ResponseEntity<>(Constants.SUCCESS, "测评成功",param);
			}
			fundEvaluationDao.save(fundEvaluation);
			return new ResponseEntity<>(Constants.SUCCESS, "测评成功",param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存或更新股权测评信息失败: ",  e);
			throw new GeneralException("保存或更新股权测评信息失败!");
		}
	}
}
