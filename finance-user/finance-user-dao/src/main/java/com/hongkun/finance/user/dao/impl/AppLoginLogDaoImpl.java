package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.AppLoginLogDao;
import com.hongkun.finance.user.model.AppLoginLog;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.AppLoginLogDaoImpl.java
 * @Class Name    : AppLoginLogDaoImpl.java
 * @Description : GENERATOR DAO实现类
 * @Author : generator
 */
@Dao
public class AppLoginLogDaoImpl extends MyBatisBaseDaoImpl<AppLoginLog, Long> implements AppLoginLogDao {

    /**
     * 根据regUserid来更新AppLogin
     */
    private static final String UPDATE_APP_LOGIN_LOG_BY_USERID = AppLoginLog.class.getName() + ".updateAppLoginLogByUserId";

    @Override
    public void updateAppLoginLogByUserId(AppLoginLog unhandLog) {
        getCurSqlSessionTemplate()
                .update(UPDATE_APP_LOGIN_LOG_BY_USERID, unhandLog);
    }
}
