package com.hongkun.finance.payment.dao.impl;

import com.hongkun.finance.payment.model.FinBankCardBinding;
import com.hongkun.finance.payment.dao.FinBankCardBindingDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.payment.dao.impl.FinBankCardBindingDaoImpl.java
 * @Class Name : FinBankCardBindingDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinBankCardBindingDaoImpl extends MyBatisBaseDaoImpl<FinBankCardBinding, java.lang.Long>
		implements FinBankCardBindingDao {
	/**
	 * 查询银行卡绑定信息
	 */
	public String FIND_BANKCARDBINGING_INFO = ".findBankCardBindingInfo";
	public String UPDATE_BANKCARDBINGING_INFO = ".update";
	public String INSERT_BANKCARDBINGING_INFO = ".insert";
	public static String UPDATE_FIN_BANK_CARD_BINDING_BY_CARD_ID = ".updateFinBankCardBindingByCardId";

	@Override
	public FinBankCardBinding findBankCardBinding(Integer bankCardId, Integer regUserId, Integer payChannel) {
		FinBankCardBinding finBankCardBinding = new FinBankCardBinding();
		finBankCardBinding.setFinBankCardId(bankCardId);
		finBankCardBinding.setRegUserId(regUserId);
		finBankCardBinding.setPayChannel(payChannel);
		return (FinBankCardBinding) getCurSqlSessionTemplate()
				.selectOne(FinBankCardBinding.class.getName() + FIND_BANKCARDBINGING_INFO, finBankCardBinding);
	}

	@Override
	public Integer updateFinBankCardBinding(FinBankCardBinding finBankCardBinding) {
		return getCurSqlSessionTemplate().update(FinBankCardBinding.class.getName() + UPDATE_BANKCARDBINGING_INFO, finBankCardBinding);
	}

	@Override
	public void insertFinBankCardBinding(FinBankCardBinding finBankCardBinding) {
		getCurSqlSessionTemplate().insert(FinBankCardBinding.class.getName() + INSERT_BANKCARDBINGING_INFO, finBankCardBinding);
	}

    @Override
    public int updateFinBankCardBindingByCardId(Integer bankCardId, Integer state) {
		Map<String,Object> params = new HashMap<>();
		params.put("bankCardId",bankCardId);
		params.put("state",state);
		return getCurSqlSessionTemplate().update(FinBankCardBinding.class.getName() + UPDATE_FIN_BANK_CARD_BINDING_BY_CARD_ID, params);
    }

}
