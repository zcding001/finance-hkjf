package com.hongkun.finance.contract.dao;

import com.hongkun.finance.contract.model.CarInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.dao.CarInfoDao.java
 * @Class Name    : CarInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface CarInfoDao extends MyBatisBaseDao<CarInfo, Long> {

    /**
     * @Description :根据汽车ID，删除汽车信息
     * @Method_Name : deleteById;
     * @param carInfoId
     * @return
     * @return : int;
     * @Creation Date : 2018年8月6日 下午4:43:45;
     * @Author : hanghe@hongkun.com.cn 何杭;
     */
    int deleteById(Integer carInfoId);
}
