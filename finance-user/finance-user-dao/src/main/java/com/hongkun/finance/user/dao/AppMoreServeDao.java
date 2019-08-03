package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.AppMoreServe;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.dao.AppMoreServeDao.java
 * @Class Name    : AppMoreServeDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface AppMoreServeDao extends MyBatisBaseDao<AppMoreServe, Long> {
    /**
     * 查询参数中数据以外的菜单
     * @param appMoreServeIds
     * @return
     */
    List<AppMoreServe> findOtherServe(List<Integer> appMoreServeIds);
}
