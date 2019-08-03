package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.model.VasRebatesRuleChild;

import java.util.List;

import com.hongkun.finance.vas.dao.VasRebatesRuleChildDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name :
 *          com.hongkun.finance.vas.dao.impl.VasRebatesRuleChildDaoImpl.java
 * @Class Name : VasRebatesRuleChildDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class VasRebatesRuleChildDaoImpl extends MyBatisBaseDaoImpl<VasRebatesRuleChild, java.lang.Long>
		implements VasRebatesRuleChildDao {
	public String FIND_VAS_REBATES_RULE_CHILD_BY_RULEID = ".findVasRebatesRuleChildByRuleId";

	public String FIND_RULE_CHILD_BY_USERTYPE_RULEID = ".findRuleChildByUserTypeAndRuleId";
	public String UPDATE_RULE_CHILD_BY_USERTYPE_RULEID = ".updateRuleChildByUserTypeAndRuleId";

	@Override
	public List<VasRebatesRuleChild> findVasRebatesRuleChildByRuleId(int vasRebatesRuleId) {
		return getCurSqlSessionTemplate().selectList(
				VasRebatesRuleChild.class.getName() + FIND_VAS_REBATES_RULE_CHILD_BY_RULEID, vasRebatesRuleId);
	}

	@Override
	public VasRebatesRuleChild findRuleChildByUserTypeAndRuleId(Integer userType, int vasRebatesRuleId) {
		VasRebatesRuleChild ruleChild = new VasRebatesRuleChild();
		ruleChild.setUserType(userType);
		ruleChild.setVasRebatesRuleId(vasRebatesRuleId);
		return getCurSqlSessionTemplate()
				.selectOne(VasRebatesRuleChild.class.getName() + FIND_RULE_CHILD_BY_USERTYPE_RULEID, ruleChild);
	}

	@Override
	public int updateRuleChildByUserTypeAndRuleId(Integer userType, int vasRebatesRuleId) {
		VasRebatesRuleChild ruleChild = new VasRebatesRuleChild();
		ruleChild.setUserType(userType);
		ruleChild.setVasRebatesRuleId(vasRebatesRuleId);
		return getCurSqlSessionTemplate()
				.update(VasRebatesRuleChild.class.getName() + UPDATE_RULE_CHILD_BY_USERTYPE_RULEID, ruleChild);
	}

}
