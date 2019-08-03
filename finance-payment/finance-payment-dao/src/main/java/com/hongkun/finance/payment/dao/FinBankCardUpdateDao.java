package com.hongkun.finance.payment.dao;

import com.hongkun.finance.payment.model.FinBankCardUpdate;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.dao.FinBankCardUpdateDao.java
 * @Class Name    : FinBankCardUpdateDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface FinBankCardUpdateDao extends MyBatisBaseDao<FinBankCardUpdate, Long> {

    /**
     *  @Description    ：条件检索股权类型信息列表
     *  @Method_Name    ：findBankCardUpdateList
     *  @param finBankCardUpdate
     *  @param pager
     *  @return com.yirun.framework.core.utils.pager
     *  @Creation Date  ：2018年05月16日 16:15
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    Pager findBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager);

    /**
     *  @Description    ：根据用户Id及银行卡Id查询解绑信息
     *  @Method_Name    ：findFinBankCardUpdateByBankIdAndUserId
     *  @param userId
     *  @param bankCardId
     *  @return com.hongkun.finance.payment.model.FinBankCardUpdate
     *  @Creation Date  ：2018年05月21日 09:32
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    List<FinBankCardUpdate> findFinBankCardUpdateByBankIdAndUserId(Integer userId, Integer bankCardId);
}
