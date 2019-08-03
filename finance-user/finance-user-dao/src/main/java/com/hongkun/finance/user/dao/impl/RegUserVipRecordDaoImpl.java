package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.model.RegUserVipRecord;
import com.hongkun.finance.user.dao.RegUserVipRecordDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.RegUserVipRecordDaoImpl.java
 * @Class Name    : RegUserVipRecordDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class RegUserVipRecordDaoImpl extends MyBatisBaseDaoImpl<RegUserVipRecord, Long> implements RegUserVipRecordDao {

    @Override
    public List<RegUserVipRecord> findRegUserVipRecordListByRegUserIds(List<Integer> ids) {
        return super.getCurSqlSessionTemplate().selectList(RegUserVipRecord.class.getName() + ".findRegUserVipRecordsList");
    }
}
