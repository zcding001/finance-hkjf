package com.hongkun.finance.property.dao.impl;

import com.hongkun.finance.property.model.ProPayRecord;
import com.hongkun.finance.property.dao.ProPayRecordDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.property.dao.impl.ProPayRecordDaoImpl.java
 * @Class Name    : ProPayRecordDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class ProPayRecordDaoImpl extends MyBatisBaseDaoImpl<ProPayRecord, java.lang.Long> implements ProPayRecordDao {

    @Override
    public Pager findProPayRecordVoList(ProPayRecord proPayRecordVo, Pager pager) {
        return this.findByCondition(proPayRecordVo,pager,ProPayRecord.class,".findProPayRecordVoList");
    }

    @Override
    public Integer updateState(Map<String,Object> params) {
        return super.getCurSqlSessionTemplate().update(ProPayRecord.class.getName()+".updateState",params);
    }
}
