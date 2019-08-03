package com.hongkun.finance.fund.dao;

import com.hongkun.finance.fund.model.HouseProInfo;
import com.hongkun.finance.fund.model.vo.HouseInfoAndDetail;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.HouseProInfoDao.java
 * @Class Name    : HouseProInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface HouseProInfoDao extends MyBatisBaseDao<HouseProInfo, Long> {

    HouseInfoAndDetail findHouseInfoAndDetailById(Integer infoId);
}
