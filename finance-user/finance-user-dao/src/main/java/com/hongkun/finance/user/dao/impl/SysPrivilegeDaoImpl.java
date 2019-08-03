package com.hongkun.finance.user.dao.impl;

import com.hongkun.finance.user.dao.SysPrivilegeDao;
import com.hongkun.finance.user.model.SysPrivilege;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.SysPrivilegeDaoImpl.java
 * @Class Name    : SysPrivilegeDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SysPrivilegeDaoImpl extends MyBatisBaseDaoImpl<SysPrivilege, java.lang.Long> implements SysPrivilegeDao {
    private static final String FINAUTHURLSBYIDS = ".finAuthUrlsByIds";
    @Override
    public List<String> finAuthUrlsByIds(List<String> list) {
        return getCurSqlSessionTemplate().selectList(SysPrivilege.class.getName() + FINAUTHURLSBYIDS, list);
    }

}
