package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.dao.PointRuleDao;
import com.hongkun.finance.point.model.PointRule;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointRuleDaoImpl.java
 * @Class Name    : PointRuleDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointRuleDaoImpl extends MyBatisBaseDaoImpl<PointRule, Long> implements PointRuleDao {

    private final String CURRENT_ONUSE_RULE = ".currentOnUseRule";

    @Override
    public PointRule getCurrentOnUseRule() {
        return getCurSqlSessionTemplate()
                .selectOne(PointRule.class.getName() + CURRENT_ONUSE_RULE);

    }
}
