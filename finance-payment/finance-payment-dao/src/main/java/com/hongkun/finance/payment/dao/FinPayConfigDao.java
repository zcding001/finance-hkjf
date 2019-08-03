package com.hongkun.finance.payment.dao;

import com.hongkun.finance.payment.model.FinPayConfig;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

/**
 * @Project : finance
 * @Program Name : com.hongkun.finance.payment.dao.FinPayConfigDao.java
 * @Class Name : FinPayConfigDao.java
 * @Description : GENERATOR DAO类
 * @Author : generator
 */
public interface FinPayConfigDao extends MyBatisBaseDao<FinPayConfig, java.lang.Long> {
    /**
     * @Description :
     * @Method_Name : findPayConfigInfo;
     * @param systemTypeName 系统名称
     * @param platformSourceName 平台名称
     * @param payChannel 支付渠道
     * @param payStyle 支付方式
     * @return
     * @return : FinPayConfig;
     * @Creation Date : 2017年6月15日 上午9:57:31;
     * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    FinPayConfig findPayConfigInfo(String key, String systemTypeName, String platformSourceName,
            String payChannel, String payStyle);
}
