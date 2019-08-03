package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.model.VasGoldRule;
import com.hongkun.finance.vas.dao.VasGoldRuleDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.dao.impl.VasGoldRuleDaoImpl.java
 * @Class Name : VasGoldRuleDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasGoldRuleDaoImpl extends MyBatisBaseDaoImpl<VasGoldRule, java.lang.Long> implements VasGoldRuleDao {

	@Override
	public VasGoldRule findVasGoldRuleByTypeAndState(int type, int state) {
		VasGoldRule vasGoldRule = new VasGoldRule();
		vasGoldRule.setState(state);
		vasGoldRule.setType(type);
		return getCurSqlSessionTemplate().selectOne(VasGoldRule.class.getName() + ".findPage", vasGoldRule);
	}

}
