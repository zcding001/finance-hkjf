package com.hongkun.finance.fund.dao.impl;

import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.dao.FundAdvisoryDao;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.impl.FundAdvisoryDaoImpl.java
 * @Class Name    : FundAdvisoryDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class FundAdvisoryDaoImpl extends MyBatisBaseDaoImpl<FundAdvisory, java.lang.Long> implements FundAdvisoryDao {

    /**
     * 条件查询股权咨询列表
     */
    private static final String FIND_FUND_ADVISORY_LIST_WITH_CONDITION = ".findFundAdvisoryListWithCondition";

    /**
     * 查询用户当天预约次数
     */
    private static final String FIND_ADVISORY_COUNT = ".findAdvisoryCount";

    @Override
    public Pager findFundAdvisoryListWithCondition(FundAdvisoryVo advisoryVo, Pager pager) {
        return this.findByCondition(advisoryVo, pager, FundAdvisory.class, FIND_FUND_ADVISORY_LIST_WITH_CONDITION);
    }

    @Override
    public int findAdvisoryCount(Map<String, Object> params) {
        return this.getCurSqlSessionTemplate().selectOne(FundAdvisory.class.getName() + FIND_ADVISORY_COUNT,params);
    }
}
