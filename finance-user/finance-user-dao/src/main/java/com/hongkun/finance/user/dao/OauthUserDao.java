package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.OauthUser;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.OauthUserDao.java
 * @Class Name    : OauthUserDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface OauthUserDao extends MyBatisBaseDao<OauthUser, java.lang.Long> {
    /**
     *  @Description    ：通过用户名检索oauthUser
     *  @Method_Name    ：findOauthUserByUserName
     *  @param userName
     *  @return com.hongkun.finance.user.model.OauthUser
     *  @Creation Date  ：2018/4/17
     *  @Author         ：zhichaoding@hongkun.com.cn
     */
    OauthUser findOauthUserByUserName(String userName);
}
