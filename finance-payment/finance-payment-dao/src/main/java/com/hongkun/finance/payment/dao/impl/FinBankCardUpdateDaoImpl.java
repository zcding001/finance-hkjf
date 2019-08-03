package com.hongkun.finance.payment.dao.impl;

import com.hongkun.finance.payment.model.FinBankCardUpdate;
import com.hongkun.finance.payment.dao.FinBankCardUpdateDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.payment.dao.impl.FinBankCardUpdateDaoImpl.java
 * @Class Name    : FinBankCardUpdateDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class FinBankCardUpdateDaoImpl extends MyBatisBaseDaoImpl<FinBankCardUpdate, Long> implements FinBankCardUpdateDao {

    /**
     * 条件检索解绑银行卡信息列表
     */
    private static final String FIND_BANK_CARD_UPDATE_LIST = ".findBankCardUpdateList";

    private static final String FIND_BANK_CARD_UPDATE_BY_BANKID_AND_USERID = ".findFinBankCardUpdateByBankIdAndUserId";


    @Override
    public Pager findBankCardUpdateList(FinBankCardUpdate finBankCardUpdate, Pager pager) {
        finBankCardUpdate.setSortColumns("create_time desc");
        return this.findByCondition(finBankCardUpdate, pager, FinBankCardUpdate.class, FIND_BANK_CARD_UPDATE_LIST);
    }

    @Override
    public List<FinBankCardUpdate> findFinBankCardUpdateByBankIdAndUserId(Integer userId, Integer bankCardId) {
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("bankCardId",bankCardId);
        return this.getCurSqlSessionTemplate().selectList(FinBankCardUpdate.class.getName()+FIND_BANK_CARD_UPDATE_BY_BANKID_AND_USERID,params);
    }
}
