package com.hongkun.finance.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.payment.dao.FinBankCardDao;
import com.hongkun.finance.payment.enums.PayChannelEnum;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.hongkun.finance.payment.service.FinBankCardService;
import com.yirun.framework.core.exception.GeneralException;
import com.yirun.framework.core.utils.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.service.impl.FinBankCardServiceImpl.java
 * @Class Name : FinBankCardServiceImpl.java
 * @Description : GENERATOR SERVICE实现类
 * @Author : generator
 */
@Service
public class FinBankCardServiceImpl implements FinBankCardService {
	private static final Logger logger = LoggerFactory.getLogger(FinBankCardServiceImpl.class);

	final int BATCH_SIZE = 50;

	/**
	 * FinBankCardDAO
	 */
	@Autowired
	private FinBankCardDao finBankCardDao;

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int insert(FinBankCard finBankCard) {
		return this.finBankCardDao.save(finBankCard);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public void insertBatch(List<FinBankCard> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i += BATCH_SIZE) {
				if (list.size() - i >= BATCH_SIZE) {
					this.finBankCardDao.insertBatch(FinBankCard.class, list.subList(i, i + BATCH_SIZE), BATCH_SIZE);
				} else {
					this.finBankCardDao.insertBatch(FinBankCard.class, list.subList(i, list.size()), list.size() - i);
				}
			}
		}
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int update(FinBankCard finBankCard) {
		logger.info("方法: update, 更新银行信息, 入参: finBankCard: {}", finBankCard.toString());
		try {
			return this.finBankCardDao.update(finBankCard);
		} catch (Exception e) {
			logger.error("更新银行信息, 更新失败: ", e);
			throw new GeneralException("更新银行信息失败！");
		}
	}

	@Override
	public FinBankCard findById(int id) {
		return this.finBankCardDao.findByPK(new Long(id), FinBankCard.class);
	}

	@Override
	public List<FinBankCard> findByCondition(FinBankCard finBankCard) {
		return this.finBankCardDao.findByCondition(finBankCard);
	}

	@Override
	public List<FinBankCard> findByCondition(FinBankCard finBankCard, int start, int limit) {
		return this.finBankCardDao.findByCondition(finBankCard, start, limit);
	}

	@Override
	public Pager findByCondition(FinBankCard finBankCard, Pager pager) {
		return this.finBankCardDao.findByCondition(finBankCard, pager);
	}

	@Override
	public List<FinBankCard> findByRegUserId(Integer regUserId) {
		return finBankCardDao.findByRegUserId(regUserId);
	}

	@Override
	public BankCardVo findBankCardInfo(Integer regUserId, Integer finBankCardId, PayChannelEnum payChannelEnum) {
		BankCardVo bankCardVo = new BankCardVo();
		bankCardVo.setRegUserId(regUserId);
		bankCardVo.setFinBankCardId(finBankCardId);
		bankCardVo.setPayChannel(payChannelEnum.getChannelNameValue());
		List<BankCardVo> bankCardVoList = finBankCardDao.findBankCardInfo(bankCardVo);
		if (bankCardVoList != null && bankCardVoList.size() > 0) {
			return bankCardVoList.get(0);
		}
		return null;
	}

	@Override
	public List<BankCardVo> findBankCardInfoList(BankCardVo bankCardVo) {
		return finBankCardDao.findBankCardInfo(bankCardVo);
	}

	@Override
	public Map<Integer, FinBankCard> findBankCardInfoListByUserIds(Set<Integer> payeeIdSet) {
		return this.finBankCardDao.findBankCardInfoListByUserIds(payeeIdSet);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
	public int updateForUnBinding(FinBankCard bankCard) {
		logger.info("方法: updateForUnBinding, 更新银行信息, 入参: bankCard: {}", bankCard.toString());
		try {
			return this.finBankCardDao.updateForUnBinding(bankCard);
		} catch (Exception e) {
			logger.error("更新银行信息, 更新失败: ", e);
			throw new GeneralException("更新银行信息失败！");
		}
	}
}
