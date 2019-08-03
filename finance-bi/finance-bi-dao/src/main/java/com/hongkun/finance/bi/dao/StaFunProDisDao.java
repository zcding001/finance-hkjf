package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaFunProDis;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.yirun.finance.bi.dao.StaFunProDisDao.java
 * @Class Name    : StaFunProDisDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaFunProDisDao extends MyBatisBaseDao<StaFunProDis, Long> {
	
    /**
    *  按产品类型进行分组查询
    *  @Method_Name             ：findStaFunProDisListByBidProType
    *  @param staFunProDis
    *  @return java.util.List<com.hongkun.finance.bi.model.StaFunProDis>
    *  @Creation Date           ：2018/9/19
    *  @Author                  ：zc.ding@foxmail.com
    */
    List<StaFunProDis> findStaFunProDisListByBidProType(StaFunProDis staFunProDis);
}
