package com.hongkun.finance.activity.dao.impl;

import com.hongkun.finance.activity.model.LotteryItem;
import com.hongkun.finance.activity.dao.LotteryItemDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.impl.LotteryItemDaoImpl.java
 * @Class Name    : LotteryItemDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class LotteryItemDaoImpl extends MyBatisBaseDaoImpl<LotteryItem, Long> implements LotteryItemDao {

    private static String GET_LOTTERY_TIEMS_GROUP_BY_ID = ".getLotteryItemsGroupById";
    private static String GET_LOTTERY_TIEMS_LOCATION_FLAG_BY_ID = ".getLotteryItemsLocationFlagById";
    private static String DEL_LOTTERY_ITEMS_BY_GROUP_AND_LOCATION = ".delLotteryItemsByGroupAndLocation";

    @Override
    public List<Integer> getLotteryItemsGroupByActivityId(Integer lotteryActivityId) {
        return super.getCurSqlSessionTemplate().selectList(LotteryItem.class.getName() + GET_LOTTERY_TIEMS_GROUP_BY_ID,lotteryActivityId);
    }

    @Override
    public List<Integer> getLotteryItemsLocationFlagActivityId(Integer lotteryActivityId) {
        return super.getCurSqlSessionTemplate().selectList(LotteryItem.class.getName() + GET_LOTTERY_TIEMS_LOCATION_FLAG_BY_ID,lotteryActivityId);
    }

    @Override
    public int deleteLotteryItemsByGroupAndLocationFlag(Map<String, Object> params) {
        return super.getCurSqlSessionTemplate().update(LotteryItem.class.getName()+DEL_LOTTERY_ITEMS_BY_GROUP_AND_LOCATION,params);
    }
}
