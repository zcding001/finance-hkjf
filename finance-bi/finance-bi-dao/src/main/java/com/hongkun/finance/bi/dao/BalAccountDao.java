package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.BalAccount;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.BalAccountDao.java
 * @Class Name    : BalAccountDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BalAccountDao extends MyBatisBaseDao<BalAccount, Long> {
    /**
    *  @Description    ：查询用户对账信息
    *  @Method_Name    ：findBalAccountByRegUserId
    *  @param regUserId
    *  @return com.hongkun.finance.bi.model.BalAccount
    *  @Creation Date  ：2018/5/2
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    BalAccount findBalAccountByRegUserId(Integer regUserId);
}
