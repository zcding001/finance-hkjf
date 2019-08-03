package com.hongkun.finance.activity.dao;

import com.hongkun.finance.activity.model.LotteryActivity;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.LotteryActivityDao.java
 * @Class Name    : LotteryActivityDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface LotteryActivityDao extends MyBatisBaseDao<LotteryActivity, Long> {


    List<LotteryActivity> findLotteryActivityByTel(Long login);
}
