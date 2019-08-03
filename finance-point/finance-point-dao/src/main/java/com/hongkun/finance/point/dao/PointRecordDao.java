package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointRecord;
import com.hongkun.finance.point.model.vo.PointVO;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointRecordDao.java
 * @Class Name    : PointRecordDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointRecordDao extends MyBatisBaseDao<PointRecord, Long> {

    Pager listPointRecord(PointVO pointVo, Pager pager);
    
    int deleteByContidion(PointRecord pointRecord);

    /**
     * 用户今天签到的次数
     * @param currentUserId
     * @return
     */
    Long userSignCount(Integer currentUserId);
}
