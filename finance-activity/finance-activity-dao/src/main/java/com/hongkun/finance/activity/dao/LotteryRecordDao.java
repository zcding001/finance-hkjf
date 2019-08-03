package com.hongkun.finance.activity.dao;

import com.hongkun.finance.activity.model.LotteryRecord;
import com.hongkun.finance.activity.model.vo.LotteryRecordVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.activity.dao.LotteryRecordDao.java
 * @Class Name    : LotteryRecordDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface LotteryRecordDao extends MyBatisBaseDao<LotteryRecord, Long> {

    List<LotteryRecord> getRandomList(LotteryRecord lotteryRecord);

    int getDayAndTotalCount(LotteryRecord record);

    List<LotteryRecordVo> findLotteryRecordDetailList(LotteryRecord lotteryRecord);

    Pager findLotteryRecordWithCondition(LotteryRecordVo vo, Pager pager);
}
