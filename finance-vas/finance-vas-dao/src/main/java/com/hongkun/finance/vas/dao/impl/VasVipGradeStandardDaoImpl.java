package com.hongkun.finance.vas.dao.impl;

import com.hongkun.finance.vas.constants.VasConstants;
import com.hongkun.finance.vas.dao.VasVipGradeStandardDao;
import com.hongkun.finance.vas.model.VasVipGradeStandard;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.vas.dao.impl.VasVipGradeStandardDaoImpl.java
 * @Class Name    : VasVipGradeStandardDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class VasVipGradeStandardDaoImpl extends MyBatisBaseDaoImpl<VasVipGradeStandard, Long> implements VasVipGradeStandardDao {

    @Override
    public Integer findLevelByGrowValue(Integer growValue) {
        //获取最高等级成长值范围
        VasVipGradeStandard condition = new VasVipGradeStandard();
        condition.setState(VasConstants.VAS_STATE_Y);
        condition.setSortColumns("`level` DESC");
        List<VasVipGradeStandard> list = this.findByCondition(condition);
        if (list != null && list.size() > 0){
            //如果成长值大于等于最大等级的最低成长值，则会员等级为最高等级
            int growthMin = list.get(0).getGrowthValMin();
            if (growValue >= growthMin){
                return list.get(0).getLevel();
            }
            //遍历获取该成长值的等级
            for (VasVipGradeStandard vipGradeStandard:list) {
                if (growValue >= vipGradeStandard.getGrowthValMin() && growValue < vipGradeStandard.getGrowthValMax()){
                    return vipGradeStandard.getLevel();
                }
            }
        }
        return 0;
    }

    @Override
    public VasVipGradeStandard findVasVipGradeStandardByLevel(int level) {
        return this.getCurSqlSessionTemplate().selectOne(VasVipGradeStandard.class.getName() +
                ".findVasVipGradeStandardByLevel", level);
    }
}
