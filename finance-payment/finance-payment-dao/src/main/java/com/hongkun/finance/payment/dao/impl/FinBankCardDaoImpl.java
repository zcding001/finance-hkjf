package com.hongkun.finance.payment.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hongkun.finance.payment.dao.FinBankCardDao;
import com.hongkun.finance.payment.model.FinBankCard;
import com.hongkun.finance.payment.model.vo.BankCardVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinBankCardDaoImpl.java
 * @Class Name : FinBankCardDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinBankCardDaoImpl extends MyBatisBaseDaoImpl<FinBankCard, java.lang.Long> implements FinBankCardDao {
	public String FIND_BANKINFO_BY_USERID = ".findByUserId";
	/** 卡号查询 */
	public String FIND_BANKINFO_BY_CRADNO = ".findByCradNo";

	public String FIND_BANK_CARD_INFO = ".findBankCardInfo";

	public static final String UPDATE_BANK_CARD_FOR_UNBINDING = ".updateBankCardForUnBinding";

	@Override
	public List<FinBankCard> findByRegUserId(Integer regUserId) {
		return getCurSqlSessionTemplate().selectList(FinBankCard.class.getName() + FIND_BANKINFO_BY_USERID, regUserId);
	}

	@Override
	public FinBankCard findByCradNo(String cradNo, Integer regUserId) {
		FinBankCard finBankCard = new FinBankCard();
		finBankCard.setBankCard(cradNo);
		finBankCard.setRegUserId(regUserId);
		return (FinBankCard) getCurSqlSessionTemplate().selectOne(FinBankCard.class.getName() + FIND_BANKINFO_BY_CRADNO,
				finBankCard);
	}

	@Override
	public List<BankCardVo> findBankCardInfo(BankCardVo bankCardVo) {
		return getCurSqlSessionTemplate().selectList(FinBankCard.class.getName() + FIND_BANK_CARD_INFO, bankCardVo);
	}

	@Override
	public Map<Integer, FinBankCard> findBankCardInfoListByUserIds(Set<Integer> payeeIdSet) {
		return this.getCurSqlSessionTemplate().selectMap(FinBankCard.class.getName() +
				".findBankCardInfoListByUserIds" ,payeeIdSet, "regUserId");
	}

    @Override
    public int updateForUnBinding(FinBankCard bankCard) {
        return this.getCurSqlSessionTemplate().update(FinBankCard.class.getName()+UPDATE_BANK_CARD_FOR_UNBINDING,bankCard);
    }
}
