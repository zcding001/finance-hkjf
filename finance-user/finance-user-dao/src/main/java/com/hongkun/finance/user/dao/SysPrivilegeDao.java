package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.SysPrivilege;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.SysPrivilegeDao.java
 * @Class Name    : SysPrivilegeDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SysPrivilegeDao extends MyBatisBaseDao<SysPrivilege, java.lang.Long> {


    /**
    *  @Description    ：根据指定的权限id找到对应的url
    *  @Method_Name    ：finAuthUrlsByIds
    *  @param authIds
    *  @return java.util.List<java.lang.String>
    *  @Creation Date  ：2018/6/11
    *  @Author         ：zhongpingtang@hongkun.com.cn
    */
    List<String> finAuthUrlsByIds(List<String> authIds);
}
