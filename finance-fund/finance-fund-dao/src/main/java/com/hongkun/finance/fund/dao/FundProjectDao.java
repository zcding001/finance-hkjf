package com.hongkun.finance.fund.dao;

import com.hongkun.finance.fund.model.FundProject;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.FundProjectDao.java
 * @Class Name    : FundProjectDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface FundProjectDao extends MyBatisBaseDao<FundProject, java.lang.Long> {

    /**
     *  @Description    ：条件检索股权类型信息列表
     *  @Method_Name    ：findFundTypeListWithCondition
     *  @param project
     *  @param pager
     *  @return com.yirun.framework.core.utils.pager.Pager
     *  @Creation Date  ：2018年04月28日 09:55
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    Pager findFundTypeListWithCondition(FundProject project, Pager pager);
}
