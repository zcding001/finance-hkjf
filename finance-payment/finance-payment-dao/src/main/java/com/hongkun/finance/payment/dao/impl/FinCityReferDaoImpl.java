package com.hongkun.finance.payment.dao.impl;

import com.hongkun.finance.payment.model.FinCityRefer;
import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinCityReferDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinCityReferDaoImpl.java
 * @Class Name : FinCityReferDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinCityReferDaoImpl extends MyBatisBaseDaoImpl<FinCityRefer, java.lang.Long> implements FinCityReferDao {

	public String FIND_CITYREFER_INFO = ".findPage";

	@Override
	public FinCityRefer findFinCityRefer(String provinceCode, String cityCode, String channelCode) {
		FinCityRefer finCityRefer = new FinCityRefer();
		finCityRefer.setThirdCode(channelCode);
		finCityRefer.setProvinceCode(provinceCode);
		finCityRefer.setCityCode(cityCode);
		finCityRefer.setState(TradeStateConstants.START_USING_STATE);
		return (FinCityRefer) getCurSqlSessionTemplate().selectOne(FinCityRefer.class.getName() + FIND_CITYREFER_INFO,
				finCityRefer);
	}

}
