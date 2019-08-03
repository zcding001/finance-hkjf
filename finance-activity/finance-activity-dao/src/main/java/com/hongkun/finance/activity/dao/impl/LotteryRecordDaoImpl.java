package com.hongkun.finance.activity.dao.impl;

import com.hongkun.finance.activity.model.LotteryRecord;
import com.hongkun.finance.activity.dao.LotteryRecordDao;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.impl.LotteryRecordDaoImpl.java
 * @Class Name    : LotteryRecordDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class LotteryRecordDaoImpl extends MyBatisBaseDaoImpl<LotteryRecord, Long> implements LotteryRecordDao {

    private final static String GET_RANDOM_LIST = ".getRandomList";
    private final static String GET_DAY_AND_TOTAL_COUNT = ".getDayAndTotalCount";
    private final static String FIND_LOTTERY_REDORD_DETAIL_LIST = ".findLotteryRecordDetailList";
    private final static String FIND_LOTTERY_RECORD_WITH_CONDITION = ".findLotteryRecordWithCondition";

    @Override
    public List<LotteryRecord> getRandomList(LotteryRecord lotteryRecord) {
        return super.getCurSqlSessionTemplate().selectList(LotteryRecord.class.getName() + GET_RANDOM_LIST,lotteryRecord);
    }

    @Override
    public int getDayAndTotalCount(LotteryRecord record) {
        return super.getCurSqlSessionTemplate().selectOne(LotteryRecord.class.getName() + GET_DAY_AND_TOTAL_COUNT,record);
    }

    @Override
    public List<LotteryRecordVo> findLotteryRecordDetailList(LotteryRecord lotteryRecord) {
        return super.getCurSqlSessionTemplate().selectList(LotteryRecord.class.getName() + FIND_LOTTERY_REDORD_DETAIL_LIST,lotteryRecord);
    }

    @Override
    public Pager findLotteryRecordWithCondition(LotteryRecordVo vo, Pager pager) {
        return super.findByCondition(vo,pager,LotteryRecord.class,FIND_LOTTERY_RECORD_WITH_CONDITION);
    }


}
