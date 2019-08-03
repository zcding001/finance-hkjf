package com.hongkun.finance.bi.dao;

import com.hongkun.finance.bi.model.StaIncome;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.math.BigDecimal;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.StaIncomeDao.java
 * @Class Name    : StaIncomeDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface StaIncomeDao extends MyBatisBaseDao<StaIncome, Long> {
    /**
    *  @Description    ：根据条件查询某种类型收入
    *  @Method_Name    ：getSumCharge
    *  @param staIncome
    *  @return java.math.BigDecimal
    *  @Creation Date  ：2018/6/6
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    BigDecimal getSumCharge(StaIncome staIncome);

    /**
    *  @Description    ：根据条件查询扣息后总金额
    *  @Method_Name    ：getSumPureMoney
    *  @param staIncome
    *  @return java.math.BigDecimal
    *  @Creation Date  ：2018/6/6
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    BigDecimal getSumPureMoney(StaIncome staIncome);
}
