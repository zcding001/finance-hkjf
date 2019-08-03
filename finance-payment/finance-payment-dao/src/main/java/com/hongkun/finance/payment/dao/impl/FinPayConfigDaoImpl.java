package com.hongkun.finance.payment.dao.impl;

import com.hongkun.finance.payment.constant.TradeStateConstants;
import com.hongkun.finance.payment.dao.FinPayConfigDao;
import com.hongkun.finance.payment.model.FinPayConfig;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import com.yirun.framework.redis.annotation.Cache;
import com.yirun.framework.redis.constants.CacheOperType;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.impl.FinPayConfigDaoImpl.java
 * @Class Name : FinPayConfigDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class FinPayConfigDaoImpl extends MyBatisBaseDaoImpl<FinPayConfig, java.lang.Long> implements FinPayConfigDao {
	/**
	 * 查询支付配置文件
	 */
	public String FIND_PAY_CONFIG = ".findPage";

	@Cache(key = "#0", prefix = "findPayConfigInfo", operType = CacheOperType.READ_WRITE)
	@Override
	public FinPayConfig findPayConfigInfo(String key, String systemTypeName, String platformSourceName,
			String payChannel, String payStyle) {
		FinPayConfig finPayConfig = new FinPayConfig();
		finPayConfig.setSysNameCode(systemTypeName);
		finPayConfig.setPlatformName(platformSourceName);
		finPayConfig.setPaywayCode(payStyle);
		finPayConfig.setThirdNameCode(payChannel);
		finPayConfig.setState(TradeStateConstants.START_USING_STATE);
		return getCurSqlSessionTemplate().selectOne(FinPayConfig.class.getName() + FIND_PAY_CONFIG, finPayConfig);
	}

}
