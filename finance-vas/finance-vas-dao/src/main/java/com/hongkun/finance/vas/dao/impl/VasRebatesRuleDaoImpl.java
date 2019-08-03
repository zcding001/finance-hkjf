package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.dao.VasRebatesRuleDao;
import com.hongkun.finance.vas.model.VasRebatesRule;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;
import com.yirun.framework.redis.annotation.Cache;
import com.yirun.framework.redis.constants.CacheOperType;

import java.util.List;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.impl.VasRebatesRuleDaoImpl.java
 * @Class Name : VasRebatesRuleDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasRebatesRuleDaoImpl extends MyBatisBaseDaoImpl<VasRebatesRule, java.lang.Long>
		implements VasRebatesRuleDao {
	@Cache(key = "#0", prefix = "findVasRebatesRule_VAS_", operType = CacheOperType.READ_WRITE,expireTime = 0)
	@Override
	public VasRebatesRule findVasRebatesRule(String key, Integer state, Integer type) {
		VasRebatesRule vasRule = new VasRebatesRule();
		vasRule.setState(state);
		vasRule.setType(type);
		return getCurSqlSessionTemplate().selectOne(VasRebatesRule.class.getName() + ".findPage", vasRule);
	}
}
