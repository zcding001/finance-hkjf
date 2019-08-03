package com.hongkun.finance.fund.dao.impl;

import com.hongkun.finance.fund.model.FundProject;
import com.hongkun.finance.fund.dao.FundProjectDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.impl.FundProjectDaoImpl.java
 * @Class Name    : FundProjectDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class FundProjectDaoImpl extends MyBatisBaseDaoImpl<FundProject, java.lang.Long> implements FundProjectDao {

    /**
     * 条件查询股权项目类型
     */
    private static final String FIND_FUNDTYPE_LIST_WITH_CONDITION = ".findFundTypeListWithCondition";

    @Override
    public Pager findFundTypeListWithCondition(FundProject project, Pager pager) {
        return this.findByCondition(project,pager,FIND_FUNDTYPE_LIST_WITH_CONDITION);
    }
}
