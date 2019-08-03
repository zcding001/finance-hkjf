package com.hongkun.finance.contract.dao.impl;

import com.hongkun.finance.contract.model.CarInfo;
import com.hongkun.finance.contract.dao.CarInfoDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.dao.impl.CarInfoDaoImpl.java
 * @Class Name    : CarInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class CarInfoDaoImpl extends MyBatisBaseDaoImpl<CarInfo, Long> implements CarInfoDao {

    @Override
    public int deleteById(Integer carInfoId){
        return this.delete(carInfoId.longValue(), CarInfo.class);
    }
}
