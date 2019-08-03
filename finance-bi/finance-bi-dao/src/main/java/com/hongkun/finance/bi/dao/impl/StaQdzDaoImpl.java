package com.hongkun.finance.bi.dao.impl;

import com.hongkun.finance.bi.model.StaQdz;
import com.hongkun.finance.bi.model.vo.StaQdzInOutVo;
import com.hongkun.finance.bi.model.vo.StaWithdrawVo;
import com.hongkun.finance.bi.dao.StaQdzDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.dao.impl.StaQdzDaoImpl.java
 * @Class Name    : StaQdzDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class StaQdzDaoImpl extends MyBatisBaseDaoImpl<StaQdz, java.lang.Long> implements StaQdzDao {

    @Override
    public StaQdzInOutVo findStaQdzSum(StaQdz staQdz) {
        return  super.getCurSqlSessionTemplate().selectOne(StaQdz.class.getName()+".findStaQdzSum",staQdz);
    }

}
