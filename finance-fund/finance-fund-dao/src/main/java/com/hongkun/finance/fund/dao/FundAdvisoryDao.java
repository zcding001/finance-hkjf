package com.hongkun.finance.fund.dao;

import com.hongkun.finance.fund.model.FundAdvisory;
import com.hongkun.finance.fund.model.vo.FundAdvisoryVo;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.fund.dao.FundAdvisoryDao.java
 * @Class Name    : FundAdvisoryDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface FundAdvisoryDao extends MyBatisBaseDao<FundAdvisory, java.lang.Long> {

    /**
     *  @Description    ：条件查询股权咨询信息列表
     *  @Method_Name    ：findFundAdvisoryListWithCondition
     *  @param advisoryVo
     *  @param pager
     *  @return com.yirun.framework.core.utils.pager.Pager
     *  @Creation Date  ：2018年04月28日 14:16
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    Pager findFundAdvisoryListWithCondition(FundAdvisoryVo advisoryVo, Pager pager);

    /**
     *  @Description    ：查询用户当日预约次数
     *  @Method_Name    ：findAdvisoryCount
     *  @param params
     *  @return int
     *  @Creation Date  ：2018年05月07日 16:19
     *  @Author         ：yunlongliu@hongkun.com.cn
     */
    int findAdvisoryCount(Map<String, Object> params);
}
