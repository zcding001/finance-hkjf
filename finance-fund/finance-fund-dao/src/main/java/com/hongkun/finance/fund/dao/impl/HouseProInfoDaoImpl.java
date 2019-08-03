package com.hongkun.finance.fund.dao.impl;

import com.hongkun.finance.fund.model.HouseProInfo;
import com.hongkun.finance.fund.dao.HouseProInfoDao;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.impl.HouseProInfoDaoImpl.java
 * @Class Name    : HouseProInfoDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class HouseProInfoDaoImpl extends MyBatisBaseDaoImpl<HouseProInfo, Long> implements HouseProInfoDao {

    private static final String FIND_HOUSEINFO_AND_DETAIL_BY_ID = ".findHouseInfoAndDetailById";

    @Override
    public HouseInfoAndDetail findHouseInfoAndDetailById(Integer infoId) {
        return super.getCurSqlSessionTemplate().selectOne(HouseProInfo.class.getName()+FIND_HOUSEINFO_AND_DETAIL_BY_ID,infoId);
    }
}
