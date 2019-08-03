package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointProductImg;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointProductImgDao.java
 * @Class Name    : PointProductImgDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointProductImgDao extends MyBatisBaseDao<PointProductImg, Long> {

    /**
     * 根据图片id知道商品的首图
     * @param productIds
     * @return
     */
    List<PointProductImg> findProductHeadImgsByProductIds(List<Integer> productIds);
}
