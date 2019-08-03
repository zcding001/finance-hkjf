package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.model.SysAppVersionRule;
import com.hongkun.finance.vas.dao.SysAppVersionRuleDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.impl.SysAppVersionRuleDaoImpl.java
 * @Class Name    : SysAppVersionRuleDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SysAppVersionRuleDaoImpl extends MyBatisBaseDaoImpl<SysAppVersionRule, Long> implements SysAppVersionRuleDao {

    @Override
    public SysAppVersionRule findRule(SysAppVersionRule condition) {
        return this.getCurSqlSessionTemplate().selectOne(SysAppVersionRule.class.getName() + ".findRule",condition);
    }
}
