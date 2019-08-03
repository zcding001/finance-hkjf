package com.hongkun.finance.point.dao;

import com.hongkun.finance.point.model.PointProduct;
import com.hongkun.finance.point.model.query.PointMallQuery;
import com.yirun.framework.core.utils.pager.Pager;
import com.yirun.framework.dao.mybatis.MyBatisBaseDao;

import java.util.List;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.point.dao.PointProductDao.java
 * @Class Name    : PointProductDao.java
 * @Description   : GENERATOR DAO类
 * @Author        : generator
 */
public interface PointProductDao extends MyBatisBaseDao<PointProduct, Long> {

    /**
     * 迁移商品分类
     * @param orginCategoryId
     * @param currentCategoryId
     */
    void moveProductToNewCategory(Integer orginCategoryId, Integer currentCategoryId);

    /**
     * 商品统计类
     * @param leafsCates
     * @return
     */
    Long productsCountFromCategory(List leafsCates);

    /**
     * 查询首页限时抢购栏目商品
     * @param emptyProduct
     * @param pager
     * @return
     */
    Pager findIndexFlashSales(PointMallQuery emptyProduct, Pager pager);

    /**
     * 查询首页热门商品
     * @return
     */
    List<PointProduct> bestSellProducts();

    /**
     * 查询首页推荐商品
     * @return
     */
    List<PointProduct> recommendIndexProducts();

    /**
     * 查询首页其他的产品
     * @return
     */
    List<PointProduct> otherProducts();


    /**
     * 根据条件过滤商品
     * @param pointMallQuery
     * @param pager
     * @return
     */
    Pager filterProduct(PointMallQuery pointMallQuery, Pager pager);

    /**
     * 查询app端首页的商品
     * @param pointMallQuery
     * @param pager
     * @return
     */
    Pager findAppPointMallIndex(PointMallQuery pointMallQuery, Pager pager);
    /**
     *  @Description    : 查询APP端友乾人积分商品
     *  @Method_Name    : recommendIndexProductsForApp;
     *  @return
     *  @return         : List<PointProduct>;
     *  @Creation Date  : 2018年9月28日 下午4:34:29;
     *  @Author         : yanbinghuang@hongkun.com.cn 黄艳兵;
     */
    List<PointProduct>  recommendIndexProductsForApp();
}
