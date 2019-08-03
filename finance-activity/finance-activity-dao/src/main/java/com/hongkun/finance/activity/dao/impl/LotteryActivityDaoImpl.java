package com.hongkun.finance.activity.dao.impl;

import com.hongkun.finance.activity.model.LotteryActivity;
import com.hongkun.finance.activity.dao.LotteryActivityDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.impl.LotteryActivityDaoImpl.java
 * @Class Name    : LotteryActivityDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class LotteryActivityDaoImpl extends MyBatisBaseDaoImpl<LotteryActivity, Long> implements LotteryActivityDao {

    private static final String FIND_LOTTERY_ACTIVITY_BY_TEL = ".findLotteryActivityByTel";
    @Override
    public List<LotteryActivity> findLotteryActivityByTel(Long tel) {
        return super.getCurSqlSessionTemplate().selectList(LotteryActivity.class.getName() + FIND_LOTTERY_ACTIVITY_BY_TEL,tel);
    }
}
