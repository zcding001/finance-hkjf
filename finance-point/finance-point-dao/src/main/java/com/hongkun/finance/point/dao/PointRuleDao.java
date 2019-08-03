package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointRule;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointRuleDao.java
 * @Class Name    : PointRuleDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointRuleDao extends MyBatisBaseDao<PointRule, Long> {

    /**
     * 获取当前启动的积分规则
     * @return
     */
    PointRule getCurrentOnUseRule();

}
