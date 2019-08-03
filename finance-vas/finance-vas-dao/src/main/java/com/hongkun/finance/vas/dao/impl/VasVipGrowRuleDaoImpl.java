package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.VasVipGrowRule;
import com.hongkun.finance.vas.dao.VasVipGrowRuleDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Date;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.impl.VasVipGrowRuleDaoImpl.java
 * @Class Name    : VasVipGrowRuleDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class VasVipGrowRuleDaoImpl extends MyBatisBaseDaoImpl<VasVipGrowRule, Long> implements VasVipGrowRuleDao {

    private static final String FIND_VAS_VIP_GROW_RULE_TIME_COUNT = ".findVasVipGrowRuleTimeCount";

    @Override
    public int findVasVipGrowRuleTimeCount(VasVipGrowRule growRule) {
        return this.getCurSqlSessionTemplate().selectOne(VasVipGrowRule.class.getName() +
                FIND_VAS_VIP_GROW_RULE_TIME_COUNT, growRule);
    }

    @Override
    public VasVipGrowRule getVipGrowRuleByTypeAndRegistTime(int type, Date registTime) {
        VasVipGrowRule vipGrowRule = new VasVipGrowRule();
		vipGrowRule.setType(type);
		vipGrowRule.setRegistBeginTimeEnd(registTime);
		vipGrowRule.setRegistEndTimeBegin(registTime);
        return this.getCurSqlSessionTemplate().selectOne(VasVipGrowRule.class.getName() +
                ".getVipGrowRuleByTypeAndRegistTime", vipGrowRule);
    }
}
