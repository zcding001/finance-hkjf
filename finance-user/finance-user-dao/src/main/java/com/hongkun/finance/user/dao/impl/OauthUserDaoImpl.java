package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.model.OauthUser;
import com.hongkun.finance.user.dao.OauthUserDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.OauthUserDaoImpl.java
 * @Class Name    : OauthUserDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class OauthUserDaoImpl extends MyBatisBaseDaoImpl<OauthUser, java.lang.Long> implements OauthUserDao {

    @Override
    public OauthUser findOauthUserByUserName(String username) {
        return getCurSqlSessionTemplate().selectOne(OauthUser.class.getName() + ".getByUsername", username);
    }
}
