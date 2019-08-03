package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.SysAppVersionRule;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.SysAppVersionRuleDao.java
 * @Class Name    : SysAppVersionRuleDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SysAppVersionRuleDao extends MyBatisBaseDao<SysAppVersionRule, Long> {

    /**
     *  @Description    ：获取app更新规则
     *  @Method_Name    ：findRule
     *  @param condition  获取条件
     *  @return com.hongkun.finance.vas.model.SysAppVersionRule
     *  @Creation Date  ：2018/5/28
     *  @Author         ：pengwu@hongkun.com.cn
     */
    SysAppVersionRule findRule(SysAppVersionRule condition);
}
