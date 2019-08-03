package com.hongkun.finance.point.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.point.dao.PointAccountDao;
import com.hongkun.finance.point.dao.PointRuleDao;
import com.hongkun.finance.point.model.PointAccount;
import com.hongkun.finance.point.model.PointRule;
import com.hongkun.finance.point.model.vo.PointVO;
import com.hongkun.finance.point.service.PointAccountService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yirun.framework.core.commons.Constants.ERROR;
import static com.yirun.framework.core.commons.Constants.SUCCESS;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.service.impl.PointAccountServiceImpl.java
 * @Class Name    : PointAccountServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class PointAccountServiceImpl implements PointAccountService {

	private static final Logger logger = LoggerFactory.getLogger(PointAccountServiceImpl.class);
	private static final String FIND_POINTACCOUNT_BY_ACCOUNTVO = ".findPointAccountByVo";

	@Autowired
	private PointAccountDao pointAccountDao;
	@Autowired
	private PointRuleDao pointRuleDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertPointAccount(PointAccount pointAccount) {
		try{
			this.pointAccountDao.save(pointAccount);
		}catch (Exception e){
			logger.error("插入积分账户异常, insertPointAccount, 积分账户信息: {}, 异常信息: ", pointAccount.toString(), e);
			throw new GeneralException("插入积分账户异常！");
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updatePointAccount(PointAccount pointAccount) {
		try {
			return this.pointAccountDao.update(pointAccount);
		}catch (Exception e){
			logger.error("更新积分账户异常, updatePointAccount, 积分账户信息: {}, 异常信息: {}", pointAccount.toString(), e);
			throw new GeneralException("更新积分账户异常！");
		}
	}

	@Override
	public PointAccount findPointAccountById(int id) {
		return this.pointAccountDao.findByPK(Long.valueOf(id), PointAccount.class);
	}
	
	@Override
	public List<PointAccount> findPointAccountList(PointAccount pointAccount) {
		return this.pointAccountDao.findByCondition(pointAccount);
	}
	
	@Override
	public List<PointAccount> findPointAccountList(PointAccount pointAccount, int start, int limit) {
		return this.pointAccountDao.findByCondition(pointAccount, start, limit);
	}
	
	@Override
	public Pager findPointAccountList(PointVO pointVO, Pager pager) {
		return this.pointAccountDao.findByCondition(pointVO, pager,PointAccount.class,FIND_POINTACCOUNT_BY_ACCOUNTVO);
	}
	
	@Override
	public int findPointAccountCount(PointAccount pointAccount){
		return this.pointAccountDao.getTotalCount(pointAccount);
	}

	@Override
	public PointAccount findPointAccountByRegUserId(int regUserId) {
		return this.pointAccountDao.findPointAccountByRegUserId(regUserId);
	}

	@Override
	public void updateByRegUserId(PointAccount pointAccount) {
		try{
			this.pointAccountDao.updateByRegUserId(pointAccount);
		}catch (Exception e){
			logger.error("根据regUserId更新积分账户异常, updateByRegUserId, 积分账户信息: {}, 异常信息: ", pointAccount.toString(), e);
			throw new GeneralException("根据regUserId更新积分账户异常！");
		}
	}

	@Override
	public ResponseEntity getUserPointAndRate(int regUserId) {
		Map<String,Object> result = new HashMap(16);
		PointAccount pointAccount = this.pointAccountDao.findPointAccountByRegUserId(regUserId);
		if (pointAccount == null){
			return new ResponseEntity(ERROR,"积分账户获取异常，请稍后重试！");
		}
		PointRule pointRule = pointRuleDao.getCurrentOnUseRule();
		if (pointRule == null){
			return new ResponseEntity(ERROR,"积分转赠手续费利率获取失败，请联系客服人员！");
		}
		result.put("point",pointAccount.getPoint());
		result.put("rate",pointRule.getPointGivingRate());
		result.put("preMoneyToPoint", pointRule.getPerMoneyToPoint());
		return new ResponseEntity(SUCCESS,result);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updatePointAccountBatch(List<PointAccount> list, int count) {
		this.pointAccountDao.updateBatch(PointAccount.class, list, count);
	}
}
