package com.hongkun.finance.activity.dao;

import com.hongkun.finance.activity.model.LotteryItem;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.LotteryItemDao.java
 * @Class Name    : LotteryItemDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface LotteryItemDao extends MyBatisBaseDao<LotteryItem, Long> {

    List<Integer> getLotteryItemsGroupByActivityId(Integer lotteryActivityId);


    List<Integer> getLotteryItemsLocationFlagActivityId(Integer lotteryActivityId);

    int deleteLotteryItemsByGroupAndLocationFlag(Map<String, Object> params);
}
