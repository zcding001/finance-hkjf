package com.hongkun.finance.contract.dao;

import com.hongkun.finance.contract.model.ConTemplate;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.Map;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.dao.ConTemplateDao.java
 * @Class Name    : ConTemplateDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface ConTemplateDao extends MyBatisBaseDao<ConTemplate, Long> {

    /**
     *  @Description    ：获取当前合同类型正启用的模板
     *  @Method_Name    ：findConTemplateByType
     *  @param contractType  合同类型
     *  @return com.hongkun.finance.contract.model.ConTemplate
     *  @Creation Date  ：2018/4/17
     *  @Author         ：pengwu@hongkun.com.cn
     */
    ConTemplate findConTemplateByType(Integer contractType);

    /**
     *  @Description    ：获取所有已启用的合同模板信息，key为合同类型type，value为ConTemplate合同模板信息
     *  @Method_Name    ：findEnableConTemplateList
     *  @return java.util.Map<java.lang.Integer,com.hongkun.finance.contract.model.ConTemplate>
     *  @Creation Date  ：2018/4/18
     *  @Author         ：pengwu@hongkun.com.cn
     */
    Map<Integer,ConTemplate> findEnableConTemplateList();
}
