package com.hongkun.finance.payment.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hongkun.finance.payment.dao.FinBankReferDao;
import com.hongkun.finance.payment.model.FinBankRefer;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import com.yirun.framework.redis.annotation.Cache;
import com.yirun.framework.redis.constants.CacheOperType;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinBankReferDaoImpl.java
 * @Class Name : FinBankReferDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinBankReferDaoImpl extends MyBatisBaseDaoImpl<FinBankRefer, java.lang.Long> implements FinBankReferDao {
	/**
	 * 查询各渠道银行卡公共信息 mapper id
	 */
	public String FIND_BANK_INFO = ".findBankInfo";
	/**
	 * 根据平台银行CODE,或第三方银行CODE，查询银行信息
	 */
	public String FIND_BANK_REFER = ".findBankRefer";

	@Cache(key = "#0", prefix = "findBankInfo", operType = CacheOperType.READ_WRITE)
	@Override
	public List<FinBankRefer> findBankInfo(String key, String thirdCode, String payWayCode, String regUserType,
			Integer state) {
		FinBankRefer finBankRefer = new FinBankRefer();
		finBankRefer.setThirdCode(thirdCode);
		finBankRefer.setPaywayCodes(payWayCode);
		finBankRefer.setRegUserType(regUserType);
		finBankRefer.setState(state);
		return getCurSqlSessionTemplate().selectList(FinBankRefer.class.getName() + FIND_BANK_INFO, finBankRefer);
	}

	@Cache(key = "#0", prefix = "findBankRefer", operType = CacheOperType.READ_WRITE)
	@Override
	public FinBankRefer findBankRefer(String key, String thirdCode, String payWayCode, String regUserType,
			String bankCode, Integer state, String thirdBankCode) {
		FinBankRefer finBankRefer = new FinBankRefer();
		finBankRefer.setThirdCode(thirdCode);
		finBankRefer.setPaywayCodes(payWayCode);
		if (StringUtils.isNotBlank(thirdBankCode)) {
			finBankRefer.setBankThirdCode(thirdBankCode);
		}
		if (StringUtils.isNotBlank(bankCode)) {
			finBankRefer.setBankCode(bankCode);
		}
		finBankRefer.setRegUserType(regUserType);
		finBankRefer.setState(state);
		return getCurSqlSessionTemplate().selectOne(FinBankRefer.class.getName() + FIND_BANK_REFER, finBankRefer);
	}

}
