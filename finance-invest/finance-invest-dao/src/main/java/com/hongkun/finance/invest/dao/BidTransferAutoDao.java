package com.hongkun.finance.invest.dao;

import com.hongkun.finance.invest.model.BidTransferAuto;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.invest.dao.BidTransferAutoDao.java
 * @Class Name    : BidTransferAutoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface BidTransferAutoDao extends MyBatisBaseDao<BidTransferAuto, java.lang.Long> {
    /**
    *  @Description    ：通过newInvestId批量删除转让关系
    *  @Method_Name    ：delByNewInvestIds
    *  @param autoIds
    *  @return void
    *  @Creation Date  ：2018/4/24
    *  @Author         ：xuhui.liu@hongkun.com.cn 刘旭辉
    */
    void delByNewInvestIds(List<Integer> autoIds);
}
