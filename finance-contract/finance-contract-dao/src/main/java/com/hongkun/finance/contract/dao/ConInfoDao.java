package com.hongkun.finance.contract.dao;

import com.hongkun.finance.contract.model.ConInfo;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.dao.ConInfoDao.java
 * @Class Name    : ConInfoDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface ConInfoDao extends MyBatisBaseDao<ConInfo, Long> {

    /**
     *  @Description    ：根据条件获取单条合同信息
     *  @Method_Name    ：findConInfo
     *  @param conInfo  查询条件
     *  @return java.util.List<com.hongkun.finance.contract.model.ConInfo>
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    ConInfo findConInfo(ConInfo conInfo);

    /**
     *  @Description    ：根据投资记录id集合获取相关合同信息
     *  @Method_Name    ：findConInfoListByInvestIds
     *  @param listInvestIds 投资记录id集合
     *  @return java.util.List<com.hongkun.finance.contract.model.ConInfo>
     *  @Creation Date  ：2018/6/1
     *  @Author         ：pengwu@hongkun.com.cn
     */
    List<ConInfo> findConInfoListByInvestIds(List<Integer> listInvestIds);
}
