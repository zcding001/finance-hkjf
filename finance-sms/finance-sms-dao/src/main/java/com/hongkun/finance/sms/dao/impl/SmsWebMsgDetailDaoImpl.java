package com.hongkun.finance.sms.dao.impl;

import com.hongkun.finance.sms.model.SmsWebMsgDetail;
import com.hongkun.finance.sms.dao.SmsWebMsgDetailDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.impl.SmsWebMsgDetailDaoImpl.java
 * @Class Name    : SmsWebMsgDetailDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class SmsWebMsgDetailDaoImpl extends MyBatisBaseDaoImpl<SmsWebMsgDetail, java.lang.Long> implements SmsWebMsgDetailDao {
    
    @Override
    public SmsWebMsgDetail findSmsWebMsgDetailBySmsWebMsgId(Integer id) {
        return getCurSqlSessionTemplate().selectOne(SmsWebMsgDetail.class.getName() + ".findBySmsWebMsgId", id);
    }
}
