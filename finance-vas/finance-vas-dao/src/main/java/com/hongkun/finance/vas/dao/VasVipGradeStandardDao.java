package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.VasVipGradeStandardDao.java
 * @Class Name    : VasVipGradeStandardDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasVipGradeStandardDao extends MyBatisBaseDao<VasVipGradeStandard, Long> {

    /**
     *  @Description    : 根据成长值获取其对应的会员等级
     *  @Method_Name    : findLevelByGrowValue
     *  @param growValue
     *  @return         : Integer
     *  @Creation Date  : 2017年07月05日 上午11:21:05
     *  @Author         : pengwu@hongkun.com.cn
     */
    Integer findLevelByGrowValue(Integer growValue);

    /**
     *  @Description    ：根据等级获取其对应的会员等级标准
     *  @Method_Name    ：findVasVipGradeStandardByLevel
     *  @param level
     *  @return com.hongkun.finance.vas.model.VasVipGradeStandard
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    VasVipGradeStandard findVasVipGradeStandardByLevel(int level);
}
