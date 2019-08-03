package com.hongkun.finance.user.dao;

import com.hongkun.finance.user.model.RegCompanyInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.RegCompanyInfoDao.java
 * @Class Name    : RegCompanyInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface RegCompanyInfoDao extends MyBatisBaseDao<RegCompanyInfo, java.lang.Long> {

    /**
    *  @Description    ：查询用户注册的企业
    *  @Method_Name    ：findRegCompanyInfoByRegUserId
    *  @param regUserId
    *  @return com.hongkun.finance.user.model.RegCompanyInfo
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhichaoding@hongkun.com.cn
    */
    RegCompanyInfo findRegCompanyInfoByRegUserId(Integer regUserId);

    /** 
    * @Description: 通过手机号查询企业账户
    * @Param: [legalTel] 
    * @return: com.hongkun.finance.user.model.RegCompanyInfo 
    * @Author: HeHang
    * @Date: 2018/9/12 
    */
    List<RegCompanyInfo> findRegCompanyInfoByLegalTel(String legalTel);
}
