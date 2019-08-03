package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasVipTreatmentDao;
import com.hongkun.finance.vas.model.VasVipTreatment;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Date;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.impl.VasVipTreatmentDaoImpl.java
 * @Class Name    : VasVipTreatmentDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class VasVipTreatmentDaoImpl extends MyBatisBaseDaoImpl<VasVipTreatment, Long> implements VasVipTreatmentDao {

    @Override
    public int findVasVipTreatmentTimeCount(VasVipTreatment vasVipTreatment) {
        return this.getCurSqlSessionTemplate().selectOne(VasVipTreatment.class.getName() +
                ".findVasVipTreatmentTimeCount", vasVipTreatment);
    }

    @Override
    public VasVipTreatment getVipTreatMentByTypeAndRegistTime(int type, Date registTime) {
        VasVipTreatment vipTreatment = new VasVipTreatment();
		vipTreatment.setState(VasConstants.VAS_STATE_Y);
		vipTreatment.setRegistBeginTimeEnd(registTime);
		vipTreatment.setRegistEndTimeBegin(registTime);
        return this.getCurSqlSessionTemplate().selectOne(VasVipTreatment.class.getName() +
                ".getVipTreatMentByTypeAndRegistTime", vipTreatment);
    }
}
