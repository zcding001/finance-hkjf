package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointSignInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointSignInfoDao.java
 * @Class Name    : PointSignInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointSignInfoDao extends MyBatisBaseDao<PointSignInfo, Long> {
	PointSignInfo getByRegUserId(int regUserId);
}
