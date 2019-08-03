package com.hongkun.finance.point.dao.impl;

import com.hongkun.finance.point.model.PointProductImg;
import com.hongkun.finance.point.dao.PointProductImgDao;
import com.yirun.framework.core.annotation.Dao;
import com.yirun.framework.dao.mybatis.impl.MyBatisBaseDaoImpl;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.impl.PointProductImgDaoImpl.java
 * @Class Name    : PointProductImgDaoImpl.java
 * @Description   : GENERATOR DAO实现类
 * @Author        : generator
 */
@Dao
public class PointProductImgDaoImpl extends MyBatisBaseDaoImpl<PointProductImg, Long> implements PointProductImgDao {

    private static final String FIND_PRODUCT_HEAD_IMGS_BY_PRODUCTIDS = ".findProductHeadImgsByProductIds";


    @Override
    public List<PointProductImg> findProductHeadImgsByProductIds(List<Integer> list) {
        return getCurSqlSessionTemplate().selectList(PointProductImg.class.getName()+FIND_PRODUCT_HEAD_IMGS_BY_PRODUCTIDS,list);
    }
}
