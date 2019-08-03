package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegUserVipRecord;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegUserVipRecordDao.java
 * @Class Name    : RegUserVipRecordDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegUserVipRecordDao extends MyBatisBaseDao<RegUserVipRecord, Long> {

    /**
    *  查询VIP最新的记录
    *  @Method_Name             ：findRegUserVipRecordListByRegUserIds
    *  @param ids
    *  @return java.util.List<com.hongkun.finance.user.model.RegUserVipRecord>
    *  @Creation Date           ：2018/6/7
    *  @Author                  ：zhichaoding@hongkun.com.cn
    */
    List<RegUserVipRecord> findRegUserVipRecordListByRegUserIds(List<Integer> ids);
	
}
