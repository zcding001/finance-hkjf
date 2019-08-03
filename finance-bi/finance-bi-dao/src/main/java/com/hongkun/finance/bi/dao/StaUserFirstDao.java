package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaUserFirst;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.dao.StaUserFirstDao.java
 * @Class Name    : StaUserFirstDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaUserFirstDao extends MyBatisBaseDao<StaUserFirst, Long> {


    /**
    *  通过类型统计用户信息
    *  @Method_Name             ：findStaUserFirstByType
    *  @param staUserFirst
    *  @return java.util.List<com.hongkun.finance.bi.model.StaUserFirst>
    *  @Creation Date           ：2018/9/18
    *  @Author                  ：zc.ding@foxmail.com
    */
    List<StaUserFirst> findStaUserFirstByType(StaUserFirst staUserFirst);

    /**
    *  查询统计信息
    *  @Method_Name             ：findStaUserFirstCount
    *  @param staUserFirst
    *  @return com.hongkun.finance.bi.model.StaUserFirst
    *  @Creation Date           ：2018/9/18
    *  @Author                  ：zc.ding@foxmail.com
    */
    StaUserFirst findStaUserFirstCount(StaUserFirst staUserFirst);
}
