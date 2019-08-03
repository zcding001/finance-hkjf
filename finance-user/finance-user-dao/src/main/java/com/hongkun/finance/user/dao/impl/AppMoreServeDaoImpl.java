package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.AppMoreServeDao;
import com.hongkun.finance.user.model.AppMoreServe;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.user.dao.impl.AppMoreServeDaoImpl.java
 * @Class Name    : AppMoreServeDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class AppMoreServeDaoImpl extends MyBatisBaseDaoImpl<AppMoreServe, Long> implements AppMoreServeDao {
    @Override
    public List<AppMoreServe> findOtherServe(List<Integer> appMoreServeIds){
        AppMoreServe appMoreServe = new AppMoreServe();
        appMoreServe.setServeIds(appMoreServeIds);
        appMoreServe.setIsShow(0);
        appMoreServe.setSortColumns("SEQ");
        return getCurSqlSessionTemplate().selectList(AppMoreServe.class.getName() + ".findOtherServe", appMoreServe);
    }
}
