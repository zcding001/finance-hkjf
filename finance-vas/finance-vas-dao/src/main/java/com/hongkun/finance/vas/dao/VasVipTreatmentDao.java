package com.hongkun.finance.vas.dao;

import com.hongkun.finance.vas.model.VasVipTreatment;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Date;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.VasVipTreatmentDao.java
 * @Class Name    : VasVipTreatmentDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface VasVipTreatmentDao extends MyBatisBaseDao<VasVipTreatment, Long> {

    /**
     *  @Description    : 获取会员待遇适用用户注册时间段有交集的规则
     *  @Method_Name    : findVasVipTreatmentTimeCount
     *  @param vasVipTreatment
     *  @return         : int
     *  @Creation Date  : 2018年01月30日 下午14:28:50
     *  @Author         : pengwu@hongkun.com.cn
     */
    int findVasVipTreatmentTimeCount(VasVipTreatment vasVipTreatment);


    VasVipTreatment getVipTreatMentByTypeAndRegistTime(int type, Date registTime);
}
