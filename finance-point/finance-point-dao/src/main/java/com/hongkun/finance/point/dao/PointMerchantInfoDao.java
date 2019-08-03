package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointMerchantInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointMerchantInfoDao.java
 * @Class Name    : PointMerchantInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointMerchantInfoDao extends MyBatisBaseDao<PointMerchantInfo, Long> {


    /**
     * @param regUserId    用户id
     * @return : Map<String,Object>
     * @Description : 获取商户状态为1-待审核，3-审核成功的商户信息
     * @Method_Name : getPointMerchantInfoByUserId
     * @Creation Date  : 2018年03月16日 上午09:41:55
     * @Author : pengwu@hongkun.com.cn
     */
    List<PointMerchantInfo> getPointMerchantInfoByUserId(Integer regUserId);

    /**
     * @param pointMerchantInfo
     * @Description : 根据条件查询商户id集合
     * @Creation Date  : 2018年8月7日16:24:49
     * @return
     */
    List<Integer> findMerchantIdsByInfo(PointMerchantInfo pointMerchantInfo);
}
