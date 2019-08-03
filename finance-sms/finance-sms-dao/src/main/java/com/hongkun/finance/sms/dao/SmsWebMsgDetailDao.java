package com.hongkun.finance.sms.dao;

import com.hongkun.finance.sms.model.SmsWebMsgDetail;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.user.dao.SmsWebMsgDetailDao.java
 * @Class Name    : SmsWebMsgDetailDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface SmsWebMsgDetailDao extends MyBatisBaseDao<SmsWebMsgDetail, java.lang.Long> {
    /**
    *  @Description    ：查找详情
    *  @Method_Name    ：findSmsWebMsgDetailBySmsWebMsgId
    *  @param id
    *  @return com.hongkun.finance.sms.model.SmsWebMsgDetail
    *  @Creation Date  ：2018/4/17
    *  @Author         ：zhichaoding@hongkun.com.cn
    */
    
    SmsWebMsgDetail findSmsWebMsgDetailBySmsWebMsgId(Integer id);
}
