package com.hongkun.finance.payment.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yirun.framework.redis.JedisClusterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinBankReferDao;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.enums.PayStyleEnum;
import com.hongkun.finance.payment.model.FinBankRefer;
import com.hongkun.finance.payment.service.FinBankReferService;
import com.yirun.framework.core.commons.Constants;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.pager.Pager;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinBankReferServiceImpl.java
 * @Class Name : FinBankReferServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinBankReferServiceImpl implements FinBankReferService {

	private static final Logger logger = LoggerFactory.getLogger(FinBankReferServiceImpl.class);

	/**
	 * FinBankReferDAO
	 */
	@Autowired
	private FinBankReferDao finBankReferDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insertFinBankRefer(FinBankRefer finBankRefer) {
		return this.finBankReferDao.save(finBankRefer);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinBankReferBatch(List<FinBankRefer> list) {
		this.finBankReferDao.insertBatch(FinBankRefer.class, list);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertFinBankReferBatch(List<FinBankRefer> list, int count) {
		if (logger.isDebugEnabled()) {
			logger.debug("default batch insert size is " + count);
		}
		this.finBankReferDao.insertBatch(FinBankRefer.class, list, count);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public ResponseEntity<?> updateFinBankRefer(FinBankRefer finBankRefer) {
		logger.info("方法: updateFinBankRefer, 修改银行信息, 入参: finBankRefer: {}", finBankRefer.toString());
		try {
			this.finBankReferDao.update(finBankRefer);
			FinBankRefer bankRefer = this.finBankReferDao.findByPK(Long.parseLong(finBankRefer.getId().toString()),
					FinBankRefer.class);
			Arrays.asList(bankRefer.getPaywayCodes().split(",")).forEach(payStyle -> {
				Arrays.asList(bankRefer.getRegUserType().split(",")).forEach(userType -> {
					JedisClusterUtils.setAsJson(bankRefer.getThirdCode() + payStyle + userType + bankRefer.getBankCode()
							+ bankRefer.getState(), bankRefer);
				});
			});
		} catch (Exception e) {
			logger.error("修改银行信息失败: ", e);
			throw new GeneralException("修改失败");
		}
		return new ResponseEntity<>(Constants.SUCCESS, "修改成功");
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinBankReferBatch(List<FinBankRefer> list, int count) {
		this.finBankReferDao.updateBatch(FinBankRefer.class, list, count);
	}

	@Override
	public FinBankRefer findFinBankReferById(int id) {
		return this.finBankReferDao.findByPK(Long.valueOf(id), FinBankRefer.class);
	}

	@Override
	public List<FinBankRefer> findFinBankReferList(FinBankRefer finBankRefer) {
		return this.finBankReferDao.findByCondition(finBankRefer);
	}

	@Override
	public List<FinBankRefer> findFinBankReferList(FinBankRefer finBankRefer, int start, int limit) {
		return this.finBankReferDao.findByCondition(finBankRefer, start, limit);
	}

	@Override
	public Pager findFinBankReferList(FinBankRefer finBankRefer, Pager pager) {
		return this.finBankReferDao.findByCondition(finBankRefer, pager);
	}

	@Override
	public int findFinBankReferCount(FinBankRefer finBankRefer) {
		return this.finBankReferDao.getTotalCount(finBankRefer);
	}

	@Override
	public List<FinBankRefer> findBankInfo(String payChannel, String payStyleCode, String userType) {
		Integer state = TradeStateConstants.START_USING_STATE;
		String key = payChannel + payStyleCode + userType + state;
		return this.finBankReferDao.findBankInfo(key, payChannel, payStyleCode, userType, state);
	}

	@Override
	public FinBankRefer findBankRefer(PayChannelEnum payChannelEnum, String bankCode, PayStyleEnum payStyleEnum,
			Integer userType) {
		Integer state = TradeStateConstants.START_USING_STATE;
		String key = payChannelEnum.getChannelKey() + payStyleEnum + String.valueOf(userType) + bankCode + state;
		FinBankRefer bankRefer = this.finBankReferDao.findBankRefer(key, payChannelEnum.getChannelKey(),
				payStyleEnum.getType(), String.valueOf(userType), bankCode, state, null);
		return bankRefer;
	}

	@Override
	public ResponseEntity<?> findBankCondition() {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Constants.SUCCESS);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, List<FinBankRefer>> bankMap = new HashMap<String, List<FinBankRefer>>();
		// 按照支付渠道分组，查询有哪些支付渠道
		FinBankRefer finBankRefer = new FinBankRefer();
		finBankRefer.setGroupColumns("third_code");
		List<FinBankRefer> bankReferList = this.finBankReferDao.findByCondition(finBankRefer);
		// 查询每个交易渠道下包含哪些银行
		for (FinBankRefer bankRefer : bankReferList) {
			FinBankRefer finBankRefers = new FinBankRefer();
			finBankRefers.setThirdCode(bankRefer.getThirdCode());
			finBankRefers.setState(TradeStateConstants.START_USING_STATE);
			List<FinBankRefer> bankList = this.finBankReferDao.findByCondition(finBankRefers);
			bankMap.put(bankRefer.getThirdCode(), bankList);
		}
		resultMap.put("payChannelList", bankReferList);
		resultMap.put("bankMap", bankMap);
		responseEntity.setParams(resultMap);
		return responseEntity;
	}
}
