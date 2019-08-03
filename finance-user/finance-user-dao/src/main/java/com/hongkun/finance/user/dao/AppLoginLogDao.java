package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.AppLoginLog;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.AppLoginLogDao.java
 * @Class Name    : AppLoginLogDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface AppLoginLogDao extends MyBatisBaseDao<AppLoginLog, Long> {

    /**
     * 根据UserId来更新AppLoginLog
     * @param unhandLog
     */
    void updateAppLoginLogByUserId(AppLoginLog unhandLog);
}
