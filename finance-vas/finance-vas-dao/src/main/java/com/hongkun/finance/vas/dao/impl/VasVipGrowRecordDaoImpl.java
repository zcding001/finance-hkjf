package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.model.VasVipGrowRecord;
import com.hongkun.finance.vas.dao.VasVipGrowRecordDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.impl.VasVipGrowRecordDaoImpl.java
 * @Class Name    : VasVipGrowRecordDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class VasVipGrowRecordDaoImpl extends MyBatisBaseDaoImpl<VasVipGrowRecord, Long> implements VasVipGrowRecordDao {

    @Override
    public int findUserCurrentGrowValue(int regUserId) {
        return this.getCurSqlSessionTemplate().selectOne(VasVipGrowRecord.class.getName() +
                ".findUserCurrentGrowValue", regUserId);
    }

    @Override
    public Map<Integer, VasVipGrowRecord> findUserGrowValueMap(List<Integer> userIdList) {
        return this.getCurSqlSessionTemplate().selectMap(VasVipGrowRecord.class.getName() +
                ".findUserGrowValueMap",userIdList,"regUserId");
    }

    @Override
    public Map<Integer, VasVipGrowRecord> findUserLevelGtZeroMap() {
        return this.getCurSqlSessionTemplate().selectMap(VasVipGrowRecord.class.getName() + ".findUserLevelGtZero", "regUserId");
    }

    @Override
    public List<Integer> findUserThreeMonthHasDown(Set<Integer> userIdList) {
        return this.getCurSqlSessionTemplate().selectList(VasVipGrowRecord.class.getName() +
                ".findUserThreeMonthHasDown", userIdList);
    }

}
