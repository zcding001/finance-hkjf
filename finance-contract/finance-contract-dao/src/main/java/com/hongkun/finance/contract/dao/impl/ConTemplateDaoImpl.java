package com.hongkun.finance.contract.dao.impl;

import com.hongkun.finance.contract.model.ConTemplate;
import com.hongkun.finance.contract.dao.ConTemplateDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.contract.dao.impl.ConTemplateDaoImpl.java
 * @Class Name    : ConTemplateDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class ConTemplateDaoImpl extends MyBatisBaseDaoImpl<ConTemplate, Long> implements ConTemplateDao {

    @Override
    public ConTemplate findConTemplateByType(Integer contractType) {
        return this.getCurSqlSessionTemplate().selectOne(ConTemplate.class.getName() + ".findConTemplateByType", contractType);
    }

    @Override
    public Map<Integer, ConTemplate> findEnableConTemplateList() {
        return this.getCurSqlSessionTemplate().selectMap(ConTemplate.class.getName() + ".findEnableConTemplateList",
                "contractType");
    }
}
